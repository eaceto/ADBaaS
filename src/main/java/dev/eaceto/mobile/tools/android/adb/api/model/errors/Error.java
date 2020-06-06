package dev.eaceto.mobile.tools.android.adb.api.model.errors;

import org.springframework.http.HttpStatus;

public class Error {
    private final String errorId;
    private final HttpStatus status;
    private final String message;

    public Error(String errorId, HttpStatus status, String message) {
        super();
        this.errorId = errorId;
        this.status = status;
        this.message = message;
    }

    public String getErrorId() {
        return errorId;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
