# Staff Attendance Report - Testing Guide

## 📋 Test Plan Overview

This guide provides comprehensive testing procedures for the Staff Attendance Report feature in the Smart School Android app.

**Feature:** Staff Attendance Report  
**Status:** Ready for Testing  
**Build Status:** ✅ Successful

---

## 🎯 Test Scope

### Components to Test
1. ✅ UI Components (Spinners, Date Pickers, Buttons)
2. ✅ API Integration (List & Filter endpoints)
3. ✅ Data Display (RecyclerView, Adapters)
4. ✅ Filter Logic (Role, Date, Attendance Type)
5. ✅ Error Handling (Network, Empty States)
6. ✅ User Interactions (Clicks, Selections)

---

## 🧪 Test Cases

### TC-001: Open Staff Attendance Report

**Objective:** Verify the activity launches successfully

**Steps:**
1. Navigate to Reports → Attendance
2. Tap "Staff Attendance Report"

**Expected Result:**
- Activity opens without crash
- Filter card is visible
- Initial state shows "All Roles" selected
- Date fields show "Select Date"
- "All Types" is selected for attendance

**Status:** [ ]

---

### TC-002: Load All Staff Attendance

**Objective:** Verify all staff attendance records load on first open

**Steps:**
1. Open Staff Attendance Report
2. Wait for data to load (or observe progress bar)

**Expected Result:**
- Progress bar shows during loading
- All active staff attendance records appear
- Summary card shows total record count
- No data layout is hidden
- RecyclerView is visible

**API Call:**
```
POST /api/staff-attendance-report/list
Body: {}
```

**Status:** [ ]

---

### TC-003: Filter by Role - Teacher

**Objective:** Verify filtering by specific role works

**Steps:**
1. Open Staff Attendance Report
2. Select "Teacher" from Role spinner
3. Tap "Generate Report"

**Expected Result:**
- Only teacher attendance records displayed
- Summary shows correct count
- All displayed records have role = "Teacher"

**API Call:**
```json
POST /api/staff-attendance-report/filter
Body: {"role_id": 2}
```

**Status:** [ ]

---

### TC-004: Filter by Date Range

**Objective:** Verify date range filtering works

**Steps:**
1. Open Staff Attendance Report
2. Tap "From Date" → Select date (e.g., Oct 1, 2025)
3. Tap "To Date" → Select date (e.g., Oct 7, 2025)
4. Tap "Generate Report"

**Expected Result:**
- Only records within date range displayed
- Date format shows as "dd MMM yyyy"
- Summary shows correct filtered count

**API Call:**
```json
POST /api/staff-attendance-report/filter
Body: {
  "from_date": "2025-10-01",
  "to_date": "2025-10-07"
}
```

**Status:** [ ]

---

### TC-005: Filter by Attendance Type - Present

**Objective:** Verify attendance type filtering works

**Steps:**
1. Open Staff Attendance Report
2. Select "Present" from Attendance Type spinner
3. Tap "Generate Report"

**Expected Result:**
- Only "Present" records displayed
- All attendance badges show green color
- Attendance type text shows "Present"

**API Call:**
```json
POST /api/staff-attendance-report/filter
Body: {"attendance_type": "present"}
```

**Status:** [ ]

---

### TC-006: Multiple Filters Combined

**Objective:** Verify multiple filters work together

**Steps:**
1. Open Staff Attendance Report
2. Select "Teacher" from Role spinner
3. Select date range: Oct 1 - Oct 7, 2025
4. Select "Present" from Attendance Type
5. Tap "Generate Report"

**Expected Result:**
- Only teachers marked present in date range
- Summary shows applied filters
- All conditions met in displayed records

**API Call:**
```json
POST /api/staff-attendance-report/filter
Body: {
  "role_id": 2,
  "from_date": "2025-10-01",
  "to_date": "2025-10-07",
  "attendance_type": "present"
}
```

**Status:** [ ]

---

### TC-007: Clear Filters

**Objective:** Verify clear filters button resets all selections

