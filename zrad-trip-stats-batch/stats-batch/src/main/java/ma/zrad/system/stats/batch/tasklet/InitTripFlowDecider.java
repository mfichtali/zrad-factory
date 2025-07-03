package ma.zrad.system.stats.batch.tasklet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.domain.EventBusinessDomain;
import ma.zrad.system.batch.common.enums.EventBatchContextEnum;
import ma.zrad.system.batch.common.exceptions.BusinessException;
import ma.zrad.system.batch.common.pojo.FullRegionDetailsOut;
import ma.zrad.system.batch.common.utils.BatchConstantUtils;
import ma.zrad.system.stats.batch.domain.port.in.EventBusinessService;
import ma.zrad.system.stats.batch.service.ReferentialRegionLoaderApiService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitTripFlowDecider implements JobExecutionDecider {

    private final ReferentialRegionLoaderApiService referentialApiService;
    private final EventBusinessService eventBusinessService;

    @Override
    public FlowExecutionStatus decide(final JobExecution jobExecution, final StepExecution stepExecution) {

        var hasFailure = jobExecution.getStepExecutions().stream().anyMatch(step -> step.getStatus() == BatchStatus.FAILED);
        if (hasFailure) {
            return FlowExecutionStatus.FAILED;
        }

        var regionCode = jobExecution.getJobParameters().getString(BatchConstantUtils.PARAM_REGION_CODE);
        var context = jobExecution.getExecutionContext();
        var regionsDetails = referentialApiService.getRegionDetails();

        if (StringUtils.isNotBlank(regionCode)) {

            var pendingZrad02Events = eventBusinessService.findPendingEventByRegionOfBatch(EventBatchContextEnum.ZRAD02, regionCode);
            if (CollectionUtils.isEmpty(pendingZrad02Events)) {
                log.info("🚨 There are no pending events for the trip analysis in region: {}", regionCode);
                return new FlowExecutionStatus(BatchConstantUtils.BATCH_TRIP_NO_DATA);
            }
            var zrad02EventsIdentifier = pendingZrad02Events.stream().map(EventBusinessDomain::getId).toList();
            context.put(BatchConstantUtils.BATCH_CTX_EVENT_ZRAD02_IDS, zrad02EventsIdentifier);
            log.info("🚀 Launching trip analysis in SIMPLE_MODE mode, parameters: regionCode={}", regionCode);

            var regionInfo = findRegionInfo(regionsDetails, regionCode);
            context.put(BatchConstantUtils.BATCH_REGION_DATA_CTX, regionInfo);
            return new FlowExecutionStatus(BatchConstantUtils.TRIP_SINGLE_MODE);

        } else {
            return new FlowExecutionStatus(BatchConstantUtils.TRIP_PARTITION_MODE);
        }
    }

    private FullRegionDetailsOut findRegionInfo(List<FullRegionDetailsOut> regionsDetails, String regionCode) {
        return regionsDetails.stream()
                .filter(r -> r.getCodeRegion().equalsIgnoreCase(regionCode))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Region code not found in the referential API: " + regionCode));
    }
}
