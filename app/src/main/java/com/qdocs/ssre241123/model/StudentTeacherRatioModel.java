package com.qdocs.ssre241123.model;

public class StudentTeacherRatioModel {
    private String totalStudent;
    private String male;
    private String female;
    private String className;
    private String sectionName;
    private String classId;
    private String sectionId;
    private String totalTeacher;
    private String boysGirlsRatio;
    private String teacherRatio;

    // Constructor
    public StudentTeacherRatioModel() {
    }

    // Getters and Setters
    public String getTotalStudent() {
        return totalStudent;
    }

    public void setTotalStudent(String totalStudent) {
        this.totalStudent = totalStudent;
    }

    public String getMale() {
        return male;
    }

    public void setMale(String male) {
        this.male = male;
    }

    public String getFemale() {
        return female;
    }

    public void setFemale(String female) {
        this.female = female;
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

    public String getTotalTeacher() {
        return totalTeacher;
    }

    public void setTotalTeacher(String totalTeacher) {
        this.totalTeacher = totalTeacher;
    }

    public String getBoysGirlsRatio() {
        return boysGirlsRatio;
    }

    public void setBoysGirlsRatio(String boysGirlsRatio) {
        this.boysGirlsRatio = boysGirlsRatio;
    }

    public String getTeacherRatio() {
        return teacherRatio;
    }

    public void setTeacherRatio(String teacherRatio) {
        this.teacherRatio = teacherRatio;
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

    public int getTotalStudentInt() {
        try {
            return Integer.parseInt(totalStudent != null ? totalStudent : "0");
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public int getMaleInt() {
        try {
            return Integer.parseInt(male != null ? male : "0");
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public int getFemaleInt() {
        try {
            return Integer.parseInt(female != null ? female : "0");
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public int getTotalTeacherInt() {
        try {
            return Integer.parseInt(totalTeacher != null ? totalTeacher : "0");
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public String getGenderDistribution() {
        return "Boys: " + male + " | Girls: " + female;
    }

    public String getStudentTeacherInfo() {
        return "Students: " + totalStudent + " | Teachers: " + totalTeacher;
    }
}

