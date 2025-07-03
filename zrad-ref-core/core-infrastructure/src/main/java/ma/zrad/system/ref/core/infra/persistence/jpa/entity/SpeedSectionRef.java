package ma.zrad.system.ref.core.infra.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ma.zrad.system.batch.common.utils.TableNameUtils;
import ma.zrad.system.ref.core.infra.persistence.jpa.entity.base.AbstractZradEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = TableNameUtils.REF_SPEED_SECTION, schema = TableNameUtils.ZRAD_DEFAULT_SCHEMA)
@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(doNotUseGetters = true, onlyExplicitlyIncluded = true)
@DynamicUpdate
@DynamicInsert
public class SpeedSectionRef extends AbstractZradEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SECTION_ID", referencedColumnName = "id", nullable = false)
    private SectionRef section;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "CO_SECTION", nullable = false)
    private String codeSection;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Digits(integer = 4, fraction = 2)
    @Column(name = "SPEED_AUTHORIZED", precision = 6, scale = 2, nullable = false)
    private BigDecimal speedAuthorized;

    @Column(name = "SPEED_TOLERANCE_KMH")
    private Integer speedToleranceKmh;

    @Column(name = "SPEED_TOLERANCE_PERCENT")
    private Integer speedTolerancePercent;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Digits(integer = 4, fraction = 2)
    @Column(name = "SPEED_SECTION_LIMIT", precision = 6, scale = 2, nullable = false)
    private BigDecimal speedSectionLimit;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "TIME_PASSAGE_SECTION_SEC", nullable = false)
    private Integer timePassageSectionInSec;

    @Column(name = "TIME_PASSAGE_TOLERANCE_SEC")
    private Integer timePassageToleranceInSec;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "TIME_PASSAGE_LIMIT_SEC", nullable = false)
    private Integer timePassageLimitInSec;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "TIME_PASSAGE_MAX_SEC", nullable = false)
    private Integer timePassageMaxInSec;

    @PostLoad
    @PostPersist
    @PostUpdate
    public void syncSectionCode() {
        if (section != null && section.getCodeSection() != null) {
            this.codeSection = section.getCodeSection();
        }

        // Ensure speedSectionLimit is calculated after loading or updating
        populateSpeedSection();

        // Ensure time passage section is populated after loading or updating
        populateTimePassageSection();
    }


    @Override
    @PrePersist
    public void prePersist() {
        super.prePersist();

        // Ensure speedSectionLimit is calculated before persisting
        populateSpeedSection();

        // Ensure time passage section is populated before persisting
        populateTimePassageSection();
    }

    private void populateTimePassageSection() {

        if (section != null) {

            if (timePassageLimitInSec != null) return;

            BigDecimal VALUE_HDS = BigDecimal.valueOf(3.6);
            BigDecimal distanceSectionM = section.getDistanceSectionM();

            if (distanceSectionM != null && this.speedSectionLimit != null) {
                var timeCalculated = distanceSectionM.divide(this.speedSectionLimit, 2, RoundingMode.DOWN).multiply(VALUE_HDS);
                this.timePassageSectionInSec = timeCalculated.setScale(0, RoundingMode.HALF_UP).intValue();
            } else {
                this.timePassageSectionInSec = 0;
            }

            if (timePassageToleranceInSec != null && timePassageToleranceInSec > 0) {
                timePassageLimitInSec = timePassageSectionInSec + timePassageToleranceInSec;
            }
        }

    }

    private void populateSpeedSection() {

        if (speedSectionLimit != null) return;

        BigDecimal tolerance = BigDecimal.ZERO;
        BigDecimal toleranceKmh = (speedToleranceKmh != null && speedToleranceKmh > 0)
                ? BigDecimal.valueOf(speedToleranceKmh)
                : null;
        BigDecimal tolerancePercent = (speedTolerancePercent != null && speedTolerancePercent > 0)
                ? speedAuthorized.multiply(BigDecimal.valueOf(speedTolerancePercent))
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP).setScale(0, RoundingMode.DOWN)
                : null;
        if (toleranceKmh != null && tolerancePercent != null) {
            tolerance = toleranceKmh.min(tolerancePercent);
        }
        speedSectionLimit = speedAuthorized.add(tolerance);
    }


}
