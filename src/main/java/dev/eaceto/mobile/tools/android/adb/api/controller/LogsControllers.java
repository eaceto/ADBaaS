package dev.eaceto.mobile.tools.android.adb.api.controller;

import dev.eaceto.mobile.tools.android.adb.api.service.androidsdk.LogcatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("logs")
@CrossOrigin(origins = "*")
public class LogsControllers {

    @Autowired
    LogcatService logcatService;

    @GetMapping(value = "stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamSseMvc(@RequestParam("deviceId") String deviceId,
                                   @RequestParam("packageName") String packageName) throws Exception {
        return logcatService.createEmitter(deviceId, packageName);
    }
}