**Steps:**
1. Apply multiple filters (role, date, attendance type)
2. Tap "Clear" button

**Expected Result:**
- Role spinner resets to "All Roles"
- Date fields show "Select Date" in gray
- Attendance Type resets to "All Types"
- All attendance records reload

**Status:** [ ]

---

### TC-008: Empty State - No Data Found

**Objective:** Verify empty state displays when no data matches filters

**Steps:**
1. Apply filters that return no results
   - Example: Select future date range with no records
2. Tap "Generate Report"

**Expected Result:**
- No data layout becomes visible
- RecyclerView is hidden
- Message: "No staff attendance data found"
- Helpful text: "Try adjusting your filters or select a different date range"

**Status:** [ ]

---

### TC-009: Date Picker - From Date

**Objective:** Verify from date picker works correctly

**Steps:**
1. Tap "From Date" field
2. DatePickerDialog should open
3. Select a date
4. Tap OK

**Expected Result:**
- DatePickerDialog opens
- Selected date shows in field as "dd MMM yyyy"
- Text color changes from gray to black
- Date stored in "yyyy-MM-dd" format internally

**Status:** [ ]

---

### TC-010: Date Picker - To Date

**Objective:** Verify to date picker works correctly

**Steps:**
1. Tap "To Date" field
2. DatePickerDialog should open
3. Select a date
4. Tap OK

**Expected Result:**
- DatePickerDialog opens
- Selected date shows in field as "dd MMM yyyy"
- Text color changes from gray to black
- Date stored in "yyyy-MM-dd" format internally

**Status:** [ ]

---

### TC-011: Attendance Badge Color - Present

**Objective:** Verify present attendance shows green badge

**Steps:**
1. Filter to show only "Present" records
2. Observe attendance type badges

