package ma.zrad.system.stats.batch.infra.persistence.jpa.repository;

import ma.zrad.system.batch.common.persistence.jpa.entity.VehicleTripInfraction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VehicleTripInfractionJpaRepository extends JpaRepository<VehicleTripInfraction, UUID> {

}
