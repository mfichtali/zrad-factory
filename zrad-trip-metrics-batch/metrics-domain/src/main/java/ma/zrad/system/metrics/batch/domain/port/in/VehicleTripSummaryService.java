package ma.zrad.system.metrics.batch.domain.port.in;

import ma.zrad.system.batch.common.domain.summary.VehicleTripDailySummaryDomain;
import ma.zrad.system.batch.common.domain.summary.VehicleTripMonthlySummaryDomain;
import ma.zrad.system.batch.common.pojo.GroupedDataTripMetrics;
import ma.zrad.system.batch.common.pojo.GroupedMonthlyTripMetrics;

public interface VehicleTripSummaryService {
    
    VehicleTripDailySummaryDomain produceDailySummary(GroupedDataTripMetrics item) throws Exception;

    VehicleTripMonthlySummaryDomain produceMonthlySummary(GroupedMonthlyTripMetrics item) throws Exception;
}
