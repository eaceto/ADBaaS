package dev.eaceto.mobile.tools.android.adb.api.controller;

import dev.eaceto.mobile.tools.android.adb.api.service.androidsdk.ScreenshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@RequestMapping("screenshots")
@CrossOrigin(origins = "*")
public class ScreenshotsController {

    @Autowired
    ScreenshotService screenshotsService;

    @RequestMapping(value = "/image", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody
    byte[] captureScreenshot(@RequestParam(value = "deviceId") String deviceId) throws Exception {
        return screenshotForDevice(deviceId);
    }

    @RequestMapping(value = "/base64", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody
    String captureScreenshotInBase64(@RequestParam(value = "deviceId") String deviceId) throws Exception {
        return new String(Base64.getEncoder().encode(screenshotForDevice(deviceId)));
    }

    private byte[] screenshotForDevice(String deviceId) throws Exception {
        return screenshotsService.captureScreenshot(deviceId);
    }

}
