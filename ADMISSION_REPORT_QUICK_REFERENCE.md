# Admission Report - Quick Reference Card

## 🚀 Quick Start

### Navigation
```
Teacher Dashboard → Reports → Student Information → Admission Report
```

### Files Location
```
Activity:  app/src/main/java/com/qdocs/ssre241123/teachers/AdmissionReportActivity.java
Model:     app/src/main/java/com/qdocs/ssre241123/model/AdmissionReportModel.java
Adapter:   app/src/main/java/com/qdocs/ssre241123/adapters/AdmissionReportAdapter.java
Layout:    app/src/main/res/layout/item_admission_report.xml
```

---

## 📡 API Reference

### Endpoint
```
POST /admission-report/filter
```

### Headers
```
Client-Service: smartschool
Auth-Key: schoolAdmin@
Content-Type: application/json
```

### Request Body
```json
{
  "class_id": 1,
  "session_id": 18
}
```

### Response
```json
{
  "status": 1,
  "message": "Admission report retrieved successfully",
  "total_records": 25,
  "data": [
    {
      "id": "123",
      "admission_no": "2024001",
      "admission_date": "2024-04-15",
      "firstname": "John",
      "middlename": "Michael",
      "lastname": "Doe",
      "class_id": "1",
      "class": "Class 10",
      "section_id": "2",
      "section": "A",
      "session_id": "18",
      "session": "2024-2025",
      "mobileno": "9876543210",
      "guardian_name": "Robert Doe",
      "guardian_relation": "Father",
      "guardian_phone": "9876543211",
      "is_active": "yes"
    }
  ]
}
```

---

## 🔧 Key Code Snippets

### Activity - Load Report Data
```java
@Override
protected void loadReportData() {
    String sessionId = getSelectedSessionId();
    String classId = getSelectedClassId();
    String sectionId = getSelectedSectionId();
    
    if (sessionId == null || classId == null || sectionId == null) {
        Toast.makeText(this, "Please select all filters", Toast.LENGTH_SHORT).show();
        hideLoading();
        return;
    }
    
    showLoading();
    fetchAdmissionReport(sessionId, classId, sectionId);
}
```

### API Call
```java
StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
    response -> {
        hideLoading();
        parseAdmissionReportResponse(response);
    },
    error -> {
        hideLoading();
        showNoData();
        Toast.makeText(this, "Error loading report", Toast.LENGTH_LONG).show();
    }) {
    
    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Client-Service", Constants.clientService);
        headers.put("Auth-Key", Constants.authKey);
        headers.put("Content-Type", "application/json");
        return headers;
    }
    
    @Override
    public byte[] getBody() {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("class_id", Integer.parseInt(classId));
        jsonBody.put("session_id", Integer.parseInt(sessionId));
        return jsonBody.toString().getBytes("UTF-8");
    }
};
```

### Parse Response
```java
JSONObject jsonObject = new JSONObject(response);
int status = jsonObject.optInt("status", 0);

if (status == 1) {
    JSONArray dataArray = jsonObject.optJSONArray("data");
    admissionList.clear();
    
    for (int i = 0; i < dataArray.length(); i++) {
        JSONObject obj = dataArray.getJSONObject(i);
        AdmissionReportModel admission = new AdmissionReportModel();
        admission.setId(obj.optString("id", ""));
        admission.setAdmissionNo(obj.optString("admission_no", ""));
        // ... set other fields
        admissionList.add(admission);
    }
    
    adapter.notifyDataSetChanged();
    showContent();
}
```

---

## 🎨 UI Components

### State Management
```java
showLoading();    // Show loading indicator
showContent();    // Show RecyclerView with data
showNoData();     // Show empty state
hideLoading();    // Hide loading indicator
```

### Theme Colors
```java
// Apply theme color to header
String primaryColor = Utility.getSharedPreferences(context, Constants.primaryColour);
holder.headerLayout.setBackgroundColor(Color.parseColor(primaryColor));
```

### Status Badge
```java
if (admission.isActiveStudent()) {
    holder.statusTv.setText("✓ Active");
    holder.statusTv.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
    holder.statusTv.setBackgroundResource(R.drawable.bg_status_active);
} else {
    holder.statusTv.setText("✗ Inactive");
    holder.statusTv.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
    holder.statusTv.setBackgroundResource(R.drawable.bg_status_inactive);
}
```

---

## 🐛 Common Issues & Solutions

### Issue: "Please select all filters"
**Solution**: Ensure Session, Class, and Section are all selected before clicking Generate Report

### Issue: Empty state shown but data exists
**Solution**: Check Logcat for JSON parsing errors. Verify API response format.

