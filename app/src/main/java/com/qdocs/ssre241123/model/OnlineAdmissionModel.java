package com.qdocs.ssre241123.model;

public class OnlineAdmissionModel {
    private String id;
    private String referenceNo;
    private String admissionNo;
    private String admissionDate;
    private String fullName;
    private String firstname;
    private String middlename;
    private String lastname;
    private String dob;
    private String gender;
    private String email;
    private String mobileno;
    private String fatherName;
    private String fatherPhone;
    private String motherName;
    private String motherPhone;
    private String guardianName;
    private String guardianPhone;
    private String currentAddress;
    private String permanentAddress;
    private String classId;
    private String className;
    private String sectionId;
    private String sectionName;
    private String category;
    private String houseName;
    private String bloodGroup;
    private String religion;
    private String cast;
    private String isEnroll;
    private String formStatus;
    private String paidStatus;
    private String createdAt;
    private String updatedAt;

    // Constructor
    public OnlineAdmissionModel() {
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

    public String getAdmissionNo() {
        return admissionNo;
    }

    public void setAdmissionNo(String admissionNo) {
        this.admissionNo = admissionNo;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getFatherPhone() {
        return fatherPhone;
    }

    public void setFatherPhone(String fatherPhone) {
        this.fatherPhone = fatherPhone;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getMotherPhone() {
        return motherPhone;
    }

    public void setMotherPhone(String motherPhone) {
        this.motherPhone = motherPhone;
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

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
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

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getIsEnroll() {
        return isEnroll;
    }

    public void setIsEnroll(String isEnroll) {
        this.isEnroll = isEnroll;
    }

    public String getFormStatus() {
        return formStatus;
    }

    public void setFormStatus(String formStatus) {
        this.formStatus = formStatus;
    }

    public String getPaidStatus() {
        return paidStatus;
    }

    public void setPaidStatus(String paidStatus) {
        this.paidStatus = paidStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Helper methods
    public String getClassSection() {
        if (className != null && sectionName != null) {
            return className + " - " + sectionName;
        } else if (className != null) {
            return className;
        } else if (sectionName != null) {
            return sectionName;
        }
        return "N/A";
    }

    public String getEnrollmentStatus() {
        if ("1".equals(isEnroll)) {
            return "Enrolled";
        } else if ("0".equals(isEnroll)) {
            return "Not Enrolled";
        }
        return "Unknown";
    }

    public boolean isEnrolled() {
        return "1".equals(isEnroll);
    }

    public String getPaymentStatus() {
        if ("1".equals(paidStatus)) {
            return "Paid";
        } else if ("0".equals(paidStatus)) {
            return "Unpaid";
        }
        return "Unknown";
    }

    public boolean isPaid() {
        return "1".equals(paidStatus);
    }

    public String getFormattedAdmissionDate() {
        if (admissionDate != null && !admissionDate.isEmpty()) {
            return admissionDate;
        }
        return "N/A";
    }

    public String getFormattedDob() {
        if (dob != null && !dob.isEmpty()) {
            return dob;
        }
        return "N/A";
    }

    public String getParentContact() {
        if (fatherPhone != null && !fatherPhone.isEmpty()) {
            return fatherPhone;
        } else if (motherPhone != null && !motherPhone.isEmpty()) {
            return motherPhone;
        } else if (guardianPhone != null && !guardianPhone.isEmpty()) {
            return guardianPhone;
        }
        return "N/A";
    }
}

