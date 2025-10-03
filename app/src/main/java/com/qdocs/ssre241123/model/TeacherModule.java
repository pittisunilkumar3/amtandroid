package com.qdocs.ssre241123.model;

import com.qdocs.ssre241123.utils.FontAwesomeIconMapper;

public class TeacherModule {
    private String id;
    private String name;
    private String displayName;
    private String iconName;
    private int iconResource;
    private boolean isEnabled;
    private String actionType;
    private String activateMenu;
    private String langKey;
    private String level;

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

    // Create TeacherModule from MenuItem API response
    public static TeacherModule fromMenuItem(MenuItem menuItem) {
        TeacherModule module = new TeacherModule();
        module.setId(menuItem.getId());
        module.setName(menuItem.getActivateMenu());
        module.setDisplayName(menuItem.getMenu());
        module.setIconName(menuItem.getIcon());
        module.setIconResource(FontAwesomeIconMapper.getDrawableResource(menuItem.getIcon()));
        module.setEnabled("1".equals(menuItem.getIsActive()));
        module.setActivateMenu(menuItem.getActivateMenu());
        module.setLangKey(menuItem.getLangKey());
        module.setLevel(menuItem.getLevel());
        module.setActionType("activity");
        return module;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getActivateMenu() {
        return activateMenu;
    }

    public void setActivateMenu(String activateMenu) {
        this.activateMenu = activateMenu;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}