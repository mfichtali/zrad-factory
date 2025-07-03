package ma.zrad.system.vp.batch.domain.port.out;


import ma.zrad.system.batch.common.domain.EventBusinessDomain;

import java.util.UUID;

public interface EventBusinessRepositoryPort {

    EventBusinessDomain saveEventBusiness(EventBusinessDomain domain);

    void updateEventBusiness(EventBusinessDomain domain);

    EventBusinessDomain findByIdentifier(UUID id);
}
