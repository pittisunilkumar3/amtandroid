# Collection Report - Testing Guide

## 🧪 Testing Checklist

### Pre-Testing Setup
- [ ] Ensure API endpoint is configured correctly in backend
- [ ] Verify authentication headers are set (Client-Service, Auth-Key)
- [ ] Check that base URL is configured in app settings
- [ ] Ensure test data exists in database

---

## 📱 Manual Testing Steps

### 1. Navigation Test
**Steps:**
1. Open Smart School app
2. Login as teacher
3. Navigate to Reports menu
4. Find and tap "Finance" category
5. Tap "Fee Collection Report"

**Expected Result:**
- Activity opens successfully
- Filter card is visible
- All filter spinners are populated
- Generate Report button is visible

---

### 2. Empty Request Test (Default Behavior)
**Steps:**
1. Open Fee Collection Report
2. Don't select any filters
3. Click "Generate Report"

**Expected Result:**
- API called with empty request `{}`
- Returns current month's collection data
- Records displayed in RecyclerView
- Toast shows "Report loaded: X records"

---

### 3. Search Duration Filter Test

#### Test 3.1: Today
**Steps:**
1. Select "Today" from Search Duration
2. Click "Generate Report"

**Expected Result:**
- API called with `search_type: "today"`
- Returns today's collections
- Date range shown in filters_applied

#### Test 3.2: This Week
**Steps:**
1. Select "This Week" from Search Duration
2. Click "Generate Report"

**Expected Result:**
- API called with `search_type: "this_week"`
- Returns current week's collections

#### Test 3.3: This Month
**Steps:**
1. Select "This Month" from Search Duration
2. Click "Generate Report"

**Expected Result:**
- API called with `search_type: "this_month"`
- Returns current month's collections

#### Test 3.4: Custom Period
**Steps:**
1. Select "Custom Period" from Search Duration
2. Select From Date (e.g., Oct 1, 2025)
3. Select To Date (e.g., Oct 31, 2025)
4. Click "Generate Report"

**Expected Result:**
- API called with `date_from` and `date_to`
- Returns collections within date range

---

### 4. Session Filter Test
**Steps:**
1. Select a session from Session dropdown
2. Click "Generate Report"

**Expected Result:**
- API called with `session_id`
- Returns collections for selected session
- Records filtered by session

---

### 5. Class Filter Test
**Steps:**
1. Select a class from Class dropdown
2. Wait for sections to load
3. Click "Generate Report"

**Expected Result:**
- Section dropdown populated with sections for selected class
- API called with `class_id`
- Returns collections for selected class

---

### 6. Section Filter Test
**Steps:**
1. Select a class
2. Select a section from Section dropdown
3. Click "Generate Report"

**Expected Result:**
- API called with `class_id` and `section_id`
- Returns collections for selected class and section

---

### 7. Fee Type Filter Test
**Steps:**
1. Select a fee type from Fee Type dropdown
2. Click "Generate Report"

**Expected Result:**
- API called with `feetype_id`
- Returns collections for selected fee type
- All records show the selected fee type

---

### 8. Collected By Filter Test
**Steps:**
1. Select a collector from Collected By dropdown
2. Click "Generate Report"

**Expected Result:**
- API called with `received_by`
- Returns collections received by selected person
- All records show the selected collector

---

### 9. Group By Filter Test
**Steps:**
1. Select "Class" from Group By dropdown
2. Click "Generate Report"

**Expected Result:**
- API called with `group: "class"`
- Returns grouped data (if supported by API)

---

### 10. Combined Filters Test
**Steps:**
1. Select "This Month" from Search Duration
2. Select a session
3. Select a class
4. Select a section
5. Select a fee type
6. Click "Generate Report"

**Expected Result:**
- API called with all selected filters
- Returns collections matching all criteria
- Records filtered correctly

---

### 11. No Data Test
**Steps:**
1. Select filters that have no matching data
2. Click "Generate Report"

**Expected Result:**
- API returns empty data array
- "No data available" layout shown
- Toast shows "No data available"

---

### 12. UI Display Test

#### Test 12.1: Card Header
**Verify:**
- [ ] Invoice number displayed correctly
- [ ] Date formatted as "MMM DD, YYYY"
- [ ] Header background uses theme primary color
- [ ] Text is white and readable

#### Test 12.2: Student Information
**Verify:**
- [ ] Student name displayed correctly
- [ ] Admission number shown (if available)
- [ ] Class and section shown correctly
- [ ] Fields hidden when not available

