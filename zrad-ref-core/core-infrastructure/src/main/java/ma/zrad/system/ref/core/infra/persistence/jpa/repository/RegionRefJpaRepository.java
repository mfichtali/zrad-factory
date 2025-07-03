package ma.zrad.system.ref.core.infra.persistence.jpa.repository;

import ma.zrad.system.batch.common.records.RegionInfoRecord;
import ma.zrad.system.ref.core.infra.persistence.jpa.entity.RegionRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RegionRefJpaRepository extends JpaRepository<RegionRef, UUID> {


    @Query("""
            select new ma.zrad.system.batch.common.records.RegionInfoRecord(r.codeRegion, r.labelRegion)
            from RegionRef r
            where r.codeRegion = :code
            """)
    RegionInfoRecord findCodeRegionAndLabelByCode(@Param("code") String code);


    @Query("""
            select new ma.zrad.system.batch.common.records.RegionInfoRecord(r.codeRegion, r.labelRegion)
            from RegionRef r
            """)
    List<RegionInfoRecord> findAllCodeRegionAndLabelByCode();

    @Query("""
            select r from RegionRef r
            right join fetch r.sections s
            left join fetch s.radarInput ri
            left join fetch s.radarOutput ro
            left join fetch s.speedSection sp
            where r.codeRegion = :code
            and s.enabled = true
            and ri.enabled = true
            and ro.enabled = true
            """)
    RegionRef findRegionByCodeAndEnabledSections(@Param("code") String code);

    @Query("""
            select r from RegionRef r
            right join fetch r.sections s
            left join fetch s.radarInput ri
            left join fetch s.radarOutput ro
            left join fetch s.speedSection sp
            where s.enabled = true
            and ri.enabled = true
            and ro.enabled = true
            """)
    List<RegionRef> findAllRegionsAndEnabledSections();

}
