package ma.zrad.system.metrics.batch.writer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.domain.summary.VehicleTripMonthlySummaryDomain;
import ma.zrad.system.metrics.batch.domain.port.out.VehicleTripSummaryRepositoryPort;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Slf4j
@StepScope
@Component
@RequiredArgsConstructor
public class TripMetricsMonthlyWriter implements ItemWriter<VehicleTripMonthlySummaryDomain> {

    private final VehicleTripSummaryRepositoryPort repositoryPort;
    
    @Override
    public void write(final Chunk<? extends VehicleTripMonthlySummaryDomain> chunk) throws Exception {
        if (chunk == null || chunk.isEmpty()) {
            log.info("🚨 No data to write for monthly trip metrics.");
            return;
        }
        var domains = chunk.getItems().stream()
                .map(item -> (VehicleTripMonthlySummaryDomain) item)
                .toList();
        repositoryPort.saveAllMonthlyMetrics(domains);
    }
}
