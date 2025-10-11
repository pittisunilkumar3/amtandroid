package com.qdocs.ssre241123.model;

/**
 * Model class for Payroll Report
 * Represents a single payroll record
 */
public class PayrollReportModel {
    
    private String id;
    private String employeeId;
    private String name;
    private String role;
    private String designation;
    private String month;
    private String year;
    private String basicSalary;
    private String earnings;
    private String deductions;
    private String grossSalary;
    private String taxAmount;
    private String netSalary;
    private String paymentMode;
    private String paymentDate;
    private String status;
    private String remarks;

    // Constructors
    public PayrollReportModel() {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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

    public String getEarnings() {
        return earnings;
    }

    public void setEarnings(String earnings) {
        this.earnings = earnings;
    }

    public String getDeductions() {
        return deductions;
    }

    public void setDeductions(String deductions) {
        this.deductions = deductions;
    }

    public String getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(String grossSalary) {
        this.grossSalary = grossSalary;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(String netSalary) {
        this.netSalary = netSalary;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "PayrollReportModel{" +
                "id='" + id + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", designation='" + designation + '\'' +
                ", month='" + month + '\'' +
                ", year='" + year + '\'' +
                ", basicSalary='" + basicSalary + '\'' +
                ", earnings='" + earnings + '\'' +
                ", deductions='" + deductions + '\'' +
                ", grossSalary='" + grossSalary + '\'' +
                ", taxAmount='" + taxAmount + '\'' +
                ", netSalary='" + netSalary + '\'' +
                ", paymentMode='" + paymentMode + '\'' +
                ", paymentDate='" + paymentDate + '\'' +
                ", status='" + status + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}