#### Test 12.3: Fee Information
**Verify:**
- [ ] Fee type displayed
- [ ] Fee code shown (if available)
- [ ] Fee group name shown (if available)
- [ ] Fields hidden when not available

#### Test 12.4: Amount Details
**Verify:**
- [ ] Amount formatted with currency symbol
- [ ] Discount shown only if > 0
- [ ] Fine shown only if > 0
- [ ] Total calculated correctly (amount - discount + fine)
- [ ] Numbers formatted with proper locale

#### Test 12.5: Payment Information
**Verify:**
- [ ] Payment mode displayed
- [ ] Received by shown (if available)
- [ ] Description shown (if available)
- [ ] Fields hidden when not available

---

### 13. Error Handling Test

#### Test 13.1: Network Error
**Steps:**
1. Turn off internet
2. Click "Generate Report"

**Expected Result:**
- Toast shows "No internet connection"
- No data layout shown
- No crash

#### Test 13.2: API Error
**Steps:**
1. Configure invalid API endpoint
2. Click "Generate Report"

**Expected Result:**
- Error handled gracefully
- Toast shows error message
- No crash

#### Test 13.3: Invalid JSON
**Steps:**
1. Mock API to return invalid JSON
2. Click "Generate Report"

**Expected Result:**
- JSON parsing error caught
- Toast shows "Error parsing report"
- No crash

---

### 14. Performance Test
**Steps:**
1. Request large dataset (e.g., full year)
2. Observe loading time
3. Scroll through results

**Expected Result:**
- Loading indicator shown during API call
- Data loads within reasonable time
- Smooth scrolling
- No lag or freeze

---

### 15. Theme Integration Test
**Steps:**
1. Change app theme color in settings
2. Open Fee Collection Report
3. Generate report

**Expected Result:**
- Card headers use new primary color
- Generate button uses new primary color
- Action bar uses new primary color
- Theme applied consistently

---

## 🔍 Debug Checklist

### LogCat Tags to Monitor
- `FeesCollectionReport` - Activity logs
- `BaseFinanceReportActivity` - Base activity logs
- `CollectionReportAdapter` - Adapter logs

### Key Log Messages
- "API Response: ..." - Full API response
- "Report loaded: X records" - Success message
- "RecyclerView adapter set successfully" - Display success
- "Error parsing report response" - Parsing error

### Common Issues

#### Issue 1: No data shown
**Check:**
- API response status
- Data array in response
- RecyclerView initialization
- Adapter setup

#### Issue 2: Filters not working
**Check:**
- Filter values being sent to API
- API endpoint configuration
- Request body format
- Filter dropdown population

#### Issue 3: Crash on display
**Check:**
- Null pointer exceptions
- RecyclerView initialization
- Adapter data
- Layout inflation

---

## 📊 Test Data Requirements

### Minimum Test Data
- At least 5 collection records
- Multiple students
- Multiple fee types
- Multiple payment modes
- Records across different dates
- Records with and without discounts
- Records with and without fines

### Recommended Test Scenarios
1. **Normal payment** - Amount only, no discount/fine
2. **Payment with discount** - Amount with discount
3. **Payment with fine** - Amount with fine
4. **Payment with both** - Amount with discount and fine
5. **Multiple payments** - Same student, different dates
6. **Different fee types** - Tuition, transport, hostel, etc.
7. **Different payment modes** - Cash, online, cheque, etc.

---

## ✅ Sign-Off Checklist

- [ ] All navigation tests passed
- [ ] All filter tests passed
- [ ] UI displays correctly
- [ ] Theme integration works
- [ ] Error handling works
- [ ] Performance is acceptable
- [ ] No crashes observed
- [ ] No memory leaks
- [ ] Logs are clean
- [ ] Ready for production

---

## 📝 Test Report Template

```
Test Date: ___________
Tester: ___________
App Version: ___________
Device: ___________
Android Version: ___________

Test Results:
- Navigation: PASS / FAIL
- Empty Request: PASS / FAIL
- Search Duration: PASS / FAIL
- Session Filter: PASS / FAIL
- Class Filter: PASS / FAIL
- Section Filter: PASS / FAIL
- Fee Type Filter: PASS / FAIL
- Collected By Filter: PASS / FAIL
- Combined Filters: PASS / FAIL
- UI Display: PASS / FAIL
- Error Handling: PASS / FAIL
- Performance: PASS / FAIL
- Theme Integration: PASS / FAIL

Issues Found:
1. ___________
2. ___________
3. ___________

Overall Status: PASS / FAIL

Notes:
___________
```

---

**Testing Guide Version:** 1.0  
**Last Updated:** October 11, 2025  
**Status:** Ready for Testing

