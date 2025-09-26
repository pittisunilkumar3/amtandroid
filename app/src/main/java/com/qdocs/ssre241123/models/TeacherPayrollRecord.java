package com.qdocs.ssre241123.models;

public class TeacherPayrollRecord {
    private String id;
    private String month;
    private String year;
    private String basicSalary;
    private String allowances;
    private String deductions;
    private String netSalary;
    private String status;
    private String paymentDate;
    private String createdAt;
    private String mode;
    private String earnings;
    private String tax;

    public TeacherPayrollRecord() {
    }

    public TeacherPayrollRecord(String id, String month, String year, String basicSalary, 
                               String allowances, String deductions, String netSalary, 
                               String status, String paymentDate, String createdAt) {
        this.id = id;
        this.month = month;
        this.year = year;
        this.basicSalary = basicSalary;
        this.allowances = allowances;
        this.deductions = deductions;
        this.netSalary = netSalary;
        this.status = status;
        this.paymentDate = paymentDate;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(String basicSalary) {
        this.basicSalary = basicSalary;
    }

    public String getAllowances() {
        return allowances;
    }

    public void setAllowances(String allowances) {
        this.allowances = allowances;
    }

    public String getDeductions() {
        return deductions;
    }

    public void setDeductions(String deductions) {
        this.deductions = deductions;
    }

    public String getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(String netSalary) {
        this.netSalary = netSalary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getEarnings() {
        return earnings;
    }

    public void setEarnings(String earnings) {
        this.earnings = earnings;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    @Override
    public String toString() {
        return "TeacherPayrollRecord{" +
                "id='" + id + '\'' +
                ", month='" + month + '\'' +
                ", year='" + year + '\'' +
                ", basicSalary='" + basicSalary + '\'' +
                ", allowances='" + allowances + '\'' +
                ", deductions='" + deductions + '\'' +
                ", netSalary='" + netSalary + '\'' +
                ", status='" + status + '\'' +
                ", paymentDate='" + paymentDate + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", mode='" + mode + '\'' +
                ", earnings='" + earnings + '\'' +
                ", tax='" + tax + '\'' +
                '}';
    }
}
