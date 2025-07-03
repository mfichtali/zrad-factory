package ma.zrad.system.vp.batch.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("vehiclePassageBatchResource")
@RequiredArgsConstructor
public class VehiclePassageBatchResource {

    private final JobLauncher jobLauncher;
    private final Job vehiclePassageImporterJob;

    @PostMapping("/batch/start")
    public ResponseEntity<String> vehiclePassageBatch() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(vehiclePassageImporterJob, params);
            return ResponseEntity.ok("Batch launched");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Batch failed: " + e.getMessage());
        }
    }

}
