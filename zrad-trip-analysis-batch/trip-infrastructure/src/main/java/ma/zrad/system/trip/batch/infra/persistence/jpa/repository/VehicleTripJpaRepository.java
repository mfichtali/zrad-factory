package ma.zrad.system.trip.batch.infra.persistence.jpa.repository;

import ma.zrad.system.batch.common.persistence.jpa.entity.VehicleTripAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface VehicleTripJpaRepository extends JpaRepository<VehicleTripAnalysis, UUID> {

    @Modifying
    @Query(nativeQuery = true,
            value = """
                    UPDATE T_VEHICLE_PASSAGE_IMPORTER
                    SET CO_STATUS_IMPORT = :status
                    WHERE CO_REGION = :coRegion
                    AND CO_SECTION_ROAD = :coSection
                    AND RN_VEHICLE = :rnVehicle
                    AND ID_EVENT = :eventId
                    """)
    void updateStatusOfVehiclePassage(
            @Param("coRegion") String coRegion,
            @Param("coSection") String coSection,
            @Param("rnVehicle") String rnVehicle,
            @Param("eventId") UUID eventId,
            @Param("status") String newStatus);
}
