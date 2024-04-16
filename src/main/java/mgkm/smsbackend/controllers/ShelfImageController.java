package mgkm.smsbackend.controllers;

import lombok.AllArgsConstructor;
import mgkm.smsbackend.models.ProductReference;
import mgkm.smsbackend.models.ProductReferenceParameters;
import mgkm.smsbackend.models.ShelfImage;
import mgkm.smsbackend.services.ShelfImageService;
import org.apache.commons.imaging.ImageReadException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/shelf-images")
@AllArgsConstructor
public class ShelfImageController extends BaseController {

    private final ShelfImageService shelfImageService;

    @GetMapping("/allShelfImages")
    public List<ShelfImage> getAllShelfImages() {
        return this.shelfImageService.getAllShelfImages();
    }

    @GetMapping("/productReferences/{shelfImageId}")
    public List<ProductReference> getProductReferencesByShelfImageId(@PathVariable Integer shelfImageId) {
        return this.shelfImageService.getProductReferencesByShelfImageId(shelfImageId);
    }

    @GetMapping("/{shelfImageId}")
    public ShelfImage getShelfImage(@PathVariable Integer shelfImageId) {
        return this.shelfImageService.getShelfImage(shelfImageId);
    }

    @PostMapping("/updateProductReferences/{shelfImageId}")
    @ResponseStatus(code = HttpStatus.OK)
    public void updateProductReferences(@RequestBody ProductReferenceParameters parameters,
                                        @PathVariable Integer shelfImageId) throws IOException, ImageReadException {
        this.shelfImageService.processProductReferences(shelfImageId, parameters);
    }

    @DeleteMapping("/{shelfImageId}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteShelfImage(@PathVariable Integer shelfImageId) throws IOException {
        this.shelfImageService.deleteShelfImage(shelfImageId);
    }

}
