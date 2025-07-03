package ma.zrad.system.metrics.batch.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.domain.summary.VehicleTripMonthlySummaryDomain;
import ma.zrad.system.batch.common.pojo.GroupedMonthlyTripMetrics;
import ma.zrad.system.metrics.batch.domain.port.in.VehicleTripSummaryService;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@StepScope
@Component
@RequiredArgsConstructor
public class TripMetricsMonthlyProcessor implements ItemProcessor<GroupedMonthlyTripMetrics, VehicleTripMonthlySummaryDomain> {

    private final VehicleTripSummaryService vehicleTripSummaryService;
    
    @Override
    public VehicleTripMonthlySummaryDomain process(final GroupedMonthlyTripMetrics item) throws Exception {
        if (item == null || item.getItems().isEmpty()) {
            return null;
        }
        return vehicleTripSummaryService.produceMonthlySummary(item);
    }

}
