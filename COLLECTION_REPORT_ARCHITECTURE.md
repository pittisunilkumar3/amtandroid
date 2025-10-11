# Collection Report - Architecture Overview

## 🏗️ System Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                         USER INTERFACE                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌───────────────────────────────────────────────────────┐    │
│  │     FeesCollectionReportActivity                      │    │
│  │     (extends BaseFinanceReportActivity)               │    │
│  │                                                        │    │
│  │  ┌──────────────────────────────────────────────┐   │    │
│  │  │  Filter Card                                  │   │    │
│  │  │  - Search Duration Spinner                    │   │    │
│  │  │  - From/To Date Pickers                       │   │    │
│  │  │  - Session Spinner                            │   │    │
│  │  │  - Class Spinner                              │   │    │
│  │  │  - Section Spinner                            │   │    │
│  │  │  - Fee Type Spinner                           │   │    │
│  │  │  - Collected By Spinner                       │   │    │
│  │  │  - Group By Spinner                           │   │    │
│  │  │  - Generate Report Button                     │   │    │
│  │  └──────────────────────────────────────────────┘   │    │
│  │                                                        │    │
│  │  ┌──────────────────────────────────────────────┐   │    │
│  │  │  RecyclerView                                 │   │    │
│  │  │  (CollectionReportAdapter)                    │   │    │
│  │  │                                                │   │    │
│  │  │  ┌────────────────────────────────────────┐ │   │    │
│  │  │  │  Collection Record Card 1              │ │   │    │
│  │  │  │  - Header (Invoice, Date)              │ │   │    │
│  │  │  │  - Student Info                        │ │   │    │
│  │  │  │  - Fee Info                            │ │   │    │
│  │  │  │  - Amount Details                      │ │   │    │
│  │  │  │  - Payment Info                        │ │   │    │
│  │  │  └────────────────────────────────────────┘ │   │    │
│  │  │                                                │   │    │
│  │  │  ┌────────────────────────────────────────┐ │   │    │
│  │  │  │  Collection Record Card 2              │ │   │    │
│  │  │  └────────────────────────────────────────┘ │   │    │
│  │  │                                                │   │    │
│  │  │  ...                                          │   │    │
│  │  └──────────────────────────────────────────────┘   │    │
│  └───────────────────────────────────────────────────────┘    │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ API Call
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                         API LAYER                               │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  POST /api/collection-report/filter                            │
│                                                                 │
│  Headers:                                                       │
│  - Client-Service: smartschool                                 │
│  - Auth-Key: schoolAdmin@                                      │
│                                                                 │
│  Request Body:                                                  │
│  {                                                              │
│    "search_type": "this_month",                                │
│    "date_from": "2025-10-01",                                  │
│    "date_to": "2025-10-31",                                    │
│    "session_id": "1",                                          │
│    "class_id": "1",                                            │
│    "section_id": "1",                                          │
│    "feetype_id": "1",                                          │
│    "received_by": "5",                                         │
│    "group": "class"                                            │
│  }                                                              │
│                                                                 │
│  Response:                                                      │
│  {                                                              │
│    "status": 1,                                                │
│    "message": "Collection report retrieved successfully",      │
│    "total_records": 150,                                       │
│    "data": [ ... ]                                             │
│  }                                                              │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ JSON Response
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                      DATA LAYER                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌───────────────────────────────────────────────────────┐    │
│  │  CollectionReportModel                                │    │
│  │                                                        │    │
│  │  Fields:                                               │    │
│  │  - id, studentFeesMasterId, feeGroupsFeetypeId        │    │
│  │  - admissionNo, firstname, middlename, lastname       │    │
│  │  - classId, className, sectionId, section             │    │
│  │  - name, type, code, isSystem                         │    │
│  │  - amount, amountDiscount, amountFine                 │    │
│  │  - description, paymentMode, date, invNo              │    │
│  │  - receivedBy                                          │    │
│  │                                                        │    │
│  │  Methods:                                              │    │
│  │  - getFullName()                                       │    │
│  │  - getClassSection()                                   │    │
│  │  - getTotalAmount()                                    │    │
│  └───────────────────────────────────────────────────────┘    │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🔄 Data Flow Diagram

