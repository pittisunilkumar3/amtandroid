package com.qdocs.ssre241123.model;

/**
 * Model class for Student Profile Report
 * Contains comprehensive student information including personal, academic, family, hostel, transport details
 */
public class StudentProfileReportModel {
    
    // Student Basic Information
    private String id;
    private String admissionNo;
    private String rollNo;
    private String admissionDate;
    private String firstname;
    private String middlename;
    private String lastname;
    private String fullName;
    private String rte;
    private String image;
    private String mobileno;
    private String email;
    private String state;
    private String city;
    private String pincode;
    private String religion;
    private String cast;
    private String dob;
    private String currentAddress;
    private String permanentAddress;
    private String categoryId;
    private String categoryName;
    private String adharNo;
    private String samagraId;
    private String bankAccountNo;
    private String bankName;
    private String ifscCode;
    private String guardianIs;
    private String isActive;
    private String createdAt;
    private String updatedAt;
    private String fatherPic;
    private String motherPic;
    private String guardianPic;
    private String gender;
    private String bloodGroup;
    private String schoolHouseId;
    private String schoolHouseName;
    private String note;
    private String previousSchool;
    private String height;
    private String weight;
    private String measurementDate;
    private String disableReason;
    private String disableNote;
    
    // Class Information
    private String classId;
    private String className;
    private String sectionId;
    private String sectionName;
    
    // Session Information
    private String sessionId;
    private String sessionName;
    
    // Father Information
    private String fatherName;
    private String fatherPhone;
    private String fatherOccupation;
    
    // Mother Information
    private String motherName;
    private String motherPhone;
    private String motherOccupation;
    
    // Guardian Information
    private String guardianName;
    private String guardianRelation;
    private String guardianPhone;
    private String guardianOccupation;
    private String guardianAddress;
    private String guardianEmail;
    
    // Hostel Information
    private String hostelId;
    private String hostelName;
    private String hostelRoomNo;
    private String hostelRoomType;
    private String hostelCostPerBed;
    
    // Transport Information
    private String vehicleNo;
    private String vehicleModel;
    private String vehicleRouteId;
    private String vehicleRouteName;
    private String driverName;
    private String driverContact;
    private String pickupPointName;
    private String transportFees;
    
    // Login Credentials
    private String username;
    private String password;
    
    // Fees Information
    private String feesDiscount;
    
