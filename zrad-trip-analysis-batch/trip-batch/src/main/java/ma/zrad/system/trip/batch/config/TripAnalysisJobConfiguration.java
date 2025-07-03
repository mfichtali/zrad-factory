package ma.zrad.system.trip.batch.config;

import lombok.RequiredArgsConstructor;
import ma.zrad.system.batch.common.records.VehiclePassageSimpleAggregateRecord;
import ma.zrad.system.batch.common.utils.BatchConstantUtils;
import ma.zrad.system.trip.batch.domain.bo.VehicleTripAnalysisResultBo;
import ma.zrad.system.trip.batch.infra.config.ZradDbConfig;
import ma.zrad.system.trip.batch.listener.BatchTripAnalysisListener;
import ma.zrad.system.trip.batch.mapper.TripSimpleRowMapper;
import ma.zrad.system.trip.batch.partitioner.SectionPartitioner;
import ma.zrad.system.trip.batch.processor.TripAnalysisProcessor;
import ma.zrad.system.trip.batch.tasklet.InitTripAnalysisTasklet;
import ma.zrad.system.trip.batch.tasklet.InitTripFlowDecider;
import ma.zrad.system.trip.batch.tasklet.UpdateEventStatusTasklet;
import ma.zrad.system.trip.batch.writer.TripAnalysisWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Configuration
@Import({ZradDbConfig.class})
@ComponentScan(basePackages = {
        ZradScanPackages.BATCH_REST,
        ZradScanPackages.BATCH_LISTENER,
        ZradScanPackages.BATCH_SERVICE,
        ZradScanPackages.BATCH_TASKLET})
