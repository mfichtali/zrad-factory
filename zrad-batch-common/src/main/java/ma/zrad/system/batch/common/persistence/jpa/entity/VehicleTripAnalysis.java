package ma.zrad.system.batch.common.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ma.zrad.system.batch.common.enums.CodeTypeIssueEnum;
import ma.zrad.system.batch.common.persistence.jpa.entity.base.AbstractZradEntity;
import ma.zrad.system.batch.common.utils.TableNameUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a vehicle trip analysis record in the system, capturing details about the trip,
 * including entry and exit times, speeds, and any detected infractions or anomalies.
 * T20 : VehicleTripAnalysis
 */

@Entity
@Table(name = TableNameUtils.T_VEHICLE_TRIP_ANALYSIS, schema = TableNameUtils.ZRAD_DEFAULT_SCHEMA)
@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(doNotUseGetters = true, onlyExplicitlyIncluded = true)
@DynamicUpdate
@DynamicInsert
public class VehicleTripAnalysis extends AbstractZradEntity {

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
    @Column(name = "RN_VEHICLE", nullable = false)
    private String rnVehicle;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "LDT_TRIP_ENTRY")
    private LocalDateTime passageEntryTime;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "LDT_TRIP_EXIT")
    private LocalDateTime passageExitTime;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "TRIP_DURATION_SECONDS")
    private Integer tripDurationInSeconds;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Digits(integer = 6, fraction = 2)
    @Column(name = "SPEED_ENTRY_KMH", precision = 8, scale = 2)
    private BigDecimal speedEntryDetection;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Digits(integer = 6, fraction = 2)
    @Column(name = "SPEED_EXIT_KMH", precision = 8, scale = 2)
    private BigDecimal speedExitDetection;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Digits(integer = 6, fraction = 2)
    @Column(name = "AVERAGE_SPEED_TRIP_KMH", precision = 8, scale = 2)
    private BigDecimal averageSpeedTrip;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Digits(integer = 6, fraction = 2)
    @Column(name = "SPEED_SECTION_LIMIT_KMH", precision = 8, scale = 2)
    private BigDecimal speedSectionLimit;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Digits(integer = 6, fraction = 2)
    @Column(name = "SPEED_SECTION_CALCULATED_KMH", precision = 8, scale = 2)
    private BigDecimal speedSectionCalculated;

    @Column(name = "IN_INFRACTION")
    private boolean infractionDetected;

    @Column(name = "IN_ANOMALY")
    private boolean anomalyDetected;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "ID_EVENT_IMPORTER", nullable = false)
    private UUID idEventImporter;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "ID_EVENT_TRIP", nullable = false)
    private UUID idEventTrip;

    @Enumerated(EnumType.STRING)
    @Column(name = "CO_ISSUE_TYPE_REF")
    private CodeTypeIssueEnum issue;

    @Column(name = "CO_ISSUE_FUNCTIONAL_CODE")
    private String codeIssue;

    @Column(name = "LB_ANALYSIS_ISSUE")
    private String messageIssue;

}
