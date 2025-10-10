# Section Dropdown - Visual Guide

## 📊 Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                    Finance Report Activity                       │
│         (TypeWiseBalanceReport / FeeCollectionColumnWise)        │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ onCreate()
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Load Initial Filters                          │
│                                                                   │
│  API: POST /api/session-fee-structure/list                       │
│  Response: {sessions, classes, fee_groups, fee_types}            │
│  Note: NO sections in response!                                  │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ Populate Dropdowns
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Initial Dropdown State                        │
│                                                                   │
│  ✅ Session Dropdown:   [Select Session (Optional)]              │
│  ✅ Class Dropdown:     [Select Class (Optional)]                │
│  ❌ Section Dropdown:   [Select Section (Optional)] - EMPTY      │
│  ✅ Fee Group Dropdown: [Select Fee Group (Optional)]            │
│  ✅ Fee Type Dropdown:  [Select Fee Type (Optional)]             │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ User Interaction
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    User Selects Session                          │
│                                                                   │
│  Session Spinner: onItemSelected()                               │
│    ├─ selectedSessionId = "21"                                   │
│    └─ loadSectionsForSelectedFilters()                           │
│         ├─ Check: selectedSessionId != null? ✅                  │
│         ├─ Check: selectedClassId != null? ❌                    │
│         └─ Return (sections not loaded yet)                      │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ User continues
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    User Selects Class                            │
│                                                                   │
│  Class Spinner: onItemSelected()                                 │
│    ├─ selectedClassId = "10"                                     │
│    └─ loadSectionsForSelectedFilters()                           │
│         ├─ Check: selectedSessionId != null? ✅                  │
│         ├─ Check: selectedClassId != null? ✅                    │
│         └─ Proceed to load sections                              │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ API Call
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Load Sections from API                        │
│                                                                   │
│  API: POST /teacher/sessions-with-classes-sections               │
│  Request Body: {}                                                │
│  Response: Hierarchical data (sessions → classes → sections)     │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ Parse Response
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Parse Sections Response                       │
│                                                                   │
│  parseSectionsFromResponse()                                     │
│    ├─ Loop through sessions array                                │
│    ├─ Find session with id = "21"                                │
│    ├─ Loop through classes array                                 │
│    ├─ Find class with id = "10"                                  │
│    ├─ Extract sections array                                     │
│    └─ Populate sectionsList                                      │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ Update UI
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Section Dropdown Updated                      │
│                                                                   │
│  setupSectionSpinner()                                           │
│    ├─ Add "Select Section (Optional)"                            │
│    ├─ Add "08199-JR-BIPC-B1"                                     │
│    ├─ Add "08199-JR-BIPC-B2"                                     │
│    ├─ Add "08199-JR-BIPC-B3"                                     │
│    └─ ... (all sections for selected session/class)             │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ User can now select
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    User Selects Section                          │
│                                                                   │
│  Section Spinner: onItemSelected()                               │
│    └─ selectedSectionId = "15"                                   │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ Generate Report
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Report Generation                             │
│                                                                   │
│  API: POST /api/type-wise-balance-report/filter                  │
│  Request Body: {                                                 │
│    "session_id": "21",                                           │
│    "class_id": "10",                                             │
│    "section_id": "15"  ← Section ID included!                    │
│  }                                                               │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🔄 State Transitions

### State 1: Initial Load
```
┌──────────────────────────────────────────┐
│  Session:  [ Select Session (Optional) ] │
│  Class:    [ Select Class (Optional)   ] │
│  Section:  [ Select Section (Optional) ] │ ← Empty (no data)
│  Fee Group:[ Select Fee Group (Optional)]│
│  Fee Type: [ Select Fee Type (Optional) ]│
└──────────────────────────────────────────┘
```

### State 2: Session Selected
```
┌──────────────────────────────────────────┐
│  Session:  [ 2024-25 ▼               ]   │ ← Selected
│  Class:    [ Select Class (Optional) ]   │
│  Section:  [ Select Section (Optional)]  │ ← Still empty
│  Fee Group:[ Select Fee Group (Optional)]│
│  Fee Type: [ Select Fee Type (Optional) ]│
└──────────────────────────────────────────┘

Action: loadSectionsForSelectedFilters() called
Result: No sections loaded (class not selected)
```

### State 3: Session + Class Selected
```
┌──────────────────────────────────────────┐
│  Session:  [ 2024-25 ▼               ]   │ ← Selected
│  Class:    [ JR-BIPC ▼               ]   │ ← Selected
│  Section:  [ Select Section (Optional)]  │ ← Loading...
│  Fee Group:[ Select Fee Group (Optional)]│
│  Fee Type: [ Select Fee Type (Optional) ]│
└──────────────────────────────────────────┘

Action: loadSectionsForSelectedFilters() called
Result: API call to load sections
```

