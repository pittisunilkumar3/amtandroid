# Other Collection Report - Testing Guide

## 🧪 Testing the Implementation

### Prerequisites
1. Backend API is running at `http://localhost/amt`
2. Android app is connected to the backend
3. Teacher account is logged in
4. Test data exists in the database

---

## 📱 Test Scenarios

### Test 1: Basic Report Generation
**Steps:**
1. Open Teacher Dashboard
2. Navigate to Reports → Finance
3. Find and click "Other Collection Report"
4. Click "Generate Report" without selecting any filters
5. Verify report loads with default filters (today's date)

**Expected Results:**
- ✅ Activity opens successfully
- ✅ All filter spinners are visible
- ✅ Default date is set to today
- ✅ Report generates on button click
- ✅ Summary card shows total records and amount
- ✅ Records are displayed in cards

---

### Test 2: Date Range Filters
**Steps:**
1. Open Other Collection Report
2. Test each search duration option:
   - Select "Today"
   - Click "Generate Report"
   - Verify results are for today
3. Repeat for:
   - "This Week"
   - "This Month"
   - "This Year"

**Expected Results:**
- ✅ Date fields update automatically
- ✅ Correct date range is sent to API
- ✅ Results match selected date range

---

### Test 3: Custom Date Range
**Steps:**
1. Open Other Collection Report
2. Select "Custom Duration" from search duration
3. Click "From Date" field
4. Select a start date
5. Click "To Date" field
6. Select an end date
7. Click "Generate Report"

**Expected Results:**
- ✅ Date pickers open correctly
- ✅ Selected dates are displayed
- ✅ Custom date range is sent to API
- ✅ Results match custom date range

---

### Test 4: Class and Section Filters
**Steps:**
1. Open Other Collection Report
2. Select a session from Session spinner
3. Select a class from Class spinner
4. Select a section from Section spinner
5. Click "Generate Report"

**Expected Results:**
- ✅ Session spinner loads sessions
- ✅ Class spinner loads classes for selected session
- ✅ Section spinner loads sections for selected class
- ✅ Results are filtered by selected class/section

---

### Test 5: Fee Type Filter
**Steps:**
1. Open Other Collection Report
2. Select a fee type from Fee Type spinner
3. Click "Generate Report"

**Expected Results:**
- ✅ Fee Type spinner loads available fee types
- ✅ Results show only selected fee type
- ✅ Fee type is displayed in each record

---

### Test 6: Collector Filter (Received By)
**Steps:**
1. Open Other Collection Report
2. Select a collector from Collect By spinner
3. Click "Generate Report"

**Expected Results:**
- ✅ Collect By spinner loads staff members
- ✅ Results show only collections by selected staff
- ✅ Collector name and employee ID are displayed

---

### Test 7: Grouping Options
**Steps:**
1. Open Other Collection Report
2. Test each grouping option:
   - Select "Group By Class"
   - Click "Generate Report"
   - Verify results are grouped by class
3. Repeat for:
   - "Group By Collection"
   - "Group By Payment Mode"

**Expected Results:**
- ✅ Results are grouped correctly
- ✅ Subtotals are calculated for each group
- ✅ All records are displayed

---

### Test 8: Combined Filters
**Steps:**
1. Open Other Collection Report
2. Select multiple filters:
   - Search Duration: "This Month"
   - Session: Select a session
   - Class: Select a class
   - Section: Select a section
   - Fee Type: Select a fee type
   - Collect By: Select a collector
   - Group By: Select "Group By Class"
3. Click "Generate Report"

**Expected Results:**
- ✅ All filters are applied correctly
- ✅ Results match all filter criteria
- ✅ Summary shows correct totals
- ✅ Records are grouped as selected

---

### Test 9: Empty Results
**Steps:**
1. Open Other Collection Report
2. Select filters that will return no results
   (e.g., future date range)
3. Click "Generate Report"

**Expected Results:**
- ✅ No data layout is displayed
- ✅ Message: "No data available"
- ✅ No crash or error

---

### Test 10: Data Display Verification
**Steps:**
1. Open Other Collection Report
2. Generate a report with data
3. Verify each record card shows:
   - Student name
   - Admission number
   - Class and section
   - Fee type
   - Fee group name
   - Total amount
   - Payment date
   - Payment mode
   - Received by (name and employee ID)
   - Amount breakdown (amount, discount, fine)

**Expected Results:**
- ✅ All fields are displayed correctly
- ✅ Dates are formatted properly
- ✅ Currency is formatted correctly
- ✅ Collector name includes employee ID
- ✅ Amount breakdown is accurate

---

## 🔍 API Request Verification

### Check Request Body
Use Logcat to verify the request body:

```
Tag: OtherCollectionReport
Message: Request Body: {...}
```

**Expected JSON:**
```json
{
  "search_type": "today",
  "session_id": "21",
  "class_id": "19",
  "section_id": "36",
  "feetype_id": "5",
  "received_by": "123",
  "group": "class"
}
```

### Check API Response
Use Logcat to verify the API response:

```
Tag: OtherCollectionReport
Message: Response: {...}
```

**Expected JSON:**
```json
{
  "status": 1,
  "message": "Other collection report retrieved successfully",
  "summary": {
    "total_records": 5,
    "total_amount": "15000.00"
  },
  "data": [...]
}
```

---

## 🐛 Common Issues and Solutions

### Issue 1: No Data Displayed
**Symptoms:** Report generates but shows "No data available"

**Solutions:**
1. Check if test data exists in database
2. Verify date range includes data
3. Check filter selections
4. Review API response in Logcat

### Issue 2: Filters Not Working
**Symptoms:** Filters don't affect results

**Solutions:**
1. Check if filter values are being sent in request
2. Verify parameter names match API specification
3. Check Logcat for request body
4. Verify API is processing filters

### Issue 3: Summary Not Showing
**Symptoms:** Summary card is not visible

**Solutions:**
1. Check if API response includes "summary" object
2. Verify summary card visibility is set to VISIBLE
3. Check if total amount is being parsed correctly

### Issue 4: Collector Name Not Showing
**Symptoms:** "Received by" shows ID instead of name

**Solutions:**
1. Check if API response includes "received_byname" object
2. Verify parsing of received_byname in parseCollectionItem()
3. Check getReceivedByDisplayName() method

### Issue 5: Date Not Formatted
**Symptoms:** Date shows as "2025-10-10" instead of "10 Oct 2025"

**Solutions:**
1. Check formatDate() method in adapter
2. Verify date field is being used (not created_at)
3. Check SimpleDateFormat patterns

---

## 📊 Test Data Requirements

### Minimum Test Data
1. At least 1 session
2. At least 1 class with sections
3. At least 1 "other" fee type (hostel, library, sports, etc.)
4. At least 1 staff member
5. At least 5 fee collection records with:
   - Different students
   - Different fee types
   - Different payment modes
   - Different collectors
   - Different dates

### Recommended Test Data
1. Multiple sessions
2. Multiple classes with multiple sections
3. Multiple fee types
4. Multiple staff members
5. 20+ fee collection records with variety

---

## ✅ Final Verification Checklist

- [ ] Report opens from Finance Reports menu
- [ ] All filters are visible and functional
- [ ] Date pickers work correctly
- [ ] Generate Report button works
- [ ] Summary card displays totals
- [ ] Records are displayed in cards
- [ ] All record fields are visible
- [ ] Dates are formatted correctly
- [ ] Currency is formatted correctly
- [ ] Collector name includes employee ID
- [ ] Amount breakdown is accurate
- [ ] Grouping works correctly
- [ ] Empty state is handled
- [ ] Loading state is shown
- [ ] Back button works
- [ ] No crashes or errors

---

## 📝 Test Report Template

```
Test Date: ___________
Tester: ___________
App Version: ___________
Backend Version: ___________

Test Results:
[ ] Test 1: Basic Report Generation - PASS/FAIL
[ ] Test 2: Date Range Filters - PASS/FAIL
[ ] Test 3: Custom Date Range - PASS/FAIL
[ ] Test 4: Class and Section Filters - PASS/FAIL
[ ] Test 5: Fee Type Filter - PASS/FAIL
[ ] Test 6: Collector Filter - PASS/FAIL
[ ] Test 7: Grouping Options - PASS/FAIL
[ ] Test 8: Combined Filters - PASS/FAIL
[ ] Test 9: Empty Results - PASS/FAIL
[ ] Test 10: Data Display Verification - PASS/FAIL

Issues Found:
1. ___________
2. ___________
3. ___________

Overall Status: PASS/FAIL
```

---

## 🎉 Success Criteria

The implementation is successful if:
1. ✅ All 10 test scenarios pass
2. ✅ No crashes or errors occur
3. ✅ Data is displayed correctly
4. ✅ Filters work as expected
5. ✅ API requests match specification
6. ✅ API responses are parsed correctly
7. ✅ UI is responsive and user-friendly

