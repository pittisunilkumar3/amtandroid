package com.qdocs.ssre241123.teachers;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Activity for Fee Collection Report Column Wise
 * Shows fee collection data with filters:
 * - Date Range (From Date - To Date)
 * - Session, Class, Section, Fee Type
 */
public class FeeCollectionReportColumnWiseActivity extends AppCompatActivity {

    private static final String TAG = "FeeCollectionColumnWise";

    // UI Components
    private FrameLayout actionBar;
    private TextView titleTextView;
    private EditText fromDateEditText, toDateEditText;
    private Spinner sessionSpinner, classSpinner, sectionSpinner, feeTypeSpinner;
    private Button generateReportButton;
    private RecyclerView reportContentRecyclerView;
    private ProgressBar progressBar;
    private LinearLayout nodataLayout;
    private CardView filtersCard;

    // Data Lists
    private List<SessionData> sessionsList = new ArrayList<>();
    private List<ClassData> classesList = new ArrayList<>();
    private List<SectionData> sectionsList = new ArrayList<>();
    private List<FeeTypeData> feeTypesList = new ArrayList<>();

    // Selected Filter Values
    private String selectedFromDate = null;
    private String selectedToDate = null;
    private String selectedSessionId = null;
    private String selectedClassId = null;
    private String selectedSectionId = null;
    private String selectedFeeTypeId = null;

    // Date Format
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_collection_report_column_wise);

        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), "langCode"));

        initializeViews();
        setupListeners();
        loadFilterOptions();
    }

    private void initializeViews() {
        actionBar = findViewById(R.id.actionBar);
        titleTextView = findViewById(R.id.title);
        fromDateEditText = findViewById(R.id.from_date_edit_text);
        toDateEditText = findViewById(R.id.to_date_edit_text);
        sessionSpinner = findViewById(R.id.session_spinner);
        classSpinner = findViewById(R.id.class_spinner);
        sectionSpinner = findViewById(R.id.section_spinner);
        feeTypeSpinner = findViewById(R.id.fee_type_spinner);
        generateReportButton = findViewById(R.id.generate_report_button);
        reportContentRecyclerView = findViewById(R.id.report_content_recyclerView);
        progressBar = findViewById(R.id.progressBar);
        nodataLayout = findViewById(R.id.nodata_layout);
        filtersCard = findViewById(R.id.filters_card);

        titleTextView.setText(R.string.fee_collection_report_column_wise);

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

        // Make date fields non-editable (click to open date picker)
        fromDateEditText.setFocusable(false);
        fromDateEditText.setClickable(true);
        toDateEditText.setFocusable(false);
        toDateEditText.setClickable(true);

        findViewById(R.id.back_button).setOnClickListener(v -> finish());
    }

    private void setupListeners() {
        generateReportButton.setOnClickListener(v -> generateReport());

        // Date pickers
        fromDateEditText.setOnClickListener(v -> showDatePicker(true));
        toDateEditText.setOnClickListener(v -> showDatePicker(false));

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

    private void showDatePicker(boolean isFromDate) {
        Calendar calendar = Calendar.getInstance();
        
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    String formattedDate = dateFormat.format(calendar.getTime());
                    String displayDate = displayDateFormat.format(calendar.getTime());
                    
                    if (isFromDate) {
                        selectedFromDate = formattedDate;
                        fromDateEditText.setText(displayDate);
                    } else {
                        selectedToDate = formattedDate;
                        toDateEditText.setText(displayDate);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        
        datePickerDialog.show();
    }

    private void generateReport() {
        // All filters are optional
        showLoading();
        fetchFeeCollectionReport();
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

    private void fetchFeeCollectionReport() {
        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            hideLoading();
            showNoData();
            Toast.makeText(this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + Constants.feeCollectionReportColumnWiseFilterUrl;

        Log.d(TAG, "Fetching report from: " + url);
        Log.d(TAG, "Filters - FromDate: " + selectedFromDate + ", ToDate: " + selectedToDate +
                   ", Session: " + selectedSessionId + ", Class: " + selectedClassId +
                   ", Section: " + selectedSectionId + ", FeeType: " + selectedFeeTypeId);

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
                    if (selectedFromDate != null && !selectedFromDate.isEmpty()) {
                        jsonBody.put("from_date", selectedFromDate);
                    }
                    if (selectedToDate != null && !selectedToDate.isEmpty()) {
                        jsonBody.put("to_date", selectedToDate);
                    }
                    if (selectedSessionId != null && !selectedSessionId.isEmpty()) {
                        jsonBody.put("session_id", selectedSessionId);
                    }
                    if (selectedClassId != null && !selectedClassId.isEmpty()) {
                        jsonBody.put("class_id", selectedClassId);
                    }
                    if (selectedSectionId != null && !selectedSectionId.isEmpty()) {
                        jsonBody.put("section_id", selectedSectionId);
                    }
                    if (selectedFeeTypeId != null && !selectedFeeTypeId.isEmpty()) {
                        jsonBody.put("fee_type_id", selectedFeeTypeId);
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
                // TODO: Parse report data and display in RecyclerView
                // For now, just show success message
                showContent();
                Toast.makeText(this, "Report generated successfully", Toast.LENGTH_SHORT).show();
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

    private static class FeeTypeData {
        String id;
        String name;
        String code;
    }
}

