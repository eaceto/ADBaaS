package dev.eaceto.mobile.tools.android.adb.api.controller;

import dev.eaceto.mobile.tools.android.adb.api.helpers.ExceptionHelper;
import dev.eaceto.mobile.tools.android.adb.api.model.errors.APIError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(name = "adbaas.errors.controlleradvice", havingValue = "true")
@RestControllerAdvice
@CrossOrigin(origins = "*")
public class APIExceptionHandler {

    @Autowired
    public ExceptionHelper exceptionHelper;
    Logger logger = LoggerFactory.getLogger(APIExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIError> handleException(Exception ex) {
        String errorId = exceptionHelper.trackException(ex);
        logger.error("Error Id: " + errorId, ex);
        HttpStatus statusError = HttpStatus.INTERNAL_SERVER_ERROR;
        APIError apiError = new APIError(errorId, statusError, ex.getLocalizedMessage());
        return new ResponseEntity<>(apiError, statusError);
    }

}
