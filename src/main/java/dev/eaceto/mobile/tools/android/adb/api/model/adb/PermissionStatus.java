package dev.eaceto.mobile.tools.android.adb.api.model.adb;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.lang.NonNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PermissionStatus {

    private final Application application;
    private final String permission;
    private final Status status;

    public PermissionStatus(Application application, String permission, Status status) {
        this.application = application;
        this.permission = permission;
        this.status = status;
    }

    public Application getApplication() {
        return application;
    }

    public String getPermission() {
        return permission;
    }

    public Status getStatus() {
        return status;
    }

    public enum Status {
        UNKNOWN("unknown"),
        GRANTED("granted"),
        REVOKED("revoked");

        private final String name;

        Status(@NonNull String name) {
            this.name = name;
        }

        public boolean equalsName(String otherName) {
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }
}
