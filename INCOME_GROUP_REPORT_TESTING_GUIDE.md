# Income Group Report - Testing Guide

## Overview
This guide provides step-by-step instructions to test the Income Group Report feature with the new API implementation.

---

## Prerequisites

1. **Backend API Running**
   - Base URL: `http://localhost/amt/api`
   - Endpoints available:
     - `POST /income-group-report/list`
     - `POST /income-group-report/filter`

2. **Android App Configuration**
   - Base URL configured in app settings
   - Authentication headers set:
     - `Client-Service: smartschool`
     - `Auth-Key: schoolAdmin@`

3. **Test Data**
   - Income heads created in backend
   - Income records available for testing

---

## Test Scenarios

### 1. Initial Load Test

**Steps:**
1. Navigate to Reports → Finance → Income Group Report
2. Observe the screen loading

**Expected Results:**
- ✅ Activity loads successfully
- ✅ Search Type dropdown shows 6 options:
  - Today
  - This Week
  - This Month
  - Last Month
  - This Year
  - Custom Period
- ✅ Income Head dropdown shows "All" + loaded income heads
- ✅ Date range layout is hidden initially
- ✅ Generate Report button is visible

**API Call:**
```
POST /income-group-report/list
Headers: Client-Service: smartschool, Auth-Key: schoolAdmin@
Body: {}
```

**Expected Response:**
```json
{
  "status": 1,
  "message": "Income heads retrieved successfully",
  "data": {
    "income_heads": [
      {"id": "1", "income_category": "Fees Collection"},
      {"id": "2", "income_category": "Donations"}
    ]
  }
}
```

---

### 2. Search Type Selection Tests

#### Test 2.1: Today
**Steps:**
1. Select "Today" from Search Type dropdown
2. Click "Generate Report"

**Expected Results:**
- ✅ Date range layout remains hidden
- ✅ API called with `search_type: "today"`
- ✅ Results show income for current day

**API Request:**
```json
{
  "search_type": "today"
}
```

#### Test 2.2: This Week
**Steps:**
1. Select "This Week" from Search Type dropdown
2. Click "Generate Report"

**Expected Results:**
- ✅ API called with `search_type: "this_week"`
- ✅ Results show income for current week

**API Request:**
```json
{
  "search_type": "this_week"
}
```

#### Test 2.3: This Month
**Steps:**
1. Select "This Month" from Search Type dropdown
2. Click "Generate Report"

**Expected Results:**
- ✅ API called with `search_type: "this_month"`
- ✅ Results show income for current month

**API Request:**
```json
{
  "search_type": "this_month"
}
```

#### Test 2.4: Last Month
**Steps:**
1. Select "Last Month" from Search Type dropdown
2. Click "Generate Report"

**Expected Results:**
- ✅ API called with `search_type: "last_month"`
- ✅ Results show income for previous month

**API Request:**
```json
{
  "search_type": "last_month"
}
```

#### Test 2.5: This Year
**Steps:**
1. Select "This Year" from Search Type dropdown
2. Click "Generate Report"

**Expected Results:**
- ✅ API called with `search_type: "this_year"`
- ✅ Results show income for current year

**API Request:**
```json
{
  "search_type": "this_year"
}
```

#### Test 2.6: Custom Period
**Steps:**
1. Select "Custom Period" from Search Type dropdown
2. Observe date range layout

**Expected Results:**
- ✅ Date range layout becomes visible
- ✅ From Date field shows current date
- ✅ To Date field shows current date

**Steps (continued):**
3. Click on From Date field
4. Select a date (e.g., 2025-01-01)
5. Click on To Date field
6. Select a date (e.g., 2025-12-31)
7. Click "Generate Report"

**Expected Results:**
- ✅ Date picker dialogs work correctly
- ✅ Selected dates displayed in fields
- ✅ API called with `date_from` and `date_to`
- ✅ Results show income for selected date range

**API Request:**
```json
{
  "date_from": "2025-01-01",
  "date_to": "2025-12-31"
}
```

---

### 3. Income Head Filter Tests

#### Test 3.1: All Income Heads
**Steps:**
1. Select "Today" from Search Type
2. Keep "All" selected in Income Head dropdown
3. Click "Generate Report"

