package ma.zrad.system.metrics.batch.domain.port.out;

import ma.zrad.system.batch.common.pojo.FullRegionDetailsOut;

import java.util.List;

public interface ReferentialRegionApiPort {

    List<FullRegionDetailsOut> getRegionDetailsByCode(String regionCode);

    List<FullRegionDetailsOut> getAllRegionDetails();

}
