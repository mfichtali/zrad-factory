package ma.zrad.system.vp.batch.business.service;

import lombok.RequiredArgsConstructor;
import ma.zrad.system.batch.common.annotations.BusinessService;
import ma.zrad.system.batch.common.domain.EventBusinessDomain;
import ma.zrad.system.vp.batch.domain.port.in.EventBusinessService;
import ma.zrad.system.vp.batch.domain.port.out.EventBusinessRepositoryPort;

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
    public EventBusinessDomain findByIdentifier(final UUID id) {
        return repositoryPort.findByIdentifier(id);
    }
}
