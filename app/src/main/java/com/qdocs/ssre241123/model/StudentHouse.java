package com.qdocs.ssre241123.model;

public class StudentHouse {
    private int id;
    private String houseName;
    private String description;
    private String isActive;
    private String createdAt;
    private String updatedAt;

    public StudentHouse() {
    }

    public StudentHouse(int id, String houseName, String description, String isActive, String createdAt, String updatedAt) {
        this.id = id;
        this.houseName = houseName;
        this.description = description;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getHouseName() {
        return houseName;
    }

    public String getDescription() {
        return description;
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
    public void setId(int id) {
        this.id = id;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public void setDescription(String description) {
        this.description = description;
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
    public boolean isActiveHouse() {
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
        return "StudentHouse{" +
                "id=" + id +
                ", houseName='" + houseName + '\'' +
                ", description='" + description + '\'' +
                ", isActive='" + isActive + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}

