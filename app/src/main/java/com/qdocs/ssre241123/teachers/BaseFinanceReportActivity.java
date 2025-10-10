package com.qdocs.ssre241123.teachers;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
 * Base Activity for Finance Reports with Hierarchical Filters
 * Provides common functionality for all finance reports
 */
public abstract class BaseFinanceReportActivity extends AppCompatActivity {

    protected static final String TAG = "BaseFinanceReport";

    // UI Components
    protected FrameLayout actionBar;
    protected ImageView backButton;
    protected TextView titleTextView;
    protected Spinner sessionSpinner, classSpinner, sectionSpinner;
    protected Spinner feeTypeSpinner, collectBySpinner, groupBySpinner;
    protected Spinner searchTypeSpinner, searchDurationSpinner;
    protected EditText fromDateEditText, toDateEditText;
    protected Button generateReportButton;
    protected RecyclerView reportContentRecyclerView;
    protected ProgressBar progressBar;
    protected LinearLayout nodataLayout;
    protected CardView filtersCard;

    // Data Lists for hierarchical structure
    protected List<SessionData> sessionsList = new ArrayList<>();
    protected List<ClassData> currentClassesList = new ArrayList<>();
    protected List<SectionData> currentSectionsList = new ArrayList<>();
    protected List<FeeTypeData> feeTypesList = new ArrayList<>();
    protected List<CollectByData> collectByList = new ArrayList<>();
    protected List<String> groupByOptions = new ArrayList<>();

    // Selected values
    protected String selectedSessionId = null;
    protected String selectedClassId = null;
    protected String selectedSectionId = null;
    protected String selectedFeeTypeId = null;
    protected String selectedCollectById = null;
    protected String selectedGroupBy = null;
    protected String selectedSearchType = "all";
    protected String selectedSearchDuration = "today";
    protected String selectedFromDate = null;
    protected String selectedToDate = null;

    // Date formatters
    protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    protected SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        Log.d(TAG, "onCreate called for " + getClass().getSimpleName());

