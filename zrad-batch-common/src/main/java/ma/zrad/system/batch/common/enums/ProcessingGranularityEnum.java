package ma.zrad.system.batch.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProcessingGranularityEnum {

    D("Daily"), M("Monthly");

    private final String description;
    
}
