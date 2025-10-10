package com.qdocs.ssre241123.model;

import java.util.List;

/**
 * Model class for Daily Collection Report
 * Represents daily fee collection data
 */
public class DailyCollectionReportModel {
    
    private String date;
    private double amount;
    private int count;
    private List<String> studentFeesDepositeIds;
    private String type; // "fees" or "other_fees"
    
    public DailyCollectionReportModel() {
    }
    
    public DailyCollectionReportModel(String date, double amount, int count, 
                                     List<String> studentFeesDepositeIds, String type) {
        this.date = date;
        this.amount = amount;
        this.count = count;
        this.studentFeesDepositeIds = studentFeesDepositeIds;
        this.type = type;
    }
    
    // Getters and Setters
    
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public int getCount() {
        return count;
    }
    
    public void setCount(int count) {
        this.count = count;
    }
    
    public List<String> getStudentFeesDepositeIds() {
        return studentFeesDepositeIds;
    }
    
    public void setStudentFeesDepositeIds(List<String> studentFeesDepositeIds) {
        this.studentFeesDepositeIds = studentFeesDepositeIds;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    // Helper methods
    
    /**
     * Get formatted amount string
     */
    public String getFormattedAmount() {
        return String.format("%.2f", amount);
    }
    
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
     * Check if this is a zero collection day
     */
    public boolean isZeroCollection() {
        return amount == 0 || count == 0;
    }
    
    /**
     * Get transaction IDs as comma-separated string
     */
    public String getTransactionIdsString() {
        if (studentFeesDepositeIds == null || studentFeesDepositeIds.isEmpty()) {
            return "No transactions";
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < studentFeesDepositeIds.size(); i++) {
            sb.append(studentFeesDepositeIds.get(i));
            if (i < studentFeesDepositeIds.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
    
    /**
     * Get type label for display
     */
    public String getTypeLabel() {
        if ("other_fees".equals(type)) {
            return "Other Fees";
        } else {
            return "Regular Fees";
        }
    }
    
    @Override
    public String toString() {
        return "DailyCollectionReportModel{" +
                "date='" + date + '\'' +
                ", amount=" + amount +
                ", count=" + count +
                ", type='" + type + '\'' +
                ", transactionCount=" + (studentFeesDepositeIds != null ? studentFeesDepositeIds.size() : 0) +
                '}';
    }
}

