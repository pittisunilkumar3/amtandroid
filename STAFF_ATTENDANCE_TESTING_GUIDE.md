# Staff Attendance Report - Testing Guide

## Quick Start Testing

### 1. Launch the Report
1. Open the Android app
2. Navigate to Staff Attendance Report from the menu
3. You should see the filter card with three spinners (Role, Month, Year)

### 2. Test Basic Report Generation

#### Test Case 1: Generate Report with All Filters
**Steps:**
1. Select "Super Admin" from Role spinner
2. Select "October" from Month spinner
3. Select "2025" from Year spinner
4. Click "Generate Report" button

**Expected Result:**
- Progress bar shows while loading
- RecyclerView displays staff attendance records
- Each card shows:
  - Staff name and employee ID
  - Role
  - Attendance percentage (large number with color)
  - Status text (Good/Poor/Average)
  - Summary counts: P: X, A: X, L: X, H: X, HD: X
  - Working days info
  - Horizontal scrollable daily attendance markers

**Check Logs:**
```
Look for these log entries:
=== GENERATING REPORT ===
Role: Super Admin
Month: October (Number: 10)
Year: 2025
Has filters: true

=== FINAL REQUEST BODY ===
{"role":"admin","month":"October","month_number":10,"year":2025}
=========================

=== API RESPONSE START ===
[Full JSON response]
=== API RESPONSE END ===
```

#### Test Case 2: Generate Report with No Filters
**Steps:**
1. Ensure all spinners are at "All" position (All Roles, All Months, All Years)
2. Click "Generate Report" button

**Expected Result:**
- Should load all staff attendance data
- Request body should be empty: `{}`

#### Test Case 3: Generate Report with Only Month Filter
**Steps:**
1. Keep Role at "All Roles"
2. Select "October" from Month spinner
3. Keep Year at "All Years"
4. Click "Generate Report" button

**Expected Result:**
- Request body should contain: `{"month":"October","month_number":10}`
- Should show attendance for all staff in October across all years

### 3. Test UI Display

#### Test Case 4: Verify Attendance Summary Display
**Steps:**
1. Generate a report with filters
2. Look at the first staff card

**Verify:**
- [ ] Staff name is displayed correctly
- [ ] Employee ID shows with "ID: " prefix
- [ ] Role is displayed
- [ ] Percentage is a large number with % symbol
- [ ] Percentage color is:
  - Green for good attendance (≥75%)
  - Red for poor attendance (<50%)
  - Gray for average (50-74%)
- [ ] Status text matches percentage color
- [ ] P: (Present count) is displayed in green
- [ ] A: (Absent count) is displayed in red
- [ ] L: (Late count) is displayed in orange
- [ ] H: (Half Day count) is displayed in blue
- [ ] HD: (Holiday count) is displayed in gray
- [ ] Working Days info shows two numbers

#### Test Case 5: Verify Daily Attendance Markers
**Steps:**
1. Look at the horizontal scroll area in a staff card
2. Scroll left and right

**Verify:**
- [ ] Each day shows a number (1-31)
- [ ] Each day shows an attendance marker (P, A, L, F, H, or -)
- [ ] Markers have colored backgrounds:
  - Light green for Present (P)
  - Light red for Absent (A)
  - Light yellow for Late (L)
  - Light blue for Half Day (F)
  - Light gray for Holiday (H)
  - Gray for Not Marked (-)
- [ ] All days of the month are visible (scroll to see all)
- [ ] Header text says "Daily Attendance (Scroll to view all days)"

#### Test Case 6: Test Calendar Dialog
**Steps:**
1. Click on any staff card
2. A dialog should pop up

**Verify:**
- [ ] Dialog shows staff name, employee ID, role
- [ ] Large percentage and status are displayed
- [ ] Attendance summary shows all counts
- [ ] Calendar grid displays with 7 columns (Mon-Sun)
- [ ] Each day in calendar shows:
  - Day number
  - Attendance marker (P, A, L, F, H, -)
  - Day abbreviation (Mon, Tue, etc.)
  - Colored background matching attendance type
- [ ] Legend at bottom explains color codes
- [ ] Close button dismisses the dialog

### 4. Test Filter Functionality

#### Test Case 7: Test Role Filter
**Steps:**
1. Select different roles from the Role spinner
2. Click "Generate Report" for each

**Verify:**
- [ ] "All Roles" sends empty role field
- [ ] "Super Admin" sends `"role":"admin"`
- [ ] "Teacher" sends `"role":"teacher"`
- [ ] "Accountant" sends `"role":"accountant"`
- [ ] "Librarian" sends `"role":"librarian"`
- [ ] "Receptionist" sends `"role":"receptionist"`

#### Test Case 8: Test Month Filter
**Steps:**
1. Select different months from the Month spinner
2. Click "Generate Report" for each

