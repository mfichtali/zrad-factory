package ma.zrad.system.batch.common.pojo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ma.zrad.system.batch.common.records.GroupedKeyTripMetricsMonthlyRecord;
import ma.zrad.system.batch.common.records.RawTripMetricsMonthlyRecord;

import java.io.Serializable;
import java.util.List;

@Accessors(chain = true)
@EqualsAndHashCode
@Getter
@Setter
public class GroupedMonthlyTripMetrics implements Serializable {

    private GroupedKeyTripMetricsMonthlyRecord key;
    private List<RawTripMetricsMonthlyRecord> items;
    private int totalAnomaly;
    private int totalInfraction;
    private int totalValid;
    
    public GroupedMonthlyTripMetrics(GroupedKeyTripMetricsMonthlyRecord key, List<RawTripMetricsMonthlyRecord> items) {
        this.key = key;
        this.items = items;
        this.totalAnomaly = items.stream().mapToInt(RawTripMetricsMonthlyRecord::countAnomaly).sum();
        this.totalInfraction = items.stream().mapToInt(RawTripMetricsMonthlyRecord::countInfraction).sum();
        this.totalValid = items.stream().mapToInt(RawTripMetricsMonthlyRecord::countValid).sum();
    }
}
