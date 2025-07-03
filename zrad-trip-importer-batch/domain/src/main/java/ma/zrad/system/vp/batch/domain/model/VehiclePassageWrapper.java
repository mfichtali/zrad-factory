package ma.zrad.system.vp.batch.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ma.zrad.system.batch.common.enums.BatchErrorTypeEnum;

@AllArgsConstructor
@Getter
public class VehiclePassageWrapper {

    private boolean valid;
    private VehiclePassageImporterDomain domain;
    private String rawLine;
    private BatchErrorTypeEnum errorType;
    private String errorMessage;

    public static VehiclePassageWrapper valid(VehiclePassageImporterDomain domain) {
        return new VehiclePassageWrapper(true, domain, null, null, null);
    }

    public static VehiclePassageWrapper invalid(String rawLine, String errorMessage, BatchErrorTypeEnum errorType) {
        return new VehiclePassageWrapper(false, null, rawLine, errorType, errorMessage);
    }

}
