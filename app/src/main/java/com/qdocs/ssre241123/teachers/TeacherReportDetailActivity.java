package com.qdocs.ssre241123.teachers;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.ssre241123.BaseActivity;
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherReportDetailActivity extends BaseActivity {

    private static final String TAG = "TeacherReportDetail";

    // UI Components
    private FrameLayout actionBar;
    private ImageView backButton;
    private TextView titleTextView;
    private Spinner sessionSpinner;
    private Spinner classSpinner;
    private Spinner sectionSpinner;
    private Button generateReportButton;
    private RecyclerView reportContentRecyclerView;
    private ProgressBar progressBar;
    private LinearLayout nodataLayout;

    // Data structures for API data
    private List<SessionData> sessionsList;
    private List<ClassData> classesList;
    private List<SectionData> sectionsList;

    private String selectedSessionId;
    private String selectedClassId;
    private String selectedSectionId;

    // Report metadata
    private String reportId;
    private String reportName;
    private String categoryId;

    // Inner classes for data structures
    protected static class SessionData {
        String id;
        String name;
        List<ClassData> classes;

        SessionData(String id, String name) {
            this.id = id;
            this.name = name;
            this.classes = new ArrayList<>();
        }
    }

    protected static class ClassData {
        String id;
        String name;
        List<SectionData> sections;

        ClassData(String id, String name) {
            this.id = id;
            this.name = name;
            this.sections = new ArrayList<>();
        }
    }

    protected static class SectionData {
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
        setContentView(R.layout.activity_teacher_report_detail);

        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), "langCode"));

        getIntentData();
        initializeViews();
        setupRecyclerView();
        loadSessionsFromAPI();
    }

    private void getIntentData() {
        reportId = getIntent().getStringExtra("report_id");
        reportName = getIntent().getStringExtra("report_name");
        categoryId = getIntent().getStringExtra("category_id");
    }

    private void initializeViews() {
        actionBar = findViewById(R.id.actionBar);
        backButton = findViewById(R.id.back_button);
        titleTextView = findViewById(R.id.title);
        sessionSpinner = findViewById(R.id.session_spinner);
        classSpinner = findViewById(R.id.class_spinner);
        sectionSpinner = findViewById(R.id.section_spinner);
        generateReportButton = findViewById(R.id.generate_report_button);
        reportContentRecyclerView = findViewById(R.id.report_content_recyclerView);
        progressBar = findViewById(R.id.progressBar);
        nodataLayout = findViewById(R.id.nodata_layout);

        // Set title
        if (reportName != null) {
            titleTextView.setText(reportName);
        }

        // Apply theme colors
        String primaryColor = Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour);
        if (primaryColor != null && !primaryColor.isEmpty()) {
            try {
                actionBar.setBackgroundColor(Color.parseColor(primaryColor));
                generateReportButton.setBackgroundColor(Color.parseColor(primaryColor));
            } catch (Exception e) {
                Log.e(TAG, "Error parsing primary color", e);
            }
        }

        // Initialize data lists
        sessionsList = new ArrayList<>();
        classesList = new ArrayList<>();
        sectionsList = new ArrayList<>();

        setupListeners();
    }

    private void setupListeners() {
        backButton.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_rightleft, R.anim.no_animation);
        });

        generateReportButton.setOnClickListener(v -> generateReport());

        sessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && sessionsList.size() > position - 1) {
                    SessionData session = sessionsList.get(position - 1);
                    selectedSessionId = session.id;
                    loadClassesForSession(session);
                } else {
                    selectedSessionId = null;
                    classesList.clear();
                    sectionsList.clear();
                    setupClassSpinner();
                    setupSectionSpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSessionId = null;
            }
        });

        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && classesList.size() > position - 1) {
                    ClassData classData = classesList.get(position - 1);
                    selectedClassId = classData.id;
                    loadSectionsForClass(classData);
                } else {
                    selectedClassId = null;
                    sectionsList.clear();
                    setupSectionSpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedClassId = null;
            }
        });

        sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && sectionsList.size() > position - 1) {
                    SectionData section = sectionsList.get(position - 1);
                    selectedSectionId = section.id;
                } else {
                    selectedSectionId = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSectionId = null;
            }
        });
    }

    private void setupRecyclerView() {
        reportContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void generateReport() {
        if (selectedSessionId == null || selectedClassId == null || selectedSectionId == null) {
            Toast.makeText(this, "Please select all filters", Toast.LENGTH_SHORT).show();
            return;
        }

        // This method will be overridden in child classes
        loadReportData();
    }

    protected void loadReportData() {
        // Override this method in child classes to load specific report data
        Toast.makeText(this, "Report generation not implemented yet", Toast.LENGTH_SHORT).show();
    }

    private void loadSessionsFromAPI() {
        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            Toast.makeText(this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading();

        String url = Utility.buildApiUrl(getApplicationContext(), "teacher/sessions-with-classes-sections");
        Log.d(TAG, "Sessions API URL: " + url);

        JSONObject requestBody = new JSONObject();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    hideLoading();
                    Log.d(TAG, "Sessions Response: " + response);
                    parseSessionsResponse(response);
                },
                error -> {
                    hideLoading();
                    Log.e(TAG, "Sessions API Error: " + error.toString());
                    Toast.makeText(this, "Error loading sessions", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody.toString().getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    return null;
                }
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseSessionsResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.optInt("status", 0);

            if (status == 1) {
                JSONArray dataArray = jsonObject.optJSONArray("data");
                if (dataArray != null) {
                    sessionsList.clear();

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject sessionObj = dataArray.getJSONObject(i);
                        String sessionId = sessionObj.optString("session_id");
                        String sessionName = sessionObj.optString("session_name");

                        SessionData session = new SessionData(sessionId, sessionName);

                        // Parse classes for this session
                        JSONArray classesArray = sessionObj.optJSONArray("classes");
                        if (classesArray != null) {
                            for (int j = 0; j < classesArray.length(); j++) {
                                JSONObject classObj = classesArray.getJSONObject(j);
                                String classId = classObj.optString("class_id");
                                String className = classObj.optString("class_name");

                                ClassData classData = new ClassData(classId, className);

                                // Parse sections for this class
                                JSONArray sectionsArray = classObj.optJSONArray("sections");
                                if (sectionsArray != null) {
                                    for (int k = 0; k < sectionsArray.length(); k++) {
                                        JSONObject sectionObj = sectionsArray.getJSONObject(k);
                                        String sectionId = sectionObj.optString("section_id");
                                        String sectionName = sectionObj.optString("section_name");

                                        SectionData section = new SectionData(sectionId, sectionName);
                                        classData.sections.add(section);
                                    }
                                }

                                session.classes.add(classData);
                            }
                        }

                        sessionsList.add(session);
                    }

                    setupSessionSpinner();
                }
            } else {
                String message = jsonObject.optString("message", "Failed to load sessions");
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing sessions response", e);
            Toast.makeText(this, "Error parsing sessions data", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupSessionSpinner() {
        List<String> sessionNames = new ArrayList<>();
        sessionNames.add("Select Session");
        for (SessionData session : sessionsList) {
            sessionNames.add(session.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sessionNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sessionSpinner.setAdapter(adapter);
    }

    private void loadClassesForSession(SessionData session) {
        classesList.clear();
        classesList.addAll(session.classes);
        setupClassSpinner();

        // Clear sections
        sectionsList.clear();
        setupSectionSpinner();
    }

    private void setupClassSpinner() {
        List<String> classNames = new ArrayList<>();
        classNames.add("Select Class");
        for (ClassData classData : classesList) {
            classNames.add(classData.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter);
    }

    private void loadSectionsForClass(ClassData classData) {
        sectionsList.clear();
        sectionsList.addAll(classData.sections);
        setupSectionSpinner();
    }

    private void setupSectionSpinner() {
        List<String> sectionNames = new ArrayList<>();
        sectionNames.add("Select Section");
        for (SectionData section : sectionsList) {
            sectionNames.add(section.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sectionNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectionSpinner.setAdapter(adapter);
    }

    protected void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        nodataLayout.setVisibility(View.GONE);
        reportContentRecyclerView.setVisibility(View.GONE);
    }

    protected void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    protected void showNoData() {
        nodataLayout.setVisibility(View.VISIBLE);
        reportContentRecyclerView.setVisibility(View.GONE);
    }

    protected void showContent() {
        nodataLayout.setVisibility(View.GONE);
        reportContentRecyclerView.setVisibility(View.VISIBLE);
    }

    // Getters for selected values (to be used by child classes)
    protected String getSelectedSessionId() {
        return selectedSessionId;
    }

    protected String getSelectedClassId() {
        return selectedClassId;
    }

    protected String getSelectedSectionId() {
        return selectedSectionId;
    }

    protected String getReportId() {
        return reportId;
    }

    protected String getReportName() {
        return reportName;
    }

    protected String getCategoryId() {
        return categoryId;
    }

    protected RecyclerView getReportContentRecyclerView() {
        return reportContentRecyclerView;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_rightleft, R.anim.no_animation);
    }
}

