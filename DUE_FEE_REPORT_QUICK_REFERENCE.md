# Due Fee Report - Quick Reference Card

## 🚀 Quick Start

### Navigation
```
Teacher Dashboard → Reports → Finance → Total Balance Fees Statement
```

### Files Location
```
Activity:  app/src/main/java/com/qdocs/ssre241123/teachers/DueFeeReportActivity.java
Model:     app/src/main/java/com/qdocs/ssre241123/model/DueFeeReportModel.java
Adapter:   app/src/main/java/com/qdocs/ssre241123/adapters/DueFeeReportAdapter.java
Layout:    app/src/main/res/layout/item_due_fee_report.xml
```

---

## 📡 API Reference

### Endpoint
```
POST /api/due-fees-report/filter
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
  "class_id": "1",
  "section_id": "2",
  "session_id": "25"
}
```

### Response
```json
{
  "status": 1,
  "message": "Due fees report retrieved successfully",
  "total_records": 25,
  "data": [
    {
      "student_id": "123",
      "admission_no": "2024001",
      "firstname": "John",
      "lastname": "Doe",
      "class": "Class 10",
      "section": "A",
      "father_name": "Robert Doe",
      "mobileno": "9876543210",
      "fees_list": [
        {
          "fee_type": "Tuition Fee",
          "fee_code": "TF001",
          "amount": "1000.00",
          "amount_paid": "500.00",
          "amount_balance": "500.00",
          "amount_fine": "50.00",
          "amount_discount": "100.00"
        }
      ],
      "transport_fees": [...]
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
    
    // Optional filters - no validation required
    showLoading();
    fetchDueFeeReport(sessionId, classId, sectionId);
}
```

### API Call
```java
StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
    response -> {
        hideLoading();
        parseDueFeeReportResponse(response);
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
        if (classId != null) jsonBody.put("class_id", classId);
        if (sectionId != null) jsonBody.put("section_id", sectionId);
        if (sessionId != null) jsonBody.put("session_id", sessionId);
        return jsonBody.toString().getBytes("UTF-8");
    }
};
```

### Parse Response and Calculate Totals
```java
double totalAmount = 0, totalPaid = 0, totalBalance = 0;

JSONArray feesListArray = studentObj.optJSONArray("fees_list");
for (int j = 0; j < feesListArray.length(); j++) {
    JSONObject feeObj = feesListArray.getJSONObject(j);
    totalAmount += parseDouble(feeObj.optString("amount", "0"));
    totalPaid += parseDouble(feeObj.optString("amount_paid", "0"));
    totalBalance += parseDouble(feeObj.optString("amount_balance", "0"));
}

dueFee.setTotalAmount(String.format("%.2f", totalAmount));
dueFee.setTotalPaid(String.format("%.2f", totalPaid));
dueFee.setTotalBalance(String.format("%.2f", totalBalance));
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

### Balance Color Coding
```java
if (dueFee.hasDueBalance()) {
    holder.totalBalanceTv.setTextColor(
        context.getResources().getColor(android.R.color.holo_red_dark));
} else {
    holder.totalBalanceTv.setTextColor(
        context.getResources().getColor(android.R.color.holo_green_dark));
}
```

---

## 🐛 Common Issues & Solutions

### Issue: No data returned
**Solution**: 
- Check if filters are too restrictive
- Verify students have due fees
- Check API date filter settings

### Issue: Incorrect calculations
**Solution**:
- Verify all fee arrays are parsed (fees_list and transport_fees)
- Check for null values in amounts
- Review parseDouble() method

### Issue: Cards not displaying
**Solution**:
- Verify layout file exists
- Check adapter is set to RecyclerView
- Verify data list is not empty

### Issue: Theme colors not applied
**Solution**:
- Check primary color in SharedPreferences
- Verify color format (#RRGGBB)
- Check for parsing exceptions

---

## 📊 Logging

### Enable Debug Logs
```java
private static final String TAG = "DueFeeReportActivity";
Log.d(TAG, "Message");
```

### Key Log Points
```
- loadReportData() called
- API URL construction
- Request headers and body
- Response received
- Response parsing
- Fee calculations
- Data list size
- Adapter notification
```

### View Logs
```bash
adb logcat | grep DueFeeReportActivity
```

---

## ✅ Testing Checklist

- [ ] Navigation works
- [ ] Filters work (optional)
- [ ] API call succeeds
- [ ] Data displays in cards
- [ ] Fee calculations accurate
- [ ] Theme colors applied
- [ ] Balance color coding works
- [ ] Empty state works
- [ ] Error handling works
- [ ] Loading indicators work
- [ ] Back navigation works

---

## 🔗 Related Files

### Base Activity
- **TeacherReportDetailActivity** - Provides filter dropdowns and state management

### Similar Reports
- **AdmissionReportActivity** - Student admission report
- **StudentReportActivity** - Basic student report

---

## 📞 Constants Reference

```java
// API Endpoints
Constants.dueFeeReportFilterUrl = "api/due-fees-report/filter"
Constants.dueFeeReportListUrl = "api/due-fees-report/list"

