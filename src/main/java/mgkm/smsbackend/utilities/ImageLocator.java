package mgkm.smsbackend.utilities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ImageLocator {

    private static String rootImagesPath;

    @Value("${sms-root-images-path}")
    public void setRootImagesPath(String rootImagesPath) {
        ImageLocator.rootImagesPath = rootImagesPath;
    }

    public static String getProductsDisplayImagesUrl(Integer productId) {
        return rootImagesPath + "/productsDisplayImages/" + productId + "/";
    }

    public static String getProductsImagesUrl(Integer productId) {
        return rootImagesPath + "/productsImages/" + productId + "/";
    }

    public static String getShelfImagesUrl(Integer shelfImageId) {
        return rootImagesPath + "/shelfImages/" + shelfImageId + "/";
    }

    public static String getImage(String imagePath, Integer objectSystemId, String fileName) {
        return rootImagesPath + "/" + imagePath + "/" + objectSystemId + "/" + fileName;
    }

    public static String getImageResourceType(String imagePath, Integer objectSystemId, String fileName) {
        return rootImagesPath + "/" + imagePath + "/" + objectSystemId + "/" + fileName;
    }

}
