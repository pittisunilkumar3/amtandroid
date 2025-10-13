# Daily Attendance Report - Flow Diagram

## User Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                      Teacher Dashboard                           │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Reports Section                             │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                   Attendance Category                            │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│              Daily Attendance Report Activity                    │
│                                                                  │
│  ┌────────────────────────────────────────────────────────┐    │
│  │              Filter Card                                │    │
│  │  ┌──────────────────────────────────────────────┐      │    │
│  │  │  📅 Date Picker: [Select Date]               │      │    │
│  │  └──────────────────────────────────────────────┘      │    │
│  │  ┌──────────────────────────────────────────────┐      │    │
│  │  │  [Generate Report]                            │      │    │
│  │  └──────────────────────────────────────────────┘      │    │
│  └────────────────────────────────────────────────────────┘    │
│                                                                  │
│  User clicks "Generate Report"                                  │
│                             │                                    │
│                             ▼                                    │
│  ┌────────────────────────────────────────────────────────┐    │
│  │              Loading Indicator                          │    │
│  └────────────────────────────────────────────────────────┘    │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                      API Call                                    │
│  POST /api/daily-attendance-report/filter                       │
│  Headers:                                                        │
│    - Client-Service: smartschool                                │
│    - Auth-Key: schoolAdmin@                                     │
│  Body: { "date": "2025-10-07" }                                 │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Backend Processing                            │
│  1. Validate date parameter                                     │
│  2. Query attendance records for the date                       │
│  3. Group by class and section                                  │
│  4. Calculate statistics                                        │
│  5. Return JSON response                                        │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Response Received                             │
│  {                                                               │
│    "status": 1,                                                  │
│    "summary": { ... },                                           │
│    "data": [ ... ]                                               │
│  }                                                               │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│              Daily Attendance Report Activity                    │
│                                                                  │
│  ┌────────────────────────────────────────────────────────┐    │
│  │              Summary Card                               │    │
│  │  Total Students: 450                                    │    │
│  │  Total Present: 420                                     │    │
│  │  Total Absent: 30                                       │    │
│  │  Present %: 93.33%                                      │    │
│  │  Date: 07 Oct 2025                                      │    │
│  └────────────────────────────────────────────────────────┘    │
│                                                                  │
│  ┌────────────────────────────────────────────────────────┐    │
│  │         Attendance List (RecyclerView)                  │    │
│  │                                                          │    │
│  │  ┌──────────────────────────────────────────────┐      │    │
│  │  │  Class 10 - A              Total: 45         │      │    │
│  │  │  ████████████████░░░░ 93%                    │      │    │
│  │  │  Present: 38  Excuse: 2  Late: 1             │      │    │
│  │  │  Half Day: 1  Absent: 3                      │      │    │
│  │  │  Total Present: 42  Absent: 7%               │      │    │
│  │  └──────────────────────────────────────────────┘      │    │
│  │                                                          │    │
│  │  ┌──────────────────────────────────────────────┐      │    │
│  │  │  Class 10 - B              Total: 42         │      │    │
│  │  │  ████████████████░░░░ 90%                    │      │    │
│  │  │  Present: 35  Excuse: 3  Late: 0             │      │    │
│  │  │  Half Day: 0  Absent: 4                      │      │    │
│  │  │  Total Present: 38  Absent: 10%              │      │    │
│  │  └──────────────────────────────────────────────┘      │    │
│  │                                                          │    │
│  │  ... more classes ...                                   │    │
│  └────────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────────┘
```

---

## Component Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                  DailyAttendanceReportActivity                   │
│                                                                  │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────┐ │
│  │   UI Components  │  │  Data Models     │  │  API Service │ │
│  │                  │  │                  │  │              │ │
│  │  - Date Picker   │  │  - Model List    │  │  - Volley    │ │
│  │  - Button        │  │  - Summary Data  │  │  - Headers   │ │
│  │  - RecyclerView  │  │                  │  │  - Request   │ │
│  │  - Summary Card  │  │                  │  │  - Response  │ │
│  └────────┬─────────┘  └────────┬─────────┘  └──────┬───────┘ │
│           │                     │                    │         │
│           └─────────────────────┼────────────────────┘         │
│                                 │                              │
└─────────────────────────────────┼──────────────────────────────┘
                                  │
                                  ▼
┌─────────────────────────────────────────────────────────────────┐
│              DailyAttendanceReportAdapter                        │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  onCreateViewHolder()                                     │  │
│  │    - Inflate item_daily_attendance_report.xml             │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  onBindViewHolder()                                       │  │
│  │    - Set class/section name                               │  │
│  │    - Set attendance counts                                │  │
│  │    - Update progress bar                                  │  │
│  │    - Apply color coding                                   │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  ViewHolder                                               │  │
│  │    - TextView references                                  │  │
│  │    - ProgressBar reference                                │  │
│  │    - CardView reference                                   │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                                  │
                                  ▼
┌─────────────────────────────────────────────────────────────────┐
│              DailyAttendanceReportModel                          │
│                                                                  │
│  Fields:                                                         │
│    - classId, className                                          │
│    - sectionId, sectionName                                      │
│    - present, excuse, absent, late, halfDay                      │
│    - totalStudent, totalPresent                                  │
│    - presentPercent, absentPercent                               │
│                                                                  │
│  Methods:                                                        │
│    - Getters/Setters                                             │
│    - getClassSectionDisplay()                                    │
│    - getPresentPercentageInt()                                   │
│    - getAbsentPercentageInt()                                    │
└─────────────────────────────────────────────────────────────────┘
```

