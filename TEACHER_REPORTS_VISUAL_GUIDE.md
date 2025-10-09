# Teacher Reports with Dropdowns - Visual Guide

## Screen Flow

```
┌─────────────────────────────────────┐
│     Teacher Dashboard               │
│                                     │
│  ┌─────┐  ┌─────┐  ┌─────┐        │
│  │ 📊  │  │ 👥  │  │ 📝  │        │
│  │Rpts │  │Stud │  │Exam │        │
│  └─────┘  └─────┘  └─────┘        │
│                                     │
│  Click Reports Icon ───────────────┼──┐
└─────────────────────────────────────┘  │
                                         │
                                         ▼
┌─────────────────────────────────────┐
│   TeacherReportsActivity            │
│   (15 Report Categories)            │
│                                     │
│  ┌──────────────┐ ┌──────────────┐ │
│  │ 👤 Student   │ │ 💰 Finance   │ │
│  │ Information  │ │              │ │
│  └──────────────┘ └──────────────┘ │
│                                     │
│  ┌──────────────┐ ┌──────────────┐ │
│  │ 📅 Attendance│ │ 📚 Library   │ │
│  │              │ │              │ │
│  └──────────────┘ └──────────────┘ │
│                                     │
│  Click Student Information ────────┼──┐
└─────────────────────────────────────┘  │
                                         │
                                         ▼
┌─────────────────────────────────────┐
│ TeacherReportCategoryActivity       │
│ Student Information Reports         │
│                                     │
│  ┌─────────────────────────────┐   │
│  │ 👤 Student Report        ➤  │   │
│  └─────────────────────────────┘   │
│  ┌─────────────────────────────┐   │
│  │ 📜 Student History       ➤  │   │
│  └─────────────────────────────┘   │
│  ┌─────────────────────────────┐   │
│  │ 📚 Class Subject Report  ➤  │   │
│  └─────────────────────────────┘   │
│  ┌─────────────────────────────┐   │
│  │ 👥 Student Profile       ➤  │   │
│  └─────────────────────────────┘   │
│                                     │
│  Click Student Report ──────────────┼──┐
└─────────────────────────────────────┘  │
                                         │
                                         ▼
┌─────────────────────────────────────┐
│ TeacherReportDetailActivity         │
│ Student Report                      │
│                                     │
│ ┌─────────────────────────────────┐ │
│ │ 🔍 Report Filters               │ │
│ │                                 │ │
│ │ Session:  [Select Session ▼]   │ │
│ │                                 │ │
│ │ Class:    [Select Class   ▼]   │ │
│ │                                 │ │
│ │ Section:  [Select Section ▼]   │ │
│ │                                 │ │
│ │ [  Generate Report  ]           │ │
│ └─────────────────────────────────┘ │
│                                     │
│ ┌─────────────────────────────────┐ │
│ │ 📊 Report Content               │ │
│ │                                 │ │
│ │ (RecyclerView with report data) │ │
│ │                                 │ │
│ └─────────────────────────────────┘ │
└─────────────────────────────────────┘
```

## Dropdown Cascading Behavior

### Step 1: Initial State
```
┌─────────────────────────────────────┐
│ Session:  [Select Session      ▼]  │
│                                     │
│ Class:    [Select Class        ▼]  │ ← Disabled
│                                     │
│ Section:  [Select Section      ▼]  │ ← Disabled
└─────────────────────────────────────┘
```

### Step 2: After Selecting Session
```
┌─────────────────────────────────────┐
│ Session:  [2024-25             ▼]  │ ✓ Selected
│                                     │
│ Class:    [Select Class        ▼]  │ ← Now shows classes
│           • JR-MPC                  │   for 2024-25
│           • SR-MPC                  │
│           • JR-CEC                  │
│                                     │
│ Section:  [Select Section      ▼]  │ ← Still disabled
└─────────────────────────────────────┘
```

### Step 3: After Selecting Class
```
┌─────────────────────────────────────┐
│ Session:  [2024-25             ▼]  │ ✓ Selected
│                                     │
│ Class:    [JR-MPC              ▼]  │ ✓ Selected
│                                     │
│ Section:  [Select Section      ▼]  │ ← Now shows sections
│           • A                       │   for JR-MPC
│           • B                       │
│           • C                       │
└─────────────────────────────────────┘
```

### Step 4: After Selecting Section
```
┌─────────────────────────────────────┐
│ Session:  [2024-25             ▼]  │ ✓ Selected
│                                     │
│ Class:    [JR-MPC              ▼]  │ ✓ Selected
│                                     │
│ Section:  [A                   ▼]  │ ✓ Selected
│                                     │
│ [  Generate Report  ] ← Now enabled │
└─────────────────────────────────────┘
```

