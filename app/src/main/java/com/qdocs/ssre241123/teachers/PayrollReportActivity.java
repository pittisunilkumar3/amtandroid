package com.qdocs.ssre241123.teachers;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.qdocs.ssre241123.adapters.PayrollReportAdapter;
import com.qdocs.ssre241123.model.PayrollReportModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Activity for Payroll Report
 * Shows staff payroll information with month, year, and role filters
 */
public class PayrollReportActivity extends AppCompatActivity {

    private static final String TAG = "PayrollReport";

    // UI Components
    private Spinner monthSpinner;
    private Spinner yearSpinner;
    private Spinner roleSpinner;
    private Button generateReportButton;
    private CardView summaryCard;
    private TextView totalRecordsTv;
    private TextView totalAmountTv;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayout nodataLayout;

    // Data
    private PayrollReportAdapter adapter;
    private List<PayrollReportModel> payrollList;
    private List<String> monthList;
    private List<String> yearList;
    private List<String> roleList;
    private List<String> roleIdList;
    
    private String selectedMonth = "";
    private String selectedYear = "";
    private String selectedRole = "";

    // Month names
    private final String[] months = {
        "All Months", "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payroll_report);

        Log.d(TAG, "onCreate called");

        // Initialize views
        initializeViews();

        // Setup RecyclerView
        setupRecyclerView();

        // Setup spinners
        setupSpinners();

        // Setup generate button
        setupGenerateButton();

        // Load filter options
        loadFilterOptions();
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        monthSpinner = findViewById(R.id.month_spinner);
        yearSpinner = findViewById(R.id.year_spinner);
        roleSpinner = findViewById(R.id.role_spinner);
        generateReportButton = findViewById(R.id.generate_report_button);
        summaryCard = findViewById(R.id.summary_card);
        totalRecordsTv = findViewById(R.id.total_records_tv);
        totalAmountTv = findViewById(R.id.total_amount_tv);
        recyclerView = findViewById(R.id.report_content_recyclerView);
        progressBar = findViewById(R.id.progressBar);
        nodataLayout = findViewById(R.id.nodata_layout);

        // Initialize lists
        payrollList = new ArrayList<>();
        monthList = new ArrayList<>();
        yearList = new ArrayList<>();
        roleList = new ArrayList<>();
        roleIdList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PayrollReportAdapter(this, payrollList);
        recyclerView.setAdapter(adapter);
    }

