package ma.zrad.system.batch.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TableNameUtils {

    public static final String ZRAD_DEFAULT_SCHEMA = "ZRAD_BATCH_METADATA";

    public static final String REF_REGION = "REF_REGION";
    public static final String REF_SECTION = "REF_SECTION";
    public static final String REF_RADAR = "REF_RADAR";
    public static final String REF_SPEED_SECTION = "REF_SPEED_SECTION";

    public static final String T_EVENT_BUSINESS = "T_EVENT_BUSINESS";
    public static final String T_EVENT_BUSINESS_STATUS_H = "T_EVENT_BUSINESS_STATUS_H";
    public static final String T_FILE_IMPORTER_METRICS = "T_FILE_IMPORTER_METRICS";

    public static final String T_VEHICLE_PASSAGE_IMPORTER = "T_VEHICLE_PASSAGE_IMPORTER";
    public static final String T_VEHICLE_TRIP_ANALYSIS = "T_VEHICLE_TRIP_ANALYSIS";
    public static final String T_VEHICLE_TRIP_ANOMALY = "T_VEHICLE_TRIP_ANOMALY";
    public static final String T_VEHICLE_TRIP_INFRACTION = "T_VEHICLE_TRIP_INFRACTION";
    public static final String T_VEHICLE_TRIP_VALID = "T_VEHICLE_TRIP_VALID";
    public static final String T_VEHICLE_TRIP_DAILY_SUMMARY = "T_VEHICLE_TRIP_DAILY_SUMMARY";
    public static final String T_VEHICLE_TRIP_MONTHLY_SUMMARY = "T_VEHICLE_TRIP_MONTHLY_SUMMARY";
    public static final String T_VEHICLE_TRIP_QUARTERLY_SUMMARY = "T_VEHICLE_TRIP_QUARTERLY_SUMMARY";
    public static final String T_VEHICLE_TRIP_YEARLY_SUMMARY = "T_VEHICLE_TRIP_YEARLY_SUMMARY";
}
