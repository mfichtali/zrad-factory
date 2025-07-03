package ma.zrad.system.stats.batch.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.domain.VehicleTripAnalysisDomain;
import ma.zrad.system.batch.common.domain.VehicleTripAnomalyDomain;
import ma.zrad.system.batch.common.domain.VehicleTripInfractionDomain;
import ma.zrad.system.batch.common.domain.VehicleTripValidDomain;
import ma.zrad.system.batch.common.enums.CodeTypeIssueEnum;
import ma.zrad.system.batch.common.pojo.FullRegionDetailsOut;
import ma.zrad.system.batch.common.pojo.SectionInfoOut;
import ma.zrad.system.batch.common.utils.BatchConstantUtils;
import ma.zrad.system.batch.common.utils.DateUtils;
import ma.zrad.system.stats.batch.domain.bo.VehicleTripStatsResultBo;
import ma.zrad.system.stats.batch.service.ReferentialRegionLoaderApiService;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@StepScope
@Component
@RequiredArgsConstructor
public class TripStatsProcessor implements ItemProcessor<VehicleTripAnalysisDomain, VehicleTripStatsResultBo>, StepExecutionListener {

    private final ReferentialRegionLoaderApiService refApiService;

    @Value("#{jobExecutionContext['" + BatchConstantUtils.BATCH_CTX_TRIP_STATS_EVENT_ID + "']}")
    private UUID currentEventId;

    private String coRegionPartition;
    private FullRegionDetailsOut regionInfoCtx;

    @Override
    public void beforeStep(final StepExecution stepExecution) {
        ExecutionContext stepCtx = stepExecution.getExecutionContext();
        ExecutionContext jobCtx = stepExecution.getJobExecution().getExecutionContext();

        if (stepCtx.containsKey(BatchConstantUtils.PARAM_REGION_CODE)) {
            this.coRegionPartition = (String) stepCtx.get(BatchConstantUtils.PARAM_REGION_CODE);
            refApiService.getRegionDetails().stream()
                    .filter(r -> r.getCodeRegion().equalsIgnoreCase(coRegionPartition))
                    .findFirst()
                    .ifPresentOrElse(
                            regionData -> this.regionInfoCtx = regionData,
                            () -> {
                                throw new IllegalArgumentException("Region not found : " + this.coRegionPartition);
                            }
                    );
        } else {
            this.regionInfoCtx = (FullRegionDetailsOut) jobCtx.get(BatchConstantUtils.BATCH_REGION_DATA_CTX);
        }

    }

    @Override
    public VehicleTripStatsResultBo process(final VehicleTripAnalysisDomain item) {

        VehicleTripStatsResultBo result = new VehicleTripStatsResultBo();
        if (item.getIssue() == CodeTypeIssueEnum.ANO000) {
            LocalDateTime now = LocalDateTime.now();
            result.setTripAnomalyDomain(createAnomaly(item, now));
            return result;
        }
        LocalDateTime ldtPassage = item.getLdtPassage();
        if (item.isAnomalyDetected()) {
            result.setTripAnomalyDomain(createAnomaly(item, ldtPassage));
        } else if (item.isInfractionDetected()) {
            result.setTripInfractionDomain(createInfraction(item, ldtPassage));
        } else {
            SectionInfoOut sectionInfo = regionInfoCtx.getSections().stream()
                    .filter(r -> r.getCodeSection().equalsIgnoreCase(item.getSectionCode()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Section code not found in the region details: " + item.getSectionCode()));
            result.setTripValidDomain(createValidTrip(item, ldtPassage, sectionInfo.getDistanceSectionM()));
        }
        return result;
    }

    private VehicleTripAnomalyDomain createAnomaly(VehicleTripAnalysisDomain item, LocalDateTime dateTime) {
        return new VehicleTripAnomalyDomain()
                .setTripAnalysis(item)
                .setLdtYearMonth(DateUtils.formatToYearMonth(dateTime))
                .setLdtDay(DateUtils.formatToDayOfMonth(dateTime))
                .setIdEventAnomaly(currentEventId);
    }

    private VehicleTripInfractionDomain createInfraction(VehicleTripAnalysisDomain item, LocalDateTime dateTime) {
        return new VehicleTripInfractionDomain()
                .setTripAnalysis(item)
                .setLdtYearMonth(DateUtils.formatToYearMonth(dateTime))
                .setLdtDay(DateUtils.formatToDayOfMonth(dateTime))
                .setIdEventInfraction(currentEventId);
    }

    private VehicleTripValidDomain createValidTrip(VehicleTripAnalysisDomain item, LocalDateTime dateTime, BigDecimal distanceM) {
        return new VehicleTripValidDomain()
                .setTripAnalysis(item)
                .setLdtYearMonth(DateUtils.formatToYearMonth(dateTime))
                .setLdtDay(DateUtils.formatToDayOfMonth(dateTime))
                .setTripDistanceInMeters(distanceM)
                .setIdEventValid(currentEventId);
    }
}
