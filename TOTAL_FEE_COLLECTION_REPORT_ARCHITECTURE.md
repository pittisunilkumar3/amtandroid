# Total Fee Collection Report - Architecture

## 📐 System Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        Teacher Dashboard                         │
│                                                                  │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐     │
│  │   Reports    │ -> │   Finance    │ -> │ Total Fee    │     │
│  │              │    │              │    │ Collection   │     │
│  └──────────────┘    └──────────────┘    └──────────────┘     │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│          TotalFeeCollectionReportActivity                        │
│                                                                  │
│  Extends: BaseFinanceReportActivity                             │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                    Filter Section                         │  │
│  │  • Search Duration (Today/Week/Month/Year/Custom)        │  │
│  │  • Date Range (From Date - To Date)                      │  │
│  │  • Class (Cascading)                                      │  │
│  │  • Section (Cascading)                                    │  │
│  │  • Fee Type                                               │  │
│  │  • Collect By                                             │  │
│  │  • Group By                                               │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                │                                 │
│                                ▼                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                  Generate Report Button                   │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                │                                 │
│                                ▼                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                    API Call Handler                       │  │
│  │  • Build request with filters                            │  │
│  │  • Call API endpoint                                      │  │
│  │  • Handle response                                        │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                         API Layer                                │
│                                                                  │
│  Endpoint: POST /api/total-fee-collection-report/filter         │
│  Headers:                                                        │
│    • Content-Type: application/json                             │
│    • Client-Service: smartschool                                │
│    • Auth-Key: schoolAdmin@                                     │
│                                                                  │
│  Request Body: { filters... }                                   │
│  Response: { status, message, summary, data }                   │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Response Parser                               │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  parseReportResponse()                                    │  │
│  │    ├─> Parse summary                                      │  │
│  │    │     ├─> Total records                                │  │
│  │    │     ├─> Total amount                                 │  │
│  │    │     └─> Fee type breakdown                           │  │
│  │    │                                                       │  │
│  │    └─> Parse data                                         │  │
│  │          ├─> Check if grouped                             │  │
│  │          ├─> Parse grouped data                           │  │
│  │          └─> Parse regular data                           │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Data Model Layer                              │
│                                                                  │
│  TotalFeeCollectionReportModel                                  │
│    • Student Information                                         │
│    • Fee Details                                                 │
│    • Amount Breakdown                                            │
│    • Payment Information                                         │
│    • Type Indicator                                              │
│    • Grouping Support                                            │
│                                                                  │
│  FeeTypeBreakdown (Inner Class)                                 │
│    • Fee Type Name                                               │
│    • Transaction Count                                           │
│    • Total Amount                                                │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Display Layer                               │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                    Summary Card                           │  │
│  │  ┌────────────────────────────────────────────────────┐  │  │
│  │  │  Total Records: 350                                 │  │  │
│  │  │  Total Amount: ₹4,50,000                           │  │  │
│  │  └────────────────────────────────────────────────────┘  │  │
│  │  ┌────────────────────────────────────────────────────┐  │  │
│  │  │  Fee Type Breakdown:                                │  │  │
│  │  │    • Tuition Fees (150) - ₹3,00,000                │  │  │
│  │  │    • Hostel Fees (100) - ₹1,00,000                 │  │  │
│  │  │    • Library Fees (50) - ₹25,000                   │  │  │
│  │  │    • Transport Fees (50) - ₹25,000                 │  │  │
│  │  └────────────────────────────────────────────────────┘  │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                  RecyclerView                             │  │
│  │  ┌────────────────────────────────────────────────────┐  │  │
│  │  │  Collection Card 1                                  │  │  │
│  │  │    • Invoice: INV-12345 | Date: Oct 15, 2025      │  │  │
│  │  │    • Student: John Doe (ADM001)                    │  │  │
│  │  │    • Class: 10 - A                                 │  │  │
│  │  │    • Fee Type: Tuition Fees                        │  │  │
│  │  │    • Amount: ₹5,000                                │  │  │
│  │  │    • Fine: ₹100 | Discount: ₹500                  │  │  │
│  │  │    • Net Amount: ₹4,600                            │  │  │
│  │  │    • Payment Mode: Cash                            │  │  │
│  │  └────────────────────────────────────────────────────┘  │  │
│  │  ┌────────────────────────────────────────────────────┐  │  │
│  │  │  Collection Card 2                                  │  │  │
│  │  │    ...                                              │  │  │
│  │  └────────────────────────────────────────────────────┘  │  │
│  │  ┌────────────────────────────────────────────────────┐  │  │
│  │  │  Collection Card N                                  │  │  │
│  │  │    ...                                              │  │  │
│  │  └────────────────────────────────────────────────────┘  │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│              TotalFeeCollectionReportAdapter                     │
│                                                                  │
│  • Binds data to ViewHolder                                     │
│  • Applies theme colors                                          │
│  • Formats currency and dates                                    │
│  • Handles optional fields visibility                            │
│  • Manages type indicators                                       │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🔄 Data Flow

