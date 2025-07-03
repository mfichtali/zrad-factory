package ma.zrad.system.metrics.batch.mapper;

import ma.zrad.system.batch.common.records.RawTripMetricsDataRecord;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TripMetricsRowMapper implements RowMapper<RawTripMetricsDataRecord> {

    @Override
    public RawTripMetricsDataRecord mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        return new RawTripMetricsDataRecord(
                rs.getString("ldt_year_month"),
                rs.getString("ldt_day"),
                rs.getString("co_region"),
                rs.getString("co_section_road"),
                rs.getString("co_issue"),
                rs.getInt("count_anomaly"),
                rs.getInt("count_infraction"),
                rs.getInt("count_valid")
        );
    }
}