    private void setupSpinners() {
        // Setup month spinner with default values
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, months);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedMonth = ""; // All months
                } else {
                    selectedMonth = months[position];
                }
                Log.d(TAG, "Selected month: " + selectedMonth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Setup year spinner with default values (current year and previous years)
        setupYearSpinner();
    }

    private void setupYearSpinner() {
        yearList.clear();
        yearList.add("All Years");
        
        // Add current year and previous 5 years
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i <= 5; i++) {
            yearList.add(String.valueOf(currentYear - i));
        }

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, yearList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedYear = ""; // All years
                } else {
                    selectedYear = yearList.get(position);
                }
                Log.d(TAG, "Selected year: " + selectedYear);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupGenerateButton() {
        generateReportButton.setOnClickListener(v -> generateReport());
    }

    private void loadFilterOptions() {
        Log.d(TAG, "=== Loading Filter Options (Roles) ===");

        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            Toast.makeText(this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            setupDefaultRoleSpinner();
            return;
        }

        // Use buildApiUrl to ensure correct URL construction
        String url = Utility.buildApiUrl(getApplicationContext(), Constants.rolesListUrl);

        Log.d(TAG, "Roles List API Endpoint: " + Constants.rolesListUrl);
        Log.d(TAG, "Roles List Full URL: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "Roles List Response: " + response);
                    parseFilterOptions(response);
                },
                error -> {
                    Log.e(TAG, "Error loading roles: " + error.toString());
                    Toast.makeText(this, "Failed to load roles", Toast.LENGTH_SHORT).show();
                    // Use default role list if API fails
                    setupDefaultRoleSpinner();
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

    private void parseFilterOptions(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);

            // Clear existing lists
            roleList.clear();
            roleIdList.clear();

            // Add "All Roles" option at the top
            roleList.add("All Roles");
            roleIdList.add("");

            // Check if response has data array
            if (jsonObject.has("data")) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                Log.d(TAG, "Roles count: " + dataArray.length());

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject roleObj = dataArray.getJSONObject(i);

                    String roleId = roleObj.optString("id", "");
                    String roleName = roleObj.optString("name", "");
                    String isActive = roleObj.optString("is_active", "0");

                    // Add all roles regardless of is_active status (as per API response, all have is_active = "0")
                    // The API returns is_active as "0" for all roles, so we'll include all roles
                    if (!roleName.isEmpty()) {
                        roleList.add(roleName);
                        roleIdList.add(roleId);
                        Log.d(TAG, "Added role: " + roleName + " (ID: " + roleId + ")");
                    }
                }

                Log.d(TAG, "Loaded " + (roleList.size() - 1) + " roles");
            }

            // Setup spinner with loaded data
            setupRoleSpinner();

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing roles response", e);
            setupDefaultRoleSpinner();
        }
    }

    private void setupRoleSpinner() {
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, roleList);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(roleAdapter);

        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRole = roleIdList.get(position);
                Log.d(TAG, "Selected role: " + selectedRole);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupDefaultRoleSpinner() {
        roleList.clear();
        roleIdList.clear();
        roleList.add("All Roles");
        roleIdList.add("");
        setupRoleSpinner();
    }

    private void generateReport() {
        Log.d(TAG, "=== Generating Report ===");
        Log.d(TAG, "Month: " + selectedMonth);
        Log.d(TAG, "Year: " + selectedYear);
        Log.d(TAG, "Role: " + selectedRole);

        showLoading();
        fetchPayrollReport();
    }

    private void fetchPayrollReport() {
        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + Constants.payrollReportFilterUrl;

        Log.d(TAG, "API URL: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "API Response: " + response);
                    parsePayrollReport(response);
                },
                error -> {
                    hideLoading();
                    Log.e(TAG, "API Error: " + error.toString());

                    String errorMessage = "Failed to load payroll report";
                    if (error.networkResponse != null) {
                        errorMessage += " (Error " + error.networkResponse.statusCode + ")";
                    } else if (error.getMessage() != null) {
                        errorMessage = error.getMessage();
                    }

                    Toast.makeText(PayrollReportActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    showNoData();
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", "application/json");
                Log.d(TAG, "Request Headers: " + headers.toString());
                return headers;
            }

            @Override
            public byte[] getBody() {
                try {
                    JSONObject jsonBody = new JSONObject();

                    // Add month if selected
                    if (!selectedMonth.isEmpty()) {
                        jsonBody.put("month", selectedMonth);
                    }

                    // Add year if selected
                    if (!selectedYear.isEmpty()) {
                        jsonBody.put("year", selectedYear);
                    }

                    // Add role if selected
                    if (!selectedRole.isEmpty()) {
                        jsonBody.put("role", selectedRole);
                    }

                    String requestBody = jsonBody.toString();
                    Log.d(TAG, "Request Body: " + requestBody);
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

    private void parsePayrollReport(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.optInt("status", 0);
            String message = jsonObject.optString("message", "");

            Log.d(TAG, "Response Status: " + status);
            Log.d(TAG, "Response Message: " + message);

            if (status == 1) {
                JSONArray dataArray = jsonObject.optJSONArray("data");

                if (dataArray != null && dataArray.length() > 0) {
                    payrollList.clear();

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject payrollObj = dataArray.getJSONObject(i);

                        PayrollReportModel payroll = new PayrollReportModel();
                        payroll.setId(payrollObj.optString("id", ""));
                        payroll.setEmployeeId(payrollObj.optString("employee_id", ""));
                        payroll.setName(payrollObj.optString("name", ""));
                        payroll.setRole(payrollObj.optString("role", ""));
                        payroll.setDesignation(payrollObj.optString("designation", ""));
                        payroll.setMonth(payrollObj.optString("month", ""));
                        payroll.setYear(payrollObj.optString("year", ""));
                        payroll.setBasicSalary(payrollObj.optString("basic_salary", "0"));
                        payroll.setEarnings(payrollObj.optString("earnings", "0"));
                        payroll.setDeductions(payrollObj.optString("deductions", "0"));
                        payroll.setGrossSalary(payrollObj.optString("gross_salary", "0"));
                        payroll.setTaxAmount(payrollObj.optString("tax", "0"));
                        payroll.setNetSalary(payrollObj.optString("net_salary", "0"));
                        payroll.setPaymentMode(payrollObj.optString("payment_mode", ""));
                        payroll.setPaymentDate(payrollObj.optString("payment_date", ""));
                        payroll.setStatus(payrollObj.optString("status", ""));
                        payroll.setRemarks(payrollObj.optString("remark", ""));

                        payrollList.add(payroll);
                    }

                    // Update summary
                    int totalRecords = jsonObject.optInt("total_records", payrollList.size());
                    updateSummary(totalRecords);

                    // Show data
                    showData();
                    adapter.notifyDataSetChanged();

                    Log.d(TAG, "Loaded " + payrollList.size() + " payroll records");
                } else {
                    showNoData();
                    Toast.makeText(this, "No payroll records found", Toast.LENGTH_SHORT).show();
                }
            } else {
                showNoData();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }

            hideLoading();
        } catch (JSONException e) {
            hideLoading();
            Log.e(TAG, "Error parsing payroll report", e);
            Toast.makeText(this, "Error parsing data", Toast.LENGTH_SHORT).show();
            showNoData();
        }
    }

    private void updateSummary(int totalRecords) {
        // Calculate total payroll amount
        double totalAmount = 0;
        for (PayrollReportModel payroll : payrollList) {
            try {
                double netSalary = Double.parseDouble(payroll.getNetSalary());
                totalAmount += netSalary;
            } catch (NumberFormatException e) {
                Log.e(TAG, "Error parsing net salary", e);
            }
        }

        // Update summary UI
        totalRecordsTv.setText(String.valueOf(totalRecords));

        // Format total amount
        String currency = Utility.getSharedPreferences(getApplicationContext(), Constants.currency);
        if (currency == null || currency.isEmpty()) {
            currency = "₹";
        }

        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("en", "IN"));
        String formattedAmount = currency + " " + numberFormat.format(totalAmount);
        totalAmountTv.setText(formattedAmount);
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

    private void showData() {
        recyclerView.setVisibility(View.VISIBLE);
        summaryCard.setVisibility(View.VISIBLE);
        nodataLayout.setVisibility(View.GONE);
    }

    private void showNoData() {
        recyclerView.setVisibility(View.GONE);
        summaryCard.setVisibility(View.GONE);
        nodataLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

