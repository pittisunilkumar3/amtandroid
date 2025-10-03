package com.qdocs.ssre241123.model;

import java.util.List;

public class ReportCategory {
    private String id;
    private String name;
    private String displayName;
    private int iconResource;
    private List<ReportItem> reportItems;

    public ReportCategory() {
    }

    public ReportCategory(String id, String name, String displayName, int iconResource, List<ReportItem> reportItems) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.iconResource = iconResource;
        this.reportItems = reportItems;
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

    public int getIconResource() {
        return iconResource;
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
    }

    public List<ReportItem> getReportItems() {
        return reportItems;
    }

    public void setReportItems(List<ReportItem> reportItems) {
        this.reportItems = reportItems;
    }
}
