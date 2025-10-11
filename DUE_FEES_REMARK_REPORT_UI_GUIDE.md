# Due Fees Remark Report - UI Guide

## 📱 Screen Layout

```
┌─────────────────────────────────────────┐
│  ← Balance Fees Report with Remark     │  ← Action Bar
├─────────────────────────────────────────┤
│                                         │
│  ┌───────────────────────────────────┐ │
│  │ Filters                           │ │
│  │                                   │ │
│  │ Session                           │ │
│  │ [Select Session ▼]                │ │
│  │                                   │ │
│  │ Class                             │ │
│  │ [Select Class ▼]                  │ │
│  │                                   │ │
│  │ Section                           │ │
│  │ [Select Section ▼]                │ │
│  │                                   │ │
│  │ [  Generate Report  ]             │ │
│  └───────────────────────────────────┘ │
│                                         │
│  ┌───────────────────────────────────┐ │
│  │ Summary                           │ │
│  │                                   │ │
│  │ 👤 Total Students:           15   │ │
│  │                                   │ │
│  │ 💰 Total Due Amount: $ 45,000.00 │ │  ← Highlighted
│  └───────────────────────────────────┘ │
│                                         │
│  ┌───────────────────────────────────┐ │
│  │ 👤 John Doe                       │ │  ← Student Card
│  │    Adm. No: 2024001               │ │
│  ├───────────────────────────────────┤ │
│  │ 🎓 Class 10 - A                   │ │
│  │ 👨 Father: Robert Doe              │ │
│  │ 📱 9876543210                      │ │
│  │                                   │ │
│  │ Total Amount:      $ 5,000.00     │ │
│  │ Total Paid:        $ 2,000.00     │ │
│  │ Total Balance:     $ 3,000.00     │ │  ← Red color
│  │ Total Fine:        $    50.00     │ │
│  │ Total Discount:    $   100.00     │ │
│  │                                   │ │
│  │ 5 fee item(s)                     │ │
│  │ • Tuition Fee: $ 2,000.00         │ │
│  │ • Library Fee: $   500.00         │ │
│  │ • Lab Fee: $       500.00         │ │
│  │                                   │ │
│  │ ┌─────────────────────────────┐   │ │
│  │ │ Remark:                     │   │ │  ← Remark Section
│  │ │ Payment pending since last  │   │ │
│  │ │ month                       │   │ │
│  │ └─────────────────────────────┘   │ │
│  └───────────────────────────────────┘ │
│                                         │
│  [More student cards...]                │
│                                         │
└─────────────────────────────────────────┘
```

---

## 🎨 UI Components Breakdown

### 1. **Action Bar**
```
┌─────────────────────────────────────────┐
│  ← Balance Fees Report with Remark     │
└─────────────────────────────────────────┘
```
- **Background:** Primary theme color
- **Back Button:** Left side
- **Title:** Center aligned
- **Text Color:** White

---

### 2. **Filters Card**
```
┌───────────────────────────────────┐
│ Filters                           │
│                                   │
│ Session                           │
│ [Select Session ▼]                │
│                                   │
│ Class                             │
│ [Select Class ▼]                  │
│                                   │
│ Section                           │
│ [Select Section ▼]                │
│                                   │
│ [  Generate Report  ]             │
└───────────────────────────────────┘
```

**Features:**
- ✅ Card with elevation and rounded corners
- ✅ Three cascading dropdowns
- ✅ Generate Report button (primary color)
- ✅ All filters optional

**Behavior:**
- Session selected → Class dropdown populates
- Class selected → Section dropdown populates
- Generate Report → API call with selected filters

---

### 3. **Summary Card**
```
┌───────────────────────────────────┐
│ Summary                           │
│                                   │
│ 👤 Total Students:           15   │
│                                   │
│ 💰 Total Due Amount: $ 45,000.00 │  ← Orange background
└───────────────────────────────────┘
```

**Features:**
- ✅ Shows after successful API call
- ✅ Hidden when no data
- ✅ Icons for visual appeal
- ✅ Currency formatted amount
- ✅ Highlighted due amount row

