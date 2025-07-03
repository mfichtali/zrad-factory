package ma.zrad.system.batch.common.persistence.jpa.entity.summary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ma.zrad.system.batch.common.persistence.jpa.entity.base.AbstractZradEntity;
import ma.zrad.system.batch.common.utils.TableNameUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;

/**
 * T33 : VehicleTripYearlySummary
 */

@Entity
@Table(name = TableNameUtils.T_VEHICLE_TRIP_YEARLY_SUMMARY, schema = TableNameUtils.ZRAD_DEFAULT_SCHEMA)
@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(doNotUseGetters = true, onlyExplicitlyIncluded = true)
@DynamicUpdate
@DynamicInsert
public class VehicleTripYearlySummary extends AbstractZradEntity {

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "LDT_YEAR", nullable = false)
    private String ldtYearMonth;
    
    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "CO_REGION", nullable = false)
    private String regionCode;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "CO_SECTION_ROAD", nullable = false)
    private String sectionCode;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "NB_ANOMALIES", nullable = false)
    private BigDecimal nbAnomalies;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "NB_VALID_TRIPS", nullable = false)
    private BigDecimal nbValidTrips;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "NB_INFRACTIONS", nullable = false)
    private BigDecimal nbInfractions;

    @Column(name = "ANOMALY_TYPE_COUNTS", columnDefinition = "TEXT")
    private String anomalyTypeCounts;

    @Column(name = "INFRACTION_TYPE_COUNTS", columnDefinition = "TEXT")
    private String infractionTypeCounts;

}
