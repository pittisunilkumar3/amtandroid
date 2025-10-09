# Online Admission Report - Testing Guide

## Overview

This guide provides comprehensive testing procedures for the Online Admission Report feature. Follow these test cases to ensure the feature works correctly.

---

## Prerequisites

Before testing, ensure:
- ✅ Android application is built successfully
- ✅ Application is installed on test device/emulator
- ✅ API server is running and accessible
- ✅ Test device has internet connection
- ✅ Teacher account credentials are available
- ✅ Test data exists in the database

---

## Test Environment Setup

### 1. API Configuration

**Base URL:** `http://localhost/amt/api/` (or your server URL)

**Test Endpoint:** `POST /online-admission/filter`

**Authentication:**
```
Client-Service: smartschool
Auth-Key: schoolAdmin@
```

### 2. Test Data Requirements

Ensure the following test data exists:
- At least 5-10 online admission records
- Mix of enrolled and not enrolled students
- Mix of paid and unpaid admissions
- Students in different classes and sections
- Some records with complete information
- Some records with partial information (missing email, etc.)

### 3. Device Setup

- Android device or emulator (API level 21+)
- Screen size: 5" or larger recommended
- Internet connection: WiFi or mobile data
- Logcat access for debugging

---

## Test Cases

### Test Case 1: Navigation to Report

**Objective:** Verify user can navigate to Online Admission Report

**Steps:**
1. Launch the application
2. Login as teacher
3. Navigate to Teacher Dashboard
4. Tap on "Reports"
5. Tap on "Student Information" category
6. Tap on "Online Admission Report"

**Expected Results:**
- ✅ Online Admission Report activity launches
- ✅ Title shows "Online Admission Report"
- ✅ Filter dropdowns are visible (Session, Class, Section)
- ✅ "Apply" button is visible
- ✅ No data is shown initially (waiting for filter application)

**Pass/Fail:** ⬜

**Notes:**
```
_____________________________________________
_____________________________________________
```

---

### Test Case 2: Load All Admissions (No Filters)

**Objective:** Verify all admissions load when no filters are selected

**Steps:**
1. Navigate to Online Admission Report
2. Don't select any filters
3. Tap "Apply" button
4. Wait for data to load

**Expected Results:**
- ✅ Loading indicator appears
- ✅ API request is made to `/online-admission/filter` with empty body `{}`
- ✅ All online admission records are displayed
- ✅ Each card shows:
  - Student name
  - Reference number
  - Enrollment status badge
  - Class and section
  - Gender and DOB
  - Contact number
  - Admission date
  - Payment status
- ✅ Cards are scrollable
- ✅ No error messages

**Pass/Fail:** ⬜

**Notes:**
```
Total records displayed: _______
_____________________________________________
```

---

### Test Case 3: Filter by Class

**Objective:** Verify filtering by class works correctly

**Steps:**
1. Navigate to Online Admission Report
2. Select a class from the dropdown (e.g., "Class 10")
3. Don't select section
4. Tap "Apply" button
5. Wait for data to load

**Expected Results:**
- ✅ Loading indicator appears
- ✅ API request includes `"class_id": <selected_class_id>`
- ✅ Only admissions for selected class are displayed
- ✅ All sections of that class are included
- ✅ Data is accurate and matches filter

**Pass/Fail:** ⬜

**Notes:**
```
Selected class: _______
Records displayed: _______
_____________________________________________
```

---

### Test Case 4: Filter by Class and Section

**Objective:** Verify filtering by both class and section works correctly

**Steps:**
1. Navigate to Online Admission Report
2. Select a class from the dropdown
3. Select a section from the dropdown
4. Tap "Apply" button
5. Wait for data to load

**Expected Results:**
- ✅ Loading indicator appears
- ✅ API request includes both `"class_id"` and `"section_id"`
- ✅ Only admissions for selected class-section are displayed
- ✅ Data is accurate and matches filters

**Pass/Fail:** ⬜

**Notes:**
```
Selected class: _______
Selected section: _______
Records displayed: _______
_____________________________________________
```

---

### Test Case 5: No Data Scenario

**Objective:** Verify proper handling when no data matches filters

**Steps:**
1. Navigate to Online Admission Report
2. Select filters that have no matching data
3. Tap "Apply" button
4. Wait for response

**Expected Results:**
- ✅ Loading indicator appears
- ✅ API request is made successfully
- ✅ "No online admissions found" message is displayed
- ✅ No error message
- ✅ UI is clean and informative

**Pass/Fail:** ⬜

**Notes:**
```
_____________________________________________
_____________________________________________
```

---

### Test Case 6: Network Error Handling

**Objective:** Verify proper error handling when network is unavailable

**Steps:**
1. Navigate to Online Admission Report
2. Disable internet connection on device
3. Select filters and tap "Apply"
4. Wait for error

