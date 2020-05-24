package dev.eaceto.mobile.tools.android.adb.api.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ExceptionHelper {

    Logger logger = LoggerFactory.getLogger(ExceptionHelper.class);

    public String trackException(Exception e) {
        e.printStackTrace();
        String uuid = UUID.randomUUID().toString();
        logger.error("Error: " + uuid, e);
        return uuid;
    }

}
