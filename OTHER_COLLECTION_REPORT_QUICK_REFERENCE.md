# Other Collection Report - Quick Reference Card

## 🚀 Quick Start

### Access the Report
```
Teacher Dashboard → Reports → Finance → Other Collection Report
```

### Generate Report
1. Select filters (optional)
2. Click "Generate Report"
3. View results

---

## 📡 API Quick Reference

### Endpoint
```
POST /api/other-collection-report/filter
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
  "search_type": "today|this_week|this_month|last_month|this_year|period",
  "date_from": "YYYY-MM-DD",
  "date_to": "YYYY-MM-DD",
  "session_id": "string",
  "class_id": "string",
  "section_id": "string",
  "feetype_id": "string",
  "received_by": "string",
  "group": "class|collection|mode"
}
```

### Response
```json
{
  "status": 1,
  "message": "string",
  "summary": {
    "total_records": 0,
    "total_amount": "0.00"
  },
  "data": [...]
}
```

---

## 🗂️ File Locations

### Java Files
```
Activity:  app/src/main/java/com/qdocs/ssre241123/teachers/OtherCollectionReportActivity.java
Model:     app/src/main/java/com/qdocs/ssre241123/model/OtherCollectionReportModel.java
Adapter:   app/src/main/java/com/qdocs/ssre241123/adapters/OtherCollectionReportAdapter.java
```

### Layout Files
```
Activity:  app/src/main/res/layout/activity_other_collection_report.xml
Item:      app/src/main/res/layout/item_other_collection_report.xml
```

### Configuration
```
Constants: app/src/main/java/com/qdocs/ssre241123/utils/Constants.java
Manifest:  app/src/main/AndroidManifest.xml
Strings:   app/src/main/res/values/strings.xml
Routing:   app/src/main/java/com/qdocs/ssre241123/adapters/ReportItemAdapter.java
```

---

## 🔑 Key Code Snippets

### Build Request Body
```java
@Override
protected String buildRequestBody() {
    JSONObject jsonBody = new JSONObject();
    
    // Map search duration to search_type
    if (selectedSearchDuration != null) {
        String searchType = mapSearchDurationToSearchType(selectedSearchDuration);
        jsonBody.put("search_type", searchType);
    }
    
    // Add date range for custom period
    if ("custom".equals(selectedSearchDuration)) {
        jsonBody.put("date_from", selectedFromDate);
        jsonBody.put("date_to", selectedToDate);
    }
    
    // Add filters
    if (selectedSessionId != null) jsonBody.put("session_id", selectedSessionId);
    if (selectedClassId != null) jsonBody.put("class_id", selectedClassId);
    if (selectedSectionId != null) jsonBody.put("section_id", selectedSectionId);
    if (selectedFeeTypeId != null) jsonBody.put("feetype_id", selectedFeeTypeId);
    if (selectedCollectById != null) jsonBody.put("received_by", selectedCollectById);
    if (selectedGroupBy != null) jsonBody.put("group", selectedGroupBy);
    
    return jsonBody.toString();
}
```

### Parse Collection Item
```java
private OtherCollectionReportModel parseCollectionItem(JSONObject item) {
    OtherCollectionReportModel model = new OtherCollectionReportModel();
    
    // Basic fields
    model.setId(item.optString("id", ""));
    model.setAmount(item.optString("amount", "0.00"));
    model.setDate(item.optString("date", ""));
    model.setPaymentMode(item.optString("payment_mode", ""));
    
    // Parse received_byname object
    if (item.has("received_byname")) {
        JSONObject receivedByObj = item.getJSONObject("received_byname");
        model.setReceivedByName(receivedByObj.optString("name", ""));
        model.setReceivedByEmployeeId(receivedByObj.optString("employee_id", ""));
    }
    
    return model;
}
```

### Display Collector Name
```java
public String getReceivedByDisplayName() {
    if (receivedByName != null && !receivedByName.isEmpty()) {
        if (receivedByEmployeeId != null && !receivedByEmployeeId.isEmpty()) {
            return receivedByName + " (" + receivedByEmployeeId + ")";
        }
        return receivedByName;
    }
    return receivedBy != null ? receivedBy : "-";
}
```

---

## 🎯 Parameter Mapping Cheat Sheet

| UI Field | Variable | API Parameter | Notes |
|----------|----------|---------------|-------|
| Search Duration | selectedSearchDuration | search_type | Mapped via mapSearchDurationToSearchType() |
| From Date | selectedFromDate | date_from | Only for custom period |
| To Date | selectedToDate | date_to | Only for custom period |
| Session | selectedSessionId | session_id | - |
| Class | selectedClassId | class_id | - |
| Section | selectedSectionId | section_id | - |
| Fee Type | selectedFeeTypeId | feetype_id | NOT fee_type_id |
| Collect By | selectedCollectById | received_by | NOT collect_by_id |
| Group By | selectedGroupBy | group | NOT group_by |

