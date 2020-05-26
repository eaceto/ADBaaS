package dev.eaceto.mobile.tools.android.adb.api;

import dev.eaceto.mobile.tools.android.adb.api.model.adb.Device;
import dev.eaceto.mobile.tools.android.adb.api.service.androidsdk.ADBService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdBaaSApplicationTests {

    @Autowired
    ADBService adbService;

    @DisplayName("List ADB Devices")
    @Test
    void listDevices() {
        try {
            File sdkDirectory = new File(adbService.getAndroidHomePath());
            if (!sdkDirectory.exists()) {
                // Avoid performing test if SDK is not available
                assertTrue(true);
                return;
            }

            List<Device> devices = adbService.getConnectedDevices();
            assertTrue(!devices.isEmpty());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e);
        }
    }

}
