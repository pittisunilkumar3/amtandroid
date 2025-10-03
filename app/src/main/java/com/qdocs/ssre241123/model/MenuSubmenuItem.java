package com.qdocs.ssre241123.model;

public class MenuSubmenuItem {
    private String id;
    private String name;
    private String displayName;
    private String url;
    private int iconResource;
    private String parentMenuId;
    private String description;

    public MenuSubmenuItem() {
    }

    public MenuSubmenuItem(String id, String name, String displayName, String url, int iconResource, String parentMenuId) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.url = url;
        this.iconResource = iconResource;
        this.parentMenuId = parentMenuId;
    }

    public MenuSubmenuItem(String id, String name, String displayName, String url, int iconResource, String parentMenuId, String description) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.url = url;
        this.iconResource = iconResource;
        this.parentMenuId = parentMenuId;
        this.description = description;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIconResource() {
        return iconResource;
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
    }

    public String getParentMenuId() {
        return parentMenuId;
    }

    public void setParentMenuId(String parentMenuId) {
        this.parentMenuId = parentMenuId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

