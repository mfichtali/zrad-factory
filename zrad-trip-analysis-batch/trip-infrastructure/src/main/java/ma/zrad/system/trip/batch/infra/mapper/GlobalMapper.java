package ma.zrad.system.trip.batch.infra.mapper;

import ma.zrad.system.batch.common.domain.EventBusinessDomain;
import ma.zrad.system.batch.common.persistence.jpa.entity.EventBusiness;
import ma.zrad.system.batch.common.persistence.jpa.entity.VehicleTripAnalysis;
import ma.zrad.system.trip.batch.domain.model.VehicleTripDomain;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component("globalMapper")
public interface GlobalMapper {

    EventBusiness eventBusinessToEntity(EventBusinessDomain domain);

    EventBusinessDomain eventBusinessToDomain(EventBusiness entity);

    List<EventBusinessDomain> eventBusinessToDomains(List<EventBusiness> entities);

    List<VehicleTripAnalysis> vehicleTripAnalysisToEntities(List<VehicleTripDomain> domains);
}
