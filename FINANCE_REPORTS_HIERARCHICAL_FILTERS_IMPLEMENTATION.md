# Finance Reports with Hierarchical Filters - Implementation Plan

## Overview

This document tracks the implementation of hierarchical filters for all finance reports using the new Fee Collection Filters API (`/fee-collection-filters/get`).

---

## Reports to Implement

### 1. Total Balance Fees Report ✅ In Progress
**Report ID:** `total_balance_fees_report`  
**Filters:**
- Session (dropdown - hierarchical)
- Class (dropdown - hierarchical)
- Section (dropdown - hierarchical)
- Search Type (dropdown - Paid/Unpaid/All)

**API Endpoint:** `/api/total-balance-fees-report/filter`

---

### 2. Total Fee Collection Report
**Report ID:** `total_fee_collection_report`  
**Filters:**
- Search Duration (dropdown - Today/This Week/This Month/This Year/Custom)
- From Date (date picker - enabled for custom duration)
- To Date (date picker - enabled for custom duration)
- Class (dropdown - hierarchical)
- Section (dropdown - hierarchical)
- Fee Type (dropdown)
- Collect By (dropdown)
- Group By (dropdown)

**API Endpoint:** `/api/total-fee-collection-report/filter`

---

### 3. Fees Collection Report
**Report ID:** `fees_collection_report`  
**Filters:**
- Search Duration (dropdown - Today/This Week/This Month/This Year/Custom)
- From Date (date picker)
- To Date (date picker)
- Session (dropdown - hierarchical)
- Class (dropdown - hierarchical)
- Fee Type (dropdown)
- Collected By (dropdown)
- Group By (dropdown)

**API Endpoint:** `/api/fees-collection-report/filter`

---

### 4. Other Fees Collection Report
**Report ID:** `other_fees_collection_report`  
**Filters:**
- Search Duration (dropdown - Today/This Week/This Month/This Year/Custom)
- From Date (date picker)
- To Date (date picker)
- Session (dropdown - hierarchical)
- Class (dropdown - hierarchical)
- Section (dropdown - hierarchical)
- Fee Type (dropdown)
- Collect By (dropdown)
- Group By (dropdown)

**API Endpoint:** `/api/other-fees-collection-report/filter`

---

### 5. Fee Collection Report Column Wise ✅ Already Implemented
**Report ID:** `fee_collection_report_column_wise`  
**Filters:**
- Search Duration (date range)
- Session (dropdown)
- Class (dropdown)
- Section (dropdown)
- Fee Type (dropdown)

**API Endpoint:** `/api/fee-collection-report-column-wise/filter`

**Status:** Already implemented using Session Fee Structure API. Needs to be updated to use hierarchical API.

---

### 6. Other Fee and Collection Fee Combined
**Report ID:** `other_fee_and_collection_fee_combined`  
**Filters:**
- Search Duration (dropdown - Today/This Week/This Month/This Year/Custom)
- From Date (date picker)
- To Date (date picker)
- Session (dropdown - hierarchical)
- Class (dropdown - hierarchical)
- Section (dropdown - hierarchical)
- Fee Type (dropdown)
- Collect By (dropdown)
- Group By (dropdown)

**API Endpoint:** `/api/other-fee-and-collection-fee-combined/filter`

---

### 7. Balance Fees Report
**Report ID:** `balance_fees_report`  
**Filters:**
- Session (dropdown - hierarchical)
- Class (dropdown - hierarchical)
- Section (dropdown - hierarchical)
- Search Type (dropdown - Paid/Unpaid/All)

**API Endpoint:** `/api/balance-fees-report/filter`

---

## Implementation Strategy

### Phase 1: Base Infrastructure ✅ Complete
1. ✅ Add API constants to `Constants.java`
2. ✅ Create `BaseFinanceReportActivity.java` with:
   - Hierarchical filter loading from `/fee-collection-filters/get`
   - Session → Class → Section cascading dropdowns
   - Fee Type, Fee Group, Collect By, Group By dropdowns
   - Search Duration with date range handling
   - Search Type (Paid/Unpaid/All) dropdown
   - Common request building and API calling logic

### Phase 2: Individual Report Activities
Each report activity will:
1. Extend `BaseFinanceReportActivity`
2. Override `getLayoutResourceId()` to provide layout
3. Override `getReportTitle()` to provide title
4. Override `getReportApiUrl()` to provide API endpoint
5. Override `setupSpecificFilters()` to show/hide specific filters
6. Override `parseReportResponse()` to handle report data

