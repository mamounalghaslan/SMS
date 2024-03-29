package mgkm.smsbackend.services;

import mgkm.smsbackend.models.Camera;
import mgkm.smsbackend.models.ProductReference;
import mgkm.smsbackend.models.ShelfImage;
import mgkm.smsbackend.repositories.ProductReferenceRepository;
import mgkm.smsbackend.repositories.ShelfImageRepository;
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
public class ShelfImageService extends ImageBase64Service {

    @Value("${sms-root-images-path}")
    private String rootImagesPath;

    private final ShelfImageRepository shelfImageRepository;

    private final ProductReferenceRepository productReferenceRepository;

    public ShelfImageService(ShelfImageRepository shelfImageRepository,
                             ProductReferenceRepository productReferenceRepository) {
        this.shelfImageRepository = shelfImageRepository;
        this.productReferenceRepository = productReferenceRepository;
    }

    private String getShelfImagesUrl(Integer shelfImageId) {
        return this.rootImagesPath + "/shelfImages/" + shelfImageId + "/";
    }

    public ShelfImage getShelfImage(Integer shelfImageId) throws IOException {
        ShelfImage shelfImage = this.shelfImageRepository.findById(shelfImageId).orElse(null);
        if(shelfImage != null) {
            shelfImage.setImageFileBase64(loadImageAsBase64(shelfImage.getImagePath()));
        }
        return shelfImage;
    }

    public List<ShelfImage> getAllShelfImages() {
        return (List<ShelfImage>) this.shelfImageRepository.findAll();
    }

    public ShelfImage getShelfImageByCamera(Camera camera) throws IOException {
        ShelfImage shelfImage = this.shelfImageRepository.findByReferencedCamera(camera);
        if(shelfImage != null) {
            shelfImage.setImageFileBase64(loadImageAsBase64(shelfImage.getImagePath()));
        }
        return shelfImage;
    }

    public ShelfImage addNewShelfImage(ShelfImage shelfImage) {
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

        String shelfImageUri = shelfImageUrl + shelfImageFile.getOriginalFilename();

        Files.write(Paths.get(shelfImageUri), shelfImageFile.getBytes());

        shelfImage.setImagePath(shelfImageUri);

        this.shelfImageRepository.save(shelfImage);

    }

    public void deleteShelfImage(ShelfImage shelfImage) {

        if(shelfImage != null) {

            // TODO: delete the product references

            this.shelfImageRepository.delete(shelfImage);
        }
    }

    public List<ProductReference> getProductReferencesByShelfImageId(Integer shelfImageId) {
        return (List<ProductReference>) this.productReferenceRepository.findAllByShelfImage_SystemId(shelfImageId);
    }

}
