package com.qdocs.ssre241123.models;

public class TeacherLeaveRecord {
    private String id;
    private String staffId;
    private String leaveTypeId;
    private String leaveFrom;
    private String leaveTo;
    private String leaveDays;
    private String employeeRemark;
    private String adminRemark;
    private String status;
    private String appliedBy;
    private String documentFile;
    private String date;
    private String createdAt;
    private String type;
    private String name;
    private String surname;
    private String employeeId;

    public TeacherLeaveRecord() {
    }

    public TeacherLeaveRecord(String id, String staffId, String leaveTypeId, String leaveFrom, 
                             String leaveTo, String leaveDays, String employeeRemark, 
                             String adminRemark, String status, String date, String type) {
        this.id = id;
        this.staffId = staffId;
        this.leaveTypeId = leaveTypeId;
        this.leaveFrom = leaveFrom;
        this.leaveTo = leaveTo;
        this.leaveDays = leaveDays;
        this.employeeRemark = employeeRemark;
        this.adminRemark = adminRemark;
        this.status = status;
        this.date = date;
        this.type = type;
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

    public String getLeaveTypeId() {
        return leaveTypeId;
    }

    public void setLeaveTypeId(String leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }

    public String getLeaveFrom() {
        return leaveFrom;
    }

    public void setLeaveFrom(String leaveFrom) {
        this.leaveFrom = leaveFrom;
    }

    public String getLeaveTo() {
        return leaveTo;
    }

    public void setLeaveTo(String leaveTo) {
        this.leaveTo = leaveTo;
    }

    public String getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(String leaveDays) {
        this.leaveDays = leaveDays;
    }

    public String getEmployeeRemark() {
        return employeeRemark;
    }

    public void setEmployeeRemark(String employeeRemark) {
        this.employeeRemark = employeeRemark;
    }

    public String getAdminRemark() {
        return adminRemark;
    }

    public void setAdminRemark(String adminRemark) {
        this.adminRemark = adminRemark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppliedBy() {
        return appliedBy;
    }

    public void setAppliedBy(String appliedBy) {
        this.appliedBy = appliedBy;
    }

    public String getDocumentFile() {
        return documentFile;
    }

    public void setDocumentFile(String documentFile) {
        this.documentFile = documentFile;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    @Override
    public String toString() {
        return "TeacherLeaveRecord{" +
                "id='" + id + '\'' +
                ", staffId='" + staffId + '\'' +
                ", leaveTypeId='" + leaveTypeId + '\'' +
                ", leaveFrom='" + leaveFrom + '\'' +
                ", leaveTo='" + leaveTo + '\'' +
                ", leaveDays='" + leaveDays + '\'' +
                ", employeeRemark='" + employeeRemark + '\'' +
                ", adminRemark='" + adminRemark + '\'' +
                ", status='" + status + '\'' +
                ", appliedBy='" + appliedBy + '\'' +
                ", documentFile='" + documentFile + '\'' +
                ", date='" + date + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", employeeId='" + employeeId + '\'' +
                '}';
    }
}
