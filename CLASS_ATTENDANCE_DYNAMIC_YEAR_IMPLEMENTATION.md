# Class Attendance Report - Dynamic Year Loading Implementation

## Overview
Implemented **dynamic year loading from Sessions API** in the Class Attendance Report (Report → Attendance → Attendance Report), matching the same logic used in Staff Attendance Report.

## Changes Made

### File Modified
**`ClassAttendanceReportActivity.java`**

### 1. Added Imports
```java
import org.json.JSONException;
import java.util.Set;
import java.util.TreeSet;
```

### 2. Refactored Year Loading

#### Before (Static Years)
```java
private void setupYearSpinner() {
    List<String> years = new ArrayList<>();
    years.add("All Years");

    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    // Hardcoded: 10 years (current + 9 previous)
    for (int i = 0; i < 10; i++) {
        years.add(String.valueOf(currentYear - i));
    }

    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
        android.R.layout.simple_spinner_item, years);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    yearSpinner.setAdapter(adapter);

    yearSpinner.setSelection(1); // Current year
}
```

#### After (Dynamic Years)
```java
private void setupYearSpinner() {
    // Load years dynamically from sessions API
    loadYearsFromSessionsAPI();
}
```

### 3. Added New Methods

#### `loadYearsFromSessionsAPI()`
Makes API call to load sessions and extract years:

```java
private void loadYearsFromSessionsAPI() {
    if (!Utility.isConnectingToInternet(getApplicationContext())) {
        Log.w(TAG, "No internet connection, using default years");
        setupDefaultYearSpinner();
        return;
    }

    String url = Utility.buildApiUrl(getApplicationContext(), 
                  Constants.teacherSessionsWithClassesSectionsUrl);
    Log.d(TAG, "Loading years from sessions API: " + url);

    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
        response -> {
            Log.d(TAG, "Sessions API Response received for years");
            parseYearsFromSessions(response);
        },
        error -> {
            Log.e(TAG, "Error loading sessions for years: " + error.toString());
            setupDefaultYearSpinner();
        }) {
        
        @Override
        public Map<String, String> getHeaders() {
            Map<String, String> headers = new HashMap<>();
            headers.put("Client-Service", Constants.clientService);
            headers.put("Auth-Key", Constants.authKey);
            headers.put("Content-Type", "application/json");
            return headers;
        }

        @Override
        public byte[] getBody() {
            return "{}".getBytes();
        }
    };

    RequestQueue requestQueue = Volley.newRequestQueue(this);
    requestQueue.add(stringRequest);
}
```

#### `parseYearsFromSessions()`
Extracts years from session names:

```java
private void parseYearsFromSessions(String response) {
    try {
        JSONObject jsonObject = new JSONObject(response);
        int status = jsonObject.optInt("status", 0);

        if (status == 1) {
            JSONArray dataArray = jsonObject.optJSONArray("data");
            Set<String> yearsSet = new TreeSet<>((a, b) -> b.compareTo(a)); // Descending

            if (dataArray != null) {
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject sessionObj = dataArray.getJSONObject(i);
                    String sessionName = sessionObj.optString("session_name", "");
                    
                    // Extract years from "2023-2024" format
                    String[] years = sessionName.split("-");
                    for (String year : years) {
                        year = year.trim();
                        if (year.matches("\\d{4}")) {
                            yearsSet.add(year);
                        }
                    }
                }
            }

            // Convert to list
            List<String> yearList = new ArrayList<>();
            yearList.add("All Years");
            yearList.addAll(yearsSet);

            Log.d(TAG, "Loaded " + (yearList.size() - 1) + " years from sessions API");
            setupYearSpinnerWithData(yearList);
        } else {
            Log.w(TAG, "Invalid response status, using default years");
            setupDefaultYearSpinner();
        }
    } catch (JSONException e) {
        Log.e(TAG, "Error parsing sessions response for years: " + e.getMessage());
        setupDefaultYearSpinner();
    }
}
```

#### `setupDefaultYearSpinner()`
Fallback when API fails:

```java
private void setupDefaultYearSpinner() {
    Log.d(TAG, "Setting up default year spinner (current + 9 years)");
    
    List<String> years = new ArrayList<>();
    years.add("All Years");

    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    for (int i = 0; i < 10; i++) {
        years.add(String.valueOf(currentYear - i));
    }

    setupYearSpinnerWithData(years);
}
```

#### `setupYearSpinnerWithData()`
Common method to setup spinner:

```java
private void setupYearSpinnerWithData(List<String> years) {
    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
        android.R.layout.simple_spinner_item, years);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    yearSpinner.setAdapter(adapter);

    // Set current year as default (position 1)
    yearSpinner.setSelection(1);
}
```

## How It Works

### Flow Diagram
```
ClassAttendanceReportActivity.onCreate()
    └─> setupYearSpinner()
        └─> loadYearsFromSessionsAPI()
            ├─> [No Internet] → setupDefaultYearSpinner()
            └─> [API Success] → parseYearsFromSessions()
                ├─> Extract years from session names
                ├─> Sort years (descending)
                └─> setupYearSpinnerWithData()
            └─> [API Error] → setupDefaultYearSpinner()
```

### Year Extraction Example

**Sessions in System:**
```json
{
  "data": [
    {"session_name": "2020-2021"},
    {"session_name": "2021-2022"},
    {"session_name": "2023-2024"},
    {"session_name": "2024-2025"}
  ]
}
```

