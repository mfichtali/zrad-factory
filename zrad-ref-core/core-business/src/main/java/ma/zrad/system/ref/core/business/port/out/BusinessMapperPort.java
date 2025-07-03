package ma.zrad.system.ref.core.business.port.out;

import ma.zrad.system.batch.common.pojo.FullRegionDetailsOut;
import ma.zrad.system.ref.core.domain.model.RegionDomain;

import java.util.List;

public interface BusinessMapperPort {

    default FullRegionDetailsOut regionDomainToRecord(RegionDomain domain) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    default List<FullRegionDetailsOut> regionDomainToRecords(List<RegionDomain> domains) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
