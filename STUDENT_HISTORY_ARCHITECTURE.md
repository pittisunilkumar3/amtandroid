# Student History (Admission Report) - Architecture Diagram

## 🏗️ System Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        Teacher Dashboard                         │
│                     (TeacherDashboard.java)                      │
└────────────────────────────┬────────────────────────────────────┘
                             │ Click "Reports" Icon
                             ↓
┌─────────────────────────────────────────────────────────────────┐
│                      Reports Main Screen                         │
│                   (TeacherReportsActivity.java)                  │
│                                                                   │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐       │
│  │ Student  │  │ Finance  │  │Attendance│  │   Exam   │       │
│  │   Info   │  │          │  │          │  │          │       │
│  └────┬─────┘  └──────────┘  └──────────┘  └──────────┘       │
│       │                                                          │
└───────┼──────────────────────────────────────────────────────────┘
        │ Click "Student Information"
        ↓
┌─────────────────────────────────────────────────────────────────┐
│                  Student Information Reports                     │
│              (TeacherReportCategoryActivity.java)                │
│                                                                   │
│  1. Student Report                                               │
│  2. Student History  ← Click Here                                │
│  3. Class Subject Report                                         │
│  4. Student Profile                                              │
│  ... (13 reports total)                                          │
└────────────────────────────┬────────────────────────────────────┘
                             │ Click "Student History"
                             ↓
┌─────────────────────────────────────────────────────────────────┐
│                    Student History Activity                      │
│                 (StudentHistoryActivity.java)                    │
│                                                                   │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                    Dropdown Filters                        │  │
│  │  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │  │
│  │  │   Session    │  │    Class     │  │   Section    │   │  │
│  │  │   Dropdown   │  │   Dropdown   │  │   Dropdown   │   │  │
│  │  └──────────────┘  └──────────────┘  └──────────────┘   │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                   │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │              [ Generate Report Button ]                    │  │
│  └───────────────────────────────────────────────────────────┘  │
│                             │                                     │
│                             ↓ Click                               │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                   Loading Indicator                        │  │
│  └───────────────────────────────────────────────────────────┘  │
│                             │                                     │
│                             ↓ API Call                            │
└─────────────────────────────┼─────────────────────────────────────┘
                              │
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                         Backend API                              │
│                                                                   │
│  POST /admission-report/filter                                   │
│                                                                   │
│  Headers:                                                        │
│    Client-Service: smartschool                                   │
│    Auth-Key: schoolAdmin@                                        │
│    Content-Type: application/json                                │
│                                                                   │
│  Body:                                                           │
│    {                                                             │
│      "class_id": 1,                                              │
│      "session_id": 18                                            │
│    }                                                             │
│                                                                   │
└────────────────────────────┬────────────────────────────────────┘
                             │ Response
                             ↓
┌─────────────────────────────────────────────────────────────────┐
│                       JSON Response                              │
│                                                                   │
│  {                                                               │
│    "status": 1,                                                  │
│    "message": "Admission report retrieved successfully",         │
│    "total_records": 25,                                          │
│    "data": [                                                     │
│      {                                                           │
│        "id": "123",                                              │
│        "admission_no": "ADM001",                                 │
│        "admission_date": "2024-04-15",                           │
│        "firstname": "John",                                      │
│        "lastname": "Doe",                                        │
│        "class": "Class 1",                                       │
│        "section": "A",                                           │
│        "session": "2024-2025",                                   │
│        "guardian_name": "Robert Doe",                            │
│        "is_active": "yes"                                        │
│      }                                                           │
│    ]                                                             │
│  }                                                               │
│                                                                   │
└────────────────────────────┬────────────────────────────────────┘
                             │ Parse Response
                             ↓
┌─────────────────────────────────────────────────────────────────┐
│                    StudentHistoryModel                           │
│                  (StudentHistoryModel.java)                      │
│                                                                   │
│  - id: String                                                    │
│  - admissionNo: String                                           │
│  - admissionDate: String                                         │
│  - firstname: String                                             │
│  - middlename: String                                            │
│  - lastname: String                                              │
│  - className: String                                             │
│  - sectionName: String                                           │
│  - sessionName: String                                           │
│  - guardianName: String                                          │
│  - guardianRelation: String                                      │
│  - guardianPhone: String                                         │
│  - mobileno: String                                              │
│  - isActive: String                                              │
│                                                                   │
│  + getFullName(): String                                         │
│  + getClassSection(): String                                     │
│  + getGuardianInfo(): String                                     │
│                                                                   │
└────────────────────────────┬────────────────────────────────────┘
                             │ Create List
                             ↓
