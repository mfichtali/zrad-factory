package ma.zrad.system.vp.batch.service;

import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.enums.EventBatchContextEnum;
import ma.zrad.system.batch.common.records.FileMetadataRecord;
import ma.zrad.system.batch.common.utils.DateUtils;
import ma.zrad.system.vp.batch.config.AppLinkerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

@Slf4j
@Service
public class IncidentFileMergerService {

    @Autowired
    private AppLinkerProperties appLinkerProperties;

    public Path mergeIncidentFiles(String tmpIncidentDir, FileMetadataRecord fileMetadataRecord, int maxLengthErrorLine) throws IOException {

        var pathTmpIncidentDir = Paths.get(tmpIncidentDir);
        var mergedFile = pathTmpIncidentDir.resolve(String.format(appLinkerProperties.getBatchFileIncidentPatternFileFusion(),
                EventBatchContextEnum.ZRAD01.getValue(),
                fileMetadataRecord.regionCode(),
                fileMetadataRecord.sectionRoadCode(),
                DateUtils.toStringFormat(fileMetadataRecord.dateDetection()),
                fileMetadataRecord.sequence()));
        fusionTemporaryIncidentFiles(maxLengthErrorLine, mergedFile, pathTmpIncidentDir);
        deleteTemporaryIncidentFiles(pathTmpIncidentDir, mergedFile);
        return mergedFile;

    }

    private static void fusionTemporaryIncidentFiles(final int maxLengthErrorLine, final Path mergedFile, final Path pathTmpIncidentDir) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(mergedFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

            try (Stream<Path> files = Files.list(pathTmpIncidentDir)) {
                files
                    .filter(Files::isRegularFile)
                        .filter(path -> !path.equals(mergedFile))
                    .forEach(path -> {
                        try (Stream<String> lines = Files.lines(path)) {
                            lines.forEach(line -> {
                                try {
                                    String[] tabs = line.split("\\|");
                                    if (tabs.length == 2) {
                                        writer.write(String.format("%-" + maxLengthErrorLine + "s | %s", tabs[0], tabs[1]));
                                        writer.newLine();
                                    }
                                } catch (IOException e) {
                                    log.warn("Error writing line to merged file: {}", e.getMessage(), e);
                                    //throw new UncheckedIOException(e);
                                }
                            });
                        }catch (IOException e) {
                            log.warn("Error reading file {}: {}", path, e.getMessage(), e);
                            //throw new UncheckedIOException(e);
                        }
                    });
            }
        }
    }

    private static void deleteTemporaryIncidentFiles(final Path pathTmpIncidentDir, final Path mergedFile) throws IOException {
        try (Stream<Path> filesToDelete = Files.list(pathTmpIncidentDir)) {
            filesToDelete
                    .filter(Files::isRegularFile)
                    .filter(path -> !path.equals(mergedFile))
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            log.warn("Failed to delete file: {} => {}", path, e.getMessage(), e);
                        }
                    });
        }
    }
}
