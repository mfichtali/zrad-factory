package ma.zrad.system.vp.batch.infra.persistence.jpa.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.vp.batch.domain.model.FileImporterMetricsDomain;
import ma.zrad.system.vp.batch.domain.port.out.FileImporterMetricsRepositoryPort;
import ma.zrad.system.vp.batch.infra.mapper.GlobalMapper;
import ma.zrad.system.vp.batch.infra.persistence.jpa.repository.FileImporterMetricsJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FileImporterMetricsRepositoryAdapter implements FileImporterMetricsRepositoryPort {

    private final GlobalMapper mapper;
    private final FileImporterMetricsJpaRepository fileImporterMetricsJpaRepository;

    @Override
    public void save(final FileImporterMetricsDomain domain) {
        var entity = mapper.fileImporterMetricsToEntity(domain);
        fileImporterMetricsJpaRepository.save(entity);
    }

    @Override
    public FileImporterMetricsDomain findByEventId(final UUID eventCtxIdentifier) {
        if (eventCtxIdentifier == null) {
            log.error("Event context identifier is null");
            return null;
        }
        return fileImporterMetricsJpaRepository.findByEventId(eventCtxIdentifier)
                .map(mapper::fileImporterMetricsToDomain)
                .orElseGet(() -> {
                    log.error("FileImporterMetrics not found for eventId: {}", eventCtxIdentifier);
                    return null;
                });
    }
}
