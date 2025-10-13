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

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.ssre241123.BaseActivity;
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.MonthlyStaffAttendanceAdapter;
import com.qdocs.ssre241123.model.MonthlyStaffAttendanceModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Activity for Monthly Staff Attendance Report
 * Shows monthly attendance with daily records, summaries, and percentages
 * API: monthly-staff-attendance/report
 */
public class StaffAttendanceReportActivity extends BaseActivity {

    private static final String TAG = "MonthlyStaffAttendance";

    // UI Components
    private Spinner roleSpinner, monthSpinner, yearSpinner;
    private TextView summaryTv, periodTv, filtersAppliedTv;
    private Button generateReportButton, clearFiltersButton;
    private RecyclerView attendanceRecyclerView;
    private ProgressBar progressBar;
    private LinearLayout nodataLayout;
    private CardView summaryCard;

    // Data
    private List<MonthlyStaffAttendanceModel> attendanceList;
    private List<String> datesList; // Dates in the selected month
    private MonthlyStaffAttendanceAdapter adapter;

    // Filter values
    private String selectedRole = "";
    private String selectedMonth = "";
    private int selectedMonthNumber = 0; // 1-12 for month number
    private String selectedYear = "";

    // Filter data lists
    private List<String> roleNamesList;
    private List<String> roleIdsList;

    // Month names
    private final String[] months = {
        "All Months", "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_attendance_report);

        // Initialize views
        initializeViews();

        // Setup components
        setupRecyclerView();
        setupMonthSpinner();
        setupYearSpinner();
        setupButtons();

        // Load roles from API
        loadRolesFromApi();

        // Show initial empty state - data will load when Generate Report is clicked
        showInitialState();
    }

