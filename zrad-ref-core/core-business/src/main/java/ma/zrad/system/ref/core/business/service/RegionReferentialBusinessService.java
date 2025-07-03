package ma.zrad.system.ref.core.business.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.annotations.BusinessService;
import ma.zrad.system.batch.common.pojo.FullRegionDetailsOut;
import ma.zrad.system.ref.core.business.port.out.BusinessMapperPort;
import ma.zrad.system.ref.core.domain.port.in.RegionReferentialService;
import ma.zrad.system.ref.core.domain.port.out.RegionRefRepositoryPort;

import java.util.List;

@Slf4j
@BusinessService
@RequiredArgsConstructor
public class RegionReferentialBusinessService implements RegionReferentialService {

    private final RegionRefRepositoryPort regionRefRepositoryPort;
    private final BusinessMapperPort mapper;

    @Override
    public List<FullRegionDetailsOut> getRegionWithRadarSections(final String regionCode) {
        log.info("Searching for region details with code: {}", regionCode);
        if (regionCode == null || regionCode.isBlank()) {
            throw new IllegalArgumentException("Region code cannot be empty");
        }
        String sanitizedCode = regionCode.trim().toUpperCase();
        var regionDomain = regionRefRepositoryPort.findRegionByCode(sanitizedCode);
        if (regionDomain == null) {
            log.warn("No region found for code: {}", sanitizedCode);
            return List.of();
        }
        return List.of(mapper.regionDomainToRecord(regionDomain));
    }

    @Override
    public List<FullRegionDetailsOut> getAllRegionsWithRadarSections() {
        log.debug("Retrieving all regions with radar sections");
        var regionDomains = regionRefRepositoryPort.findAllRegions();
        if (regionDomains.isEmpty()) {
            log.warn("No regions found in the system");
            return List.of();
        }
        return mapper.regionDomainToRecords(regionDomains);
    }
}
