package com.qdocs.ssre241123.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.StudentProfileAdapter;
import com.qdocs.ssre241123.adapters.TeacherPayrollAdapter;
import com.qdocs.ssre241123.models.TeacherPayrollRecord;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Dialog;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class TeacherPayrollFragment extends Fragment {
    
    RecyclerView recyclerView;
    StudentProfileAdapter adapter;
    TeacherPayrollAdapter payrollAdapter;
    ArrayList<String> payrollValues = new ArrayList<String>();
    HashMap<String, String> payrollData = new HashMap<>();
    List<TeacherPayrollRecord> payrollRecords = new ArrayList<>();
    
    // Payroll field arrays - comprehensive payroll information
    int[] payrollHeaderArray = {
        R.string.basic_salary,
        R.string.account_title,
        R.string.bank_account_no,
        R.string.bankName,
        R.string.bank_branch,
        R.string.ifscCode,
        R.string.contract_type,
        R.string.shift,
        R.string.location,
        R.string.status,  // Latest payroll status
        R.string.teacher_paymentDate,  // Latest payment date
        R.string.net_salary,  // Net salary from summary
        R.string.earnings,    // Total earnings
        R.string.deduction,   // Total deductions
        R.string.tax          // Tax amount
    };

    @SuppressLint("ValidFragment")
    public TeacherPayrollFragment() {
    }

    public static TeacherPayrollFragment newInstance(JSONObject teacherData) {
        TeacherPayrollFragment fragment = new TeacherPayrollFragment();
        Bundle args = new Bundle();
        if (teacherData != null) {
            args.putString("teacherData", teacherData.toString());
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Don't load data here - RecyclerView doesn't exist yet
        // Data will be loaded in onCreateView after UI is set up
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_teacher_payroll, container, false);
        
        recyclerView = mainView.findViewById(R.id.teacherPayrollFragment_recyclerView);

        // Initialize both adapters - use list adapter if payroll records are available
        adapter = new StudentProfileAdapter(getActivity().getApplicationContext(),
                                          payrollHeaderArray, payrollValues, payrollData);
        payrollAdapter = new TeacherPayrollAdapter(getActivity().getApplicationContext(), payrollRecords);

        Log.d("TeacherPayrollFragment", "🔧 Adapters initialized - Generic: " + (adapter != null) +
              ", Payroll: " + (payrollAdapter != null));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Set click listener for payroll items
        payrollAdapter.setOnPayrollItemClickListener(new TeacherPayrollAdapter.OnPayrollItemClickListener() {
            @Override
            public void onViewPayslipClick(TeacherPayrollRecord record, int position) {
                // Show detailed payslip view dialog
                showPayslipDialog(record);
            }
        });

        // Initially set the generic adapter, will switch based on data availability
        recyclerView.setAdapter(adapter);

        // Now that RecyclerView is set up, load the data and switch adapters if needed
        loadPayrollData();

        return mainView;
    }

    private void loadPayrollData() {
        payrollValues.clear();
        payrollData.clear();
        payrollRecords.clear();

        Log.d("TeacherPayrollFragment", "=== PAYROLL DATA LOADING DEBUG ===");

        if (getArguments() != null && getArguments().getString("teacherData") != null) {
            try {
                JSONObject teacherData = new JSONObject(getArguments().getString("teacherData"));

                // Check if this is an error state
                if (teacherData.optBoolean("error", false)) {
                    String errorMessage = teacherData.optString("error_message", "Unknown error occurred");
                    for (int i = 0; i < payrollHeaderArray.length; i++) {
                        payrollValues.add("Error: " + errorMessage);
                    }
                    return;
                }

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
                } else {
                    payrollValues.add("Not available");
                }

                // Extract bank account details
                if (bankDetails != null) {
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
                    for (int i = 0; i < 8; i++) {
                        payrollValues.add("Not available");
                    }
                }

                // Extract payroll records and salary summary
                JSONObject payrollDetails = teacherData.optJSONObject("payroll_details");
                Log.d("TeacherPayrollFragment", "Payroll details: " + (payrollDetails != null ? "Present" : "Missing"));

                if (payrollDetails != null) {
                    JSONArray payrollRecordsArray = payrollDetails.optJSONArray("payroll_records");
                    JSONObject salarySummary = payrollDetails.optJSONObject("salary_summary");
                    Log.d("TeacherPayrollFragment", "Payroll records count: " + (payrollRecordsArray != null ? payrollRecordsArray.length() : 0));

                    if (payrollRecordsArray != null && payrollRecordsArray.length() > 0) {
                        // Get the latest payroll record (first in array)
                        JSONObject latestPayroll = payrollRecordsArray.optJSONObject(0);
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
                        // No payroll records available - show appropriate message
                        payrollValues.add("No payroll records generated yet");
                        payrollValues.add("No payment date available");
                        Log.d("TeacherPayrollFragment", "No payroll records found - showing default message");
                    }

                    // Add salary summary information if available
                    if (salarySummary != null) {
                        String netSalary = salarySummary.optString("net_salary", "");
                        String earnings = salarySummary.optString("earnings", "");
                        String deduction = salarySummary.optString("deduction", "");
                        String tax = salarySummary.optString("tax", "");

                        payrollValues.add(!netSalary.isEmpty() && !netSalary.equals("null") ? currency + " " + netSalary : "Not calculated");
                        payrollValues.add(!earnings.isEmpty() && !earnings.equals("null") ? currency + " " + earnings : "Not calculated");
                        payrollValues.add(!deduction.isEmpty() && !deduction.equals("null") ? currency + " " + deduction : "Not calculated");
                        payrollValues.add(!tax.isEmpty() && !tax.equals("null") ? currency + " " + tax : "Not calculated");
                    } else {
                        payrollValues.add("Not calculated");
                        payrollValues.add("Not calculated");
                        payrollValues.add("Not calculated");
                        payrollValues.add("Not calculated");
                    }
                } else {
                    // No payroll details section - fill remaining fields
                    payrollValues.add("No payroll data available");
                    payrollValues.add("No payment data available");
                    payrollValues.add("Not calculated");
                    payrollValues.add("Not calculated");
                    payrollValues.add("Not calculated");
                    payrollValues.add("Not calculated");
                    Log.d("TeacherPayrollFragment", "No payroll details section found");
                }

                // Parse individual payroll records for list display
                parsePayrollRecords(teacherData);

                // Decide which adapter to use based on available data
                Log.d("TeacherPayrollFragment", "🔍 ADAPTER DECISION: payrollRecords.size() = " + payrollRecords.size());

                // Only switch adapters if RecyclerView is available
                if (recyclerView != null) {
                    if (payrollRecords.size() > 0) {
                        Log.d("TeacherPayrollFragment", "✅ Using LIST ADAPTER with " + payrollRecords.size() + " records");
                        payrollAdapter.updateData(payrollRecords);
                        recyclerView.setAdapter(payrollAdapter);

                    // Ensure RecyclerView is visible
                    recyclerView.setVisibility(View.VISIBLE);

                    // Force layout refresh
                    recyclerView.post(() -> {
                        if (payrollAdapter != null) {
                            payrollAdapter.notifyDataSetChanged();
                            Log.d("TeacherPayrollFragment", "List adapter data refreshed");
                        }
                    });
                    } else {
                        Log.d("TeacherPayrollFragment", "⚠️ Using GENERIC ADAPTER with summary data");
                        recyclerView.setAdapter(adapter);
                        recyclerView.setVisibility(View.VISIBLE);

                        // Notify generic adapter
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                            Log.d("TeacherPayrollFragment", "Generic adapter data refreshed");
                        }
                    }
                } else {
                    Log.w("TeacherPayrollFragment", "RecyclerView not available yet - adapter switching skipped");
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

    /**
     * Update the fragment with new teacher payroll data
     */
    public void updatePayrollData(JSONObject teacherData) {
        if (teacherData != null) {
            Log.d("TeacherPayrollFragment", "Updating payroll data with new teacher data");

            // Update the arguments with new data
            Bundle args = getArguments();
            if (args == null) {
                args = new Bundle();
            }
            args.putString("teacherData", teacherData.toString());
            setArguments(args);

            // Reload payroll data with new information - this will handle adapter switching
            loadPayrollData();

            // No need to manually notify adapters here as loadPayrollData handles it
            Log.d("TeacherPayrollFragment", "Payroll data update completed");
        } else {
            Log.w("TeacherPayrollFragment", "Attempted to update with null teacher data");
        }
    }

    private void parsePayrollRecords(JSONObject teacherData) {
        try {
            Log.d("TeacherPayrollFragment", "🔍 PARSING PAYROLL RECORDS - Starting...");

            JSONObject payrollDetails = teacherData.optJSONObject("payroll_details");
            if (payrollDetails != null) {
                Log.d("TeacherPayrollFragment", "✅ Payroll details section found");

                JSONArray payrollRecordsArray = payrollDetails.optJSONArray("payroll_records");

                if (payrollRecordsArray != null && payrollRecordsArray.length() > 0) {
                    Log.d("TeacherPayrollFragment", "✅ Found " + payrollRecordsArray.length() + " payroll records");

                    for (int i = 0; i < payrollRecordsArray.length(); i++) {
                        JSONObject recordObj = payrollRecordsArray.optJSONObject(i);
                        if (recordObj != null) {
                            TeacherPayrollRecord record = new TeacherPayrollRecord();
                            record.setId(recordObj.optString("id", String.valueOf(i + 1)));
                            record.setMonth(recordObj.optString("month", ""));
                            record.setYear(recordObj.optString("year", ""));
                            // Map API fields correctly
                            record.setBasicSalary(recordObj.optString("basic", "0"));
                            record.setAllowances(recordObj.optString("total_allowance", "0"));
                            record.setDeductions(recordObj.optString("total_deduction", "0"));
                            record.setNetSalary(recordObj.optString("net_salary", "0"));
                            record.setStatus(recordObj.optString("status", ""));
                            record.setPaymentDate(recordObj.optString("payment_date", ""));
                            record.setCreatedAt(recordObj.optString("created_at", ""));
                            // Calculate earnings as basic + allowances
                            double basic = Double.parseDouble(recordObj.optString("basic", "0"));
                            double allowances = Double.parseDouble(recordObj.optString("total_allowance", "0"));
                            record.setEarnings(String.valueOf(basic + allowances));
                            record.setTax(recordObj.optString("tax", "0"));

                            payrollRecords.add(record);
                            Log.d("TeacherPayrollFragment", "✅ Added record " + (i+1) + ": " +
                                  record.getMonth() + " " + record.getYear() + " - ₹" + record.getNetSalary());
                        }
                    }
                    Log.d("TeacherPayrollFragment", "🎉 TOTAL RECORDS PARSED: " + payrollRecords.size());
                } else {
                    Log.w("TeacherPayrollFragment", "⚠️ No payroll records array found or empty");
                }
            } else {
                Log.w("TeacherPayrollFragment", "⚠️ No payroll details section found");
            }
        } catch (Exception e) {
            Log.e("TeacherPayrollFragment", "❌ Error parsing payroll records: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showPayslipDialog(TeacherPayrollRecord record) {
        if (getActivity() == null) return;

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_payslip_view);
        dialog.getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                                   android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

        // Get currency symbol
        String currency = Utility.getSharedPreferences(getActivity(), Constants.currency);
        if (currency == null || currency.isEmpty()) {
            currency = "₹";
        }

        // Find views
        ImageView closeButton = dialog.findViewById(R.id.payslip_close_button);
        TextView monthYearTV = dialog.findViewById(R.id.payslip_month_year);
        TextView payslipNumberTV = dialog.findViewById(R.id.payslip_number);
        TextView paymentDateTV = dialog.findViewById(R.id.payslip_payment_date);
        TextView statusTV = dialog.findViewById(R.id.payslip_status);
        TextView basicSalaryTV = dialog.findViewById(R.id.payslip_basic_salary);
        TextView allowancesTV = dialog.findViewById(R.id.payslip_allowances);
        TextView totalEarningsTV = dialog.findViewById(R.id.payslip_total_earnings);
        TextView taxTV = dialog.findViewById(R.id.payslip_tax);
        TextView deductionsTV = dialog.findViewById(R.id.payslip_deductions);
        TextView totalDeductionsTV = dialog.findViewById(R.id.payslip_total_deductions);
        TextView netSalaryTV = dialog.findViewById(R.id.payslip_net_salary);

        // Set data
        monthYearTV.setText(record.getMonth() + " " + record.getYear());
        payslipNumberTV.setText(record.getId());
        paymentDateTV.setText(formatPaymentDate(record.getPaymentDate()));
        statusTV.setText(capitalizeFirst(record.getStatus()));

        // Set status background
        setStatusBackground(statusTV, record.getStatus());

        // Set financial data
        basicSalaryTV.setText(currency + " " + formatAmount(record.getBasicSalary()));
        allowancesTV.setText(currency + " " + formatAmount(record.getAllowances()));
        totalEarningsTV.setText(currency + " " + formatAmount(record.getEarnings()));
        taxTV.setText(currency + " " + formatAmount(record.getTax()));
        deductionsTV.setText(currency + " " + formatAmount(record.getDeductions()));

        // Calculate total deductions (tax + other deductions)
        double tax = parseAmount(record.getTax());
        double deductions = parseAmount(record.getDeductions());
        double totalDeductionsAmount = tax + deductions;
        totalDeductionsTV.setText(currency + " " + formatAmount(String.valueOf(totalDeductionsAmount)));

        netSalaryTV.setText(currency + " " + formatAmount(record.getNetSalary()));

        // Set close button listener
        closeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        Log.d("TeacherPayrollFragment", "Payslip dialog shown for " + record.getMonth() + " " + record.getYear());
    }

    private String formatPaymentDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty() || dateStr.equals("null")) {
            return "Not specified";
        }
        try {
            return Utility.parseDate(dateStr, "yyyy-MM-dd", "dd MMM yyyy");
        } catch (Exception e) {
            return dateStr; // Return original if parsing fails
        }
    }

    private String formatAmount(String amount) {
        if (amount == null || amount.isEmpty() || amount.equals("null")) {
            return "0";
        }
        try {
            double value = Double.parseDouble(amount);
            return String.format("%.0f", value);
        } catch (NumberFormatException e) {
            return amount;
        }
    }

    private double parseAmount(String amount) {
        if (amount == null || amount.isEmpty() || amount.equals("null")) {
            return 0.0;
        }
        try {
            return Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private String capitalizeFirst(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    private void setStatusBackground(TextView statusTV, String status) {
        if (status == null) status = "";

        switch (status.toLowerCase()) {
            case "generated":
            case "paid":
                statusTV.setBackgroundResource(R.drawable.status_approved_bg);
                break;
            case "pending":
                statusTV.setBackgroundResource(R.drawable.status_pending_bg);
                break;
            case "rejected":
            case "cancelled":
                statusTV.setBackgroundResource(R.drawable.status_rejected_bg);
                break;
            default:
                statusTV.setBackgroundResource(R.drawable.status_pending_bg);
                break;
        }
    }
}
