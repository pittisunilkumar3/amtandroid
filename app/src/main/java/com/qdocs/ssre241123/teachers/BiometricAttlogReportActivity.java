package com.qdocs.ssre241123.teachers;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.ssre241123.BaseActivity;
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.BiometricAttlogReportAdapter;
import com.qdocs.ssre241123.model.BiometricAttlogReportModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Activity for Biometric Attendance Log Report
 * Shows biometric attendance log records with student details
 * 
 * API Endpoint: POST /api/biometric-attlog-report/filter
 * 
 * Features:
 * - Filter by date range (from_date, to_date)
 * - Filter by student (optional)
 * - Pagination support (limit, offset)
 * - Display attendance logs with student details
 */
public class BiometricAttlogReportActivity extends BaseActivity {

    private static final String TAG = "BiometricAttlogReport";
    private static final int DEFAULT_LIMIT = 50;

    // UI Components
    private TextView fromDateTv;
    private TextView toDateTv;
    private Spinner studentSpinner;
    private Button generateReportButton;
    private CardView summaryCard;
    private TextView totalRecordsTv;
    private TextView returnedRecordsTv;
    private TextView dateRangeTv;
    private Button loadMoreButton;
    private ProgressBar progressBar;
    private LinearLayout nodataLayout;
    private RecyclerView attlogRecyclerView;

    // Data
    private List<BiometricAttlogReportModel> attlogList;
    private BiometricAttlogReportAdapter adapter;
    private List<StudentData> studentsList;

    // Selected filters
    private String selectedFromDate = "";
    private String selectedToDate = "";
    private String selectedStudentId = null;
    private int currentOffset = 0;
    private int totalRecords = 0;

