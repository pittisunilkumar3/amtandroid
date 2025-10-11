package com.qdocs.ssre241123.teachers;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.IncomeReportAdapter;
import com.qdocs.ssre241123.model.IncomeHeadModel;
import com.qdocs.ssre241123.model.IncomeReportModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Activity for Income Group Report
 * Shows income records grouped by income head with filters
 */
public class IncomeGroupReportActivity extends AppCompatActivity {

    private static final String TAG = "IncomeGroupReport";

    // UI Components
    private Spinner searchTypeSpinner;
    private Spinner incomeHeadSpinner;
    private LinearLayout dateRangeLayout;
    private EditText fromDateEt;
    private EditText toDateEt;
    private Button generateReportButton;
    private CardView summaryCard;
    private TextView totalRecordsTv;
    private TextView totalAmountTv;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayout nodataLayout;

    // Data
    private IncomeReportAdapter adapter;
    private List<IncomeReportModel> incomeList;
    private List<IncomeHeadModel> incomeHeadList;
    private List<String> incomeHeadNameList;
    private List<String> incomeHeadIdList;
    private Calendar fromDateCalendar;
    private Calendar toDateCalendar;
    private SimpleDateFormat dateFormat;
    private String selectedSearchType = "";
    private String selectedIncomeHeadId = "";

    // Search type options
    private final String[] searchTypes = {"Today", "Month", "Year", "Custom"};
    private final String[] searchTypeKeys = {"today", "month", "year", "period"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_group_report);

        Log.d(TAG, "onCreate called");

        // Initialize date format
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Initialize calendars
        fromDateCalendar = Calendar.getInstance();
        toDateCalendar = Calendar.getInstance();

        // Initialize lists
        incomeHeadList = new ArrayList<>();
        incomeHeadNameList = new ArrayList<>();
        incomeHeadIdList = new ArrayList<>();

        // Initialize views
        initializeViews();

        // Setup RecyclerView
        setupRecyclerView();

        // Setup search type spinner
        setupSearchTypeSpinner();

        // Setup date pickers
        setupDatePickers();

        // Setup generate button
        setupGenerateButton();

        // Load income heads
        loadIncomeHeads();
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        searchTypeSpinner = findViewById(R.id.search_type_spinner);
        incomeHeadSpinner = findViewById(R.id.income_head_spinner);
        dateRangeLayout = findViewById(R.id.date_range_layout);
        fromDateEt = findViewById(R.id.from_date_et);
        toDateEt = findViewById(R.id.to_date_et);
        generateReportButton = findViewById(R.id.generate_report_button);
        summaryCard = findViewById(R.id.summary_card);
        totalRecordsTv = findViewById(R.id.total_records_tv);
        totalAmountTv = findViewById(R.id.total_amount_tv);
        recyclerView = findViewById(R.id.report_content_recyclerView);
        progressBar = findViewById(R.id.progressBar);
        nodataLayout = findViewById(R.id.nodata_layout);

        // Initialize list
        incomeList = new ArrayList<>();

