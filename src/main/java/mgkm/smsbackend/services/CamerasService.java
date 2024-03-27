package mgkm.smsbackend.services;

import mgkm.smsbackend.models.Camera;
import mgkm.smsbackend.models.CameraStatusType;
import mgkm.smsbackend.models.ShelfImage;
import mgkm.smsbackend.repositories.CameraRepository;
import mgkm.smsbackend.repositories.CameraStatusTypeRepository;
import mgkm.smsbackend.repositories.ShelfImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CamerasService {

    private final CameraRepository cameraRepository;

    private final CameraStatusTypeRepository cameraStatusTypeRepository;

    private final ShelfImageRepository shelfImageRepository;

    public CamerasService(CameraRepository cameraRepository,
                          CameraStatusTypeRepository cameraStatusTypeRepository,
                          ShelfImageRepository shelfImageRepository) {
        this.cameraRepository = cameraRepository;
        this.cameraStatusTypeRepository = cameraStatusTypeRepository;
        this.shelfImageRepository = shelfImageRepository;
    }

    public List<Camera> getAllCameras() {
        return (List<Camera>) this.cameraRepository.findAll();
    }

    public void addNewCamera(Camera camera) {
        CameraStatusType cameraStatusType = this.cameraStatusTypeRepository.findById(2).orElse(null);
        assert cameraStatusType != null;
        camera.setCameraStatusType(cameraStatusType);
        this.cameraRepository.save(camera);
    }

    public Camera getCamera(Integer cameraId) {
        return this.cameraRepository.findById(cameraId).orElse(null);
    }

    public void deleteCamera(Camera camera) {

        ShelfImage referenceShelfImage = this.shelfImageRepository.findByReferencedCamera(camera);

        if (referenceShelfImage != null) {
            referenceShelfImage.setReferencedCamera(null);
            this.shelfImageRepository.save(referenceShelfImage);
        }

        this.cameraRepository.delete(camera);

    }

}
