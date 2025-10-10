# Online Admission Report - Testing Guide

## 🎯 Overview

This guide provides step-by-step instructions for testing the Online Admission Report feature in the Smart School Android application.

---

## 📋 Pre-requisites

### 1. Server Setup
- ✅ Backend API is running at: `https://school.cyberdetox.in`
- ✅ Online Admission API endpoints are available:
  - `POST /api/online-admission/list`
  - `POST /api/online-admission/filter`
  - `POST /api/online-admission/get/{id}`

### 2. Test Data
- ✅ At least one online admission record exists in the database
- ✅ Test records have various enrollment statuses (Enrolled/Not Enrolled)
- ✅ Test records have various payment statuses (Paid/Unpaid)
- ✅ Test records belong to different classes and sections

### 3. User Account
- ✅ Valid teacher account credentials
- ✅ Teacher has permission to view reports
- ✅ Teacher has access to student information reports

---

## 🧪 Test Cases

### Test Case 1: Navigation to Report

**Objective**: Verify user can navigate to Online Admission Report

**Steps**:
1. Launch the Smart School app
2. Login as a teacher
3. Navigate to Teacher Dashboard
4. Click on "Reports" module
5. Click on "Student Information" category
6. Click on "Online Admission Report"

**Expected Result**:
- ✅ Online Admission Report screen opens
- ✅ Title shows "Online Admission Report"
- ✅ Filter dropdowns are visible (Session, Class, Section)
- ✅ "Generate Report" button is visible
- ✅ No data is shown initially

**Status**: [ ] Pass [ ] Fail

**Notes**: _______________________________

---

### Test Case 2: Filter Dropdowns Population

**Objective**: Verify filter dropdowns are populated with data

**Steps**:
1. Navigate to Online Admission Report
2. Observe Session dropdown
3. Observe Class dropdown
4. Observe Section dropdown

**Expected Result**:
- ✅ Session dropdown shows available sessions
- ✅ Class dropdown shows available classes
- ✅ Section dropdown shows available sections
- ✅ Dropdowns are populated from API

**Status**: [ ] Pass [ ] Fail

**Notes**: _______________________________

---

### Test Case 3: Generate Report with Filters

**Objective**: Verify report generation with selected filters

**Steps**:
1. Navigate to Online Admission Report
2. Select a session from dropdown
3. Select a class from dropdown
4. Select a section from dropdown
5. Click "Generate Report" button

**Expected Result**:
- ✅ Loading indicator is shown
- ✅ API request is sent with correct filters
- ✅ Data is fetched from server
- ✅ Loading indicator disappears
- ✅ Admission records are displayed in cards

**Status**: [ ] Pass [ ] Fail

**Notes**: _______________________________

---

### Test Case 4: Data Display Verification

**Objective**: Verify admission data is displayed correctly

**Steps**:
1. Generate report with filters
2. Observe the displayed admission cards
3. Verify each field is shown correctly

**Expected Result**:
Each card should display:
- ✅ Student full name (bold, prominent)
- ✅ Enrollment status badge (Green for Enrolled, Orange for Not Enrolled)
- ✅ Reference number
- ✅ Admission number (if available)
- ✅ Class and section
- ✅ Gender and date of birth
- ✅ Contact number
- ✅ Email (if available)
- ✅ Father's name (if available)
- ✅ Admission date
- ✅ Payment status (Green for Paid, Red for Unpaid)

**Status**: [ ] Pass [ ] Fail

**Notes**: _______________________________

---

### Test Case 5: Enrollment Status Badge

**Objective**: Verify enrollment status badge colors

**Steps**:
1. Generate report
2. Find an enrolled student record
3. Find a not enrolled student record
4. Observe the badge colors

**Expected Result**:
- ✅ Enrolled students have GREEN badge
- ✅ Not enrolled students have ORANGE badge
- ✅ Badge text is readable
- ✅ Badge is positioned correctly

**Status**: [ ] Pass [ ] Fail

**Notes**: _______________________________

---

### Test Case 6: Payment Status Colors

**Objective**: Verify payment status text colors

**Steps**:
1. Generate report
2. Find a paid student record
3. Find an unpaid student record
4. Observe the payment status colors

**Expected Result**:
- ✅ Paid status shows in GREEN color
- ✅ Unpaid status shows in RED color
- ✅ Text is readable
- ✅ Status is positioned correctly