        // Hide date range layout initially
        dateRangeLayout.setVisibility(View.GONE);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IncomeReportAdapter(this, incomeList);
        recyclerView.setAdapter(adapter);
    }

    private void setupSearchTypeSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, searchTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchTypeSpinner.setAdapter(adapter);

        searchTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSearchType = searchTypeKeys[position];
                Log.d(TAG, "Selected search type: " + selectedSearchType);

                // Show/hide date range layout based on selection
                if ("period".equals(selectedSearchType)) {
                    dateRangeLayout.setVisibility(View.VISIBLE);
                    setDefaultDates();
                } else {
                    dateRangeLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupIncomeHeadSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, incomeHeadNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        incomeHeadSpinner.setAdapter(adapter);

        incomeHeadSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedIncomeHeadId = incomeHeadIdList.get(position);
                Log.d(TAG, "Selected income head ID: " + selectedIncomeHeadId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setDefaultDates() {
        fromDateEt.setText(dateFormat.format(fromDateCalendar.getTime()));
        toDateEt.setText(dateFormat.format(toDateCalendar.getTime()));
    }

    private void setupDatePickers() {
        // From Date Picker
        fromDateEt.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        fromDateCalendar.set(Calendar.YEAR, year);
                        fromDateCalendar.set(Calendar.MONTH, month);
                        fromDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        fromDateEt.setText(dateFormat.format(fromDateCalendar.getTime()));
                    },
                    fromDateCalendar.get(Calendar.YEAR),
                    fromDateCalendar.get(Calendar.MONTH),
                    fromDateCalendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // To Date Picker
        toDateEt.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        toDateCalendar.set(Calendar.YEAR, year);
                        toDateCalendar.set(Calendar.MONTH, month);
                        toDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        toDateEt.setText(dateFormat.format(toDateCalendar.getTime()));
                    },
                    toDateCalendar.get(Calendar.YEAR),
                    toDateCalendar.get(Calendar.MONTH),
                    toDateCalendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });
    }

    private void setupGenerateButton() {
        generateReportButton.setOnClickListener(v -> {
            if (validateInput()) {
                generateReport();
            }
        });
    }

    private boolean validateInput() {
        if ("period".equals(selectedSearchType)) {
            String fromDate = fromDateEt.getText().toString().trim();
            String toDate = toDateEt.getText().toString().trim();

            if (fromDate.isEmpty() || toDate.isEmpty()) {
                Toast.makeText(this, "Please select both dates", Toast.LENGTH_SHORT).show();
                return false;
            }

            // Check if from date is after to date
            if (fromDateCalendar.after(toDateCalendar)) {
                Toast.makeText(this, "From Date cannot be after To Date", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    private void loadIncomeHeads() {
        Log.d(TAG, "=== Loading Income Heads ===");

        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            Toast.makeText(this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            setupDefaultIncomeHeadSpinner();
            return;
        }

        // Use buildApiUrl to ensure correct URL construction
        String url = Utility.buildApiUrl(getApplicationContext(), Constants.incomeHeadListUrl);

        Log.d(TAG, "Income Head List API Endpoint: " + Constants.incomeHeadListUrl);
        Log.d(TAG, "Income Head List Full URL: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "Income Head List Response: " + response);
                    parseIncomeHeadResponse(response);
                },
                error -> {
                    Log.e(TAG, "Error loading income heads", error);
                    Toast.makeText(this, "Failed to load income heads", Toast.LENGTH_SHORT).show();
                    setupDefaultIncomeHeadSpinner();
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

    private void parseIncomeHeadResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);

            // Clear existing lists
            incomeHeadList.clear();
            incomeHeadNameList.clear();
            incomeHeadIdList.clear();

            // Add "All" option at the top
            incomeHeadNameList.add("All");
            incomeHeadIdList.add("");

            // Check if response has data array
            if (jsonObject.has("data")) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                Log.d(TAG, "Income heads count: " + dataArray.length());

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject headObj = dataArray.getJSONObject(i);

                    IncomeHeadModel head = new IncomeHeadModel();
                    head.setId(headObj.optString("id", ""));
                    head.setIncomeCategory(headObj.optString("income_category", ""));
                    head.setIsActive(headObj.optString("is_active", "yes"));

                    // Only add active income heads (is_active = "yes")
                    if ("yes".equalsIgnoreCase(head.getIsActive())) {
                        incomeHeadList.add(head);
                        incomeHeadNameList.add(head.getIncomeCategory());
                        incomeHeadIdList.add(head.getId());
                        Log.d(TAG, "Added income head: " + head.getIncomeCategory() + " (ID: " + head.getId() + ")");
                    }
                }

                Log.d(TAG, "Loaded " + incomeHeadList.size() + " active income heads");
            }

            // Setup spinner with loaded data
            setupIncomeHeadSpinner();

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing income head response", e);
            setupDefaultIncomeHeadSpinner();
        }
    }

    private void setupDefaultIncomeHeadSpinner() {
        incomeHeadNameList.clear();
        incomeHeadIdList.clear();
        incomeHeadNameList.add("All");
        incomeHeadIdList.add("");
        setupIncomeHeadSpinner();
    }

    private void generateReport() {
        Log.d(TAG, "=== Generating Report ===");
        Log.d(TAG, "Search Type: " + selectedSearchType);
        Log.d(TAG, "Income Head ID: " + selectedIncomeHeadId);

        showLoading();
        fetchIncomeGroupReport();
    }

    private void fetchIncomeGroupReport() {
        Log.d(TAG, "=== Fetching Income Group Report ===");

        // Use buildApiUrl to ensure correct URL construction
        String url = Utility.buildApiUrl(getApplicationContext(), Constants.incomeGroupReportFilterUrl);

        Log.d(TAG, "Report API Endpoint: " + Constants.incomeGroupReportFilterUrl);
        Log.d(TAG, "Report Full URL: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "=== API Response Received ===");
                    Log.d(TAG, "Response: " + response);
                    hideLoading();
                    parseIncomeReportResponse(response);
                },
                error -> {
                    Log.e(TAG, "Error fetching report", error);
                    hideLoading();
                    showNoData();
                    Toast.makeText(this, R.string.apiErrorMsg, Toast.LENGTH_SHORT).show();
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

                    if ("period".equals(selectedSearchType)) {
                        // Custom period - send date_from and date_to
                        String fromDate = fromDateEt.getText().toString().trim();
                        String toDate = toDateEt.getText().toString().trim();
                        jsonBody.put("date_from", fromDate);
                        jsonBody.put("date_to", toDate);
                    } else {
                        // Predefined search type
                        jsonBody.put("search_type", selectedSearchType);
                    }

                    // Add income head ID if selected (not "All")
                    if (!selectedIncomeHeadId.isEmpty()) {
                        jsonBody.put("income_head_id", selectedIncomeHeadId);
                    }

                    String requestBody = jsonBody.toString();
                    Log.d(TAG, "=== Request Body ===");
                    Log.d(TAG, requestBody);

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

    private void parseIncomeReportResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);

            // Clear existing data
            incomeList.clear();

            // Parse income records
            if (jsonObject.has("data")) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                Log.d(TAG, "Income records count: " + dataArray.length());

                int totalRecords = 0;
                double totalAmount = 0.0;

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject incomeObj = dataArray.getJSONObject(i);

                    IncomeReportModel income = new IncomeReportModel();
                    income.setId(incomeObj.optString("id", ""));
                    income.setName(incomeObj.optString("name", ""));
                    income.setInvoiceNo(incomeObj.optString("invoice_no", ""));
                    income.setDate(incomeObj.optString("date", ""));
                    income.setAmount(incomeObj.optString("amount", "0"));
                    income.setIncomeHead(incomeObj.optString("income_head", ""));
                    income.setIncomeHeadId(incomeObj.optString("income_head_id", ""));
                    income.setNote(incomeObj.optString("note", ""));
                    income.setDocuments(incomeObj.optString("documents", ""));

                    incomeList.add(income);

                    // Calculate totals
                    totalRecords++;
                    try {
                        totalAmount += Double.parseDouble(income.getAmount());
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "Error parsing amount: " + income.getAmount());
                    }
                }

                Log.d(TAG, "Income list size: " + incomeList.size());
                Log.d(TAG, "Total Records: " + totalRecords);
                Log.d(TAG, "Total Amount: " + totalAmount);

                if (!incomeList.isEmpty()) {
                    // Update adapter
                    adapter.notifyDataSetChanged();

                    // Update summary
                    updateSummary(totalRecords, totalAmount);

                    // Show content
                    showContent();

                    // Show success message
                    String successMessage = "Found " + incomeList.size() + " income record(s)";
                    Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "No data in response");
                    showNoData();
                    Toast.makeText(this, "No income records found for the selected filters",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d(TAG, "No data field in response");
                showNoData();
                Toast.makeText(this, "No income records found", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing response", e);
            showNoData();
            Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSummary(int totalRecords, double totalAmount) {
        totalRecordsTv.setText(String.valueOf(totalRecords));

        // Format amount with currency
        String currency = Utility.getSharedPreferences(getApplicationContext(), Constants.currency);
        if (currency == null || currency.isEmpty()) {
            currency = "₹";
        }

        NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);

        totalAmountTv.setText(currency + formatter.format(totalAmount));
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        nodataLayout.setVisibility(View.GONE);
        summaryCard.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void showContent() {
        recyclerView.setVisibility(View.VISIBLE);
        summaryCard.setVisibility(View.VISIBLE);
        nodataLayout.setVisibility(View.GONE);
    }

    private void showNoData() {
        nodataLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        summaryCard.setVisibility(View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
