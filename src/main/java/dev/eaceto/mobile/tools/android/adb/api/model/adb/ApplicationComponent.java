package dev.eaceto.mobile.tools.android.adb.api.model.adb;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationComponent {

    private final String packageName;
    private final String componentName;

    public ApplicationComponent(String packageName, String componentName) {
        this.packageName = packageName;
        this.componentName = componentName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getComponentName() {
        return componentName;
    }
}
