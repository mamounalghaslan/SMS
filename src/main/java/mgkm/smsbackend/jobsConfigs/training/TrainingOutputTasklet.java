package mgkm.smsbackend.jobsConfigs.training;

import jakarta.annotation.Nonnull;
import mgkm.smsbackend.jobsConfigs.listeners.JobListener;
import mgkm.smsbackend.models.Model;
import mgkm.smsbackend.models.ModelType;
import mgkm.smsbackend.services.ModelService;
import mgkm.smsbackend.utilities.DirectoryUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDateTime;

public class TrainingOutputTasklet implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(JobListener.class);

    private final ModelService modelService;

    public TrainingOutputTasklet(ModelService modelService) {
        this.modelService = modelService;
    }

    @Override
    public RepeatStatus execute(@Nonnull StepContribution contribution,
                                @Nonnull ChunkContext chunkContext)  {
        log.info("Training Output Tasklet");

        Model newModel = new Model();
        newModel.setModelType(modelService.getModelTypes().getFirst());
        newModel.setCreationDate(LocalDateTime.now());
        newModel.setIsRunning(false);

        modelService.saveModel(newModel);

        newModel.setModelFileName(newModel.getSystemId() + "_weights.pth");

        modelService.saveModel(newModel);

        try {
            DirectoryUtilities.purgeOrCreateDirectory(
                    DirectoryUtilities.getRecognitionModelWeightsPath() + "/" + newModel.getSystemId());
            DirectoryUtilities.copyFileToDirectory(
                    DirectoryUtilities.getRecognitionModelWeightsPath() + "/checkpoint.pth",
                    DirectoryUtilities.getRecognitionModelWeightsPath() + "/" + newModel.getSystemId() + "/" + newModel.getModelFileName()
            );
        } catch (Exception e) {
            log.error("Failed to copy the model weights directory.");
            throw new RuntimeException(e);
        }

        return RepeatStatus.FINISHED;
    }

}