**Expected Results:**
- ✅ API called without `head` parameter
- ✅ Results show income from all heads

**API Request:**
```json
{
  "search_type": "today"
}
```

#### Test 3.2: Specific Income Head
**Steps:**
1. Select "This Month" from Search Type
2. Select "Fees Collection" from Income Head dropdown
3. Click "Generate Report"

**Expected Results:**
- ✅ API called with `head` parameter
- ✅ Results show only income from selected head
- ✅ All records have matching income category

**API Request:**
```json
{
  "search_type": "this_month",
  "head": "1"
}
```

---

### 4. Combined Filter Tests

#### Test 4.1: Custom Period + Specific Head
**Steps:**
1. Select "Custom Period" from Search Type
2. Select date range: 2025-01-01 to 2025-06-30
3. Select "Donations" from Income Head dropdown
4. Click "Generate Report"

**Expected Results:**
- ✅ API called with all parameters
- ✅ Results filtered by both date range and income head

**API Request:**
```json
{
  "date_from": "2025-01-01",
  "date_to": "2025-06-30",
  "head": "2"
}
```

---

### 5. Results Display Tests

#### Test 5.1: Summary Card
**Steps:**
1. Generate a report with results
2. Observe summary card

**Expected Results:**
- ✅ Summary card visible above list
- ✅ Total Records shows correct count
- ✅ Total Amount shows correct sum with currency
- ✅ Amount formatted with 2 decimal places

#### Test 5.2: Income List
**Steps:**
1. Generate a report with results
2. Scroll through the list

**Expected Results:**
- ✅ Each card shows:
  - Income name
  - Invoice number (format: "Invoice: INV001")
  - Income category
  - Amount (with currency and theme color)
  - Date (format: "15 Jan 2025")
  - Note (only if available)
- ✅ Cards are properly formatted
- ✅ Theme color applied to amount

#### Test 5.3: Empty Results
**Steps:**
1. Select filters that return no results
2. Click "Generate Report"

**Expected Results:**
- ✅ No data layout displayed
- ✅ Summary card hidden
- ✅ RecyclerView hidden
- ✅ Toast message: "No income records found for the selected filters"

---

### 6. Validation Tests

#### Test 6.1: Custom Period - Missing Dates
**Steps:**
1. Select "Custom Period"
2. Clear From Date field
3. Click "Generate Report"

**Expected Results:**
- ✅ Validation error shown
- ✅ Toast message: "Please select both dates"
- ✅ API not called

#### Test 6.2: Custom Period - Invalid Date Range
**Steps:**
1. Select "Custom Period"
2. Set From Date: 2025-12-31
3. Set To Date: 2025-01-01
4. Click "Generate Report"

**Expected Results:**
- ✅ Validation error shown
- ✅ Toast message: "From Date cannot be after To Date"
- ✅ API not called

---

### 7. Error Handling Tests

#### Test 7.1: Network Error
**Steps:**
1. Disable internet connection
2. Try to generate report

**Expected Results:**
- ✅ Error message shown
- ✅ No data layout displayed
- ✅ Toast message: "No internet connection"

#### Test 7.2: API Error Response
**Steps:**
1. Simulate API returning status: 0
2. Generate report

**Expected Results:**
- ✅ Error message from API displayed
- ✅ No data layout shown
- ✅ Toast shows API error message

#### Test 7.3: Invalid Response Format
**Steps:**
1. Simulate API returning invalid JSON
2. Generate report

**Expected Results:**
- ✅ Error handled gracefully
- ✅ Toast message: "Error parsing response"
- ✅ No data layout displayed

---

### 8. Loading State Tests

#### Test 8.1: Loading Indicator
**Steps:**
1. Click "Generate Report"
2. Observe loading state

**Expected Results:**
- ✅ Progress bar visible during API call
- ✅ RecyclerView hidden
- ✅ Summary card hidden
- ✅ No data layout hidden

**Steps (continued):**
3. Wait for API response

**Expected Results:**
- ✅ Progress bar hidden after response
- ✅ Results displayed

---

### 9. UI/UX Tests

#### Test 9.1: Theme Color
**Steps:**
1. Check if theme color is applied
2. Generate report with results

