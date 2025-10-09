# Class Section Report - Quick Reference Guide

## 🚀 Quick Start

### For Developers

#### Files to Know
```
Model:      app/src/main/java/com/qdocs/ssre241123/model/ClassSectionReportModel.java
Adapter:    app/src/main/java/com/qdocs/ssre241123/adapters/ClassSectionReportAdapter.java
Activity:   app/src/main/java/com/qdocs/ssre241123/teachers/ClassSectionReportActivity.java
Layout:     app/src/main/res/layout/item_class_section_report.xml
```

#### Key Classes
- **ClassSectionReportModel** - Data model
- **ClassSectionReportAdapter** - RecyclerView adapter
- **ClassSectionReportActivity** - Main activity (extends TeacherReportDetailActivity)

#### Navigation
```
Teacher Dashboard → Reports → Student Information → Class & Section Report
```

---

## 📡 API Quick Reference

### Endpoint
```
POST /api/class-section-report/filter
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
  "session_id": 18,
  "class_id": 10,
  "section_id": 15
}
```

### Response
```json
{
  "status": 1,
  "message": "Class section report retrieved successfully",
  "total_records": 7,
  "summary": {
    "total_classes": 1,
    "total_sections": 7,
    "total_students": 42
  },
  "data": [
    {
      "id": "15",
      "class_id": "10",
      "section_id": "15",
      "class": "JR-BIPC",
      "section": "08199-JR-BIPC-B1",
      "student_count": "42"
    }
  ]
}
```

---

## 🔧 Common Tasks

### Add New Field to Display

1. **Update Model** (ClassSectionReportModel.java)
```java
private String newField;

public String getNewField() {
    return newField;
}

public void setNewField(String newField) {
    this.newField = newField;
}
```

2. **Update Layout** (item_class_section_report.xml)
```xml
<TextView
    android:id="@+id/new_field_tv"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="New Field" />
```

3. **Update Adapter** (ClassSectionReportAdapter.java)
```java
// In ViewHolder
TextView newFieldTv;

// In constructor
newFieldTv = itemView.findViewById(R.id.new_field_tv);

// In onBindViewHolder
holder.newFieldTv.setText(classSection.getNewField());
```

4. **Update Activity** (ClassSectionReportActivity.java)
```java
// In parseClassSectionReportResponse
classSection.setNewField(classSectionObj.optString("new_field", ""));
```

---

### Change API Endpoint

**File**: ClassSectionReportActivity.java

```java
// Line ~76
String url = baseUrl + "your-new-endpoint";
```

---

### Modify Filters

**File**: ClassSectionReportActivity.java

```java
// In getBody() method, add new filter
if (newFilterId != null && !newFilterId.isEmpty()) {
    jsonBody.put("new_filter_id", Integer.parseInt(newFilterId));
}
```

---

### Change Card Design

**File**: item_class_section_report.xml

Modify the CardView properties:
```xml
<androidx.cardview.widget.CardView
    app:cardCornerRadius="12dp"    <!-- Change corner radius -->
    app:cardElevation="6dp"        <!-- Change elevation -->
    android:layout_margin="12dp">  <!-- Change margin -->
```

---

## 🐛 Debugging

### Enable Detailed Logging

**File**: ClassSectionReportActivity.java

```java
private static final String TAG = "ClassSectionReport";
```

All logs use this TAG. Filter Logcat:
```
adb logcat -s ClassSectionReport
```

### Common Log Points
```
D/ClassSectionReport: loadReportData called
D/ClassSectionReport: === API Request Details ===
D/ClassSectionReport: === API Response Received ===
D/ClassSectionReport: === Parsing Response ===
E/ClassSectionReport: === API Error ===
```

---

## 🔍 Troubleshooting

### Issue: No Data Displayed

**Check**:
1. API response in Logcat
2. `status` field in response (should be 1)
3. `data` array is not empty
4. Adapter is notified: `adapter.notifyDataSetChanged()`

