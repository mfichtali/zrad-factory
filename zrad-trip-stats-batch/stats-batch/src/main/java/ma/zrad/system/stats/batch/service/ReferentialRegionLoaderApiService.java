package ma.zrad.system.stats.batch.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.pojo.FullRegionDetailsOut;
import ma.zrad.system.stats.batch.domain.port.out.ReferentialRegionApiPort;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Getter
public class ReferentialRegionLoaderApiService {

    private final ReferentialRegionApiPort referentialRegionApiPort;
    private List<FullRegionDetailsOut> regionDetails;

    @PostConstruct
    public void init() {
        loadApiRefRegions();
    }

    private void loadApiRefRegions() {
        try {
            regionDetails = referentialRegionApiPort.getAllRegionDetails();
        } catch (Exception ex) {
            log.error("Error loading region details from referential service", ex);
            regionDetails = null;
        }
    }

}
