# Expense Group Report - Quick Reference Card

## 📋 Overview
Expense Group Report allows filtering and viewing expense records by time period and expense head category.

---

## 🔗 API Endpoints

### List API (Load Filters)
```
POST /expense-group-report/list
Headers: Client-Service: smartschool, Auth-Key: schoolAdmin@
Body: {}
```

### Filter API (Generate Report)
```
POST /expense-group-report/filter
Headers: Client-Service: smartschool, Auth-Key: schoolAdmin@
Body: {
  "search_type": "this_month",  // optional
  "date_from": "2025-01-01",    // optional
  "date_to": "2025-12-31",      // optional
  "head_id": 1                  // optional
}
```

---

## 🎯 Search Types (11 Options)

| Display Name | API Key | Description |
|--------------|---------|-------------|
| Today | `today` | Current day |
| This Week | `this_week` | Current week |
| Last Week | `last_week` | Previous week |
| This Month | `this_month` | Current month |
| Last Month | `last_month` | Previous month |
| Last 3 Months | `last_3_month` | Last 3 months |
| Last 6 Months | `last_6_month` | Last 6 months |
| Last 12 Months | `last_12_month` | Last 12 months |
| This Year | `this_year` | Current year |
| Last Year | `last_year` | Previous year |
| Custom Period | `period` | Custom date range |

---

## 📊 Request Examples

### 1. Today's Expenses
```json
{
  "search_type": "today"
}
```

### 2. Last 3 Months
```json
{
  "search_type": "last_3_month"
}
```

### 3. Custom Date Range
```json
{
  "date_from": "2025-01-01",
  "date_to": "2025-06-30"
}
```

### 4. Specific Expense Head
```json
{
  "search_type": "this_year",
  "head_id": 1
}
```

---

## 📥 Response Structure

### Success Response
```json
{
  "status": 1,
  "message": "Expense group report retrieved successfully",
  "summary": {
    "total_expenses": 15,
    "total_amount": "125,500.00"
  },
  "data": [
    {
      "id": "1",
      "date": "2025-01-15",
      "name": "Office Supplies",
      "invoice_no": "INV-001",
      "amount": "5000.00",
      "exp_category": "Stationery Purchase",
      "exp_head_id": "1",
      "note": "Monthly office supplies"
    }
  ]
}
```

---

## 🔑 Key Field Mappings

### Request Parameters
| Old Name | New Name | Type | Required |
|----------|----------|------|----------|
| `expense_head_id` | `head_id` | integer | No |
| `search_type` | `search_type` | string | No |
| `date_from` | `date_from` | string | No |
| `date_to` | `date_to` | string | No |

### Response Fields
| Field | Type | Description |
|-------|------|-------------|
| `total_expenses` | integer | Total expense count |
| `total_amount` | string | Total amount (may have commas) |
| `exp_category` | string | Expense category name |
| `exp_head_id` | string | Expense head ID |

---

## ✅ Build Status

```
BUILD SUCCESSFUL in 22s
29 actionable tasks: 9 executed, 20 up-to-date
```

✅ **Compilation:** No errors
✅ **Implementation:** Complete
✅ **Ready for Testing**

---

## 🎨 UI Components

### Filters
1. **Search Type Spinner** - 11 options (Today to Custom Period)
2. **Expense Head Spinner** - "All" + loaded expense heads
3. **Date Range Layout** - From/To date pickers (shown for Custom Period)
4. **Generate Report Button** - Triggers API call

### Results Display
1. **Summary Card** - Total expenses and total amount
2. **RecyclerView** - List of expense records
3. **Progress Bar** - Loading indicator
4. **No Data Layout** - Empty state

---

## 🔍 Testing Quick Checks

### ✅ Functionality
- [ ] All 11 search types work
- [ ] Expense heads load from API
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
- [ ] Red color applied to amounts
- [ ] Loading state shows
- [ ] Empty state shows
- [ ] Date format correct (dd MMM yyyy)
- [ ] Amount format correct (with currency)

---

## 🐛 Common Issues

### Issue: Expense heads not loading
**Check:** API endpoint and response format
**Solution:** Verify `/expense-group-report/list` endpoint

### Issue: Summary amounts incorrect
**Check:** Comma parsing in amounts
**Solution:** Verify comma removal before parsing

### Issue: Filter not working
**Check:** Request parameter name
**Solution:** Use `head_id` not `expense_head_id`

---

## 📝 Code Locations

### Activity
```
app/src/main/java/com/qdocs/ssre241123/teachers/
  └── ExpenseGroupReportActivity.java
```

### Model
```
app/src/main/java/com/qdocs/ssre241123/model/
  ├── ExpenseReportModel.java
  └── ExpenseHeadModel.java
```

### Adapter
```
app/src/main/java/com/qdocs/ssre241123/adapters/
  └── ExpenseReportAdapter.java
```

### Constants
```
app/src/main/java/com/qdocs/ssre241123/utils/
  └── Constants.java
      ├── expenseGroupReportFilterUrl
      └── expenseGroupReportListUrl
```

---

## 🚀 Quick Start

1. Navigate to: **Reports → Finance → Expense Group Report**
2. Select search type (default: Today)
3. Select expense head (default: All)
4. Click **Generate Report**
5. View results

---

## 📊 Data Flow

```
Activity Start
    ↓
loadExpenseHeads()
    ↓
POST /expense-group-report/list
    ↓
parseExpenseHeadResponse()
    ↓
Populate Expense Head Spinner
    ↓
User Selects Filters
    ↓
Click Generate Report
    ↓
validateInput()
    ↓
fetchExpenseGroupReport()
    ↓
POST /expense-group-report/filter
    ↓
parseExpenseReportResponse()
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
- Amount text color (RED for expenses)

---

## ✨ Features

- ✅ 11 search type options
- ✅ Dynamic expense head loading
- ✅ Custom date range picker
- ✅ Summary with totals
- ✅ Formatted amounts and dates
- ✅ Red color for expense amounts
- ✅ Error handling
- ✅ Loading states
- ✅ Empty state handling
- ✅ Input validation
- ✅ Comma-formatted amount parsing

---

## 📅 Version

**Implementation Date:** October 11, 2025  
**API Version:** 1.0  
**Status:** ✅ Complete & Built

---

## 🔗 Related Documentation

- `EXPENSE_GROUP_REPORT_API_IMPLEMENTATION.md` - Full implementation details
- `INCOME_GROUP_REPORT_API_IMPLEMENTATION.md` - Similar implementation for income

