package com.qdocs.ssre241123.model;

/**
 * Model class for Online Admission Fee Collection Report
 * Represents a single online admission payment record
 */
public class OnlineAdmissionReportModel {
    
    private String id;
    private String referenceNo;
    private String firstname;
    private String middlename;
    private String lastname;
    private String mobileno;
    private String email;
    private String className;
    private String sectionName;
    private String category;
    private String date;
    private String paidAmount;
    private String paymentMode;
    private String paymentId;
    private String hostelName;
    private String roomType;
    private String roomNo;
    private String routeTitle;
    private String vehicleNo;
    private String houseName;
    private String onlineAdmissionId;

    // Constructors
    public OnlineAdmissionReportModel() {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
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

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getHostelName() {
        return hostelName;
    }

    public void setHostelName(String hostelName) {
        this.hostelName = hostelName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getRouteTitle() {
        return routeTitle;
    }

    public void setRouteTitle(String routeTitle) {
        this.routeTitle = routeTitle;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public String getOnlineAdmissionId() {
        return onlineAdmissionId;
    }

    public void setOnlineAdmissionId(String onlineAdmissionId) {
        this.onlineAdmissionId = onlineAdmissionId;
    }

    /**
     * Get full name of the applicant
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

    @Override
    public String toString() {
        return "OnlineAdmissionReportModel{" +
                "id='" + id + '\'' +
                ", referenceNo='" + referenceNo + '\'' +
                ", firstname='" + firstname + '\'' +
                ", middlename='" + middlename + '\'' +
                ", lastname='" + lastname + '\'' +
                ", mobileno='" + mobileno + '\'' +
                ", email='" + email + '\'' +
                ", className='" + className + '\'' +
                ", sectionName='" + sectionName + '\'' +
                ", category='" + category + '\'' +
                ", date='" + date + '\'' +
                ", paidAmount='" + paidAmount + '\'' +
                ", paymentMode='" + paymentMode + '\'' +
                ", paymentId='" + paymentId + '\'' +
                ", hostelName='" + hostelName + '\'' +
                ", roomType='" + roomType + '\'' +
                ", roomNo='" + roomNo + '\'' +
                ", routeTitle='" + routeTitle + '\'' +
                ", vehicleNo='" + vehicleNo + '\'' +
                ", houseName='" + houseName + '\'' +
                ", onlineAdmissionId='" + onlineAdmissionId + '\'' +
                '}';
    }
}