## API Data Flow

```
┌──────────────────────────────────────────────────────────┐
│                    Activity Lifecycle                     │
└──────────────────────────────────────────────────────────┘
                           │
                           ▼
┌──────────────────────────────────────────────────────────┐
│              onCreate() - Initialize Views                │
└──────────────────────────────────────────────────────────┘
                           │
                           ▼
┌──────────────────────────────────────────────────────────┐
│         loadSessionsFromAPI()                             │
│                                                           │
│  POST /teacher/sessions-with-classes-sections            │
│  Headers:                                                 │
│    - Client-Service: smartschool                         │
│    - Auth-Key: schoolAdmin@                              │
│  Body: {}                                                 │
└──────────────────────────────────────────────────────────┘
                           │
                           ▼
┌──────────────────────────────────────────────────────────┐
│              API Response (JSON)                          │
│                                                           │
│  {                                                        │
│    "status": 1,                                          │
│    "data": [                                             │
│      {                                                    │
│        "session_id": "21",                               │
│        "session_name": "2024-25",                        │
│        "classes": [                                      │
│          {                                                │
│            "class_id": "22",                             │
│            "class_name": "JR-MPC",                       │
│            "sections": [                                 │
│              {                                            │
│                "section_id": "14",                       │
│                "section_name": "A"                       │
│              }                                            │
│            ]                                              │
│          }                                                │
│        ]                                                  │
│      }                                                    │
│    ]                                                      │
│  }                                                        │
└──────────────────────────────────────────────────────────┘
                           │
                           ▼
┌──────────────────────────────────────────────────────────┐
│         parseSessionsResponse()                           │
│                                                           │
│  • Parse JSON response                                   │
│  • Create SessionData objects                            │
│  • Create ClassData objects (nested)                     │
│  • Create SectionData objects (nested)                   │
│  • Store in sessionsList                                 │
└──────────────────────────────────────────────────────────┘
                           │
                           ▼
┌──────────────────────────────────────────────────────────┐
│         setupSessionSpinner()                             │
│                                                           │
│  • Create list of session names                          │
│  • Add "Select Session" as first item                    │
│  • Create ArrayAdapter                                   │
│  • Set adapter to sessionSpinner                         │
└──────────────────────────────────────────────────────────┘
                           │
                           ▼
┌──────────────────────────────────────────────────────────┐
│              User Interaction                             │
│                                                           │
│  User selects session → loadClassesForSession()          │
│  User selects class   → loadSectionsForClass()           │
│  User selects section → selectedSectionId set            │
│  User clicks button   → generateReport()                 │
└──────────────────────────────────────────────────────────┘
```

## Class Hierarchy

```
┌─────────────────────────────────────────────────────────┐
│                    BaseActivity                          │
│  (Provided by framework)                                │
└─────────────────────────────────────────────────────────┘
                           │
                           │ extends
                           ▼
┌─────────────────────────────────────────────────────────┐
│          TeacherReportDetailActivity                     │
│  (Base class for all report activities)                │
│                                                          │
│  Fields:                                                 │
│    - sessionsList: List<SessionData>                    │
│    - classesList: List<ClassData>                       │
│    - sectionsList: List<SectionData>                    │
│    - selectedSessionId: String                          │
│    - selectedClassId: String                            │
│    - selectedSectionId: String                          │
│                                                          │
│  Methods:                                                │
│    + onCreate()                                          │
│    + loadSessionsFromAPI()                              │
│    + parseSessionsResponse()                            │
│    + setupSessionSpinner()                              │
│    + setupClassSpinner()                                │
│    + setupSectionSpinner()                              │
│    + generateReport()                                    │
│    # loadReportData() ← Override in child classes       │
│    # showLoading()                                       │
│    # hideLoading()                                       │
│    # showNoData()                                        │
│    # showContent()                                       │
│    # getSelectedSessionId()                             │
│    # getSelectedClassId()                               │
│    # getSelectedSectionId()                             │
└─────────────────────────────────────────────────────────┘
                           │
                           │ extends (future)
                           ▼
┌─────────────────────────────────────────────────────────┐
│            StudentReportActivity                         │
│  (Specific implementation for Student Report)           │
│                                                          │
│  Methods:                                                │
│    @ loadReportData() ← Overridden                      │
│      - Get selected filters                             │
│      - Call student report API                          │
│      - Parse response                                    │
│      - Update RecyclerView                              │
│      - Show content or no data                          │
└─────────────────────────────────────────────────────────┘
```

