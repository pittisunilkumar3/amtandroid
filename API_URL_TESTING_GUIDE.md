# 🧪 API URL Fixes - Testing Guide

## Quick Testing Checklist

---

## 📱 Prerequisites

1. **Build the APK**:
   ```bash
   ./gradlew assembleDebug
   ```

2. **Install on Device**:
   ```bash
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```

3. **Enable Logcat Filtering**:
   ```bash
   adb logcat | grep -E "Utility|SplashActivity|TeacherSubmenuActivity|TeacherStudentDetailsActivity"
   ```

---

## ✅ Test Scenarios

### Test 1: Splash Screen URL ✅
**Purpose**: Verify splash screen uses correct API URL

**Steps**:
1. Launch the app
2. Observe splash screen
3. Check Logcat

**Expected Logs**:
```
D/Utility: API URL enforced: https://school.cyberdetox.in/api/
D/SplashActivity: Maintenance Mode API URL: https://school.cyberdetox.in/api/webservice/getMaintenanceModeStatus
```

**Success Criteria**:
- ✅ URL is `https://school.cyberdetox.in/api/webservice/getMaintenanceModeStatus`
- ✅ NO double `/api/` in URL
- ✅ App proceeds to login or dashboard

---

### Test 2: Teacher Login URL ✅
**Purpose**: Verify teacher login uses correct API URL

**Steps**:
1. Navigate to teacher login
2. Enter credentials:
   - Email: (your teacher email)
   - Password: (your password)
3. Click Login
4. Check Logcat

**Expected Logs**:
```
D/Utility: API URL enforced: https://school.cyberdetox.in/api/
D/Utility: Built API URL: https://school.cyberdetox.in/api/teacher/login
E/Teacher Login URL: https://school.cyberdetox.in/api/teacher/login
```

**Success Criteria**:
- ✅ URL is `https://school.cyberdetox.in/api/teacher/login`
- ✅ Login successful
- ✅ Navigates to teacher dashboard

---

### Test 3: Teacher Menu URL ✅ CRITICAL
**Purpose**: Verify teacher menu API uses correct URL (no double /api/)

**Steps**:
1. After teacher login, dashboard loads
2. Menu items should appear
3. Check Logcat

**Expected Logs**:
```
D/Utility: Built API URL: https://school.cyberdetox.in/api/teacher/menu
D/TeacherSubmenuActivity: Teacher Menu API URL: https://school.cyberdetox.in/api/teacher/menu
```

**Success Criteria**:
- ✅ URL is `https://school.cyberdetox.in/api/teacher/menu`
- ✅ NO double `/api/` (was: `https://school.cyberdetox.in/api//api/teacher/menu`)
- ✅ Menu items load correctly
- ✅ All menu icons display properly

---

### Test 4: Student Details - Sessions API ✅
**Purpose**: Verify sessions API uses correct URL

**Steps**:
1. From teacher dashboard, navigate to: Student Information → Student Details
2. Activity loads and fetches sessions
3. Check Logcat

**Expected Logs**:
```
D/Utility: Built API URL: https://school.cyberdetox.in/api/teacher/sessions-with-classes-sections
D/TeacherStudentDetailsActivity: Sessions API URL: https://school.cyberdetox.in/api/teacher/sessions-with-classes-sections
```

**Success Criteria**:
- ✅ URL is `https://school.cyberdetox.in/api/teacher/sessions-with-classes-sections`
- ✅ Sessions dropdown populates
- ✅ No errors in Logcat

---

### Test 5: Student Details - Students API ✅
**Purpose**: Verify students API uses correct URL

**Steps**:
1. In Student Details activity
2. Select Session, Class, Section
3. Click "Apply Filter"
4. Check Logcat

**Expected Logs**:
```
D/Utility: Built API URL: https://school.cyberdetox.in/api/teacher/students
D/TeacherStudentDetailsActivity: Students API URL: https://school.cyberdetox.in/api/teacher/students
D/TeacherStudentDetailsActivity: Request Body: {"class_id":"19","section_id":"47","session_id":"21"}
```

**Success Criteria**:
- ✅ URL is `https://school.cyberdetox.in/api/teacher/students`
- ✅ Students list loads
- ✅ Student cards display correctly

---

### Test 6: Error Retry Mechanism ✅
**Purpose**: Verify retry uses correct URL

