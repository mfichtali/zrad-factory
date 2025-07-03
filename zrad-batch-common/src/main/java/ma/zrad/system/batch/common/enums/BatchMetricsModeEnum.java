package ma.zrad.system.batch.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BatchMetricsModeEnum {

    DAILY("d"),
    MONTHLY("m"),
    QUARTERLY("q"),
    YEARLY("y");

    private final String label;
}
