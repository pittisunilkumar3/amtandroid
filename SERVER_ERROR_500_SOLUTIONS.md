# 🔧 Server Error 500 - Solutions

## Error Details

**Error Message**: "Server error (500). Please contact administrator."
**Root Cause**: Backend PHP error - `Undefined property: Webservice::$language_model`
**API Endpoint**: `https://school.cyberdetox.in/api/webservice/getMaintenanceModeStatus`

---

## 🎯 You Have 3 Options

### **Option 1: Fix the Backend (RECOMMENDED for Production)** ✅
### **Option 2: Skip Maintenance Check (Quick Fix for Testing)** ⚡
### **Option 3: Use "Continue to Login" Button (Immediate Workaround)** 🚀

---

## Option 1: Fix the Backend ✅ RECOMMENDED

### Problem:
The backend API is missing the `language_model` in the Webservice controller.

### Backend Error:
```
Severity: Warning
Message: Undefined property: Webservice::$language_model
Filename: core/Model.php
Line Number: 74
File: /home/digita90/school.cyberdetox.in/api/application/controllers/Webservice.php
Line: 5057
```

### Solution:
Add the `language_model` to the Webservice controller constructor.

### Backend Fix (PHP):

**File**: `/home/digita90/school.cyberdetox.in/api/application/controllers/Webservice.php`

**Add this line to the constructor**:
```php
public function __construct() {
    parent::__construct();
    
    // Add this line to load the language_model
    $this->load->model('language_model');
    
    // Other models
    $this->load->model('setting_model');
    $this->load->model('student_model');
    // ... other models
}
```

### Steps:
1. SSH into your server
2. Edit the Webservice.php file
3. Add `$this->load->model('language_model');` to the constructor
4. Save the file
5. Test the API again

### Verification:
```bash
curl -X POST "https://school.cyberdetox.in/api/webservice/getMaintenanceModeStatus" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -H "Content-Type: application/json"

# Expected response:
{"maintenance_mode":"0"}
```

---

## Option 2: Skip Maintenance Check ⚡ QUICK FIX

If you want to bypass the maintenance check temporarily for testing, you can disable it in the app.

### Android App Fix:

**File**: `app/src/main/java/com/qdocs/ssre241123/SplashActivity.java`

**Change the splash() method to skip maintenance check**:

```java
private void splash() {
    new Handler().postDelayed(new Runnable() {
        public void run() {
            try {
                isLoggegIn = Utility.getSharedPreferencesBoolean(getApplicationContext(), Constants.isLoggegIn);
                isLock = Utility.getSharedPreferencesBoolean(getApplicationContext(), Constants.isLock);
            } catch (NullPointerException NPE) {
                isLoggegIn = false;
                isLock = false;
            }

            // SKIP MAINTENANCE CHECK - Go directly to next screen
            Utility.setSharedPreferenceBoolean(getApplicationContext(), "maintenance_mode", false);
            navigateToNextScreen();
        }
    }, SPLASH_TIME_OUT);
}
```

### Pros:
- ✅ Quick fix for testing
- ✅ App works immediately
- ✅ No backend changes needed

### Cons:
- ⚠️ Skips maintenance mode check
- ⚠️ Not recommended for production
- ⚠️ Should be temporary

---

## Option 3: Use "Continue to Login" Button 🚀 IMMEDIATE

**This is the EASIEST option - no code changes needed!**

### Steps:
1. Launch the app
2. When you see the error dialog: "Server error (500). Please contact administrator."
3. Click the **"Continue to Login"** button
4. The app will skip the maintenance check and take you to the login screen
5. Login normally and use the app

### Why This Works:
The error dialog already has a "Continue to Login" button that bypasses the maintenance check. This is exactly what you need!

### Dialog Options:
- **Retry**: Tries the API call again (will fail if backend not fixed)
- **Continue to Login**: ✅ **USE THIS** - Skips maintenance check and goes to login
- **Exit**: Closes the app

---

## 🎯 Recommended Approach

### For Immediate Use (Right Now):
1. **Click "Continue to Login"** when you see the error dialog
2. Login and use the app normally
3. The maintenance check is skipped

### For Long-Term Fix (Production):
1. **Fix the backend** by adding `language_model` to Webservice.php
2. This ensures the maintenance check works properly
3. All users will have a smooth experience

---

## 📱 Current App Behavior

The Android app is **working correctly**! It's detecting the server error and giving you options:

```
┌─────────────────────────────────────┐
│     Connection Error                │
├─────────────────────────────────────┤
│ Server error (500). Please contact │
│ administrator.                      │
│                                     │
│ What would you like to do?          │
├─────────────────────────────────────┤
│  [Retry]  [Continue to Login] [Exit]│
└─────────────────────────────────────┘
```

**Just click "Continue to Login"** and you're good to go! ✅

---

## 🔍 Why This Happens

### The Flow:
1. App launches → Splash screen
2. Calls maintenance mode API
3. Backend has PHP error (missing language_model)
4. Returns HTTP 500 error
5. App detects error and shows dialog
6. You can choose to continue anyway

### The Backend Issue:
```php
// In Webservice.php, line 5057
// Trying to use $this->language_model but it's not loaded
$this->language_model->get(); // ❌ Error: language_model not loaded

// Fix: Load it in constructor
$this->load->model('language_model'); // ✅ This fixes it
```

---

## 🧪 Testing After Fix

### If You Fixed the Backend:
```bash
# Test the API
curl -X POST "https://school.cyberdetox.in/api/webservice/getMaintenanceModeStatus" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -H "Content-Type: application/json"

# Expected response:
{"maintenance_mode":"0"}

# If you see this, the backend is fixed! ✅
```

### If You're Using "Continue to Login":
1. Launch app
2. Click "Continue to Login" on error dialog
3. Login normally
4. Use the app ✅

---

## 📊 Summary

| Option | Difficulty | Time | Recommended For |
|--------|-----------|------|-----------------|
| **Option 1: Fix Backend** | Medium | 5-10 min | Production use |
| **Option 2: Skip Check in App** | Easy | 2 min | Testing only |
| **Option 3: Use Button** | Very Easy | 0 min | **Immediate use** ✅ |

---

## 🎊 Immediate Solution

**RIGHT NOW, DO THIS:**

1. Launch the app
2. When you see the error dialog
3. **Click "Continue to Login"**
4. Login and use the app normally

**That's it!** The app will work perfectly. The maintenance check is optional and can be skipped.

---

## 🔧 Long-Term Solution

**For production deployment:**

1. SSH into your server
2. Edit: `/home/digita90/school.cyberdetox.in/api/application/controllers/Webservice.php`
3. Add to constructor: `$this->load->model('language_model');`
4. Save and test

This ensures all users have a smooth experience without seeing the error dialog.

---

**Status**: ✅ WORKAROUND AVAILABLE (Click "Continue to Login")
**Long-Term Fix**: ⚠️ Backend needs language_model loaded
**App Status**: ✅ Working correctly - detecting and handling errors properly

