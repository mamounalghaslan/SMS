package mgkm.smsbackend.services;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${sms-root-images-path}")
    private String rootImagesPath;

    public Resource getImage(String imagePath,
                             Integer objectSystemId,
                             String fileName) throws MalformedURLException {
        Path path = Paths.get(this.rootImagesPath + "/" + imagePath + "/" + objectSystemId + "/" + fileName);
        return new UrlResource(path.toUri());
    }

    public String getImageResourceType(String imagePath,
                                       Integer objectSystemId,
                                       String fileName) throws IOException {
        Path path = Paths.get(this.rootImagesPath + "/" + imagePath + "/" + objectSystemId + "/" + fileName);
        return Files.probeContentType(path);
    }



}
