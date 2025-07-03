package ma.zrad.system.stats.batch.domain.port.out;

import ma.zrad.system.batch.common.domain.VehicleTripAnalysisDomain;

import java.util.List;
import java.util.UUID;

public interface VehicleTripAnalysisRepositoryPort {

    List<VehicleTripAnalysisDomain> findTripAnalysisByRegionAndEvents(String coRegion, List<UUID> eventIds);
}
