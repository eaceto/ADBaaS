package dev.eaceto.mobile.tools.android.adb.api.controller;

import dev.eaceto.mobile.tools.android.adb.api.model.adb.PermissionStatus;
import dev.eaceto.mobile.tools.android.adb.api.service.androidsdk.ADBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("device/{deviceId}/application/{packageName}/permission")
@CrossOrigin(origins = "*")
public class SecurityController {

    @Autowired
    ADBService adbService;

    @RequestMapping(value = "/{permission}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public PermissionStatus grantPermission(@PathVariable("deviceId") String deviceId,
                                            @PathVariable("packageName") String packageName,
                                            @PathVariable("permission") String permission) throws Exception {
        return adbService.grantPermission(deviceId, packageName, permission);
    }

    @RequestMapping(value = "/{permission}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PermissionStatus revokePermission(@PathVariable("deviceId") String deviceId,
                                             @PathVariable("packageName") String packageName,
                                             @PathVariable("permission") String permission) throws Exception {
        return adbService.revokePermission(deviceId, packageName, permission);
    }

}
