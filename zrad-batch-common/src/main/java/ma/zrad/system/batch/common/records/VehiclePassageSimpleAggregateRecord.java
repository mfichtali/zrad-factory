package ma.zrad.system.batch.common.records;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record VehiclePassageSimpleAggregateRecord(
        String rnVehicle,
        String regionCode,
        String sectionRoadCode,
        UUID idEvent,
        int countInput,
        int countOutput,
        LocalDateTime passageInputTime,
        LocalDateTime passageOutputTime,
        BigDecimal speedInput,
        BigDecimal speedOutput,
        boolean anomalyDetected
) implements Serializable {}
