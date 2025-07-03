package ma.zrad.system.metrics.batch.business.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import ma.zrad.system.batch.common.annotations.BusinessService;
import ma.zrad.system.batch.common.domain.summary.VehicleTripDailySummaryDomain;
import ma.zrad.system.batch.common.domain.summary.VehicleTripMonthlySummaryDomain;
import ma.zrad.system.batch.common.pojo.GroupedDataTripMetrics;
import ma.zrad.system.batch.common.pojo.GroupedMonthlyTripMetrics;
import ma.zrad.system.batch.common.records.RawTripMetricsDataRecord;
import ma.zrad.system.metrics.batch.domain.port.in.VehicleTripSummaryService;
import ma.zrad.system.metrics.batch.domain.port.out.VehicleTripSummaryRepositoryPort;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@BusinessService
@RequiredArgsConstructor
public class VehicleTripSummaryBusinessService implements VehicleTripSummaryService {

    private final VehicleTripSummaryRepositoryPort repositoryPort;
    private final ObjectMapper objectMapper;
    
    @Override
    public VehicleTripDailySummaryDomain produceDailySummary(final GroupedDataTripMetrics item) throws Exception {

        var coRegion = item.getKey().coRegion();
        var coSection = item.getKey().coSection();
        var yearMonth = item.getKey().ldtYearMonth();
        var day = item.getKey().ldtDay();

        var existingDomain = repositoryPort.findMetricsMonthlySummaryByKey(coRegion, coSection, yearMonth, day);
        if (shouldSkipUpdateDaily(existingDomain, item)) {
            return null;
        }

        var domain = (existingDomain != null) ? existingDomain : createNewDailyDomain(coRegion, coSection, yearMonth, day);
        domain.setNbAnomalies(BigDecimal.valueOf(item.getTotalAnomaly()));
        domain.setNbInfractions(BigDecimal.valueOf(item.getTotalInfraction()));
        domain.setNbValidTrips(BigDecimal.valueOf(item.getTotalValid()));
        populateIssueTypeCounts(item, domain);
        return domain;
    }

    @Override
    public VehicleTripMonthlySummaryDomain produceMonthlySummary(final GroupedMonthlyTripMetrics item) throws Exception {

        var coRegion = item.getKey().coRegion();
        var coSection = item.getKey().coSection();
        var yearMonth = item.getKey().ldtYearMonth();

        var existingDomain = repositoryPort.findMetricsMonthlySummaryByKey(coRegion, coSection, yearMonth);
        if (shouldSkipUpdateMonthly(existingDomain, item)) {
            return null;
        }

        var domain = (existingDomain != null) ? existingDomain : createNewMonthlyDomain(coRegion, coSection, yearMonth);
        domain.setNbAnomalies(BigDecimal.valueOf(item.getTotalAnomaly()));
        domain.setNbInfractions(BigDecimal.valueOf(item.getTotalInfraction()));
        domain.setNbValidTrips(BigDecimal.valueOf(item.getTotalValid()));

        var metricsDailiesOfMonth = repositoryPort.findMetricsDailyMetricsByKey(coRegion, coSection, yearMonth);
        aggregateIssuesTypeCounts(metricsDailiesOfMonth, domain);
        return domain;
    }

    private boolean shouldSkipUpdateDaily(VehicleTripDailySummaryDomain domain, GroupedDataTripMetrics item) {
        if (domain == null) {
            return false;
        }
        return domain.getNbAnomalies().compareTo(BigDecimal.valueOf(item.getTotalAnomaly())) == 0 &&
                domain.getNbInfractions().compareTo(BigDecimal.valueOf(item.getTotalInfraction())) == 0 &&
                domain.getNbValidTrips().compareTo(BigDecimal.valueOf(item.getTotalValid())) == 0;
    }

    private boolean shouldSkipUpdateMonthly(VehicleTripMonthlySummaryDomain domain, GroupedMonthlyTripMetrics item) {
        if (domain == null) {
            return false;
        }
        return domain.getNbAnomalies().compareTo(BigDecimal.valueOf(item.getTotalAnomaly())) == 0 &&
                domain.getNbInfractions().compareTo(BigDecimal.valueOf(item.getTotalInfraction())) == 0 &&
                domain.getNbValidTrips().compareTo(BigDecimal.valueOf(item.getTotalValid())) == 0;
    }

    private VehicleTripDailySummaryDomain createNewDailyDomain(String coRegion, String coSection, String yearMonth, String day) {
        var domain = new VehicleTripDailySummaryDomain();
        domain.setRegionCode(coRegion);
        domain.setSectionCode(coSection);
        domain.setLdtYearMonth(yearMonth);
        domain.setLdtDay(day);
        return domain;
    }

    private VehicleTripMonthlySummaryDomain createNewMonthlyDomain(String coRegion, String coSection, String yearMonth) {
        var domain = new VehicleTripMonthlySummaryDomain();
        domain.setRegionCode(coRegion);
        domain.setSectionCode(coSection);
        domain.setLdtYearMonth(yearMonth);
        return domain;
    }

    public void populateIssueTypeCounts(GroupedDataTripMetrics metrics, VehicleTripDailySummaryDomain dailySummaryDomain) throws Exception {

        // Build anomaly type counts
        Map<String, Integer> anomalyMap = metrics.getItems().stream()
                .filter(item -> item.codeIssue().startsWith("ANO"))
                .collect(Collectors.toMap(
                        RawTripMetricsDataRecord::codeIssue,
                        RawTripMetricsDataRecord::countAnomaly,
                        Integer::sum
                ));

        // Build infraction type counts
        Map<String, Integer> infractionMap = metrics.getItems().stream()
                .filter(item -> item.codeIssue().startsWith("INF"))
                .collect(Collectors.toMap(
                        RawTripMetricsDataRecord::codeIssue,
                        RawTripMetricsDataRecord::countInfraction,
                        Integer::sum
                ));
        // Set to domain
        dailySummaryDomain.setAnomalyTypeCounts(objectMapper.writeValueAsString(anomalyMap));
        dailySummaryDomain.setInfractionTypeCounts(objectMapper.writeValueAsString(infractionMap));
    }

    public void aggregateIssuesTypeCounts(List<VehicleTripDailySummaryDomain> dailies, VehicleTripMonthlySummaryDomain monthlySummaryDomain) throws Exception {
        Map<String, Integer> aggregatedAnomalies = new HashMap<>();
        Map<String, Integer> aggregatedInfractions = new HashMap<>();

        for (VehicleTripDailySummaryDomain record : dailies) {
            String anomalyJson = record.getAnomalyTypeCounts();
            String infractionJson = record.getInfractionTypeCounts();

            if (anomalyJson != null && !anomalyJson.isBlank()) {
                Map<String, Integer> anomalyMap = objectMapper.readValue(anomalyJson, new TypeReference<>() {});
                for (var entry : anomalyMap.entrySet()) {
                    aggregatedAnomalies.merge(entry.getKey(), entry.getValue(), Integer::sum);
                }
            }

            if (infractionJson != null && !infractionJson.isBlank()) {
                Map<String, Integer> infractionMap = objectMapper.readValue(infractionJson, new TypeReference<>() {});
                for (var entry : infractionMap.entrySet()) {
                    aggregatedInfractions.merge(entry.getKey(), entry.getValue(), Integer::sum);
                }
            }
        }
        monthlySummaryDomain.setAnomalyTypeCounts(objectMapper.writeValueAsString(aggregatedAnomalies));
        monthlySummaryDomain.setInfractionTypeCounts(objectMapper.writeValueAsString(aggregatedInfractions));
    }
    
}
