package com.qdocs.ssre241123.teachers;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.ssre241123.BaseActivity;
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.ClassAttendanceReportAdapter;
import com.qdocs.ssre241123.model.ClassAttendanceReportModel;
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
 * Activity for Class Attendance Report
 * Shows class-wise attendance statistics with filtering options
 * Supports filtering by: Class, Section, Month, Year
 */
public class ClassAttendanceReportActivity extends BaseActivity {

    private static final String TAG = "ClassAttendanceReport";

    // UI Components
    private Spinner sessionSpinner;
    private Spinner classSpinner;
    private Spinner sectionSpinner;
    private Spinner monthSpinner;
    private Spinner yearSpinner;
    private Button generateReportButton;
    private CardView summaryCard;
    private TextView summaryTv;
    private ProgressBar progressBar;
    private LinearLayout nodataLayout;
    private RecyclerView attendanceRecyclerView;

    // Data
    private List<SessionData> sessionsList;
    private List<ClassData> currentClassesList;
    private List<SectionData> currentSectionsList;
    private List<ClassAttendanceReportModel> attendanceList;
    private ClassAttendanceReportAdapter adapter;

    // Selected values
    private String selectedSessionId = "";
    private String selectedClassId = "";
    private String selectedSectionId = "";
    private int selectedMonth = 0;
    private int selectedYear = 0;

    // Inner classes for hierarchical data structures
    private static class SessionData {
        String id;
        String name;
        List<ClassData> classes;
    }

    private static class ClassData {
        String id;
        String name;
        List<SectionData> sections;

        ClassData(String id, String name) {
            this.id = id;
            this.name = name;
            this.sections = new ArrayList<>();
        }
    }

    private static class SectionData {
        String id;
        String name;

        SectionData(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "=== ClassAttendanceReportActivity onCreate START ===");

        // Use LayoutInflater to add content to BaseActivity's container
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_class_attendance_report, null, false);
        mDrawerLayout.addView(contentView, 0);

