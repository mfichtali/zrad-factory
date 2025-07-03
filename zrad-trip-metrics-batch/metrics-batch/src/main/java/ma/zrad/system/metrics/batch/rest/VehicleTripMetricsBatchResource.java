package ma.zrad.system.metrics.batch.rest;

import lombok.RequiredArgsConstructor;
import ma.zrad.system.batch.common.utils.BatchConstantUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("vehicleTripMetricsBatchResource")
@RequiredArgsConstructor
public class VehicleTripMetricsBatchResource {

    private final JobLauncher jobLauncher;
    private final Job tripMetricsJob;

    @PostMapping("/batch/start")
    public ResponseEntity<String> vehiclePassageBatch(
            @RequestParam(name = "granularity") String coGranularity,
            @RequestParam(name = "date-processing") String dateProcessing
    ) {
        try {
            JobParametersBuilder builder = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis())
                    .addString(BatchConstantUtils.BATCH_PROCESSING_GRANULARITY, coGranularity)
                    .addString(BatchConstantUtils.BATCH_PROCESSING_DATE, dateProcessing);
            JobParameters params = builder.toJobParameters();
            jobLauncher.run(tripMetricsJob, params);
            return ResponseEntity.ok("Batch launched");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Batch failed: " + e.getMessage());
        }
    }

}
