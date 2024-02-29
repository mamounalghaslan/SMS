package mgkm.smsbackend.services;

import mgkm.smsbackend.models.Camera;
import mgkm.smsbackend.models.CameraStatusType;
import mgkm.smsbackend.repositories.CameraRepository;
import mgkm.smsbackend.repositories.CameraStatusTypeRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

@Service
public class CamerasService {

    @Value("${sms-root-images-path}")
    private String rootImagesPath;

    private final CameraRepository cameraRepository;

    private final CameraStatusTypeRepository cameraStatusTypeRepository;

    public CamerasService(CameraRepository cameraRepository, CameraStatusTypeRepository cameraStatusTypeRepository) {
        this.cameraRepository = cameraRepository;
        this.cameraStatusTypeRepository = cameraStatusTypeRepository;
    }

    public Camera getCamera(Integer cameraId) {
        return this.cameraRepository.findById(cameraId).orElse(null);
    }

    public void deleteCamera(Integer cameraId) throws IOException {

        String referenceImageUrl = this.rootImagesPath + "/cameraReferenceImages/" + cameraId + "/";

        Path path = Paths.get(referenceImageUrl);
        if(Files.exists(path)) {
            FileUtils.cleanDirectory(path.toFile());
            Files.delete(path);
        }

        this.cameraRepository.deleteById(cameraId);
    }

    public void addNewCamera(Camera camera) {
        this.cameraRepository.save(camera);
    }

    public List<Camera> getAllCameras() throws IOException {
        List<Camera> cameras = (List<Camera>) this.cameraRepository.findAll();

        for (Camera camera : cameras) {
            camera.setReferenceImageFileBase64(loadImageAsBase64(camera.getReferenceImagePath()));
        }

        return cameras;
    }

    private String loadImageAsBase64(String imagePath) throws IOException {

        if (imagePath != null) {
            File file = new File(imagePath);

            byte[] fileContent = Files.readAllBytes(file.toPath());
            String encodedString = Base64.getEncoder().encodeToString(fileContent);

            //get the file extension
            String extension = imagePath.substring(imagePath.lastIndexOf(".") + 1);

            return "data:image/" + extension + ";base64," + encodedString;
        }

        return "";
    }

    public List<CameraStatusType> getAllCameraStatusTypes() {
        return (List<CameraStatusType>) this.cameraStatusTypeRepository.findAll();
    }

    public void updateCameraReferenceImage(Integer cameraId, MultipartFile imageFile) throws IOException {

        String referenceImageUrl = this.rootImagesPath + "/cameraReferenceImages/" + cameraId + "/";
        String referenceImageUri = referenceImageUrl + imageFile.getOriginalFilename();

        Path path = Paths.get(referenceImageUrl);

        if (!Files.exists(path)) {
            Files.createDirectories(path);
        } else {
            FileUtils.cleanDirectory(path.toFile());
        }

        Files.write(Paths.get(referenceImageUri), imageFile.getBytes());

        Camera camera = this.cameraRepository.findById(cameraId).orElse(null);
        if (camera != null) {
            camera.setReferenceImagePath(referenceImageUri);
            camera.setReferenceImageCaptureDate(java.time.LocalDate.now());
            this.cameraRepository.save(camera);
        }

    }

}
