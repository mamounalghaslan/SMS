package mgkm.smsbackend.services;

import mgkm.smsbackend.models.Camera;
import mgkm.smsbackend.models.CameraStatusType;
import mgkm.smsbackend.repositories.CameraRepository;
import mgkm.smsbackend.repositories.CameraStatusTypeRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class CamerasService extends ImageBase64Service {

    @Value("${sms-root-images-path}")
    private String rootImagesPath;

    private final CameraRepository cameraRepository;

    private final CameraStatusTypeRepository cameraStatusTypeRepository;

    public CamerasService(CameraRepository cameraRepository, CameraStatusTypeRepository cameraStatusTypeRepository) {
        this.cameraRepository = cameraRepository;
        this.cameraStatusTypeRepository = cameraStatusTypeRepository;
    }

    private String getReferenceImageUrl(Integer cameraId) {
        return this.rootImagesPath + "/cameraReferenceImages/" + cameraId + "/";
    }

    public List<Camera> getAllCameras() {
        return (List<Camera>) this.cameraRepository.findAll();
    }

    public List<CameraStatusType> getAllCameraStatusTypes() {
        return (List<CameraStatusType>) this.cameraStatusTypeRepository.findAll();
    }

    public void addNewCamera(Camera camera) {
        CameraStatusType cameraStatusType = this.cameraStatusTypeRepository.findById(1).orElse(null);
        assert cameraStatusType != null;
        camera.setCameraStatusType(cameraStatusType);
        this.cameraRepository.save(camera);
    }

    public Camera getCamera(Integer cameraId) {
        return this.cameraRepository.findById(cameraId).orElse(null);
    }

    public void updateCamera(Camera camera) {
        this.cameraRepository.save(camera);
    }

    public void deleteCamera(Integer cameraId) throws IOException {

        String referenceImageUrl = this.getReferenceImageUrl(cameraId);

        Path path = Paths.get(referenceImageUrl);
        if(Files.exists(path)) {
            FileUtils.cleanDirectory(path.toFile());
            Files.delete(path);
        }

        this.cameraRepository.deleteById(cameraId);
    }

}
