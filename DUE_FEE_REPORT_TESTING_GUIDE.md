# Due Fee Report - Testing Guide

## 📋 Overview

This guide provides comprehensive testing instructions for the Due Fee Report (Total Balance Fee Statement) feature in the Smart School Android application.

---

## 🎯 Test Scenarios

### Test 1: Navigation to Due Fee Report

**Steps:**
1. Open the Smart School app
2. Login as a teacher
3. From Teacher Dashboard, tap on "Reports" icon
4. Tap on "Finance" category
5. Scroll to find "Total Balance Fees Statement"
6. Tap on "Total Balance Fees Statement"

**Expected Result:**
- ✅ DueFeeReportActivity opens
- ✅ Title shows "Total Balance Fees Statement"
- ✅ Three filter dropdowns are visible: Session, Class, Section
- ✅ "Generate Report" button is visible
- ✅ Empty state message is shown

---

### Test 2: Generate Report Without Filters

**Steps:**
1. Open Due Fee Report
2. Do NOT select any filters
3. Tap "Generate Report" button

**Expected Result:**
- ✅ Loading indicator appears
- ✅ API request is sent without filter parameters
- ✅ All students with due fees are displayed
- ✅ Toast message shows: "Found X student(s) with due fees"
- ✅ Cards display correctly

---

### Test 3: Generate Report with Session Filter Only

**Steps:**
1. Open Due Fee Report
2. Select Session: "2024-2025"
3. Leave Class and Section unselected
4. Tap "Generate Report" button

**Expected Result:**
- ✅ Loading indicator appears
- ✅ API request includes only session_id
- ✅ Students from selected session with due fees are displayed
- ✅ Toast message shows count of students found
- ✅ Cards show correct session students

---

### Test 4: Generate Report with All Filters

**Steps:**
1. Select Session: "2024-2025"
2. Select Class: "Class 10"
3. Select Section: "A"
4. Tap "Generate Report" button

**Expected Result:**
- ✅ Loading indicator appears
- ✅ API request includes all three filters
- ✅ Only students from Class 10-A with due fees are displayed
- ✅ Most specific filtering applied
- ✅ Correct student count shown

---

### Test 5: Due Fee Card Display

**Steps:**
1. Generate report with valid filters
2. Observe the displayed due fee cards

**Expected Result:**
Each card should display:
- ✅ Header with theme color (primary color from app settings)
- ✅ Student icon and full name (bold, white text)
- ✅ Admission number
- ✅ Class and section with icon
- ✅ Father name
- ✅ Student mobile number with 📱 icon
- ✅ Guardian information
- ✅ Guardian phone with 📞 icon
- ✅ Fee Summary section header
- ✅ Total Amount
- ✅ Total Paid (green color)
- ✅ Total Balance (highlighted in orange background, red text if due)
- ✅ Total Fine (if applicable)
- ✅ Total Discount (if applicable)
- ✅ Fee items count
- ✅ Detailed fee breakdown

---

### Test 6: Fee Calculation Accuracy

**Steps:**
1. Generate report
2. Select a student card
3. Manually verify fee calculations

**Expected Result:**
- ✅ Total Amount = Sum of all fee amounts
- ✅ Total Paid = Sum of all paid amounts
- ✅ Total Balance = Total Amount - Total Paid
- ✅ Total Fine = Sum of all fine amounts
- ✅ Total Discount = Sum of all discount amounts
- ✅ Fee breakdown matches individual items

---

### Test 7: Empty State

**Steps:**
1. Select filters that have no students with due fees
2. Tap "Generate Report"

**Expected Result:**
- ✅ Loading indicator appears and disappears
- ✅ Empty state message is displayed
- ✅ Toast message shows: "No students with due fees found"
- ✅ No cards are displayed

---

### Test 8: Network Error Handling

**Steps:**
1. Turn off internet connection
2. Select valid filters
3. Tap "Generate Report"

**Expected Result:**
- ✅ Loading indicator appears
- ✅ Error message is displayed
- ✅ Toast shows: "No internet connection"
- ✅ Empty state is shown

---

### Test 9: API Error Handling

**Steps:**
1. Use invalid API credentials (modify Constants.java temporarily)
2. Select valid filters
3. Tap "Generate Report"

**Expected Result:**
- ✅ Loading indicator appears
- ✅ Error message is displayed
- ✅ Toast shows server error message
- ✅ Empty state is shown

---

### Test 10: Theme Color Integration

**Steps:**
1. Go to app settings and change primary color
2. Navigate to Due Fee Report
3. Generate report

**Expected Result:**
- ✅ Card headers use the selected primary color
- ✅ "Generate Report" button uses the selected primary color
- ✅ Action bar uses the selected primary color
- ✅ Icons use the secondary color

---

### Test 11: Balance Color Coding

**Steps:**
1. Generate report with students having different balance statuses
2. Observe balance display colors

**Expected Result:**
- ✅ Students with due balance show red text
- ✅ Students with zero balance show green text
- ✅ Balance row has orange background highlight
- ✅ Color coding is consistent across all cards

---

### Test 12: Transport Fees Display

**Steps:**
1. Generate report for students with transport fees
2. Check fee details breakdown

**Expected Result:**
- ✅ Transport fees are listed separately
- ✅ Transport fees are prefixed with "Transport - "
- ✅ Transport fees are included in total calculations
- ✅ Both regular and transport fees display correctly

---

### Test 13: Optional Fields Visibility

**Steps:**
1. Generate report
2. Check cards for students with missing optional data

**Expected Result:**
- ✅ Missing father name field is hidden
- ✅ Missing mobile number field is hidden
- ✅ Missing guardian info field is hidden
- ✅ Missing guardian phone field is hidden
- ✅ Missing fine row is hidden
- ✅ Missing discount row is hidden
- ✅ Layout adjusts properly for missing fields

