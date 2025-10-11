# Total Student Academic Report - Quick Reference Card

## 🚀 Quick Start

### Location in App
```
Reports → Finance → Total Balance Fees Report (3rd item)
```

### API Endpoint
```
POST /api/total-student-academic-report/filter
```

### Key Files
```
Model:    TotalStudentAcademicReportModel.java
Adapter:  TotalStudentAcademicReportAdapter.java
Activity: TotalBalanceFeesReportActivity.java
Layout:   item_total_student_academic_report.xml
```

---

## 📊 Data Structure

### Request
```json
{
    "session_id": "1",    // Optional
    "class_id": "1",      // Optional
    "section_id": "1"     // Optional
}
```

### Response
```json
{
    "status": 1,
    "data": [
        {
            "name": "Student Name",
            "class": "Class 1",
            "section": "A",
            "admission_no": "ADM001",
            "roll_no": "001",
            "father_name": "Mr. Doe",
            "total_fee": "10000.00",
            "deposit": "7000.00",
            "discount": "500.00",
            "fine": "100.00",
            "balance": "2600.00"
        }
    ]
}
```

---

## 🔧 Key Components

### Model Class
```java
TotalStudentAcademicReportModel
├── Student Info: name, className, section, admissionNo, rollNo, fatherName
├── Fee Summary: totalFee, deposit, discount, fine, balance
└── Helpers: getClassSection(), getTotalFeeDouble(), etc.
```

### Adapter Class
```java
TotalStudentAcademicReportAdapter
├── Currency formatting
├── Number formatting
├── Theme color integration
└── Color-coded balance (red/green)
```

### Activity Class
```java
TotalBalanceFeesReportActivity extends BaseFinanceReportActivity
├── Inherits: Session, Class, Section filters
├── Implements: parseReportResponse()
└── Manages: RecyclerView, Adapter, Data
```

---

## 🎨 UI Components

### Student Card Layout
```
┌─────────────────────────────┐
│ [Header - Theme Color]      │
│ Name, Adm No, Roll No       │
│ Class, Section, Father      │
├─────────────────────────────┤
│ Total Fee:    ₹ 10,000.00   │
│ Deposit:      ₹  7,000.00   │
│ Discount:     ₹    500.00   │
│ Fine:         ₹    100.00   │
│ ─────────────────────────   │
│ Balance:      ₹  2,600.00   │
└─────────────────────────────┘
```

### Color Coding
- **Balance > 0:** 🔴 Red (Due)
- **Balance ≤ 0:** 🟢 Green (Paid)
- **Header:** Theme primary color

---

## 💻 Code Snippets

### Parse Response
```java
JSONArray dataArray = jsonResponse.optJSONArray("data");
for (int i = 0; i < dataArray.length(); i++) {
    JSONObject obj = dataArray.getJSONObject(i);
    TotalStudentAcademicReportModel student = new TotalStudentAcademicReportModel();
    student.setName(obj.optString("name", ""));
    student.setBalance(obj.optString("balance", "0.00"));
    // ... set other fields
    studentList.add(student);
}
adapter.updateData(studentList);
```

### Format Currency
```java
NumberFormat numberFormat = NumberFormat.getInstance(new Locale("en", "IN"));
String formatted = currency + " " + numberFormat.format(amount);
```

### Color Balance
```java
if (balance > 0) {
    holder.balanceTv.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
} else {
    holder.balanceTv.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
}
```

---

## 🔍 Debugging

### LogCat Tags
```
TotalBalanceFeesReport    - Activity logs
BaseFinanceReport         - Base class logs
Volley                    - Network logs
```

### Common Issues

**Issue:** Filters not loading
```
Check: API endpoint, network, authentication
```

**Issue:** Empty results
```
Check: Filter parameters, API response format, JSON parsing
```

**Issue:** Balance not color-coded
```
Check: Balance value is numeric, color resources exist
```

**Issue:** Crash on opening
```
Check: Views exist in layout, adapter initialized, RecyclerView setup
```

---

## 🧪 Quick Test

### Test Command (cURL)
```bash
curl -X POST "http://your-server/api/total-student-academic-report/filter" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d '{"class_id":"1"}'
```

### Expected Result
```json
{
    "status": 1,
    "message": "Success",
    "total_records": 25,
    "data": [...]
}
```

---

## 📱 Testing Checklist

### Quick Tests
- [ ] Open report (no crash)
- [ ] Load filters (spinners populate)
- [ ] Generate report (data displays)
- [ ] Scroll list (smooth)
- [ ] Back button (returns to finance)

### Data Tests
- [ ] No filters (all students)
- [ ] Session filter (filtered students)
- [ ] Class filter (filtered students)
- [ ] All filters (filtered students)
- [ ] Empty result (no data message)

### UI Tests
- [ ] Theme colors applied
- [ ] Currency formatted
- [ ] Balance color-coded
- [ ] Cards display correctly
- [ ] Text readable

---

## 🔗 Related Files

### Similar Reports
```
BalanceFeesReportActivity.java
DueFeeReportActivity.java
YearReportDueFeeActivity.java
```

### Base Classes
```
BaseFinanceReportActivity.java
```

### Adapters
```
StudentAcademicReportAdapter.java (different structure)
DueFeeReportAdapter.java
```

---

## 📚 Documentation

### Full Documentation
- `TOTAL_STUDENT_ACADEMIC_REPORT_IMPLEMENTATION.md` - Complete guide
- `TOTAL_STUDENT_ACADEMIC_REPORT_TESTING_GUIDE.md` - Testing guide
- `TOTAL_STUDENT_ACADEMIC_REPORT_SUMMARY.md` - Summary
- `TOTAL_STUDENT_ACADEMIC_REPORT_CHECKLIST.md` - Checklist

### API Documentation
- See initial request for complete API documentation

---

## 🎯 Key Points

### Architecture
- Extends `BaseFinanceReportActivity`
- Uses RecyclerView with custom adapter
- Model-View-Adapter pattern

### Features
- Session, Class, Section filters
- Graceful empty filter handling
- Color-coded balance display
- Currency and number formatting
- Theme color integration

### API
- POST request with JSON body
- Optional filters (all nullable)
- Returns array of student objects
- Status code indicates success/failure

---

## 🚨 Important Notes

1. **API Endpoint:** Uses `totalStudentAcademicReportFilterUrl` constant
2. **Filters:** All optional, empty request returns all students
3. **Balance Calculation:** (Total Fee - Discount) - Deposit + Fine
4. **Color Coding:** Red for due, green for paid/zero
5. **Currency:** From app settings, defaults to ₹

---

## 📞 Support

### For Help
- Check full documentation
- Review similar reports
- Check LogCat for errors
- Test API with cURL

### For Issues
- Check implementation checklist
- Run through testing guide
- Verify API response format
- Check base class behavior

---

**Quick Reference Version:** 1.0  
**Last Updated:** October 11, 2025  
**Status:** Ready for Testing