    private void initializeViews() {
        roleSpinner = findViewById(R.id.roleSpinner);
        monthSpinner = findViewById(R.id.monthSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        summaryTv = findViewById(R.id.summaryTv);
        periodTv = findViewById(R.id.periodTv);
        filtersAppliedTv = findViewById(R.id.filtersAppliedTv);
        generateReportButton = findViewById(R.id.generateReportButton);
        clearFiltersButton = findViewById(R.id.clearFiltersButton);
        attendanceRecyclerView = findViewById(R.id.attendanceRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        nodataLayout = findViewById(R.id.nodataLayout);
        summaryCard = findViewById(R.id.summaryCard);

        attendanceList = new ArrayList<>();
        datesList = new ArrayList<>();
        roleNamesList = new ArrayList<>();
        roleIdsList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        attendanceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MonthlyStaffAttendanceAdapter(this, attendanceList, datesList);
        attendanceRecyclerView.setAdapter(adapter);
    }

    private void loadRolesFromApi() {
        Log.d(TAG, "=== Loading Roles from API ===");

        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            Toast.makeText(this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            setupDefaultRoleSpinner();
            return;
        }

        String url = Utility.buildApiUrl(getApplicationContext(), Constants.rolesListUrl);
        Log.d(TAG, "Roles List URL: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "Roles Response: " + response);
                    parseRolesResponse(response);
                },
                error -> {
                    Log.e(TAG, "Error loading roles: " + error.toString());
                    Toast.makeText(this, "Failed to load roles, using defaults", Toast.LENGTH_SHORT).show();
                    setupDefaultRoleSpinner();
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", "application/json");
                
                String token = Utility.getSharedPreferences(getApplicationContext(), "token");
                if (token != null && !token.isEmpty()) {
                    headers.put("Authorization", "Bearer " + token);
                }
                
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

    private void parseRolesResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);

            // Clear existing lists
            roleNamesList.clear();
            roleIdsList.clear();

            // Add "All Roles" option at the top
            roleNamesList.add("All Roles");
            roleIdsList.add("");

            // Check if response has data array
            if (jsonObject.has("data")) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                Log.d(TAG, "Roles count: " + dataArray.length());

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject roleObj = dataArray.getJSONObject(i);

                    String roleId = roleObj.optString("id", "");
                    String roleName = roleObj.optString("name", "");

                    if (!roleName.isEmpty()) {
                        roleNamesList.add(roleName);
                        roleIdsList.add(roleId);
                        Log.d(TAG, "Added role: " + roleName + " (ID: " + roleId + ")");
                    }
                }

                Log.d(TAG, "Loaded " + (roleNamesList.size() - 1) + " roles from API");
            }

            // Setup spinner with loaded data
            setupRoleSpinner();

        } catch (Exception e) {
            Log.e(TAG, "Error parsing roles response", e);
            setupDefaultRoleSpinner();
        }
    }

    private void setupDefaultRoleSpinner() {
        Log.d(TAG, "Setting up default role spinner");
        
        // Clear and add default roles
        roleNamesList.clear();
        roleIdsList.clear();
        
        roleNamesList.add("All Roles");
        roleIdsList.add("");
        
        roleNamesList.add("Super Admin");
        roleIdsList.add("1");
        
        roleNamesList.add("Teacher");
        roleIdsList.add("2");
        
        roleNamesList.add("Accountant");
        roleIdsList.add("3");
        
        roleNamesList.add("Librarian");
        roleIdsList.add("4");
        
        roleNamesList.add("Receptionist");
        roleIdsList.add("7");

        setupRoleSpinner();
    }

    private void setupRoleSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, roleNamesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedRole = ""; // "All Roles"
                } else {
                    selectedRole = roleNamesList.get(position);
                }
                Log.d(TAG, "Selected Role: " + roleNamesList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupMonthSpinner() {
        List<String> monthList = new ArrayList<>();
        for (String month : months) {
            monthList.add(month);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, monthList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapter);

        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedMonth = "";
                    selectedMonthNumber = 0;
                } else {
                    selectedMonth = months[position]; // Month name (e.g., "January")
                    selectedMonthNumber = position; // Month number (1-12)
                }
                Log.d(TAG, "Selected Month: " + months[position] + " (Name: " + selectedMonth + ", Number: " + selectedMonthNumber + ")");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupYearSpinner() {
        // Load years dynamically from sessions API
        loadYearsFromSessionsAPI();
    }

    private void loadYearsFromSessionsAPI() {
        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            Log.w(TAG, "No internet connection, using default years");
            setupDefaultYearSpinner();
            return;
        }

        String url = Utility.buildApiUrl(getApplicationContext(), Constants.staffAttendanceYearsListUrl);
        Log.d(TAG, "Loading years from staff-attendance-years API: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "Staff Attendance Years API Response received");
                    Log.d(TAG, "Response: " + response);
                    parseYearsFromAPI(response);
                },
                error -> {
                    Log.e(TAG, "Error loading staff attendance years: " + error.toString());
                    // Fallback to default year list
                    setupDefaultYearSpinner();
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
                return "{}".getBytes();
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseYearsFromAPI(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.optInt("status", 0);

            if (status == 1) {
                JSONArray dataArray = jsonObject.optJSONArray("data");
                List<String> yearList = new ArrayList<>();
                yearList.add("All Years");

                if (dataArray != null && dataArray.length() > 0) {
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject yearObj = dataArray.getJSONObject(i);
                        String year = yearObj.optString("year", "");
                        
                        if (!year.isEmpty()) {
                            yearList.add(year);
                        }
                    }
                }

                int totalYears = jsonObject.optInt("total_years", 0);
                Log.d(TAG, "Loaded " + totalYears + " years from staff-attendance-years API");

                if (yearList.size() > 1) {
                    // Setup spinner with API years
                    setupYearSpinnerWithData(yearList);
                } else {
                    Log.w(TAG, "No years found in API response, using default years");
                    setupDefaultYearSpinner();
                }
            } else {
                String message = jsonObject.optString("message", "Failed to load years");
                Log.w(TAG, "API returned status 0: " + message);
                setupDefaultYearSpinner();
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing staff attendance years response: " + e.getMessage());
            setupDefaultYearSpinner();
        }
    }

    private void setupDefaultYearSpinner() {
        Log.d(TAG, "Setting up default year spinner (current + 5 years)");
        
        List<String> yearList = new ArrayList<>();
        yearList.add("All Years");
        
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        
        // Add current year and 5 years back
        for (int i = 0; i <= 5; i++) {
            yearList.add(String.valueOf(currentYear - i));
        }

        setupYearSpinnerWithData(yearList);
    }

    private void setupYearSpinnerWithData(List<String> yearList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, yearList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(adapter);

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedYear = "";
                } else {
                    selectedYear = yearList.get(position);
                }
                Log.d(TAG, "Selected Year: " + (position == 0 ? "All Years" : selectedYear));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupButtons() {
        generateReportButton.setOnClickListener(v -> generateReport());
        
        clearFiltersButton.setOnClickListener(v -> {
            clearFilters();
            // Don't auto-load data - user must click Generate Report button
            showInitialState();
        });
    }

    private void clearFilters() {
        roleSpinner.setSelection(0);
        monthSpinner.setSelection(0);
        yearSpinner.setSelection(0);
        selectedRole = "";
        selectedMonth = "";
        selectedMonthNumber = 0;
        selectedYear = "";
        filtersAppliedTv.setVisibility(View.GONE);
    }

    private void generateReport() {
        Log.d(TAG, "=== GENERATING REPORT ===");
        Log.d(TAG, "Role: " + selectedRole);
        Log.d(TAG, "Month: " + selectedMonth + " (Number: " + selectedMonthNumber + ")");
        Log.d(TAG, "Year: " + selectedYear);

        // Validate that both month and year are selected (required for monthly view)
        if (selectedMonth == null || selectedMonth.isEmpty() || selectedMonth.equals("All Months")) {
            Toast.makeText(this, "Please select a specific month to view attendance", Toast.LENGTH_LONG).show();
            Log.w(TAG, "Report generation cancelled: No month selected");
            return;
        }

        if (selectedYear == null || selectedYear.isEmpty() || selectedYear.equals("All Years")) {
            Toast.makeText(this, "Please select a specific year to view attendance", Toast.LENGTH_LONG).show();
            Log.w(TAG, "Report generation cancelled: No year selected");
            return;
        }

        // Check if any filter is applied (excluding "All" selections)
        boolean hasRoleFilter = selectedRole != null && !selectedRole.isEmpty() && !selectedRole.equals("All Roles");
        boolean hasMonthFilter = selectedMonth != null && !selectedMonth.isEmpty() && !selectedMonth.equals("All Months");
        boolean hasYearFilter = selectedYear != null && !selectedYear.isEmpty() && !selectedYear.equals("All Years");

        boolean hasFilters = hasRoleFilter || hasMonthFilter || hasYearFilter;

        Log.d(TAG, "Has role filter: " + hasRoleFilter);
        Log.d(TAG, "Has month filter: " + hasMonthFilter);
        Log.d(TAG, "Has year filter: " + hasYearFilter);
        Log.d(TAG, "Has any filters: " + hasFilters);
        Log.d(TAG, "Validation passed - proceeding with report generation");

        // Always use the filtered endpoint (it handles empty filters)
        loadFilteredStaffAttendance();
    }

    private void loadAllStaffAttendance() {
        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            Toast.makeText(this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading();

        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + Constants.staffAttendanceReportListUrl;

        Log.d(TAG, "Loading all staff attendance from: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "All staff attendance response: " + response);
                    hideLoading();
                    parseStaffAttendanceResponse(response);
                },
                error -> {
                    Log.e(TAG, "Error loading staff attendance: " + error.toString());
                    hideLoading();
                    showNoData();
                    Toast.makeText(this, "Error loading staff attendance data", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", "application/json");
                
                String token = Utility.getSharedPreferences(getApplicationContext(), "token");
                if (token != null && !token.isEmpty()) {
                    headers.put("Authorization", "Bearer " + token);
                }
                
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

    private void loadFilteredStaffAttendance() {
        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            Toast.makeText(this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading();

        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + Constants.monthlyStaffAttendanceReportUrl; // Changed to use monthly API

        Log.d(TAG, "Loading filtered staff attendance from: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "=== API RESPONSE START ===");
                    Log.d(TAG, "Full response: " + response);
                    Log.d(TAG, "Response length: " + response.length());
                    Log.d(TAG, "=== API RESPONSE END ===");
                    hideLoading();
                    parseStaffAttendanceResponse(response);
                },
                error -> {
                    Log.e(TAG, "Error loading filtered staff attendance: " + error.toString());
                    hideLoading();
                    showNoData();
                    Toast.makeText(this, "Error loading filtered staff attendance", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", "application/json");
                
                String token = Utility.getSharedPreferences(getApplicationContext(), "token");
                if (token != null && !token.isEmpty()) {
                    headers.put("Authorization", "Bearer " + token);
                }
                
                return headers;
            }

            @Override
            public byte[] getBody() {
                try {
                    JSONObject jsonBody = new JSONObject();

                    // Add role if selected
                    // Note: New API accepts role as-is (e.g., "Super Admin", "Teacher")
                    // But we keep lowercase mapping for backward compatibility
                    if (selectedRole != null && !selectedRole.isEmpty() && !selectedRole.equals("All Roles")) {
                        String roleValue = selectedRole;
                        // Keep the role as-is, but map "Super Admin" to "admin" for compatibility
                        if (roleValue.equalsIgnoreCase("Super Admin")) {
                            roleValue = "Super Admin";
                        }
                        jsonBody.put("role", roleValue);
                        Log.d(TAG, "Adding role to request: " + roleValue);
                    }

                    // Add month if selected
                    // Note: New API only needs month name, not month_number
                    if (selectedMonth != null && !selectedMonth.isEmpty() && !selectedMonth.equals("All Months")) {
                        jsonBody.put("month", selectedMonth); // Month name (e.g., "October")
                        Log.d(TAG, "Adding month to request: " + selectedMonth);
                    }

                    // Add year if selected
                    if (selectedYear != null && !selectedYear.isEmpty() && !selectedYear.equals("All Years")) {
                        jsonBody.put("year", Integer.parseInt(selectedYear));
                        Log.d(TAG, "Adding year to request: " + selectedYear);
                    }

                    String body = jsonBody.toString();
                    Log.d(TAG, "=== FINAL REQUEST BODY ===");
                    Log.d(TAG, body);
                    Log.d(TAG, "=========================");
                    return body.getBytes();
                } catch (Exception e) {
                    Log.e(TAG, "Error creating request body", e);
                    return "{}".getBytes();
                }
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseStaffAttendanceResponse(String response) {
        try {
            Log.d(TAG, "=== PARSING START ===");
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.optInt("status", 0);
            Log.d(TAG, "Status: " + status);

            if (status == 1) {
                // Clear previous data
                attendanceList.clear();
                datesList.clear();

                // Parse dates array
                JSONArray datesArray = jsonObject.optJSONArray("dates");
                Log.d(TAG, "Dates array exists: " + (datesArray != null));
                if (datesArray != null) {
                    Log.d(TAG, "Dates array length: " + datesArray.length());
                    for (int i = 0; i < datesArray.length(); i++) {
                        datesList.add(datesArray.getString(i));
                    }
                    Log.d(TAG, "Parsed " + datesList.size() + " dates from API");
                    if (datesList.size() > 0) {
                        Log.d(TAG, "First date: " + datesList.get(0));
                        Log.d(TAG, "Last date: " + datesList.get(datesList.size() - 1));
                    }
                } else {
                    Log.e(TAG, "No dates array found in API response");
                }
                
                JSONArray dataArray = jsonObject.optJSONArray("data");
                Log.d(TAG, "Data array exists: " + (dataArray != null));
                if (dataArray != null && dataArray.length() > 0) {
                    Log.d(TAG, "Data array length: " + dataArray.length());
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject staffObj = dataArray.getJSONObject(i);
                        Log.d(TAG, "--- Processing staff " + (i + 1) + " ---");
                        
                        MonthlyStaffAttendanceModel staff = new MonthlyStaffAttendanceModel();
                        staff.setStaffId(staffObj.optString("staff_id", ""));
                        Log.d(TAG, "Staff ID: " + staff.getStaffId());

                        // Parse staff_info
                        Log.d(TAG, "Has staff_info: " + staffObj.has("staff_info"));
                        if (staffObj.has("staff_info")) {
                            JSONObject staffInfoObj = staffObj.getJSONObject("staff_info");
                            MonthlyStaffAttendanceModel.StaffInfo staffInfo = 
                                new MonthlyStaffAttendanceModel.StaffInfo();
                            
                            staffInfo.setName(staffInfoObj.optString("name", ""));
                            staffInfo.setSurname(staffInfoObj.optString("surname", ""));
                            staffInfo.setEmployeeId(staffInfoObj.optString("employee_id", ""));
                            staffInfo.setContactNo(staffInfoObj.optString("contact_no", ""));
                            staffInfo.setEmail(staffInfoObj.optString("email", ""));
                            staffInfo.setRole(staffInfoObj.optString("role", ""));
                            
                            Log.d(TAG, "Staff name: " + staffInfo.getName() + " " + staffInfo.getSurname());
                            Log.d(TAG, "Employee ID: " + staffInfo.getEmployeeId());
                            Log.d(TAG, "Role: " + staffInfo.getRole());
                            
                            staff.setStaffInfo(staffInfo);
                        } else {
                            Log.e(TAG, "staff_info object missing for staff " + staff.getStaffId());
                        }

                        // Parse daily_attendance (object with dates as keys)
                        if (staffObj.has("daily_attendance")) {
                            JSONObject dailyAttObj = staffObj.getJSONObject("daily_attendance");
                            Map<String, MonthlyStaffAttendanceModel.DailyAttendance> dailyMap = 
                                new HashMap<>();
                            
                            Log.d(TAG, "Parsing daily_attendance for staff " + staff.getStaffId());
                            
                            Iterator<String> keys = dailyAttObj.keys();
                            int dayCount = 0;
                            while (keys.hasNext()) {
                                String date = keys.next();
                                JSONObject dayObj = dailyAttObj.getJSONObject(date);
                                
                                MonthlyStaffAttendanceModel.DailyAttendance dayAtt = 
                                    new MonthlyStaffAttendanceModel.DailyAttendance();
                                
                                dayAtt.setDate(dayObj.optString("date", ""));
                                dayAtt.setDayName(dayObj.optString("day_name", ""));
                                dayAtt.setDayShort(dayObj.optString("day_short", ""));
                                dayAtt.setAttendanceType(dayObj.optString("attendance_type", ""));
                                dayAtt.setAttendanceKey(dayObj.optString("attendance_key", ""));
                                dayAtt.setRemark(dayObj.optString("remark", ""));
                                
                                dailyMap.put(date, dayAtt);
                                dayCount++;
                            }
                            
                            Log.d(TAG, "Added " + dayCount + " daily attendance records for staff " + staff.getStaffId());
                            staff.setDailyAttendance(dailyMap);
                        } else {
                            Log.w(TAG, "No daily_attendance found for staff " + staff.getStaffId());
                        }

                        // Parse attendance_summary
                        if (staffObj.has("attendance_summary")) {
                            JSONObject summaryObj = staffObj.getJSONObject("attendance_summary");
                            MonthlyStaffAttendanceModel.AttendanceSummary summary =
                                new MonthlyStaffAttendanceModel.AttendanceSummary();

                            summary.setPresent(summaryObj.optInt("Present", 0));
                            summary.setLate(summaryObj.optInt("Late", 0));
                            summary.setAbsent(summaryObj.optInt("Absent", 0));
                            summary.setHalfDay(summaryObj.optInt("Half Day", 0));
                            summary.setHoliday(summaryObj.optInt("Holiday", 0));

                            Log.d(TAG, "Attendance Summary - P: " + summary.getPresent() +
                                      ", A: " + summary.getAbsent() +
                                      ", L: " + summary.getLate() +
                                      ", H: " + summary.getHalfDay() +
                                      ", HD: " + summary.getHoliday());

                            staff.setAttendanceSummary(summary);
                        } else {
                            Log.w(TAG, "No attendance_summary found for staff " + staff.getStaffId());
                        }

                        // Parse other fields
                        staff.setAttendancePercentage(staffObj.optDouble("attendance_percentage", 0));
                        // attendance_percentage_display can be "-" or a number as string
                        String percentDisplay = staffObj.optString("attendance_percentage_display", "0");
                        try {
                            staff.setAttendancePercentageDisplay(Integer.parseInt(percentDisplay));
                        } catch (NumberFormatException e) {
                            staff.setAttendancePercentageDisplay(0); // Default to 0 for "-" or invalid values
                        }
                        staff.setAttendanceStatus(staffObj.optString("attendance_status", ""));
                        staff.setAttendanceStatusClass(staffObj.optString("attendance_status_class", ""));
                        staff.setTotalWorkingDays(staffObj.optInt("total_working_days", 0));
                        staff.setTotalPresentDays(staffObj.optInt("total_present_days", 0));

                        attendanceList.add(staff);
                    }
                    
                    adapter.updateData(attendanceList, datesList);
                    showData();
                    updateSummary(jsonObject);
                    
                    Log.d(TAG, "Loaded " + attendanceList.size() + " staff attendance records");
                } else {
                    showNoData();
                    Log.d(TAG, "No staff attendance data found");
                }
            } else {
                String message = jsonObject.optString("message", "Unknown error");
                Log.e(TAG, "API returned error status. Message: " + message);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                showNoData();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing staff attendance response", e);
            Log.e(TAG, "Response that failed to parse: " + response);
            Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
            showNoData();
        }
    }

    private void updateSummary(JSONObject jsonObject) {
        try {
            int totalRecords = jsonObject.optInt("total_records", attendanceList.size());
            summaryTv.setText("Total Records: " + totalRecords);

            // Display selected month and year
            StringBuilder periodText = new StringBuilder();
            boolean hasPeriod = false;

            if (selectedMonth != null && !selectedMonth.isEmpty() && !selectedMonth.equals("All Months")) {
                periodText.append(selectedMonth);
                hasPeriod = true;
            }

            if (selectedYear != null && !selectedYear.isEmpty() && !selectedYear.equals("All Years")) {
                if (hasPeriod) {
                    periodText.append(" ");
                }
                periodText.append(selectedYear);
                hasPeriod = true;
            }

            if (hasPeriod) {
                periodTv.setText("Period: " + periodText.toString());
                periodTv.setVisibility(View.VISIBLE);
                Log.d(TAG, "Displaying period: " + periodText.toString());
            } else {
                periodTv.setVisibility(View.GONE);
                Log.d(TAG, "No specific period selected - showing all data");
            }

            // Show role filter if applied
            if (selectedRole != null && !selectedRole.isEmpty() && !selectedRole.equals("All Roles")) {
                filtersAppliedTv.setText("Role: " + selectedRole);
                filtersAppliedTv.setVisibility(View.VISIBLE);
            } else {
                filtersAppliedTv.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error updating summary", e);
        }
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        attendanceRecyclerView.setVisibility(View.GONE);
        nodataLayout.setVisibility(View.GONE);
        summaryCard.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void showData() {
        attendanceRecyclerView.setVisibility(View.VISIBLE);
        summaryCard.setVisibility(View.VISIBLE);
        nodataLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void showNoData() {
        nodataLayout.setVisibility(View.VISIBLE);
        attendanceRecyclerView.setVisibility(View.GONE);
        summaryCard.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void showInitialState() {
        // Hide all content initially - user must click Generate Report
        attendanceRecyclerView.setVisibility(View.GONE);
        summaryCard.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        nodataLayout.setVisibility(View.GONE);
        
        // Clear filters display
        filtersAppliedTv.setVisibility(View.GONE);
        
        Log.d(TAG, "Initial state shown - waiting for user to generate report");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_rightleft, R.anim.no_animation);
    }
}
