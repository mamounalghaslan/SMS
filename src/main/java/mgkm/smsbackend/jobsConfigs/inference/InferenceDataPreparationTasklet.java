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
import org.apache.commons.imaging.ImageReadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Random;

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

            // camera must have a reference image
            ShelfImage referenceShelfImage = shelfImageService.getShelfImageByCamera(camera);

            if (referenceShelfImage == null) {
                continue;
            }

            // for each camera, create a directory in the inference data directory
            String cameraDirectory = DirectoryUtilities.getInferenceDataPath() + "/camera_" + camera.getSystemId();

            try {

                DirectoryUtilities.purgeOrCreateDirectory(cameraDirectory);

                DirectoryUtilities.purgeOrCreateDirectory(cameraDirectory + "/images");

                // This is the part where we should capture an image from the cameras
                // and save it in the camera directory

                String sampleImagePath = "";

                if(camera.getSystemId() == 1) {
                    List<String> camera1samples = DirectoryUtilities.readFileNamesInDirectory(
                            DirectoryUtilities.getCamera1SamplesPath());
                    // randomly select a sample image from camera1samples
                    Random random = new Random();
                    int index = random.nextInt(camera1samples.size());
                    sampleImagePath = DirectoryUtilities.getCamera1SamplesPath() + "/" + camera1samples.get(index);
                } else if(camera.getSystemId() == 2) {
                    List<String> camera2samples = DirectoryUtilities.readFileNamesInDirectory(
                            DirectoryUtilities.getCamera2SamplesPath());
                    // randomly select a sample image from camera2samples
                    Random random = new Random();
                    int index = random.nextInt(camera2samples.size());
                    sampleImagePath = DirectoryUtilities.getCamera2SamplesPath() + "/" + camera2samples.get(index);
                }

                DirectoryUtilities.copyFileToDirectory(
                        sampleImagePath, cameraDirectory + "/images/capture.jpg");

                ImageUtilities.rotateImage(cameraDirectory + "/images/capture.jpg");

            } catch (IOException e) {
                log.error("Failed to create camera directory: {}", cameraDirectory);
                throw new RuntimeException(e);
            } catch (ImageReadException e) {
                log.error("Failed to rotate image file: {}", cameraDirectory + "/images/capture.jpg");
                throw new RuntimeException(e);
            }

            try {

                String shelfImageFile = ImageUtilities.getShelfImageUrl(referenceShelfImage.getSystemId())
                        + referenceShelfImage.getImageFileName();

                DirectoryUtilities.copyFileToDirectory(
                        shelfImageFile,
                        cameraDirectory + "/reference.jpg");

            } catch (IOException e) {
                log.error("Failed to copy reference shelf image {} to camera directory: {}",
                        referenceShelfImage, cameraDirectory + "/reference.jpg");
                throw new RuntimeException(e);
            }

            List<ProductReference> productReferences =
                    this.shelfImageService.getProductReferencesByShelfImageId(referenceShelfImage.getSystemId());

            try {

                log.info("Writing metadata.json to camera directory: {}", cameraDirectory + "/metadata.json");

                // This is the actual method call
                String metadataJson = this.shelfImageService.generateProductReferencesMetadata(productReferences);

                DirectoryUtilities.writeStringToFile(metadataJson, cameraDirectory + "/metadata.json");

            } catch (IOException | URISyntaxException e) {
                log.error("Failed to write metadata.json to camera directory: {}", cameraDirectory);
                throw new RuntimeException(e);
            }

        }

        return RepeatStatus.FINISHED;
    }

}
