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
 * Activity for Income Report
 * Shows all income records with optional filters
 */
public class IncomeReportActivity extends AppCompatActivity {

    private static final String TAG = "IncomeReport";

    // UI Components
    private Spinner searchTypeSpinner;
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
    private Calendar fromDateCalendar;
    private Calendar toDateCalendar;
    private SimpleDateFormat dateFormat;
    private String selectedSearchType = "";

    // Search type options
    private final String[] searchTypes = {"Today", "This Week", "This Month", "Last Month", "This Year", "Custom Period"};
    private final String[] searchTypeKeys = {"today", "this_week", "this_month", "last_month", "this_year", "period"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_report);

        Log.d(TAG, "onCreate called");

        // Initialize date format
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Initialize calendars
        fromDateCalendar = Calendar.getInstance();
        toDateCalendar = Calendar.getInstance();

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
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        searchTypeSpinner = findViewById(R.id.search_type_spinner);
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

    private void generateReport() {
        Log.d(TAG, "=== Generating Report ===");
        Log.d(TAG, "Search Type: " + selectedSearchType);

        showLoading();
        fetchIncomeReport();
    }

    private void fetchIncomeReport() {
        Log.d(TAG, "=== Fetching Income Report ===");

        // Get base URL from shared preferences
        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + Constants.incomeReportFilterUrl;

        Log.d(TAG, "Full API URL: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "=== API Response Received ===");
                    Log.d(TAG, "Response: " + response);
                    hideLoading();
                    parseIncomeReportResponse(response);
                },
                error -> {
                    Log.e(TAG, "=== API Error ===");
                    Log.e(TAG, "Error: " + error.toString());

                    String errorMessage = "Unknown error occurred";

                    if (error.networkResponse != null) {
                        Log.e(TAG, "Status Code: " + error.networkResponse.statusCode);
                        try {
                            String errorBody = new String(error.networkResponse.data, "UTF-8");
                            Log.e(TAG, "Error Body: " + errorBody);
                            errorMessage = "Server error: " + error.networkResponse.statusCode;
                        } catch (UnsupportedEncodingException e) {
                            Log.e(TAG, "Error parsing error body", e);
                        }
                    } else if (error instanceof com.android.volley.NoConnectionError) {
                        errorMessage = "No internet connection";
                    } else if (error instanceof com.android.volley.TimeoutError) {
                        errorMessage = "Request timeout";
                    }

                    hideLoading();
                    showNoData();
                    Toast.makeText(this, "Error loading report: " + errorMessage,
                            Toast.LENGTH_LONG).show();
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", "application/json");

                Log.d(TAG, "=== Request Headers ===");
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    Log.d(TAG, entry.getKey() + ": " + entry.getValue());
                }

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

        // Add request to queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        Log.d(TAG, "Request added to queue");
    }

    private void parseIncomeReportResponse(String response) {
        Log.d(TAG, "=== Parsing Response ===");

        try {
            JSONObject jsonObject = new JSONObject(response);

            int status = jsonObject.optInt("status", 0);
            String message = jsonObject.optString("message", "");

            Log.d(TAG, "Status: " + status);
            Log.d(TAG, "Message: " + message);

            if (status == 1) {
                // Success - parse data
                incomeList.clear();

                // Parse summary
                JSONObject summary = jsonObject.optJSONObject("summary");
                int totalRecords = 0;
                double totalAmount = 0;

                if (summary != null) {
                    totalRecords = summary.optInt("total_records", 0);
                    totalAmount = summary.optDouble("total_amount", 0);
                }

                // Parse data array
                JSONArray dataArray = jsonObject.optJSONArray("data");
                if (dataArray != null) {
                    Log.d(TAG, "Data Array Length: " + dataArray.length());

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject dataObj = dataArray.getJSONObject(i);

                        IncomeReportModel income = new IncomeReportModel();
                        income.setId(dataObj.optString("id", ""));
                        income.setName(dataObj.optString("name", ""));
                        income.setInvoiceNo(dataObj.optString("invoice_no", ""));
                        income.setDate(dataObj.optString("date", ""));
                        income.setAmount(dataObj.optString("amount", "0"));
                        income.setIncomeHead(dataObj.optString("income_head", ""));
                        income.setIncomeHeadId(dataObj.optString("income_head_id", ""));
                        income.setNote(dataObj.optString("note", ""));
                        income.setDocuments(dataObj.optString("documents", ""));

                        incomeList.add(income);

                        if (i == 0) {
                            Log.d(TAG, "First Income - Name: " + income.getName());
                            Log.d(TAG, "First Income - Amount: " + income.getAmount());
                        }
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
                    Toast.makeText(this, "No income records found for the selected period",
                            Toast.LENGTH_SHORT).show();
                }

            } else {
                // Error status
                Log.e(TAG, "Error status from API: " + message);
                showNoData();
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            Log.e(TAG, "JSON parsing error", e);
            showNoData();
            Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSummary(int totalRecords, double totalAmount) {
        // Get currency symbol
        String currency = Utility.getSharedPreferences(getApplicationContext(), Constants.currency);
        if (currency == null || currency.isEmpty()) {
            currency = "₹";
        }

        // Format amount with currency
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("en", "IN"));
        String formattedAmount = currency + " " + numberFormat.format(totalAmount);

        totalRecordsTv.setText(String.valueOf(totalRecords));
        totalAmountTv.setText(formattedAmount);
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        nodataLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        summaryCard.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void showNoData() {
        nodataLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        summaryCard.setVisibility(View.GONE);
    }

    private void showContent() {
        nodataLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        summaryCard.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
