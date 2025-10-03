package com.qdocs.ssre241123.model;

public class SubMenuItem {
    private String id;
    private String sidebar_menu_id;
    private String menu;
    private String key;
    private String lang_key;
    private String url;
    private String level;
    private String access_permissions;
    private String permission_group_id;
    private String activate_controller;
    private String activate_methods;
    private String addon_permission;
    private String is_active;
    private String created_at;

    // Constructors
    public SubMenuItem() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSidebarMenuId() { return sidebar_menu_id; }
    public void setSidebarMenuId(String sidebar_menu_id) { this.sidebar_menu_id = sidebar_menu_id; }

    public String getMenu() { return menu; }
    public void setMenu(String menu) { this.menu = menu; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getLangKey() { return lang_key; }
    public void setLangKey(String lang_key) { this.lang_key = lang_key; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getAccessPermissions() { return access_permissions; }
    public void setAccessPermissions(String access_permissions) { this.access_permissions = access_permissions; }

    public String getPermissionGroupId() { return permission_group_id; }
    public void setPermissionGroupId(String permission_group_id) { this.permission_group_id = permission_group_id; }

    public String getActivateController() { return activate_controller; }
    public void setActivateController(String activate_controller) { this.activate_controller = activate_controller; }

    public String getActivateMethods() { return activate_methods; }
    public void setActivateMethods(String activate_methods) { this.activate_methods = activate_methods; }

    public String getAddonPermission() { return addon_permission; }
    public void setAddonPermission(String addon_permission) { this.addon_permission = addon_permission; }

    public String getIsActive() { return is_active; }
    public void setIsActive(String is_active) { this.is_active = is_active; }

    public String getCreatedAt() { return created_at; }
    public void setCreatedAt(String created_at) { this.created_at = created_at; }
}