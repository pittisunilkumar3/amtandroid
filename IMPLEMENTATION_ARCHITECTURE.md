# Session Fee Structure Reports - Architecture

## System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                     Smart School Android App                     │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ Teacher Login
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Teacher Dashboard                           │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ Navigate to Reports
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Teacher Reports Activity                      │
│                  (TeacherReportsActivity.java)                   │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ Select Finance Category
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                 Teacher Report Category Activity                 │
│              (TeacherReportCategoryActivity.java)                │
│                                                                   │
│  Finance Reports List:                                           │
│  • Total Balance Fees Statement                                  │
│  • Type Wise Balance Report          ◄── NEW                     │
│  • Fee Collection Report Column Wise ◄── NEW                     │
│  • Daily Collection Report                                       │
│  • ... (other reports)                                           │
└─────────────────────────────────────────────────────────────────┘
                    │                        │
        ┌───────────┴────────┐   ┌──────────┴──────────┐
        │                    │   │                      │
        ▼                    ▼   ▼                      ▼
┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐
│ Report Item      │  │ Report Item      │  │ Report Item      │
│ Adapter          │  │ Adapter          │  │ Adapter          │
│ (Routing Logic)  │  │ (Routing Logic)  │  │ (Routing Logic)  │
└──────────────────┘  └──────────────────┘  └──────────────────┘
        │                    │                      │
        │ type_wise_         │ fee_collection_      │
        │ balance_report     │ report_column_wise   │
        ▼                    ▼                      ▼
┌──────────────────┐  ┌──────────────────────────────────────┐
│ TypeWiseBalance  │  │ FeeCollectionReportColumnWise        │
│ ReportActivity   │  │ Activity                             │
└──────────────────┘  └──────────────────────────────────────┘
```

---

## Component Flow - Type Wise Balance Report

```
┌─────────────────────────────────────────────────────────────────┐
│         TypeWiseBalanceReportActivity.java                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  onCreate()                                                       │
│    ├─ initializeViews()                                          │
│    ├─ setupListeners()                                           │
│    └─ loadFilterOptions() ──────────────────┐                    │
│                                              │                    │
│  loadFilterOptions()                         │                    │
│    ├─ API: POST /session-fee-structure/list │                    │
│    └─ parseFilterOptions() ◄────────────────┘                    │
│         ├─ Parse sessions                                         │
│         ├─ Parse classes                                          │
│         ├─ Parse fee_groups                                       │
│         ├─ Parse fee_types                                        │
│         ├─ setupSessionSpinner()                                  │
│         ├─ setupClassSpinner()                                    │
│         ├─ setupSectionSpinner()                                  │
│         ├─ setupFeeGroupSpinner()                                 │
│         └─ setupFeeTypeSpinner()                                  │
│                                                                   │
│  generateReport() [User clicks button]                           │
│    └─ fetchTypeWiseBalanceReport() ──────────┐                   │
│                                              │                    │
│  fetchTypeWiseBalanceReport()                │                    │
│    ├─ Build request body with filters       │                    │
│    ├─ API: POST /type-wise-balance-report/  │                    │
│    │         filter                          │                    │
│    └─ parseReportResponse() ◄───────────────┘                    │
│         └─ Display report data                                    │
│                                                                   │
└─────────────────────────────────────────────────────────────────┘
```

---

## Component Flow - Fee Collection Report Column Wise

```
┌─────────────────────────────────────────────────────────────────┐
│      FeeCollectionReportColumnWiseActivity.java                  │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  onCreate()                                                       │
│    ├─ initializeViews()                                          │
│    ├─ setupListeners()                                           │
│    └─ loadFilterOptions() ──────────────────┐                    │
│                                              │                    │
│  loadFilterOptions()                         │                    │
│    ├─ API: POST /session-fee-structure/list │                    │
│    └─ parseFilterOptions() ◄────────────────┘                    │
│         ├─ Parse sessions                                         │
│         ├─ Parse classes                                          │
│         ├─ Parse fee_types                                        │
│         ├─ setupSessionSpinner()                                  │
│         ├─ setupClassSpinner()                                    │
│         ├─ setupSectionSpinner()                                  │
│         └─ setupFeeTypeSpinner()                                  │
│                                                                   │
│  showDatePicker() [User clicks date field]                       │
│    ├─ Show DatePickerDialog                                      │
│    └─ Format date (display: dd-MM-yyyy, API: yyyy-MM-dd)         │
│                                                                   │
│  generateReport() [User clicks button]                           │
│    └─ fetchFeeCollectionReport() ────────────┐                   │
│                                              │                    │
│  fetchFeeCollectionReport()                  │                    │
│    ├─ Build request body with filters       │                    │
│    ├─ API: POST /fee-collection-report-     │                    │
│    │         column-wise/filter             │                    │
│    └─ parseReportResponse() ◄───────────────┘                    │
│         └─ Display report data                                    │
│                                                                   │
└─────────────────────────────────────────────────────────────────┘
```

---

## API Integration Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        Android App                               │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ HTTP POST
                              │ Headers:
                              │   Client-Service: smartschool
                              │   Auth-Key: schoolAdmin@
                              │   Content-Type: application/json
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Backend API Server                          │
│                   (http://localhost/amt/api)                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  Endpoint 1: /session-fee-structure/list                         │
│    ├─ Request: {}                                                │
│    └─ Response: {sessions, classes, fee_groups, fee_types}       │
│                                                                   │
│  Endpoint 2: /type-wise-balance-report/filter                    │
│    ├─ Request: {session_id?, class_id?, section_id?,             │
│    │            fee_group_id?, fee_type_id?}                     │
│    └─ Response: {status, message, data[]}                        │
│                                                                   │
│  Endpoint 3: /fee-collection-report-column-wise/filter           │
│    ├─ Request: {from_date?, to_date?, session_id?,               │
│    │            class_id?, section_id?, fee_type_id?}            │
│    └─ Response: {status, message, data[]}                        │
│                                                                   │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ Database Query
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                      MySQL Database                              │
│                                                                   │
│  Tables:                                                          │
│  • sessions                                                       │
│  • classes                                                        │
│  • sections                                                       │
│  • fee_groups                                                     │
│  • fee_types                                                      │
│  • fee_session_groups                                             │
│  • fee_groups_feetype                                             │
│  • student_fees_master                                            │
│  • student_fees_deposite                                          │
│                                                                   │
└─────────────────────────────────────────────────────────────────┘
```

