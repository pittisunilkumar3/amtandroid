# Online Admission Report - Flow Diagram

## 📊 Complete Flow Visualization

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         TEACHER DASHBOARD                                │
│                                                                          │
│  ┌──────┐  ┌──────┐  ┌──────┐  ┌──────┐  ┌──────┐  ┌──────┐          │
│  │ Home │  │ Stud │  │ Exam │  │ Fees │  │REPORT│  │ More │          │
│  └──────┘  └──────┘  └──────┘  └──────┘  └──────┘  └──────┘          │
│                                              ▲                           │
│                                              │ Click                    │
└──────────────────────────────────────────────┼──────────────────────────┘
                                               │
                                               ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                      REPORTS CATEGORIES                                  │
│                                                                          │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐        │
│  │   STUDENT INFO  │  │   ATTENDANCE    │  │    FINANCE      │        │
│  │   📊 Reports    │  │   📅 Reports    │  │   💰 Reports    │        │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘        │
│           ▲                                                              │
│           │ Click                                                        │
└───────────┼──────────────────────────────────────────────────────────────┘
            │
            ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                   STUDENT INFORMATION REPORTS                            │
│                                                                          │
│  • Student Report                                                        │
│  • Student History                                                       │
│  • Class Subject Report                                                  │
│  • Student Profile Report                                                │
│  • Online Admission Report  ◄── YOU ARE HERE                            │
│  • Class Section Report                                                  │
│  • Student Login Credential                                              │
│  • Admission Report                                                      │
│           ▲                                                              │
│           │ Click                                                        │
└───────────┼──────────────────────────────────────────────────────────────┘
            │
            ▼
┌─────────────────────────────────────────────────────────────────────────┐
│              ONLINE ADMISSION REPORT ACTIVITY                            │
│  ┌───────────────────────────────────────────────────────────────────┐  │
│  │  ◄ Back    Online Admission Report                               │  │
│  └───────────────────────────────────────────────────────────────────┘  │
│                                                                          │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │  Session:  [2024-2025 ▼]                                        │   │
│  │  Class:    [Class 10   ▼]                                       │   │
│  │  Section:  [Section A  ▼]                                       │   │
│  │                                                                  │   │
│  │  [        Generate Report        ]                              │   │
│  └─────────────────────────────────────────────────────────────────┘   │
│           │                                                              │
│           │ Click Generate                                               │
│           ▼                                                              │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │  ⏳ Loading...                                                   │   │
│  └─────────────────────────────────────────────────────────────────┘   │
└───────────┼──────────────────────────────────────────────────────────────┘
            │
            ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                          API CALL                                        │
│                                                                          │
│  POST https://school.cyberdetox.in/api/online-admission/filter          │
│                                                                          │
│  Headers:                                                                │
│    Client-Service: smartschool                                           │
│    Auth-Key: schoolAdmin@                                                │
│    Content-Type: application/json                                        │
│                                                                          │
│  Body:                                                                   │
│    {                                                                     │
│      "class_id": 19,                                                     │
│      "section_id": 47                                                    │
│    }                                                                     │
│           │                                                              │
│           ▼                                                              │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │  Response:                                                       │   │
│  │  {                                                               │   │
│  │    "status": 1,                                                  │   │
│  │    "message": "Success",                                         │   │
│  │    "total_records": 15,                                          │   │
│  │    "data": [...]                                                 │   │
│  │  }                                                               │   │
│  └─────────────────────────────────────────────────────────────────┘   │
└───────────┼──────────────────────────────────────────────────────────────┘
            │
            ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                      DATA PARSING                                        │
│                                                                          │
│  parseOnlineAdmissionResponse(response)                                  │
│    │                                                                     │
│    ├─► Parse JSON                                                       │
│    ├─► Check status                                                     │
│    ├─► Extract data array                                               │
│    ├─► Loop through records                                             │
│    │     │                                                               │
│    │     ├─► Create OnlineAdmissionModel                                │
│    │     ├─► Set basic info (id, reference_no, admission_no)            │
│    │     ├─► Set name fields (full_name, firstname, etc.)               │
│    │     ├─► Set personal info (dob, gender, email, mobile)             │
│    │     ├─► Set parent info (father, mother, guardian)                 │
│    │     ├─► Parse class_info object                                    │
│    │     ├─► Set status fields (is_enroll, paid_status)                 │
│    │     └─► Add to admissionList                                       │
│    │                                                                     │
│    └─► Update adapter                                                   │
│                                                                          │
└───────────┼──────────────────────────────────────────────────────────────┘
            │
            ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                    DISPLAY RESULTS                                       │
