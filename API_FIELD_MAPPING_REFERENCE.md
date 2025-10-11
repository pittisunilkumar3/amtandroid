# API Field Mapping Reference - Due Fees Remark Report

## 📡 API Endpoint
```
POST /api/due-fees-remark-report/filter
```

---

## 🗺️ Complete Field Mapping

### **1. Summary Object**

```json
"summary": {
    "total_students": 303,
    "total_amount": "7063200.00",
    "total_paid": "5537700.00",
    "total_balance": "1525500.00"
}
```

| API Field | Type | Code Usage | Display As |
|-----------|------|------------|------------|
| `total_students` | integer | Total Students Count | "303" |
| `total_balance` | string | Total Due Amount | "₹ 1,525,500.00" |
| `total_amount` | string | _(available but not used)_ | - |
| `total_paid` | string | _(available but not used)_ | - |

---

### **2. Student Object**

```json
{
    "student_id": "1457",
    "admission_no": "202401",
    "firstname": "SHAIK",
    "middlename": "",
    "lastname": "PARVESH",
    "class": "SR-CEC",
    "section": "08199-SR-CEC-B1",
    "guardian_phone": "9010226855",
    "remark": "",
    "total_amount": "16400.00",
    "total_paid": "12400.00",
    "total_balance": "4000.00",
    "fees": [...]
}
```

| API Field | Type | Code Usage | Display As |
|-----------|------|------------|------------|
| `student_id` | string | Student ID | Internal use |
| `admission_no` | string | Admission Number | "Adm. No: 202401" |
| `firstname` | string | First Name | "SHAIK" |
| `middlename` | string | Middle Name | "" |
| `lastname` | string | Last Name | "PARVESH" |
| `class` | string | Class Name | "SR-CEC" |
| `section` | string | Section Name | "08199-SR-CEC-B1" |
| `guardian_phone` | string | Guardian Phone | "📞 9010226855" |
| `remark` | string | Remark Text | Highlighted section |
| `total_amount` | string | Total Amount | "₹ 16,400.00" |
| `total_paid` | string | Total Paid | "₹ 12,400.00" (green) |
| `total_balance` | string | Total Balance | "₹ 4,000.00" (red) |

---

### **3. Fee Object (in fees array)**

```json
{
    "fee_group": "JR-CEC",
    "fee_type": "TUITION FEE",
    "due_date": "2024-01-01",
    "amount": "12000.00",
    "paid": "8000.00",
    "balance": "4000.00"
}
```

| API Field | Type | Code Usage | Display As |
|-----------|------|------------|------------|
| `fee_group` | string | Fee Code/Group | "(JR-CEC)" |
| `fee_type` | string | Fee Type | "TUITION FEE" |
| `due_date` | string | Due Date | "2024-01-01" |
| `amount` | string | Fee Amount | "₹ 12,000.00" |
| `paid` | string | Paid Amount | "₹ 8,000.00" |
| `balance` | string | **Balance Amount** | **"₹ 4,000.00"** |

**Display Format:**
```
• TUITION FEE (JR-CEC): ₹ 4,000.00
```

---

## 🔑 Key Field Names (Critical for Display)

### **For Summary Card:**
```java
// Total Due Amount
summaryObj.optDouble("total_balance", 0.0)  // NOT "total_due_amount"
```

### **For Fee Items:**
```java
// Fee Group (displayed in parentheses)
feeObj.optString("fee_group", "")  // NOT "fee_code"

// Paid Amount
feeObj.optString("paid", "0.00")  // NOT "paid_amount"

// Balance Amount (the most important!)
feeObj.optString("balance", "0.00")  // NOT "balance_amount"
```

---

## 📋 Field Name Comparison

### **Summary Level**
| Expected Name | Actual API Name | Status |
|---------------|-----------------|--------|
| `total_due_amount` | `total_balance` | ⚠️ Different |
| `total_students` | `total_students` | ✅ Same |

### **Student Level**
| Expected Name | Actual API Name | Status |
|---------------|-----------------|--------|
| `id` | `student_id` | ⚠️ Different |
| `admission_no` | `admission_no` | ✅ Same |
| `total_balance` | `total_balance` | ✅ Same |

### **Fee Level**
| Expected Name | Actual API Name | Status |
|---------------|-----------------|--------|
| `fee_code` | `fee_group` | ⚠️ Different |
| `paid_amount` | `paid` | ⚠️ Different |
| `balance_amount` | `balance` | ⚠️ Different |
| `fee_type` | `fee_type` | ✅ Same |
| `amount` | `amount` | ✅ Same |

---

## 🎨 Display Examples

