# Income Group Report - Quick Reference Card

## 📋 Overview
Income Group Report allows filtering and viewing income records by time period and income head category.

---

## 🔗 API Endpoints

### List API (Load Filters)
```
POST /income-group-report/list
Headers: Client-Service: smartschool, Auth-Key: schoolAdmin@
Body: {}
```

### Filter API (Generate Report)
```
POST /income-group-report/filter
Headers: Client-Service: smartschool, Auth-Key: schoolAdmin@
Body: {
  "search_type": "this_month",  // optional
  "date_from": "2025-01-01",    // optional
  "date_to": "2025-12-31",      // optional
  "head": "1"                   // optional
}
```

---

## 🎯 Search Types

| Display Name | API Key | Description |
|--------------|---------|-------------|
| Today | `today` | Current day |
| This Week | `this_week` | Current week |
| This Month | `this_month` | Current month |
| Last Month | `last_month` | Previous month |
| This Year | `this_year` | Current year |
| Custom Period | `period` | Custom date range |

---

## 📊 Request Examples

### 1. Today's Income
```json
{
  "search_type": "today"
}
```

### 2. This Month's Income
```json
{
  "search_type": "this_month"
}
```

### 3. Custom Date Range
```json
{
  "date_from": "2025-01-01",
  "date_to": "2025-06-30"
}
```

### 4. Specific Income Head
```json
{
  "search_type": "this_year",
  "head": "1"
}
```

### 5. All Filters Combined
```json
{
  "date_from": "2025-01-01",
  "date_to": "2025-12-31",
  "head": "2"
}
```

---

## 📥 Response Structure

### Success Response
```json
{
  "status": 1,
  "message": "Income group report retrieved successfully",
  "summary": {
    "total_records": 8,
    "total_amount": "57500.00"
  },
  "data": [
    {
      "id": "1",
      "name": "Student Fee Payment",
      "invoice_no": "INV001",
      "date": "2025-01-15",
      "amount": "5000.00",
      "income_category": "Fees Collection",
      "head_id": "1",
      "note": "Monthly fee payment"
    }
  ]
}
```

### Error Response
```json
{
  "status": 0,
  "message": "Unauthorized access"
}
```

---

## 🔑 Key Field Mappings

### Request Parameters
| Old Name | New Name | Type | Required |
|----------|----------|------|----------|
| `income_head_id` | `head` | string | No |
| `search_type` | `search_type` | string | No |
| `date_from` | `date_from` | string | No |
| `date_to` | `date_to` | string | No |

### Response Fields
| Old Name | New Name | Type | Description |
|----------|----------|------|-------------|
| `income_head` | `income_category` | string | Income category name |
| `income_head_id` | `head_id` | string | Income head ID |

---

## 🎨 UI Components

### Filters
1. **Search Type Spinner** - 6 options (Today to Custom Period)
2. **Income Head Spinner** - "All" + loaded income heads
3. **Date Range Layout** - From/To date pickers (shown for Custom Period)
4. **Generate Report Button** - Triggers API call

### Results Display
1. **Summary Card** - Total records and total amount
2. **RecyclerView** - List of income records
3. **Progress Bar** - Loading indicator
4. **No Data Layout** - Empty state

---

## ✅ Validation Rules

### Custom Period
- Both dates required
- From date cannot be after To date
- Date format: yyyy-MM-dd

### Network
- Internet connection required
- Shows error if offline

---

## 🎯 User Flow

```
1. Open Income Group Report
   ↓
2. Select Search Type (e.g., "This Month")
   ↓
3. Select Income Head (optional, default "All")
   ↓
4. If Custom Period: Select date range
   ↓
5. Click "Generate Report"
   ↓
6. View Results:
   - Summary card with totals
   - List of income records
```

---

## 🔍 Testing Quick Checks

### ✅ Functionality
- [ ] All 6 search types work
- [ ] Income heads load from API
- [ ] Custom period shows date pickers
- [ ] Generate button triggers API
- [ ] Results display correctly
- [ ] Summary shows correct totals

### ✅ Validation
- [ ] Custom period requires both dates
- [ ] Date range validation works
- [ ] Network error handled
- [ ] API error handled