┌─────────────────────────────────────────────────────────────────┐
│                   StudentHistoryAdapter                          │
│                (StudentHistoryAdapter.java)                      │
│                                                                   │
│  - context: Context                                              │
│  - studentList: List<StudentHistoryModel>                        │
│                                                                   │
│  + onCreateViewHolder()                                          │
│  + onBindViewHolder()                                            │
│  + getItemCount()                                                │
│                                                                   │
└────────────────────────────┬────────────────────────────────────┘
                             │ Bind Data
                             ↓
┌─────────────────────────────────────────────────────────────────┐
│                      RecyclerView                                │
│                                                                   │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │  ┌─────────────────────────────────────────────────────┐  │  │
│  │  │ 📚  John Michael Doe          [Admitted: 2024-04-15]│  │  │
│  │  │     Adm. No: ADM001                                  │  │  │
│  │  │  ─────────────────────────────────────────────────  │  │  │
│  │  │  • Class: Class 1 - A                               │  │  │
│  │  │  • Session: 2024-2025                               │  │  │
│  │  │  • Guardian: Robert Doe (Father)                    │  │  │
│  │  │  • 📱 9876543210    📞 9876543210                   │  │  │
│  │  │  • Status: Active                                   │  │  │
│  │  └─────────────────────────────────────────────────────┘  │  │
│  │                                                             │  │
│  │  ┌─────────────────────────────────────────────────────┐  │  │
│  │  │ 📚  Jane Smith                [Admitted: 2024-03-20]│  │  │
│  │  │     Adm. No: ADM002                                  │  │  │
│  │  │  ─────────────────────────────────────────────────  │  │  │
│  │  │  • Class: Class 1 - B                               │  │  │
│  │  │  • Session: 2024-2025                               │  │  │
│  │  │  • Guardian: Mary Smith (Mother)                    │  │  │
│  │  │  • 📱 9876543211    📞 9876543211                   │  │  │
│  │  │  • Status: Active                                   │  │  │
│  │  └─────────────────────────────────────────────────────┘  │  │
│  │                                                             │  │
│  │  ... (more cards)                                           │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                   │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🔄 Data Flow Diagram

```
┌──────────────┐
│    User      │
└──────┬───────┘
       │ 1. Select Filters
       ↓
┌──────────────────────────┐
│  StudentHistoryActivity  │
│                          │
│  loadReportData()        │
└──────┬───────────────────┘
       │ 2. Validate Filters
       ↓
┌──────────────────────────┐
│  fetchStudentHistory()   │
│                          │
│  - Build API URL         │
│  - Create JSON Body      │
│  - Set Headers           │
└──────┬───────────────────┘
       │ 3. HTTP POST Request
       ↓
┌──────────────────────────┐
│    Volley Library        │
│                          │
│  StringRequest           │
└──────┬───────────────────┘
       │ 4. Network Call
       ↓
┌──────────────────────────┐
│    Backend Server        │
│                          │
│  /admission-report/filter│
└──────┬───────────────────┘
       │ 5. JSON Response
       ↓
┌──────────────────────────┐
│  Response Listener       │
│                          │
│  onResponse()            │
└──────┬───────────────────┘
       │ 6. Parse JSON
       ↓
┌──────────────────────────┐
│parseStudentHistoryResponse│
│                          │
│  - Parse JSON Array      │
│  - Create Model Objects  │
│  - Add to List           │
└──────┬───────────────────┘
       │ 7. Update UI
       ↓
┌──────────────────────────┐
│  StudentHistoryAdapter   │
│                          │
│  notifyDataSetChanged()  │
└──────┬───────────────────┘
       │ 8. Display Data
       ↓
┌──────────────────────────┐
│     RecyclerView         │
│                          │
│  Show Admission Cards    │
└──────────────────────────┘
```

---

## 🏛️ Class Hierarchy

```
AppCompatActivity
    ↓ extends
TeacherReportDetailActivity
    ↓ extends
StudentHistoryActivity
    ↓ uses
StudentHistoryAdapter
    ↓ displays
StudentHistoryModel
```

---

## 📦 Component Relationships