### **Summary Card**
```
┌─────────────────────────────────┐
│ Summary                         │
│                                 │
│ 👤 Total Students:        303   │
│                                 │
│ 💰 Total Due Amount:            │
│    ₹ 1,525,500.00              │
└─────────────────────────────────┘
```

**Data Source:**
- Total Students: `summary.total_students`
- Total Due Amount: `summary.total_balance`

---

### **Student Card Header**
```
┌─────────────────────────────────┐
│ 📄 SHAIK PARVESH                │
│    Adm. No: 202401              │
├─────────────────────────────────┤
```

**Data Source:**
- Name: `firstname + " " + middlename + " " + lastname`
- Admission No: `admission_no`

---

### **Student Card Info**
```
│ 🎓 SR-CEC - 08199-SR-CEC-B1     │
│ 📞 9010226855                    │
```

**Data Source:**
- Class: `class`
- Section: `section`
- Phone: `guardian_phone`

---

### **Fee Summary**
```
│ Fee Summary                     │
│ Total Amount:    ₹ 16,400.00    │
│ Total Paid:      ₹ 12,400.00    │
│ Total Balance:   ₹ 4,000.00     │
```

**Data Source:**
- Total Amount: `total_amount`
- Total Paid: `total_paid`
- Total Balance: `total_balance`

---

### **Fee Breakdown**
```
│ 6 fee item(s)                   │
│                                 │
│ • ADMISSION FEE (JR-CEC):       │
│   ₹ 0.00                        │
│ • TUITION FEE (JR-CEC):         │
│   ₹ 4,000.00                    │
│ • EXAM FEE (JR-CEC):            │
│   ₹ 0.00                        │
```

**Data Source:**
- Fee Type: `fees[].fee_type`
- Fee Group: `fees[].fee_group`
- Balance: `fees[].balance`

**Format:**
```
• {fee_type} ({fee_group}): ₹ {balance}
```

---

### **Remark Section**
```
│ ┌───────────────────────────┐   │
│ │ Remark:                   │   │
│ │ Payment pending since     │   │
│ │ last month                │   │
│ └───────────────────────────┘   │
└─────────────────────────────────┘
```

**Data Source:**
- Remark: `remark`
- Only shown if `remark` is not empty

---

## 🔍 Debugging Quick Reference

### **Check Summary Amount:**
```java
Log.d(TAG, "Summary: " + summaryObj.toString());
// Should show: {"total_students":303,"total_balance":"1525500.00",...}

double amount = summaryObj.optDouble("total_balance", 0.0);
Log.d(TAG, "Total Balance: " + amount);
// Should show: Total Balance: 1525500.0
```

### **Check Fee Balance:**
```java
Log.d(TAG, "Fee: " + feeObj.toString());
// Should show: {"fee_type":"TUITION FEE","balance":"4000.00",...}

String balance = feeObj.optString("balance", "0.00");
Log.d(TAG, "Balance: " + balance);
// Should show: Balance: 4000.00
```

---

## ⚠️ Common Mistakes to Avoid

### ❌ Wrong Field Names
```java
// DON'T USE:
summaryObj.optDouble("total_due_amount", 0.0)  // Wrong!
feeObj.optString("balance_amount", "0.00")     // Wrong!
feeObj.optString("paid_amount", "0.00")        // Wrong!
```

### ✅ Correct Field Names
```java
// USE THESE:
summaryObj.optDouble("total_balance", 0.0)     // Correct!
feeObj.optString("balance", "0.00")            // Correct!
feeObj.optString("paid", "0.00")               // Correct!
```

---

## 📊 Sample API Response (Abbreviated)

```json
{
    "status": 1,
    "message": "Due fees remark report retrieved successfully",
    "summary": {
        "total_students": 303,
        "total_balance": "1525500.00"
    },
    "data": [
        {
            "student_id": "1457",
            "admission_no": "202401",
            "firstname": "SHAIK",
            "lastname": "PARVESH",
            "class": "SR-CEC",
            "section": "08199-SR-CEC-B1",
            "total_balance": "4000.00",
            "fees": [
                {
                    "fee_group": "JR-CEC",
                    "fee_type": "TUITION FEE",
                    "amount": "12000.00",
                    "paid": "8000.00",
                    "balance": "4000.00"
                }
            ]
        }
    ]
}
```

---

## 🎯 Quick Checklist

When integrating this API, always use:

- ✅ `summary.total_balance` for total due amount
- ✅ `student_id` for student ID
- ✅ `fees[].fee_group` for fee code/group
- ✅ `fees[].paid` for paid amount
- ✅ `fees[].balance` for balance amount
- ✅ `total_balance` for student's total balance

---

**Last Updated:** October 11, 2025  
**API Version:** Current  
**Status:** ✅ Verified and Documented

