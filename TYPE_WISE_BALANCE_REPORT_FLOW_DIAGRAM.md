# Type Wise Balance Report - Flow Diagram

## 📊 Complete Data Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                  User Opens Report Activity                      │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ onCreate()
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Load Filter Options                           │
│                                                                   │
│  API: POST /api/session-fee-structure/list                       │
│  Response: {sessions, classes, fee_groups, fee_types}            │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ Populate Dropdowns
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Dropdowns Populated                           │
│                                                                   │
│  ✅ Session Dropdown:   [2024-25, 2025-26, ...]                  │
│  ✅ Class Dropdown:     [JR-BIPC, SR-MPC, ...]                   │
│  ✅ Section Dropdown:   [Cascading - loads after session+class]  │
│  ✅ Fee Group Dropdown: [2025-2026 -SR- 0NTC, ...]               │
│  ✅ Fee Type Dropdown:  [TUITION FEE (1), ...]                   │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ User Interaction
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    User Selects Filters                          │
│                                                                   │
│  1. Select Session (REQUIRED)                                    │
│  2. Select Class (Optional)                                      │
│  3. Select Section (Optional - cascading)                        │
│  4. Select Fee Group (Optional)                                  │
│  5. Select Fee Type (Optional)                                   │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ Click "Generate Report"
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Validate Session ID                           │
│                                                                   │
│  if (selectedSessionId == null || selectedSessionId.isEmpty())  │
│    → Show error: "Please select a Session"                      │
│    → Return                                                      │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ Validation Passed
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Build Request Payload                         │
│                                                                   │
│  JSONObject jsonBody = new JSONObject();                         │
│  jsonBody.put("session_id", selectedSessionId);  // REQUIRED     │
│                                                                   │
│  JSONArray feetypeIds = new JSONArray();                         │
│  if (selectedFeeTypeId != null) {                                │
│    feetypeIds.put(selectedFeeTypeId);                            │
│  }                                                               │
│  jsonBody.put("feetype_ids", feetypeIds);  // Array              │
│                                                                   │
│  if (selectedFeeGroupId != null) {                               │
│    JSONArray feegroupIds = new JSONArray();                      │
│    feegroupIds.put(selectedFeeGroupId);                          │
│    jsonBody.put("feegroup_ids", feegroupIds);  // Array          │
│  }                                                               │
│                                                                   │
│  if (selectedClassId != null) {                                  │
│    jsonBody.put("class_id", selectedClassId);                    │
│  }                                                               │
│                                                                   │
│  if (selectedSectionId != null) {                                │
│    jsonBody.put("section_id", selectedSectionId);                │
│  }                                                               │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ Send Request
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    API Request                                   │
│                                                                   │
│  POST /api/type-wise-balance-report/filter                       │
│                                                                   │
│  Headers:                                                        │
│    Content-Type: application/json                                │
│    Client-Service: smartschool                                   │
│    Auth-Key: schoolAdmin@                                        │
│                                                                   │
│  Body:                                                           │
│  {                                                               │
│    "session_id": "21",                                           │
│    "feetype_ids": ["33"],                                        │
│    "feegroup_ids": ["139"],                                      │
│    "class_id": "10",                                             │
│    "section_id": "15"                                            │
│  }                                                               │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ Response Received
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Parse API Response                            │
│                                                                   │
│  JSONObject jsonObject = new JSONObject(response);               │
│  int status = jsonObject.optInt("status", 0);                    │
│                                                                   │
│  if (status == 1) {                                              │
│    → Success: Parse data                                         │
│  } else {                                                        │
│    → Error: Show message                                         │
│  }                                                               │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ Status = 1 (Success)
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Parse Report Data                             │
│                                                                   │
│  reportDataList.clear();                                         │
│  JSONArray dataArray = jsonObject.optJSONArray("data");          │
│  int totalRecords = jsonObject.optInt("total_records", 0);       │
│                                                                   │
│  for (int i = 0; i < dataArray.length(); i++) {                  │
│    JSONObject recordJson = dataArray.getJSONObject(i);           │
│    TypeWiseBalanceReportData reportData =                        │
│        new TypeWiseBalanceReportData(recordJson);                │
│    reportDataList.add(reportData);                               │
│  }                                                               │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ Data Parsed
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    TypeWiseBalanceReportData                     │
│                                                                   │
│  Parse each record:                                              │
│    - admissionNo = "2025 SR-ONTC-53"                             │
│    - studentName = "MUTHAYA NAVANEETH"                           │
│      (firstname + middlename + lastname)                         │
│    - className = "SR-MPC"                                        │
│    - sectionName = "2025-26 SR SPARK"                            │
│    - feeType = "TUITION FEE"                                     │
│    - feeGroupName = "2025-2026 SR MPC"                           │
│    - mobileNo = "9949683860"                                     │
│    - total = "22000.00" (string)                                 │
│    - fine = "0.00" (string)                                      │
│    - totalAmount = 0 (integer)                                   │
│    - totalFine = 0 (integer)                                     │
│    - totalDiscount = 0 (integer)                                 │
│    - balance = "22000.00" (calculated if needed)                 │
│                                                                   │
│  Balance Calculation:                                            │
│    balance = total - totalAmount + totalFine - totalDiscount     │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ All Records Parsed
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Display Report Data                           │
│                                                                   │
│  displayReportData()                                             │
│    - Calculate totals                                            │
│    - Log summary                                                 │
│    - Prepare for RecyclerView (TODO: Create adapter)             │
│                                                                   │
│  showContent()                                                   │
│    - Hide progress bar                                           │
│    - Hide no data layout                                         │
│    - Show RecyclerView                                           │
│                                                                   │
│  Toast: "Report generated: 42 records"                           │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ Display Complete
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Report Displayed                              │
│                                                                   │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │  Type Wise Balance Report                                  │ │
│  │                                                             │ │
│  │  Filters Applied:                                          │ │
│  │    Session: 2024-25                                        │ │
│  │    Class: JR-BIPC                                          │ │
│  │    Section: 08199-JR-BIPC-B1                               │ │
│  │    Fee Group: 2025-2026 -SR- 0NTC                          │ │
│  │    Fee Type: TUITION FEE (1)                               │ │
│  │                                                             │ │
│  │  Total Records: 42                                         │ │
│  │  Total Amount: ₹924,000.00                                 │ │
│  │  Total Balance: ₹462,000.00                                │ │
│  │                                                             │ │
│  │  [RecyclerView with report data]                           │ │
│  └────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🔀 Error Handling Flow

