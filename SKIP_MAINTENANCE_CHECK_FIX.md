# ⚡ Skip Maintenance Check - Quick Android Fix

## Purpose
Bypass the maintenance mode check to avoid the server error 500 issue.

---

## 🎯 Quick Fix (2 Minutes)

If you want to completely skip the maintenance check and go straight to login, here's the fix:

---

## Option A: Skip Maintenance Check Entirely

### File to Edit:
`app/src/main/java/com/qdocs/ssre241123/SplashActivity.java`

### Find this code (around line 68-110):
```java
private void splash() {
    new Handler().postDelayed(new Runnable() {
        public void run() {
            try {
                isLoggegIn = Utility.getSharedPreferencesBoolean(getApplicationContext(), Constants.isLoggegIn);
                isLock = Utility.getSharedPreferencesBoolean(getApplicationContext(), Constants.isLock);
                isUrlTaken = Utility.getSharedPreferencesBoolean(getApplicationContext(), "isUrlTaken");
            } catch (NullPointerException NPE) {
                isLoggegIn = false;
                isUrlTaken = false;
                isLock = false;
            }

            if(Constants.askUrlFromUser) {
                // ... code for URL input
            } else {
                // Use getApiUrl() to ensure we always use the configured domain
                String apiUrl = Utility.getApiUrl(getApplicationContext());
                Utility.setSharedPreferenceBoolean(getApplicationContext(), "isUrlTaken", false);
                
                if(Utility.isConnectingToInternet(SplashActivity.this)){
                    ismaintenancemode(apiUrl);  // ← This calls the API
                }else{
                    makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }, SPLASH_TIME_OUT);
}
```

### Replace with this:
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

            // SKIP MAINTENANCE CHECK - Set API URL and go directly to next screen
            String apiUrl = Utility.getApiUrl(getApplicationContext());
            Utility.setSharedPreferenceBoolean(getApplicationContext(), "maintenance_mode", false);
            Utility.setSharedPreferenceBoolean(getApplicationContext(), "isUrlTaken", false);
            
            Log.e("SplashActivity", "Skipping maintenance check - going directly to next screen");
            
            // Navigate directly to next screen
            navigateToNextScreen();
        }
    }, SPLASH_TIME_OUT);
}
```

### What This Does:
- ✅ Skips the maintenance mode API call
- ✅ Sets maintenance_mode to false
- ✅ Goes directly to login or dashboard
- ✅ No server error dialog

---

## Option B: Add a Flag to Enable/Disable Maintenance Check

### Step 1: Add constant to Constants.java

**File**: `app/src/main/java/com/qdocs/ssre241123/utils/Constants.java`

**Add this line** (around line 5):
```java
public static final Boolean checkMaintenanceMode = false; // Set to false to skip check
```

### Step 2: Update SplashActivity.java

**File**: `app/src/main/java/com/qdocs/ssre241123/SplashActivity.java`

**Find the splash() method and update it**:
```java
private void splash() {
    new Handler().postDelayed(new Runnable() {
        public void run() {
            try {
                isLoggegIn = Utility.getSharedPreferencesBoolean(getApplicationContext(), Constants.isLoggegIn);
                isLock = Utility.getSharedPreferencesBoolean(getApplicationContext(), Constants.isLock);
                isUrlTaken = Utility.getSharedPreferencesBoolean(getApplicationContext(), "isUrlTaken");
            } catch (NullPointerException NPE) {
                isLoggegIn = false;
                isUrlTaken = false;
                isLock = false;
            }

            // Set API URL
            String apiUrl = Utility.getApiUrl(getApplicationContext());
            Utility.setSharedPreferenceBoolean(getApplicationContext(), "isUrlTaken", false);
            
            // Check if maintenance check is enabled
            if(Constants.checkMaintenanceMode && Utility.isConnectingToInternet(SplashActivity.this)){
                // Call maintenance mode API
                ismaintenancemode(apiUrl);
            } else {
                // Skip maintenance check and go directly to next screen
                Utility.setSharedPreferenceBoolean(getApplicationContext(), "maintenance_mode", false);
                Log.e("SplashActivity", "Maintenance check disabled - going directly to next screen");
                navigateToNextScreen();
            }
        }
    }, SPLASH_TIME_OUT);
}
```

### What This Does:
- ✅ Adds a flag to enable/disable maintenance check
- ✅ Easy to toggle: just change `checkMaintenanceMode` to true/false
- ✅ When false: skips API call and goes to login
- ✅ When true: calls API normally

---

## 🔧 Implementation Steps

### For Option A (Complete Skip):

1. **Open the file**:
   ```
   app/src/main/java/com/qdocs/ssre241123/SplashActivity.java
   ```

2. **Find the `splash()` method** (around line 68)

3. **Replace the entire method** with the code from Option A above

4. **Build and test**:
   ```bash
   ./gradlew assembleDebug
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```

5. **Launch the app** - it should go directly to login!

---

### For Option B (Toggle Flag):

1. **Edit Constants.java**:
   - Add: `public static final Boolean checkMaintenanceMode = false;`

2. **Edit SplashActivity.java**:
   - Update the `splash()` method with the code from Option B above

3. **Build and test**:
   ```bash
   ./gradlew assembleDebug
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```

4. **To enable maintenance check later**:
   - Just change `checkMaintenanceMode = true` in Constants.java

---

## 📊 Comparison

| Approach | Pros | Cons | Best For |
|----------|------|------|----------|
| **Click "Continue to Login"** | No code changes | Must click every time | Immediate testing |
| **Option A: Complete Skip** | Simple, permanent | No maintenance check | Development/Testing |
| **Option B: Toggle Flag** | Easy to enable/disable | Slightly more code | Flexible deployment |

---

## 🎯 Recommendation

### For Right Now:
**Just click "Continue to Login"** when you see the error dialog. No code changes needed!

### For Development:
Use **Option B (Toggle Flag)** so you can easily enable/disable the maintenance check.

### For Production:
**Fix the backend** by adding `language_model` to the Webservice controller.

---

## 🧪 Testing

After implementing the fix:

1. **Launch the app**
2. **Should see**: Splash screen → Login screen (no error dialog)
3. **Login** and use the app normally

### Expected Logcat:
```
E/SplashActivity: Skipping maintenance check - going directly to next screen
D/Utility: API URL enforced: https://school.cyberdetox.in/api/
```

---

## 🔄 Reverting the Change

If you want to re-enable the maintenance check later:

### For Option A:
Restore the original `splash()` method from git history

### For Option B:
Change `checkMaintenanceMode = true` in Constants.java

---

## 📝 Summary

**Easiest Solution**: Click "Continue to Login" button (no code changes)

**Quick Fix**: Use Option A to skip maintenance check entirely

**Flexible Fix**: Use Option B to add a toggle flag

**Production Fix**: Fix the backend PHP error

---

**Choose the option that works best for your situation!** ✅

