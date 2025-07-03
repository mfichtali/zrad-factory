package ma.zrad.system.batch.common.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import ma.zrad.system.batch.common.enums.EventBatchContextEnum;
import ma.zrad.system.batch.common.enums.EventBatchStatusEnum;
import ma.zrad.system.batch.common.enums.EventBusinessTypeEnum;
import ma.zrad.system.batch.common.enums.EventProcessingStatusEnum;
import ma.zrad.system.batch.common.persistence.jpa.entity.base.AbstractZradEntity;
import ma.zrad.system.batch.common.utils.TableNameUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

/**
 * Represents a business event in the system, capturing details about the event's context,
 * type, status, and associated metadata.
 * T01 : EventBusiness
 */
@Entity
@Table(name = TableNameUtils.T_EVENT_BUSINESS, schema = TableNameUtils.ZRAD_DEFAULT_SCHEMA)
@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(doNotUseGetters = true, onlyExplicitlyIncluded = true)
@DynamicUpdate
@DynamicInsert
public class EventBusiness extends AbstractZradEntity {

    @ToString.Include
    @EqualsAndHashCode.Include
    @Enumerated(EnumType.STRING)
    @Column(name = "CO_EVENT_BATCH_CTX", nullable = false)
    private EventBatchContextEnum eventBatchContextEnum;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Enumerated(EnumType.STRING)
    @Column(name = "CO_EVENT", nullable = false)
    private EventBusinessTypeEnum eventBusinessTypeEnum;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "LB_EVENT", nullable = false)
    private String eventLabel;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "JOB_EXECUTION_ID", nullable = false)
    private long jobExecutionId;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "JOB_NAME", nullable = false)
    private String jobName;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "LDT_START_BATCH_EVENT", nullable = false)
    private LocalDateTime startBatchEvent;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "LDT_END_BATCH_EVENT")
    private LocalDateTime endBatchEvent;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Enumerated(EnumType.STRING)
    @Column(name = "CO_EVENT_BATCH_STATUS", nullable = false)
    private EventBatchStatusEnum eventBatchStatus;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Enumerated(EnumType.STRING)
    @Column(name = "CO_EVENT_PROCESSING_STATUS", nullable = false)
    private EventProcessingStatusEnum eventProcessingStatus;

    @ToString.Include
    @Column(name = "PROCESSED_COUNT")
    private Integer processedCount;

    @Column(name = "OCCURRED_ERROR_MESSAGE")
    private String occurredErrorMessage;

    @Column(name = "METADATA", columnDefinition = "TEXT")
    private String metadata;

//    @JsonManagedReference
//    @OrderBy("tsStartStatus DESC")
//    @OneToMany(mappedBy = "eventBusiness", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    private Set<EventBusinessStatusHistory> eventStatusHistories = new HashSet<>(0);

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "EVENT_PARENT_ID", insertable = false, updatable = false)
//    private EventBusiness eventParent;
//
//    @Column(name = "EVENT_PARENT_ID")
//    private UUID eventParentId;

    @Override
    public void syncPreInsert() {
        super.syncPreInsert();
        if(StringUtils.isBlank(eventLabel)) {
            eventLabel = eventBusinessTypeEnum.getLabel();
        }
//        if (eventParent != null) {
//            this.eventParentId = eventParent.getId();
//        }
    }

//    @Override
//    public void syncPreUpdate() {
//        super.syncPreUpdate();
//        if (eventParent != null) {
//            this.eventParentId = eventParent.getId();
//        }
//    }

//    @Override
//    public void syncPostLoad() {
//        super.syncPostLoad();
//        if (eventParent != null) {
//            this.eventParentId = eventParent.getId();
//        }
//    }
}
