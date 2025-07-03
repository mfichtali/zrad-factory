package ma.zrad.system.vp.batch.listener;

import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.pojo.BatchFileContext;
import ma.zrad.system.batch.common.records.FileMetadataRecord;
import ma.zrad.system.batch.common.utils.BatchConstantUtils;
import ma.zrad.system.batch.common.utils.BatchServiceUtils;
import ma.zrad.system.vp.batch.domain.model.FileImporterMetricsDomain;
import ma.zrad.system.vp.batch.domain.port.in.FileImporterService;
import ma.zrad.system.vp.batch.domain.port.out.FileImporterMetricsRepositoryPort;
import ma.zrad.system.vp.batch.service.ImporterStatisticsCollectorService;
import ma.zrad.system.vp.batch.service.IncidentFileMergerService;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
public class FileImporterMetricsStepListener implements StepExecutionListener {

    private final ImporterStatisticsCollectorService statisticsCollector;
    private final IncidentFileMergerService mergerService;
    private final FileImporterMetricsRepositoryPort fileImporterMetricsRepositoryPort;
    private final FileImporterService fileImporterService;

    private BatchFileContext batchFileCtx;
    private FileMetadataRecord fileMetadataCtx;
    private UUID currentEventId;

    FileImporterMetricsStepListener(
            ImporterStatisticsCollectorService statisticsCollector,
            IncidentFileMergerService mergerService,
            FileImporterMetricsRepositoryPort fileImporterMetricsRepositoryPort,
            @Qualifier("ftpFileImporterService") FileImporterService fileImporterService) {
        this.statisticsCollector = statisticsCollector;
        this.mergerService = mergerService;
        this.fileImporterMetricsRepositoryPort = fileImporterMetricsRepositoryPort;
        this.fileImporterService = fileImporterService;
    }

    @Override
    public void beforeStep(final StepExecution stepExecution) {
        ExecutionContext jobContext = stepExecution.getJobExecution().getExecutionContext();
        this.currentEventId = (UUID) jobContext.get(BatchConstantUtils.BATCH_CTX_VPI_EVENT_ID);
        this.batchFileCtx = (BatchFileContext) jobContext.get(BatchConstantUtils.BATCH_ROOT_FILE_CTX);
        this.fileMetadataCtx = (FileMetadataRecord) jobContext.get(BatchConstantUtils.BATCH_FILE_METADATA_CTX);
        log.info("Context loaded: batchFileCtx={}, fileMetadataCtx={}", batchFileCtx, fileMetadataCtx);
        statisticsCollector.reset();
    }

    @Override
    public ExitStatus afterStep(final StepExecution stepExecution) {

        if (stepExecution.getExitStatus().getExitCode().equals(ExitStatus.FAILED.getExitCode())) {
            return BatchServiceUtils.determineFailureExitStatus(stepExecution);
        }
        Path outPutFile = null;
        if (statisticsCollector.getInvalid().get() > 0) {
            int maxLength = statisticsCollector.getMaxLengthErrorLine().get() + 10; // Add some padding to the max length
            try {
                outPutFile = mergerService.mergeIncidentFiles(batchFileCtx.getOutputDir(), fileMetadataCtx, maxLength);
            } catch (IOException e) {
                log.warn("Error merging incident files: {}", e.getMessage(), e);
            }
        }
        FileImporterMetricsDomain fileImportMetrics = new FileImporterMetricsDomain()
                .setEventId(this.currentEventId)
                .setSourceFilename(fileImporterService.sourceImportFile())
                .setOriginFilename(this.fileMetadataCtx.fileOriginalName())
                .setFileImportDate(this.fileMetadataCtx.dateDetection())
                .setJobId(stepExecution.getJobExecution().getJobId())
                .setJobName(stepExecution.getJobExecution().getJobInstance().getJobName())
                .setStepName(stepExecution.getStepName())
                .setJobExecutionStartTime(stepExecution.getJobExecution().getStartTime())
                .setJobExecutionEndTime(LocalDateTime.now())
                .setTotalRead(statisticsCollector.getTotal().get())
                .setTotalValid(statisticsCollector.getValid().get())
                .setTotalInvalid(statisticsCollector.getInvalid().get())
                .setIncidentFilename(outPutFile != null ? outPutFile.getFileName().toString() : null)
                .setSequenceFile(this.fileMetadataCtx.sequence());
        fileImporterMetricsRepositoryPort.save(fileImportMetrics);
        return ExitStatus.COMPLETED;
    }

}
