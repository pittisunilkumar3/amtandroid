# Student History (Admission Report) Testing Guide

## 🎯 Quick Start

This guide provides step-by-step instructions for testing the Student History (Admission Report) feature.

---

## 📋 Prerequisites

- ✅ App installed on device/emulator
- ✅ Teacher account credentials
- ✅ Backend API running at configured URL
- ✅ Test data: Students with admission records in database

---

## 🧪 Test Scenarios

### Scenario 1: Basic Report Generation

**Objective:** Verify basic functionality of generating admission report

**Steps:**
1. Launch the app
2. Login with teacher credentials
3. Navigate to Teacher Dashboard
4. Scroll to "Tools & Reports" section
5. Click on "Reports" icon (📊 bar chart icon)
6. Click on "Student Information" category
7. Click on "Student History" report
8. Select a session from the dropdown
9. Wait for classes to populate
10. Select a class from the dropdown
11. Wait for sections to populate
12. Select a section from the dropdown
13. Click "Generate Report" button

**Expected Results:**
- ✅ Loading indicator appears
- ✅ API request sent successfully
- ✅ Toast message: "Found X admission record(s)"
- ✅ RecyclerView displays admission records
- ✅ Each card shows complete information

**Logcat Filter:**
```bash
adb logcat -s StudentHistoryActivity:D ReportItemAdapter:D
```

---

### Scenario 2: Data Verification

**Objective:** Verify all data fields are displayed correctly

**Steps:**
1. Generate report with valid filters (follow Scenario 1)
2. Examine the first card in the list
3. Verify the following fields are present:
   - Student full name (header)
   - Admission number
   - Admission date (in badge)
   - Class and section
   - Session name
   - Guardian name and relation
   - Student mobile number
   - Guardian phone number
   - Active/Inactive status

**Expected Results:**
- ✅ All fields display correct data
- ✅ Optional fields hidden if empty
- ✅ Status color-coded (Green=Active, Red=Inactive)
- ✅ Admission date formatted correctly
- ✅ Phone numbers display with emoji icons

---

### Scenario 3: Empty Results

**Objective:** Verify behavior when no records found

**Steps:**
1. Open Student History report
2. Select filters that have no students
   - Example: New session with no admissions
3. Click "Generate Report"

**Expected Results:**
- ✅ Loading indicator appears
- ✅ API request completes
- ✅ Toast: "No admission records found for selected filters"
- ✅ Empty state displayed
- ✅ No crash or error

---

### Scenario 4: Network Error Handling

**Objective:** Verify error handling for network issues

**Steps:**
1. Open Student History report
2. Enable Airplane mode or disable WiFi
3. Select filters
4. Click "Generate Report"

**Expected Results:**
- ✅ Loading indicator appears
- ✅ Error toast: "Network error. Please check your internet connection."
- ✅ Empty state displayed
- ✅ App remains functional

---

### Scenario 5: API Error Handling

**Objective:** Verify error handling for API errors

**Steps:**
1. Stop the backend server
2. Open Student History report
3. Select filters
4. Click "Generate Report"

**Expected Results:**
- ✅ Loading indicator appears
- ✅ Error toast with appropriate message
- ✅ Empty state displayed
- ✅ Error logged in Logcat

---

### Scenario 6: Dropdown Cascading

**Objective:** Verify cascading dropdown behavior

**Steps:**
1. Open Student History report
2. Observe initial state:
   - Session dropdown: Enabled with options
   - Class dropdown: Disabled/Empty
   - Section dropdown: Disabled/Empty
3. Select a session
4. Observe class dropdown populates
5. Select a class
6. Observe section dropdown populates
7. Change session selection
8. Observe class and section dropdowns reset

**Expected Results:**
- ✅ Dropdowns cascade correctly
- ✅ Dependent dropdowns reset on parent change
- ✅ Loading indicators during data fetch
- ✅ No crashes during selection changes

---

### Scenario 7: Multiple Records

**Objective:** Verify handling of large datasets

**Steps:**
1. Select filters with many students (50+)
2. Generate report
3. Scroll through the entire list
4. Verify smooth scrolling
5. Check memory usage

**Expected Results:**
- ✅ All records loaded successfully
- ✅ Smooth scrolling performance
- ✅ No memory leaks
- ✅ Correct record count in toast

---

### Scenario 8: Back Navigation

**Objective:** Verify navigation behavior

**Steps:**
1. Open Student History report
2. Generate report with data
3. Press back button
4. Verify returned to Student Information reports list
5. Click Student History again
6. Verify state is reset (no previous data)

**Expected Results:**
- ✅ Back navigation works correctly
- ✅ No data persistence between sessions
- ✅ No crashes
- ✅ Smooth transitions

---

### Scenario 9: Rotation Handling

**Objective:** Verify behavior on device rotation

**Steps:**
1. Open Student History report
2. Generate report with data
3. Rotate device to landscape
4. Verify data still displayed
5. Rotate back to portrait
6. Verify data still displayed

**Expected Results:**
- ✅ Data persists through rotation
- ✅ Layout adapts to orientation
- ✅ No crashes
- ✅ No duplicate API calls

---

### Scenario 10: API Response Validation

**Objective:** Verify correct API request format

