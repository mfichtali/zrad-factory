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
import ma.zrad.system.batch.common.enums.CodeImportStatusEnum;
import ma.zrad.system.batch.common.enums.CodeRadarDetectionEnum;
import ma.zrad.system.batch.common.persistence.jpa.entity.base.AbstractZradEntity;
import ma.zrad.system.batch.common.utils.TableNameUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a vehicle passage record imported into the system.
 * T10 : VehiclePassageImporter
 */

@Entity
@Table(name = TableNameUtils.T_VEHICLE_PASSAGE_IMPORTER, schema = TableNameUtils.ZRAD_DEFAULT_SCHEMA)
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(doNotUseGetters = true, onlyExplicitlyIncluded = true)
@DynamicUpdate
@DynamicInsert
public class VehiclePassageImporter extends AbstractZradEntity {

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "CO_REGION", nullable = false)
    private String regionCode;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "CO_SECTION_ROAD", nullable = false)
    private String sectionRoadCode;

    //TODO Accept only 2 value I / O
    @ToString.Include
    @EqualsAndHashCode.Include
    @Enumerated(EnumType.STRING)
    @Column(name = "CO_RADAR_DETECTION", nullable = false)
    private CodeRadarDetectionEnum radarDetectionCode;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "RN_VEHICLE", nullable = false)
    private String rnVehicle;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Digits(integer = 8, fraction = 2)
    @Column(name = "SPEED_PASSAGE_KM", precision = 10, scale = 2)
    private BigDecimal speedPassage;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "LDT_PASSAGE_DATE", nullable = false)
    private LocalDateTime datePassage;

    @Enumerated(EnumType.STRING)
    @Column(name = "CO_STATUS_IMPORT", nullable = false)
    private CodeImportStatusEnum statusImport;

    @Column(name = "TXT_NOTE_ERR_IMPORT")
    private String noteImport;

    @Column(name = "ID_EVENT", nullable = false)
    private UUID eventId;

}
