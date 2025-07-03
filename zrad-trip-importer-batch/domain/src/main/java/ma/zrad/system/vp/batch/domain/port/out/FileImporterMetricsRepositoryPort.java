package ma.zrad.system.vp.batch.domain.port.out;

import ma.zrad.system.vp.batch.domain.model.FileImporterMetricsDomain;

import java.util.UUID;

public interface FileImporterMetricsRepositoryPort {

    void save(FileImporterMetricsDomain domain);

    FileImporterMetricsDomain findByEventId(UUID eventCtxIdentifier);
}
