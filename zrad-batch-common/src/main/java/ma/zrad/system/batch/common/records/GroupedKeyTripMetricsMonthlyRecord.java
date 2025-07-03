package ma.zrad.system.batch.common.records;

import java.io.Serializable;

public record GroupedKeyTripMetricsMonthlyRecord(
        String ldtYearMonth,
        String coRegion,
        String coSection
) implements Serializable {}
