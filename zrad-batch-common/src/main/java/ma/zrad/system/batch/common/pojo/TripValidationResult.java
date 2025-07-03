package ma.zrad.system.batch.common.pojo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ma.zrad.system.batch.common.enums.CodeTypeIssueEnum;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
public class TripValidationResult {
    private boolean hasAnomaly;
    private boolean hasInfraction;
    private CodeTypeIssueEnum issue;
    private BigDecimal speedCalculated;

    @Getter(AccessLevel.NONE)
    private String messageOverride;

    public String getMessageOverride() {
        if(StringUtils.isBlank(messageOverride)) {
            this.messageOverride = issue.getMessage();
        }
        return messageOverride;
    }
}
