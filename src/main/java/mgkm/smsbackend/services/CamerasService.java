package mgkm.smsbackend.services;

import mgkm.smsbackend.models.Camera;
import mgkm.smsbackend.models.CameraStatusType;
import mgkm.smsbackend.repositories.CameraRepository;
import mgkm.smsbackend.repositories.CameraStatusTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CamerasService {

    private final CameraRepository cameraRepository;

    private final CameraStatusTypeRepository cameraStatusTypeRepository;

    public CamerasService(CameraRepository cameraRepository,
                          CameraStatusTypeRepository cameraStatusTypeRepository) {
        this.cameraRepository = cameraRepository;
        this.cameraStatusTypeRepository = cameraStatusTypeRepository;
    }

    public Camera getCamera(Integer cameraId) {
        return this.cameraRepository.findById(cameraId).orElse(null);
    }

    public void deleteCamera(Integer cameraId) {
        this.cameraRepository.deleteById(cameraId);
    }

    public Integer addNewCamera(Camera camera) {
        CameraStatusType connectedCameraStatusType = cameraStatusTypeRepository.findById(4).orElse(null);
        if (connectedCameraStatusType != null) {
            camera.setCameraStatusType(connectedCameraStatusType);
        } else {
            throw new RuntimeException("Default camera status (NEWLY ADDED) type not found.");
        }
        camera.setCameraStatusType(connectedCameraStatusType);
        return this.cameraRepository.save(camera).getSystemId();
    }

    public List<Camera> getAllCameras() {
        return (List<Camera>) this.cameraRepository.findAll();
    }

}
