package com.qdocs.ssre241123.model;

/**
 * Model class for Online Fees Report
 * Represents a single online fee payment record
 */
public class OnlineFeesReportModel {
    
    private String id;
    private String studentId;
    private String admissionNo;
    private String studentName;
    private String className;
    private String sectionName;
    private String feeGroup;
    private String feeType;
    private String feeCode;
    private String amount;
    private String paymentDate;
    private String paymentMode;

    // Constructors
    public OnlineFeesReportModel() {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getAdmissionNo() {
        return admissionNo;
    }

    public void setAdmissionNo(String admissionNo) {
        this.admissionNo = admissionNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getFeeGroup() {
        return feeGroup;
    }

    public void setFeeGroup(String feeGroup) {
        this.feeGroup = feeGroup;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getFeeCode() {
        return feeCode;
    }

    public void setFeeCode(String feeCode) {
        this.feeCode = feeCode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    @Override
    public String toString() {
        return "OnlineFeesReportModel{" +
                "id='" + id + '\'' +
                ", studentId='" + studentId + '\'' +
                ", admissionNo='" + admissionNo + '\'' +
                ", studentName='" + studentName + '\'' +
                ", className='" + className + '\'' +
                ", sectionName='" + sectionName + '\'' +
                ", feeGroup='" + feeGroup + '\'' +
                ", feeType='" + feeType + '\'' +
                ", feeCode='" + feeCode + '\'' +
                ", amount='" + amount + '\'' +
                ", paymentDate='" + paymentDate + '\'' +
                ", paymentMode='" + paymentMode + '\'' +
                '}';
    }
}

