package ma.zrad.system.trip.batch.writer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.enums.CodeImportStatusEnum;
import ma.zrad.system.trip.batch.domain.bo.VehicleTripAnalysisResultBo;
import ma.zrad.system.trip.batch.domain.model.VehicleTripDomain;
import ma.zrad.system.trip.batch.domain.port.out.VehicleTripAnalysisRepositoryPort;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@StepScope
@Component
@RequiredArgsConstructor
public class TripAnalysisWriter implements ItemWriter<VehicleTripAnalysisResultBo> {

    private final VehicleTripAnalysisRepositoryPort vehicleTripRepositoryPort;

    @Override
    public void write(final Chunk<? extends VehicleTripAnalysisResultBo> chunk) {

        List<VehicleTripDomain> vehicleTrips = new ArrayList<>();
        List<UniquePassage> passageUpdates = new ArrayList<>();

        for (VehicleTripAnalysisResultBo item : chunk.getItems()) {
            VehicleTripDomain trip = item.getTripDomain();
            CodeImportStatusEnum status = determineStatus(trip);
            vehicleTrips.add(trip);
            passageUpdates.add(new UniquePassage(
                    trip.getRegionCode(),
                    trip.getSectionCode(),
                    trip.getRnVehicle(),
                    trip.getIdEventImporter(),
                    status));
        }

        vehicleTripRepositoryPort.saveVehicleTrips(vehicleTrips);
        for (UniquePassage record : passageUpdates) {
            vehicleTripRepositoryPort.updateStatusOfVehiclePassage(
                    record.coRegion,
                    record.coSection(),
                    record.rnVehicle(),
                    record.eventId,
                    record.issue.name());
        }
    }

    private CodeImportStatusEnum determineStatus(VehicleTripDomain trip) {
        if (trip.isAnomalyDetected()) {
            return CodeImportStatusEnum.PROCESSED_ANOMALY;
        } else if (trip.isInfractionDetected()) {
            return CodeImportStatusEnum.PROCESSED_INFRACTION;
        }
        return CodeImportStatusEnum.PROCESSED;
    }

    public record UniquePassage(
            String coRegion,
            String coSection,
            String rnVehicle,
            UUID eventId,
            CodeImportStatusEnum issue
    ) {
    }
    
}
