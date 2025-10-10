package com.qdocs.ssre241123.teachers;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.TypeWiseBalanceReportAdapter;
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

/**
 * Activity for Type Wise Balance Report
 * Shows fee balance grouped by fee types with filters:
 * - Session, Class, Section, Fee Group, Fee Type
 */
public class TypeWiseBalanceReportActivity extends AppCompatActivity {

    private static final String TAG = "TypeWiseBalanceReport";

    // UI Components
    private FrameLayout actionBar;
    private TextView titleTextView;
    private Spinner sessionSpinner, classSpinner, sectionSpinner, feeGroupSpinner, feeTypeSpinner;
    private Button generateReportButton;
    private RecyclerView reportContentRecyclerView;
    private ProgressBar progressBar;
    private LinearLayout nodataLayout;
    private CardView filtersCard;

    // Data Lists
    private List<SessionData> sessionsList = new ArrayList<>();
    private List<ClassData> classesList = new ArrayList<>();
    private List<SectionData> sectionsList = new ArrayList<>();
    private List<FeeGroupData> feeGroupsList = new ArrayList<>();
    private List<FeeTypeData> feeTypesList = new ArrayList<>();
    private List<TypeWiseBalanceReportData> reportDataList = new ArrayList<>();

    // Selected Filter IDs
    private String selectedSessionId = null;
    private String selectedClassId = null;
    private String selectedSectionId = null;
    private String selectedFeeGroupId = null;
    private String selectedFeeTypeId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_wise_balance_report);

        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), "langCode"));

        initializeViews();
        setupListeners();
        loadFilterOptions();
    }

    private void initializeViews() {
        actionBar = findViewById(R.id.actionBar);
        titleTextView = findViewById(R.id.title);
        sessionSpinner = findViewById(R.id.session_spinner);
        classSpinner = findViewById(R.id.class_spinner);
        sectionSpinner = findViewById(R.id.section_spinner);
        feeGroupSpinner = findViewById(R.id.fee_group_spinner);
        feeTypeSpinner = findViewById(R.id.fee_type_spinner);
        generateReportButton = findViewById(R.id.generate_report_button);
        reportContentRecyclerView = findViewById(R.id.report_content_recyclerView);
        progressBar = findViewById(R.id.progressBar);
        nodataLayout = findViewById(R.id.nodata_layout);
        filtersCard = findViewById(R.id.filters_card);

        titleTextView.setText(R.string.type_wise_balance_report);

        // Apply theme color
        String themeColor = Utility.getSharedPreferences(getApplicationContext(), "primaryColour");
        if (themeColor != null && !themeColor.isEmpty()) {
            try {
                actionBar.setBackgroundColor(android.graphics.Color.parseColor(themeColor));
                generateReportButton.setBackgroundColor(android.graphics.Color.parseColor(themeColor));
            } catch (Exception e) {
                Log.e(TAG, "Error parsing theme color", e);
            }
        }

        // Setup RecyclerView
        reportContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.back_button).setOnClickListener(v -> finish());
    }

    private void setupListeners() {
        generateReportButton.setOnClickListener(v -> generateReport());

        sessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && sessionsList.size() > position - 1) {
                    selectedSessionId = sessionsList.get(position - 1).id;
                    // Load sections when session or class changes
                    loadSectionsForSelectedFilters();
                } else {
                    selectedSessionId = null;
                    // Clear sections when session is deselected
                    sectionsList.clear();
                    setupSectionSpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSessionId = null;
                sectionsList.clear();
                setupSectionSpinner();
            }
        });

        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && classesList.size() > position - 1) {
                    selectedClassId = classesList.get(position - 1).id;
                    // Load sections when session or class changes
                    loadSectionsForSelectedFilters();
                } else {
                    selectedClassId = null;
                    // Clear sections when class is deselected
                    sectionsList.clear();
                    setupSectionSpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedClassId = null;
                sectionsList.clear();
                setupSectionSpinner();
            }
        });

        sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && sectionsList.size() > position - 1) {
                    selectedSectionId = sectionsList.get(position - 1).id;
                } else {
                    selectedSectionId = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSectionId = null;
            }
        });

        feeGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && feeGroupsList.size() > position - 1) {
                    selectedFeeGroupId = feeGroupsList.get(position - 1).id;
                } else {
                    selectedFeeGroupId = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedFeeGroupId = null;
            }
        });

        feeTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && feeTypesList.size() > position - 1) {
                    selectedFeeTypeId = feeTypesList.get(position - 1).id;
                } else {
                    selectedFeeTypeId = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedFeeTypeId = null;
            }
        });
    }

    private void generateReport() {
        // All filters are optional
        showLoading();
        fetchTypeWiseBalanceReport();
    }

    private void loadFilterOptions() {
        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            Toast.makeText(this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading();

        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + Constants.sessionFeeStructureListUrl;

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
                    Toast.makeText(this, "Error loading filters: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                    return jsonBody.toString().getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "Error creating request body", e);
                    return null;
                }
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
                // Parse sessions
                JSONArray sessionsArray = jsonObject.optJSONArray("sessions");
                if (sessionsArray != null) {
                    sessionsList.clear();
                    for (int i = 0; i < sessionsArray.length(); i++) {
                        JSONObject sessionObj = sessionsArray.getJSONObject(i);
                        SessionData session = new SessionData();
                        session.id = sessionObj.optString("id");
                        session.name = sessionObj.optString("session");
                        sessionsList.add(session);
                    }
                }

                // Parse classes
                JSONArray classesArray = jsonObject.optJSONArray("classes");
                if (classesArray != null) {
                    classesList.clear();
                    for (int i = 0; i < classesArray.length(); i++) {
                        JSONObject classObj = classesArray.getJSONObject(i);
                        ClassData classData = new ClassData();
                        classData.id = classObj.optString("id");
                        classData.name = classObj.optString("class");
                        classesList.add(classData);
                    }
                }

                // Parse fee groups
                JSONArray feeGroupsArray = jsonObject.optJSONArray("fee_groups");
                if (feeGroupsArray != null) {
                    feeGroupsList.clear();
                    for (int i = 0; i < feeGroupsArray.length(); i++) {
                        JSONObject feeGroupObj = feeGroupsArray.getJSONObject(i);
                        FeeGroupData feeGroup = new FeeGroupData();
                        feeGroup.id = feeGroupObj.optString("id");
                        feeGroup.name = feeGroupObj.optString("name");
                        feeGroupsList.add(feeGroup);
                    }
                }

                // Parse fee types
                JSONArray feeTypesArray = jsonObject.optJSONArray("fee_types");
                if (feeTypesArray != null) {
                    feeTypesList.clear();
                    for (int i = 0; i < feeTypesArray.length(); i++) {
                        JSONObject feeTypeObj = feeTypesArray.getJSONObject(i);
                        FeeTypeData feeType = new FeeTypeData();
                        feeType.id = feeTypeObj.optString("id");
                        feeType.name = feeTypeObj.optString("type");
                        feeType.code = feeTypeObj.optString("code");
                        feeTypesList.add(feeType);
                    }
                }

                // Setup spinners
                setupSessionSpinner();
                setupClassSpinner();
                setupSectionSpinner();
                setupFeeGroupSpinner();
                setupFeeTypeSpinner();

                Toast.makeText(this, "Filters loaded successfully", Toast.LENGTH_SHORT).show();
            } else {
                String message = jsonObject.optString("message", "Failed to load filters");
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing filter options", e);
            Toast.makeText(this, "Error parsing filters", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupSessionSpinner() {
        List<String> sessionNames = new ArrayList<>();
        sessionNames.add("Select Session (Optional)");
        for (SessionData session : sessionsList) {
            sessionNames.add(session.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sessionNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sessionSpinner.setAdapter(adapter);
    }

    private void setupClassSpinner() {
        List<String> classNames = new ArrayList<>();
        classNames.add("Select Class (Optional)");
        for (ClassData classData : classesList) {
            classNames.add(classData.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter);
    }

    private void setupSectionSpinner() {
        List<String> sectionNames = new ArrayList<>();
        sectionNames.add("Select Section (Optional)");
        for (SectionData section : sectionsList) {
            sectionNames.add(section.name);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sectionNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectionSpinner.setAdapter(adapter);
    }

    /**
     * Load sections based on selected session and class filters
     * Uses the sessions-with-classes-sections API to get hierarchical data
     */
    private void loadSectionsForSelectedFilters() {
        // Only load sections if both session and class are selected
        if (selectedSessionId == null || selectedClassId == null) {
            sectionsList.clear();
            setupSectionSpinner();
            return;
        }

        Log.d(TAG, "Loading sections for Session: " + selectedSessionId + ", Class: " + selectedClassId);

        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + Constants.teacherSessionsWithClassesSectionsUrl;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "Sections API response: " + response);
                    parseSectionsFromResponse(response);
                },
                error -> {
                    Log.e(TAG, "Error loading sections", error);
                    Toast.makeText(this, "Error loading sections", Toast.LENGTH_SHORT).show();
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
                    return jsonBody.toString().getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "Error creating request body", e);
                    return null;
                }
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /**
     * Parse sections from the sessions-with-classes-sections API response
     * Filters sections based on selected session and class
     */
    private void parseSectionsFromResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.optInt("status", 0);

            if (status == 1) {
                sectionsList.clear();

                JSONArray sessionsArray = jsonObject.optJSONArray("data");
                if (sessionsArray != null) {
                    // Find the selected session
                    for (int i = 0; i < sessionsArray.length(); i++) {
                        JSONObject sessionObj = sessionsArray.getJSONObject(i);
                        String sessionId = sessionObj.optString("session_id");

                        // Check if this is the selected session
                        if (sessionId.equals(selectedSessionId)) {
                            JSONArray classesArray = sessionObj.optJSONArray("classes");
                            if (classesArray != null) {
                                // Find the selected class
                                for (int j = 0; j < classesArray.length(); j++) {
                                    JSONObject classObj = classesArray.getJSONObject(j);
                                    String classId = classObj.optString("class_id");

                                    // Check if this is the selected class
                                    if (classId.equals(selectedClassId)) {
                                        JSONArray sectionsArray = classObj.optJSONArray("sections");
                                        if (sectionsArray != null) {
                                            // Parse all sections for this class
                                            for (int k = 0; k < sectionsArray.length(); k++) {
                                                JSONObject sectionObj = sectionsArray.getJSONObject(k);
                                                SectionData section = new SectionData();
                                                section.id = sectionObj.optString("section_id");
                                                section.name = sectionObj.optString("section_name");
                                                sectionsList.add(section);
                                            }
                                        }
                                        break; // Found the class, no need to continue
                                    }
                                }
                            }
                            break; // Found the session, no need to continue
                        }
                    }
                }

                // Update the section spinner
                setupSectionSpinner();

                if (sectionsList.isEmpty()) {
                    Log.d(TAG, "No sections found for selected session and class");
                } else {
                    Log.d(TAG, "Loaded " + sectionsList.size() + " sections");
                }
            } else {
                String message = jsonObject.optString("message", "Failed to load sections");
                Log.e(TAG, "API error: " + message);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing sections response", e);
        }
    }

    private void setupFeeGroupSpinner() {
        List<String> feeGroupNames = new ArrayList<>();
        feeGroupNames.add("Select Fee Group (Optional)");
        for (FeeGroupData feeGroup : feeGroupsList) {
            feeGroupNames.add(feeGroup.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, feeGroupNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feeGroupSpinner.setAdapter(adapter);
    }

    private void setupFeeTypeSpinner() {
        List<String> feeTypeNames = new ArrayList<>();
        feeTypeNames.add("Select Fee Type (Optional)");
        for (FeeTypeData feeType : feeTypesList) {
            String displayName = feeType.name;
            if (feeType.code != null && !feeType.code.isEmpty()) {
                displayName += " (" + feeType.code + ")";
            }
            feeTypeNames.add(displayName);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, feeTypeNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feeTypeSpinner.setAdapter(adapter);
    }

    private void fetchTypeWiseBalanceReport() {
        // Validate required field: session_id
        if (selectedSessionId == null || selectedSessionId.isEmpty()) {
            Toast.makeText(this, "Please select a Session", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            hideLoading();
            showNoData();
            Toast.makeText(this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + Constants.typeWiseBalanceReportFilterUrl;

        Log.d(TAG, "Fetching report from: " + url);
        Log.d(TAG, "Filters - Session: " + selectedSessionId + ", Class: " + selectedClassId +
                   ", Section: " + selectedSectionId + ", FeeGroup: " + selectedFeeGroupId +
                   ", FeeType: " + selectedFeeTypeId);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "Report response: " + response);
                    hideLoading();
                    parseReportResponse(response);
                },
                error -> {
                    Log.e(TAG, "Error fetching report", error);
                    hideLoading();
                    showNoData();
                    String errorMsg = "Error loading report";
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        try {
                            String errorResponse = new String(error.networkResponse.data, "UTF-8");
                            Log.e(TAG, "Error response: " + errorResponse);
                            JSONObject errorJson = new JSONObject(errorResponse);
                            errorMsg = errorJson.optString("message", errorMsg);
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing error response", e);
                        }
                    }
                    Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
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

                    // session_id is REQUIRED
                    jsonBody.put("session_id", selectedSessionId);

                    // feetype_ids - array (empty array returns all fee types)
                    JSONArray feetypeIds = new JSONArray();
                    if (selectedFeeTypeId != null && !selectedFeeTypeId.isEmpty()) {
                        feetypeIds.put(selectedFeeTypeId);
                    }
                    jsonBody.put("feetype_ids", feetypeIds);

                    // feegroup_ids - array (optional)
                    if (selectedFeeGroupId != null && !selectedFeeGroupId.isEmpty()) {
                        JSONArray feegroupIds = new JSONArray();
                        feegroupIds.put(selectedFeeGroupId);
                        jsonBody.put("feegroup_ids", feegroupIds);
                    }

                    // class_id - optional
                    if (selectedClassId != null && !selectedClassId.isEmpty()) {
                        jsonBody.put("class_id", selectedClassId);
                    }

                    // section_id - optional
                    if (selectedSectionId != null && !selectedSectionId.isEmpty()) {
                        jsonBody.put("section_id", selectedSectionId);
                    }

                    String requestBody = jsonBody.toString();
                    Log.d(TAG, "Request body: " + requestBody);
                    return requestBody.getBytes("UTF-8");
                } catch (JSONException | UnsupportedEncodingException e) {
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
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.optInt("status", 0);
            String message = jsonObject.optString("message", "");

            if (status == 1) {
                reportDataList.clear();

                JSONArray dataArray = jsonObject.optJSONArray("data");
                int totalRecords = jsonObject.optInt("total_records", 0);

                Log.d(TAG, "Total records: " + totalRecords);

                if (dataArray != null && dataArray.length() > 0) {
                    // Parse each record
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject recordJson = dataArray.getJSONObject(i);
                        TypeWiseBalanceReportData reportData = new TypeWiseBalanceReportData(recordJson);
                        reportDataList.add(reportData);
                    }

                    Log.d(TAG, "Parsed " + reportDataList.size() + " records");

                    // Display the data
                    displayReportData();
                    showContent();

                    // Show summary
                    String summaryMsg = "Report generated: " + reportDataList.size() + " records";
                    Toast.makeText(this, summaryMsg, Toast.LENGTH_SHORT).show();
                } else {
                    showNoData();
                    Toast.makeText(this, "No data found for selected filters", Toast.LENGTH_SHORT).show();
                }
            } else {
                showNoData();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing report response", e);
            showNoData();
            Toast.makeText(this, "Error parsing report data", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Display report data in RecyclerView
     */
    private void displayReportData() {
        Log.d(TAG, "Displaying " + reportDataList.size() + " records");

        // Calculate totals
        double totalBalance = 0;
        double totalAmount = 0;

        for (TypeWiseBalanceReportData data : reportDataList) {
            try {
                totalBalance += Double.parseDouble(data.balance);
                totalAmount += Double.parseDouble(data.total);
            } catch (NumberFormatException e) {
                Log.e(TAG, "Error parsing amounts", e);
            }
        }

        Log.d(TAG, "Total Amount: " + totalAmount);
        Log.d(TAG, "Total Balance: " + totalBalance);

        // Create and set adapter
        TypeWiseBalanceReportAdapter adapter = new TypeWiseBalanceReportAdapter(this, reportDataList);
        reportContentRecyclerView.setAdapter(adapter);
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        nodataLayout.setVisibility(View.GONE);
        reportContentRecyclerView.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void showContent() {
        progressBar.setVisibility(View.GONE);
        nodataLayout.setVisibility(View.GONE);
        reportContentRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showNoData() {
        progressBar.setVisibility(View.GONE);
        nodataLayout.setVisibility(View.VISIBLE);
        reportContentRecyclerView.setVisibility(View.GONE);
    }

    // Data classes
    private static class SessionData {
        String id;
        String name;
    }

    private static class ClassData {
        String id;
        String name;
    }

    private static class SectionData {
        String id;
        String name;
    }

    private static class FeeGroupData {
        String id;
        String name;
    }

    private static class FeeTypeData {
        String id;
        String name;
        String code;
    }

    /**
     * Model class for Type Wise Balance Report data
     */
    public static class TypeWiseBalanceReportData {
        public String admissionNo;
        public String studentName;
        public String className;
        public String sectionName;
        public String feeType;
        public String feeGroupName;
        public String mobileNo;
        public String total;
        public String fine;
        public int totalAmount;
        public int totalFine;
        public int totalDiscount;
        public String balance;

        // Constructor to parse from JSON
        public TypeWiseBalanceReportData(JSONObject json) {
            this.admissionNo = json.optString("admission_no", "");

            // Construct full name
            String firstName = json.optString("firstname", "");
            String middleName = json.optString("middlename", "");
            String lastName = json.optString("lastname", "");
            this.studentName = (firstName + " " + (middleName != null && !middleName.equals("null") ? middleName + " " : "") + lastName).trim();

            this.className = json.optString("class", "");
            this.sectionName = json.optString("section", "");
            this.feeType = json.optString("type", "");
            this.feeGroupName = json.optString("feegroupname", "");
            this.mobileNo = json.optString("mobileno", "");
            this.total = json.optString("total", "0.00");
            this.fine = json.optString("fine", "0.00");
            this.totalAmount = json.optInt("total_amount", 0);
            this.totalFine = json.optInt("total_fine", 0);
            this.totalDiscount = json.optInt("total_discount", 0);

            // Balance can be string or integer
            if (json.has("balance")) {
                Object balanceObj = json.opt("balance");
                if (balanceObj instanceof String) {
                    this.balance = (String) balanceObj;
                } else if (balanceObj instanceof Integer) {
                    this.balance = String.valueOf(balanceObj);
                } else if (balanceObj instanceof Double) {
                    this.balance = String.format("%.2f", (Double) balanceObj);
                } else {
                    // Calculate balance if not provided
                    double calculatedBalance = Double.parseDouble(this.total) - this.totalAmount + this.totalFine - this.totalDiscount;
                    this.balance = String.format("%.2f", calculatedBalance);
                }
            } else {
                // Calculate balance
                double calculatedBalance = Double.parseDouble(this.total) - this.totalAmount + this.totalFine - this.totalDiscount;
                this.balance = String.format("%.2f", calculatedBalance);
            }
        }
    }
}

