package ma.zrad.system.metrics.batch.infra.persistence.jpa.repository;

import ma.zrad.system.batch.common.persistence.jpa.entity.summary.VehicleTripMonthlySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface VehicleTripMonthlySummaryJpaRepository extends JpaRepository<VehicleTripMonthlySummary, UUID> {
    
    @Query("""
            select tvtds
            from VehicleTripMonthlySummary tvtds
            where tvtds.regionCode = :coRegion
            and tvtds.sectionCode = :coSection
            and tvtds.ldtYearMonth = :yearMonth
            """)
    VehicleTripMonthlySummary findMetricsMonthlySummaryByKey(@Param("coRegion") String coRegion, @Param("coSection") String coSection,
                                                         @Param("yearMonth") String yearMonth);
}
