package ma.zrad.system.stats.batch.infra.persistence.jpa.repository;

import ma.zrad.system.batch.common.enums.EventBatchContextEnum;
import ma.zrad.system.batch.common.enums.EventProcessingStatusEnum;
import ma.zrad.system.batch.common.persistence.jpa.entity.EventBusiness;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EventBusinessJpaRepository extends JpaRepository<EventBusiness, UUID> {

    @Query(value = """
            select ev
            from EventBusiness ev
            join VehicleTripAnalysis ta on ta.idEventTrip = ev.id
            where ev.eventBatchStatus = 'COMPLETED'
            and ev.eventProcessingStatus = 'PENDING'
            and ev.eventBatchContextEnum = :batchContext
            and ta.regionCode = :coRegion
            """)
    List<EventBusiness> findPendingEventByRegionOfBatch(@Param("batchContext") EventBatchContextEnum batchContext,
                                                        @Param("coRegion") String regionCode);

    @Modifying
    @Query("""
            UPDATE EventBusiness SET eventProcessingStatus = :status
            WHERE id IN :eventIds
            """)
    void updateEventStatus(@Param("eventIds") List<UUID> eventIds, @Param("status") EventProcessingStatusEnum status);

}
