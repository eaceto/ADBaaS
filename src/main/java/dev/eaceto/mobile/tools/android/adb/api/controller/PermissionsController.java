package dev.eaceto.mobile.tools.android.adb.api.controller;

import dev.eaceto.mobile.tools.android.adb.api.model.adb.PermissionStatus;
import dev.eaceto.mobile.tools.android.adb.api.service.androidsdk.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("permissions")
@CrossOrigin(origins = "*")
public class PermissionsController {

    @Autowired
    PermissionService permissionService;

    @RequestMapping(value = "/{permission}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public PermissionStatus grantPermission(@RequestParam("deviceId") String deviceId,
                                            @RequestParam("packageName") String packageName,
                                            @PathVariable("permission") String permission) throws Exception {
        return permissionService.grantPermission(deviceId, packageName, permission);
    }

    @RequestMapping(value = "/{permission}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PermissionStatus revokePermission(@RequestParam("deviceId") String deviceId,
                                             @RequestParam("packageName") String packageName,
                                             @PathVariable("permission") String permission) throws Exception {
        return permissionService.revokePermission(deviceId, packageName, permission);
    }
}
