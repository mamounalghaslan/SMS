package mgkm.smsbackend.controllers;

import lombok.AllArgsConstructor;
import mgkm.smsbackend.jobsConfigs.JobConfig;
import mgkm.smsbackend.models.Model;
import mgkm.smsbackend.models.ModelType;
import mgkm.smsbackend.services.ModelService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/models")
@AllArgsConstructor
public class ModelsController extends BaseController {

    private final ModelService modelService;

    private final JobConfig jobConfig;

    // Inference -------------------------------------------------------------

    @PostMapping("/start-inference")
    public String startInferenceJob(@RequestBody Model model) {
        return jobConfig.startInferenceJob(model);
    }

    @PostMapping("/stop-inference")
    public String stopInferenceJob() {
        return jobConfig.stopInferenceJob();
    }

    @GetMapping("/inference-job-status")
    public String getInferenceJobStatus() {
        return String.valueOf(jobConfig.isInferenceJobRunning());
    }

    // Training -------------------------------------------------------------

    @PostMapping("/start-training")
    public String startTrainingJob(@RequestBody ModelType modelType) {
        return jobConfig.startTrainingJob(modelType.getBackboneName());
    }

    @PostMapping("/stop-training")
    public String stopTrainingJob() {
        return jobConfig.stopTrainingJob();
    }

    @GetMapping("/training-job-status")
    public String getTrainingJobStatus() {
        return String.valueOf(jobConfig.isTrainingJobRunning());
    }

    // Models -------------------------------------------------------------

    @GetMapping("/models")
    public List<Model> getModels() {
        return this.modelService.getAllModels();
    }

    @GetMapping("/model-types")
    public List<ModelType> getModelTypes() {
        return this.modelService.getModelTypes();
    }

    @PostMapping("/initialize")
    public void initialize() throws IOException {
        this.modelService.initialize();
    }

    @DeleteMapping("/{modelId}")
    public void deleteModel(@PathVariable Integer modelId) throws IOException {
        this.modelService.deleteModel(modelId);
    }

}
