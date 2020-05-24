package dev.eaceto.mobile.tools.android.adb.api.model.adb;

public class PermissionStatus {
    private String status;

    public PermissionStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
