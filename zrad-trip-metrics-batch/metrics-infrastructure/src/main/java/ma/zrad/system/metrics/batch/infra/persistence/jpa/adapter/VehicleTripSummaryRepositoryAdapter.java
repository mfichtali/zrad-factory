package ma.zrad.system.metrics.batch.infra.persistence.jpa.adapter;

import lombok.RequiredArgsConstructor;
import ma.zrad.system.batch.common.domain.summary.VehicleTripDailySummaryDomain;
import ma.zrad.system.batch.common.domain.summary.VehicleTripMonthlySummaryDomain;
import ma.zrad.system.metrics.batch.domain.port.out.VehicleTripSummaryRepositoryPort;
import ma.zrad.system.metrics.batch.infra.mapper.GlobalMapper;
import ma.zrad.system.metrics.batch.infra.persistence.jpa.repository.VehicleTripDailySummaryJpaRepository;
import ma.zrad.system.metrics.batch.infra.persistence.jpa.repository.VehicleTripMonthlySummaryJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VehicleTripSummaryRepositoryAdapter implements VehicleTripSummaryRepositoryPort {
    
    private final GlobalMapper mapper;
    private final VehicleTripDailySummaryJpaRepository vehicleTripDailySummaryJpaRepository;
    private final VehicleTripMonthlySummaryJpaRepository vehicleTripMonthlySummaryJpaRepository;
    
    @Override
    public VehicleTripDailySummaryDomain findMetricsMonthlySummaryByKey(final String coRegion, final String coSection, final String yearMonth, final String day) {
        var entity = vehicleTripDailySummaryJpaRepository.getMetricsDailySummaryByKey(coRegion, coSection, yearMonth, day);
        return mapper.vehicleTripDailySummaryToDomain(entity);
    }

    @Override
    public VehicleTripMonthlySummaryDomain findMetricsMonthlySummaryByKey(final String coRegion, final String coSection, final String yearMonth) {
        var entity = vehicleTripMonthlySummaryJpaRepository.findMetricsMonthlySummaryByKey(coRegion, coSection, yearMonth);
        return mapper.vehicleTripMonthlySummaryToDomain(entity);
    }

    @Override
    public void saveAllDailyMetrics(final List<VehicleTripDailySummaryDomain> items) {
        var entities = mapper.vehicleTripDailySummaryToEntities(items);
        vehicleTripDailySummaryJpaRepository.saveAll(entities);
    }

    @Override
    public void saveAllMonthlyMetrics(final List<VehicleTripMonthlySummaryDomain> items) {
        var entities = mapper.vehicleTripMonthlySummaryToEntities(items);
        vehicleTripMonthlySummaryJpaRepository.saveAll(entities);
    }

    @Override
    public List<VehicleTripDailySummaryDomain> findMetricsDailyMetricsByKey(final String coRegion, final String coSection, final String yearMonth) {
        var entity = vehicleTripDailySummaryJpaRepository.findMetricsDailySummaryByKey(coRegion, coSection, yearMonth);
        return mapper.vehicleTripDailSummaryToDomains(entity);
    }
}
