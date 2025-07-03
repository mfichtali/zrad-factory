package ma.zrad.system.stats.batch.infra.persistence.jpa.repository;

import ma.zrad.system.batch.common.persistence.jpa.entity.VehicleTripAnomaly;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VehicleTripAnomalyJpaRepository extends JpaRepository<VehicleTripAnomaly, UUID> {

}
