package com.qdocs.ssre241123.model;

/**
 * Model class for Expense Report
 * Represents a single expense record
 */
public class ExpenseReportModel {
    
    private String id;
    private String name;
    private String invoiceNo;
    private String date;
    private String amount;
    private String expCategory;
    private String expHeadId;
    private String note;
    private String documents;

    // Constructors
    public ExpenseReportModel() {
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

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getExpCategory() {
        return expCategory;
    }

    public void setExpCategory(String expCategory) {
        this.expCategory = expCategory;
    }

    public String getExpHeadId() {
        return expHeadId;
    }

    public void setExpHeadId(String expHeadId) {
        this.expHeadId = expHeadId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    @Override
    public String toString() {
        return "ExpenseReportModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", invoiceNo='" + invoiceNo + '\'' +
                ", date='" + date + '\'' +
                ", amount='" + amount + '\'' +
                ", expCategory='" + expCategory + '\'' +
                ", expHeadId='" + expHeadId + '\'' +
                ", note='" + note + '\'' +
                ", documents='" + documents + '\'' +
                '}';
    }
}

