package ma.zrad.system.vp.batch.infra.mapper;

import ma.zrad.system.batch.common.domain.EventBusinessDomain;
import ma.zrad.system.batch.common.enums.CodeRadarDetectionEnum;
import ma.zrad.system.batch.common.persistence.jpa.entity.EventBusiness;
import ma.zrad.system.batch.common.persistence.jpa.entity.FileImporterMetrics;
import ma.zrad.system.batch.common.persistence.jpa.entity.VehiclePassageImporter;
import ma.zrad.system.vp.batch.domain.model.FileImporterMetricsDomain;
import ma.zrad.system.vp.batch.domain.model.VehiclePassageImporterDomain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component("globalMapper")
public interface GlobalMapper {

    EventBusiness eventBusinessToEntity(EventBusinessDomain domain);

    EventBusinessDomain eventBusinessToDomain(EventBusiness entity);

    FileImporterMetrics fileImporterMetricsToEntity(FileImporterMetricsDomain domain);

    FileImporterMetricsDomain fileImporterMetricsToDomain(FileImporterMetrics entity);
    
    @Mapping(target = "radarDetectionCode", qualifiedByName = "mapToRadarDetectionCodeEnum")
    @Mapping(target = "speedPassage", source = "speedDetection")
    @Mapping(target = "datePassage", source = "passageTime")
    @Mapping(target = "statusImport", expression = "java(ma.zrad.system.batch.common.enums.CodeImportStatusEnum.PENDING)")
    VehiclePassageImporter vehiclePassageToEntity(VehiclePassageImporterDomain domain);

    List<VehiclePassageImporter> vehiclePassageToEntities(List<VehiclePassageImporterDomain> domains);

    @Named("mapToRadarDetectionCodeEnum")
    default CodeRadarDetectionEnum mapToRadarDetectionCodeEnum(String value) {
        return CodeRadarDetectionEnum.fromCodeOrDefault(value);
    }

}
