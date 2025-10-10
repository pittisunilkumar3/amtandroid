package com.qdocs.ssre241123.model;

/**
 * Model class for Admission Report
 * Represents student admission information from the Admission Report API
 */
public class AdmissionReportModel {
    private String id;
    private String admissionNo;
    private String admissionDate;
    private String firstname;
    private String middlename;
    private String lastname;
    private String classId;
    private String className;
    private String sectionId;
    private String sectionName;
    private String sessionId;
    private String sessionName;
    private String mobileno;
    private String guardianName;
    private String guardianRelation;
    private String guardianPhone;
    private String isActive;

    // Constructor
    public AdmissionReportModel() {
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

    public String getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
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

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
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

    public String getGuardianRelation() {
        return guardianRelation;
    }

    public void setGuardianRelation(String guardianRelation) {
        this.guardianRelation = guardianRelation;
    }

    public String getGuardianPhone() {
        return guardianPhone;
    }

    public void setGuardianPhone(String guardianPhone) {
        this.guardianPhone = guardianPhone;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    // Helper methods
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

    public String getGuardianInfo() {
        StringBuilder info = new StringBuilder();
        if (guardianName != null && !guardianName.isEmpty()) {
            info.append(guardianName);
        }
        if (guardianRelation != null && !guardianRelation.isEmpty()) {
            if (info.length() > 0) info.append(" (");
            info.append(guardianRelation);
            if (info.length() > guardianName.length()) info.append(")");
        }
        return info.toString();
    }

    public String getAdmissionYear() {
        if (admissionDate != null && !admissionDate.isEmpty()) {
            // Extract year from date (assuming format: YYYY-MM-DD)
            String[] parts = admissionDate.split("-");
            if (parts.length > 0) {
                return parts[0];
            }
        }
        return "";
    }

    public boolean isActiveStudent() {
        return "yes".equalsIgnoreCase(isActive);
    }
}

