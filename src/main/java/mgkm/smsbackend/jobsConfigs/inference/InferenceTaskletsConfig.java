package mgkm.smsbackend.jobsConfigs.inference;

import mgkm.smsbackend.jobsConfigs.listeners.JobListener;
import mgkm.smsbackend.jobsConfigs.listeners.StepListener;
import mgkm.smsbackend.models.Model;
import mgkm.smsbackend.services.CamerasService;
import mgkm.smsbackend.services.ModelService;
import mgkm.smsbackend.services.ProductsService;
import mgkm.smsbackend.services.ShelfImageService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
public class InferenceTaskletsConfig {

    private final CamerasService camerasService;
    private final ShelfImageService shelfImageService;
    private final ProductsService productsService;
    private final ModelService modelService;

    public InferenceTaskletsConfig(CamerasService camerasService,
                                   ShelfImageService shelfImageService,
                                   ProductsService productsService,
                                   ModelService modelService) {
        this.camerasService = camerasService;
        this.shelfImageService = shelfImageService;
        this.productsService = productsService;
        this.modelService = modelService;
    }

    protected Step dataPreparationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Inference Data Preparation Step", jobRepository)
                .listener(new StepListener())
                .tasklet(new InferenceDataPreparationTasklet(
                        this.camerasService, this.shelfImageService), transactionManager)
                .build();
    }

    protected Step inferenceStep(JobRepository jobRepository,
                                 PlatformTransactionManager transactionManager,
                                 Model model) {
        return new StepBuilder("Inference Step", jobRepository)
                .listener(new StepListener())
                .tasklet(new InferenceTasklet(modelService, model), transactionManager)
                .build();
    }

    protected Step outputStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Inference Output Step", jobRepository)
                .listener(new StepListener())
                .tasklet(new InferenceOutputTasklet(
                        this.camerasService, this.shelfImageService, this.productsService), transactionManager)
                .build();
    }

    public Job inferenceJob(JobRepository jobRepository,
                            PlatformTransactionManager transactionManager,
                            Model model) {
        Step dataPreparationStep = dataPreparationStep(jobRepository, transactionManager);
        Step inferenceStep = inferenceStep(jobRepository, transactionManager, model);
        Step outputStep = outputStep(jobRepository, transactionManager);

        return new JobBuilder("Inference Job", jobRepository)
                .listener(new JobListener())
                .start(dataPreparationStep)
                .on("COMPLETED").to(inferenceStep)
                .from(inferenceStep).on("COMPLETED").to(outputStep)
                .from(outputStep).end()
                .build();
    }

}
