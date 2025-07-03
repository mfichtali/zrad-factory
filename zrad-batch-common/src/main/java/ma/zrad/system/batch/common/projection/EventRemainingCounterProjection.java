package ma.zrad.system.batch.common.projection;

import java.util.UUID;

public interface EventRemainingCounterProjection {

    UUID getEventId();
    String getCoEventProcessingStatus();
    long getRemainingPendingCounter();
}