**Expected Result:**
- Badge background: Light green (#E8F5E9)
- Badge text: Green (#4CAF50)
- Text reads "Present"

**Status:** [ ]

---

### TC-012: Attendance Badge Color - Absent

**Objective:** Verify absent attendance shows red badge

**Steps:**
1. Filter to show only "Absent" records
2. Observe attendance type badges

**Expected Result:**
- Badge background: Light red (#FFEBEE)
- Badge text: Red (#F44336)
- Text reads "Absent"

**Status:** [ ]

---

### TC-013: Attendance Badge Color - Late

**Objective:** Verify late attendance shows orange badge

**Steps:**
1. Filter to show only "Late" records
2. Observe attendance type badges

**Expected Result:**
- Badge background: Light orange (#FFF3E0)
- Badge text: Orange (#FF9800)
- Text reads "Late"

**Status:** [ ]

---

### TC-014: Attendance Badge Color - Half Day

**Objective:** Verify half day attendance shows blue badge

**Steps:**
1. Filter to show only "Half Day" records
2. Observe attendance type badges

**Expected Result:**
- Badge background: Light blue (#E3F2FD)
- Badge text: Blue (#2196F3)
- Text reads "Half Day" or "Half_Day"

**Status:** [ ]

---

### TC-015: Staff Information Display

**Objective:** Verify all staff fields display correctly

**Steps:**
1. Open Staff Attendance Report
2. Observe a staff attendance item

**Expected Result:**
- Staff name shows (bold, large)
- Employee ID shows below name (small, gray)
- Role shows with user icon
- Department shows with building icon
- Designation shows with ID card icon
- Date shows with calendar icon
- Attendance type badge visible

**Status:** [ ]

---

### TC-016: Optional Field - Remark Display

**Objective:** Verify remark shows when available

**Steps:**
1. Find a record with remark in API response
2. Observe the list item

**Expected Result:**
- Remark field is visible
- Text shows "Remark: [remark text]"
- Background is light gray
- Text is italic

**Status:** [ ]

---

### TC-017: Optional Field - Remark Hidden

**Objective:** Verify remark hides when empty

**Steps:**
1. Find a record without remark
2. Observe the list item

**Expected Result:**
- Remark field is hidden (GONE)
- No extra spacing where remark would be

**Status:** [ ]

---

### TC-018: Summary Card Display

**Objective:** Verify summary card shows correct information

**Steps:**
1. Load staff attendance (with or without filters)
2. Observe summary card

**Expected Result:**
- Summary card is visible
- Shows "Total Records: [count]"
- If filters applied, shows which filters

**Status:** [ ]

---

### TC-019: Loading State

**Objective:** Verify loading indicator displays during API call

**Steps:**
1. Apply filters
2. Tap "Generate Report"
3. Observe immediately

**Expected Result:**
- Progress bar becomes visible
- RecyclerView is hidden
- No data layout is hidden
- Summary card is hidden

**Status:** [ ]

---

### TC-020: Network Error Handling

**Objective:** Verify proper error handling on network failure

**Steps:**
1. Turn off internet/wifi
2. Open Staff Attendance Report or tap Generate Report

**Expected Result:**
- Toast message: "No Internet Connection" or similar
- No data layout shows
- No crash

**Status:** [ ]

---

### TC-021: API Error Response Handling

**Objective:** Verify handling of API error status

**Steps:**
1. Simulate API returning status: 0
2. Observe app behavior

**Expected Result:**
- Error message from API displayed in toast
- No data layout shows
- No crash
- Error logged in Logcat

**Status:** [ ]

---

### TC-022: RecyclerView Scrolling

**Objective:** Verify smooth scrolling with many records

**Steps:**
1. Load report with many records (50+)
2. Scroll through the list

**Expected Result:**
- Smooth scrolling, no lag
- Items render properly
- No memory leaks
- Images/icons load correctly

**Status:** [ ]

---

### TC-023: Back Button Navigation

**Objective:** Verify back button returns to previous screen

**Steps:**
1. Open Staff Attendance Report
2. Press device back button

**Expected Result:**
- Returns to previous screen
- Slide animation plays
- No crash

**Status:** [ ]

---

### TC-024: Screen Rotation

**Objective:** Verify data persists on screen rotation

**Steps:**
1. Load staff attendance with filters
2. Rotate device

**Expected Result:**
- Data remains visible
- Filters remain selected
- No re-API call (optional enhancement)
- No crash

**Status:** [ ]

---

### TC-025: Role Filter - All Roles

**Objective:** Verify "All Roles" selection shows all staff

**Steps:**
1. Select "All Roles" from spinner
2. Tap "Generate Report"

**Expected Result:**
- All staff roles displayed
- Teachers, Admins, Accountants, etc. all visible
- No role_id sent in API request

**Status:** [ ]

---

## 🔍 API Testing

### API Test 1: List Endpoint

**Request:**
```bash
curl -X POST "http://localhost/amt/api/staff-attendance-report/list" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d '{}'
```

**Expected Response:**
```json
{
  "status": 1,
  "message": "Staff attendance report retrieved successfully",
  "total_records": 50,
  "data": [...]
}
```

**Verify:**
- [ ] Status code: 200
- [ ] status field: 1
- [ ] data array contains records
- [ ] total_records matches data length

---

### API Test 2: Filter by Role

**Request:**
```bash
curl -X POST "http://localhost/amt/api/staff-attendance-report/filter" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d '{"role_id": 2}'
```

**Verify:**
- [ ] All records have role_id = "2"
- [ ] filters_applied contains role_id
- [ ] total_records is correct

---

### API Test 3: Filter by Date Range

**Request:**
```bash
curl -X POST "http://localhost/amt/api/staff-attendance-report/filter" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d '{
    "from_date": "2025-10-01",
    "to_date": "2025-10-07"
  }'
```

**Verify:**
- [ ] All records fall within date range
- [ ] Date format is yyyy-MM-dd
- [ ] filters_applied contains from_date and to_date

---

### API Test 4: Filter by Attendance Type

**Request:**
```bash
curl -X POST "http://localhost/amt/api/staff-attendance-report/filter" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d '{"attendance_type": "present"}'
```

**Verify:**
- [ ] All records have attendance_type = "Present"
- [ ] filters_applied contains attendance_type
- [ ] Case handling works (lowercase in request)

---

### API Test 5: Multiple Filters

**Request:**
```bash
curl -X POST "http://localhost/amt/api/staff-attendance-report/filter" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d '{
    "role_id": 2,
    "from_date": "2025-10-01",
    "to_date": "2025-10-07",
    "attendance_type": "present"
  }'
```

**Verify:**
- [ ] All conditions met simultaneously
- [ ] filters_applied shows all filters
- [ ] Correct total_records count

---

## 📱 Device Testing Matrix

| Device Type | Screen Size | Android Version | Status |
|-------------|-------------|-----------------|--------|
| Phone | 5.5" | Android 8.0 | [ ] |
| Phone | 6.0" | Android 10 | [ ] |
| Phone | 6.5" | Android 11 | [ ] |
| Phone | 6.7" | Android 12 | [ ] |
| Tablet | 7" | Android 10 | [ ] |
| Tablet | 10" | Android 11 | [ ] |

---

## 🐛 Known Issues / Edge Cases

### Edge Case 1: Empty Employee ID
**Scenario:** Staff record has no employee_id  
**Expected:** Employee ID field should hide (GONE)  
**Status:** [ ]

### Edge Case 2: Empty Department
**Scenario:** Staff record has no department  
**Expected:** Department field should hide (GONE)  
**Status:** [ ]

### Edge Case 3: Very Long Names
**Scenario:** Staff name is very long (30+ characters)  
**Expected:** Text wraps or truncates gracefully  
**Status:** [ ]

### Edge Case 4: Special Characters in Remark
**Scenario:** Remark contains special characters  
**Expected:** Displays correctly without breaking UI  
**Status:** [ ]

### Edge Case 5: Same From and To Date
**Scenario:** User selects same date for both  
**Expected:** Shows attendance for that single day  
**Status:** [ ]

---

## 🔧 Debugging Checklist

### If No Data Loads:
- [ ] Check Logcat for "StaffAttendanceReport" tag
- [ ] Verify API URL is correct
- [ ] Check network connection
- [ ] Verify authentication headers
- [ ] Check API response in Logcat

### If Filters Don't Work:
- [ ] Check selectedRoleId, selectedFromDate, etc. values in Logcat
- [ ] Verify JSON body construction
- [ ] Check API request body in Logcat
- [ ] Verify filter parameters match API expectations

### If Colors Don't Show:
- [ ] Check attendance_type field in API response
- [ ] Verify switch statement matches API values
- [ ] Check color resources exist in colors.xml

---

## 📊 Test Results Summary

**Total Test Cases:** 25  
**Passed:** ___  
**Failed:** ___  
**Blocked:** ___  
**Not Tested:** ___

**Overall Status:** ⬜ Not Started | 🟡 In Progress | ✅ Complete

---

## 🎯 Acceptance Criteria

✅ **Must Have:**
- [ ] All staff attendance records load successfully
- [ ] Role filter works correctly
- [ ] Date range filter works correctly
- [ ] Attendance type filter works correctly
- [ ] Clear filters resets all selections
- [ ] Attendance badges show correct colors
- [ ] No crashes during normal operation

✅ **Should Have:**
- [ ] Summary shows correct record count
- [ ] Applied filters displayed in summary
- [ ] Empty state shows helpful message
- [ ] Loading indicator displays during API call
- [ ] Optional fields hide when empty

✅ **Nice to Have:**
- [ ] Smooth scrolling with many records
- [ ] Data persists on screen rotation
- [ ] Graceful error handling
- [ ] Responsive on different screen sizes

---

## 📝 Test Sign-Off

| Role | Name | Date | Signature |
|------|------|------|-----------|
| QA Lead | _________ | ______ | _________ |
| Developer | _________ | ______ | _________ |
| Product Owner | _________ | ______ | _________ |

---

**Test Plan Version:** 1.0  
**Last Updated:** October 2025  
**Status:** Ready for Testing ✅
