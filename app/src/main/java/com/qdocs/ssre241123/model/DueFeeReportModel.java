package com.qdocs.ssre241123.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class for Due Fee Report (Total Balance Fee Statement)
 * Represents a student with their due fees information
 */
public class DueFeeReportModel {
    
    // Student Information
    private String studentId;
    private String admissionNo;
    private String firstname;
    private String middlename;
    private String lastname;
    private String className;
    private String sectionName;
    private String fatherName;
    private String mobileno;
    private String guardianName;
    private String guardianPhone;
    
    // Fee Summary
    private String totalAmount;
    private String totalPaid;
    private String totalBalance;
    private String totalFine;
    private String totalDiscount;
    
    // Fee Details List
    private List<FeeDetail> feesList;
    private List<FeeDetail> transportFeesList;
    
    // Constructor
    public DueFeeReportModel() {
        this.feesList = new ArrayList<>();
        this.transportFeesList = new ArrayList<>();
    }
    
    // Getters and Setters
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
    
    public String getFatherName() {
        return fatherName;
    }
    
    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }
    
    public String getMobileno() {
        return mobileno;
    }
    
    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }
    
    public String getGuardianName() {
        return guardianName;
    }
    
    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }
    
    public String getGuardianPhone() {
        return guardianPhone;
    }
    
    public void setGuardianPhone(String guardianPhone) {
        this.guardianPhone = guardianPhone;
    }
    
    public String getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String getTotalPaid() {
        return totalPaid;
    }
    
    public void setTotalPaid(String totalPaid) {
        this.totalPaid = totalPaid;
    }
    
    public String getTotalBalance() {
        return totalBalance;
    }
    
    public void setTotalBalance(String totalBalance) {
        this.totalBalance = totalBalance;
    }
    
    public String getTotalFine() {
        return totalFine;
    }
    
    public void setTotalFine(String totalFine) {
        this.totalFine = totalFine;
    }
    
    public String getTotalDiscount() {
        return totalDiscount;
    }
    
    public void setTotalDiscount(String totalDiscount) {
        this.totalDiscount = totalDiscount;
    }
    
    public List<FeeDetail> getFeesList() {
        return feesList;
    }
    
    public void setFeesList(List<FeeDetail> feesList) {
        this.feesList = feesList;
    }
    
    public List<FeeDetail> getTransportFeesList() {
        return transportFeesList;
    }
    
    public void setTransportFeesList(List<FeeDetail> transportFeesList) {
        this.transportFeesList = transportFeesList;
    }
    
    // Helper Methods
    
    /**
     * Get full name of student
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
     * Get class and section combined
     */
    public String getClassSection() {
        if (className != null && sectionName != null) {
            return className + " - " + sectionName;
        } else if (className != null) {
            return className;
        } else if (sectionName != null) {
            return sectionName;
        }
        return "";
    }
    
    /**
     * Check if student has due balance
     */
    public boolean hasDueBalance() {
        if (totalBalance != null && !totalBalance.isEmpty()) {
            try {
                double balance = Double.parseDouble(totalBalance);
                return balance > 0;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }
    
    /**
     * Get total number of fee items
     */
    public int getTotalFeeItems() {
        return feesList.size() + transportFeesList.size();
    }
    
    /**
     * Inner class for Fee Detail
     */
    public static class FeeDetail {
        private String feeType;
        private String feeCode;
        private String dueDate;
        private String amount;
        private String paidAmount;
        private String balanceAmount;
        private String fineAmount;
        private String discountAmount;
        private String status;
        
        // Constructor
        public FeeDetail() {
        }
        
        // Getters and Setters
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
        
        public String getDueDate() {
            return dueDate;
        }
        
        public void setDueDate(String dueDate) {
            this.dueDate = dueDate;
        }
        
        public String getAmount() {
            return amount;
        }
        
        public void setAmount(String amount) {
            this.amount = amount;
        }
        
        public String getPaidAmount() {
            return paidAmount;
        }
        
        public void setPaidAmount(String paidAmount) {
            this.paidAmount = paidAmount;
        }
        
        public String getBalanceAmount() {
            return balanceAmount;
        }
        
        public void setBalanceAmount(String balanceAmount) {
            this.balanceAmount = balanceAmount;
        }
        
        public String getFineAmount() {
            return fineAmount;
        }
        
        public void setFineAmount(String fineAmount) {
            this.fineAmount = fineAmount;
        }
        
        public String getDiscountAmount() {
            return discountAmount;
        }
        
        public void setDiscountAmount(String discountAmount) {
            this.discountAmount = discountAmount;
        }
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
    }
}

