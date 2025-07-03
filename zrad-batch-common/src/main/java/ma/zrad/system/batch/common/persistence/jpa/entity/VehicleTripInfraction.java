package ma.zrad.system.batch.common.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
import java.util.UUID;

/**
 * T22 : VehicleTripInfraction
 */

@Entity
@Table(name = TableNameUtils.T_VEHICLE_TRIP_INFRACTION, schema = TableNameUtils.ZRAD_DEFAULT_SCHEMA)
@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(doNotUseGetters = true, onlyExplicitlyIncluded = true)
@DynamicUpdate
@DynamicInsert
public class VehicleTripInfraction extends AbstractZradEntity {

    @OneToOne
    @JoinColumn(name = "TRIP_ID", referencedColumnName = "ID", insertable = false, updatable = false, nullable = false)
    private VehicleTripAnalysis tripAnalysis;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "TRIP_ID", nullable = false)
    private UUID idTrip;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "LDT_YEAR_MONTH", nullable = false)
    private String ldtYearMonth; // YYYYMM

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "LDT_DAY", nullable = false)
    private String ldtDay; // DD

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
    @Digits(integer = 6, fraction = 2)
    @Column(name = "SPEED_SECTION_CALCULATED_KMH", precision = 8, scale = 2, nullable = false)
    private BigDecimal speedSectionCalculated;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "ID_EVENT_TRIP", nullable = false)
    private UUID idEventTrip;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "ID_EVENT_STATS", nullable = false)
    private UUID idEventInfraction;

    @Enumerated(EnumType.STRING)
    @Column(name = "CO_ISSUE_TYPE_REF", nullable = false)
    private CodeTypeIssueEnum issue;

    @Column(name = "CO_ISSUE_FUNCTIONAL_CODE", nullable = false)
    private String codeIssue;

    @Column(name = "LB_ANALYSIS_ISSUE")
    private String messageIssue;

    @Override
    public void syncPreInsert() {
        super.syncPreInsert();
        if(tripAnalysis != null) {
            this.idTrip = tripAnalysis.getId();
            this.idEventTrip = tripAnalysis.getIdEventTrip();
            this.regionCode = tripAnalysis.getRegionCode();
            this.sectionCode = tripAnalysis.getSectionCode();
            this.rnVehicle = tripAnalysis.getRnVehicle();
            this.speedSectionCalculated = tripAnalysis.getSpeedSectionCalculated();
            this.issue = tripAnalysis.getIssue();
            this.codeIssue = tripAnalysis.getCodeIssue();
            this.messageIssue = tripAnalysis.getMessageIssue();
        }
    }

}