**Steps:**
1. Enable Logcat monitoring
2. Open Student History report
3. Select: Session=18, Class=1, Section=2
4. Click "Generate Report"
5. Check Logcat for API request details

**Expected Logcat Output:**
```
D/StudentHistoryActivity: === API Request Details ===
D/StudentHistoryActivity: Base URL: http://localhost/amt/api/
D/StudentHistoryActivity: Full API URL: http://localhost/amt/api/admission-report/filter
D/StudentHistoryActivity: Session ID: 18
D/StudentHistoryActivity: Class ID: 1
D/StudentHistoryActivity: Section ID: 2
D/StudentHistoryActivity: === Request Headers ===
D/StudentHistoryActivity: Client-Service: smartschool
D/StudentHistoryActivity: Auth-Key: schoolAdmin@
D/StudentHistoryActivity: Content-Type: application/json
D/StudentHistoryActivity: Request Body: {"class_id":1,"session_id":18}
```

**Expected Results:**
- ✅ Correct API endpoint
- ✅ Correct headers
- ✅ Correct JSON body format
- ✅ Correct parameter values

---

## 🔍 Debugging Commands

### View All Logs
```bash
adb logcat -s StudentHistoryActivity:D
```

### View API Requests
```bash
adb logcat -s StudentHistoryActivity:D | grep "API Request"
```

### View API Responses
```bash
adb logcat -s StudentHistoryActivity:D | grep "Response"
```

### View Errors Only
```bash
adb logcat -s StudentHistoryActivity:E
```

### Clear Logs
```bash
adb logcat -c
```

---

## 📊 Test Data Requirements

### Minimum Test Data Needed:

1. **Sessions:** At least 2 active sessions
2. **Classes:** At least 3 classes per session
3. **Sections:** At least 2 sections per class
4. **Students:** At least 10 students with admission records
5. **Admission Dates:** Various dates to test sorting
6. **Guardian Info:** Complete guardian information
7. **Status:** Mix of active and inactive students

---

## ✅ Test Checklist

Use this checklist to track testing progress:

- [ ] Basic report generation works
- [ ] All data fields display correctly
- [ ] Empty results handled properly
- [ ] Network errors handled gracefully
- [ ] API errors handled gracefully
- [ ] Dropdown cascading works correctly
- [ ] Multiple records display properly
- [ ] Back navigation works
- [ ] Device rotation handled
- [ ] API request format correct
- [ ] Loading indicators work
- [ ] Toast messages appropriate
- [ ] No memory leaks
- [ ] No crashes
- [ ] Performance acceptable

---

## 🐛 Common Issues and Solutions

### Issue 1: "No admission records found" but data exists

**Possible Causes:**
- Wrong session/class/section selected
- API endpoint incorrect
- Database has no admission_date values
- API filtering logic issue

**Solution:**
- Check Logcat for API request/response
- Verify database has admission records
- Test API directly with Postman/curl

---

### Issue 2: App crashes on "Generate Report"

**Possible Causes:**
- Null pointer exception
- JSON parsing error
- Network timeout

**Solution:**
- Check Logcat for stack trace
- Verify API response format
- Check internet connection

---

### Issue 3: Dropdowns not populating

**Possible Causes:**
- API endpoint for sessions/classes not working
- Network error
- Authentication failure

**Solution:**
- Check TeacherReportDetailActivity logs
- Verify API endpoints
- Check authentication headers

---

### Issue 4: Data not displaying in cards

**Possible Causes:**
- Adapter not notified
- RecyclerView not initialized
- Layout inflation error

**Solution:**
- Check adapter.notifyDataSetChanged() called
- Verify RecyclerView setup
- Check layout XML for errors

---

## 📞 Support

### Logcat Commands:
```bash
# View all logs
adb logcat

# Filter by app package
adb logcat | grep "com.qdocs.ssre241123"

# Filter by tag
adb logcat -s StudentHistoryActivity:D

# Save logs to file
adb logcat -d > logs.txt
```

### Build Commands:
```bash
# Clean build
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Install APK
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Uninstall app
adb uninstall com.qdocs.ssre241123
```

---

## 📝 Test Report Template

```
Test Date: _______________
Tester Name: _______________
Device: _______________
Android Version: _______________
App Version: _______________

Test Results:
[ ] Scenario 1: Basic Report Generation - PASS/FAIL
[ ] Scenario 2: Data Verification - PASS/FAIL
[ ] Scenario 3: Empty Results - PASS/FAIL
[ ] Scenario 4: Network Error - PASS/FAIL
[ ] Scenario 5: API Error - PASS/FAIL
[ ] Scenario 6: Dropdown Cascading - PASS/FAIL
[ ] Scenario 7: Multiple Records - PASS/FAIL
[ ] Scenario 8: Back Navigation - PASS/FAIL
[ ] Scenario 9: Rotation Handling - PASS/FAIL
[ ] Scenario 10: API Validation - PASS/FAIL

Issues Found:
1. _______________
2. _______________
3. _______________

Overall Status: PASS/FAIL

Notes:
_______________
_______________
_______________
```

---

**Last Updated:** October 9, 2025  
**Version:** 1.0  
**Status:** Ready for Testing