│                                                                          │
│  ┌───────────────────────────────────────────────────────────────────┐  │
│  │  ╔═══════════════════════════════════════════════════════════╗   │  │
│  │  ║  John Doe Smith                    [Enrolled]             ║   │  │
│  │  ║  Ref No: REF2024001                                       ║   │  │
│  │  ║  Adm No: ADM2024001                                       ║   │  │
│  │  ║  ─────────────────────────────────────────────────────    ║   │  │
│  │  ║  Class: Class 10 - Section A                             ║   │  │
│  │  ║  Gender: Male          DOB: 2010-05-15                   ║   │  │
│  │  ║  Contact: 9876543210                                     ║   │  │
│  │  ║  Email: john@example.com                                 ║   │  │
│  │  ║  Father: Robert Smith                                    ║   │  │
│  │  ║  ─────────────────────────────────────────────────────    ║   │  │
│  │  ║  Admission Date: 2024-01-15        Payment: Paid         ║   │  │
│  │  ╚═══════════════════════════════════════════════════════════╝   │  │
│  │                                                                   │  │
│  │  ╔═══════════════════════════════════════════════════════════╗   │  │
│  │  ║  Jane Smith                    [Not Enrolled]             ║   │  │
│  │  ║  Ref No: REF2024002                                       ║   │  │
│  │  ║  ─────────────────────────────────────────────────────    ║   │  │
│  │  ║  Class: Class 10 - Section A                             ║   │  │
│  │  ║  Gender: Female        DOB: 2010-08-20                   ║   │  │
│  │  ║  Contact: 9876543211                                     ║   │  │
│  │  ║  ─────────────────────────────────────────────────────    ║   │  │
│  │  ║  Admission Date: 2024-01-16        Payment: Unpaid       ║   │  │
│  │  ╚═══════════════════════════════════════════════════════════╝   │  │
│  │                                                                   │  │
│  │  ... more cards ...                                               │  │
│  └───────────────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 🔄 State Flow Diagram

```
┌─────────────┐
│   INITIAL   │
│   STATE     │
└──────┬──────┘
       │
       │ User clicks "Generate Report"
       ▼
┌─────────────┐
│   LOADING   │ ◄─── showLoading()
│   STATE     │      • Show progress bar
└──────┬──────┘      • Hide content
       │             • Hide error
       │
       │ API Call
       ▼
┌─────────────────────────────────┐
│     API RESPONSE                │
└─────────┬───────────────────────┘
          │
          ├─────────────┬─────────────┬─────────────┐
          │             │             │             │
          ▼             ▼             ▼             ▼
    ┌─────────┐   ┌─────────┐   ┌─────────┐   ┌─────────┐
    │ SUCCESS │   │ NO DATA │   │  ERROR  │   │ NETWORK │
    │ status=1│   │ data=[] │   │ status=0│   │  ERROR  │
    └────┬────┘   └────┬────┘   └────┬────┘   └────┬────┘
         │             │             │             │
         ▼             ▼             ▼             ▼
    ┌─────────┐   ┌─────────┐   ┌─────────┐   ┌─────────┐
    │ CONTENT │   │ NO DATA │   │  ERROR  │   │  ERROR  │
    │  STATE  │   │  STATE  │   │  STATE  │   │  STATE  │
    └─────────┘   └─────────┘   └─────────┘   └─────────┘
         │             │             │             │
         ▼             ▼             ▼             ▼
    Show cards    Show message  Show error    Show error
    in RecyclerView  "No data"   message       message
```

---

## 🎨 UI Component Hierarchy

```
OnlineAdmissionReportActivity
│
├── TeacherReportDetailActivity (Parent)
│   │
│   ├── ActionBar
│   │   ├── Back Button
│   │   └── Title TextView
│   │
│   ├── Filter Section
│   │   ├── Session Spinner
│   │   ├── Class Spinner
│   │   ├── Section Spinner
│   │   └── Generate Report Button
│   │
│   ├── Content Section
│   │   ├── ProgressBar (Loading State)
│   │   ├── RecyclerView (Content State)
│   │   │   └── OnlineAdmissionAdapter
│   │   │       └── Multiple CardViews
│   │   │           └── item_online_admission.xml
│   │   ├── NoData Layout (No Data State)
│   │   └── Error Layout (Error State)
│   │
│   └── State Management
│       ├── showLoading()
│       ├── showContent()
│       ├── showNoData()
│       └── showError()
│
└── Custom Methods
    ├── loadReportData()
    ├── fetchOnlineAdmissions()
    └── parseOnlineAdmissionResponse()
```

