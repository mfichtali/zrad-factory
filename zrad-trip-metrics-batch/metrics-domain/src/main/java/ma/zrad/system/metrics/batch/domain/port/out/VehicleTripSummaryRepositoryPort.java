package ma.zrad.system.metrics.batch.domain.port.out;

import ma.zrad.system.batch.common.domain.summary.VehicleTripDailySummaryDomain;
import ma.zrad.system.batch.common.domain.summary.VehicleTripMonthlySummaryDomain;

import java.util.List;

public interface VehicleTripSummaryRepositoryPort {
    
    VehicleTripDailySummaryDomain findMetricsMonthlySummaryByKey(String coRegion, String coSection, String yearMonth, String day);

    VehicleTripMonthlySummaryDomain findMetricsMonthlySummaryByKey(String coRegion, String coSection, String yearMonth);
    
    void saveAllDailyMetrics(List<VehicleTripDailySummaryDomain> items);
    
    void saveAllMonthlyMetrics(List<VehicleTripMonthlySummaryDomain> items);

    List<VehicleTripDailySummaryDomain> findMetricsDailyMetricsByKey(String coRegion, String coSection, String yearMonth);
}
