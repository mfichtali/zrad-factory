package ma.zrad.system.stats.batch.infra.persistence.jpa.adapter;

import lombok.RequiredArgsConstructor;
import ma.zrad.system.batch.common.domain.VehicleTripAnalysisDomain;
import ma.zrad.system.stats.batch.domain.port.out.VehicleTripAnalysisRepositoryPort;
import ma.zrad.system.stats.batch.infra.mapper.GlobalMapper;
import ma.zrad.system.stats.batch.infra.persistence.jpa.repository.VehicleTripAnalysisJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class VehicleTripAnalysisRepositoryAdapter implements VehicleTripAnalysisRepositoryPort {

    private final GlobalMapper mapper;
    private final VehicleTripAnalysisJpaRepository repository;

    @Override
    public List<VehicleTripAnalysisDomain> findTripAnalysisByRegionAndEvents(final String coRegion, final List<UUID> eventIds) {
        var tripAnalysis = repository.findTripAnalysisByRegionAndEvents(coRegion, eventIds);
        return mapper.vehicleTripAnalysisToDomains(tripAnalysis);
    }
}
