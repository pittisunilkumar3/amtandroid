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
import com.qdocs.ssre241123.adapters.GuardianReportAdapter;
import com.qdocs.ssre241123.model.GuardianReportModel;
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
 * Activity for displaying Guardian Report
 * Shows student guardian information including father, mother, and guardian details
 */
public class GuardianReportActivity extends TeacherReportDetailActivity {

    private static final String TAG = "GuardianReportActivity";
    
    private RecyclerView reportContentRecyclerView;
    private GuardianReportAdapter adapter;
    private List<GuardianReportModel> guardianList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize RecyclerView
        reportContentRecyclerView = getReportContentRecyclerView();
        reportContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Initialize list and adapter
        guardianList = new ArrayList<>();
        adapter = new GuardianReportAdapter(this, guardianList);
        reportContentRecyclerView.setAdapter(adapter);
        
        Log.d(TAG, "GuardianReportActivity initialized");
    }

    @Override
    protected void loadReportData() {
        String classId = getSelectedClassId();
        String sectionId = getSelectedSectionId();

        Log.d(TAG, "loadReportData called");
        Log.d(TAG, "Class ID: " + classId);
        Log.d(TAG, "Section ID: " + sectionId);

        // For guardian report, filters are optional
        // If any filter is null, we'll send it as null to get all records
        
        showLoading();
        fetchGuardianReport(classId, sectionId);
    }

    private void fetchGuardianReport(String classId, String sectionId) {
        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + Constants.guardianReportFilterUrl;

        Log.d(TAG, "=== API Request Details ===");
        Log.d(TAG, "Base URL: " + baseUrl);
        Log.d(TAG, "Full API URL: " + url);
        Log.d(TAG, "Class ID: " + classId);
        Log.d(TAG, "Section ID: " + sectionId);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoading();
                        Log.d(TAG, "=== API Response ===");
                        Log.d(TAG, "Response: " + response);
                        parseGuardianResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideLoading();
                        
                        String errorMessage = "Error loading guardian report";
                        if (error.networkResponse != null) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                Log.e(TAG, "Error Response: " + responseBody);
                                errorMessage = "Server Error: " + error.networkResponse.statusCode;
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }

                        Log.e(TAG, "Error Details: " + error.toString());
                        error.printStackTrace();

                        Toast.makeText(GuardianReportActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        showNoData();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", "smartschool");
                headers.put("Auth-Key", "schoolAdmin@");
                headers.put("Content-Type", "application/json");
                Log.d(TAG, "Headers: " + headers.toString());
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject jsonBody = new JSONObject();
                    
                    // Add filters only if they are not null
                    if (classId != null && !classId.isEmpty()) {
                        jsonBody.put("class_id", Integer.parseInt(classId));
                    }
                    if (sectionId != null && !sectionId.isEmpty()) {
                        jsonBody.put("section_id", Integer.parseInt(sectionId));
                    }
                    
                    String requestBody = jsonBody.toString();
                    Log.d(TAG, "Request Body: " + requestBody);
                    
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseGuardianResponse(String response) {
        try {
            Log.d(TAG, "=== Parsing Response ===");
            JSONObject jsonResponse = new JSONObject(response);
            
            // Check status - API returns integer 1 for success
            int status = jsonResponse.optInt("status", 0);
            Log.d(TAG, "Status: " + status);
            
            if (status == 1) {
                JSONArray dataArray = jsonResponse.optJSONArray("data");
                
                if (dataArray == null) {
                    Log.e(TAG, "Data array is null");
                    showNoData();
                    Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                Log.d(TAG, "Data array length: " + dataArray.length());
                guardianList.clear();
                
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject studentObj = dataArray.getJSONObject(i);
                    
                    Log.d(TAG, "Processing student " + (i+1) + ": " + studentObj.toString());
                    
                    GuardianReportModel guardian = new GuardianReportModel();
                    guardian.setId(studentObj.optString("id", ""));
                    guardian.setAdmissionNo(studentObj.optString("admission_no", ""));
                    guardian.setFirstname(studentObj.optString("firstname", ""));
                    guardian.setMiddlename(studentObj.optString("middlename", ""));
                    guardian.setLastname(studentObj.optString("lastname", ""));
                    guardian.setClassId(studentObj.optString("class_id", ""));
                    guardian.setClassName(studentObj.optString("class", ""));
                    guardian.setSectionId(studentObj.optString("section_id", ""));
                    guardian.setSectionName(studentObj.optString("section", ""));
                    guardian.setMobileno(studentObj.optString("mobileno", ""));
                    guardian.setGuardianName(studentObj.optString("guardian_name", ""));
                    guardian.setGuardianRelation(studentObj.optString("guardian_relation", ""));
                    guardian.setGuardianPhone(studentObj.optString("guardian_phone", ""));
                    guardian.setFatherName(studentObj.optString("father_name", ""));
                    guardian.setFatherPhone(studentObj.optString("father_phone", ""));
                    guardian.setMotherName(studentObj.optString("mother_name", ""));
                    guardian.setMotherPhone(studentObj.optString("mother_phone", ""));
                    guardian.setIsActive(studentObj.optString("is_active", ""));
                    
                    Log.d(TAG, "Guardian Name: " + guardian.getGuardianName());
                    Log.d(TAG, "Father Name: " + guardian.getFatherName());
                    Log.d(TAG, "Mother Name: " + guardian.getMotherName());
                    
                    guardianList.add(guardian);
                }
                
                Log.d(TAG, "Total records parsed: " + guardianList.size());
                
                if (guardianList.isEmpty()) {
                    Log.d(TAG, "List is empty, showing no data");
                    showNoData();
                    Toast.makeText(this, "No guardian records found", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "Showing content with " + guardianList.size() + " records");
                    showContent();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Loaded " + guardianList.size() + " records", Toast.LENGTH_SHORT).show();
                }
            } else {
                String message = jsonResponse.optString("message", "No data found");
                Log.e(TAG, "API returned error status. Message: " + message);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                showNoData();
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSON Parsing Error", e);
            Log.e(TAG, "Response that failed to parse: " + response);
            Toast.makeText(this, "Error parsing data: " + e.getMessage(), Toast.LENGTH_LONG).show();
            showNoData();
        }
    }
}

