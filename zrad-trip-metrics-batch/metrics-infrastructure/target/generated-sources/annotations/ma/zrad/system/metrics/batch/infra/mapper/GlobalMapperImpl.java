package ma.zrad.system.metrics.batch.infra.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import ma.zrad.system.batch.common.domain.EventBusinessDomain;
import ma.zrad.system.batch.common.domain.summary.VehicleTripDailySummaryDomain;
import ma.zrad.system.batch.common.domain.summary.VehicleTripMonthlySummaryDomain;
import ma.zrad.system.batch.common.persistence.jpa.entity.EventBusiness;
import ma.zrad.system.batch.common.persistence.jpa.entity.summary.VehicleTripDailySummary;
import ma.zrad.system.batch.common.persistence.jpa.entity.summary.VehicleTripMonthlySummary;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-03T11:46:17+0200",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.50.v20250624-0847, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class GlobalMapperImpl implements GlobalMapper {

    @Override
    public VehicleTripDailySummaryDomain vehicleTripDailySummaryToDomain(VehicleTripDailySummary entity) {
        if ( entity == null ) {
            return null;
        }

        VehicleTripDailySummaryDomain vehicleTripDailySummaryDomain = new VehicleTripDailySummaryDomain();

        vehicleTripDailySummaryDomain.setAnomalyTypeCounts( entity.getAnomalyTypeCounts() );
        vehicleTripDailySummaryDomain.setId( entity.getId() );
        vehicleTripDailySummaryDomain.setInfractionTypeCounts( entity.getInfractionTypeCounts() );
        vehicleTripDailySummaryDomain.setLdtDay( entity.getLdtDay() );
        vehicleTripDailySummaryDomain.setLdtYearMonth( entity.getLdtYearMonth() );
        vehicleTripDailySummaryDomain.setNbAnomalies( entity.getNbAnomalies() );
        vehicleTripDailySummaryDomain.setNbInfractions( entity.getNbInfractions() );
        vehicleTripDailySummaryDomain.setNbValidTrips( entity.getNbValidTrips() );
        vehicleTripDailySummaryDomain.setRegionCode( entity.getRegionCode() );
        vehicleTripDailySummaryDomain.setSectionCode( entity.getSectionCode() );
        vehicleTripDailySummaryDomain.setVersion( entity.getVersion() );

        return vehicleTripDailySummaryDomain;
    }

    @Override
    public List<VehicleTripDailySummary> vehicleTripDailySummaryToEntities(List<VehicleTripDailySummaryDomain> items) {
        if ( items == null ) {
            return null;
        }

        List<VehicleTripDailySummary> list = new ArrayList<VehicleTripDailySummary>( items.size() );
        for ( VehicleTripDailySummaryDomain vehicleTripDailySummaryDomain : items ) {
            list.add( vehicleTripDailySummaryDomainToVehicleTripDailySummary( vehicleTripDailySummaryDomain ) );
        }

        return list;
    }

    @Override
    public EventBusiness eventBusinessToEntity(EventBusinessDomain domain) {
        if ( domain == null ) {
            return null;
        }

        EventBusiness eventBusiness = new EventBusiness();

        eventBusiness.setVersion( domain.getVersion() );
        eventBusiness.setId( domain.getId() );
        eventBusiness.setEndBatchEvent( domain.getEndBatchEvent() );
        eventBusiness.setEventBatchContextEnum( domain.getEventBatchContextEnum() );
        eventBusiness.setEventBatchStatus( domain.getEventBatchStatus() );
        eventBusiness.setEventBusinessTypeEnum( domain.getEventBusinessTypeEnum() );
        eventBusiness.setEventLabel( domain.getEventLabel() );
        eventBusiness.setEventProcessingStatus( domain.getEventProcessingStatus() );
        eventBusiness.setJobExecutionId( domain.getJobExecutionId() );
        eventBusiness.setJobName( domain.getJobName() );
        eventBusiness.setMetadata( domain.getMetadata() );
        eventBusiness.setOccurredErrorMessage( domain.getOccurredErrorMessage() );
        eventBusiness.setProcessedCount( domain.getProcessedCount() );
        eventBusiness.setStartBatchEvent( domain.getStartBatchEvent() );

        return eventBusiness;
    }

    @Override
    public EventBusinessDomain eventBusinessToDomain(EventBusiness entity) {
        if ( entity == null ) {
            return null;
        }

        EventBusinessDomain eventBusinessDomain = new EventBusinessDomain();

        eventBusinessDomain.setEndBatchEvent( entity.getEndBatchEvent() );
        eventBusinessDomain.setEventBatchContextEnum( entity.getEventBatchContextEnum() );
        eventBusinessDomain.setEventBatchStatus( entity.getEventBatchStatus() );
        eventBusinessDomain.setEventBusinessTypeEnum( entity.getEventBusinessTypeEnum() );
        eventBusinessDomain.setEventLabel( entity.getEventLabel() );
        eventBusinessDomain.setEventProcessingStatus( entity.getEventProcessingStatus() );
        eventBusinessDomain.setId( entity.getId() );
        eventBusinessDomain.setJobExecutionId( entity.getJobExecutionId() );
        eventBusinessDomain.setJobName( entity.getJobName() );
        eventBusinessDomain.setMetadata( entity.getMetadata() );
        eventBusinessDomain.setOccurredErrorMessage( entity.getOccurredErrorMessage() );
        eventBusinessDomain.setProcessedCount( entity.getProcessedCount() );
        eventBusinessDomain.setStartBatchEvent( entity.getStartBatchEvent() );
        eventBusinessDomain.setVersion( entity.getVersion() );

        return eventBusinessDomain;
    }

    @Override
    public VehicleTripMonthlySummaryDomain vehicleTripMonthlySummaryToDomain(VehicleTripMonthlySummary entity) {
        if ( entity == null ) {
            return null;
        }

        VehicleTripMonthlySummaryDomain vehicleTripMonthlySummaryDomain = new VehicleTripMonthlySummaryDomain();

        vehicleTripMonthlySummaryDomain.setAnomalyTypeCounts( entity.getAnomalyTypeCounts() );
        vehicleTripMonthlySummaryDomain.setInfractionTypeCounts( entity.getInfractionTypeCounts() );
        vehicleTripMonthlySummaryDomain.setLdtYearMonth( entity.getLdtYearMonth() );
        vehicleTripMonthlySummaryDomain.setNbAnomalies( entity.getNbAnomalies() );
        vehicleTripMonthlySummaryDomain.setNbInfractions( entity.getNbInfractions() );
        vehicleTripMonthlySummaryDomain.setNbValidTrips( entity.getNbValidTrips() );
        vehicleTripMonthlySummaryDomain.setRegionCode( entity.getRegionCode() );
        vehicleTripMonthlySummaryDomain.setSectionCode( entity.getSectionCode() );

        return vehicleTripMonthlySummaryDomain;
    }

    @Override
    public List<VehicleTripDailySummaryDomain> vehicleTripDailSummaryToDomains(List<VehicleTripDailySummary> entity) {
        if ( entity == null ) {
            return null;
        }

        List<VehicleTripDailySummaryDomain> list = new ArrayList<VehicleTripDailySummaryDomain>( entity.size() );
        for ( VehicleTripDailySummary vehicleTripDailySummary : entity ) {
            list.add( vehicleTripDailySummaryToDomain( vehicleTripDailySummary ) );
        }

        return list;
    }

    @Override
    public List<VehicleTripMonthlySummary> vehicleTripMonthlySummaryToEntities(List<VehicleTripMonthlySummaryDomain> items) {
        if ( items == null ) {
            return null;
        }

        List<VehicleTripMonthlySummary> list = new ArrayList<VehicleTripMonthlySummary>( items.size() );
        for ( VehicleTripMonthlySummaryDomain vehicleTripMonthlySummaryDomain : items ) {
            list.add( vehicleTripMonthlySummaryDomainToVehicleTripMonthlySummary( vehicleTripMonthlySummaryDomain ) );
        }

        return list;
    }

    protected VehicleTripDailySummary vehicleTripDailySummaryDomainToVehicleTripDailySummary(VehicleTripDailySummaryDomain vehicleTripDailySummaryDomain) {
        if ( vehicleTripDailySummaryDomain == null ) {
            return null;
        }

        VehicleTripDailySummary vehicleTripDailySummary = new VehicleTripDailySummary();

        vehicleTripDailySummary.setVersion( vehicleTripDailySummaryDomain.getVersion() );
        vehicleTripDailySummary.setId( vehicleTripDailySummaryDomain.getId() );
        vehicleTripDailySummary.setAnomalyTypeCounts( vehicleTripDailySummaryDomain.getAnomalyTypeCounts() );
        vehicleTripDailySummary.setInfractionTypeCounts( vehicleTripDailySummaryDomain.getInfractionTypeCounts() );
        vehicleTripDailySummary.setLdtDay( vehicleTripDailySummaryDomain.getLdtDay() );
        vehicleTripDailySummary.setLdtYearMonth( vehicleTripDailySummaryDomain.getLdtYearMonth() );
        vehicleTripDailySummary.setNbAnomalies( vehicleTripDailySummaryDomain.getNbAnomalies() );
        vehicleTripDailySummary.setNbInfractions( vehicleTripDailySummaryDomain.getNbInfractions() );
        vehicleTripDailySummary.setNbValidTrips( vehicleTripDailySummaryDomain.getNbValidTrips() );
        vehicleTripDailySummary.setRegionCode( vehicleTripDailySummaryDomain.getRegionCode() );
        vehicleTripDailySummary.setSectionCode( vehicleTripDailySummaryDomain.getSectionCode() );

        return vehicleTripDailySummary;
    }

    protected VehicleTripMonthlySummary vehicleTripMonthlySummaryDomainToVehicleTripMonthlySummary(VehicleTripMonthlySummaryDomain vehicleTripMonthlySummaryDomain) {
        if ( vehicleTripMonthlySummaryDomain == null ) {
            return null;
        }

        VehicleTripMonthlySummary vehicleTripMonthlySummary = new VehicleTripMonthlySummary();

        vehicleTripMonthlySummary.setAnomalyTypeCounts( vehicleTripMonthlySummaryDomain.getAnomalyTypeCounts() );
        vehicleTripMonthlySummary.setInfractionTypeCounts( vehicleTripMonthlySummaryDomain.getInfractionTypeCounts() );
        vehicleTripMonthlySummary.setLdtYearMonth( vehicleTripMonthlySummaryDomain.getLdtYearMonth() );
        vehicleTripMonthlySummary.setNbAnomalies( vehicleTripMonthlySummaryDomain.getNbAnomalies() );
        vehicleTripMonthlySummary.setNbInfractions( vehicleTripMonthlySummaryDomain.getNbInfractions() );
        vehicleTripMonthlySummary.setNbValidTrips( vehicleTripMonthlySummaryDomain.getNbValidTrips() );
        vehicleTripMonthlySummary.setRegionCode( vehicleTripMonthlySummaryDomain.getRegionCode() );
        vehicleTripMonthlySummary.setSectionCode( vehicleTripMonthlySummaryDomain.getSectionCode() );

        return vehicleTripMonthlySummary;
    }
}
