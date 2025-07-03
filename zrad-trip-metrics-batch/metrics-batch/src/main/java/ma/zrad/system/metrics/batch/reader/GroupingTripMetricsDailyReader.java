package ma.zrad.system.metrics.batch.reader;

import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.pojo.GroupedDataTripMetrics;
import ma.zrad.system.batch.common.records.GroupedKeyTripMetricsRecord;
import ma.zrad.system.batch.common.records.RawTripMetricsDataRecord;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@StepScope
@Component
public class GroupingTripMetricsDailyReader implements ItemStreamReader<GroupedDataTripMetrics> {
    
    private final ItemReader<RawTripMetricsDataRecord> delegate;
    private Iterator<Map.Entry<GroupedKeyTripMetricsRecord, List<RawTripMetricsDataRecord>>> iterator;
    private boolean dataLoaded = false;

    public GroupingTripMetricsDailyReader(ItemReader<RawTripMetricsDataRecord> delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public GroupedDataTripMetrics read() throws Exception {

        if(!dataLoaded) {
            loadAndGroupItem();
            dataLoaded = true;
        }

        if (iterator != null && iterator.hasNext()) {
            Map.Entry<GroupedKeyTripMetricsRecord, List<RawTripMetricsDataRecord>> entry = iterator.next();
            return new GroupedDataTripMetrics(entry.getKey(), entry.getValue());
        }
        return null;
    }

    private void loadAndGroupItem() throws Exception {
        Map<GroupedKeyTripMetricsRecord, List<RawTripMetricsDataRecord>> groupedItem = new HashMap<>();
        RawTripMetricsDataRecord item;
        while ((item = delegate.read()) != null) {
            GroupedKeyTripMetricsRecord key = new GroupedKeyTripMetricsRecord(
                    item.ldtYearMonth(),
                    item.ldtDay(),
                    item.coRegion(),
                    item.coSection()
            );
            groupedItem.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
        }
        iterator = groupedItem.entrySet().iterator();
    }

    @Override
    public void open(final ExecutionContext executionContext) throws ItemStreamException {
        if (delegate instanceof ItemStreamReader) {
            ((ItemStreamReader<?>) delegate).open(executionContext);
        }
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        if (delegate instanceof ItemStreamReader) {
            ((ItemStreamReader<?>) delegate).update(executionContext);
        }
    }
    
    @Override
    public void close() throws ItemStreamException {
        if (delegate instanceof ItemStreamReader) {
            ((ItemStreamReader<?>) delegate).close();
        }
    }
}
