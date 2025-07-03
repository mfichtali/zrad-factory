package ma.zrad.system.metrics.batch.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.domain.summary.VehicleTripDailySummaryDomain;
import ma.zrad.system.batch.common.pojo.GroupedDataTripMetrics;
import ma.zrad.system.metrics.batch.domain.port.in.VehicleTripSummaryService;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@StepScope
@Component
@RequiredArgsConstructor
public class TripMetricsDailyProcessor implements ItemProcessor<GroupedDataTripMetrics, VehicleTripDailySummaryDomain> {

    private final VehicleTripSummaryService vehicleTripSummaryService;
    
    @Override
    public VehicleTripDailySummaryDomain process(final GroupedDataTripMetrics item) throws Exception {
        if (item == null || item.getItems().isEmpty()) {
            return null;
        }
        return vehicleTripSummaryService.produceDailySummary(item);
    }

}