### State 4: Sections Loaded
```
┌──────────────────────────────────────────┐
│  Session:  [ 2024-25 ▼                ]  │ ← Selected
│  Class:    [ JR-BIPC ▼                ]  │ ← Selected
│  Section:  [ 08199-JR-BIPC-B1 ▼       ]  │ ← Populated!
│            [ 08199-JR-BIPC-B2         ]  │
│            [ 08199-JR-BIPC-B3         ]  │
│  Fee Group:[ Select Fee Group (Optional)]│
│  Fee Type: [ Select Fee Type (Optional) ]│
└──────────────────────────────────────────┘

Action: Sections loaded and displayed
Result: User can now select a section
```

### State 5: All Filters Selected
```
┌──────────────────────────────────────────┐
│  Session:  [ 2024-25 ▼                ]  │ ← Selected
│  Class:    [ JR-BIPC ▼                ]  │ ← Selected
│  Section:  [ 08199-JR-BIPC-B1 ▼       ]  │ ← Selected
│  Fee Group:[ 2025-2026 -SR- 0NTC ▼    ]  │ ← Selected
│  Fee Type: [ TUITION FEE (1) ▼        ]  │ ← Selected
└──────────────────────────────────────────┘

Action: User clicks "Generate Report"
Result: Report generated with all filters
```

---

## 🔀 Decision Flow

```
User Selects Session or Class
    ↓
loadSectionsForSelectedFilters() called
    ↓
┌─────────────────────────────────┐
│ Is selectedSessionId != null?   │
└─────────────────────────────────┘
    │
    ├─ NO → Clear sections, return
    │
    └─ YES
        ↓
    ┌─────────────────────────────────┐
    │ Is selectedClassId != null?     │
    └─────────────────────────────────┘
        │
        ├─ NO → Clear sections, return
        │
        └─ YES
            ↓
        ┌─────────────────────────────────┐
        │ Make API call to load sections  │
        └─────────────────────────────────┘
            ↓
        ┌─────────────────────────────────┐
        │ Parse response                  │
        │ Filter by session + class       │
        └─────────────────────────────────┘
            ↓
        ┌─────────────────────────────────┐
        │ Update section dropdown         │
        └─────────────────────────────────┘
```

---

## 📱 User Interface Flow

### Step 1: Open Report
```
┌────────────────────────────────────────────────┐
│  Type Wise Balance Report                      │
├────────────────────────────────────────────────┤
│                                                 │
│  ┌──────────────────────────────────────────┐ │
│  │  Filters                                  │ │
│  │                                           │ │
│  │  Session:  [Select Session (Optional) ▼] │ │
│  │  Class:    [Select Class (Optional)   ▼] │ │
│  │  Section:  [Select Section (Optional) ▼] │ │ ← Empty
│  │  Fee Group:[Select Fee Group (Optional)▼]│ │
│  │  Fee Type: [Select Fee Type (Optional) ▼]│ │
│  │                                           │ │
│  │  [  Generate Report  ]                    │ │
│  └──────────────────────────────────────────┘ │
│                                                 │
└────────────────────────────────────────────────┘
```

### Step 2: Select Session
```
┌────────────────────────────────────────────────┐
│  Type Wise Balance Report                      │
├────────────────────────────────────────────────┤
│                                                 │
│  ┌──────────────────────────────────────────┐ │
│  │  Filters                                  │ │
│  │                                           │ │
│  │  Session:  [2024-25 ▼                  ] │ │ ← Selected
│  │  Class:    [Select Class (Optional)   ▼] │ │
│  │  Section:  [Select Section (Optional) ▼] │ │ ← Still empty
│  │  Fee Group:[Select Fee Group (Optional)▼]│ │
│  │  Fee Type: [Select Fee Type (Optional) ▼]│ │
│  │                                           │ │
│  │  [  Generate Report  ]                    │ │
│  └──────────────────────────────────────────┘ │
│                                                 │
└────────────────────────────────────────────────┘
```

### Step 3: Select Class (Sections Load)
```
┌────────────────────────────────────────────────┐
│  Type Wise Balance Report                      │
├────────────────────────────────────────────────┤
│                                                 │
│  ┌──────────────────────────────────────────┐ │
│  │  Filters                                  │ │
│  │                                           │ │
│  │  Session:  [2024-25 ▼                  ] │ │ ← Selected
│  │  Class:    [JR-BIPC ▼                  ] │ │ ← Selected
│  │  Section:  [Select Section (Optional) ▼] │ │ ← Loading...
│  │  Fee Group:[Select Fee Group (Optional)▼]│ │
│  │  Fee Type: [Select Fee Type (Optional) ▼]│ │
│  │                                           │ │
│  │  [  Generate Report  ]                    │ │
│  └──────────────────────────────────────────┘ │
│                                                 │
│  ⏳ Loading sections...                        │
└────────────────────────────────────────────────┘
```