---

## Data Flow Diagram

```
┌──────────────┐
│   User       │
└──────┬───────┘
       │
       │ 1. Opens Report
       ▼
┌──────────────────────────┐
│  Report Activity         │
│  (onCreate)              │
└──────┬───────────────────┘
       │
       │ 2. Load Filter Options
       ▼
┌──────────────────────────┐
│  Session Fee Structure   │
│  List API                │
└──────┬───────────────────┘
       │
       │ 3. Return Filter Data
       ▼
┌──────────────────────────┐
│  Parse & Populate        │
│  Dropdowns               │
└──────┬───────────────────┘
       │
       │ 4. User Selects Filters
       ▼
┌──────────────────────────┐
│  User Clicks             │
│  "Generate Report"       │
└──────┬───────────────────┘
       │
       │ 5. Build Request with Filters
       ▼
┌──────────────────────────┐
│  Report Filter API       │
│  (type-wise or column-   │
│   wise)                  │
└──────┬───────────────────┘
       │
       │ 6. Return Report Data
       ▼
┌──────────────────────────┐
│  Parse & Display         │
│  Report Data             │
└──────┬───────────────────┘
       │
       │ 7. View Report
       ▼
┌──────────────┐
│   User       │
└──────────────┘
```

---

## Class Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│              TypeWiseBalanceReportActivity                       │
├─────────────────────────────────────────────────────────────────┤
│ - sessionsList: List<SessionData>                               │
│ - classesList: List<ClassData>                                  │
│ - sectionsList: List<SectionData>                               │
│ - feeGroupsList: List<FeeGroupData>                             │
│ - feeTypesList: List<FeeTypeData>                               │
│ - selectedSessionId: String                                      │
│ - selectedClassId: String                                        │
│ - selectedSectionId: String                                      │
│ - selectedFeeGroupId: String                                     │
│ - selectedFeeTypeId: String                                      │
├─────────────────────────────────────────────────────────────────┤
│ + onCreate()                                                     │
│ + initializeViews()                                              │
│ + setupListeners()                                               │
│ + loadFilterOptions()                                            │
│ + parseFilterOptions(String)                                     │
│ + setupSessionSpinner()                                          │
│ + setupClassSpinner()                                            │
│ + setupSectionSpinner()                                          │
│ + setupFeeGroupSpinner()                                         │
│ + setupFeeTypeSpinner()                                          │
│ + generateReport()                                               │
│ + fetchTypeWiseBalanceReport()                                   │
│ + parseReportResponse(String)                                    │
│ + showLoading()                                                  │
│ + hideLoading()                                                  │
│ + showContent()                                                  │
│ + showNoData()                                                   │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│         FeeCollectionReportColumnWiseActivity                    │
├─────────────────────────────────────────────────────────────────┤
│ - sessionsList: List<SessionData>                               │
│ - classesList: List<ClassData>                                  │
│ - sectionsList: List<SectionData>                               │
│ - feeTypesList: List<FeeTypeData>                               │
│ - selectedFromDate: String                                       │
│ - selectedToDate: String                                         │
│ - selectedSessionId: String                                      │
│ - selectedClassId: String                                        │
│ - selectedSectionId: String                                      │
│ - selectedFeeTypeId: String                                      │
├─────────────────────────────────────────────────────────────────┤
│ + onCreate()                                                     │
│ + initializeViews()                                              │
│ + setupListeners()                                               │
│ + showDatePicker(boolean)                                        │
│ + loadFilterOptions()                                            │
│ + parseFilterOptions(String)                                     │
│ + setupSessionSpinner()                                          │
│ + setupClassSpinner()                                            │
│ + setupSectionSpinner()                                          │
│ + setupFeeTypeSpinner()                                          │
│ + generateReport()                                               │
│ + fetchFeeCollectionReport()                                     │
│ + parseReportResponse(String)                                    │
│ + showLoading()                                                  │
│ + hideLoading()                                                  │
│ + showContent()                                                  │
│ + showNoData()                                                   │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                    Inner Data Classes                            │
├─────────────────────────────────────────────────────────────────┤
│ SessionData { String id; String name; }                          │
│ ClassData { String id; String name; }                            │
│ SectionData { String id; String name; }                          │
│ FeeGroupData { String id; String name; }                         │
│ FeeTypeData { String id; String name; String code; }             │
└─────────────────────────────────────────────────────────────────┘
```

---

## File Structure

```
smart_school_android_app_src/
│
├── app/src/main/
│   │
│   ├── java/com/qdocs/ssre241123/
│   │   │
│   │   ├── teachers/
│   │   │   ├── TypeWiseBalanceReportActivity.java          ◄── NEW
│   │   │   ├── FeeCollectionReportColumnWiseActivity.java  ◄── NEW
│   │   │   ├── TeacherReportsActivity.java
│   │   │   ├── TeacherReportCategoryActivity.java
│   │   │   └── ... (other activities)
│   │   │
│   │   ├── adapters/
│   │   │   ├── ReportItemAdapter.java                      ◄── MODIFIED
│   │   │   └── ... (other adapters)
│   │   │
│   │   └── utils/
│   │       ├── Constants.java                              ◄── MODIFIED
│   │       └── ... (other utils)
│   │
│   ├── res/layout/
│   │   ├── activity_type_wise_balance_report.xml           ◄── NEW
│   │   ├── activity_fee_collection_report_column_wise.xml  ◄── NEW
│   │   └── ... (other layouts)
│   │
│   └── AndroidManifest.xml                                 ◄── MODIFIED
│
└── Documentation/
    ├── SESSION_FEE_STRUCTURE_REPORTS_IMPLEMENTATION.md     ◄── NEW
    ├── SESSION_FEE_STRUCTURE_REPORTS_QUICK_REFERENCE.md    ◄── NEW
    ├── SESSION_FEE_STRUCTURE_API_EXAMPLES.md               ◄── NEW
    ├── TESTING_SESSION_FEE_STRUCTURE_REPORTS.md            ◄── NEW
    ├── IMPLEMENTATION_COMPLETE_SUMMARY.md                  ◄── NEW
    └── IMPLEMENTATION_ARCHITECTURE.md                      ◄── NEW (this file)
