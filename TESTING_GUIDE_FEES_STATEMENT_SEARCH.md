# Testing Guide - Fees Statement Search Feature

## 📱 Test Environment

**Device:** BRP-NX1 - Android 15  
**App Version:** Debug Build  
**Build Status:** ✅ Successful  
**Installation Status:** ✅ Installed  
**Date:** October 10, 2025

---

## 🎯 Testing Objectives

1. Verify search functionality works correctly
2. Validate API integration with Report By Name endpoint
3. Test user interface and user experience
4. Ensure proper error handling
5. Verify report generation after student selection

---

## 📋 Pre-Testing Checklist

- [x] App built successfully
- [x] App installed on device
- [ ] Teacher login credentials available
- [ ] Test data available (student names, admission numbers)
- [ ] Network connection active
- [ ] API server accessible

---

## 🧪 Test Cases

### Test Case 1: Access Fees Statement Screen

**Steps:**
1. Launch the app
2. Login as Teacher
3. Navigate to Dashboard
4. Tap on "Reports"
5. Tap on "Finance"
6. Tap on "Fees Statement"

**Expected Result:**
- Fees Statement screen opens
- Search section visible at top
- Filter section visible below with OR divider
- All UI elements properly themed

**Status:** [ ] Pass [ ] Fail

**Notes:**
_______________________________________

---

### Test Case 2: Search by Student Name

**Steps:**
1. Open Fees Statement screen
2. Tap on search EditText
3. Enter student name (e.g., "John")
4. Tap "Search" button

**Expected Result:**
- Search dialog opens
- Loading indicator shows
- Search results display after API response
- Result count shows (e.g., "Found 5 student(s)")
- Each result shows:
  - Student name
  - Admission number
  - Class and section
  - Roll number
  - Fee summary (Total, Paid, Balance)

**Status:** [ ] Pass [ ] Fail

**Notes:**
_______________________________________

---

### Test Case 3: Search by Admission Number

**Steps:**
1. Open Fees Statement screen
2. Enter admission number in search box (e.g., "ADM001")
3. Tap "Search" button

**Expected Result:**
- Search dialog opens
- Specific student found
- Student details displayed correctly
- Fee amounts formatted with currency symbol

**Status:** [ ] Pass [ ] Fail

**Notes:**
_______________________________________

---

### Test Case 4: Partial Name Search

**Steps:**
1. Open Fees Statement screen
2. Enter partial name (e.g., "Joh" for "John")
3. Tap "Search" button

**Expected Result:**
- All students with matching partial name displayed
- Results include firstname, middlename, and lastname matches

**Status:** [ ] Pass [ ] Fail

**Notes:**
_______________________________________

---

### Test Case 5: Empty Search Validation

**Steps:**
1. Open Fees Statement screen
2. Leave search box empty
3. Tap "Search" button

**Expected Result:**
- Toast message: "Please enter a name or admission number to search"
- No API call made
- Search dialog does not open

**Status:** [ ] Pass [ ] Fail

**Notes:**
_______________________________________

---

### Test Case 6: No Results Found

**Steps:**
1. Open Fees Statement screen
2. Enter non-existent name (e.g., "ZZZZZ")
3. Tap "Search" button

**Expected Result:**
- Search dialog opens
- No results layout displayed
- Message: "No students found"
- Helpful text: "Try searching with a different name or admission number"

**Status:** [ ] Pass [ ] Fail

**Notes:**
_______________________________________

---

### Test Case 7: Select Student from Search Results

**Steps:**
1. Perform a search with results
2. Tap on any student card in results

**Expected Result:**
- Dialog closes automatically
- Toast message shows: "Selected: [Student Name] ([Admission No])"
- Loading indicator appears
- Report generation starts automatically
- Search text clears

**Status:** [ ] Pass [ ] Fail

**Notes:**
_______________________________________

---

### Test Case 8: Close Search Dialog

**Steps:**
1. Perform a search
2. Tap the close (X) button in dialog

**Expected Result:**
- Dialog closes
- No student selected
- Search text remains in EditText
- Can perform another search

**Status:** [ ] Pass [ ] Fail

**Notes:**
_______________________________________

---

### Test Case 9: Network Error Handling

**Steps:**
1. Disable network connection
2. Perform a search
3. Re-enable network

**Expected Result:**
- Error message displayed
- No results layout shown
- Toast with error details
- App doesn't crash

**Status:** [ ] Pass [ ] Fail

**Notes:**
_______________________________________

---

### Test Case 10: Theme Color Integration

**Steps:**
1. Check if school has custom theme colors
2. Open Fees Statement screen
3. Observe search button color

**Expected Result:**
- Search button uses primary theme color
- Matches other buttons (Generate Report)
- Consistent with app theme

**Status:** [ ] Pass [ ] Fail

**Notes:**
_______________________________________

---

### Test Case 11: Search vs Filter Selection

**Steps:**
1. Use search to select a student
2. Verify report generates
3. Go back to Fees Statement
4. Use filters (Session → Class → Section → Student)
5. Generate report

**Expected Result:**
- Both methods work independently
- Search method is faster
- Filter method provides more control
- Both generate correct reports

