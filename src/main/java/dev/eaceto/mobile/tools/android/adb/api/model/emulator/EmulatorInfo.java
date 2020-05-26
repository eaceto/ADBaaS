package dev.eaceto.mobile.tools.android.adb.api.model.emulator;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmulatorInfo {
    private final String serialNumber;
    private final long processId;

    public EmulatorInfo(String serialNumber, long processId) {
        this.serialNumber = serialNumber;
        this.processId = processId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public long getProcessId() {
        return processId;
    }
}
