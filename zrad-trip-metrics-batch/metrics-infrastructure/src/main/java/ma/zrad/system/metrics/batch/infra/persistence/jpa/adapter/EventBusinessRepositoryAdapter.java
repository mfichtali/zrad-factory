package ma.zrad.system.metrics.batch.infra.persistence.jpa.adapter;

import lombok.RequiredArgsConstructor;
import ma.zrad.system.batch.common.domain.EventBusinessDomain;
import ma.zrad.system.metrics.batch.domain.port.out.EventBusinessRepositoryPort;
import ma.zrad.system.metrics.batch.infra.mapper.GlobalMapper;
import ma.zrad.system.metrics.batch.infra.persistence.jpa.repository.EventBusinessJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class EventBusinessRepositoryAdapter implements EventBusinessRepositoryPort {

    private final EventBusinessJpaRepository eventBusinessJpaRepository;
    private final GlobalMapper mapper;

    @Override
    public EventBusinessDomain saveEventBusiness(final EventBusinessDomain domain) {
        var eventBusiness = mapper.eventBusinessToEntity(domain);
        return mapper.eventBusinessToDomain(eventBusinessJpaRepository.saveAndFlush(eventBusiness));
    }

    @Override
    public void updateEventBusiness(final EventBusinessDomain domain) {
        var eventBusiness = mapper.eventBusinessToEntity(domain);
        eventBusinessJpaRepository.save(eventBusiness);
    }

    @Override
    public EventBusinessDomain findByIdentifier(final UUID id) {
        return eventBusinessJpaRepository.findById(id)
                .map(mapper::eventBusinessToDomain)
                .orElse(null);
    }
}
