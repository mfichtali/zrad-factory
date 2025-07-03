package ma.zrad.system.batch.common.records;

import ma.zrad.system.batch.common.enums.EventProcessingStatusEnum;

import java.util.UUID;

public record EventRemainingCounterRecord(
    UUID eventId,
    EventProcessingStatusEnum status,
    long remainingPendingCounter
) {
}
