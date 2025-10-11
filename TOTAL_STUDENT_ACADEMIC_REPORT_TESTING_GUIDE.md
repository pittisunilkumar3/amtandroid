# Total Student Academic Report - Testing Guide

## Quick Start Testing

### Prerequisites
1. App installed and running
2. Teacher account logged in
3. Backend API configured and running
4. Test data available in database

---

## Test Scenario 1: Basic Navigation

### Steps:
1. Open the app and login as teacher
2. From Teacher Dashboard, tap on "Reports" icon
3. Tap on "Finance" category
4. Scroll down to find "Total Balance Fees Report" (3rd item)
5. Tap on "Total Balance Fees Report"

### Expected Results:
- ✅ TotalBalanceFeesReportActivity opens
- ✅ Title shows "Total Balance Fees Report"
- ✅ Filters card is visible with Session, Class, Section spinners
- ✅ Generate Report button is visible
- ✅ No crashes or errors

---

## Test Scenario 2: Load Filter Options

### Steps:
1. Navigate to Total Balance Fees Report (as above)
2. Observe the spinners loading

### Expected Results:
- ✅ Session spinner populates with available sessions
- ✅ Class spinner populates with available classes
- ✅ Section spinner initially shows "Select Section"
- ✅ All spinners are clickable

---

## Test Scenario 3: Generate Report with No Filters

### Steps:
1. Navigate to Total Balance Fees Report
2. Without selecting any filters, tap "Generate Report" button
3. Wait for response

### Expected Results:
- ✅ Progress bar shows while loading
- ✅ Report loads with all students from all classes and sessions
- ✅ Student cards display in RecyclerView
- ✅ Toast message shows "Report loaded: X students"
- ✅ Each card shows:
  - Student name
  - Admission number
  - Roll number
  - Class and section
  - Father name
  - Total fee
  - Deposit
  - Discount
  - Fine
  - Balance (color-coded)

---

## Test Scenario 4: Generate Report with Session Filter

### Steps:
1. Navigate to Total Balance Fees Report
2. Select a session from Session spinner
3. Tap "Generate Report" button
4. Wait for response

### Expected Results:
- ✅ Report loads with students from selected session only
- ✅ Student count matches expected number
- ✅ All displayed students belong to selected session

---

## Test Scenario 5: Generate Report with Class Filter

### Steps:
1. Navigate to Total Balance Fees Report
2. Select a class from Class spinner
3. Observe Section spinner updates
4. Tap "Generate Report" button
5. Wait for response

### Expected Results:
- ✅ Section spinner updates with sections for selected class
- ✅ Report loads with students from selected class only
- ✅ All displayed students belong to selected class

---

## Test Scenario 6: Generate Report with All Filters

### Steps:
1. Navigate to Total Balance Fees Report
2. Select a session from Session spinner
3. Select a class from Class spinner
4. Select a section from Section spinner
5. Tap "Generate Report" button
6. Wait for response

### Expected Results:
- ✅ Report loads with students matching all filters
- ✅ Student count is filtered correctly
- ✅ All displayed students match the filter criteria

---

## Test Scenario 7: Empty Result Handling

### Steps:
1. Navigate to Total Balance Fees Report
2. Select filters that have no students (e.g., empty class)
3. Tap "Generate Report" button
4. Wait for response

### Expected Results:
- ✅ "No data available" message displays
- ✅ No crash or error
- ✅ Toast shows "No students found"
- ✅ Can select different filters and try again

---

## Test Scenario 8: Visual Verification

### Steps:
1. Generate a report with students who have:
   - Positive balance (due amount)
   - Zero balance (fully paid)
   - Negative balance (overpaid)
2. Observe the balance colors

### Expected Results:
- ✅ Positive balance shows in RED
- ✅ Zero/negative balance shows in GREEN
- ✅ Theme color applied to card headers
- ✅ Currency symbol displays correctly
- ✅ Numbers formatted with commas (e.g., 10,000.00)

---

## Test Scenario 9: Scrolling and Performance

### Steps:
1. Generate a report with many students (50+)
2. Scroll through the list
3. Scroll to bottom
4. Scroll back to top

### Expected Results:
- ✅ Smooth scrolling
- ✅ No lag or stuttering
- ✅ All cards render correctly
- ✅ No memory issues

---

## Test Scenario 10: Back Navigation

