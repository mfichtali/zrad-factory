package ma.zrad.system.batch.common.enums;

public enum EventProcessingStatusEnum {

    PENDING, // Event is pending processing
    PROCESSED, // Event has been processed successfully
    IN_PROGRESS, // Event in progress, not yet terminate
    NO_DATA,
    FAILED, // Event processing failed
    COMPLETED, // Event processing completed

    PROCESSED_TO_ANOMALY, // Event has been processed and identified as an anomaly
    PROCESSED_TO_INFRACTION, // Event has been processed and identified as an infraction
    ;
}
