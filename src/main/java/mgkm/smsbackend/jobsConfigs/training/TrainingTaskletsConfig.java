package mgkm.smsbackend.jobsConfigs.training;

import mgkm.smsbackend.jobsConfigs.listeners.JobListener;
import mgkm.smsbackend.jobsConfigs.listeners.StepListener;
import mgkm.smsbackend.services.CamerasService;
import mgkm.smsbackend.services.ModelService;
import mgkm.smsbackend.services.ShelfImageService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public class TrainingTaskletsConfig {

    private final CamerasService camerasService;
    private final ShelfImageService shelfImageService;
    private final ModelService modelService;

    public TrainingTaskletsConfig(CamerasService camerasService,
                                  ShelfImageService shelfImageService,
                                  ModelService modelService) {
        this.camerasService = camerasService;
        this.shelfImageService = shelfImageService;
        this.modelService = modelService;
    }

    protected Step dataPreparationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Training Data Preparation Step", jobRepository)
                .listener(new StepListener())
                .tasklet(new TrainingDataPreparationTasklet(
                        this.camerasService, this.shelfImageService), transactionManager)
                .build();
    }

    protected Step trainingStep(JobRepository jobRepository,
                                PlatformTransactionManager transactionManager,
                                String backboneName) {
        return new StepBuilder("Training Step", jobRepository)
                .listener(new StepListener())
                .tasklet(new TrainingTasklet(backboneName), transactionManager)
                .build();
    }

    protected Step outputStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Training Output Step", jobRepository)
                .listener(new StepListener())
                .tasklet(new TrainingOutputTasklet(this.modelService), transactionManager)
                .build();
    }

    public Job trainingJob(JobRepository jobRepository,
                           PlatformTransactionManager transactionManager,
                           String backboneName) {

        return new JobBuilder("Training Job", jobRepository)
                .listener(new JobListener())
                .start(dataPreparationStep(jobRepository, transactionManager))
                .next(trainingStep(jobRepository, transactionManager, backboneName))
                .next(outputStep(jobRepository, transactionManager))
                .build();
    }

}
