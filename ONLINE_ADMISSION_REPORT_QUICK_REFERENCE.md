# Online Admission Report - Quick Reference Guide

## 🚀 Quick Start

### For Developers

#### Key Files
```
Model:      app/src/main/java/com/qdocs/ssre241123/model/OnlineAdmissionModel.java
Adapter:    app/src/main/java/com/qdocs/ssre241123/adapters/OnlineAdmissionAdapter.java
Activity:   app/src/main/java/com/qdocs/ssre241123/teachers/OnlineAdmissionReportActivity.java
Layout:     app/src/main/res/layout/item_online_admission.xml
```

#### Navigation Path
```
Teacher Dashboard → Reports → Student Information → Online Admission Report
```

#### Report ID
```java
"online_admission_report"
```

---

## 📡 API Quick Reference

### Endpoint
```
POST /api/online-admission/filter
```

### Headers
```
Client-Service: smartschool
Auth-Key: schoolAdmin@
Content-Type: application/json
```

### Request (All Optional)
```json
{
  "class_id": 19,
  "section_id": 47
}
```

### Response
```json
{
  "status": 1,
  "message": "Online admissions filtered successfully",
  "total_records": 15,
  "data": [
    {
      "id": 123,
      "reference_no": "REF2024001",
      "admission_no": "ADM2024001",
      "full_name": "John Doe Smith",
      "class_info": {
        "class_id": 19,
        "class_name": "Class 10",
        "section_id": 47,
        "section_name": "Section A"
      },
      "is_enroll": "1",
      "paid_status": "1",
      ...
    }
  ]
}
```

---

## 🔧 Common Tasks

### Add New Field to Display

1. **Update Model** (OnlineAdmissionModel.java)
```java
private String newField;

public String getNewField() {
    return newField;
}

public void setNewField(String newField) {
    this.newField = newField;
}
```

2. **Update Layout** (item_online_admission.xml)
```xml
<TextView
    android:id="@+id/new_field_tv"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="New Field" />
```

3. **Update Adapter** (OnlineAdmissionAdapter.java)
```java
// In ViewHolder
TextView newFieldTv;

// In constructor
newFieldTv = itemView.findViewById(R.id.new_field_tv);

// In onBindViewHolder
holder.newFieldTv.setText(admission.getNewField());
```

4. **Update Activity** (OnlineAdmissionReportActivity.java)
```java
// In parseOnlineAdmissionResponse
admission.setNewField(admissionObj.optString("new_field", ""));
```

---

### Change API Endpoint

**File:** OnlineAdmissionReportActivity.java

```java
// Line ~76
String url = baseUrl + "your-new-endpoint";
```

---

### Modify Filters

**File:** OnlineAdmissionReportActivity.java

```java
// In getBody() method, add new filter
if (newFilterId != null && !newFilterId.isEmpty()) {
    jsonBody.put("new_filter_id", Integer.parseInt(newFilterId));
}
```

---

### Change Card Design

**File:** item_online_admission.xml

Modify the CardView properties:
```xml
<androidx.cardview.widget.CardView
    app:cardCornerRadius="12dp"    <!-- Change corner radius -->
    app:cardElevation="6dp"        <!-- Change elevation -->
    android:layout_margin="12dp">  <!-- Change margin -->
```

---

### Change Status Colors

**File:** OnlineAdmissionAdapter.java

```java
// Enrollment status colors
if (admission.isEnrolled()) {
    holder.enrollmentStatusTv.setBackgroundColor(Color.parseColor("#YOUR_COLOR"));
} else {
    holder.enrollmentStatusTv.setBackgroundColor(Color.parseColor("#YOUR_COLOR"));
}

// Payment status colors
if (admission.isPaid()) {
    holder.paymentStatusTv.setTextColor(Color.parseColor("#YOUR_COLOR"));
} else {
    holder.paymentStatusTv.setTextColor(Color.parseColor("#YOUR_COLOR"));
}
```

---

## 🐛 Debugging

### Enable Detailed Logging

**File:** OnlineAdmissionReportActivity.java

```java
private static final String TAG = "OnlineAdmissionReport";
```

