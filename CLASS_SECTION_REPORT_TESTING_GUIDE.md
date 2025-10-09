# Class Section Report - Testing Guide

## Quick Start Testing

### Prerequisites
1. ✅ API server running at configured URL
2. ✅ Class Section Report API endpoint implemented
3. ✅ Database has class and section data
4. ✅ Android app compiled and installed

### Test Environment
- **API Endpoint**: `POST /api/class-section-report/filter`
- **Authentication**: `Client-Service: smartschool`, `Auth-Key: schoolAdmin@`
- **Test Data**: Classes with sections and enrolled students

---

## Test Cases

### Test Case 1: Navigate to Class Section Report
**Objective**: Verify navigation to the report screen

**Steps**:
1. Launch the app and login as Teacher
2. Navigate to Teacher Dashboard
3. Scroll to "Tools & Reports" section
4. Click on "Reports" icon
5. Click on "Student Information" category
6. Scroll to find "Class & Section Report"
7. Click on "Class & Section Report"

**Expected Result**:
- ✅ ClassSectionReportActivity opens
- ✅ Title shows "Class & Section Report"
- ✅ Three filter dropdowns visible (Session, Class, Section)
- ✅ "Generate Report" button visible
- ✅ No data message shown initially

**Status**: [ ] Pass [ ] Fail

---

### Test Case 2: Load All Class Sections (No Filters)
**Objective**: Test loading all class sections without filters

**Steps**:
1. Open Class Section Report screen
2. Do NOT select any filters (leave all as default)
3. Click "Generate Report" button

**Expected Result**:
- ✅ Loading indicator appears
- ✅ API request sent with empty body `{}`
- ✅ All class sections displayed in list
- ✅ Each card shows: Class name, Section name, Student count
- ✅ Toast shows summary: "Found X class(es), Y section(s) with Z student(s)"
- ✅ List is scrollable if many items

**API Request**:
```json
{}
```

**Status**: [ ] Pass [ ] Fail

---

### Test Case 3: Filter by Session Only
**Objective**: Test filtering by session

**Steps**:
1. Open Class Section Report screen
2. Select a session from Session dropdown
3. Leave Class and Section as default
4. Click "Generate Report" button

**Expected Result**:
- ✅ Loading indicator appears
- ✅ API request includes session_id
- ✅ Class sections for selected session displayed
- ✅ Summary shows correct counts
- ✅ All sections belong to selected session

**API Request**:
```json
{
  "session_id": 18
}
```

**Status**: [ ] Pass [ ] Fail

---

### Test Case 4: Filter by Class
**Objective**: Test filtering by specific class

**Steps**:
1. Open Class Section Report screen
2. Select a session (if required)
3. Select a class from Class dropdown
4. Leave Section as default
5. Click "Generate Report" button

**Expected Result**:
- ✅ Loading indicator appears
- ✅ API request includes class_id
- ✅ Only sections for selected class displayed
- ✅ All displayed sections belong to selected class
- ✅ Summary shows correct counts

**API Request**:
```json
{
  "session_id": 18,
  "class_id": 10
}
```

**Status**: [ ] Pass [ ] Fail

---

### Test Case 5: Filter by Class and Section
**Objective**: Test filtering by specific class and section

**Steps**:
1. Open Class Section Report screen
2. Select a session
3. Select a class
4. Select a section from Section dropdown
5. Click "Generate Report" button

**Expected Result**:
- ✅ Loading indicator appears
- ✅ API request includes class_id and section_id
- ✅ Single section displayed (or multiple if section filter allows)
- ✅ Displayed section matches selected filters
- ✅ Summary shows correct counts

**API Request**:
```json
{
  "session_id": 18,
  "class_id": 10,
  "section_id": 15
}
```

**Status**: [ ] Pass [ ] Fail

---

### Test Case 6: No Data Scenario
**Objective**: Test behavior when no data matches filters

**Steps**:
1. Open Class Section Report screen
2. Select filters that have no matching data
3. Click "Generate Report" button

**Expected Result**:
- ✅ Loading indicator appears
- ✅ API request sent successfully
- ✅ "No data" message displayed
- ✅ Toast shows "No class sections found for selected filters"
- ✅ No crash or error

**Status**: [ ] Pass [ ] Fail

---

### Test Case 7: Network Error Handling
**Objective**: Test error handling when network is unavailable

**Steps**:
1. Disable device network (WiFi and Mobile data)
2. Open Class Section Report screen
3. Click "Generate Report" button

**Expected Result**:
- ✅ Loading indicator appears briefly
- ✅ Error message displayed
- ✅ Toast shows "Network error. Please check your internet connection."
- ✅ "No data" state shown
- ✅ No app crash

**Status**: [ ] Pass [ ] Fail

---

### Test Case 8: API Error Handling
**Objective**: Test error handling when API returns error

**Steps**:
1. Configure API to return error response (status: 0)
2. Open Class Section Report screen
3. Click "Generate Report" button

**Expected Result**:
- ✅ Loading indicator appears
- ✅ Error message from API displayed in Toast
- ✅ "No data" state shown
- ✅ Error logged in Logcat
- ✅ No app crash

**Status**: [ ] Pass [ ] Fail

