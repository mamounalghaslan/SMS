package mgkm.smsbackend.utilities;

import mgkm.smsbackend.jobsConfigs.listeners.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class PythonCaller {

    private static final Logger log = LoggerFactory.getLogger(JobListener.class);

    public static int callPython(String... args) {

        int exitCode;

        try {

            ArrayList<String> fullArgs = new ArrayList<>();
            fullArgs.add(DirectoryUtilities.pythonPath);
            fullArgs.addAll(Arrays.asList(args));

            log.info("Python Caller: {}", fullArgs);

            ProcessBuilder processBuilder = new ProcessBuilder(fullArgs);

            Process process = processBuilder.start();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.info(line);
            }

            exitCode = process.waitFor();
            log.info("Script executed, exit code: {}", exitCode);

        } catch (IOException e) {
            log.error("Error executing python script.");
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            log.warn("Python script interrupted.");
            throw new RuntimeException(e);
        }

        return exitCode;
    }

}
