package ma.zrad.system.batch.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum PassageTimeSectionRefEnum {

    INF2000M(1, 2000),
    INF5000KM(2, 5000),
    DEFAULT_KM(1, 1000)
    ;

    private final int maxHour;
    private final int distance;

    public static String findMaxRefHourByDistanceAsString(BigDecimal distance) {
        if(distance == null) return DEFAULT_KM.maxHour+"h";
        return Arrays.stream(PassageTimeSectionRefEnum.values())
                .filter(p -> p.getDistance() > distance.intValue())
                .map(PassageTimeSectionRefEnum::getMaxHour)
                .map(n -> n+"h")
                .findFirst()
                .orElse(DEFAULT_KM.maxHour+"h");
    }

    public static int findMaxRefSecondByDistance(BigDecimal distance) {
        if(distance == null) return DEFAULT_KM.maxHour * 3600;
        return Arrays.stream(PassageTimeSectionRefEnum.values())
                .filter(p -> p.getDistance() > distance.intValue())
                .map(PassageTimeSectionRefEnum::getMaxHour)
                .map(n -> n  * 3600)
                .findFirst()
                .orElse(DEFAULT_KM.maxHour * 3600);
    }

}
