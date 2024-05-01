package mgkm.smsbackend.jobsConfigs.listeners;

import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class JobListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(JobListener.class);

    @Override
    public void beforeJob(@Nonnull JobExecution jobExecution) {
        log.info("Job started: {}", jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(@Nonnull JobExecution jobExecution) {
        log.info("Job finished: {}", jobExecution.getJobInstance().getJobName());
    }

}
