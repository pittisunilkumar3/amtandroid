package com.qdocs.ssre241123.model;

/**
 * Model class for Collection Report
 * Represents fee collection data from the collection-report API
 */
public class CollectionReportModel {

    // Basic IDs
    private String id;
    private String studentFeesMasterId;
    private String feeGroupsFeetypeId;
    private String studentId;
    private String studentSessionId;
    
    // Student Information
    private String admissionNo;
    private String firstname;
    private String middlename;
    private String lastname;
    
    // Class Information
    private String classId;
    private String className;
    private String sectionId;
    private String section;
    
    // Fee Information
    private String name;        // Fee group name
    private String type;        // Fee type name
    private String code;        // Fee type code
    private String isSystem;
    
    // Payment Information
    private String amount;
    private String amountDiscount;
    private String amountFine;
    private String description;
    private String paymentMode;
    private String date;
    private String invNo;
    private String receivedBy;
    
    // Constructor
    public CollectionReportModel() {
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

    public String getFeeGroupsFeetypeId() {
        return feeGroupsFeetypeId;
    }

    public void setFeeGroupsFeetypeId(String feeGroupsFeetypeId) {
        this.feeGroupsFeetypeId = feeGroupsFeetypeId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentSessionId() {
        return studentSessionId;
    }

    public void setStudentSessionId(String studentSessionId) {
        this.studentSessionId = studentSessionId;
    }

    public String getAdmissionNo() {
        return admissionNo;
    }

    public void setAdmissionNo(String admissionNo) {
        this.admissionNo = admissionNo;
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

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
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

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    // Helper methods
    
    /**
     * Get full student name
     */
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

    /**
     * Get formatted class and section
     */
    public String getClassSection() {
        if (className != null && section != null && !section.isEmpty()) {
            return className + " - " + section;
        } else if (className != null) {
            return className;
        }
        return "";
    }

    /**
     * Calculate total amount (amount - discount + fine)
     */
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
}

