package dev.eaceto.mobile.tools.android.adb.api.service.androidsdk;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class ScreenshotService extends ADBService {

    public byte[] captureScreenshot(String deviceId) throws Exception {
        String args = "-s " + deviceId + " shell screencap -p";
        Process p = executeProcess(adb, args);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        p.getInputStream().transferTo(baos);
        return baos.toByteArray();
    }

}
