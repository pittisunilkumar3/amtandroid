package com.qdocs.ssre241123.teachers;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.FeesStatementAdapter;
import com.qdocs.ssre241123.adapters.SearchResultAdapter;
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
    private EditText searchEditText;
    private Button searchButton;
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
        setupSearchFunctionality();
        setupSpinners();
        setupGenerateButton();
        loadHierarchicalData();
    }

    private void initializeViews() {
        actionBar = findViewById(R.id.actionBar);
        backButton = findViewById(R.id.actionBarBackBtn);
        titleTextView = findViewById(R.id.actionBarTitleTV);
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
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
                if (searchButton != null) {
                    searchButton.setBackgroundColor(Color.parseColor(primaryColor));
                }
            } catch (Exception e) {
                Log.e(TAG, "Error parsing primary color", e);
            }
        }
    }

    private void setupSearchFunctionality() {
        if (searchButton != null) {
            searchButton.setOnClickListener(v -> performSearch());
        }

        // Optional: Search on Enter key
        if (searchEditText != null) {
            searchEditText.setOnEditorActionListener((v, actionId, event) -> {
                performSearch();
                return true;
            });
        }
    }

    private void performSearch() {
        String searchText = searchEditText.getText().toString().trim();

        if (searchText.isEmpty()) {
            Toast.makeText(this, "Please enter a name or admission number to search", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Performing search for: " + searchText);
        searchStudents(searchText);
    }

    private void searchStudents(String searchText) {
        Log.d(TAG, "Searching students with text: " + searchText);

        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + Constants.reportByNameFilterUrl;
        Log.d(TAG, "Search API URL: " + url);

        // Show search dialog
        showSearchDialog(searchText);
    }

    private void showSearchDialog(String searchText) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_search_results);
        dialog.setCancelable(true);

        // Get dialog views
        ImageView closeButton = dialog.findViewById(R.id.closeButton);
        TextView searchInfoTextView = dialog.findViewById(R.id.searchInfoTextView);
        ProgressBar searchProgressBar = dialog.findViewById(R.id.searchProgressBar);
        LinearLayout noResultsLayout = dialog.findViewById(R.id.noResultsLayout);
        RecyclerView searchResultsRecyclerView = dialog.findViewById(R.id.searchResultsRecyclerView);

        // Setup RecyclerView
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchResultsRecyclerView.setHasFixedSize(true);

        // Close button
        closeButton.setOnClickListener(v -> dialog.dismiss());

        // Show loading
        searchProgressBar.setVisibility(View.VISIBLE);
        noResultsLayout.setVisibility(View.GONE);
        searchResultsRecyclerView.setVisibility(View.GONE);
        searchInfoTextView.setText("Searching...");

        // Make API call
        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + Constants.reportByNameFilterUrl;

        Log.d(TAG, "Search Base URL: " + baseUrl);
        Log.d(TAG, "Search Full URL: " + url);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "Search response: " + response);
                    searchProgressBar.setVisibility(View.GONE);
                    parseSearchResults(response, searchInfoTextView, noResultsLayout,
                                     searchResultsRecyclerView, dialog);
                },
                error -> {
                    Log.e(TAG, "Search error", error);
                    searchProgressBar.setVisibility(View.GONE);
                    noResultsLayout.setVisibility(View.VISIBLE);
                    searchInfoTextView.setText("Error searching students");
                    Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("search_text", searchText);
                    String body = jsonBody.toString();
                    Log.d(TAG, "Search request body: " + body);
                    return body.getBytes();
                } catch (JSONException e) {
                    Log.e(TAG, "Error building search request", e);
                    return "{}".getBytes();
                }
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

        dialog.show();
    }

    private void parseSearchResults(String response, TextView searchInfoTextView,
                                   LinearLayout noResultsLayout,
                                   RecyclerView searchResultsRecyclerView,
                                   Dialog dialog) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            int status = jsonResponse.optInt("status", 0);

            if (status == 1) {
                JSONArray dataArray = jsonResponse.optJSONArray("data");
                int totalRecords = jsonResponse.optInt("total_records", 0);

                searchInfoTextView.setText("Found " + totalRecords + " student(s)");

                if (dataArray != null && dataArray.length() > 0) {
                    List<SearchResultAdapter.SearchResultItem> searchResults = new ArrayList<>();

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject studentObj = dataArray.getJSONObject(i);
                        SearchResultAdapter.SearchResultItem item = new SearchResultAdapter.SearchResultItem();

                        item.setStudentId(studentObj.optString("student_id"));
                        item.setAdmissionNo(studentObj.optString("admission_no"));
                        item.setFirstname(studentObj.optString("firstname"));
                        item.setMiddlename(studentObj.optString("middlename"));
                        item.setLastname(studentObj.optString("lastname"));
                        item.setFullName(studentObj.optString("full_name"));
                        item.setClassName(studentObj.optString("class"));
                        item.setSection(studentObj.optString("section"));
                        item.setRollNo(studentObj.optString("roll_no"));
                        item.setFatherName(studentObj.optString("father_name"));

                        // Parse fee amounts
                        item.setTotalFee(parseDouble(studentObj.optString("total_fee")));
                        item.setDeposit(parseDouble(studentObj.optString("deposit")));
                        item.setDiscount(parseDouble(studentObj.optString("discount")));
                        item.setFine(parseDouble(studentObj.optString("fine")));
                        item.setBalance(parseDouble(studentObj.optString("balance")));

                        searchResults.add(item);
                    }

                    // Setup adapter
                    SearchResultAdapter adapter = new SearchResultAdapter(this, searchResults,
                        selectedItem -> {
                            // Handle item selection
                            onSearchResultSelected(selectedItem);
                            dialog.dismiss();
                        });

                    searchResultsRecyclerView.setAdapter(adapter);
                    searchResultsRecyclerView.setVisibility(View.VISIBLE);
                    noResultsLayout.setVisibility(View.GONE);
                } else {
                    noResultsLayout.setVisibility(View.VISIBLE);
                    searchResultsRecyclerView.setVisibility(View.GONE);
                }
            } else {
                String message = jsonResponse.optString("message", "No results found");
                searchInfoTextView.setText(message);
                noResultsLayout.setVisibility(View.VISIBLE);
                searchResultsRecyclerView.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing search results", e);
            searchInfoTextView.setText("Error parsing results");
            noResultsLayout.setVisibility(View.VISIBLE);
            searchResultsRecyclerView.setVisibility(View.GONE);
        }
    }

    private double parseDouble(String value) {
        try {
            if (value != null && !value.isEmpty()) {
                return Double.parseDouble(value);
            }
        } catch (NumberFormatException e) {
            Log.e(TAG, "Error parsing double: " + value, e);
        }
        return 0.0;
    }

    private void onSearchResultSelected(SearchResultAdapter.SearchResultItem selectedItem) {
        Log.d(TAG, "Selected student: " + selectedItem.getFullName());

        // Store the selected student ID
        selectedStudentId = selectedItem.getStudentId();

        // Clear search text
        if (searchEditText != null) {
            searchEditText.setText("");
        }

        // Show a toast with selected student info
        Toast.makeText(this,
            "Selected: " + selectedItem.getFullName() + " (" + selectedItem.getAdmissionNo() + ")",
            Toast.LENGTH_LONG).show();

        // Automatically generate report for selected student
        showLoading();
        fetchReport();
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
        Log.d(TAG, "=== Loading hierarchical data from API ===");

        // Check network connectivity first
        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            hideLoading();
            Toast.makeText(this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            updateSessionSpinner(new ArrayList<>());
            return;
        }

        showLoading();

        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + Constants.feeCollectionFiltersGetHierarchyUrl;

        Log.d(TAG, "Base URL: " + baseUrl);
        Log.d(TAG, "Endpoint: " + Constants.feeCollectionFiltersGetHierarchyUrl);
        Log.d(TAG, "Full API URL: " + url);
        Log.d(TAG, "Request Method: POST");
        Log.d(TAG, "Request Body: {}");

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "=== API Response Received ===");
                        Log.d(TAG, "Response length: " + response.length() + " characters");
                        Log.d(TAG, "Response: " + response);
                        hideLoading();
                        parseHierarchicalData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "=== API Error Occurred ===");
                        Log.e(TAG, "Error loading hierarchical data", error);
                        hideLoading();

                        String errorMessage = "Error loading filters";
                        if (error != null) {
                            if (error.networkResponse != null) {
                                int statusCode = error.networkResponse.statusCode;
                                errorMessage += ": HTTP " + statusCode;
                                Log.e(TAG, "HTTP Status Code: " + statusCode);

                                try {
                                    String responseBody = new String(error.networkResponse.data, "UTF-8");
                                    Log.e(TAG, "Error response body: " + responseBody);
                                    errorMessage += " - " + responseBody;
                                } catch (Exception e) {
                                    Log.e(TAG, "Error reading response body", e);
                                }
                            } else if (error.getMessage() != null) {
                                errorMessage += ": " + error.getMessage();
                                Log.e(TAG, "Error message: " + error.getMessage());
                            } else {
                                errorMessage += ": Network error or timeout";
                                Log.e(TAG, "Network error or timeout - no response received");
                            }
                        }

                        Toast.makeText(FeesStatementActivity.this, errorMessage, Toast.LENGTH_LONG).show();

                        // Initialize empty spinners so UI is still usable
                        updateSessionSpinner(new ArrayList<>());
                    }
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
                Log.d(TAG, "  Content-Type: " + Constants.contentType);

                return headers;
            }

            @Override
            public byte[] getBody() {
                // Send empty JSON body as per API documentation
                String body = "{}";
                Log.d(TAG, "Request body: " + body);
                return body.getBytes();
            }
        };

        // Set timeout to 30 seconds
        request.setRetryPolicy(new DefaultRetryPolicy(
            30000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
        Log.d(TAG, "Request added to queue");
    }

    private void parseHierarchicalData(String response) {
        try {
            Log.d(TAG, "Parsing response: " + response);
            JSONObject jsonResponse = new JSONObject(response);
            int status = jsonResponse.optInt("status", 0);

            if (status == 1) {
                // According to API documentation, data is a direct array of sessions
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
                                ClassData classData = new ClassData();
                                classData.id = classObj.optString("id");
                                classData.name = classObj.optString("name");
                                classData.sections = new ArrayList<>();

                                // Parse sections
                                JSONArray sectionsArray = classObj.optJSONArray("sections");
                                if (sectionsArray != null) {
                                    Log.d(TAG, "Found " + sectionsArray.length() + " sections in class " + classData.name);

                                    for (int k = 0; k < sectionsArray.length(); k++) {
                                        JSONObject sectionObj = sectionsArray.getJSONObject(k);
                                        SectionData section = new SectionData();
                                        section.id = sectionObj.optString("id");
                                        section.name = sectionObj.optString("name");
                                        section.students = new ArrayList<>();

                                        // Parse students
                                        JSONArray studentsArray = sectionObj.optJSONArray("students");
                                        if (studentsArray != null) {
                                            Log.d(TAG, "Found " + studentsArray.length() + " students in section " + section.name);

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

                    Log.d(TAG, "Successfully parsed " + sessionsList.size() + " sessions");
                    setupSessionSpinner();
                } else {
                    Log.e(TAG, "No sessions data found in response or data array is empty");
                    Log.e(TAG, "Response keys: " + jsonResponse.keys().toString());
                    Toast.makeText(this, "No sessions available", Toast.LENGTH_SHORT).show();
                    // Initialize empty spinner so UI is still usable
                    updateSessionSpinner(new ArrayList<>());
                }
            } else {
                String message = jsonResponse.optString("message", "Failed to load data");
                Log.e(TAG, "API returned error status: " + status + ", message: " + message);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                // Initialize empty spinner
                updateSessionSpinner(new ArrayList<>());
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing hierarchical data", e);
            Log.e(TAG, "Response was: " + response);
            Toast.makeText(this, "Error parsing data: " + e.getMessage(), Toast.LENGTH_LONG).show();
            // Initialize empty spinner
            updateSessionSpinner(new ArrayList<>());
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

    private void updateSessionSpinner(List<SessionData> sessions) {
        sessionsList.clear();
        sessionsList.addAll(sessions);

        List<String> sessionNames = new ArrayList<>();
        sessionNames.add("Select Session");
        for (SessionData session : sessions) {
            sessionNames.add(session.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sessionNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sessionSpinner.setAdapter(adapter);

        // Reset dependent spinners
        currentClassesList.clear();
        updateClassSpinner(new ArrayList<>());
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
        Log.d(TAG, "=== Fetching Fees Statement Report ===");

        // Check network connectivity first
        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            hideLoading();
            Toast.makeText(this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            showNoData();
            return;
        }

        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + Constants.reportByNameFilterUrl;

        Log.d(TAG, "Base URL: " + baseUrl);
        Log.d(TAG, "Endpoint: " + Constants.reportByNameFilterUrl);
        Log.d(TAG, "Full API URL: " + url);
        Log.d(TAG, "Request Method: POST");

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "=== Report Response Received ===");
                        Log.d(TAG, "Response length: " + response.length() + " characters");
                        Log.d(TAG, "Response: " + response);
                        hideLoading();
                        parseReportResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "=== Report Fetch Error ===");
                        Log.e(TAG, "Error fetching report", error);
                        hideLoading();

                        String errorMessage = "Error fetching report";
                        if (error != null) {
                            if (error instanceof com.android.volley.TimeoutError) {
                                errorMessage = "Request timeout. The report is taking too long to generate. Please try again.";
                                Log.e(TAG, "Timeout error - request took too long");
                            } else if (error.networkResponse != null) {
                                int statusCode = error.networkResponse.statusCode;
                                errorMessage += ": HTTP " + statusCode;
                                Log.e(TAG, "HTTP Status Code: " + statusCode);

                                try {
                                    String responseBody = new String(error.networkResponse.data, "UTF-8");
                                    Log.e(TAG, "Error response body: " + responseBody);
                                } catch (Exception e) {
                                    Log.e(TAG, "Error reading response body", e);
                                }
                            } else if (error.getMessage() != null) {
                                errorMessage += ": " + error.getMessage();
                                Log.e(TAG, "Error message: " + error.getMessage());
                            } else {
                                errorMessage += ": Network error";
                                Log.e(TAG, "Network error - no response received");
                            }
                        }

                        Toast.makeText(FeesStatementActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        showNoData();
                    }
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
                Log.d(TAG, "  Content-Type: " + Constants.contentType);

                return headers;
            }

            @Override
            public byte[] getBody() {
                String body = buildRequestBody();
                Log.d(TAG, "Request body: " + body);
                return body.getBytes();
            }
        };

        // Set timeout to 60 seconds for report generation (reports can take longer)
        request.setRetryPolicy(new DefaultRetryPolicy(
            60000,  // 60 seconds timeout
            0,      // No retries for reports (to avoid duplicate processing)
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        Log.d(TAG, "Request timeout set to 60 seconds");
        Log.d(TAG, "Adding request to queue...");

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
            Log.d(TAG, "=== Parsing Report Response ===");
            JSONObject jsonResponse = new JSONObject(response);
            int status = jsonResponse.optInt("status", 0);

            if (status == 1) {
                JSONArray dataArray = jsonResponse.optJSONArray("data");

                if (dataArray != null && dataArray.length() > 0) {
                    JSONObject studentData = dataArray.getJSONObject(0);

                    // Parse student header
                    FeesStatementAdapter.StudentHeader header = new FeesStatementAdapter.StudentHeader();
                    header.studentName = studentData.optString("firstname", "") + " " +
                                       studentData.optString("middlename", "") + " " +
                                       studentData.optString("lastname", "");
                    header.studentName = header.studentName.trim().replaceAll("\\s+", " ");
                    header.admissionNo = studentData.optString("admission_no", "N/A");
                    header.className = studentData.optString("class", "N/A");
                    header.section = studentData.optString("section", "N/A");
                    header.rollNo = studentData.optString("roll_no", "N/A");
                    header.fatherName = studentData.optString("father_name", "N/A");

                    Log.d(TAG, "Student: " + header.studentName);

                    // Parse fee groups
                    List<FeesStatementAdapter.FeeGroup> feeGroups = new ArrayList<>();
                    JSONArray feesArray = studentData.optJSONArray("fees");

                    double totalFee = 0;
                    double totalPaid = 0;
                    double totalDiscount = 0;
                    double totalFine = 0;

                    if (feesArray != null) {
                        Log.d(TAG, "Found " + feesArray.length() + " fee groups");

                        for (int i = 0; i < feesArray.length(); i++) {
                            JSONArray feeGroupArray = feesArray.getJSONArray(i);

                            if (feeGroupArray.length() > 0) {
                                JSONObject firstFee = feeGroupArray.getJSONObject(0);
                                String groupName = firstFee.optString("name", "Fee Group " + (i + 1));

                                FeesStatementAdapter.FeeGroup feeGroup = new FeesStatementAdapter.FeeGroup();
                                feeGroup.groupName = groupName;
                                feeGroup.feeTypes = new ArrayList<>();

                                // Parse fee types in this group
                                for (int j = 0; j < feeGroupArray.length(); j++) {
                                    JSONObject feeTypeObj = feeGroupArray.getJSONObject(j);

                                    FeesStatementAdapter.FeeType feeType = new FeesStatementAdapter.FeeType();
                                    feeType.typeName = feeTypeObj.optString("type", "Fee Type");
                                    feeType.amount = parseDouble(feeTypeObj.optString("amount", "0"));
                                    feeType.fine = parseDouble(feeTypeObj.optString("fine_amount", "0"));
                                    feeType.dueDate = feeTypeObj.optString("due_date", "");

                                    // Parse payment details from amount_detail
                                    String amountDetail = feeTypeObj.optString("amount_detail", "0");
                                    double paidAmount = 0;
                                    double discountAmount = 0;
                                    boolean hasPayments = false;

                                    if (!amountDetail.equals("0") && !amountDetail.isEmpty()) {
                                        try {
                                            JSONObject amountDetailObj = new JSONObject(amountDetail);
                                            hasPayments = true;

                                            // Iterate through payment records
                                            for (int k = 1; k <= 100; k++) {
                                                if (amountDetailObj.has(String.valueOf(k))) {
                                                    JSONObject payment = amountDetailObj.getJSONObject(String.valueOf(k));
                                                    paidAmount += payment.optDouble("amount", 0);
                                                    discountAmount += payment.optDouble("amount_discount", 0);
                                                }
                                            }
                                        } catch (JSONException e) {
                                            Log.e(TAG, "Error parsing amount_detail", e);
                                        }
                                    }

                                    feeType.paidAmount = paidAmount;
                                    feeType.discount = discountAmount;
                                    // Balance = Amount - Paid - Discount + Fine
                                    feeType.balance = feeType.amount - paidAmount - discountAmount + feeType.fine;
                                    feeType.hasPayments = hasPayments;

                                    // Add to totals
                                    totalFee += feeType.amount;
                                    totalPaid += paidAmount;
                                    totalDiscount += discountAmount;
                                    totalFine += feeType.fine;

                                    feeGroup.feeTypes.add(feeType);

                                    Log.d(TAG, "  Fee Type: " + feeType.typeName +
                                             ", Amount: " + feeType.amount +
                                             ", Paid: " + paidAmount +
                                             ", Discount: " + discountAmount +
                                             ", Balance: " + feeType.balance);
                                }

                                feeGroups.add(feeGroup);
                            }
                        }
                    }

                    // Create summary
                    FeesStatementAdapter.FeeSummary summary = new FeesStatementAdapter.FeeSummary();
                    summary.totalFee = totalFee;
                    summary.totalPaid = totalPaid;
                    summary.totalDiscount = totalDiscount;
                    summary.totalFine = totalFine;
                    // Total Balance = Total Fee - Total Paid - Total Discount + Total Fine
                    summary.totalBalance = totalFee - totalPaid - totalDiscount + totalFine;

                    Log.d(TAG, "Summary - Total Fee: " + totalFee +
                             ", Paid: " + totalPaid +
                             ", Discount: " + totalDiscount +
                             ", Fine: " + totalFine +
                             ", Balance: " + summary.totalBalance);

                    // Display data
                    FeesStatementAdapter adapter = new FeesStatementAdapter(this);
                    adapter.setData(header, feeGroups, summary);
                    reportContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                    reportContentRecyclerView.setAdapter(adapter);

                    showReportContent();
                    Toast.makeText(this, "Report generated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "No data found in response");
                    Toast.makeText(this, "No fee data available for this student", Toast.LENGTH_SHORT).show();
                    showNoData();
                }
            } else {
                String message = jsonResponse.optString("message", "Failed to generate report");
                Log.e(TAG, "API returned error: " + message);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                showNoData();
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing report response", e);
            Log.e(TAG, "Response was: " + response);
            Toast.makeText(this, "Error parsing report: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

