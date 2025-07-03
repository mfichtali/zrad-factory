package ma.zrad.system.stats.batch.domain.port.in;

import ma.zrad.system.batch.common.domain.EventBusinessDomain;
import ma.zrad.system.batch.common.enums.EventBatchContextEnum;
import ma.zrad.system.batch.common.enums.EventProcessingStatusEnum;

import java.util.List;
import java.util.UUID;

public interface EventBusinessService {

    EventBusinessDomain createEventBusiness(EventBusinessDomain domain);

    void updateEventBusiness(EventBusinessDomain domain);

    void updateEventStatus(final List<UUID> eventIds, final EventProcessingStatusEnum status);

    EventBusinessDomain findByIdentifier(UUID id);

    List<EventBusinessDomain> findPendingEventByRegionOfBatch(EventBatchContextEnum batchCtx, String regionCode);

}
