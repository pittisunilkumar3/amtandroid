# Type Wise Balance Report - Visual Guide

## 📱 UI Layout Structure

### RecyclerView Item Card

```
┌─────────────────────────────────────────────────────────────┐
│  ┌───────────────────────────────────────────────────────┐  │
│  │  [THEME COLOR HEADER]                                 │  │
│  │  👤  Student Name (Bold, White, 18sp)                 │  │
│  │      Adm. No: 2024001 (White, 14sp)                   │  │
│  └───────────────────────────────────────────────────────┘  │
│                                                               │
│  🎓 Class 10 - A                                             │
│                                                               │
│  💰 TUITION FEE (Bold)                                       │
│                                                               │
│  Fee Group: 2025-2026 SR MPC                                 │
│                                                               │
│  📱 9876543210                                               │
│                                                               │
│  ─────────────────────────────────────────────────────────  │
│                                                               │
│  Fee Summary (Bold, 16sp)                                    │
│                                                               │
│  Total Amount:                              ₹ 22000.00       │
│  Total Paid:                                ₹ 0.00 (Green)   │
│  Fine:                                      ₹ 0.00 (Orange)  │
│  Discount:                                  ₹ 0.00 (Green)   │
│                                                               │
│  ┌───────────────────────────────────────────────────────┐  │
│  │ [HIGHLIGHTED BACKGROUND #FFF3E0]                      │  │
│  │ Balance:                        ₹ 22000.00 (Red, 16sp)│  │
│  └───────────────────────────────────────────────────────┘  │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

---

## 🎨 Color Scheme

### Header Section
- **Background:** Dynamic theme color from SharedPreferences (`primaryColour`)
- **Text:** White (`@android:color/white`)
- **Icon:** White tint

### Content Section
- **Background:** White
- **Primary Text:** Black (`@android:color/black`)
- **Secondary Text:** Dark Gray (`@android:color/darker_gray`)
- **Icons:** Theme color tint

### Financial Data Colors
| Field | Color | Hex/Resource |
|-------|-------|--------------|
| Total Amount | Black | `@android:color/black` |
| Total Paid | Green | `@android:color/holo_green_dark` |
| Fine | Orange | `@android:color/holo_orange_dark` |
| Discount | Green | `@android:color/holo_green_dark` |
| Balance (Due) | Red | `@android:color/holo_red_dark` |
| Balance (Paid) | Green | `@android:color/holo_green_dark` |

### Balance Highlight
- **Background:** Light Orange (`#FFF3E0`)
- **Text Size:** 16sp (larger than other rows)
- **Text Style:** Bold

---

## 📊 Data Display Examples

### Example 1: Student with Outstanding Balance

```
┌─────────────────────────────────────────────────────────────┐
│  ┌───────────────────────────────────────────────────────┐  │
│  │  [BLUE HEADER]                                        │  │
│  │  👤  Rohith Kumar Bollineni                           │  │
│  │      Adm. No: 2024001                                 │  │
│  └───────────────────────────────────────────────────────┘  │
│                                                               │
│  🎓 Class 10 - A                                             │
│  💰 TUITION FEE                                              │
│  Fee Group: 2025-2026 SR MPC                                 │
│  📱 9876543210                                               │
│  ─────────────────────────────────────────────────────────  │
│  Fee Summary                                                 │
│  Total Amount:                              ₹ 22000.00       │
│  Total Paid:                                ₹ 0.00           │
│  ┌───────────────────────────────────────────────────────┐  │
│  │ Balance:                        ₹ 22000.00 (RED)      │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

### Example 2: Student with Partial Payment and Fine

```
┌─────────────────────────────────────────────────────────────┐
│  ┌───────────────────────────────────────────────────────┐  │
│  │  [BLUE HEADER]                                        │  │
│  │  👤  Sunil Kumar Pitti                                │  │
│  │      Adm. No: 2024002                                 │  │
│  └───────────────────────────────────────────────────────┘  │
│                                                               │
│  🎓 Class 9 - B                                              │
│  💰 ADMISSION FEE                                            │
│  Fee Group: 2025-2026 SR BIPC                                │
│  📱 9123456789                                               │
│  ─────────────────────────────────────────────────────────  │
│  Fee Summary                                                 │
│  Total Amount:                              ₹ 15000.00       │
│  Total Paid:                                ₹ 10000.00       │
│  Fine:                                      ₹ 500.00         │
│  ┌───────────────────────────────────────────────────────┐  │
│  │ Balance:                        ₹ 5500.00 (RED)       │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

### Example 3: Student with Full Payment and Discount

