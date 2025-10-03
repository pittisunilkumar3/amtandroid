package com.qdocs.ssre241123.model;

import java.util.List;

public class MenuItem {
    private String id;
    private String permission_group_id;
    private String icon;
    private String menu;
    private String activate_menu;
    private String lang_key;
    private String system_level;
    private String level;
    private String sidebar_display;
    private String access_permissions;
    private String is_active;
    private String created_at;
    private List<SubMenuItem> submenus;

    // Constructors
    public MenuItem() {}

    public MenuItem(String id, String icon, String menu, String activate_menu) {
        this.id = id;
        this.icon = icon;
        this.menu = menu;
        this.activate_menu = activate_menu;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPermissionGroupId() { return permission_group_id; }
    public void setPermissionGroupId(String permission_group_id) { this.permission_group_id = permission_group_id; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getMenu() { return menu; }
    public void setMenu(String menu) { this.menu = menu; }

    public String getActivateMenu() { return activate_menu; }
    public void setActivateMenu(String activate_menu) { this.activate_menu = activate_menu; }

    public String getLangKey() { return lang_key; }
    public void setLangKey(String lang_key) { this.lang_key = lang_key; }

    public String getSystemLevel() { return system_level; }
    public void setSystemLevel(String system_level) { this.system_level = system_level; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getSidebarDisplay() { return sidebar_display; }
    public void setSidebarDisplay(String sidebar_display) { this.sidebar_display = sidebar_display; }

    public String getAccessPermissions() { return access_permissions; }
    public void setAccessPermissions(String access_permissions) { this.access_permissions = access_permissions; }

    public String getIsActive() { return is_active; }
    public void setIsActive(String is_active) { this.is_active = is_active; }

    public String getCreatedAt() { return created_at; }
    public void setCreatedAt(String created_at) { this.created_at = created_at; }

    public List<SubMenuItem> getSubmenus() { return submenus; }
    public void setSubmenus(List<SubMenuItem> submenus) { this.submenus = submenus; }
}