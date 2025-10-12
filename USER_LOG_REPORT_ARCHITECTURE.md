# User Log Report - Architecture & Flow

## Component Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                     TeacherReportsActivity                       │
│                    (Reports Menu Screen)                         │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             │ User taps "User Log Report"
                             │
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                      ReportItemAdapter                           │
│                   (Routing Logic)                                │
│                                                                   │
│  if ("user_log".equals(reportItem.getId()))                     │
│      → Launch UserLogReportActivity                              │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                   UserLogReportActivity                          │
│                  (Main Report Screen)                            │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │ UI Components:                                           │   │
│  │  • User Type Spinner (All/Students/Parents/Staff)       │   │
│  │  • From Date Picker                                      │   │
│  │  • To Date Picker                                        │   │
│  │  • Generate Report Button                                │   │
│  │  • Summary Card (Total Records)                          │   │
│  │  • RecyclerView (User Logs)                              │   │
│  │  • Progress Bar                                           │   │
│  │  • No Data Layout                                         │   │
│  └─────────────────────────────────────────────────────────┘   │
│                                                                   │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │ Logic:                                                    │   │
│  │  • setupUserTypeSpinner()                                │   │
│  │  • setupDatePickers()                                     │   │
│  │  • generateReport()                                       │   │
│  │  • parseUserLogResponse()                                │   │
│  │  • showLoading() / hideLoading()                         │   │
│  │  • showData() / showNoData()                             │   │
│  └─────────────────────────────────────────────────────────┘   │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             │ API Request
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Volley Network Layer                        │
│                                                                   │
│  POST /api/user-log/filter                                      │
│  Headers:                                                        │
│    • Client-Service: smartschool                                │
│    • Auth-Key: schoolAdmin@                                     │
│    • Content-Type: application/json                             │
│  Body:                                                           │
│    • role (optional)                                             │
│    • from_date                                                   │
│    • to_date                                                     │
│    • limit                                                       │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             │ API Response
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                      JSON Response Parser                        │
│                                                                   │
│  Parse JSON → Create UserLogModel objects → Add to List         │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             │ Data List
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                       UserLogAdapter                             │
│                   (RecyclerView Adapter)                         │
│                                                                   │
│  For each UserLogModel:                                          │
│    • Bind data to ViewHolder                                     │
│    • Set role badge color                                        │
│    • Show/hide class/section                                     │
│    • Format date/time                                            │
│    • Extract device/browser info                                 │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             │ Display
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                    adapter_user_log_item.xml                     │
│                     (List Item Layout)                           │
│                                                                   │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │ [👤] Username                          [Student Badge]    │ │
│  │ ─────────────────────────────────────────────────────────  │ │
│  │ [🎓] Class: 10 - A                                        │ │
│  │ [📅] 2025-10-12 10:30 AM                                  │ │
│  │ [🌐] IP: 192.168.1.100                                    │ │
│  │ [💻] Desktop - Chrome                                     │ │
│  └───────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

---

## Data Flow Diagram

```
┌──────────────┐
│     User     │
│   Selects    │
│   Filters    │
└──────┬───────┘
       │
       ▼
┌──────────────────────────────────────────────────────────┐
│  User Type: [All User Log ▼]                             │
│  From Date: [2025-10-05]  To Date: [2025-10-12]         │
│  [Generate Report]                                        │
└──────┬───────────────────────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────────────────────────────┐
│  Build API Request                                        │
│  • Determine role parameter based on user type           │
│  • Add date range                                         │
│  • Set limit to 100                                       │
└──────┬───────────────────────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────────────────────────────┐
│  Send POST Request to API                                 │
│  URL: {base_url}/api/user-log/filter                     │
└──────┬───────────────────────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────────────────────────────┐
│  API Processing (Backend)                                 │
│  • Validate request                                       │
│  • Query database with filters                           │
│  • Format response                                        │
└──────┬───────────────────────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────────────────────────────┐
│  Receive JSON Response                                    │
│  {                                                        │
│    "status": 1,                                           │
│    "total_records": 150,                                  │
│    "data": [...]                                          │
│  }                                                        │
└──────┬───────────────────────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────────────────────────────┐
│  Parse Response                                           │
│  • Check status                                           │
│  • Extract data array                                     │
│  • Create UserLogModel objects                           │
│  • Apply client-side filtering (for staff)               │
└──────┬───────────────────────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────────────────────────────┐
│  Update UI                                                │
│  • Show summary card with total count                    │
│  • Notify adapter of data change                         │
│  • Display RecyclerView                                   │
│  • Show success toast                                     │
└──────┬───────────────────────────────────────────────────┘
       │
       ▼
┌──────────────────────────────────────────────────────────┐
│  User Views Results                                       │
│  • Scroll through logs                                    │
│  • View details for each log                             │
│  • Apply different filters if needed                     │
└──────────────────────────────────────────────────────────┘
```

