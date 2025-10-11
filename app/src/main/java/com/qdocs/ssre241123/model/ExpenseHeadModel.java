package com.qdocs.ssre241123.model;

/**
 * Model class for Expense Head
 * Used for parsing Expense Head List API response
 */
public class ExpenseHeadModel {
    private String id;
    private String exp_category;
    private String description;
    private String is_active;
    private String is_deleted;
    private String created_at;

    // Constructors
    public ExpenseHeadModel() {}

    public ExpenseHeadModel(String id, String exp_category) {
        this.id = id;
        this.exp_category = exp_category;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExpCategory() {
        return exp_category;
    }

    public void setExpCategory(String exp_category) {
        this.exp_category = exp_category;
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
        return exp_category != null ? exp_category : "";
    }
}

