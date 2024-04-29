package mgkm.smsbackend.services;

import mgkm.smsbackend.models.Model;
import mgkm.smsbackend.models.ModelType;
import mgkm.smsbackend.models.ProductReference;
import mgkm.smsbackend.repositories.ModelRepository;
import mgkm.smsbackend.repositories.ModelTypeRepository;
import mgkm.smsbackend.utilities.JSONReader;
import mgkm.smsbackend.utilities.PythonCaller;
import mgkm.smsbackend.utilities.DirectoryUtilities;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;


@Service
public class ModelService {

    private final ModelTypeRepository modelTypeRepository;
    private final ModelRepository modelRepository;

    public ModelService(ModelTypeRepository modelTypeRepository,
                        ModelRepository modelRepository) {
        this.modelTypeRepository = modelTypeRepository;
        this.modelRepository = modelRepository;
    }

    public List<ProductReference> detectProducts(String imagePath) {

        PythonCaller.callPython(
                DirectoryUtilities.detectionPredictScriptPath,
                DirectoryUtilities.detectionModelPath,
                imagePath,
                DirectoryUtilities.detectionResultsPath
        );

        return JSONReader.readProductReferencesBoxesJSON(DirectoryUtilities.detectionResultsPath);

    }

    public List<ModelType> getModelTypes() {
        return (List<ModelType>) this.modelTypeRepository.findAll();
    }

    public List<Model> getAllModels() {
        List<Model> models = (List<Model>) this.modelRepository.findAll();
        models.sort(Comparator.comparing(Model::getCreationDate));
        return models;
    }

}