### Phase 3: Layouts
Create layouts for each report with only the required filters:
- Common elements: Action bar, progress bar, no data layout, RecyclerView
- Variable elements: Filter spinners and date pickers based on report requirements

### Phase 4: Routing
Update `ReportItemAdapter.java` to route to the correct activity for each report ID.

### Phase 5: AndroidManifest
Register all new activities in `AndroidManifest.xml`.

---

## API Structure

### Fee Collection Filters API
**Endpoint:** `POST /api/fee-collection-filters/get`

**Request Body:** (All optional)
```json
{
  "session_id": 21,
  "class_id": 19,
  "section_id": 1
}
```

**Response:**
```json
{
  "status": 1,
  "message": "Filter options retrieved successfully",
  "data": {
    "sessions": [
      {
        "id": 21,
        "name": "2024-2025",
        "classes": [
          {
            "id": 19,
            "name": "Class 1",
            "sections": [
              {"id": 1, "name": "Section A"},
              {"id": 2, "name": "Section B"}
            ]
          }
        ]
      }
    ],
    "fee_groups": [
      {"id": 1, "name": "Tuition Fees"}
    ],
    "fee_types": [
      {"id": 1, "name": "Monthly Fee", "code": "MF001"}
    ],
    "collect_by": [
      {"id": 1, "name": "John Doe", "employee_id": "EMP001"}
    ],
    "group_by_options": ["class", "collect", "mode"]
  }
}
```

---

## Files Created

### Java Files
1. ✅ `app/src/main/java/com/qdocs/ssre241123/teachers/BaseFinanceReportActivity.java`
2. ⏳ `app/src/main/java/com/qdocs/ssre241123/teachers/TotalBalanceFeesReportActivity.java`
3. ⏳ `app/src/main/java/com/qdocs/ssre241123/teachers/TotalFeeCollectionReportActivity.java`
4. ⏳ `app/src/main/java/com/qdocs/ssre241123/teachers/FeesCollectionReportActivity.java`
5. ⏳ `app/src/main/java/com/qdocs/ssre241123/teachers/OtherFeesCollectionReportActivity.java`
6. ⏳ `app/src/main/java/com/qdocs/ssre241123/teachers/OtherFeeAndCollectionFeeCombinedActivity.java`
7. ⏳ `app/src/main/java/com/qdocs/ssre241123/teachers/BalanceFeesReportActivity.java`

### Layout Files
1. ⏳ `app/src/main/res/layout/activity_total_balance_fees_report.xml`
2. ⏳ `app/src/main/res/layout/activity_total_fee_collection_report.xml`
3. ⏳ `app/src/main/res/layout/activity_fees_collection_report.xml`
4. ⏳ `app/src/main/res/layout/activity_other_fees_collection_report.xml`
5. ⏳ `app/src/main/res/layout/activity_other_fee_and_collection_fee_combined.xml`
6. ⏳ `app/src/main/res/layout/activity_balance_fees_report.xml`

### Modified Files
1. ✅ `app/src/main/java/com/qdocs/ssre241123/utils/Constants.java` - Added API endpoints
2. ⏳ `app/src/main/java/com/qdocs/ssre241123/adapters/ReportItemAdapter.java` - Add routing
3. ⏳ `app/src/main/AndroidManifest.xml` - Register activities

---

## Testing Checklist

For each report:
- [ ] Filters load correctly from hierarchical API
- [ ] Session → Class → Section cascading works
- [ ] Date pickers work for duration-based reports
- [ ] Search duration dropdown updates dates correctly
- [ ] Generate Report button calls correct API
- [ ] Request body includes all selected filters
- [ ] Response is parsed correctly
- [ ] RecyclerView displays data (when adapter is implemented)
- [ ] No data state shows when no results
- [ ] Loading state shows during API calls
- [ ] Error handling works correctly

---

## Next Steps

1. ✅ Complete `BaseFinanceReportActivity.java`
2. ⏳ Simplify `TotalBalanceFeesReportActivity.java` to extend base
3. ⏳ Create remaining report activities
4. ⏳ Create layouts for each report
5. ⏳ Update routing in `ReportItemAdapter.java`
6. ⏳ Register activities in `AndroidManifest.xml`
7. ⏳ Test each report thoroughly

---

## Notes

- All filters are optional unless specified
- Empty request body `{}` returns all available data
- Hierarchical structure maintains Session → Class → Section relationship
- Date ranges default to "Today" for duration-based reports
- Search Type defaults to "All" for balance reports
- Group By options come from API: ["class", "collect", "mode"]

---

**Status:** Phase 1 Complete, Phase 2 In Progress  
**Last Updated:** October 10, 2025

