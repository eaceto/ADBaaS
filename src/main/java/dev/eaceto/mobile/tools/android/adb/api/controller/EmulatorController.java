package dev.eaceto.mobile.tools.android.adb.api.controller;


import dev.eaceto.mobile.tools.android.adb.api.service.AndroidSDKService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("emulator")
@CrossOrigin(origins = "*")
public class EmulatorController {

    @Autowired
    AndroidSDKService androidSDKService;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getAvailableEmulators() throws Exception {
        return androidSDKService.getEmulators();
    }

}
