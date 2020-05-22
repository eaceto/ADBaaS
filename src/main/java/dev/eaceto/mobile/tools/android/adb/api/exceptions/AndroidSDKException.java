package dev.eaceto.mobile.tools.android.adb.api.exceptions;

public class AndroidSDKException extends Exception {
    private String uuid;
    private Exception baseException;

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
