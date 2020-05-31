package dev.eaceto.mobile.tools.android.adb.api.model.adb;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationState {
    private final Application application;
    private String state;
    private Long pid;

    public ApplicationState(Application application) {
        this.application = application;
        this.state = "unknown";
        this.pid = null;
    }

    public Application getApplication() {
        return application;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getPID() {
        return pid;
    }

    public void setPID(Long pid) {
        this.pid = pid;
    }
}
