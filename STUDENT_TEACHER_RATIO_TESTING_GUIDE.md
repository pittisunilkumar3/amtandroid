# Student Teacher Ratio Report - Testing Guide

## Overview
This guide provides comprehensive testing procedures for the Student Teacher Ratio Report feature in the Smart School Android application.

## Prerequisites

### 1. Environment Setup
- ✅ Android Studio installed
- ✅ Android device or emulator running
- ✅ API server running and accessible
- ✅ Valid authentication credentials
- ✅ Test data available in database

### 2. Test Data Requirements
- At least 2 classes with sections
- Students assigned to classes and sections
- Teachers assigned to classes via subject_timetable
- Active session configured

### 3. API Verification
Test the API endpoint before testing the Android app:

```bash
curl -X POST "http://localhost/amt/api/student-teacher-ratio-report/filter" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d '{}'
```

Expected response:
```json
{
  "status": 1,
  "message": "Student teacher ratio report retrieved successfully",
  "total_records": 5,
  "summary": {
    "total_students": 150,
    "total_boys": 80,
    "total_girls": 70,
    "total_teachers": 15,
    "boys_girls_ratio": "1:0.88",
    "student_teacher_ratio": "1:0.1"
  },
  "data": [...]
}
```

## Test Cases

### Test Case 1: Navigation to Report
**Objective**: Verify navigation to Student Teacher Ratio Report

**Steps**:
1. Launch the app
2. Login as teacher
3. Navigate to Teacher Dashboard
4. Click on "Reports" icon
5. Select "Student Information" category
6. Click on "Student Teacher Ratio Report"

**Expected Result**:
- StudentTeacherRatioActivity opens
- Screen shows title "Student Teacher Ratio Report"
- Filter dropdowns are visible (Session, Class, Section)
- "Generate Report" button is visible
- No data is displayed initially

**Logcat Verification**:
```
D/ReportItemAdapter: Launching StudentTeacherRatioActivity
```

**Status**: [ ] Pass [ ] Fail

---

### Test Case 2: Load All Records (No Filters)
**Objective**: Verify loading all ratio statistics without filters

**Steps**:
1. Navigate to Student Teacher Ratio Report
2. Do not select any filters
3. Click "Generate Report" button
4. Wait for data to load

**Expected Result**:
- Loading indicator appears
- API request sent with empty body `{}`
- Data loads successfully
- RecyclerView displays all ratio statistics
- Each card shows:
  - Class - Section name
  - Total students, boys, girls
  - Total teachers
  - Boys:Girls ratio
  - Student:Teacher ratio
  - Class ID and Section ID
- Toast shows summary statistics
- No errors displayed

**Logcat Verification**:
```
D/StudentTeacherRatio: loadReportData called
D/StudentTeacherRatio: === API Request Details ===
D/StudentTeacherRatio: Request Body: {}
D/StudentTeacherRatio: === API Response Received ===
D/StudentTeacherRatio: Status: 1
D/StudentTeacherRatio: Total Records: X
D/StudentTeacherRatio: Processing X records
D/StudentTeacherRatio: Showing content...
```

**Status**: [ ] Pass [ ] Fail

---

### Test Case 3: Filter by Session
**Objective**: Verify filtering by session

**Steps**:
1. Navigate to Student Teacher Ratio Report
2. Select a session from Session dropdown
3. Click "Generate Report" button
4. Wait for data to load

**Expected Result**:
- Loading indicator appears
- API request includes session_id
- Data loads for selected session
- All displayed records belong to selected session
- Toast shows summary for filtered data

**Logcat Verification**:
```
D/StudentTeacherRatio: Session ID: 18
D/StudentTeacherRatio: Request Body: {"session_id":18}
```

**Status**: [ ] Pass [ ] Fail

---

### Test Case 4: Filter by Class
**Objective**: Verify filtering by class

**Steps**:
1. Navigate to Student Teacher Ratio Report
2. Select a class from Class dropdown
3. Click "Generate Report" button
4. Wait for data to load

**Expected Result**:
- Loading indicator appears
- API request includes class_id
- Data loads for selected class
- All displayed records belong to selected class
- Multiple sections of the class are shown (if available)
- Toast shows summary for filtered data

**Logcat Verification**:
```
D/StudentTeacherRatio: Class ID: 1
D/StudentTeacherRatio: Request Body: {"class_id":1}
```

**Status**: [ ] Pass [ ] Fail

---

### Test Case 5: Filter by Class and Section
**Objective**: Verify filtering by both class and section