**Expected Results:**
- ✅ Loading indicator appears
- ✅ Error message is displayed after timeout
- ✅ Message indicates network error
- ✅ User can retry after enabling internet
- ✅ No app crash

**Pass/Fail:** ⬜

**Notes:**
```
Error message displayed: _______
_____________________________________________
```

---

### Test Case 7: Enrollment Status Display

**Objective:** Verify enrollment status is displayed correctly

**Steps:**
1. Load admissions with mixed enrollment status
2. Observe the enrollment status badges

**Expected Results:**
- ✅ Enrolled students show green badge with "Enrolled" text
- ✅ Not enrolled students show orange badge with "Not Enrolled" text
- ✅ Badge colors are clearly distinguishable
- ✅ Text is readable

**Pass/Fail:** ⬜

**Notes:**
```
Enrolled count: _______
Not enrolled count: _______
_____________________________________________
```

---

### Test Case 8: Payment Status Display

**Objective:** Verify payment status is displayed correctly

**Steps:**
1. Load admissions with mixed payment status
2. Observe the payment status in bottom right of cards

**Expected Results:**
- ✅ Paid admissions show "Paid" in green color
- ✅ Unpaid admissions show "Unpaid" in red color
- ✅ Colors are clearly distinguishable
- ✅ Text is readable

**Pass/Fail:** ⬜

**Notes:**
```
Paid count: _______
Unpaid count: _______
_____________________________________________
```

---

### Test Case 9: Conditional Field Display

**Objective:** Verify optional fields show/hide correctly

**Steps:**
1. Load admissions with varying data completeness
2. Observe which fields are displayed

**Expected Results:**
- ✅ Admission number shown only if student is enrolled
- ✅ Email shown only if email exists
- ✅ Father name shown only if father name exists
- ✅ Hidden fields don't leave empty space
- ✅ Layout adjusts properly

**Pass/Fail:** ⬜

**Notes:**
```
_____________________________________________
_____________________________________________
```

---

### Test Case 10: Data Accuracy

**Objective:** Verify displayed data matches API response

**Steps:**
1. Load admissions
2. Check Logcat for API response
3. Compare displayed data with response data
4. Verify at least 3 different records

**Expected Results:**
- ✅ Student names match exactly
- ✅ Reference numbers match
- ✅ Admission numbers match (if enrolled)
- ✅ Class and section match
- ✅ Gender matches
- ✅ DOB matches
- ✅ Contact numbers match
- ✅ Admission dates match
- ✅ Enrollment status matches
- ✅ Payment status matches

**Pass/Fail:** ⬜

**Notes:**
```
Records verified: _______
Discrepancies found: _______
_____________________________________________
```

---

### Test Case 11: UI Responsiveness

**Objective:** Verify UI is responsive and smooth

**Steps:**
1. Load a list with 10+ admissions
2. Scroll through the list
3. Apply different filters multiple times
4. Rotate device (if applicable)

**Expected Results:**
- ✅ Scrolling is smooth without lag
- ✅ Cards render quickly
- ✅ Filter changes apply quickly
- ✅ No UI freezing
- ✅ No memory issues
- ✅ Rotation handled properly (if applicable)

**Pass/Fail:** ⬜

**Notes:**
```
_____________________________________________
_____________________________________________
```

---

### Test Case 12: Large Dataset

**Objective:** Verify performance with large number of records

**Steps:**
1. Load all admissions (no filters)
2. Ensure dataset has 50+ records
3. Scroll through entire list
4. Apply filters

**Expected Results:**
- ✅ All records load successfully
- ✅ Scrolling remains smooth
- ✅ No memory errors
- ✅ No app crash
- ✅ Filtering works correctly

**Pass/Fail:** ⬜

**Notes:**
```
Total records: _______
Performance: _______
_____________________________________________
```

---

### Test Case 13: API Error Handling

**Objective:** Verify proper handling of API errors

**Steps:**
1. Temporarily modify API to return error (status: 0)
2. Load admissions
3. Observe error handling

**Expected Results:**
- ✅ Error message is displayed
- ✅ Message shows API error text
- ✅ No app crash
- ✅ User can retry

**Pass/Fail:** ⬜

**Notes:**
```
_____________________________________________
_____________________________________________
```

---

### Test Case 14: Back Navigation

**Objective:** Verify back navigation works correctly

**Steps:**
1. Navigate to Online Admission Report
2. Load some data
3. Press back button
4. Navigate to report again

**Expected Results:**
- ✅ Back button returns to previous screen
- ✅ No data persists incorrectly
- ✅ Filters reset on new navigation
- ✅ No memory leaks

**Pass/Fail:** ⬜

**Notes:**
```
_____________________________________________
_____________________________________________
```

