package ma.zrad.system.stats.batch.domain.port.out;

import ma.zrad.system.batch.common.domain.VehicleTripAnomalyDomain;
import ma.zrad.system.batch.common.domain.VehicleTripInfractionDomain;
import ma.zrad.system.batch.common.domain.VehicleTripValidDomain;

import java.util.List;

public interface VehicleTripStatsRepositoryPort {

    void saveAllValidTrip(List<VehicleTripValidDomain> trips);
    void saveAllAnomalyTrip(List<VehicleTripAnomalyDomain> trips);
    void saveAllInfractionTrip(List<VehicleTripInfractionDomain> trips);
}
