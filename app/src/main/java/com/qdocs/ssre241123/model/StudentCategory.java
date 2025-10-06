package com.qdocs.ssre241123.model;

public class StudentCategory {
    private int categoryId;
    private String categoryName;
    private String isActive;
    private String createdAt;
    private String updatedAt;

    public StudentCategory() {
    }

    public StudentCategory(int categoryId, String categoryName, String isActive, String createdAt, String updatedAt) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getIsActive() {
        return isActive;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Helper methods
    public boolean isActiveCategory() {
        if (isActive == null) {
            return false;
        }
        // Handle multiple possible values: "yes", "1", "true", "active"
        String normalized = isActive.trim().toLowerCase();
        return "yes".equals(normalized) ||
               "1".equals(normalized) ||
               "true".equals(normalized) ||
               "active".equals(normalized);
    }

    @Override
    public String toString() {
        return "StudentCategory{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", isActive='" + isActive + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}

