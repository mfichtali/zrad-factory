package ma.zrad.system.stats.batch.domain.port.out;

import ma.zrad.system.batch.common.pojo.FullRegionDetailsOut;

import java.util.List;

public interface ReferentialRegionApiPort {

    List<FullRegionDetailsOut> getAllRegionDetails();
}
