package ma.zrad.system.stats.batch.infra.mapper;

import ma.zrad.system.batch.common.domain.EventBusinessDomain;
import ma.zrad.system.batch.common.domain.VehicleTripAnalysisDomain;
import ma.zrad.system.batch.common.domain.VehicleTripAnomalyDomain;
import ma.zrad.system.batch.common.domain.VehicleTripInfractionDomain;
import ma.zrad.system.batch.common.domain.VehicleTripValidDomain;
import ma.zrad.system.batch.common.persistence.jpa.entity.EventBusiness;
import ma.zrad.system.batch.common.persistence.jpa.entity.VehicleTripAnalysis;
import ma.zrad.system.batch.common.persistence.jpa.entity.VehicleTripAnomaly;
import ma.zrad.system.batch.common.persistence.jpa.entity.VehicleTripInfraction;
import ma.zrad.system.batch.common.persistence.jpa.entity.VehicleTripValid;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component("globalMapper")
public interface GlobalMapper {

    EventBusiness eventBusinessToEntity(EventBusinessDomain domain);

    EventBusinessDomain eventBusinessToDomain(EventBusiness entity);

    List<EventBusinessDomain> eventBusinessToDomains(List<EventBusiness> entities);

    List<VehicleTripAnalysisDomain> vehicleTripAnalysisToDomains(List<VehicleTripAnalysis> tripAnalysis);

    List<VehicleTripValid> vehicleTripValidToEntities(List<VehicleTripValidDomain> trips);

    List<VehicleTripAnomaly> vehicleTripAnomalyToEntities(List<VehicleTripAnomalyDomain> trips);

    List<VehicleTripInfraction> vehicleTripInfractionToEntities(List<VehicleTripInfractionDomain> trips);
}
