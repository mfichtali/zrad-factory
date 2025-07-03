package ma.zrad.system.stats.batch.infra.persistence.jpa.adapter;

import lombok.RequiredArgsConstructor;
import ma.zrad.system.batch.common.domain.VehicleTripAnomalyDomain;
import ma.zrad.system.batch.common.domain.VehicleTripInfractionDomain;
import ma.zrad.system.batch.common.domain.VehicleTripValidDomain;
import ma.zrad.system.stats.batch.domain.port.out.VehicleTripStatsRepositoryPort;
import ma.zrad.system.stats.batch.infra.mapper.GlobalMapper;
import ma.zrad.system.stats.batch.infra.persistence.jpa.repository.VehicleTripAnomalyJpaRepository;
import ma.zrad.system.stats.batch.infra.persistence.jpa.repository.VehicleTripInfractionJpaRepository;
import ma.zrad.system.stats.batch.infra.persistence.jpa.repository.VehicleTripValidJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VehicleTripStatsRepositoryAdapter implements VehicleTripStatsRepositoryPort {

    private final GlobalMapper mapper;
    private final VehicleTripAnomalyJpaRepository anomalyRepository;
    private final VehicleTripValidJpaRepository validRepository;
    private final VehicleTripInfractionJpaRepository infractionRepository;

    @Override
    public void saveAllValidTrip(final List<VehicleTripValidDomain> trips) {
        var tripEntities = mapper.vehicleTripValidToEntities(trips);
        validRepository.saveAll(tripEntities);
    }

    @Override
    public void saveAllAnomalyTrip(final List<VehicleTripAnomalyDomain> trips) {
        var tripEntities = mapper.vehicleTripAnomalyToEntities(trips);
        anomalyRepository.saveAll(tripEntities);
    }

    @Override
    public void saveAllInfractionTrip(final List<VehicleTripInfractionDomain> trips) {
        var tripEntities = mapper.vehicleTripInfractionToEntities(trips);
        infractionRepository.saveAll(tripEntities);
    }
}
