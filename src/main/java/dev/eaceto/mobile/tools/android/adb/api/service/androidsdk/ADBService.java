package dev.eaceto.mobile.tools.android.adb.api.service.androidsdk;

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
import java.util.stream.Collectors;

@Service
public class ADBService {

    private final String adb = "/platform-tools/adb";

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
            if (line.contains("device") && line.contains("not found"))
                throw new Exception("device not found: " + deviceId);
            if (line.contains("Unknown permission"))
                throw new IllegalArgumentException("Unknown permission: " + permission);
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
            if (line.contains("device") && line.contains("not found"))
                throw new Exception("device not found: " + deviceId);
            if (line.contains("Unknown permission"))
                throw new IllegalArgumentException("Unknown permission: " + permission);
            if (line.contains("Unknown package")) throw new IllegalArgumentException("Unknown package: " + packageName);
            if (line.contains("exception")) throw new Exception("Unknown exception");
        }
        br.close();
        return new PermissionStatus("revoked");
    }

    public Application installApplication(String deviceId, String packageName, String filePath) throws Exception {
        String args = "-s " + deviceId + " install -r -t -i " + packageName + " " + filePath;
        Process p = executeProcess(adb, args);

        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        boolean installed = false;
        String line;

        while ((line = br.readLine()) != null) {
            if (line.contains("adb:")) {
                throw new Exception(line.replace("adb:", ""));
            }
            if ("Success".equals(line)) {
                installed = true;
                break;
            }
        }
        br.close();

        if (!installed) throw new Exception("Could not install application");

        List<Application> apps = getApplications(deviceId, true);
        List<Application> filtered = apps.stream().filter(application -> packageName.equals(application.getPackageName())).collect(Collectors.toList());
        if (filtered.isEmpty()) throw new Exception("Application not found");
        if (filtered.size() != 1) throw new Exception("Internal service error");
        return filtered.get(0);
    }

    public void deleteApplication(String deviceId, String packageName, boolean keepData) throws Exception {
        String args = "-s " + deviceId + " shell cmd package uninstall " + (keepData ? "-k" : "") + " " + packageName;
        Process p = executeProcess(adb, args);

        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        boolean uninstalled = false;
        String line;

        while ((line = br.readLine()) != null) {
            if (line.contains("adb:")) {
                throw new Exception(line.replace("adb:", ""));
            }
            if ("Success".equals(line)) {
                uninstalled = true;
                break;
            }
        }
        br.close();

        if (!uninstalled) throw new Exception("Could not uninstall application");
    }

    public Long getApplicationPID(String deviceId, String packageName) throws Exception {
        String args = "-s " + deviceId + " shell pidof -s " + packageName;
        Process p = executeProcess(adb, args);

        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        StringBuilder pidStrBuilder = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            pidStrBuilder.append(line.trim());
        }
        br.close();

        String pidString = pidStrBuilder.toString();
        if (!pidString.isBlank()) {
            return Long.valueOf(pidString);
        }
        return null;
    }
}
