package ma.zrad.system.stats.batch.reader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.domain.VehicleTripAnalysisDomain;
import ma.zrad.system.batch.common.utils.BatchConstantUtils;
import ma.zrad.system.batch.common.utils.BatchServiceUtils;
import ma.zrad.system.stats.batch.domain.port.out.VehicleTripAnalysisRepositoryPort;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Slf4j
@StepScope
@Component
@RequiredArgsConstructor
public class TripStatsReader implements ItemReader<VehicleTripAnalysisDomain>, StepExecutionListener {

    private final VehicleTripAnalysisRepositoryPort repositoryPort;

    private Iterator<VehicleTripAnalysisDomain> tripIterator;

    @Override
    public void beforeStep(final StepExecution stepExecution) {
        ExecutionContext stepCtx = stepExecution.getExecutionContext();
        ExecutionContext jobCtx = stepExecution.getJobExecution().getExecutionContext();
        JobParameters jobParams = stepExecution.getJobExecution().getJobParameters();

        List<UUID> eventIds;
        String coRegion;
        if (stepCtx.containsKey(BatchConstantUtils.PARAM_REGION_CODE)) {
            coRegion = stepCtx.getString(BatchConstantUtils.PARAM_REGION_CODE);
            eventIds = (List<UUID>) stepCtx.get(BatchConstantUtils.BATCH_CTX_EVENT_ZRAD02_IDS);
        } else if (jobParams.getString(BatchConstantUtils.PARAM_REGION_CODE) != null) {
            coRegion = jobParams.getString(BatchConstantUtils.PARAM_REGION_CODE);
            eventIds = (List<UUID>) jobCtx.get(BatchConstantUtils.BATCH_CTX_EVENT_ZRAD02_IDS);
        } else {
            throw new IllegalStateException("Region code or event IDs not found in context");
        }

        var trips = repositoryPort.findTripAnalysisByRegionAndEvents(coRegion, eventIds);
        this.tripIterator = trips.iterator();

    }

    @Override
    public VehicleTripAnalysisDomain read() {
        return (tripIterator != null && tripIterator.hasNext()) ? tripIterator.next() : null;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (stepExecution.getExitStatus().getExitCode().equals(ExitStatus.FAILED.getExitCode())) {
            return BatchServiceUtils.determineFailureExitStatus(stepExecution);
        }
        return stepExecution.getExitStatus();
    }
}
