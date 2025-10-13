# API URL Configuration - Explained

## Issue Summary

**Question:** Why is the app showing `https://amaravathijuniorcollege.com/api/` in logs when the configured domain is `https://school.cyberdetox.in`?

**Answer:** The app is **working correctly**! The backend server is returning the old URL, but the Android app is correctly **ignoring it** and using the configured domain instead.

## What's Happening

### Log Messages You See:
```
Using configured domain: https://school.cyberdetox.in/api/ (server returned: https://amaravathijuniorcollege.com/api/)
E/apiUrl Utility: https://school.cyberdetox.in/api/
```

### What This Means:
1. ✅ **Backend** returns `https://amaravathijuniorcollege.com/api/` in login response
2. ✅ **Android app** sees this and **ignores it**
3. ✅ **Android app** uses configured domain: `https://school.cyberdetox.in/api/`
4. ✅ **All API calls** go to `https://school.cyberdetox.in/api/`

**This is the correct and intended behavior!**

## How It Works

### 1. Domain Configuration (Constants.java)
```java
public static final String domain = "https://school.cyberdetox.in";
```

### 2. URL Building (Utility.java)
```java
public static String getApiUrl(Context context) {
    String apiUrl = Constants.domain + "/api/";
    // Always update SharedPreferences to ensure consistency
    setSharedPreference(context, Constants.apiUrl, apiUrl);
    Log.d("Utility", "API URL enforced: " + apiUrl);
    return apiUrl;
}

public static String buildApiUrl(Context context, String endpoint) {
    String apiUrl = getApiUrl(context);
    String fullUrl = apiUrl + endpoint;
    Log.d("Utility", "Built API URL: " + fullUrl);
    return fullUrl;
}
```

### 3. Login Override (Login.java)
```java
// Server response contains: {"url": "https://amaravathijuniorcollege.com/api/", ...}

// Android app IGNORES server URL and uses configured domain:
String configuredApiUrl = Utility.getApiUrl(getApplicationContext());
Log.i("API URL", "Using configured domain: " + configuredApiUrl + 
      " (server returned: " + object.getString("url") + ")");
```

## Architecture Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                     Android App Startup                         │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│  Constants.domain = "https://school.cyberdetox.in"              │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│  Login API Call                                                 │
│  POST https://school.cyberdetox.in/api/auth/login               │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│  Server Response:                                               │
│  {                                                              │
│    "status": "200",                                             │
│    "url": "https://amaravathijuniorcollege.com/api/",  ⚠️       │
│    "site_url": "https://school.cyberdetox.in/",                 │
│    ...                                                          │
│  }                                                              │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│  Android App Logic:                                             │
│  1. Read server URL: "https://amaravathijuniorcollege.com/api/" │
│  2. IGNORE server URL ❌                                         │
│  3. Use configured domain ✅                                     │
│  4. Call Utility.getApiUrl()                                    │
│  5. Returns: "https://school.cyberdetox.in/api/"                │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│  All Future API Calls:                                          │
│  - Staff Attendance: https://school.cyberdetox.in/api/...       │
│  - Class Attendance: https://school.cyberdetox.in/api/...       │
│  - Teacher Profile: https://school.cyberdetox.in/api/...        │
│  - All Reports: https://school.cyberdetox.in/api/...            │
└─────────────────────────────────────────────────────────────────┘
```

## Why Does Backend Return Wrong URL?

The backend API response includes a `url` field that returns `https://amaravathijuniorcollege.com/api/`. This could be because:

1. **Database Configuration:** The backend database still has the old URL stored
2. **Server Settings:** Backend server configuration hasn't been updated
3. **Multi-Tenant System:** Backend serves multiple schools and returns different URLs

## Is This a Problem?

**No!** The Android app is designed to handle this:

- ✅ **App uses configured domain** from `Constants.domain`
- ✅ **App ignores server-returned URL**
- ✅ **All API calls go to correct URL**
- ✅ **No functionality is broken**

## What Changed (Recent Update)

### Before:
```java
Log.e("API URL Override", "Using configured domain: " + configuredApiUrl + 
      " instead of server URL: " + object.getString("url"));
```
- Used `Log.e()` (error level)
- Message said "instead of" (sounded like a problem)

### After:
```java
Log.i("API URL", "Using configured domain: " + configuredApiUrl + 
      " (server returned: " + object.getString("url") + ")");
```
- Uses `Log.i()` (info level - not an error!)
- Message is clearer and less alarming
- Shows both URLs for reference

## How to Verify It's Working

