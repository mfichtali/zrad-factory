package ma.zrad.system.vp.batch.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
public class VehiclePassageImporterDomain implements Serializable {
    private String rnVehicle;
    private LocalDateTime passageTime;
    private String regionCode;
    private String sectionRoadCode;
    private String radarDetectionCode;
    private BigDecimal speedDetection;
    private UUID eventId;
}
