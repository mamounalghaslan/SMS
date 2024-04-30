package mgkm.smsbackend.controllers;

import lombok.AllArgsConstructor;
import mgkm.smsbackend.models.Camera;
import mgkm.smsbackend.models.ShelfImage;
import mgkm.smsbackend.services.CamerasService;
import mgkm.smsbackend.services.ShelfImageService;
import org.apache.commons.imaging.ImageReadException;
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

    private final ShelfImageService shelfImageService;

    @GetMapping("/allCameras")
    public List<Camera> getAllCameras() {
        return this.camerasService.getAllCameras();
    }

    @GetMapping("/{cameraId}")
    public Camera getCamera(@PathVariable("cameraId") Integer cameraId) {
        return this.camerasService.getCamera(cameraId);
    }

    @PostMapping("/addNewCamera")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void addNewCamera(@RequestBody Camera camera) {
        this.camerasService.addNewCamera(camera);
    }

    @DeleteMapping("/deleteCamera")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteCamera(@RequestBody Camera camera) {
        this.camerasService.deleteCamera(camera);
    }

    @PostMapping("/addNewShelfImage")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ShelfImage addNewShelfImage(@RequestBody ShelfImage shelfImage) {
        return this.shelfImageService.addNewShelfImage(shelfImage);
    }

    @PostMapping("/addShelfImageFile/{shelfImageId}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void addShelfImageFile(@PathVariable("shelfImageId") Integer shelfImageId,
                                  @RequestParam(value = "imageFile") MultipartFile shelfImageFile)
            throws IOException, ImageReadException {
        this.shelfImageService.addShelfImageFile(
                this.shelfImageService.getShelfImage(shelfImageId), shelfImageFile);
    }

    @GetMapping("/getCameraReferenceImage/{cameraId}")
    public ShelfImage getCameraReferenceImage(@PathVariable Integer cameraId) {
        return this.shelfImageService.getShelfImageByCamera(this.camerasService.getCamera(cameraId));
    }

    @PostMapping("/initialize")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void initialize() {
        this.camerasService.initialize();
    }


}
