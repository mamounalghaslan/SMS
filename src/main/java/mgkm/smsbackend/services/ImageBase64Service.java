package mgkm.smsbackend.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class ImageBase64Service {

    protected String loadImageAsBase64(String imagePath) throws IOException {

        if (imagePath != null) {
            File file = new File(imagePath);

            byte[] fileContent = Files.readAllBytes(file.toPath());
            String encodedString = Base64.getEncoder().encodeToString(fileContent);

            //get the file extension
            String extension = imagePath.substring(imagePath.lastIndexOf(".") + 1);

            return "data:image/" + extension + ";base64," + encodedString;
        }

        return "";
    }

}
