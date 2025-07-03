package ma.zrad.system.vp.batch.domain.port.in;


import ma.zrad.system.batch.common.domain.EventBusinessDomain;

import java.util.UUID;

public interface EventBusinessService {

    EventBusinessDomain createEventBusiness(EventBusinessDomain domain);

    void updateEventBusiness(EventBusinessDomain domain);

    EventBusinessDomain findByIdentifier(UUID id);
}
