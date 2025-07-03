package ma.zrad.system.batch.common.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public enum CodeRadarDetectionEnum {

    I, O, N;

    public static CodeRadarDetectionEnum fromCodeOrDefault(String code) {
        if(StringUtils.isBlank(code)) {
            return N;
        }
        return Arrays.stream(CodeRadarDetectionEnum.values())
                .filter(detection -> detection.name().equalsIgnoreCase(code))
                .findFirst()
                .orElse(N);
    }

}
