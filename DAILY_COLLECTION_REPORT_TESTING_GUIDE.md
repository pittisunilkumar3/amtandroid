# Daily Collection Report - Testing Guide

## 🎯 Quick Overview

**Feature**: Daily Collection Report  
**Location**: Reports → Finance → Daily Collection Report  
**Purpose**: Show daily fee collection data with date range filters  
**Status**: ✅ Complete and Ready to Test

---

## 📋 Pre-Testing Checklist

- [ ] APK installed on test device
- [ ] Internet connection available
- [ ] API server is running
- [ ] Test data exists (collections in date range)
- [ ] Logcat is ready for debugging

---

## 🧪 Test Cases

### **Test Case 1: Default Date Range**

**Objective**: Verify default date range is set correctly

**Steps**:
1. Open the app
2. Navigate to: Reports → Finance → Daily Collection Report
3. Observe the date fields

**Expected Result**:
- ✅ "From Date" is set to 30 days ago
- ✅ "To Date" is set to today
- ✅ Dates are in `YYYY-MM-DD` format
- ✅ Calendar icons are visible

**Pass/Fail**: ___________

---

### **Test Case 2: Date Picker Functionality**

**Objective**: Verify date pickers work correctly

**Steps**:
1. Navigate to Daily Collection Report
2. Click on "From Date" field
3. Verify DatePickerDialog appears
4. Select a date (e.g., 2025-09-01)
5. Verify date is updated in the field
6. Repeat for "To Date" field

**Expected Result**:
- ✅ DatePickerDialog opens on click
- ✅ Selected date is displayed in field
- ✅ Date format is `YYYY-MM-DD`
- ✅ Calendar icon remains visible

**Pass/Fail**: ___________

---

### **Test Case 3: Valid Date Range Report**

**Objective**: Generate report with valid date range

**Steps**:
1. Navigate to Daily Collection Report
2. Set "From Date" to 2025-09-01
3. Set "To Date" to 2025-10-10
4. Click "Generate Report"
5. Wait for data to load

**Expected Result**:
- ✅ Loading indicator appears
- ✅ Data loads successfully
- ✅ Summary card is visible with totals
- ✅ Daily collection cards are displayed
- ✅ Each card shows:
  - Date (formatted as "Sep 01, 2025")
  - Amount collected
  - Transaction count
  - "View Transaction IDs" button
- ✅ Success toast message appears

**Pass/Fail**: ___________

---

### **Test Case 4: Invalid Date Range (From > To)**

**Objective**: Verify validation for invalid date range

**Steps**:
1. Navigate to Daily Collection Report
2. Set "From Date" to 2025-10-10
3. Set "To Date" to 2025-09-01
4. Click "Generate Report"

**Expected Result**:
- ✅ Error toast appears: "From Date cannot be after To Date"
- ✅ Report is not generated
- ✅ No API call is made

**Pass/Fail**: ___________

---

### **Test Case 5: Empty Date Fields**

**Objective**: Verify validation for empty dates

**Steps**:
1. Navigate to Daily Collection Report
2. Clear both date fields (if possible)
3. Click "Generate Report"

**Expected Result**:
- ✅ Error toast appears: "Please select both dates"
- ✅ Report is not generated

**Pass/Fail**: ___________

---

### **Test Case 6: Summary Card Calculation**

**Objective**: Verify summary totals are calculated correctly

**Steps**:
1. Generate report with valid date range
2. Manually calculate:
   - Sum of all daily amounts
   - Sum of all transaction counts
3. Compare with summary card values

**Expected Result**:
- ✅ Total Amount = Sum of all daily amounts
- ✅ Total Transactions = Sum of all daily counts
- ✅ Date range is displayed correctly
- ✅ Currency symbol is correct (₹)
- ✅ Number formatting is correct (e.g., ₹1,82,800)

**Pass/Fail**: ___________

---

### **Test Case 7: Expandable Transaction IDs**

**Objective**: Verify transaction IDs can be expanded/collapsed

**Steps**:
1. Generate report with valid date range
2. Find a card with transactions
3. Click "View Transaction IDs" button
4. Verify transaction IDs are displayed
5. Click button again to hide

