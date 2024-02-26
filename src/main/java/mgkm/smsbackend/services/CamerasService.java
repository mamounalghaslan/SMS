package mgkm.smsbackend.services;

import mgkm.smsbackend.models.Camera;
import java.util.List;

public interface CamerasService {

    Camera getCamera(Integer cameraId);

    void deleteCamera(Integer cameraId);

    Integer addNewCamera(Camera camera);

    List<Camera> getAllCameras();

}
