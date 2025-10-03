# Teacher Dashboard Submenu System - Testing Guide

## Quick Test Instructions

### Step 1: Build and Install
```bash
./gradlew assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### Step 2: Launch and Login
1. Open the app
2. Login as a teacher
3. Navigate to Teacher Dashboard

### Step 3: Test Basic Functionality

**Test 1: Student Information Module**
1. Click on "Student Information" icon
2. ✅ Should navigate to submenu screen
3. ✅ Should show title "Student Information"
4. ✅ Should display 9 submenu items
5. ✅ Each item should have an icon and text

**Test 2: Fees Collection Module**
1. Click on "Fees Collection" icon
2. ✅ Should display 10 submenu items
3. ✅ Icons should be relevant (money/payment icons)

**Test 3: Attendance Module**
1. Click on "Attendance" icon
2. ✅ Should display 3 submenu items
3. ✅ Icons should show calendar/check icons

### Step 4: Check Logcat

Open Logcat and filter by `TeacherSubmenuActivity`:
```bash
adb logcat -s TeacherSubmenuActivity:D
```

**Expected Log Output:**
```
D/TeacherSubmenuActivity: Menu ID: 2, Name: Student Information, Activate: student_information
D/TeacherSubmenuActivity: Loading submenus for menu: Student Information (ID: 2)
D/TeacherSubmenuActivity: API Response received
D/TeacherSubmenuActivity: Total menus in response: 38
D/TeacherSubmenuActivity: Found matching menu: Student Information
D/TeacherSubmenuActivity: Target menu found: Student Information
D/TeacherSubmenuActivity: Submenus count: 9
D/TeacherSubmenuActivity: Parsing 9 submenu items
D/TeacherSubmenuActivity: Displaying 9 submenu items
D/TeacherSubmenuActivity: Adapter set and notified
```

### Step 5: Visual Verification

Compare the submenu screen with the Reports category screen:
- ✅ Same white card with rounded corners
- ✅ Same title positioning
- ✅ Same list layout
- ✅ Same icon + text + arrow design
- ✅ Same theme colors

## Common Issues and Solutions

### Issue: Blank Screen (No Submenus)

**Check Logcat for:**
```
D/TeacherSubmenuActivity: Submenus count: 0
```
or
```
W/TeacherSubmenuActivity: No submenus found for menu: ...
```

**Solution:**
- Verify API is returning data
- Check network connection
- Verify menu has submenus in API response

### Issue: "Menu not found" Error

**Check Logcat for:**
```
W/TeacherSubmenuActivity: Target menu not found. Looking for ID: X, Activate: Y
```

**Solution:**
- Check TeacherModuleAdapter is passing correct data
- Verify module.getId() and module.getActivateMenu() are not null
- Compare with API response structure

### Issue: Icons Not Displaying

**Check:**
- Drawable resources exist
- Icon mapping logic in `getIconForSubmenuItem()`
- Logcat for resource not found errors

**Solution:**
- Use default icon (ic_fa_list_alt) as fallback
- Verify all referenced drawables exist

## Quick Test Checklist

- [ ] App builds successfully
- [ ] App installs without errors
- [ ] Teacher login works
- [ ] Dashboard displays all modules
- [ ] Clicking module navigates to submenu
- [ ] Submenu title is correct
- [ ] Submenu items are visible
- [ ] Icons are displayed
- [ ] Clicking submenu item shows toast
- [ ] Back button returns to dashboard
- [ ] No crashes or errors
- [ ] Logcat shows expected output

## Test 3-4 Different Modules

### Module 1: Front Office
- Expected submenus: 7
- Key items: Admission Enquiry, Visitor Book, Phone Call Log

### Module 2: Student Information
- Expected submenus: 9
- Key items: Student Details, Student Admission, Online Admission

### Module 3: Fees Collection
- Expected submenus: 10
- Key items: Collect Fees, Search Fees Payment, Fees Master

### Module 4: Attendance
- Expected submenus: 3
- Key items: Student Attendance, Attendance By Date, Approve Leave

## Success Criteria

✅ **PASS** if:
- All modules navigate correctly
- Submenus display for each module
- UI matches Reports module design
- No crashes or errors

❌ **FAIL** if:
- Blank screens
- Crashes
- Wrong submenu count
- UI doesn't match Reports

## Report Issues

If you encounter issues:
1. Collect Logcat output
2. Note which module failed
3. Take screenshots
4. Check SUBMENU_DEBUGGING_GUIDE.md