### ✅ UI/UX
- [ ] Theme color applied
- [ ] Loading state shows
- [ ] Empty state shows
- [ ] Date format correct (dd MMM yyyy)
- [ ] Amount format correct (with currency)

---

## 🐛 Common Issues

### Issue: Income heads not loading
**Check:** API endpoint and response format
**Solution:** Verify `/income-group-report/list` endpoint

### Issue: Empty results always shown
**Check:** Response parsing and field names
**Solution:** Verify `income_category` and `head_id` fields

### Issue: Filter not working
**Check:** Request parameter name
**Solution:** Use `head` not `income_head_id`

---

## 📝 Code Locations

### Activity
```
app/src/main/java/com/qdocs/ssre241123/teachers/
  └── IncomeGroupReportActivity.java
```

### Model
```
app/src/main/java/com/qdocs/ssre241123/model/
  ├── IncomeReportModel.java
  └── IncomeHeadModel.java
```

### Adapter
```
app/src/main/java/com/qdocs/ssre241123/adapters/
  └── IncomeReportAdapter.java
```

### Layout
```
app/src/main/res/layout/
  ├── activity_income_group_report.xml
  └── item_income_report.xml
```

### Constants
```
app/src/main/java/com/qdocs/ssre241123/utils/
  └── Constants.java
      ├── incomeGroupReportFilterUrl
      └── incomeGroupReportListUrl
```

---

## 🔧 Key Methods

### IncomeGroupReportActivity

| Method | Purpose |
|--------|---------|
| `loadIncomeHeads()` | Load income heads from list API |
| `parseIncomeHeadResponse()` | Parse list API response |
| `fetchIncomeGroupReport()` | Call filter API with parameters |
| `parseIncomeReportResponse()` | Parse filter API response |
| `validateInput()` | Validate user input |
| `updateSummary()` | Update summary card |

---

## 📊 Data Flow

```
Activity Start
    ↓
loadIncomeHeads()
    ↓
POST /income-group-report/list
    ↓
parseIncomeHeadResponse()
    ↓
Populate Income Head Spinner
    ↓
User Selects Filters
    ↓
Click Generate Report
    ↓
validateInput()
    ↓
fetchIncomeGroupReport()
    ↓
POST /income-group-report/filter
    ↓
parseIncomeReportResponse()
    ↓
Update UI:
  - Summary Card
  - RecyclerView
```

---

## 🎨 Theme Integration

### Colors Applied
- Action bar background
- Generate button background
- Amount text color in list items

### Source
```java
String primaryColor = Utility.getSharedPreferences(
    context, 
    Constants.primaryColour
);
```

---

## 📱 Screen States

### 1. Initial State
- Filters visible
- Results hidden
- No data hidden
- Progress hidden

### 2. Loading State
- Progress visible
- Results hidden
- No data hidden

### 3. Results State
- Progress hidden
- Summary visible
- Results visible
- No data hidden

### 4. Empty State
- Progress hidden
- Summary hidden
- Results hidden
- No data visible

---

## 🚀 Quick Start

1. Navigate to: **Reports → Finance → Income Group Report**
2. Select search type (default: Today)
3. Select income head (default: All)
4. Click **Generate Report**
5. View results

---

## 📞 Support

For issues or questions:
1. Check logs with tag: `IncomeGroupReport`
2. Verify API endpoints are accessible
3. Check authentication headers
4. Review response format

---

## ✨ Features

- ✅ 6 search type options
- ✅ Dynamic income head loading
- ✅ Custom date range picker
- ✅ Summary with totals
- ✅ Formatted amounts and dates
- ✅ Theme color integration
- ✅ Error handling
- ✅ Loading states
- ✅ Empty state handling
- ✅ Input validation

---

## 📅 Version

**Implementation Date:** October 11, 2025  
**API Version:** 1.0  
**Status:** ✅ Complete

---

## 🔗 Related Documentation

- `INCOME_GROUP_REPORT_API_IMPLEMENTATION.md` - Full implementation details
- `INCOME_GROUP_REPORT_TESTING_GUIDE.md` - Comprehensive testing guide
- `INCOME_GROUP_REPORT_CHANGES_SUMMARY.md` - Detailed changes summary

