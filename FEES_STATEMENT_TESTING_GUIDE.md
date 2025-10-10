# Fees Statement Report - Testing Guide

## 🚀 Quick Start

### Prerequisites
1. Android device or emulator connected
2. Smart School backend server running
3. Teacher account credentials

---

## 📱 Installation Steps

### 1. Build the APK
```bash
cd smart_school_android_app_src
./gradlew assembleDebug
```

### 2. Install on Device
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### 3. Launch the App
- Open Smart School app on device
- Login with teacher credentials

---

## 🧪 Testing Steps

### Test 1: Navigation to Fees Statement
**Steps:**
1. Login as teacher
2. Navigate to Dashboard
3. Click on "Reports" menu
4. Click on "Finance" category
5. Click on "Fees Statement" report

**Expected Result:**
- Fees Statement activity opens
- Title shows "Fees Statement"
- Filters card is visible
- All 4 dropdowns are visible (Session, Class, Section, Student)
- Generate Report button is visible

---

### Test 2: Load Hierarchical Data
**Steps:**
1. Open Fees Statement activity
2. Observe the loading behavior

**Expected Result:**
- Progress bar shows briefly
- Session dropdown populates with sessions
- Class dropdown shows "Select Class" (disabled)
- Section dropdown shows "Select Section" (disabled)
- Student dropdown shows "Select Student" (disabled)

**Check Logs:**
```
D/FeesStatementActivity: Loading hierarchical data from API
D/FeesStatementActivity: API URL: http://localhost/amt/api/fee-collection-filters/get-hierarchy
D/FeesStatementActivity: Hierarchical data loaded successfully
D/FeesStatementActivity: Parsed X sessions
```

---

### Test 3: Session Selection (Cascading)
**Steps:**
1. Click on Session dropdown
2. Select a session (e.g., "2024-2025")
3. Observe the Class dropdown

**Expected Result:**
- Class dropdown becomes enabled
- Class dropdown populates with classes for selected session
- Section dropdown remains disabled with "Select Section"
- Student dropdown remains disabled with "Select Student"

**Check Logs:**
```
D/FeesStatementActivity: Selected Session ID: 21
```

---

### Test 4: Class Selection (Cascading)
**Steps:**
1. Select a session first
2. Click on Class dropdown
3. Select a class (e.g., "Class 1")
4. Observe the Section dropdown

**Expected Result:**
- Section dropdown becomes enabled
- Section dropdown populates with sections for selected class
- Student dropdown remains disabled with "Select Student"

**Check Logs:**
```
D/FeesStatementActivity: Selected Class ID: 19
```

---

### Test 5: Section Selection (Cascading)
**Steps:**
1. Select session and class first
2. Click on Section dropdown
3. Select a section (e.g., "Section A")
4. Observe the Student dropdown

**Expected Result:**
- Student dropdown becomes enabled
- Student dropdown populates with students for selected section
- Students show as "Full Name (Admission No)"
- Example: "John Doe (STU001)"

**Check Logs:**
```
D/FeesStatementActivity: Selected Section ID: 1
```

---

### Test 6: Student Selection
**Steps:**
1. Select session, class, and section first
2. Click on Student dropdown
3. Select a student

**Expected Result:**
- Student is selected
- Generate Report button remains enabled

**Check Logs:**
```
D/FeesStatementActivity: Selected Student ID: 101
```

---

### Test 7: Generate Report Without Student
**Steps:**
1. Open Fees Statement activity
2. Do NOT select any filters
3. Click "Generate Report" button

**Expected Result:**
- Toast message appears: "Please select a student"
- No API call is made
- Report is not generated

---

### Test 8: Generate Report With All Filters
**Steps:**
1. Select Session
2. Select Class
3. Select Section
4. Select Student
5. Click "Generate Report" button

**Expected Result:**
- Progress bar shows
- API call is made to `/api/fees-statement/filter`
- Request includes all selected filter IDs
- Response is received
- Success/error message is shown

**Check Logs:**
```
D/FeesStatementActivity: Generate Report clicked
D/FeesStatementActivity: Selected Session ID: 21
D/FeesStatementActivity: Selected Class ID: 19
D/FeesStatementActivity: Selected Section ID: 1
D/FeesStatementActivity: Selected Student ID: 101
D/FeesStatementActivity: Fetching report from API
D/FeesStatementActivity: API URL: http://localhost/amt/api/fees-statement/filter
D/FeesStatementActivity: Request body: {"session_id":"21","class_id":"19","section_id":"1","student_id":"101"}
D/FeesStatementActivity: Report fetched successfully
```

---

### Test 9: Cascading Reset - Change Session
**Steps:**
1. Select all filters (Session, Class, Section, Student)
2. Change the Session dropdown to a different session
3. Observe other dropdowns

**Expected Result:**
- Class dropdown resets and shows new classes for new session
- Section dropdown resets to "Select Section" (disabled)
- Student dropdown resets to "Select Student" (disabled)
- Previously selected class, section, student are cleared

---

### Test 10: Cascading Reset - Change Class
**Steps:**
1. Select all filters (Session, Class, Section, Student)
2. Change the Class dropdown to a different class
3. Observe other dropdowns

