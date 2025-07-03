package ma.zrad.system.batch.common.records;

import java.io.Serializable;

public record RawTripMetricsDataRecord(
        String ldtYearMonth,
        String ldtDay,
        String coRegion,
        String coSection,
        String codeIssue,
        int countAnomaly,
        int countInfraction,
        int countValid
) implements Serializable {}
