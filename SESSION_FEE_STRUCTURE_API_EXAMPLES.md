# Session Fee Structure Reports - API Examples

## Overview

This document provides complete API request/response examples for testing the Session Fee Structure Reports implementation.

---

## 1. Session Fee Structure List API

### Purpose
Load filter options (sessions, classes, fee groups, fee types) for the dropdown filters.

### Request

**Method:** POST  
**Endpoint:** `/api/session-fee-structure/list`  
**Base URL:** `http://localhost/amt/api` (or your configured base URL)

**Headers:**
```http
Content-Type: application/json
Client-Service: smartschool
Auth-Key: schoolAdmin@
```

**Body:**
```json
{}
```

### cURL Example
```bash
curl -X POST "http://localhost/amt/api/session-fee-structure/list" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d '{}'
```

### Response Example

**Success (200 OK):**
```json
{
  "status": 1,
  "message": "Session fee structure filter options retrieved successfully",
  "sessions": [
    {
      "id": "7",
      "session": "2016-17",
      "is_active": "no",
      "created_at": "2017-04-20 12:12:19",
      "updated_at": "0000-00-00",
      "active": "0"
    },
    {
      "id": "11",
      "session": "2017-18",
      "is_active": "no",
      "created_at": "2017-04-20 12:11:37",
      "updated_at": "0000-00-00",
      "active": "0"
    },
    {
      "id": "21",
      "session": "2025-26",
      "is_active": "yes",
      "created_at": "2024-03-17 14:17:56",
      "updated_at": null,
      "active": "1"
    }
  ],
  "classes": [
    {
      "id": "10",
      "class": "JR-BIPC",
      "is_active": "no",
      "created_at": "2024-03-17 14:17:56",
      "updated_at": null
    },
    {
      "id": "11",
      "class": "JR-CEC",
      "is_active": "no",
      "created_at": "2024-03-17 14:25:05",
      "updated_at": null
    }
  ],
  "fee_groups": [
    {
      "id": "25",
      "name": "2020-202108199OTHERFEE",
      "is_system": "0",
      "description": "OTHERFEE",
      "is_active": "no",
      "created_at": "2024-04-06 16:01:55"
    },
    {
      "id": "139",
      "name": "2025-2026 -SR- 0NTC",
      "is_system": "0",
      "description": "",
      "is_active": "no",
      "created_at": "2024-03-17 14:17:56"
    }
  ],
  "fee_types": [
    {
      "id": "21",
      "is_system": "0",
      "feecategory_id": null,
      "type": "Topper Discount",
      "code": "discount123",
      "is_active": "no",
      "description": "",
      "created_at": "2023-08-11 17:13:43",
      "updated_at": null
    },
    {
      "id": "33",
      "is_system": "0",
      "feecategory_id": null,
      "type": "TUITION FEE",
      "code": "1",
      "is_active": "no",
      "description": "",
      "created_at": "2023-12-09 21:59:20",
      "updated_at": null
    },
    {
      "id": "40",
      "is_system": "0",
      "feecategory_id": null,
      "type": "ADMISSION FEE",
      "code": "8",
      "is_active": "no",
      "description": "ADMISSION FEE\r\n",
      "created_at": "2023-12-09 22:00:20",
      "updated_at": null
    }
  ],
  "note": "Use the filter endpoint with parameters to get session fee structure data",
  "timestamp": "2025-10-10 12:54:03"
}
```

**Error (401 Unauthorized):**
```json
{
  "status": 0,
  "message": "Unauthorized access"
}
```

---

## 2. Type Wise Balance Report API

### Purpose
Generate Type Wise Balance Report with optional filters.

### Request

**Method:** POST  
**Endpoint:** `/api/type-wise-balance-report/filter`  
**Base URL:** `http://localhost/amt/api` (or your configured base URL)

**Headers:**
```http
Content-Type: application/json
Client-Service: smartschool
Auth-Key: schoolAdmin@
```

### Example 1: No Filters (All Data)

**Body:**
```json
{}
```