**Expected Result:**
- Section dropdown resets and shows new sections for new class
- Student dropdown resets to "Select Student" (disabled)
- Previously selected section and student are cleared
- Session remains selected

---

### Test 11: Cascading Reset - Change Section
**Steps:**
1. Select all filters (Session, Class, Section, Student)
2. Change the Section dropdown to a different section
3. Observe Student dropdown

**Expected Result:**
- Student dropdown resets and shows new students for new section
- Previously selected student is cleared
- Session and Class remain selected

---

### Test 12: Back Button
**Steps:**
1. Open Fees Statement activity
2. Click back button in action bar

**Expected Result:**
- Activity closes
- Returns to previous screen (Finance Reports list)
- Slide animation plays

---

### Test 13: Theme Colors
**Steps:**
1. Check if school has custom theme colors configured
2. Open Fees Statement activity
3. Observe colors

**Expected Result:**
- Action bar uses primary color from theme
- Generate Report button uses primary color from theme
- If no custom colors, uses default colors

---

### Test 14: Error Handling - Network Error
**Steps:**
1. Disconnect device from network
2. Open Fees Statement activity

**Expected Result:**
- Progress bar shows
- Error toast appears: "Error loading filters: [error message]"
- Session dropdown remains empty

---

### Test 15: Error Handling - API Error
**Steps:**
1. Ensure backend API returns error (status: 0)
2. Open Fees Statement activity

**Expected Result:**
- Progress bar shows
- Error toast appears with API error message
- Session dropdown remains empty

---

## 📊 Test Data Requirements

### Minimum Test Data Needed
1. **At least 1 Session** with:
   - At least 1 Class
   - At least 1 Section in that class
   - At least 1 Student in that section

### Recommended Test Data
1. **Multiple Sessions** (e.g., 2024-2025, 2023-2024)
2. **Multiple Classes per Session** (e.g., Class 1, Class 2, Class 3)
3. **Multiple Sections per Class** (e.g., Section A, Section B)
4. **Multiple Students per Section** (e.g., 5-10 students)

---

## 🐛 Common Issues & Solutions

### Issue 1: Dropdowns Not Populating
**Symptoms:** Session dropdown remains empty after loading

**Possible Causes:**
- API not returning data
- Network connectivity issue
- Incorrect API endpoint
- Authentication headers missing

**Solution:**
1. Check logcat for API errors
2. Verify backend API is running
3. Test API with Postman/curl
4. Check Constants.java for correct domain

---

### Issue 2: Cascading Not Working
**Symptoms:** Selecting session doesn't populate class dropdown

**Possible Causes:**
- API response structure mismatch
- JSON parsing error
- Null pointer exception

**Solution:**
1. Check logcat for parsing errors
2. Verify API response structure matches expected format
3. Check if classes array exists in session object

---

### Issue 3: Student Names Not Showing
**Symptoms:** Student dropdown shows blank entries

**Possible Causes:**
- full_name field is null/empty in API response
- admission_no field is missing

**Solution:**
1. Check API response for student data
2. Verify full_name and admission_no fields exist
3. Check if students array is populated

---

### Issue 4: Generate Report Fails
**Symptoms:** Clicking Generate Report shows error

**Possible Causes:**
- Student not selected
- API endpoint incorrect
- Request body format wrong

**Solution:**
1. Verify student is selected
2. Check logcat for request body
3. Test API endpoint with Postman
4. Verify request format matches API expectations

---

## 📝 Logcat Filters

### View All Fees Statement Logs
```
adb logcat -s FeesStatementActivity
```

### View API Requests
```
adb logcat | grep "API URL"
```

### View Errors Only
```
adb logcat *:E
```

---

## ✅ Test Results Template

| Test # | Test Name | Status | Notes |
|--------|-----------|--------|-------|
| 1 | Navigation | ⬜ | |
| 2 | Load Data | ⬜ | |
| 3 | Session Selection | ⬜ | |
| 4 | Class Selection | ⬜ | |
| 5 | Section Selection | ⬜ | |
| 6 | Student Selection | ⬜ | |
| 7 | Validation | ⬜ | |
| 8 | Generate Report | ⬜ | |
| 9 | Reset Session | ⬜ | |
| 10 | Reset Class | ⬜ | |
| 11 | Reset Section | ⬜ | |
| 12 | Back Button | ⬜ | |
| 13 | Theme Colors | ⬜ | |
| 14 | Network Error | ⬜ | |
| 15 | API Error | ⬜ | |

Legend: ✅ Pass | ❌ Fail | ⬜ Not Tested

---

## 🎯 Success Criteria

The implementation is considered successful if:
- ✅ All 15 tests pass
- ✅ No crashes occur
- ✅ Cascading dropdowns work smoothly
- ✅ API integration works correctly
- ✅ Validation works as expected
- ✅ UI is responsive and user-friendly

---

**Testing Date:** _______________  
**Tester Name:** _______________  
**Device/Emulator:** _______________  
**Android Version:** _______________  
**Overall Result:** ⬜ Pass | ⬜ Fail

