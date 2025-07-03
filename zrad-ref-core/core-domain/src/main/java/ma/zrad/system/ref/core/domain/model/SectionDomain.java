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
public class SectionDomain implements Serializable {

    private UUID id;
    private String codeSection;
    private String labelSection;
    private String codeRegion;

    private BigDecimal distanceKm;
    private BigDecimal distanceM;
    private Integer distanceToleranceM;
    private BigDecimal distanceSectionM;

    private RadarDomain radarInput;
    private RadarDomain radarOutput;
    private SpeedSectionDomain speedSection;
    private boolean enabled;

}
