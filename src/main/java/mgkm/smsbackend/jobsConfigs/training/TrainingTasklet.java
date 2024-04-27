package mgkm.smsbackend.jobsConfigs.training;

import jakarta.annotation.Nonnull;
import mgkm.smsbackend.jobsConfigs.listeners.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class TrainingTasklet implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(JobListener.class);

    @Override
    public RepeatStatus execute(@Nonnull StepContribution contribution,
                                @Nonnull ChunkContext chunkContext) {
        log.info("Training Tasklet");
        return RepeatStatus.FINISHED;
    }

}