**Steps**:
1. Turn off WiFi/Mobile data
2. Launch the app
3. Should see "Connection Error" dialog
4. Turn on internet
5. Click "Retry" button
6. Check Logcat

**Expected Logs**:
```
D/Utility: API URL enforced: https://school.cyberdetox.in/api/
D/SplashActivity: Maintenance Mode API URL: https://school.cyberdetox.in/api/webservice/getMaintenanceModeStatus
```

**Success Criteria**:
- ✅ Retry uses correct URL
- ✅ App proceeds after retry
- ✅ No errors

---

## 🔍 What to Look For

### ✅ CORRECT URLs:
```
https://school.cyberdetox.in/api/teacher/menu
https://school.cyberdetox.in/api/teacher/login
https://school.cyberdetox.in/api/teacher/sessions-with-classes-sections
https://school.cyberdetox.in/api/teacher/students
https://school.cyberdetox.in/api/webservice/getMaintenanceModeStatus
https://school.cyberdetox.in/api/auth/login
```

### ❌ INCORRECT URLs (Should NOT appear):
```
https://school.cyberdetox.in/api//api/teacher/menu  ❌ (double /api/)
https://school.cyberdetox.in/teacher/menu  ❌ (missing /api/)
http://localhost/api/teacher/menu  ❌ (wrong domain)
```

---

## 📊 Test Results Template

```
Test Date: ___________
Tester: ___________
Device: ___________
Android Version: ___________

Test 1 (Splash Screen): ☐ Pass ☐ Fail
  URL Correct: ☐ Yes ☐ No
  Notes: _________________________________

Test 2 (Teacher Login): ☐ Pass ☐ Fail
  URL Correct: ☐ Yes ☐ No
  Login Success: ☐ Yes ☐ No
  Notes: _________________________________

Test 3 (Teacher Menu): ☐ Pass ☐ Fail
  URL Correct: ☐ Yes ☐ No
  No Double /api/: ☐ Yes ☐ No
  Menu Loads: ☐ Yes ☐ No
  Notes: _________________________________

Test 4 (Sessions API): ☐ Pass ☐ Fail
  URL Correct: ☐ Yes ☐ No
  Sessions Load: ☐ Yes ☐ No
  Notes: _________________________________

Test 5 (Students API): ☐ Pass ☐ Fail
  URL Correct: ☐ Yes ☐ No
  Students Load: ☐ Yes ☐ No
  Notes: _________________________________

Test 6 (Error Retry): ☐ Pass ☐ Fail
  URL Correct: ☐ Yes ☐ No
  Retry Works: ☐ Yes ☐ No
  Notes: _________________________________

Overall Result: ☐ Pass ☐ Fail
```

---

## 🐛 Troubleshooting

### Issue: URLs still showing double /api/
**Solution**: 
1. Uninstall the app completely
2. Rebuild: `./gradlew clean assembleDebug`
3. Reinstall: `adb install app/build/outputs/apk/debug/app-debug.apk`

### Issue: No logs appearing
**Solution**:
```bash
# Use broader filter
adb logcat | grep -i "url\|api"

# Or view all logs
adb logcat
```

### Issue: App crashes on API call
**Solution**:
1. Check Logcat for stack trace
2. Verify internet connection
3. Check server is running
4. Test API with curl:
   ```bash
   curl -X POST "https://school.cyberdetox.in/api/teacher/menu" \
     -H "Client-Service: smartschool" \
     -H "Auth-Key: schoolAdmin@" \
     -H "Content-Type: application/json" \
     -d '{"staff_id":1}'
   ```

---

## ✅ Final Checklist

Before marking as complete:

- ☐ All 6 test scenarios pass
- ☐ All URLs use `https://school.cyberdetox.in/api/`
- ☐ No double `/api/` in any URL
- ☐ Teacher login works
- ☐ Teacher menu loads
- ☐ Student details works
- ☐ Error retry works
- ☐ Build successful
- ☐ No crashes

---

## 📞 Support

If you encounter issues:

1. **Check Logcat** for detailed error messages
2. **Verify API URL** in Constants.java is correct
3. **Test API directly** with curl to isolate issues
4. **Check network permissions** in AndroidManifest.xml
5. **Verify device has internet** connection

---

**Happy Testing!** 🚀

