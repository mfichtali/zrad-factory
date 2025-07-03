package ma.zrad.system.batch.common.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ma.zrad.system.batch.common.enums.CodeTypeIssueEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
public class VehicleTripAnalysisDomain implements Serializable {

    private UUID id;
    private Long version;
    private String regionCode;
    private String sectionCode;
    private String rnVehicle;
    private LocalDateTime passageEntryTime;
    private LocalDateTime passageExitTime;

    @Getter(AccessLevel.NONE)
    private Integer tripDurationInSeconds;
    private BigDecimal speedEntryDetection;
    private BigDecimal speedExitDetection;

    @Getter(AccessLevel.NONE)
    private BigDecimal averageSpeedTrip;
    private BigDecimal speedSectionLimit;
    private BigDecimal speedSectionCalculated;
    private boolean anomalyDetected;
    private boolean infractionDetected;

    private UUID idEventImporter;
    private UUID idEventTrip;

    @Setter(AccessLevel.NONE)
    private CodeTypeIssueEnum issue;
    private String codeIssue;
    private String messageIssue;

    public Integer getTripDurationInSeconds() {
        if (tripDurationInSeconds != null) {
            return tripDurationInSeconds;
        }
        if(passageEntryTime != null && passageExitTime != null) {
            tripDurationInSeconds = (int) Duration.between(passageEntryTime, passageExitTime).getSeconds();
        }
        return tripDurationInSeconds;
    }

    public BigDecimal getAverageSpeedTrip() {
        if (speedEntryDetection != null && speedExitDetection != null) {
            averageSpeedTrip = speedEntryDetection.add(speedExitDetection).divide(BigDecimal.valueOf(2), 2, RoundingMode.DOWN);
        }
        return averageSpeedTrip;
    }

    public void setIssue(final CodeTypeIssueEnum issue) {
        this.issue = issue;
        if (issue != null) {
            this.codeIssue = issue.getCode();
        }
    }

    public LocalDateTime getLdtPassage() {
        if(passageEntryTime != null) return passageEntryTime;
        return passageExitTime;
    }
}
