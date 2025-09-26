# 🔧 Drawable Resource Error Fix - Missing Status Background

## ❌ **ERROR ENCOUNTERED**
```
AAPT: error: resource drawable/status_background (aka com.qdocs.ssre241123:drawable/status_background) not found.
```

**Location**: `dialog_payslip_view.xml:176`

## 🔍 **ROOT CAUSE**
The payslip dialog layout was referencing a non-existent drawable resource:
- **Referenced**: `@drawable/status_background` (doesn't exist)
- **Available**: `status_approved_bg.xml`, `status_generated_bg.xml`, `status_pending_bg.xml`, `status_rejected_bg.xml`

## ✅ **SOLUTION APPLIED**
Updated the dialog layout to use the correct existing drawable:

**Before (Broken)**:
```xml
android:background="@drawable/status_background"
```

**After (Fixed)**:
```xml
android:background="@drawable/status_approved_bg"
```

## 🎯 **RATIONALE**
- **Default Status**: Used `status_approved_bg` as the default background in the layout
- **Dynamic Updates**: The `setStatusBackground()` method in `TeacherPayrollFragment.java` will dynamically change this background based on the actual payroll status
- **Consistency**: Matches the existing status background system used throughout the app

## 🧪 **VERIFICATION**
- ✅ **Drawable Exists**: Confirmed `status_approved_bg.xml` exists in drawable folder
- ✅ **No Other References**: Verified no other files reference the non-existent `status_background`
- ✅ **Method Compatibility**: Confirmed `setStatusBackground()` method uses correct drawable resources
- ✅ **No Diagnostics**: IDE reports no issues with the layout file

## 📱 **FUNCTIONALITY**
The payslip dialog will now:
1. **Load with default green background** (`status_approved_bg`)
2. **Dynamically update** based on actual payroll status via `setStatusBackground()` method:
   - **Green**: Generated, Paid (`status_approved_bg`)
   - **Orange**: Pending (`status_pending_bg`) 
   - **Red**: Rejected, Cancelled (`status_rejected_bg`)

## 🎉 **FINAL STATUS**
**✅ DRAWABLE ERROR RESOLVED** - The missing drawable resource issue has been fixed and the payslip dialog will display correctly with proper status backgrounds.
