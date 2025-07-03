package ma.zrad.system.stats.batch.tasklet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.enums.EventProcessingStatusEnum;
import ma.zrad.system.batch.common.utils.BatchConstantUtils;
import ma.zrad.system.batch.common.utils.BatchServiceUtils;
import ma.zrad.system.stats.batch.domain.port.in.EventBusinessService;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateEventStatusTasklet extends AbstractZradTasklet implements StepExecutionListener {

    private List<UUID> eventZrad02Ids;

    private final EventBusinessService eventBusinessService;

    @Override
    public void beforeStep(final StepExecution stepExecution) {
        log.info("📌 Update E.S Tasklet - beforeStep");
        ExecutionContext jobCtx = stepExecution.getJobExecution().getExecutionContext();
        if (jobCtx.containsKey(BatchConstantUtils.BATCH_ALL_EVENT_ZRAD02_IDS)) {
            eventZrad02Ids = (List<UUID>) jobCtx.get(BatchConstantUtils.BATCH_ALL_EVENT_ZRAD02_IDS);
        } else {
            eventZrad02Ids = (List<UUID>) jobCtx.get(BatchConstantUtils.BATCH_CTX_EVENT_ZRAD02_IDS);
        }
    }

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
        eventBusinessService.updateEventStatus(eventZrad02Ids, EventProcessingStatusEnum.PROCESSED);
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

}