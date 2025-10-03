# Quick Testing Guide - Teacher Dashboard API

## 🎯 Quick Test Steps

### 1. Verify API Endpoint
```bash
# Test your API directly
curl -X POST http://localhost/amt/api/teacher/menu \
  -H "Content-Type: application/json" \
  -d '{"staff_id": 1}'
```

**Expected Response:**
```json
{
  "status": 1,
  "message": "Menu items retrieved successfully.",
  "data": {
    "staff_id": 1,
    "menus": [...]
  }
}
```

### 2. Check Android Logs
Open Android Studio → Logcat → Filter by "TeacherMenuAPI"

**Look for these logs:**
```
TeacherMenuAPI: URL: http://localhost/amt/api/teacher/menu
TeacherMenuAPI: Request: {"staff_id":"1"}
TeacherMenuAPI: Staff ID: 1
TeacherMenuAPI: Response: {full JSON}
TeacherMenuAPI: Received 38 menu items
```

### 3. Visual Verification
Open Teacher Dashboard and verify:
- [ ] 4 sections are visible (Management, Academic, Communication, Tools)
- [ ] Each section has a header
- [ ] Modules display in 4-column grid
- [ ] Icons appear for each module
- [ ] Module names match your API response

### 4. Test Scenarios

#### ✅ Success Scenario
1. Login as teacher (staff_id: 1)
2. Dashboard should load 38 menu items
3. Icons should be properly displayed
4. Clicking modules should navigate (if implemented)

#### ❌ Failure Scenario (API Down)
1. Disconnect from network
2. Dashboard should show default modules
3. No crash or blank screen

#### 🔄 Re-login Test
1. Logout and login again
2. Dashboard should refresh with latest menu items

---

## 🐛 Common Issues & Fixes

### Issue: "No menu items displayed"
**Check:**
1. LogCat for API errors
2. SharedPreferences has `teacherStaffId` or `userId`
3. API endpoint is correct in SharedPreferences `apiUrl`
4. Network connectivity

**Fix:**
```java
// Verify in app/java/utils/Constants.java
public static final String teacherMenuUrl = "teacher/menu"; // ✅ Correct
```

### Issue: "Wrong icons displayed"
**Check:**
1. FontAwesomeIconMapper has mapping for your icon class
2. Drawable resources exist in res/drawable/

**Fix:**
Add mapping to `FontAwesomeIconMapper.java`:
```java
iconMap.put("fa-your-icon", R.drawable.ic_your_icon);
```

### Issue: "Modules in wrong sections"
**Fix:**
Update categorization in `TeacherDashboard.java`:
```java
private boolean isManagementModule(String activateMenu) {
    return activateMenu != null && (
        activateMenu.contains("your_menu_name") // Add here
    );
}
```

---

## 📱 Testing Checklist

### Pre-Test Setup
- [ ] API is running at `http://localhost/amt/api/teacher/menu`
- [ ] Database has test teacher with staff_id: 1
- [ ] Android device/emulator can reach localhost
- [ ] App is built successfully (`.\gradlew assembleDebug`)

### Functional Testing
- [ ] Login with teacher credentials
- [ ] Dashboard loads within 3 seconds
- [ ] All 38 menu items display correctly
- [ ] Icons match menu types
- [ ] Module names are readable
- [ ] 4-column grid layout is responsive

### Error Handling
- [ ] API timeout shows default modules
- [ ] Invalid staff_id shows default modules
- [ ] Network error shows default modules
- [ ] Empty response shows default modules

### Performance
- [ ] Dashboard loads smoothly
- [ ] No lag when scrolling
- [ ] Icons load instantly
- [ ] No memory leaks

---

## 🔍 Debug Commands

### Check SharedPreferences
```bash
# Via adb shell
adb shell
run-as com.qdocs.ssre241123
cat shared_prefs/MyPrefs.xml | grep staff
```

### Monitor Network Traffic
```bash
# Via adb
adb logcat | grep -E "TeacherMenuAPI|Volley"
```

### Test API from Android
```kotlin
// In Android Studio Terminal
adb shell am start -a android.intent.action.VIEW -d "http://localhost/amt/api/teacher/menu"
```

---

## ✅ Success Criteria

### Must Have
✅ All 38 menu items from API displayed
✅ Proper icons for each module
✅ 4-column grid layout
✅ Categorization into 4 sections
✅ Error handling with fallback

### Nice to Have
⭐ Loading indicator during API call
⭐ Pull-to-refresh functionality
⭐ Empty state message
⭐ Module click analytics

---

## 📊 Expected Results

### Menu Distribution
- **Management Section**: ~7-8 modules
  (Student Info, Fees, Income, Expense, etc.)
  
- **Academic Section**: ~10-12 modules
  (Attendance, Exams, Library, Results, etc.)
  
- **Communication Section**: ~8-10 modules
  (Communicate, Transport, Hostel, etc.)
  
- **Tools Section**: ~6-8 modules
  (Certificate, Settings, Reports, etc.)

### Total: ~38 modules (based on your API response)

---

## 🚀 Deploy & Test

### Build & Install
```bash
# Build debug APK
.\gradlew assembleDebug

# Install on device
adb install app/build/outputs/apk/debug/app-debug.apk

# Launch app
adb shell am start -n com.qdocs.ssre241123/.Login
```

### Monitor in Real-Time
```bash
# Clear logs and monitor
adb logcat -c
adb logcat | grep -E "TeacherMenuAPI|TeacherDashboard"
```

---

## 📞 Support

### Log Files to Share
When reporting issues, include:
1. Full LogCat output (filtered by TeacherMenuAPI)
2. API response JSON
3. SharedPreferences values (staff_id, apiUrl)
4. Screenshots of dashboard

### Key Information
- **API Endpoint**: `teacher/menu`
- **Request Format**: `{"staff_id": 1}`
- **Response Expected**: JSON with status, data.menus array
- **Staff ID Source**: SharedPreferences → `teacherStaffId` or `userId`

---

**Good luck with testing! 🎉**
The implementation is complete and ready for your end-to-end testing with staff_id: 1.
