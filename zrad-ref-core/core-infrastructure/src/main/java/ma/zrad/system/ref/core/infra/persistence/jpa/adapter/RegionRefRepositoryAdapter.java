package ma.zrad.system.ref.core.infra.persistence.jpa.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.ref.core.domain.model.RegionDomain;
import ma.zrad.system.ref.core.domain.port.out.RegionRefRepositoryPort;
import ma.zrad.system.ref.core.infra.mapper.GlobalMapper;
import ma.zrad.system.ref.core.infra.persistence.jpa.entity.RegionRef;
import ma.zrad.system.ref.core.infra.persistence.jpa.repository.RegionRefJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RegionRefRepositoryAdapter implements RegionRefRepositoryPort {

    private final RegionRefJpaRepository regionRefJpaRepository;
    private final GlobalMapper mapper;

    @Override
    public RegionDomain findRegionByCode(final String code) {
        var regionWithSections = regionRefJpaRepository.findRegionByCodeAndEnabledSections(code);
        if (regionWithSections != null) {
            return mapper.regionRefToDomain(regionWithSections);
        }
        return findBasicRegionInfo(code);
    }

    @Override
    public List<RegionDomain> findAllRegions() {
        log.debug("Finding all regions");
        var regionsWithSections = regionRefJpaRepository.findAllRegionsAndEnabledSections();
        var allRegionsInfo = regionRefJpaRepository.findAllCodeRegionAndLabelByCode();

        var existingCodes = regionsWithSections.stream()
                .map(RegionRef::getCodeRegion)
                .collect(Collectors.toSet());

        allRegionsInfo.stream()
                .filter(info -> !existingCodes.contains(info.codeRegion()))
                .map(info -> createBasicRegionEntity(info.codeRegion(), info.labelRegion()))
                .forEach(regionsWithSections::add);

        log.debug("Found {} total regions", regionsWithSections.size());
        return mapper.regionRefToDomains(regionsWithSections);
    }

    private RegionDomain findBasicRegionInfo(String code) {
        var regionInfo = regionRefJpaRepository.findCodeRegionAndLabelByCode(code);
        if (regionInfo != null) {
            var basicRegion = createBasicRegionEntity(regionInfo.codeRegion(), regionInfo.labelRegion());
            return mapper.regionRefToDomain(basicRegion);
        }
        return null;
    }

    private RegionRef createBasicRegionEntity(String codeRegion, String labelRegion) {
        var region = new RegionRef();
        region.setCodeRegion(codeRegion);
        region.setLabelRegion(labelRegion);
        return region;
    }

}