---

### Test Case 15: Multiple Filter Changes

**Objective:** Verify multiple filter applications work correctly

**Steps:**
1. Navigate to Online Admission Report
2. Apply filter 1 (e.g., Class 10)
3. Wait for data to load
4. Change filter to filter 2 (e.g., Class 9)
5. Apply again
6. Repeat 2-3 times with different filters

**Expected Results:**
- ✅ Each filter change loads correct data
- ✅ Previous data is cleared
- ✅ No data mixing between filters
- ✅ Loading state shows each time
- ✅ No memory issues

**Pass/Fail:** ⬜

**Notes:**
```
_____________________________________________
_____________________________________________
```

---

## Logcat Verification

### Key Log Messages to Verify

1. **Activity Launch:**
```
D/OnlineAdmissionReport: loadReportData called
```

2. **API Request:**
```
D/OnlineAdmissionReport: === Fetching Online Admissions ===
D/OnlineAdmissionReport: URL: http://localhost/amt/api/online-admission/filter
D/OnlineAdmissionReport: Method: POST
D/OnlineAdmissionReport: Headers: {Client-Service=smartschool, Auth-Key=schoolAdmin@, Content-Type=application/json}
D/OnlineAdmissionReport: Request Body: {"class_id":10}
```

3. **API Response:**
```
D/OnlineAdmissionReport: === API Response Received ===
D/OnlineAdmissionReport: Response: {"status":1,"message":"...","data":[...]}
```

4. **Parsing:**
```
D/OnlineAdmissionReport: === Parsing Response ===
D/OnlineAdmissionReport: Status: 1
D/OnlineAdmissionReport: Data array length: 15
D/OnlineAdmissionReport: Parsed admission: John Doe (Ref: REF2024001)
D/OnlineAdmissionReport: Total admissions parsed: 15
```

5. **Errors (if any):**
```
E/OnlineAdmissionReport: === API Error ===
E/OnlineAdmissionReport: Error: ...
```

---

## Performance Benchmarks

### Expected Performance Metrics

| Metric | Target | Acceptable | Poor |
|--------|--------|------------|------|
| API Response Time | < 1s | 1-3s | > 3s |
| UI Render Time | < 500ms | 500ms-1s | > 1s |
| Scroll FPS | 60 fps | 45-60 fps | < 45 fps |
| Memory Usage | < 50MB | 50-100MB | > 100MB |

**Actual Performance:**
```
API Response Time: _______
UI Render Time: _______
Scroll FPS: _______
Memory Usage: _______
```

---

## Bug Report Template

If you find any issues, use this template:

```
Bug ID: OAR-XXX
Title: [Brief description]
Severity: [Critical/High/Medium/Low]
Priority: [High/Medium/Low]

Steps to Reproduce:
1. 
2. 
3. 

Expected Result:


Actual Result:


Screenshots/Logs:


Device Information:
- Device Model: 
- Android Version: 
- App Version: 

Additional Notes:

```

---

## Test Summary

### Overall Results

| Test Case | Status | Notes |
|-----------|--------|-------|
| TC1: Navigation | ⬜ Pass / ⬜ Fail | |
| TC2: Load All | ⬜ Pass / ⬜ Fail | |
| TC3: Filter Class | ⬜ Pass / ⬜ Fail | |
| TC4: Filter Class+Section | ⬜ Pass / ⬜ Fail | |
| TC5: No Data | ⬜ Pass / ⬜ Fail | |
| TC6: Network Error | ⬜ Pass / ⬜ Fail | |
| TC7: Enrollment Status | ⬜ Pass / ⬜ Fail | |
| TC8: Payment Status | ⬜ Pass / ⬜ Fail | |
| TC9: Conditional Fields | ⬜ Pass / ⬜ Fail | |
| TC10: Data Accuracy | ⬜ Pass / ⬜ Fail | |
| TC11: UI Responsiveness | ⬜ Pass / ⬜ Fail | |
| TC12: Large Dataset | ⬜ Pass / ⬜ Fail | |
| TC13: API Error | ⬜ Pass / ⬜ Fail | |
| TC14: Back Navigation | ⬜ Pass / ⬜ Fail | |
| TC15: Multiple Filters | ⬜ Pass / ⬜ Fail | |

**Total Passed:** _____ / 15  
**Total Failed:** _____ / 15  
**Pass Rate:** _____%

---

## Sign-off

**Tester Name:** _______________________  
**Date:** _______________________  
**Signature:** _______________________

**Approval:** ⬜ Approved / ⬜ Rejected / ⬜ Needs Revision

**Comments:**
```
_____________________________________________
_____________________________________________
_____________________________________________
```

---

**Document Version:** 1.0.0  
**Last Updated:** 2025-10-09