**Colors:**
- Total Students: Primary color
- Total Due Amount: Red (holo_red_dark)
- Background: Light orange (#FFF3E0)

---

### 4. **Student Card**

#### **Header Section**
```
┌───────────────────────────────────┐
│ 👤 John Doe                       │  ← Primary color background
│    Adm. No: 2024001               │
├───────────────────────────────────┤
```
- **Background:** Primary theme color
- **Text Color:** White
- **Icon:** User icon
- **Content:** Student name, admission number

#### **Student Information**
```
│ 🎓 Class 10 - A                   │
│ 👨 Father: Robert Doe              │
│ 📱 9876543210                      │
│ 👥 Guardian: Mary Doe              │  ← If available
│ 📞 9876543211                      │  ← If available
```
- Icons for each field
- Conditional display (guardian info)

#### **Fee Summary**
```
│ Total Amount:      $ 5,000.00     │
│ Total Paid:        $ 2,000.00     │  ← Green
│ Total Balance:     $ 3,000.00     │  ← Red, highlighted
│ Total Fine:        $    50.00     │  ← Orange
│ Total Discount:    $   100.00     │  ← Green
```
- **Total Balance:** Highlighted with orange background
- **Color Coding:**
  - Balance: Red if due, Green if paid
  - Paid: Green
  - Fine: Orange
  - Discount: Green

#### **Fee Details**
```
│ 5 fee item(s)                     │
│ • Tuition Fee (TF001): $ 2,000.00 │
│ • Library Fee (LF001): $   500.00 │
│ • Lab Fee (LAB01): $       500.00 │
```
- Count of fee items
- Bullet list with fee type, code, and balance

#### **Remark Section** (NEW)
```
│ ┌─────────────────────────────┐   │
│ │ Remark:                     │   │
│ │ Payment pending since last  │   │
│ │ month                       │   │
│ └─────────────────────────────┘   │
```
- **Background:** Light blue (#E3F2FD)
- **Label:** "Remark:" in primary color
- **Text:** Black color
- **Visibility:** Only shown if remark exists

---

## 🎯 UI States

### **1. Initial State**
```
┌─────────────────────────────────────────┐
│  ← Balance Fees Report with Remark     │
├─────────────────────────────────────────┤
│                                         │
│  [Filters Card]                         │
│                                         │
│  (No summary card)                      │
│  (No student cards)                     │
│                                         │
└─────────────────────────────────────────┘
```

### **2. Loading State**
```
┌─────────────────────────────────────────┐
│  ← Balance Fees Report with Remark     │
├─────────────────────────────────────────┤
│                                         │
│  [Filters Card]                         │
│                                         │
│         ⏳ Loading...                   │  ← Progress bar
│                                         │
└─────────────────────────────────────────┘
```

### **3. Success State (With Data)**
```
┌─────────────────────────────────────────┐
│  ← Balance Fees Report with Remark     │
├─────────────────────────────────────────┤
│                                         │
│  [Filters Card]                         │
│                                         │
│  [Summary Card]                         │  ← Visible
│                                         │
│  [Student Card 1]                       │  ← List of students
│  [Student Card 2]                       │
│  [Student Card 3]                       │
│  ...                                    │
│                                         │
└─────────────────────────────────────────┘
```

### **4. Empty State (No Data)**
```
┌─────────────────────────────────────────┐
│  ← Balance Fees Report with Remark     │
├─────────────────────────────────────────┤
│                                         │
│  [Filters Card]                         │
│                                         │
│         📋                              │
│    No students with due fees found      │
│                                         │
└─────────────────────────────────────────┘
```

---

## 🎨 Color Scheme

### **Primary Colors**
- **Action Bar:** Primary theme color (from app settings)
- **Card Headers:** Primary theme color
- **Buttons:** Primary theme color

### **Status Colors**
- **Due Balance:** Red (#F44336 - holo_red_dark)
- **Paid Amount:** Green (#4CAF50 - holo_green_dark)
- **Fine Amount:** Orange (#FF9800 - holo_orange_dark)
- **Discount:** Green (#4CAF50 - holo_green_dark)

### **Background Colors**
- **Balance Row:** Light orange (#FFF3E0)
- **Remark Section:** Light blue (#E3F2FD)
- **Cards:** White with elevation

---

## 📐 Spacing & Sizing

### **Card Spacing**
- **Margin:** 16dp on all sides
- **Padding:** 16dp inside cards
- **Corner Radius:** 8dp
- **Elevation:** 4dp

### **Text Sizes**
- **Title:** 18sp (bold)
- **Section Headers:** 16sp (bold)
- **Body Text:** 14sp
- **Small Text:** 12sp
- **Remark Text:** 12sp

### **Icon Sizes**
- **Header Icons:** 40dp x 40dp
- **Info Icons:** 20dp x 20dp

---

## 🔄 Interactions

### **Filter Interactions**
1. **Session Dropdown:**
   - Tap → Shows list of sessions
   - Select → Populates class dropdown
   - "Select Session" → Clears class and section

2. **Class Dropdown:**
   - Disabled until session selected
   - Tap → Shows classes for selected session
   - Select → Populates section dropdown

3. **Section Dropdown:**
   - Disabled until class selected
   - Tap → Shows sections for selected class
   - Select → Ready to generate report

4. **Generate Report Button:**
   - Tap → Shows loading spinner
   - Makes API call with filters
   - Shows results or error

### **Card Interactions**
- **Student Cards:** Scrollable list
- **No Click Actions:** Display only (can be extended)

---

## 📱 Responsive Design

### **Portrait Mode**
- Full width cards
- Vertical scrolling
- Stacked layout

### **Landscape Mode**
- Same layout (vertical scroll)
- Better use of width
- More content visible

---

## ✨ Visual Enhancements

### **Icons Used**
- 👤 User icon (student, total students)
- 🎓 Graduation cap (class)
- 👨 Father icon
- 📱 Phone icon (mobile)
- 👥 Guardian icon
- 💰 Money icon (due amount)

### **Elevation & Shadows**
- Cards have 4dp elevation
- Creates depth and hierarchy
- Separates content sections

### **Color Coding**
- Red for due amounts (urgent)
- Green for paid amounts (positive)
- Orange for fines (warning)
- Blue for remarks (informational)

---

## 🎯 Accessibility

### **Content Descriptions**
- All icons have content descriptions
- Screen reader friendly

### **Touch Targets**
- Minimum 48dp height for interactive elements
- Adequate spacing between elements

### **Contrast**
- High contrast text on backgrounds
- Readable font sizes

---

**Last Updated:** October 11, 2025  
**Status:** ✅ Complete UI Implementation

