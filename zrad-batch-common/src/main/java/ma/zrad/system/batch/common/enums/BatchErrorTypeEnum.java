package ma.zrad.system.batch.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BatchErrorTypeEnum {

    FUNCTIONAL("Functional Error"),
    TECHNICAL("Technical Error");

    private final String label;
}
