# Session Fee Structure Reports - Quick Reference

## 🚀 Quick Start

### Navigation Paths

**Type Wise Balance Report:**
```
Teacher Dashboard → Reports → Finance → Type Wise Balance Report
```

**Fee Collection Report Column Wise:**
```
Teacher Dashboard → Reports → Finance → Fee Collection Report Column Wise
```

---

## 📁 File Locations

### Type Wise Balance Report
```
Activity:  app/src/main/java/com/qdocs/ssre241123/teachers/TypeWiseBalanceReportActivity.java
Layout:    app/src/main/res/layout/activity_type_wise_balance_report.xml
```

### Fee Collection Report Column Wise
```
Activity:  app/src/main/java/com/qdocs/ssre241123/teachers/FeeCollectionReportColumnWiseActivity.java
Layout:    app/src/main/res/layout/activity_fee_collection_report_column_wise.xml
```

### Modified Files
```
Constants:         app/src/main/java/com/qdocs/ssre241123/utils/Constants.java
Adapter:           app/src/main/java/com/qdocs/ssre241123/adapters/ReportItemAdapter.java
Manifest:          app/src/main/AndroidManifest.xml
```

---

## 📡 API Endpoints

### Session Fee Structure List API
```
POST /api/session-fee-structure/list
Headers:
  Client-Service: smartschool
  Auth-Key: schoolAdmin@
  Content-Type: application/json
Body: {}
```

**Returns:** Sessions, Classes, Fee Groups, Fee Types

### Type Wise Balance Report API
```
POST /api/type-wise-balance-report/filter
Headers:
  Client-Service: smartschool
  Auth-Key: schoolAdmin@
  Content-Type: application/json
Body: {
  "session_id": "1",      // Optional
  "class_id": "2",        // Optional
  "section_id": "3",      // Optional
  "fee_group_id": "4",    // Optional
  "fee_type_id": "5"      // Optional
}
```

### Fee Collection Report Column Wise API
```
POST /api/fee-collection-report-column-wise/filter
Headers:
  Client-Service: smartschool
  Auth-Key: schoolAdmin@
  Content-Type: application/json
Body: {
  "from_date": "2024-01-01",  // Optional (yyyy-MM-dd)
  "to_date": "2024-12-31",    // Optional (yyyy-MM-dd)
  "session_id": "1",          // Optional
  "class_id": "2",            // Optional
  "section_id": "3",          // Optional
  "fee_type_id": "5"          // Optional
}
```

---

## 🎯 Filter Configuration

### Type Wise Balance Report Filters

| Filter | Type | Required | Source |
|--------|------|----------|--------|
| Session | Dropdown | No | Session Fee Structure API |
| Class | Dropdown | No | Session Fee Structure API |
| Section | Dropdown | No | Placeholder (TODO: cascading) |
| Fee Group | Dropdown | No | Session Fee Structure API |
| Fee Type | Dropdown | No | Session Fee Structure API |

### Fee Collection Report Column Wise Filters

| Filter | Type | Required | Source |
|--------|------|----------|--------|
| From Date | Date Picker | No | User input |
| To Date | Date Picker | No | User input |
| Session | Dropdown | No | Session Fee Structure API |
| Class | Dropdown | No | Session Fee Structure API |
| Section | Dropdown | No | Placeholder (TODO: cascading) |
| Fee Type | Dropdown | No | Session Fee Structure API |

---

## 🔧 Key Methods

### TypeWiseBalanceReportActivity

```java
// Load filter options from API
loadFilterOptions()

// Parse API response
parseFilterOptions(String response)

// Setup dropdown spinners
setupSessionSpinner()
setupClassSpinner()
setupSectionSpinner()
setupFeeGroupSpinner()
setupFeeTypeSpinner()

// Fetch report data
fetchTypeWiseBalanceReport()

// Parse report response
parseReportResponse(String response)

// UI state management
showLoading()
hideLoading()
showContent()
showNoData()
```

### FeeCollectionReportColumnWiseActivity

```java
// Show date picker dialog
showDatePicker(boolean isFromDate)

// Load filter options from API
loadFilterOptions()

// Parse API response
parseFilterOptions(String response)

// Setup dropdown spinners
setupSessionSpinner()
setupClassSpinner()
setupSectionSpinner()
setupFeeTypeSpinner()

// Fetch report data
fetchFeeCollectionReport()

// Parse report response
parseReportResponse(String response)

// UI state management
showLoading()
hideLoading()
showContent()
showNoData()
```

---

## 🎨 UI Components

### Type Wise Balance Report Layout

```xml
<!-- Action Bar -->
<FrameLayout android:id="@+id/actionBar" />

<!-- Filters Card -->
<CardView android:id="@+id/filters_card">
  <Spinner android:id="@+id/session_spinner" />
  <Spinner android:id="@+id/class_spinner" />
  <Spinner android:id="@+id/section_spinner" />
  <Spinner android:id="@+id/fee_group_spinner" />
  <Spinner android:id="@+id/fee_type_spinner" />
  <Button android:id="@+id/generate_report_button" />
</CardView>

<!-- Loading/Content -->
<ProgressBar android:id="@+id/progressBar" />
<LinearLayout android:id="@+id/nodata_layout" />
<RecyclerView android:id="@+id/report_content_recyclerView" />
```

