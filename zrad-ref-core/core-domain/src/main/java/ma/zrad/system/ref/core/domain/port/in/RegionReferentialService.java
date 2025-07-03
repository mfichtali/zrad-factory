package ma.zrad.system.ref.core.domain.port.in;

import ma.zrad.system.batch.common.pojo.FullRegionDetailsOut;

import java.util.List;

public interface RegionReferentialService {

    /**
     * Builds the full details of a region based on its code.
     *
     * @param code the code of the region
     * @return FullRegionDetailsRecord containing the details of the region
     */
    List<FullRegionDetailsOut> getRegionWithRadarSections(String code);

    /**
     * Builds the full details of a region based on its code.
     *
     * @param code the code of the region
     * @return FullRegionDetailsRecord containing the details of the region
     */
    List<FullRegionDetailsOut> getAllRegionsWithRadarSections();
}
