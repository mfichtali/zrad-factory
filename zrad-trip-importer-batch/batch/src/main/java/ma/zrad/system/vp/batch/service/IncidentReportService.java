package ma.zrad.system.vp.batch.service;

import ma.zrad.system.batch.common.enums.EventBatchContextEnum;
import ma.zrad.system.vp.batch.config.AppLinkerProperties;
import ma.zrad.system.vp.batch.domain.model.VehiclePassageWrapper;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Service
@StepScope
public class IncidentReportService {

    @Autowired
    private AppLinkerProperties appLinkerProperties;

    public void writeChunkIncidentReport(List<VehiclePassageWrapper> errorLines, Path tmpIncidentDir, int chunkIndex) {
        Path incidentFile = tmpIncidentDir.resolve(String.format(appLinkerProperties.getBatchFileIncidentPatternFilePerChunk(),
                EventBatchContextEnum.ZRAD01.getValue(), chunkIndex));
        try (BufferedWriter writer = Files.newBufferedWriter(incidentFile, StandardOpenOption.CREATE)) {
            for (VehiclePassageWrapper itemError : errorLines) {
                var rawLine = itemError.getRawLine();
                var errorMessage = itemError.getErrorMessage();
                writer.write(String.format("%s | [Type: %s] [Reason: %s]", rawLine, itemError.getErrorType(), errorMessage));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write incident file for chunk " + chunkIndex, e);
        }

    }
}
