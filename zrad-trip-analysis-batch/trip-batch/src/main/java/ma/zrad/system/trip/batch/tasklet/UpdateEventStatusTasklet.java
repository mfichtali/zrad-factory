package ma.zrad.system.trip.batch.tasklet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.enums.EventProcessingStatusEnum;
import ma.zrad.system.batch.common.projection.EventRemainingCounterProjection;
import ma.zrad.system.batch.common.utils.BatchConstantUtils;
import ma.zrad.system.batch.common.utils.BatchServiceUtils;
import ma.zrad.system.trip.batch.domain.port.in.EventBusinessService;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateEventStatusTasklet extends AbstractZradTasklet implements StepExecutionListener {

    private ExecutionContext context;
    private List<UUID> eventZrad01Ids;

    private final EventBusinessService eventBusinessService;

    @Override
    public void beforeStep(final StepExecution stepExecution) {
        log.info("📌 Update E.S Tasklet - beforeStep");
        context = stepExecution.getJobExecution().getExecutionContext();
        eventZrad01Ids = (List<UUID>) context.get(BatchConstantUtils.BATCH_CTX_EVENT_ZRAD01_IDS);
    }

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {

        var regionCode = getJobParam(chunkContext, BatchConstantUtils.PARAM_REGION_CODE);
        var pendingEvents = eventBusinessService.findRemainingPendingVehicle(eventZrad01Ids, regionCode);

        Map<Boolean, List<UUID>> partitionedIds = pendingEvents.stream()
                .collect(Collectors.partitioningBy(
                        event -> event.getRemainingPendingCounter() > 0,
                        Collectors.mapping(EventRemainingCounterProjection::getEventId, Collectors.toList())
                ));

        List<UUID> inProgressIds = partitionedIds.get(true);
        List<UUID> processedIds = partitionedIds.get(false);

        if (!processedIds.isEmpty()) {
            eventBusinessService.updateEventStatus(processedIds, EventProcessingStatusEnum.PROCESSED);
        }

        if (!inProgressIds.isEmpty()) {
            eventBusinessService.updateEventStatus(inProgressIds, EventProcessingStatusEnum.IN_PROGRESS);
        }
        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(final StepExecution stepExecution) {
        log.info("📌 Init E.S Tasklet - afterStep");
        if (stepExecution.getExitStatus().getExitCode().equals(ExitStatus.FAILED.getExitCode())) {
            return BatchServiceUtils.determineFailureExitStatus(stepExecution);
        }
        return stepExecution.getExitStatus();
    }

    private String getJobParam(ChunkContext context, String paramName) {
        return (String) context.getStepContext().getJobParameters().get(paramName);
    }
}