    // Default constructor
    public StudentProfileReportModel() {
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
    
    public String getRollNo() {
        return rollNo;
    }
    
    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
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
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getRte() {
        return rte;
    }
    
    public void setRte(String rte) {
        this.rte = rte;
    }
    
    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
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
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getPincode() {
        return pincode;
    }
    
    public void setPincode(String pincode) {
        this.pincode = pincode;
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
    
    public String getDob() {
        return dob;
    }
    
    public void setDob(String dob) {
        this.dob = dob;
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
    
    public String getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public String getAdharNo() {
        return adharNo;
    }
    
    public void setAdharNo(String adharNo) {
        this.adharNo = adharNo;
    }
    
    public String getSamagraId() {
        return samagraId;
    }
    
    public void setSamagraId(String samagraId) {
        this.samagraId = samagraId;
    }
    
    public String getBankAccountNo() {
        return bankAccountNo;
    }
    
    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }
    
    public String getBankName() {
        return bankName;
    }
    
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    
    public String getIfscCode() {
        return ifscCode;
    }
    
    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getGuardianIs() {
        return guardianIs;
    }

    public void setGuardianIs(String guardianIs) {
        this.guardianIs = guardianIs;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
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

    public String getFatherPic() {
        return fatherPic;
    }

    public void setFatherPic(String fatherPic) {
        this.fatherPic = fatherPic;
    }

    public String getMotherPic() {
        return motherPic;
    }

    public void setMotherPic(String motherPic) {
        this.motherPic = motherPic;
    }

    public String getGuardianPic() {
        return guardianPic;
    }

    public void setGuardianPic(String guardianPic) {
        this.guardianPic = guardianPic;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getSchoolHouseId() {
        return schoolHouseId;
    }

    public void setSchoolHouseId(String schoolHouseId) {
        this.schoolHouseId = schoolHouseId;
    }

    public String getSchoolHouseName() {
        return schoolHouseName;
    }

    public void setSchoolHouseName(String schoolHouseName) {
        this.schoolHouseName = schoolHouseName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPreviousSchool() {
        return previousSchool;
    }

    public void setPreviousSchool(String previousSchool) {
        this.previousSchool = previousSchool;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getMeasurementDate() {
        return measurementDate;
    }

    public void setMeasurementDate(String measurementDate) {
        this.measurementDate = measurementDate;
    }

    public String getDisableReason() {
        return disableReason;
    }

    public void setDisableReason(String disableReason) {
        this.disableReason = disableReason;
    }

    public String getDisableNote() {
        return disableNote;
    }

    public void setDisableNote(String disableNote) {
        this.disableNote = disableNote;
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

    public String getFatherOccupation() {
        return fatherOccupation;
    }

    public void setFatherOccupation(String fatherOccupation) {
        this.fatherOccupation = fatherOccupation;
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

    public String getMotherOccupation() {
        return motherOccupation;
    }

    public void setMotherOccupation(String motherOccupation) {
        this.motherOccupation = motherOccupation;
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

    public String getGuardianOccupation() {
        return guardianOccupation;
    }

    public void setGuardianOccupation(String guardianOccupation) {
        this.guardianOccupation = guardianOccupation;
    }

    public String getGuardianAddress() {
        return guardianAddress;
    }

    public void setGuardianAddress(String guardianAddress) {
        this.guardianAddress = guardianAddress;
    }

    public String getGuardianEmail() {
        return guardianEmail;
    }

    public void setGuardianEmail(String guardianEmail) {
        this.guardianEmail = guardianEmail;
    }

    public String getHostelId() {
        return hostelId;
    }

    public void setHostelId(String hostelId) {
        this.hostelId = hostelId;
    }

    public String getHostelName() {
        return hostelName;
    }

    public void setHostelName(String hostelName) {
        this.hostelName = hostelName;
    }

    public String getHostelRoomNo() {
        return hostelRoomNo;
    }

    public void setHostelRoomNo(String hostelRoomNo) {
        this.hostelRoomNo = hostelRoomNo;
    }

    public String getHostelRoomType() {
        return hostelRoomType;
    }

    public void setHostelRoomType(String hostelRoomType) {
        this.hostelRoomType = hostelRoomType;
    }

    public String getHostelCostPerBed() {
        return hostelCostPerBed;
    }

    public void setHostelCostPerBed(String hostelCostPerBed) {
        this.hostelCostPerBed = hostelCostPerBed;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleRouteId() {
        return vehicleRouteId;
    }

    public void setVehicleRouteId(String vehicleRouteId) {
        this.vehicleRouteId = vehicleRouteId;
    }

    public String getVehicleRouteName() {
        return vehicleRouteName;
    }

    public void setVehicleRouteName(String vehicleRouteName) {
        this.vehicleRouteName = vehicleRouteName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverContact() {
        return driverContact;
    }

    public void setDriverContact(String driverContact) {
        this.driverContact = driverContact;
    }

    public String getPickupPointName() {
        return pickupPointName;
    }

    public void setPickupPointName(String pickupPointName) {
        this.pickupPointName = pickupPointName;
    }

    public String getTransportFees() {
        return transportFees;
    }

    public void setTransportFees(String transportFees) {
        this.transportFees = transportFees;
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

    public String getFeesDiscount() {
        return feesDiscount;
    }

    public void setFeesDiscount(String feesDiscount) {
        this.feesDiscount = feesDiscount;
    }
}

