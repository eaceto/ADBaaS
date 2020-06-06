package dev.eaceto.mobile.tools.android.adb.api.exceptions;

import java.util.UUID;

public class APIBaseException extends Exception {
    private final String uuid;

    public APIBaseException() {
        this.uuid = UUID.randomUUID().toString();
    }

    public String getUUID() {
        return uuid;
    }

}
