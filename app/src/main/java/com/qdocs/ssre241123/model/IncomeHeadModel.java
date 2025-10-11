package com.qdocs.ssre241123.model;

/**
 * Model class for Income Head
 * Used for parsing Income Head List API response
 */
public class IncomeHeadModel {
    private String id;
    private String income_category;
    private String description;
    private String is_active;
    private String is_deleted;
    private String created_at;

    // Constructors
    public IncomeHeadModel() {}

    public IncomeHeadModel(String id, String income_category) {
        this.id = id;
        this.income_category = income_category;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIncomeCategory() {
        return income_category;
    }

    public void setIncomeCategory(String income_category) {
        this.income_category = income_category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsActive() {
        return is_active;
    }

    public void setIsActive(String is_active) {
        this.is_active = is_active;
    }

    public String getIsDeleted() {
        return is_deleted;
    }

    public void setIsDeleted(String is_deleted) {
        this.is_deleted = is_deleted;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return income_category != null ? income_category : "";
    }
}

