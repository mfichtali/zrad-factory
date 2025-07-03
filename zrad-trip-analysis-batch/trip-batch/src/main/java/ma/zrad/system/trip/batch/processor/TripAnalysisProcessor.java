package ma.zrad.system.trip.batch.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.enums.CodeImportStatusEnum;
import ma.zrad.system.batch.common.pojo.FullRegionDetailsOut;
import ma.zrad.system.batch.common.pojo.SectionInfoOut;
import ma.zrad.system.batch.common.pojo.TripValidationResult;
import ma.zrad.system.batch.common.records.VehiclePassageSimpleAggregateRecord;
import ma.zrad.system.batch.common.utils.BatchConstantUtils;
import ma.zrad.system.trip.batch.domain.bo.VehicleTripAnalysisResultBo;
import ma.zrad.system.trip.batch.domain.model.VehicleTripDomain;
import ma.zrad.system.trip.batch.service.VehicleTripAnalysisService;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@StepScope
@Component
@RequiredArgsConstructor
public class TripAnalysisProcessor implements ItemProcessor<VehiclePassageSimpleAggregateRecord, VehicleTripAnalysisResultBo> {

    private final VehicleTripAnalysisService vehicleTripAnalysisService;

    @Value("#{jobExecutionContext['" + BatchConstantUtils.BATCH_CTX_TRIP_EVENT_ID + "']}")
    private UUID currentEventId;

    @Value("#{jobExecutionContext['" + BatchConstantUtils.BATCH_REGION_DATA_CTX + "']}")
    private FullRegionDetailsOut regionInfoCtx;

    @Value("#{jobExecutionContext['" + BatchConstantUtils.BATCH_SECTION_DATA_CTX + "']}")
    private SectionInfoOut sectionInfoCtx;

    @Value("#{stepExecutionContext['" + BatchConstantUtils.PARAM_SECTION_CODE + "']}")
    private String section;

    @Override
    public VehicleTripAnalysisResultBo process(final VehiclePassageSimpleAggregateRecord itemAggregate) {
        VehicleTripDomain tripDomain = buildVehicleTrip(itemAggregate);
        TripValidationResult result = vehicleTripAnalysisService.validateTrip(tripDomain);
        CodeImportStatusEnum statusResult = vehicleTripAnalysisService.determineTripStatus(result);
        tripDomain.setAnomalyDetected(result.isHasAnomaly());
        tripDomain.setInfractionDetected(result.isHasInfraction());
        tripDomain.setSpeedSectionCalculated(result.getSpeedCalculated());
        if (result.isHasAnomaly() || result.isHasInfraction()) {
            tripDomain.setIssue(result.getIssue());
            tripDomain.setMessageIssue(result.getMessageOverride());
        }
        return buildAnalysisResult(tripDomain, itemAggregate, statusResult);
    }

    private VehicleTripAnalysisResultBo buildAnalysisResult(
            VehicleTripDomain trip,
            VehiclePassageSimpleAggregateRecord aggregate,
            CodeImportStatusEnum status) {
        return new VehicleTripAnalysisResultBo()
                .setTripDomain(trip)
                .setTripAggregate(aggregate)
                .setStatus(status);
    }

    private VehicleTripDomain buildVehicleTrip(VehiclePassageSimpleAggregateRecord item) {

        if(sectionInfoCtx == null) {
            regionInfoCtx.getSections().stream()
                    .filter(r -> r.getCodeSection().equalsIgnoreCase(section))
                    .findFirst()
                    .ifPresentOrElse(
                        sectionInfo -> sectionInfoCtx = sectionInfo,
                        () -> {
                            throw new IllegalArgumentException("Section code not found in the region details: " + section);
                        }
                    );
        }
        return new VehicleTripDomain()
                .setRegionCode(regionInfoCtx.getCodeRegion())
                .setSectionCode(sectionInfoCtx.getCodeSection())
                .setRnVehicle(item.rnVehicle())
                .setPassageEntryTime(item.passageInputTime())
                .setPassageExitTime(item.passageOutputTime())
                .setSpeedEntryDetection(item.speedInput())
                .setSpeedExitDetection(item.speedOutput())
                .setSpeedSectionLimit(sectionInfoCtx.getSpeedSection().getSpeedSectionLimit())
                .setInfractionDetected(Boolean.FALSE)
                .setIdEventImporter(item.idEvent())
                .setIdEventTrip(currentEventId);
    }

}