```
API Request Sent
    ↓
┌─────────────────────────────────────────┐
│ Network Error?                          │
└─────────────────────────────────────────┘
    │
    ├─ YES → Extract error message from response
    │         └─ Show Toast with error message
    │         └─ showNoData()
    │
    └─ NO → Response Received
              ↓
          ┌─────────────────────────────────────────┐
          │ status == 1?                            │
          └─────────────────────────────────────────┘
              │
              ├─ YES → Parse data
              │         └─ dataArray.length() > 0?
              │             ├─ YES → Display data
              │             └─ NO → showNoData()
              │                     "No data found for selected filters"
              │
              └─ NO → showNoData()
                      Show API error message
```

---

## 📊 Request Payload Examples

### Example 1: All Fee Types (Empty Array)
```json
{
  "session_id": "21",
  "feetype_ids": []
}
```
**Result:** Returns all fee types for session 21

---

### Example 2: Specific Fee Type
```json
{
  "session_id": "21",
  "feetype_ids": ["33"]
}
```
**Result:** Returns only TUITION FEE data

---

### Example 3: Multiple Filters
```json
{
  "session_id": "21",
  "feetype_ids": ["33"],
  "feegroup_ids": ["139"],
  "class_id": "10",
  "section_id": "15"
}
```
**Result:** Returns TUITION FEE data for specific class/section/fee group

---

## 🎯 Key Decision Points