**Expected Result**:
- ✅ Transaction IDs are hidden initially
- ✅ Clicking button shows transaction IDs
- ✅ Button text changes to "Hide Transaction IDs"
- ✅ Clicking again hides transaction IDs
- ✅ Button text changes back to "View Transaction IDs"

**Pass/Fail**: ___________

---

### **Test Case 8: Zero Collection Days**

**Objective**: Verify zero collection days are displayed correctly

**Steps**:
1. Generate report with date range that includes zero collection days
2. Find a card with zero amount

**Expected Result**:
- ✅ Card is displayed for zero collection day
- ✅ Amount shows "₹ 0"
- ✅ Amount is in gray color (not green)
- ✅ Transaction count shows "0"
- ✅ "View Transaction IDs" button is hidden

**Pass/Fail**: ___________

---

### **Test Case 9: Regular Fees vs Other Fees**

**Objective**: Verify both fee types are displayed

**Steps**:
1. Generate report with date range that has both fee types
2. Check for cards with "Regular Fees" and "Other Fees" labels

**Expected Result**:
- ✅ Regular fees cards don't show type label
- ✅ Other fees cards show "Other Fees" label
- ✅ Both types are included in summary totals

**Pass/Fail**: ___________

---

### **Test Case 10: No Data Scenario**

**Objective**: Verify behavior when no data is available

**Steps**:
1. Navigate to Daily Collection Report
2. Select a date range with no collections
3. Click "Generate Report"

**Expected Result**:
- ✅ Loading indicator appears
- ✅ "No Data Available" message is displayed
- ✅ Toast message: "No collections found for the selected date range"
- ✅ Summary card is hidden
- ✅ RecyclerView is hidden

**Pass/Fail**: ___________

---

### **Test Case 11: Network Error**

**Objective**: Verify error handling for network issues

**Steps**:
1. Turn off internet connection
2. Navigate to Daily Collection Report
3. Click "Generate Report"

**Expected Result**:
- ✅ Loading indicator appears
- ✅ Error toast appears: "Error loading report: No internet connection"
- ✅ "No Data Available" message is displayed
- ✅ App doesn't crash

**Pass/Fail**: ___________

---

### **Test Case 12: Theme Color Integration**

**Objective**: Verify theme colors are applied correctly

**Steps**:
1. Generate report with valid date range
2. Check card headers

**Expected Result**:
- ✅ Card headers use primary theme color
- ✅ Text on headers is white
- ✅ Generate Report button uses primary color
- ✅ Colors are consistent with app theme

**Pass/Fail**: ___________

---

### **Test Case 13: Date Formatting**

**Objective**: Verify dates are formatted correctly

**Steps**:
1. Generate report with valid date range
2. Check date display on cards

**Expected Result**:
- ✅ Dates are formatted as "Sep 01, 2025" (not "2025-09-01")
- ✅ Month names are abbreviated (Jan, Feb, Mar, etc.)
- ✅ Format is consistent across all cards

**Pass/Fail**: ___________

---

### **Test Case 14: Currency Formatting**

**Objective**: Verify currency is formatted correctly

**Steps**:
1. Generate report with valid date range
2. Check amount display

**Expected Result**:
- ✅ Currency symbol is displayed (₹)
- ✅ Numbers use Indian formatting (e.g., 1,82,800)
- ✅ Decimal places are shown (e.g., 1,82,800.00)
- ✅ Formatting is consistent across all cards

**Pass/Fail**: ___________

---

### **Test Case 15: Scrolling and Performance**

**Objective**: Verify app performance with large data sets

**Steps**:
1. Generate report with large date range (e.g., 1 year)
2. Scroll through the list
3. Expand/collapse transaction IDs

**Expected Result**:
- ✅ Scrolling is smooth
- ✅ No lag or stuttering
- ✅ Cards load quickly
- ✅ Expand/collapse is responsive
- ✅ No memory issues

**Pass/Fail**: ___________

---

### **Test Case 16: Back Navigation**

**Objective**: Verify back button works correctly

**Steps**:
1. Navigate to Daily Collection Report
2. Click back button in toolbar
3. Verify navigation

