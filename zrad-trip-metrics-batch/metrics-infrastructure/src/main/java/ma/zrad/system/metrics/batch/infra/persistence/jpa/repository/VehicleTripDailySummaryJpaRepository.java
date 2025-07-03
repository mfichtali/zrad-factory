package ma.zrad.system.metrics.batch.infra.persistence.jpa.repository;

import ma.zrad.system.batch.common.persistence.jpa.entity.summary.VehicleTripDailySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface VehicleTripDailySummaryJpaRepository extends JpaRepository<VehicleTripDailySummary, UUID> {
    
    @Query("""
            select tvtds
            from VehicleTripDailySummary tvtds
            where tvtds.regionCode = :coRegion
            and tvtds.sectionCode = :coSection
            and tvtds.ldtYearMonth = :yearMonth
            and tvtds.ldtDay = :day
            """)
    VehicleTripDailySummary getMetricsDailySummaryByKey(@Param("coRegion") String coRegion, @Param("coSection") String coSection,
                                                        @Param("yearMonth") String yearMonth, @Param("day") String day);

    @Query("""
            select tvtds
            from VehicleTripDailySummary tvtds
            where tvtds.regionCode = :coRegion
            and tvtds.sectionCode = :coSection
            and tvtds.ldtYearMonth = :yearMonth
            """)
    List<VehicleTripDailySummary> findMetricsDailySummaryByKey(@Param("coRegion") String coRegion, @Param("coSection") String coSection,
                                                               @Param("yearMonth") String yearMonth);
}