---

## 🔄 Search Duration Mapping

| UI Value | Internal Value | API Value |
|----------|----------------|-----------|
| Today | today | today |
| This Week | week | this_week |
| This Month | month | this_month |
| This Year | year | this_year |
| Custom Duration | custom | period |

---

## 📊 Model Fields Quick Reference

### Student Info
- firstname, middlename, lastname
- admissionNo, studentId
- classId, className, sectionId, section

### Fee Info
- type, code, name
- feeGroupsFeetypeId
- isSystem

### Payment Info
- amount, amountDiscount, amountFine
- description, paymentMode
- date, invNo

### Collector Info
- receivedBy (ID)
- receivedByName (Name)
- receivedByEmployeeId (Employee ID)

---

## 🐛 Common Issues & Quick Fixes

### Issue: No data displayed
```
✓ Check if test data exists
✓ Verify date range includes data
✓ Check Logcat for API response
✓ Verify filters are correct
```

### Issue: Wrong parameter names
```
✓ Use feetype_id (not fee_type_id)
✓ Use received_by (not collect_by_id)
✓ Use group (not group_by)
```

### Issue: Date not formatted
```
✓ Use 'date' field (not created_at)
✓ Check formatDate() method
✓ Verify SimpleDateFormat patterns
```

### Issue: Collector name not showing
```
✓ Check received_byname object parsing
✓ Verify getReceivedByDisplayName() method
✓ Check if API returns received_byname
```

---

## 🔍 Debugging Commands

### View Request Body
```
adb logcat -s OtherCollectionReport:D | grep "Request Body"
```

### View API Response
```
adb logcat -s OtherCollectionReport:D | grep "Response"
```

### View Summary
```
adb logcat -s OtherCollectionReport:D | grep "Summary"
```

### View All Logs
```
adb logcat -s OtherCollectionReport:D
```

---

## 📱 UI Component IDs

### Activity Layout
```xml
R.id.searchDurationSpinner
R.id.fromDateEditText
R.id.toDateEditText
R.id.sessionSpinner
R.id.classSpinner
R.id.sectionSpinner
R.id.feeTypeSpinner
R.id.collectBySpinner
R.id.groupBySpinner
R.id.generateReportButton
R.id.summaryCard
R.id.totalRecordsTv
R.id.totalAmountTv
R.id.progressBar
R.id.nodataLayout
R.id.reportContentRecyclerView
```

### Item Layout
```xml
R.id.card_view
R.id.student_name_tv
R.id.admission_no_tv
R.id.class_sec_tv
R.id.fee_type_tv
R.id.fee_group_tv
R.id.amount_tv
R.id.payment_date_tv
R.id.payment_mode_tv
R.id.received_by_tv
R.id.details_tv
```

---

## 🎨 Theme Colors

```java
// Get primary color
String primaryColor = Utility.getSharedPreferences(context, Constants.primaryColour);

// Apply to views
amountTv.setTextColor(Color.parseColor(primaryColor));
paymentModeTv.setTextColor(Color.parseColor(primaryColor));
```

---

## 📚 Related Documentation

- **Complete Implementation:** OTHER_COLLECTION_REPORT_API_IMPLEMENTATION_COMPLETE.md
- **Testing Guide:** OTHER_COLLECTION_REPORT_TESTING_GUIDE.md
- **Architecture:** OTHER_COLLECTION_REPORT_ARCHITECTURE.md
- **Final Summary:** OTHER_COLLECTION_REPORT_FINAL_SUMMARY.md

---

## ✅ Quick Checklist

Before deploying:
- [ ] All filters working
- [ ] API request correct
- [ ] Response parsed correctly
- [ ] Summary displayed
- [ ] Records displayed
- [ ] Dates formatted
- [ ] Currency formatted
- [ ] Collector name with ID
- [ ] Empty state works
- [ ] Loading state works
- [ ] Back button works
- [ ] No crashes

---

## 🚀 Deployment Steps

1. **Build APK**
   ```
   ./gradlew assembleDebug
   ```

2. **Test on Device**
   ```
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

3. **Verify Functionality**
   - Open report
   - Apply filters
   - Generate report
   - Check data display

4. **Deploy to Production**
   ```
   ./gradlew assembleRelease
   ```

---

## 📞 Quick Support

**For Issues:**
1. Check Logcat
2. Review testing guide
3. Check API response
4. Verify test data

**For Questions:**
1. Review documentation
2. Check code comments
3. Review architecture
4. Check API spec

---

**Status: ✅ READY FOR USE**

