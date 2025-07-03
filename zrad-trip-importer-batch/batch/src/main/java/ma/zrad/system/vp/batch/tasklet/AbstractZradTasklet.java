package ma.zrad.system.vp.batch.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

public class AbstractZradTasklet implements Tasklet {

    public static final String JAVA_TMP_DIR_PROPERTY = System.getProperty("java.io.tmpdir");
    public static final String ENV_BATCH_WORK_DIR = "BATCH_WORK_DIR";


    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
        return null;
    }

    public ExecutionContext getJobExecutionContext(ChunkContext chunkContext) {
        return chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
    }

    public StepExecution getStepExecutionContext(ChunkContext chunkContext) {
        return chunkContext.getStepContext().getStepExecution();
    }
}
