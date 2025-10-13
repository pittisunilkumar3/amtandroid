package com.qdocs.ssre241123.model;

/**
 * Model class for Staff Attendance Report
 * Represents individual staff attendance records with complete staff information
 */
public class StaffAttendanceReportModel {
    
    private String id;
    private String staffId;
    private String date;
    private String staffAttendanceTypeId;
    private String remark;
    private String isActive;
    private String name;
    private String surname;
    private String employeeId;
    private String department;
    private String designation;
    private String roleId;
    private String role;
    private String attendanceType;

    public StaffAttendanceReportModel() {
    }

    public StaffAttendanceReportModel(String id, String staffId, String date, String staffAttendanceTypeId,
                                     String remark, String isActive, String name, String surname,
                                     String employeeId, String department, String designation,
                                     String roleId, String role, String attendanceType) {
        this.id = id;
        this.staffId = staffId;
        this.date = date;
        this.staffAttendanceTypeId = staffAttendanceTypeId;
        this.remark = remark;
        this.isActive = isActive;
        this.name = name;
        this.surname = surname;
        this.employeeId = employeeId;
        this.department = department;
        this.designation = designation;
        this.roleId = roleId;
        this.role = role;
        this.attendanceType = attendanceType;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStaffAttendanceTypeId() {
        return staffAttendanceTypeId;
    }

    public void setStaffAttendanceTypeId(String staffAttendanceTypeId) {
        this.staffAttendanceTypeId = staffAttendanceTypeId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }

    /**
     * Get full name (name + surname)
     */
    public String getFullName() {
        if (surname != null && !surname.isEmpty()) {
            return name + " " + surname;
        }
        return name;
    }

    @Override
    public String toString() {
        return "StaffAttendanceReportModel{" +
                "id='" + id + '\'' +
                ", staffId='" + staffId + '\'' +
                ", date='" + date + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", department='" + department + '\'' +
                ", designation='" + designation + '\'' +
                ", role='" + role + '\'' +
                ", attendanceType='" + attendanceType + '\'' +
                '}';
    }
}
