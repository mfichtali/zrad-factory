package ma.zrad.system.batch.common.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
public class RadarInfoOut implements Serializable {
    private String codeRadar;
    private String labelLocalisation;
    //private boolean enabled;
}
