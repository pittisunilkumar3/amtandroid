package com.qdocs.ssre241123.teachers;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.ssre241123.R;
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
 * Activity for Fees Statement Report
 * Uses hierarchical API to load Session → Class → Section → Student dropdowns
 * Filters: Session, Class, Section, Student
 */
public class FeesStatementActivity extends AppCompatActivity {

    private static final String TAG = "FeesStatementActivity";

    // UI Components
    private FrameLayout actionBar;
    private ImageView backButton;
    private TextView titleTextView;
    private Spinner sessionSpinner, classSpinner, sectionSpinner, studentSpinner;
    private Button generateReportButton;
    private RecyclerView reportContentRecyclerView;
    private ProgressBar progressBar;
    private LinearLayout nodataLayout;
    private CardView filtersCard;

    // Data lists
    private List<SessionData> sessionsList = new ArrayList<>();
    private List<ClassData> currentClassesList = new ArrayList<>();
    private List<SectionData> currentSectionsList = new ArrayList<>();
    private List<StudentData> currentStudentsList = new ArrayList<>();

    // Selected values
    private String selectedSessionId = null;
    private String selectedClassId = null;
    private String selectedSectionId = null;
    private String selectedStudentId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fees_statement);

        Log.d(TAG, "onCreate called");

        initializeViews();
        setupActionBar();
        setupSpinners();
        setupGenerateButton();
        loadHierarchicalData();
    }

    private void initializeViews() {
        actionBar = findViewById(R.id.actionBar);
        backButton = findViewById(R.id.actionBarBackBtn);
        titleTextView = findViewById(R.id.actionBarTitleTV);
        sessionSpinner = findViewById(R.id.sessionSpinner);
        classSpinner = findViewById(R.id.classSpinner);
        sectionSpinner = findViewById(R.id.sectionSpinner);
        studentSpinner = findViewById(R.id.studentSpinner);
        generateReportButton = findViewById(R.id.generateReportButton);
        reportContentRecyclerView = findViewById(R.id.reportContentRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        nodataLayout = findViewById(R.id.nodataLayout);
        filtersCard = findViewById(R.id.filtersCard);

        titleTextView.setText(R.string.fees_statement);

        // Setup RecyclerView
        reportContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reportContentRecyclerView.setHasFixedSize(true);
    }

    private void setupActionBar() {
        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                finish();
                overridePendingTransition(R.anim.slide_rightleft, R.anim.no_animation);
            });
        }

        // Apply theme colors
        String primaryColor = Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour);
        if (primaryColor != null && !primaryColor.isEmpty()) {
            try {
                if (actionBar != null) {
                    actionBar.setBackgroundColor(Color.parseColor(primaryColor));
                }
                if (generateReportButton != null) {
                    generateReportButton.setBackgroundColor(Color.parseColor(primaryColor));
                }
            } catch (Exception e) {
                Log.e(TAG, "Error parsing primary color", e);
            }
        }
    }

    private void setupSpinners() {
        // Session Spinner
        sessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && sessionsList.size() > position - 1) {
                    SessionData session = sessionsList.get(position - 1);
                    selectedSessionId = session.id;
                    updateClassSpinner(session.classes);
                } else {
                    selectedSessionId = null;
                    currentClassesList.clear();
                    updateClassSpinner(new ArrayList<>());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSessionId = null;
            }
        });

        // Class Spinner
        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && currentClassesList.size() > position - 1) {
                    ClassData classData = currentClassesList.get(position - 1);
                    selectedClassId = classData.id;
                    updateSectionSpinner(classData.sections);
                } else {
                    selectedClassId = null;
                    currentSectionsList.clear();
                    updateSectionSpinner(new ArrayList<>());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedClassId = null;
            }
        });

        // Section Spinner
        sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && currentSectionsList.size() > position - 1) {
                    SectionData section = currentSectionsList.get(position - 1);
                    selectedSectionId = section.id;
                    updateStudentSpinner(section.students);
                } else {
                    selectedSectionId = null;
                    currentStudentsList.clear();
                    updateStudentSpinner(new ArrayList<>());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSectionId = null;
            }
        });

        // Student Spinner
        studentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && currentStudentsList.size() > position - 1) {
                    StudentData student = currentStudentsList.get(position - 1);
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

    private void setupGenerateButton() {
        if (generateReportButton != null) {
            generateReportButton.setOnClickListener(v -> generateReport());
        }
    }

    private void generateReport() {
        Log.d(TAG, "Generate Report clicked");
        Log.d(TAG, "Selected Session ID: " + selectedSessionId);
        Log.d(TAG, "Selected Class ID: " + selectedClassId);
        Log.d(TAG, "Selected Section ID: " + selectedSectionId);
        Log.d(TAG, "Selected Student ID: " + selectedStudentId);

        // Validate that student is selected
        if (selectedStudentId == null || selectedStudentId.isEmpty()) {
            Toast.makeText(this, "Please select a student", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading();
        fetchReport();
    }

    private void loadHierarchicalData() {
        Log.d(TAG, "Loading hierarchical data from API");
        showLoading();

        String url = Constants.domain + "/api/" + Constants.feeCollectionFiltersGetHierarchyUrl;
        Log.d(TAG, "API URL: " + url);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Hierarchical data loaded successfully");
                        Log.d(TAG, "Response: " + response);
                        hideLoading();
                        parseHierarchicalData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error loading hierarchical data", error);
                        hideLoading();
                        Toast.makeText(FeesStatementActivity.this,
                                "Error loading filters: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                return headers;
            }

            @Override
            public byte[] getBody() {
                // Send empty JSON body
                return "{}".getBytes();
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void parseHierarchicalData(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            int status = jsonResponse.optInt("status", 0);

            if (status == 1) {
                JSONArray dataArray = jsonResponse.optJSONArray("data");
                if (dataArray != null) {
                    sessionsList.clear();

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject sessionObj = dataArray.getJSONObject(i);
                        SessionData session = new SessionData();
                        session.id = sessionObj.optString("id");
                        session.name = sessionObj.optString("name");
                        session.classes = new ArrayList<>();

                        // Parse classes
                        JSONArray classesArray = sessionObj.optJSONArray("classes");
                        if (classesArray != null) {
                            for (int j = 0; j < classesArray.length(); j++) {
                                JSONObject classObj = classesArray.getJSONObject(j);
                                ClassData classData = new ClassData();
                                classData.id = classObj.optString("id");
                                classData.name = classObj.optString("name");
                                classData.sections = new ArrayList<>();

                                // Parse sections
                                JSONArray sectionsArray = classObj.optJSONArray("sections");
                                if (sectionsArray != null) {
                                    for (int k = 0; k < sectionsArray.length(); k++) {
                                        JSONObject sectionObj = sectionsArray.getJSONObject(k);
                                        SectionData section = new SectionData();
                                        section.id = sectionObj.optString("id");
                                        section.name = sectionObj.optString("name");
                                        section.students = new ArrayList<>();

                                        // Parse students
                                        JSONArray studentsArray = sectionObj.optJSONArray("students");
                                        if (studentsArray != null) {
                                            for (int l = 0; l < studentsArray.length(); l++) {
                                                JSONObject studentObj = studentsArray.getJSONObject(l);
                                                StudentData student = new StudentData();
                                                student.id = studentObj.optString("id");
                                                student.admissionNo = studentObj.optString("admission_no");
                                                student.rollNo = studentObj.optString("roll_no");
                                                student.fullName = studentObj.optString("full_name");
                                                student.firstname = studentObj.optString("firstname");
                                                student.lastname = studentObj.optString("lastname");

                                                section.students.add(student);
                                            }
                                        }

                                        classData.sections.add(section);
                                    }
                                }

                                session.classes.add(classData);
                            }
                        }

                        sessionsList.add(session);
                    }

                    Log.d(TAG, "Parsed " + sessionsList.size() + " sessions");
                    setupSessionSpinner();
                } else {
                    Log.e(TAG, "Data array is null");
                    Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
                }
            } else {
                String message = jsonResponse.optString("message", "Failed to load data");
                Log.e(TAG, "API returned error: " + message);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing hierarchical data", e);
            Toast.makeText(this, "Error parsing data", Toast.LENGTH_SHORT).show();
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

        // Reset section and student spinners
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

        // Reset student spinner
        currentStudentsList.clear();
        updateStudentSpinner(new ArrayList<>());
    }

    private void updateStudentSpinner(List<StudentData> students) {
        currentStudentsList.clear();
        currentStudentsList.addAll(students);

        List<String> studentNames = new ArrayList<>();
        studentNames.add("Select Student");
        for (StudentData student : students) {
            String displayName = student.fullName + " (" + student.admissionNo + ")";
            studentNames.add(displayName);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, studentNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        studentSpinner.setAdapter(adapter);
    }

    private void fetchReport() {
        Log.d(TAG, "Fetching report from API");

        String url = Constants.domain + "/api/" + Constants.feesStatementFilterUrl;
        Log.d(TAG, "API URL: " + url);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Report fetched successfully");
                        Log.d(TAG, "Response: " + response);
                        hideLoading();
                        parseReportResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error fetching report", error);
                        hideLoading();
                        Toast.makeText(FeesStatementActivity.this,
                                "Error fetching report: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                return headers;
            }

            @Override
            public byte[] getBody() {
                return buildRequestBody().getBytes();
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private String buildRequestBody() {
        try {
            JSONObject jsonBody = new JSONObject();

            // Add filters
            if (selectedSessionId != null && !selectedSessionId.isEmpty()) {
                jsonBody.put("session_id", selectedSessionId);
            }
            if (selectedClassId != null && !selectedClassId.isEmpty()) {
                jsonBody.put("class_id", selectedClassId);
            }
            if (selectedSectionId != null && !selectedSectionId.isEmpty()) {
                jsonBody.put("section_id", selectedSectionId);
            }
            if (selectedStudentId != null && !selectedStudentId.isEmpty()) {
                jsonBody.put("student_id", selectedStudentId);
            }

            String body = jsonBody.toString();
            Log.d(TAG, "Request body: " + body);
            return body;
        } catch (JSONException e) {
            Log.e(TAG, "Error building request body", e);
            return "{}";
        }
    }

    private void parseReportResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            int status = jsonResponse.optInt("status", 0);

            if (status == 1) {
                // TODO: Parse and display report data
                // For now, just show success message
                Toast.makeText(this, "Report generated successfully", Toast.LENGTH_SHORT).show();
                showNoData();
            } else {
                String message = jsonResponse.optString("message", "Failed to generate report");
                Log.e(TAG, "API returned error: " + message);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                showNoData();
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing report response", e);
            Toast.makeText(this, "Error parsing report", Toast.LENGTH_SHORT).show();
            showNoData();
        }
    }

    private void showLoading() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        if (reportContentRecyclerView != null) {
            reportContentRecyclerView.setVisibility(View.GONE);
        }
        if (nodataLayout != null) {
            nodataLayout.setVisibility(View.GONE);
        }
    }

    private void hideLoading() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void showNoData() {
        if (nodataLayout != null) {
            nodataLayout.setVisibility(View.VISIBLE);
        }
        if (reportContentRecyclerView != null) {
            reportContentRecyclerView.setVisibility(View.GONE);
        }
    }

    private void showReportContent() {
        if (reportContentRecyclerView != null) {
            reportContentRecyclerView.setVisibility(View.VISIBLE);
        }
        if (nodataLayout != null) {
            nodataLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_rightleft, R.anim.no_animation);
    }

    // Data classes
    private static class SessionData {
        String id;
        String name;
        List<ClassData> classes;
    }

    private static class ClassData {
        String id;
        String name;
        List<SectionData> sections;
    }

    private static class SectionData {
        String id;
        String name;
        List<StudentData> students;
    }

    private static class StudentData {
        String id;
        String admissionNo;
        String rollNo;
        String fullName;
        String firstname;
        String lastname;
    }
}

