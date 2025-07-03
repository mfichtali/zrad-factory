package ma.zrad.system.vp.batch.tasklet;

import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.enums.EventBatchContextEnum;
import ma.zrad.system.batch.common.enums.EventBatchStatusEnum;
import ma.zrad.system.batch.common.exceptions.BusinessException;
import ma.zrad.system.batch.common.pojo.BatchFileContext;
import ma.zrad.system.batch.common.records.FileMetadataRecord;
import ma.zrad.system.batch.common.utils.BatchConstantUtils;
import ma.zrad.system.batch.common.utils.BatchServiceUtils;
import ma.zrad.system.vp.batch.config.AppLinkerProperties;
import ma.zrad.system.vp.batch.config.ReferentialRegionLoaderApiService;
import ma.zrad.system.vp.batch.domain.port.in.FileImporterService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.regex.Pattern;

@Slf4j
@Component
public class InitVPImporterTasklet extends AbstractZradTasklet implements StepExecutionListener {

    private final ReferentialRegionLoaderApiService referentialApiService;
    private final FileImporterService fileImporterService;
    private final AppLinkerProperties appLinkerProperties;

    private ExecutionContext context;
    private BatchFileContext batchFileCtx;

    public InitVPImporterTasklet(
            @Qualifier("ftpFileImporterService") FileImporterService fileImporterService,
            ReferentialRegionLoaderApiService referentialApiService,
            AppLinkerProperties appLinkerProperties) {
        this.referentialApiService = referentialApiService;
        this.fileImporterService = fileImporterService;
        this.appLinkerProperties = appLinkerProperties;
    }

    @Override
    public void beforeStep(final StepExecution stepExecution) {
        log.info("📌 Init VP Importer Tasklet - beforeStep");
        context = stepExecution.getJobExecution().getExecutionContext();
        try {
            // Prepare work directory and file context
            String envBatchWorkDir = System.getenv(ENV_BATCH_WORK_DIR);
            var rootBathWorkDirPath = StringUtils.isBlank(envBatchWorkDir)
                    ? JAVA_TMP_DIR_PROPERTY : envBatchWorkDir;
            batchFileCtx = new BatchFileContext(rootBathWorkDirPath, EventBatchContextEnum.ZRAD01.name());
            context.put(BatchConstantUtils.BATCH_ROOT_FILE_CTX, batchFileCtx);
        } catch (IOException e) {
            throw new RuntimeException("Error creating batch file context", e);
        }

    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("🚀 Initializing VP tasklet, execution started ...");
        var pathFileImported = fileImporterService.importFile(batchFileCtx);
        if (pathFileImported == null) {
            log.warn("File import failed, cannot proceed with validation");
            StepExecution stepExecution = getStepExecutionContext(chunkContext);
            stepExecution.setExitStatus(new ExitStatus(EventBatchStatusEnum.NO_DATA.name()));
            return RepeatStatus.FINISHED;
        }
        var filename = pathFileImported.getFileName().toString();
        validateFile(filename);
        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(final StepExecution stepExecution) {
        log.info("📌 Init VP Importer Tasklet - afterStep");
        if (stepExecution.getExitStatus().getExitCode().equals(ExitStatus.FAILED.getExitCode())) {
            return BatchServiceUtils.determineFailureExitStatus(stepExecution);
        }
        return stepExecution.getExitStatus();
    }

    private void validateFile(String filename) {

        // 1. Check if the file name contains the right extension
        if(!filename.toLowerCase().endsWith(appLinkerProperties.getBatchFileExtension())) {
            throw new BusinessException("File is not contains the right extension " + appLinkerProperties.getBatchFileExtension());
        }

        // 2. Check if the file name is matching the regex pattern
        if(!filename.matches(appLinkerProperties.getBatchFilePattern())) {
            throw new BusinessException("File name is not matching the regex pattern");
        }

        // 3. Extract the metadata from the file name
        var pattern = Pattern.compile(appLinkerProperties.getBatchFilePatternCompile());
        var matcher = pattern.matcher(filename);
        if(matcher.matches()) {
            String regionCode = matcher.group(1);
            String sectionRoadCode = matcher.group(2);
            String dateDetection = matcher.group(3);

            // make this field optional, if not present, set it to 000
            String sequence = matcher.group(4);

            // 4. Verify the region code and section road code against the referential API
            var regionDetails = referentialApiService.getRegionDetails();
            if (CollectionUtils.isEmpty(regionDetails)) {
                throw new BusinessException("Region details are not available in the referential API");
            }

            boolean matchRegionSection = regionDetails.stream()
                    .filter(region -> region.getCodeRegion().equalsIgnoreCase(regionCode))
                    .flatMap(region -> region.getSections().stream())
                    .anyMatch(section -> section.getCodeSection().equalsIgnoreCase(sectionRoadCode));

            if (!matchRegionSection) {
                throw new BusinessException("Region code and section road code do not match the referential API");
            }

            var fileMetadataRecord = FileMetadataRecord.from(filename, regionCode, sectionRoadCode, dateDetection, sequence);
            context.put(BatchConstantUtils.BATCH_FILE_METADATA_CTX, fileMetadataRecord);
        }

    }
}
