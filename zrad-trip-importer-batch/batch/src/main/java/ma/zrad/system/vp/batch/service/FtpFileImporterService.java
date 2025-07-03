package ma.zrad.system.vp.batch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.enums.SourceFileImporterEnum;
import ma.zrad.system.batch.common.pojo.BatchFileContext;
import ma.zrad.system.vp.batch.config.AppLinkerProperties;
import ma.zrad.system.vp.batch.domain.model.FileImporterMetricsDomain;
import ma.zrad.system.vp.batch.domain.port.in.FileImporterService;
import ma.zrad.system.vp.batch.domain.port.out.FileImporterMetricsRepositoryPort;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;

@Slf4j
@Service("ftpFileImporterService")
@RequiredArgsConstructor
public class FtpFileImporterService implements FileImporterService {

    public static final String PATTERN_FTP_DIR = "%s/%04d/%02d/%02d";
    private final AppLinkerProperties appLinkerProperties;
    private final FileImporterMetricsRepositoryPort fileImporterMetricsRepositoryPort;
    
    @Override
    public Path importFile(final BatchFileContext fileCtx) {
        log.info("Starting FTP file import for context: {}", fileCtx);
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient = connectToFtp();
            ftpClient.changeWorkingDirectory(appLinkerProperties.getFtpServerRemoteDataDir());
            FTPFile[] files = ftpClient.listFiles();

            FTPFile latestFile = Arrays.stream(files)
                    .filter(FTPFile::isFile)
                    .min(Comparator.comparing(FTPFile::getTimestamp))
                    .orElse(null);
            
            if (latestFile != null) {
                String remoteFileName = latestFile.getName();
                Path localFilePath = Paths.get(fileCtx.getInputDir()).resolve(remoteFileName);
                try (OutputStream outputStream = Files.newOutputStream(localFilePath)) {
                    boolean success = ftpClient.retrieveFile(remoteFileName, outputStream);
                    if (!success) {
                        throw new IOException("Failed to download file from FTP.");
                    }
                }
                log.info("File downloaded from FTP: {}", remoteFileName);
                return localFilePath;
            } else {
                log.info("No files found in the FTP directory: {}", appLinkerProperties.getFtpServerRemoteDataDir());
                return null;
            }
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            disconnectFromFtp(ftpClient);
        }
    }

    @Override
    public void postImportCleanup(final BatchFileContext fileCtx, final FileImporterMetricsDomain fileMetricData) {

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient = connectToFtp();
            LocalDate fileImportDate = fileMetricData.getFileImportDate();
            
            // Move file to archived directory
            moveSourceFileToArchivedDir(fileMetricData, fileImportDate, ftpClient);

            // Upload incident file
            uploadToIncidentDir(fileCtx, fileImportDate, ftpClient);

            // Upload report file
            uploadToReportDir(fileCtx, fileImportDate, ftpClient);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            disconnectFromFtp(ftpClient);
        }
    }

    @Override
    public SourceFileImporterEnum sourceImportFile() {
        return SourceFileImporterEnum.FTP;
    }
    
    private void uploadToReportDir(final BatchFileContext fileCtx, final LocalDate fileImportDate, final FTPClient ftpClient) throws IOException {
        
        String tmpReportDir = fileCtx.getReportDir();
        String localReportPath = fileCtx.retrieveReportFile();
        if( localReportPath == null) {
            log.warn("No report file found in report directory: {}", tmpReportDir);
            return; // No report file to upload
        }

        String reportDir = String.format(PATTERN_FTP_DIR,
                appLinkerProperties.getFtpServerRemoteReportDir(),
                fileImportDate.getYear(),
                fileImportDate.getMonthValue(),
                fileImportDate.getDayOfMonth());
        ensureFtpPathExists(ftpClient, reportDir);
        
        File localReportFile = new File(tmpReportDir, localReportPath);
        try (FileInputStream fis = new FileInputStream(localReportFile)) {
            // Construct full remote path (target file on FTP)
            String remoteFilePath = reportDir + "/" + localReportFile.getName();
            boolean success = ftpClient.storeFile(remoteFilePath, fis);
            if (!success) {
                throw new IOException("❌ Failed to upload report file to FTP server");
            } else {
                log.info("✅ Report file uploaded to FTP: {}", remoteFilePath);
            }
        }
    }

    private void uploadToIncidentDir(final BatchFileContext fileCtx, final LocalDate fileImportDate, final FTPClient ftpClient) throws IOException {
        
        String tmpOutputDir = fileCtx.getOutputDir();
        String localIncidentPath = fileCtx.retrieveIncidentFile();
        if( localIncidentPath == null) {
            log.warn("No incident file found in output directory: {}", tmpOutputDir);
            return; // No incident file to upload
        }

        String incidentDir = String.format(PATTERN_FTP_DIR,
                appLinkerProperties.getFtpServerRemoteIncidentDir(),
                fileImportDate.getYear(),
                fileImportDate.getMonthValue(),
                fileImportDate.getDayOfMonth());
        ensureFtpPathExists(ftpClient, incidentDir);
        
        File localIncidentFile = new File(tmpOutputDir, localIncidentPath);
        try (FileInputStream fis = new FileInputStream(localIncidentFile)) {
            // Construct full remote path (target file on FTP)
            String remoteFilePath = incidentDir + "/" + localIncidentFile.getName();
            boolean success = ftpClient.storeFile(remoteFilePath, fis);
            if (!success) {
                throw new IOException("❌ Failed to upload incident file to FTP server");
            } else {
                log.info("✅ Incident file uploaded to FTP: {}", remoteFilePath);
            }
        }
    }

    private void moveSourceFileToArchivedDir(final FileImporterMetricsDomain fileMetricData, final LocalDate fileImportDate, final FTPClient ftpClient) throws IOException {
        // e.g. zrad/archive/2023/10/01
        String archiveDir = String.format(PATTERN_FTP_DIR, 
                appLinkerProperties.getFtpServerRemoteArchivedDir(),
                fileImportDate.getYear(), 
                fileImportDate.getMonthValue(), 
                fileImportDate.getDayOfMonth());
        ensureFtpPathExists(ftpClient, archiveDir);

        // Perform the move
        String sourcePath = "/" + fileMetricData.getOriginFilename();
        String targetPath = archiveDir + "/" + fileMetricData.getOriginFilename();
        ftpClient.changeWorkingDirectory(appLinkerProperties.getFtpServerRemoteDataDir());
        boolean renamed = ftpClient.rename(sourcePath, targetPath);
        if (!renamed) {
            throw new IOException("Failed to move file to archived directory: " + targetPath);
        }
    }

    private static void ensureFtpPathExists(FTPClient ftpClient, String path) throws IOException {
        String[] directories = path.split("/");
        String currentPath = "";
        for (String dir : directories) {
            if (dir.isEmpty()) continue;
            currentPath += "/" + dir;
            if (!ftpClient.changeWorkingDirectory(currentPath)) {
                boolean created = ftpClient.makeDirectory(currentPath);
                if (!created) {
                    throw new IOException("Failed to create directory: " + currentPath);
                }
                ftpClient.changeWorkingDirectory(currentPath);
            }
        }
    }

    private FTPClient connectToFtp() throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(appLinkerProperties.getFtpServerHost(), appLinkerProperties.getFtpServerPort());
        ftpClient.login(appLinkerProperties.getFtpServerUser(), appLinkerProperties.getFtpServerPassword());
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        return ftpClient;
    }

    private void disconnectFromFtp(FTPClient ftpClient) {
        if (ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                log.error("Error disconnecting from FTP server: {}", e.getMessage(), e);
            }
        }
    }
}
