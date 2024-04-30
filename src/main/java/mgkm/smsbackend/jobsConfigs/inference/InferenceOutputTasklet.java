package mgkm.smsbackend.jobsConfigs.inference;

import jakarta.annotation.Nonnull;
import mgkm.smsbackend.jobsConfigs.listeners.JobListener;
import mgkm.smsbackend.models.MisplacedProductReference;
import mgkm.smsbackend.models.ShelfImage;
import mgkm.smsbackend.models.ShelfImageType;
import mgkm.smsbackend.models.inference.CameraResults;
import mgkm.smsbackend.models.inference.Gap;
import mgkm.smsbackend.models.inference.InferenceOutput;
import mgkm.smsbackend.models.inference.MisplacedProduct;
import mgkm.smsbackend.services.CamerasService;
import mgkm.smsbackend.services.ProductsService;
import mgkm.smsbackend.services.ShelfImageService;
import mgkm.smsbackend.utilities.DirectoryUtilities;
import mgkm.smsbackend.utilities.ImageUtilities;
import mgkm.smsbackend.utilities.JSONReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InferenceOutputTasklet implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(JobListener.class);

    private final CamerasService camerasService;
    private final ShelfImageService shelfImageService;
    private final ProductsService productsService;

    public InferenceOutputTasklet(CamerasService camerasService,
                                  ShelfImageService shelfImageService,
                                  ProductsService productsService) {
        this.camerasService = camerasService;
        this.shelfImageService = shelfImageService;
        this.productsService = productsService;
    }

    @Override
    public RepeatStatus execute(@Nonnull StepContribution contribution,
                                @Nonnull ChunkContext chunkContext) {
        log.info("Inference Output Tasklet");

        // Read the inference json output file
        List<CameraResults> cameraResults = JSONReader.readCameraResultsJSON(
                DirectoryUtilities.getInferenceDataPath() + "/inference.json");

        // create a shelf image for each camera
        for (CameraResults cameraResult : cameraResults) {

            // create a shelf image
            ShelfImage shelfImage = new ShelfImage();

            shelfImage.setShelfImageType(new ShelfImageType(3, "Inference"));
            shelfImage.setCaptureDate(LocalDateTime.now());
            shelfImage.setReferencedCamera(
                    this.camerasService.getCamera(
                            Integer.parseInt(cameraResult.getCamera().substring(7)))
            );

            // save to get the new ID
            shelfImage = this.shelfImageService.addNewShelfImage(shelfImage);

            try {
                DirectoryUtilities.purgeOrCreateDirectory(ImageUtilities.getShelfImageUrl(shelfImage.getSystemId()));
                DirectoryUtilities.copyFileToDirectory(
                        DirectoryUtilities.getInferenceDataPath() + "/" + cameraResult.getCamera() + "/images/capture.jpg",
                        ImageUtilities.getShelfImageUrl(shelfImage.getSystemId()) + "/capture.jpg"
                );
                shelfImage.setImageFileName("capture.jpg");
            } catch (IOException e) {
                log.error("Failed to copy the shelf image file.");
                throw new RuntimeException(e);
            }

            // save the shelf image
            shelfImage = this.shelfImageService.addNewShelfImage(shelfImage);

            List<MisplacedProductReference> misplacedProductReferences = new ArrayList<>();

            for(InferenceOutput inferenceOutput : cameraResult.getResults()) {

                for(MisplacedProduct misplacedInferenceProduct : inferenceOutput.getMisplacedProducts()) {

                    MisplacedProductReference misplacedProductReference = new MisplacedProductReference();

                    misplacedProductReference.setShelfImage(shelfImage);

                    misplacedProductReference.setMisplacedProduct(
                            this.productsService.getProduct(
                                    misplacedInferenceProduct.getPositionProductId())
                    );

                    misplacedProductReference.setDetectedProduct(
                            this.productsService.getProduct(
                                    misplacedInferenceProduct.getDetectedObject().getId())
                    );

                    misplacedProductReference.setX1(
                            misplacedInferenceProduct.getDetectedObject().getBoundingBox().getX1());
                    misplacedProductReference.setY1(
                            misplacedInferenceProduct.getDetectedObject().getBoundingBox().getY1());
                    misplacedProductReference.setX2(
                            misplacedInferenceProduct.getDetectedObject().getBoundingBox().getX2());
                    misplacedProductReference.setY2(
                            misplacedInferenceProduct.getDetectedObject().getBoundingBox().getY2());

                    misplacedProductReferences.add(misplacedProductReference);

                }

                for(Gap gap: inferenceOutput.getGaps()) {

                    MisplacedProductReference gapProductReference = new MisplacedProductReference();

                    gapProductReference.setShelfImage(shelfImage);

                    gapProductReference.setMisplacedProduct(
                            this.productsService.getProduct(
                                    gap.getId())
                    );

                    gapProductReference.setX1(
                            gap.getBoundingBox().getX1());
                    gapProductReference.setY1(
                            gap.getBoundingBox().getY1());
                    gapProductReference.setX2(
                            gap.getBoundingBox().getX2());
                    gapProductReference.setY2(
                            gap.getBoundingBox().getY2());

                    misplacedProductReferences.add(gapProductReference);

                }

            }

            this.shelfImageService.saveAllMisplacedProductReferences(misplacedProductReferences);

        }

        return RepeatStatus.FINISHED;
    }

}
