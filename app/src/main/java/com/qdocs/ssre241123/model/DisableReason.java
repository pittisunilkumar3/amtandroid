package com.qdocs.ssre241123.model;

public class DisableReason {
    private int id;
    private String reason;
    private String createdAt;
    private String updatedAt;

    public DisableReason() {
    }

    public DisableReason(int id, String reason, String createdAt, String updatedAt) {
        this.id = id;
        this.reason = reason;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getReason() {
        return reason;
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

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "DisableReason{" +
                "id=" + id +
                ", reason='" + reason + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}

