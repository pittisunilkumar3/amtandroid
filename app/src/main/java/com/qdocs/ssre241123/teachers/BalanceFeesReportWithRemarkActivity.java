package com.qdocs.ssre241123.teachers;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.DueFeeReportAdapter;
import com.qdocs.ssre241123.model.DueFeeReportModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity for Balance Fees Report With Remark (Due Fees Remark Report)
 * Filters: Session, Class, Section
 * API: POST /api/due-fees-remark-report/filter
 */
public class BalanceFeesReportWithRemarkActivity extends BaseFinanceReportActivity {

    private static final String TAG = "BalanceFeesReportWithRemark";

    private DueFeeReportAdapter adapter;
    private List<DueFeeReportModel> dueFeeList;

    // Summary views
    private CardView summaryCard;
    private TextView totalStudentsTextView;
    private TextView totalDueAmountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate called");

        // Initialize summary views
        summaryCard = findViewById(R.id.summaryCard);
        totalStudentsTextView = findViewById(R.id.totalStudentsTextView);
        totalDueAmountTextView = findViewById(R.id.totalDueAmountTextView);

        // Initialize list and adapter
        dueFeeList = new ArrayList<>();
        adapter = new DueFeeReportAdapter(this, dueFeeList);
        reportContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reportContentRecyclerView.setAdapter(adapter);

