package ma.zrad.system.trip.batch.domain.port.in;

import ma.zrad.system.batch.common.domain.EventBusinessDomain;
import ma.zrad.system.batch.common.enums.EventBatchContextEnum;
import ma.zrad.system.batch.common.enums.EventProcessingStatusEnum;
import ma.zrad.system.batch.common.projection.EventRemainingCounterProjection;

import java.util.List;
import java.util.UUID;

public interface EventBusinessService {

    EventBusinessDomain createEventBusiness(EventBusinessDomain domain);

    void updateEventBusiness(EventBusinessDomain domain);

    void updateEventStatus(List<UUID> eventIds, EventProcessingStatusEnum status);

    EventBusinessDomain findByIdentifier(UUID id);

    List<EventBusinessDomain> findPendingEventByRegionOfBatch(EventBatchContextEnum batchCtx, String regionCode);

    List<EventBusinessDomain> findPendingEventByRegionAndSectionOfBatch(EventBatchContextEnum batchCtx, String regionCode, String sectionCode);
    
    List<EventRemainingCounterProjection> findRemainingPendingVehicle(List<UUID> eventZrad01Ids, String regionCode);
}
