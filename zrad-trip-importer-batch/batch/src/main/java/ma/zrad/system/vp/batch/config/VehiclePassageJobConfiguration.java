package ma.zrad.system.vp.batch.config;

import lombok.RequiredArgsConstructor;
import ma.zrad.system.batch.common.enums.EventBatchStatusEnum;
import ma.zrad.system.batch.common.pojo.BatchFileContext;
import ma.zrad.system.batch.common.records.FileMetadataRecord;
import ma.zrad.system.batch.common.utils.BatchConstantUtils;
import ma.zrad.system.vp.batch.domain.model.VehiclePassageWrapper;
import ma.zrad.system.vp.batch.infra.config.ZradDbConfig;
import ma.zrad.system.vp.batch.infra.config.ZradScanPackages;
import ma.zrad.system.vp.batch.listener.BatchVPImporterListener;
import ma.zrad.system.vp.batch.listener.FileImporterMetricsStepListener;
import ma.zrad.system.vp.batch.processor.VehiclePassageImporterProcessor;
import ma.zrad.system.vp.batch.reader.VehiclePassageImporterReader;
import ma.zrad.system.vp.batch.tasklet.BatchFlowStatusDecider;
import ma.zrad.system.vp.batch.tasklet.GenerateReportTasklet;
import ma.zrad.system.vp.batch.tasklet.InitVPImporterTasklet;
import ma.zrad.system.vp.batch.writer.VehiclePassageCompositeWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Import({ZradDbConfig.class})
@ComponentScan(basePackages = {
        ZradScanPackages.BATCH_REST,
        ZradScanPackages.BATCH_LISTENER,
        ZradScanPackages.BATCH_READER,
        ZradScanPackages.BATCH_SERVICE,
        ZradScanPackages.BATCH_TASKLET})
@EnableBatchProcessing(dataSourceRef = "zradDataSource", transactionManagerRef = "zradTransactionManager")
//@EnableScheduling
@RequiredArgsConstructor
public class VehiclePassageJobConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager zradTransactionManager;

    // Listeners
    private final BatchVPImporterListener batchVPImporterListener;

    // Tasklets
    private final InitVPImporterTasklet initVPImporterTasklet;
    private final GenerateReportTasklet generateReportTasklet;

    // Deciders
    private final BatchFlowStatusDecider batchFlowStatusDecider;

    @Bean
    Job vehiclePassageImporterJob(@Qualifier("stepVPImporterTasklet") Step stepVPImporterTasklet,
                                  @Qualifier("stepVehiclePassageProcessing") Step stepVehiclePassageProcessing,
                                  @Qualifier("stepGenerateReportTasklet") Step stepGenerateReportTasklet) {
        var name = "Vehicle Passage Importer Job";
        return new JobBuilder(name, jobRepository)
                .start(stepVPImporterTasklet)

                .on("*").to(batchFlowStatusDecider)

                .from(batchFlowStatusDecider)
                .on("FAILED").fail()

                .from(batchFlowStatusDecider)
                .on(EventBatchStatusEnum.NO_DATA.name()).end()

                .from(batchFlowStatusDecider)
                .on("CONTINUE").to(stepVehiclePassageProcessing)

                .next(stepGenerateReportTasklet)

                .end()
                .listener(batchVPImporterListener)
                .build();
    }

    @Bean
    Step stepVPImporterTasklet() {
        return new StepBuilder("Init VP Tasklet", jobRepository)
                .tasklet(initVPImporterTasklet, zradTransactionManager)
                .listener(initVPImporterTasklet)
                .build();
    }

    @Bean
    Step stepVehiclePassageProcessing(
            VehiclePassageImporterReader itemReader,
            VehiclePassageImporterProcessor itemProcessor,
            VehiclePassageCompositeWriter itemWriter,
            FileImporterMetricsStepListener fileImporterMetricsStepListener,
            AppLinkerProperties appLinkerProperties) {
        var name = "Step Populate DB from CSV file";
        return new StepBuilder(name, jobRepository)
                .<String, VehiclePassageWrapper>chunk(appLinkerProperties.getBatchChunkSize(), zradTransactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .listener(fileImporterMetricsStepListener)
                .build();
    }

    @Bean
    @StepScope
    VehiclePassageImporterReader vehiclePassageImporterReader(
            @Value("#{jobExecutionContext['" + BatchConstantUtils.BATCH_ROOT_FILE_CTX + "']}") BatchFileContext batchFileCtx,
            @Value("#{jobExecutionContext['" + BatchConstantUtils.BATCH_FILE_METADATA_CTX + "']}") FileMetadataRecord fileMetadataCtx) {
        return new VehiclePassageImporterReader(batchFileCtx, fileMetadataCtx);
    }

    @Bean
    Step stepGenerateReportTasklet() {
        return new StepBuilder("Generate Report Json File", jobRepository)
                .tasklet(generateReportTasklet, zradTransactionManager)
                .build();
    }

}
