package dev.eaceto.mobile.tools.android.adb.api.service.androidsdk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmulatorService {

    private final String emulator = "/emulator/emulator";
    @Autowired
    private Environment env;

    public String getAndroidHomePath() {
        return env.getProperty("ANDROID_HOME");
    }

    private String getCommand(String executable, String arguments) {
        return getAndroidHomePath() + executable + " " + arguments;
    }

    private Process executeProcess(String executable, String arguments) throws Exception {
        return Runtime.getRuntime().exec(getCommand(executable, arguments));
    }

    public List<String> getEmulators() throws Exception {
        List<String> emulators = new ArrayList<>();

        Process p = executeProcess(emulator, "-list-avds");

        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (!line.isBlank()) {
                emulators.add(line);
            }
        }
        br.close();

        return emulators;
    }

}
