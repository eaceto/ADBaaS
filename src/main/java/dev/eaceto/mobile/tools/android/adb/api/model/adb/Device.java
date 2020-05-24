package dev.eaceto.mobile.tools.android.adb.api.model.adb;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Arrays;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Device {
    private final String serialNumber;
    private final String status;
    private String usb;
    private String product;
    private String model;
    private String device;
    private String transportId;

    private Device(String serialNumber, String status) {
        this.serialNumber = serialNumber;
        this.status = status;
    }

    public static Device fromADBOutput(String[] output) {
        List<String> parts = Arrays.asList(output);

        if (parts.size() < 2) return null;

        String serialNumber = parts.get(0).trim();
        String status = parts.get(1).trim();

        if (serialNumber.isBlank() || status.isBlank()) return null;

        Device device = new Device(serialNumber, status);
        if (parts.size() > 2) {
            parts = parts.subList(2, parts.size());
            for (String part : parts) {
                String[] keyValue = part.split(":");
                if (keyValue.length != 2) continue;

                device.addProperty(keyValue[0], keyValue[1]);
            }
        }

        return device;
    }

    private boolean addProperty(String key, String value) {
        value = value.replaceAll("_", " ");
        switch (key) {
            case "usb": {
                usb = value;
                return true;
            }
            case "product": {
                product = value;
                return true;
            }
            case "model": {
                model = value;
                return true;
            }
            case "device": {
                device = value;
                return true;
            }
            case "transport_id": {
                transportId = value;
                return true;
            }
        }
        return false;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getStatus() {
        return status;
    }

    public String getUsb() {
        return usb;
    }

    public String getProduct() {
        return product;
    }

    public String getModel() {
        return model;
    }

    public String getDevice() {
        return device;
    }

    public String getTransportId() {
        return transportId;
    }
}
