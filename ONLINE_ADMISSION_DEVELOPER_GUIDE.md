# Online Admission Report - Developer Quick Reference

## 🚀 Quick Start

### File Locations
```
Model:      app/src/main/java/com/qdocs/ssre241123/model/OnlineAdmissionModel.java
Adapter:    app/src/main/java/com/qdocs/ssre241123/adapters/OnlineAdmissionAdapter.java
Activity:   app/src/main/java/com/qdocs/ssre241123/teachers/OnlineAdmissionReportActivity.java
Layout:     app/src/main/res/layout/item_online_admission.xml
Constants:  app/src/main/java/com/qdocs/ssre241123/utils/Constants.java
```

---

## 📡 API Integration

### Endpoint
```java
// In Constants.java
public static final String onlineAdmissionFilterUrl = "online-admission/filter";
public static final String onlineAdmissionListUrl = "online-admission/list";
public static final String onlineAdmissionGetUrl = "online-admission/get/";
```

### Making API Call
```java
// Build URL using helper method
String url = Utility.buildApiUrl(getApplicationContext(), Constants.onlineAdmissionFilterUrl);

// Create request body
JSONObject jsonBody = new JSONObject();
jsonBody.put("class_id", Integer.parseInt(classId));
jsonBody.put("section_id", Integer.parseInt(sectionId));

// Add headers
headers.put("Client-Service", Constants.clientService);
headers.put("Auth-Key", Constants.authKey);
headers.put("Content-Type", "application/json");
```

### Response Parsing
```java
JSONObject jsonObject = new JSONObject(response);
int status = jsonObject.optInt("status", 0);

if (status == 1) {
    JSONArray dataArray = jsonObject.optJSONArray("data");
    for (int i = 0; i < dataArray.length(); i++) {
        JSONObject admissionObj = dataArray.getJSONObject(i);
        OnlineAdmissionModel admission = new OnlineAdmissionModel();
        
        // Parse basic info
        admission.setId(admissionObj.optString("id", ""));
        admission.setReferenceNo(admissionObj.optString("reference_no", ""));
        
        // Parse class info (nested object)
        JSONObject classInfo = admissionObj.optJSONObject("class_info");
        if (classInfo != null) {
            admission.setClassId(classInfo.optString("class_id", ""));
            admission.setClassName(classInfo.optString("class_name", ""));
        }
        
        admissionList.add(admission);
    }
}
```

---

## 🎨 UI Components

### RecyclerView Setup
```java
reportContentRecyclerView = findViewById(R.id.report_content_recyclerView);
reportContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

admissionList = new ArrayList<>();
adapter = new OnlineAdmissionAdapter(this, admissionList);
reportContentRecyclerView.setAdapter(adapter);
```

### Adapter Implementation
```java
@Override
public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    OnlineAdmissionModel admission = admissionList.get(position);
    
    // Set data
    holder.studentNameTv.setText(admission.getFullName());
    holder.referenceNoTv.setText(admission.getReferenceNo());
    
    // Set enrollment status with color
    holder.enrollmentStatusTv.setText(admission.getEnrollmentStatus());
    if (admission.isEnrolled()) {
        holder.enrollmentStatusTv.setBackgroundColor(Color.parseColor("#4CAF50")); // Green
    } else {
        holder.enrollmentStatusTv.setBackgroundColor(Color.parseColor("#FF9800")); // Orange
    }
    
    // Hide optional fields if empty
    if (admission.getAdmissionNo() != null && !admission.getAdmissionNo().isEmpty()) {
        holder.admissionNoLayout.setVisibility(View.VISIBLE);
    } else {
        holder.admissionNoLayout.setVisibility(View.GONE);
    }
}
```

---

## 🔧 Model Helper Methods

### OnlineAdmissionModel.java
```java
// Get formatted class and section
public String getClassSection() {
    if (className != null && sectionName != null) {
        return className + " - " + sectionName;
    }
    return "N/A";
}

// Get enrollment status text
public String getEnrollmentStatus() {
    return "1".equals(isEnroll) ? "Enrolled" : "Not Enrolled";
}

// Check if enrolled (boolean)
public boolean isEnrolled() {
    return "1".equals(isEnroll);
}

// Get payment status text
public String getPaymentStatus() {
    return "1".equals(paidStatus) ? "Paid" : "Unpaid";
}

// Check if paid (boolean)
public boolean isPaid() {
    return "1".equals(paidStatus);
}

// Get first available parent contact
public String getParentContact() {
    if (fatherPhone != null && !fatherPhone.isEmpty()) {
        return fatherPhone;
    } else if (motherPhone != null && !motherPhone.isEmpty()) {
        return motherPhone;
    } else if (guardianPhone != null && !guardianPhone.isEmpty()) {
        return guardianPhone;
    }
    return "N/A";
}
```

---

## 🎯 State Management

### Activity States
```java
// Show loading
showLoading();

// Show content
showContent();

// Show no data
showNoData("No online admissions found");

// Show error
showError("Failed to load data");
```

### Inherited from TeacherReportDetailActivity
```java
// Get filter values
String sessionId = getSelectedSessionId();
String classId = getSelectedClassId();
String sectionId = getSelectedSectionId();

// Get RecyclerView
RecyclerView recyclerView = getReportContentRecyclerView();
```

---

## 🔍 Debugging

