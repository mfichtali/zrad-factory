package ma.zrad.system.trip.batch.tasklet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.exceptions.BusinessException;
import ma.zrad.system.batch.common.utils.BatchConstantUtils;
import ma.zrad.system.batch.common.utils.BatchServiceUtils;
import ma.zrad.system.trip.batch.service.ReferentialRegionLoaderApiService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
public class InitTripAnalysisTasklet extends AbstractZradTasklet implements StepExecutionListener {

    private final ReferentialRegionLoaderApiService referentialApiService;

    @Override
    public void beforeStep(final StepExecution stepExecution) {
        log.info("📌 Init T.A Tasklet - beforeStep");
    }

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
        log.info("🚀 Initializing T.A tasklet, execution started ...");
        StepExecution stepExecution = getStepExecutionContext(chunkContext);
        var regionsDetails = referentialApiService.getRegionDetails();
        if (CollectionUtils.isEmpty(regionsDetails)) {
            log.warn("🚨 Region details are not available in the referential API");
            throw new BusinessException("Region details are not available in the referential API");
        }

        var regionCode = stepExecution.getJobParameters().getString(BatchConstantUtils.PARAM_REGION_CODE);
        if (StringUtils.isBlank(regionCode)) {
            throw new BusinessException("Region code is required to start the trip analysis");
        }

        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(final StepExecution stepExecution) {
        log.info("📌 Init T.A Tasklet - afterStep");
        if (stepExecution.getExitStatus().getExitCode().equals(ExitStatus.FAILED.getExitCode())) {
            return BatchServiceUtils.determineFailureExitStatus(stepExecution);
        }
        return stepExecution.getExitStatus();
    }
}