        Log.d(TAG, "RecyclerView and adapter initialized");
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_balance_fees_report_with_remark;
    }

    @Override
    protected String getReportTitle() {
        return getString(R.string.balance_fees_report_with_remark);
    }

    @Override
    protected String getReportApiUrl() {
        return Constants.balanceFeesReportWithRemarkFilterUrl;
    }

    @Override
    protected void setupSpecificFilters() {
        // No additional filters needed - Session, Class, Section are handled by base class
    }

    @Override
    protected void parseReportResponse(String response) {
        Log.d(TAG, "=== Parsing Due Fees Remark Report Response ===");
        Log.d(TAG, "Response: " + response);

        try {
            JSONObject jsonResponse = new JSONObject(response);

            int status = jsonResponse.optInt("status", 0);
            String message = jsonResponse.optString("message", "");

            Log.d(TAG, "Status: " + status);
            Log.d(TAG, "Message: " + message);

            if (status == 1) {
                // Parse summary
                JSONObject summaryObj = jsonResponse.optJSONObject("summary");
                Log.d(TAG, "Summary object: " + (summaryObj != null ? summaryObj.toString() : "null"));

                if (summaryObj != null) {
                    int totalStudents = summaryObj.optInt("total_students", 0);
                    // API returns "total_balance" not "total_due_amount"
                    double totalDueAmount = summaryObj.optDouble("total_balance", 0.0);

                    Log.d(TAG, "Parsed summary - Students: " + totalStudents + ", Due Amount: " + totalDueAmount);

                    // Update summary card
                    updateSummaryCard(totalStudents, totalDueAmount);
                } else {
                    Log.w(TAG, "Summary object is null - summary card will not be displayed");
                }

                // Parse data array
                JSONArray dataArray = jsonResponse.optJSONArray("data");

                if (dataArray != null && dataArray.length() > 0) {
                    Log.d(TAG, "Data array length: " + dataArray.length());

                    // Clear existing list
                    dueFeeList.clear();

                    // Parse each student record
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject studentObj = dataArray.getJSONObject(i);

                        DueFeeReportModel dueFee = new DueFeeReportModel();

                        // Parse student information
                        dueFee.setStudentId(studentObj.optString("student_id", "")); // API uses "student_id" not "id"
                        dueFee.setAdmissionNo(studentObj.optString("admission_no", ""));
                        dueFee.setFirstname(studentObj.optString("firstname", ""));
                        dueFee.setMiddlename(studentObj.optString("middlename", ""));
                        dueFee.setLastname(studentObj.optString("lastname", ""));
                        dueFee.setClassName(studentObj.optString("class", ""));
                        dueFee.setSectionName(studentObj.optString("section", ""));
                        dueFee.setFatherName(studentObj.optString("father_name", ""));
                        dueFee.setMobileno(studentObj.optString("mobileno", ""));
                        dueFee.setGuardianName(studentObj.optString("guardian_name", ""));
                        dueFee.setGuardianPhone(studentObj.optString("guardian_phone", ""));

                        // Parse fee summary - API uses "total_paid" and "total_balance"
                        dueFee.setTotalAmount(studentObj.optString("total_amount", "0.00"));
                        dueFee.setTotalPaid(studentObj.optString("total_paid", "0.00"));
                        dueFee.setTotalBalance(studentObj.optString("total_balance", "0.00"));
                        dueFee.setTotalFine(studentObj.optString("total_fine", "0.00"));
                        dueFee.setTotalDiscount(studentObj.optString("total_discount", "0.00"));

                        // Parse remark
                        String remark = studentObj.optString("remark", "");
                        dueFee.setRemark(remark);
                        Log.d(TAG, "Student " + i + " - Remark: " + (remark.isEmpty() ? "(empty)" : remark));

                        // Parse fee details
                        JSONArray feesArray = studentObj.optJSONArray("fees");
                        Log.d(TAG, "Student " + i + " - Fees array: " + (feesArray != null ? feesArray.length() + " items" : "null"));

                        if (feesArray != null && feesArray.length() > 0) {
                            List<DueFeeReportModel.FeeDetail> feesList = new ArrayList<>();
                            for (int j = 0; j < feesArray.length(); j++) {
                                JSONObject feeObj = feesArray.getJSONObject(j);

                                DueFeeReportModel.FeeDetail feeDetail = new DueFeeReportModel.FeeDetail();
                                feeDetail.setFeeType(feeObj.optString("fee_type", ""));
                                feeDetail.setFeeCode(feeObj.optString("fee_group", "")); // API uses "fee_group" not "fee_code"
                                feeDetail.setDueDate(feeObj.optString("due_date", ""));
                                feeDetail.setAmount(feeObj.optString("amount", "0.00"));
                                feeDetail.setPaidAmount(feeObj.optString("paid", "0.00")); // API uses "paid" not "paid_amount"
                                feeDetail.setBalanceAmount(feeObj.optString("balance", "0.00")); // API uses "balance" not "balance_amount"
                                feeDetail.setFineAmount(feeObj.optString("fine_amount", "0.00"));
                                feeDetail.setDiscountAmount(feeObj.optString("discount_amount", "0.00"));
                                feeDetail.setStatus(feeObj.optString("status", ""));

                                Log.d(TAG, "  Fee " + j + ": " + feeDetail.getFeeType() +
                                      " (" + feeDetail.getFeeCode() + ") - Balance: " + feeDetail.getBalanceAmount());

                                feesList.add(feeDetail);
                            }
                            dueFee.setFeesList(feesList);
                            Log.d(TAG, "Student " + i + " - Added " + feesList.size() + " fee items");
                        } else {
                            Log.d(TAG, "Student " + i + " - No fees array or empty");
                        }

                        // Parse transport fees
                        JSONArray transportFeesArray = studentObj.optJSONArray("transport_fees");
                        if (transportFeesArray != null && transportFeesArray.length() > 0) {
                            List<DueFeeReportModel.FeeDetail> transportFeesList = new ArrayList<>();
                            for (int j = 0; j < transportFeesArray.length(); j++) {
                                JSONObject feeObj = transportFeesArray.getJSONObject(j);

                                DueFeeReportModel.FeeDetail feeDetail = new DueFeeReportModel.FeeDetail();
                                feeDetail.setFeeType(feeObj.optString("fee_type", "Transport Fee"));
                                feeDetail.setFeeCode(feeObj.optString("fee_group", "")); // API uses "fee_group"
                                feeDetail.setDueDate(feeObj.optString("due_date", ""));
                                feeDetail.setAmount(feeObj.optString("amount", "0.00"));
                                feeDetail.setPaidAmount(feeObj.optString("paid", "0.00")); // API uses "paid"
                                feeDetail.setBalanceAmount(feeObj.optString("balance", "0.00")); // API uses "balance"
                                feeDetail.setFineAmount(feeObj.optString("fine_amount", "0.00"));
                                feeDetail.setDiscountAmount(feeObj.optString("discount_amount", "0.00"));
                                feeDetail.setStatus(feeObj.optString("status", ""));

                                transportFeesList.add(feeDetail);
                            }
                            dueFee.setTransportFeesList(transportFeesList);
                            Log.d(TAG, "Student " + i + " - Added " + transportFeesList.size() + " transport fee items");
                        }

                        dueFeeList.add(dueFee);
                    }

                    // Update adapter
                    adapter.notifyDataSetChanged();

                    // Show content
                    showContent();

                    Log.d(TAG, "Successfully parsed " + dueFeeList.size() + " due fee records");
                    Toast.makeText(this, "Loaded " + dueFeeList.size() + " student(s) with due fees", Toast.LENGTH_SHORT).show();

                } else {
                    Log.d(TAG, "No data found in response");
                    showNoData();
                    Toast.makeText(this, "No students with due fees found", Toast.LENGTH_SHORT).show();
                }

            } else {
                Log.d(TAG, "API returned error status");
                showNoData();
                Toast.makeText(this, message.isEmpty() ? "No data found" : message, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing report response", e);
            showNoData();
            Toast.makeText(this, "Error parsing report data", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Update summary card with total students and total due amount
     */
    private void updateSummaryCard(int totalStudents, double totalDueAmount) {
        Log.d(TAG, "updateSummaryCard called - Students: " + totalStudents + ", Due Amount: " + totalDueAmount);

        if (summaryCard == null) {
            Log.e(TAG, "summaryCard is null!");
            return;
        }
        if (totalStudentsTextView == null) {
            Log.e(TAG, "totalStudentsTextView is null!");
            return;
        }
        if (totalDueAmountTextView == null) {
            Log.e(TAG, "totalDueAmountTextView is null!");
            return;
        }

        // Show summary card
        summaryCard.setVisibility(View.VISIBLE);
        Log.d(TAG, "Summary card visibility set to VISIBLE");

        // Set total students
        totalStudentsTextView.setText(String.valueOf(totalStudents));
        Log.d(TAG, "Total students text set to: " + totalStudents);

        // Format and set total due amount
        String currency = Utility.getSharedPreferences(getApplicationContext(), Constants.currency);
        if (currency == null || currency.isEmpty()) {
            currency = "$";
        }
        Log.d(TAG, "Currency symbol: " + currency);

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        String formattedAmount = currency + " " + decimalFormat.format(totalDueAmount);
        totalDueAmountTextView.setText(formattedAmount);

        Log.d(TAG, "✅ Summary card updated successfully - Students: " + totalStudents + ", Due Amount: " + formattedAmount);
    }
}