**Verify:**
- [ ] "All Months" sends no month fields
- [ ] "January" sends `"month":"January","month_number":1`
- [ ] "February" sends `"month":"February","month_number":2`
- [ ] "October" sends `"month":"October","month_number":10`
- [ ] "December" sends `"month":"December","month_number":12`

#### Test Case 9: Test Clear Filters
**Steps:**
1. Select some filters (Role, Month, Year)
2. Click "Clear Filters" button

**Verify:**
- [ ] All spinners reset to "All" position
- [ ] No data is displayed (initial state)
- [ ] User must click "Generate Report" to see data again

### 5. Test Error Handling

#### Test Case 10: No Internet Connection
**Steps:**
1. Turn off internet/WiFi
2. Click "Generate Report"

**Expected Result:**
- Toast message: "No internet connection"
- No data displayed

#### Test Case 11: API Error Response
**Steps:**
1. Use invalid filters that return no data
2. Click "Generate Report"

**Expected Result:**
- "No data" layout is displayed
- Message: "No staff attendance data found"
- Suggestion: "Try adjusting your filters or select a different date range"

### 6. Performance Testing

#### Test Case 12: Large Dataset
**Steps:**
1. Generate report with no filters (all staff, all months)
2. Observe loading time and scrolling performance

**Verify:**
- [ ] Progress bar shows during loading
- [ ] RecyclerView scrolls smoothly
- [ ] No lag when scrolling through many staff records
- [ ] Horizontal scroll in each card works smoothly

#### Test Case 13: Memory Usage
**Steps:**
1. Generate multiple reports with different filters
2. Open and close calendar dialogs multiple times
3. Navigate away and back to the report

**Verify:**
- [ ] No memory leaks
- [ ] App doesn't crash
- [ ] Data refreshes correctly when returning to screen

## Debugging Tips

### Check Logcat Filters
Use these tags to filter logs:
- `MonthlyStaffAttendance` - Main activity logs
- `MonthlyStaffAdapter` - Adapter logs

### Common Issues and Solutions

**Issue: No data displayed after clicking Generate Report**
- Check logs for API response
- Verify internet connection
- Check if API returned status: 1
- Verify dates array is not empty

**Issue: Attendance summary shows all zeros**
- Check logs for "Attendance summary is NULL!"
- Verify API response includes attendance_summary object
- Check if summary parsing is working correctly

**Issue: Daily attendance markers not showing**
- Check logs for "Dates list is EMPTY!"
- Verify dates array is populated from API
- Check if daily_attendance map is not null
- Verify date keys match between dates array and daily_attendance map

**Issue: Wrong data sent to API**
- Check logs for "=== FINAL REQUEST BODY ==="
- Verify month_number is included when month is selected
- Verify role is converted to lowercase
- Verify year is sent as integer, not string

**Issue: Calendar dialog crashes**
- Check for null pointer exceptions in logs
- Verify attendance summary null check is in place
- Verify daily attendance map is not null

## API Response Validation

### Sample Valid Response Structure
```json
{
    "status": 1,
    "message": "Success",
    "attendance_types": [...],
    "total_staff": 5,
    "total_days": 31,
    "dates": ["2025-10-01", "2025-10-02", ...],
    "data": [
        {
            "staff_id": "1",
            "staff_info": {
                "name": "John",
                "surname": "Doe",
                "employee_id": "EMP001",
                "role": "Teacher"
            },
            "daily_attendance": {
                "2025-10-01": {
                    "attendance_type": "Present",
                    "attendance_key": "<b>P</b>",
                    "day_name": "Tuesday",
                    "day_short": "Tue"
                }
            },
            "attendance_summary": {
                "Present": 22,
                "Absent": 3,
                "Late": 2,
                "Half Day": 1,
                "Holiday": 3
            },
            "attendance_percentage": 85.5,
            "attendance_percentage_display": 85,
            "attendance_status": "Good",
            "attendance_status_class": "success",
            "total_working_days": 28,
            "total_present_days": 25
        }
    ]
}
```

### Validate Response Fields
- [ ] `status` is 1 for success
- [ ] `dates` array is not empty
- [ ] `data` array contains staff records
- [ ] Each staff has `staff_info` object
- [ ] Each staff has `daily_attendance` object
- [ ] Each staff has `attendance_summary` object
- [ ] Percentage values are numeric
- [ ] Status class is "success", "danger", or "warning"

## Sign-off Checklist

Before marking the feature as complete:
- [ ] All test cases pass
- [ ] No crashes or exceptions
- [ ] UI displays all data correctly
- [ ] Filters work as expected
- [ ] API integration is correct
- [ ] Logging is comprehensive
- [ ] Performance is acceptable
- [ ] Error handling works properly
- [ ] Calendar dialog works correctly
- [ ] Code is properly documented

## Contact for Issues

If you encounter any issues during testing:
1. Capture logcat output with relevant tags
2. Take screenshots of the issue
3. Note the exact steps to reproduce
4. Document the expected vs actual behavior
5. Check if the issue is consistent or intermittent

