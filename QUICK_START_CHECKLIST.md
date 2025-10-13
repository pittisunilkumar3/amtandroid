# Staff Attendance Report - Quick Start Checklist

## 🚀 Quick Start Guide

### Step 1: Build and Install
```bash
# Build the project
./gradlew assembleDebug

# Install on device/emulator
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### Step 2: Launch the Feature
1. Open the app
2. Login as admin/teacher
3. Navigate to: **Reports → Staff Attendance Report**

### Step 3: Test Basic Functionality
- [ ] Screen loads without crashes
- [ ] Filter spinners are populated
- [ ] "Generate Report" button is visible
- [ ] "Clear Filters" button is visible

### Step 4: Test Report Generation
1. Select filters:
   - Role: **Super Admin**
   - Month: **October**
   - Year: **2025**
2. Click **Generate Report**
3. Verify:
   - [ ] Progress bar shows
   - [ ] Data loads successfully
   - [ ] Staff cards are displayed
   - [ ] No crashes or errors

### Step 5: Verify Data Display
For each staff card, check:
- [ ] Staff name is visible
- [ ] Employee ID is visible
- [ ] Role is visible
- [ ] Percentage is large and color-coded
- [ ] Status text matches percentage color
- [ ] Summary shows: P: X, A: X, L: X, H: X, HD: X
- [ ] Working days info is visible
- [ ] Daily attendance markers are visible
- [ ] Can scroll horizontally to see all days

### Step 6: Test Calendar Dialog
1. Click on any staff card
2. Verify dialog shows:
   - [ ] Staff information
   - [ ] Large percentage and status
   - [ ] Attendance summary
   - [ ] Calendar grid with all days
   - [ ] Color-coded day cells
   - [ ] Legend
   - [ ] Close button works

### Step 7: Check Logs
Open logcat and filter by:
- `MonthlyStaffAttendance`
- `MonthlyStaffAdapter`

Look for:
- [ ] "=== GENERATING REPORT ===" message
- [ ] "=== FINAL REQUEST BODY ===" with correct JSON
- [ ] "=== API RESPONSE START ===" with data
- [ ] No error messages or exceptions

## 📋 Expected Request Body

When you select:
- Role: Super Admin
- Month: October  
- Year: 2025

You should see in logs:
```json
{
    "role": "admin",
    "month": "October",
    "month_number": 10,
    "year": 2025
}
```

## ✅ Success Criteria

The feature is working correctly if:
1. ✅ No crashes or exceptions
2. ✅ Filters send correct data to API
3. ✅ API response is parsed successfully
4. ✅ All staff data is displayed correctly
5. ✅ Attendance summary shows correct counts
6. ✅ Daily markers show for all days
7. ✅ Calendar dialog works properly
8. ✅ Colors are applied correctly

## 🐛 Common Issues & Solutions

### Issue: No data displayed
**Check:**
- Internet connection
- API endpoint is correct
- Request body format (check logs)
- API response status is 1

**Solution:**
- Verify API is accessible
- Check logs for error messages
- Ensure filters are valid

### Issue: Attendance summary shows zeros
**Check:**
- API response includes `attendance_summary` object
- Summary parsing logs

**Solution:**
- Verify API returns summary data
- Check if summary object exists in response

### Issue: Daily markers not showing
**Check:**
- `dates` array in API response
- `daily_attendance` object in API response
- Adapter logs for "Creating day views"

**Solution:**
- Verify dates array is not empty
- Verify daily_attendance map is populated
- Check date key format matches (YYYY-MM-DD)

### Issue: Wrong data sent to API
**Check:**
- "=== FINAL REQUEST BODY ===" in logs
- Month number is included
- Role is lowercase

**Solution:**
- Verify month_number field is present
- Verify role mapping is correct
- Check filter selection logic

## 📱 Quick Test Scenarios

### Scenario 1: All Filters (2 minutes)
1. Select: Super Admin, October, 2025
2. Generate Report
3. Verify data displays
4. Click a card to see calendar
5. Close dialog

### Scenario 2: Month Only (1 minute)
1. Select: All Roles, October, All Years
2. Generate Report
3. Verify data displays

### Scenario 3: No Filters (1 minute)
1. Keep all at "All"
2. Generate Report
3. Verify data displays

### Scenario 4: Clear Filters (30 seconds)
1. Select some filters
2. Click Clear Filters
3. Verify all reset to "All"

## 🎯 Key Points to Verify

### Request Body ✅
- [ ] `role` is lowercase (e.g., "admin", not "Super Admin")
- [ ] `month` is month name (e.g., "October")
- [ ] `month_number` is numeric (e.g., 10)
- [ ] `year` is numeric (e.g., 2025)

### Display ✅
- [ ] Percentage color: Green (≥75%), Red (<50%), Gray (50-74%)
- [ ] Summary colors: P=green, A=red, L=orange, H=blue, HD=gray
- [ ] Day markers: P, A, L, F, H, or -
- [ ] Day backgrounds: Light green, red, yellow, blue, gray

### Calendar Dialog ✅
- [ ] 7 columns (Mon-Sun)
- [ ] All days of month visible
- [ ] Color-coded backgrounds
- [ ] Legend explains codes
- [ ] Scrollable if needed

## 📊 Performance Benchmarks

Expected performance:
- **API Response Time:** < 3 seconds
- **Data Parsing:** < 1 second
- **UI Rendering:** < 1 second
- **Scroll Performance:** Smooth (60 FPS)
- **Dialog Open:** Instant

If performance is slower:
- Check network speed
- Check device performance
- Review logs for bottlenecks

## 🔍 Debugging Commands

### View Logs
```bash
# Filter by activity
adb logcat -s MonthlyStaffAttendance

# Filter by adapter
adb logcat -s MonthlyStaffAdapter

# View all app logs
adb logcat | grep "com.qdocs.ssre241123"
```

### Clear App Data
```bash
adb shell pm clear com.qdocs.ssre241123
```

### Reinstall App
```bash
adb uninstall com.qdocs.ssre241123
adb install app/build/outputs/apk/debug/app-debug.apk
```

## 📞 Support

If you encounter issues:
1. ✅ Check this checklist first
2. ✅ Review the testing guide (STAFF_ATTENDANCE_TESTING_GUIDE.md)
3. ✅ Check the fixes summary (STAFF_ATTENDANCE_FIXES_SUMMARY.md)
4. ✅ Capture logs and screenshots
5. ✅ Document steps to reproduce

## 🎉 Success!

If all checkboxes are ticked, the feature is working correctly! 

**Next Steps:**
1. Perform comprehensive testing (see STAFF_ATTENDANCE_TESTING_GUIDE.md)
2. Test with different data sets
3. Test edge cases
4. Get user acceptance
5. Deploy to production

---

**Last Updated:** 2025-10-13
**Status:** ✅ All fixes applied, ready for testing

