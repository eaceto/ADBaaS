package dev.eaceto.mobile.tools.android.adb.api.model.errors;

import org.springframework.http.HttpStatus;

public class APIError {
    private String errorId;
    private HttpStatus status;
    private String message;

    public APIError(String errorId, HttpStatus status, String message) {
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
