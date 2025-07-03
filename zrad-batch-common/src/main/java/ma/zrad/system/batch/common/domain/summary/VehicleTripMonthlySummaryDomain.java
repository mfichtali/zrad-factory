package ma.zrad.system.batch.common.domain.summary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * T31 : VehicleTripMonthlySummary
 */

@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
public class VehicleTripMonthlySummaryDomain implements Serializable {

    private String ldtYearMonth;
    private String regionCode;
    private String sectionCode;
    private BigDecimal nbAnomalies;
    private BigDecimal nbValidTrips;
    private BigDecimal nbInfractions;
    private String anomalyTypeCounts;
    private String infractionTypeCounts;

}
