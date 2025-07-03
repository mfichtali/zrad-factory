package ma.zrad.system.stats.batch.domain.bo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ma.zrad.system.batch.common.domain.VehicleTripAnomalyDomain;
import ma.zrad.system.batch.common.domain.VehicleTripInfractionDomain;
import ma.zrad.system.batch.common.domain.VehicleTripValidDomain;

import java.io.Serializable;

@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
public class VehicleTripStatsResultBo implements Serializable {
    //private VehicleTripAnalysisDomain tripAnalysisDomain;
    private VehicleTripValidDomain tripValidDomain;
    private VehicleTripAnomalyDomain tripAnomalyDomain;
    private VehicleTripInfractionDomain tripInfractionDomain;
}
