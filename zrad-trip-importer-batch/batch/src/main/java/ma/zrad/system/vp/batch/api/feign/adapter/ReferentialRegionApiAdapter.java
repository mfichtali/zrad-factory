package ma.zrad.system.vp.batch.api.feign.adapter;

import lombok.RequiredArgsConstructor;
import ma.zrad.system.batch.common.pojo.FullRegionDetailsOut;
import ma.zrad.system.vp.batch.api.feign.region.ReferentialRegionClientFeign;
import ma.zrad.system.vp.batch.domain.port.out.ReferentialRegionApiPort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReferentialRegionApiAdapter implements ReferentialRegionApiPort {

    private final ReferentialRegionClientFeign referentialRegionClientFeign;

    @Override
    public List<FullRegionDetailsOut> getRegionDetailsByCode(final String regionCode) {
        return List.of();
    }

    @Override
    public List<FullRegionDetailsOut> getAllRegionDetails() {
        return referentialRegionClientFeign.retrieveAllRegionDetails();
    }
}