        initializeViews();
        setupActionBar();
        setupCommonSpinners();
        setupSpecificFilters();
        setupGenerateButton();
        loadFilterOptions();
    }

    // Abstract methods to be implemented by child classes
    protected abstract int getLayoutResourceId();
    protected abstract String getReportTitle();
    protected abstract String getReportApiUrl();
    protected abstract void setupSpecificFilters();
    protected abstract void parseReportResponse(String response);

    protected void initializeViews() {
        actionBar = findViewById(R.id.actionBar);
        backButton = findViewById(R.id.actionBarBackBtn);
        titleTextView = findViewById(R.id.actionBarTitleTV);
        generateReportButton = findViewById(R.id.generateReportButton);
        reportContentRecyclerView = findViewById(R.id.reportContentRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        nodataLayout = findViewById(R.id.nodataLayout);
        filtersCard = findViewById(R.id.filtersCard);

        // Optional filters (may not exist in all layouts)
        sessionSpinner = findViewById(R.id.sessionSpinner);
        classSpinner = findViewById(R.id.classSpinner);
        sectionSpinner = findViewById(R.id.sectionSpinner);
        feeTypeSpinner = findViewById(R.id.feeTypeSpinner);
        collectBySpinner = findViewById(R.id.collectBySpinner);
        groupBySpinner = findViewById(R.id.groupBySpinner);
        searchTypeSpinner = findViewById(R.id.searchTypeSpinner);
        searchDurationSpinner = findViewById(R.id.searchDurationSpinner);
        fromDateEditText = findViewById(R.id.fromDateEditText);
        toDateEditText = findViewById(R.id.toDateEditText);

        titleTextView.setText(getReportTitle());

        // Setup RecyclerView
        if (reportContentRecyclerView != null) {
            reportContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            reportContentRecyclerView.setHasFixedSize(true);
        }
    }

    protected void setupActionBar() {
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

    protected void setupCommonSpinners() {
        // Session Spinner
        if (sessionSpinner != null) {
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
        }

        // Class Spinner
        if (classSpinner != null) {
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
        }

        // Section Spinner
        if (sectionSpinner != null) {
            sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0 && currentSectionsList.size() > position - 1) {
                        SectionData section = currentSectionsList.get(position - 1);
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
    }

    protected void setupGenerateButton() {
        if (generateReportButton != null) {
            generateReportButton.setOnClickListener(v -> generateReport());
        }
    }

    protected void generateReport() {
        showLoading();
        fetchReport();
    }

    protected void loadFilterOptions() {
        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            Toast.makeText(this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading();

        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + Constants.feeCollectionFiltersGetUrl;

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
                headers.put("Content-Type", Constants.contentType);
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

    protected void parseFilterOptions(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);

            if (jsonResponse.getInt("status") == 1) {
                JSONObject data = jsonResponse.getJSONObject("data");

                // Parse hierarchical sessions
                if (data.has("sessions")) {
                    parseSessionsHierarchy(data.getJSONArray("sessions"));
                }

                // Parse fee types
                if (data.has("fee_types")) {
                    parseFeeTypes(data.getJSONArray("fee_types"));
                }

                // Parse collect by
                if (data.has("collect_by")) {
                    parseCollectBy(data.getJSONArray("collect_by"));
                }

                // Parse group by options
                if (data.has("group_by_options")) {
                    parseGroupByOptions(data.getJSONArray("group_by_options"));
                }

                setupAllSpinners();
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing filter options", e);
            Toast.makeText(this, "Error parsing filters", Toast.LENGTH_SHORT).show();
        }
    }

    protected void parseSessionsHierarchy(JSONArray sessionsArray) throws JSONException {
        sessionsList.clear();

        for (int i = 0; i < sessionsArray.length(); i++) {
            JSONObject sessionObj = sessionsArray.getJSONObject(i);
            SessionData session = new SessionData();
            session.id = sessionObj.getString("id");
            session.name = sessionObj.getString("name");
            session.classes = new ArrayList<>();

            // Parse classes for this session
            if (sessionObj.has("classes")) {
                JSONArray classesArray = sessionObj.getJSONArray("classes");
                for (int j = 0; j < classesArray.length(); j++) {
                    JSONObject classObj = classesArray.getJSONObject(j);
                    ClassData classData = new ClassData();
                    classData.id = classObj.getString("id");
                    classData.name = classObj.getString("name");
                    classData.sections = new ArrayList<>();

                    // Parse sections for this class
                    if (classObj.has("sections")) {
                        JSONArray sectionsArray = classObj.getJSONArray("sections");
                        for (int k = 0; k < sectionsArray.length(); k++) {
                            JSONObject sectionObj = sectionsArray.getJSONObject(k);
                            SectionData section = new SectionData();
                            section.id = sectionObj.getString("id");
                            section.name = sectionObj.getString("name");
                            classData.sections.add(section);
                        }
                    }

                    session.classes.add(classData);
                }
            }

            sessionsList.add(session);
        }
    }

    protected void parseFeeTypes(JSONArray feeTypesArray) throws JSONException {
        feeTypesList.clear();
        for (int i = 0; i < feeTypesArray.length(); i++) {
            JSONObject obj = feeTypesArray.getJSONObject(i);
            FeeTypeData feeType = new FeeTypeData();
            feeType.id = obj.getString("id");
            feeType.name = obj.getString("name");
            if (obj.has("code")) {
                feeType.code = obj.getString("code");
            }
            feeTypesList.add(feeType);
        }
    }

    protected void parseCollectBy(JSONArray collectByArray) throws JSONException {
        collectByList.clear();
        for (int i = 0; i < collectByArray.length(); i++) {
            JSONObject obj = collectByArray.getJSONObject(i);
            CollectByData collectBy = new CollectByData();
            collectBy.id = obj.getString("id");
            collectBy.name = obj.getString("name");
            if (obj.has("employee_id")) {
                collectBy.employeeId = obj.getString("employee_id");
            }
            collectByList.add(collectBy);
        }
    }

    protected void parseGroupByOptions(JSONArray groupByArray) throws JSONException {
        groupByOptions.clear();
        for (int i = 0; i < groupByArray.length(); i++) {
            groupByOptions.add(groupByArray.getString(i));
        }
    }

    protected void setupAllSpinners() {
        if (sessionSpinner != null) setupSessionSpinner();
        if (feeTypeSpinner != null) setupFeeTypeSpinner();
        if (collectBySpinner != null) setupCollectBySpinner();
        if (groupBySpinner != null) setupGroupBySpinner();
    }

    protected void setupSessionSpinner() {
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

    protected void updateClassSpinner(List<ClassData> classes) {
        if (classSpinner == null) return;

        currentClassesList = classes;
        List<String> classNames = new ArrayList<>();
        classNames.add("Select Class");
        for (ClassData classData : classes) {
            classNames.add(classData.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, classNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter);
    }

    protected void updateSectionSpinner(List<SectionData> sections) {
        if (sectionSpinner == null) return;

        currentSectionsList = sections;
        List<String> sectionNames = new ArrayList<>();
        sectionNames.add("Select Section");
        for (SectionData section : sections) {
            sectionNames.add(section.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sectionNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectionSpinner.setAdapter(adapter);
    }

    protected void setupFeeTypeSpinner() {
        List<String> feeTypeNames = new ArrayList<>();
        feeTypeNames.add("Select Fee Type");
        for (FeeTypeData feeType : feeTypesList) {
            feeTypeNames.add(feeType.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, feeTypeNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feeTypeSpinner.setAdapter(adapter);

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

    protected void setupCollectBySpinner() {
        List<String> collectByNames = new ArrayList<>();
        collectByNames.add("Select Collector");
        for (CollectByData collectBy : collectByList) {
            collectByNames.add(collectBy.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, collectByNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        collectBySpinner.setAdapter(adapter);

        collectBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && collectByList.size() > position - 1) {
                    selectedCollectById = collectByList.get(position - 1).id;
                } else {
                    selectedCollectById = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCollectById = null;
            }
        });
    }

    protected void setupGroupBySpinner() {
        List<String> groupByNames = new ArrayList<>();
        groupByNames.add("Select Group By");
        for (String option : groupByOptions) {
            groupByNames.add(option.substring(0, 1).toUpperCase() + option.substring(1));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, groupByNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupBySpinner.setAdapter(adapter);

        groupBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && groupByOptions.size() > position - 1) {
                    selectedGroupBy = groupByOptions.get(position - 1);
                } else {
                    selectedGroupBy = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedGroupBy = null;
            }
        });
    }

    protected void setupSearchTypeSpinner() {
        if (searchTypeSpinner == null) return;

        List<String> searchTypes = new ArrayList<>();
        searchTypes.add("All");
        searchTypes.add("Paid");
        searchTypes.add("Unpaid");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, searchTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchTypeSpinner.setAdapter(adapter);

        searchTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        selectedSearchType = "all";
                        break;
                    case 1:
                        selectedSearchType = "paid";
                        break;
                    case 2:
                        selectedSearchType = "unpaid";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSearchType = "all";
            }
        });
    }

    protected void setupSearchDurationSpinner() {
        if (searchDurationSpinner == null) return;

        List<String> durations = new ArrayList<>();
        durations.add("Today");
        durations.add("This Week");
        durations.add("This Month");
        durations.add("This Year");
        durations.add("Custom Duration");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, durations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchDurationSpinner.setAdapter(adapter);

        searchDurationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        selectedSearchDuration = "today";
                        setTodayDates();
                        break;
                    case 1:
                        selectedSearchDuration = "week";
                        setThisWeekDates();
                        break;
                    case 2:
                        selectedSearchDuration = "month";
                        setThisMonthDates();
                        break;
                    case 3:
                        selectedSearchDuration = "year";
                        setThisYearDates();
                        break;
                    case 4:
                        selectedSearchDuration = "custom";
                        enableDatePickers();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSearchDuration = "today";
            }
        });
    }

    protected void setupDatePickers() {
        if (fromDateEditText != null) {
            fromDateEditText.setOnClickListener(v -> showDatePicker(true));
        }
        if (toDateEditText != null) {
            toDateEditText.setOnClickListener(v -> showDatePicker(false));
        }
    }

    protected void showDatePicker(boolean isFromDate) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    String formattedDate = dateFormat.format(calendar.getTime());
                    String displayDate = displayDateFormat.format(calendar.getTime());

                    if (isFromDate) {
                        selectedFromDate = formattedDate;
                        if (fromDateEditText != null) {
                            fromDateEditText.setText(displayDate);
                        }
                    } else {
                        selectedToDate = formattedDate;
                        if (toDateEditText != null) {
                            toDateEditText.setText(displayDate);
                        }
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    protected void setTodayDates() {
        Calendar calendar = Calendar.getInstance();
        selectedFromDate = dateFormat.format(calendar.getTime());
        selectedToDate = dateFormat.format(calendar.getTime());
        updateDateFields();
    }

    protected void setThisWeekDates() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        selectedFromDate = dateFormat.format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_WEEK, 6);
        selectedToDate = dateFormat.format(calendar.getTime());
        updateDateFields();
    }

    protected void setThisMonthDates() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        selectedFromDate = dateFormat.format(calendar.getTime());

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        selectedToDate = dateFormat.format(calendar.getTime());
        updateDateFields();
    }

    protected void setThisYearDates() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        selectedFromDate = dateFormat.format(calendar.getTime());

        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        selectedToDate = dateFormat.format(calendar.getTime());
        updateDateFields();
    }

    protected void enableDatePickers() {
        if (fromDateEditText != null) {
            fromDateEditText.setEnabled(true);
            fromDateEditText.setFocusable(true);
        }
        if (toDateEditText != null) {
            toDateEditText.setEnabled(true);
            toDateEditText.setFocusable(true);
        }
    }

    protected void updateDateFields() {
        try {
            if (fromDateEditText != null && selectedFromDate != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateFormat.parse(selectedFromDate));
                fromDateEditText.setText(displayDateFormat.format(cal.getTime()));
                fromDateEditText.setEnabled(false);
            }
            if (toDateEditText != null && selectedToDate != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateFormat.parse(selectedToDate));
                toDateEditText.setText(displayDateFormat.format(cal.getTime()));
                toDateEditText.setEnabled(false);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error updating date fields", e);
        }
    }

    protected void fetchReport() {
        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            hideLoading();
            showNoData();
            Toast.makeText(this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + getReportApiUrl();

        Log.d(TAG, "Fetching report from: " + url);

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
                    Toast.makeText(this, "Error loading report", Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    protected String buildRequestBody() {
        try {
            JSONObject jsonBody = new JSONObject();

            // Add filters only if they are selected
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
            if (selectedCollectById != null && !selectedCollectById.isEmpty()) {
                jsonBody.put("collect_by_id", selectedCollectById);
            }
            if (selectedGroupBy != null && !selectedGroupBy.isEmpty()) {
                jsonBody.put("group_by", selectedGroupBy);
            }
            if (selectedSearchType != null && !selectedSearchType.isEmpty()) {
                jsonBody.put("search_type", selectedSearchType);
            }
            if (selectedFromDate != null && !selectedFromDate.isEmpty()) {
                jsonBody.put("from_date", selectedFromDate);
            }
            if (selectedToDate != null && !selectedToDate.isEmpty()) {
                jsonBody.put("to_date", selectedToDate);
            }

            String requestBody = jsonBody.toString();
            Log.d(TAG, "Request Body: " + requestBody);

            return requestBody;
        } catch (JSONException e) {
            Log.e(TAG, "Error creating request body", e);
            return "{}";
        }
    }

    protected void showLoading() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        if (reportContentRecyclerView != null) reportContentRecyclerView.setVisibility(View.GONE);
        if (nodataLayout != null) nodataLayout.setVisibility(View.GONE);
    }

    protected void hideLoading() {
        if (progressBar != null) progressBar.setVisibility(View.GONE);
    }

    protected void showContent() {
        if (progressBar != null) progressBar.setVisibility(View.GONE);
        if (reportContentRecyclerView != null) reportContentRecyclerView.setVisibility(View.VISIBLE);
        if (nodataLayout != null) nodataLayout.setVisibility(View.GONE);
    }

    protected void showNoData() {
        if (progressBar != null) progressBar.setVisibility(View.GONE);
        if (reportContentRecyclerView != null) reportContentRecyclerView.setVisibility(View.GONE);
        if (nodataLayout != null) nodataLayout.setVisibility(View.VISIBLE);
    }

    // Data classes for hierarchical structure
    protected static class SessionData {
        String id;
        String name;
        List<ClassData> classes;
    }

    protected static class ClassData {
        String id;
        String name;
        List<SectionData> sections;
    }

    protected static class SectionData {
        String id;
        String name;
    }

    protected static class FeeTypeData {
        String id;
        String name;
        String code;
    }

    protected static class CollectByData {
        String id;
        String name;
        String employeeId;
    }
}

