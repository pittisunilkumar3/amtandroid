# Income Report Testing Guide

## Overview
This guide provides step-by-step instructions for testing the Income Report feature.

---

## Prerequisites

1. ✅ App is installed and running
2. ✅ Teacher login credentials available
3. ✅ Backend API is running at configured URL
4. ✅ Test data exists in the database

---

## Test Scenarios

### Test 1: Navigate to Income Report

**Steps:**
1. Login as Teacher
2. Navigate to Dashboard
3. Click on "Reports" menu
4. Click on "Finance" category
5. Click on "Income Report"

**Expected Result:**
- IncomeReportActivity opens
- Title shows "Income Report"
- Search Type dropdown is visible
- Generate Report button is visible
- Default selection is "Today"
- Date range layout is hidden

---

### Test 2: Generate Report with "Today" Filter

**Steps:**
1. Open Income Report
2. Ensure "Today" is selected in Search Type dropdown
3. Click "Generate Report" button

**Expected Result:**
- Progress bar shows during loading
- API request sent with `{"search_type": "today"}`
- If data exists:
  - Summary card shows with total records and amount
  - RecyclerView displays income records
  - Each card shows: name, invoice, income head, amount, date
  - Success toast: "Found X income record(s)"
- If no data:
  - No data layout shows
  - Toast: "No income records found for the selected period"

---

### Test 3: Generate Report with "This Week" Filter

**Steps:**
1. Open Income Report
2. Select "This Week" from Search Type dropdown
3. Click "Generate Report" button

**Expected Result:**
- API request sent with `{"search_type": "this_week"}`
- Report displays income for current week
- Summary shows correct totals

---

### Test 4: Generate Report with "This Month" Filter

**Steps:**
1. Open Income Report
2. Select "This Month" from Search Type dropdown
3. Click "Generate Report" button

**Expected Result:**
- API request sent with `{"search_type": "this_month"}`
- Report displays income for current month
- Summary shows correct totals

---

### Test 5: Generate Report with "Last Month" Filter

**Steps:**
1. Open Income Report
2. Select "Last Month" from Search Type dropdown
3. Click "Generate Report" button

**Expected Result:**
- API request sent with `{"search_type": "last_month"}`
- Report displays income for previous month
- Summary shows correct totals

---

### Test 6: Generate Report with "This Year" Filter

**Steps:**
1. Open Income Report
2. Select "This Year" from Search Type dropdown
3. Click "Generate Report" button

**Expected Result:**
- API request sent with `{"search_type": "this_year"}`
- Report displays income for current year
- Summary shows correct totals

---

### Test 7: Generate Report with "Custom Period" Filter

**Steps:**
1. Open Income Report
2. Select "Custom Period" from Search Type dropdown
3. Verify date range layout appears
4. Click on "From Date" field
5. Select a date from calendar (e.g., 2025-01-01)
6. Click on "To Date" field
7. Select a date from calendar (e.g., 2025-03-31)
8. Click "Generate Report" button

**Expected Result:**
- Date range layout shows when "Custom Period" is selected
- Calendar dialogs open for date selection
- Selected dates display in the fields
- API request sent with `{"date_from": "2025-01-01", "date_to": "2025-03-31"}`
- Report displays income for selected date range
- Summary shows correct totals

---

### Test 8: Date Validation - From Date After To Date

**Steps:**
1. Open Income Report
2. Select "Custom Period" from Search Type dropdown
3. Select "From Date" as 2025-03-31
4. Select "To Date" as 2025-01-01
5. Click "Generate Report" button

**Expected Result:**
- Toast message: "From Date cannot be after To Date"
- No API request is sent
- Report is not generated

---

### Test 9: Date Validation - Empty Dates

**Steps:**
1. Open Income Report
2. Select "Custom Period" from Search Type dropdown
3. Do not select any dates
4. Click "Generate Report" button

**Expected Result:**
- Toast message: "Please select both dates"
- No API request is sent
- Report is not generated

---

### Test 10: Verify Income Record Display

**Steps:**
1. Generate a report with data
2. Verify each income card displays:
   - Income name (bold, black)
   - Invoice number (gray, small)
   - Income head (gray, small)
   - Amount (primary color, bold, large)
   - Date (formatted as "dd MMM yyyy")
   - Note (if available)

