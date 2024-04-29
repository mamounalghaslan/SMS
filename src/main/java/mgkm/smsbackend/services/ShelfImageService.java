package mgkm.smsbackend.services;

import mgkm.smsbackend.models.*;
import mgkm.smsbackend.repositories.MisplacedProductReferenceRepository;
import mgkm.smsbackend.repositories.ProductReferenceRepository;
import mgkm.smsbackend.repositories.ShelfImageRepository;
import mgkm.smsbackend.utilities.DirectoryUtilities;
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
    private final MisplacedProductReferenceRepository misplacedProductReferenceRepository;

    private final ModelService modelService;

    public ShelfImageService(ShelfImageRepository shelfImageRepository,
                             ProductReferenceRepository productReferenceRepository,
                                MisplacedProductReferenceRepository misplacedProductReferenceRepository,
                             ModelService modelService) {
        this.shelfImageRepository = shelfImageRepository;
        this.productReferenceRepository = productReferenceRepository;
        this.misplacedProductReferenceRepository = misplacedProductReferenceRepository;
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

        Iterable<ShelfImage> shelfImages = this.shelfImageRepository.findAllByReferencedCamera_SystemId(camera.getSystemId());
        for(ShelfImage shelfImage : shelfImages) {
            if(shelfImage.getShelfImageType().getSystemId() == 2) {
                return shelfImage;
            }
        }

        return null;

    }

    public ShelfImage addNewShelfImage(ShelfImage shelfImage) {

        ShelfImage previousShelfImage = null;

        if (shelfImage.getReferencedCamera() != null && shelfImage.getShelfImageType().getSystemId() == 2) {
            previousShelfImage = this.getShelfImageByCamera(shelfImage.getReferencedCamera());
        }

        if(previousShelfImage != null) {
            previousShelfImage.setReferencedCamera(null);
            previousShelfImage.setShelfImageType(
                    new ShelfImageType(1, "Training")
            );
            this.shelfImageRepository.save(previousShelfImage);
        }

        return this.shelfImageRepository.save(shelfImage);
    }

    public void addShelfImageFile(ShelfImage shelfImage, MultipartFile shelfImageFile) throws IOException, ImageReadException {

        try {

            // 1. Save the shelf image file

            String shelfImageUrl = ImageUtilities.getShelfImageUrl(shelfImage.getSystemId());

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

            DirectoryUtilities.purgeOrCreateDirectory(productReferenceImageUrl);

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

    public List<MisplacedProductReference> getMisplacedProductReferencesByShelfImageId(Integer shelfImageId) {
        return (List<MisplacedProductReference>)
                this.misplacedProductReferenceRepository.findAllByShelfImage_SystemId(shelfImageId);
    }

    public void processProductReferences(Integer shelfImageId, ProductReferenceParameters parameters) throws IOException, ImageReadException {

        ShelfImage shelfImage = this.shelfImageRepository.findById(shelfImageId).orElse(null);

        // read the image file

        String shelfImageUrl = ImageUtilities.getShelfImageUrl(shelfImageId);

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

                DirectoryUtilities.purgeDirectory(
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
                DirectoryUtilities.purgeDirectory(
                        ImageUtilities.getProductReferenceImagesUrl(productReference.getSystemId())
                );
            }

            this.productReferenceRepository.deleteAll(productReferences);

            DirectoryUtilities.purgeDirectory(ImageUtilities.getShelfImageUrl(shelfImageId));
            this.shelfImageRepository.delete(shelfImage);
        }
    }

    public String generateProductReferencesMetadata(List<ProductReference> productReferences) {

        int productReferencesSize = productReferences.size();

        StringBuilder metadata = new StringBuilder();

        metadata.append("[\n");

        for(int i = 0; i < productReferencesSize; i++) {

            ProductReference productReference = productReferences.get(i);
            if (productReference.getProduct() == null) {
                continue;
            }

            metadata.append("  {\n");
            metadata.append("    \"name\": \"").append(productReferences.get(i).getProduct().getName()).append("\",\n");
            metadata.append("    \"id\": ").append(productReferences.get(i).getProduct().getSystemId()).append(",\n");
            metadata.append("    \"box\": {\n");
            metadata.append("      \"x1\": ").append(productReferences.get(i).getX1()).append(",\n");
            metadata.append("      \"y1\": ").append(productReferences.get(i).getY1()).append(",\n");
            metadata.append("      \"x2\": ").append(productReferences.get(i).getX2()).append(",\n");
            metadata.append("      \"y2\": ").append(productReferences.get(i).getY2()).append("\n");
            metadata.append("    }\n");
            metadata.append("  },\n");

        }
        // remove the last two characters ",\n"
        metadata.delete(metadata.length() - 2, metadata.length());
        metadata.append("\n]");

        return metadata.toString();
    }

    public void saveAllMisplacedProductReferences(List<MisplacedProductReference> misplacedProductReferences) {
        this.misplacedProductReferenceRepository.saveAll(misplacedProductReferences);
    }

}
