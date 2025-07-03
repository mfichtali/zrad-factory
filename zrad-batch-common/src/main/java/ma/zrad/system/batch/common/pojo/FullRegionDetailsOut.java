package ma.zrad.system.batch.common.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Set;

@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
public class FullRegionDetailsOut implements Serializable {
    private String codeRegion;
    private String labelRegion;
    Set<SectionInfoOut> sections;
}
