package ma.zrad.system.stats.batch.config;

import lombok.RequiredArgsConstructor;
import ma.zrad.system.batch.common.domain.VehicleTripAnalysisDomain;
import ma.zrad.system.batch.common.utils.BatchConstantUtils;
import ma.zrad.system.stats.batch.domain.bo.VehicleTripStatsResultBo;
import ma.zrad.system.stats.batch.infra.config.ZradDbConfig;
import ma.zrad.system.stats.batch.infra.config.ZradScanPackages;
import ma.zrad.system.stats.batch.listener.BatchTripStatsListener;
import ma.zrad.system.stats.batch.partitioner.RegionPartitioner;
import ma.zrad.system.stats.batch.processor.TripStatsProcessor;
import ma.zrad.system.stats.batch.reader.TripStatsReader;
import ma.zrad.system.stats.batch.tasklet.InitTripFlowDecider;
import ma.zrad.system.stats.batch.tasklet.InitTripStatsTasklet;
import ma.zrad.system.stats.batch.tasklet.UpdateEventStatusTasklet;
import ma.zrad.system.stats.batch.writer.TripStatsWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;

@Configuration
@Import({ZradDbConfig.class})
@ComponentScan(basePackages = {
        ZradScanPackages.BATCH_REST,
        ZradScanPackages.BATCH_LISTENER,
        ZradScanPackages.BATCH_READER,
        ZradScanPackages.BATCH_SERVICE,
        ZradScanPackages.BATCH_TASKLET})
@EnableBatchProcessing(dataSourceRef = "zradDataSource", transactionManagerRef = "zradTransactionManager")
@RequiredArgsConstructor
public class TripStatsJobConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager zradTransactionManager;
    private final AppLinkerProperties appLinkerProperties;

    // Listeners
    private final BatchTripStatsListener batchTripStatsListener;

    // Tasklets
    private final InitTripStatsTasklet initTripStatsTasklet;
    private final InitTripFlowDecider initTripFlowDecider;
    private final UpdateEventStatusTasklet updateEventStatusTasklet;


    @Bean
    Job tripAnalysisJob(
            @Qualifier("stepInitTripStatsTasklet") Step stepInitTripStatsTasklet,
            @Qualifier("stepTripStatsSingleMode") Step stepTripStatsSingleMode,
            @Qualifier("partitionedStepTripStats") Step partitionedStepTripStats,
            @Qualifier("stepUpdateEventStatusTasklet") Step stepUpdateEventStatusTasklet
    ) {

        var name = "Trip Statistics Job";
        return new JobBuilder(name, jobRepository)

                .start(stepInitTripStatsTasklet)

                .on("*").to(initTripFlowDecider)

                .from(initTripFlowDecider).on("FAILED").fail()

                .from(initTripFlowDecider)
                .on(BatchConstantUtils.BATCH_TRIP_NO_DATA).end()

                .from(initTripFlowDecider)
                .on(BatchConstantUtils.TRIP_SINGLE_MODE).to(stepTripStatsSingleMode)
                .next(stepUpdateEventStatusTasklet)

                .from(initTripFlowDecider)
                .on(BatchConstantUtils.TRIP_PARTITION_MODE).to(partitionedStepTripStats)
                .next(stepUpdateEventStatusTasklet)

                .end()
                .listener(batchTripStatsListener)
                .build();

    }

    @Bean
    Step stepInitTripStatsTasklet() {
        return new StepBuilder("Init T.S Tasklet", jobRepository)
                .tasklet(initTripStatsTasklet, zradTransactionManager)
                .listener(initTripStatsTasklet)
                .build();
    }

    @Bean
    Step stepTripStatsSingleMode(
            TripStatsReader itemReader,
            TripStatsProcessor itemProcessor,
            TripStatsWriter itemWriter
    ) {
        var name = "Processing Trip Stats Single Mode Step";
        return new StepBuilder(name, jobRepository)
                .<VehicleTripAnalysisDomain, VehicleTripStatsResultBo>chunk(appLinkerProperties.getBatchChunkSize(), zradTransactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    Step partitionedStepTripStats(@Qualifier("stepWorkerTripStatsPartitionedMode") Step stepWorkerTripStatsPartitionedMode,
                                  RegionPartitioner partitioner) {
        var name = "Partitioned Step Trip";
        return new StepBuilder(name, jobRepository)
                .partitioner(stepWorkerTripStatsPartitionedMode.getName(), partitioner)
                .step(stepWorkerTripStatsPartitionedMode)
                .gridSize(appLinkerProperties.getBatchPartitionGridSize())
                .taskExecutor(new SimpleAsyncTaskExecutor()) // Use an async task executor for parallel processing
                .build();
    }

    @Bean
    @StepScope
    public RegionPartitioner partitioner() {
        return new RegionPartitioner();
    }

    @Bean
    Step stepWorkerTripStatsPartitionedMode(
            TripStatsReader itemReader,
            TripStatsProcessor itemProcessor,
            TripStatsWriter itemWriter) throws IOException {
        var name = "Processing Trip Partitioned Mode Step";
        return new StepBuilder(name, jobRepository)
                .<VehicleTripAnalysisDomain, VehicleTripStatsResultBo>chunk(appLinkerProperties.getBatchChunkSize(), zradTransactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    Step stepUpdateEventStatusTasklet() {
        return new StepBuilder("Update E.Status Tasklet", jobRepository)
                .tasklet(updateEventStatusTasklet, zradTransactionManager)
                .listener(updateEventStatusTasklet)
                .build();
    }

}
