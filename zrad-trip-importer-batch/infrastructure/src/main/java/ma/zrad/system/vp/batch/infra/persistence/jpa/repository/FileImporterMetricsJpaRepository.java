package ma.zrad.system.vp.batch.infra.persistence.jpa.repository;

import ma.zrad.system.batch.common.persistence.jpa.entity.FileImporterMetrics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FileImporterMetricsJpaRepository extends JpaRepository<FileImporterMetrics, UUID> {

    Optional<FileImporterMetrics> findByEventId(UUID eventCtxIdentifier);
}
