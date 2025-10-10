package com.qdocs.ssre241123.teachers;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.StudentAcademicReportAdapter;
import com.qdocs.ssre241123.model.StudentAcademicReportModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Activity for Balance Fees Report (Student Academic Report)
 * Supports multiple search options:
 * - By Class/Section
 * - By Student ID
 * - By Admission Number
 */
public class BalanceFeesReportActivity extends BaseFinanceReportActivity {

    private static final String TAG = "BalanceFeesReport";

    // Search type views
    private RadioGroup searchByRadioGroup;
    private RadioButton searchByClassRadio;
    private RadioButton searchByStudentIdRadio;
    private RadioButton searchByAdmissionNoRadio;

    // Input fields
    private LinearLayout studentIdLayout;
    private LinearLayout admissionNoLayout;
    private LinearLayout classFiltersLayout;
    private EditText studentIdEditText;
    private EditText admissionNoEditText;

    // Adapter
    private StudentAcademicReportAdapter adapter;
    private List<StudentAcademicReportModel> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize views
        searchByRadioGroup = findViewById(R.id.searchByRadioGroup);
        searchByClassRadio = findViewById(R.id.searchByClassRadio);
        searchByStudentIdRadio = findViewById(R.id.searchByStudentIdRadio);
        searchByAdmissionNoRadio = findViewById(R.id.searchByAdmissionNoRadio);

        studentIdLayout = findViewById(R.id.studentIdLayout);
        admissionNoLayout = findViewById(R.id.admissionNoLayout);
        classFiltersLayout = findViewById(R.id.classFiltersLayout);
        studentIdEditText = findViewById(R.id.studentIdEditText);
        admissionNoEditText = findViewById(R.id.admissionNoEditText);

        // Setup radio group listener
        setupSearchTypeRadioGroup();

