package ma.zrad.system.batch.common.domain.summary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * T30 : VehicleTripDailySummary
 */

@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
public class VehicleTripDailySummaryDomain implements Serializable {

    private UUID id;
    private Long version;
    private String ldtYearMonth;
    private String ldtDay;
    private String regionCode;
    private String sectionCode;
    private BigDecimal nbAnomalies;
    private BigDecimal nbValidTrips;
    private BigDecimal nbInfractions;
    private String anomalyTypeCounts;
    private String infractionTypeCounts;

}
