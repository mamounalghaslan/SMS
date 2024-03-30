package mgkm.smsbackend.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PythonCaller {

    public static void callPython(
            String scriptPath, String modelPath, String imagePath, String outputPath) {

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "F:/anaconda/python.exe", scriptPath, modelPath, imagePath, outputPath);

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