---

## Filter Logic Flow

### All User Log
```
User selects "All User Log"
    ↓
selectedUserType = ""
    ↓
API Request: { "from_date": "...", "to_date": "...", "limit": 100 }
    ↓
API returns all logs
    ↓
Display all logs without filtering
```

### Students
```
User selects "Students"
    ↓
selectedUserType = "student"
    ↓
API Request: { "role": "student", "from_date": "...", "to_date": "...", "limit": 100 }
    ↓
API returns only student logs
    ↓
Display student logs with class/section
```

### Parents
```
User selects "Parents"
    ↓
selectedUserType = "parent"
    ↓
API Request: { "role": "parent", "from_date": "...", "to_date": "...", "limit": 100 }
    ↓
API returns only parent logs
    ↓
Display parent logs without class/section
```

### Staff
```
User selects "Staff"
    ↓
selectedUserType = "staff"
    ↓
API Request: { "from_date": "...", "to_date": "...", "limit": 100 }
(No role parameter)
    ↓
API returns all logs
    ↓
Client-side filter: Exclude student and parent roles
    ↓
Display staff logs (Teacher, Admin, etc.)
```

---

## Class Diagram

```
┌─────────────────────────────────────────────────────────┐
│                    UserLogModel                          │
├─────────────────────────────────────────────────────────┤
│ - id: String                                             │
│ - user: String                                           │
│ - role: String                                           │
│ - classSectionId: String                                 │
│ - ipaddress: String                                      │
│ - userAgent: String                                      │
│ - loginDatetime: String                                  │
│ - classId: String                                        │
│ - className: String                                      │
│ - sectionId: String                                      │
│ - sectionName: String                                    │
│ - date: String                                           │
│ - time: String                                           │
│ - datetime: String                                       │
│ - classSection: String                                   │
├─────────────────────────────────────────────────────────┤
│ + getFormattedClassSection(): String                    │
│ + getFormattedRole(): String                            │
│ + getFormattedUser(): String                            │
│ + getFormattedIpAddress(): String                       │
│ + getFormattedDateTime(): String                        │
│ + getDeviceInfo(): String                               │
│ + getBrowserInfo(): String                              │
└─────────────────────────────────────────────────────────┘
                        ▲
                        │ uses
                        │
┌─────────────────────────────────────────────────────────┐
│                   UserLogAdapter                         │
├─────────────────────────────────────────────────────────┤
│ - context: Context                                       │
│ - userLogList: List<UserLogModel>                       │
├─────────────────────────────────────────────────────────┤
│ + onCreateViewHolder(): ViewHolder                      │
│ + onBindViewHolder(): void                              │
│ + getItemCount(): int                                   │
└─────────────────────────────────────────────────────────┘
                        ▲
                        │ uses
                        │
┌─────────────────────────────────────────────────────────┐
│              UserLogReportActivity                       │
├─────────────────────────────────────────────────────────┤
│ - userTypeSpinner: Spinner                              │
│ - fromDateEt: EditText                                  │
│ - toDateEt: EditText                                    │
│ - generateReportButton: Button                          │
│ - recyclerView: RecyclerView                            │
│ - adapter: UserLogAdapter                               │
│ - userLogList: List<UserLogModel>                       │
│ - selectedUserType: String                              │
├─────────────────────────────────────────────────────────┤
│ + onCreate(): void                                       │
│ + setupUserTypeSpinner(): void                          │
│ + setupDatePickers(): void                              │
│ + generateReport(): void                                │
│ + parseUserLogResponse(): void                          │
│ + showLoading(): void                                   │
│ + showData(): void                                      │
│ + showNoData(): void                                    │
└─────────────────────────────────────────────────────────┘
```

---

## State Diagram

