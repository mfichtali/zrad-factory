package ma.zrad.system.stats.batch.infra.persistence.jpa.repository;

import ma.zrad.system.batch.common.persistence.jpa.entity.VehicleTripAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface VehicleTripAnalysisJpaRepository extends JpaRepository<VehicleTripAnalysis, UUID> {

    @Query("""
            SELECT ta
            FROM VehicleTripAnalysis ta
            WHERE ta.regionCode = :coRegion
            AND ta.idEventTrip IN :eventIds
            """)
    List<VehicleTripAnalysis> findTripAnalysisByRegionAndEvents(@Param("coRegion") String coRegion, @Param("eventIds") List<UUID> eventIds);
}
