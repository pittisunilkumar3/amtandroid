package com.qdocs.ssre241123.model;

/**
 * Model class for Daily Attendance Report
 * Represents attendance statistics grouped by class and section for a specific date
 */
public class DailyAttendanceReportModel {
    
    // Class and section information
    private String classId;
    private String className;
    private String sectionId;
    private String sectionName;
    
    // Attendance counts by type
    private String present;
    private String excuse;
    private String absent;
    private String late;
    private String halfDay;
    
    // Summary statistics
    private String totalStudent;
    private String totalPresent;
    private String presentPercent;
    private String absentPercent;
    
    public DailyAttendanceReportModel() {
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
    
    public String getPresent() {
        return present != null ? present : "0";
    }
    
    public void setPresent(String present) {
        this.present = present;
    }
    
    public String getExcuse() {
        return excuse != null ? excuse : "0";
    }
    
    public void setExcuse(String excuse) {
        this.excuse = excuse;
    }
    
    public String getAbsent() {
        return absent != null ? absent : "0";
    }
    
    public void setAbsent(String absent) {
        this.absent = absent;
    }
    
    public String getLate() {
        return late != null ? late : "0";
    }
    
    public void setLate(String late) {
        this.late = late;
    }
    
    public String getHalfDay() {
        return halfDay != null ? halfDay : "0";
    }
    
    public void setHalfDay(String halfDay) {
        this.halfDay = halfDay;
    }
    
    public String getTotalStudent() {
        return totalStudent != null ? totalStudent : "0";
    }
    
    public void setTotalStudent(String totalStudent) {
        this.totalStudent = totalStudent;
    }
    
    public String getTotalPresent() {
        return totalPresent != null ? totalPresent : "0";
    }
    
    public void setTotalPresent(String totalPresent) {
        this.totalPresent = totalPresent;
    }
    
    public String getPresentPercent() {
        return presentPercent != null ? presentPercent : "0%";
    }
    
    public void setPresentPercent(String presentPercent) {
        this.presentPercent = presentPercent;
    }
    
    public String getAbsentPercent() {
        return absentPercent != null ? absentPercent : "0%";
    }
    
    public void setAbsentPercent(String absentPercent) {
        this.absentPercent = absentPercent;
    }
    
    /**
     * Get formatted class and section display text
     */
    public String getClassSectionDisplay() {
        return className + " - " + sectionName;
    }
    
    /**
     * Get attendance percentage as integer for progress display
     */
    public int getPresentPercentageInt() {
        try {
            String percent = presentPercent.replace("%", "").trim();
            return Integer.parseInt(percent);
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Get absent percentage as integer
     */
    public int getAbsentPercentageInt() {
        try {
            String percent = absentPercent.replace("%", "").trim();
            return Integer.parseInt(percent);
        } catch (Exception e) {
            return 0;
        }
    }
}