**Steps**:
1. Navigate to Student Teacher Ratio Report
2. Select a class from Class dropdown
3. Select a section from Section dropdown
4. Click "Generate Report" button
5. Wait for data to load

**Expected Result**:
- Loading indicator appears
- API request includes both class_id and section_id
- Data loads for selected class-section
- Single record displayed (or no data if section has no students)
- Toast shows summary for the section

**Logcat Verification**:
```
D/StudentTeacherRatio: Class ID: 1
D/StudentTeacherRatio: Section ID: 2
D/StudentTeacherRatio: Request Body: {"class_id":1,"section_id":2}
```

**Status**: [ ] Pass [ ] Fail

---

### Test Case 6: No Data Scenario
**Objective**: Verify behavior when no data matches filters

**Steps**:
1. Navigate to Student Teacher Ratio Report
2. Select filters that have no matching data
3. Click "Generate Report" button
4. Wait for response

**Expected Result**:
- Loading indicator appears
- API returns empty data array
- "No data" state is displayed
- Appropriate message shown: "No data found for selected filters"
- No crash or error

**Logcat Verification**:
```
D/StudentTeacherRatio: Data Array Length: 0
W/StudentTeacherRatio: Data array is null or empty
```

**Status**: [ ] Pass [ ] Fail

---

### Test Case 7: Network Error
**Objective**: Verify error handling for network issues

**Steps**:
1. Disable network connection (WiFi and mobile data)
2. Navigate to Student Teacher Ratio Report
3. Click "Generate Report" button
4. Wait for error

**Expected Result**:
- Loading indicator appears
- Network error detected
- Error message displayed: "Network error. Please check your internet connection."
- "No data" state shown
- App does not crash

**Logcat Verification**:
```
E/StudentTeacherRatio: === API Error ===
E/StudentTeacherRatio: Network error - no response from server
```

**Status**: [ ] Pass [ ] Fail

---

### Test Case 8: API Error (Status 0)
**Objective**: Verify handling of API errors

**Steps**:
1. Ensure API returns status 0 (modify API temporarily or use invalid filters)
2. Navigate to Student Teacher Ratio Report
3. Click "Generate Report" button
4. Wait for response

**Expected Result**:
- Loading indicator appears
- API returns status 0
- Error message from API displayed
- "No data" state shown
- App does not crash

**Logcat Verification**:
```
E/StudentTeacherRatio: API returned status 0: [error message]
```

**Status**: [ ] Pass [ ] Fail

---

### Test Case 9: Data Display Verification
**Objective**: Verify correct display of all data fields

**Steps**:
1. Navigate to Student Teacher Ratio Report
2. Load data with known values
3. Verify each field in the first card

**Expected Result**:
Each card displays:
- ✅ Class - Section name (bold, 18sp)
- ✅ Total Students count
- ✅ Boys count
- ✅ Girls count
- ✅ Total Teachers count
- ✅ Boys:Girls Ratio (green, bold)
- ✅ Student:Teacher Ratio (green, bold)
- ✅ Class ID (small gray text)
- ✅ Section ID (small gray text)

**Verification**:
- Compare displayed values with API response
- Check formatting and styling
- Verify all sections are visible
- Check dividers between sections

**Status**: [ ] Pass [ ] Fail

---

### Test Case 10: Summary Toast Verification
**Objective**: Verify summary information in Toast

**Steps**:
1. Navigate to Student Teacher Ratio Report
2. Load data successfully
3. Read the Toast message

**Expected Result**:
- Toast appears after data loads
- Toast shows:
  - Total students count
  - Total boys count
  - Total girls count
  - Total teachers count
  - Overall boys:girls ratio
  - Overall student:teacher ratio
- Format: "Total: X students (Y boys, Z girls), T teachers\nRatios - Boys:Girls: 1:X, Student:Teacher: 1:Y"

**Status**: [ ] Pass [ ] Fail

---

### Test Case 11: Ratio Calculation Verification
**Objective**: Verify ratio calculations are correct

**Steps**:
1. Navigate to Student Teacher Ratio Report
2. Load data for a specific class-section
3. Note the values:
   - Boys count
   - Girls count
   - Total students
   - Total teachers
4. Manually calculate ratios
5. Compare with displayed ratios

**Expected Result**:
- Boys:Girls ratio matches manual calculation
- Student:Teacher ratio matches manual calculation
- Ratios are in "1:X" format
- Ratios are displayed in green color

**Manual Calculation**:
- Boys:Girls = Boys / Girls (e.g., 25/20 = 1.25, displayed as "1:0.8")
- Student:Teacher = Teachers / Students (e.g., 5/45 = 0.11, displayed as "1:0.11")

