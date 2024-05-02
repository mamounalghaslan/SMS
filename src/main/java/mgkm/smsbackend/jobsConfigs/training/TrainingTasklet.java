package mgkm.smsbackend.jobsConfigs.training;

import jakarta.annotation.Nonnull;
import mgkm.smsbackend.jobsConfigs.listeners.JobListener;
import mgkm.smsbackend.utilities.DirectoryUtilities;
import mgkm.smsbackend.utilities.PythonCaller;
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


        // Generate the train.yaml
        String trainConfig =
                "python: " + DirectoryUtilities.pythonPath + "\n"
                + "program: " + DirectoryUtilities.recognitionTrainScriptPath + "\n"
                + "backbone: resnet18\n"
                + "dataset: danube\n"
                + "data_dir: " + DirectoryUtilities.getInferenceDataPath().replace('/', '\\') + "\n"
                + "output_dir: " + DirectoryUtilities.getRecognitionModelWeightsPath() + "\n"
                + "full_training: true\n"
                + "batch_size: 256\n"
                + "save_every: 50\n"
                + "epochs: 80\n"
                + "test_percent: 0.0\n"
                + "epsilon: 0\n"
                + "lr: 0.01\n"
                + "lr_decay: cosine\n"
                + "lr_decay_epochs: 26,53\n"
                + "n_views: 2\n"
                + "device: " + DirectoryUtilities.deviceType;

        log.info("Train Config: {}", "\n"+trainConfig);

        try {
            DirectoryUtilities.writeStringToFile(trainConfig, DirectoryUtilities.recognitionTrainConfigPath);
        } catch (Exception e) {
            log.info("Failed to write train config file.");
            throw new RuntimeException(e);
        }

        int exitCode = PythonCaller.callPython(
                DirectoryUtilities.recognitionLauncherScriptPath,
                DirectoryUtilities.recognitionTrainConfigPath
        );

        if (exitCode != 0) {
            log.error("Training failed with exit code: {}", exitCode);
            throw new RuntimeException("Training failed with exit code: " + exitCode);
        }

        return RepeatStatus.FINISHED;
    }

}