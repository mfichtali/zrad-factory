package ma.zrad.system.batch.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventBusinessTypeEnum {

    VEHICLE_PASSAGE_IMPORT("Init Vehicle Passage Import"),
    VEHICLE_TRIP_ANALYSIS("Vehicle Trip Analysis"),
    VEHICLE_TRIP_STATS("Vehicle Trip Statistics"),
    VEHICLE_TRIP_METRICS("Vehicle Trip Metrics"),
    ;
    public final String label;

}
