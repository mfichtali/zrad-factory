package ma.zrad.system.metrics.batch.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.domain.EventBusinessDomain;
import ma.zrad.system.batch.common.enums.EventBatchContextEnum;
import ma.zrad.system.batch.common.enums.EventBatchStatusEnum;
import ma.zrad.system.batch.common.enums.EventBusinessTypeEnum;
import ma.zrad.system.batch.common.enums.EventProcessingStatusEnum;
import ma.zrad.system.batch.common.utils.BatchConstantUtils;
import ma.zrad.system.batch.common.utils.BatchThreadContext;
import ma.zrad.system.metrics.batch.domain.port.in.EventBusinessService;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchTripMetricsListener implements JobExecutionListener {

    private final EventBusinessService eventBusinessService;

    @Override
    public void beforeJob(final JobExecution jobExecution) {
        ExecutionContext context = jobExecution.getExecutionContext();
        var eventCtx = EventBusinessDomain.buildStartedEvent(EventBatchContextEnum.ZRAD04);
        eventCtx.setEventBusinessTypeEnum(EventBusinessTypeEnum.VEHICLE_TRIP_METRICS);
        eventCtx.setJobExecutionId(jobExecution.getJobInstance().getInstanceId());
        eventCtx.setJobName(jobExecution.getJobInstance().getJobName());
        var eventBusinessCtx = eventBusinessService.createEventBusiness(eventCtx);
        context.put(BatchConstantUtils.BATCH_CTX_TRIP_EVENT_ID, eventBusinessCtx.getId());
    }

    @Override
    public void afterJob(final JobExecution jobExecution) {
        ExecutionContext context = jobExecution.getExecutionContext();
        var eventCtxIdentifier = (UUID) context.get(BatchConstantUtils.BATCH_CTX_TRIP_EVENT_ID);
        var freshEventCtx = eventBusinessService.findByIdentifier(eventCtxIdentifier);
        freshEventCtx.setEndBatchEvent(LocalDateTime.now());
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            boolean noDataProcessed = jobExecution.getStepExecutions().stream()
                    .allMatch(step -> step.getReadCount() == 0);
            if (noDataProcessed) {
                log.info("Job COMPLETED with NO DATA processed.");
                freshEventCtx.setEventBatchStatus(EventBatchStatusEnum.NO_DATA);
                freshEventCtx.setEventProcessingStatus(EventProcessingStatusEnum.NO_DATA);
            } else {
                log.info("Job COMPLETED with data processed.");
                freshEventCtx.setEventBatchStatus(EventBatchStatusEnum.COMPLETED);
                freshEventCtx.setEventProcessingStatus(EventProcessingStatusEnum.COMPLETED);
            }
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
    }
}
