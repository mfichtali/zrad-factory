package ma.zrad.system.vp.batch.business.service;

import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.annotations.BusinessService;
import ma.zrad.system.batch.common.enums.SourceFileImporterEnum;
import ma.zrad.system.batch.common.pojo.BatchFileContext;
import ma.zrad.system.vp.batch.domain.model.FileImporterMetricsDomain;
import ma.zrad.system.vp.batch.domain.port.in.FileImporterService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;

@Slf4j
@BusinessService("localFileImporterService")
public class LocalFileImporterBusinessService implements FileImporterService {

    private static final String LOCAL_SOURCE_FILE = "C:\\mfichtali\\projects\\zrad-system\\docker-files-factory";
    private static final String ENV_LOCAL_SOURCE_DATA_DIR = "LOCAL_SOURCE_DATA_DIR";

    @Override
    public Path importFile(final BatchFileContext fileCtx) {
        try {
            String sourceDirEnv = System.getenv(ENV_LOCAL_SOURCE_DATA_DIR);
            if (sourceDirEnv == null) {
                log.warn("Environment variable {} is not set, using default directory: {}", ENV_LOCAL_SOURCE_DATA_DIR, LOCAL_SOURCE_FILE);
                sourceDirEnv = LOCAL_SOURCE_FILE;
            }

            Path sourceDir = Paths.get(sourceDirEnv);
            if (!Files.isDirectory(sourceDir)) {
                throw new IllegalStateException("Source directory does not exist or is not a directory: " + sourceDir);
            }

            List<Path> sortedFiles = Files.list(sourceDir)
                    .filter(Files::isRegularFile)
                    .sorted(Comparator.comparing(file -> {
                        try {
                            return Files.getLastModifiedTime(file).toMillis();
                        } catch (IOException e) {
                            return Long.MAX_VALUE;
                        }
                    })).toList();

            if (sortedFiles.isEmpty()) {
                log.info("No files found in directory: {}", sourceDir);
                return null;
            }
            Path fileToProcess = sortedFiles.getFirst();
            Path inputDirPath = Paths.get(fileCtx.getInputDir());
            Path target = inputDirPath.resolve(fileToProcess.getFileName());
            Files.copy(fileToProcess, target, StandardCopyOption.REPLACE_EXISTING);
            Files.delete(fileToProcess);
            log.info("File imported and deleted: {}", fileToProcess);
            return target;
        } catch (IOException e) {
            log.error("Error importing file: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void postImportCleanup(final BatchFileContext fileCtx, final FileImporterMetricsDomain fileMetricData) {
        log.info("Post-import cleanup for local file importer is not required.");
    }

    @Override
    public SourceFileImporterEnum sourceImportFile() {
        return SourceFileImporterEnum.LOCAL;
    }
}
