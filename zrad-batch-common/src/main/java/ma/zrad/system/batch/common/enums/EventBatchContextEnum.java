package ma.zrad.system.batch.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventBatchContextEnum {

    ZRAD01("ZRAD01"),
    ZRAD02("ZRAD02"),
    ZRAD03("ZRAD03"),
    ZRAD04("ZRAD04"),
    ;

    private final String value;
}