**Status**: [ ] Pass [ ] Fail

**Notes**: _______________________________

---

### Test Case 7: Optional Fields Visibility

**Objective**: Verify optional fields are hidden when empty

**Steps**:
1. Generate report
2. Find a record without admission number
3. Find a record without email
4. Find a record without father name

**Expected Result**:
- ✅ Admission number row is hidden when empty
- ✅ Email row is hidden when empty
- ✅ Father name row is hidden when empty
- ✅ Layout adjusts properly

**Status**: [ ] Pass [ ] Fail

**Notes**: _______________________________

---

### Test Case 8: No Data Scenario

**Objective**: Verify behavior when no data is available

**Steps**:
1. Navigate to Online Admission Report
2. Select filters that have no data
3. Click "Generate Report"

**Expected Result**:
- ✅ Loading indicator is shown
- ✅ API request is sent
- ✅ "No online admissions found" message is displayed
- ✅ No error occurs
- ✅ User can try different filters

**Status**: [ ] Pass [ ] Fail

**Notes**: _______________________________

---

### Test Case 9: Network Error Handling

**Objective**: Verify error handling when network fails

**Steps**:
1. Turn off internet connection
2. Navigate to Online Admission Report
3. Select filters
4. Click "Generate Report"

**Expected Result**:
- ✅ Error message is displayed
- ✅ Message is user-friendly
- ✅ No app crash
- ✅ User can retry after reconnecting

**Status**: [ ] Pass [ ] Fail

**Notes**: _______________________________

---

### Test Case 10: API Error Handling

**Objective**: Verify error handling when API returns error

**Steps**:
1. Simulate API error (if possible)
2. Generate report
3. Observe error handling

**Expected Result**:
- ✅ Error message is displayed
- ✅ Error details are logged
- ✅ No app crash
- ✅ User can retry

**Status**: [ ] Pass [ ] Fail

**Notes**: _______________________________

---

### Test Case 11: Scrolling and Performance

**Objective**: Verify list scrolling and performance

**Steps**:
1. Generate report with many records (50+)
2. Scroll through the list
3. Observe performance

**Expected Result**:
- ✅ List scrolls smoothly
- ✅ No lag or stuttering
- ✅ Cards load properly
- ✅ Memory usage is reasonable

**Status**: [ ] Pass [ ] Fail

**Notes**: _______________________________

---

### Test Case 12: Back Navigation

**Objective**: Verify back button functionality

**Steps**:
1. Navigate to Online Admission Report
2. Click back button
3. Observe navigation

**Expected Result**:
- ✅ Returns to previous screen (Student Information category)
- ✅ Proper animation
- ✅ No data loss
- ✅ No crash

**Status**: [ ] Pass [ ] Fail

**Notes**: _______________________________

---

## 🔍 API Testing

### Manual API Test

Use this curl command to test the API directly:

```bash
curl -X POST "https://school.cyberdetox.in/api/online-admission/filter" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d '{
    "class_id": 19,
    "section_id": 47
  }'
```

**Expected Response**:
```json
{
  "status": 1,
  "message": "Online admissions filtered successfully",
  "total_records": 15,
  "data": [...]
}
```

---

## 📊 Test Summary

| Test Case | Status | Notes |
|-----------|--------|-------|
| TC1: Navigation | [ ] | |
| TC2: Filter Dropdowns | [ ] | |
| TC3: Generate Report | [ ] | |
| TC4: Data Display | [ ] | |
| TC5: Enrollment Badge | [ ] | |
| TC6: Payment Status | [ ] | |
| TC7: Optional Fields | [ ] | |
| TC8: No Data | [ ] | |
| TC9: Network Error | [ ] | |
| TC10: API Error | [ ] | |
| TC11: Performance | [ ] | |
| TC12: Back Navigation | [ ] | |

---

## 🐛 Bug Report Template

If you find any issues, use this template:

```
Bug ID: OAR-XXX
Title: [Brief description]
Severity: [Critical/High/Medium/Low]
Steps to Reproduce:
1. 
2. 
3. 

Expected Result:

Actual Result:

Screenshots/Logs:

Device Info:
- Device Model:
- Android Version:
- App Version:
```

---

## ✅ Sign-off

**Tester Name**: _______________________________

**Date**: _______________________________

**Overall Status**: [ ] Pass [ ] Fail

**Comments**: _______________________________

