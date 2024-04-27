package mgkm.smsbackend.services;

import mgkm.smsbackend.models.Model;
import mgkm.smsbackend.models.ModelType;
import mgkm.smsbackend.models.ProductReference;
import mgkm.smsbackend.repositories.ModelRepository;
import mgkm.smsbackend.repositories.ModelTypeRepository;
import mgkm.smsbackend.utilities.ProductReferencesBoxesJSONReader;
import mgkm.smsbackend.utilities.PythonCaller;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${detection-model-path}")
    private String detectionModelLocation;

    @Value("${detection-predict-script-path}")
    private String detectionPredictScriptLocation;

    @Value("${detection-results-path}")
    private String detectionResultsLocation;

    public List<ProductReference> detectProducts(String imagePath) {

        PythonCaller.callPython(
                detectionPredictScriptLocation,
                detectionModelLocation,
                imagePath,
                detectionResultsLocation
        );

        return ProductReferencesBoxesJSONReader.readProductReferencesBoxesJSON(detectionResultsLocation);

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
