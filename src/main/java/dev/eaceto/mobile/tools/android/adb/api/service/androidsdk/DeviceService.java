package dev.eaceto.mobile.tools.android.adb.api.service.androidsdk;

import dev.eaceto.mobile.tools.android.adb.api.model.adb.Device;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DeviceService extends ADBService {

    public List<Device> getConnectedDevices() throws Exception {
        List<Device> devices = new ArrayList<>();

        Process p = executeProcess(adb, "devices -l");

        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        boolean foundStartingTag = false;
        while ((line = br.readLine()) != null) {
            if ("List of devices attached".equals(line)) {
                foundStartingTag = true;
                continue;
            }
            if (!foundStartingTag) continue;

            String[] parts = Arrays.stream(line.split(" ")).filter(s -> !s.isBlank()).toArray(String[]::new);
            if (parts.length < 2) continue;

            Device device = Device.fromADBOutput(parts);
            if (device != null) {
                devices.add(device);
            }
        }
        br.close();

        return devices;
    }

}
