package ma.zrad.system.vp.batch.api.feign.region;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.pojo.FullRegionDetailsOut;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ReferentialRegionFallbackFactory implements FallbackFactory<ReferentialRegionClientFeign> {

    @Override
    public ReferentialRegionClientFeign create(final Throwable cause) {
        log.error("An exception occurred when calling the ReferentialRegionClientFeign", cause);
        return new ReferentialRegionClientFeign() {
            @Override
            public List<FullRegionDetailsOut> retrieveAllRegionDetails() {
                log.error("ReferentialRegionClientFeign#retrieveAllRegionDetails fallback, exception", cause);
                if (cause instanceof FeignException fe) {
                    throw fe;
                }
                return null;
            }
        };
    }
}
