package ma.zrad.system.metrics.batch.reader;

import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.pojo.GroupedMonthlyTripMetrics;
import ma.zrad.system.batch.common.records.GroupedKeyTripMetricsMonthlyRecord;
import ma.zrad.system.batch.common.records.RawTripMetricsMonthlyRecord;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@StepScope
@Component
public class GroupingTripMetricsMonthlyReader implements ItemStreamReader<GroupedMonthlyTripMetrics> {
    
    private final ItemReader<RawTripMetricsMonthlyRecord> delegate;
    private Iterator<Map.Entry<GroupedKeyTripMetricsMonthlyRecord, List<RawTripMetricsMonthlyRecord>>> iterator;
    private boolean dataLoaded = false;
    private String coRegion;
    private YearMonth ldtYearMonth;

    public GroupingTripMetricsMonthlyReader(ItemReader<RawTripMetricsMonthlyRecord> delegate, 
                                            String coRegion, 
                                            YearMonth ldtYearMonth) {
        this.delegate = delegate;
        this.coRegion = coRegion;
        this.ldtYearMonth = ldtYearMonth;
    }
    
    @Override
    public GroupedMonthlyTripMetrics read() throws Exception {

        if(!dataLoaded) {
            loadAndGroupItem();
            dataLoaded = true;
        }

        if (iterator != null && iterator.hasNext()) {
            Map.Entry<GroupedKeyTripMetricsMonthlyRecord, List<RawTripMetricsMonthlyRecord>> entry = iterator.next();
            return new GroupedMonthlyTripMetrics(entry.getKey(), entry.getValue());
        }
        return null;
    }

    private void loadAndGroupItem() throws Exception {
        Map<GroupedKeyTripMetricsMonthlyRecord, List<RawTripMetricsMonthlyRecord>> groupedItem = new HashMap<>();
        RawTripMetricsMonthlyRecord item;
        while ((item = delegate.read()) != null) {
            GroupedKeyTripMetricsMonthlyRecord key = new GroupedKeyTripMetricsMonthlyRecord(
                    item.ldtYearMonth(),
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
