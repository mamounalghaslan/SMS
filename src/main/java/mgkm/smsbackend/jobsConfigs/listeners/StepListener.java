package mgkm.smsbackend.jobsConfigs.listeners;

import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class StepListener implements StepExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(JobListener.class);

    @Override
    public void beforeStep(@Nonnull StepExecution stepExecution) {
        log.info("Before Step: {}", stepExecution.getStepName());
    }

    @Override
    public ExitStatus afterStep(@Nonnull StepExecution stepExecution) {
        log.info("After Step: {}", stepExecution.getStepName());

        if (!stepExecution.getFailureExceptions().isEmpty()) {
            log.warn("Exception caught in step: {}", stepExecution.getFailureExceptions().getFirst().toString());
            return ExitStatus.FAILED;
        }

        return ExitStatus.COMPLETED;
    }

}