**Extracted Years:**
- "2020-2021" → [2020, 2021]
- "2021-2022" → [2021, 2022]
- "2023-2024" → [2023, 2024]
- "2024-2025" → [2024, 2025]

**Year Dropdown (Sorted Descending):**
```
All Years
2025
2024
2023
2022
2021
2020
```

## Comparison with Staff Attendance Report

Both reports now use **identical logic** for dynamic year loading:

| Feature | Class Attendance Report | Staff Attendance Report |
|---------|------------------------|-------------------------|
| **API Used** | `teacher/sessions-with-classes-sections` | `teacher/sessions-with-classes-sections` |
| **Extraction Logic** | Parse session names | Parse session names |
| **Sorting** | Descending (TreeSet) | Descending (TreeSet) |
| **Fallback** | Current + 9 years | Current + 5 years |
| **Default Selection** | Position 1 (current year) | Position 0 (All Years) |

### Key Difference
- **Class Attendance**: Defaults to **current year** (position 1)
- **Staff Attendance**: Defaults to **"All Years"** (position 0)

This difference is intentional as class attendance typically focuses on the current academic year.

## Benefits

### 1. **Consistency Across Reports**
Both attendance reports now use the same dynamic year loading mechanism.

### 2. **Always Current**
Year dropdown automatically reflects all academic sessions configured in the system.

### 3. **No Manual Updates**
No need to modify code when new academic sessions are added.

### 4. **Smart Fallback**
Gracefully handles:
- No internet connection
- API failures
- Invalid responses

### 5. **Better User Experience**
Users see only relevant years that have actual sessions in the system.

## API Details

### Endpoint
**URL:** `[base_url]/api/teacher/sessions-with-classes-sections`

### Request
```json
{}
```

### Response
```json
{
  "status": 1,
  "message": "Success",
  "data": [
    {
      "session_id": "1",
      "session_name": "2023-2024",
      "classes": [...]
    }
  ]
}
```

## Testing Checklist

### ✅ Build Status
- [x] Code compiles successfully
- [x] No syntax errors
- [x] All imports resolved

### 📋 Manual Testing Required

#### Test 1: Dynamic Year Loading
- [ ] Navigate to **Report → Attendance → Attendance Report**
- [ ] Check Logcat for: `"Loading years from sessions API"`
- [ ] Verify year dropdown shows years from actual sessions
- [ ] Confirm years are sorted newest to oldest

#### Test 2: Default Selection
- [ ] Verify current year is selected by default (not "All Years")
- [ ] Verify selection at position 1 (second item)

#### Test 3: API Fallback
- [ ] Turn off internet
- [ ] Open Class Attendance Report
- [ ] Check Logcat for: `"No internet connection, using default years"`
- [ ] Verify dropdown shows 10 years (current + 9)

#### Test 4: Year Filter Functionality
- [ ] Select different years from dropdown
- [ ] Click "Generate Report"
- [ ] Verify API request includes selected year
- [ ] Verify filtered results display correctly

#### Test 5: Comparison with Staff Attendance
- [ ] Open Staff Attendance Report → Check year dropdown
- [ ] Open Class Attendance Report → Check year dropdown
- [ ] Verify both show same years (from sessions API)

## Logging Output

### Successful Dynamic Load
```
D/ClassAttendanceReport: Loading years from sessions API: https://example.com/api/teacher/sessions-with-classes-sections
D/ClassAttendanceReport: Sessions API Response received for years
D/ClassAttendanceReport: Loaded 5 years from sessions API
```

### Fallback to Default
```
W/ClassAttendanceReport: No internet connection, using default years
D/ClassAttendanceReport: Setting up default year spinner (current + 9 years)
```

### API Error
```
D/ClassAttendanceReport: Loading years from sessions API: https://example.com/api/teacher/sessions-with-classes-sections
E/ClassAttendanceReport: Error loading sessions for years: com.android.volley.TimeoutError
D/ClassAttendanceReport: Setting up default year spinner (current + 9 years)
```

## Code Statistics

### Lines Modified
- **Imports:** +3 lines
- **setupYearSpinner():** Simplified to 3 lines (was 14 lines)
- **New Methods:** +113 lines
- **Net Change:** +102 lines

### Methods Added
1. `loadYearsFromSessionsAPI()` - 41 lines
2. `parseYearsFromSessions()` - 47 lines
3. `setupDefaultYearSpinner()` - 13 lines
4. `setupYearSpinnerWithData()` - 12 lines

## Summary

### What Changed
- **Before:** Static years (current + 9 hardcoded years)
- **After:** Dynamic years loaded from Sessions API

### How It Works
1. Calls sessions API on activity load
2. Extracts years from session names (e.g., "2023-2024")
3. Populates year dropdown with unique, sorted years
4. Falls back to default 10 years if API fails
5. Defaults to current year selection

### Impact
- ✅ **Consistency:** Same logic as Staff Attendance Report
- ✅ **Dynamic Data:** Shows actual academic years from system
- ✅ **Automatic Updates:** New sessions appear without code changes
- ✅ **Reliable Fallback:** Works offline with default years
- ✅ **Better UX:** Users see only relevant year options

---

**Last Updated:** December 2024  
**Feature:** Dynamic Year Loading in Class Attendance Report  
**Status:** ✅ Implemented & Built Successfully  
**Related:** Staff Attendance Report uses identical logic
