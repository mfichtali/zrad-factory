package ma.zrad.system.ref.core.infra.mapper;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import ma.zrad.system.batch.common.pojo.FullRegionDetailsOut;
import ma.zrad.system.batch.common.pojo.RadarInfoOut;
import ma.zrad.system.batch.common.pojo.SectionInfoOut;
import ma.zrad.system.batch.common.pojo.SpeedSectionInfoOut;
import ma.zrad.system.ref.core.domain.model.RadarDomain;
import ma.zrad.system.ref.core.domain.model.RegionDomain;
import ma.zrad.system.ref.core.domain.model.SectionDomain;
import ma.zrad.system.ref.core.domain.model.SpeedSectionDomain;
import ma.zrad.system.ref.core.infra.persistence.jpa.entity.RadarRef;
import ma.zrad.system.ref.core.infra.persistence.jpa.entity.RegionRef;
import ma.zrad.system.ref.core.infra.persistence.jpa.entity.SectionRef;
import ma.zrad.system.ref.core.infra.persistence.jpa.entity.SpeedSectionRef;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-03T11:46:22+0200",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.50.v20250624-0847, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class GlobalMapperImpl implements GlobalMapper {

    @Override
    public RegionDomain regionRefToDomain(RegionRef region) {
        if ( region == null ) {
            return null;
        }

        RegionDomain regionDomain = new RegionDomain();

        regionDomain.setCodeRegion( region.getCodeRegion() );
        regionDomain.setId( region.getId() );
        regionDomain.setLabelRegion( region.getLabelRegion() );
        regionDomain.setSections( sectionRefSetToSectionDomainSet( region.getSections() ) );

        return regionDomain;
    }

    @Override
    public List<RegionDomain> regionRefToDomains(List<RegionRef> regions) {
        if ( regions == null ) {
            return null;
        }

        List<RegionDomain> list = new ArrayList<RegionDomain>( regions.size() );
        for ( RegionRef regionRef : regions ) {
            list.add( regionRefToDomain( regionRef ) );
        }

        return list;
    }

    @Override
    public FullRegionDetailsOut regionDomainToRecord(RegionDomain domain) {
        if ( domain == null ) {
            return null;
        }

        FullRegionDetailsOut fullRegionDetailsOut = new FullRegionDetailsOut();

        fullRegionDetailsOut.setCodeRegion( domain.getCodeRegion() );
        fullRegionDetailsOut.setLabelRegion( domain.getLabelRegion() );
        fullRegionDetailsOut.setSections( sectionDomainSetToSectionInfoOutSet( domain.getSections() ) );

        return fullRegionDetailsOut;
    }

    @Override
    public List<FullRegionDetailsOut> regionDomainToRecords(List<RegionDomain> domains) {
        if ( domains == null ) {
            return null;
        }

        List<FullRegionDetailsOut> list = new ArrayList<FullRegionDetailsOut>( domains.size() );
        for ( RegionDomain regionDomain : domains ) {
            list.add( regionDomainToRecord( regionDomain ) );
        }

        return list;
    }

    protected RadarDomain radarRefToRadarDomain(RadarRef radarRef) {
        if ( radarRef == null ) {
            return null;
        }

        RadarDomain radarDomain = new RadarDomain();

        radarDomain.setCodeRadar( radarRef.getCodeRadar() );
        radarDomain.setEnabled( radarRef.isEnabled() );
        radarDomain.setId( radarRef.getId() );
        radarDomain.setLabelLocalisation( radarRef.getLabelLocalisation() );

        return radarDomain;
    }

    protected SpeedSectionDomain speedSectionRefToSpeedSectionDomain(SpeedSectionRef speedSectionRef) {
        if ( speedSectionRef == null ) {
            return null;
        }

        SpeedSectionDomain speedSectionDomain = new SpeedSectionDomain();

        speedSectionDomain.setCodeSection( speedSectionRef.getCodeSection() );
        speedSectionDomain.setId( speedSectionRef.getId() );
        speedSectionDomain.setSpeedAuthorized( speedSectionRef.getSpeedAuthorized() );
        speedSectionDomain.setSpeedSectionLimit( speedSectionRef.getSpeedSectionLimit() );
        speedSectionDomain.setSpeedToleranceKmh( speedSectionRef.getSpeedToleranceKmh() );
        speedSectionDomain.setSpeedTolerancePercent( speedSectionRef.getSpeedTolerancePercent() );
        speedSectionDomain.setTimePassageLimitInSec( speedSectionRef.getTimePassageLimitInSec() );
        speedSectionDomain.setTimePassageMaxInSec( speedSectionRef.getTimePassageMaxInSec() );
        speedSectionDomain.setTimePassageSectionInSec( speedSectionRef.getTimePassageSectionInSec() );
        speedSectionDomain.setTimePassageToleranceInSec( speedSectionRef.getTimePassageToleranceInSec() );

        return speedSectionDomain;
    }

    protected SectionDomain sectionRefToSectionDomain(SectionRef sectionRef) {
        if ( sectionRef == null ) {
            return null;
        }

        SectionDomain sectionDomain = new SectionDomain();

        sectionDomain.setCodeRegion( sectionRef.getCodeRegion() );
        sectionDomain.setCodeSection( sectionRef.getCodeSection() );
        sectionDomain.setDistanceKm( sectionRef.getDistanceKm() );
        sectionDomain.setDistanceM( sectionRef.getDistanceM() );
        sectionDomain.setDistanceSectionM( sectionRef.getDistanceSectionM() );
        sectionDomain.setDistanceToleranceM( sectionRef.getDistanceToleranceM() );
        sectionDomain.setEnabled( sectionRef.isEnabled() );
        sectionDomain.setId( sectionRef.getId() );
        sectionDomain.setLabelSection( sectionRef.getLabelSection() );
        sectionDomain.setRadarInput( radarRefToRadarDomain( sectionRef.getRadarInput() ) );
        sectionDomain.setRadarOutput( radarRefToRadarDomain( sectionRef.getRadarOutput() ) );
        sectionDomain.setSpeedSection( speedSectionRefToSpeedSectionDomain( sectionRef.getSpeedSection() ) );

        return sectionDomain;
    }

    protected Set<SectionDomain> sectionRefSetToSectionDomainSet(Set<SectionRef> set) {
        if ( set == null ) {
            return null;
        }

        Set<SectionDomain> set1 = new LinkedHashSet<SectionDomain>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( SectionRef sectionRef : set ) {
            set1.add( sectionRefToSectionDomain( sectionRef ) );
        }

        return set1;
    }

    protected RadarInfoOut radarDomainToRadarInfoOut(RadarDomain radarDomain) {
        if ( radarDomain == null ) {
            return null;
        }

        RadarInfoOut radarInfoOut = new RadarInfoOut();

        radarInfoOut.setCodeRadar( radarDomain.getCodeRadar() );
        radarInfoOut.setLabelLocalisation( radarDomain.getLabelLocalisation() );

        return radarInfoOut;
    }

    protected SpeedSectionInfoOut speedSectionDomainToSpeedSectionInfoOut(SpeedSectionDomain speedSectionDomain) {
        if ( speedSectionDomain == null ) {
            return null;
        }

        SpeedSectionInfoOut speedSectionInfoOut = new SpeedSectionInfoOut();

        speedSectionInfoOut.setSpeedAuthorized( speedSectionDomain.getSpeedAuthorized() );
        speedSectionInfoOut.setSpeedSectionLimit( speedSectionDomain.getSpeedSectionLimit() );
        speedSectionInfoOut.setSpeedToleranceKmh( speedSectionDomain.getSpeedToleranceKmh() );
        speedSectionInfoOut.setSpeedTolerancePercent( speedSectionDomain.getSpeedTolerancePercent() );
        speedSectionInfoOut.setTimePassageLimitInSec( speedSectionDomain.getTimePassageLimitInSec() );
        speedSectionInfoOut.setTimePassageMaxInSec( speedSectionDomain.getTimePassageMaxInSec() );
        speedSectionInfoOut.setTimePassageSectionInSec( speedSectionDomain.getTimePassageSectionInSec() );
        speedSectionInfoOut.setTimePassageToleranceInSec( speedSectionDomain.getTimePassageToleranceInSec() );

        return speedSectionInfoOut;
    }

    protected SectionInfoOut sectionDomainToSectionInfoOut(SectionDomain sectionDomain) {
        if ( sectionDomain == null ) {
            return null;
        }

        SectionInfoOut sectionInfoOut = new SectionInfoOut();

        sectionInfoOut.setCodeSection( sectionDomain.getCodeSection() );
        sectionInfoOut.setDistanceKm( sectionDomain.getDistanceKm() );
        sectionInfoOut.setDistanceM( sectionDomain.getDistanceM() );
        sectionInfoOut.setDistanceSectionM( sectionDomain.getDistanceSectionM() );
        sectionInfoOut.setDistanceToleranceM( sectionDomain.getDistanceToleranceM() );
        sectionInfoOut.setLabelSection( sectionDomain.getLabelSection() );
        sectionInfoOut.setRadarInput( radarDomainToRadarInfoOut( sectionDomain.getRadarInput() ) );
        sectionInfoOut.setRadarOutput( radarDomainToRadarInfoOut( sectionDomain.getRadarOutput() ) );
        sectionInfoOut.setSpeedSection( speedSectionDomainToSpeedSectionInfoOut( sectionDomain.getSpeedSection() ) );

        return sectionInfoOut;
    }

    protected Set<SectionInfoOut> sectionDomainSetToSectionInfoOutSet(Set<SectionDomain> set) {
        if ( set == null ) {
            return null;
        }

        Set<SectionInfoOut> set1 = new LinkedHashSet<SectionInfoOut>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( SectionDomain sectionDomain : set ) {
            set1.add( sectionDomainToSectionInfoOut( sectionDomain ) );
        }

        return set1;
    }
}
