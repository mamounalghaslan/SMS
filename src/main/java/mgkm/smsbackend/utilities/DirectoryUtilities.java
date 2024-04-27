package mgkm.smsbackend.utilities;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
public class DirectoryUtilities {

    public static String rootDataPath;

    @Value("${sms-root-data-path}")
    public void setRootImagesPath(String rootImagesPath) {
        DirectoryUtilities.rootDataPath = rootImagesPath;
    }

    public static String getInferenceDataPath() {
        return rootDataPath + "/inference-data";
    }

    public static void purgeOrCreateDirectory(String url) throws IOException {
        Path path = Paths.get(url);
        if(!Files.exists(path)) {
            Files.createDirectories(path);
        } else {
            FileUtils.cleanDirectory(path.toFile());
        }
    }

    public static void purgeDirectory(String url) throws IOException {
        Path path = Paths.get(url);
        if(Files.exists(path)) {
            FileUtils.cleanDirectory(path.toFile());
            Files.delete(path);
        }
    }

    public static void copyFileToDirectory(String sourceFileUrl, String targetFileUrl) throws IOException {
        Path sourcePath = Paths.get(sourceFileUrl);
        Path targetPath = Paths.get(targetFileUrl);
        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void writeStringToFile(String content, String targetFileUrl) throws IOException, URISyntaxException {
        Path targetPath = Paths.get(targetFileUrl);
        Files.writeString(targetPath, content);
    }

}
