package ma.zrad.system.stats.batch.infra.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
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
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-03T11:46:13+0200",
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
    public List<VehicleTripAnalysisDomain> vehicleTripAnalysisToDomains(List<VehicleTripAnalysis> tripAnalysis) {
        if ( tripAnalysis == null ) {
            return null;
        }

        List<VehicleTripAnalysisDomain> list = new ArrayList<VehicleTripAnalysisDomain>( tripAnalysis.size() );
        for ( VehicleTripAnalysis vehicleTripAnalysis : tripAnalysis ) {
            list.add( vehicleTripAnalysisToVehicleTripAnalysisDomain( vehicleTripAnalysis ) );
        }

        return list;
    }

    @Override
    public List<VehicleTripValid> vehicleTripValidToEntities(List<VehicleTripValidDomain> trips) {
        if ( trips == null ) {
            return null;
        }

        List<VehicleTripValid> list = new ArrayList<VehicleTripValid>( trips.size() );
        for ( VehicleTripValidDomain vehicleTripValidDomain : trips ) {
            list.add( vehicleTripValidDomainToVehicleTripValid( vehicleTripValidDomain ) );
        }

        return list;
    }

    @Override
    public List<VehicleTripAnomaly> vehicleTripAnomalyToEntities(List<VehicleTripAnomalyDomain> trips) {
        if ( trips == null ) {
            return null;
        }

        List<VehicleTripAnomaly> list = new ArrayList<VehicleTripAnomaly>( trips.size() );
        for ( VehicleTripAnomalyDomain vehicleTripAnomalyDomain : trips ) {
            list.add( vehicleTripAnomalyDomainToVehicleTripAnomaly( vehicleTripAnomalyDomain ) );
        }

        return list;
    }

    @Override
    public List<VehicleTripInfraction> vehicleTripInfractionToEntities(List<VehicleTripInfractionDomain> trips) {
        if ( trips == null ) {
            return null;
        }

        List<VehicleTripInfraction> list = new ArrayList<VehicleTripInfraction>( trips.size() );
        for ( VehicleTripInfractionDomain vehicleTripInfractionDomain : trips ) {
            list.add( vehicleTripInfractionDomainToVehicleTripInfraction( vehicleTripInfractionDomain ) );
        }

        return list;
    }

    protected VehicleTripAnalysisDomain vehicleTripAnalysisToVehicleTripAnalysisDomain(VehicleTripAnalysis vehicleTripAnalysis) {
        if ( vehicleTripAnalysis == null ) {
            return null;
        }

        VehicleTripAnalysisDomain vehicleTripAnalysisDomain = new VehicleTripAnalysisDomain();

        vehicleTripAnalysisDomain.setAnomalyDetected( vehicleTripAnalysis.isAnomalyDetected() );
        vehicleTripAnalysisDomain.setAverageSpeedTrip( vehicleTripAnalysis.getAverageSpeedTrip() );
        vehicleTripAnalysisDomain.setCodeIssue( vehicleTripAnalysis.getCodeIssue() );
        vehicleTripAnalysisDomain.setId( vehicleTripAnalysis.getId() );
        vehicleTripAnalysisDomain.setIdEventImporter( vehicleTripAnalysis.getIdEventImporter() );
        vehicleTripAnalysisDomain.setIdEventTrip( vehicleTripAnalysis.getIdEventTrip() );
        vehicleTripAnalysisDomain.setInfractionDetected( vehicleTripAnalysis.isInfractionDetected() );
        vehicleTripAnalysisDomain.setIssue( vehicleTripAnalysis.getIssue() );
        vehicleTripAnalysisDomain.setMessageIssue( vehicleTripAnalysis.getMessageIssue() );
        vehicleTripAnalysisDomain.setPassageEntryTime( vehicleTripAnalysis.getPassageEntryTime() );
        vehicleTripAnalysisDomain.setPassageExitTime( vehicleTripAnalysis.getPassageExitTime() );
        vehicleTripAnalysisDomain.setRegionCode( vehicleTripAnalysis.getRegionCode() );
        vehicleTripAnalysisDomain.setRnVehicle( vehicleTripAnalysis.getRnVehicle() );
        vehicleTripAnalysisDomain.setSectionCode( vehicleTripAnalysis.getSectionCode() );
        vehicleTripAnalysisDomain.setSpeedEntryDetection( vehicleTripAnalysis.getSpeedEntryDetection() );
        vehicleTripAnalysisDomain.setSpeedExitDetection( vehicleTripAnalysis.getSpeedExitDetection() );
        vehicleTripAnalysisDomain.setSpeedSectionCalculated( vehicleTripAnalysis.getSpeedSectionCalculated() );
        vehicleTripAnalysisDomain.setSpeedSectionLimit( vehicleTripAnalysis.getSpeedSectionLimit() );
        vehicleTripAnalysisDomain.setTripDurationInSeconds( vehicleTripAnalysis.getTripDurationInSeconds() );
        vehicleTripAnalysisDomain.setVersion( vehicleTripAnalysis.getVersion() );

        return vehicleTripAnalysisDomain;
    }

    protected VehicleTripAnalysis vehicleTripAnalysisDomainToVehicleTripAnalysis(VehicleTripAnalysisDomain vehicleTripAnalysisDomain) {
        if ( vehicleTripAnalysisDomain == null ) {
            return null;
        }

        VehicleTripAnalysis vehicleTripAnalysis = new VehicleTripAnalysis();

        vehicleTripAnalysis.setVersion( vehicleTripAnalysisDomain.getVersion() );
        vehicleTripAnalysis.setId( vehicleTripAnalysisDomain.getId() );
        vehicleTripAnalysis.setAnomalyDetected( vehicleTripAnalysisDomain.isAnomalyDetected() );
        vehicleTripAnalysis.setAverageSpeedTrip( vehicleTripAnalysisDomain.getAverageSpeedTrip() );
        vehicleTripAnalysis.setCodeIssue( vehicleTripAnalysisDomain.getCodeIssue() );
        vehicleTripAnalysis.setIdEventImporter( vehicleTripAnalysisDomain.getIdEventImporter() );
        vehicleTripAnalysis.setIdEventTrip( vehicleTripAnalysisDomain.getIdEventTrip() );
        vehicleTripAnalysis.setInfractionDetected( vehicleTripAnalysisDomain.isInfractionDetected() );
        vehicleTripAnalysis.setIssue( vehicleTripAnalysisDomain.getIssue() );
        vehicleTripAnalysis.setMessageIssue( vehicleTripAnalysisDomain.getMessageIssue() );
        vehicleTripAnalysis.setPassageEntryTime( vehicleTripAnalysisDomain.getPassageEntryTime() );
        vehicleTripAnalysis.setPassageExitTime( vehicleTripAnalysisDomain.getPassageExitTime() );
        vehicleTripAnalysis.setRegionCode( vehicleTripAnalysisDomain.getRegionCode() );
        vehicleTripAnalysis.setRnVehicle( vehicleTripAnalysisDomain.getRnVehicle() );
        vehicleTripAnalysis.setSectionCode( vehicleTripAnalysisDomain.getSectionCode() );
        vehicleTripAnalysis.setSpeedEntryDetection( vehicleTripAnalysisDomain.getSpeedEntryDetection() );
        vehicleTripAnalysis.setSpeedExitDetection( vehicleTripAnalysisDomain.getSpeedExitDetection() );
        vehicleTripAnalysis.setSpeedSectionCalculated( vehicleTripAnalysisDomain.getSpeedSectionCalculated() );
        vehicleTripAnalysis.setSpeedSectionLimit( vehicleTripAnalysisDomain.getSpeedSectionLimit() );
        vehicleTripAnalysis.setTripDurationInSeconds( vehicleTripAnalysisDomain.getTripDurationInSeconds() );

        return vehicleTripAnalysis;
    }

    protected VehicleTripValid vehicleTripValidDomainToVehicleTripValid(VehicleTripValidDomain vehicleTripValidDomain) {
        if ( vehicleTripValidDomain == null ) {
            return null;
        }

        VehicleTripValid vehicleTripValid = new VehicleTripValid();

        vehicleTripValid.setIdEventValid( vehicleTripValidDomain.getIdEventValid() );
        vehicleTripValid.setLdtDay( vehicleTripValidDomain.getLdtDay() );
        vehicleTripValid.setLdtYearMonth( vehicleTripValidDomain.getLdtYearMonth() );
        vehicleTripValid.setTripAnalysis( vehicleTripAnalysisDomainToVehicleTripAnalysis( vehicleTripValidDomain.getTripAnalysis() ) );
        vehicleTripValid.setTripDistanceInMeters( vehicleTripValidDomain.getTripDistanceInMeters() );

        return vehicleTripValid;
    }

    protected VehicleTripAnomaly vehicleTripAnomalyDomainToVehicleTripAnomaly(VehicleTripAnomalyDomain vehicleTripAnomalyDomain) {
        if ( vehicleTripAnomalyDomain == null ) {
            return null;
        }

        VehicleTripAnomaly vehicleTripAnomaly = new VehicleTripAnomaly();

        vehicleTripAnomaly.setIdEventAnomaly( vehicleTripAnomalyDomain.getIdEventAnomaly() );
        vehicleTripAnomaly.setLdtDay( vehicleTripAnomalyDomain.getLdtDay() );
        vehicleTripAnomaly.setLdtYearMonth( vehicleTripAnomalyDomain.getLdtYearMonth() );
        vehicleTripAnomaly.setTripAnalysis( vehicleTripAnalysisDomainToVehicleTripAnalysis( vehicleTripAnomalyDomain.getTripAnalysis() ) );

        return vehicleTripAnomaly;
    }

    protected VehicleTripInfraction vehicleTripInfractionDomainToVehicleTripInfraction(VehicleTripInfractionDomain vehicleTripInfractionDomain) {
        if ( vehicleTripInfractionDomain == null ) {
            return null;
        }

        VehicleTripInfraction vehicleTripInfraction = new VehicleTripInfraction();

        vehicleTripInfraction.setIdEventInfraction( vehicleTripInfractionDomain.getIdEventInfraction() );
        vehicleTripInfraction.setLdtDay( vehicleTripInfractionDomain.getLdtDay() );
        vehicleTripInfraction.setLdtYearMonth( vehicleTripInfractionDomain.getLdtYearMonth() );
        vehicleTripInfraction.setTripAnalysis( vehicleTripAnalysisDomainToVehicleTripAnalysis( vehicleTripInfractionDomain.getTripAnalysis() ) );

        return vehicleTripInfraction;
    }
}