## Data Structures

```
┌─────────────────────────────────────────────────────────┐
│                    SessionData                           │
│                                                          │
│  + id: String                                           │
│  + name: String                                         │
│  + classes: List<ClassData>                             │
│                                                          │
│  Example:                                                │
│    id = "21"                                            │
│    name = "2024-25"                                     │
│    classes = [ClassData, ClassData, ...]                │
└─────────────────────────────────────────────────────────┘
                           │
                           │ contains
                           ▼
┌─────────────────────────────────────────────────────────┐
│                     ClassData                            │
│                                                          │
│  + id: String                                           │
│  + name: String                                         │
│  + sections: List<SectionData>                          │
│                                                          │
│  Example:                                                │
│    id = "22"                                            │
│    name = "JR-MPC"                                      │
│    sections = [SectionData, SectionData, ...]           │
└─────────────────────────────────────────────────────────┘
                           │
                           │ contains
                           ▼
┌─────────────────────────────────────────────────────────┐
│                    SectionData                           │
│                                                          │
│  + id: String                                           │
│  + name: String                                         │
│                                                          │
│  Example:                                                │
│    id = "14"                                            │
│    name = "A"                                           │
└─────────────────────────────────────────────────────────┘
```

## UI Component Hierarchy

```
activity_teacher_report_detail.xml

LinearLayout (Root)
│
├── FrameLayout (Action Bar)
│   └── Toolbar
│       ├── ImageView (Back Button)
│       └── TextView (Title)
│
└── ScrollView
    └── LinearLayout
        │
        ├── CardView (Filter Card)
        │   └── LinearLayout
        │       ├── TextView (Filter Title)
        │       │
        │       ├── TextView (Session Label)
        │       ├── FrameLayout
        │       │   └── Spinner (session_spinner)
        │       │
        │       ├── TextView (Class Label)
        │       ├── FrameLayout
        │       │   └── Spinner (class_spinner)
        │       │
        │       ├── TextView (Section Label)
        │       ├── FrameLayout
        │       │   └── Spinner (section_spinner)
        │       │
        │       └── Button (generate_report_button)
        │
        └── LinearLayout (Report Content Section)
            ├── ProgressBar (progressBar)
            ├── LinearLayout (nodata_layout)
            │   ├── ImageView (No Data Icon)
            │   ├── TextView (No Data Title)
            │   └── TextView (No Data Message)
            └── RecyclerView (report_content_recyclerView)
```

## State Management

```
┌─────────────────────────────────────────────────────────┐
│                   Loading State                          │
│                                                          │
│  progressBar.visibility = VISIBLE                       │
│  nodataLayout.visibility = GONE                         │
│  reportContentRecyclerView.visibility = GONE            │
│                                                          │
│  ┌─────────────────────────────────────────┐           │
│  │                                          │           │
│  │              ⏳ Loading...               │           │
│  │                                          │           │
│  └─────────────────────────────────────────┘           │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│                   No Data State                          │
│                                                          │
│  progressBar.visibility = GONE                          │
│  nodataLayout.visibility = VISIBLE                      │
│  reportContentRecyclerView.visibility = GONE            │
│                                                          │
│  ┌─────────────────────────────────────────┐           │
│  │              📊                          │           │
│  │        No report data                    │           │
│  │  Please select filters and generate      │           │
│  └─────────────────────────────────────────┘           │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│                   Content State                          │
│                                                          │
│  progressBar.visibility = GONE                          │
│  nodataLayout.visibility = GONE                         │
│  reportContentRecyclerView.visibility = VISIBLE         │
│                                                          │
│  ┌─────────────────────────────────────────┐           │
│  │ • Student 1 - Roll No: 001              │           │
│  │ • Student 2 - Roll No: 002              │           │
│  │ • Student 3 - Roll No: 003              │           │
│  │ • Student 4 - Roll No: 004              │           │
│  └─────────────────────────────────────────┘           │
└─────────────────────────────────────────────────────────┘
```

## Summary

This visual guide shows:
- ✅ Complete screen flow from dashboard to report detail
- ✅ Cascading dropdown behavior step-by-step
- ✅ API data flow and processing
- ✅ Class hierarchy and inheritance
- ✅ Data structure relationships
- ✅ UI component hierarchy
- ✅ State management (loading, no data, content)

All components work together to provide a seamless experience for teachers to generate reports with proper filtering.

