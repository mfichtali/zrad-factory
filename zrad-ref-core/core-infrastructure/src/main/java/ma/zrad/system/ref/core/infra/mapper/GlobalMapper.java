package ma.zrad.system.ref.core.infra.mapper;

import ma.zrad.system.batch.common.pojo.FullRegionDetailsOut;
import ma.zrad.system.ref.core.business.port.out.BusinessMapperPort;
import ma.zrad.system.ref.core.domain.model.RegionDomain;
import ma.zrad.system.ref.core.infra.persistence.jpa.entity.RegionRef;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GlobalMapper extends BusinessMapperPort {

    RegionDomain regionRefToDomain(RegionRef region);
    List<RegionDomain> regionRefToDomains(List<RegionRef> regions);

    @Override
    FullRegionDetailsOut regionDomainToRecord(RegionDomain domain);

    @Override
    List<FullRegionDetailsOut> regionDomainToRecords(List<RegionDomain> domains);
}