```
User Action
    │
    ▼
Select Filters
    │
    ▼
Tap Generate Report
    │
    ▼
Build Request
    │
    ├─> search_type
    ├─> date_from
    ├─> date_to
    ├─> class_id
    ├─> section_id
    ├─> session_id
    ├─> feetype_id
    ├─> received_by
    └─> group
    │
    ▼
API Call
    │
    ▼
Receive Response
    │
    ├─> status
    ├─> message
    ├─> summary
    │     ├─> total_records
    │     ├─> total_amount
    │     └─> fee_type_breakdown[]
    │           ├─> fee_type
    │           ├─> count
    │           └─> total
    │
    └─> data[]
          ├─> Regular Data
          │     └─> Collection Records[]
          │
          └─> Grouped Data
                └─> Groups[]
                      ├─> group_name
                      ├─> records[]
                      └─> subtotal
    │
    ▼
Parse Response
    │
    ├─> Parse Summary
    │     ├─> Extract total_records
    │     ├─> Extract total_amount
    │     └─> Extract fee_type_breakdown
    │
    └─> Parse Data
          ├─> Check if grouped
          ├─> Parse each record
          └─> Create model objects
    │
    ▼
Create Models
    │
    └─> TotalFeeCollectionReportModel[]
          ├─> Student info
          ├─> Fee details
          ├─> Amount breakdown
          ├─> Payment info
          └─> Type indicator
    │
    ▼
Display Summary
    │
    ├─> Show total records
    ├─> Show total amount
    └─> Show fee type breakdown
    │
    ▼
Display Records
    │
    └─> RecyclerView with Adapter
          ├─> Bind data to ViewHolder
          ├─> Apply theme colors
          ├─> Format currency
          ├─> Format dates
          └─> Handle visibility
    │
    ▼
User Views Report
```

---

## 🏗️ Component Hierarchy

```
TotalFeeCollectionReportActivity
│
├─> BaseFinanceReportActivity (Parent)
│   │
│   ├─> Filter Management
│   │   ├─> Session Spinner
│   │   ├─> Class Spinner
│   │   ├─> Section Spinner
│   │   ├─> Fee Type Spinner
│   │   ├─> Collect By Spinner
│   │   └─> Group By Spinner
│   │
│   ├─> Date Management
│   │   ├─> Search Duration Spinner
│   │   ├─> From Date Picker
│   │   └─> To Date Picker
│   │
│   ├─> API Management
│   │   ├─> Build Request
│   │   ├─> Make API Call
│   │   └─> Handle Response
│   │
│   └─> UI State Management
│       ├─> Show Loading
│       ├─> Show Content
│       ├─> Show No Data
│       └─> Show Error
│
├─> Summary Card
│   ├─> Total Records TextView
│   ├─> Total Amount TextView
│   └─> Fee Type Breakdown Layout
│       └─> Breakdown Items (Dynamic)
│
├─> RecyclerView
│   └─> TotalFeeCollectionReportAdapter
│       └─> ViewHolder
│           ├─> Header Layout
│           │   ├─> Invoice Number
│           │   ├─> Date
│           │   └─> Type Indicator
│           │
│           ├─> Student Info Section
│           │   ├─> Student Name
│           │   ├─> Admission Number
│           │   └─> Class & Section
│           │
│           ├─> Fee Details Section
│           │   ├─> Fee Type
│           │   └─> Fee Code
│           │
│           ├─> Amount Section
│           │   ├─> Amount
│           │   ├─> Fine (Optional)
│           │   ├─> Discount (Optional)
│           │   └─> Net Amount
│           │
│           ├─> Payment Section
│           │   ├─> Payment Mode
│           │   └─> Collected By (Optional)
│           │
│           └─> Note Section (Optional)
│
└─> Data Models
    ├─> TotalFeeCollectionReportModel
    │   ├─> Student Information
    │   ├─> Fee Details
    │   ├─> Amount Breakdown
    │   ├─> Payment Information
    │   └─> Type Indicator
    │
    └─> FeeTypeBreakdown
        ├─> Fee Type Name
        ├─> Transaction Count
        └─> Total Amount
```

