# Staff Attendance Report - API Testing Scenarios

## API Endpoint
**URL:** `POST https://school.cyberdetox.in/api/monthly-staff-attendance/report`

**Headers:**
```
Client-Service: smartschool
Auth-Key: schoolAdmin@
Content-Type: application/json
```

---

## Test Scenarios

### Scenario 1: All Filters Selected (Specific Month & Year)
**UI Selection:**
- Role: Accountant
- Month: August
- Year: 2024

**Expected Request Body:**
```json
{
    "role": "accountant",
    "month": "August",
    "month_number": 8,
    "year": 2024
}
```

**Expected Behavior:**
- ✅ Request includes role, month, month_number, and year
- ✅ API returns data for Accountants in August 2024 only
- ✅ UI displays: "Period: August 2024"
- ✅ UI displays: "Role: Accountant"

---

### Scenario 2: All Months Selected (Should NOT send month)
**UI Selection:**
- Role: Accountant
- Month: All Months
- Year: 2024

**Expected Request Body:**
```json
{
    "role": "accountant",
    "year": 2024
}
```

**Expected Behavior:**
- ✅ Request does NOT include "month" or "month_number"
- ✅ API returns data for Accountants for entire year 2024
- ✅ UI displays: "Period: 2024"
- ✅ UI displays: "Role: Accountant"
- ⚠️ Month field should NOT be sent when "All Months" is selected

---

### Scenario 3: All Years Selected (Should NOT send year)
**UI Selection:**
- Role: Accountant
- Month: August
- Year: All Years

**Expected Request Body:**
```json
{
    "role": "accountant",
    "month": "August",
    "month_number": 8
}
```

**Expected Behavior:**
- ✅ Request does NOT include "year"
- ✅ API returns data for Accountants in August across all years
- ✅ UI displays: "Period: August"
- ✅ UI displays: "Role: Accountant"
- ⚠️ Year field should NOT be sent when "All Years" is selected

---

### Scenario 4: All Roles Selected
**UI Selection:**
- Role: All Roles
- Month: August
- Year: 2024

**Expected Request Body:**
```json
{
    "month": "August",
    "month_number": 8,
    "year": 2024
}
```

**Expected Behavior:**
- ✅ Request does NOT include "role"
- ✅ API returns data for all staff roles in August 2024
- ✅ UI displays: "Period: August 2024"
- ✅ UI does NOT display role filter
- ⚠️ Role field should NOT be sent when "All Roles" is selected

---

### Scenario 5: All Filters Set to "All"
**UI Selection:**
- Role: All Roles
- Month: All Months
- Year: All Years

**Expected Request Body:**
```json
{}
```

**Expected Behavior:**
- ✅ Request body is empty JSON object
- ✅ API returns all staff attendance data
- ✅ UI does NOT display "Period: ..." (no specific period)
- ✅ UI does NOT display role filter
- ⚠️ No filters should be sent to API

---

### Scenario 6: Only Month Selected
**UI Selection:**
- Role: All Roles
- Month: October
- Year: All Years

**Expected Request Body:**
```json
{
    "month": "October",
    "month_number": 10
}
```

**Expected Behavior:**
- ✅ Request includes only month and month_number
- ✅ API returns data for October across all years and roles
- ✅ UI displays: "Period: October"
- ✅ UI does NOT display role filter

---

### Scenario 7: Only Year Selected
**UI Selection:**
- Role: All Roles
- Month: All Months
- Year: 2024

**Expected Request Body:**
```json
{
    "year": 2024
}
```

**Expected Behavior:**
- ✅ Request includes only year
- ✅ API returns data for entire year 2024 for all roles
- ✅ UI displays: "Period: 2024"
- ✅ UI does NOT display role filter

---

### Scenario 8: Only Role Selected
**UI Selection:**
- Role: Teacher
- Month: All Months
- Year: All Years

**Expected Request Body:**
```json
{
    "role": "teacher"
}
```

**Expected Behavior:**
- ✅ Request includes only role
- ✅ API returns data for Teachers across all months and years
- ✅ UI does NOT display "Period: ..." (no specific period)
- ✅ UI displays: "Role: Teacher"

---

### Scenario 9: Month and Year (No Role)
**UI Selection:**
- Role: All Roles
- Month: December
- Year: 2023

**Expected Request Body:**
```json
{
    "month": "December",
    "month_number": 12,
    "year": 2023
}
```

**Expected Behavior:**
- ✅ Request includes month, month_number, and year
- ✅ API returns data for December 2023 for all roles
- ✅ UI displays: "Period: December 2023"
- ✅ UI does NOT display role filter

---

### Scenario 10: Role and Month (No Year)
**UI Selection:**
- Role: Super Admin
- Month: January
- Year: All Years

**Expected Request Body:**
```json
{
    "role": "admin",
    "month": "January",
    "month_number": 1
}
```

