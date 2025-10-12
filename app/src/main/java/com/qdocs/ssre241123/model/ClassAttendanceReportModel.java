package com.qdocs.ssre241123.model;

public class ClassAttendanceReportModel {
    // Student information
    private String studentId;
    private String admissionNo;
    private String studentName;
    private String gender;
    
    // Class and section information
    private String classId;
    private String className;
    private String sectionId;
    private String sectionName;
    
    // Attendance data
    private String totalStudents;
    private String presentCount;
    private String excuseCount;
    private String lateCount;
    private String halfDayCount;
    private String absentCount;
    private String totalPresent;
    private String presentPercentage;
    private String absentPercentage;
    private String dateRange;
    private int totalDays;

    // Student getters and setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getAdmissionNo() {
        return admissionNo;
    }

    public void setAdmissionNo(String admissionNo) {
        this.admissionNo = admissionNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // Getters and Setters
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

    public String getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(String totalStudents) {
        this.totalStudents = totalStudents;
    }

    public String getPresentCount() {
        return presentCount;
    }

    public void setPresentCount(String presentCount) {
        this.presentCount = presentCount;
    }

    public String getExcuseCount() {
        return excuseCount;
    }

    public void setExcuseCount(String excuseCount) {
        this.excuseCount = excuseCount;
    }

    public String getLateCount() {
        return lateCount;
    }

    public void setLateCount(String lateCount) {
        this.lateCount = lateCount;
    }

    public String getHalfDayCount() {
        return halfDayCount;
    }

    public void setHalfDayCount(String halfDayCount) {
        this.halfDayCount = halfDayCount;
    }

    public String getAbsentCount() {
        return absentCount;
    }

    public void setAbsentCount(String absentCount) {
        this.absentCount = absentCount;
    }

    public String getTotalPresent() {
        return totalPresent;
    }

    public void setTotalPresent(String totalPresent) {
        this.totalPresent = totalPresent;
    }

    public String getPresentPercentage() {
        return presentPercentage;
    }

    public void setPresentPercentage(String presentPercentage) {
        this.presentPercentage = presentPercentage;
    }

    public String getAbsentPercentage() {
        return absentPercentage;
    }

    public void setAbsentPercentage(String absentPercentage) {
        this.absentPercentage = absentPercentage;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    // Helper methods for formatted display
    public String getClassSection() {
        return className + " - " + sectionName;
    }

    public String getFormattedTotalStudents() {
        return totalStudents != null && !totalStudents.trim().isEmpty() ? totalStudents : "0";
    }

    public String getFormattedPresentCount() {
        return presentCount != null && !presentCount.trim().isEmpty() ? presentCount : "0";
    }

    public String getFormattedAbsentCount() {
        return absentCount != null && !absentCount.trim().isEmpty() ? absentCount : "0";
    }

    public String getFormattedPresentPercentage() {
        return presentPercentage != null && !presentPercentage.trim().isEmpty() ? presentPercentage : "0%";
    }

    public String getFormattedAbsentPercentage() {
        return absentPercentage != null && !absentPercentage.trim().isEmpty() ? absentPercentage : "0%";
    }

    public String getAttendanceBreakdown() {
        StringBuilder breakdown = new StringBuilder();
        if (presentCount != null && !presentCount.equals("0")) {
            breakdown.append("P:").append(presentCount);
        }
        if (excuseCount != null && !excuseCount.equals("0")) {
            if (breakdown.length() > 0) breakdown.append(" | ");
            breakdown.append("E:").append(excuseCount);
        }
        if (lateCount != null && !lateCount.equals("0")) {
            if (breakdown.length() > 0) breakdown.append(" | ");
            breakdown.append("L:").append(lateCount);
        }
        if (halfDayCount != null && !halfDayCount.equals("0")) {
            if (breakdown.length() > 0) breakdown.append(" | ");
            breakdown.append("H:").append(halfDayCount);
        }
        if (absentCount != null && !absentCount.equals("0")) {
            if (breakdown.length() > 0) breakdown.append(" | ");
            breakdown.append("A:").append(absentCount);
        }
        return breakdown.length() > 0 ? breakdown.toString() : "No Data";
    }
}
