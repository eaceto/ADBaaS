package dev.eaceto.mobile.tools.android.adb.api.model.adb;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationState {
    private Application application;
    private boolean running;
    private Long pid;

    public ApplicationState(Application application) {
        this.application = application;
        this.running = false;
        this.pid = null;
    }

    public Application getApplication() {
        return application;
    }

    public boolean isRunning() {
        return running;
    }

    public Long getPID() {
        return pid;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setPID(Long pid) {
        this.pid = pid;
    }
}
