package ma.zrad.system.vp.batch.infra.persistence.jpa.adapter;

import lombok.RequiredArgsConstructor;
import ma.zrad.system.vp.batch.domain.model.VehiclePassageImporterDomain;
import ma.zrad.system.vp.batch.domain.port.out.VehiclePassageRepositoryPort;
import ma.zrad.system.vp.batch.infra.mapper.GlobalMapper;
import ma.zrad.system.vp.batch.infra.persistence.jpa.repository.VehiclePassageJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VehiclePassageRepositoryAdapter implements VehiclePassageRepositoryPort {

    private final GlobalMapper mapper;
    private final VehiclePassageJpaRepository vehiclePassageJpaRepository;

    @Override
    public void saveVehiclePassages(List<VehiclePassageImporterDomain> domains) {
        var entities = mapper.vehiclePassageToEntities(domains);
        vehiclePassageJpaRepository.saveAll(entities);
    }

}