```
┌─────────────┐
│   Initial   │
│   State     │
└──────┬──────┘
       │
       ▼
┌─────────────────────────────────────┐
│  Idle                                │
│  • Filters visible                   │
│  • No data displayed                 │
│  • Generate button enabled           │
└──────┬──────────────────────────────┘
       │ User taps Generate
       ▼
┌─────────────────────────────────────┐
│  Loading                             │
│  • Progress bar visible              │
│  • RecyclerView hidden               │
│  • Generate button disabled          │
└──────┬──────────────────────────────┘
       │
       ├─── Success ───┐
       │               ▼
       │        ┌─────────────────────────────────────┐
       │        │  Data Displayed                      │
       │        │  • Summary card visible              │
       │        │  • RecyclerView visible with data   │
       │        │  • Generate button enabled           │
       │        └──────┬──────────────────────────────┘
       │               │ User changes filters
       │               └──────────┐
       │                          │
       └─── No Data ───┐          │
                       ▼          │
                ┌─────────────────────────────────────┐
                │  Empty State                         │
                │  • No data layout visible            │
                │  • RecyclerView hidden               │
                │  • Generate button enabled           │
                └──────┬──────────────────────────────┘
                       │ User changes filters
                       └──────────┘
```

---

## Sequence Diagram

```
User          Activity        Adapter         API           Model
 │               │               │              │              │
 │─Select Filter─▶               │              │              │
 │               │               │              │              │
 │─Tap Generate─▶│               │              │              │
 │               │               │              │              │
 │               │─Show Loading─▶│              │              │
 │               │               │              │              │
 │               │─POST Request─────────────────▶              │
 │               │               │              │              │
 │               │◀─JSON Response───────────────│              │
 │               │               │              │              │
 │               │─Parse JSON───────────────────────────────▶  │
 │               │               │              │              │
 │               │◀─UserLogModel List──────────────────────────│
 │               │               │              │              │
 │               │─Update Data──▶│              │              │
 │               │               │              │              │
 │               │─Notify Change─▶│              │              │
 │               │               │              │              │
 │               │◀─Bind Views───│              │              │
 │               │               │              │              │
 │◀─Display Data─│               │              │              │
 │               │               │              │              │
```

---

## Technology Stack

### Android Components
- **Activity:** UserLogReportActivity (AppCompatActivity)
- **RecyclerView:** For displaying list of logs
- **Adapter:** UserLogAdapter (RecyclerView.Adapter)
- **ViewHolder:** UserLogViewHolder
- **Layouts:** XML layouts with Material Design

### Networking
- **Library:** Volley
- **Method:** POST
- **Format:** JSON

### Data Handling
- **Model:** UserLogModel (POJO)
- **Parser:** JSONObject/JSONArray
- **Storage:** ArrayList<UserLogModel>

### UI Components
- **Spinner:** User type selection
- **EditText:** Date fields (non-editable, clickable)
- **DatePickerDialog:** Date selection
- **Button:** Generate report
- **CardView:** Filter card, summary card, list items
- **ProgressBar:** Loading indicator
- **LinearLayout:** No data layout

---

## Design Patterns Used

1. **Model-View-Adapter (MVA)**
   - Model: UserLogModel
   - View: XML layouts
   - Adapter: UserLogAdapter

2. **ViewHolder Pattern**
   - Efficient RecyclerView item recycling
   - Reduces findViewById calls

3. **Singleton Pattern**
   - Volley RequestQueue
   - Utility classes

4. **Observer Pattern**
   - Spinner item selection listener
   - Click listeners

5. **Builder Pattern**
   - JSON request body building

---

## Performance Optimizations

1. **RecyclerView**
   - ViewHolder pattern for efficient recycling
   - Only visible items are rendered

2. **Network**
   - Limit parameter to prevent large responses
   - Volley caching for repeated requests

3. **UI**
   - Loading indicator during API calls
   - Smooth animations

4. **Memory**
   - ArrayList for dynamic sizing
   - Proper lifecycle management

---

## Security Considerations

1. **Authentication**
   - Client-Service header
   - Auth-Key header

2. **Data Validation**
   - Date format validation
   - Role parameter validation

3. **Error Handling**
   - Network errors caught
   - API errors handled gracefully
   - No sensitive data in logs

4. **Privacy**
   - IP addresses displayed (for admin use)
   - User agents shown (for security tracking)

