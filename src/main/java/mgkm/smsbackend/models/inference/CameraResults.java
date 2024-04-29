package mgkm.smsbackend.models.inference;

import lombok.Data;

import java.util.List;

@Data
public class CameraResults {

    private String camera;
    private List<InferenceOutput> results;

}
