package ma.zrad.system.batch.common.pojo;

import lombok.Getter;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Getter
public class BatchFileContext implements Serializable {

    private final String tempDir;
    private final String inputDir;
    private final String outputDir;
    private final String reportDir;

    public BatchFileContext(String rootPath, String batchCtx) throws IOException {

        Path rootPathObj = Paths.get(rootPath);
        Files.createDirectories(rootPathObj);
        Path tempDirPath = Files.createTempDirectory(rootPathObj, String.format("b-tmp-%s-", batchCtx.toLowerCase()));
        Path inputDirPath = tempDirPath.resolve("input");
        Path outputDirPath = tempDirPath.resolve("output");
        Path reportDirPath = tempDirPath.resolve("report");

        Files.createDirectories(inputDirPath);
        Files.createDirectories(outputDirPath);
        Files.createDirectories(reportDirPath);

        this.tempDir = tempDirPath.toString();
        this.inputDir = inputDirPath.toString();
        this.outputDir = outputDirPath.toString();
        this.reportDir = reportDirPath.toString();
    }

    public void cleanup() {
        try {
            Path tempPath = Paths.get(tempDir);
            if (Files.exists(tempPath)) {
                FileSystemUtils.deleteRecursively(tempPath);
            }
        } catch (IOException e) {
            System.err.println("Error when cleanup temp directory : " + e.getMessage());
        }
    }

    public String retrieveIncidentFile() {
        Path path = Paths.get(getOutputDir());
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            return null;
        }
        try (Stream<Path> files = Files.list(path)) {
            return files
                    .filter(Files::isRegularFile)
                    .map(p -> p.getFileName().toString())
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read directory: " + getOutputDir(), e);
        }
    }

    public String retrieveReportFile() {
        Path path = Paths.get(getReportDir());
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            return null;
        }
        try (Stream<Path> files = Files.list(path)) {
            return files
                    .filter(Files::isRegularFile)
                    .map(p -> p.getFileName().toString())
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read directory: " + getReportDir(), e);
        }
    }

}
