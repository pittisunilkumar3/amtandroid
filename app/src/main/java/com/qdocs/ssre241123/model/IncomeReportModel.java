package com.qdocs.ssre241123.model;

/**
 * Model class for Income Report
 * Represents a single income record
 */
public class IncomeReportModel {
    
    private String id;
    private String name;
    private String invoiceNo;
    private String date;
    private String amount;
    private String incomeHead;
    private String incomeHeadId;
    private String note;
    private String documents;

    // Constructors
    public IncomeReportModel() {
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

    public String getIncomeHead() {
        return incomeHead;
    }

    public void setIncomeHead(String incomeHead) {
        this.incomeHead = incomeHead;
    }

    public String getIncomeHeadId() {
        return incomeHeadId;
    }

    public void setIncomeHeadId(String incomeHeadId) {
        this.incomeHeadId = incomeHeadId;
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
        return "IncomeReportModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", invoiceNo='" + invoiceNo + '\'' +
                ", date='" + date + '\'' +
                ", amount='" + amount + '\'' +
                ", incomeHead='" + incomeHead + '\'' +
                ", incomeHeadId='" + incomeHeadId + '\'' +
                ", note='" + note + '\'' +
                ", documents='" + documents + '\'' +
                '}';
    }
}