### Steps:
1. Navigate to Total Balance Fees Report
2. Generate a report
3. Tap back button

### Expected Results:
- ✅ Returns to Finance Reports list
- ✅ Smooth transition animation
- ✅ No crash

---

## Test Scenario 11: API Error Handling

### Steps:
1. Disconnect from network or stop backend server
2. Navigate to Total Balance Fees Report
3. Tap "Generate Report" button
4. Wait for response

### Expected Results:
- ✅ Error message displays
- ✅ "No data available" state shows
- ✅ No crash
- ✅ Can retry after reconnecting

---

## Test Scenario 12: Data Accuracy

### Steps:
1. Generate a report for a specific student
2. Verify the displayed data against database/backend
3. Check calculations:
   - Balance = (Total Fee - Discount) - Deposit + Fine

### Expected Results:
- ✅ Student name matches
- ✅ Admission number matches
- ✅ Class and section match
- ✅ Fee amounts match
- ✅ Balance calculation is correct

---

## API Testing with cURL

### Test 1: Get All Students
```bash
curl -X POST "http://your-server/api/total-student-academic-report/filter" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d "{}"
```

### Test 2: Filter by Class
```bash
curl -X POST "http://your-server/api/total-student-academic-report/filter" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d '{"class_id":"1"}'
```

### Test 3: Filter by Class and Section
```bash
curl -X POST "http://your-server/api/total-student-academic-report/filter" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d '{"class_id":"1","section_id":"1"}'
```

### Test 4: Get Filter Options
```bash
curl -X POST "http://your-server/api/total-student-academic-report/list" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d "{}"
```

---

## Debugging Tips

### Enable Logging
Check LogCat for these tags:
- `TotalBalanceFeesReport` - Activity logs
- `BaseFinanceReport` - Base class logs
- `Volley` - Network request logs

### Common Issues

**Issue 1: Filters not loading**
- Check: API endpoint configured correctly
- Check: Network connectivity
- Check: Authentication headers

**Issue 2: Empty results when data exists**
- Check: Filter parameters being sent correctly
- Check: API response format matches expected structure
- Check: JSON parsing in `parseReportResponse()`

**Issue 3: Balance not color-coded**
- Check: Balance value is numeric
- Check: Color resources exist
- Check: Theme colors configured

**Issue 4: Crash on opening**
- Check: All required views exist in layout
- Check: Adapter initialized before setting
- Check: RecyclerView layout manager set

---

## Performance Benchmarks

### Expected Performance:
- **Load time:** < 2 seconds for 50 students
- **Scroll FPS:** 60 FPS
- **Memory usage:** < 50 MB additional
- **Network request:** < 1 second

---

## Regression Testing

After any changes, verify:
1. Other finance reports still work
2. Navigation to/from report works
3. Theme colors still apply
4. Currency formatting consistent
5. Filter behavior unchanged

---

## Sign-off Checklist

Before marking as complete:
- [ ] All test scenarios pass
- [ ] No crashes or ANRs
- [ ] UI matches design specifications
- [ ] Data accuracy verified
- [ ] Performance acceptable
- [ ] Error handling works
- [ ] Back navigation works
- [ ] Theme colors apply correctly
- [ ] Currency formatting correct
- [ ] API integration verified

---

## Test Results Template

```
Test Date: ___________
Tester: ___________
App Version: ___________
Backend Version: ___________

Scenario 1: [ ] Pass [ ] Fail - Notes: ___________
Scenario 2: [ ] Pass [ ] Fail - Notes: ___________
Scenario 3: [ ] Pass [ ] Fail - Notes: ___________
Scenario 4: [ ] Pass [ ] Fail - Notes: ___________
Scenario 5: [ ] Pass [ ] Fail - Notes: ___________
Scenario 6: [ ] Pass [ ] Fail - Notes: ___________
Scenario 7: [ ] Pass [ ] Fail - Notes: ___________
Scenario 8: [ ] Pass [ ] Fail - Notes: ___________
Scenario 9: [ ] Pass [ ] Fail - Notes: ___________
Scenario 10: [ ] Pass [ ] Fail - Notes: ___________
Scenario 11: [ ] Pass [ ] Fail - Notes: ___________
Scenario 12: [ ] Pass [ ] Fail - Notes: ___________

Overall Result: [ ] Pass [ ] Fail
```

