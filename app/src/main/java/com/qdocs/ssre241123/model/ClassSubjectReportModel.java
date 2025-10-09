package com.qdocs.ssre241123.model;

public class ClassSubjectReportModel {
    private String timetableId;
    private String subjectId;
    private String subjectName;
    private String subjectCode;
    private String subjectType;
    private String staffId;
    private String staffName;
    private String staffSurname;
    private String employeeId;
    private String classId;
    private String className;
    private String sectionId;
    private String sectionName;
    private String day;
    private String timeFrom;
    private String timeTo;
    private String roomNo;
    private String sessionId;

    // Constructor
    public ClassSubjectReportModel() {
    }

    // Getters and Setters
    public String getTimetableId() {
        return timetableId;
    }

    public void setTimetableId(String timetableId) {
        this.timetableId = timetableId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffSurname() {
        return staffSurname;
    }

    public void setStaffSurname(String staffSurname) {
        this.staffSurname = staffSurname;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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

    public String getTeacherFullName() {
        if (staffName != null && staffSurname != null) {
            return staffName + " " + staffSurname;
        } else if (staffName != null) {
            return staffName;
        } else if (staffSurname != null) {
            return staffSurname;
        }
        return "";
    }

    public String getTimeSlot() {
        if (timeFrom != null && timeTo != null) {
            return formatTime(timeFrom) + " - " + formatTime(timeTo);
        } else if (timeFrom != null) {
            return formatTime(timeFrom);
        } else if (timeTo != null) {
            return formatTime(timeTo);
        }
        return "";
    }

    private String formatTime(String time) {
        if (time == null || time.isEmpty()) {
            return "";
        }
        // Convert 24-hour format to 12-hour format
        try {
            String[] parts = time.split(":");
            if (parts.length >= 2) {
                int hour = Integer.parseInt(parts[0]);
                int minute = Integer.parseInt(parts[1]);
                String ampm = hour >= 12 ? "PM" : "AM";
                hour = hour % 12;
                if (hour == 0) hour = 12;
                return String.format("%02d:%02d %s", hour, minute, ampm);
            }
        } catch (Exception e) {
            // Return original time if parsing fails
        }
        return time;
    }

    public String getSubjectWithCode() {
        if (subjectName != null && subjectCode != null && !subjectCode.isEmpty()) {
            return subjectName + " (" + subjectCode + ")";
        } else if (subjectName != null) {
            return subjectName;
        }
        return "";
    }

    public String getDayTimeInfo() {
        if (day != null && !getTimeSlot().isEmpty()) {
            return day + ", " + getTimeSlot();
        } else if (day != null) {
            return day;
        } else if (!getTimeSlot().isEmpty()) {
            return getTimeSlot();
        }
        return "";
    }
}

