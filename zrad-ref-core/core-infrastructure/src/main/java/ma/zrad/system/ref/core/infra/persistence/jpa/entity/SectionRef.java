package ma.zrad.system.ref.core.infra.persistence.jpa.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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

@Entity
@Table(name = TableNameUtils.REF_SECTION, schema = TableNameUtils.ZRAD_DEFAULT_SCHEMA)
@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(doNotUseGetters = true, onlyExplicitlyIncluded = true)
@DynamicUpdate
@DynamicInsert
public class SectionRef extends AbstractZradEntity {

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "CO_SECTION", nullable = false)
    private String codeSection;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "LB_SECTION", nullable = false)
    private String labelSection;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REGION_ID", referencedColumnName = "id", nullable = false)
    private RegionRef region;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "CO_REGION", nullable = false)
    private String codeRegion;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Digits(integer = 4, fraction = 2)
    @Column(name = "DISTANCE_KM", precision = 6, scale = 2)
    private BigDecimal distanceKm;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Digits(integer = 8, fraction = 2)
    @Column(name = "DISTANCE_M", precision = 10, scale = 2)
    private BigDecimal distanceM;

    @Column(name = "DISTANCE_TOLERANCE_M")
    private Integer distanceToleranceM;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Digits(integer = 8, fraction = 2)
    @Column(name = "DISTANCE_SECTION_M", precision = 10, scale = 2, nullable = false)
    private BigDecimal distanceSectionM;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RADAR_INPUT_ID", referencedColumnName = "id", nullable = false)
    private RadarRef radarInput;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "CO_RADAR_INPUT", nullable = false)
    private String codeRadarInput;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RADAR_OUTPUT_ID", referencedColumnName = "id", nullable = false)
    private RadarRef radarOutput;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "CO_RADAR_OUTPUT", nullable = false)
    private String codeRadarOutput;

    @OneToOne(mappedBy = "section", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private SpeedSectionRef speedSection;

    @Column(name = "IS_ENABLED")
    private boolean enabled = true;

    @PostPersist
    @PostLoad
    @PostUpdate
    public void syncPostField() {
        if (region != null && region.getCodeRegion() != null) {
            this.codeRegion = region.getCodeRegion();
        }

        if (radarInput != null && radarInput.getCodeRadar() != null) {
            this.codeRadarInput = radarInput.getCodeRadar();
        }

        if (radarOutput != null && radarOutput.getCodeRadar() != null) {
            this.codeRadarOutput = radarOutput.getCodeRadar();
        }
    }

    @Override
    @PrePersist
    public void prePersist() {
        super.prePersist();

        if (distanceKm != null) {
            this.distanceM = distanceKm.multiply(BigDecimal.valueOf(1000));
        }
        distanceSectionM = distanceToleranceM != null
                ? distanceM.add(BigDecimal.valueOf(distanceToleranceM))
                : distanceM;
    }

    @Override
    @PreUpdate
    public void preUpdate() {
        super.preUpdate();
        if (distanceKm != null) {
            this.distanceM = distanceKm.multiply(BigDecimal.valueOf(1000));
        }
        distanceSectionM = distanceToleranceM != null
                ? distanceM.add(BigDecimal.valueOf(distanceToleranceM))
                : distanceM;
    }
}
