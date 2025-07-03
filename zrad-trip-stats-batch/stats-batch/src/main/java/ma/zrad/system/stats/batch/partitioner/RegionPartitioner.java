package ma.zrad.system.stats.batch.partitioner;

import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.domain.EventBusinessDomain;
import ma.zrad.system.batch.common.enums.EventBatchContextEnum;
import ma.zrad.system.batch.common.pojo.FullRegionDetailsOut;
import ma.zrad.system.batch.common.utils.BatchConstantUtils;
import ma.zrad.system.stats.batch.domain.port.in.EventBusinessService;
import ma.zrad.system.stats.batch.service.ReferentialRegionLoaderApiService;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RegionPartitioner implements Partitioner {

    @Autowired
    private ReferentialRegionLoaderApiService refApiService;

    @Autowired
    private EventBusinessService eventBusinessService;

    @Override
    public Map<String, ExecutionContext> partition(final int gridSize) {
        Map<String, ExecutionContext> partitions = new HashMap<>();
        var allEventIds = new ArrayList<>();

        var regionDetails = refApiService.getRegionDetails();

        int partitionIndex = 0;
        for (FullRegionDetailsOut region : regionDetails) {
            String regionCode = region.getCodeRegion();
            var pendingEvents = eventBusinessService.findPendingEventByRegionOfBatch(EventBatchContextEnum.ZRAD02, regionCode);
            if (pendingEvents.isEmpty()) {
                log.info("✅ No pending events for region {}, skipping partition", regionCode);
                continue;
            }

            var eventIds = pendingEvents.stream().map(EventBusinessDomain::getId).toList();
            allEventIds.addAll(eventIds);
            ExecutionContext context = new ExecutionContext();
            context.putString(BatchConstantUtils.PARAM_REGION_CODE, regionCode);
            context.put(BatchConstantUtils.BATCH_CTX_EVENT_ZRAD02_IDS, eventIds);
            partitions.put("partition_" + partitionIndex++, context);
        }

        StepExecution stepExecution = StepSynchronizationManager.getContext().getStepExecution();
        stepExecution.getJobExecution().getExecutionContext().put(BatchConstantUtils.BATCH_ALL_EVENT_ZRAD02_IDS, allEventIds);

        return partitions;
    }
}
