package com.qdocs.ssre241123.model;

/**
 * Model class for Role
 * Used for parsing Roles List API response
 */
public class RoleModel {
    private String id;
    private String name;
    private String slug;
    private String is_system;
    private String is_superadmin;
    private String is_active;
    private String created_at;

    // Constructors
    public RoleModel() {}

    public RoleModel(String id, String name) {
        this.id = id;
        this.name = name;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getIsSystem() {
        return is_system;
    }

    public void setIsSystem(String is_system) {
        this.is_system = is_system;
    }

    public String getIsSuperadmin() {
        return is_superadmin;
    }

    public void setIsSuperadmin(String is_superadmin) {
        this.is_superadmin = is_superadmin;
    }

    public String getIsActive() {
        return is_active;
    }

    public void setIsActive(String is_active) {
        this.is_active = is_active;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return name != null ? name : "";
    }
}