**Status:** [ ] Pass [ ] Fail

**Notes:**
_______________________________________

---

### Test Case 12: Fee Amount Formatting

**Steps:**
1. Perform a search
2. Check fee amounts in results

**Expected Result:**
- Currency symbol displayed (₹, $, etc.)
- Amounts formatted with commas (e.g., ₹10,000.00)
- Paid amount in green color
- Balance amount in red color
- Decimal places shown correctly

**Status:** [ ] Pass [ ] Fail

**Notes:**
_______________________________________

---

### Test Case 13: Multiple Search Sessions

**Steps:**
1. Perform first search
2. Close dialog
3. Perform second search with different text
4. Select a student
5. Perform third search

**Expected Result:**
- Each search works independently
- No data from previous searches
- Dialog state resets properly
- No memory leaks

**Status:** [ ] Pass [ ] Fail

**Notes:**
_______________________________________

---

### Test Case 14: Landscape Orientation

**Steps:**
1. Open Fees Statement screen
2. Rotate device to landscape
3. Perform a search
4. Rotate back to portrait

**Expected Result:**
- Layout adapts to orientation
- Search dialog displays properly
- No data loss on rotation
- UI remains functional

**Status:** [ ] Pass [ ] Fail

**Notes:**
_______________________________________

---

### Test Case 15: Long Student Names

**Steps:**
1. Search for student with very long name
2. Check display in results

**Expected Result:**
- Long names wrap properly
- No text cutoff
- Card height adjusts
- Readable and properly formatted

**Status:** [ ] Pass [ ] Fail

**Notes:**
_______________________________________

---

## 🔍 API Testing

### API Endpoint Verification

**Endpoint:** `POST /api/report-by-name/filter`

**Request Headers:**
```
Client-Service: smartschool
Auth-Key: schoolAdmin@
Content-Type: application/json
```

**Request Body:**
```json
{
  "search_text": "John"
}
```

**Expected Response:**
```json
{
  "status": 1,
  "message": "Report by name retrieved successfully",
  "total_records": 5,
  "data": [...]
}
```

**Verification Points:**
- [ ] Correct endpoint called
- [ ] Headers sent correctly
- [ ] Request body formatted properly
- [ ] Response parsed successfully
- [ ] Error responses handled

---

## 📊 Performance Testing

### Response Time Benchmarks

| Action | Expected Time | Actual Time | Status |
|--------|--------------|-------------|--------|
| Search API Call | < 2 seconds | _______ | [ ] |
| Dialog Open | < 500ms | _______ | [ ] |
| Results Display | < 1 second | _______ | [ ] |
| Student Selection | < 500ms | _______ | [ ] |
| Report Generation | < 3 seconds | _______ | [ ] |

---

## 🐛 Bug Report Template

**Bug ID:** _______  
**Severity:** [ ] Critical [ ] High [ ] Medium [ ] Low  
**Test Case:** _______  
**Steps to Reproduce:**
1. _______
2. _______
3. _______

**Expected Result:** _______

**Actual Result:** _______

**Screenshots/Logs:** _______

**Device Info:** _______

**Additional Notes:** _______

---

## ✅ Test Summary

**Total Test Cases:** 15  
**Passed:** _______  
**Failed:** _______  
**Blocked:** _______  
**Not Tested:** _______

**Pass Rate:** _______%

---

## 📝 Testing Notes

### Positive Observations:
- _______________________________________
- _______________________________________
- _______________________________________

### Issues Found:
- _______________________________________
- _______________________________________
- _______________________________________

### Recommendations:
- _______________________________________
- _______________________________________
- _______________________________________

---

## 🎓 Test Data

### Sample Student Names for Testing:
- John Doe
- Jane Smith
- Michael Johnson
- Sarah Williams
- David Brown

### Sample Admission Numbers:
- ADM001
- ADM002
- ADM003
- STU001
- STU002

### Sample Search Queries:
- Full name: "John Doe"
- Partial name: "Joh"
- Admission number: "ADM001"
- Partial admission: "ADM"
- Common name: "Smith"

---

## 🔄 Regression Testing

After any bug fixes or changes, re-test:
- [ ] All failed test cases
- [ ] Related functionality
- [ ] Critical path (login → navigate → search → select → report)

---

## 📱 Device Compatibility

Test on multiple devices if available:
- [ ] Android 10 (API 29)
- [ ] Android 11 (API 30)
- [ ] Android 12 (API 31)
- [ ] Android 13 (API 33)
- [ ] Android 14 (API 34)
- [x] Android 15 (API 35) - Current device

---

## 🎯 Acceptance Criteria

Feature is ready for production when:
- [ ] All test cases pass
- [ ] No critical or high severity bugs
- [ ] Performance meets benchmarks
- [ ] UI/UX approved
- [ ] API integration verified
- [ ] Error handling tested
- [ ] Documentation complete

---

**Tester Name:** _______________________  
**Test Date:** _______________________  
**Sign-off:** _______________________

---

**Status:** 🟡 Ready for Testing  
**Last Updated:** October 10, 2025