All logs use this TAG. Filter Logcat:
```bash
adb logcat -s OnlineAdmissionReport
```

### Common Log Points
```
D/OnlineAdmissionReport: loadReportData called
D/OnlineAdmissionReport: === Fetching Online Admissions ===
D/OnlineAdmissionReport: === API Response Received ===
D/OnlineAdmissionReport: === Parsing Response ===
E/OnlineAdmissionReport: === API Error ===
```

---

## 🔍 Troubleshooting

### Issue: No Data Displayed

**Check:**
1. API response in Logcat
2. `status` field in response (should be 1)
3. `data` array is not empty
4. Adapter is notified: `adapter.notifyDataSetChanged()`

**Fix:**
```java
// Add logging
Log.d(TAG, "Data array length: " + dataArray.length());
Log.d(TAG, "List size: " + admissionList.size());
```

---

### Issue: Network Error

**Check:**
1. Device has internet connection
2. API URL is correct
3. Server is running
4. Firewall/proxy settings

**Fix:**
```java
// Check base URL
String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
Log.d(TAG, "Base URL: " + baseUrl);
```

---

### Issue: Parsing Error

**Check:**
1. API response format matches expected
2. Field names are correct
3. Data types match

**Fix:**
```java
// Add try-catch around parsing
try {
    admission.setFullName(admissionObj.optString("full_name", ""));
} catch (Exception e) {
    Log.e(TAG, "Error parsing full name", e);
}
```

---

### Issue: UI Not Updating

**Check:**
1. Adapter is set to RecyclerView
2. `notifyDataSetChanged()` is called
3. `showContent()` is called
4. RecyclerView is visible

**Fix:**
```java
// Force UI update
runOnUiThread(() -> {
    adapter.notifyDataSetChanged();
    showContent();
});
```

---

### Issue: Wrong Status Colors

**Check:**
1. Color values are correct hex codes
2. `isEnrolled()` and `isPaid()` methods work correctly
3. Status fields have correct values ("0" or "1")

**Fix:**
```java
// Debug status values
Log.d(TAG, "is_enroll: " + admission.getIsEnroll());
Log.d(TAG, "paid_status: " + admission.getPaidStatus());
Log.d(TAG, "isEnrolled(): " + admission.isEnrolled());
Log.d(TAG, "isPaid(): " + admission.isPaid());
```

---

## 📊 Performance Tips

### Optimize RecyclerView
```java
// In onCreate()
reportContentRecyclerView.setHasFixedSize(true);
reportContentRecyclerView.setItemViewCacheSize(20);
```

### Reduce API Calls
```java
// Cache data
private List<OnlineAdmissionModel> cachedData;

if (cachedData != null && !cachedData.isEmpty()) {
    admissionList.addAll(cachedData);
    adapter.notifyDataSetChanged();
    showContent();
    return;
}
```

### Optimize Layouts
```xml
<!-- Use ConstraintLayout for complex layouts -->
<androidx.constraintlayout.widget.ConstraintLayout>
    <!-- Views here -->
</androidx.constraintlayout.widget.ConstraintLayout>
```

---

## 🎨 UI Customization

### Change Colors

**File:** app/src/main/res/values/colors.xml

```xml
<color name="enrollment_enrolled">#4CAF50</color>
<color name="enrollment_not_enrolled">#FF9800</color>
<color name="payment_paid">#4CAF50</color>
<color name="payment_unpaid">#F44336</color>
```

**File:** OnlineAdmissionAdapter.java

```xml
holder.enrollmentStatusTv.setBackgroundColor(
    ContextCompat.getColor(context, R.color.enrollment_enrolled)
);
```

---

### Change Text Sizes

**File:** item_online_admission.xml

```xml
<TextView
    android:textSize="20sp"  <!-- Increase from 18sp -->
    android:textStyle="bold" />
```

---

### Add Icons

**File:** item_online_admission.xml

```xml
<ImageView
    android:id="@+id/student_icon"
    android:layout_width="24dp"
    android:layout_height="24dp"
    android:src="@drawable/ic_student"
    android:tint="@color/green" />
```

---

## 📱 Testing Commands

