package ma.zrad.system.trip.batch.service;

import ma.zrad.system.batch.common.annotations.TripValidationRule;
import ma.zrad.system.batch.common.enums.CodeImportStatusEnum;
import ma.zrad.system.batch.common.enums.CodeTypeIssueEnum;
import ma.zrad.system.batch.common.enums.PassageTimeSectionRefEnum;
import ma.zrad.system.batch.common.pojo.SectionInfoOut;
import ma.zrad.system.batch.common.pojo.SpeedSectionInfoOut;
import ma.zrad.system.batch.common.pojo.TripValidationResult;
import ma.zrad.system.batch.common.utils.DateUtils;
import ma.zrad.system.batch.common.utils.ServiceUtils;
import ma.zrad.system.trip.batch.config.AppLinkerProperties;
import ma.zrad.system.trip.batch.domain.model.VehicleTripDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Service
public class VehicleTripAnalysisService {

    @Autowired
    private AppLinkerProperties appLinkerProperties;

    @Autowired
    private ReferentialRegionLoaderApiService referentialApiService;

    private final List<TripValidationRule<VehicleTripDomain>> anomalyRules;
    private final List<TripValidationRule<VehicleTripDomain>> infractionRules;

    public VehicleTripAnalysisService() {
        this.anomalyRules = List.of(
                this::validatePassageTrip,
                this::validatePassageEntryTime,
                this::validatePassageExitTime,
                this::validatePassageTimes,
                this::validatePassageSuspiciousSpeed,
                this::validatePassageLongStopOrBreakdown
        );
        this.infractionRules = List.of(
                this::validatePassageOverSpeedLimit
        );
    }

    public CodeImportStatusEnum determineTripStatus(TripValidationResult result) {
        if (result.isHasAnomaly()) return CodeImportStatusEnum.FAILED;
        if (result.isHasInfraction()) return CodeImportStatusEnum.WARNING;
        return CodeImportStatusEnum.PROCESSED;
    }

    public TripValidationResult validateTrip(VehicleTripDomain trip) {

        TripValidationResult validationAnomaly = anomalyRules.stream()
                .map(rule -> rule.validate(trip))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
        if(validationAnomaly != null) {
            return validationAnomaly;
        }

        return infractionRules.stream()
                .map(rule -> rule.validate(trip))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseGet(() -> {
                    LocalDateTime passageEntryTime = trip.getPassageEntryTime();
                    LocalDateTime passageExitTime = trip.getPassageExitTime();
                    BigDecimal distanceSectionInMeters = getSectionDistanceInMeters(trip.getSectionCode());
                    var speedCalculated = ServiceUtils.calculateSpeed(passageEntryTime, passageExitTime, distanceSectionInMeters);
                    return new TripValidationResult()
                            .setHasAnomaly(Boolean.FALSE)
                            .setHasInfraction(Boolean.FALSE)
                            .setSpeedCalculated(speedCalculated);
                });
    }

    private TripValidationResult validatePassageTrip(VehicleTripDomain item) {
        if (item.getPassageEntryTime() == null && item.getSpeedExitDetection() == null) {
            return new TripValidationResult()
                    .setHasAnomaly(Boolean.TRUE)
                    .setHasInfraction(Boolean.FALSE)
                    .setIssue(CodeTypeIssueEnum.ANO000);
        }
        return null;
    }

    private TripValidationResult validatePassageEntryTime(VehicleTripDomain item) {
        if (item.getPassageEntryTime() == null) {
            CodeTypeIssueEnum code = CodeTypeIssueEnum.ANO001;
            // Vehicle [%s] - Passage at exit point at %s - no trace of a passage at entry point.
            return new TripValidationResult()
                    .setHasAnomaly(Boolean.TRUE)
                    .setHasInfraction(Boolean.FALSE)
                    .setIssue(code)
                    .setMessageOverride(String.format(code.getMessage(),
                            item.getRnVehicle(),
                            DateUtils.asStringMessage(item.getPassageExitTime())));
        }
        return null;
    }

    private TripValidationResult validatePassageExitTime(VehicleTripDomain item) {
        if (item.getPassageExitTime() == null) {
            CodeTypeIssueEnum code = CodeTypeIssueEnum.ANO002;
            BigDecimal distanceSectionInMeters = getSectionDistanceInMeters(item.getSectionCode());
            String maxRefHourByDistance = PassageTimeSectionRefEnum.findMaxRefHourByDistanceAsString(distanceSectionInMeters);
            // Vehicle [%s] Passage detected at entry at %s - no exit passage after %s delay.
            return new TripValidationResult()
                    .setHasAnomaly(Boolean.TRUE)
                    .setHasInfraction(Boolean.FALSE)
                    .setIssue(code)
                    .setMessageOverride(String.format(code.getMessage(),
                            item.getRnVehicle(),
                            DateUtils.asStringMessage(item.getPassageEntryTime()),
                            maxRefHourByDistance));
        }
        return null;
    }

    private TripValidationResult validatePassageTimes(VehicleTripDomain item) {
        if (!item.getPassageEntryTime().isBefore(item.getPassageExitTime())) {
            CodeTypeIssueEnum code = CodeTypeIssueEnum.ANO003;
            // Vehicle [%s] passage entry must be strictly before exit.
            return new TripValidationResult()
                    .setHasAnomaly(Boolean.TRUE)
                    .setHasInfraction(Boolean.FALSE)
                    .setIssue(code)
                    .setMessageOverride(String.format(code.getMessage(), item.getRnVehicle()));
        }
        Duration duration = Duration.between(item.getPassageEntryTime(), item.getPassageExitTime());
        if (duration.getSeconds() < appLinkerProperties.getTripValidationMinimumDurationSec()) {
            CodeTypeIssueEnum code = CodeTypeIssueEnum.ANO004;
            // Vehicle [%s] passage duration too short (%ds)
            return new TripValidationResult()
                    .setHasAnomaly(Boolean.TRUE)
                    .setHasInfraction(Boolean.FALSE)
                    .setIssue(code)
                    .setMessageOverride(String.format(code.getMessage(),
                            item.getRnVehicle(),
                            duration.getSeconds()));
        }
        return null;
    }