    // Date format
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private Calendar fromDateCalendar;
    private Calendar toDateCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometric_attlog_report);

        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), "langCode"));

        initializeViews();
        setupRecyclerView();
        setupDatePickers();
        setupListeners();

        // Set default date range (last 7 days)
        setDefaultDateRange();

        // Load students for dropdown
        loadStudentsFromAPI();
    }

    private void initializeViews() {
        fromDateTv = findViewById(R.id.fromDateTv);
        toDateTv = findViewById(R.id.toDateTv);
        studentSpinner = findViewById(R.id.studentSpinner);
        generateReportButton = findViewById(R.id.generateReportButton);
        summaryCard = findViewById(R.id.summaryCard);
        totalRecordsTv = findViewById(R.id.totalRecordsTv);
        returnedRecordsTv = findViewById(R.id.returnedRecordsTv);
        dateRangeTv = findViewById(R.id.dateRangeTv);
        loadMoreButton = findViewById(R.id.loadMoreButton);
        progressBar = findViewById(R.id.progressBar);
        nodataLayout = findViewById(R.id.nodataLayout);
        attlogRecyclerView = findViewById(R.id.attlogRecyclerView);

        attlogList = new ArrayList<>();
        studentsList = new ArrayList<>();
        fromDateCalendar = Calendar.getInstance();
        toDateCalendar = Calendar.getInstance();
    }

    private void setupRecyclerView() {
        adapter = new BiometricAttlogReportAdapter(this, attlogList);
        attlogRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        attlogRecyclerView.setAdapter(adapter);
    }

    private void setupDatePickers() {
        // From Date Picker
        fromDateTv.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        fromDateCalendar.set(Calendar.YEAR, year);
                        fromDateCalendar.set(Calendar.MONTH, month);
                        fromDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        selectedFromDate = dateFormat.format(fromDateCalendar.getTime());
                        fromDateTv.setText(selectedFromDate);
                    },
                    fromDateCalendar.get(Calendar.YEAR),
                    fromDateCalendar.get(Calendar.MONTH),
                    fromDateCalendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // To Date Picker
        toDateTv.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        toDateCalendar.set(Calendar.YEAR, year);
                        toDateCalendar.set(Calendar.MONTH, month);
                        toDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        selectedToDate = dateFormat.format(toDateCalendar.getTime());
                        toDateTv.setText(selectedToDate);
                    },
                    toDateCalendar.get(Calendar.YEAR),
                    toDateCalendar.get(Calendar.MONTH),
                    toDateCalendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });
    }

    private void setupListeners() {
        generateReportButton.setOnClickListener(v -> {
            currentOffset = 0;
            generateReport();
        });

        loadMoreButton.setOnClickListener(v -> {
            currentOffset += DEFAULT_LIMIT;
            loadMoreRecords();
        });

        studentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && studentsList.size() > position - 1) {
                    StudentData student = studentsList.get(position - 1);
                    selectedStudentId = student.id;
                } else {
                    selectedStudentId = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedStudentId = null;
            }
        });
    }

    private void setDefaultDateRange() {
        // Set to date to today
        toDateCalendar.setTimeInMillis(System.currentTimeMillis());
        selectedToDate = dateFormat.format(toDateCalendar.getTime());
        toDateTv.setText(selectedToDate);

        // Set from date to 7 days ago
        fromDateCalendar.setTimeInMillis(System.currentTimeMillis());
        fromDateCalendar.add(Calendar.DAY_OF_MONTH, -7);
        selectedFromDate = dateFormat.format(fromDateCalendar.getTime());
        fromDateTv.setText(selectedFromDate);
    }

    private void loadStudentsFromAPI() {
        Log.d(TAG, "Loading students from API");

        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            Toast.makeText(this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            setupDefaultStudentSpinner();
            return;
        }

        String url = Utility.buildApiUrl(getApplicationContext(), Constants.teacherStudentsUrl);
        Log.d(TAG, "Students API URL: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "Students API Response: " + response);
                    parseStudentsResponse(response);
                },
                error -> {
                    Log.e(TAG, "Error loading students", error);
                    Toast.makeText(this, "Error loading students", Toast.LENGTH_SHORT).show();
                    setupDefaultStudentSpinner();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", "application/json");
                Log.d(TAG, "Request Headers: " + headers);
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject jsonBody = new JSONObject();
                    // Empty body to get all students
                    String requestBody = jsonBody.toString();
                    Log.d(TAG, "Request Body: " + requestBody);
                    return requestBody.getBytes("utf-8");
                } catch (Exception e) {
                    Log.e(TAG, "Error creating request body", e);
                    return null;
                }
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseStudentsResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            int status = jsonResponse.optInt("status", 0);

            if (status == 1) {
                JSONArray dataArray = jsonResponse.optJSONArray("data");
                if (dataArray != null) {
                    studentsList.clear();
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject studentObj = dataArray.getJSONObject(i);
                        StudentData student = new StudentData();
                        student.id = studentObj.optString("id");
                        student.admissionNo = studentObj.optString("admission_no");
                        student.fullName = studentObj.optString("full_name");
                        studentsList.add(student);
                    }
                    Log.d(TAG, "Loaded " + studentsList.size() + " students");
                }
            }
            setupStudentSpinner();
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing students response", e);
            setupDefaultStudentSpinner();
        }
    }

    private void setupStudentSpinner() {
        List<String> studentNames = new ArrayList<>();
        studentNames.add("All Students");
        for (StudentData student : studentsList) {
            String displayName = student.fullName + " (" + student.admissionNo + ")";
            studentNames.add(displayName);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, studentNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        studentSpinner.setAdapter(adapter);
    }

    private void setupDefaultStudentSpinner() {
        List<String> studentNames = new ArrayList<>();
        studentNames.add("All Students");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, studentNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        studentSpinner.setAdapter(adapter);
    }

    private void generateReport() {
        Log.d(TAG, "Generating report with filters:");
        Log.d(TAG, "From Date: " + selectedFromDate);
        Log.d(TAG, "To Date: " + selectedToDate);
        Log.d(TAG, "Student ID: " + selectedStudentId);

        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            Toast.makeText(this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading();
        fetchBiometricAttlogReport(selectedFromDate, selectedToDate, selectedStudentId, DEFAULT_LIMIT, currentOffset);
    }

    private void loadMoreRecords() {
        Log.d(TAG, "Loading more records with offset: " + currentOffset);

        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            Toast.makeText(this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading();
        fetchBiometricAttlogReport(selectedFromDate, selectedToDate, selectedStudentId, DEFAULT_LIMIT, currentOffset);
    }

    private void fetchBiometricAttlogReport(String fromDate, String toDate, String studentId, int limit, int offset) {
        String url = Utility.buildApiUrl(getApplicationContext(), Constants.biometricAttlogReportFilterUrl);
        Log.d(TAG, "Biometric Attlog Report API URL: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "API Response: " + response);
                    hideLoading();
                    parseReportResponse(response);
                },
                error -> {
                    Log.e(TAG, "Error fetching report", error);
                    hideLoading();
                    showNoData();
                    Toast.makeText(this, "Error fetching report", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", "application/json");
                Log.d(TAG, "Request Headers: " + headers);
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject jsonBody = new JSONObject();

                    // Add filters (all are optional)
                    if (fromDate != null && !fromDate.isEmpty()) {
                        jsonBody.put("from_date", fromDate);
                    }
                    if (toDate != null && !toDate.isEmpty()) {
                        jsonBody.put("to_date", toDate);
                    }
                    if (studentId != null && !studentId.isEmpty()) {
                        jsonBody.put("student_id", Integer.parseInt(studentId));
                    }

                    // Add pagination
                    jsonBody.put("limit", limit);
                    jsonBody.put("offset", offset);

                    String requestBody = jsonBody.toString();
                    Log.d(TAG, "Request Body: " + requestBody);
                    return requestBody.getBytes("utf-8");
                } catch (Exception e) {
                    Log.e(TAG, "Error creating request body", e);
                    return null;
                }
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseReportResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            int status = jsonResponse.optInt("status", 0);

            if (status == 1) {
                // Parse summary data
                totalRecords = jsonResponse.optInt("total_records", 0);
                int returnedRecords = jsonResponse.optInt("returned_records", 0);

                // Parse data array
                JSONArray dataArray = jsonResponse.optJSONArray("data");
                if (dataArray != null && dataArray.length() > 0) {
                    // If offset is 0, clear the list (new search)
                    if (currentOffset == 0) {
                        attlogList.clear();
                    }

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject attlogObj = dataArray.getJSONObject(i);
                        BiometricAttlogReportModel attlog = new BiometricAttlogReportModel();

                        attlog.setId(attlogObj.optString("id"));
                        attlog.setStudentSessionId(attlogObj.optString("student_session_id"));
                        attlog.setDate(attlogObj.optString("date"));
                        attlog.setAttendenceTypeId(attlogObj.optString("attendence_type_id"));
                        attlog.setRemark(attlogObj.optString("remark"));
                        attlog.setBiometricAttendence(attlogObj.optString("biometric_attendence"));
                        attlog.setBiometricDeviceData(attlogObj.optString("biometric_device_data"));
                        attlog.setName(attlogObj.optString("name"));
                        attlog.setFirstname(attlogObj.optString("firstname"));
                        attlog.setMiddlename(attlogObj.optString("middlename"));
                        attlog.setLastname(attlogObj.optString("lastname"));
                        attlog.setRollNo(attlogObj.optString("roll_no"));
                        attlog.setAdmissionNo(attlogObj.optString("admission_no"));
                        attlog.setClassName(attlogObj.optString("class"));
                        attlog.setSection(attlogObj.optString("section"));

                        attlogList.add(attlog);
                    }

                    Log.d(TAG, "Parsed " + attlogList.size() + " attendance log records");

                    // Update UI
                    updateSummary(totalRecords, attlogList.size());
                    showData();
                    adapter.updateData(attlogList);

                    // Show/hide load more button
                    if (attlogList.size() < totalRecords) {
                        loadMoreButton.setVisibility(View.VISIBLE);
                    } else {
                        loadMoreButton.setVisibility(View.GONE);
                    }
                } else {
                    Log.d(TAG, "No data found in response");
                    if (currentOffset == 0) {
                        attlogList.clear();
                        adapter.updateData(attlogList);
                        showNoData();
                    } else {
                        Toast.makeText(this, "No more records", Toast.LENGTH_SHORT).show();
                        loadMoreButton.setVisibility(View.GONE);
                    }
                }
            } else {
                String message = jsonResponse.optString("message", "Failed to load report");
                Log.e(TAG, "API returned error: " + message);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                showNoData();
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing report response", e);
            Toast.makeText(this, "Error parsing report data", Toast.LENGTH_SHORT).show();
            showNoData();
        }
    }

    private void updateSummary(int total, int showing) {
        totalRecordsTv.setText(String.valueOf(total));
        returnedRecordsTv.setText(String.valueOf(showing));

        String dateRangeText = "Date Range: " + selectedFromDate + " to " + selectedToDate;
        dateRangeTv.setText(dateRangeText);
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        nodataLayout.setVisibility(View.GONE);
        attlogRecyclerView.setVisibility(View.GONE);
        summaryCard.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void showData() {
        progressBar.setVisibility(View.GONE);
        nodataLayout.setVisibility(View.GONE);
        attlogRecyclerView.setVisibility(View.VISIBLE);
        summaryCard.setVisibility(View.VISIBLE);
    }

    private void showNoData() {
        progressBar.setVisibility(View.GONE);
        nodataLayout.setVisibility(View.VISIBLE);
        attlogRecyclerView.setVisibility(View.GONE);
        summaryCard.setVisibility(View.GONE);
    }

    /**
     * Inner class for Student Data
     */
    private static class StudentData {
        String id;
        String admissionNo;
        String fullName;
    }
}
