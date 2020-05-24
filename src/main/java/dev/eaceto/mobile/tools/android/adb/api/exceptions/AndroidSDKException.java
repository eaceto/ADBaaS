package dev.eaceto.mobile.tools.android.adb.api.exceptions;

public class AndroidSDKException extends Exception {
    private final String uuid;
    private final Exception baseException;

    public AndroidSDKException(String uuid, Exception exception) {
        this.uuid = uuid;
        this.baseException = exception;
    }

    public String getUUID() {
        return uuid;
    }

    public String getException() {
        return baseException.getMessage();
    }
}
