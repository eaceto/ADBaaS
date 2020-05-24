package dev.eaceto.mobile.tools.android.adb.api.service;

import dev.eaceto.mobile.tools.android.adb.api.model.adb.Application;
import dev.eaceto.mobile.tools.android.adb.api.model.adb.Device;
import dev.eaceto.mobile.tools.android.adb.api.model.adb.PermissionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AndroidSDKService {

    @Autowired
    private Environment env;

    private String adb = "/platform-tools/adb";
    private String emulator = "/emulator/emulator";
    private String emulatorCheck = "/emulator/emulator-check";

    public String getAndroidHomePath() {
        return env.getProperty("ANDROID_HOME");
    }

    private String getCommand(String executable, String arguments) {
        return getAndroidHomePath() + executable + " " + arguments;
    }

    private Process executeProcess(String executable, String arguments) throws Exception {
        return Runtime.getRuntime().exec(getCommand(executable, arguments));
    }

    private Process executeProcessWithBash(String executable, String arguments) throws Exception {
        return Runtime.getRuntime().exec("bash -c '" + getCommand(executable, arguments) + "'");
    }

    public List<Device> getConnectedDevices() throws Exception {
        List<Device> devices = new ArrayList<>();

        Process p = executeProcess(adb, "devices -l");

        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        boolean foundStartingTag = false;
        while ((line = br.readLine()) != null) {
            if ("List of devices attached".equals(line)) {
                foundStartingTag = true;
                continue;
            }
            if (!foundStartingTag) continue;

            String[] parts = Arrays.stream(line.split(" ")).filter(s -> !s.isBlank()).toArray(String[]::new);
            if (parts.length < 2) continue;

            Device device = Device.fromADBOutput(parts);
            if (device != null) {
                devices.add(device);
            }
        }
        br.close();

        return devices;
    }

    public List<Application> getApplications(String deviceId, boolean allApps) throws Exception {
        List<Application> applications = new ArrayList<>();

        String args = "-s " + deviceId + " shell pm list packages --show-versioncode";
        if (!allApps) {
            args += " -3";
        }
        Process p = executeProcess(adb, args);

        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {
            Application app = Application.fromADBOutput(line.split(" "));
            if (app != null) {
                applications.add(app);
            }
        }
        br.close();

        return applications;
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

    public byte[] captureScreenshot(String deviceId) throws Exception {
        String args = "-s " + deviceId + " shell screencap -p";
        Process p = executeProcess(adb, args);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        p.getInputStream().transferTo(baos);
        return baos.toByteArray();
    }

    public PermissionStatus grantPermission(String deviceId, String packageName, String permission) throws Exception {
        String args = "-s " + deviceId + " shell pm grant " + packageName + " " + permission;
        Process p = executeProcess(adb, args);

        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.contains("device") && line.contains("not found")) throw new Exception("device not found: " + deviceId);
            if (line.contains("Unknown permission")) throw new IllegalArgumentException("Unknown permission: " + permission);
            if (line.contains("Unknown package")) throw new IllegalArgumentException("Unknown package: " + packageName);
            if (line.contains("exception")) throw new Exception("Unknown exception");
        }
        br.close();
        return new PermissionStatus("granted");
    }

    public PermissionStatus revokePermission(String deviceId, String packageName, String permission) throws Exception {
        String args = "-s " + deviceId + " shell pm revoke " + packageName + " " + permission;
        Process p = executeProcess(adb, args);

        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.contains("device") && line.contains("not found")) throw new Exception("device not found: " + deviceId);
            if (line.contains("Unknown permission")) throw new IllegalArgumentException("Unknown permission: " + permission);
            if (line.contains("Unknown package")) throw new IllegalArgumentException("Unknown package: " + packageName);
            if (line.contains("exception")) throw new Exception("Unknown exception");
        }
        br.close();
        return new PermissionStatus("revoked");
    }
}