---

## Data Flow

```
┌──────────────┐
│     User     │
└──────┬───────┘
       │ 1. Selects date
       ▼
┌──────────────────────┐
│   Date Picker        │
│   (Calendar Dialog)  │
└──────┬───────────────┘
       │ 2. Date selected
       ▼
┌──────────────────────┐
│   Activity           │
│   - selectedDate     │
└──────┬───────────────┘
       │ 3. Click Generate
       ▼
┌──────────────────────┐
│   API Request        │
│   - Build JSON       │
│   - Set headers      │
│   - Send POST        │
└──────┬───────────────┘
       │ 4. Network call
       ▼
┌──────────────────────┐
│   Backend API        │
│   - Process request  │
│   - Query database   │
│   - Calculate stats  │
└──────┬───────────────┘
       │ 5. JSON response
       ▼
┌──────────────────────┐
│   Response Handler   │
│   - Parse JSON       │
│   - Create models    │
└──────┬───────────────┘
       │ 6. Update UI
       ▼
┌──────────────────────┐
│   Summary Card       │
│   - Total students   │
│   - Total present    │
│   - Percentages      │
└──────────────────────┘
       │
       ▼
┌──────────────────────┐
│   RecyclerView       │
│   - Adapter          │
│   - ViewHolder       │
│   - Item views       │
└──────────────────────┘
       │
       ▼
┌──────────────────────┐
│   Display to User    │
└──────────────────────┘
```

---

## State Management

```
┌─────────────────────────────────────────────────────────────────┐
│                      Activity States                             │
└─────────────────────────────────────────────────────────────────┘

Initial State:
┌──────────────────────┐
│  - Date picker shown │
│  - Today's date set  │
│  - No data visible   │
└──────────────────────┘

Loading State:
┌──────────────────────┐
│  - Progress bar ON   │
│  - RecyclerView OFF  │
│  - Summary card OFF  │
│  - No data OFF       │
└──────────────────────┘

Success State:
┌──────────────────────┐
│  - Progress bar OFF  │
│  - RecyclerView ON   │
│  - Summary card ON   │
│  - No data OFF       │
└──────────────────────┘

No Data State:
┌──────────────────────┐
│  - Progress bar OFF  │
│  - RecyclerView OFF  │
│  - Summary card OFF  │
│  - No data ON        │
└──────────────────────┘

Error State:
┌──────────────────────┐
│  - Progress bar OFF  │
│  - RecyclerView OFF  │
│  - Summary card OFF  │
│  - No data ON        │
│  - Toast message     │
└──────────────────────┘
```

---

## Color Coding Logic

```
┌─────────────────────────────────────────────────────────────────┐
│                   Attendance Percentage                          │
└─────────────────────────────────────────────────────────────────┘

Input: presentPercent (e.g., "93%")
       │
       ▼
┌──────────────────────┐
│  Parse to integer    │
│  Remove "%" symbol   │
└──────┬───────────────┘
       │
       ▼
┌──────────────────────────────────────────────────────────┐
│  if (percent >= 90)                                      │
│    ├─ Progress bar: Green (#4CAF50)                      │
│    └─ Text color: Green                                  │
│                                                           │
│  else if (percent >= 75)                                 │
│    ├─ Progress bar: Orange (#FF9800)                     │
│    └─ Text color: Orange                                 │
│                                                           │
│  else                                                     │
│    ├─ Progress bar: Red (#F44336)                        │
│    └─ Text color: Red                                    │
└───────────────────────────────────────────────────────────┘
```

---

## Error Handling Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                      Error Scenarios                             │
└─────────────────────────────────────────────────────────────────┘

No Internet:
┌──────────────────────┐
│  Check connectivity  │
│         │            │
│         ▼            │
│  Show toast message  │
│  "No internet"       │
└──────────────────────┘

API Error:
┌──────────────────────┐
│  Volley error        │
│         │            │
│         ▼            │
│  Log error details   │
│         │            │
│         ▼            │
│  Show toast message  │
│  Hide loading        │
│  Show no data        │
└──────────────────────┘

JSON Parse Error:
┌──────────────────────┐
│  JSONException       │
│         │            │
│         ▼            │
│  Log exception       │
│         │            │
│         ▼            │
│  Show toast message  │
│  "Error parsing"     │
│  Show no data        │
└──────────────────────┘

Empty Response:
┌──────────────────────┐
│  status = 1          │
│  data array empty    │
│         │            │
│         ▼            │
│  Show no data layout │
│  "No attendance      │
│   data found"        │
└──────────────────────┘
```

---

## Summary

This flow diagram illustrates:
1. **User Journey** - From dashboard to viewing report
2. **Component Architecture** - How classes interact
3. **Data Flow** - From user input to display
4. **State Management** - Different UI states
5. **Color Coding** - Visual feedback logic
6. **Error Handling** - Graceful error management

The implementation follows a clean, modular architecture with proper separation of concerns and comprehensive error handling.

