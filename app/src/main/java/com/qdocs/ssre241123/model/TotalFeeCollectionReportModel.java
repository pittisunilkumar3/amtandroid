package com.qdocs.ssre241123.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class for Total Fee Collection Report
 * Represents fee collection data with fee type breakdown
 */
public class TotalFeeCollectionReportModel {
    
    // Collection record fields
    private String id;
    private String invoiceNo;
    private String admissionNo;
    private String studentName;
    private String className;
    private String sectionName;
    private String fatherName;
    private String mobileNo;
    private String feeType;
    private String feeCode;
    private double amount;
    private double fine;
    private double discount;
    private double netAmount;
    private String paymentMode;
    private String date;
    private String collectedBy;
    private String note;
    private String type; // "fees", "other_fees", or "transport_fees"
    
    // For grouped data
    private String groupName;
    private List<TotalFeeCollectionReportModel> groupedRecords;
    private double subtotal;
    
    public TotalFeeCollectionReportModel() {
        this.groupedRecords = new ArrayList<>();
    }
    
    // Getters and Setters
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getInvoiceNo() {
        return invoiceNo;
    }
    
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
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
    
    public String getFatherName() {
        return fatherName;
    }
    
    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }
    
    public String getMobileNo() {
        return mobileNo;
    }
    
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
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
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public double getFine() {
        return fine;
    }
    
    public void setFine(double fine) {
        this.fine = fine;
    }
    
    public double getDiscount() {
        return discount;
    }
    
    public void setDiscount(double discount) {
        this.discount = discount;
    }
    
    public double getNetAmount() {
        return netAmount;
    }
    
    public void setNetAmount(double netAmount) {
        this.netAmount = netAmount;
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
    
    public String getCollectedBy() {
        return collectedBy;
    }
    
    public void setCollectedBy(String collectedBy) {
        this.collectedBy = collectedBy;
    }
    
    public String getNote() {
        return note;
    }
    
    public void setNote(String note) {
        this.note = note;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getGroupName() {
        return groupName;
    }
    
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    public List<TotalFeeCollectionReportModel> getGroupedRecords() {
        return groupedRecords;
    }
    
    public void setGroupedRecords(List<TotalFeeCollectionReportModel> groupedRecords) {
        this.groupedRecords = groupedRecords;
    }
    
    public double getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    // Helper methods
    
    /**
     * Get formatted date (e.g., "Sep 01, 2025")
     */
    public String getFormattedDate() {
        if (date == null || date.isEmpty()) {
            return "";
        }
        
        try {
            // Parse date in format YYYY-MM-DD
            String[] parts = date.split("-");
            if (parts.length == 3) {
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int day = Integer.parseInt(parts[2]);
                
                String[] monthNames = {
                    "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
                };
                
                String monthName = monthNames[month - 1];
                return String.format("%s %02d, %d", monthName, day, year);
            }
        } catch (Exception e) {
            // Return original date if parsing fails
        }
        
        return date;
    }
    
    /**
     * Get full class name with section
     */
    public String getFullClassName() {
        if (className != null && sectionName != null && !sectionName.isEmpty()) {
            return className + " - " + sectionName;
        } else if (className != null) {
            return className;
        }
        return "";
    }
    
    /**
     * Get type label for display
     */
    public String getTypeLabel() {
        if ("other_fees".equals(type)) {
            return "Other Fees";
        } else if ("transport_fees".equals(type)) {
            return "Transport Fees";
        } else {
            return "Regular Fees";
        }
    }
    
    /**
     * Check if this is a grouped record
     */
    public boolean isGrouped() {
        return groupName != null && !groupName.isEmpty();
    }
    
    @Override
    public String toString() {
        return "TotalFeeCollectionReportModel{" +
                "invoiceNo='" + invoiceNo + '\'' +
                ", studentName='" + studentName + '\'' +
                ", feeType='" + feeType + '\'' +
                ", amount=" + amount +
                ", netAmount=" + netAmount +
                ", date='" + date + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
    
    /**
     * Inner class for Fee Type Breakdown
     */
    public static class FeeTypeBreakdown {
        private String feeType;
        private int count;
        private double total;
        
        public FeeTypeBreakdown() {
        }
        
        public FeeTypeBreakdown(String feeType, int count, double total) {
            this.feeType = feeType;
            this.count = count;
            this.total = total;
        }
        
        public String getFeeType() {
            return feeType;
        }
        
        public void setFeeType(String feeType) {
            this.feeType = feeType;
        }
        
        public int getCount() {
            return count;
        }
        
        public void setCount(int count) {
            this.count = count;
        }
        
        public double getTotal() {
            return total;
        }
        
        public void setTotal(double total) {
            this.total = total;
        }
    }
}

