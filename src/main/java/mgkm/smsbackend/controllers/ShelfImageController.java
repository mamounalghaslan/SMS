package mgkm.smsbackend.controllers;

import lombok.AllArgsConstructor;
import mgkm.smsbackend.models.ShelfImage;
import mgkm.smsbackend.services.ShelfImageService;
import org.springframework.web.bind.annotation.*;

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

}
