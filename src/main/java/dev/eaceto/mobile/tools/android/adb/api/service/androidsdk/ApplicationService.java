package dev.eaceto.mobile.tools.android.adb.api.service.androidsdk;

import dev.eaceto.mobile.tools.android.adb.api.exceptions.ApplicationNotFoundException;
import dev.eaceto.mobile.tools.android.adb.api.model.adb.Application;
import dev.eaceto.mobile.tools.android.adb.api.model.adb.ApplicationComponent;
import dev.eaceto.mobile.tools.android.adb.api.model.adb.ApplicationState;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationService extends ADBService{

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

    public boolean isApplicationInstalled(String deviceId, String packageName) throws Exception {
        String args = "-s " + deviceId + " shell pm list packages --show-versioncode";

        Process p = executeProcess(adb, args);

        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        boolean found = false;
        while ((line = br.readLine()) != null) {
            Application app = Application.fromADBOutput(line.split(" "));
            if (app != null && packageName.equals(app.getPackageName())) {
                found = true;
                break;
            }
        }
        br.close();

        return found;
    }

    public Application getApplication(String deviceId, String packageName, boolean fullInformation) throws Exception {
        Application application = null;

        String args = "-s " + deviceId + " shell pm list packages --show-versioncode";

        Process p = executeProcess(adb, args);

        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {
            Application app = Application.fromADBOutput(line.split(" "));
            if (app != null && packageName.equals(app.getPackageName())) {
                application = app;
                break;
            }
        }
        br.close();

        if (application == null) {
            throw new ApplicationNotFoundException(deviceId, packageName);
        }

        if (fullInformation) {
            application.setState(getApplicationState(deviceId, packageName));
            application.setComponents(getApplicationComponents(deviceId, packageName));
        }

        return application;
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

        return getApplication(deviceId, packageName, installed);
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

    public ApplicationState getApplicationState(String deviceId, String packageName) throws Exception {
        Long pid = getApplicationPID(deviceId, packageName);
        if (pid != null && pid >= 0) {
            return new ApplicationState(ApplicationState.State.RUNNING, pid);
        }
        return new ApplicationState(ApplicationState.State.STOPPED);
    }

    public List<ApplicationComponent> getApplicationComponents(String deviceId, String packageName) {
        ArrayList<ApplicationComponent> components = new ArrayList<>();

        return components;
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
