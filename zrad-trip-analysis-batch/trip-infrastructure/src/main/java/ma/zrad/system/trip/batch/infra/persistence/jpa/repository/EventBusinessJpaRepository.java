package ma.zrad.system.trip.batch.infra.persistence.jpa.repository;

import ma.zrad.system.batch.common.enums.EventBatchContextEnum;
import ma.zrad.system.batch.common.enums.EventProcessingStatusEnum;
import ma.zrad.system.batch.common.persistence.jpa.entity.EventBusiness;
import ma.zrad.system.batch.common.projection.EventRemainingCounterProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventBusinessJpaRepository extends JpaRepository<EventBusiness, UUID> {

    @Query(value = """            
            select ev
            from EventBusiness ev
            join VehiclePassageImporter pi on pi.eventId = ev.id
            where ev.eventBatchStatus = 'COMPLETED'
            and ev.eventProcessingStatus in ('PENDING', 'IN_PROGRESS')
            and ev.eventBatchContextEnum = :batchContext
            and pi.regionCode = :coRegion
            and pi.sectionRoadCode = :coSection
            """)
    List<EventBusiness> findPendingEventByRegionAndSectionOfBatch(@Param("batchContext") EventBatchContextEnum batchContext,
                                                                  @Param("coRegion") String regionCode,
                                                                  @Param("coSection") String sectionCode);

    @Query(value = """
            select ev
            from EventBusiness ev
            join VehiclePassageImporter pi on pi.eventId = ev.id
            where ev.eventBatchStatus = 'COMPLETED'
            and ev.eventProcessingStatus in ('PENDING', 'IN_PROGRESS')
            and ev.eventBatchContextEnum = :batchContext
            and pi.regionCode = :coRegion
            """)
    List<EventBusiness> findPendingEventByRegionOfBatch(@Param("batchContext") EventBatchContextEnum batchContext,
                                                        @Param("coRegion") String regionCode);

    @Query(value = """
            SELECT
                teb.id AS event_id,
                teb.co_event_processing_status,
                COUNT(tvpi.id) FILTER (
                    WHERE tvpi.co_region = :coRegion
                      AND tvpi.co_status_import = 'PENDING'
                ) AS remaining_pending_counter
            FROM t_event_business teb
            LEFT JOIN t_vehicle_passage_importer tvpi
                ON tvpi.id_event = teb.id
            WHERE teb.id IN (:eventIds)
            GROUP BY teb.id, teb.co_event_processing_status
            """, nativeQuery = true)
    List<EventRemainingCounterProjection> findRemainingPendingVehicle(@Param("eventIds") List<UUID> eventZrad01Ids, @Param("coRegion") String regionCode);

    @Modifying
    @Query("""
            UPDATE EventBusiness SET eventProcessingStatus = :status
            WHERE id IN :eventIds
            """)
    void updateEventStatus(@Param("eventIds") List<UUID> eventIds, @Param("status") EventProcessingStatusEnum status);

}