    private TripValidationResult validatePassageSuspiciousSpeed(VehicleTripDomain item) {
        LocalDateTime passageEntryTime = item.getPassageEntryTime();
        LocalDateTime passageExitTime = item.getPassageExitTime();
        BigDecimal distanceSectionInMeters = getSectionDistanceInMeters(item.getSectionCode());
        var speedCalculated = ServiceUtils.calculateSpeed(passageEntryTime, passageExitTime, distanceSectionInMeters);
        if (speedCalculated != null && speedCalculated.intValue() > appLinkerProperties.getTripValidationThresholdSpeed()) {
            CodeTypeIssueEnum code = CodeTypeIssueEnum.ANO005;
            // Vehicle [%s] Average speed of %d km/h - exceeds anomaly threshold (250 km/h).
            return new TripValidationResult()
                    .setHasAnomaly(Boolean.TRUE)
                    .setHasInfraction(Boolean.FALSE)
                    .setSpeedCalculated(speedCalculated)
                    .setIssue(code)
                    .setMessageOverride(String.format(code.getMessage(),
                            item.getRnVehicle(),
                            speedCalculated.intValue()));
        }

//        if (item.getAverageSpeedTrip() != null && item.getAverageSpeedTrip().intValue() > appLinkerProperties.getTripValidationThresholdSpeed()) {
//            CodeTypeAnomalyEnum code = CodeTypeAnomalyEnum.ANO005;
//            return new TripValidationResult()
//                    .setHasAnomaly(Boolean.TRUE)
//                    .setHasInfraction(Boolean.FALSE)
//                    .setIssue(code)
//                    .setMessageOverride(String.format(code.getMessageAnomaly(), item.getRnVehicle(),
//                            item.getAverageSpeedTrip().intValue()));
//        }
        return null;
    }

    private TripValidationResult validatePassageLongStopOrBreakdown(VehicleTripDomain item) {
        LocalDateTime passageEntryTime = item.getPassageEntryTime();
        LocalDateTime passageExitTime = item.getPassageExitTime();
        BigDecimal distanceSectionInMeters = getSectionDistanceInMeters(item.getSectionCode());
        int maxRefSecondByDistance = PassageTimeSectionRefEnum.findMaxRefSecondByDistance(distanceSectionInMeters);
        long durationInSeconds = Duration.between(passageEntryTime, passageExitTime).getSeconds();
        if (durationInSeconds > maxRefSecondByDistance) {
            String duractionAsString = DateUtils.formatDuration(passageEntryTime, passageExitTime);
            BigDecimal distanceSectionKm = ServiceUtils.formatDistanceKm(distanceSectionInMeters);
            CodeTypeIssueEnum code = CodeTypeIssueEnum.ANO006;
            // Vehicle [%s] Duration of %s for %d km - exceeds the maximum permitted threshold
            return new TripValidationResult()
                    .setHasAnomaly(Boolean.TRUE)
                    .setHasInfraction(Boolean.FALSE)
                    .setIssue(code)
                    .setMessageOverride(String.format(code.getMessage(),
                            item.getRnVehicle(),
                            duractionAsString,
                            ServiceUtils.formatDistanceKmLabel(distanceSectionInMeters)));
        }
        return null;
    }

    private TripValidationResult validatePassageOverSpeedLimit(VehicleTripDomain item) {
        LocalDateTime passageEntryTime = item.getPassageEntryTime();
        LocalDateTime passageExitTime = item.getPassageExitTime();
        BigDecimal distanceSectionInMeters = getSectionDistanceInMeters(item.getSectionCode());
        BigDecimal speedCalculated = ServiceUtils.calculateSpeed(passageEntryTime, passageExitTime, distanceSectionInMeters);
        BigDecimal speedLimitSection = getSpeedLimitOfSection(item.getSectionCode());
        if(speedCalculated.compareTo(speedLimitSection) > 0) {
            CodeTypeIssueEnum code = CodeTypeIssueEnum.INF001;
            // Vehicle [%s] Average speed of %d km/h exceeds the allowed limit (%d km/h)
            return new TripValidationResult()
                    .setHasAnomaly(Boolean.FALSE)
                    .setHasInfraction(Boolean.TRUE)
                    .setSpeedCalculated(speedCalculated)
                    .setIssue(code)
                    .setMessageOverride(String.format(code.getMessage(),
                            item.getRnVehicle(),
                            speedCalculated.intValue(),
                            speedLimitSection.intValue()));
        }
        return null;
    }

    private BigDecimal getSpeedLimitOfSection(final String coSection) {
        return referentialApiService.getRegionDetails().stream()
                .flatMap(region -> region.getSections().stream())
                .filter(r -> r.getCodeSection().equalsIgnoreCase(coSection))
                .findFirst()
                .map(SectionInfoOut::getSpeedSection)
                .map(SpeedSectionInfoOut::getSpeedSectionLimit)
                .orElse(null);
    }

    private BigDecimal getSectionDistanceInMeters(final String coSection) {
        return referentialApiService.getRegionDetails().stream()
                .flatMap(region -> region.getSections().stream())
                .filter(r -> r.getCodeSection().equalsIgnoreCase(coSection))
                .findFirst()
                .map(SectionInfoOut::getDistanceSectionM)
                .orElse(null);
    }


}