```
┌─────────────────────────────────────────────────────────────┐
│  ┌───────────────────────────────────────────────────────┐  │
│  │  [BLUE HEADER]                                        │  │
│  │  👤  Amit Sharma                                      │  │
│  │      Adm. No: 2024003                                 │  │
│  └───────────────────────────────────────────────────────┘  │
│                                                               │
│  🎓 Class 8 - C                                              │
│  💰 LIBRARY FEE                                              │
│  Fee Group: 2025-2026 SR MEC                                 │
│  📱 9988776655                                               │
│  ─────────────────────────────────────────────────────────  │
│  Fee Summary                                                 │
│  Total Amount:                              ₹ 5000.00        │
│  Total Paid:                                ₹ 4500.00        │
│  Discount:                                  ₹ 500.00         │
│  ┌───────────────────────────────────────────────────────┐  │
│  │ Balance:                        ₹ 0.00 (GREEN)        │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

---

## 🔄 Visibility States

### Conditional Display Rules

| Element | Condition | Action |
|---------|-----------|--------|
| Mobile Number | `mobileNo == null` or `mobileNo.isEmpty()` | Hide row |
| Fee Group | `feeGroupName == null` or `feeGroupName.isEmpty()` | Hide row |
| Fine Row | `fine == "0.00"` or `fine == null` | Hide row |
| Discount Row | `totalDiscount == 0` | Hide row |
| Balance Color | `balance > 0` | Red (Due) |
| Balance Color | `balance == 0` | Green (Paid) |

### Example: Minimal Display (No Mobile, No Fine, No Discount)

```
┌─────────────────────────────────────────────────────────────┐
│  ┌───────────────────────────────────────────────────────┐  │
│  │  [BLUE HEADER]                                        │  │
│  │  👤  John Doe                                         │  │
│  │      Adm. No: 2024004                                 │  │
│  └───────────────────────────────────────────────────────┘  │
│                                                               │
│  🎓 Class 7 - A                                              │
│  💰 SPORTS FEE                                               │
│  Fee Group: 2025-2026 SR                                     │
│  ─────────────────────────────────────────────────────────  │
│  Fee Summary                                                 │
│  Total Amount:                              ₹ 3000.00        │
│  Total Paid:                                ₹ 1000.00        │
│  ┌───────────────────────────────────────────────────────┐  │
│  │ Balance:                        ₹ 2000.00 (RED)       │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

---

## 📐 Spacing and Dimensions

### Card Properties
- **Margin:** 8dp (all sides)
- **Corner Radius:** 12dp
- **Elevation:** 4dp

### Header Section
- **Padding:** 12dp (all sides)
- **Icon Size:** 40dp x 40dp
- **Icon Margin:** 12dp (start)
- **Name Text Size:** 18sp
- **Admission Text Size:** 14sp
- **Admission Top Margin:** 4dp

### Content Section
- **Padding:** 16dp (all sides)
- **Row Margin Bottom:** 8dp (class/section), 4dp (fee rows)
- **Icon Size:** 20dp x 20dp
- **Icon Margin:** 8dp (start)
- **Text Size:** 14sp (default), 16sp (balance)
- **Divider Height:** 1dp
- **Divider Margin:** 8dp (vertical)

### Balance Highlight
- **Padding:** 8dp (all sides)
- **Margin Bottom:** 4dp
- **Text Size:** 16sp
- **Text Style:** Bold

---

## 🎯 Icons Used

| Element | Icon Resource | Color |
|---------|--------------|-------|
| Student | `@drawable/ic_fa_user` | White (header) |
| Class/Section | `@drawable/ic_fa_graduation_cap` | Theme color |
| Fee Type | `@drawable/ic_fa_money` | Theme color |
| Mobile | Emoji 📱 | N/A |

---

## 📱 Screen States

### 1. Loading State
```
┌─────────────────────────────────────────────────────────────┐
│                                                               │
│                                                               │
│                      [PROGRESS BAR]                           │
│                                                               │
│                      Loading report...                        │
│                                                               │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

### 2. No Data State
```
┌─────────────────────────────────────────────────────────────┐
│                                                               │
│                                                               │
│                      [NO DATA ICON]                           │
│                                                               │
│                   No records found                            │
│                                                               │
│          Please adjust filters and try again                  │
│                                                               │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

### 3. Content State (RecyclerView with Data)
```
┌─────────────────────────────────────────────────────────────┐
│  [CARD 1 - Student 1]                                        │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  Header + Content                                     │  │
│  └───────────────────────────────────────────────────────┘  │
│                                                               │
│  [CARD 2 - Student 2]                                        │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  Header + Content                                     │  │
│  └───────────────────────────────────────────────────────┘  │
│                                                               │
│  [CARD 3 - Student 3]                                        │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  Header + Content                                     │  │
│  └───────────────────────────────────────────────────────┘  │
│                                                               │
│  ↓ Scroll for more ↓                                         │
└─────────────────────────────────────────────────────────────┘
```

---

## 🔍 Data Mapping

### API Response → UI Display

| API Field | UI Element | Format |
|-----------|------------|--------|
| `firstname`, `middlename`, `lastname` | Student Name | "First Middle Last" |
| `admission_no` | Admission Number | "Adm. No: {value}" |
| `class` | Class/Section | "{class} - {section}" |
| `section` | Class/Section | "{class} - {section}" |
| `type` | Fee Type | Bold text |
| `feegroupname` | Fee Group | "Fee Group: {value}" |
| `mobileno` | Mobile Number | "📱 {value}" |
| `total` | Total Amount | "{currency} {value}" |
| `total_amount` | Total Paid | "{currency} {value}" |
| `fine` | Fine | "{currency} {value}" |
| `total_discount` | Discount | "{currency} {value}" |
| `balance` | Balance | "{currency} {value}" |

---

## ✅ Implementation Checklist

- ✅ CardView with rounded corners and elevation
- ✅ Dynamic theme color for header
- ✅ Student icon with white tint
- ✅ Student name and admission number in header
- ✅ Class/section with graduation cap icon
- ✅ Fee type with money icon (bold)
- ✅ Fee group name display
- ✅ Mobile number with emoji
- ✅ Divider line between sections
- ✅ Fee Summary section title
- ✅ Total Amount row
- ✅ Total Paid row (green)
- ✅ Fine row (orange, conditional)
- ✅ Discount row (green, conditional)
- ✅ Balance row (highlighted, color-coded)
- ✅ Currency formatting from SharedPreferences
- ✅ Null/empty value handling
- ✅ RecyclerView adapter implementation
- ✅ ViewHolder pattern
- ✅ Data binding
- ✅ Smooth scrolling

---

**Last Updated:** 2025-10-10  
**Status:** ✅ Complete  
**Visual Design:** ✅ Matches App Standards  

