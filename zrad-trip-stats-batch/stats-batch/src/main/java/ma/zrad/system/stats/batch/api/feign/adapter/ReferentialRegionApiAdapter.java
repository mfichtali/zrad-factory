package ma.zrad.system.stats.batch.api.feign.adapter;

import lombok.RequiredArgsConstructor;
import ma.zrad.system.batch.common.pojo.FullRegionDetailsOut;
import ma.zrad.system.stats.batch.api.feign.region.ReferentialRegionClientFeign;
import ma.zrad.system.stats.batch.domain.port.out.ReferentialRegionApiPort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReferentialRegionApiAdapter implements ReferentialRegionApiPort {

    private final ReferentialRegionClientFeign referentialRegionClientFeign;

    @Override
    public List<FullRegionDetailsOut> getAllRegionDetails() {
        return referentialRegionClientFeign.retrieveAllRegionDetails();
    }
}
