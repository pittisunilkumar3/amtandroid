package com.qdocs.ssre241123.teachers;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.ssre241123.BaseActivity;
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.UserLogAdapter;
import com.qdocs.ssre241123.model.UserLogModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Activity for User Log Report
 * Shows user login activity with filter for user type
 * Supports filtering by: All User Log, Students, Parents, Staff
 */
public class UserLogReportActivity extends BaseActivity {

    private static final String TAG = "UserLogReportActivity";

    // UI Components
    private Spinner searchTypeSpinner;
    private Spinner roleTypeSpinner;
    private Button generateReportButton;
    private CardView summaryCard;
    private TextView totalRecordsTv;
    private ProgressBar progressBar;
    private LinearLayout nodataLayout;
    private RecyclerView userLogRecyclerView;

    // Data
    private List<UserLogModel> userLogList;
    private UserLogAdapter adapter;

    // Selected values
    private String selectedSearchType = "";
    private String selectedRoleType = "";

    // Search type options
    private final String[] searchTypes = {"All", "By Date Range", "By IP Address", "By Device"};
    private final String[] searchTypeKeys = {"all", "date_range", "ip_address", "device"};

    // Role type options
    private final String[] roleTypes = {"All Users", "Students", "Parents", "Teachers", "Staff", "Admin"};
    private final String[] roleTypeKeys = {"", "student", "parent", "teacher", "staff", "admin"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use LayoutInflater to add content to BaseActivity's container
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_user_log_report, null, false);
        mDrawerLayout.addView(contentView, 0);

        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), "langCode"));

        // Set the title in BaseActivity's title TextView
        titleTV.setText("User Log Report");

        initializeViews();
        setupRecyclerView();
        setupSearchTypeSpinner();
        setupRoleTypeSpinner();
    }

    private void initializeViews() {
        Log.d(TAG, "initializeViews: Starting view initialization");

        // Find views from the inflated content
        searchTypeSpinner = findViewById(R.id.searchTypeSpinner);
        roleTypeSpinner = findViewById(R.id.roleTypeSpinner);
        generateReportButton = findViewById(R.id.generateReportButton);
        summaryCard = findViewById(R.id.summaryCard);
        totalRecordsTv = findViewById(R.id.totalRecordsTv);
        progressBar = findViewById(R.id.progressBar);
        nodataLayout = findViewById(R.id.nodataLayout);
        userLogRecyclerView = findViewById(R.id.userLogRecyclerView);

        // Log view initialization status
        Log.d(TAG, "searchTypeSpinner: " + (searchTypeSpinner != null ? "Found" : "NULL"));
        Log.d(TAG, "roleTypeSpinner: " + (roleTypeSpinner != null ? "Found" : "NULL"));
        Log.d(TAG, "generateReportButton: " + (generateReportButton != null ? "Found" : "NULL"));

        // Apply theme colors to generate button
        String primaryColor = Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour);
        Log.d(TAG, "Primary color: " + primaryColor);

        if (primaryColor != null && !primaryColor.isEmpty()) {
            try {
                if (generateReportButton != null) {
                    generateReportButton.setBackgroundColor(Color.parseColor(primaryColor));
                    Log.d(TAG, "Applied color to generate button");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error parsing primary color", e);
            }
        }

        // Initialize data list
        userLogList = new ArrayList<>();

        setupListeners();
        Log.d(TAG, "initializeViews: Completed");
    }

    private void setupListeners() {
        // Back button is handled by BaseActivity

        generateReportButton.setOnClickListener(v -> generateReport());
    }

    private void setupRecyclerView() {
        userLogRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserLogAdapter(this, userLogList);
        userLogRecyclerView.setAdapter(adapter);
    }

    private void setupSearchTypeSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, searchTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchTypeSpinner.setAdapter(adapter);

        searchTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSearchType = searchTypeKeys[position];
                Log.d(TAG, "Selected search type: " + searchTypes[position] + " (key: " + selectedSearchType + ")");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSearchType = "all";
            }
        });
    }

    private void setupRoleTypeSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roleTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleTypeSpinner.setAdapter(adapter);

        roleTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRoleType = roleTypeKeys[position];
                Log.d(TAG, "Selected role type: " + roleTypes[position] + " (key: " + selectedRoleType + ")");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedRoleType = "";
            }
        });
    }

    private void generateReport() {
        Log.d(TAG, "Generating report with filters:");
        Log.d(TAG, "Search Type: " + selectedSearchType);
        Log.d(TAG, "Role Type: " + selectedRoleType);

        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            Toast.makeText(this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading();

        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + Constants.userLogFilterUrl;

        Log.d(TAG, "Fetching user log report from: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "User log report response: " + response);
                    hideLoading();
                    parseUserLogResponse(response);
                },
                error -> {
                    Log.e(TAG, "Error fetching user log report", error);
                    hideLoading();
                    showNoData();
                    Toast.makeText(this, "Error loading report", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() {
                try {
                    JSONObject jsonBody = new JSONObject();

                    // Add role filter only if selected (not "All Users")
                    if (!selectedRoleType.isEmpty()) {
                        jsonBody.put("role", selectedRoleType);
                    }

                    // Add search type filter
                    if (!selectedSearchType.equals("all")) {
                        jsonBody.put("search_type", selectedSearchType);
                    }

                    String requestBody = jsonBody.toString();
                    Log.d(TAG, "Request body: " + requestBody);
                    return requestBody.getBytes();
                } catch (Exception e) {
                    Log.e(TAG, "Error creating request body", e);
                    return "{}".getBytes();
                }
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseUserLogResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.optInt("status", 0);

            if (status == 1) {
                JSONArray dataArray = jsonObject.optJSONArray("data");
                int totalRecords = jsonObject.optInt("total_records", 0);

                userLogList.clear();

                if (dataArray != null && dataArray.length() > 0) {
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject logObj = dataArray.getJSONObject(i);

                        UserLogModel userLog = new UserLogModel();
                        userLog.setId(logObj.optString("id", ""));
                        userLog.setUser(logObj.optString("user", ""));
                        userLog.setRole(logObj.optString("role", ""));
                        userLog.setClassSectionId(logObj.optString("class_section_id", ""));
                        userLog.setIpaddress(logObj.optString("ipaddress", ""));
                        userLog.setUserAgent(logObj.optString("user_agent", ""));
                        userLog.setLoginDatetime(logObj.optString("login_datetime", ""));
                        userLog.setClassId(logObj.optString("class_id", ""));
                        userLog.setClassName(logObj.optString("class_name", ""));
                        userLog.setSectionId(logObj.optString("section_id", ""));
                        userLog.setSectionName(logObj.optString("section_name", ""));
                        userLog.setDate(logObj.optString("date", ""));
                        userLog.setTime(logObj.optString("time", ""));
                        userLog.setDatetime(logObj.optString("datetime", ""));
                        userLog.setClassSection(logObj.optString("class_section", ""));

                        userLogList.add(userLog);
                    }

                    // Update UI
                    adapter.notifyDataSetChanged();
                    showData();

                    // Update summary
                    totalRecordsTv.setText("Total User Logs: " + totalRecords);

                    Toast.makeText(this, "Found " + totalRecords + " user log records", Toast.LENGTH_SHORT).show();
                } else {
                    showNoData();
                    Toast.makeText(this, "No user log records found", Toast.LENGTH_SHORT).show();
                }
            } else {
                String message = jsonObject.optString("message", "Failed to load user log report");
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                showNoData();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing user log response", e);
            Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
            showNoData();
        }
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        userLogRecyclerView.setVisibility(View.GONE);
        nodataLayout.setVisibility(View.GONE);
        summaryCard.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void showData() {
        userLogRecyclerView.setVisibility(View.VISIBLE);
        summaryCard.setVisibility(View.VISIBLE);
        nodataLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void showNoData() {
        nodataLayout.setVisibility(View.VISIBLE);
        userLogRecyclerView.setVisibility(View.GONE);
        summaryCard.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_rightleft, R.anim.no_animation);
    }
}