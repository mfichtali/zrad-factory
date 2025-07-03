package ma.zrad.system.vp.batch.tasklet;

import ma.zrad.system.batch.common.enums.EventBatchStatusEnum;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;

@Component
public class BatchFlowStatusDecider implements JobExecutionDecider {
    @Override
    public FlowExecutionStatus decide(final JobExecution jobExecution, final StepExecution stepExecution) {
        boolean hasNoData = jobExecution.getStepExecutions().stream()
                .anyMatch(step -> EventBatchStatusEnum.NO_DATA.name().equals(step.getExitStatus().getExitCode()));

        boolean hasFailure = jobExecution.getStepExecutions().stream()
                .anyMatch(step -> step.getStatus() == BatchStatus.FAILED);

        if (hasFailure) {
            return FlowExecutionStatus.FAILED;
        } else if (hasNoData) {
            return new FlowExecutionStatus(EventBatchStatusEnum.NO_DATA.name());
        } else {
            return new FlowExecutionStatus("CONTINUE");
        }
    }
}
