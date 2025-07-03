package ma.zrad.system.trip.batch.mapper;

import ma.zrad.system.batch.common.records.VehiclePassageSimpleAggregateRecord;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class TripSimpleRowMapper implements RowMapper<VehiclePassageSimpleAggregateRecord> {

    @Override
    public VehiclePassageSimpleAggregateRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new VehiclePassageSimpleAggregateRecord(
                rs.getString("rn_vehicle"),
                rs.getString("co_region"),
                rs.getString("co_section_road"),
                rs.getObject("id_event", UUID.class),
                rs.getInt("count_input"),
                rs.getInt("count_output"),
                rs.getTimestamp("horo_input") != null ? rs.getTimestamp("horo_input").toLocalDateTime() : null,
                rs.getTimestamp("horo_output") != null ? rs.getTimestamp("horo_output").toLocalDateTime() : null,
                rs.getBigDecimal("speed_input"),
                rs.getBigDecimal("speed_output"),
                rs.getBoolean("in_anomaly"));
    }
}
