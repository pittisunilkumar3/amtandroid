#!/usr/bin/env python3
"""
Comprehensive fix for Teacher Profile issues
"""

import json

def create_android_logging_fix():
    """Create Android logging additions to debug data flow"""
    
    logging_code = '''
// Add these logging statements to TeacherProfile.java in parseComprehensiveTeacherProfile method

Log.d("Teacher Profile Debug", "=== COMPREHENSIVE PROFILE PARSING DEBUG ===");
Log.d("Teacher Profile Debug", "Response status: " + response.optString("status"));
Log.d("Teacher Profile Debug", "Response message: " + response.optString("message"));

// Log all main sections
Log.d("Teacher Profile Debug", "Basic Info: " + (response.has("basic_info") ? "Present" : "Missing"));
Log.d("Teacher Profile Debug", "Contact Info: " + (response.has("contact_info") ? "Present" : "Missing"));
Log.d("Teacher Profile Debug", "Personal Info: " + (response.has("personal_info") ? "Present" : "Missing"));
Log.d("Teacher Profile Debug", "Address Info: " + (response.has("address_info") ? "Present" : "Missing"));
Log.d("Teacher Profile Debug", "Bank Details: " + (response.has("bank_details") ? "Present" : "Missing"));
Log.d("Teacher Profile Debug", "Payroll Details: " + (response.has("payroll_details") ? "Present" : "Missing"));
Log.d("Teacher Profile Debug", "Leave Records: " + (response.has("leave_records") ? "Present" : "Missing"));
Log.d("Teacher Profile Debug", "Attendance Records: " + (response.has("attendance_records") ? "Present" : "Missing"));
Log.d("Teacher Profile Debug", "Documents: " + (response.has("documents") ? "Present" : "Missing"));
Log.d("Teacher Profile Debug", "QR Code: " + (response.has("qr_code") ? "Present" : "Missing"));

// Log payroll details specifically
JSONObject payrollDetails = response.optJSONObject("payroll_details");
if (payrollDetails != null) {
    JSONArray payrollRecords = payrollDetails.optJSONArray("payroll_records");
    Log.d("Teacher Profile Debug", "Payroll records count: " + (payrollRecords != null ? payrollRecords.length() : 0));
    if (payrollRecords != null && payrollRecords.length() > 0) {
        Log.d("Teacher Profile Debug", "First payroll record: " + payrollRecords.optJSONObject(0).toString());
    }
} else {
    Log.d("Teacher Profile Debug", "Payroll details is null");
}

// Log QR code details
JSONObject qrCode = response.optJSONObject("qr_code");
if (qrCode != null) {
    Log.d("Teacher Profile Debug", "QR Code URL: " + qrCode.optString("qr_code_url"));
    Log.d("Teacher Profile Debug", "QR Code Data: " + qrCode.optJSONObject("data"));
} else {
    Log.d("Teacher Profile Debug", "QR Code is null");
}

Log.d("Teacher Profile Debug", "=== END COMPREHENSIVE PROFILE PARSING DEBUG ===");
'''
    
    return logging_code

