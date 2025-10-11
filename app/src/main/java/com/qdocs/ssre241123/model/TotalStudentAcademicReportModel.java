package com.qdocs.ssre241123.model;

/**
 * Model class for Total Student Academic Report
 * Represents individual student with their fee summary
 * Based on API: /api/total-student-academic-report/filter
 */
public class TotalStudentAcademicReportModel {
    
    // Student Information
    private String name;
    private String className;
    private String section;
    private String admissionNo;
    private String rollNo;
    private String fatherName;
    
    // Fee Summary
    private String totalFee;
    private String deposit;
    private String discount;
    private String fine;
    private String balance;
    
    // Constructor
    public TotalStudentAcademicReportModel() {
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
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
    
    public String getAdmissionNo() {
        return admissionNo;
    }
    
    public void setAdmissionNo(String admissionNo) {
        this.admissionNo = admissionNo;
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
    
    public String getTotalFee() {
        return totalFee;
    }
    
    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }
    
    public String getDeposit() {
        return deposit;
    }
    
    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }
    
    public String getDiscount() {
        return discount;
    }
    
    public void setDiscount(String discount) {
        this.discount = discount;
    }
    
    public String getFine() {
        return fine;
    }
    
    public void setFine(String fine) {
        this.fine = fine;
    }
    
    public String getBalance() {
        return balance;
    }
    
    public void setBalance(String balance) {
        this.balance = balance;
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
    
    // Helper methods to get amounts as double
    // Note: API returns amounts with comma separators (e.g., "51,000.00")
    // We need to remove commas before parsing
    public double getTotalFeeDouble() {
        try {
            if (totalFee != null) {
                String cleanValue = totalFee.replace(",", "");
                return Double.parseDouble(cleanValue);
            }
            return 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public double getDepositDouble() {
        try {
            if (deposit != null) {
                String cleanValue = deposit.replace(",", "");
                return Double.parseDouble(cleanValue);
            }
            return 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public double getDiscountDouble() {
        try {
            if (discount != null) {
                String cleanValue = discount.replace(",", "");
                return Double.parseDouble(cleanValue);
            }
            return 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public double getFineDouble() {
        try {
            if (fine != null) {
                String cleanValue = fine.replace(",", "");
                return Double.parseDouble(cleanValue);
            }
            return 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public double getBalanceDouble() {
        try {
            if (balance != null) {
                String cleanValue = balance.replace(",", "");
                return Double.parseDouble(cleanValue);
            }
            return 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}

