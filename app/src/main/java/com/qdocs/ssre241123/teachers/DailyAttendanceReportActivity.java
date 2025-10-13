package com.qdocs.ssre241123.teachers;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.ssre241123.BaseActivity;
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.DailyAttendanceReportAdapter;
import com.qdocs.ssre241123.model.DailyAttendanceReportModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Activity for Daily Attendance Report
 * Shows attendance statistics grouped by class and section for a specific date
 * 
 * API Endpoint: POST /api/daily-attendance-report/filter
 * 
 * Features:
 * - Filter by date using date picker
 * - Display attendance summary statistics
 * - Show class-wise attendance breakdown
 * - Support for all attendance types (present, absent, late, excuse, half_day)
 */
public class DailyAttendanceReportActivity extends BaseActivity {

    private static final String TAG = "DailyAttendanceReport";

    // UI Components
    private TextView datePickerTv;
    private Button generateReportButton;
    private CardView summaryCard;
    private TextView totalStudentsSummaryTv;
    private TextView totalPresentSummaryTv;
    private TextView totalAbsentSummaryTv;
    private TextView overallPercentageTv;
    private TextView dateSummaryTv;
    private ProgressBar progressBar;
    private LinearLayout nodataLayout;
    private RecyclerView attendanceRecyclerView;

    // Data
    private List<DailyAttendanceReportModel> attendanceList;
    private DailyAttendanceReportAdapter adapter;

