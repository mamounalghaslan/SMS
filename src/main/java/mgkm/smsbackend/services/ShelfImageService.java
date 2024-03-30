package mgkm.smsbackend.services;

import mgkm.smsbackend.models.Camera;
import mgkm.smsbackend.models.ProductReference;
import mgkm.smsbackend.models.ShelfImage;
import mgkm.smsbackend.repositories.ProductReferenceRepository;
import mgkm.smsbackend.repositories.ShelfImageRepository;
import mgkm.smsbackend.utilities.ProductReferencesBoxesJSONReader;
import mgkm.smsbackend.utilities.PythonCaller;
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
public class ShelfImageService {

    @Value("${sms-root-images-path}")
    private String rootImagesPath;

    private final ShelfImageRepository shelfImageRepository;

    private final ProductReferenceRepository productReferenceRepository;

    public ShelfImageService(ShelfImageRepository shelfImageRepository,
                             ProductReferenceRepository productReferenceRepository) {
        this.shelfImageRepository = shelfImageRepository;
        this.productReferenceRepository = productReferenceRepository;
    }

    public String getShelfImagesUrl(Integer shelfImageId) {
        return this.rootImagesPath + "/shelfImages/" + shelfImageId + "/";
    }

    public ShelfImage getShelfImage(Integer shelfImageId) {
        return this.shelfImageRepository.findById(shelfImageId).orElse(null);
    }

    public List<ShelfImage> getAllShelfImages() {
        return (List<ShelfImage>) this.shelfImageRepository.findAll();
    }

    public ShelfImage getShelfImageByCamera(Camera camera) {
        return this.shelfImageRepository.findByReferencedCamera_SystemId(camera.getSystemId());
    }

    public ShelfImage addNewShelfImage(ShelfImage shelfImage) {

        ShelfImage previousShelfImage = null;
        if (shelfImage.getReferencedCamera() != null) {
            previousShelfImage = this.shelfImageRepository.findByReferencedCamera_SystemId(
                    shelfImage.getReferencedCamera().getSystemId());
        }

        if(previousShelfImage != null) {
            previousShelfImage.setReferencedCamera(null);
            this.shelfImageRepository.save(previousShelfImage);
        }

        return this.shelfImageRepository.save(shelfImage);
    }

    public void addShelfImageFile(ShelfImage shelfImage, MultipartFile shelfImageFile) throws IOException {

        String shelfImageUrl = this.getShelfImagesUrl(shelfImage.getSystemId());

        Path path = Paths.get(shelfImageUrl);

        if(!Files.exists(path)) {
            Files.createDirectories(path);
        } else {
            FileUtils.cleanDirectory(path.toFile());
        }

        Files.write(Paths.get(shelfImageUrl + shelfImageFile.getOriginalFilename()), shelfImageFile.getBytes());

        shelfImage.setImageFileName(shelfImageFile.getOriginalFilename());

        this.shelfImageRepository.save(shelfImage);

        // ----------------------------------------------

        PythonCaller.callPython(
                "F:/SMS/sms-backend/src/main/python/run-predict.py",
                "F:/SMS/sms-backend/src/main/python/yolov9c.pt",
                shelfImageUrl + shelfImageFile.getOriginalFilename(),
                "F:/SMS/sms-backend/src/main/python/results.json"
        );

        List<ProductReference> productReferences = ProductReferencesBoxesJSONReader.readProductReferencesBoxesJSON(
                "F:/SMS/sms-backend/src/main/python/results.json"
        );

        for(ProductReference productReference : productReferences) {
            productReference.setShelfImage(shelfImage);
            this.productReferenceRepository.save(productReference);
        }

    }

    public void deleteShelfImage(ShelfImage shelfImage) {
        if(shelfImage != null) {
            this.shelfImageRepository.delete(shelfImage);
        }
    }

    public List<ProductReference> getProductReferencesByShelfImageId(Integer shelfImageId) {
        return (List<ProductReference>) this.productReferenceRepository.findAllByShelfImage_SystemId(shelfImageId);
    }

}
