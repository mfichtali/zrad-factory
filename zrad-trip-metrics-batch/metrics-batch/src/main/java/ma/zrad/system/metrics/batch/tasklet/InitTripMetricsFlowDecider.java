package ma.zrad.system.metrics.batch.tasklet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.enums.ProcessingGranularityEnum;
import ma.zrad.system.batch.common.exceptions.BusinessException;
import ma.zrad.system.batch.common.pojo.FullRegionDetailsOut;
import ma.zrad.system.batch.common.utils.BatchConstantUtils;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitTripMetricsFlowDecider implements JobExecutionDecider {
    
    @Override
    public FlowExecutionStatus decide(final JobExecution jobExecution, final StepExecution stepExecution) {

        var hasFailure = jobExecution.getStepExecutions()
                .stream()
                .anyMatch(step -> step.getStatus() == BatchStatus.FAILED);
        if (hasFailure) {
            return FlowExecutionStatus.FAILED;
        }

        var jobContext = jobExecution.getExecutionContext();
        var processingGranularityModeCtx = (ProcessingGranularityEnum) jobContext.get(BatchConstantUtils.BATCH_METRICS_MODE_CTX);

        if (processingGranularityModeCtx == null) {
            return new FlowExecutionStatus(BatchConstantUtils.TRIP_INVALID_MODE);
        }
        return switch (processingGranularityModeCtx) {
            case D -> {
                log.info("✅ Daily Mode Partition");
                yield new FlowExecutionStatus(BatchConstantUtils.TRIP_METRICS_PARTITION_DAILY_MODE);
            }
            case M -> {
                log.info("✅ Monthly Mode Partition");
                yield new FlowExecutionStatus(BatchConstantUtils.TRIP_METRICS_PARTITION_MONTHLY_MODE);
            }
        };
    }

    private FullRegionDetailsOut findRegionInfo(List<FullRegionDetailsOut> regionsDetails, String regionCode) {
        return regionsDetails.stream()
                .filter(r -> r.getCodeRegion().equalsIgnoreCase(regionCode))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Region code not found in the referential API: " + regionCode));
    }
}
