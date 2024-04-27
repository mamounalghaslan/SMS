package mgkm.smsbackend.jobsConfigs.inference;

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
public class InferenceTaskletsConfig {

    protected Step dataPreparationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Inference Data Preparation Step", jobRepository)
                .listener(new StepListener())
                .tasklet(new InferenceDataPreparationTasklet(), transactionManager)
                .build();
    }

    protected Step inferenceStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Inference Step", jobRepository)
                .listener(new StepListener())
                .tasklet(new InferenceTasklet(), transactionManager)
                .build();
    }

    protected Step outputStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Inference Output Step", jobRepository)
                .listener(new StepListener())
                .tasklet(new InferenceOutputTasklet(), transactionManager)
                .build();
    }

    public Job inferenceJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {

        return new JobBuilder("Inference Job", jobRepository)
                .listener(new JobListener())
                .start(dataPreparationStep(jobRepository, transactionManager))
                .next(inferenceStep(jobRepository, transactionManager))
                .next(outputStep(jobRepository, transactionManager))
                .build();
    }

}
