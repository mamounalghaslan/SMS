package mgkm.smsbackend.jobsConfigs.inference;

import jakarta.annotation.Nonnull;
import mgkm.smsbackend.jobsConfigs.listeners.JobListener;
import mgkm.smsbackend.models.Model;
import mgkm.smsbackend.services.ModelService;
import mgkm.smsbackend.utilities.DirectoryUtilities;
import mgkm.smsbackend.utilities.PythonCaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

public class InferenceTasklet implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(JobListener.class);

    private final ModelService modelService;
    private final Model model;

    public InferenceTasklet(ModelService modelService,
                            Model model) {
        this.modelService = modelService;
        this.model = model;
    }

    @Override
    public RepeatStatus execute(@Nonnull StepContribution contribution,
                                @Nonnull ChunkContext chunkContext) {

        log.info("Inference Tasklet");

        List<Model> allModels = this.modelService.getAllModels();
        allModels.forEach(model -> {
            model.setIsRunning(Objects.equals(model.getSystemId(), this.model.getSystemId()));
            modelService.saveModel(model);
        });

        // Generate the inference.yaml
        String inferenceConfig =
                "python: " + DirectoryUtilities.pythonPath + "\n"
                + "program: " + DirectoryUtilities.recognitionInferenceScriptPath + "\n"
                + "data_dir: " + DirectoryUtilities.getInferenceDataPath() + "\n"
                + "camera_names: all\n"
                + "backbone: " + model.getModelType().getBackboneName() + "\n"
                + "checkpoint: " + DirectoryUtilities.getRecognitionModelWeightsPath()
                        + "/" + model.getSystemId()
                        + "/" + model.getModelFileName() + "\n"
                + "yolo_weights: " + DirectoryUtilities.recognitionYoloWeightsPath + "\n"
                + "device: " + DirectoryUtilities.deviceType;

        log.info("Inference Config: {}", inferenceConfig);

        try {
            DirectoryUtilities.writeStringToFile(inferenceConfig, DirectoryUtilities.recognitionInferenceConfigPath);
        } catch (IOException | URISyntaxException e) {
            log.info("Failed to write inference config file.");
            throw new RuntimeException(e);
        }

        int exitCode = PythonCaller.callPython(
                DirectoryUtilities.recognitionLauncherScriptPath,
                DirectoryUtilities.recognitionInferenceConfigPath
        );

        if (exitCode != 0) {
            log.error("Inference failed with exit code: {}", exitCode);
            throw new RuntimeException("Inference failed with exit code: " + exitCode);
        }

        return RepeatStatus.FINISHED;
    }

}
