package com.qdocs.ssre241123.model;

public class AlumniModel {
    private String id;
    private String studentName;
    private String admissionNo;
    private String classSection;
    private String passOutYear;
    private String currentEmail;
    private String currentPhone;
    private String occupation;
    private String currentAddress;
    private String guardianName;
    private String guardianPhone;
    private String dateOfBirth;
    private String gender;
    private String category;
    private String bloodGroup;
    private String religion;
    private String caste;
    private String motherTongue;
    private String studentImage;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getAdmissionNo() {
        return admissionNo;
    }

    public void setAdmissionNo(String admissionNo) {
        this.admissionNo = admissionNo;
    }

    public String getClassSection() {
        return classSection;
    }

    public void setClassSection(String classSection) {
        this.classSection = classSection;
    }

    public String getPassOutYear() {
        return passOutYear;
    }

    public void setPassOutYear(String passOutYear) {
        this.passOutYear = passOutYear;
    }

    public String getCurrentEmail() {
        return currentEmail;
    }

    public void setCurrentEmail(String currentEmail) {
        this.currentEmail = currentEmail;
    }

    public String getCurrentPhone() {
        return currentPhone;
    }

    public void setCurrentPhone(String currentPhone) {
        this.currentPhone = currentPhone;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getMotherTongue() {
        return motherTongue;
    }

    public void setMotherTongue(String motherTongue) {
        this.motherTongue = motherTongue;
    }

    public String getStudentImage() {
        return studentImage;
    }

    public void setStudentImage(String studentImage) {
        this.studentImage = studentImage;
    }

    // Helper methods for formatted display
    public String getFormattedName() {
        return studentName != null && !studentName.isEmpty() ? studentName : "-";
    }

    public String getFormattedAdmissionNo() {
        return admissionNo != null && !admissionNo.isEmpty() ? admissionNo : "-";
    }

    public String getFormattedClassSection() {
        return classSection != null && !classSection.isEmpty() ? classSection : "-";
    }

    public String getFormattedPassOutYear() {
        return passOutYear != null && !passOutYear.isEmpty() ? passOutYear : "-";
    }

    public String getFormattedEmail() {
        return currentEmail != null && !currentEmail.isEmpty() ? currentEmail : "Not provided";
    }

    public String getFormattedPhone() {
        return currentPhone != null && !currentPhone.isEmpty() ? currentPhone : "Not provided";
    }

    public String getFormattedOccupation() {
        return occupation != null && !occupation.isEmpty() ? occupation : "Not specified";
    }

    public String getFormattedAddress() {
        return currentAddress != null && !currentAddress.isEmpty() ? currentAddress : "Not provided";
    }

    public String getFormattedGuardianName() {
        return guardianName != null && !guardianName.isEmpty() ? guardianName : "-";
    }

    public String getFormattedGuardianPhone() {
        return guardianPhone != null && !guardianPhone.isEmpty() ? guardianPhone : "-";
    }

    public String getFormattedGender() {
        if (gender == null || gender.isEmpty()) return "-";
        return gender.substring(0, 1).toUpperCase() + gender.substring(1).toLowerCase();
    }

    public String getFormattedCategory() {
        return category != null && !category.isEmpty() ? category : "-";
    }

    public String getFormattedBloodGroup() {
        return bloodGroup != null && !bloodGroup.isEmpty() ? bloodGroup : "-";
    }
}

