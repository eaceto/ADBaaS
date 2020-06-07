package dev.eaceto.mobile.tools.android.adb.api.model.logcat;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogcatSession {

    private final String packageName;
    private final String deviceId;
    private final String sessionId;
    private Long pid;
    private String state;

    public LogcatSession(@NonNull String deviceId, @NonNull String packageName, @NonNull String sessionId) {
        this.deviceId = deviceId;
        this.packageName = packageName;
        this.sessionId = sessionId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Long getPID() {
        return pid;
    }

    public void setPID(Long pid) {
        this.pid = pid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LogcatSession started() {
        LogcatSession newSession = new LogcatSession(deviceId, packageName, sessionId);
        newSession.setPID(pid);
        newSession.setState("started");
        return newSession;
    }

    public LogcatSession finished() {
        LogcatSession newSession = new LogcatSession(deviceId, packageName, sessionId);
        newSession.setPID(pid);
        newSession.setState("finished");
        return newSession;
    }

    public String jsonString() {
        JSONObject object = new JSONObject();
        object.put("packageName", packageName);
        object.put("deviceId", deviceId);
        object.put("sessionId", sessionId);
        if (pid != null) {
            object.put("pid", pid);
        }
        if (state != null) {
            object.put("state", state);
        }
        return object.toString();
    }
}
