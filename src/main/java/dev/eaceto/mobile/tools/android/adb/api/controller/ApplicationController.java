package dev.eaceto.mobile.tools.android.adb.api.controller;

import dev.eaceto.mobile.tools.android.adb.api.model.adb.Application;
import dev.eaceto.mobile.tools.android.adb.api.model.adb.Device;
import dev.eaceto.mobile.tools.android.adb.api.service.AndroidSDKService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("device/{deviceId}")
@CrossOrigin(origins = "*")
public class ApplicationController {

    @Autowired
    AndroidSDKService androidSDKService;

    @RequestMapping(value = "/application", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Application> getApplications(@PathVariable("deviceId") String deviceId,
                                             @RequestParam(required = false) String type) throws Exception {
        boolean allApps = "all".equals(type);
        return androidSDKService.getApplications(deviceId, allApps);
    }

    @RequestMapping(value = "/application/{packageName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Application> findApplication(@PathVariable("deviceId") String deviceId,
                                       @PathVariable("packageName") String packageName) throws Exception {
        List<Application> apps = androidSDKService.getApplications(deviceId, true);
        List<Application> filtered = apps.stream().filter( application -> packageName.equals(application.getPackageName())).collect(Collectors.toList());
        if (filtered.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else if (filtered.size() > 1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "multiple applications with the same packageName");
        }
        return new ResponseEntity(filtered.get(0), HttpStatus.OK);
    }

}