@EnableBatchProcessing(dataSourceRef = "zradDataSource", transactionManagerRef = "zradTransactionManager")
@RequiredArgsConstructor
public class TripAnalysisJobConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager zradTransactionManager;
    private final DataSource zradDataSource;
    private final AppLinkerProperties appLinkerProperties;

    // Listeners
    private final BatchTripAnalysisListener batchTripAnalysisListener;

    // Tasklet
    private final InitTripAnalysisTasklet initTripAnalysisTasklet;
    private final UpdateEventStatusTasklet updateEventStatusTasklet;

    // Deciders
    private final InitTripFlowDecider initTripFlowDecider;

    @Bean
    Job tripAnalysisJob(@Qualifier("stepInitTripAnalysisTasklet") Step stepInitTripAnalysisTasklet,
                        @Qualifier("stepUpdateEventStatusTasklet") Step stepUpdateEventStatusTasklet,
                        @Qualifier("stepTripSimpleMode") Step stepTripSimpleMode,
                        @Qualifier("partitionedStepTrip") Step partitionedStepTrip) {
        var name = "Trip Analysis Job";
        return new JobBuilder(name, jobRepository)

                .start(stepInitTripAnalysisTasklet)

                .on("*").to(initTripFlowDecider)

                .from(initTripFlowDecider)
                .on("FAILED").fail()

                .from(initTripFlowDecider)
                .on(BatchConstantUtils.BATCH_TRIP_NO_DATA).end()

                .from(initTripFlowDecider)
                .on(BatchConstantUtils.TRIP_SINGLE_MODE).to(stepTripSimpleMode)
                .next(stepUpdateEventStatusTasklet)

                .from(initTripFlowDecider)
                    .on(BatchConstantUtils.TRIP_PARTITION_MODE).to(partitionedStepTrip)
                .next(stepUpdateEventStatusTasklet)

                .end()
                .listener(batchTripAnalysisListener)
                .build();
    }

    @Bean
    Step stepInitTripAnalysisTasklet() {
        return new StepBuilder("Init T.A Tasklet", jobRepository)
                .tasklet(initTripAnalysisTasklet, zradTransactionManager)
                .listener(initTripAnalysisTasklet)
                .build();
    }


    @Bean
    Step stepUpdateEventStatusTasklet() {
        return new StepBuilder("Update E.Status Tasklet", jobRepository)
                .tasklet(updateEventStatusTasklet, zradTransactionManager)
                .listener(updateEventStatusTasklet)
                .build();
    }

    @Bean
    Step stepTripSimpleMode(
            TripAnalysisProcessor itemProcessor,
            TripAnalysisWriter itemWriter) throws IOException {
        var name = "Processing Trip Simple Mode Step";
        return new StepBuilder(name, jobRepository)
                .<VehiclePassageSimpleAggregateRecord, VehicleTripAnalysisResultBo>chunk(appLinkerProperties.getBatchChunkSize(), zradTransactionManager)
                .reader(tripSimpleItemReader(null, null, null))
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    @StepScope
    public SectionPartitioner partitioner() {
        return new SectionPartitioner();
    }

    @Bean
    Step partitionedStepTrip(@Qualifier("stepWorkerTripPartitionedMode") Step stepWorkerTripPartitionedMode,
                             SectionPartitioner partitioner){
        var name = "Partitioned Step Trip";
        return new StepBuilder(name, jobRepository)
                .partitioner(stepWorkerTripPartitionedMode.getName(), partitioner)
                .step(stepWorkerTripPartitionedMode)
                .gridSize(appLinkerProperties.getBatchPartitionGridSize())
                .taskExecutor(new SimpleAsyncTaskExecutor()) // Use an async task executor for parallel processing
                .build();
    }

    @Bean
    Step stepWorkerTripPartitionedMode(
            TripAnalysisProcessor itemProcessor,
            TripAnalysisWriter itemWriter) throws IOException {
        var name = "Processing Trip Partitioned Mode Step";
        return new StepBuilder(name, jobRepository)
                .<VehiclePassageSimpleAggregateRecord, VehicleTripAnalysisResultBo>chunk(appLinkerProperties.getBatchChunkSize(), zradTransactionManager)
                .reader(tripPartitionedItemReader(null, null, null))
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    @StepScope
    JdbcCursorItemReader<VehiclePassageSimpleAggregateRecord> tripSimpleItemReader(
            @Value("#{jobParameters['" + BatchConstantUtils.PARAM_REGION_CODE + "']}") String region,
            @Value("#{jobParameters['" + BatchConstantUtils.PARAM_SECTION_CODE + "']}") String section,
            @Value("#{jobExecutionContext['" + BatchConstantUtils.BATCH_CTX_EVENT_ZRAD01_IDS + "']}") List<UUID> eventIds) throws IOException {

        JdbcCursorItemReader<VehiclePassageSimpleAggregateRecord> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(zradDataSource);
        reader.setRowMapper(new TripSimpleRowMapper());

        String sql = new String(Files.readAllBytes(Paths.get(
                ResourceUtils.getFile("classpath:db/query/trip_vehicle_aggregate.sql").toURI())));
        reader.setSql(sql);

        UUID[] eventIdArray = eventIds.toArray(new UUID[0]);
        reader.setPreparedStatementSetter((ps) -> {
            ps.setString(1, region);
            ps.setString(2, section);
            ps.setArray(3, ps.getConnection().createArrayOf("UUID", eventIdArray));
        });
        return reader;
    }

    @Bean
    @StepScope
    JdbcCursorItemReader<VehiclePassageSimpleAggregateRecord> tripPartitionedItemReader(
            @Value("#{stepExecutionContext['" + BatchConstantUtils.PARAM_REGION_CODE + "']}") String region,
            @Value("#{stepExecutionContext['" + BatchConstantUtils.PARAM_SECTION_CODE + "']}") String section,
            @Value("#{jobExecutionContext['" + BatchConstantUtils.BATCH_CTX_EVENT_ZRAD01_IDS + "']}") List<UUID> eventIds) throws IOException {

        JdbcCursorItemReader<VehiclePassageSimpleAggregateRecord> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(zradDataSource);
        reader.setRowMapper(new TripSimpleRowMapper());

        String sql = new String(Files.readAllBytes(Paths.get(
                ResourceUtils.getFile("classpath:db/query/trip_vehicle_aggregate.sql").toURI())));
        reader.setSql(sql);

        UUID[] eventIdArray = eventIds.toArray(new UUID[0]);
        reader.setPreparedStatementSetter((ps) -> {
            ps.setString(1, region);
            ps.setString(2, section);
            ps.setArray(3, ps.getConnection().createArrayOf("UUID", eventIdArray));
        });
        return reader;
    }

}
