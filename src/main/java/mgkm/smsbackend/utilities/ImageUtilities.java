package mgkm.smsbackend.utilities;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImageUtilities {

    public static String getProductsDisplayImagesUrl(Integer productId) {
        return DirectoryUtilities.rootDataPath + "/productsDisplayImages/" + productId + "/";
    }

    public static String getProductReferenceImagesUrl(Integer productReferenceId) {
        return DirectoryUtilities.rootDataPath + "/productsReferenceImages/" + productReferenceId + "/";
    }

    public static String getShelfImageUrl(Integer shelfImageId) {
        return DirectoryUtilities.rootDataPath + "/shelfImages/" + shelfImageId + "/";
    }

    public static String getImageUrl(String imagePath, Integer objectSystemId, String fileName) {
        return DirectoryUtilities.rootDataPath + "/" + imagePath + "/" + objectSystemId + "/" + fileName;
    }

    public static void saveMultipartFileImage(String imageUrl, MultipartFile imageFile) throws IOException {

        DirectoryUtilities.purgeOrCreateDirectory(imageUrl);

        Files.write(Paths.get(imageUrl + imageFile.getOriginalFilename()), imageFile.getBytes());

    }

    public static BufferedImage getBufferedImage(String imageUrl) throws IOException, ImageReadException {

        File imageFile = new File(imageUrl);

        BufferedImage bufferedImage = ImageIO.read(imageFile);

        ImageMetadata metadata = Imaging.getMetadata(imageFile);

        if (metadata instanceof JpegImageMetadata jpegMetadata) {
            TiffField orientationField = jpegMetadata.findEXIFValueWithExactMatch(TiffTagConstants.TIFF_TAG_ORIENTATION);
            if (orientationField != null) {
                short orientation = (Short) orientationField.getValue();
                bufferedImage = switch (orientation) {
                    case 3 -> rotateImage(bufferedImage, 180);
                    case 6 -> rotateImage(bufferedImage, 90);
                    case 8 -> rotateImage(bufferedImage, 270);
                    default -> bufferedImage;
                };
            }
        }

        return bufferedImage;
    }


    private static BufferedImage rotateImage(BufferedImage originalImage, int degrees) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        AffineTransform affineTransform = new AffineTransform();

        switch (degrees) {
            case 90:
                affineTransform.translate(height, 0);
                affineTransform.rotate(Math.toRadians(90));
                break;
            case 180:
                affineTransform.translate(width, height);
                affineTransform.rotate(Math.toRadians(180));
                break;
            case 270:
                affineTransform.translate(0, width);
                affineTransform.rotate(Math.toRadians(270));
                break;
            default:
                return originalImage;
        }

        BufferedImage newImage = new BufferedImage((degrees == 90 || degrees == 270) ? height : width,
                (degrees == 90 || degrees == 270) ? width : height,
                originalImage.getType());

        Graphics2D graphics = newImage.createGraphics();
        graphics.transform(affineTransform);
        graphics.drawImage(originalImage, 0, 0, null);
        graphics.dispose();

        return newImage;
    }

    public static void extractAndSaveSubImage(BufferedImage bufferedImage, String outputImageUrl,
                                       Integer x1, Integer y1, Integer x2, Integer y2) throws IOException {

        // Cropping the image
        BufferedImage croppedImage = bufferedImage.getSubimage(
                x1, y1, x2 - x1, y2 - y1
        );

        File outputFile = new File(outputImageUrl);

        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            ImageIO.write(croppedImage, "jpg", fos);
        }

    }

    public static void rotateImage(String imageUrl) throws IOException, ImageReadException {
        File imageFile = new File(imageUrl);
        BufferedImage bufferedImage = ImageUtilities.getBufferedImage(imageUrl);
        ImageIO.write(bufferedImage, "jpg", imageFile);
    }


}