---

## 🔌 API Integration Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                      Android App                                 │
│                                                                  │
│  TotalFeeCollectionReportActivity                               │
│    │                                                             │
│    ├─> buildRequestParams()                                     │
│    │     └─> Create JSON with filters                           │
│    │                                                             │
│    ├─> makeApiCall()                                            │
│    │     ├─> Add headers                                        │
│    │     ├─> Set request body                                   │
│    │     └─> Send POST request                                  │
│    │                                                             │
│    └─> onResponse()                                             │
│          └─> parseReportResponse()                              │
└─────────────────────────────────────────────────────────────────┘
                                │
                                │ HTTP POST
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Backend API                                 │
│                                                                  │
│  POST /api/total-fee-collection-report/filter                   │
│    │                                                             │
│    ├─> Validate headers                                         │
│    │     ├─> Client-Service: smartschool                        │
│    │     └─> Auth-Key: schoolAdmin@                             │
│    │                                                             │
│    ├─> Parse request body                                       │
│    │     └─> Extract filters                                    │
│    │                                                             │
│    ├─> Query database                                           │
│    │     ├─> Merge regular fees                                 │
│    │     ├─> Merge other fees                                   │
│    │     ├─> Merge transport fees                               │
│    │     ├─> Apply filters                                      │
│    │     └─> Calculate breakdown                                │
│    │                                                             │
│    └─> Build response                                           │
│          ├─> Create summary                                     │
│          ├─> Create fee type breakdown                          │
│          └─> Format data                                        │
└─────────────────────────────────────────────────────────────────┘
                                │
                                │ JSON Response
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Android App                                 │
│                                                                  │
│  Response Handler                                                │
│    │                                                             │
│    ├─> Parse JSON                                               │
│    │     ├─> Extract status                                     │
│    │     ├─> Extract message                                    │
│    │     ├─> Extract summary                                    │
│    │     └─> Extract data                                       │
│    │                                                             │
│    ├─> Create models                                            │
│    │     └─> TotalFeeCollectionReportModel[]                    │
│    │                                                             │
│    └─> Update UI                                                │
│          ├─> Display summary                                    │
│          └─> Display records                                    │
└─────────────────────────────────────────────────────────────────┘
```

---

## 📊 Class Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│         BaseFinanceReportActivity (Abstract)                     │
├─────────────────────────────────────────────────────────────────┤
│ # sessionSpinner: Spinner                                        │
│ # classSpinner: Spinner                                          │
│ # sectionSpinner: Spinner                                        │
│ # feeTypeSpinner: Spinner                                        │
│ # collectBySpinner: Spinner                                      │
│ # groupBySpinner: Spinner                                        │
│ # searchDurationSpinner: Spinner                                 │
│ # fromDateEditText: EditText                                     │
│ # toDateEditText: EditText                                       │
│ # generateReportButton: Button                                   │
│ # reportContentRecyclerView: RecyclerView                        │
│ # progressBar: ProgressBar                                       │
│ # nodataLayout: LinearLayout                                     │
├─────────────────────────────────────────────────────────────────┤
│ + onCreate()                                                     │
│ + initializeViews()                                              │
│ + setupActionBar()                                               │
│ + setupCommonSpinners()                                          │
│ + loadFilterOptions()                                            │
│ + buildRequestParams()                                           │
│ + makeApiCall()                                                  │
│ # abstract getLayoutResourceId(): int                            │
│ # abstract getReportTitle(): String                              │
│ # abstract getReportApiUrl(): String                             │
│ # abstract setupSpecificFilters()                                │
│ # abstract parseReportResponse(String)                           │
└─────────────────────────────────────────────────────────────────┘
                                △
                                │ extends
                                │
┌─────────────────────────────────────────────────────────────────┐
│         TotalFeeCollectionReportActivity                         │
├─────────────────────────────────────────────────────────────────┤
│ - summaryCard: CardView                                          │
│ - totalRecordsTv: TextView                                       │
│ - totalAmountTv: TextView                                        │
│ - feeTypeBreakdownLayout: LinearLayout                           │
│ - collectionList: List<TotalFeeCollectionReportModel>           │
│ - adapter: TotalFeeCollectionReportAdapter                       │
│ - currency: String                                               │
│ - numberFormat: NumberFormat                                     │
├─────────────────────────────────────────────────────────────────┤
│ + getLayoutResourceId(): int                                     │
│ + getReportTitle(): String                                       │
│ + getReportApiUrl(): String                                      │
│ + setupSpecificFilters()                                         │
│ + parseReportResponse(String)                                    │
│ - displaySummary(JSONObject)                                     │
│ - displayFeeTypeBreakdown(JSONArray)                             │
│ - parseRegularData(JSONArray)                                    │
│ - parseGroupedData(JSONArray)                                    │
│ - parseCollectionItem(JSONObject): Model                         │
│ - displayReport()                                                │
└─────────────────────────────────────────────────────────────────┘
                                │ uses
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│         TotalFeeCollectionReportAdapter                          │
├─────────────────────────────────────────────────────────────────┤
│ - context: Context                                               │
│ - collectionList: List<TotalFeeCollectionReportModel>           │
│ - currency: String                                               │
│ - numberFormat: NumberFormat                                     │
├─────────────────────────────────────────────────────────────────┤
│ + onCreateViewHolder(): ViewHolder                               │
│ + onBindViewHolder(ViewHolder, int)                              │
│ + getItemCount(): int                                            │
└─────────────────────────────────────────────────────────────────┘
                                │ uses
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│         TotalFeeCollectionReportModel                            │
├─────────────────────────────────────────────────────────────────┤
│ - id: String                                                     │
│ - invoiceNo: String                                              │
│ - admissionNo: String                                            │
│ - studentName: String                                            │
│ - className: String                                              │
│ - sectionName: String                                            │
│ - fatherName: String                                             │
│ - mobileNo: String                                               │
│ - feeType: String                                                │
│ - feeCode: String                                                │
│ - amount: double                                                 │
│ - fine: double                                                   │
│ - discount: double                                               │
│ - netAmount: double                                              │
│ - paymentMode: String                                            │
│ - date: String                                                   │
│ - collectedBy: String                                            │
│ - note: String                                                   │
│ - type: String                                                   │
│ - groupName: String                                              │
│ - groupedRecords: List<TotalFeeCollectionReportModel>           │
│ - subtotal: double                                               │
├─────────────────────────────────────────────────────────────────┤
│ + getters/setters for all fields                                │
│ + getFormattedDate(): String                                     │
│ + getFullClassName(): String                                     │
│ + getTypeLabel(): String                                         │
│ + isGrouped(): boolean                                           │
└─────────────────────────────────────────────────────────────────┘
                                │ contains
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│         FeeTypeBreakdown (Inner Class)                           │
├─────────────────────────────────────────────────────────────────┤
│ - feeType: String                                                │
│ - count: int                                                     │
│ - total: double                                                  │
├─────────────────────────────────────────────────────────────────┤
│ + getters/setters for all fields                                │
└─────────────────────────────────────────────────────────────────┘
```

---

**Last Updated:** 2025-10-10  
**Version:** 1.0