### 1. Session Validation
```
User clicks "Generate Report"
    ↓
Is session_id selected?
    ├─ NO → Show error "Please select a Session"
    │       Return (don't make API call)
    └─ YES → Continue to build request
```

### 2. Fee Type Array
```
Building request payload
    ↓
Is fee type selected?
    ├─ NO → feetype_ids = []  (empty array)
    │       API returns all fee types
    └─ YES → feetype_ids = [selectedFeeTypeId]
             API returns only selected fee type
```

### 3. Optional Filters
```
Building request payload
    ↓
For each optional filter (class, section, fee group):
    ├─ Selected? → Add to payload
    └─ Not selected? → Don't add to payload
```

---

## 📝 Code Flow

```java
// 1. User clicks Generate Report
generateReportButton.setOnClickListener(v -> {
    fetchTypeWiseBalanceReport();
});

// 2. Validate and build request
private void fetchTypeWiseBalanceReport() {
    // Validate session_id (required)
    if (selectedSessionId == null || selectedSessionId.isEmpty()) {
        Toast.makeText(this, "Please select a Session", Toast.LENGTH_SHORT).show();
        return;
    }
    
    // Build request payload
    JSONObject jsonBody = new JSONObject();
    jsonBody.put("session_id", selectedSessionId);  // Required
    
    JSONArray feetypeIds = new JSONArray();
    if (selectedFeeTypeId != null && !selectedFeeTypeId.isEmpty()) {
        feetypeIds.put(selectedFeeTypeId);
    }
    jsonBody.put("feetype_ids", feetypeIds);  // Array
    
    // Add optional filters...
    
    // Send request
    StringRequest request = new StringRequest(POST, url, ...);
    requestQueue.add(request);
}

// 3. Parse response
private void parseReportResponse(String response) {
    JSONObject jsonObject = new JSONObject(response);
    int status = jsonObject.optInt("status", 0);
    
    if (status == 1) {
        JSONArray dataArray = jsonObject.optJSONArray("data");
        
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject recordJson = dataArray.getJSONObject(i);
            TypeWiseBalanceReportData data = new TypeWiseBalanceReportData(recordJson);
            reportDataList.add(data);
        }
        
        displayReportData();
        showContent();
    } else {
        showNoData();
    }
}

// 4. Display data
private void displayReportData() {
    // Calculate totals
    double totalBalance = 0;
    double totalAmount = 0;
    
    for (TypeWiseBalanceReportData data : reportDataList) {
        totalBalance += Double.parseDouble(data.balance);
        totalAmount += Double.parseDouble(data.total);
    }
    
    // TODO: Set adapter to RecyclerView
    // TODO: Display summary cards
}
```

---

## 🔍 Data Transformation

```
API Response (JSON)
    ↓
{
  "admission_no": "2025 SR-ONTC-53",
  "firstname": "MUTHAYA",
  "middlename": null,
  "lastname": "NAVANEETH",
  "class": "SR-MPC",
  "section": "2025-26 SR SPARK",
  "type": "TUITION FEE",
  "feegroupname": "2025-2026 SR MPC",
  "mobileno": "9949683860",
  "total": "22000.00",
  "fine": "0.00",
  "total_amount": 0,
  "total_fine": 0,
  "total_discount": 0,
  "balance": "22000.00"
}
    ↓
TypeWiseBalanceReportData Object
    ↓
{
  admissionNo: "2025 SR-ONTC-53"
  studentName: "MUTHAYA NAVANEETH"  ← Constructed from firstname + middlename + lastname
  className: "SR-MPC"
  sectionName: "2025-26 SR SPARK"
  feeType: "TUITION FEE"
  feeGroupName: "2025-2026 SR MPC"
  mobileNo: "9949683860"
  total: "22000.00"
  fine: "0.00"
  totalAmount: 0
  totalFine: 0
  totalDiscount: 0
  balance: "22000.00"  ← Calculated if not provided
}
    ↓
Display in RecyclerView
```

---

This diagram shows the complete flow from user interaction to data display!

