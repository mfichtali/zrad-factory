package ma.zrad.system.vp.batch.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ma.zrad.system.batch.common.enums.SourceFileImporterEnum;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
public class FileImporterMetricsDomain implements Serializable {
    private UUID eventId;
    private SourceFileImporterEnum sourceFilename; // FTP, LOCAL, S3, etc.
    private String originFilename;
    private LocalDate fileImportDate;
    private long jobId;
    private String jobName;
    private String stepName;
    private LocalDateTime jobExecutionStartTime;
    private LocalDateTime jobExecutionEndTime;
    private int totalRead;
    private int totalValid;
    private int totalInvalid;
    private String incidentFilename;
    private String sequenceFile;
}
