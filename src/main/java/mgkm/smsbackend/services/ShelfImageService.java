package mgkm.smsbackend.services;

import mgkm.smsbackend.models.Camera;
import mgkm.smsbackend.models.ProductReference;
import mgkm.smsbackend.models.ProductReferenceParameters;
import mgkm.smsbackend.models.ShelfImage;
import mgkm.smsbackend.repositories.ProductReferenceRepository;
import mgkm.smsbackend.repositories.ShelfImageRepository;
import mgkm.smsbackend.utilities.ImageUtilities;
import org.apache.commons.imaging.ImageReadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Comparator;
import java.util.List;

@Service
public class ShelfImageService {

    private final ShelfImageRepository shelfImageRepository;

    private final ProductReferenceRepository productReferenceRepository;

    private final ModelService modelService;

    public ShelfImageService(ShelfImageRepository shelfImageRepository,
                             ProductReferenceRepository productReferenceRepository,
                             ModelService modelService) {
        this.shelfImageRepository = shelfImageRepository;
        this.productReferenceRepository = productReferenceRepository;
        this.modelService = modelService;
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

    public void addShelfImageFile(ShelfImage shelfImage, MultipartFile shelfImageFile) throws IOException, ImageReadException {

        try {

            // 1. Save the shelf image file

            String shelfImageUrl = ImageUtilities.getShelfImagesUrl(shelfImage.getSystemId());

            ImageUtilities.saveMultipartFileImage(shelfImageUrl, shelfImageFile);

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

            productReferences = (List<ProductReference>)
                    this.productReferenceRepository.findAllByShelfImage_SystemId(shelfImage.getSystemId());

            // 3. Extract and save the product reference images

            extractAndSaveProductReferences(
                    ImageUtilities.getBufferedImage(shelfImageUrl + shelfImageFile.getOriginalFilename()),
                    productReferences);

            this.productReferenceRepository.saveAll(productReferences);

        } catch (Exception e) {

            // find a way to revert the reference image resetting
            // happening in addNewShelfImage method

            this.productReferenceRepository.deleteAll(
                    this.productReferenceRepository.findAllByShelfImage_SystemId(shelfImage.getSystemId()));

            this.shelfImageRepository.delete(shelfImage);

            throw e;
        }
    }

    private void extractAndSaveProductReferences(
            BufferedImage bufferedShelfImage, List<ProductReference> productReferences) throws IOException {

        for(ProductReference productReference : productReferences) {

            String productReferenceImageUrl =
                    ImageUtilities.getProductReferenceImagesUrl(productReference.getSystemId());

            ImageUtilities.purgeOrCreateDirectory(productReferenceImageUrl);

            String productReferenceImageFileName = productReference.getSystemId() + ".jpg";

            ImageUtilities.extractAndSaveSubImage(
                    bufferedShelfImage,
                    productReferenceImageUrl + productReferenceImageFileName,
                    productReference.getX1().intValue(),
                    productReference.getY1().intValue(),
                    productReference.getX2().intValue(),
                    productReference.getY2().intValue()
            );

            productReference.setImageFileName(productReferenceImageFileName);

        }

    }

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

    public void processProductReferences(Integer shelfImageId, ProductReferenceParameters parameters) throws IOException, ImageReadException {

        ShelfImage shelfImage = this.shelfImageRepository.findById(shelfImageId).orElse(null);

        // read the image file

        String shelfImageUrl = ImageUtilities.getShelfImagesUrl(shelfImageId);

        if(shelfImage != null) {

            // Inserts ---------------------------------------------------------

            this.productReferenceRepository.saveAll(parameters.getInserts());

            extractAndSaveProductReferences(
                    ImageUtilities.getBufferedImage(shelfImageUrl + shelfImage.getImageFileName()),
                    parameters.getInserts());

            this.productReferenceRepository.saveAll(parameters.getInserts());

            // Updates ---------------------------------------------------------

            this.productReferenceRepository.saveAll(parameters.getUpdates());

            extractAndSaveProductReferences(
                    ImageUtilities.getBufferedImage(shelfImageUrl + shelfImage.getImageFileName()),
                    parameters.getUpdates());

            this.productReferenceRepository.saveAll(parameters.getUpdates());

            // Deletes ---------------------------------------------------------

            for(ProductReference productReference: parameters.getDeletes()) {

                ImageUtilities.purgeDirectory(
                        ImageUtilities.getProductReferenceImagesUrl(productReference.getSystemId())
                );

            }

            this.productReferenceRepository.deleteAll(parameters.getDeletes());

        }

    }

    public void deleteShelfImage(Integer shelfImageId) throws IOException {
        ShelfImage shelfImage = this.shelfImageRepository.findById(shelfImageId).orElse(null);

        if(shelfImage != null) {
            List<ProductReference> productReferences = (List<ProductReference>)
                    this.productReferenceRepository.findAllByShelfImage_SystemId(shelfImageId);

            for(ProductReference productReference : productReferences) {
                ImageUtilities.purgeDirectory(
                        ImageUtilities.getProductReferenceImagesUrl(productReference.getSystemId())
                );
            }

            this.productReferenceRepository.deleteAll(productReferences);

            ImageUtilities.purgeDirectory(ImageUtilities.getShelfImagesUrl(shelfImageId));
            this.shelfImageRepository.delete(shelfImage);
        }
    }

}
