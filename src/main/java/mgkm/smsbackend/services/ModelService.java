package mgkm.smsbackend.services;

import mgkm.smsbackend.jobsConfigs.listeners.JobListener;
import mgkm.smsbackend.models.Model;
import mgkm.smsbackend.models.ModelType;
import mgkm.smsbackend.models.ProductReference;
import mgkm.smsbackend.repositories.ModelRepository;
import mgkm.smsbackend.repositories.ModelTypeRepository;
import mgkm.smsbackend.utilities.JSONReader;
import mgkm.smsbackend.utilities.PythonCaller;
import mgkm.smsbackend.utilities.DirectoryUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;


@Service
public class ModelService {

    private static final Logger log = LoggerFactory.getLogger(JobListener.class);

    private final ModelTypeRepository modelTypeRepository;
    private final ModelRepository modelRepository;

    public ModelService(ModelTypeRepository modelTypeRepository,
                        ModelRepository modelRepository) {
        this.modelTypeRepository = modelTypeRepository;
        this.modelRepository = modelRepository;
    }

    public List<ProductReference> detectProducts(String imagePath) {

        int exitCode = PythonCaller.callPython(
                DirectoryUtilities.detectionPredictScriptPath,
                DirectoryUtilities.detectionModelPath,
                imagePath,
                DirectoryUtilities.detectionResultsPath
        );

        if (exitCode != 0) {
            log.error("Detection failed with exit code: {}", exitCode);
            throw new RuntimeException("Detection failed with exit code: " + exitCode);
        }

        return JSONReader.readProductReferencesBoxesJSON(DirectoryUtilities.detectionResultsPath);

    }

    public Model saveModel(Model model) {
        return this.modelRepository.save(model);
    }

    public List<ModelType> getModelTypes() {
        return (List<ModelType>) this.modelTypeRepository.findAll();
    }

    public List<Model> getAllModels() {
        List<Model> models = (List<Model>) this.modelRepository.findAll();
        models.sort(Comparator.comparing(Model::getCreationDate));
        return models;
    }

    public void initialize() throws IOException {
        ModelType modelType = new ModelType(null, "resnet18");
        this.modelTypeRepository.save(modelType);

        Model model = new Model(
                null,
                modelType,
                LocalDateTime.now(),
                false
                , "checkpoint.pth");

        this.modelRepository.save(model);

        model.setModelFileName(model.getSystemId() + "_weights.pth");

        this.modelRepository.save(model);

        DirectoryUtilities.purgeOrCreateDirectory(
                DirectoryUtilities.getRecognitionModelWeightsPath() + "/" + model.getSystemId());

        DirectoryUtilities.copyFileToDirectory(
                DirectoryUtilities.getRecognitionModelWeightsPath() + "/checkpoint_80.pth",
                DirectoryUtilities.getRecognitionModelWeightsPath() + "/" + model.getSystemId() + "/" + model.getModelFileName()
        );

    }

    public void deleteModel(Integer modelId) throws IOException {

        Model model = this.modelRepository.findById(modelId).orElse(null);

        if(model != null) {
            DirectoryUtilities.purgeDirectory(
                    DirectoryUtilities.getRecognitionModelWeightsPath() + "/" + model.getSystemId());

            this.modelRepository.delete(model);
        }

    }

}
