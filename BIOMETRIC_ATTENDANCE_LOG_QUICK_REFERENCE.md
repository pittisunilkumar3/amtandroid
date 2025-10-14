# Biometric Attendance Log Report - Quick Reference

## 🚀 Quick Start

### Access the Report
1. Open Smart School Android App
2. Navigate to **Reports** → **Attendance** → **Biometric Attendance Log**

### Generate Report
1. Select **From Date** (default: 7 days ago)
2. Select **To Date** (default: today)
3. Optionally select a **Student** (default: All Students)
4. Tap **Generate Report**

---

## 📊 API Quick Reference

### Endpoint
```
POST /api/biometric-attlog-report/filter
```

### Headers
```
Client-Service: smartschool
Auth-Key: schoolAdmin@
Content-Type: application/json
```

### Request Body (All Optional)
```json
{
  "from_date": "2025-10-01",      // Optional: Start date (YYYY-MM-DD)
  "to_date": "2025-10-07",        // Optional: End date (YYYY-MM-DD)
  "student_id": 50,               // Optional: Student ID (integer)
  "limit": 50,                    // Optional: Records per page (default: 100)
  "offset": 0                     // Optional: Starting position (default: 0)
}
```

### Response Fields
| Field | Type | Description |
|-------|------|-------------|
| `id` | string | Attendance record ID |
| `student_session_id` | string | Student session ID |
| `date` | string | Attendance date (YYYY-MM-DD) |
| `attendence_type_id` | string | 1=Present, 2=Excuse, 3=Late, 4=Absent, 6=Half Day |
| `remark` | string | Attendance remark |
| `biometric_attendence` | string | 1=biometric, 0=manual |
| `biometric_device_data` | string | Device information |
| `name` | string | Student full name |
| `admission_no` | string | Student admission number |
| `roll_no` | string | Student roll number |
| `class` | string | Class name |
| `section` | string | Section name |

---

## 🎨 Attendance Type Colors

| Type | ID | Color | Hex Code |
|------|----|----|----------|
| Present | 1 | 🟢 Green | #4CAF50 |
| Excuse | 2 | 🔵 Blue | #2196F3 |
| Late | 3 | 🟠 Orange | #FF9800 |
| Absent | 4 | 🔴 Red | #F44336 |
| Half Day | 6 | 🔷 Cyan | #00BCD4 |

---

## 📁 Key Files

### Java Classes
```
BiometricAttlogReportModel.java          - Data model
BiometricAttlogReportAdapter.java        - RecyclerView adapter
BiometricAttlogReportActivity.java       - Main activity
```

### Layout Files
```
activity_biometric_attlog_report.xml     - Main activity layout
list_item_biometric_attlog_report.xml    - List item layout
```

### Constants
```java
Constants.biometricAttlogReportFilterUrl = "biometric-attlog-report/filter"
Constants.biometricAttlogReportListUrl = "biometric-attlog-report/list"
```

---

## 🔧 Configuration

### Change Pagination Limit
**File:** `BiometricAttlogReportActivity.java`
```java
private static final int DEFAULT_LIMIT = 50;  // Change this value
```

### Change Default Date Range
**File:** `BiometricAttlogReportActivity.java`
```java
private void setDefaultDateRange() {
    // Current: Last 7 days
    fromDateCalendar.add(Calendar.DAY_OF_MONTH, -7);  // Change -7 to desired days
}
```

---

## 🐛 Troubleshooting

### Issue: ActivityNotFoundException
**Error:** `Unable to find explicit activity class BiometricAttlogReportActivity`
**Solution:** Ensure the activity is declared in `AndroidManifest.xml`:
```xml
<activity
    android:name=".teachers.BiometricAttlogReportActivity"
    android:exported="false" />
```

### Issue: Report not showing in menu
**Solution:** Ensure menu API returns item with ID `biometric_attendance_log` or `biometric_attlog_report`

### Issue: No data displayed
**Solution:** 
- Check date range has data
- Verify API endpoint is accessible
- Check network connectivity
- Review API response in logs

### Issue: Student dropdown empty
**Solution:**
- Verify `/teacher/students` API is working
- Check API authentication headers
- Review logs for API errors

### Issue: Load More button not appearing
**Solution:**
- Verify `total_records` > current list size
- Check pagination logic in `parseReportResponse()`

---

## 📝 Testing Checklist

- [ ] Report opens from menu
- [ ] Default date range is last 7 days
- [ ] Date pickers work correctly
- [ ] Student dropdown loads
- [ ] Generate Report button works
- [ ] Data displays in list
- [ ] Attendance type colors are correct
- [ ] Biometric device info shows when available
- [ ] Remarks show when available
- [ ] Summary card displays correct counts
- [ ] Load More button works
- [ ] Pagination loads additional records
- [ ] Empty state shows when no data
- [ ] Loading indicator shows during API calls

---

## 💡 Tips

1. **Performance**: Use pagination for large datasets
2. **UX**: Default date range helps users get started quickly
3. **Filtering**: Student filter is optional - leave as "All Students" for overview
4. **Colors**: Attendance type colors provide quick visual feedback
5. **Device Info**: Biometric device data helps track which device recorded attendance

---

## 📞 Support

For issues or questions:
- Check logs with tag: `BiometricAttlogReport`
- Review API response format
- Verify authentication headers
- Test API endpoint with Postman/curl

---

**Last Updated:** October 2025  
**Version:** 1.0.0  
**Status:** ✅ Production Ready