**Expected Result:**
- All fields display correctly
- Currency symbol shows (₹ or configured currency)
- Amount is formatted with commas (e.g., ₹5,000)
- Date is formatted (e.g., "05 Oct 2025")
- Note section is hidden if note is empty

---

### Test 11: Verify Summary Display

**Steps:**
1. Generate a report with data
2. Check summary card at top

**Expected Result:**
- Summary card is visible
- "Total Records" shows correct count
- "Total Amount" shows correct sum with currency
- Amount is formatted with commas

---

### Test 12: Test Loading State

**Steps:**
1. Open Income Report
2. Click "Generate Report" button
3. Observe during API call

**Expected Result:**
- Progress bar shows immediately
- Summary card is hidden
- RecyclerView is hidden
- No data layout is hidden
- After response, progress bar hides

---

### Test 13: Test No Data State

**Steps:**
1. Open Income Report
2. Select a date range with no income records
3. Click "Generate Report" button

**Expected Result:**
- After loading, no data layout shows
- Image icon displays (faded)
- Text: "No Data Available"
- Subtext: "Select filters and generate report"
- Toast: "No income records found for the selected period"

---

### Test 14: Test Error Handling - No Internet

**Steps:**
1. Disable internet connection
2. Open Income Report
3. Click "Generate Report" button

**Expected Result:**
- Progress bar shows
- After timeout, progress bar hides
- No data layout shows
- Toast: "Error loading report: No internet connection"

---

### Test 15: Test Error Handling - Server Error

**Steps:**
1. Configure invalid API URL or stop backend server
2. Open Income Report
3. Click "Generate Report" button

**Expected Result:**
- Progress bar shows
- After error, progress bar hides
- No data layout shows
- Toast: "Error loading report: Server error: XXX"

---

### Test 16: Test Back Navigation

**Steps:**
1. Open Income Report
2. Click back button in toolbar

**Expected Result:**
- Activity closes
- Returns to previous screen (Finance Reports list)
- Slide animation plays

---

### Test 17: Test Theme Colors

**Steps:**
1. Verify app theme colors are configured
2. Open Income Report
3. Check UI elements

**Expected Result:**
- Toolbar background uses primary color
- Generate Report button uses primary color
- Amount text uses primary color
- All theme colors are applied correctly

---

### Test 18: Test Scroll Behavior

**Steps:**
1. Generate a report with many records (>10)
2. Scroll through the list

**Expected Result:**
- RecyclerView scrolls smoothly
- All cards display correctly
- No performance issues
- Summary card stays at top (in ScrollView)

---

### Test 19: Test Orientation Change

**Steps:**
1. Generate a report with data
2. Rotate device to landscape
3. Rotate back to portrait

**Expected Result:**
- Layout adjusts correctly
- Data is preserved (or re-fetched)
- No crashes or data loss

---

### Test 20: Test Multiple Report Generations

**Steps:**
1. Generate report with "Today"
2. Change to "This Month"
3. Generate report again
4. Change to "Custom Period"
5. Generate report again

**Expected Result:**
- Each report generation works correctly
- Previous data is cleared
- New data displays correctly
- No memory leaks or performance issues

---

## API Testing

### Test API Request Format

**Verify request headers:**
```
Client-Service: smartschool
Auth-Key: schoolAdmin@
Content-Type: application/json
```

**Verify request body for predefined types:**
```json
{
  "search_type": "today"
}
```

**Verify request body for custom period:**
```json
{
  "date_from": "2025-01-01",
  "date_to": "2025-12-31"
}
```

---

## Checklist

- [ ] All 20 test scenarios pass
- [ ] API requests are correct
- [ ] Response parsing works
- [ ] UI displays correctly
- [ ] Error handling works
- [ ] Loading states work
- [ ] Date validation works
- [ ] Theme colors applied
- [ ] Navigation works
- [ ] No crashes or errors

---

## Known Issues

None at this time.

---

## Notes

1. Ensure backend API is running and accessible
2. Test data should exist for meaningful testing
3. Check logcat for detailed API logs (TAG: "IncomeReport")
4. Verify currency symbol matches school configuration

---

## Support

For issues or questions, please contact the development team.

**Last Updated:** October 10, 2025

