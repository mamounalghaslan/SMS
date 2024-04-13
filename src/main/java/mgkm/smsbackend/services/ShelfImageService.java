package mgkm.smsbackend.services;

import mgkm.smsbackend.models.Camera;
import mgkm.smsbackend.models.ProductReference;
import mgkm.smsbackend.models.ProductReferenceParameters;
import mgkm.smsbackend.models.ShelfImage;
import mgkm.smsbackend.repositories.ProductReferenceRepository;
import mgkm.smsbackend.repositories.ShelfImageRepository;
import mgkm.smsbackend.utilities.ImageLocator;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

@Service
public class ShelfImageService {

    private final ShelfImageRepository shelfImageRepository;

    private final ProductReferenceRepository productReferenceRepository;

    @Autowired
    private ModelService modelService;

    public ShelfImageService(ShelfImageRepository shelfImageRepository,
                             ProductReferenceRepository productReferenceRepository) {
        this.shelfImageRepository = shelfImageRepository;
        this.productReferenceRepository = productReferenceRepository;
    }

    public ShelfImage getShelfImage(Integer shelfImageId) {
        return this.shelfImageRepository.findById(shelfImageId).orElse(null);
    }

    public List<ShelfImage> getAllShelfImages() {

        List<ShelfImage> shelfImages = (List<ShelfImage>) this.shelfImageRepository.findAll();

        shelfImages.sort(Comparator.comparing(ShelfImage::getCaptureDate).reversed());

        return shelfImages;
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

        try {

            // 1. Save the shelf image
            String shelfImageUrl = ImageLocator.getShelfImagesUrl(shelfImage.getSystemId());

            Path path = Paths.get(shelfImageUrl);

            if(!Files.exists(path)) {
                Files.createDirectories(path);
            } else {
                FileUtils.cleanDirectory(path.toFile());
            }

            Files.write(Paths.get(shelfImageUrl + shelfImageFile.getOriginalFilename()), shelfImageFile.getBytes());

            shelfImage.setImageFileName(shelfImageFile.getOriginalFilename());

            this.shelfImageRepository.save(shelfImage);

            // 2. Run the detection model
            List<ProductReference> productReferences =
                    modelService.detectProducts(
                            shelfImageUrl + shelfImageFile.getOriginalFilename());

            for(ProductReference productReference : productReferences) {
                productReference.setShelfImage(shelfImage);
            }

            this.productReferenceRepository.saveAll(productReferences);

        } catch (Exception e) {

            this.shelfImageRepository.delete(shelfImage);

            throw e;
        }
    }

//    public void deleteShelfImage(ShelfImage shelfImage) {
//        if(shelfImage != null) {
//            this.shelfImageRepository.delete(shelfImage);
//        }
//    }

    public List<ProductReference> getProductReferencesByShelfImageId(Integer shelfImageId) {
        List<ProductReference> productReferences =
                (List<ProductReference>) this.productReferenceRepository.findAllByShelfImage_SystemId(shelfImageId);

        // bin the product references into 5 bins based on their y-coordinate
        // for each bin, sort the product references based on their x-coordinate
        productReferences.sort(Comparator.comparing(ProductReference::getY1));
        int binSize = productReferences.size() / 5;
        for(int i = 0; i < 5; i++) {
            List<ProductReference> bin = productReferences.subList(i * binSize, (i + 1) * binSize);
            bin.sort(Comparator.comparing(ProductReference::getX1));
        }

        return productReferences;
    }

    public void processProductReferences(ProductReferenceParameters parameters) {

        this.productReferenceRepository.saveAll(parameters.getInserts());

        this.productReferenceRepository.saveAll(parameters.getUpdates());

        this.productReferenceRepository.deleteAll(parameters.getDeletes());

    }

}