def create_payroll_fragment_fix():
    """Create fix for payroll fragment to handle the actual API response structure"""
    
    fix_code = '''
// ISSUE IDENTIFIED: The API response structure doesn't match what the fragment expects
// The API returns payroll_records as an empty array, but the provided JSON shows it should have data

// SOLUTION 1: Update the fragment to handle both empty and populated payroll records better
// SOLUTION 2: Check if the API endpoint is correct or if there's a data issue

// In TeacherPayrollFragment.java, update the loadPayrollData method:

private void loadPayrollData() {
    payrollValues.clear();
    payrollData.clear();
    
    Log.d("TeacherPayrollFragment", "=== PAYROLL DATA LOADING DEBUG ===");
    
    if (getArguments() != null && getArguments().getString("teacherData") != null) {
        try {
            JSONObject teacherData = new JSONObject(getArguments().getString("teacherData"));
            String currency = Utility.getSharedPreferences(getActivity(), Constants.currency);
            
            Log.d("TeacherPayrollFragment", "Teacher data received, parsing payroll information");
            
            // Extract bank details first (these should always be shown)
            JSONObject bankDetails = teacherData.optJSONObject("bank_details");
            Log.d("TeacherPayrollFragment", "Bank details: " + (bankDetails != null ? bankDetails.toString() : "null"));
            
            // Basic salary from bank_details
            if (bankDetails != null) {
                String basicSalary = bankDetails.optString("basic_salary", "0");
                if (!basicSalary.equals("0") && !basicSalary.isEmpty()) {
                    payrollValues.add(currency + " " + basicSalary);
                } else {
                    payrollValues.add("Not specified");
                }
                
                // Bank account details
                payrollValues.add(bankDetails.optString("account_title", "Not provided"));
                payrollValues.add(bankDetails.optString("bank_account_no", "Not provided"));
                payrollValues.add(bankDetails.optString("bank_name", "Not provided"));
                payrollValues.add(bankDetails.optString("bank_branch", "Not provided"));
                payrollValues.add(bankDetails.optString("ifsc_code", "Not provided"));
                payrollValues.add(bankDetails.optString("contract_type", "Not specified"));
                payrollValues.add(bankDetails.optString("shift", "Not specified"));
                payrollValues.add(bankDetails.optString("location", "Not specified"));
            } else {
                // Fill with default values if bank details are missing
                for (int i = 0; i < 9; i++) {
                    payrollValues.add("Not available");
                }
            }

            // Extract payroll records
            JSONObject payrollDetails = teacherData.optJSONObject("payroll_details");
            Log.d("TeacherPayrollFragment", "Payroll details: " + (payrollDetails != null ? "Present" : "Missing"));
            
            if (payrollDetails != null) {
                JSONArray payrollRecords = payrollDetails.optJSONArray("payroll_records");
                Log.d("TeacherPayrollFragment", "Payroll records count: " + (payrollRecords != null ? payrollRecords.length() : 0));
                
                if (payrollRecords != null && payrollRecords.length() > 0) {
                    // Get the latest payroll record (first in array)
                    JSONObject latestPayroll = payrollRecords.optJSONObject(0);
                    if (latestPayroll != null) {
                        Log.d("TeacherPayrollFragment", "Latest payroll: " + latestPayroll.toString());
                        
                        String status = latestPayroll.optString("status", "Unknown");
                        payrollValues.add(status);

                        // Format payment date
                        String paymentDate = latestPayroll.optString("payment_date", "");
                        if (!paymentDate.isEmpty()) {
                            try {
                                paymentDate = Utility.parseDate("yyyy-MM-dd",
                                    Utility.getSharedPreferences(getActivity(), "dateFormat"),
                                    paymentDate);
                            } catch (Exception e) {
                                Log.e("TeacherPayrollFragment", "Date parsing error: " + e.getMessage());
                            }
                        } else {
                            paymentDate = "Not specified";
                        }
                        payrollValues.add(paymentDate);
                    } else {
                        payrollValues.add("No record details");
                        payrollValues.add("No payment date");
                    }
                } else {
                    // No payroll records available - this is the current case
                    payrollValues.add("No payroll records");
                    payrollValues.add("No payment date");
                    Log.d("TeacherPayrollFragment", "No payroll records found - showing default message");
                }
            } else {
                // No payroll details section
                payrollValues.add("No payroll data");
                payrollValues.add("No payment data");
                Log.d("TeacherPayrollFragment", "No payroll details section found");
            }
            
            Log.d("TeacherPayrollFragment", "Final payroll values count: " + payrollValues.size());
            for (int i = 0; i < payrollValues.size(); i++) {
                Log.d("TeacherPayrollFragment", "Value " + i + ": " + payrollValues.get(i));
            }
            
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("TeacherPayrollFragment", "JSON parsing error: " + e.getMessage());
            // Fill with error values if parsing fails
            for (int i = 0; i < payrollHeaderArray.length; i++) {
                payrollValues.add("Error loading data");
            }
        }
    } else {
        Log.w("TeacherPayrollFragment", "No teacher data available");
        // Fill with empty values if no data
        for (int i = 0; i < payrollHeaderArray.length; i++) {
            payrollValues.add("No data available");
        }
    }
    
    // Populate payroll data map for adapter
    for (int i = 0; i < payrollHeaderArray.length && i < payrollValues.size(); i++) {
        payrollData.put(String.valueOf(payrollHeaderArray[i]), payrollValues.get(i));
    }
    
    Log.d("TeacherPayrollFragment", "=== END PAYROLL DATA LOADING DEBUG ===");
}
'''
    
    return fix_code

