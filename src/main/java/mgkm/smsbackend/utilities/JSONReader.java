package mgkm.smsbackend.utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import mgkm.smsbackend.models.ProductReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import mgkm.smsbackend.models.inference.CameraResults;

public class JSONReader {

    public static List<ProductReference> readProductReferencesBoxesJSON(String jsonFilePath) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(
                    new File(jsonFilePath),
                    new TypeReference<>() {
                    }
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<CameraResults> readCameraResultsJSON(String jsonFilePath) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(
                    new File(jsonFilePath),
                    new TypeReference<>() {
                    }
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