**Status**: [ ] Pass [ ] Fail

---

### Test Case 12: Scroll Performance
**Objective**: Verify smooth scrolling with large datasets

**Steps**:
1. Navigate to Student Teacher Ratio Report
2. Load all records (should have 10+ records)
3. Scroll through the list
4. Scroll to top and bottom multiple times

**Expected Result**:
- Smooth scrolling without lag
- No frame drops
- All items render correctly
- No memory issues
- RecyclerView recycles views efficiently

**Status**: [ ] Pass [ ] Fail

---

### Test Case 13: Filter Change and Reload
**Objective**: Verify changing filters and reloading data

**Steps**:
1. Navigate to Student Teacher Ratio Report
2. Load data with Class filter
3. Change to different Class
4. Click "Generate Report" again
5. Verify new data loads

**Expected Result**:
- Previous data is cleared
- Loading indicator appears
- New data loads for new filter
- RecyclerView updates correctly
- No duplicate data

**Status**: [ ] Pass [ ] Fail

---

### Test Case 14: Rotation Test
**Objective**: Verify behavior on device rotation

**Steps**:
1. Navigate to Student Teacher Ratio Report
2. Load data successfully
3. Rotate device (portrait ↔ landscape)
4. Verify data persistence

**Expected Result**:
- Data remains visible after rotation
- Layout adjusts appropriately
- No data loss
- No crash

**Status**: [ ] Pass [ ] Fail

---

### Test Case 15: Back Navigation
**Objective**: Verify back navigation works correctly

**Steps**:
1. Navigate to Student Teacher Ratio Report
2. Load data
3. Press back button
4. Verify navigation

**Expected Result**:
- Returns to previous screen (TeacherReportCategoryActivity)
- No crash
- No memory leak

**Status**: [ ] Pass [ ] Fail

---

## Test Results Summary

| Test Case | Status | Notes |
|-----------|--------|-------|
| TC1: Navigation | [ ] | |
| TC2: Load All Records | [ ] | |
| TC3: Filter by Session | [ ] | |
| TC4: Filter by Class | [ ] | |
| TC5: Filter by Class and Section | [ ] | |
| TC6: No Data Scenario | [ ] | |
| TC7: Network Error | [ ] | |
| TC8: API Error | [ ] | |
| TC9: Data Display | [ ] | |
| TC10: Summary Toast | [ ] | |
| TC11: Ratio Calculation | [ ] | |
| TC12: Scroll Performance | [ ] | |
| TC13: Filter Change | [ ] | |
| TC14: Rotation Test | [ ] | |
| TC15: Back Navigation | [ ] | |

**Overall Status**: [ ] All Pass [ ] Some Fail

---

## Bug Report Template

If any test fails, use this template to report:

```
**Bug ID**: STR-XXX
**Test Case**: [Test Case Number and Name]
**Severity**: [Critical/High/Medium/Low]
**Priority**: [High/Medium/Low]

**Description**:
[Brief description of the issue]

**Steps to Reproduce**:
1. [Step 1]
2. [Step 2]
3. [Step 3]

**Expected Result**:
[What should happen]

**Actual Result**:
[What actually happened]

**Logcat Output**:
```
[Paste relevant logcat output]
```

**Screenshots**:
[Attach screenshots if applicable]

**Device Info**:
- Device: [Device name]
- Android Version: [Version]
- App Version: [Version]

**Additional Notes**:
[Any other relevant information]
```

---

## Logcat Monitoring

### Filter by Tag
```bash
adb logcat -s StudentTeacherRatio
```

### Full Logcat
```bash
adb logcat > student_teacher_ratio_test.log
```

### Clear Logcat
```bash
adb logcat -c
```

---

## Performance Metrics

### Acceptable Performance
- API response time: < 2 seconds
- UI render time: < 100ms
- Scroll FPS: > 55 FPS
- Memory usage: < 50MB increase

### Monitoring
```bash
# Monitor memory
adb shell dumpsys meminfo com.qdocs.ssre241123

# Monitor CPU
adb shell top | grep com.qdocs.ssre241123
```

---

## Test Completion Checklist

- [ ] All 15 test cases executed
- [ ] Test results documented
- [ ] Bugs reported (if any)
- [ ] Performance metrics recorded
- [ ] Logcat logs saved
- [ ] Screenshots captured
- [ ] Test summary prepared

---

**Testing Date**: _______________
**Tester Name**: _______________
**App Version**: _______________
**API Version**: _______________

---

**Status**: Ready for Testing ✅

