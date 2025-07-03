package ma.zrad.system.stats.batch.writer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.domain.VehicleTripAnomalyDomain;
import ma.zrad.system.batch.common.domain.VehicleTripInfractionDomain;
import ma.zrad.system.batch.common.domain.VehicleTripValidDomain;
import ma.zrad.system.stats.batch.domain.bo.VehicleTripStatsResultBo;
import ma.zrad.system.stats.batch.domain.port.out.VehicleTripStatsRepositoryPort;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@StepScope
@Component
@RequiredArgsConstructor
public class TripStatsWriter implements ItemWriter<VehicleTripStatsResultBo> {

    private final VehicleTripStatsRepositoryPort repositoryPort;

    @Override
    public void write(final Chunk<? extends VehicleTripStatsResultBo> chunk) {

        List<VehicleTripValidDomain> validTrips = new ArrayList<>();
        List<VehicleTripAnomalyDomain> anomalyTrips = new ArrayList<>();
        List<VehicleTripInfractionDomain> infractionTrips = new ArrayList<>();

        for (VehicleTripStatsResultBo item : chunk.getItems()) {
            if (item.getTripValidDomain() != null) {
                validTrips.add(item.getTripValidDomain());
            }
            if (item.getTripAnomalyDomain() != null) {
                anomalyTrips.add(item.getTripAnomalyDomain());
            }
            if (item.getTripInfractionDomain() != null) {
                infractionTrips.add(item.getTripInfractionDomain());
            }
        }

        if(!validTrips.isEmpty()) {
            repositoryPort.saveAllValidTrip(validTrips);
        }

        if(!anomalyTrips.isEmpty()) {
            repositoryPort.saveAllAnomalyTrip(anomalyTrips);
        }

        if(!infractionTrips.isEmpty()) {
            repositoryPort.saveAllInfractionTrip(infractionTrips);
        }

    }
}
