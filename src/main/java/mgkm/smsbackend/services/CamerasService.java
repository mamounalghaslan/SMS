package mgkm.smsbackend.services;

import mgkm.smsbackend.models.Camera;
import mgkm.smsbackend.models.CameraStatusType;
import mgkm.smsbackend.models.ShelfImage;
import mgkm.smsbackend.models.ShelfImageType;
import mgkm.smsbackend.repositories.CameraRepository;
import mgkm.smsbackend.repositories.CameraStatusTypeRepository;
import mgkm.smsbackend.repositories.ShelfImageRepository;
import mgkm.smsbackend.repositories.ShelfImageTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CamerasService {

    private final CameraRepository cameraRepository;

    private final CameraStatusTypeRepository cameraStatusTypeRepository;

    private final ShelfImageRepository shelfImageRepository;

    private final ShelfImageTypeRepository shelfImageTypeRepository;

    public CamerasService(CameraRepository cameraRepository,
                          CameraStatusTypeRepository cameraStatusTypeRepository,
                          ShelfImageRepository shelfImageRepository,
                          ShelfImageTypeRepository shelfImageTypeRepository) {
        this.cameraRepository = cameraRepository;
        this.cameraStatusTypeRepository = cameraStatusTypeRepository;
        this.shelfImageRepository = shelfImageRepository;
        this.shelfImageTypeRepository = shelfImageTypeRepository;
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

        Iterable<ShelfImage> referenceShelfImages =
                this.shelfImageRepository.findAllByReferencedCamera_SystemId(camera.getSystemId());

        for (ShelfImage referenceShelfImage : referenceShelfImages) {
            referenceShelfImage.setReferencedCamera(null);
            this.shelfImageRepository.save(referenceShelfImage);
        }

        this.cameraRepository.delete(camera);

    }

    public void initialize() {
        CameraStatusType cameraStatusType1 = new CameraStatusType(1, "Online");
        CameraStatusType cameraStatusType2 = new CameraStatusType(2, "Offline");
        this.cameraStatusTypeRepository.save(cameraStatusType1);
        this.cameraStatusTypeRepository.save(cameraStatusType2);

        Camera camera1 = new Camera(null, cameraStatusType1, "1.2.3.4:123", "Shelf 1A", "admin", "admin123");
        Camera camera2 = new Camera(null, cameraStatusType1, "2.3.4.5:321", "Shelf 2B", "admin", "admin123");
        this.cameraRepository.save(camera1);
        this.cameraRepository.save(camera2);

        ShelfImageType shelfImageType1 = new ShelfImageType(1, "Training");
        ShelfImageType shelfImageType2 = new ShelfImageType(2, "Reference");
        ShelfImageType shelfImageType3 = new ShelfImageType(3, "Inference");
        this.shelfImageTypeRepository.save(shelfImageType1);
        this.shelfImageTypeRepository.save(shelfImageType2);
        this.shelfImageTypeRepository.save(shelfImageType3);
    }

}
