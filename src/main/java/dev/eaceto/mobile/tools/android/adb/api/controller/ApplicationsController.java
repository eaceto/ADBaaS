package dev.eaceto.mobile.tools.android.adb.api.controller;

import dev.eaceto.mobile.tools.android.adb.api.model.adb.Application;
import dev.eaceto.mobile.tools.android.adb.api.model.adb.ApplicationComponent;
import dev.eaceto.mobile.tools.android.adb.api.model.adb.ApplicationState;
import dev.eaceto.mobile.tools.android.adb.api.service.StorageService;
import dev.eaceto.mobile.tools.android.adb.api.service.androidsdk.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("applications")
@CrossOrigin(origins = "*")
public class ApplicationsController {

    private final StorageService storageService;

    @Autowired
    ApplicationService applicationService;

    @Autowired
    public ApplicationsController(StorageService storageService) {
        this.storageService = storageService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Application> getApplications(@RequestParam("deviceId") String deviceId,
                                             @RequestParam(name = "type", required = false) String type) throws Exception {
        boolean allApps = "all".equals(type);
        return applicationService.getApplications(deviceId, allApps);
    }

    @RequestMapping(value = "/{packageName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Application> getApplication(@RequestParam("deviceId") String deviceId,
                                                      @PathVariable("packageName") String packageName) throws Exception {
        Application app = applicationService.getApplication(deviceId, packageName, true);
        if (app != null) {
            return new ResponseEntity(app, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/{packageName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Application> installApplication(@RequestParam("deviceId") String deviceId,
                                                          @PathVariable("packageName") String packageName,
                                                          @RequestParam("file") MultipartFile file) throws Exception {

        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "file is empty.");
        }

        String filePath = storageService.store(file);
        if (filePath == null || filePath.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "cannot store file.");
        }

        Application application = applicationService.installApplication(deviceId, packageName, filePath);
        return new ResponseEntity(application, HttpStatus.OK);
    }

    @RequestMapping(value = "/{packageName}", method = RequestMethod.DELETE)
    public void uninstallApplication(@RequestParam("deviceId") String deviceId,
                                     @PathVariable("packageName") String packageName,
                                     @RequestParam(name = "keepData", required = false, defaultValue = "false") boolean keepData) throws Exception {
        applicationService.deleteApplication(deviceId, packageName, keepData);
    }

    @RequestMapping(value = "/{packageName}/state", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApplicationState> getApplicationState(@RequestParam("deviceId") String deviceId,
                                                                @PathVariable("packageName") String packageName) throws Exception {
        if (!applicationService.isApplicationInstalled(deviceId, packageName)) {
            return ResponseEntity.notFound().build();
        }
        ApplicationState appState = applicationService.getApplicationState(deviceId, packageName);
        return new ResponseEntity(appState, HttpStatus.OK);
    }

    @RequestMapping(value = "/{packageName}/components", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ApplicationComponent>> getApplicationComponents(@RequestParam("deviceId") String deviceId,
                                                                               @PathVariable("packageName") String packageName) throws Exception {
        if (!applicationService.isApplicationInstalled(deviceId, packageName)) {
            return ResponseEntity.notFound().build();
        }
        List<ApplicationComponent> appComponents = applicationService.getApplicationComponents(deviceId, packageName);
        return new ResponseEntity(appComponents, HttpStatus.OK);
    }

}