### Step 4: Sections Loaded
```
┌────────────────────────────────────────────────┐
│  Type Wise Balance Report                      │
├────────────────────────────────────────────────┤
│                                                 │
│  ┌──────────────────────────────────────────┐ │
│  │  Filters                                  │ │
│  │                                           │ │
│  │  Session:  [2024-25 ▼                  ] │ │
│  │  Class:    [JR-BIPC ▼                  ] │ │
│  │  Section:  [08199-JR-BIPC-B1 ▼         ] │ │ ← Populated!
│  │            ├ Select Section (Optional)    │ │
│  │            ├ 08199-JR-BIPC-B1             │ │
│  │            ├ 08199-JR-BIPC-B2             │ │
│  │            ├ 08199-JR-BIPC-B3             │ │
│  │            └ ...                           │ │
│  │  Fee Group:[Select Fee Group (Optional)▼]│ │
│  │  Fee Type: [Select Fee Type (Optional) ▼]│ │
│  │                                           │ │
│  │  [  Generate Report  ]                    │ │
│  └──────────────────────────────────────────┘ │
│                                                 │
└────────────────────────────────────────────────┘
```

---

## 🔍 Code Flow Diagram

```
setupListeners()
    │
    ├─ sessionSpinner.setOnItemSelectedListener()
    │   └─ onItemSelected()
    │       ├─ Set selectedSessionId
    │       └─ Call loadSectionsForSelectedFilters()
    │
    ├─ classSpinner.setOnItemSelectedListener()
    │   └─ onItemSelected()
    │       ├─ Set selectedClassId
    │       └─ Call loadSectionsForSelectedFilters()
    │
    └─ sectionSpinner.setOnItemSelectedListener()
        └─ onItemSelected()
            └─ Set selectedSectionId

loadSectionsForSelectedFilters()
    │
    ├─ Check if session and class are selected
    │   ├─ If NO → Clear sections, return
    │   └─ If YES → Continue
    │
    ├─ Build API URL
    ├─ Create StringRequest
    ├─ Set headers (Client-Service, Auth-Key, Content-Type)
    ├─ Set request body ({})
    └─ Add to RequestQueue
        │
        └─ On Success → parseSectionsFromResponse()

parseSectionsFromResponse(response)
    │
    ├─ Parse JSON response
    ├─ Check status == 1
    ├─ Clear sectionsList
    │
    ├─ Loop through sessions array
    │   └─ Find session with id == selectedSessionId
    │       │
    │       └─ Loop through classes array
    │           └─ Find class with id == selectedClassId
    │               │
    │               └─ Loop through sections array
    │                   └─ Create SectionData objects
    │                       └─ Add to sectionsList
    │
    └─ Call setupSectionSpinner()

setupSectionSpinner()
    │
    ├─ Create sectionNames list
    ├─ Add "Select Section (Optional)"
    ├─ Loop through sectionsList
    │   └─ Add section.name to sectionNames
    │
    ├─ Create ArrayAdapter
    └─ Set adapter to sectionSpinner
```

---

## 📊 Data Flow

```
API Response (sessions-with-classes-sections)
    ↓
{
  "data": [
    {
      "session_id": "21",
      "classes": [
        {
          "class_id": "10",
          "sections": [
            {"section_id": "15", "section_name": "08199-JR-BIPC-B1"},
            {"section_id": "16", "section_name": "08199-JR-BIPC-B2"}
          ]
        }
      ]
    }
  ]
}
    ↓
parseSectionsFromResponse()
    ↓
Filter by selectedSessionId = "21"
    ↓
Filter by selectedClassId = "10"
    ↓
Extract sections array
    ↓
sectionsList = [
  {id: "15", name: "08199-JR-BIPC-B1"},
  {id: "16", name: "08199-JR-BIPC-B2"}
]
    ↓
setupSectionSpinner()
    ↓
Section Dropdown = [
  "Select Section (Optional)",
  "08199-JR-BIPC-B1",
  "08199-JR-BIPC-B2"
]
```

---

## 🎯 Key Points

1. **Cascading Logic:** Sections only load when BOTH session AND class are selected
2. **API Reuse:** Uses existing `/teacher/sessions-with-classes-sections` API
3. **Filtering:** Parses hierarchical response and filters by selected session/class
4. **Dynamic Updates:** Sections update automatically when session or class changes
5. **Clear on Deselect:** Sections clear when session or class is deselected
6. **Optional Filter:** Section remains optional - reports work without it

---

This visual guide helps understand the complete flow of the section dropdown implementation!

