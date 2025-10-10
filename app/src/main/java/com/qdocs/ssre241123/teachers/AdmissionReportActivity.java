package com.qdocs.ssre241123.teachers;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.ssre241123.adapters.AdmissionReportAdapter;
import com.qdocs.ssre241123.model.AdmissionReportModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Activity for displaying Admission Report
 * Extends TeacherReportDetailActivity to inherit filter dropdown functionality
 * 
 * API Endpoint: POST /admission-report/filter
 * 
 * Features:
 * - Filter by session, class, and year
 * - Display admission records with comprehensive information
 * - Support for multi-select filters (class_id, year)
 * - Graceful handling of null/empty filters
 */
public class AdmissionReportActivity extends TeacherReportDetailActivity {

    private static final String TAG = "AdmissionReportActivity";
    private RecyclerView reportContentRecyclerView;
    private AdmissionReportAdapter adapter;
    private List<AdmissionReportModel> admissionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "onCreate: Initializing Admission Report Activity");
        
        // Initialize RecyclerView
        reportContentRecyclerView = findViewById(com.qdocs.ssre241123.R.id.report_content_recyclerView);
        reportContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Initialize data list and adapter
        admissionList = new ArrayList<>();
        adapter = new AdmissionReportAdapter(this, admissionList);
        reportContentRecyclerView.setAdapter(adapter);
        
        Log.d(TAG, "onCreate: RecyclerView and adapter initialized");
    }

    @Override
    protected void loadReportData() {
        Log.d(TAG, "=== loadReportData called ===");
        
        // Get filter values from parent activity
        String sessionId = getSelectedSessionId();
        String classId = getSelectedClassId();
        String sectionId = getSelectedSectionId();

        Log.d(TAG, "Filters - Session: " + sessionId + ", Class: " + classId + ", Section: " + sectionId);
        
        // Validate required filters
        if (sessionId == null || classId == null || sectionId == null) {
            Log.e(TAG, "One or more required filters are null");
            Toast.makeText(this, "Please select all filters (Session, Class, Section)", Toast.LENGTH_SHORT).show();
            hideLoading();
            return;
        }

        // Show loading state
        showLoading();
        
        // Fetch data from API
        fetchAdmissionReport(sessionId, classId, sectionId);
    }

    /**
     * Fetches admission report data from the API
     * 
     * @param sessionId Session ID filter
     * @param classId Class ID filter
     * @param sectionId Section ID filter (not used in API but required for UI)
     */
    private void fetchAdmissionReport(String sessionId, String classId, String sectionId) {
        Log.d(TAG, "=== Fetching Admission Report ===");
        
        // Build API URL
        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + Constants.admissionReportFilterUrl;

        Log.d(TAG, "Base URL: " + baseUrl);
        Log.d(TAG, "Full API URL: " + url);
        Log.d(TAG, "Session ID: " + sessionId);
        Log.d(TAG, "Class ID: " + classId);

        // Create Volley request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "=== API Response Received ===");
                        Log.d(TAG, "Response Length: " + response.length());
                        Log.d(TAG, "Response: " + response);
                        hideLoading();
                        parseAdmissionReportResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideLoading();
                        Log.e(TAG, "=== API Error ===");

                        String errorMessage = "Error loading admission report";

                        if (error.networkResponse != null) {
                            Log.e(TAG, "Status Code: " + error.networkResponse.statusCode);

                            if (error.networkResponse.data != null) {
                                try {
                                    String errorResponse = new String(error.networkResponse.data, "UTF-8");
                                    Log.e(TAG, "Error Response: " + errorResponse);
                                    errorMessage = "Server Error: " + errorResponse;
                                } catch (UnsupportedEncodingException e) {
                                    Log.e(TAG, "Error parsing error response", e);
                                }
                            }
                        } else {
                            Log.e(TAG, "Network error - no response from server");
                            errorMessage = "Network error. Please check your internet connection.";
                        }

                        Log.e(TAG, "Error Details: " + error.toString());
                        error.printStackTrace();

                        Toast.makeText(AdmissionReportActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        showNoData();
                    }
                }) {
            
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", "application/json");

                Log.d(TAG, "=== Request Headers ===");
                Log.d(TAG, "Client-Service: " + Constants.clientService);
                Log.d(TAG, "Auth-Key: " + Constants.authKey);
                Log.d(TAG, "Content-Type: application/json");

                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject jsonBody = new JSONObject();
                    
                    // Add class_id filter (required)
                    jsonBody.put("class_id", Integer.parseInt(classId));
                    
                    // Add session_id filter (required)
                    jsonBody.put("session_id", Integer.parseInt(sessionId));
                    
                    String requestBody = jsonBody.toString();
                    Log.d(TAG, "=== Request Body ===");
                    Log.d(TAG, requestBody);
                    
                    return requestBody.getBytes("UTF-8");
                } catch (JSONException | UnsupportedEncodingException e) {
                    Log.e(TAG, "Error creating request body", e);
                    return null;
                }
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        // Add request to queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /**
     * Parses the API response and updates the UI
     * 
     * @param response JSON response from API
     */
    private void parseAdmissionReportResponse(String response) {
        try {
            Log.d(TAG, "=== Parsing Response ===");
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.optInt("status", 0);

            Log.d(TAG, "Status: " + status);

            if (status == 1) {
                JSONArray dataArray = jsonObject.optJSONArray("data");
                int totalRecords = jsonObject.optInt("total_records", 0);

                Log.d(TAG, "Total Records: " + totalRecords);
                Log.d(TAG, "Data Array Length: " + (dataArray != null ? dataArray.length() : 0));

                admissionList.clear();

                if (dataArray != null && dataArray.length() > 0) {
                    Log.d(TAG, "Processing " + dataArray.length() + " admission records");

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject admissionObj = dataArray.getJSONObject(i);

                        AdmissionReportModel admission = new AdmissionReportModel();
                        admission.setId(admissionObj.optString("id", ""));
                        admission.setAdmissionNo(admissionObj.optString("admission_no", ""));
                        admission.setAdmissionDate(admissionObj.optString("admission_date", ""));
                        admission.setFirstname(admissionObj.optString("firstname", ""));
                        admission.setMiddlename(admissionObj.optString("middlename", ""));
                        admission.setLastname(admissionObj.optString("lastname", ""));
                        admission.setClassId(admissionObj.optString("class_id", ""));
                        admission.setClassName(admissionObj.optString("class", ""));
                        admission.setSectionId(admissionObj.optString("section_id", ""));
                        admission.setSectionName(admissionObj.optString("section", ""));
                        admission.setSessionId(admissionObj.optString("session_id", ""));
                        admission.setSessionName(admissionObj.optString("session", ""));
                        admission.setMobileno(admissionObj.optString("mobileno", ""));
                        admission.setGuardianName(admissionObj.optString("guardian_name", ""));
                        admission.setGuardianRelation(admissionObj.optString("guardian_relation", ""));
                        admission.setGuardianPhone(admissionObj.optString("guardian_phone", ""));
                        admission.setIsActive(admissionObj.optString("is_active", "yes"));

                        admissionList.add(admission);

                        if (i == 0) {
                            Log.d(TAG, "First Admission: " + admission.getFullName());
                        }
                    }

                    Log.d(TAG, "Admission list size: " + admissionList.size());
                    Log.d(TAG, "Notifying adapter...");
                    adapter.notifyDataSetChanged();

                    Log.d(TAG, "Showing content...");
                    showContent();

                    String message = "Found " + totalRecords + " admission record(s)";
                    Log.d(TAG, "Success message: " + message);
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Log.w(TAG, "Data array is null or empty");
                    showNoData();
                    Toast.makeText(this, "No admission records found for selected filters", Toast.LENGTH_SHORT).show();
                }
            } else {
                String message = jsonObject.optString("message", "Failed to load admission report");
                Log.e(TAG, "API returned status 0: " + message);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                showNoData();
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSON Parsing Error", e);
            e.printStackTrace();
            Toast.makeText(this, "Error parsing admission report data: " + e.getMessage(), Toast.LENGTH_LONG).show();
            showNoData();
        }
    }
}

