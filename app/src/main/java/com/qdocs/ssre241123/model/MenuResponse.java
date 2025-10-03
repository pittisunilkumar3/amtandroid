package com.qdocs.ssre241123.model;

import java.util.List;

public class MenuResponse {
    private int status;
    private String message;
    private MenuData data;

    // Constructors
    public MenuResponse() {}

    // Getters and Setters
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public MenuData getData() { return data; }
    public void setData(MenuData data) { this.data = data; }

    // Inner class for menu data
    public static class MenuData {
        private int staff_id;
        private StaffInfo staff_info;
        private Role role;
        private List<MenuItem> menus;
        private int total_menus;
        private String timestamp;

        // Constructors
        public MenuData() {}

        // Getters and Setters
        public int getStaffId() { return staff_id; }
        public void setStaffId(int staff_id) { this.staff_id = staff_id; }

        public StaffInfo getStaffInfo() { return staff_info; }
        public void setStaffInfo(StaffInfo staff_info) { this.staff_info = staff_info; }

        public Role getRole() { return role; }
        public void setRole(Role role) { this.role = role; }

        public List<MenuItem> getMenus() { return menus; }
        public void setMenus(List<MenuItem> menus) { this.menus = menus; }

        public int getTotalMenus() { return total_menus; }
        public void setTotalMenus(int total_menus) { this.total_menus = total_menus; }

        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    }

    public static class StaffInfo {
        private int id;
        private String name;
        private String surname;
        private String employee_id;
        private String full_name;

        // Constructors
        public StaffInfo() {}

        // Getters and Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getSurname() { return surname; }
        public void setSurname(String surname) { this.surname = surname; }

        public String getEmployeeId() { return employee_id; }
        public void setEmployeeId(String employee_id) { this.employee_id = employee_id; }

        public String getFullName() { return full_name; }
        public void setFullName(String full_name) { this.full_name = full_name; }
    }

    public static class Role {
        private int id;
        private String name;
        private String slug;
        private boolean is_superadmin;

        // Constructors
        public Role() {}

        // Getters and Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getSlug() { return slug; }
        public void setSlug(String slug) { this.slug = slug; }

        public boolean getIsSuperadmin() { return is_superadmin; }
        public void setIsSuperadmin(boolean is_superadmin) { this.is_superadmin = is_superadmin; }
    }
}
