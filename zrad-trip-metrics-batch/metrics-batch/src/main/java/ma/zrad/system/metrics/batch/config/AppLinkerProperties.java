package ma.zrad.system.metrics.batch.config;

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

}
