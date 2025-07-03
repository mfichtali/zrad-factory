package ma.zrad.system.batch.common.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ma.zrad.system.batch.common.enums.EventBatchContextEnum;
import ma.zrad.system.batch.common.enums.EventBatchStatusEnum;
import ma.zrad.system.batch.common.enums.EventBusinessTypeEnum;
import ma.zrad.system.batch.common.enums.EventProcessingStatusEnum;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
public class EventBusinessDomain implements Serializable {

    private UUID id;
    private Long version;
    private EventBatchContextEnum eventBatchContextEnum;
    private EventBusinessTypeEnum eventBusinessTypeEnum;
    private String eventLabel;
    private long jobExecutionId;
    private String jobName;

    private LocalDateTime startBatchEvent;
    private LocalDateTime endBatchEvent;

    private EventBatchStatusEnum eventBatchStatus;
    private EventProcessingStatusEnum eventProcessingStatus;

    private Integer processedCount;
    private String occurredErrorMessage;
    private String metadata;

    //private List<EventBusinessStatusHistoryDomain> eventStatusHistories = new ArrayList<>();
    private EventBusinessDomain eventParent;

    public static EventBusinessDomain buildStartedEvent(
            EventBatchContextEnum codeEventBatchCtx) {
        return new EventBusinessDomain()
                .setEventBatchContextEnum(codeEventBatchCtx)
                .setStartBatchEvent(LocalDateTime.now())
                .setEventBatchStatus(EventBatchStatusEnum.STARTED)
                .setEventProcessingStatus(EventProcessingStatusEnum.PENDING);
    }

//    public EventBusinessStatusHistoryDomain getLastEventStatusHistory() {
//        return eventStatusHistories.stream()
//                .max(Comparator.comparing(EventBusinessStatusHistoryDomain::getTsStartStatus))
//                .orElse(null);
//    }
//
//    public void addEventStatusHistory(EventBusinessStatusHistoryDomain statusHistory) {
//        if (statusHistory != null) {
//            this.eventStatusHistories.add(statusHistory);
//        }
//    }
}
