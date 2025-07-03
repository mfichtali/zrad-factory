package ma.zrad.system.metrics.batch.tasklet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.enums.ProcessingGranularityEnum;
import ma.zrad.system.batch.common.exceptions.BusinessException;
import ma.zrad.system.batch.common.utils.BatchConstantUtils;
import ma.zrad.system.batch.common.utils.BatchServiceUtils;
import ma.zrad.system.metrics.batch.service.ReferentialRegionLoaderApiService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;

import static ma.zrad.system.batch.common.utils.DateUtils.parseDailyDate;
import static ma.zrad.system.batch.common.utils.DateUtils.parseMonthlyDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitTripMetricsTasklet extends AbstractZradTasklet implements StepExecutionListener {

    private final ReferentialRegionLoaderApiService referentialApiService;
    
    @Override
    public void beforeStep(final StepExecution stepExecution) {
        log.info("📌 Init T.M Tasklet - beforeStep");
    }

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) {
        log.info("🚀 Initializing T.M tasklet, execution started ...");
        StepExecution stepExecution = getStepExecutionContext(chunkContext);
        var jobContext = getJobExecutionContext(chunkContext);
        var regionsDetails = referentialApiService.getRegionDetails();
        if (CollectionUtils.isEmpty(regionsDetails)) {
            log.warn("🚨 Region details are not available in the referential API");
            throw new BusinessException("Region details are not available in the referential API");
        }
        
        // Validation of input params
        var processingGranularity = validateAndParseGranularity(stepExecution);
        var processingDate = validateProcessingDate(stepExecution);

        // Parsing et logging
        switch (processingGranularity) {
            case D:
                var processedLocalDate = parseDailyDate(processingDate);
                
                // Validate processedLocalDate is not in the future
                if (processedLocalDate.isAfter(LocalDate.now())) {
                    throw new IllegalArgumentException("Processing date cannot be in the future: " + processedLocalDate);
                }
                
                jobContext.put(BatchConstantUtils.BATCH_METRICS_LOCAL_DATE_CTX, processedLocalDate);
                log.info("✅ Daily processing date parsed: {}", processedLocalDate);
                break;
            case M:
                var processedYearMonth = parseMonthlyDate(processingDate);

                // Validate processedYearMonth is not in the future
                if (processedYearMonth.isAfter(YearMonth.now())) {
                    throw new IllegalArgumentException("Processing month cannot be in the future: " + processedYearMonth);
                }
                
                processedLocalDate = processedYearMonth.atDay(1);
                jobContext.put(BatchConstantUtils.BATCH_METRICS_YEAR_MONTH_CTX, processedYearMonth);

                log.info("✅ Monthly processing date parsed: {} (YearMonth: {})", processedLocalDate, processedYearMonth);
                break;
        }
        jobContext.put(BatchConstantUtils.BATCH_METRICS_MODE_CTX, processingGranularity);
        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(final StepExecution stepExecution) {
        log.info("📌 Init T.M Tasklet - afterStep");
        if (stepExecution.getExitStatus().getExitCode().equals(ExitStatus.FAILED.getExitCode())) {
            return BatchServiceUtils.determineFailureExitStatus(stepExecution);
        }
        return ExitStatus.COMPLETED;
    }

    private ProcessingGranularityEnum validateAndParseGranularity(StepExecution stepExecution) {
        var granularity = stepExecution.getJobParameters().getString(BatchConstantUtils.BATCH_PROCESSING_GRANULARITY);
        if (StringUtils.isBlank(granularity)) {
            log.warn("🚨 Granularity is empty !!");
            throw new IllegalArgumentException("The granularity is mandatory and must not be empty");
        }
        try {
            return ProcessingGranularityEnum.valueOf(granularity);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    String.format("Value of granularity invalid : '%s'. Values authorized : %s",
                            granularity, Arrays.toString(ProcessingGranularityEnum.values()))
            );
        }
    }
    
    private String validateProcessingDate(StepExecution stepExecution) {
        var processingDate = stepExecution.getJobParameters().getString(BatchConstantUtils.BATCH_PROCESSING_DATE);
        if (StringUtils.isBlank(processingDate)) {
            log.warn("🚨 Processing date is empty !!");
            throw new IllegalArgumentException("The processing date is mandatory and must not be empty");
        }
        return processingDate;
    }
    
}
