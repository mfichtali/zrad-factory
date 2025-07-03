package ma.zrad.system.trip.batch.infra.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import ma.zrad.system.batch.common.domain.EventBusinessDomain;
import ma.zrad.system.batch.common.persistence.jpa.entity.EventBusiness;
import ma.zrad.system.batch.common.persistence.jpa.entity.VehicleTripAnalysis;
import ma.zrad.system.trip.batch.domain.model.VehicleTripDomain;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-03T11:46:06+0200",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.50.v20250624-0847, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class GlobalMapperImpl implements GlobalMapper {

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
    public List<EventBusinessDomain> eventBusinessToDomains(List<EventBusiness> entities) {
        if ( entities == null ) {
            return null;
        }

        List<EventBusinessDomain> list = new ArrayList<EventBusinessDomain>( entities.size() );
        for ( EventBusiness eventBusiness : entities ) {
            list.add( eventBusinessToDomain( eventBusiness ) );
        }

        return list;
    }

    @Override
    public List<VehicleTripAnalysis> vehicleTripAnalysisToEntities(List<VehicleTripDomain> domains) {
        if ( domains == null ) {
            return null;
        }

        List<VehicleTripAnalysis> list = new ArrayList<VehicleTripAnalysis>( domains.size() );
        for ( VehicleTripDomain vehicleTripDomain : domains ) {
            list.add( vehicleTripDomainToVehicleTripAnalysis( vehicleTripDomain ) );
        }

        return list;
    }

    protected VehicleTripAnalysis vehicleTripDomainToVehicleTripAnalysis(VehicleTripDomain vehicleTripDomain) {
        if ( vehicleTripDomain == null ) {
            return null;
        }

        VehicleTripAnalysis vehicleTripAnalysis = new VehicleTripAnalysis();

        vehicleTripAnalysis.setAnomalyDetected( vehicleTripDomain.isAnomalyDetected() );
        vehicleTripAnalysis.setAverageSpeedTrip( vehicleTripDomain.getAverageSpeedTrip() );
        vehicleTripAnalysis.setCodeIssue( vehicleTripDomain.getCodeIssue() );
        vehicleTripAnalysis.setIdEventImporter( vehicleTripDomain.getIdEventImporter() );
        vehicleTripAnalysis.setIdEventTrip( vehicleTripDomain.getIdEventTrip() );
        vehicleTripAnalysis.setInfractionDetected( vehicleTripDomain.isInfractionDetected() );
        vehicleTripAnalysis.setIssue( vehicleTripDomain.getIssue() );
        vehicleTripAnalysis.setMessageIssue( vehicleTripDomain.getMessageIssue() );
        vehicleTripAnalysis.setPassageEntryTime( vehicleTripDomain.getPassageEntryTime() );
        vehicleTripAnalysis.setPassageExitTime( vehicleTripDomain.getPassageExitTime() );
        vehicleTripAnalysis.setRegionCode( vehicleTripDomain.getRegionCode() );
        vehicleTripAnalysis.setRnVehicle( vehicleTripDomain.getRnVehicle() );
        vehicleTripAnalysis.setSectionCode( vehicleTripDomain.getSectionCode() );
        vehicleTripAnalysis.setSpeedEntryDetection( vehicleTripDomain.getSpeedEntryDetection() );
        vehicleTripAnalysis.setSpeedExitDetection( vehicleTripDomain.getSpeedExitDetection() );
        vehicleTripAnalysis.setSpeedSectionCalculated( vehicleTripDomain.getSpeedSectionCalculated() );
        vehicleTripAnalysis.setSpeedSectionLimit( vehicleTripDomain.getSpeedSectionLimit() );
        vehicleTripAnalysis.setTripDurationInSeconds( vehicleTripDomain.getTripDurationInSeconds() );

        return vehicleTripAnalysis;
    }
}
