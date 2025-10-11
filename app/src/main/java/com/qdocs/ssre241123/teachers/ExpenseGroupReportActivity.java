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
import com.qdocs.ssre241123.adapters.ExpenseReportAdapter;
import com.qdocs.ssre241123.model.ExpenseHeadModel;
import com.qdocs.ssre241123.model.ExpenseReportModel;
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
 * Activity for Expense Group Report
 * Shows expense records grouped by expense head with filters
 */
public class ExpenseGroupReportActivity extends AppCompatActivity {

    private static final String TAG = "ExpenseGroupReport";

    // UI Components
    private Spinner searchTypeSpinner;
    private Spinner expenseHeadSpinner;
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
    private ExpenseReportAdapter adapter;
    private List<ExpenseReportModel> expenseList;
    private List<ExpenseHeadModel> expenseHeadList;
    private List<String> expenseHeadNameList;
    private List<String> expenseHeadIdList;
    private Calendar fromDateCalendar;
    private Calendar toDateCalendar;
    private SimpleDateFormat dateFormat;
    private String selectedSearchType = "";
    private String selectedExpenseHeadId = "";

    // Search type options - Updated to match API specification
    private final String[] searchTypes = {
            "Today", "This Week", "Last Week", "This Month", "Last Month",
            "Last 3 Months", "Last 6 Months", "Last 12 Months", "This Year", "Last Year", "Custom Period"
    };
    private final String[] searchTypeKeys = {
            "today", "this_week", "last_week", "this_month", "last_month",
            "last_3_month", "last_6_month", "last_12_month", "this_year", "last_year", "period"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_group_report);

        Log.d(TAG, "onCreate called");

        // Initialize date format
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Initialize calendars
        fromDateCalendar = Calendar.getInstance();
        toDateCalendar = Calendar.getInstance();

        // Initialize lists
        expenseHeadList = new ArrayList<>();
        expenseHeadNameList = new ArrayList<>();
        expenseHeadIdList = new ArrayList<>();

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

        // Load expense heads
        loadExpenseHeads();
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        searchTypeSpinner = findViewById(R.id.search_type_spinner);
        expenseHeadSpinner = findViewById(R.id.expense_head_spinner);
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
        expenseList = new ArrayList<>();

        // Hide date range layout initially
        dateRangeLayout.setVisibility(View.GONE);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ExpenseReportAdapter(this, expenseList);
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

    private void setupExpenseHeadSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, expenseHeadNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseHeadSpinner.setAdapter(adapter);

        expenseHeadSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedExpenseHeadId = expenseHeadIdList.get(position);
                Log.d(TAG, "Selected expense head ID: " + selectedExpenseHeadId);
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

