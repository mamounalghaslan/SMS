package mgkm.smsbackend.utilities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class PythonCaller {

    private static String pythonPath;

    @Value("${python-executable-path}")
    public void setPythonPath(String pythonPath) {
        PythonCaller.pythonPath = pythonPath;
    }

    public static void callPython(
            String scriptPath,
            String modelPath,
            String imagePath,
            String outputPath) {

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    pythonPath,
                    scriptPath,
                    modelPath,
                    imagePath,
                    outputPath);

            Process process = processBuilder.start();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("Script executed, exit code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
