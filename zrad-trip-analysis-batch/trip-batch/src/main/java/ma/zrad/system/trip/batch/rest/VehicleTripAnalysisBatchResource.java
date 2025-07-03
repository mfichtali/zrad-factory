package ma.zrad.system.trip.batch.rest;

import lombok.RequiredArgsConstructor;
import ma.zrad.system.batch.common.utils.BatchConstantUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("vehicleTripAnalysisBatchResource")
@RequiredArgsConstructor
public class VehicleTripAnalysisBatchResource {

    private final JobLauncher jobLauncher;
    private final Job tripAnalysisJob;

    @PostMapping("/batch/start")
    public ResponseEntity<String> vehiclePassageBatch(@RequestParam(name = "regionCode") String region,
                                                      @RequestParam(name = "sectionCode", required = false) String section) {
        try {
            JobParametersBuilder builder = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis())
                    .addString(BatchConstantUtils.PARAM_REGION_CODE, region);

            if (StringUtils.isNotBlank(section)) {
                builder.addString(BatchConstantUtils.PARAM_SECTION_CODE, section);
            }

            JobParameters params = builder.toJobParameters();
            jobLauncher.run(tripAnalysisJob, params);
            return ResponseEntity.ok("Batch launched");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Batch failed: " + e.getMessage());
        }
    }

}