```

---

## Technology Stack

```
┌─────────────────────────────────────────────────────────────────┐
│                      Technology Stack                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  Frontend (Android):                                             │
│  • Language: Java                                                │
│  • UI Framework: Android SDK                                     │
│  • HTTP Client: Volley                                           │
│  • JSON Parsing: org.json                                        │
│  • UI Components: RecyclerView, Spinner, DatePickerDialog        │
│  • Layout: XML                                                   │
│                                                                   │
│  Backend (API):                                                  │
│  • Framework: CodeIgniter / Laravel (assumed)                    │
│  • Database: MySQL                                               │
│  • Authentication: Custom headers (Client-Service, Auth-Key)     │
│  • Response Format: JSON                                         │
│                                                                   │
│  Build Tools:                                                    │
│  • Build System: Gradle                                          │
│  • Android Gradle Plugin: 8.2.0                                  │
│  • Compile SDK: 35                                               │
│  • Min SDK: 21 (assumed)                                         │
│  • Target SDK: 35 (assumed)                                      │
│                                                                   │
└─────────────────────────────────────────────────────────────────┘
```

---

## Design Patterns Used

1. **Activity Pattern** - Each report is a separate Activity
2. **Adapter Pattern** - ReportItemAdapter for routing
3. **Observer Pattern** - Spinner listeners for filter selection
4. **Template Method Pattern** - Common methods (showLoading, hideLoading, etc.)
5. **Data Transfer Object** - Inner data classes (SessionData, ClassData, etc.)
6. **Singleton Pattern** - Volley RequestQueue
7. **Factory Pattern** - Intent creation for different reports

---

## Security Considerations

```
┌─────────────────────────────────────────────────────────────────┐
│                    Security Measures                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  1. Authentication:                                              │
│     • Client-Service header validation                           │
│     • Auth-Key header validation                                 │
│     • Teacher session validation                                 │
│                                                                   │
│  2. Data Validation:                                             │
│     • Input sanitization on backend                              │
│     • SQL injection prevention                                   │
│     • XSS prevention                                             │
│                                                                   │
│  3. Network Security:                                            │
│     • HTTPS recommended for production                           │
│     • Certificate pinning (optional)                             │
│     • Request timeout handling                                   │
│                                                                   │
│  4. Data Privacy:                                                │
│     • No sensitive data in logs                                  │
│     • Secure storage of credentials                              │
│     • Role-based access control                                  │
│                                                                   │
└─────────────────────────────────────────────────────────────────┘
```

---

## Performance Optimization

```
┌─────────────────────────────────────────────────────────────────┐
│                Performance Optimizations                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  1. Network:                                                     │
│     • Single API call for filter options                         │
│     • Optional filters reduce payload size                       │
│     • Request caching (future enhancement)                       │
│                                                                   │
│  2. UI:                                                          │
│     • RecyclerView for efficient list rendering                  │
│     • ViewHolder pattern for list items                          │
│     • Lazy loading of report data                                │
│                                                                   │
│  3. Memory:                                                      │
│     • Clear lists when not needed                                │
│     • Proper lifecycle management                                │
│     • No memory leaks                                            │
│                                                                   │
│  4. Threading:                                                   │
│     • Network calls on background thread (Volley)                │
│     • UI updates on main thread                                  │
│     • No blocking operations                                     │
│                                                                   │
└─────────────────────────────────────────────────────────────────┘
```

---

This architecture document provides a comprehensive overview of the implementation structure, data flow, and technical details of the Session Fee Structure Reports feature.