**Fix**:
```java
// Add logging
Log.d(TAG, "Data array length: " + dataArray.length());
Log.d(TAG, "List size: " + classSectionList.size());
```

---

### Issue: Network Error

**Check**:
1. Device has internet connection
2. API URL is correct
3. Server is running
4. Firewall/proxy settings

**Fix**:
```java
// Check base URL
String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
Log.d(TAG, "Base URL: " + baseUrl);
```

---

### Issue: Parsing Error

**Check**:
1. API response format matches expected
2. Field names are correct
3. Data types match

**Fix**:
```java
// Add try-catch around parsing
try {
    classSection.setClassName(classSectionObj.optString("class", ""));
} catch (Exception e) {
    Log.e(TAG, "Error parsing class name", e);
}
```

---

### Issue: UI Not Updating

**Check**:
1. Adapter is set to RecyclerView
2. `notifyDataSetChanged()` is called
3. `showContent()` is called
4. RecyclerView is visible

**Fix**:
```java
// Force UI update
runOnUiThread(() -> {
    adapter.notifyDataSetChanged();
    showContent();
});
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
private List<ClassSectionReportModel> cachedData;

if (cachedData != null && !cachedData.isEmpty()) {
    classSectionList.addAll(cachedData);
    adapter.notifyDataSetChanged();
    showContent();
    return;
}
```

### Optimize Layouts
```xml
<!-- Use ConstraintLayout instead of nested LinearLayouts -->
<androidx.constraintlayout.widget.ConstraintLayout>
    <!-- Views here -->
</androidx.constraintlayout.widget.ConstraintLayout>
```

---

## 🎨 UI Customization

### Change Colors

**File**: app/src/main/res/values/colors.xml

```xml
<color name="class_section_primary">#4CAF50</color>
<color name="class_section_accent">#FF9800</color>
```

**File**: item_class_section_report.xml

```xml
<TextView
    android:textColor="@color/class_section_primary" />
```

---

### Change Text Sizes

**File**: item_class_section_report.xml

```xml
<TextView
    android:textSize="20sp"  <!-- Increase from 18sp -->
    android:textStyle="bold" />
```

---

### Add Icons

**File**: item_class_section_report.xml

```xml
<ImageView
    android:id="@+id/class_icon"
    android:layout_width="24dp"
    android:layout_height="24dp"
    android:src="@drawable/ic_fa_users"
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
adb logcat -s ClassSectionReport
```

### Clear App Data
```bash
adb shell pm clear com.qdocs.ssre241123
```

### Test API with cURL
```bash
curl -X POST "http://localhost/amt/api/class-section-report/filter" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d '{"class_id": 10}'
```

---

## 📚 Related Files

### Similar Reports
- StudentReportActivity.java
- StudentHistoryActivity.java
- GuardianReportActivity.java
- ParentLoginActivity.java
- StudentLoginActivity.java

### Base Classes
- TeacherReportDetailActivity.java
- BaseActivity.java

### Utilities
- Constants.java
- Utility.java

---

## 🔗 Quick Links

### Documentation
- [Implementation Guide](CLASS_SECTION_REPORT_IMPLEMENTATION.md)
- [Testing Guide](CLASS_SECTION_REPORT_TESTING_GUIDE.md)
- [Summary](CLASS_SECTION_REPORT_SUMMARY.md)

### API Documentation
- [Class Section Report API](# Class Section Report API Documentation)

---

## 💡 Tips & Tricks

### Tip 1: Use Existing Patterns
Always follow the pattern used in StudentReportActivity.java for consistency.

### Tip 2: Log Everything
Add detailed logs during development, remove or reduce in production.

### Tip 3: Handle Nulls
Always use `optString()` instead of `getString()` for JSON parsing.

### Tip 4: Test Edge Cases
- Empty response
- Network error
- Invalid data
- Large datasets

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

**Last Updated**: 2025-10-09
**Version**: 1.0.0

---

**Quick Reference Complete! 📖**

