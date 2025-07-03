package ma.zrad.system.metrics.batch.config;

import lombok.RequiredArgsConstructor;
import ma.zrad.system.batch.common.domain.summary.VehicleTripDailySummaryDomain;
import ma.zrad.system.batch.common.domain.summary.VehicleTripMonthlySummaryDomain;
import ma.zrad.system.batch.common.pojo.GroupedDataTripMetrics;
import ma.zrad.system.batch.common.pojo.GroupedMonthlyTripMetrics;
import ma.zrad.system.batch.common.records.RawTripMetricsDataRecord;
import ma.zrad.system.batch.common.records.RawTripMetricsMonthlyRecord;
import ma.zrad.system.batch.common.utils.BatchConstantUtils;
import ma.zrad.system.batch.common.utils.DateUtils;
import ma.zrad.system.metrics.batch.infra.config.ZradDbConfig;
import ma.zrad.system.metrics.batch.infra.config.ZradScanPackages;
import ma.zrad.system.metrics.batch.listener.BatchTripMetricsListener;
import ma.zrad.system.metrics.batch.mapper.TripMetricsMonthlyRowMapper;
import ma.zrad.system.metrics.batch.mapper.TripMetricsRowMapper;
import ma.zrad.system.metrics.batch.partitioner.RegionPartitioner;
import ma.zrad.system.metrics.batch.processor.TripMetricsDailyProcessor;
import ma.zrad.system.metrics.batch.processor.TripMetricsMonthlyProcessor;
import ma.zrad.system.metrics.batch.reader.GroupingTripMetricsDailyReader;
import ma.zrad.system.metrics.batch.reader.GroupingTripMetricsMonthlyReader;
import ma.zrad.system.metrics.batch.tasklet.InitTripMetricsFlowDecider;
import ma.zrad.system.metrics.batch.tasklet.InitTripMetricsTasklet;
import ma.zrad.system.metrics.batch.writer.TripMetricsDailyWriter;
import ma.zrad.system.metrics.batch.writer.TripMetricsMonthlyWriter;
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
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;

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
public class TripMetricsJobConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager zradTransactionManager;
    private final AppLinkerProperties appLinkerProperties;
    private final DataSource zradDataSource;

    // Listeners
    private final BatchTripMetricsListener batchTripMetricsListener;
    
    // Tasklet
    private final InitTripMetricsTasklet initTripMetricsTasklet;
    private final InitTripMetricsFlowDecider initTripMetricsFlowDecider;
    
    @Bean
    Job tripMetricsJob(
            @Qualifier("stepInitTripMetricsTasklet") Step stepInitTripMetricsTasklet,
            @Qualifier("partitionedStepTripDailyMetrics") Step partitionedStepTripDailyMetrics,
            @Qualifier("partitionedStepRecalculateFullDailyOfMonthMetrics") Step partitionedStepRecalculateFullDailyOfMonthMetrics,
            @Qualifier("partitionedStepTripPropagateMonthlyMetrics") Step partitionedStepTripPropagateMonthlyMetrics
            ) {
        var name = "Trip Statistics Job";
        return new JobBuilder(name, jobRepository)

                .start(stepInitTripMetricsTasklet)

                .on("*").to(initTripMetricsFlowDecider)

                .from(initTripMetricsFlowDecider).on("FAILED").fail()

                .from(initTripMetricsFlowDecider).on(BatchConstantUtils.TRIP_INVALID_MODE).end()

                .from(initTripMetricsFlowDecider)
                .on(BatchConstantUtils.TRIP_METRICS_PARTITION_DAILY_MODE).to(partitionedStepTripDailyMetrics)
                //.next(stepUpdateEventStatusTasklet)

                .from(initTripMetricsFlowDecider)
                .on(BatchConstantUtils.TRIP_METRICS_PARTITION_MONTHLY_MODE)
                .to(partitionedStepRecalculateFullDailyOfMonthMetrics)
                .next(partitionedStepTripPropagateMonthlyMetrics)
                
                .end()
                .listener(batchTripMetricsListener)
                .build();

    }

    @Bean
    Step stepInitTripMetricsTasklet() {
        return new StepBuilder("Step - Init T.M Tasklet", jobRepository)
                .tasklet(initTripMetricsTasklet, zradTransactionManager)
                .listener(initTripMetricsTasklet)
                .build();
    }

    @Bean
    Step partitionedStepTripDailyMetrics(
            @Qualifier("stepWorkerTripMetricsDailyPartitionedMode") Step stepWorkerTripMetricsDailyPartitionedMode,
            RegionPartitioner partitioner) {
        var name = "Partitioned Step Trip Daily Metrics";
        return new StepBuilder(name, jobRepository)
                .partitioner(stepWorkerTripMetricsDailyPartitionedMode.getName(), partitioner)
                .step(stepWorkerTripMetricsDailyPartitionedMode)
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
    Step stepWorkerTripMetricsDailyPartitionedMode(
            TripMetricsDailyProcessor itemProcessor,
            TripMetricsDailyWriter itemWriter) throws IOException {
        var name = "Worker Trip Metrics Daily Partitioned Mode Step";
        return new StepBuilder(name, jobRepository)
                .<GroupedDataTripMetrics, VehicleTripDailySummaryDomain>chunk(appLinkerProperties.getBatchChunkSize(), zradTransactionManager)
                .reader(groupingTripDailyReader(null, null))
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    Step partitionedStepRecalculateFullDailyOfMonthMetrics(
            @Qualifier("stepWorkerRecalculateFullDailyOfMonthPartitionedMode") Step stepWorkerRecalculateFullDailyOfMonthPartitionedMode,
            RegionPartitioner partitioner) {
        var name = "Partitioned Step Recalculate Full Daily Of Month Metrics";
        return new StepBuilder(name, jobRepository)
                .partitioner(stepWorkerRecalculateFullDailyOfMonthPartitionedMode.getName(), partitioner)
                .step(stepWorkerRecalculateFullDailyOfMonthPartitionedMode)
                .gridSize(appLinkerProperties.getBatchPartitionGridSize())
                .taskExecutor(new SimpleAsyncTaskExecutor()) // Use an async task executor for parallel processing
                .build();
    }

    @Bean
    Step partitionedStepTripPropagateMonthlyMetrics(
            @Qualifier("stepWorkerTripMetricsPropagateMonthlyPartitionedMode") Step stepWorkerTripMetricsPropagateMonthlyPartitionedMode,
            RegionPartitioner partitioner) {
        var name = "Partitioned Step Trip Propagate Monthly Metrics";
        return new StepBuilder(name, jobRepository)
                .partitioner(stepWorkerTripMetricsPropagateMonthlyPartitionedMode.getName(), partitioner)
                .step(stepWorkerTripMetricsPropagateMonthlyPartitionedMode)
                .gridSize(appLinkerProperties.getBatchPartitionGridSize())
                .taskExecutor(new SimpleAsyncTaskExecutor()) // Use an async task executor for parallel processing
                .build();
    }


    @Bean
    Step stepWorkerRecalculateFullDailyOfMonthPartitionedMode(
            TripMetricsDailyProcessor itemProcessor,
            TripMetricsDailyWriter itemWriter) throws IOException {
        var name = "Worker Recalculate Full Daily Of Month Partitioned Mode Step";
        return new StepBuilder(name, jobRepository)
                .<GroupedDataTripMetrics, VehicleTripDailySummaryDomain>chunk(appLinkerProperties.getBatchChunkSize(), zradTransactionManager)
                .reader(groupingTripDailiesOfMonthReader(null, null))
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    Step stepWorkerTripMetricsPropagateMonthlyPartitionedMode(
            TripMetricsMonthlyProcessor itemProcessor,
            TripMetricsMonthlyWriter itemWriter) throws IOException {
        var name = "Worker Trip Metrics Propagate Monthly Partitioned Mode Step";
        return new StepBuilder(name, jobRepository)
                .<GroupedMonthlyTripMetrics, VehicleTripMonthlySummaryDomain>chunk(appLinkerProperties.getBatchChunkSize(), zradTransactionManager)
                .reader(groupingTripMonthlyReader(null, null))
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    @StepScope
    public GroupingTripMetricsDailyReader groupingTripDailyReader(
            @Value("#{stepExecutionContext['" + BatchConstantUtils.PARAM_REGION_CODE + "']}") String region,
            @Value("#{jobExecutionContext['" + BatchConstantUtils.BATCH_METRICS_LOCAL_DATE_CTX + "']}") LocalDate dateProcessing
    ) throws IOException {
        return new GroupingTripMetricsDailyReader(tripMetricsDailyItemReader(region, dateProcessing));
    }

    @Bean
    @StepScope
    public GroupingTripMetricsDailyReader groupingTripDailiesOfMonthReader(
            @Value("#{stepExecutionContext['" + BatchConstantUtils.PARAM_REGION_CODE + "']}") String region,
            @Value("#{jobExecutionContext['" + BatchConstantUtils.BATCH_METRICS_YEAR_MONTH_CTX + "']}") YearMonth yearMonthProcessing
    ) throws IOException {
        return new GroupingTripMetricsDailyReader(tripMetricsDailiesOfMonthItemReader(region, yearMonthProcessing));
    }

    @Bean
    @StepScope
    public GroupingTripMetricsMonthlyReader groupingTripMonthlyReader(
            @Value("#{stepExecutionContext['" + BatchConstantUtils.PARAM_REGION_CODE + "']}") String region,
            @Value("#{jobExecutionContext['" + BatchConstantUtils.BATCH_METRICS_YEAR_MONTH_CTX + "']}") YearMonth yearMonthProcessing
    ) throws IOException {
        return new GroupingTripMetricsMonthlyReader(tripMetricsMonthlyItemReader(region, yearMonthProcessing), region, yearMonthProcessing);
    }

    @Bean
    @StepScope
    JdbcCursorItemReader<RawTripMetricsDataRecord> tripMetricsDailyItemReader(
            @Value("#{stepExecutionContext['" + BatchConstantUtils.PARAM_REGION_CODE + "']}") String region,
            @Value("#{jobExecutionContext['" + BatchConstantUtils.BATCH_METRICS_LOCAL_DATE_CTX + "']}") LocalDate dateProcessing
    ) throws IOException {
        var ym = DateUtils.yearMonthAsString(dateProcessing);
        var dd = DateUtils.dayAsString(dateProcessing);
        return createDailyItemReader("classpath:db/query/trip_metrics_daily_aggregator.sql",
                ps -> {
                    int i = 1;
                    // anomaly
                    ps.setString(i++, region);   // co_region
                    ps.setString(i++, ym);   // ldt_year_month
                    ps.setString(i++, dd);   // ldt_day
                    // infraction
                    ps.setString(i++, region);
                    ps.setString(i++, ym);
                    ps.setString(i++, dd);
                    // valid
                    ps.setString(i++, region);
                    ps.setString(i++, ym);
                    ps.setString(i++, dd);
                });
    }

    @Bean
    @StepScope
    JdbcCursorItemReader<RawTripMetricsDataRecord> tripMetricsDailiesOfMonthItemReader(
            @Value("#{stepExecutionContext['" + BatchConstantUtils.PARAM_REGION_CODE + "']}") String region,
            @Value("#{jobExecutionContext['" + BatchConstantUtils.BATCH_METRICS_YEAR_MONTH_CTX + "']}") YearMonth yearMonthProcessing
    ) throws IOException {
        var ym = DateUtils.yearMonthAsString(yearMonthProcessing);
        return createDailyItemReader("classpath:db/query/trip_metrics_monthly_aggregator.sql",
                ps -> {
                    int i = 1;
                    // anomaly
                    ps.setString(i++, region);  // co_region
                    ps.setString(i++, ym);   // ldt_year_month
                    // infraction
                    ps.setString(i++, region);
                    ps.setString(i++, ym);
                    // valid
                    ps.setString(i++, region);
                    ps.setString(i++, ym);
                });
    }

    @Bean
    @StepScope
    JdbcCursorItemReader<RawTripMetricsMonthlyRecord> tripMetricsMonthlyItemReader(
            @Value("#{stepExecutionContext['" + BatchConstantUtils.PARAM_REGION_CODE + "']}") String region,
            @Value("#{jobExecutionContext['" + BatchConstantUtils.BATCH_METRICS_YEAR_MONTH_CTX + "']}") YearMonth yearMonthProcessing
    ) throws IOException {
        var ym = DateUtils.yearMonthAsString(yearMonthProcessing);
        return createMonthFromDailyItemReader("classpath:db/query/trip_metrics_monthly_from_daily_aggregator.sql",
                ps -> {
                    ps.setString(1, region);
                    ps.setString(2, ym);
                });
    }

    private JdbcCursorItemReader<RawTripMetricsDataRecord> createDailyItemReader(String queryPath,
                                                                                 PreparedStatementSetter pss) throws IOException {
        JdbcCursorItemReader<RawTripMetricsDataRecord> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(zradDataSource);
        reader.setRowMapper(new TripMetricsRowMapper());
        String sql = new String(Files.readAllBytes(Paths.get(ResourceUtils.getFile(queryPath).toURI())));
        reader.setSql(sql);
        reader.setPreparedStatementSetter(pss);
        return reader;
    }

    private JdbcCursorItemReader<RawTripMetricsMonthlyRecord> createMonthFromDailyItemReader(String queryPath,
                                                                                             PreparedStatementSetter pss) throws IOException {
        JdbcCursorItemReader<RawTripMetricsMonthlyRecord> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(zradDataSource);
        reader.setRowMapper(new TripMetricsMonthlyRowMapper());
        String sql = new String(Files.readAllBytes(Paths.get(ResourceUtils.getFile(queryPath).toURI())));
        reader.setSql(sql);
        reader.setPreparedStatementSetter(pss);
        return reader;
    }
}
