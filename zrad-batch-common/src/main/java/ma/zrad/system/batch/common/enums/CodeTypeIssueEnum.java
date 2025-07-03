package ma.zrad.system.batch.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodeTypeIssueEnum {

    ANO000("PASSAGE_NOT_EXPLOITED", "Passage not exploited.", CodeCategory.ANOMALY),
    ANO001("PASSAGE_I_WITHOUT_O", "Vehicle [%s] - Passage at exit point at %s - no trace of a passage at entry point.", CodeCategory.ANOMALY),
    ANO002("MISSING_PASSAGE_O", "Vehicle [%s] Passage detected at entry at %s - no exit passage after %s delay.", CodeCategory.ANOMALY),
    ANO003("INVALID_TIMESTAMP_ORDER", "Vehicle [%s] passage entry must be strictly before exit.", CodeCategory.ANOMALY),
    ANO004("TIME_GAP_TOO_SHORT", "Vehicle [%s] passage duration too short (%ds)", CodeCategory.ANOMALY),
    ANO005("SUSPICIOUS_SPEED", "Vehicle [%s] Average speed of %d km/h - exceeds anomaly threshold (250 km/h).", CodeCategory.ANOMALY),
    ANO006("LONG_STOP_OR_BREAKDOWN", "Vehicle [%s] Duration of %s for %s - exceeds the maximum permitted threshold.", CodeCategory.ANOMALY),


    INF001("PASSAGE_OVER_SPEED_LIMIT", "Vehicle [%s] Average speed of %d km/h exceeds the allowed limit (%d km/h).", CodeCategory.ANOMALY),
    ;

    private final String code;
    private final String message;
    private final CodeCategory category;

    public enum CodeCategory {
        ANOMALY,
        INFRACTION
    }
}
