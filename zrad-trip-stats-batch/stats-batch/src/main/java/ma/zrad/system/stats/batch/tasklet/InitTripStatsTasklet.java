package ma.zrad.system.stats.batch.tasklet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.exceptions.BusinessException;
import ma.zrad.system.batch.common.utils.BatchServiceUtils;
import ma.zrad.system.stats.batch.service.ReferentialRegionLoaderApiService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitTripStatsTasklet extends AbstractZradTasklet implements StepExecutionListener {

    private final ReferentialRegionLoaderApiService referentialApiService;
    
    @Override
    public void beforeStep(final StepExecution stepExecution) {
        log.info("📌 Init T.S Tasklet - beforeStep");
    }

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) {
        log.info("🚀 Initializing T.S tasklet, execution started ...");
        var regionsDetails = referentialApiService.getRegionDetails();
        if (CollectionUtils.isEmpty(regionsDetails)) {
            log.warn("🚨 Region details are not available in the referential API");
            throw new BusinessException("Region details are not available in the referential API");
        }
        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(final StepExecution stepExecution) {
        log.info("📌 Init T.S Tasklet - afterStep");
        if (stepExecution.getExitStatus().getExitCode().equals(ExitStatus.FAILED.getExitCode())) {
            return BatchServiceUtils.determineFailureExitStatus(stepExecution);
        }
        return ExitStatus.COMPLETED;
    }
}
