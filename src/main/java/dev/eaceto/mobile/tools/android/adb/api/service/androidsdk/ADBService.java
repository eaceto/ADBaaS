package dev.eaceto.mobile.tools.android.adb.api.service.androidsdk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class ADBService {

    public final String adb = "/platform-tools/adb";

    @Autowired
    private Environment env;

    public String getAndroidHomePath() {
        return env.getProperty("ANDROID_HOME");
    }

    public String getCommand(String executable, String arguments) {
        return getAndroidHomePath() + executable + " " + arguments;
    }

    public Process executeProcess(String executable, String arguments) throws Exception {
        return Runtime.getRuntime().exec(getCommand(executable, arguments));
    }

}
