package ma.zrad.system.batch.common.records;

import ma.zrad.system.batch.common.enums.CodeImportStatusEnum;

public record AnomalyTripRecord(
        String rnVehicle,
        CodeImportStatusEnum updateStatus,
        String errorMessageAnomaly) {
}
