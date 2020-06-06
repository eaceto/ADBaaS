package dev.eaceto.mobile.tools.android.adb.api.service.androidsdk;

import dev.eaceto.mobile.tools.android.adb.api.exceptions.ApplicationNotFoundException;
import dev.eaceto.mobile.tools.android.adb.api.model.adb.Application;
import dev.eaceto.mobile.tools.android.adb.api.model.adb.PermissionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class PermissionService extends ADBService {

    @Autowired
    private ApplicationService applicationService;


    public PermissionStatus grantPermission(String deviceId, String packageName, String permission) throws Exception {
        Application app = applicationService.getApplication(deviceId, packageName, false);
        if (app == null) {
            throw new ApplicationNotFoundException(deviceId, packageName);
        }

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

        return new PermissionStatus(app, permission, PermissionStatus.Status.GRANTED);
    }

    public PermissionStatus revokePermission(String deviceId, String packageName, String permission) throws Exception {
        Application app = applicationService.getApplication(deviceId, packageName, false);
        if (app == null) {
            throw new ApplicationNotFoundException(deviceId, packageName);
        }

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

        return new PermissionStatus(app, permission, PermissionStatus.Status.REVOKED);
    }

}
