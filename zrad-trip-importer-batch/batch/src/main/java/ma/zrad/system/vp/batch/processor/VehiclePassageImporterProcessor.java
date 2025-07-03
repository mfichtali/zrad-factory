package ma.zrad.system.vp.batch.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.enums.BatchErrorTypeEnum;
import ma.zrad.system.batch.common.exceptions.BusinessException;
import ma.zrad.system.batch.common.records.FileMetadataRecord;
import ma.zrad.system.batch.common.utils.BatchConstantUtils;
import ma.zrad.system.batch.common.utils.DateUtils;
import ma.zrad.system.batch.common.utils.ServiceUtils;
import ma.zrad.system.vp.batch.config.AppLinkerProperties;
import ma.zrad.system.vp.batch.domain.model.VehiclePassageImporterDomain;
import ma.zrad.system.vp.batch.domain.model.VehiclePassageWrapper;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@StepScope
@Component
@RequiredArgsConstructor
public class VehiclePassageImporterProcessor implements ItemProcessor<String, VehiclePassageWrapper> {

    private final AppLinkerProperties appLinkerProperties;

    @Value("#{jobExecutionContext['"+ BatchConstantUtils.BATCH_FILE_METADATA_CTX +"']}")
    private FileMetadataRecord fileMetadataCtx;

    @Value("#{jobExecutionContext['" + BatchConstantUtils.BATCH_CTX_VPI_EVENT_ID + "']}")
    private UUID currentEventId;

    @Override
    public VehiclePassageWrapper process(final String line) throws Exception {
        try {
            VehiclePassageImporterDomain vp = parseAndValidateVehiclePassage(line);
            return VehiclePassageWrapper.valid(vp);
        } catch (IllegalArgumentException e) {
            log.error("Invalid line: {}. Error: {}", line, e.getMessage());
            return VehiclePassageWrapper.invalid(line, e.getMessage(), BatchErrorTypeEnum.FUNCTIONAL);
        } catch (Exception e) {
            log.error("Unexpected technical error while processing line: {}", line, e);
            return VehiclePassageWrapper.invalid(line, e.getMessage(), BatchErrorTypeEnum.TECHNICAL);
        }
    }

    private VehiclePassageImporterDomain parseAndValidateVehiclePassage(String line) {
        // Line model : (AB-123-CD;20250501T080000;TR01;A;127.5)
        var tokens = line.split(appLinkerProperties.getBatchFileDelimiter());
        if (tokens.length < appLinkerProperties.getBatchFileNbrFieldsPerLine()) {
            throw new BusinessException(
                    String.format("Not enough fields in line. Expected %d, but found %d",
                            appLinkerProperties.getBatchFileNbrFieldsPerLine(), tokens.length)
            );
        } else if (tokens.length > appLinkerProperties.getBatchFileNbrFieldsPerLine()) {
            throw new BusinessException(
                    String.format("Too many fields in line. Expected %d, but found %d",
                            appLinkerProperties.getBatchFileNbrFieldsPerLine(), tokens.length)
            );
        }

        // Validate RnVehicle mandatory field
        var rnVehicle = tokens[0].trim();
        if (rnVehicle.isEmpty()) {
            throw new BusinessException("Vehicle registration number is empty");
        }

        // Validate and parse date and time, expecting format YYYYMMDD'T'HHMMSS
        var dateTimePassage = tokens[1].trim();
        var ldtVehiclePassage = DateUtils.toLocalDateTime(dateTimePassage);
        if (ldtVehiclePassage == null) {
            throw new BusinessException("Invalid date and time passage: " + dateTimePassage);
        }
        // Check that the date is not in the future
        if (ldtVehiclePassage.isAfter(LocalDateTime.now())) {
            throw new BusinessException("Date and time passage cannot be in the future: " + dateTimePassage);
        }

        // Validate section road code
        var codeSection = tokens[2].trim();
        if(!fileMetadataCtx.sectionRoadCode().equals(codeSection)) {
            throw new BusinessException("Section road code does not match: " + codeSection);
        }

        // Validate radar code
        var radarCode = tokens[3].trim();
        if (radarCode.isEmpty()) {
            throw new BusinessException("Radar code is empty");
        }

        // Validate and parse speed
        var speedStr = tokens[4].trim();
        var speed = ServiceUtils.parseToBigDecimal(speedStr, "Speed");
        if (speed == null || speed.compareTo(BigDecimal.ZERO) < appLinkerProperties.getFieldSpeedMin() ||
                speed.compareTo(new BigDecimal(appLinkerProperties.getFieldSpeedMax())) > 0) {
            throw new BusinessException(String.format("Invalid speed value: %s . Speed must be a logical and real value between %d and %d km/h.",
                    speed, appLinkerProperties.getFieldSpeedMin(), appLinkerProperties.getFieldSpeedMax()));
        }

        return new VehiclePassageImporterDomain()
                .setRnVehicle(rnVehicle)
                .setPassageTime(ldtVehiclePassage)
                .setRegionCode(fileMetadataCtx.regionCode())
                .setSectionRoadCode(fileMetadataCtx.sectionRoadCode())
                .setRadarDetectionCode(radarCode)
                .setSpeedDetection(speed)
                .setEventId(currentEventId);
    }
}
