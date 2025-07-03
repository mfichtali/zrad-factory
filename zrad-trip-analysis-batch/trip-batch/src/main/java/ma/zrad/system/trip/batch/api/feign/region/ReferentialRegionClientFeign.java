package ma.zrad.system.trip.batch.api.feign.region;

import ma.zrad.system.batch.common.pojo.FullRegionDetailsOut;
import ma.zrad.system.trip.batch.api.feign.common.AbstractClientFeign;
import ma.zrad.system.trip.batch.api.feign.common.FeignSupportConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import static ma.zrad.system.trip.batch.api.feign.common.AbstractClientFeign.REF_CORE_SERVICE_NAME;
import static ma.zrad.system.trip.batch.api.feign.common.AbstractClientFeign.REF_CORE_SERVICE_URL;

@FeignClient(contextId = "regionClient",
        name= REF_CORE_SERVICE_NAME,
        url= REF_CORE_SERVICE_URL,
        configuration = FeignSupportConfig.class,
        fallbackFactory = ReferentialRegionFallbackFactory.class)
public interface ReferentialRegionClientFeign extends AbstractClientFeign {

    String REGION_DETAILS_GET = "/api/ref/regions/details";

    @GetMapping(REGION_DETAILS_GET)
    @ResponseStatus(HttpStatus.OK)
    List<FullRegionDetailsOut> retrieveAllRegionDetails();

}
