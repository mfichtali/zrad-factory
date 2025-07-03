package ma.zrad.system.ref.core.domain.port.out;

import ma.zrad.system.ref.core.domain.model.RegionDomain;

import java.util.List;

public interface RegionRefRepositoryPort {

    RegionDomain findRegionByCode(String code);

    List<RegionDomain> findAllRegions();
}
