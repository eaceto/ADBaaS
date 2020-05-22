package dev.eaceto.mobile.tools.android.adb.api.model.adb;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmulatorInfo {
    private String serialNumber;
    private long processId;

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
