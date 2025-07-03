package ma.zrad.system.trip.batch.domain.port.out;

import ma.zrad.system.batch.common.domain.EventBusinessDomain;
import ma.zrad.system.batch.common.enums.EventBatchContextEnum;
import ma.zrad.system.batch.common.enums.EventProcessingStatusEnum;
import ma.zrad.system.batch.common.projection.EventRemainingCounterProjection;

import java.util.List;
import java.util.UUID;

public interface EventBusinessRepositoryPort {

    EventBusinessDomain saveEventBusiness(EventBusinessDomain domain);

    void updateEventBusiness(EventBusinessDomain domain);

    EventBusinessDomain findByIdentifier(UUID id);

    List<EventBusinessDomain> findPendingEventByRegionOfBatch(EventBatchContextEnum batchCxt, String regionCode);

    List<EventBusinessDomain> findPendingEventByRegionAndSectionOfBatch(EventBatchContextEnum batchCxt, String regionCode, String sectionCode);
    
    List<EventRemainingCounterProjection> findRemainingPendingVehicle(List<UUID> eventZrad01Ids, String regionCode);
    
    void updateEventStatus(List<UUID> eventIds, EventProcessingStatusEnum status);
}
