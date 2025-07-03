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
public class SectionInfoOut implements Serializable {
    private String codeSection;
    private String labelSection;
    private BigDecimal distanceKm;
    private BigDecimal distanceM;
    private Integer distanceToleranceM;
    private BigDecimal distanceSectionM;
    private RadarInfoOut radarInput;
    private RadarInfoOut radarOutput;
    private SpeedSectionInfoOut speedSection;
    //private boolean enabled;
}
