package ma.zrad.system.batch.common.records;

import java.io.Serializable;

public record GroupedKeyTripMetricsRecord(
        String ldtYearMonth,
        String ldtDay,
        String coRegion,
        String coSection
) implements Serializable {}
