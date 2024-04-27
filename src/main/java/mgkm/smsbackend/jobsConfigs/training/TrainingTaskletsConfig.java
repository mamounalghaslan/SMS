package mgkm.smsbackend.jobsConfigs.training;

import mgkm.smsbackend.jobsConfigs.listeners.JobListener;
import mgkm.smsbackend.jobsConfigs.listeners.StepListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public class TrainingTaskletsConfig {

    protected Step dataPreparationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Training Data Preparation Step", jobRepository)
                .listener(new StepListener())
                .tasklet(new TrainingDataPreparationTasklet(), transactionManager)
                .build();
    }

    protected Step inferenceStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Training Step", jobRepository)
                .listener(new StepListener())
                .tasklet(new TrainingTasklet(), transactionManager)
                .build();
    }

    protected Step outputStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Training Output Step", jobRepository)
                .listener(new StepListener())
                .tasklet(new TrainingOutputTasklet(), transactionManager)
                .build();
    }

    public Job trainingJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {

        return new JobBuilder("Training Job", jobRepository)
                .listener(new JobListener())
                .start(dataPreparationStep(jobRepository, transactionManager))
                .next(inferenceStep(jobRepository, transactionManager))
                .next(outputStep(jobRepository, transactionManager))
                .build();
    }

}
