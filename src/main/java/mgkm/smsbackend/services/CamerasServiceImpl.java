package mgkm.smsbackend.services;

import mgkm.smsbackend.models.Camera;
import mgkm.smsbackend.repositories.CameraRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CamerasServiceImpl implements CamerasService {

    private final CameraRepository cameraRepository;

    public CamerasServiceImpl(CameraRepository cameraRepository) {
        this.cameraRepository = cameraRepository;
    }

    @Override
    public Camera getCamera(Integer cameraId) {
        return this.cameraRepository.findById(cameraId).orElse(null);
    }

    @Override
    public void deleteCamera(Integer cameraId) {
        this.cameraRepository.deleteById(cameraId);
    }

    @Override
    public Integer addNewCamera(Camera camera) {
        return this.cameraRepository.save(camera).getSystemId();
    }

    @Override
    public List<Camera> getAllCameras() {
        return (List<Camera>) this.cameraRepository.findAll();
    }


}
