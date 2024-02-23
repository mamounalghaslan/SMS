package mgkm.smsbackend.services;

import mgkm.smsbackend.models.Camera;
import mgkm.smsbackend.models.CameraReferenceImage;
import mgkm.smsbackend.models.CameraStatusType;

import java.util.List;

public interface CamerasService {

    Integer addNewCamera(Camera camera);

    void deleteCamera(Integer cameraId);

    Camera getCamera(Integer cameraId);

    List<Camera> getAllCameras();

    List<CameraStatusType> getAllCameraStatusTypes();

    CameraReferenceImage getCameraReferenceImage(Integer cameraId);

}
