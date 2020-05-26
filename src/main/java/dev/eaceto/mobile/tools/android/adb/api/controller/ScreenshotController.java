package dev.eaceto.mobile.tools.android.adb.api.controller;

import dev.eaceto.mobile.tools.android.adb.api.service.androidsdk.ADBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@RequestMapping("device/{deviceId}")
@CrossOrigin(origins = "*")
public class ScreenshotController {

    @Autowired
    ADBService adbService;

    @RequestMapping(value = "/screenshot", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody
    byte[] captureScreenshot(@PathVariable("deviceId") String deviceId) throws Exception {
        return adbService.captureScreenshot(deviceId);
    }

    @RequestMapping(value = "/screenshot/base64", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody
    String captureScreenshotInBase64(@PathVariable("deviceId") String deviceId) throws Exception {
        byte[] screenshot = adbService.captureScreenshot(deviceId);
        return "data:image/png;base64," + new String(Base64.getEncoder().encode(screenshot));
    }

}
