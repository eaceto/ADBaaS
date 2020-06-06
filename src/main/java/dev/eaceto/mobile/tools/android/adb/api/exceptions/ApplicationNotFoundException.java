package dev.eaceto.mobile.tools.android.adb.api.exceptions;

public class ApplicationNotFoundException extends APIBaseException {

    private final String deviceId;
    private final String packageName;

    public ApplicationNotFoundException(String deviceId, String packageName) {
        this.deviceId = deviceId;
        this.packageName = packageName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getPackageName() {
        return packageName;
    }
}
