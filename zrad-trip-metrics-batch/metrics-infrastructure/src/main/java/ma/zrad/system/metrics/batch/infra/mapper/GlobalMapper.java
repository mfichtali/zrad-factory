package ma.zrad.system.metrics.batch.infra.mapper;

import ma.zrad.system.batch.common.domain.EventBusinessDomain;
import ma.zrad.system.batch.common.domain.summary.VehicleTripDailySummaryDomain;
import ma.zrad.system.batch.common.domain.summary.VehicleTripMonthlySummaryDomain;
import ma.zrad.system.batch.common.persistence.jpa.entity.EventBusiness;
import ma.zrad.system.batch.common.persistence.jpa.entity.summary.VehicleTripDailySummary;
import ma.zrad.system.batch.common.persistence.jpa.entity.summary.VehicleTripMonthlySummary;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component("globalMapper")
public interface GlobalMapper {

    VehicleTripDailySummaryDomain vehicleTripDailySummaryToDomain(VehicleTripDailySummary entity);

    List<VehicleTripDailySummary> vehicleTripDailySummaryToEntities(List<VehicleTripDailySummaryDomain> items);

    EventBusiness eventBusinessToEntity(EventBusinessDomain domain);

    EventBusinessDomain eventBusinessToDomain(EventBusiness entity);

    VehicleTripMonthlySummaryDomain vehicleTripMonthlySummaryToDomain(VehicleTripMonthlySummary entity);

    List<VehicleTripDailySummaryDomain> vehicleTripDailSummaryToDomains(List<VehicleTripDailySummary> entity);

    List<VehicleTripMonthlySummary> vehicleTripMonthlySummaryToEntities(List<VehicleTripMonthlySummaryDomain> items);
}