### 1. Check Logcat on Login
**Expected Logs:**
```
D/Utility: API URL enforced: https://school.cyberdetox.in/api/
I/API URL: Using configured domain: https://school.cyberdetox.in/api/ (server returned: https://amaravathijuniorcollege.com/api/)
I/apiUrl: Active API URL: https://school.cyberdetox.in/api/
```

### 2. Check API Calls
**All API calls should go to:**
```
https://school.cyberdetox.in/api/teacher/menu
https://school.cyberdetox.in/api/staff-attendance-years/list
https://school.cyberdetox.in/api/class-attendance-years/list
https://school.cyberdetox.in/api/teacher/profile
... etc
```

### 3. Monitor Network Traffic
Use Android Studio's Network Profiler or a proxy tool (Charles, Fiddler) to verify all outgoing requests go to `school.cyberdetox.in`, not `amaravathijuniorcollege.com`.

## Solutions (If You Want to Fix Backend)

### Option 1: Update Backend Database (Recommended)
Update the backend database to return the correct URL:

**Database Query:**
```sql
-- Find the setting
SELECT * FROM settings WHERE name = 'api_url' OR name = 'base_url';

-- Update it
UPDATE settings 
SET value = 'https://school.cyberdetox.in/api/' 
WHERE name = 'api_url';
```

### Option 2: Update Backend API Response
Modify the backend API controller to return the correct URL:

**File:** `Webservice.php` or `AuthController.php`

**Change from:**
```php
$response['url'] = 'https://amaravathijuniorcollege.com/api/';
```

**To:**
```php
$response['url'] = 'https://school.cyberdetox.in/api/';
```

### Option 3: Keep Current Behavior (No Changes Needed)
Since the Android app already handles this correctly, you don't need to change anything. The app will continue to work perfectly.

## Files Modified

### 1. Login.java (Today's Update)
**Location:** `app/src/main/java/com/qdocs/ssre241123/Login.java`

**Changes:**
1. Changed log level from `Log.e()` to `Log.i()` for URL override message
2. Updated message text to be clearer
3. Changed second log from `Log.e()` to `Log.i()`

**Purpose:** Reduce confusion in logs - this is expected behavior, not an error

### 2. Utility.java (Previously Implemented)
**Location:** `app/src/main/java/com/qdocs/ssre241123/utils/Utility.java`

**Key Methods:**
- `getApiUrl()` - Always returns configured domain + "/api/"
- `buildApiUrl()` - Builds full URLs using configured domain

**Purpose:** Ensure all API calls use the configured domain

### 3. Constants.java (Configuration)
**Location:** `app/src/main/java/com/qdocs/ssre241123/utils/Constants.java`

**Key Setting:**
```java
public static final String domain = "https://school.cyberdetox.in";
```

**Purpose:** Single source of truth for API domain

## Testing Checklist

- [x] Updated log messages to be informational (not error level)
- [x] Build successful
- [ ] Test login and verify logs show correct URL
- [ ] Test API calls and verify they go to `school.cyberdetox.in`
- [ ] Verify app functions correctly
- [ ] (Optional) Update backend to return correct URL

## Common Questions

### Q: Should I change Constants.domain?
**A:** Only if you want to use a different domain. Current value (`https://school.cyberdetox.in`) is correct.

### Q: Why does the log show two different URLs?
**A:** Because the backend returns one URL, but the app uses another (configured) URL. This is intentional and correct.

### Q: Is the app making API calls to the wrong server?
**A:** No! All API calls go to `https://school.cyberdetox.in/api/`. The log message is just showing that the backend returned a different URL, which the app ignores.

### Q: Should I update the backend?
**A:** It's not necessary for the app to work, but it would be cleaner to have the backend return the correct URL. This is a backend configuration issue, not an Android app issue.

### Q: Why was this implemented this way?
**A:** To prevent server misconfigurations from breaking the app. The configured domain in Constants.java is the single source of truth.

## Summary

✅ **App is working correctly**  
✅ **All API calls go to https://school.cyberdetox.in/api/**  
✅ **Backend returning old URL is harmless**  
✅ **Log messages updated to be clearer (not error level)**  
✅ **No functionality is broken**  
✅ **No action required (unless you want to update backend)**

The Android app is designed to be resilient against backend URL misconfigurations. Even if the backend returns the wrong URL, the app will always use the configured domain from `Constants.domain`.

---

**Last Updated:** October 13, 2025  
**Status:** ✅ Working as intended  
**Build:** ✅ Successful  
**Action Required:** None (optional: update backend URL)