**cURL:**
```bash
curl -X POST "http://localhost/amt/api/type-wise-balance-report/filter" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d '{}'
```

### Example 2: Session Filter Only

**Body:**
```json
{
  "session_id": "21"
}
```

**cURL:**
```bash
curl -X POST "http://localhost/amt/api/type-wise-balance-report/filter" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d '{"session_id":"21"}'
```

### Example 3: Multiple Filters

**Body:**
```json
{
  "session_id": "21",
  "class_id": "10",
  "fee_group_id": "139",
  "fee_type_id": "40"
}
```

**cURL:**
```bash
curl -X POST "http://localhost/amt/api/type-wise-balance-report/filter" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d '{"session_id":"21","class_id":"10","fee_group_id":"139","fee_type_id":"40"}'
```

### Example 4: All Filters

**Body:**
```json
{
  "session_id": "21",
  "class_id": "10",
  "section_id": "11",
  "fee_group_id": "139",
  "fee_type_id": "40"
}
```

**cURL:**
```bash
curl -X POST "http://localhost/amt/api/type-wise-balance-report/filter" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d '{"session_id":"21","class_id":"10","section_id":"11","fee_group_id":"139","fee_type_id":"40"}'
```

---

## 3. Fee Collection Report Column Wise API

### Purpose
Generate Fee Collection Report Column Wise with optional filters including date range.

### Request

**Method:** POST  
**Endpoint:** `/api/fee-collection-report-column-wise/filter`  
**Base URL:** `http://localhost/amt/api` (or your configured base URL)

**Headers:**
```http
Content-Type: application/json
Client-Service: smartschool
Auth-Key: schoolAdmin@
```

### Example 1: No Filters (All Data)

**Body:**
```json
{}
```

**cURL:**
```bash
curl -X POST "http://localhost/amt/api/fee-collection-report-column-wise/filter" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d '{}'
```

### Example 2: Date Range Only

**Body:**
```json
{
  "from_date": "2024-01-01",
  "to_date": "2024-12-31"
}
```

**cURL:**
```bash
curl -X POST "http://localhost/amt/api/fee-collection-report-column-wise/filter" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d '{"from_date":"2024-01-01","to_date":"2024-12-31"}'
```

### Example 3: Session and Class Filters

**Body:**
```json
{
  "session_id": "21",
  "class_id": "10"
}
```

**cURL:**
```bash
curl -X POST "http://localhost/amt/api/fee-collection-report-column-wise/filter" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d '{"session_id":"21","class_id":"10"}'
```

### Example 4: Date Range + Session + Fee Type

**Body:**
```json
{
  "from_date": "2024-01-01",
  "to_date": "2024-12-31",
  "session_id": "21",
  "fee_type_id": "40"
}
```

**cURL:**
```bash
curl -X POST "http://localhost/amt/api/fee-collection-report-column-wise/filter" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d '{"from_date":"2024-01-01","to_date":"2024-12-31","session_id":"21","fee_type_id":"40"}'
```

### Example 5: All Filters

**Body:**
```json
{
  "from_date": "2024-01-01",
  "to_date": "2024-12-31",
  "session_id": "21",
  "class_id": "10",
  "section_id": "11",
  "fee_type_id": "40"
}
```

**cURL:**
```bash
curl -X POST "http://localhost/amt/api/fee-collection-report-column-wise/filter" \
  -H "Content-Type: application/json" \
  -H "Client-Service: smartschool" \
  -H "Auth-Key: schoolAdmin@" \
  -d '{"from_date":"2024-01-01","to_date":"2024-12-31","session_id":"21","class_id":"10","section_id":"11","fee_type_id":"40"}'
```

---

## Testing with Postman

### Setup

1. **Create New Request**
   - Method: POST
   - URL: `http://localhost/amt/api/session-fee-structure/list`

2. **Add Headers**
   - Key: `Content-Type`, Value: `application/json`
   - Key: `Client-Service`, Value: `smartschool`
   - Key: `Auth-Key`, Value: `schoolAdmin@`

