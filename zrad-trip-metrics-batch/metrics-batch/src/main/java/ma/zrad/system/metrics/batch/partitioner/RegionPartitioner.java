package ma.zrad.system.metrics.batch.partitioner;

import ma.zrad.system.batch.common.utils.BatchConstantUtils;
import ma.zrad.system.metrics.batch.service.ReferentialRegionLoaderApiService;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class RegionPartitioner implements Partitioner {

    @Autowired
    private ReferentialRegionLoaderApiService refApiService;
    
    @Override
    public Map<String, ExecutionContext> partition(final int gridSize) {
        Map<String, ExecutionContext> partitions = new HashMap<>();
        var regionDetails = refApiService.getRegionDetails();
        IntStream.range(0, regionDetails.size()).forEach(i -> {
            var regionInfo = regionDetails.get(i);
            ExecutionContext context = new ExecutionContext();
            context.putString(BatchConstantUtils.PARAM_REGION_CODE, regionInfo.getCodeRegion());
            partitions.put("partition_" + i, context);
        });
        return partitions;
    }
}