    private void loadExpenseHeads() {
        Log.d(TAG, "=== Loading Expense Heads ===");

        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            Toast.makeText(this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            setupDefaultExpenseHeadSpinner();
            return;
        }

        // Use the expense-group-report/list endpoint to get expense heads
        String url = Utility.buildApiUrl(getApplicationContext(), Constants.expenseGroupReportListUrl);

        Log.d(TAG, "Expense Head List API Endpoint: " + Constants.expenseGroupReportListUrl);
        Log.d(TAG, "Expense Head List Full URL: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "Expense Head List Response: " + response);
                    parseExpenseHeadResponse(response);
                },
                error -> {
                    Log.e(TAG, "Error loading expense heads", error);
                    Toast.makeText(this, "Failed to load expense heads", Toast.LENGTH_SHORT).show();
                    setupDefaultExpenseHeadSpinner();
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

    private void parseExpenseHeadResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);

            // Clear existing lists
            expenseHeadList.clear();
            expenseHeadNameList.clear();
            expenseHeadIdList.clear();

            // Add "All" option at the top
            expenseHeadNameList.add("All");
            expenseHeadIdList.add("");

            // Check status
            int status = jsonObject.optInt("status", 0);
            if (status != 1) {
                Log.e(TAG, "Failed to load expense heads: " + jsonObject.optString("message", "Unknown error"));
                setupExpenseHeadSpinner();
                return;
            }

            // Check if response has data object with expense_heads array
            if (jsonObject.has("data")) {
                JSONObject dataObj = jsonObject.getJSONObject("data");

                if (dataObj.has("expense_heads")) {
                    JSONArray expenseHeadsArray = dataObj.getJSONArray("expense_heads");
                    Log.d(TAG, "Expense heads count: " + expenseHeadsArray.length());

                    for (int i = 0; i < expenseHeadsArray.length(); i++) {
                        JSONObject headObj = expenseHeadsArray.getJSONObject(i);

                        ExpenseHeadModel head = new ExpenseHeadModel();
                        head.setId(headObj.optString("id", ""));
                        head.setExpCategory(headObj.optString("exp_category", ""));

                        // Add all expense heads from the list API
                        expenseHeadList.add(head);
                        expenseHeadNameList.add(head.getExpCategory());
                        expenseHeadIdList.add(head.getId());
                        Log.d(TAG, "Added expense head: " + head.getExpCategory() + " (ID: " + head.getId() + ")");
                    }

                    Log.d(TAG, "Loaded " + expenseHeadList.size() + " expense heads");
                }
            }

            // Setup spinner with loaded data
            setupExpenseHeadSpinner();

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing expense head response", e);
            setupDefaultExpenseHeadSpinner();
        }
    }

    private void setupDefaultExpenseHeadSpinner() {
        expenseHeadNameList.clear();
        expenseHeadIdList.clear();
        expenseHeadNameList.add("All");
        expenseHeadIdList.add("");
        setupExpenseHeadSpinner();
    }

    private void generateReport() {
        Log.d(TAG, "=== Generating Report ===");
        Log.d(TAG, "Search Type: " + selectedSearchType);
        Log.d(TAG, "Expense Head ID: " + selectedExpenseHeadId);

        showLoading();
        fetchExpenseGroupReport();
    }

    private void fetchExpenseGroupReport() {
        Log.d(TAG, "=== Fetching Expense Group Report ===");

        // Use buildApiUrl to ensure correct URL construction
        String url = Utility.buildApiUrl(getApplicationContext(), Constants.expenseGroupReportFilterUrl);

        Log.d(TAG, "Report API Endpoint: " + Constants.expenseGroupReportFilterUrl);
        Log.d(TAG, "Report Full URL: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "=== API Response Received ===");
                    Log.d(TAG, "Response: " + response);
                    hideLoading();
                    parseExpenseReportResponse(response);
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
                    } else if (!selectedSearchType.isEmpty()) {
                        // Predefined search type
                        jsonBody.put("search_type", selectedSearchType);
                    }

                    // Add expense head ID if selected (not "All")
                    // API expects parameter name "head_id" not "expense_head_id"
                    if (!selectedExpenseHeadId.isEmpty()) {
                        jsonBody.put("head_id", selectedExpenseHeadId);
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

    private void parseExpenseReportResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);

            // Check status
            int status = jsonObject.optInt("status", 0);
            if (status != 1) {
                String message = jsonObject.optString("message", "Failed to fetch report");
                Log.e(TAG, "API Error: " + message);
                showNoData();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;
            }

            // Clear existing data
            expenseList.clear();

            // Get totals from summary if available
            int totalRecords = 0;
            double totalAmount = 0.0;

            if (jsonObject.has("summary")) {
                JSONObject summary = jsonObject.getJSONObject("summary");
                totalRecords = summary.optInt("total_expenses", 0);

                // Parse total_amount which may have commas
                String totalAmountStr = summary.optString("total_amount", "0");
                try {
                    // Remove commas before parsing
                    totalAmountStr = totalAmountStr.replace(",", "");
                    totalAmount = Double.parseDouble(totalAmountStr);
                } catch (NumberFormatException e) {
                    Log.e(TAG, "Error parsing total amount from summary: " + totalAmountStr);
                }
                Log.d(TAG, "Summary - Total Expenses: " + totalRecords + ", Total Amount: " + totalAmount);
            }

            // Parse expense records
            if (jsonObject.has("data")) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                Log.d(TAG, "Expense records count: " + dataArray.length());

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject expenseObj = dataArray.getJSONObject(i);

                    ExpenseReportModel expense = new ExpenseReportModel();
                    expense.setId(expenseObj.optString("id", ""));
                    expense.setName(expenseObj.optString("name", ""));
                    expense.setInvoiceNo(expenseObj.optString("invoice_no", ""));
                    expense.setDate(expenseObj.optString("date", ""));
                    expense.setAmount(expenseObj.optString("amount", "0"));
                    expense.setExpCategory(expenseObj.optString("exp_category", ""));
                    expense.setExpHeadId(expenseObj.optString("exp_head_id", ""));
                    expense.setNote(expenseObj.optString("note", ""));
                    expense.setDocuments(expenseObj.optString("documents", ""));

                    expenseList.add(expense);
                }

                Log.d(TAG, "Expense list size: " + expenseList.size());

                // If summary was not available, calculate from data
                if (totalRecords == 0 && !expenseList.isEmpty()) {
                    totalRecords = expenseList.size();
                    for (ExpenseReportModel expense : expenseList) {
                        try {
                            totalAmount += Double.parseDouble(expense.getAmount());
                        } catch (NumberFormatException e) {
                            Log.e(TAG, "Error parsing amount: " + expense.getAmount());
                        }
                    }
                }

                if (!expenseList.isEmpty()) {
                    // Update adapter
                    adapter.notifyDataSetChanged();

                    // Update summary
                    updateSummary(totalRecords, totalAmount);

                    // Show content
                    showContent();

                    // Show success message
                    String successMessage = "Found " + expenseList.size() + " expense record(s)";
                    Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "No data in response");
                    showNoData();
                    Toast.makeText(this, "No expense records found for the selected filters",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d(TAG, "No data field in response");
                showNoData();
                Toast.makeText(this, "No expense records found", Toast.LENGTH_SHORT).show();
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
