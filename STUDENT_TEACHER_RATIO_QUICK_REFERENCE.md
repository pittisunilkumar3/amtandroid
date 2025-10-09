# Student Teacher Ratio Report - Quick Reference

## 🚀 Quick Start

### Files Overview
```
Model:      app/src/main/java/com/qdocs/ssre241123/model/StudentTeacherRatioModel.java
Adapter:    app/src/main/java/com/qdocs/ssre241123/adapters/StudentTeacherRatioAdapter.java
Activity:   app/src/main/java/com/qdocs/ssre241123/teachers/StudentTeacherRatioActivity.java
Layout:     app/src/main/res/layout/item_student_teacher_ratio.xml
```

### Navigation
```
Teacher Dashboard → Reports → Student Information → Student Teacher Ratio Report
```

---

## 📡 API Quick Reference

### Endpoint
```
POST /api/student-teacher-ratio-report/filter
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
  "class_id": 1,
  "section_id": 2
}
```

### Response
```json
{
  "status": 1,
  "message": "Student teacher ratio report retrieved successfully",
  "total_records": 5,
  "summary": {
    "total_students": 150,
    "total_boys": 80,
    "total_girls": 70,
    "total_teachers": 15,
    "boys_girls_ratio": "1:0.88",
    "student_teacher_ratio": "1:0.1"
  },
  "data": [
    {
      "total_student": "45",
      "male": "25",
      "female": "20",
      "class": "Class 1",
      "section": "A",
      "class_id": "1",
      "section_id": "1",
      "total_teacher": 5,
      "boys_girls_ratio": "1:0.8",
      "teacher_ratio": "1:0.11"
    }
  ]
}
```

---

## 🔧 Common Tasks

### Add New Field to Display

1. **Update Model** (StudentTeacherRatioModel.java)
```java
private String newField;

public String getNewField() {
    return newField;
}

public void setNewField(String newField) {
    this.newField = newField;
}
```

2. **Update Layout** (item_student_teacher_ratio.xml)
```xml
<TextView
    android:id="@+id/new_field_tv"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="New Field" />
```

3. **Update Adapter** (StudentTeacherRatioAdapter.java)
```java
// In ViewHolder
TextView newFieldTv;

// In constructor
newFieldTv = itemView.findViewById(R.id.new_field_tv);

// In onBindViewHolder
holder.newFieldTv.setText(ratio.getNewField());
```

4. **Update Activity** (StudentTeacherRatioActivity.java)
```java
// In parseStudentTeacherRatioResponse
ratio.setNewField(ratioObj.optString("new_field", ""));
```

---

### Change API Endpoint

**File**: StudentTeacherRatioActivity.java

```java
// Line ~76
String url = baseUrl + "your-new-endpoint";
```

---

### Modify Card Design

**File**: item_student_teacher_ratio.xml

```xml
<androidx.cardview.widget.CardView
    app:cardCornerRadius="12dp"    <!-- Change corner radius -->
    app:cardElevation="6dp"        <!-- Change elevation -->
    android:layout_margin="12dp">  <!-- Change margin -->
```

---

## 🐛 Debugging

### Enable Logging
```
adb logcat -s StudentTeacherRatio
```

### Common Log Points
```
D/StudentTeacherRatio: loadReportData called
D/StudentTeacherRatio: === API Request Details ===
D/StudentTeacherRatio: === API Response Received ===
D/StudentTeacherRatio: === Parsing Response ===
E/StudentTeacherRatio: === API Error ===
```

---

## 🔍 Troubleshooting

### No Data Displayed

**Check**:
1. API response in Logcat
2. `status` field = 1
3. `data` array not empty
4. Adapter notified

**Fix**:
```java
Log.d(TAG, "Data array length: " + dataArray.length());
Log.d(TAG, "List size: " + ratioList.size());
```

---

### Network Error

**Check**:
1. Internet connection
2. API URL correct
3. Server running

**Fix**:
```java
String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
Log.d(TAG, "Base URL: " + baseUrl);
```

---

### Parsing Error

**Check**:
1. Response format matches expected
2. Field names correct
3. Data types match

**Fix**:
```java
// Handle mixed types for total_teacher
Object totalTeacherObj = ratioObj.opt("total_teacher");
if (totalTeacherObj != null) {
    ratio.setTotalTeacher(String.valueOf(totalTeacherObj));
}
```

---

## 📊 Ratio Interpretation

### Boys:Girls Ratio
- **1:1** = Equal boys and girls
- **1:0.8** = More boys (1 boy per 0.8 girls)
- **1:1.2** = More girls (1 boy per 1.2 girls)

### Student:Teacher Ratio
- **1:0.1** = 10 students per teacher
- **1:0.05** = 20 students per teacher
- **1:0.2** = 5 students per teacher

**Formula**: Students per teacher = 1 ÷ ratio value

---

## 🎨 UI Customization

### Change Colors

**File**: app/src/main/res/values/colors.xml

```xml
<color name="ratio_primary">#4CAF50</color>
<color name="ratio_accent">#FF9800</color>
```

**File**: item_student_teacher_ratio.xml

```xml
<TextView
    android:textColor="@color/ratio_primary" />
```

---

### Change Text Sizes

**File**: item_student_teacher_ratio.xml

```xml
<TextView
    android:textSize="20sp"  <!-- Increase from 18sp -->
    android:textStyle="bold" />
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
adb logcat -s StudentTeacherRatio
```

### Test API with cURL
```bash
curl -X POST "http://localhost/amt/api/student-teacher-ratio-report/filter" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d '{"class_id": 1}'
```

---

## 💡 Tips & Tricks

### Tip 1: Follow Patterns
Always follow the pattern used in ClassSectionReportActivity.java for consistency.

### Tip 2: Log Everything
Add detailed logs during development, reduce in production.

### Tip 3: Handle Nulls
Always use `optString()` instead of `getString()` for JSON parsing.

### Tip 4: Test Edge Cases
- Empty response
- Network error
- Invalid data
- Large datasets

### Tip 5: Mixed Data Types
Handle both integer and string values for numeric fields:
```java
Object value = jsonObj.opt("field");
String stringValue = String.valueOf(value);
```

---

## 📚 Related Files

### Similar Reports
- ClassSectionReportActivity.java
- StudentReportActivity.java
- GuardianReportActivity.java

### Base Classes
- TeacherReportDetailActivity.java
- BaseActivity.java

---

## 🎯 Checklist

### Before Committing
- [ ] Code compiles without errors
- [ ] No IDE warnings
- [ ] Logs appropriate
- [ ] Error handling comprehensive
- [ ] UI responsive
- [ ] Documentation updated

### Before Release
- [ ] All test cases passed
- [ ] Performance tested
- [ ] Memory leaks checked
- [ ] Release notes prepared

---

## 📞 Support

### For Help
1. Check this quick reference
2. Review STUDENT_TEACHER_RATIO_IMPLEMENTATION.md
3. Check Logcat for errors
4. Test API with cURL

### Common Solutions
- **Rebuild**: File → Invalidate Caches / Restart
- **Clean**: Build → Clean Project → Rebuild Project
- **Sync**: File → Sync Project with Gradle Files

---

## 🔗 Documentation Links

- [Implementation Guide](STUDENT_TEACHER_RATIO_IMPLEMENTATION.md)
- [Testing Guide](STUDENT_TEACHER_RATIO_TESTING_GUIDE.md)
- [Summary](STUDENT_TEACHER_RATIO_SUMMARY.md)

---

**Last Updated**: 2025-10-09
**Version**: 1.0.0

---

**Quick Reference Complete! 📖**

