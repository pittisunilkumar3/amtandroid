package com.qdocs.ssre241123.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Model class for Monthly Staff Attendance Report
 * Represents a staff member's complete monthly attendance with daily records
 */
public class MonthlyStaffAttendanceModel {
    
    private String staffId;
    private StaffInfo staffInfo;
    private Map<String, DailyAttendance> dailyAttendance; // Keyed by date (YYYY-MM-DD)
    private AttendanceSummary attendanceSummary;
    private double attendancePercentage;
    private int attendancePercentageDisplay;
    private String attendanceStatus;
    private String attendanceStatusClass;
    private int totalWorkingDays;
    private int totalPresentDays;

    public MonthlyStaffAttendanceModel() {
        this.dailyAttendance = new HashMap<>();
        this.attendanceSummary = new AttendanceSummary();
    }

    // Inner class for Staff Information
    public static class StaffInfo {
        private String name;
        private String surname;
        private String employeeId;
        private String contactNo;
        private String email;
        private String role;

        public StaffInfo() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(String employeeId) {
            this.employeeId = employeeId;
        }

        public String getContactNo() {
            return contactNo;
        }

        public void setContactNo(String contactNo) {
            this.contactNo = contactNo;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getFullName() {
            if (surname != null && !surname.isEmpty()) {
                return name + " " + surname;
            }
            return name;
        }
    }

    // Inner class for Daily Attendance
    public static class DailyAttendance {
        private String date;
        private String dayName;
        private String dayShort;
        private String attendanceType;
        private String attendanceKey;
        private String remark;

        public DailyAttendance() {
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDayName() {
            return dayName;
        }

        public void setDayName(String dayName) {
            this.dayName = dayName;
        }

        public String getDayShort() {
            return dayShort;
        }

        public void setDayShort(String dayShort) {
            this.dayShort = dayShort;
        }

        public String getAttendanceType() {
            return attendanceType;
        }

        public void setAttendanceType(String attendanceType) {
            this.attendanceType = attendanceType;
        }

        public String getAttendanceKey() {
            return attendanceKey;
        }

        public void setAttendanceKey(String attendanceKey) {
            this.attendanceKey = attendanceKey;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

    // Inner class for Attendance Summary
    public static class AttendanceSummary {
        private int present;
        private int late;
        private int absent;
        private int halfDay;
        private int holiday;

        public AttendanceSummary() {
        }

        public int getPresent() {
            return present;
        }

        public void setPresent(int present) {
            this.present = present;
        }

        public int getLate() {
            return late;
        }

        public void setLate(int late) {
            this.late = late;
        }

        public int getAbsent() {
            return absent;
        }

        public void setAbsent(int absent) {
            this.absent = absent;
        }

        public int getHalfDay() {
            return halfDay;
        }

        public void setHalfDay(int halfDay) {
            this.halfDay = halfDay;
        }

        public int getHoliday() {
            return holiday;
        }

        public void setHoliday(int holiday) {
            this.holiday = holiday;
        }
    }

    // Main class getters and setters
    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public StaffInfo getStaffInfo() {
        return staffInfo;
    }

    public void setStaffInfo(StaffInfo staffInfo) {
        this.staffInfo = staffInfo;
    }

    public Map<String, DailyAttendance> getDailyAttendance() {
        return dailyAttendance;
    }

    public void setDailyAttendance(Map<String, DailyAttendance> dailyAttendance) {
        this.dailyAttendance = dailyAttendance;
    }

    public AttendanceSummary getAttendanceSummary() {
        return attendanceSummary;
    }

    public void setAttendanceSummary(AttendanceSummary attendanceSummary) {
        this.attendanceSummary = attendanceSummary;
    }

    public double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public int getAttendancePercentageDisplay() {
        return attendancePercentageDisplay;
    }

    public void setAttendancePercentageDisplay(int attendancePercentageDisplay) {
        this.attendancePercentageDisplay = attendancePercentageDisplay;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public String getAttendanceStatusClass() {
        return attendanceStatusClass;
    }

    public void setAttendanceStatusClass(String attendanceStatusClass) {
        this.attendanceStatusClass = attendanceStatusClass;
    }

    public int getTotalWorkingDays() {
        return totalWorkingDays;
    }

    public void setTotalWorkingDays(int totalWorkingDays) {
        this.totalWorkingDays = totalWorkingDays;
    }

    public int getTotalPresentDays() {
        return totalPresentDays;
    }

    public void setTotalPresentDays(int totalPresentDays) {
        this.totalPresentDays = totalPresentDays;
    }

    @Override
    public String toString() {
        return "MonthlyStaffAttendanceModel{" +
                "staffId='" + staffId + '\'' +
                ", staffInfo=" + (staffInfo != null ? staffInfo.getFullName() : "null") +
                ", attendancePercentage=" + attendancePercentageDisplay + "%" +
                ", attendanceStatus='" + attendanceStatus + '\'' +
                ", totalWorkingDays=" + totalWorkingDays +
                ", totalPresentDays=" + totalPresentDays +
                '}';
    }
}