---

## 📦 Data Flow

```
┌──────────────────────────────────────────────────────────────┐
│                    DATA FLOW                                  │
└──────────────────────────────────────────────────────────────┘

API Response (JSON)
    │
    ▼
parseOnlineAdmissionResponse()
    │
    ├─► JSONObject jsonObject = new JSONObject(response)
    │
    ├─► int status = jsonObject.optInt("status", 0)
    │
    ├─► JSONArray dataArray = jsonObject.optJSONArray("data")
    │
    └─► for each item in dataArray
            │
            ├─► JSONObject admissionObj = dataArray.getJSONObject(i)
            │
            ├─► OnlineAdmissionModel admission = new OnlineAdmissionModel()
            │
            ├─► admission.setId(admissionObj.optString("id"))
            ├─► admission.setReferenceNo(admissionObj.optString("reference_no"))
            ├─► admission.setFullName(admissionObj.optString("full_name"))
            │   ... (set all fields)
            │
            ├─► Parse nested class_info object
            │   │
            │   ├─► JSONObject classInfo = admissionObj.optJSONObject("class_info")
            │   ├─► admission.setClassId(classInfo.optString("class_id"))
            │   └─► admission.setClassName(classInfo.optString("class_name"))
            │
            └─► admissionList.add(admission)
                    │
                    ▼
            adapter.notifyDataSetChanged()
                    │
                    ▼
            OnlineAdmissionAdapter.onBindViewHolder()
                    │
                    ├─► holder.studentNameTv.setText(admission.getFullName())
                    ├─► holder.referenceNoTv.setText(admission.getReferenceNo())
                    ├─► holder.enrollmentStatusTv.setText(admission.getEnrollmentStatus())
                    │   ... (bind all fields)
                    │
                    └─► Display in RecyclerView
```

---

## 🔐 Authentication Flow

```
┌──────────────────────────────────────────────────────────────┐
│                 AUTHENTICATION FLOW                           │
└──────────────────────────────────────────────────────────────┘

Teacher Login
    │
    ├─► Store credentials in SharedPreferences
    │   ├─► teacherStaffId
    │   ├─► userId
    │   └─► authToken (if applicable)
    │
    ▼
Navigate to Reports
    │
    ▼
Online Admission Report
    │
    ├─► Build API URL
    │   └─► Utility.buildApiUrl(context, Constants.onlineAdmissionFilterUrl)
    │
    ├─► Add Headers
    │   ├─► Client-Service: smartschool (from Constants.clientService)
    │   ├─► Auth-Key: schoolAdmin@ (from Constants.authKey)
    │   └─► Content-Type: application/json
    │
    ├─► Add Request Body
    │   ├─► class_id: from filter selection
    │   └─► section_id: from filter selection
    │
    └─► Make API Call
        │
        ├─► Success → Parse and display data
        └─► Error → Show error message
```

---

## 📊 Class Diagram

```
┌─────────────────────────────────────────────────────────────┐
│           OnlineAdmissionReportActivity                      │
├─────────────────────────────────────────────────────────────┤
│ - TAG: String                                                │
│ - reportContentRecyclerView: RecyclerView                    │
│ - adapter: OnlineAdmissionAdapter                            │
│ - admissionList: List<OnlineAdmissionModel>                  │
├─────────────────────────────────────────────────────────────┤
│ + onCreate(Bundle): void                                     │
│ + getReportTitle(): String                                   │
│ # loadReportData(): void                                     │
│ - fetchOnlineAdmissions(String, String, String): void        │
│ - parseOnlineAdmissionResponse(String): void                 │
└─────────────────────────────────────────────────────────────┘
                        │
                        │ extends
                        ▼
┌─────────────────────────────────────────────────────────────┐
│           TeacherReportDetailActivity                        │
├─────────────────────────────────────────────────────────────┤
│ # sessionSpinner: Spinner                                    │
│ # classSpinner: Spinner                                      │
│ # sectionSpinner: Spinner                                    │
│ # generateReportButton: Button                               │
├─────────────────────────────────────────────────────────────┤
│ # getSelectedSessionId(): String                             │
│ # getSelectedClassId(): String                               │
│ # getSelectedSectionId(): String                             │
│ # showLoading(): void                                        │
│ # showContent(): void                                        │
│ # showNoData(String): void                                   │
│ # showError(String): void                                    │
└─────────────────────────────────────────────────────────────┘
```

---

**Last Updated**: 2025-10-09
**Version**: 1.0

