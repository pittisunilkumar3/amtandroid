# 🚀 Quick Debug Reference - Student Report

## ⚡ Quick Commands

### Watch Logs:
```bash
adb logcat -s StudentReportActivity:D
```

### Test API:
```bash
curl -X POST "http://localhost/amt/api/student-report/filter" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d '{"session_id":21,"class_id":22,"section_id":14}'
```

### Reinstall App:
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

## 🔍 What to Look For in Logs

### ✅ Success Pattern:
```
D/StudentReportActivity: loadReportData called
D/StudentReportActivity: Session ID: 21
D/StudentReportActivity: Class ID: 22
D/StudentReportActivity: Section ID: 14
D/StudentReportActivity: Full API URL: http://localhost/amt/api/student-report/filter
D/StudentReportActivity: Request Body: {"session_id":21,"class_id":22,"section_id":14}
D/StudentReportActivity: Response: {"status":1,...}
D/StudentReportActivity: Total Records: 25
D/StudentReportActivity: Student list size: 25
D/StudentReportActivity: Success message: Found 25 student(s)
```

### ❌ Common Error Patterns:

**1. Null Filters:**
```
E/StudentReportActivity: One or more filters are null
```
→ **Fix:** Select all dropdowns (Session, Class, Section)

**2. Wrong URL:**
```
D/StudentReportActivity: Base URL: null
```
→ **Fix:** Check API URL in SharedPreferences

**3. Network Error:**
```
E/StudentReportActivity: Network error - no response from server
```
→ **Fix:** Check internet connection and server status

**4. Server Error:**
```
E/StudentReportActivity: Status Code: 500
```
→ **Fix:** Check backend server logs

**5. Empty Data:**
```
W/StudentReportActivity: Data array is null or empty
```
→ **Fix:** Check if students exist for selected filters

**6. JSON Error:**
```
E/StudentReportActivity: JSON Parsing Error
```
→ **Fix:** Verify API returns valid JSON

---

## 📋 Quick Checklist

When debugging, check these in order:

1. [ ] Are all three dropdowns selected?
2. [ ] Is the API URL correct in logs?
3. [ ] Is the request body correct?
4. [ ] Is the response received?
5. [ ] Is the response status 1?
6. [ ] Is the data array populated?
7. [ ] Is the student list size > 0?
8. [ ] Is showContent() called?

---

## 🎯 Most Common Issues

### Issue #1: "No students showing"
**Check:** Log shows "Student list size: 0"  
**Cause:** No students in selected class/section  
**Fix:** Try different filters or add students to database

### Issue #2: "Generate Report does nothing"
**Check:** Log shows "One or more filters are null"  
**Cause:** Dropdowns not selected  
**Fix:** Select all three dropdowns before clicking Generate Report

### Issue #3: "Network error"
**Check:** Log shows "Network error - no response"  
**Cause:** Server not accessible  
**Fix:** Check server is running and URL is correct

---

## 📞 Report Format

If issue persists, provide:

1. **What you did:** [Steps to reproduce]
2. **What happened:** [Actual behavior]
3. **Log output:** [Paste logs from `adb logcat -s StudentReportActivity:D`]
4. **API test result:** [Result from curl command]

---

## 🔧 Emergency Fixes

### Clear App Data:
```bash
adb shell pm clear com.qdocs.ssre241123
```

### Force Stop App:
```bash
adb shell am force-stop com.qdocs.ssre241123
```

### Restart ADB:
```bash
adb kill-server
adb start-server
```

---

**Quick Tip:** Always check logs first! They tell you exactly what's happening.

