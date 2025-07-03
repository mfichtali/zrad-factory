package ma.zrad.system.vp.batch.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AppLinkerProperties {

    @Value("${application.batch.chuck-size}")
    private int batchChunkSize;

    @Value("${application.batch.file.extension}")
    private String batchFileExtension;

    @Value("${application.batch.file.incident-pattern-file-per-chunk}")
    private String batchFileIncidentPatternFilePerChunk;

    @Value("${application.batch.file.incident-pattern-file-fusion}")
    private String batchFileIncidentPatternFileFusion;

    @Value("${application.batch.file.report-filename}")
    private String batchFileReportFilename;

    @Value("${application.batch.file.delimiter}")
    private String batchFileDelimiter;

    @Value("${application.batch.file.nbr-fields-per-line}")
    private int batchFileNbrFieldsPerLine;

    @Value("${application.batch.file.default-max-length-line}")
    private int batchFileDefaultMaxLengthLine;

    @Value("${application.batch.file.pattern}")
    private String batchFilePattern;

    @Value("${application.batch.file.pattern-compile}")
    private String batchFilePatternCompile;

    @Value("${application.batch.file.fields.constraints.speed.max}")
    private int fieldSpeedMax;

    @Value("${application.batch.file.fields.constraints.speed.min}")
    private int fieldSpeedMin;

    @Value("${application.ftp-server-config.host}")
    private String ftpServerHost;

    @Value("${application.ftp-server-config.port}")
    private int ftpServerPort;

    @Value("${application.ftp-server-config.user}")
    private String ftpServerUser;

    @Value("${application.ftp-server-config.password}")
    private String ftpServerPassword;

    @Value("${application.ftp-server-config.remote-data-dir}")
    private String ftpServerRemoteDataDir;

    @Value("${application.ftp-server-config.remote-archived-dir}")
    private String ftpServerRemoteArchivedDir;

    @Value("${application.ftp-server-config.remote-incident-dir}")
    private String ftpServerRemoteIncidentDir;

    @Value("${application.ftp-server-config.remote-report-dir}")
    private String ftpServerRemoteReportDir;

}