        Log.d(TAG, "Layout inflated and added to BaseActivity container");

        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), "langCode"));

        // Set the title in BaseActivity's title TextView
        titleTV.setText("Class Attendance Report");
        Log.d(TAG, "Title set to 'Class Attendance Report'");

        initializeViews();
        setupRecyclerView();
        loadFilterOptions();

        Log.d(TAG, "=== ClassAttendanceReportActivity onCreate COMPLETE ===");
    }

    private void initializeViews() {
        Log.d(TAG, "initializeViews: Starting view initialization");

        // Find views from the inflated content
        sessionSpinner = findViewById(R.id.sessionSpinner);
        classSpinner = findViewById(R.id.classSpinner);
        sectionSpinner = findViewById(R.id.sectionSpinner);
        monthSpinner = findViewById(R.id.monthSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        generateReportButton = findViewById(R.id.generateReportButton);
        summaryCard = findViewById(R.id.summaryCard);
        summaryTv = findViewById(R.id.summaryTv);
        progressBar = findViewById(R.id.progressBar);
        nodataLayout = findViewById(R.id.nodataLayout);
        attendanceRecyclerView = findViewById(R.id.attendanceRecyclerView);

        // Log view initialization status
        Log.d(TAG, "sessionSpinner: " + (sessionSpinner != null ? "Found" : "NULL"));
        Log.d(TAG, "classSpinner: " + (classSpinner != null ? "Found" : "NULL"));
        Log.d(TAG, "sectionSpinner: " + (sectionSpinner != null ? "Found" : "NULL"));
        Log.d(TAG, "monthSpinner: " + (monthSpinner != null ? "Found" : "NULL"));
        Log.d(TAG, "yearSpinner: " + (yearSpinner != null ? "Found" : "NULL"));
        Log.d(TAG, "generateReportButton: " + (generateReportButton != null ? "Found" : "NULL"));

        // Apply theme colors to generate button
        String primaryColor = Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour);
        Log.d(TAG, "Primary color: " + primaryColor);

        if (primaryColor != null && !primaryColor.isEmpty()) {
            try {
                if (generateReportButton != null) {
                    generateReportButton.setBackgroundColor(Color.parseColor(primaryColor));
                    Log.d(TAG, "Applied color to generate button");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error parsing primary color", e);
            }
        }

        // Initialize data lists
        sessionsList = new ArrayList<>();
        currentClassesList = new ArrayList<>();
        currentSectionsList = new ArrayList<>();
        attendanceList = new ArrayList<>();

        // Setup month and year spinners with static data
        setupMonthSpinner();
        setupYearSpinner();

        setupListeners();
        Log.d(TAG, "initializeViews: Completed");
    }

    private void setupListeners() {
        generateReportButton.setOnClickListener(v -> generateReport());

        // Session spinner listener
        sessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && sessionsList.size() > position - 1) {
                    SessionData session = sessionsList.get(position - 1);
                    selectedSessionId = session.id;
                    updateClassSpinner(session.classes);
                    Log.d(TAG, "Selected session: " + session.name + " (ID: " + selectedSessionId + ")");
                } else {
                    selectedSessionId = "";
                    currentClassesList.clear();
                    updateClassSpinner(new ArrayList<>());
                    Log.d(TAG, "Selected: All Sessions");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSessionId = "";
            }
        });

        // Class spinner listener
        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && currentClassesList.size() > position - 1) {
                    ClassData classData = currentClassesList.get(position - 1);
                    selectedClassId = classData.id;
                    updateSectionSpinner(classData.sections);
                    Log.d(TAG, "Selected class: " + classData.name + " (ID: " + selectedClassId + ")");
                } else {
                    selectedClassId = "";
                    currentSectionsList.clear();
                    updateSectionSpinner(new ArrayList<>());
                    Log.d(TAG, "Selected: All Classes");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedClassId = "";
            }
        });

        // Section spinner listener
        sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && currentSectionsList.size() > position - 1) {
                    SectionData section = currentSectionsList.get(position - 1);
                    selectedSectionId = section.id;
                    Log.d(TAG, "Selected section: " + section.name + " (ID: " + selectedSectionId + ")");
                } else {
                    selectedSectionId = "";
                    Log.d(TAG, "Selected: All Sections");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSectionId = "";
            }
        });

        // Month spinner listener
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMonth = position; // 0 = All, 1-12 = Jan-Dec
                Log.d(TAG, "Selected month: " + selectedMonth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedMonth = 0;
            }
        });

        // Year spinner listener
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedYear = 0; // All years
                } else {
                    selectedYear = Calendar.getInstance().get(Calendar.YEAR) - position + 1;
                }
                Log.d(TAG, "Selected year: " + selectedYear);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedYear = 0;
            }
        });
    }

    private void setupRecyclerView() {
        attendanceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ClassAttendanceReportAdapter(this, attendanceList);
        attendanceRecyclerView.setAdapter(adapter);
    }

    private void setupMonthSpinner() {
        List<String> months = new ArrayList<>();
        months.add("All Months");
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapter);

        // Set current month as default
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        monthSpinner.setSelection(currentMonth);
    }

    private void setupYearSpinner() {
        // Load years dynamically from sessions API
        loadYearsFromSessionsAPI();
    }

    private void loadYearsFromSessionsAPI() {
        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            Log.w(TAG, "No internet connection, using default years");
            setupDefaultYearSpinner();
            return;
        }

        String url = Utility.buildApiUrl(getApplicationContext(), Constants.classAttendanceYearsListUrl);
        Log.d(TAG, "Loading years from class-attendance-years API: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "Class Attendance Years API Response received");
                    Log.d(TAG, "Response: " + response);
                    parseYearsFromAPI(response);
                },
                error -> {
                    Log.e(TAG, "Error loading class attendance years: " + error.toString());
                    // Fallback to default year list
                    setupDefaultYearSpinner();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() {
                return "{}".getBytes();
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseYearsFromAPI(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.optInt("status", 0);

            if (status == 1) {
                JSONArray dataArray = jsonObject.optJSONArray("data");
                List<String> yearList = new ArrayList<>();
                yearList.add("All Years");

                if (dataArray != null && dataArray.length() > 0) {
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject yearObj = dataArray.getJSONObject(i);
                        String year = yearObj.optString("year", "");
                        
                        if (!year.isEmpty()) {
                            yearList.add(year);
                        }
                    }
                }

                int totalYears = jsonObject.optInt("total_years", 0);
                Log.d(TAG, "Loaded " + totalYears + " years from class-attendance-years API");

                if (yearList.size() > 1) {
                    // Setup spinner with API years
                    setupYearSpinnerWithData(yearList);
                } else {
                    Log.w(TAG, "No years found in API response, using default years");
                    setupDefaultYearSpinner();
                }
            } else {
                String message = jsonObject.optString("message", "Failed to load years");
                Log.w(TAG, "API returned status 0: " + message);
                setupDefaultYearSpinner();
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing class attendance years response: " + e.getMessage());
            setupDefaultYearSpinner();
        }
    }

    private void setupDefaultYearSpinner() {
        Log.d(TAG, "Setting up default year spinner (current + 9 years)");
        
        List<String> years = new ArrayList<>();
        years.add("All Years");

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < 10; i++) {
            years.add(String.valueOf(currentYear - i));
        }

        setupYearSpinnerWithData(years);
    }

    private void setupYearSpinnerWithData(List<String> years) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(adapter);

        // Set current year as default
        yearSpinner.setSelection(1);
    }

    private void loadFilterOptions() {
        Log.d(TAG, "=== Loading hierarchical data from API (like FeesStatementActivity) ===");

        // Check network connectivity first
        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            Toast.makeText(this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "No internet connection available");
            return;
        }

        showLoading();

        // Use the same hierarchical API as FeesStatementActivity
        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + Constants.feeCollectionFiltersGetHierarchyUrl;

        Log.d(TAG, "Base URL: " + baseUrl);
        Log.d(TAG, "Endpoint: " + Constants.feeCollectionFiltersGetHierarchyUrl);
        Log.d(TAG, "Full API URL: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "=== API SUCCESS ===");
                    Log.d(TAG, "Response length: " + response.length());
                    hideLoading();
                    parseHierarchicalData(response);
                },
                error -> {
                    Log.e(TAG, "=== API ERROR ===");
                    Log.e(TAG, "Error loading hierarchical data: " + error.toString());
                    if (error.networkResponse != null) {
                        Log.e(TAG, "Error code: " + error.networkResponse.statusCode);
                        try {
                            String errorBody = new String(error.networkResponse.data, "UTF-8");
                            Log.e(TAG, "Error response: " + errorBody);
                        } catch (Exception e) {
                            Log.e(TAG, "Error reading error response", e);
                        }
                    }
                    hideLoading();
                    Toast.makeText(this, "Error loading filters. Please try again.", Toast.LENGTH_LONG).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                
                Log.d(TAG, "Request Headers:");
                Log.d(TAG, "  Client-Service: " + Constants.clientService);
                Log.d(TAG, "  Auth-Key: " + Constants.authKey);
                
                return headers;
            }

            @Override
            public byte[] getBody() {
                String requestBody = "{}";
                Log.d(TAG, "Request body: " + requestBody);
                return requestBody.getBytes();
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        Log.d(TAG, "Request added to queue");
    }

    private void parseHierarchicalData(String response) {
        try {
            Log.d(TAG, "=== Parsing hierarchical data ===");
            JSONObject jsonResponse = new JSONObject(response);
            int status = jsonResponse.optInt("status", 0);

            if (status == 1) {
                // Data is a direct array of sessions
                JSONArray sessionsArray = jsonResponse.optJSONArray("data");

                if (sessionsArray != null && sessionsArray.length() > 0) {
                    sessionsList.clear();
                    Log.d(TAG, "Found " + sessionsArray.length() + " sessions in response");

                    for (int i = 0; i < sessionsArray.length(); i++) {
                        JSONObject sessionObj = sessionsArray.getJSONObject(i);
                        SessionData session = new SessionData();
                        session.id = sessionObj.optString("id");
                        session.name = sessionObj.optString("name");
                        session.classes = new ArrayList<>();

                        Log.d(TAG, "Parsing session: " + session.name + " (ID: " + session.id + ")");

                        // Parse classes
                        JSONArray classesArray = sessionObj.optJSONArray("classes");
                        if (classesArray != null) {
                            Log.d(TAG, "Found " + classesArray.length() + " classes in session " + session.name);

                            for (int j = 0; j < classesArray.length(); j++) {
                                JSONObject classObj = classesArray.getJSONObject(j);
                                ClassData classData = new ClassData(
                                    classObj.optString("id"),
                                    classObj.optString("name")
                                );
                                classData.sections = new ArrayList<>();

                                Log.d(TAG, "Parsing class: " + classData.name + " (ID: " + classData.id + ")");

                                // Parse sections
                                JSONArray sectionsArray = classObj.optJSONArray("sections");
                                if (sectionsArray != null) {
                                    Log.d(TAG, "Found " + sectionsArray.length() + " sections in class " + classData.name);

                                    for (int k = 0; k < sectionsArray.length(); k++) {
                                        JSONObject sectionObj = sectionsArray.getJSONObject(k);
                                        SectionData section = new SectionData(
                                            sectionObj.optString("id"),
                                            sectionObj.optString("name")
                                        );
                                        classData.sections.add(section);
                                        Log.d(TAG, "Added section: " + section.name + " (ID: " + section.id + ")");
                                    }
                                }

                                session.classes.add(classData);
                            }
                        }

                        sessionsList.add(session);
                    }

                    Log.d(TAG, "✓ Successfully parsed " + sessionsList.size() + " sessions with hierarchical data");
                    setupSessionSpinner();
                    Toast.makeText(this, "✓ Loaded " + sessionsList.size() + " sessions", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "No sessions data found in response");
                    Toast.makeText(this, "No sessions available", Toast.LENGTH_SHORT).show();
                }
            } else {
                String message = jsonResponse.optString("message", "Failed to load data");
                Log.e(TAG, "API returned error status: " + status + ", message: " + message);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing hierarchical data", e);
            Log.e(TAG, "Response was: " + response);
            Toast.makeText(this, "Error parsing data: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setupSessionSpinner() {
        List<String> sessionNames = new ArrayList<>();
        sessionNames.add("Select Session");
        for (SessionData session : sessionsList) {
            sessionNames.add(session.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sessionNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sessionSpinner.setAdapter(adapter);
        
        Log.d(TAG, "✓ Session spinner setup with " + sessionNames.size() + " items");
    }

    private void updateClassSpinner(List<ClassData> classes) {
        currentClassesList.clear();
        currentClassesList.addAll(classes);

        List<String> classNames = new ArrayList<>();
        classNames.add("Select Class");
        for (ClassData classData : classes) {
            classNames.add(classData.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, classNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter);
        
        Log.d(TAG, "✓ Class spinner updated with " + classNames.size() + " items");

        // Reset dependent spinners
        currentSectionsList.clear();
        updateSectionSpinner(new ArrayList<>());
    }

    private void updateSectionSpinner(List<SectionData> sections) {
        currentSectionsList.clear();
        currentSectionsList.addAll(sections);

        List<String> sectionNames = new ArrayList<>();
        sectionNames.add("Select Section");
        for (SectionData section : sections) {
            sectionNames.add(section.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sectionNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectionSpinner.setAdapter(adapter);
        
        Log.d(TAG, "✓ Section spinner updated with " + sectionNames.size() + " items");
    }

    private void generateReport() {
        Log.d(TAG, "Generating report with filters:");
        Log.d(TAG, "Class ID: " + selectedClassId);
        Log.d(TAG, "Section ID: " + selectedSectionId);
        Log.d(TAG, "Month: " + selectedMonth);
        Log.d(TAG, "Year: " + selectedYear);

        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            Toast.makeText(this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading();

        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + Constants.classAttendanceReportFilterUrl;

        Log.d(TAG, "Fetching attendance report from: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "Attendance report response: " + response);
                    hideLoading();
                    parseAttendanceResponse(response);
                },
                error -> {
                    Log.e(TAG, "Error fetching attendance report", error);
                    hideLoading();
                    showNoData();
                    Toast.makeText(this, "Error loading report", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() {
                try {
                    JSONObject jsonBody = new JSONObject();

                    // Add filters only if selected
                    if (!selectedClassId.isEmpty()) {
                        jsonBody.put("class_id", Integer.parseInt(selectedClassId));
                    }
                    if (!selectedSectionId.isEmpty()) {
                        jsonBody.put("section_id", Integer.parseInt(selectedSectionId));
                    }

                    // Add date range based on month and year
                    if (selectedMonth > 0 && selectedYear > 0) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, selectedYear);
                        calendar.set(Calendar.MONTH, selectedMonth - 1);
                        calendar.set(Calendar.DAY_OF_MONTH, 1);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String fromDate = sdf.format(calendar.getTime());

                        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                        String toDate = sdf.format(calendar.getTime());

                        jsonBody.put("from_date", fromDate);
                        jsonBody.put("to_date", toDate);

                        Log.d(TAG, "Date range: " + fromDate + " to " + toDate);
                    }

                    String requestBody = jsonBody.toString();
                    Log.d(TAG, "Request body: " + requestBody);
                    return requestBody.getBytes();
                } catch (Exception e) {
                    Log.e(TAG, "Error creating request body", e);
                    return "{}".getBytes();
                }
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseAttendanceResponse(String response) {
        try {
            Log.d(TAG, "=== Parsing Attendance Response ===");
            Log.d(TAG, "Full Response: " + response);
            
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.optInt("status", 0);
            
            if (status == 1) {
                // Get the data array - contains individual student records
                JSONArray dataArray = jsonObject.optJSONArray("data");
                int totalRecords = jsonObject.optInt("total_records", 0);
                
                Log.d(TAG, "Status: " + status);
                Log.d(TAG, "Total records: " + totalRecords);
                Log.d(TAG, "Data array length: " + (dataArray != null ? dataArray.length() : "null"));

                attendanceList.clear();

                if (dataArray != null && dataArray.length() > 0) {
                    Log.d(TAG, "Processing " + dataArray.length() + " student attendance records");

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject studentObj = dataArray.getJSONObject(i);

                        ClassAttendanceReportModel attendance = new ClassAttendanceReportModel();
                        
                        // Parse student information
                        attendance.setStudentId(studentObj.optString("student_id", ""));
                        attendance.setAdmissionNo(studentObj.optString("admission_no", ""));
                        attendance.setGender(studentObj.optString("gender", ""));
                        
                        // Build student name from parts
                        String firstName = studentObj.optString("firstname", "");
                        String middleName = studentObj.optString("middlename", "");
                        String lastName = studentObj.optString("lastname", "");
                        
                        StringBuilder fullName = new StringBuilder();
                        if (!firstName.isEmpty()) {
                            fullName.append(firstName);
                        }
                        if (!middleName.isEmpty() && !middleName.equals("null")) {
                            if (fullName.length() > 0) fullName.append(" ");
                            fullName.append(middleName);
                        }
                        if (!lastName.isEmpty()) {
                            if (fullName.length() > 0) fullName.append(" ");
                            fullName.append(lastName);
                        }
                        attendance.setStudentName(fullName.toString().trim());
                        
                        // Parse class and section
                        attendance.setClassId(studentObj.optString("class_id", ""));
                        attendance.setClassName(studentObj.optString("class", ""));
                        attendance.setSectionId(studentObj.optString("section_id", ""));
                        attendance.setSectionName(studentObj.optString("section", ""));
                        
                        // Parse attendance counts
                        attendance.setPresentCount(studentObj.optString("present_count", "0"));
                        attendance.setExcuseCount(studentObj.optString("excuse_count", "0"));
                        attendance.setLateCount(studentObj.optString("late_count", "0"));
                        attendance.setHalfDayCount(studentObj.optString("half_day_count", "0"));
                        attendance.setAbsentCount(studentObj.optString("absent_count", "0"));
                        attendance.setTotalDays(studentObj.optInt("total_days", 0));
                        attendance.setTotalPresent(studentObj.optString("total_present", "0"));
                        attendance.setPresentPercentage(studentObj.optString("attendance_percentage", "0%"));
                        
                        // Calculate absent percentage if not provided
                        String attendancePercentage = studentObj.optString("attendance_percentage", "0%");
                        try {
                            double percentage = Double.parseDouble(attendancePercentage.replace("%", ""));
                            double absentPercentage = 100.0 - percentage;
                            attendance.setAbsentPercentage(String.format("%.2f%%", absentPercentage));
                        } catch (Exception e) {
                            attendance.setAbsentPercentage("0%");
                        }

                        Log.d(TAG, "Record " + i + ": " + attendance.getStudentName() + " - " + 
                              attendance.getClassSection() + " - " + attendance.getPresentPercentage());

                        attendanceList.add(attendance);
                    }

                    Log.d(TAG, "Successfully parsed " + attendanceList.size() + " attendance records");

                    // Update UI
                    adapter.notifyDataSetChanged();
                    showData();

                    // Update summary from API
                    JSONObject summary = jsonObject.optJSONObject("summary");
                    if (summary != null) {
                        int totalStudents = summary.optInt("total_students", 0);
                        int totalPresent = summary.optInt("total_present", 0);
                        int totalAbsent = summary.optInt("total_absent", 0);
                        int totalAttendanceDays = summary.optInt("total_attendance_days", 0);

                        StringBuilder summaryText = new StringBuilder();
                        summaryText.append("Total Students: ").append(totalStudents).append("\n");
                        summaryText.append("Total Present: ").append(totalPresent).append("\n");
                        summaryText.append("Total Absent: ").append(totalAbsent).append("\n");
                        summaryText.append("Total Attendance Days: ").append(totalAttendanceDays);

                        summaryTv.setText(summaryText.toString());
                    } else {
                        summaryTv.setText("Total Records: " + totalRecords);
                    }

                    Toast.makeText(this, "Found " + totalRecords + " student records", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "No attendance data found in response");
                    showNoData();
                    Toast.makeText(this, "No attendance records found for selected filters", Toast.LENGTH_SHORT).show();
                }
            } else {
                String message = jsonObject.optString("message", "Failed to load attendance report");
                Log.e(TAG, "API returned error status. Message: " + message);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                showNoData();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing attendance response", e);
            Log.e(TAG, "Response that failed to parse: " + response);
            Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
            showNoData();
        }
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        attendanceRecyclerView.setVisibility(View.GONE);
        nodataLayout.setVisibility(View.GONE);
        summaryCard.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void showData() {
        attendanceRecyclerView.setVisibility(View.VISIBLE);
        summaryCard.setVisibility(View.VISIBLE);
        nodataLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void showNoData() {
        nodataLayout.setVisibility(View.VISIBLE);
        attendanceRecyclerView.setVisibility(View.GONE);
        summaryCard.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_rightleft, R.anim.no_animation);
    }
}
