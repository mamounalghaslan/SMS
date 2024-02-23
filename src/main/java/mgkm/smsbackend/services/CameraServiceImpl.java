package mgkm.smsbackend.services;

import mgkm.smsbackend.models.Camera;
import mgkm.smsbackend.models.CameraReferenceImage;
import mgkm.smsbackend.models.CameraStatusType;
import mgkm.smsbackend.repositories.CameraReferenceImageRepository;
import mgkm.smsbackend.repositories.CameraRepository;
import mgkm.smsbackend.repositories.CameraStatusTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CameraServiceImpl implements CamerasService {

    private final CameraRepository cameraRepository;
    private final CameraReferenceImageRepository cameraReferenceImageRepository;
    private final CameraStatusTypeRepository cameraStatusTypeRepository;

    public CameraServiceImpl(CameraRepository cameraRepository,
                             CameraReferenceImageRepository cameraReferenceImageRepository,
                             CameraStatusTypeRepository cameraStatusTypeRepository) {
        this.cameraRepository = cameraRepository;
        this.cameraReferenceImageRepository = cameraReferenceImageRepository;
        this.cameraStatusTypeRepository = cameraStatusTypeRepository;
    }

    @Override
    public Integer addNewCamera(Camera camera) {
        return this.cameraRepository.save(camera).getSystemId();
    }

    @Override
    public void deleteCamera(Integer cameraId) {
        this.cameraRepository.deleteById(cameraId);
    }

    @Override
    public Camera getCamera(Integer cameraId) {
        return this.cameraRepository.findById(cameraId).orElse(null);
    }

    @Override
    public List<Camera> getAllCameras() {
        return (List<Camera>) this.cameraRepository.findAll();
    }

    @Override
    public List<CameraStatusType> getAllCameraStatusTypes() {
        return (List<CameraStatusType>) this.cameraStatusTypeRepository.findAll();
    }

    @Override
    public CameraReferenceImage getCameraReferenceImage(Integer cameraId) {
        return this.cameraReferenceImageRepository.findById(cameraId).orElse(null);
    }


}
