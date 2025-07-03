package ma.zrad.system.batch.common.enums;

import java.util.Arrays;

public enum CodeImportStatusEnum {

    PENDING,
    PROCESSED,
    FAILED,
    ARCHIVED,
    WARNING,
    PROCESSED_ANOMALY,
    PROCESSED_INFRACTION,
    ;

    public static CodeImportStatusEnum fromCode(String code) {
        return Arrays.stream(CodeImportStatusEnum.values())
                .filter(c -> c.name().equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);
    }
}
