package com.qdocs.ssre241123.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class for Student Academic Report
 * Represents individual student with their fee information
 */
public class StudentAcademicReportModel {
    
    // Student Information
    private String id;
    private String admissionNo;
    private String firstname;
    private String middlename;
    private String lastname;
    private String className;
    private String section;
    private String rollNo;
    private String fatherName;
    
    // Fee Details List
    private List<FeeDetail> fees;
    
    // Constructor
    public StudentAcademicReportModel() {
        this.fees = new ArrayList<>();
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
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
    
    public String getSection() {
        return section;
    }
    
    public void setSection(String section) {
        this.section = section;
    }
    
    public String getRollNo() {
        return rollNo;
    }
    
    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }
    
    public String getFatherName() {
        return fatherName;
    }
    
    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }
    
    public List<FeeDetail> getFees() {
        return fees;
    }
    
    public void setFees(List<FeeDetail> fees) {
        this.fees = fees;
    }
    
    // Helper method to get full name
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
    
    // Helper method to get class with section
    public String getClassSection() {
        if (className != null && section != null) {
            return className + " - " + section;
        } else if (className != null) {
            return className;
        }
        return "";
    }
    
    /**
     * Inner class for Fee Details
     */
    public static class FeeDetail {
        private String id;
        private String name;
        private String amount;
        private String amountPaid;
        private String amountDiscount;
        private String amountFine;
        
        // Constructor
        public FeeDetail() {
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
        
        public String getAmount() {
            return amount;
        }
        
        public void setAmount(String amount) {
            this.amount = amount;
        }
        
        public String getAmountPaid() {
            return amountPaid;
        }
        
        public void setAmountPaid(String amountPaid) {
            this.amountPaid = amountPaid;
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
        
        // Helper method to calculate balance
        public double getBalance() {
            try {
                double amt = amount != null ? Double.parseDouble(amount) : 0.0;
                double paid = amountPaid != null ? Double.parseDouble(amountPaid) : 0.0;
                double discount = amountDiscount != null ? Double.parseDouble(amountDiscount) : 0.0;
                double fine = amountFine != null ? Double.parseDouble(amountFine) : 0.0;
                
                return amt - paid - discount + fine;
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        
        // Helper method to get amount as double
        public double getAmountDouble() {
            try {
                return amount != null ? Double.parseDouble(amount) : 0.0;
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        
        // Helper method to get paid amount as double
        public double getAmountPaidDouble() {
            try {
                return amountPaid != null ? Double.parseDouble(amountPaid) : 0.0;
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        
        // Helper method to get discount as double
        public double getAmountDiscountDouble() {
            try {
                return amountDiscount != null ? Double.parseDouble(amountDiscount) : 0.0;
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        
        // Helper method to get fine as double
        public double getAmountFineDouble() {
            try {
                return amountFine != null ? Double.parseDouble(amountFine) : 0.0;
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
    }
}

