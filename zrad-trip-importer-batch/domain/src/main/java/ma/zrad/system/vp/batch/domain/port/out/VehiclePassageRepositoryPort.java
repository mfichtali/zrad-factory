package ma.zrad.system.vp.batch.domain.port.out;

import ma.zrad.system.vp.batch.domain.model.VehiclePassageImporterDomain;

import java.util.List;

public interface VehiclePassageRepositoryPort {

    void saveVehiclePassages(List<VehiclePassageImporterDomain> domains);

}
