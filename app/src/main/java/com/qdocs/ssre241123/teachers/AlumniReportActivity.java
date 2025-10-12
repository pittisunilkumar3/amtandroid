package com.qdocs.ssre241123.teachers;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.ssre241123.BaseActivity;
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.AlumniAdapter;
import com.qdocs.ssre241123.model.AlumniModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlumniReportActivity extends BaseActivity {

    private static final String TAG = "AlumniReportActivity";

    // UI Components
    private Spinner sessionSpinner;
    private Spinner classSpinner;
    private Spinner sectionSpinner;
    private Spinner categorySpinner;
    private Button generateReportButton;
    private CardView summaryCard;
    private TextView totalRecordsTv;
    private ProgressBar progressBar;
    private LinearLayout nodataLayout;
    private RecyclerView alumniRecyclerView;

    // Data
    private List<SessionData> sessionsList;
    private List<ClassData> classesList;
    private List<SectionData> sectionsList;
    private List<CategoryData> categoriesList;
    private List<AlumniModel> alumniList;
    private AlumniAdapter adapter;

    // Selected values
    private String selectedSessionId = "";
    private String selectedClassId = "";
    private String selectedSectionId = "";
    private String selectedCategoryId = "";

    // Inner classes for data structures
    private static class SessionData {
        String id;
        String name;

        SessionData(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    private static class ClassData {
        String id;
        String name;

        ClassData(String id, String name) {
            this.id = id;
            this.name = name;
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

    private static class CategoryData {
        String id;
        String name;

        CategoryData(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "=== AlumniReportActivity onCreate START ===");

        // Use LayoutInflater to add content to BaseActivity's container
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_alumni_report, null, false);
        mDrawerLayout.addView(contentView, 0);
        
        Log.d(TAG, "Layout inflated and added to BaseActivity container");

        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), "langCode"));

        // Set the title in BaseActivity's title TextView
        titleTV.setText("Alumni Report");
        Log.d(TAG, "Title set to 'Alumni Report'");

        initializeViews();
        setupRecyclerView();
        loadFilterOptions();
        
        Log.d(TAG, "=== AlumniReportActivity onCreate COMPLETE ===");
    }

    private void initializeViews() {
        Log.d(TAG, "initializeViews: Starting view initialization");

        // Find views from the inflated content
        sessionSpinner = findViewById(R.id.sessionSpinner);
        classSpinner = findViewById(R.id.classSpinner);
        sectionSpinner = findViewById(R.id.sectionSpinner);
        categorySpinner = findViewById(R.id.categorySpinner);
        generateReportButton = findViewById(R.id.generateReportButton);
        summaryCard = findViewById(R.id.summaryCard);
        totalRecordsTv = findViewById(R.id.totalRecordsTv);
        progressBar = findViewById(R.id.progressBar);
        nodataLayout = findViewById(R.id.nodataLayout);
        alumniRecyclerView = findViewById(R.id.alumniRecyclerView);

        // Log view initialization status
        Log.d(TAG, "sessionSpinner: " + (sessionSpinner != null ? "Found" : "NULL"));
        Log.d(TAG, "classSpinner: " + (classSpinner != null ? "Found" : "NULL"));
        Log.d(TAG, "sectionSpinner: " + (sectionSpinner != null ? "Found" : "NULL"));
        Log.d(TAG, "categorySpinner: " + (categorySpinner != null ? "Found" : "NULL"));
        Log.d(TAG, "generateReportButton: " + (generateReportButton != null ? "Found" : "NULL"));
        Log.d(TAG, "summaryCard: " + (summaryCard != null ? "Found" : "NULL"));
        Log.d(TAG, "progressBar: " + (progressBar != null ? "Found" : "NULL"));
        Log.d(TAG, "nodataLayout: " + (nodataLayout != null ? "Found" : "NULL"));
        Log.d(TAG, "alumniRecyclerView: " + (alumniRecyclerView != null ? "Found" : "NULL"));

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
        classesList = new ArrayList<>();
        sectionsList = new ArrayList<>();
        categoriesList = new ArrayList<>();
        alumniList = new ArrayList<>();

        setupListeners();
        Log.d(TAG, "initializeViews: Completed");
    }

    private void setupListeners() {
        // Back button is handled by BaseActivity

        generateReportButton.setOnClickListener(v -> generateReport());

        // Session spinner listener
        sessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && sessionsList.size() > position - 1) {
                    selectedSessionId = sessionsList.get(position - 1).id;
                } else {
                    selectedSessionId = "";
                }
                Log.d(TAG, "Selected session ID: " + selectedSessionId);
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
                if (position > 0 && classesList.size() > position - 1) {
                    selectedClassId = classesList.get(position - 1).id;
                } else {
                    selectedClassId = "";
                }
                Log.d(TAG, "Selected class ID: " + selectedClassId);
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
                if (position > 0 && sectionsList.size() > position - 1) {
                    selectedSectionId = sectionsList.get(position - 1).id;
                } else {
                    selectedSectionId = "";
                }
                Log.d(TAG, "Selected section ID: " + selectedSectionId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSectionId = "";
            }
        });

        // Category spinner listener
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && categoriesList.size() > position - 1) {
                    selectedCategoryId = categoriesList.get(position - 1).id;
                } else {
                    selectedCategoryId = "";
                }
                Log.d(TAG, "Selected category ID: " + selectedCategoryId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategoryId = "";
            }
        });
    }

    private void setupRecyclerView() {
        alumniRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AlumniAdapter(this, alumniList);
        alumniRecyclerView.setAdapter(adapter);
    }

    private void loadFilterOptions() {
        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            Toast.makeText(this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading();

        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + Constants.alumniReportListUrl;

        Log.d(TAG, "Loading filter options from: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "Filter options response: " + response);
                    hideLoading();
                    parseFilterOptions(response);
                },
                error -> {
                    Log.e(TAG, "Error loading filter options", error);
                    hideLoading();
                    Toast.makeText(this, "Error loading filters", Toast.LENGTH_SHORT).show();
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

    private void parseFilterOptions(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.optInt("status", 0);

            if (status == 1) {
                JSONObject data = jsonObject.optJSONObject("data");
                if (data != null) {
                    // Parse classes
                    JSONArray classesArray = data.optJSONArray("classes");
                    if (classesArray != null) {
                        classesList.clear();
                        for (int i = 0; i < classesArray.length(); i++) {
                            JSONObject classObj = classesArray.getJSONObject(i);
                            String id = classObj.optString("id", "");
                            String name = classObj.optString("class", "");
                            classesList.add(new ClassData(id, name));
                        }
                        Log.d(TAG, "Loaded " + classesList.size() + " classes");
                    }

                    // Parse sessions
                    JSONArray sessionsArray = data.optJSONArray("sessions");
                    if (sessionsArray != null) {
                        sessionsList.clear();
                        for (int i = 0; i < sessionsArray.length(); i++) {
                            JSONObject sessionObj = sessionsArray.getJSONObject(i);
                            String id = sessionObj.optString("id", "");
                            String name = sessionObj.optString("session", "");
                            sessionsList.add(new SessionData(id, name));
                        }
                        Log.d(TAG, "Loaded " + sessionsList.size() + " sessions");
                    }

                    // Parse sections
                    JSONArray sectionsArray = data.optJSONArray("sections");
                    if (sectionsArray != null) {
                        sectionsList.clear();
                        for (int i = 0; i < sectionsArray.length(); i++) {
                            JSONObject sectionObj = sectionsArray.getJSONObject(i);
                            String id = sectionObj.optString("id", "");
                            String name = sectionObj.optString("section", "");
                            sectionsList.add(new SectionData(id, name));
                        }
                        Log.d(TAG, "Loaded " + sectionsList.size() + " sections");
                    }

                    // Parse categories
                    JSONArray categoriesArray = data.optJSONArray("categories");
                    if (categoriesArray != null) {
                        categoriesList.clear();
                        for (int i = 0; i < categoriesArray.length(); i++) {
                            JSONObject categoryObj = categoriesArray.getJSONObject(i);
                            String id = categoryObj.optString("id", "");
                            String name = categoryObj.optString("category", "");
                            categoriesList.add(new CategoryData(id, name));
                        }
                        Log.d(TAG, "Loaded " + categoriesList.size() + " categories");
                    }

                    // Setup spinners
                    setupSessionSpinner();
                    setupClassSpinner();
                    setupSectionSpinner();
                    setupCategorySpinner();
                }
            } else {
                String message = jsonObject.optString("message", "Failed to load filters");
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing filter options", e);
            Toast.makeText(this, "Error parsing filters", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupSessionSpinner() {
        List<String> sessionNames = new ArrayList<>();
        sessionNames.add("All Sessions");
        for (SessionData session : sessionsList) {
            sessionNames.add(session.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sessionNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sessionSpinner.setAdapter(adapter);
    }

    private void setupClassSpinner() {
        List<String> classNames = new ArrayList<>();
        classNames.add("All Classes");
        for (ClassData classData : classesList) {
            classNames.add(classData.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter);
    }

    private void setupSectionSpinner() {
        List<String> sectionNames = new ArrayList<>();
        sectionNames.add("All Sections");
        for (SectionData section : sectionsList) {
            sectionNames.add(section.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sectionNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectionSpinner.setAdapter(adapter);
    }

    private void setupCategorySpinner() {
        List<String> categoryNames = new ArrayList<>();
        categoryNames.add("All Categories");
        for (CategoryData category : categoriesList) {
            categoryNames.add(category.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    private void generateReport() {
        Log.d(TAG, "Generating report with filters:");
        Log.d(TAG, "Session ID: " + selectedSessionId);
        Log.d(TAG, "Class ID: " + selectedClassId);
        Log.d(TAG, "Section ID: " + selectedSectionId);
        Log.d(TAG, "Category ID: " + selectedCategoryId);

        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            Toast.makeText(this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading();

        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + Constants.alumniReportFilterUrl;

        Log.d(TAG, "Fetching alumni report from: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "Alumni report response: " + response);
                    hideLoading();
                    parseAlumniResponse(response);
                },
                error -> {
                    Log.e(TAG, "Error fetching alumni report", error);
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
                    if (!selectedSessionId.isEmpty()) {
                        jsonBody.put("session_id", Integer.parseInt(selectedSessionId));
                    }
                    if (!selectedClassId.isEmpty()) {
                        jsonBody.put("class_id", Integer.parseInt(selectedClassId));
                    }
                    if (!selectedSectionId.isEmpty()) {
                        jsonBody.put("section_id", Integer.parseInt(selectedSectionId));
                    }
                    if (!selectedCategoryId.isEmpty()) {
                        jsonBody.put("category_id", Integer.parseInt(selectedCategoryId));
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

    private void parseAlumniResponse(String response) {
        try {
            Log.d(TAG, "=== Parsing Alumni Response ===");
            Log.d(TAG, "Response length: " + response.length());
            
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.optInt("status", 0);
            String message = jsonObject.optString("message", "");
            
            Log.d(TAG, "API Status: " + status);
            Log.d(TAG, "API Message: " + message);

            if (status == 1) {
                JSONArray dataArray = jsonObject.optJSONArray("data");
                int totalRecords = jsonObject.optInt("total_records", 0);
                
                Log.d(TAG, "Total records from API: " + totalRecords);
                Log.d(TAG, "Data array length: " + (dataArray != null ? dataArray.length() : "null"));

                alumniList.clear();

                if (dataArray != null && dataArray.length() > 0) {
                    Log.d(TAG, "Processing " + dataArray.length() + " alumni records");
                    
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject alumniObj = dataArray.getJSONObject(i);
                        Log.d(TAG, "Processing alumni " + (i + 1) + ": " + alumniObj.optString("student_name", ""));

                        AlumniModel alumni = new AlumniModel();
                        
                        // Basic Information
                        alumni.setId(alumniObj.optString("id", ""));
                        alumni.setAdmissionNo(alumniObj.optString("admission_no", ""));
                        
                        // Student name - try multiple field variations
                        String studentName = alumniObj.optString("student_name", "");
                        if (studentName.isEmpty()) {
                            // Construct name from firstname, middlename, lastname
                            String firstName = alumniObj.optString("firstname", "");
                            String middleName = alumniObj.optString("middlename", "");
                            String lastName = alumniObj.optString("lastname", "");
                            
                            StringBuilder nameBuilder = new StringBuilder();
                            if (!firstName.isEmpty()) nameBuilder.append(firstName);
                            if (!middleName.isEmpty()) {
                                if (nameBuilder.length() > 0) nameBuilder.append(" ");
                                nameBuilder.append(middleName);
                            }
                            if (!lastName.isEmpty()) {
                                if (nameBuilder.length() > 0) nameBuilder.append(" ");
                                nameBuilder.append(lastName);
                            }
                            studentName = nameBuilder.toString();
                        }
                        alumni.setStudentName(studentName);
                        
                        // Class and Section
                        alumni.setClassSection(alumniObj.optString("class_section", ""));
                        alumni.setPassOutYear(alumniObj.optString("pass_out_year", ""));
                        
                        // Contact Information
                        alumni.setCurrentEmail(alumniObj.optString("current_email", ""));
                        alumni.setCurrentPhone(alumniObj.optString("current_phone", ""));
                        
                        // If current contact is empty, try regular contact fields
                        if (alumni.getCurrentEmail().isEmpty()) {
                            alumni.setCurrentEmail(alumniObj.optString("email", ""));
                        }
                        if (alumni.getCurrentPhone().isEmpty()) {
                            alumni.setCurrentPhone(alumniObj.optString("mobileno", ""));
                        }
                        
                        // Career Information
                        alumni.setOccupation(alumniObj.optString("occupation", ""));
                        alumni.setCurrentAddress(alumniObj.optString("current_address_alumni", ""));
                        
                        // If current address is empty, try other address fields
                        if (alumni.getCurrentAddress().isEmpty()) {
                            alumni.setCurrentAddress(alumniObj.optString("current_address", ""));
                            if (alumni.getCurrentAddress().isEmpty()) {
                                alumni.setCurrentAddress(alumniObj.optString("permanent_address", ""));
                            }
                        }
                        
                        // Guardian Information
                        alumni.setGuardianName(alumniObj.optString("guardian_name", ""));
                        alumni.setGuardianPhone(alumniObj.optString("guardian_phone", ""));
                        
                        // Personal Information
                        alumni.setDateOfBirth(alumniObj.optString("dob", ""));
                        alumni.setGender(alumniObj.optString("gender", ""));
                        alumni.setCategory(alumniObj.optString("category", ""));
                        alumni.setReligion(alumniObj.optString("religion", ""));
                        
                        // Optional fields that might not be in all responses
                        alumni.setBloodGroup(alumniObj.optString("blood_group", ""));
                        alumni.setCaste(alumniObj.optString("cast", ""));
                        alumni.setMotherTongue(alumniObj.optString("mother_tongue", ""));
                        alumni.setStudentImage(alumniObj.optString("image", ""));
                        
                        Log.d(TAG, "Parsed alumni: " + alumni.getStudentName() + 
                              " - " + alumni.getClassSection() + 
                              " - " + alumni.getPassOutYear());

                        alumniList.add(alumni);
                    }
                    
                    Log.d(TAG, "Successfully parsed " + alumniList.size() + " alumni records");

                    // Update UI
                    adapter.notifyDataSetChanged();
                    showData();

                    // Update summary with detailed information
                    JSONObject summary = jsonObject.optJSONObject("summary");
                    if (summary != null) {
                        int totalAlumni = summary.optInt("total_alumni", totalRecords);
                        int totalClasses = summary.optInt("total_classes", 0);
                        int totalSessions = summary.optInt("total_sessions", 0);
                        
                        Log.d(TAG, "Summary - Alumni: " + totalAlumni + ", Classes: " + totalClasses + ", Sessions: " + totalSessions);
                        
                        StringBuilder summaryText = new StringBuilder();
                        summaryText.append("Total Alumni: ").append(totalAlumni);
                        if (totalClasses > 0) {
                            summaryText.append("\nClasses: ").append(totalClasses);
                        }
                        if (totalSessions > 0) {
                            summaryText.append("\nSessions: ").append(totalSessions);
                        }
                        
                        totalRecordsTv.setText(summaryText.toString());
                    } else {
                        totalRecordsTv.setText("Total Alumni: " + totalRecords);
                    }

                    Toast.makeText(this, "Found " + totalRecords + " alumni records", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "No alumni data found");
                    showNoData();
                    Toast.makeText(this, "No alumni records found", Toast.LENGTH_SHORT).show();
                }
            } else {
                String errorMessage = jsonObject.optString("message", "Failed to load alumni report");
                Log.e(TAG, "API returned error status. Message: " + errorMessage);
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                showNoData();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing alumni response", e);
            Log.e(TAG, "Response that failed to parse: " + response);
            Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
            showNoData();
        }
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        alumniRecyclerView.setVisibility(View.GONE);
        nodataLayout.setVisibility(View.GONE);
        summaryCard.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void showData() {
        alumniRecyclerView.setVisibility(View.VISIBLE);
        summaryCard.setVisibility(View.VISIBLE);
        nodataLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void showNoData() {
        nodataLayout.setVisibility(View.VISIBLE);
        alumniRecyclerView.setVisibility(View.GONE);
        summaryCard.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_rightleft, R.anim.no_animation);
    }
}

