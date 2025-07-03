package ma.zrad.system.ref.core.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
public class RegionDomain implements Serializable {

    private UUID id;
    private String codeRegion;
    private String labelRegion;
    private Set<SectionDomain> sections;

}
