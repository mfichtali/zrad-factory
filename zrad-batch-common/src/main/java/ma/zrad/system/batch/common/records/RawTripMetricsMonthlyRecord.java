package ma.zrad.system.batch.common.records;

import java.io.Serializable;

public record RawTripMetricsMonthlyRecord(
        String ldtYearMonth,
        String coRegion,
        String coSection,
        int countAnomaly,
        int countInfraction,
        int countValid
) implements Serializable {}
