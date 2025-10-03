package com.qdocs.ssre241123.model;

public class TeacherModule {
    private String name;
    private String displayName;
    private String iconName;
    private int iconResource;
    private boolean isEnabled;
    private String actionType;

    public TeacherModule() {
    }

    public TeacherModule(String name, String displayName, String iconName, int iconResource, boolean isEnabled) {
        this.name = name;
        this.displayName = displayName;
        this.iconName = iconName;
        this.iconResource = iconResource;
        this.isEnabled = isEnabled;
        this.actionType = "activity";
    }

    public TeacherModule(String name, String displayName, String iconName, int iconResource, boolean isEnabled, String actionType) {
        this.name = name;
        this.displayName = displayName;
        this.iconName = iconName;
        this.iconResource = iconResource;
        this.isEnabled = isEnabled;
        this.actionType = actionType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public int getIconResource() {
        return iconResource;
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
}