    // Selected date
    private String selectedDate = "";
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_attendance_report);

        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), "langCode"));

        initializeViews();
        setupRecyclerView();
        setupDatePicker();
        setupListeners();

        // Set today's date as default
        setTodayDate();
    }

    private void initializeViews() {
        datePickerTv = findViewById(R.id.datePickerTv);
        generateReportButton = findViewById(R.id.generateReportButton);
        summaryCard = findViewById(R.id.summaryCard);
        totalStudentsSummaryTv = findViewById(R.id.totalStudentsSummaryTv);
        totalPresentSummaryTv = findViewById(R.id.totalPresentSummaryTv);
        totalAbsentSummaryTv = findViewById(R.id.totalAbsentSummaryTv);
        overallPercentageTv = findViewById(R.id.overallPercentageTv);
        dateSummaryTv = findViewById(R.id.dateSummaryTv);
        progressBar = findViewById(R.id.progressBar);
        nodataLayout = findViewById(R.id.nodataLayout);
        attendanceRecyclerView = findViewById(R.id.attendanceRecyclerView);

        attendanceList = new ArrayList<>();
        calendar = Calendar.getInstance();
    }

    private void setupRecyclerView() {
        attendanceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DailyAttendanceReportAdapter(this, attendanceList);
        attendanceRecyclerView.setAdapter(adapter);
    }

    private void setupDatePicker() {
        datePickerTv.setOnClickListener(v -> showDatePickerDialog());
    }

    private void setupListeners() {
        generateReportButton.setOnClickListener(v -> generateReport());
    }

    private void setTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selectedDate = sdf.format(calendar.getTime());
        
        SimpleDateFormat displayFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        datePickerTv.setText(displayFormat.format(calendar.getTime()));
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            (view, year, month, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                selectedDate = sdf.format(calendar.getTime());
                
                SimpleDateFormat displayFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                datePickerTv.setText(displayFormat.format(calendar.getTime()));
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        
        datePickerDialog.show();
    }

    private void generateReport() {
        Log.d(TAG, "Generating report for date: " + selectedDate);

        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            Toast.makeText(this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading();
        fetchDailyAttendanceReport(selectedDate);
    }

    private void fetchDailyAttendanceReport(String date) {
        Log.d(TAG, "=== Fetching Daily Attendance Report ===");
        
        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + Constants.dailyAttendanceReportFilterUrl;
        
        Log.d(TAG, "Base URL: " + baseUrl);
        Log.d(TAG, "Full API URL: " + url);
        Log.d(TAG, "Date: " + date);
        
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
            response -> {
                Log.d(TAG, "=== API Response Received ===");
                Log.d(TAG, "Response Length: " + response.length());
                Log.d(TAG, "Response: " + response);
                hideLoading();
                parseDailyAttendanceReportResponse(response);
            },
            error -> {
                Log.e(TAG, "=== API Error ===");
                Log.e(TAG, "Error: " + error.toString());
                if (error.networkResponse != null) {
                    Log.e(TAG, "Status Code: " + error.networkResponse.statusCode);
                    try {
                        String errorBody = new String(error.networkResponse.data, "UTF-8");
                        Log.e(TAG, "Error Body: " + errorBody);
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                }
                hideLoading();
                showNoData();
                Toast.makeText(getApplicationContext(), 
                    "Failed to fetch attendance report: " + error.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            }) {
            
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Client-Service", "smartschool");
                headers.put("Auth-Key", "schoolAdmin@");
                headers.put("Content-Type", "application/json");
                
                Log.d(TAG, "=== Request Headers ===");
                for (java.util.Map.Entry<String, String> entry : headers.entrySet()) {
                    Log.d(TAG, entry.getKey() + ": " + entry.getValue());
                }
                
                return headers;
            }
            
            @Override
            public byte[] getBody() {
                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("date", date);
                    
                    String requestBody = jsonBody.toString();
                    Log.d(TAG, "=== Request Body ===");
                    Log.d(TAG, requestBody);
                    
                    return requestBody.getBytes("UTF-8");
                } catch (JSONException | UnsupportedEncodingException e) {
                    Log.e(TAG, "Error creating request body", e);
                    return null;
                }
            }
        };
        
        // Add request to queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        
        Log.d(TAG, "Request added to queue");
    }

    private void parseDailyAttendanceReportResponse(String response) {
        Log.d(TAG, "=== Parsing Response ===");

        try {
            JSONObject jsonResponse = new JSONObject(response);

            int status = jsonResponse.optInt("status", 0);
            String message = jsonResponse.optString("message", "");

            Log.d(TAG, "Status: " + status);
            Log.d(TAG, "Message: " + message);

            if (status == 1) {
                // Clear existing data
                attendanceList.clear();

                // Parse summary data
                if (jsonResponse.has("summary")) {
                    JSONObject summary = jsonResponse.getJSONObject("summary");
                    updateSummaryCard(summary);
                }

                // Parse attendance data
                if (jsonResponse.has("data")) {
                    JSONArray dataArray = jsonResponse.getJSONArray("data");
                    Log.d(TAG, "Total records: " + dataArray.length());

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject item = dataArray.getJSONObject(i);

                        DailyAttendanceReportModel model = new DailyAttendanceReportModel();
                        model.setClassId(item.optString("class_id", ""));
                        model.setClassName(item.optString("class_name", ""));
                        model.setSectionId(item.optString("section_id", ""));
                        model.setSectionName(item.optString("section_name", ""));
                        model.setPresent(item.optString("present", "0"));
                        model.setExcuse(item.optString("excuse", "0"));
                        model.setAbsent(item.optString("absent", "0"));
                        model.setLate(item.optString("late", "0"));
                        model.setHalfDay(item.optString("half_day", "0"));
                        model.setTotalStudent(item.optString("total_student", "0"));
                        model.setTotalPresent(item.optString("total_present", "0"));
                        model.setPresentPercent(item.optString("present_percent", "0%"));
                        model.setAbsentPercent(item.optString("absent_percent", "0%"));

                        attendanceList.add(model);

                        Log.d(TAG, "Added: " + model.getClassSectionDisplay() +
                              " - Present: " + model.getPresentPercent());
                    }
                }

                // Update UI
                if (attendanceList.isEmpty()) {
                    showNoData();
                } else {
                    showData();
                    adapter.notifyDataSetChanged();
                }

            } else {
                Log.e(TAG, "API returned error status: " + message);
                showNoData();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Log.e(TAG, "JSON parsing error", e);
            showNoData();
            Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSummaryCard(JSONObject summary) {
        try {
            String totalStudents = summary.optString("total_students", "0");
            String totalPresent = summary.optString("total_present", "0");
            String totalAbsent = summary.optString("total_absent", "0");
            String overallPresentPercentage = summary.optString("overall_present_percentage", "0%");

            totalStudentsSummaryTv.setText(totalStudents);
            totalPresentSummaryTv.setText(totalPresent);
            totalAbsentSummaryTv.setText(totalAbsent);
            overallPercentageTv.setText(overallPresentPercentage);

            SimpleDateFormat displayFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            dateSummaryTv.setText("Date: " + displayFormat.format(calendar.getTime()));

            summaryCard.setVisibility(View.VISIBLE);

            Log.d(TAG, "Summary updated - Total: " + totalStudents +
                  ", Present: " + totalPresent + ", Percentage: " + overallPresentPercentage);

        } catch (Exception e) {
            Log.e(TAG, "Error updating summary card", e);
        }
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        nodataLayout.setVisibility(View.GONE);
        attendanceRecyclerView.setVisibility(View.GONE);
        summaryCard.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void showData() {
        nodataLayout.setVisibility(View.GONE);
        attendanceRecyclerView.setVisibility(View.VISIBLE);
        summaryCard.setVisibility(View.VISIBLE);
    }

    private void showNoData() {
        nodataLayout.setVisibility(View.VISIBLE);
        attendanceRecyclerView.setVisibility(View.GONE);
        summaryCard.setVisibility(View.GONE);
    }
}