**Expected Behavior:**
- ✅ Request includes role, month, and month_number
- ✅ Role "Super Admin" is mapped to "admin"
- ✅ API returns data for Admins in January across all years
- ✅ UI displays: "Period: January"
- ✅ UI displays: "Role: Super Admin"

---

## UI Display Rules

### Period Display (periodTv)
**Show when:**
- Month is selected (not "All Months") OR
- Year is selected (not "All Years")

**Format:**
- Month only: "Period: October"
- Year only: "Period: 2024"
- Both: "Period: October 2024"

**Hide when:**
- Both Month and Year are "All"

### Role Filter Display (filtersAppliedTv)
**Show when:**
- Role is selected (not "All Roles")

**Format:**
- "Role: Accountant"
- "Role: Teacher"
- "Role: Super Admin"

**Hide when:**
- Role is "All Roles"

---

## Testing Checklist

### Request Body Validation
- [ ] "All Roles" does NOT send "role" field
- [ ] "All Months" does NOT send "month" or "month_number" fields
- [ ] "All Years" does NOT send "year" field
- [ ] Month name is sent as string (e.g., "August")
- [ ] Month number is sent as integer (e.g., 8)
- [ ] Year is sent as integer (e.g., 2024)
- [ ] Role is sent in lowercase (e.g., "teacher", "admin")
- [ ] "Super Admin" is mapped to "admin"

### UI Display Validation
- [ ] Period shows selected month and/or year
- [ ] Period is hidden when both are "All"
- [ ] Role filter shows selected role
- [ ] Role filter is hidden when "All Roles"
- [ ] Total records count is displayed
- [ ] Staff cards show attendance data
- [ ] Percentage is color-coded correctly
- [ ] Summary counts (P, A, L, H, HD) are visible
- [ ] Daily attendance markers are visible

### API Response Validation
- [ ] API returns status: 1 for success
- [ ] API returns dates array
- [ ] API returns data array with staff records
- [ ] Each staff has daily_attendance object
- [ ] Each staff has attendance_summary object
- [ ] Attendance percentage is calculated
- [ ] Status class is provided (success/danger/default)

---

## Logging for Debugging

### Check Logs for:
```
MonthlyStaffAttendance: === GENERATING REPORT ===
MonthlyStaffAttendance: Role: [selected role]
MonthlyStaffAttendance: Month: [selected month] (Number: [month number])
MonthlyStaffAttendance: Year: [selected year]
MonthlyStaffAttendance: Has role filter: [true/false]
MonthlyStaffAttendance: Has month filter: [true/false]
MonthlyStaffAttendance: Has year filter: [true/false]
MonthlyStaffAttendance: Has any filters: [true/false]
MonthlyStaffAttendance: === FINAL REQUEST BODY ===
MonthlyStaffAttendance: [JSON body]
MonthlyStaffAttendance: Displaying period: [period text]
```

---

## Expected API Response Structure

```json
{
    "status": 1,
    "message": "Success",
    "attendance_types": [...],
    "total_staff": 2,
    "total_days": 31,
    "dates": ["2024-08-01", "2024-08-02", ...],
    "data": [
        {
            "staff_id": "1",
            "staff_info": {
                "name": "MAHA LAKSHMI",
                "surname": "SALLA",
                "employee_id": "200226",
                "role": "Accountant"
            },
            "daily_attendance": {
                "2024-08-03": {
                    "attendance_type": "Present",
                    "attendance_key": "<b>P</b>",
                    "day_name": "Saturday",
                    "day_short": "Sat"
                }
            },
            "attendance_summary": {
                "Present": 15,
                "Absent": 0,
                "Late": 0,
                "Half Day": 0,
                "Holiday": 0
            },
            "attendance_percentage": 100.0,
            "attendance_percentage_display": 100,
            "attendance_status": "Good",
            "attendance_status_class": "success",
            "total_working_days": 15,
            "total_present_days": 15
        }
    ]
}
```

---

## Common Issues & Solutions

### Issue: Month filter not working
**Cause:** Sending "All Months" to API
**Solution:** Check that "All Months" does NOT send month fields

### Issue: Wrong data displayed
**Cause:** Filters not properly excluded
**Solution:** Verify request body excludes "All" selections

### Issue: Period not showing
**Cause:** periodTv visibility logic
**Solution:** Check updateSummary() method logic

### Issue: Role mapping incorrect
**Cause:** Role name not converted to lowercase
**Solution:** Verify role mapping in getBody() method

---

## Test Execution Steps

1. **Build and install the app**
2. **Navigate to Staff Attendance Report**
3. **For each scenario above:**
   - Select the specified filters
   - Click "Generate Report"
   - Check logcat for request body
   - Verify API response
   - Verify UI display
   - Take screenshot
4. **Document any discrepancies**
5. **Verify all checkboxes above**

---

**Testing Status:** Ready for execution
**Last Updated:** 2025-10-13

