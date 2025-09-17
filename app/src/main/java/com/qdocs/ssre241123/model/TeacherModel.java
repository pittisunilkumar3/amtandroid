package com.qdocs.ssre241123.model;

public class TeacherModel {
    private String id;
    private String staff_id;
    private String employee_id;
    private String role;
    private String email;
    private String contact_no;
    private String username;
    private String name;
    private String surname;
    private String designation;
    private String department;
    private String date_format;
    private String currency_symbol;
    private String currency_short_name;
    private String currency_id;
    private String timezone;
    private String sch_name;
    private String is_rtl;
    private String theme;
    private String image;
    private String start_week;
    private String superadmin_restriction;
    private String token;
    private String jwt_token;
    private LanguageModel language;

    // Constructors
    public TeacherModel() {}

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDate_format() {
        return date_format;
    }

    public void setDate_format(String date_format) {
        this.date_format = date_format;
    }

    public String getCurrency_symbol() {
        return currency_symbol;
    }

    public void setCurrency_symbol(String currency_symbol) {
        this.currency_symbol = currency_symbol;
    }

    public String getCurrency_short_name() {
        return currency_short_name;
    }

    public void setCurrency_short_name(String currency_short_name) {
        this.currency_short_name = currency_short_name;
    }

    public String getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(String currency_id) {
        this.currency_id = currency_id;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getSch_name() {
        return sch_name;
    }

    public void setSch_name(String sch_name) {
        this.sch_name = sch_name;
    }

    public String getIs_rtl() {
        return is_rtl;
    }

    public void setIs_rtl(String is_rtl) {
        this.is_rtl = is_rtl;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStart_week() {
        return start_week;
    }

    public void setStart_week(String start_week) {
        this.start_week = start_week;
    }

    public String getSuperadmin_restriction() {
        return superadmin_restriction;
    }

    public void setSuperadmin_restriction(String superadmin_restriction) {
        this.superadmin_restriction = superadmin_restriction;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getJwt_token() {
        return jwt_token;
    }

    public void setJwt_token(String jwt_token) {
        this.jwt_token = jwt_token;
    }

    public LanguageModel getLanguage() {
        return language;
    }

    public void setLanguage(LanguageModel language) {
        this.language = language;
    }

    // Inner class for Language
    public static class LanguageModel {
        private String lang_id;
        private String language;
        private String short_code;

        public String getLang_id() {
            return lang_id;
        }

        public void setLang_id(String lang_id) {
            this.lang_id = lang_id;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getShort_code() {
            return short_code;
        }

        public void setShort_code(String short_code) {
            this.short_code = short_code;
        }
    }
}
