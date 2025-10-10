# Year Report Due Fee - Quick Reference Guide

## 🎯 Quick Overview

**Feature**: Balance Fees Statement (Year Report)  
**Location**: Reports → Finance → Balance Fees Statement  
**Purpose**: Show students with due fees for the entire year (up to December 31st)  
**Status**: ✅ Complete and Ready to Test

---

## 📋 Key Information

### **Report Identification**
- **Report ID**: `balance_fees_statement`
- **Activity**: `YearReportDueFeeActivity`
- **Adapter**: `YearReportDueFeeAdapter`
- **Model**: `DueFeeReportModel` (reused)
- **Layout**: `item_due_fee_report.xml` (reused)

### **API Details**
- **Endpoint**: `POST /api/year-report-due-fees/filter`
- **Base URL**: From shared preferences (`apiUrl`)
- **Headers**:
  - `Client-Service: smartschool`
  - `Auth-Key: schoolAdmin@`
  - `Content-Type: application/json`

### **Filters** (All Optional)
- Session ID
- Class ID
- Section ID

---

## 🔑 Key Differences from Regular Due Fees Report

| Aspect | Regular Due Fees | Year Report Due Fees |
|--------|-----------------|---------------------|
| **Report ID** | `total_balance_fees_statement` | `balance_fees_statement` |
| **Activity** | `DueFeeReportActivity` | `YearReportDueFeeActivity` |
| **API Endpoint** | `/api/due-fees-report/filter` | `/api/year-report-due-fees/filter` |
| **Due Date** | Current date | December 31st of current year |
| **Purpose** | Current status | Year-end reporting |

---

## 📊 Fee Calculation Formula

```
For Each Fee Item:
  feeAmount = from "amount" field
  paidAmount = sum of all payments in "amount_detail"
  balanceAmount = feeAmount - paidAmount
  fineAmount = sum of all fines in "amount_detail"
  discountAmount = sum of all discounts in "amount_detail"

For Each Student:
  totalAmount = sum of all feeAmount
  totalPaid = sum of all paidAmount
  totalBalance = sum of all balanceAmount
  totalFine = sum of all fineAmount
  totalDiscount = sum of all discountAmount
```

---

## 🎨 UI Components

### **Card Header** (Theme Color)
- Student Name
- Admission Number
- Class - Section

### **Contact Information**
- Father Name
- Mobile Number (with 📱 icon)
- Guardian Name
- Guardian Phone (with 📞 icon)

### **Fee Summary**
- Total Amount
- Total Paid
- Total Balance (color-coded: red for due, green for paid)
- Total Fine (shown only if > 0)
- Total Discount (shown only if > 0)

### **Fee Details**
- Fee items count
- List of fee items with balance

---

## 🧪 Quick Test Steps

### **Test 1: Basic Functionality**
1. Open app
2. Navigate to Reports → Finance → Balance Fees Statement
3. Click "Generate Report" (no filters)
4. Verify students with due fees are displayed

### **Test 2: With Filters**
1. Select Session, Class, Section
2. Click "Generate Report"
3. Verify filtered results

### **Test 3: Calculations**
1. Pick any student card
2. Verify: Total Balance = Total Amount - Total Paid
3. Check that balance is red if > 0, green if = 0

---

## 🐛 Common Issues & Solutions

### **Issue 1: "Error loading year report: null"**
**Cause**: API endpoint issue  
**Solution**: Check that endpoint is `year-report-due-fees/filter` (no `api/` prefix)

### **Issue 2: Wrong calculations**
**Cause**: Not parsing `amount_detail` correctly  
**Solution**: Verify `amount_detail` is parsed as JSON and all payments are summed

### **Issue 3: No data shown**
**Cause**: No students have due fees for the year  
**Solution**: Check that fees have due dates on or before December 31st

### **Issue 4: Filters not working**
**Cause**: Incorrect filter values  
**Solution**: Verify session_id, class_id, section_id are valid

---

## 📝 Code Snippets

### **Routing Logic** (ReportItemAdapter.java)
```java
} else if ("balance_fees_statement".equals(reportItem.getId())) {
    intent = new Intent(context, YearReportDueFeeActivity.class);
}
```

### **API Endpoint** (Constants.java)
```java
public static final String yearReportDueFeeFilterUrl = "year-report-due-fees/filter";
```

### **Activity Registration** (AndroidManifest.xml)
```xml
<activity
    android:name=".teachers.YearReportDueFeeActivity"
    android:exported="false" />
```

---

## 📚 Related Files

### **New Files**
1. `app/src/main/java/com/qdocs/ssre241123/teachers/YearReportDueFeeActivity.java`
2. `app/src/main/java/com/qdocs/ssre241123/adapters/YearReportDueFeeAdapter.java`

### **Modified Files**
1. `app/src/main/java/com/qdocs/ssre241123/utils/Constants.java`
2. `app/src/main/java/com/qdocs/ssre241123/adapters/ReportItemAdapter.java`
3. `app/src/main/AndroidManifest.xml`

### **Reused Files**
1. `app/src/main/java/com/qdocs/ssre241123/model/DueFeeReportModel.java`
2. `app/src/main/res/layout/item_due_fee_report.xml`

---

## 🔍 Debugging Tips

### **Enable Logging**
Look for these log tags in Logcat:
- `YearReportDueFee` - Main activity logs
- Check for:
  - API URL
  - Request body
  - Response data
  - Calculation results

### **Check API Response**
```json
{
  "status": 1,
  "message": "Year report due fees retrieved successfully",
  "filters_applied": {
    "date": "2025-12-31"  // Should be December 31st
  },
  "data": [...]
}
```

### **Verify Calculations**
Look for these logs:
```
D/YearReportDueFee: First Fee - Type: TUITION FEE
D/YearReportDueFee: First Fee - Amount: 16000.0
D/YearReportDueFee: First Fee - Paid: 15000.0
D/YearReportDueFee: First Fee - Balance: 1000.0
```

---

## ✅ Checklist

### **Before Testing**
- [ ] APK installed on device
- [ ] Internet connection available
- [ ] API server is running
- [ ] Test data exists (students with due fees)

### **During Testing**
- [ ] Report opens without crash
- [ ] Filters load correctly
- [ ] Generate report works
- [ ] Data displays in cards
- [ ] Calculations are accurate
- [ ] Colors are correct (red/green)
- [ ] Theme colors applied

### **After Testing**
- [ ] No crashes observed
- [ ] Performance is acceptable
- [ ] UI looks professional
- [ ] Matches web version functionality

---

## 🎓 Summary

**What**: Year Report Due Fees (Balance Fees Statement)  
**Where**: Reports → Finance → Balance Fees Statement  
**When**: For year-end fee reporting (up to December 31st)  
**How**: Uses API endpoint `/api/year-report-due-fees/filter`  
**Status**: ✅ Complete and Ready

---

## 📞 Support

**Documentation Files**:
- `YEAR_REPORT_DUE_FEE_IMPLEMENTATION_SUMMARY.md` - Complete implementation details
- `YEAR_REPORT_DUE_FEE_QUICK_REFERENCE.md` - This file
- API Documentation - Provided by user

**For Issues**:
1. Check Logcat for error messages
2. Verify API endpoint and response
3. Compare with web version
4. Review calculation logic

---

**Last Updated**: 2025-01-10  
**Version**: 1.0  
**Status**: Production Ready