### Build and Install
```bash
./gradlew assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### View Logs
```bash
adb logcat -s OnlineAdmissionReport
```

### Clear App Data
```bash
adb shell pm clear com.qdocs.ssre241123
```

### Test API with cURL
```bash
curl -X POST "http://localhost/amt/api/online-admission/filter" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d '{"class_id": 19}'
```

---

## 📚 Related Files

### Similar Reports
- StudentReportActivity.java
- StudentHistoryActivity.java
- GuardianReportActivity.java
- ClassSectionReportActivity.java

### Base Classes
- TeacherReportDetailActivity.java
- BaseActivity.java

### Utilities
- Constants.java
- Utility.java

---

## 🔗 Documentation Links

### Full Documentation
- [Implementation Guide](ONLINE_ADMISSION_REPORT_IMPLEMENTATION.md)
- [Testing Guide](ONLINE_ADMISSION_REPORT_TESTING_GUIDE.md)
- [Summary](ONLINE_ADMISSION_REPORT_SUMMARY.md)

### API Documentation
- See the Online Admission API documentation provided

---

## 💡 Tips & Tricks

### Tip 1: Use Existing Patterns
Always follow the pattern used in ClassSectionReportActivity.java for consistency.

### Tip 2: Log Everything During Development
Add detailed logs during development, reduce in production.

### Tip 3: Handle Nulls Gracefully
Always use `optString()` instead of `getString()` for JSON parsing.

### Tip 4: Test Edge Cases
- Empty response
- Network error
- Invalid data
- Large datasets
- Missing fields

### Tip 5: Keep UI Responsive
Use background threads for heavy operations, update UI on main thread.

---

## 🎯 Checklist

### Before Committing
- [ ] Code compiles without errors
- [ ] No IDE warnings
- [ ] Logs are appropriate (not too verbose)
- [ ] Error handling is comprehensive
- [ ] UI is responsive
- [ ] Tested on multiple devices
- [ ] Documentation updated

### Before Release
- [ ] All test cases passed
- [ ] Performance tested
- [ ] Memory leaks checked
- [ ] Proguard rules added if needed
- [ ] Release notes prepared
- [ ] User documentation ready

---

## 📞 Support

### For Help
1. Check this quick reference
2. Review implementation documentation
3. Check Logcat for errors
4. Review similar report implementations
5. Test API with cURL

### Common Solutions
- **Rebuild project**: File → Invalidate Caches / Restart
- **Clean build**: Build → Clean Project → Rebuild Project
- **Sync Gradle**: File → Sync Project with Gradle Files

---

## 🔑 Key Code Snippets

### Get Filter Values
```java
String sessionId = getSelectedSessionId();
String classId = getSelectedClassId();
String sectionId = getSelectedSectionId();
```

### Make API Call
```java
StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
    response -> parseOnlineAdmissionResponse(response),
    error -> showError("Network error")
) {
    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Client-Service", Constants.clientService);
        headers.put("Auth-Key", Constants.authKey);
        return headers;
    }
};
```

### Parse Response
```java
JSONObject jsonObject = new JSONObject(response);
int status = jsonObject.optInt("status", 0);
if (status == 1) {
    JSONArray dataArray = jsonObject.optJSONArray("data");
    // Process data
}
```

### Update UI
```java
runOnUiThread(() -> {
    adapter.notifyDataSetChanged();
    showContent();
});
```

---

## 📈 Status Indicators

### Enrollment Status
- **"1"** = Enrolled (Green badge)
- **"0"** = Not Enrolled (Orange badge)

### Payment Status
- **"1"** = Paid (Green text)
- **"0"** = Unpaid (Red text)

### Form Status
- **"1"** = Submitted
- **"0"** = Draft

---

## 🎨 Color Reference

### Status Colors
```
Enrolled:       #4CAF50 (Green)
Not Enrolled:   #FF9800 (Orange)
Paid:           #4CAF50 (Green)
Unpaid:         #F44336 (Red)
```

### Text Colors
```
Primary:        #000000 (Black)
Secondary:      #757575 (Gray)
Divider:        #E0E0E0 (Light Gray)
```

---

**Quick Reference Complete! 📖**

**Last Updated:** 2025-10-09  
**Version:** 1.0.0

