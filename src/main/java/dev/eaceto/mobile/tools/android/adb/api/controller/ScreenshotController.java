package dev.eaceto.mobile.tools.android.adb.api.controller;

import dev.eaceto.mobile.tools.android.adb.api.model.adb.Device;
import dev.eaceto.mobile.tools.android.adb.api.service.AndroidSDKService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("device/{deviceId}")
@CrossOrigin(origins = "*")
public class ScreenshotController {

    @Autowired
    AndroidSDKService androidSDKService;

    @RequestMapping(value = "/screenshot", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] captureScreenshot(@PathVariable("deviceId") String deviceId) throws Exception {
        return androidSDKService.captureScreenshot(deviceId);
    }

    @RequestMapping(value = "/screenshot/base64", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String captureScreenshotInBase64(@PathVariable("deviceId") String deviceId) throws Exception {
        byte[] screenshot = androidSDKService.captureScreenshot(deviceId);
        return "data:image/png;base64," + new String(Base64.getEncoder().encode(screenshot));
    }

}
