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
public class CamerasController {

    private final CamerasService camerasService;

    @GetMapping("/{cameraId}")
    public Camera getCamera(@PathVariable("cameraId") Integer cameraId) {
        return this.camerasService.getCamera(cameraId);
    }

    @DeleteMapping("/{cameraId}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteCamera(@PathVariable("cameraId") Integer cameraId) {
        this.camerasService.deleteCamera(cameraId);
    }

    @PostMapping("/addNewCamera")
    public Integer addNewCamera(@RequestBody Camera camera) {
        return this.camerasService.addNewCamera(camera);
    }

    @GetMapping("/allCameras")
    public List<Camera> getAllCameras() {
        return this.camerasService.getAllCameras();
    }


}