```
┌─────────────────────────────────────────────────────────────┐
│                  StudentHistoryActivity                      │
│                                                              │
│  ┌────────────────────────────────────────────────────┐    │
│  │              Inherited from Base                    │    │
│  │                                                      │    │
│  │  - Session Dropdown Management                      │    │
│  │  - Class Dropdown Management                        │    │
│  │  - Section Dropdown Management                      │    │
│  │  - Loading State Management                         │    │
│  │  - Error State Management                           │    │
│  │  - Empty State Management                           │    │
│  │  - Content State Management                         │    │
│  └────────────────────────────────────────────────────┘    │
│                                                              │
│  ┌────────────────────────────────────────────────────┐    │
│  │              Custom Implementation                  │    │
│  │                                                      │    │
│  │  - loadReportData()                                 │    │
│  │  - fetchStudentHistory()                            │    │
│  │  - parseStudentHistoryResponse()                    │    │
│  └────────────────────────────────────────────────────┘    │
│                                                              │
│  ┌────────────────────────────────────────────────────┐    │
│  │              Components Used                        │    │
│  │                                                      │    │
│  │  - RecyclerView                                     │    │
│  │  - StudentHistoryAdapter                            │    │
│  │  - List<StudentHistoryModel>                        │    │
│  │  - Volley RequestQueue                              │    │
│  └────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

---

## 🎨 UI Component Structure

```
activity_teacher_report_detail.xml (Inherited Layout)
    ↓
┌─────────────────────────────────────────────────────────────┐
│  CoordinatorLayout                                           │
│                                                              │
│  ┌────────────────────────────────────────────────────┐    │
│  │  AppBarLayout                                       │    │
│  │  ┌──────────────────────────────────────────────┐  │    │
│  │  │  Toolbar                                      │  │    │
│  │  │  - Title: "Student History"                  │  │    │
│  │  │  - Back Button                                │  │    │
│  │  └──────────────────────────────────────────────┘  │    │
│  └────────────────────────────────────────────────────┘    │
│                                                              │
│  ┌────────────────────────────────────────────────────┐    │
│  │  Filter Section (CardView)                         │    │
│  │  ┌──────────────────────────────────────────────┐  │    │
│  │  │  Session Spinner                              │  │    │
│  │  └──────────────────────────────────────────────┘  │    │
│  │  ┌──────────────────────────────────────────────┐  │    │
│  │  │  Class Spinner                                │  │    │
│  │  └──────────────────────────────────────────────┘  │    │
│  │  ┌──────────────────────────────────────────────┐  │    │
│  │  │  Section Spinner                              │  │    │
│  │  └──────────────────────────────────────────────┘  │    │
│  │  ┌──────────────────────────────────────────────┐  │    │
│  │  │  Generate Report Button                       │  │    │
│  │  └──────────────────────────────────────────────┘  │    │
│  └────────────────────────────────────────────────────┘    │
│                                                              │
│  ┌────────────────────────────────────────────────────┐    │
│  │  Content Section (FrameLayout)                     │    │
│  │                                                      │    │
│  │  ┌──────────────────────────────────────────────┐  │    │
│  │  │  RecyclerView (report_content_recyclerView)  │  │    │
│  │  │                                               │  │    │
│  │  │  ┌────────────────────────────────────────┐  │  │    │
│  │  │  │  item_student_history.xml (Card)       │  │  │    │
│  │  │  │  - Student Name                        │  │  │    │
│  │  │  │  - Admission Number                    │  │  │    │
│  │  │  │  - Admission Date Badge                │  │  │    │
│  │  │  │  - Class & Section                     │  │  │    │
│  │  │  │  - Session                             │  │  │    │
│  │  │  │  - Guardian Info                       │  │  │    │
│  │  │  │  - Contact Numbers                     │  │  │    │
│  │  │  │  - Status                              │  │  │    │
│  │  │  └────────────────────────────────────────┘  │  │    │
│  │  │                                               │  │    │
│  │  │  (Repeated for each student)                  │  │    │
│  │  └──────────────────────────────────────────────┘  │    │
│  │                                                      │    │
│  │  ┌──────────────────────────────────────────────┐  │    │
│  │  │  Loading Indicator (ProgressBar)             │  │    │
│  │  └──────────────────────────────────────────────┘  │    │
│  │                                                      │    │
│  │  ┌──────────────────────────────────────────────┐  │    │
│  │  │  Empty State (TextView)                      │  │    │
│  │  └──────────────────────────────────────────────┘  │    │
│  └────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

---

## 🔐 Security & Authentication

```
┌─────────────────────────────────────────────────────────────┐
│                    Request Headers                           │
│                                                              │
│  Client-Service: smartschool                                 │
│  Auth-Key: schoolAdmin@                                      │
│  Content-Type: application/json                              │
│                                                              │
│  ↓ Validated by Backend                                      │
│                                                              │
│  ✅ Authentication Successful                                │
│  ✅ Authorization Granted                                    │
│  ✅ Request Processed                                        │
└─────────────────────────────────────────────────────────────┘
```

---

**Last Updated:** October 9, 2025  
**Version:** 1.0  
**Status:** Production Ready

