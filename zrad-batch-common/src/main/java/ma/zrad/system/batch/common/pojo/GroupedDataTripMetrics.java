package ma.zrad.system.batch.common.pojo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ma.zrad.system.batch.common.records.GroupedKeyTripMetricsRecord;
import ma.zrad.system.batch.common.records.RawTripMetricsDataRecord;

import java.io.Serializable;
import java.util.List;

@Accessors(chain = true)
@EqualsAndHashCode
@Getter
@Setter
public class GroupedDataTripMetrics implements Serializable {

    private GroupedKeyTripMetricsRecord key;
    private List<RawTripMetricsDataRecord> items;
    private int totalAnomaly;
    private int totalInfraction;
    private int totalValid;
    
    public GroupedDataTripMetrics(GroupedKeyTripMetricsRecord key, List<RawTripMetricsDataRecord> items) {
        this.key = key;
        this.items = items;
        this.totalAnomaly = items.stream().mapToInt(RawTripMetricsDataRecord::countAnomaly).sum();
        this.totalInfraction = items.stream().mapToInt(RawTripMetricsDataRecord::countInfraction).sum();
        this.totalValid = items.stream().mapToInt(RawTripMetricsDataRecord::countValid).sum();
    }
}
