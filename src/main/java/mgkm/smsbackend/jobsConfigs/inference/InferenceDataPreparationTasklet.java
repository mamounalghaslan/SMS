package mgkm.smsbackend.jobsConfigs.inference;

import jakarta.annotation.Nonnull;
import lombok.Setter;
import mgkm.smsbackend.jobsConfigs.listeners.JobListener;
import mgkm.smsbackend.models.Camera;
import mgkm.smsbackend.models.ProductReference;
import mgkm.smsbackend.models.ShelfImage;
import mgkm.smsbackend.services.CamerasService;
import mgkm.smsbackend.services.ShelfImageService;
import mgkm.smsbackend.utilities.DirectoryUtilities;
import mgkm.smsbackend.utilities.ImageUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Setter
public class InferenceDataPreparationTasklet implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(JobListener.class);

    private final CamerasService camerasService;
    private final ShelfImageService shelfImageService;

    public InferenceDataPreparationTasklet(CamerasService camerasService,
                                           ShelfImageService shelfImageService) {
        this.camerasService = camerasService;
        this.shelfImageService = shelfImageService;
    }

    @Override
    public RepeatStatus execute(@Nonnull StepContribution contribution,
                                @Nonnull ChunkContext chunkContext) {
        log.info("Inference Data Preparation Tasklet");

        // Purge or create the inference data directory
        try {
            log.info("Purging or creating the inference data directory.");
            DirectoryUtilities.purgeOrCreateDirectory(DirectoryUtilities.getInferenceDataPath());
        } catch (IOException e) {
            log.error("Failed to purge or create the inference data directory.");
            throw new RuntimeException(e);
        }

        // Get all cameras
        List<Camera> cameras = camerasService.getAllCameras();

        for (Camera camera : cameras) {

            // for each camera, create a directory in the inference data directory
            String cameraDirectory = DirectoryUtilities.getInferenceDataPath() + "/camera_" + camera.getSystemId();

            try {
                DirectoryUtilities.purgeOrCreateDirectory(cameraDirectory);
            } catch (IOException e) {
                log.error("Failed to create camera directory: {}", cameraDirectory);
                throw new RuntimeException(e);
            }

            // for each camera, get the reference shelf image and save it as reference.jpg in the camera directory
            ShelfImage referenceShelfImage = shelfImageService.getShelfImageByCamera(camera);

            try {
                DirectoryUtilities.copyFileToDirectory(
                        ImageUtilities.getShelfImageUrl(referenceShelfImage.getSystemId())
                                + referenceShelfImage.getImageFileName(),
                        cameraDirectory + "/reference.jpg");
            } catch (IOException e) {
                log.error("Failed to copy reference shelf image {} to camera directory: {}",
                        referenceShelfImage, cameraDirectory + "/reference.jpg");
                throw new RuntimeException(e);
            }

            // for each camera, get the reference shelf image and get its product references
            List<ProductReference> productReferences =
                    this.shelfImageService.getProductReferencesByShelfImageId(referenceShelfImage.getSystemId());

            // put all product references in metadata.json in the camera directory
            String metadataJson = this.shelfImageService.generateProductReferencesMetadata(productReferences);

            try {
                log.info("Writing metadata.json to camera directory: {}", cameraDirectory + "/metadata.json");
                DirectoryUtilities.writeStringToFile(metadataJson, cameraDirectory + "/metadata.json");
            } catch (IOException | URISyntaxException e) {
                log.error("Failed to write metadata.json to camera directory: {}", cameraDirectory);
                throw new RuntimeException(e);
            }

        }

        return RepeatStatus.FINISHED;
    }

}