**Expected Results:**
- ✅ Action bar has theme color
- ✅ Generate button has theme color
- ✅ Amount text in list has theme color

#### Test 9.2: Back Navigation
**Steps:**
1. Click back button in action bar
2. Observe navigation

**Expected Results:**
- ✅ Activity closes
- ✅ Returns to previous screen
- ✅ Slide animation applied

#### Test 9.3: Date Formatting
**Steps:**
1. Generate report with results
2. Check date format in list

**Expected Results:**
- ✅ Dates formatted as "dd MMM yyyy"
- ✅ Example: "15 Jan 2025"

#### Test 9.4: Amount Formatting
**Steps:**
1. Generate report with results
2. Check amount format in list and summary

**Expected Results:**
- ✅ Currency symbol displayed
- ✅ Amounts formatted with 2 decimal places
- ✅ Thousands separator used (if applicable)
- ✅ Example: "₹ 5,000.00"

---

## API Response Validation

### Successful Response
```json
{
  "status": 1,
  "message": "Income group report retrieved successfully",
  "filters_applied": {
    "search_type": "this_month",
    "date_from": null,
    "date_to": null,
    "head": null
  },
  "date_range": {
    "start_date": "2025-10-01",
    "end_date": "2025-10-31",
    "label": "01/10/2025 to 31/10/2025"
  },
  "summary": {
    "total_records": 8,
    "total_amount": "57500.00",
    "income_heads": [
      {
        "head_id": "1",
        "income_category": "Fees Collection",
        "count": 5,
        "total": 45000
      }
    ]
  },
  "total_records": 8,
  "data": [
    {
      "id": "1",
      "name": "Student Fee Payment",
      "invoice_no": "INV001",
      "date": "2025-01-15",
      "amount": "5000.00",
      "income_category": "Fees Collection",
      "head_id": "1",
      "note": "Monthly fee payment",
      "documents": ""
    }
  ],
  "timestamp": "2025-10-08 22:15:30"
}
```

### Error Response
```json
{
  "status": 0,
  "message": "Unauthorized access",
  "timestamp": "2025-10-08 22:15:30"
}
```

---

## Logging

Check Android Logcat for detailed logs:

**Tag:** `IncomeGroupReport`

**Key Log Messages:**
- "=== Loading Income Heads ==="
- "Income Head List API Endpoint: ..."
- "Income heads count: X"
- "=== Generating Report ==="
- "Search Type: ..."
- "Income Head ID: ..."
- "=== Request Body ==="
- "=== API Response Received ==="
- "Summary - Total Records: X, Total Amount: Y"
- "Income list size: X"

---

## Test Checklist

### Functionality
- [ ] Income heads load on activity start
- [ ] All search types work correctly
- [ ] Custom period date pickers work
- [ ] Income head filter works
- [ ] Combined filters work
- [ ] Generate button triggers API call
- [ ] Results display correctly
- [ ] Summary shows correct totals
- [ ] Empty results handled

### Validation
- [ ] Custom period requires both dates
- [ ] From date cannot be after to date
- [ ] Network error handled
- [ ] API error handled
- [ ] Invalid response handled

### UI/UX
- [ ] Theme color applied
- [ ] Loading state shows
- [ ] Back navigation works
- [ ] Date formatting correct
- [ ] Amount formatting correct
- [ ] Note visibility conditional
- [ ] Smooth scrolling in list

---

## Common Issues & Solutions

### Issue 1: Income heads not loading
**Solution:** Check API endpoint and response format

### Issue 2: Empty results always shown
**Solution:** Verify API response structure matches parsing code

### Issue 3: Date validation not working
**Solution:** Check date format and calendar comparison logic

### Issue 4: Theme color not applied
**Solution:** Verify theme color stored in SharedPreferences

### Issue 5: Amounts not formatted correctly
**Solution:** Check NumberFormat locale and currency symbol

---

## Status

✅ **Ready for Testing**

All test scenarios documented and implementation complete.

---

## Next Steps

1. Run through all test scenarios
2. Verify API integration
3. Check UI/UX on different devices
4. Test with real data
5. Performance testing with large datasets
6. Report any issues found