3. **Add Body**
   - Select: `raw`
   - Type: `JSON`
   - Content: `{}`

4. **Send Request**

### Collections

Create a Postman collection with these requests:

1. **Session Fee Structure - List**
   - GET filter options

2. **Type Wise Balance Report - No Filters**
   - Test with empty body

3. **Type Wise Balance Report - Session Only**
   - Test with session_id

4. **Type Wise Balance Report - All Filters**
   - Test with all parameters

5. **Fee Collection Report - No Filters**
   - Test with empty body

6. **Fee Collection Report - Date Range**
   - Test with from_date and to_date

7. **Fee Collection Report - All Filters**
   - Test with all parameters

---

## Android App Testing

### Test Flow

1. **Launch App**
   - Login as teacher
   - Navigate to Reports → Finance

2. **Type Wise Balance Report**
   - Click "Type Wise Balance Report"
   - Wait for filters to load
   - Verify all 5 dropdowns are populated
   - Select filters (optional)
   - Click "Generate Report"
   - Verify API call in logs

3. **Fee Collection Report Column Wise**
   - Click "Fee Collection Report Column Wise"
   - Wait for filters to load
   - Verify date pickers work
   - Verify all 4 dropdowns are populated
   - Select filters (optional)
   - Click "Generate Report"
   - Verify API call in logs

### Log Tags

Monitor these log tags in Android Studio Logcat:

```
TypeWiseBalanceReport
FeeCollectionColumnWise
```

### Expected Log Output

**Type Wise Balance Report:**
```
D/TypeWiseBalanceReport: Loading filter options from: http://localhost/amt/api/session-fee-structure/list
D/TypeWiseBalanceReport: Filter options response: {...}
D/TypeWiseBalanceReport: Fetching report from: http://localhost/amt/api/type-wise-balance-report/filter
D/TypeWiseBalanceReport: Filters - Session: 21, Class: 10, Section: null, FeeGroup: 139, FeeType: 40
D/TypeWiseBalanceReport: Request body: {"session_id":"21","class_id":"10","fee_group_id":"139","fee_type_id":"40"}
D/TypeWiseBalanceReport: Report response: {...}
```

**Fee Collection Report Column Wise:**
```
D/FeeCollectionColumnWise: Loading filter options from: http://localhost/amt/api/session-fee-structure/list
D/FeeCollectionColumnWise: Filter options response: {...}
D/FeeCollectionColumnWise: Fetching report from: http://localhost/amt/api/fee-collection-report-column-wise/filter
D/FeeCollectionColumnWise: Filters - FromDate: 2024-01-01, ToDate: 2024-12-31, Session: 21, Class: 10, Section: null, FeeType: 40
D/FeeCollectionColumnWise: Request body: {"from_date":"2024-01-01","to_date":"2024-12-31","session_id":"21","class_id":"10","fee_type_id":"40"}
D/FeeCollectionColumnWise: Report response: {...}
```

---

## Common Issues

### Issue 1: 401 Unauthorized
**Cause:** Missing or incorrect authentication headers  
**Solution:** Verify headers are set correctly:
```
Client-Service: smartschool
Auth-Key: schoolAdmin@
```

### Issue 2: Empty Dropdowns
**Cause:** API not returning data or parse error  
**Solution:** Check API response format matches expected structure

### Issue 3: Date Format Error
**Cause:** Incorrect date format sent to API  
**Solution:** Ensure dates are in yyyy-MM-dd format (e.g., "2024-01-01")

### Issue 4: Network Error
**Cause:** Base URL not configured or server not accessible  
**Solution:** Check SharedPreferences has correct apiUrl value

---

## Notes

1. **All filters are optional** - Empty body `{}` is valid
2. **Date format** - Always use yyyy-MM-dd for API requests
3. **String IDs** - All IDs should be sent as strings, not integers
4. **Null handling** - Don't include null or empty parameters in request body
5. **Response parsing** - Check `status` field first (1 = success, 0 = error)

