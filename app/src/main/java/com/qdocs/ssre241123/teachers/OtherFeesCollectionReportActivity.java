package com.qdocs.ssre241123.teachers;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.OtherCollectionReportAdapter;
import com.qdocs.ssre241123.model.OtherCollectionReportModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.OtherCollectionReportFilterHelper;
import com.qdocs.ssre241123.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Activity for Other Fees Collection Report
 * Shows fee collection data for "other" fee types (hostel, library, etc.)
 * Filters: Search Duration, Session, Class, Section, Fee Type, Received By, Group By
 */
public class OtherFeesCollectionReportActivity extends BaseFinanceReportActivity {

    private static final String TAG = "OtherFeesCollectionReport";

    private OtherCollectionReportAdapter adapter;
    private List<OtherCollectionReportModel> collectionList;
    private String currency;

    // Filter helper
    private OtherCollectionReportFilterHelper filterHelper;

    // Summary UI components
    private CardView summaryCard;
    private TextView totalRecordsTv;
    private TextView totalAmountTv;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_other_fees_collection_report;
    }

    @Override
    protected String getReportTitle() {
        return getString(R.string.other_fees_collection_report);
    }

    @Override
    protected String getReportApiUrl() {
        // Use the correct endpoint: other-collection-report/filter
        return Constants.otherCollectionReportFilterUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize currency
        currency = Utility.getSharedPreferences(
            getApplicationContext(), Constants.currency);
        if (currency == null || currency.isEmpty()) {
            currency = "₹";
        }

        // Initialize collection list
        collectionList = new ArrayList<>();

        // Initialize filter helper
        filterHelper = new OtherCollectionReportFilterHelper();

        // Initialize summary UI components
        summaryCard = findViewById(R.id.summaryCard);
        totalRecordsTv = findViewById(R.id.totalRecordsTv);
        totalAmountTv = findViewById(R.id.totalAmountTv);
    }

    @Override
    protected void setupSpecificFilters() {
        // Setup date pickers (but NOT setupSearchDurationSpinner - we'll do that after API loads)
        setupDatePickers();

        // Set default to today
        setTodayDates();

        // Load sessions from standard API (for session/class/section hierarchy)
        loadSessionsForHierarchy();

        // Load custom filter data from /list API (for other filters)
        // This will call populateSearchDurationSpinner() which sets up the spinner properly
        loadCustomFilterData();
    }

    @Override
    protected void loadFilterOptions() {
        // Override to prevent BaseFinanceReportActivity from loading standard filters
        // We use our custom loadCustomFilterData() instead
        Log.d(TAG, "loadFilterOptions() overridden - using custom filter data instead");
    }

    /**
     * Load sessions with hierarchical class/section data
     * This is needed for the Session -> Class -> Section cascading dropdowns
     */
    private void loadSessionsForHierarchy() {
        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            Log.w(TAG, "No internet connection for loading sessions");
            return;
        }

        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + Constants.feeCollectionFiltersGetUrl;

        Log.d(TAG, "Loading sessions from: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "Sessions response received");
                    parseSessionsResponse(response);
                },
                error -> {
                    Log.e(TAG, "Error loading sessions", error);
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

    /**
     * Parse sessions response and populate session spinner
     */
    private void parseSessionsResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);

            if (jsonResponse.getInt("status") == 1 && jsonResponse.has("data")) {
                JSONObject data = jsonResponse.getJSONObject("data");

                // Parse hierarchical sessions
                if (data.has("sessions")) {
                    parseSessionsHierarchy(data.getJSONArray("sessions"));
                    setupSessionSpinner();
                    Log.d(TAG, "Sessions loaded and spinner populated");
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing sessions response", e);
        }
    }

    /**
     * Load custom filter data from the /list API endpoint
     * This includes classes, fee_types, and received_by (collect by) options
     */
    private void loadCustomFilterData() {
        Log.d(TAG, "========================================");
        Log.d(TAG, "loadCustomFilterData() CALLED");
        Log.d(TAG, "========================================");

        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + Constants.otherCollectionReportListUrl;

        Log.d(TAG, "Base URL: " + baseUrl);
        Log.d(TAG, "List URL constant: " + Constants.otherCollectionReportListUrl);
        Log.d(TAG, "Full URL: " + url);

        Log.d(TAG, "Creating Volley request...");

        StringRequest request = new StringRequest(
            Request.Method.POST,
            url,
            response -> {
                Log.d(TAG, "========================================");
                Log.d(TAG, "API RESPONSE RECEIVED");
                Log.d(TAG, "Response length: " + response.length() + " characters");
                Log.d(TAG, "Response: " + response);
                Log.d(TAG, "========================================");
                parseCustomFilterData(response);
            },
            error -> {
                Log.e(TAG, "========================================");
                Log.e(TAG, "API ERROR");
                Log.e(TAG, "Error loading filter data", error);
                if (error.networkResponse != null) {
                    Log.e(TAG, "Status code: " + error.networkResponse.statusCode);
                    Log.e(TAG, "Response data: " + new String(error.networkResponse.data));
                }
                Log.e(TAG, "========================================");
                Toast.makeText(OtherFeesCollectionReportActivity.this,
                    "Error loading filter options", Toast.LENGTH_SHORT).show();
            }
        ) {
            @Override
            public byte[] getBody() {
                // Send empty JSON object as payload
                return "{}".getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                return headers;
            }
        };

        Log.d(TAG, "Adding request to Volley queue...");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
        Log.d(TAG, "Request added to queue successfully");
        Log.d(TAG, "========================================");
    }

    /**
     * Parse the custom filter data response using the helper class
     */
    private void parseCustomFilterData(String response) {
        Log.d(TAG, "========================================");
        Log.d(TAG, "parseCustomFilterData called");
        Log.d(TAG, "========================================");

        // Use the helper to parse the response
        boolean success = filterHelper.parseFilterData(response);

        if (success) {
            Log.d(TAG, "Filter data parsed successfully, populating dropdowns...");

            // Populate all dropdowns with the parsed data
            // Note: Session/Class/Section are populated from hierarchical API
            // Only populate the custom filters here
            populateSearchDurationSpinner();
            populateGroupBySpinner();
            populateFeeTypeSpinner();
            populateCollectBySpinner();

            Log.d(TAG, "All dropdowns populated successfully");
            Toast.makeText(this, "Filters loaded successfully", Toast.LENGTH_SHORT).show();
        } else {
            Log.e(TAG, "Failed to parse filter data");
            Toast.makeText(this, "Error loading filter options", Toast.LENGTH_SHORT).show();
        }

        Log.d(TAG, "========================================");
    }

    /**
     * Populate Search Duration spinner (overrides base implementation)
     */
    private void populateSearchDurationSpinner() {
        if (searchDurationSpinner == null) {
            Log.w(TAG, "searchDurationSpinner is null");
            return;
        }

        List<OtherCollectionReportFilterHelper.SearchTypeOption> searchTypes = filterHelper.getSearchTypes();

        final List<String> displayNames = new ArrayList<>();
        displayNames.add("Select Duration");

        for (OtherCollectionReportFilterHelper.SearchTypeOption option : searchTypes) {
            displayNames.add(option.getDisplayName());
        }

        runOnUiThread(() -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                displayNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            searchDurationSpinner.setAdapter(adapter);

            // Set up item selection listener to handle date range changes
            searchDurationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        // "Select Duration" - keep existing dates (already set to today in setupSpecificFilters)
                        Log.d(TAG, "Select Duration selected - keeping existing dates");
                        return;
                    }

                    String selectedDuration = displayNames.get(position);
                    Log.d(TAG, "Search Duration selected: " + selectedDuration + " at position " + position);

                    // Map display name to duration type and set dates accordingly
                    if (selectedDuration.equalsIgnoreCase("Today")) {
                        selectedSearchDuration = "today";
                        setTodayDates();
                        Log.d(TAG, "Set dates to Today");
                    } else if (selectedDuration.equalsIgnoreCase("This Week")) {
                        selectedSearchDuration = "week";
                        setThisWeekDates();
                        Log.d(TAG, "Set dates to This Week");
                    } else if (selectedDuration.equalsIgnoreCase("This Month")) {
                        selectedSearchDuration = "month";
                        setThisMonthDates();
                        Log.d(TAG, "Set dates to This Month");
                    } else if (selectedDuration.equalsIgnoreCase("Last Month")) {
                        selectedSearchDuration = "last_month";
                        setLastMonthDates();
                        Log.d(TAG, "Set dates to Last Month");
                    } else if (selectedDuration.equalsIgnoreCase("This Year")) {
                        selectedSearchDuration = "year";
                        setThisYearDates();
                        Log.d(TAG, "Set dates to This Year");
                    } else if (selectedDuration.equalsIgnoreCase("Custom Period") ||
                               selectedDuration.equalsIgnoreCase("Custom Duration")) {
                        selectedSearchDuration = "custom";
                        enableDatePickers();
                        Log.d(TAG, "Enabled date pickers for Custom Period");
                    } else {
                        // Default to today
                        Log.w(TAG, "Unknown duration: " + selectedDuration + ", defaulting to Today");
                        selectedSearchDuration = "today";
                        setTodayDates();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Log.d(TAG, "Nothing selected in Search Duration - keeping existing dates");
                    // Don't change dates if nothing is selected
                }
            });

            Log.d(TAG, "Search Duration spinner populated with " + displayNames.size() + " items");

            // Find "Today" option and set it as default selection
            int todayPosition = -1;
            for (int i = 0; i < displayNames.size(); i++) {
                if (displayNames.get(i).equalsIgnoreCase("Today")) {
                    todayPosition = i;
                    break;
                }
            }

            if (todayPosition > 0) {
                Log.d(TAG, "Setting default selection to 'Today' at position " + todayPosition);
                searchDurationSpinner.setSelection(todayPosition);
            } else {
                Log.w(TAG, "'Today' option not found in dropdown, keeping position 0");
            }
        });
    }

    /**
     * Populate Group By spinner
     */
    private void populateGroupBySpinner() {
        if (groupBySpinner == null) {
            Log.w(TAG, "groupBySpinner is null");
            return;
        }

        List<OtherCollectionReportFilterHelper.GroupByOption> groupByOpts = filterHelper.getGroupByOptions();

        final List<String> displayNames = new ArrayList<>();
        final List<String> values = new ArrayList<>();

        displayNames.add("No Grouping");
        values.add("");

        for (OtherCollectionReportFilterHelper.GroupByOption option : groupByOpts) {
            displayNames.add(option.getDisplayName());
            values.add(option.getValue());
        }

        runOnUiThread(() -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                displayNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            groupBySpinner.setAdapter(adapter);

            groupBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedGroupBy = values.get(position);
                    Log.d(TAG, "Selected group by: " + displayNames.get(position) + " (value: " + selectedGroupBy + ")");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    selectedGroupBy = "";
                }
            });

            Log.d(TAG, "Group By spinner populated with " + displayNames.size() + " items");
        });
    }

    /**
     * Populate Fee Type spinner
     */
    private void populateFeeTypeSpinner() {
        if (feeTypeSpinner == null) {
            Log.w(TAG, "feeTypeSpinner is null");
            return;
        }

        List<OtherCollectionReportFilterHelper.FeeTypeOption> feeTypes = filterHelper.getFeeTypes();

        final List<String> displayNames = new ArrayList<>();
        final List<String> ids = new ArrayList<>();

        displayNames.add("All Fee Types");
        ids.add("");

        for (OtherCollectionReportFilterHelper.FeeTypeOption option : feeTypes) {
            displayNames.add(option.getType());
            ids.add(option.getId());
        }

        runOnUiThread(() -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                displayNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            feeTypeSpinner.setAdapter(adapter);

            feeTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedFeeTypeId = ids.get(position);
                    Log.d(TAG, "Selected fee type: " + displayNames.get(position) + " (ID: " + selectedFeeTypeId + ")");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    selectedFeeTypeId = "";
                }
            });

            Log.d(TAG, "Fee Type spinner populated with " + displayNames.size() + " items");
        });
    }

    /**
     * Populate Collect By (Received By) spinner
     */
    private void populateCollectBySpinner() {
        if (collectBySpinner == null) {
            Log.w(TAG, "collectBySpinner is null");
            return;
        }

        List<OtherCollectionReportFilterHelper.ReceivedByOption> receivedByList = filterHelper.getReceivedBy();

        final List<String> displayNames = new ArrayList<>();
        final List<String> ids = new ArrayList<>();

        displayNames.add("All Collectors");
        ids.add("");

        for (OtherCollectionReportFilterHelper.ReceivedByOption option : receivedByList) {
            displayNames.add(option.getName());
            ids.add(option.getId());
        }

        runOnUiThread(() -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                displayNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            collectBySpinner.setAdapter(adapter);

            collectBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedCollectById = ids.get(position);
                    Log.d(TAG, "Selected collector: " + displayNames.get(position) + " (ID: " + selectedCollectById + ")");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    selectedCollectById = "";
                }
            });

            Log.d(TAG, "Collect By spinner populated with " + displayNames.size() + " items");
        });
    }

    @Override
    protected String buildRequestBody() {
        try {
            JSONObject jsonBody = new JSONObject();

            // Add date range - send from_date and to_date directly (no search_type)
            if (selectedFromDate != null && !selectedFromDate.isEmpty()) {
                jsonBody.put("from_date", selectedFromDate);
            }
            if (selectedToDate != null && !selectedToDate.isEmpty()) {
                jsonBody.put("to_date", selectedToDate);
            }

            // Add other filters
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
                jsonBody.put("feetype_id", selectedFeeTypeId);
            }
            // Use 'collect_by_id' as per API specification
            if (selectedCollectById != null && !selectedCollectById.isEmpty()) {
                jsonBody.put("collect_by_id", selectedCollectById);
            }
            if (selectedGroupBy != null && !selectedGroupBy.isEmpty()) {
                jsonBody.put("group", selectedGroupBy);
            }

            String requestBody = jsonBody.toString();
            Log.d(TAG, "Request Body: " + requestBody);

            return requestBody;
        } catch (JSONException e) {
            Log.e(TAG, "Error creating request body", e);
            return "{}";
        }
    }

    @Override
    protected void parseReportResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);

            Log.d(TAG, "Response: " + response);

            if (jsonResponse.getInt("status") == 1) {
                // Parse summary
                if (jsonResponse.has("summary")) {
                    JSONObject summary = jsonResponse.getJSONObject("summary");
                    int totalRecords = summary.optInt("total_records", 0);
                    String totalPaid = summary.optString("total_paid", "0.00");
                    String totalDiscount = summary.optString("total_discount", "0.00");
                    String totalFine = summary.optString("total_fine", "0.00");
                    String grandTotal = summary.optString("grand_total", "0.00");

                    // Display summary on UI thread
                    runOnUiThread(() -> {
                        displaySummary(totalRecords, totalPaid, totalDiscount, totalFine, grandTotal);
                    });
                }

                // Parse data
                if (jsonResponse.has("data")) {
                    JSONArray dataArray = jsonResponse.getJSONArray("data");
                    Log.d(TAG, "Data array length: " + dataArray.length());

                    if (dataArray.length() > 0) {
                        Log.d(TAG, "Data found, clearing existing collection list");
                        collectionList.clear();

                        // Check if data is grouped
                        JSONObject firstItem = dataArray.getJSONObject(0);
                        Log.d(TAG, "First item: " + firstItem.toString());

                        if (firstItem.has("group_name")) {
                            // Grouped data
                            Log.d(TAG, "Parsing grouped data");
                            parseGroupedData(dataArray);
                        } else {
                            // Non-grouped data
                            Log.d(TAG, "Parsing non-grouped data");
                            parseNonGroupedData(dataArray);
                        }

                        // Setup RecyclerView on UI thread
                        Log.d(TAG, "Setting up RecyclerView");
                        runOnUiThread(() -> {
                            setupRecyclerView();
                            showContent();
                        });
                    } else {
                        // No data found - show helpful message
                        runOnUiThread(() -> {
                            showNoData();
                        });
                        String message = jsonResponse.optString("message", "No records found");

                        // Check if there's debug information
                        if (jsonResponse.has("debug")) {
                            JSONObject debug = jsonResponse.getJSONObject("debug");
                            String note = debug.optString("note", "");
                            Log.d(TAG, "Debug note: " + note);

                            // Show suggestions if available
                            if (debug.has("suggestions")) {
                                JSONArray suggestions = debug.getJSONArray("suggestions");
                                Log.d(TAG, "Suggestions:");
                                for (int i = 0; i < suggestions.length(); i++) {
                                    Log.d(TAG, "  - " + suggestions.getString(i));
                                }
                            }
                        }

                        runOnUiThread(() -> {
                            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                        });
                    }
                } else {
                    runOnUiThread(() -> {
                        showNoData();
                        Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
                    });
                }
            } else {
                runOnUiThread(() -> {
                    showNoData();
                    String message = jsonResponse.optString("message", "No data found");
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                });
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing report response", e);
            runOnUiThread(() -> {
                showNoData();
                Toast.makeText(this, "Error parsing report data", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void parseNonGroupedData(JSONArray dataArray) throws JSONException {
        Log.d(TAG, "parseNonGroupedData called with " + dataArray.length() + " items");

        // Clear existing data
        collectionList.clear();

        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject item = dataArray.getJSONObject(i);
            OtherCollectionReportModel model = parseCollectionItem(item);
            collectionList.add(model);
            Log.d(TAG, "Added item " + (i + 1) + ": " + model.getFirstname() + " - " + model.getType());
        }

        Log.d(TAG, "Total items in collectionList: " + collectionList.size());
    }

    private void parseGroupedData(JSONArray dataArray) throws JSONException {
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject group = dataArray.getJSONObject(i);

            // You could add a header item here for the group name if needed

            if (group.has("records")) {
                JSONArray records = group.getJSONArray("records");
                for (int j = 0; j < records.length(); j++) {
                    JSONObject item = records.getJSONObject(j);
                    OtherCollectionReportModel model = parseCollectionItem(item);
                    collectionList.add(model);
                }
            }
        }
    }

    private OtherCollectionReportModel parseCollectionItem(JSONObject item) throws JSONException {
        OtherCollectionReportModel model = new OtherCollectionReportModel();

        // Payment ID (e.g., "945/1")
        model.setInvNo(item.optString("payment_id", ""));

        // Date
        model.setDate(item.optString("date", ""));

        // Student information
        model.setAdmissionNo(item.optString("admission_no", ""));
        model.setFirstname(item.optString("student_name", ""));  // API returns full name in student_name

        // Class information (e.g., "SR-BIPC (08199-SR-BIPC-FTB)")
        model.setClassName(item.optString("class", ""));

        // Fee information
        model.setType(item.optString("fee_type", ""));

        // Collected by (e.g., "MAHA LAKSHMI SALLA (200226)")
        model.setReceivedByName(item.optString("collect_by", ""));

        // Payment mode
        model.setPaymentMode(item.optString("mode", ""));

        // Amount information
        model.setAmount(item.optString("paid", "0.00"));
        model.setAmountDiscount(item.optString("discount", "0.00"));
        model.setAmountFine(item.optString("fine", "0.00"));

        // Calculate net amount (total)
        String total = item.optString("total", "0.00");
        model.setAmount(total);  // Store total as amount for display

        // Note/Description
        model.setDescription(item.optString("note", ""));

        // Parse raw_data if present for additional IDs
        if (item.has("raw_data") && !item.isNull("raw_data")) {
            JSONObject rawData = item.getJSONObject("raw_data");
            model.setId(rawData.optString("id", ""));
            model.setStudentId(rawData.optString("student_id", ""));
            model.setClassId(rawData.optString("class_id", ""));
            model.setSectionId(rawData.optString("section_id", ""));
            model.setReceivedBy(rawData.optString("received_by", ""));
        }

        Log.d(TAG, "Parsed item: " + model.getFirstname() + " - " + model.getType() + " - " + model.getAmount());

        return model;
    }

    private void setupRecyclerView() {
        Log.d(TAG, "setupRecyclerView called");
        Log.d(TAG, "reportContentRecyclerView is " + (reportContentRecyclerView == null ? "NULL" : "NOT NULL"));
        Log.d(TAG, "collectionList size: " + (collectionList == null ? "NULL" : collectionList.size()));

        if (reportContentRecyclerView != null) {
            if (collectionList != null && !collectionList.isEmpty()) {
                adapter = new OtherCollectionReportAdapter(this, collectionList);
                reportContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                reportContentRecyclerView.setAdapter(adapter);
                Log.d(TAG, "RecyclerView adapter set successfully with " + collectionList.size() + " items");
            } else {
                Log.w(TAG, "collectionList is empty or null, cannot set adapter");
            }
        } else {
            Log.e(TAG, "reportContentRecyclerView is NULL! Cannot setup RecyclerView");
        }
    }

    private void displaySummary(int totalRecords, String totalPaid, String totalDiscount, String totalFine, String grandTotal) {
        try {
            // Show summary card
            if (summaryCard != null) {
                summaryCard.setVisibility(View.VISIBLE);
            }

            // Display total records
            if (totalRecordsTv != null) {
                totalRecordsTv.setText(String.valueOf(totalRecords));
            }

            // Display grand total as the main amount
            if (totalAmountTv != null) {
                double amount = Double.parseDouble(grandTotal.replace(",", ""));
                NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
                formatter.setMinimumFractionDigits(2);
                formatter.setMaximumFractionDigits(2);
                String formattedAmount = currency + " " + formatter.format(amount);
                totalAmountTv.setText(formattedAmount);
            }

            String summaryText = "Total Records: " + totalRecords +
                               ", Total Paid: " + totalPaid +
                               ", Discount: " + totalDiscount +
                               ", Fine: " + totalFine +
                               ", Grand Total: " + grandTotal;
            Log.d(TAG, "Summary: " + summaryText);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Error parsing amounts", e);
            if (totalAmountTv != null) {
                totalAmountTv.setText(currency + " 0.00");
            }
        }
    }

    /**
     * Set date range to last month
     */
    private void setLastMonthDates() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();

        // Go back one month
        calendar.add(java.util.Calendar.MONTH, -1);

        // Set to first day of last month
        calendar.set(java.util.Calendar.DAY_OF_MONTH, 1);
        selectedFromDate = dateFormat.format(calendar.getTime());

        // Set to last day of last month
        calendar.set(java.util.Calendar.DAY_OF_MONTH, calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH));
        selectedToDate = dateFormat.format(calendar.getTime());

        updateDateFields();
        Log.d(TAG, "Last Month dates set: from=" + selectedFromDate + ", to=" + selectedToDate);
    }
}
