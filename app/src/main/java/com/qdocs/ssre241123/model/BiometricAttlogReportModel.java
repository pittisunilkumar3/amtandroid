package com.qdocs.ssre241123.model;

/**
 * Model class for Biometric Attendance Log Report
 * Represents a single biometric attendance log entry with student details
 */
public class BiometricAttlogReportModel {
    
    private String id;
    private String studentSessionId;
    private String date;
    private String attendenceTypeId;
    private String remark;
    private String biometricAttendence;
    private String biometricDeviceData;
    private String name;
    private String firstname;
    private String middlename;
    private String lastname;
    private String rollNo;
    private String admissionNo;
    private String className;
    private String section;

    // Constructor
    public BiometricAttlogReportModel() {
    }

    public BiometricAttlogReportModel(String id, String studentSessionId, String date, 
                                     String attendenceTypeId, String remark, 
                                     String biometricAttendence, String biometricDeviceData,
                                     String name, String firstname, String middlename, 
                                     String lastname, String rollNo, String admissionNo,
                                     String className, String section) {
        this.id = id;
        this.studentSessionId = studentSessionId;
        this.date = date;
        this.attendenceTypeId = attendenceTypeId;
        this.remark = remark;
        this.biometricAttendence = biometricAttendence;
        this.biometricDeviceData = biometricDeviceData;
        this.name = name;
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.rollNo = rollNo;
        this.admissionNo = admissionNo;
        this.className = className;
        this.section = section;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentSessionId() {
        return studentSessionId;
    }

    public void setStudentSessionId(String studentSessionId) {
        this.studentSessionId = studentSessionId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAttendenceTypeId() {
        return attendenceTypeId;
    }

    public void setAttendenceTypeId(String attendenceTypeId) {
        this.attendenceTypeId = attendenceTypeId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBiometricAttendence() {
        return biometricAttendence;
    }

    public void setBiometricAttendence(String biometricAttendence) {
        this.biometricAttendence = biometricAttendence;
    }

    public String getBiometricDeviceData() {
        return biometricDeviceData;
    }

    public void setBiometricDeviceData(String biometricDeviceData) {
        this.biometricDeviceData = biometricDeviceData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getAdmissionNo() {
        return admissionNo;
    }

    public void setAdmissionNo(String admissionNo) {
        this.admissionNo = admissionNo;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    /**
     * Get attendance type name based on attendence_type_id
     * 1=Present, 2=Excuse, 3=Late, 4=Absent, 6=Half Day
     */
    public String getAttendanceTypeName() {
        if (attendenceTypeId == null) return "Unknown";
        
        switch (attendenceTypeId) {
            case "1":
                return "Present";
            case "2":
                return "Excuse";
            case "3":
                return "Late";
            case "4":
                return "Absent";
            case "6":
                return "Half Day";
            default:
                return "Unknown";
        }
    }

    /**
     * Get color for attendance type
     */
    public int getAttendanceTypeColor() {
        if (attendenceTypeId == null) return android.graphics.Color.parseColor("#9E9E9E");
        
        switch (attendenceTypeId) {
            case "1": // Present
                return android.graphics.Color.parseColor("#4CAF50");
            case "2": // Excuse
                return android.graphics.Color.parseColor("#2196F3");
            case "3": // Late
                return android.graphics.Color.parseColor("#FF9800");
            case "4": // Absent
                return android.graphics.Color.parseColor("#F44336");
            case "6": // Half Day
                return android.graphics.Color.parseColor("#00BCD4");
            default:
                return android.graphics.Color.parseColor("#9E9E9E");
        }
    }

    /**
     * Check if this is a biometric attendance entry
     */
    public boolean isBiometric() {
        return "1".equals(biometricAttendence);
    }

    /**
     * Get full student name
     */
    public String getFullName() {
        if (name != null && !name.isEmpty()) {
            return name;
        }
        
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
        
        return fullName.length() > 0 ? fullName.toString() : "Unknown";
    }

    @Override
    public String toString() {
        return "BiometricAttlogReportModel{" +
                "id='" + id + '\'' +
                ", studentSessionId='" + studentSessionId + '\'' +
                ", date='" + date + '\'' +
                ", attendenceTypeId='" + attendenceTypeId + '\'' +
                ", name='" + name + '\'' +
                ", admissionNo='" + admissionNo + '\'' +
                ", className='" + className + '\'' +
                ", section='" + section + '\'' +
                ", biometricDeviceData='" + biometricDeviceData + '\'' +
                '}';
    }
}

