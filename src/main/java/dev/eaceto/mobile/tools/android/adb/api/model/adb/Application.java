package dev.eaceto.mobile.tools.android.adb.api.model.adb;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Application {
    private String packageName;
    private String versionCode;

    private Application(String packageName) {
        if (packageName == null) throw new NullPointerException("package cannot be null");
        this.packageName = packageName;
    }

    public static Application fromADBOutput(String[] output) {
        if (output.length < 1) return null;

        HashMap<String, String> properties = new HashMap<>();

        for (String prop : output) {
            String[] keyValue = prop.split(":");
            if (keyValue.length != 2) continue;
            properties.put(keyValue[0].trim(), keyValue[1].trim());
        }

        if (!properties.containsKey("package")) return null;
        Application application = new Application(properties.get("package"));
        application.versionCode = properties.getOrDefault("versionCode", null);

        return application;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getVersionCode() {
        return versionCode;
    }
}
