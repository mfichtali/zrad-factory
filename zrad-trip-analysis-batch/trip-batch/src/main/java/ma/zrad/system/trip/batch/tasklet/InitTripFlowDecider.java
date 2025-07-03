package ma.zrad.system.trip.batch.tasklet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.domain.EventBusinessDomain;
import ma.zrad.system.batch.common.enums.EventBatchContextEnum;
import ma.zrad.system.batch.common.exceptions.BusinessException;
import ma.zrad.system.batch.common.pojo.FullRegionDetailsOut;
import ma.zrad.system.batch.common.pojo.SectionInfoOut;
import ma.zrad.system.batch.common.utils.BatchConstantUtils;
import ma.zrad.system.trip.batch.domain.port.in.EventBusinessService;
import ma.zrad.system.trip.batch.service.ReferentialRegionLoaderApiService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
        var sectionCode = jobExecution.getJobParameters().getString(BatchConstantUtils.PARAM_SECTION_CODE);
        var context = jobExecution.getExecutionContext();
        var regionsDetails = referentialApiService.getRegionDetails();

        if (StringUtils.isNotBlank(regionCode)) {

            var pendingZrad01Events = StringUtils.isNotBlank(sectionCode)
                    ? eventBusinessService.findPendingEventByRegionAndSectionOfBatch(EventBatchContextEnum.ZRAD01, regionCode, sectionCode)
                    : eventBusinessService.findPendingEventByRegionOfBatch(EventBatchContextEnum.ZRAD01, regionCode);

            if (CollectionUtils.isEmpty(pendingZrad01Events)) {
                log.info("🚨 There are no pending events for the trip analysis in region: {}, section: {}", regionCode, sectionCode);
                return new FlowExecutionStatus(BatchConstantUtils.BATCH_TRIP_NO_DATA);
            }

            var zrad01EventsIdentifier = pendingZrad01Events.stream().map(EventBusinessDomain::getId).toList();
            context.put(BatchConstantUtils.BATCH_CTX_EVENT_ZRAD01_IDS, zrad01EventsIdentifier);

            log.info("🚀 Launching trip analysis in {} mode, parameters: regionCode={}, sectionCode={}",
                    StringUtils.isNotBlank(sectionCode) ? "SIMPLE_MODE" : "PARTITION_MODE", regionCode, sectionCode);

            var regionInfo = findRegionInfo(regionsDetails, regionCode);
            context.put(BatchConstantUtils.BATCH_REGION_DATA_CTX, regionInfo);

            if (StringUtils.isNotBlank(sectionCode)) {
                findSectionInfo(regionInfo, sectionCode).ifPresentOrElse(
                        section -> context.put(BatchConstantUtils.BATCH_SECTION_DATA_CTX, section),
                        () -> {
                            throw new BusinessException("Section code not found in the referential API: " + sectionCode);
                        }
                );
                return new FlowExecutionStatus(BatchConstantUtils.TRIP_SINGLE_MODE);
            }
            return new FlowExecutionStatus(BatchConstantUtils.TRIP_PARTITION_MODE);

        }
        return FlowExecutionStatus.COMPLETED;
    }

    private FullRegionDetailsOut findRegionInfo(List<FullRegionDetailsOut> regionsDetails, String regionCode) {
        return regionsDetails.stream()
                .filter(r -> r.getCodeRegion().equalsIgnoreCase(regionCode))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Region code not found in the referential API: " + regionCode));
    }

    private Optional<SectionInfoOut> findSectionInfo(FullRegionDetailsOut regionInfo, String sectionCode) {
        return regionInfo.getSections().stream()
                .filter(s -> s.getCodeSection().equalsIgnoreCase(sectionCode))
                .findFirst();
    }

}
