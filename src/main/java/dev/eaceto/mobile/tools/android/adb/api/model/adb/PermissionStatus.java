package dev.eaceto.mobile.tools.android.adb.api.model.adb;

public class PermissionStatus {
    private final String status;

    public PermissionStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
