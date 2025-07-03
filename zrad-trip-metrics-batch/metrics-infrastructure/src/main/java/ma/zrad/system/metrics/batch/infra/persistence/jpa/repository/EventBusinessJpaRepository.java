package ma.zrad.system.metrics.batch.infra.persistence.jpa.repository;

import ma.zrad.system.batch.common.persistence.jpa.entity.EventBusiness;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventBusinessJpaRepository extends JpaRepository<EventBusiness, UUID> {

}
