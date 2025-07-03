package ma.zrad.system.trip.batch.domain.port.out;

import ma.zrad.system.trip.batch.domain.model.VehicleTripDomain;

import java.util.List;
import java.util.UUID;

public interface VehicleTripAnalysisRepositoryPort {

    void saveVehicleTrips(List<VehicleTripDomain> domains);
    
    void updateStatusOfVehiclePassage(String coRegion, String coSection, String rnVehicle, UUID eventId, String newStatus);
}
