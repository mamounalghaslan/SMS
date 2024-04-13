package mgkm.smsbackend.services;

import mgkm.smsbackend.models.ProductReference;
import mgkm.smsbackend.utilities.ProductReferencesBoxesJSONReader;
import mgkm.smsbackend.utilities.PythonCaller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelService {

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

}
