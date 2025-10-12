package com.qdocs.ssre241123.model;

/**
 * Model class for User Log Report
 * Represents user login activity data
 */
public class UserLogModel {

    private String id;
    private String user;
    private String role;
    private String classSectionId;
    private String ipaddress;
    private String userAgent;
    private String loginDatetime;
    private String classId;
    private String className;
    private String sectionId;
    private String sectionName;
    private String date;
    private String time;
    private String datetime;
    private String classSection;

    // Constructors
    public UserLogModel() {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getClassSectionId() {
        return classSectionId;
    }

    public void setClassSectionId(String classSectionId) {
        this.classSectionId = classSectionId;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getLoginDatetime() {
        return loginDatetime;
    }

    public void setLoginDatetime(String loginDatetime) {
        this.loginDatetime = loginDatetime;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getClassSection() {
        return classSection;
    }

    public void setClassSection(String classSection) {
        this.classSection = classSection;
    }

    // Helper methods
    public String getFormattedClassSection() {
        if (classSection != null && !classSection.isEmpty() && !classSection.equals("null")) {
            return classSection;
        } else if (className != null && !className.isEmpty() && !className.equals("null")) {
            if (sectionName != null && !sectionName.isEmpty() && !sectionName.equals("null")) {
                return className + " - " + sectionName;
            }
            return className;
        }
        return "-";
    }

    public String getFormattedRole() {
        if (role != null && !role.isEmpty()) {
            // Capitalize first letter
            return role.substring(0, 1).toUpperCase() + role.substring(1);
        }
        return "-";
    }

    public String getFormattedUser() {
        if (user != null && !user.isEmpty()) {
            return user;
        }
        return "-";
    }

    public String getFormattedIpAddress() {
        if (ipaddress != null && !ipaddress.isEmpty()) {
            return ipaddress;
        }
        return "-";
    }

    public String getFormattedDateTime() {
        if (datetime != null && !datetime.isEmpty()) {
            return datetime;
        } else if (loginDatetime != null && !loginDatetime.isEmpty()) {
            return loginDatetime;
        }
        return "-";
    }

    public String getFormattedDate() {
        if (date != null && !date.isEmpty()) {
            return date;
        }
        return "-";
    }

    public String getFormattedTime() {
        if (time != null && !time.isEmpty()) {
            return time;
        }
        return "-";
    }

    public String getDeviceInfo() {
        if (userAgent != null && !userAgent.isEmpty()) {
            // Extract device type from user agent
            if (userAgent.toLowerCase().contains("mobile")) {
                return "Mobile";
            } else if (userAgent.toLowerCase().contains("tablet")) {
                return "Tablet";
            } else {
                return "Desktop";
            }
        }
        return "Unknown";
    }

    public String getBrowserInfo() {
        if (userAgent != null && !userAgent.isEmpty()) {
            // Extract browser from user agent
            if (userAgent.contains("Chrome")) {
                return "Chrome";
            } else if (userAgent.contains("Firefox")) {
                return "Firefox";
            } else if (userAgent.contains("Safari")) {
                return "Safari";
            } else if (userAgent.contains("Edge")) {
                return "Edge";
            } else if (userAgent.contains("Opera")) {
                return "Opera";
            }
        }
        return "Unknown";
    }
}
