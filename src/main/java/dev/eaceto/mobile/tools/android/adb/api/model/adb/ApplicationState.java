package dev.eaceto.mobile.tools.android.adb.api.model.adb;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.lang.NonNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationState {

    private State state;
    private Long pid;

    public ApplicationState() {
        this(State.UNINSTALLED);
    }

    public ApplicationState(State state) {
        this(state, null);
    }

    public ApplicationState(State state, Long pid) {
        this.state = state;
        this.pid = pid;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Long getPID() {
        return pid;
    }

    public void setPID(Long pid) {
        this.pid = pid;
    }

    public enum State {
        UNINSTALLED("uninstalled"),
        RUNNING("running"),
        STOPPED("stopped");

        private final String name;

        State(@NonNull String name) {
            this.name = name;
        }

        public boolean equalsName(String otherName) {
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }
}
