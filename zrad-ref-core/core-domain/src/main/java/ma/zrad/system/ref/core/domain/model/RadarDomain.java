package ma.zrad.system.ref.core.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
public class RadarDomain implements Serializable {

    private UUID id;
    private String codeRadar;
    private String labelLocalisation;
    private boolean enabled;

}
