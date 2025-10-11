package com.qdocs.ssre241123.model;

/**
 * Model class for Other Collection Report
 * Represents fee collection data for "other" fee types (hostel, library, etc.)
 * Updated to match the new API specification
 */
public class OtherCollectionReportModel {

    private String id;
    private String studentFeesMasterId;
    private String feeGroupsFeetypeId;
    private String amount;
    private String amountDiscount;
    private String amountFine;
    private String description;
    private String paymentMode;
    private String receivedBy;
    private String receivedByName;
    private String receivedByEmployeeId;
    private String date;
    private String invNo;
    private String createdAt;
    private String firstname;
    private String middlename;
    private String lastname;
    private String admissionNo;
    private String studentId;
    private String classId;
    private String className;
    private String sectionId;
    private String section;
    private String type;
    private String code;
    private String name;
    private String studentSessionId;
    private String isSystem;

    // Constructor
    public OtherCollectionReportModel() {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentFeesMasterId() {
        return studentFeesMasterId;
    }

    public void setStudentFeesMasterId(String studentFeesMasterId) {
        this.studentFeesMasterId = studentFeesMasterId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmountDiscount() {
        return amountDiscount;
    }

    public void setAmountDiscount(String amountDiscount) {
        this.amountDiscount = amountDiscount;
    }

    public String getAmountFine() {
        return amountFine;
    }

    public void setAmountFine(String amountFine) {
        this.amountFine = amountFine;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAdmissionNo() {
        return admissionNo;
    }

    public void setAdmissionNo(String admissionNo) {
        this.admissionNo = admissionNo;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFeeGroupsFeetypeId() {
        return feeGroupsFeetypeId;
    }

    public void setFeeGroupsFeetypeId(String feeGroupsFeetypeId) {
        this.feeGroupsFeetypeId = feeGroupsFeetypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReceivedByName() {
        return receivedByName;
    }

    public void setReceivedByName(String receivedByName) {
        this.receivedByName = receivedByName;
    }

    public String getReceivedByEmployeeId() {
        return receivedByEmployeeId;
    }

    public void setReceivedByEmployeeId(String receivedByEmployeeId) {
        this.receivedByEmployeeId = receivedByEmployeeId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInvNo() {
        return invNo;
    }

    public void setInvNo(String invNo) {
        this.invNo = invNo;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getStudentSessionId() {
        return studentSessionId;
    }

    public void setStudentSessionId(String studentSessionId) {
        this.studentSessionId = studentSessionId;
    }

    public String getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
    }

    // Helper methods
    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        if (firstname != null && !firstname.isEmpty()) {
            fullName.append(firstname);
        }
        if (middlename != null && !middlename.isEmpty()) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(middlename);
        }
        if (lastname != null && !lastname.isEmpty()) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(lastname);
        }
        return fullName.toString();
    }

    public String getClassSection() {
        if (className != null && section != null) {
            return className + " - " + section;
        } else if (className != null) {
            return className;
        }
        return "";
    }

    public double getTotalAmount() {
        try {
            double amt = amount != null ? Double.parseDouble(amount) : 0.0;
            double discount = amountDiscount != null ? Double.parseDouble(amountDiscount) : 0.0;
            double fine = amountFine != null ? Double.parseDouble(amountFine) : 0.0;
            return amt - discount + fine;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public String getReceivedByDisplayName() {
        if (receivedByName != null && !receivedByName.isEmpty()) {
            if (receivedByEmployeeId != null && !receivedByEmployeeId.isEmpty()) {
                return receivedByName + " (" + receivedByEmployeeId + ")";
            }
            return receivedByName;
        }
        return receivedBy != null ? receivedBy : "-";
    }
}

