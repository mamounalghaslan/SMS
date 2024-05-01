package mgkm.smsbackend.services;

import mgkm.smsbackend.utilities.ImageUtilities;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class LocalImageService {

    public Resource getImage(String imagePath,
                             Integer objectSystemId,
                             String fileName) throws MalformedURLException {
        Path path = Paths.get(ImageUtilities.getImageUrl(imagePath, objectSystemId, fileName));
        return new UrlResource(path.toUri());
    }

    public String getImageResourceType(String imagePath,
                                       Integer objectSystemId,
                                       String fileName) throws IOException {
        Path path = Paths.get(ImageUtilities.getImageUrl(imagePath, objectSystemId, fileName));
        return Files.probeContentType(path);
    }


}
