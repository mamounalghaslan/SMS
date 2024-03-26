package mgkm.smsbackend.controllers;

import lombok.AllArgsConstructor;
import mgkm.smsbackend.models.Camera;
import mgkm.smsbackend.services.CamerasService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cameras")
@AllArgsConstructor
public class CamerasController extends BaseController {

    private final CamerasService camerasService;

    @GetMapping("/allCameras")
    public List<Camera> getAllCameras() {
        return this.camerasService.getAllCameras();
    }

    @GetMapping("/{cameraId}")
    public Camera getCamera(@PathVariable("cameraId") Integer cameraId) {
        return this.camerasService.getCamera(cameraId);
    }

    @PostMapping("/addNewCamera")
    public void addNewCamera(@RequestBody Camera camera) {
        this.camerasService.addNewCamera(camera);
    }

    @DeleteMapping("/{cameraId}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteCamera(@PathVariable Integer cameraId) {
        this.camerasService.deleteCamera(cameraId);
    }

}
