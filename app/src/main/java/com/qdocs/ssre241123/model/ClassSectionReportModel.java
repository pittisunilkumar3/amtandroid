package com.qdocs.ssre241123.model;

public class ClassSectionReportModel {
    private String id;
    private String classId;
    private String sectionId;
    private String className;
    private String sectionName;
    private String studentCount;
    private String isActive;

    // Constructor
    public ClassSectionReportModel() {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
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

    public String getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(String studentCount) {
        this.studentCount = studentCount;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
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
        return "";
    }

    public int getStudentCountInt() {
        try {
            return Integer.parseInt(studentCount != null ? studentCount : "0");
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public boolean isActiveSection() {
        return "yes".equalsIgnoreCase(isActive);
    }
}

