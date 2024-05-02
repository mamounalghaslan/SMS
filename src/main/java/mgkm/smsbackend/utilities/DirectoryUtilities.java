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
import java.util.List;
import java.util.stream.Stream;

@Component
public class DirectoryUtilities {

    public static String rootDataPath;
    public static String pythonPath;
    public static String deviceType;

    public static String detectionModelPath;
    public static String detectionPredictScriptPath;
    public static String detectionResultsPath;

    public static String recognitionLauncherScriptPath;
    public static String recognitionInferenceScriptPath;
    public static String recognitionInferenceConfigPath;
    public static String recognitionTrainScriptPath;
    public static String recognitionTrainConfigPath;
    public static String recognitionYoloWeightsPath;

    public static String getCamera1SamplesPath() {
        return rootDataPath + "/samples/camera_1";
    }

    public static String getCamera2SamplesPath() {
        return rootDataPath + "/samples/camera_2";
    }

    @Value("${sms-root-data-path}")
    public void setRootDataPath(String rootImagesPath) {
        DirectoryUtilities.rootDataPath = rootImagesPath;
    }

    @Value("${python-executable-path}")
    public void setPythonPath(String pythonPath) {
        DirectoryUtilities.pythonPath = pythonPath;
    }

    @Value("${device-type}")
    public void setDeviceType(String deviceType) {
        DirectoryUtilities.deviceType = deviceType;
    }

    @Value("${detection-model-path}")
    public void setDetectionModelLocation(String detectionModelLocation) {
        DirectoryUtilities.detectionModelPath = detectionModelLocation;
    }

    @Value("${detection-predict-script-path}")
    public void setDetectionPredictScriptLocation(String detectionPredictScriptLocation) {
        DirectoryUtilities.detectionPredictScriptPath = detectionPredictScriptLocation;
    }

    @Value("${detection-results-path}")
    public void setDetectionResultsLocation(String detectionResultsLocation) {
        DirectoryUtilities.detectionResultsPath = detectionResultsLocation;
    }

    @Value("${recognition-launcher-script-path}")
    public void setRecognitionLauncherScriptLocation(String recognitionLauncherScriptLocation) {
        DirectoryUtilities.recognitionLauncherScriptPath = recognitionLauncherScriptLocation;
    }

    @Value("${recognition-inference-config-path}")
    public void setRecognitionInferenceConfigLocation(String recognitionInferenceConfigLocation) {
        DirectoryUtilities.recognitionInferenceConfigPath = recognitionInferenceConfigLocation;
    }

    @Value("${recognition-train-script-path}")
    public void setRecognitionTrainScriptLocation(String recognitionTrainScriptLocation) {
        DirectoryUtilities.recognitionTrainScriptPath = recognitionTrainScriptLocation;
    }

    @Value("${recognition-train-config-path}")
    public void setRecognitionTrainConfigLocation(String recognitionTrainConfigLocation) {
        DirectoryUtilities.recognitionTrainConfigPath = recognitionTrainConfigLocation;
    }

    @Value("${recognition-yolo-weights-path}")
    public void setRecognitionYoloWeightsLocation(String recognitionYoloWeightsLocation) {
        DirectoryUtilities.recognitionYoloWeightsPath = recognitionYoloWeightsLocation;
    }

    @Value("${recognition-inference-script-path}")
    public void setRecognitionInferenceScriptLocation(String recognitionInferenceScriptLocation) {
        DirectoryUtilities.recognitionInferenceScriptPath = recognitionInferenceScriptLocation;
    }

    public static String getInferenceDataPath() {
        return rootDataPath + "/inference-data";
    }

    public static String getRecognitionModelWeightsPath() {
        return rootDataPath + "/weights";
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

    public static List<String> readFileNamesInDirectory(String directoryUrl) throws IOException {
        Path directoryPath = Paths.get(directoryUrl);
        try (Stream<Path> stream = Files.list(directoryPath)) {
            return stream.map(Path::getFileName)
                    .map(Path::toString)
                    .toList();
        }
    }


}