### Fee Collection Report Column Wise Layout

```xml
<!-- Action Bar -->
<FrameLayout android:id="@+id/actionBar" />

<!-- Filters Card -->
<CardView android:id="@+id/filters_card">
  <EditText android:id="@+id/from_date_edit_text" />
  <EditText android:id="@+id/to_date_edit_text" />
  <Spinner android:id="@+id/session_spinner" />
  <Spinner android:id="@+id/class_spinner" />
  <Spinner android:id="@+id/section_spinner" />
  <Spinner android:id="@+id/fee_type_spinner" />
  <Button android:id="@+id/generate_report_button" />
</CardView>

<!-- Loading/Content -->
<ProgressBar android:id="@+id/progressBar" />
<LinearLayout android:id="@+id/nodata_layout" />
<RecyclerView android:id="@+id/report_content_recyclerView" />
```

---

## 📊 Data Classes

```java
private static class SessionData {
    String id;
    String name;
}

private static class ClassData {
    String id;
    String name;
}

private static class SectionData {
    String id;
    String name;
}

private static class FeeGroupData {
    String id;
    String name;
}

private static class FeeTypeData {
    String id;
    String name;
    String code;
}
```

---

## ✅ Testing Checklist

### Type Wise Balance Report

- [ ] Navigate to report from Finance category
- [ ] Verify all 5 dropdowns load with data
- [ ] Test with no filters selected
- [ ] Test with Session filter only
- [ ] Test with Class filter only
- [ ] Test with Section filter only
- [ ] Test with Fee Group filter only
- [ ] Test with Fee Type filter only
- [ ] Test with multiple filters combined
- [ ] Verify loading state shows
- [ ] Verify no data state shows when appropriate
- [ ] Verify theme color is applied
- [ ] Test back button navigation

### Fee Collection Report Column Wise

- [ ] Navigate to report from Finance category
- [ ] Verify date pickers open on click
- [ ] Verify date format displays correctly (dd-MM-yyyy)
- [ ] Verify all 4 dropdowns load with data
- [ ] Test with no filters selected
- [ ] Test with date range only
- [ ] Test with Session filter only
- [ ] Test with Class filter only
- [ ] Test with Section filter only
- [ ] Test with Fee Type filter only
- [ ] Test with all filters combined
- [ ] Verify loading state shows
- [ ] Verify no data state shows when appropriate
- [ ] Verify theme color is applied
- [ ] Test back button navigation

---

## 🐛 Common Issues & Solutions

### Issue: Dropdowns not populating
**Solution:** Check API endpoint is accessible and returns correct data format

### Issue: Date picker not showing
**Solution:** Verify EditText fields have `focusable="false"` and `clickable="true"`

### Issue: Theme color not applied
**Solution:** Check `primaryColour` is stored in SharedPreferences

### Issue: Report not generating
**Solution:** Check API endpoint constants in Constants.java are correct

### Issue: Section dropdown empty
**Solution:** This is expected - cascading logic needs to be implemented

---

## 🔄 Future Enhancements

1. ✅ ~~**Cascading Sections:** Implement logic to load sections based on selected session/class~~ **COMPLETED**
2. **Report Data Display:** Create model and adapter classes to display actual report data
3. **Export Functionality:** Add PDF and Excel export options
4. **Print Functionality:** Add print option for reports
5. **Data Caching:** Cache filter options to reduce API calls
6. **Search Functionality:** Add search/filter within report results
7. **Sorting:** Add column sorting for report data
8. **Summary Cards:** Add summary statistics at the top of reports

---

## 📝 Important Notes

1. **All filters are optional** - Reports work with any combination of filters
2. **Date format** - Display uses dd-MM-yyyy, API uses yyyy-MM-dd
3. **Section dropdown** - ✅ **FIXED** - Now fully functional with cascading logic (see `SECTION_DROPDOWN_FIX_DOCUMENTATION.md`)
4. **API integration** - Make sure backend APIs are implemented
5. **Theme support** - Activities automatically apply school theme color
6. **Loading states** - All states (loading, no data, content) are handled
7. **Error handling** - Network errors and parse errors are handled gracefully

---

## 🎓 Code Pattern

Both reports follow this pattern:

```java
1. onCreate() → Initialize views and setup listeners
2. loadFilterOptions() → Load dropdown data from API
3. parseFilterOptions() → Parse API response
4. setupXXXSpinner() → Setup each dropdown
5. generateReport() → User clicks button
6. fetchXXXReport() → Call report API with filters
7. parseReportResponse() → Parse and display data
```

This ensures consistency and maintainability across all reports.

---

## 📞 Support

For issues or questions:
1. Check API endpoints are accessible
2. Verify Constants.java has correct endpoint URLs
3. Check AndroidManifest.xml has activity registrations
4. Review logs for error messages
5. Verify SharedPreferences has required data (apiUrl, primaryColour)

