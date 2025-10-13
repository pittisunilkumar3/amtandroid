# Quick Testing Guide - Monthly Staff Attendance Calendar

## 🎯 What to Test

### 1. Main List View
- **Staff cards appear** with name, ID, role
- **Percentage displays** (shows "-" or number)
- **Color coding works**: Green for good, Red for poor
- **All 31 days visible** in horizontal scroll bar
- **Attendance markers** (P, A, L, F, H, -) show on each day
- **Summary counts** (Present, Absent, Late, etc.) display

### 2. Calendar Popup
- **Click on any staff card** → Popup opens
- **Full calendar grid** shows with 7 columns (Mon-Sun)
- **All days of month** visible in grid format
- **Days aligned correctly** (Oct 1 = Wednesday shows in Wed column)
- **Color-coded backgrounds**:
  - Light Green = Present
  - Light Red = Absent
  - Light Yellow = Late
  - Light Blue = Half Day
  - Light Gray = Holiday/Not Marked
- **Close button** dismisses popup

### 3. Data Accuracy
- **Matches API response** (check staff names, IDs)
- **Percentage calculation correct**
- **Summary counts match** daily attendance
- **Attendance markers correct** for each date

---

## 🚀 How to Test

### Step 1: Build & Install
```powershell
.\gradlew assembleDebug
```
Install: `app\build\outputs\apk\debug\app-debug.apk`

### Step 2: Navigate to Report
1. Open app
2. Go to **Reports** → **Attendance** → **Staff Attendance Report**

### Step 3: Generate Report
1. Select filters (optional):
   - Role: Any or specific role
   - Month: October
   - Year: 2025
2. Click **"Generate Report"** button

### Step 4: Verify List View
- See staff cards with calendar strips
- Scroll horizontally within each card to see all dates
- Check colors and markers

### Step 5: Test Popup
- Click on any staff card
- Popup should open with full calendar
- Scroll if needed (for smaller screens)
- Click "Close" to dismiss
- Repeat for different staff members

---

## ✅ Expected Results

### For Staff with No Attendance Data
- Percentage: **0%** or **-**
- Status: **"No Data"**
- Color: **Gray**
- All day markers: **"-"**
- Summary counts: All **0**

### For Staff with Attendance Data
- Percentage: **Calculated value**
- Status: **"Good Attendance"** or similar
- Color: **Green (>75%)** or **Red (<75%)**
- Day markers: **P, A, L, F, H** based on actual attendance
- Summary counts: **Actual counts**

---

## 🐛 Common Issues & Fixes

### Issue: "No data" showing even with Generate Report
**Check**: 
- API endpoint configured correctly in Constants.java
- Internet connection active
- Filters not too restrictive

### Issue: Popup not opening
**Check**:
- Click on card (not on scroll area)
- dialog_monthly_calendar.xml exists in res/layout
- No crash in Logcat

### Issue: Wrong colors showing
**Check**:
- colors.xml has all required colors
- Color parsing logic correct
- API attendance_type matches case (e.g., "Present" not "present")

### Issue: Days misaligned in calendar
**Check**:
- First day offset calculated correctly
- day_short from API is correct format (Mon, Tue, etc.)
- GridLayout columns = 7

---

## 📊 Test Scenarios

| Scenario | Expected Behavior |
|----------|-------------------|
| Click Generate Report | Shows list of staff cards |
| Scroll horizontally in card | See all 31 days |
| Click on staff card | Popup calendar opens |
| No attendance data | Shows "-" markers, 0%, gray |
| Some attendance marked | Shows correct markers (P, A, L) |
| Month starting on Monday | Calendar grid aligns at column 1 |
| Month starting on Sunday | Calendar grid aligns at column 7 (with 6 empty cells) |
| Multiple staff members | Each popup shows correct data |
| Click Close in popup | Dialog dismisses |
| Rotate device | Layout adjusts (ScrollView helps) |

---

## 📱 Device Testing

### Minimum Requirements
- Android SDK: Check minSdk in build.gradle
- Screen: 5" or larger recommended
- RAM: 2GB+ recommended

### Test Devices
- [ ] Phone (5-6 inch)
- [ ] Tablet (7+ inch)
- [ ] Different Android versions
- [ ] Portrait orientation
- [ ] Landscape orientation (optional)

---

## 🔍 Debugging

### Enable Logging
Look for these tags in Logcat:
```
StaffAttendanceReport
```

### Key Log Messages
- "Loading filtered staff attendance from: [URL]"
- "Request body: [JSON]"
- "Filtered staff attendance response: [JSON]"
- "Loaded X staff attendance records"
- "Error parsing staff attendance response"

### Check API Response
Log shows full JSON - verify:
- `status`: should be 1
- `dates`: array of all dates
- `data`: array of staff objects
- `daily_attendance`: object with date keys
- `attendance_percentage_display`: can be "-" or number

---

## ✨ Success Criteria

✅ **Main View**
- All staff appear in list
- Calendar strips show all dates
- Colors match attendance status
- Horizontal scroll works smoothly

✅ **Popup View**
- Opens on card click
- Shows full calendar grid
- All days visible
- Colors match attendance types
- Staff details correct
- Close button works

✅ **Data Accuracy**
- Percentages correct
- Counts match daily data
- Markers on correct dates
- No crashes or errors

---

## 📞 Report Issues

If you find bugs, note:
1. Device model & Android version
2. Steps to reproduce
3. Expected vs actual behavior
4. Screenshots if applicable
5. Logcat errors

---

**Testing Time Estimate**: 10-15 minutes  
**Priority**: HIGH  
**Status**: Ready for Testing
