package ma.zrad.system.vp.batch.writer;

import lombok.RequiredArgsConstructor;
import ma.zrad.system.batch.common.pojo.BatchFileContext;
import ma.zrad.system.batch.common.utils.BatchConstantUtils;
import ma.zrad.system.vp.batch.config.AppLinkerProperties;
import ma.zrad.system.vp.batch.domain.model.VehiclePassageImporterDomain;
import ma.zrad.system.vp.batch.domain.model.VehiclePassageWrapper;
import ma.zrad.system.vp.batch.domain.port.out.VehiclePassageRepositoryPort;
import ma.zrad.system.vp.batch.service.ImporterStatisticsCollectorService;
import ma.zrad.system.vp.batch.service.IncidentReportService;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
@StepScope
@RequiredArgsConstructor
public class VehiclePassageCompositeWriter implements ItemWriter<VehiclePassageWrapper> {

    private final VehiclePassageRepositoryPort vehiclePassageRepositoryPort;

    private final IncidentReportService incidentReportService;

    private final ImporterStatisticsCollectorService statisticsCollector;

    private final AppLinkerProperties appLinkerProperties;

    @Value("#{jobExecutionContext['" + BatchConstantUtils.BATCH_ROOT_FILE_CTX + "']}")
    private BatchFileContext batchFileContext;

    private int chunkIndex = 0;

    @Override
    public void write(final Chunk<? extends VehiclePassageWrapper> items) throws Exception {

        chunkIndex++;
        List<VehiclePassageImporterDomain> validItems = new ArrayList<>();
        List<VehiclePassageWrapper> invalidItems = new ArrayList<>();
        items.forEach(item -> {
            if (item.isValid()) {
                validItems.add(item.getDomain());
            } else {
                invalidItems.add(item);
            }
        });

        // Save valid items to the repository
        if (!validItems.isEmpty()) {
            vehiclePassageRepositoryPort.saveVehiclePassages(validItems);
            statisticsCollector.addValid(validItems.size());
        }

        // Write incident report for invalid items into file
        if(!invalidItems.isEmpty()) {
            var maxLengthCurrentChunk = invalidItems.stream().map(VehiclePassageWrapper::getRawLine)
                    .mapToInt(String::length).max().orElse(appLinkerProperties.getBatchFileDefaultMaxLengthLine());
            statisticsCollector.updateMaxLengthErrorLine(maxLengthCurrentChunk);
            incidentReportService.writeChunkIncidentReport(invalidItems, Paths.get(batchFileContext.getOutputDir()), chunkIndex);
            statisticsCollector.addInvalid(invalidItems.size());
        }
    }
}
