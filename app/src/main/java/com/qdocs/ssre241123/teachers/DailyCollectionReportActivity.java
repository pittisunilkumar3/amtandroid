package com.qdocs.ssre241123.teachers;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.qdocs.ssre241123.adapters.DailyCollectionReportAdapter;
import com.qdocs.ssre241123.model.DailyCollectionReportModel;
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
 * Activity for displaying Daily Collection Report
 * Shows daily fee collection data with date range filters
 */
public class DailyCollectionReportActivity extends AppCompatActivity {
    
    private static final String TAG = "DailyCollectionReport";
    
    private EditText fromDateEt;
    private EditText toDateEt;
    private Button generateReportButton;
    private CardView summaryCard;
    private TextView totalAmountTv;
    private TextView totalCountTv;
    private TextView dateRangeTv;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayout nodataLayout;
    
    private DailyCollectionReportAdapter adapter;
    private List<DailyCollectionReportModel> collectionList;
    
    private Calendar fromDateCalendar;
    private Calendar toDateCalendar;
    private SimpleDateFormat dateFormat;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_collection_report);
        
        Log.d(TAG, "onCreate called");
        
        // Initialize date format
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        
        // Initialize calendars with default date range (last 30 days)
        fromDateCalendar = Calendar.getInstance();
        fromDateCalendar.add(Calendar.DAY_OF_MONTH, -30);
        
        toDateCalendar = Calendar.getInstance();
        
        // Initialize views
        initializeViews();
        
        // Setup RecyclerView
        setupRecyclerView();
        
        // Set default dates
        setDefaultDates();
        
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
        
        fromDateEt = findViewById(R.id.from_date_et);
        toDateEt = findViewById(R.id.to_date_et);
        generateReportButton = findViewById(R.id.generate_report_button);
        summaryCard = findViewById(R.id.summary_card);
        totalAmountTv = findViewById(R.id.total_amount_tv);
        totalCountTv = findViewById(R.id.total_count_tv);
        dateRangeTv = findViewById(R.id.date_range_tv);
        recyclerView = findViewById(R.id.report_content_recyclerView);
        progressBar = findViewById(R.id.progressBar);
        nodataLayout = findViewById(R.id.nodata_layout);
        
        // Initialize list
        collectionList = new ArrayList<>();
    }
    
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DailyCollectionReportAdapter(this, collectionList);
        recyclerView.setAdapter(adapter);
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
            if (validateDateRange()) {
                generateReport();
            }
        });
    }
    
    private boolean validateDateRange() {
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
        
        return true;
    }
    
    private void generateReport() {
        Log.d(TAG, "=== Generating Report ===");
        
        String fromDate = fromDateEt.getText().toString().trim();
        String toDate = toDateEt.getText().toString().trim();
        
        Log.d(TAG, "From Date: " + fromDate);
        Log.d(TAG, "To Date: " + toDate);
        
        showLoading();
        fetchDailyCollectionReport(fromDate, toDate);
    }
    
    private void fetchDailyCollectionReport(String fromDate, String toDate) {
        Log.d(TAG, "=== Fetching Daily Collection Report ===");
        
        // Get base URL from shared preferences
        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + Constants.dailyCollectionReportFilterUrl;
        
        Log.d(TAG, "Base URL: " + baseUrl);
        Log.d(TAG, "Full API URL: " + url);
        Log.d(TAG, "From Date: " + fromDate);
        Log.d(TAG, "To Date: " + toDate);
        
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
            response -> {
                Log.d(TAG, "=== API Response Received ===");
                Log.d(TAG, "Response Length: " + response.length());
                Log.d(TAG, "Response: " + response);
                hideLoading();
                parseDailyCollectionReportResponse(response);
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
                    Log.e(TAG, "No connection error");
                } else if (error instanceof com.android.volley.TimeoutError) {
                    errorMessage = "Request timeout";
                    Log.e(TAG, "Timeout error");
                } else if (error instanceof com.android.volley.ServerError) {
                    errorMessage = "Server error";
                    Log.e(TAG, "Server error");
                } else if (error instanceof com.android.volley.ParseError) {
                    errorMessage = "Parse error";
                    Log.e(TAG, "Parse error");
                } else if (error.getMessage() != null) {
                    errorMessage = error.getMessage();
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
                    jsonBody.put("date_from", fromDate);
                    jsonBody.put("date_to", toDate);
                    
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
    
    private void parseDailyCollectionReportResponse(String response) {
        Log.d(TAG, "=== Parsing Response ===");

        try {
            JSONObject jsonObject = new JSONObject(response);

            int status = jsonObject.optInt("status", 0);
            String message = jsonObject.optString("message", "");

            Log.d(TAG, "Status: " + status);
            Log.d(TAG, "Message: " + message);

            if (status == 1) {
                // Success - parse data
                collectionList.clear();

                double totalAmount = 0;
                int totalCount = 0;

                // Parse fees_data
                JSONArray feesDataArray = jsonObject.optJSONArray("fees_data");
                if (feesDataArray != null) {
                    Log.d(TAG, "Fees Data Array Length: " + feesDataArray.length());

                    for (int i = 0; i < feesDataArray.length(); i++) {
                        JSONObject dataObj = feesDataArray.getJSONObject(i);

                        DailyCollectionReportModel collection = new DailyCollectionReportModel();
                        collection.setDate(dataObj.optString("date", ""));
                        collection.setAmount(dataObj.optDouble("amt", 0));
                        collection.setCount(dataObj.optInt("count", 0));
                        collection.setType("fees");

                        // Parse student_fees_deposite_ids array
                        JSONArray idsArray = dataObj.optJSONArray("student_fees_deposite_ids");
                        List<String> ids = new ArrayList<>();
                        if (idsArray != null) {
                            for (int j = 0; j < idsArray.length(); j++) {
                                ids.add(idsArray.optString(j));
                            }
                        }
                        collection.setStudentFeesDepositeIds(ids);

                        collectionList.add(collection);

                        totalAmount += collection.getAmount();
                        totalCount += collection.getCount();

                        if (i == 0) {
                            Log.d(TAG, "First Collection - Date: " + collection.getDate());
                            Log.d(TAG, "First Collection - Amount: " + collection.getAmount());
                            Log.d(TAG, "First Collection - Count: " + collection.getCount());
                        }
                    }
                }

                // Parse other_fees_data
                JSONArray otherFeesDataArray = jsonObject.optJSONArray("other_fees_data");
                if (otherFeesDataArray != null) {
                    Log.d(TAG, "Other Fees Data Array Length: " + otherFeesDataArray.length());

                    for (int i = 0; i < otherFeesDataArray.length(); i++) {
                        JSONObject dataObj = otherFeesDataArray.getJSONObject(i);

                        DailyCollectionReportModel collection = new DailyCollectionReportModel();
                        collection.setDate(dataObj.optString("date", ""));
                        collection.setAmount(dataObj.optDouble("amt", 0));
                        collection.setCount(dataObj.optInt("count", 0));
                        collection.setType("other_fees");

                        // Parse student_fees_deposite_ids array
                        JSONArray idsArray = dataObj.optJSONArray("student_fees_deposite_ids");
                        List<String> ids = new ArrayList<>();
                        if (idsArray != null) {
                            for (int j = 0; j < idsArray.length(); j++) {
                                ids.add(idsArray.optString(j));
                            }
                        }
                        collection.setStudentFeesDepositeIds(ids);

                        collectionList.add(collection);

                        totalAmount += collection.getAmount();
                        totalCount += collection.getCount();
                    }
                }

                Log.d(TAG, "Collection list size: " + collectionList.size());
                Log.d(TAG, "Total Amount: " + totalAmount);
                Log.d(TAG, "Total Count: " + totalCount);

                if (!collectionList.isEmpty()) {
                    // Update adapter
                    adapter.notifyDataSetChanged();

                    // Update summary
                    updateSummary(totalAmount, totalCount);

                    // Show content
                    showContent();

                    // Show success message
                    String successMessage = "Found " + collectionList.size() + " collection record(s)";
                    Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "No data in response");
                    showNoData();
                    Toast.makeText(this, "No collections found for the selected date range",
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

    private void updateSummary(double totalAmount, int totalCount) {
        // Get currency symbol
        String currency = Utility.getSharedPreferences(getApplicationContext(), Constants.currency);
        if (currency == null || currency.isEmpty()) {
            currency = "₹";
        }

        // Format amount with currency
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("en", "IN"));
        String formattedAmount = currency + " " + numberFormat.format(totalAmount);

        totalAmountTv.setText(formattedAmount);
        totalCountTv.setText(String.valueOf(totalCount));

        // Set date range
        String fromDate = fromDateEt.getText().toString();
        String toDate = toDateEt.getText().toString();
        dateRangeTv.setText("Date Range: " + fromDate + " to " + toDate);
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

