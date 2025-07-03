package ma.zrad.system.vp.batch.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.domain.EventBusinessDomain;
import ma.zrad.system.batch.common.enums.EventBatchContextEnum;
import ma.zrad.system.batch.common.enums.EventBatchStatusEnum;
import ma.zrad.system.batch.common.enums.EventBusinessTypeEnum;
import ma.zrad.system.batch.common.enums.EventProcessingStatusEnum;
import ma.zrad.system.batch.common.pojo.BatchFileContext;
import ma.zrad.system.batch.common.utils.BatchConstantUtils;
import ma.zrad.system.batch.common.utils.BatchThreadContext;
import ma.zrad.system.vp.batch.domain.port.in.EventBusinessService;
import ma.zrad.system.vp.batch.domain.port.in.FileImporterService;
import ma.zrad.system.vp.batch.domain.port.out.FileImporterMetricsRepositoryPort;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
//@RequiredArgsConstructor
public class BatchVPImporterListener implements JobExecutionListener {

    private final ObjectMapper objectMapper;
    private final EventBusinessService eventBusinessService;
    private final FileImporterService fileImporterService;
    private final FileImporterMetricsRepositoryPort fileImporterMetricsRepositoryPort;

    public BatchVPImporterListener(
            ObjectMapper objectMapper,
            EventBusinessService eventBusinessService,
            @Qualifier("ftpFileImporterService") FileImporterService fileImporterService,
            FileImporterMetricsRepositoryPort fileImporterMetricsRepositoryPort) {
        this.objectMapper = objectMapper;
        this.eventBusinessService = eventBusinessService;
        this.fileImporterService = fileImporterService;
        this.fileImporterMetricsRepositoryPort = fileImporterMetricsRepositoryPort;
    }

    @Override
    public void beforeJob(final JobExecution jobExecution) {
        ExecutionContext context = jobExecution.getExecutionContext();
        var eventCtx = EventBusinessDomain.buildStartedEvent(EventBatchContextEnum.ZRAD01);
        eventCtx.setEventBusinessTypeEnum(EventBusinessTypeEnum.VEHICLE_PASSAGE_IMPORT);
        eventCtx.setJobExecutionId(jobExecution.getJobInstance().getInstanceId());
        eventCtx.setJobName(jobExecution.getJobInstance().getJobName());
        var eventBusinessCtx = eventBusinessService.createEventBusiness(eventCtx);
        context.put(BatchConstantUtils.BATCH_CTX_VPI_EVENT_ID, eventBusinessCtx.getId());
    }

    @Override
    public void afterJob(final JobExecution jobExecution) {
        ExecutionContext context = jobExecution.getExecutionContext();
        var eventCtxIdentifier = (UUID) context.get(BatchConstantUtils.BATCH_CTX_VPI_EVENT_ID);
        var freshEventCtx = eventBusinessService.findByIdentifier(eventCtxIdentifier);
        freshEventCtx.setEndBatchEvent(LocalDateTime.now());
        boolean postImport = false;
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            boolean noDataProcessed = jobExecution.getStepExecutions().stream()
                    .allMatch(step -> step.getReadCount() == 0);

            var metadata = freshEventCtx.getMetadata();
            int totalValidLine = getTotalValidLine(metadata);
            if (noDataProcessed || totalValidLine == 0) {
                log.info("Job COMPLETED with NO DATA processed.");
                freshEventCtx.setEventBatchStatus(EventBatchStatusEnum.NO_DATA);
                freshEventCtx.setEventProcessingStatus(EventProcessingStatusEnum.NO_DATA);
            } else {
                log.info("Job COMPLETED with data processed.");
                freshEventCtx.setEventBatchStatus(EventBatchStatusEnum.COMPLETED);
            }
            postImport = true;
        } else {
            log.info("Job FAILED with status: {}", jobExecution.getStatus());
            if(jobExecution.getStatus() == BatchStatus.FAILED) {
                String errorMessage = (String) BatchThreadContext.get(BatchConstantUtils.BATCH_ERROR_MESSAGE_CTX);
                freshEventCtx.setEventBatchStatus(EventBatchStatusEnum.FAILED);
                freshEventCtx.setEventProcessingStatus(EventProcessingStatusEnum.FAILED);
                freshEventCtx.setOccurredErrorMessage(errorMessage);
                if (errorMessage == null) {
                    jobExecution.getAllFailureExceptions().stream().findFirst()
                            .ifPresent(err -> freshEventCtx.setOccurredErrorMessage(err.getMessage()));
                }
            }
        }
        eventBusinessService.updateEventBusiness(freshEventCtx);

        // Clean context thread-local
        BatchThreadContext.clear();

        // Cleanup batch file context
        var fileMetricData = fileImporterMetricsRepositoryPort.findByEventId(eventCtxIdentifier);
        var batchFileCtx = (BatchFileContext) context.get(BatchConstantUtils.BATCH_ROOT_FILE_CTX);
        if (fileMetricData != null && postImport) {
            fileImporterService.postImportCleanup(batchFileCtx, fileMetricData);
            assert batchFileCtx != null;
            batchFileCtx.cleanup();
        }
    }

    private int getTotalValidLine(final String metadata) {
        try {
            JsonNode rootNode = objectMapper.readTree(metadata);
            return rootNode.get("total_valid_lines").asInt();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
