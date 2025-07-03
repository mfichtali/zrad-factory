package ma.zrad.system.batch.common.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ma.zrad.system.batch.common.enums.EventProcessingStatusEnum;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
public class EventBusinessStatusHistoryDomain {
    private UUID id;
    private Long version;
    private LocalDateTime tsStartStatus;
    private LocalDateTime tsEndStatus;
    private EventProcessingStatusEnum eventProcessingStatus;
    private EventBusinessDomain eventBusinessDomain;
}
