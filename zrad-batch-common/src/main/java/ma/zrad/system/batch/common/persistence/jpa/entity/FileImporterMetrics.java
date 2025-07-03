package ma.zrad.system.batch.common.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ma.zrad.system.batch.common.enums.SourceFileImporterEnum;
import ma.zrad.system.batch.common.persistence.jpa.entity.base.AbstractZradEntity;
import ma.zrad.system.batch.common.utils.TableNameUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents metrics related to file import operations in the system.
 * T02 : FileImporterMetrics
 */

@Entity
@Table(name = TableNameUtils.T_FILE_IMPORTER_METRICS, schema = TableNameUtils.ZRAD_DEFAULT_SCHEMA)
@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(doNotUseGetters = true, onlyExplicitlyIncluded = true)
@DynamicUpdate
@DynamicInsert
public class FileImporterMetrics extends AbstractZradEntity {

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "EVENT_ID", nullable = false)
    private UUID eventId;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Enumerated(EnumType.STRING)
    @Column(name = "CO_SOURCE_FILE_IMPORTER", nullable = false)
    private SourceFileImporterEnum sourceFilename;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "ORIGIN_FILENAME", nullable = false)
    private String originFilename;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "LD_FILE_IMPORT", nullable = false)
    private LocalDate fileImportDate;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "JOB_ID",nullable = false)
    private long jobId;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "JOB_NAME",nullable = false)
    private String jobName;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "STEP_NAME",nullable = false)
    private String stepName;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "LDT_JOB_EXECUTION_START", nullable = false)
    private LocalDateTime jobExecutionStartTime;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "LDT_JOB_EXECUTION_END", nullable = false)
    private LocalDateTime jobExecutionEndTime;

    @Transient
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private long executionDurationSeconds;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "TOTAL_LINE_READ", nullable = false)
    private int totalRead;

    @Column(name = "TOTAL_LINE_VALID", nullable = false)
    private int totalValid;

    @Column(name = "TOTAL_LINE_INVALID", nullable = false)
    private int totalInvalid;

    @Column(name = "INCIDENT_FILENAME")
    private String incidentFilename;

    @Column(name = "SEQUENCE_FILE", nullable = false)
    private String sequenceFile;

    public long getExecutionDurationSeconds() {
        if (jobExecutionStartTime != null && jobExecutionEndTime != null) {
            return Duration.between(jobExecutionStartTime, jobExecutionEndTime).getSeconds();
        }
        return 0;
    }
}