```
┌──────────┐
│  User    │
└────┬─────┘
     │
     │ 1. Selects Filters
     ▼
┌─────────────────────────────┐
│  FeesCollectionReport       │
│  Activity                   │
│                             │
│  - Collects filter values   │
│  - Builds request body      │
└────┬────────────────────────┘
     │
     │ 2. Clicks Generate Report
     ▼
┌─────────────────────────────┐
│  BaseFinanceReportActivity  │
│                             │
│  - Shows loading indicator  │
│  - Makes API call           │
└────┬────────────────────────┘
     │
     │ 3. HTTP POST Request
     ▼
┌─────────────────────────────┐
│  API Server                 │
│  /api/collection-report/    │
│  filter                     │
│                             │
│  - Validates request        │
│  - Queries database         │
│  - Returns JSON response    │
└────┬────────────────────────┘
     │
     │ 4. JSON Response
     ▼
┌─────────────────────────────┐
│  FeesCollectionReport       │
│  Activity                   │
│                             │
│  - parseReportResponse()    │
│  - Parses JSON              │
│  - Creates model objects    │
└────┬────────────────────────┘
     │
     │ 5. List<CollectionReportModel>
     ▼
┌─────────────────────────────┐
│  CollectionReportAdapter    │
│                             │
│  - Receives data list       │
│  - Binds to ViewHolders     │
│  - Formats display          │
└────┬────────────────────────┘
     │
     │ 6. Display Cards
     ▼
┌─────────────────────────────┐
│  RecyclerView               │
│                             │
│  - Shows collection cards   │
│  - Handles scrolling        │
└─────────────────────────────┘
```

---

## 📦 Component Relationships

```
┌─────────────────────────────────────────────────────────┐
│                  BaseFinanceReportActivity              │
│                                                         │
│  - Common filter handling                               │
│  - API calling logic                                    │
│  - Loading/error states                                 │
│  - Abstract methods                                     │
└────────────────────┬────────────────────────────────────┘
                     │
                     │ extends
                     ▼
┌─────────────────────────────────────────────────────────┐
│            FeesCollectionReportActivity                 │
│                                                         │
│  - Implements abstract methods                          │
│  - parseReportResponse()                                │
│  - parseCollectionItem()                                │
│  - displayReport()                                      │
└────┬────────────────────────────────────┬──────────────┘
     │                                    │
     │ uses                               │ uses
     ▼                                    ▼
┌──────────────────────┐      ┌──────────────────────────┐
│ CollectionReport     │      │ CollectionReport         │
│ Model                │      │ Adapter                  │
│                      │      │                          │
│ - Data structure     │◄─────│ - Display logic          │
│ - Helper methods     │ uses │ - View binding           │
└──────────────────────┘      │ - Formatting             │
                              └────────┬─────────────────┘
                                       │
                                       │ inflates
                                       ▼
                              ┌──────────────────────────┐
                              │ item_collection_         │
                              │ report.xml               │
                              │                          │
                              │ - Card layout            │
                              │ - View components        │
                              └──────────────────────────┘
```

---

## 🎯 Class Diagram

