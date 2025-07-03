package ma.zrad.system.batch.common.persistence.jpa.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity {

    @Version
    private Long version;

    @CreatedBy
    @Column(name = "CREATED_BY", length = 50, updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "CREATION_DATE", updatable = false, nullable = false)
    private LocalDateTime creationDate;

    @LastModifiedBy
    @Column(name = "UPDATED_BY", length = 50)
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "UPDATE_DATE")
    private LocalDateTime updateDate;

    @PrePersist
    public void syncPreInsert() {
        this.creationDate = LocalDateTime.now();
    }

    @PreUpdate
    public void syncPreUpdate() {
        if (this.creationDate == null) {
            this.creationDate = LocalDateTime.now();
        }
        this.updateDate = this.creationDate;
    }

    @PostLoad
    public void syncPostLoad() {
    }

}
