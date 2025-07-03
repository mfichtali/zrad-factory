package ma.zrad.system.batch.common.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
public class SpeedSectionInfoOut implements Serializable {
    private BigDecimal speedAuthorized;
    private Integer speedToleranceKmh;
    private Integer speedTolerancePercent;
    private BigDecimal speedSectionLimit;

    private Integer timePassageSectionInSec;
    private Integer timePassageToleranceInSec;
    private Integer timePassageLimitInSec;
    private Integer timePassageMaxInSec;
}
