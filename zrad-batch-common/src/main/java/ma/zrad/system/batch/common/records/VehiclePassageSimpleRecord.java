package ma.zrad.system.batch.common.records;

import ma.zrad.system.batch.common.enums.CodeImportStatusEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record VehiclePassageSimpleRecord (
        UUID id,
        String regionCode,
        String sectionRoadCode,
        String rnVehicle,
        LocalDateTime passageTime,
        BigDecimal speedDetection,
        String radarDetectionCode,
        CodeImportStatusEnum status
) implements Serializable {

    public static VehiclePassageSimpleRecord from(
            UUID id,
            String regionCode,
            String sectionRoadCode,
            String rnVehicle,
            LocalDateTime passageTime,
            BigDecimal speedDetection,
            String radarDetectionCode,
            String status
    ) {
        return new VehiclePassageSimpleRecord(
                id,
                regionCode,
                sectionRoadCode,
                rnVehicle,
                passageTime,
                speedDetection,
                radarDetectionCode,
                CodeImportStatusEnum.fromCode(status)
        );
    }
}
