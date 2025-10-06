# 🧪 Splash Screen API Fix - Testing Guide

## Quick Testing Checklist

---

## ✅ Test Scenarios

### Scenario 1: Normal Operation (Server Working)
**Expected**: App should work normally

**Steps**:
1. Ensure server is running and API is working
2. Launch the app
3. Wait for splash screen
4. Should navigate to login or dashboard

**Expected Result**: ✅ Normal flow without errors

---

### Scenario 2: Server Error (500)
**Expected**: App should show error dialog with options

**Steps**:
1. Launch the app (server currently returns 500 error)
2. Wait for splash screen
3. Should see "Server Error" dialog

**Dialog Options**:
- **Retry**: Try API call again
- **Continue to Login**: Skip maintenance check and go to login
- **Exit**: Close the app

**Expected Result**: ✅ All three options work correctly

**Test Each Option**:
- Click "Retry" → Should retry API call
- Click "Continue to Login" → Should go to login screen
- Click "Exit" → Should close app

---

### Scenario 3: Network Error (No Internet)
**Expected**: App should show network error dialog

**Steps**:
1. Turn off WiFi/Mobile data
2. Launch the app
3. Wait for splash screen
4. Should see "Connection Error" dialog with message about network

**Dialog Options**:
- **Retry**: Try API call again
- **Continue Anyway**: Skip maintenance check
- **Exit**: Close the app

**Test Recovery**:
1. Turn on internet
2. Click "Retry"
3. Should work now

**Expected Result**: ✅ Network error detected and retry works

---

### Scenario 4: HTML Error Response
**Expected**: App should detect HTML and show server error

**Steps**:
1. Launch the app (server returns HTML error page)
2. Wait for splash screen
3. Should detect HTML response
4. Should show "Server Error" dialog

**Expected Result**: ✅ HTML detection works

---

### Scenario 5: Maintenance Mode Active
**Expected**: App should show maintenance dialog

**Steps**:
1. Set maintenance_mode = "1" in backend
2. Launch the app
3. Should see "Maintenance Mode" dialog
4. Click "OK" → Should exit app

**Expected Result**: ✅ Maintenance mode works

---

## 📱 How to Test

### Method 1: Using Android Studio
```bash
1. Open project in Android Studio
2. Connect device or start emulator
3. Click Run (Shift + F10)
4. Observe splash screen behavior
5. Check Logcat for detailed logs
```

### Method 2: Using Command Line
```bash
# Build the APK
./gradlew assembleDebug

# Install on device
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Launch the app
adb shell am start -n com.qdocs.ssre241123/.SplashActivity

# View logs
adb logcat | grep SplashActivity
```

---

## 🔍 What to Look For

### In Logcat
Look for these log messages:

**Success**:
```
D/SplashActivity: Maintenance Mode API URL: https://school.cyberdetox.in/api/webservice/getMaintenanceModeStatus
D/SplashActivity: API Response: {"maintenance_mode":"0"}
D/SplashActivity: Maintenance mode: 0
```

**Server Error**:
```
E/SplashActivity: Server returned HTML error page instead of JSON
E/SplashActivity: HTTP Status Code: 500
E/SplashActivity: Error Response Body: <html>...
```

**Network Error**:
```
E/SplashActivity: Volley Error: com.android.volley.NoConnectionError
E/SplashActivity: Network error - no response from server
```

---

## 🐛 Common Issues & Solutions

### Issue 1: App Still Shows Generic Error
**Cause**: Old APK installed
**Solution**: Uninstall and reinstall
```bash
adb uninstall com.qdocs.ssre241123
./gradlew assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Issue 2: No Logs Visible
**Cause**: Logcat filter too strict
**Solution**: Use broader filter
```bash
adb logcat | grep -i "splash\|error\|volley"
```

### Issue 3: Dialog Not Showing
**Cause**: Activity finishing too quickly
**Solution**: Check if ProgressDialog is dismissed properly

### Issue 4: Retry Not Working
**Cause**: API URL not set correctly
**Solution**: Check Constants.domain value

---

## 📊 Expected Behavior Summary

| Scenario | Expected Dialog | Options Available |
|----------|----------------|-------------------|
| Server Error (500) | "Server Error" | Retry, Continue to Login, Exit |
| Network Error | "Connection Error" | Retry, Continue Anyway, Exit |
| HTML Response | "Server Error" | Retry, Continue to Login, Exit |
| Maintenance Mode | "Maintenance Mode" | OK (exits app) |
| Success | None | Navigate to next screen |

---

## 🎯 Success Criteria

### ✅ All Tests Pass If:
1. Server error shows informative dialog
2. All dialog options work correctly
3. Retry mechanism works
4. Continue option bypasses maintenance check
5. Exit option closes app gracefully
6. Logs show detailed error information
7. App doesn't crash on any error
8. Users can proceed to login despite errors

---

## 📝 Test Report Template

```
Test Date: ___________
Tester: ___________
Device: ___________
Android Version: ___________

Scenario 1 (Normal): ☐ Pass ☐ Fail
Scenario 2 (Server Error): ☐ Pass ☐ Fail
Scenario 3 (Network Error): ☐ Pass ☐ Fail
Scenario 4 (HTML Response): ☐ Pass ☐ Fail
Scenario 5 (Maintenance Mode): ☐ Pass ☐ Fail

Notes:
_________________________________
_________________________________
_________________________________

Overall Result: ☐ Pass ☐ Fail
```

---

## 🔧 Backend Testing

### Test Backend API Directly
```bash
# Test maintenance mode API
curl -X POST "https://school.cyberdetox.in/api/webservice/getMaintenanceModeStatus" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -H "Content-Type: application/json" \
  -v

# Expected (when working):
# {"maintenance_mode":"0"}

# Current (with error):
# HTTP 500 with HTML error page
```

---

## 📱 Device Testing Matrix

Test on multiple devices/Android versions:

| Device | Android Version | Test Result | Notes |
|--------|----------------|-------------|-------|
| Emulator | 13 | ☐ | |
| Emulator | 11 | ☐ | |
| Physical Device 1 | ___ | ☐ | |
| Physical Device 2 | ___ | ☐ | |

---

## 🎊 Final Checklist

Before marking as complete:

- ☐ All 5 test scenarios pass
- ☐ Logs show detailed error information
- ☐ Dialogs display correctly
- ☐ All dialog options work
- ☐ Retry mechanism works
- ☐ Continue option works
- ☐ Exit option works
- ☐ App doesn't crash
- ☐ Build successful
- ☐ Tested on multiple devices

---

## 📞 Support

If you encounter issues during testing:

1. Check Logcat for detailed error messages
2. Verify API URL in Constants.java
3. Test API endpoint directly with curl
4. Check network permissions in AndroidManifest.xml
5. Verify device has internet connection

---

**Happy Testing!** 🚀