---

### Test Case 9: Data Display Verification
**Objective**: Verify all data fields are displayed correctly

**Steps**:
1. Load class sections with known data
2. Verify each card displays correct information

**Expected Result**:
- ✅ Class name displayed (bold, large text)
- ✅ Section name displayed (medium text)
- ✅ Class-Section combined format shown
- ✅ Student count displayed with label
- ✅ Class ID shown (small gray text)
- ✅ Section ID shown (small gray text)
- ✅ Status badge shown (Active/Inactive)
- ✅ All data matches API response

**Status**: [ ] Pass [ ] Fail

---

### Test Case 10: UI Responsiveness
**Objective**: Test UI behavior and responsiveness

**Steps**:
1. Load class sections
2. Scroll through the list
3. Rotate device (portrait/landscape)
4. Navigate back and forth

**Expected Result**:
- ✅ Smooth scrolling
- ✅ No lag or stuttering
- ✅ Cards properly sized and spaced
- ✅ Data persists on rotation
- ✅ Back button works correctly
- ✅ Animations smooth

**Status**: [ ] Pass [ ] Fail

---

## Logcat Verification

### Expected Log Messages

#### On Report Load
```
D/ClassSectionReport: loadReportData called
D/ClassSectionReport: Session ID: 18
D/ClassSectionReport: Class ID: 10
D/ClassSectionReport: Section ID: 15
D/ClassSectionReport: === API Request Details ===
D/ClassSectionReport: Base URL: http://localhost/amt/api/
D/ClassSectionReport: Full API URL: http://localhost/amt/api/class-section-report/filter
D/ClassSectionReport: === Request Headers ===
D/ClassSectionReport: Client-Service: smartschool
D/ClassSectionReport: Auth-Key: schoolAdmin@
D/ClassSectionReport: Request Body: {"session_id":18,"class_id":10,"section_id":15}
```

#### On Successful Response
```
D/ClassSectionReport: === API Response Received ===
D/ClassSectionReport: Response Length: 1234
D/ClassSectionReport: === Parsing Response ===
D/ClassSectionReport: Status: 1
D/ClassSectionReport: Total Records: 7
D/ClassSectionReport: Processing 7 class sections
D/ClassSectionReport: First Class Section: JR-BIPC - 08199-JR-BIPC-B1
D/ClassSectionReport: Class section list size: 7
D/ClassSectionReport: Notifying adapter...
D/ClassSectionReport: Showing content...
D/ClassSectionReport: Success message: Found 1 class(es), 7 section(s) with 42 student(s)
```

#### On Error
```
E/ClassSectionReport: === API Error ===
E/ClassSectionReport: Status Code: 500
E/ClassSectionReport: Error Response: {"status":0,"message":"Internal server error"}
E/ClassSectionReport: Error Details: com.android.volley.ServerError
```

---

## API Testing with cURL

### Test 1: All Class Sections
```bash
curl -X POST "http://localhost/amt/api/class-section-report/filter" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d '{}'
```

### Test 2: Filter by Class
```bash
curl -X POST "http://localhost/amt/api/class-section-report/filter" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d '{"class_id": 10}'
```

### Test 3: Filter by Class and Section
```bash
curl -X POST "http://localhost/amt/api/class-section-report/filter" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d '{"class_id": 10, "section_id": 15}'
```

---

## Checklist

### Pre-Testing
- [ ] API server is running
- [ ] Database has test data
- [ ] App is compiled and installed
- [ ] Logcat is ready for monitoring

### During Testing
- [ ] All test cases executed
- [ ] Screenshots captured for each test
- [ ] Logcat logs saved
- [ ] Issues documented

### Post-Testing
- [ ] All test cases passed
- [ ] Issues reported and tracked
- [ ] Documentation updated
- [ ] Code reviewed

---

## Known Issues

### Issue Template
**Issue #**: [Number]
**Title**: [Brief description]
**Severity**: [Critical/High/Medium/Low]
**Steps to Reproduce**:
1. Step 1
2. Step 2
3. Step 3

**Expected**: [What should happen]
**Actual**: [What actually happens]
**Logs**: [Relevant log messages]
**Status**: [Open/In Progress/Resolved]

---

## Test Results Summary

| Test Case | Status | Notes |
|-----------|--------|-------|
| TC1: Navigation | [ ] | |
| TC2: Load All | [ ] | |
| TC3: Filter Session | [ ] | |
| TC4: Filter Class | [ ] | |
| TC5: Filter Class+Section | [ ] | |
| TC6: No Data | [ ] | |
| TC7: Network Error | [ ] | |
| TC8: API Error | [ ] | |
| TC9: Data Display | [ ] | |
| TC10: UI Responsiveness | [ ] | |

**Overall Status**: [ ] All Pass [ ] Some Fail [ ] Not Tested

**Tested By**: _______________
**Date**: _______________
**Build Version**: _______________

---

## Next Steps

After successful testing:
1. ✅ Mark all test cases as passed
2. ✅ Document any issues found
3. ✅ Update implementation if needed
4. ✅ Prepare for production deployment
5. ✅ Create user documentation
6. ✅ Train users on new feature

---

**Happy Testing! 🚀**

