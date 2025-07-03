package ma.zrad.system.ref.core.infra.persistence.jpa.entity.base;

import jakarta.persistence.*;
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
    @Column(name = "created_by", length = 50, updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "creation_date", updatable = false, nullable = false)
    private LocalDateTime creationDate;

    @LastModifiedBy
    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @PrePersist
    public void prePersist() {
        this.creationDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updateDate = LocalDateTime.now();
        if (getCreationDate() == null) {
            setCreationDate(LocalDateTime.now());
        }
    }

}