### Enable Logging
```java
private static final String TAG = "OnlineAdmissionReport";

Log.d(TAG, "=== Fetching Online Admissions ===");
Log.d(TAG, "URL: " + url);
Log.d(TAG, "Request Body: " + requestBody);
Log.d(TAG, "Response: " + response);
Log.d(TAG, "Total admissions parsed: " + admissionList.size());
```

### Common Issues

#### Issue 1: Double /api/ in URL
**Problem**: URL becomes `https://school.cyberdetox.in/api//api/online-admission/filter`

**Solution**: Use `Utility.buildApiUrl()` instead of manual concatenation
```java
// ❌ Wrong
String url = Utility.getSharedPreferences(context, "apiUrl") + "online-admission/filter";

// ✅ Correct
String url = Utility.buildApiUrl(context, Constants.onlineAdmissionFilterUrl);
```

#### Issue 2: Null Pointer Exception
**Problem**: Accessing null fields

**Solution**: Use `optString()` with default values
```java
// ❌ Wrong
admission.setEmail(admissionObj.getString("email"));

// ✅ Correct
admission.setEmail(admissionObj.optString("email", ""));
```

#### Issue 3: ClassCastException
**Problem**: Parsing integer as string

**Solution**: Use appropriate parsing method
```java
// ❌ Wrong
jsonBody.put("class_id", classId); // classId is String

// ✅ Correct
jsonBody.put("class_id", Integer.parseInt(classId));
```

---

## 📝 Code Snippets

### Add New Filter Parameter
```java
// In fetchOnlineAdmissions() method
if (gender != null && !gender.isEmpty()) {
    jsonBody.put("gender", gender);
    Log.d(TAG, "Added gender filter: " + gender);
}
```

### Add New Field to Model
```java
// 1. Add field to OnlineAdmissionModel.java
private String newField;

public String getNewField() {
    return newField;
}

public void setNewField(String newField) {
    this.newField = newField;
}

// 2. Parse in OnlineAdmissionReportActivity.java
admission.setNewField(admissionObj.optString("new_field", ""));

// 3. Display in OnlineAdmissionAdapter.java
holder.newFieldTv.setText(admission.getNewField());
```

### Add New UI Element
```xml
<!-- 1. Add to item_online_admission.xml -->
<LinearLayout
    android:id="@+id/new_field_layout"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="horizontal">
    
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="New Field: " />
    
    <TextView
        android:id="@+id/new_field_tv"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
</LinearLayout>
```

```java
// 2. Add to ViewHolder in OnlineAdmissionAdapter.java
TextView newFieldTv;
LinearLayout newFieldLayout;

public ViewHolder(@NonNull View itemView) {
    super(itemView);
    newFieldTv = itemView.findViewById(R.id.new_field_tv);
    newFieldLayout = itemView.findViewById(R.id.new_field_layout);
}

// 3. Bind in onBindViewHolder()
if (admission.getNewField() != null && !admission.getNewField().isEmpty()) {
    holder.newFieldTv.setText(admission.getNewField());
    holder.newFieldLayout.setVisibility(View.VISIBLE);
} else {
    holder.newFieldLayout.setVisibility(View.GONE);
}
```

---

## 🧪 Testing

### Unit Test Example
```java
@Test
public void testEnrollmentStatus() {
    OnlineAdmissionModel admission = new OnlineAdmissionModel();
    
    admission.setIsEnroll("1");
    assertEquals("Enrolled", admission.getEnrollmentStatus());
    assertTrue(admission.isEnrolled());
    
    admission.setIsEnroll("0");
    assertEquals("Not Enrolled", admission.getEnrollmentStatus());
    assertFalse(admission.isEnrolled());
}
```

### Manual Testing
```bash
# Test API directly
curl -X POST "https://school.cyberdetox.in/api/online-admission/filter" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d '{"class_id": 19, "section_id": 47}'
```

---

## 📚 Related Files

### Similar Reports
- `StudentReportActivity.java` - Student report implementation
- `GuardianReportActivity.java` - Guardian report implementation
- `ClassSectionReportActivity.java` - Class section report

### Base Classes
- `TeacherReportDetailActivity.java` - Base activity with filters
- `BaseActivity.java` - App base activity

### Utilities
- `Constants.java` - App constants
- `Utility.java` - Helper methods

---

## 🔗 Documentation

- [API Documentation](./ONLINE_ADMISSION_API_DOCUMENTATION.md)
- [Implementation Summary](./ONLINE_ADMISSION_API_IMPLEMENTATION_SUMMARY.md)
- [Testing Guide](./ONLINE_ADMISSION_TESTING_GUIDE.md)

---

## 💡 Best Practices

1. ✅ Always use `Utility.buildApiUrl()` for URL construction
2. ✅ Use `optString()` instead of `getString()` for safe parsing
3. ✅ Add comprehensive logging for debugging
4. ✅ Handle null values gracefully
5. ✅ Hide optional UI elements when data is empty
6. ✅ Use constants instead of hardcoded strings
7. ✅ Follow existing code patterns in the app
8. ✅ Test with various data scenarios (empty, null, large datasets)

---

## 🎉 Quick Commands

```bash
# Build the app
./gradlew assembleDebug

# Install on device
adb install app/build/outputs/apk/debug/app-debug.apk

# View logs
adb logcat | grep "OnlineAdmissionReport"

# Clear app data
adb shell pm clear com.qdocs.ssre241123
```

---

**Last Updated**: 2025-10-09
**Version**: 1.0

