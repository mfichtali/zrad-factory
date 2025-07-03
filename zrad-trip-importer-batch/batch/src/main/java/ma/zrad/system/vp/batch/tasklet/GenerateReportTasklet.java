package ma.zrad.system.vp.batch.tasklet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.enums.EventBatchContextEnum;
import ma.zrad.system.batch.common.pojo.BatchFileContext;
import ma.zrad.system.batch.common.records.FileMetadataRecord;
import ma.zrad.system.batch.common.utils.BatchConstantUtils;
import ma.zrad.system.batch.common.utils.DateUtils;
import ma.zrad.system.vp.batch.config.AppLinkerProperties;
import ma.zrad.system.vp.batch.domain.port.in.EventBusinessService;
import ma.zrad.system.vp.batch.domain.port.out.FileImporterMetricsRepositoryPort;
import org.apache.commons.lang.StringUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenerateReportTasklet implements Tasklet {

    private final ObjectMapper objectMapper;
    private final AppLinkerProperties appLinkerProperties;
    private final EventBusinessService eventBusinessService;
    private final FileImporterMetricsRepositoryPort fileImporterMetricsRepositoryPort;

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
        ExecutionContext jobContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
        var eventCtxIdentifier = (UUID) jobContext.get(BatchConstantUtils.BATCH_CTX_VPI_EVENT_ID);
        var fileImporterDomain = fileImporterMetricsRepositoryPort.findByEventId(eventCtxIdentifier);
        if(fileImporterDomain != null) {
            ObjectNode report = objectMapper.createObjectNode();
            report.put("jobName", fileImporterDomain.getJobName());
            report.put("origin_filename", fileImporterDomain.getOriginFilename());
            report.put("execution_start", DateUtils.toStringFormat(fileImporterDomain.getJobExecutionStartTime(), DateUtils.DEFAULT_DATE_PATTERN));
            report.put("execution_end", DateUtils.toStringFormat(fileImporterDomain.getJobExecutionEndTime(), DateUtils.DEFAULT_DATE_PATTERN));
            report.put("duration", DateUtils.formatDuration(fileImporterDomain.getJobExecutionStartTime(), fileImporterDomain.getJobExecutionEndTime()));
            report.put("total_lines", fileImporterDomain.getTotalRead());
            report.put("total_valid_lines", fileImporterDomain.getTotalValid());
            if(fileImporterDomain.getTotalValid() > 0) {
                report.put("valid_percentage", String.format("%.2f%%", (fileImporterDomain.getTotalValid() * 100.0) / fileImporterDomain.getTotalRead()));
            } else {
                report.put("valid_percentage", "0.00%");
            }
            report.put("total_invalid_lines", fileImporterDomain.getTotalInvalid());
            if(fileImporterDomain.getTotalInvalid() > 0) {
                report.put("invalid_percentage", String.format("%.2f%%", (fileImporterDomain.getTotalInvalid() * 100.0) / fileImporterDomain.getTotalRead()));
            } else {
                report.put("invalid_percentage", "0.00%");
            }
            if(StringUtils.isNotBlank(fileImporterDomain.getIncidentFilename())) {
                report.put("incident_filename", fileImporterDomain.getIncidentFilename());
            }
            var fileMetadataCtx = (FileMetadataRecord) jobContext.get(BatchConstantUtils.BATCH_FILE_METADATA_CTX);
            var batchFileCtx = (BatchFileContext) jobContext.get(BatchConstantUtils.BATCH_ROOT_FILE_CTX);
            if(batchFileCtx != null && fileMetadataCtx != null) {
                Path reportPath = Paths.get(batchFileCtx.getReportDir()).resolve(
                        String.format(appLinkerProperties.getBatchFileReportFilename(),
                                EventBatchContextEnum.ZRAD01.getValue(),
                                fileMetadataCtx.regionCode(),
                                fileMetadataCtx.sectionRoadCode(),
                                DateUtils.toStringFormat(fileMetadataCtx.dateDetection()),
                                fileMetadataCtx.sequence()));
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(reportPath.toFile(), report);
            }

            var freshEventCtx = eventBusinessService.findByIdentifier(eventCtxIdentifier);
            if (freshEventCtx != null) {
                freshEventCtx.setMetadata(objectMapper.writeValueAsString(report));
                eventBusinessService.updateEventBusiness(freshEventCtx);
            }
        }
        return RepeatStatus.FINISHED;
    }
}
