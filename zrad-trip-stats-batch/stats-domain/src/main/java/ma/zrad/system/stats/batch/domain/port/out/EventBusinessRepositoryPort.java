package ma.zrad.system.stats.batch.domain.port.out;

import ma.zrad.system.batch.common.domain.EventBusinessDomain;
import ma.zrad.system.batch.common.enums.EventBatchContextEnum;
import ma.zrad.system.batch.common.enums.EventProcessingStatusEnum;

import java.util.List;
import java.util.UUID;

public interface EventBusinessRepositoryPort {

    EventBusinessDomain saveEventBusiness(EventBusinessDomain domain);

    void updateEventBusiness(EventBusinessDomain domain);

    EventBusinessDomain findByIdentifier(UUID id);

    List<EventBusinessDomain> findPendingEventByRegionOfBatch(EventBatchContextEnum batchCxt, String regionCode);

    void updateEventStatus(List<UUID> eventIds, EventProcessingStatusEnum status);
}
