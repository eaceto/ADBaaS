package dev.eaceto.mobile.tools.android.adb.api.controller;

import dev.eaceto.mobile.tools.android.adb.api.service.androidsdk.ADBService;
import dev.eaceto.mobile.tools.android.adb.api.service.androidsdk.LogcatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("device/{deviceId}")
@CrossOrigin(origins = "*")
public class LogcatController {

    @Autowired
    LogcatService logcatService;

    @Autowired
    ADBService adbService;

    @GetMapping(value = "/application/{packageName}/logcat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamSseMvc(@PathVariable("deviceId") String deviceId,
                                   @PathVariable("packageName") String packageName) throws Exception {

        Long pid = adbService.getApplicationPID(deviceId, packageName);
        if (pid == null || pid <= 0) {
            throw new IllegalArgumentException("Application is not running");
        }

        return logcatService.createEmitter(deviceId, packageName, pid);
    }
}