def create_qr_code_fix():
    """Create fix for QR code functionality"""
    
    fix_code = '''
// QR CODE ISSUE IDENTIFIED: The QR code URL returns 404
// Current URL: https://school.cyberdetox.in/api/api/teacher/qr-code/6
// Notice the double "/api/api" - this might be the issue

// SOLUTION: Fix the QR code URL construction or handle the 404 gracefully

// In TeacherProfile.java, update the loadQRCode method:

private void loadQRCode() {
    Log.d("Teacher QR Code", "=== QR CODE LOADING DEBUG ===");
    
    if (qrCodeData != null) {
        Log.d("Teacher QR Code", "QR Code data available: " + qrCodeData.toString());
        
        String qrCodeUrl = qrCodeData.optString("qr_code_url", "");
        Log.d("Teacher QR Code", "QR Code URL: " + qrCodeUrl);
        
        if (!qrCodeUrl.isEmpty()) {
            // Try to load the QR code with error handling
            Picasso.with(getApplicationContext())
                .load(qrCodeUrl)
                .placeholder(R.drawable.demo)  // Show placeholder while loading
                .error(R.drawable.demo)        // Show default image if loading fails
                .into(qrcodeIV, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("Teacher QR Code", "QR Code loaded successfully");
                        qrcode_layout.setVisibility(View.VISIBLE);
                        
                        // Set up click listener
                        qrcodeIV.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showAddDialog(TeacherProfile.this, qrCodeUrl, "QR Code");
                            }
                        });
                    }

                    @Override
                    public void onError() {
                        Log.e("Teacher QR Code", "Failed to load QR Code from URL: " + qrCodeUrl);
                        
                        // Try alternative: Generate QR code locally from qr_string
                        String qrString = qrCodeData.optString("qr_string", "");
                        if (!qrString.isEmpty()) {
                            Log.d("Teacher QR Code", "Attempting to generate QR code locally from string");
                            generateQRCodeLocally(qrString);
                        } else {
                            Log.w("Teacher QR Code", "No QR string available, hiding QR code layout");
                            qrcode_layout.setVisibility(View.GONE);
                        }
                    }
                });
        } else {
            Log.w("Teacher QR Code", "QR Code URL is empty");
            qrcode_layout.setVisibility(View.GONE);
        }
    } else {
        Log.w("Teacher QR Code", "QR Code data is null");
        qrcode_layout.setVisibility(View.GONE);
    }
    
    Log.d("Teacher QR Code", "=== END QR CODE LOADING DEBUG ===");
}

// Add this method to generate QR code locally if the URL fails
private void generateQRCodeLocally(String qrString) {
    // This would require adding a QR code generation library
    // For now, just show the QR code layout with a placeholder
    Log.d("Teacher QR Code", "Local QR generation not implemented, showing placeholder");
    qrcode_layout.setVisibility(View.VISIBLE);
    qrcodeIV.setImageResource(R.drawable.demo);
    
    // Set up click listener to show the QR string in a dialog
    qrcodeIV.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showQRStringDialog(qrString);
        }
    });
}

private void showQRStringDialog(String qrString) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("QR Code Data");
    builder.setMessage(qrString);
    builder.setPositiveButton("OK", null);
    builder.show();
}
'''
    
    return fix_code

def main():
    print("🔧 TEACHER PROFILE ISSUES ANALYSIS & FIXES")
    print("=" * 50)
    
    print("\n📋 IDENTIFIED ISSUES:")
    print("1. ❌ Payroll records are empty in API response")
    print("2. ❌ QR code URL returns 404 error")
    print("3. ⚠️  Bank details are mostly empty")
    print("4. ⚠️  No documents available")
    
    print("\n🔍 ROOT CAUSE ANALYSIS:")
    print("1. API Response Mismatch:")
    print("   - Your provided JSON shows payroll_records with data")
    print("   - Current API returns empty payroll_records array")
    print("   - This suggests either:")
    print("     a) API endpoint issue")
    print("     b) Data was removed/changed")
    print("     c) Different teacher ID being used")
    
    print("\n2. QR Code URL Issue:")
    print("   - URL has double '/api/api' path")
    print("   - Returns 404 Not Found")
    print("   - Needs URL correction or fallback handling")
    
    print("\n🛠️  RECOMMENDED FIXES:")
    
    print("\n1. ADD COMPREHENSIVE LOGGING:")
    logging_code = create_android_logging_fix()
    with open('android_logging_fix.txt', 'w') as f:
        f.write(logging_code)
    print("   ✅ Logging code saved to 'android_logging_fix.txt'")
    
    print("\n2. UPDATE PAYROLL FRAGMENT:")
    payroll_fix = create_payroll_fragment_fix()
    with open('payroll_fragment_fix.txt', 'w') as f:
        f.write(payroll_fix)
    print("   ✅ Payroll fix code saved to 'payroll_fragment_fix.txt'")
    
    print("\n3. FIX QR CODE HANDLING:")
    qr_fix = create_qr_code_fix()
    with open('qr_code_fix.txt', 'w') as f:
        f.write(qr_fix)
    print("   ✅ QR code fix saved to 'qr_code_fix.txt'")
    
    print("\n📱 IMMEDIATE TESTING STEPS:")
    print("1. Add the logging code to TeacherProfile.java")
    print("2. Run the app and check Android logs")
    print("3. Verify API is being called correctly")
    print("4. Check if fragments are receiving data")
    print("5. Test QR code loading with error handling")
    
    print("\n🔍 API INVESTIGATION NEEDED:")
    print("1. Verify the correct API endpoint")
    print("2. Check if payroll data exists for staff_id=6")
    print("3. Test with different teacher IDs")
    print("4. Fix QR code URL generation on server side")
    
    print("\n✅ EXPECTED RESULTS AFTER FIXES:")
    print("- Comprehensive logging will show exact data flow")
    print("- Payroll tab will show bank details + 'No payroll records'")
    print("- QR code will either load or show graceful fallback")
    print("- All fragments will display available data correctly")

if __name__ == "__main__":
    main()
