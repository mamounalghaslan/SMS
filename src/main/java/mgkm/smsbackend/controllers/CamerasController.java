package mgkm.smsbackend.controllers;

import lombok.AllArgsConstructor;
import mgkm.smsbackend.models.Camera;
import mgkm.smsbackend.models.CameraStatusType;
import mgkm.smsbackend.services.CamerasService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/cameras")
@AllArgsConstructor
public class CamerasController extends BaseController {

    private final CamerasService camerasService;

    @GetMapping("/{cameraId}")
    public Camera getCamera(@PathVariable("cameraId") Integer cameraId) {
        return this.camerasService.getCamera(cameraId);
    }

    @DeleteMapping("/{cameraId}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteCamera(@PathVariable("cameraId") Integer cameraId) throws IOException {
        this.camerasService.deleteCamera(cameraId);
    }

    @PostMapping("/addNewCamera")
    public void addNewCamera(@RequestBody Camera camera) {
        this.camerasService.addNewCamera(camera);
    }

    @GetMapping("/allCameras")
    public List<Camera> getAllCameras() throws IOException {
        return this.camerasService.getAllCameras();
    }

    @PostMapping("/updateCameraReferenceImage/{cameraId}")
    public void updateCameraReferenceImage(@PathVariable("cameraId") Integer cameraId,
                                           @RequestParam(required = false, value = "imageFile") MultipartFile imageFile)
            throws IOException {
        this.camerasService.updateCameraReferenceImage(cameraId, imageFile);
    }

    @GetMapping("/allCameraStatusTypes")
    public List<CameraStatusType> getAllCameraStatusTypes() {
        return this.camerasService.getAllCameraStatusTypes();
    }


}
