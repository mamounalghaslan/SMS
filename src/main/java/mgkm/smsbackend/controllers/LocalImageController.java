package mgkm.smsbackend.controllers;

import lombok.AllArgsConstructor;
import mgkm.smsbackend.services.LocalImageService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/images")
@AllArgsConstructor
public class LocalImageController extends BaseController {

    private final LocalImageService imageService;

    @GetMapping("/{imagePath}/{objectSystemId}/{fileName}")
    @ResponseBody
    public ResponseEntity<Resource> getImage(@PathVariable String imagePath,
                                             @PathVariable Integer objectSystemId,
                                             @PathVariable String fileName) {
        try {
            Resource resource = this.imageService.getImage(imagePath, objectSystemId, fileName);
            String contentType = this.imageService.getImageResourceType(imagePath, objectSystemId, fileName);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header("Content-Disposition", "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
