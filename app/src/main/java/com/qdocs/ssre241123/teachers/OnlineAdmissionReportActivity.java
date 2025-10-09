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
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.OnlineAdmissionAdapter;
import com.qdocs.ssre241123.model.OnlineAdmissionModel;
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

public class OnlineAdmissionReportActivity extends TeacherReportDetailActivity {

    private static final String TAG = "OnlineAdmissionReport";
    private RecyclerView reportContentRecyclerView;
    private OnlineAdmissionAdapter adapter;
    private List<OnlineAdmissionModel> admissionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize RecyclerView
        reportContentRecyclerView = findViewById(R.id.report_content_recyclerView);
        reportContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        admissionList = new ArrayList<>();
        adapter = new OnlineAdmissionAdapter(this, admissionList);
        reportContentRecyclerView.setAdapter(adapter);
    }

    @Override
    protected String getReportTitle() {
        return "Online Admission Report";
    }

    @Override
    protected void loadReportData() {
        Log.d(TAG, "loadReportData called");
        
        // Get filter values from parent activity
        String sessionId = getSelectedSessionId();
        String classId = getSelectedClassId();
        String sectionId = getSelectedSectionId();
        
        Log.d(TAG, "Filters - Session: " + sessionId + ", Class: " + classId + ", Section: " + sectionId);
        
        // Show loading state
        showLoading();
        
        // Fetch data from API
        fetchOnlineAdmissions(sessionId, classId, sectionId);
    }

    private void fetchOnlineAdmissions(String sessionId, String classId, String sectionId) {
        Log.d(TAG, "=== Fetching Online Admissions ===");
        
        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + "online-admission/filter";
        
        Log.d(TAG, "=== API Request Details ===");
        Log.d(TAG, "URL: " + url);
        Log.d(TAG, "Method: POST");
        
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "=== API Response Received ===");
                        Log.d(TAG, "Response: " + response);
                        parseOnlineAdmissionResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "=== API Error ===");
                        Log.e(TAG, "Error: " + error.toString());
                        
                        if (error.networkResponse != null) {
                            Log.e(TAG, "Status Code: " + error.networkResponse.statusCode);
                            try {
                                String errorBody = new String(error.networkResponse.data, "UTF-8");
                                Log.e(TAG, "Error Body: " + errorBody);
                            } catch (UnsupportedEncodingException e) {
                                Log.e(TAG, "Error parsing error body", e);
                            }
                        }
                        
                        showError("Failed to load online admissions. Please try again.");
                        Toast.makeText(OnlineAdmissionReportActivity.this, 
                                "Network error: " + error.getMessage(), 
                                Toast.LENGTH_SHORT).show();
                    }
                }) {
            
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", "application/json");
                
                Log.d(TAG, "Headers: " + headers.toString());
                return headers;
            }
            
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject jsonBody = new JSONObject();
                    
                    // Add filters only if they are selected (not empty)
                    if (classId != null && !classId.isEmpty()) {
                        jsonBody.put("class_id", Integer.parseInt(classId));
                        Log.d(TAG, "Added class_id filter: " + classId);
                    }
                    
                    if (sectionId != null && !sectionId.isEmpty()) {
                        jsonBody.put("section_id", Integer.parseInt(sectionId));
                        Log.d(TAG, "Added section_id filter: " + sectionId);
                    }
                    
                    String requestBody = jsonBody.toString();
                    Log.d(TAG, "Request Body: " + requestBody);
                    
                    return requestBody.getBytes("UTF-8");
                } catch (Exception e) {
                    Log.e(TAG, "Error creating request body", e);
                    return null;
                }
            }
            
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
        
        requestQueue.add(stringRequest);
    }

    private void parseOnlineAdmissionResponse(String response) {
        Log.d(TAG, "=== Parsing Response ===");
        
        try {
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.optInt("status", 0);
            String message = jsonObject.optString("message", "");
            
            Log.d(TAG, "Status: " + status);
            Log.d(TAG, "Message: " + message);
            
            if (status == 1) {
                // Success
                JSONArray dataArray = jsonObject.optJSONArray("data");
                
                if (dataArray != null && dataArray.length() > 0) {
                    Log.d(TAG, "Data array length: " + dataArray.length());
                    
                    admissionList.clear();
                    
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject admissionObj = dataArray.getJSONObject(i);
                        OnlineAdmissionModel admission = new OnlineAdmissionModel();
                        
                        // Parse basic info
                        admission.setId(admissionObj.optString("id", ""));
                        admission.setReferenceNo(admissionObj.optString("reference_no", ""));
                        admission.setAdmissionNo(admissionObj.optString("admission_no", ""));
                        admission.setAdmissionDate(admissionObj.optString("admission_date", ""));
                        
                        // Parse name fields
                        admission.setFullName(admissionObj.optString("full_name", ""));
                        admission.setFirstname(admissionObj.optString("firstname", ""));
                        admission.setMiddlename(admissionObj.optString("middlename", ""));
                        admission.setLastname(admissionObj.optString("lastname", ""));
                        
                        // Parse personal info
                        admission.setDob(admissionObj.optString("dob", ""));
                        admission.setGender(admissionObj.optString("gender", ""));
                        admission.setEmail(admissionObj.optString("email", ""));
                        admission.setMobileno(admissionObj.optString("mobileno", ""));
                        
                        // Parse parent info
                        admission.setFatherName(admissionObj.optString("father_name", ""));
                        admission.setFatherPhone(admissionObj.optString("father_phone", ""));
                        admission.setMotherName(admissionObj.optString("mother_name", ""));
                        admission.setMotherPhone(admissionObj.optString("mother_phone", ""));
                        admission.setGuardianName(admissionObj.optString("guardian_name", ""));
                        admission.setGuardianPhone(admissionObj.optString("guardian_phone", ""));
                        
                        // Parse address
                        admission.setCurrentAddress(admissionObj.optString("current_address", ""));
                        admission.setPermanentAddress(admissionObj.optString("permanent_address", ""));
                        
                        // Parse class info
                        JSONObject classInfo = admissionObj.optJSONObject("class_info");
                        if (classInfo != null) {
                            admission.setClassId(classInfo.optString("class_id", ""));
                            admission.setClassName(classInfo.optString("class_name", ""));
                            admission.setSectionId(classInfo.optString("section_id", ""));
                            admission.setSectionName(classInfo.optString("section_name", ""));
                        }
                        
                        // Parse additional info
                        admission.setCategory(admissionObj.optString("category", ""));
                        admission.setHouseName(admissionObj.optString("house_name", ""));
                        admission.setBloodGroup(admissionObj.optString("blood_group", ""));
                        admission.setReligion(admissionObj.optString("religion", ""));
                        admission.setCast(admissionObj.optString("cast", ""));
                        
                        // Parse status fields
                        admission.setIsEnroll(admissionObj.optString("is_enroll", "0"));
                        admission.setFormStatus(admissionObj.optString("form_status", ""));
                        admission.setPaidStatus(admissionObj.optString("paid_status", "0"));
                        
                        // Parse timestamps
                        admission.setCreatedAt(admissionObj.optString("created_at", ""));
                        admission.setUpdatedAt(admissionObj.optString("updated_at", ""));
                        
                        admissionList.add(admission);
                        
                        Log.d(TAG, "Parsed admission: " + admission.getFullName() + 
                                " (Ref: " + admission.getReferenceNo() + ")");
                    }
                    
                    Log.d(TAG, "Total admissions parsed: " + admissionList.size());
                    
                    // Update UI
                    runOnUiThread(() -> {
                        adapter.notifyDataSetChanged();
                        showContent();
                    });
                    
                } else {
                    Log.d(TAG, "No data found in response");
                    showNoData("No online admissions found");
                }
                
            } else {
                // API returned error status
                Log.e(TAG, "API returned error status: " + status);
                showError("Error: " + message);
            }
            
        } catch (JSONException e) {
            Log.e(TAG, "JSON parsing error", e);
            showError("Failed to parse response data");
        }
    }
}