---

### Test 14: Scrolling and Performance

**Steps:**
1. Generate report with many records (50+)
2. Scroll through the list

**Expected Result:**
- ✅ Smooth scrolling
- ✅ No lag or stuttering
- ✅ Cards load quickly
- ✅ No memory issues
- ✅ RecyclerView recycling works properly

---

### Test 15: Back Navigation

**Steps:**
1. Open Due Fee Report
2. Tap back button

**Expected Result:**
- ✅ Returns to Finance reports list
- ✅ Slide animation plays
- ✅ No crash or error

---

### Test 16: Rotation Handling

**Steps:**
1. Generate report with records displayed
2. Rotate device to landscape
3. Rotate back to portrait

**Expected Result:**
- ✅ Data is preserved after rotation
- ✅ Layout adjusts correctly
- ✅ No crash or data loss
- ✅ Filters remain selected

---

## 🔍 Detailed Verification

### API Request Verification

**Check Logcat for:**
```
D/DueFeeReportActivity: === Fetching Due Fee Report ===
D/DueFeeReportActivity: Base URL: https://school.cyberdetox.in/
D/DueFeeReportActivity: Full API URL: https://school.cyberdetox.in/api/due-fees-report/filter
D/DueFeeReportActivity: Session ID: 25
D/DueFeeReportActivity: Class ID: 1
D/DueFeeReportActivity: Section ID: 2
D/DueFeeReportActivity: === Request Headers ===
D/DueFeeReportActivity: Client-Service: smartschool
D/DueFeeReportActivity: Auth-Key: schoolAdmin@
D/DueFeeReportActivity: Content-Type: application/json
D/DueFeeReportActivity: === Request Body ===
D/DueFeeReportActivity: {"class_id":"1","section_id":"2","session_id":"25"}
```

### API Response Verification

**Check Logcat for:**
```
D/DueFeeReportActivity: === API Response Received ===
D/DueFeeReportActivity: Response Length: 5432
D/DueFeeReportActivity: Response: {"status":1,"message":"Due fees report retrieved successfully",...}
D/DueFeeReportActivity: === Parsing Response ===
D/DueFeeReportActivity: Status: 1
D/DueFeeReportActivity: Data Array Length: 25
D/DueFeeReportActivity: First Student: John Michael Doe
D/DueFeeReportActivity: Total Balance: 500.00
D/DueFeeReportActivity: Due fee list size: 25
```

---

## 🐛 Common Issues and Solutions

### Issue 1: No data shown but students have due fees
**Cause:** API filtering too restrictive or date filter issue  
**Solution:** 
- Check if due_date filter is set correctly in API
- Verify students are enrolled in selected session
- Check fee_groups are assigned to selected session

### Issue 2: Incorrect fee calculations
**Cause:** Missing fee items or incorrect parsing  
**Solution:**
- Check Logcat for JSON parsing errors
- Verify all fee arrays are being parsed (fees_list and transport_fees)
- Check for null values in fee amounts

### Issue 3: Cards not displaying correctly
**Cause:** Layout resource not found or binding issues  
**Solution:**
- Verify `item_due_fee_report.xml` exists in `res/layout/`
- Check all view IDs match between layout and adapter
- Verify adapter is set to RecyclerView

### Issue 4: Network error on valid connection
**Cause:** Incorrect API URL or authentication  
**Solution:**
- Verify `Constants.dueFeeReportFilterUrl` is correct
- Check `clientService` and `authKey` in Constants
- Verify base URL in SharedPreferences

### Issue 5: Theme colors not applied
**Cause:** Color parsing error or missing preferences  
**Solution:**
- Check primary color is set in SharedPreferences
- Verify color format is valid hex (#RRGGBB)
- Check for exceptions in Logcat

---

## 📊 Test Data Requirements

### Minimum Test Data
- At least 1 active session
- At least 1 class with sections
- At least 5 students with due fees
- At least 1 student with transport fees
- At least 1 student with fine
- At least 1 student with discount

### Recommended Test Data
- 2-3 sessions
- 3-5 classes with multiple sections each
- 20-50 students with various fee statuses
- Mix of paid, partial, and unpaid fees
- Various fee types (tuition, library, lab, etc.)
- Different due dates
- Students with and without transport fees

---

## ✅ Test Completion Checklist

- [ ] All 16 test scenarios passed
- [ ] API requests verified in Logcat
- [ ] API responses verified in Logcat
- [ ] UI displays correctly on different screen sizes
- [ ] Theme colors applied correctly
- [ ] Fee calculations are accurate
- [ ] Error handling works as expected
- [ ] Empty state displays correctly
- [ ] Loading indicators work properly
- [ ] Navigation works correctly
- [ ] No crashes or ANRs
- [ ] Performance is acceptable
- [ ] Data validation is correct
- [ ] Optional fields hide/show correctly

---

## 📝 Test Report Template

```
Test Date: ___________
Tester: ___________
Device: ___________
Android Version: ___________
App Version: ___________

Test Results:
- Navigation: ☐ Pass ☐ Fail
- Filters: ☐ Pass ☐ Fail
- API Integration: ☐ Pass ☐ Fail
- UI Display: ☐ Pass ☐ Fail
- Fee Calculations: ☐ Pass ☐ Fail
- Error Handling: ☐ Pass ☐ Fail
- Performance: ☐ Pass ☐ Fail

Issues Found:
1. ___________
2. ___________
3. ___________

Notes:
___________
___________
___________
```

---

## 🎓 Summary

This testing guide covers all aspects of the Due Fee Report feature. Follow each test scenario carefully and document any issues found. The feature should work seamlessly with proper error handling and user-friendly messages.

**Happy Testing! 🚀**

