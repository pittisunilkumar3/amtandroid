package com.qdocs.ssre241123.model;

/**
 * Model class for Student Login Credential Report
 * Represents student login information including username and password
 */
public class StudentLoginModel {
    
    private String id;
    private String admissionNo;
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
    private String email;
    private String username;
    private String password;
    private String isActive;
    
    // Constructor
    public StudentLoginModel() {
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
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
}