// Authentication
Constants.clientService = "smartschool"
Constants.authKey = "schoolAdmin@"

// Theme Colors
Constants.primaryColour = "primaryColour"
Constants.secondaryColour = "secondaryColour"

// Currency
Constants.currency = "currency"
Constants.currency_price = "currency_price"
```

---

## 🎯 Model Helper Methods

```java
// Get full name
String fullName = dueFee.getFullName();

// Get class and section
String classSection = dueFee.getClassSection();

// Check if has due balance
boolean hasDue = dueFee.hasDueBalance();

// Get total fee items
int totalItems = dueFee.getTotalFeeItems();
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
R.id.class_section_tv
R.id.father_name_tv
R.id.mobile_no_tv
R.id.guardian_info_tv
R.id.guardian_phone_tv
R.id.total_amount_tv
R.id.total_paid_tv
R.id.total_balance_tv
R.id.total_fine_tv
R.id.total_discount_tv
R.id.fee_items_count_tv
R.id.fee_details_tv
R.id.fine_row
R.id.discount_row
```

---

## 🚀 Build & Run

### Build Command
```bash
.\gradlew.bat assembleDebug
```

### Install APK
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### Run on Device
```bash
adb shell am start -n com.qdocs.ssre241123/.teachers.DueFeeReportActivity
```

---

## 📚 Documentation Links

- **Implementation Summary**: `DUE_FEE_REPORT_IMPLEMENTATION_SUMMARY.md`
- **Testing Guide**: `DUE_FEE_REPORT_TESTING_GUIDE.md`
- **API Documentation**: `DUE_FEES_REPORT_API_FIX.md`
- **README**: `README_DUE_FEE_REPORT.md`

---

## 💡 Pro Tips

1. **Optional Filters**: Unlike other reports, this works without filters
2. **Fee Aggregation**: Automatically sums regular and transport fees
3. **Color Coding**: Use red for due, green for paid
4. **Null Handling**: Always check for null before displaying
5. **Currency**: Get from SharedPreferences for consistency
6. **Logging**: Add comprehensive logs for debugging
7. **Error Messages**: Provide user-friendly error messages
8. **Loading States**: Always show loading during API calls

---

## 🎓 Quick Commands

```bash
# View logs
adb logcat | grep DueFeeReportActivity

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

## 📊 Filter Behavior

| Filters Selected | Result |
|-----------------|--------|
| None | All students with due fees |
| Session only | Students in that session with due fees |
| Session + Class | Students in that session and class with due fees |
| Session + Class + Section | Most specific - exact combination |

---

## 🔍 Fee Calculation Logic

```
Total Amount = Sum of all fee amounts (regular + transport)
Total Paid = Sum of all paid amounts
Total Balance = Total Amount - Total Paid
Total Fine = Sum of all fine amounts
Total Discount = Sum of all discount amounts
```

---

**Last Updated**: 2025-01-10  
**Version**: 1.0  
**Status**: ✅ Production Ready

