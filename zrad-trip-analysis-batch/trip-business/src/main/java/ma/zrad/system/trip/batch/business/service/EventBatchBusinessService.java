package ma.zrad.system.trip.batch.business.service;

import lombok.RequiredArgsConstructor;
import ma.zrad.system.batch.common.annotations.BusinessService;
import ma.zrad.system.batch.common.domain.EventBusinessDomain;
import ma.zrad.system.batch.common.enums.EventBatchContextEnum;
import ma.zrad.system.batch.common.enums.EventProcessingStatusEnum;
import ma.zrad.system.batch.common.projection.EventRemainingCounterProjection;
import ma.zrad.system.trip.batch.domain.port.in.EventBusinessService;
import ma.zrad.system.trip.batch.domain.port.out.EventBusinessRepositoryPort;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.UUID;

@BusinessService
@RequiredArgsConstructor
public class EventBatchBusinessService implements EventBusinessService {

    private final EventBusinessRepositoryPort repositoryPort;

    @Override
    public EventBusinessDomain createEventBusiness(final EventBusinessDomain domain) {
        return repositoryPort.saveEventBusiness(domain);
    }

    @Override
    public void updateEventBusiness(final EventBusinessDomain domain) {
        repositoryPort.updateEventBusiness(domain);
    }

    @Override
    public void updateEventStatus(final List<UUID> eventIds, final EventProcessingStatusEnum status) {
        if (CollectionUtils.isEmpty(eventIds)) return;
        repositoryPort.updateEventStatus(eventIds, status);
    }

    @Override
    public EventBusinessDomain findByIdentifier(final UUID id) {
        return repositoryPort.findByIdentifier(id);
    }

    @Override
    public List<EventBusinessDomain> findPendingEventByRegionOfBatch(final EventBatchContextEnum batchCtx, final String regionCode) {
        return repositoryPort.findPendingEventByRegionOfBatch(batchCtx, regionCode);
    }

    @Override
    public List<EventBusinessDomain> findPendingEventByRegionAndSectionOfBatch(final EventBatchContextEnum batchCtx, final String regionCode, final String sectionCode) {
        return repositoryPort.findPendingEventByRegionAndSectionOfBatch(batchCtx, regionCode, sectionCode);
    }

    @Override
    public List<EventRemainingCounterProjection> findRemainingPendingVehicle(final List<UUID> eventZrad01Ids, final String regionCode) {
        return repositoryPort.findRemainingPendingVehicle(eventZrad01Ids, regionCode);
    }
}
