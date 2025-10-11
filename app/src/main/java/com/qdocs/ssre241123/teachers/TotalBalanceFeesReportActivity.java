package com.qdocs.ssre241123.teachers;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.TotalStudentAcademicReportAdapter;
import com.qdocs.ssre241123.model.TotalStudentAcademicReportModel;
import com.qdocs.ssre241123.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for Total Balance Fees Report (Total Student Academic Report)
 * Filters: Session, Class, Section
 * API: /api/total-student-academic-report/filter
 */
public class TotalBalanceFeesReportActivity extends BaseFinanceReportActivity {

    private static final String TAG = "TotalBalanceFeesReport";

    private List<TotalStudentAcademicReportModel> studentList;
    private TotalStudentAcademicReportAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate called");

        // Initialize list and adapter
        studentList = new ArrayList<>();
        adapter = new TotalStudentAcademicReportAdapter(this, studentList);
        reportContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reportContentRecyclerView.setAdapter(adapter);

        Log.d(TAG, "RecyclerView and adapter initialized");
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_total_balance_fees_report;
    }

    @Override
    protected String getReportTitle() {
        return getString(R.string.total_balance_fees_report);
    }

    @Override
    protected String getReportApiUrl() {
        return Constants.totalStudentAcademicReportFilterUrl;
    }

    @Override
    protected void setupSpecificFilters() {
        // No additional filters needed - Session, Class, Section are handled by base class
    }

    @Override
    protected void parseReportResponse(String response) {
        Log.d(TAG, "=== Parsing Total Student Academic Report Response ===");
        Log.d(TAG, "Response: " + response);

        try {
            JSONObject jsonResponse = new JSONObject(response);

            int status = jsonResponse.optInt("status", 0);
            String message = jsonResponse.optString("message", "");

            Log.d(TAG, "Status: " + status);
            Log.d(TAG, "Message: " + message);

            if (status == 1) {
                JSONArray dataArray = jsonResponse.optJSONArray("data");

                if (dataArray != null && dataArray.length() > 0) {
                    Log.d(TAG, "Data array length: " + dataArray.length());

                    studentList.clear();

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject studentObj = dataArray.getJSONObject(i);

                        TotalStudentAcademicReportModel student = new TotalStudentAcademicReportModel();
                        student.setName(studentObj.optString("name", ""));
                        student.setClassName(studentObj.optString("class", ""));
                        student.setSection(studentObj.optString("section", ""));
                        student.setAdmissionNo(studentObj.optString("admission_no", ""));
                        student.setRollNo(studentObj.optString("roll_no", ""));
                        student.setFatherName(studentObj.optString("father_name", ""));
                        student.setTotalFee(studentObj.optString("total_fee", "0.00"));
                        student.setDeposit(studentObj.optString("deposit", "0.00"));
                        student.setDiscount(studentObj.optString("discount", "0.00"));
                        student.setFine(studentObj.optString("fine", "0.00"));
                        student.setBalance(studentObj.optString("balance", "0.00"));

                        studentList.add(student);

                        Log.d(TAG, "Added student: " + student.getName() + " - Balance: " + student.getBalance());
                    }

                    Log.d(TAG, "Total students parsed: " + studentList.size());

                    // Update adapter and show content
                    adapter.updateData(studentList);
                    showContent();

                    Toast.makeText(this, "Report loaded: " + studentList.size() + " students", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "No data in response");
                    showNoData();
                    Toast.makeText(this, "No students found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d(TAG, "Status is not 1: " + message);
                showNoData();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing report response", e);
            showNoData();
            Toast.makeText(this, "Error parsing report: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