### Issue: Network error
**Solution**: 
1. Check internet connection
2. Verify API URL in Constants.java
3. Check authentication headers

### Issue: Cards not displaying
**Solution**: 
1. Verify `item_admission_report.xml` exists
2. Check adapter is set to RecyclerView
3. Verify data list is not empty

### Issue: Theme colors not applied
**Solution**: 
1. Check primary color is set in SharedPreferences
2. Verify color parsing doesn't throw exception
3. Check drawable resources exist

---

## 📊 Logging

### Enable Debug Logs
```java
private static final String TAG = "AdmissionReportActivity";
Log.d(TAG, "Message");
```

### Key Log Points
```
- loadReportData() called
- API URL construction
- Request headers
- Request body
- Response received
- Response parsing
- Data list size
- Adapter notification
- State changes
```

### View Logs
```bash
adb logcat | grep AdmissionReportActivity
```

---

## ✅ Testing Checklist

- [ ] Navigation works
- [ ] Filters populate correctly
- [ ] API call succeeds
- [ ] Data displays in cards
- [ ] Theme colors applied
- [ ] Status badges show correctly
- [ ] Empty state works
- [ ] Error handling works
- [ ] Loading indicator works
- [ ] Back navigation works

---

## 🔗 Related Reports

### Student History
- **ID**: `student_history`
- **Activity**: `StudentHistoryActivity`
- **Purpose**: Simple historical view
- **Same API**: Yes

### Online Admission Report
- **ID**: `online_admission_report`
- **Activity**: `OnlineAdmissionReportActivity`
- **Purpose**: Online admission tracking
- **Different API**: Yes

---

## 📞 Constants Reference

```java
// API Endpoints
Constants.admissionReportFilterUrl = "admission-report/filter"
Constants.admissionReportListUrl = "admission-report/list"

// Authentication
Constants.clientService = "smartschool"
Constants.authKey = "schoolAdmin@"

// Theme Colors
Constants.primaryColour = "primaryColour"
Constants.secondaryColour = "secondaryColour"
```

---

## 🎯 Model Helper Methods

```java
// Get full name
String fullName = admission.getFullName();

// Get class and section
String classSection = admission.getClassSection();

// Get guardian info
String guardianInfo = admission.getGuardianInfo();

// Get admission year
String year = admission.getAdmissionYear();

// Check if active
boolean isActive = admission.isActiveStudent();
```

---

## 📱 Layout IDs

```xml
<!-- RecyclerView -->
R.id.report_content_recyclerView

<!-- Card Components -->
R.id.card_view
R.id.header_layout
R.id.student_name_tv
R.id.admission_no_tv
R.id.admission_date_tv
R.id.class_section_tv
R.id.session_tv
R.id.guardian_info_tv
R.id.mobile_no_tv
R.id.guardian_phone_tv
R.id.status_tv
```

---

## 🚀 Build & Run

### Build Command
```bash
./gradlew assembleDebug
```

### Install APK
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### Run on Device
```bash
adb shell am start -n com.qdocs.ssre241123/.teachers.AdmissionReportActivity
```

---

## 📚 Documentation Links

- **Implementation Summary**: `ADMISSION_REPORT_IMPLEMENTATION_SUMMARY.md`
- **Testing Guide**: `ADMISSION_REPORT_TESTING_GUIDE.md`
- **Architecture**: `ADMISSION_REPORT_ARCHITECTURE.md`
- **Comparison**: `ADMISSION_REPORTS_COMPARISON.md`
- **API Documentation**: `api/documentation/ADMISSION_REPORT_API_DOCUMENTATION.md`

---

## 💡 Pro Tips

1. **Always check filters**: Validate all filters before API call
2. **Use logging**: Add comprehensive logs for debugging
3. **Handle nulls**: Check for null/empty values in response
4. **Theme integration**: Always use theme colors for consistency
5. **Error messages**: Provide user-friendly error messages
6. **Loading states**: Show loading indicators during API calls
7. **Empty states**: Handle empty data gracefully
8. **Test thoroughly**: Test with various data scenarios

---

## 🎓 Quick Commands

```bash
# View logs
adb logcat | grep AdmissionReportActivity

# Clear app data
adb shell pm clear com.qdocs.ssre241123

# Take screenshot
adb shell screencap -p /sdcard/screenshot.png

# Pull screenshot
adb pull /sdcard/screenshot.png

# Check network
adb shell ping google.com
```

---

**Last Updated**: 2025-10-10  
**Version**: 1.0  
**Status**: ✅ Production Ready

