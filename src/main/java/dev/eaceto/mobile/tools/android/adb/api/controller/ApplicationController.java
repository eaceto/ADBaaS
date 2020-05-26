package dev.eaceto.mobile.tools.android.adb.api.controller;

import dev.eaceto.mobile.tools.android.adb.api.model.adb.Application;
import dev.eaceto.mobile.tools.android.adb.api.model.adb.ApplicationState;
import dev.eaceto.mobile.tools.android.adb.api.service.StorageService;
import dev.eaceto.mobile.tools.android.adb.api.service.androidsdk.ADBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("device/{deviceId}")
@CrossOrigin(origins = "*")
public class ApplicationController {

    private final StorageService storageService;
    @Autowired
    ADBService adbService;

    @Autowired
    public ApplicationController(StorageService storageService) {
        this.storageService = storageService;
    }

    @RequestMapping(value = "/application", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Application> getApplications(@PathVariable("deviceId") String deviceId,
                                             @RequestParam(name = "type", required = false) String type) throws Exception {
        boolean allApps = "all".equals(type);
        return adbService.getApplications(deviceId, allApps);
    }

    @PostMapping(value = "/application/{packageName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Application> installApplication(@PathVariable("deviceId") String deviceId,
                                          @PathVariable("packageName") String packageName,
                                          @RequestParam("file") MultipartFile file) throws Exception {

        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "file is empty.");
        }

        String filePath = storageService.store(file);
        if (filePath == null || filePath.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "cannot store file.");
        }

        Application application = adbService.installApplication(deviceId, packageName, filePath);
        return new ResponseEntity(application, HttpStatus.OK);
    }

    @RequestMapping(value = "/application/{packageName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Application> findApplication(@PathVariable("deviceId") String deviceId,
                                                       @PathVariable("packageName") String packageName) throws Exception {
        List<Application> apps = adbService.getApplications(deviceId, true);
        List<Application> filtered = apps.stream().filter(application -> packageName.equals(application.getPackageName())).collect(Collectors.toList());
        if (filtered.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else if (filtered.size() > 1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "multiple applications with the same packageName");
        }
        return new ResponseEntity(filtered.get(0), HttpStatus.OK);
    }

    @RequestMapping(value = "/application/{packageName}/state", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApplicationState> isApplicationRunning(@PathVariable("deviceId") String deviceId,
                                                                 @PathVariable("packageName") String packageName) throws Exception {
        List<Application> apps = adbService.getApplications(deviceId, true);
        List<Application> filtered = apps.stream().filter(application -> packageName.equals(application.getPackageName())).collect(Collectors.toList());
        if (filtered.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else if (filtered.size() > 1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "multiple applications with the same packageName");
        }

        ApplicationState appState = new ApplicationState(filtered.get(0));

        Long pid = adbService.getApplicationPID(deviceId, packageName);
        if (pid != null && pid >= 0) {
            appState.setRunning(true);
            appState.setPID(pid);
        }

        return new ResponseEntity(appState, HttpStatus.OK);
    }

    @RequestMapping(value = "/application/{packageName}", method = RequestMethod.DELETE)
    public void deleteApplication(@PathVariable("deviceId") String deviceId,
                                  @PathVariable("packageName") String packageName,
                                  @RequestParam(name = "keepData", required = false, defaultValue = "false") boolean keepData) throws Exception {

        adbService.deleteApplication(deviceId, packageName, keepData);
    }

}
