package ma.zrad.system.batch.common.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;


@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
public class VehicleTripValidDomain implements Serializable {
    private VehicleTripAnalysisDomain tripAnalysis;
    private String ldtYearMonth; // YYYYMM
    private String ldtDay; // DD
    private BigDecimal tripDistanceInMeters;
    private UUID idEventValid;
}
