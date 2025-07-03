package ma.zrad.system.trip.batch.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class AppLinkerProperties {

    @Value("${application.batch.chuck-size}")
    private int batchChunkSize;

    @Value("${application.batch.partition.grid-size}")
    private int batchPartitionGridSize;

    @Value("${application.batch.trip.validation.minimum-duration-seconds}")
    private int tripValidationMinimumDurationSec;
    
    @Value("${application.batch.trip.validation.speed-threshold}")
    private int tripValidationThresholdSpeed;

}