```
┌─────────────────────────────────────────────────────────┐
│  BaseFinanceReportActivity (Abstract)                   │
├─────────────────────────────────────────────────────────┤
│  # reportContentRecyclerView: RecyclerView              │
│  # progressBar: ProgressBar                             │
│  # nodataLayout: LinearLayout                           │
│  # generateReportButton: Button                         │
│  # sessionSpinner: Spinner                              │
│  # classSpinner: Spinner                                │
│  # sectionSpinner: Spinner                              │
│  # feeTypeSpinner: Spinner                              │
│  # collectBySpinner: Spinner                            │
│  # groupBySpinner: Spinner                              │
├─────────────────────────────────────────────────────────┤
│  + onCreate()                                           │
│  + setupCommonSpinners()                                │
│  + setupGenerateButton()                                │
│  + loadFilterOptions()                                  │
│  + generateReport()                                     │
│  # showLoading()                                        │
│  # showContent()                                        │
│  # showNoData()                                         │
│  # abstract getLayoutResourceId(): int                  │
│  # abstract getReportTitle(): String                    │
│  # abstract getReportApiUrl(): String                   │
│  # abstract setupSpecificFilters()                      │
│  # abstract parseReportResponse(String)                 │
└─────────────────────────────────────────────────────────┘
                          △
                          │ extends
                          │
┌─────────────────────────────────────────────────────────┐
│  FeesCollectionReportActivity                           │
├─────────────────────────────────────────────────────────┤
│  - collectionList: List<CollectionReportModel>          │
│  - adapter: CollectionReportAdapter                     │
├─────────────────────────────────────────────────────────┤
│  + getLayoutResourceId(): int                           │
│  + getReportTitle(): String                             │
│  + getReportApiUrl(): String                            │
│  + setupSpecificFilters()                               │
│  + parseReportResponse(String)                          │
│  - parseCollectionItem(JSONObject): CollectionReportModel│
│  - displayReport()                                      │
└─────────────────────────────────────────────────────────┘
                          │
                          │ uses
                          ▼
┌─────────────────────────────────────────────────────────┐
│  CollectionReportModel                                  │
├─────────────────────────────────────────────────────────┤
│  - id: String                                           │
│  - studentFeesMasterId: String                          │
│  - admissionNo: String                                  │
│  - firstname: String                                    │
│  - middlename: String                                   │
│  - lastname: String                                     │
│  - className: String                                    │
│  - section: String                                      │
│  - type: String                                         │
│  - code: String                                         │
│  - name: String                                         │
│  - amount: String                                       │
│  - amountDiscount: String                               │
│  - amountFine: String                                   │
│  - paymentMode: String                                  │
│  - date: String                                         │
│  - invNo: String                                        │
│  - receivedBy: String                                   │
├─────────────────────────────────────────────────────────┤
│  + getFullName(): String                                │
│  + getClassSection(): String                            │
│  + getTotalAmount(): double                             │
│  + getters/setters for all fields                       │
└─────────────────────────────────────────────────────────┘
                          △
                          │ uses
                          │
┌─────────────────────────────────────────────────────────┐
│  CollectionReportAdapter                                │
├─────────────────────────────────────────────────────────┤
│  - context: Context                                     │
│  - collectionList: List<CollectionReportModel>          │
│  - currency: String                                     │
├─────────────────────────────────────────────────────────┤
│  + onCreateViewHolder(): ViewHolder                     │
│  + onBindViewHolder(ViewHolder, int)                    │
│  + getItemCount(): int                                  │
│  - formatDate(String): String                           │
└─────────────────────────────────────────────────────────┘
```

---

## 🔐 Security Flow

```
┌──────────┐
│  User    │
└────┬─────┘
     │
     │ 1. Login
     ▼
┌─────────────────────┐
│  Authentication     │
│  - Verify credentials│
│  - Store auth token │
└────┬────────────────┘
     │
     │ 2. Navigate to Report
     ▼
┌─────────────────────┐
│  Activity           │
│  - Check auth       │
│  - Load filters     │
└────┬────────────────┘
     │
     │ 3. Generate Report
     ▼
┌─────────────────────┐
│  API Request        │
│  Headers:           │
│  - Client-Service   │
│  - Auth-Key         │
└────┬────────────────┘
     │
     │ 4. Validate
     ▼
┌─────────────────────┐
│  API Server         │
│  - Check headers    │
│  - Verify auth      │
│  - Process request  │
└────┬────────────────┘
     │
     │ 5. Return Data
     ▼
┌─────────────────────┐
│  Display Results    │
└─────────────────────┘
```

---

## 📊 State Management

```
┌─────────────────────────────────────────────────────────┐
│                    Activity States                      │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  ┌──────────┐                                          │
│  │ INITIAL  │                                          │
│  └────┬─────┘                                          │
│       │                                                 │
│       │ onCreate()                                      │
│       ▼                                                 │
│  ┌──────────┐                                          │
│  │ LOADING  │ ◄──────────┐                            │
│  │ FILTERS  │            │                            │
│  └────┬─────┘            │                            │
│       │                  │                            │
│       │ Filters loaded   │                            │
│       ▼                  │                            │
│  ┌──────────┐            │                            │
│  │  READY   │            │                            │
│  └────┬─────┘            │                            │
│       │                  │                            │
│       │ Generate Report  │                            │
│       ▼                  │                            │
│  ┌──────────┐            │                            │
│  │ LOADING  │            │                            │
│  │  DATA    │            │                            │
│  └────┬─────┘            │                            │
│       │                  │                            │
│       ├──────────────────┘                            │
│       │ Error                                          │
│       │                                                 │
│       │ Success                                         │
│       ▼                                                 │
│  ┌──────────┐                                          │
│  │DISPLAYING│                                          │
│  │  DATA    │                                          │
│  └──────────┘                                          │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

---

**Architecture Version:** 1.0  
**Last Updated:** October 11, 2025  
**Status:** Complete

