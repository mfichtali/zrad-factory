package ma.zrad.system.trip.batch.infra.persistence.jpa.adapter;

import lombok.RequiredArgsConstructor;
import ma.zrad.system.batch.common.persistence.jpa.entity.VehicleTripAnalysis;
import ma.zrad.system.trip.batch.domain.model.VehicleTripDomain;
import ma.zrad.system.trip.batch.domain.port.out.VehicleTripAnalysisRepositoryPort;
import ma.zrad.system.trip.batch.infra.mapper.GlobalMapper;
import ma.zrad.system.trip.batch.infra.persistence.jpa.repository.VehicleTripJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class VehicleTripAnalysisRepositoryAdapter implements VehicleTripAnalysisRepositoryPort {

    private final GlobalMapper mapper;
    private final VehicleTripJpaRepository vehicleTripJpaRepository;

    @Override
    public void saveVehicleTrips(final List<VehicleTripDomain> domains) {
        List<VehicleTripAnalysis> vehicleTripAnalyses = mapper.vehicleTripAnalysisToEntities(domains);
        vehicleTripJpaRepository.saveAll(vehicleTripAnalyses);
    }

    @Override
    public void updateStatusOfVehiclePassage(final String coRegion, final String coSection, final String rnVehicle, final UUID eventId, final String newStatus) {
        vehicleTripJpaRepository.updateStatusOfVehiclePassage(coRegion, coSection, rnVehicle, eventId, newStatus);
    }
}
