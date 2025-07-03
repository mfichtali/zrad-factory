package ma.zrad.system.batch.common.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.UUID;

@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
public class VehicleTripAnomalyDomain implements Serializable {
    private VehicleTripAnalysisDomain tripAnalysis;
    private String ldtYearMonth;
    private String ldtDay;
    private UUID idEventAnomaly;
}