**Expected Result**:
- ✅ Returns to previous screen
- ✅ No crash
- ✅ Data is not retained on return

**Pass/Fail**: ___________

---

### **Test Case 17: Rotation Test**

**Objective**: Verify app handles screen rotation

**Steps**:
1. Generate report with valid date range
2. Rotate device to landscape
3. Rotate back to portrait

**Expected Result**:
- ✅ Layout adjusts correctly
- ✅ Data is retained
- ✅ No crash
- ✅ UI remains functional

**Pass/Fail**: ___________

---

## 🐛 Common Issues and Solutions

### **Issue 1: "Error loading report: null"**
**Cause**: API endpoint issue  
**Solution**: Check that endpoint is `daily-collection-report/filter` (no `api/` prefix)  
**Verify**: Check Logcat for full API URL

---

### **Issue 2: Date picker not opening**
**Cause**: Click listener not set  
**Solution**: Verify `setupDatePickers()` is called in `onCreate()`  
**Verify**: Check Logcat for any errors

---

### **Issue 3: Invalid date format**
**Cause**: Date format mismatch  
**Solution**: Ensure date format is `yyyy-MM-dd`  
**Verify**: Check request body in Logcat

---

### **Issue 4: Summary totals incorrect**
**Cause**: Calculation error  
**Solution**: Verify both `fees_data` and `other_fees_data` are included  
**Verify**: Check parsing logic in `parseDailyCollectionReportResponse()`

---

### **Issue 5: Transaction IDs not showing**
**Cause**: IDs array is null or empty  
**Solution**: Verify API returns `student_fees_deposite_ids` array  
**Verify**: Check API response in Logcat

---

## 📊 Test Results Summary

| Test Case | Status | Notes |
|-----------|--------|-------|
| 1. Default Date Range | ⬜ | |
| 2. Date Picker Functionality | ⬜ | |
| 3. Valid Date Range Report | ⬜ | |
| 4. Invalid Date Range | ⬜ | |
| 5. Empty Date Fields | ⬜ | |
| 6. Summary Calculation | ⬜ | |
| 7. Expandable Transaction IDs | ⬜ | |
| 8. Zero Collection Days | ⬜ | |
| 9. Regular vs Other Fees | ⬜ | |
| 10. No Data Scenario | ⬜ | |
| 11. Network Error | ⬜ | |
| 12. Theme Color Integration | ⬜ | |
| 13. Date Formatting | ⬜ | |
| 14. Currency Formatting | ⬜ | |
| 15. Scrolling Performance | ⬜ | |
| 16. Back Navigation | ⬜ | |
| 17. Rotation Test | ⬜ | |

**Legend**: ⬜ Not Tested | ✅ Passed | ❌ Failed

---

## 🔍 Debugging Tips

### **Enable Logging**
Look for these log tags in Logcat:
- `DailyCollectionReport` - Main activity logs

### **Check API Request**
```
D/DailyCollectionReport: === Request Body ===
D/DailyCollectionReport: {"date_from":"2025-09-01","date_to":"2025-10-10"}
```

### **Check API Response**
```
D/DailyCollectionReport: === API Response Received ===
D/DailyCollectionReport: Response: {"status":1,"message":"Daily collection report retrieved successfully",...}
```

### **Check Parsing**
```
D/DailyCollectionReport: Fees Data Array Length: 40
D/DailyCollectionReport: First Collection - Date: 2025-09-01
D/DailyCollectionReport: First Collection - Amount: 182800.0
D/DailyCollectionReport: First Collection - Count: 42
```

---

## ✅ Final Checklist

- [ ] All test cases executed
- [ ] No crashes observed
- [ ] Performance is acceptable
- [ ] UI looks professional
- [ ] Date pickers work correctly
- [ ] Validation works as expected
- [ ] Summary calculations are accurate
- [ ] Error handling is robust
- [ ] Theme colors are applied
- [ ] Ready for production

---

**Testing Date**: ___________  
**Tester Name**: ___________  
**Device**: ___________  
**Android Version**: ___________  
**Overall Status**: ⬜ Pass | ⬜ Fail

---

**Last Updated**: 2025-01-10  
**Version**: 1.0  
**Status**: Ready for Testing

