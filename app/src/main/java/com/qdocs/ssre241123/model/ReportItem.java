package com.qdocs.ssre241123.model;

public class ReportItem {
    private String id;
    private String name;
    private String displayName;
    private String categoryId;
    private int iconResource;
    private String description;

    public ReportItem() {
    }

    public ReportItem(String id, String name, String displayName, String categoryId, int iconResource) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.categoryId = categoryId;
        this.iconResource = iconResource;
    }

    public ReportItem(String id, String name, String displayName, String categoryId, int iconResource, String description) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.categoryId = categoryId;
        this.iconResource = iconResource;
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getIconResource() {
        return iconResource;
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
