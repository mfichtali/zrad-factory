package ma.zrad.system.metrics.batch.mapper;

import ma.zrad.system.batch.common.records.RawTripMetricsMonthlyRecord;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TripMetricsMonthlyRowMapper implements RowMapper<RawTripMetricsMonthlyRecord> {

    @Override
    public RawTripMetricsMonthlyRecord mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        return new RawTripMetricsMonthlyRecord(
                rs.getString("ldt_year_month"),
                rs.getString("co_region"),
                rs.getString("co_section_road"),
                rs.getInt("count_anomaly"),
                rs.getInt("count_infraction"),
                rs.getInt("count_valid")
        );
    }
}
