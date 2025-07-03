package ma.zrad.system.trip.batch.partitioner;

import ma.zrad.system.batch.common.pojo.FullRegionDetailsOut;
import ma.zrad.system.batch.common.pojo.SectionInfoOut;
import ma.zrad.system.batch.common.utils.BatchConstantUtils;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class SectionPartitioner implements Partitioner {

    @Value("#{jobExecutionContext['" + BatchConstantUtils.BATCH_REGION_DATA_CTX + "']}")
    private FullRegionDetailsOut regionInfoCtx;

    @Override
    public Map<String, ExecutionContext> partition(final int gridSize) {
        Map<String, ExecutionContext> partitions = new HashMap<>();

        var sectionCodes = regionInfoCtx.getSections().stream().map(SectionInfoOut::getCodeSection).toList();
        IntStream.range(0, sectionCodes.size()).forEach(i -> {
            String section = sectionCodes.get(i);
            ExecutionContext context = new ExecutionContext();
            context.putString(BatchConstantUtils.PARAM_REGION_CODE, regionInfoCtx.getCodeRegion());
            context.putString(BatchConstantUtils.PARAM_SECTION_CODE, section);
            partitions.put("partition_" + i, context);
        });
        return partitions;
    }
}
