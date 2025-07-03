package ma.zrad.system.stats.batch.infra.persistence.jpa.repository;

import ma.zrad.system.batch.common.persistence.jpa.entity.VehicleTripValid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VehicleTripValidJpaRepository extends JpaRepository<VehicleTripValid, UUID> {

}
