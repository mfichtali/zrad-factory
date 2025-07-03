package ma.zrad.system.ref.core.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
public class SpeedSectionDomain implements Serializable {

    private UUID id;
    private String codeSection;
    
    private BigDecimal speedAuthorized;
    private Integer speedToleranceKmh;
    private Integer speedTolerancePercent;
    private BigDecimal speedSectionLimit;

    private Integer timePassageSectionInSec;
    private Integer timePassageToleranceInSec;
    private Integer timePassageLimitInSec;
    private Integer timePassageMaxInSec;
}