        // Initialize adapter
        studentList = new ArrayList<>();
        adapter = new StudentAcademicReportAdapter(this, studentList);
        reportContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reportContentRecyclerView.setAdapter(adapter);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_balance_fees_report;
    }

    @Override
    protected String getReportTitle() {
        return getString(R.string.balance_fees_report);
    }

    @Override
    protected String getReportApiUrl() {
        return Constants.studentAcademicReportFilterUrl;
    }

    @Override
    protected void setupSpecificFilters() {
        // No additional filters needed - handled in onCreate
    }

    /**
     * Setup radio group to toggle between search types
     */
    private void setupSearchTypeRadioGroup() {
        searchByRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.searchByClassRadio) {
                // Show class filters, hide others
                classFiltersLayout.setVisibility(View.VISIBLE);
                studentIdLayout.setVisibility(View.GONE);
                admissionNoLayout.setVisibility(View.GONE);
            } else if (checkedId == R.id.searchByStudentIdRadio) {
                // Show student ID input, hide others
                classFiltersLayout.setVisibility(View.GONE);
                studentIdLayout.setVisibility(View.VISIBLE);
                admissionNoLayout.setVisibility(View.GONE);
            } else if (checkedId == R.id.searchByAdmissionNoRadio) {
                // Show admission number input, hide others
                classFiltersLayout.setVisibility(View.GONE);
                studentIdLayout.setVisibility(View.GONE);
                admissionNoLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected String buildRequestBody() {
        try {
            JSONObject requestBody = new JSONObject();

            // Determine which search type is selected
            int selectedId = searchByRadioGroup.getCheckedRadioButtonId();

            if (selectedId == R.id.searchByStudentIdRadio) {
                // Search by Student ID
                String studentId = studentIdEditText.getText().toString().trim();
                if (!studentId.isEmpty()) {
                    requestBody.put("student_id", studentId);
                } else {
                    Toast.makeText(this, "Please enter Student ID", Toast.LENGTH_SHORT).show();
                    return null;
                }
            } else if (selectedId == R.id.searchByAdmissionNoRadio) {
                // Search by Admission Number
                String admissionNo = admissionNoEditText.getText().toString().trim();
                if (!admissionNo.isEmpty()) {
                    requestBody.put("admission_no", admissionNo);
                } else {
                    Toast.makeText(this, "Please enter Admission Number", Toast.LENGTH_SHORT).show();
                    return null;
                }
            } else {
                // Search by Class/Section (default)
                if (selectedClassId != null && !selectedClassId.isEmpty()) {
                    requestBody.put("class_id", selectedClassId);

                    // Add section if selected
                    if (selectedSectionId != null && !selectedSectionId.isEmpty()) {
                        requestBody.put("section_id", selectedSectionId);
                    }

                    // Add session if selected
                    if (selectedSessionId != null && !selectedSessionId.isEmpty()) {
                        requestBody.put("session_id", selectedSessionId);
                    }
                } else {
                    Toast.makeText(this, "Please select a class", Toast.LENGTH_SHORT).show();
                    return null;
                }
            }

            Log.d(TAG, "Request body: " + requestBody.toString());
            return requestBody.toString();

        } catch (JSONException e) {
            Log.e(TAG, "Error building request body", e);
            return null;
        }
    }

    @Override
    protected void parseReportResponse(String response) {
        try {
            Log.d(TAG, "Parsing response: " + response);
            JSONObject jsonResponse = new JSONObject(response);

            if (jsonResponse.getInt("status") == 1) {
                studentList.clear();

                // Check if data is an array or single object
                if (jsonResponse.has("data")) {
                    Object dataObject = jsonResponse.get("data");

                    if (dataObject instanceof JSONArray) {
                        // Multiple students
                        JSONArray dataArray = (JSONArray) dataObject;
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject studentJson = dataArray.getJSONObject(i);
                            StudentAcademicReportModel student = parseStudentData(studentJson);
                            if (student != null) {
                                studentList.add(student);
                            }
                        }
                    } else if (dataObject instanceof JSONObject) {
                        // Single student
                        JSONObject studentJson = (JSONObject) dataObject;
                        StudentAcademicReportModel student = parseStudentData(studentJson);
                        if (student != null) {
                            studentList.add(student);
                        }
                    }
                }

                if (!studentList.isEmpty()) {
                    adapter.updateData(studentList);
                    showContent();

                    String message = jsonResponse.optString("message", "Report generated successfully");
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                } else {
                    showNoData();
                    Toast.makeText(this, "No students found", Toast.LENGTH_SHORT).show();
                }
            } else {
                showNoData();
                String message = jsonResponse.optString("message", "No data found");
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing report response", e);
            showNoData();
            Toast.makeText(this, "Error parsing report", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Parse individual student data from JSON
     */
    private StudentAcademicReportModel parseStudentData(JSONObject studentJson) {
        try {
            StudentAcademicReportModel student = new StudentAcademicReportModel();

            // Parse student information
            student.setId(studentJson.optString("id", ""));
            student.setAdmissionNo(studentJson.optString("admission_no", ""));
            student.setFirstname(studentJson.optString("firstname", ""));
            student.setMiddlename(studentJson.optString("middlename", ""));
            student.setLastname(studentJson.optString("lastname", ""));
            student.setClassName(studentJson.optString("class", ""));
            student.setSection(studentJson.optString("section", ""));
            student.setRollNo(studentJson.optString("roll_no", ""));
            student.setFatherName(studentJson.optString("father_name", ""));

            // Parse fees array
            if (studentJson.has("fees")) {
                Object feesObject = studentJson.get("fees");
                List<StudentAcademicReportModel.FeeDetail> feesList = new ArrayList<>();

                // Handle both array and nested array structures
                JSONArray feesArray;
                if (feesObject instanceof JSONArray) {
                    feesArray = (JSONArray) feesObject;

                    // Check if it's a nested array (array of arrays)
                    if (feesArray.length() > 0) {
                        Object firstElement = feesArray.get(0);
                        if (firstElement instanceof JSONArray) {
                            // It's a nested array, flatten it
                            JSONArray flatArray = new JSONArray();
                            for (int i = 0; i < feesArray.length(); i++) {
                                JSONArray innerArray = feesArray.getJSONArray(i);
                                for (int j = 0; j < innerArray.length(); j++) {
                                    flatArray.put(innerArray.get(j));
                                }
                            }
                            feesArray = flatArray;
                        }
                    }
                } else {
                    // If it's not an array, skip fees parsing
                    Log.w(TAG, "Fees is not an array, skipping");
                    student.setFees(feesList);
                    return student;
                }

                // Parse each fee item
                for (int i = 0; i < feesArray.length(); i++) {
                    try {
                        JSONObject feeJson = feesArray.getJSONObject(i);
                        StudentAcademicReportModel.FeeDetail fee = new StudentAcademicReportModel.FeeDetail();

                        // Parse fee details
                        fee.setId(feeJson.optString("id", ""));

                        // Use 'type' field for fee name if 'name' is not available
                        String feeName = feeJson.optString("name", "");
                        if (feeName.isEmpty()) {
                            feeName = feeJson.optString("type", "Fee");
                        }
                        fee.setName(feeName);

                        fee.setAmount(feeJson.optString("amount", "0.00"));

                        // Calculate paid amount from amount_detail if available
                        String amountPaid = "0.00";
                        String amountDiscount = "0.00";
                        String amountFine = feeJson.optString("fine_amount", "0.00");

                        // Parse amount_detail to get paid amount
                        if (feeJson.has("amount_detail")) {
                            String amountDetailStr = feeJson.optString("amount_detail", "");
                            if (!amountDetailStr.isEmpty() && !amountDetailStr.equals("0")) {
                                try {
                                    JSONObject amountDetail = new JSONObject(amountDetailStr);
                                    double totalPaid = 0.0;
                                    double totalDiscount = 0.0;
                                    double totalFine = 0.0;

                                    // Iterate through payment records
                                    for (java.util.Iterator<String> it = amountDetail.keys(); it.hasNext(); ) {
                                        String key = it.next();
                                        JSONObject payment = amountDetail.getJSONObject(key);
                                        totalPaid += payment.optDouble("amount", 0.0);
                                        totalDiscount += payment.optDouble("amount_discount", 0.0);
                                        totalFine += payment.optDouble("amount_fine", 0.0);
                                    }

                                    amountPaid = String.format("%.2f", totalPaid);
                                    amountDiscount = String.format("%.2f", totalDiscount);
                                    if (totalFine > 0) {
                                        amountFine = String.format("%.2f", totalFine);
                                    }
                                } catch (JSONException e) {
                                    Log.w(TAG, "Error parsing amount_detail: " + e.getMessage());
                                }
                            }
                        }

                        fee.setAmountPaid(amountPaid);
                        fee.setAmountDiscount(amountDiscount);
                        fee.setAmountFine(amountFine);

                        feesList.add(fee);
                    } catch (JSONException e) {
                        Log.w(TAG, "Error parsing fee item at index " + i + ": " + e.getMessage());
                        // Continue with next fee item
                    }
                }

                student.setFees(feesList);
            }

            return student;
        } catch (Exception e) {
            Log.e(TAG, "Error parsing student data: " + e.getMessage(), e);
            return null;
        }
    }
}
