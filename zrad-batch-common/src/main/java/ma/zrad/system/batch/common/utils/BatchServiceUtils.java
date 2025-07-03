package ma.zrad.system.batch.common.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ma.zrad.system.batch.common.enums.BatchErrorTypeEnum;
import ma.zrad.system.batch.common.exceptions.BusinessException;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;

import java.util.List;

@Slf4j
@UtilityClass
public class BatchServiceUtils {

    public static ExitStatus determineFailureExitStatus(final StepExecution stepExecution) {
        List<Throwable> failureExceptions = stepExecution.getFailureExceptions();
        failureExceptions.stream()
                .findFirst()
                .ifPresent(exception -> {
                    log.error("Error during Step Vehicle processing execution: {}", exception.getMessage(), exception);
                    if (exception instanceof BusinessException) {
                        stepExecution.setExitStatus(new ExitStatus(BatchErrorTypeEnum.FUNCTIONAL.name()));
                        BatchThreadContext.put(BatchConstantUtils.BATCH_ERROR_MESSAGE_CTX, String.format("FUNCTIONAL ERROR : %s", exception.getMessage()));
                    } else {
                        BatchThreadContext.put(BatchConstantUtils.BATCH_ERROR_MESSAGE_CTX, String.format("TECHNICAL ERROR : %s", exception.getMessage()));
                    }
                });
        return stepExecution.getExitStatus();
    }
}
