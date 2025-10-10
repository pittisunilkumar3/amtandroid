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

        Log.d(TAG, "onCreate called");

        try {
            // Initialize RecyclerView
            reportContentRecyclerView = findViewById(R.id.report_content_recyclerView);

            if (reportContentRecyclerView == null) {
                Log.e(TAG, "reportContentRecyclerView is null - layout issue");
                Toast.makeText(this, "Error initializing view", Toast.LENGTH_SHORT).show();
                return;
            }

            reportContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Initialize data list
            admissionList = new ArrayList<>();

            // Initialize adapter
            adapter = new OnlineAdmissionAdapter(this, admissionList);

            if (adapter == null) {
                Log.e(TAG, "Failed to create adapter");
                Toast.makeText(this, "Error initializing adapter", Toast.LENGTH_SHORT).show();
                return;
            }

            reportContentRecyclerView.setAdapter(adapter);

            Log.d(TAG, "RecyclerView and adapter initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate", e);
            Toast.makeText(this, "Error initializing screen: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void loadReportData() {
        Log.d(TAG, "loadReportData called");

        // Get filter values from parent activity (all filters are optional)
        String sessionId = getSelectedSessionId();
        String classId = getSelectedClassId();
        String sectionId = getSelectedSectionId();

        Log.d(TAG, "Filters - Session: " + sessionId + ", Class: " + classId + ", Section: " + sectionId);
        Log.d(TAG, "Note: All filters are optional. API will return all records if no filters are selected.");

        // Show loading state
        showLoading();

        // Fetch data from API (filters are optional)
        fetchOnlineAdmissions(sessionId, classId, sectionId);
    }

    private void fetchOnlineAdmissions(String sessionId, String classId, String sectionId) {
        Log.d(TAG, "=== Fetching Online Admissions ===");
        Log.d(TAG, "Note: Filters are optional. Will send only selected filters to API.");

        // Use buildApiUrl() to ensure consistent URL construction with configured domain
        String url = Utility.buildApiUrl(getApplicationContext(), Constants.onlineAdmissionFilterUrl);

        // Validate URL
        if (url == null || url.isEmpty()) {
            Log.e(TAG, "Failed to build API URL");
            hideLoading();
            showNoData();
            Toast.makeText(this, "Configuration error: Invalid API URL", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "=== API Request Details ===");
        Log.d(TAG, "URL: " + url);
        Log.d(TAG, "Method: POST");
        Log.d(TAG, "Session ID: " + (sessionId != null && !sessionId.isEmpty() ? sessionId : "Not selected"));
        Log.d(TAG, "Class ID: " + (classId != null && !classId.isEmpty() ? classId : "Not selected"));
        Log.d(TAG, "Section ID: " + (sectionId != null && !sectionId.isEmpty() ? sectionId : "Not selected"));

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

                        String errorMessage = "Failed to load online admissions";

                        if (error.networkResponse != null) {
                            Log.e(TAG, "Status Code: " + error.networkResponse.statusCode);
                            try {
                                String errorBody = new String(error.networkResponse.data, "UTF-8");
                                Log.e(TAG, "Error Body: " + errorBody);

                                // Try to parse error response
                                try {
                                    JSONObject errorJson = new JSONObject(errorBody);
                                    String apiMessage = errorJson.optString("message", "");
                                    if (!apiMessage.isEmpty()) {
                                        errorMessage = apiMessage;
                                    }
                                } catch (JSONException je) {
                                    Log.e(TAG, "Error parsing error JSON", je);
                                }
                            } catch (UnsupportedEncodingException e) {
                                Log.e(TAG, "Error parsing error body", e);
                            }
                        } else if (error.getMessage() != null && !error.getMessage().isEmpty()) {
                            errorMessage = error.getMessage();
                        } else {
                            errorMessage = "Network error. Please check your internet connection.";
                        }

                        hideLoading();
                        showNoData();
                        Toast.makeText(OnlineAdmissionReportActivity.this,
                                errorMessage,
                                Toast.LENGTH_LONG).show();
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

                    // Add filters only if they are selected (all filters are optional)
                    // This matches the API example: {"class_id": 19, "gender": "Male", "is_enroll": "0"}

                    if (classId != null && !classId.isEmpty()) {
                        try {
                            int classIdInt = Integer.parseInt(classId);
                            jsonBody.put("class_id", classIdInt);
                            Log.d(TAG, "Added class_id filter: " + classIdInt);
                        } catch (NumberFormatException e) {
                            Log.w(TAG, "Invalid class_id format: " + classId + ", skipping this filter", e);
                            // Don't throw error, just skip this filter
                        }
                    } else {
                        Log.d(TAG, "class_id not selected, will fetch all classes");
                    }

                    if (sectionId != null && !sectionId.isEmpty()) {
                        try {
                            int sectionIdInt = Integer.parseInt(sectionId);
                            jsonBody.put("section_id", sectionIdInt);
                            Log.d(TAG, "Added section_id filter: " + sectionIdInt);
                        } catch (NumberFormatException e) {
                            Log.w(TAG, "Invalid section_id format: " + sectionId + ", skipping this filter", e);
                            // Don't throw error, just skip this filter
                        }
                    } else {
                        Log.d(TAG, "section_id not selected, will fetch all sections");
                    }

                    // Note: Additional filters like gender, is_enroll can be added here in the future
                    // Example: jsonBody.put("gender", "Male");
                    // Example: jsonBody.put("is_enroll", "0");

                    String requestBody = jsonBody.toString();
                    Log.d(TAG, "Request Body: " + requestBody);

                    if (jsonBody.length() == 0) {
                        Log.d(TAG, "No filters selected, sending empty body to fetch all records");
                    }

                    return requestBody.getBytes("UTF-8");
                } catch (JSONException e) {
                    Log.e(TAG, "JSON error creating request body", e);
                    throw new AuthFailureError("Failed to create request body: " + e.getMessage());
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "Encoding error creating request body", e);
                    throw new AuthFailureError("Failed to encode request body: " + e.getMessage());
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

        // Validate response is not null or empty
        if (response == null || response.trim().isEmpty()) {
            Log.e(TAG, "Response is null or empty");
            hideLoading();
            showNoData();
            Toast.makeText(this, "Received empty response from server", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Response length: " + response.length());

        try {
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.optInt("status", -1);
            String message = jsonObject.optString("message", "Unknown error");

            Log.d(TAG, "Status: " + status);
            Log.d(TAG, "Message: " + message);

            if (status == 1) {
                // Success - check for data
                JSONArray dataArray = jsonObject.optJSONArray("data");

                if (dataArray != null && dataArray.length() > 0) {
                    Log.d(TAG, "Data array length: " + dataArray.length());

                    // Clear existing data
                    if (admissionList == null) {
                        admissionList = new ArrayList<>();
                    }
                    admissionList.clear();
                    
                    for (int i = 0; i < dataArray.length(); i++) {
                        try {
                            JSONObject admissionObj = dataArray.getJSONObject(i);

                            // Skip null objects
                            if (admissionObj == null) {
                                Log.w(TAG, "Skipping null admission object at index " + i);
                                continue;
                            }

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
                            } else {
                                Log.w(TAG, "class_info is null for admission at index " + i);
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

                            Log.d(TAG, "Parsed admission " + (i + 1) + "/" + dataArray.length() + ": " +
                                    admission.getFullName() + " (Ref: " + admission.getReferenceNo() + ")");
                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing admission at index " + i, e);
                            // Continue with next item instead of failing completely
                        }
                    }
                    
                    Log.d(TAG, "Total admissions parsed successfully: " + admissionList.size());

                    // Verify we have data after parsing
                    if (admissionList.isEmpty()) {
                        Log.w(TAG, "Admission list is empty after parsing");
                        hideLoading();
                        showNoData();
                        Toast.makeText(this, "No valid admission records found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Update UI on main thread
                    runOnUiThread(() -> {
                        try {
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                                hideLoading();
                                showContent();
                                Log.d(TAG, "UI updated successfully with " + admissionList.size() + " admissions");
                            } else {
                                Log.e(TAG, "Adapter is null, cannot update UI");
                                hideLoading();
                                showNoData();
                                Toast.makeText(OnlineAdmissionReportActivity.this,
                                        "Error displaying data", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error updating UI", e);
                            hideLoading();
                            showNoData();
                            Toast.makeText(OnlineAdmissionReportActivity.this,
                                    "Error displaying data", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    // Data array is null or empty
                    Log.d(TAG, "No data found in response - data array is " +
                            (dataArray == null ? "null" : "empty"));
                    hideLoading();
                    showNoData();
                    Toast.makeText(this,
                            "No online admissions found for the selected filters",
                            Toast.LENGTH_LONG).show();
                }

            } else {
                // API returned error status
                Log.e(TAG, "API returned error status: " + status + ", message: " + message);
                hideLoading();
                showNoData();

                // Provide more specific error message
                String errorMsg = message;
                if (errorMsg == null || errorMsg.isEmpty() || errorMsg.equals("Unknown error")) {
                    errorMsg = "Failed to load online admissions. Please try again.";
                }
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            Log.e(TAG, "JSON parsing error: " + e.getMessage(), e);
            e.printStackTrace();
            hideLoading();
            showNoData();
            Toast.makeText(this,
                    "Error parsing server response. Please contact support.",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error parsing response: " + e.getMessage(), e);
            e.printStackTrace();
            hideLoading();
            showNoData();
            Toast.makeText(this,
                    "Unexpected error occurred. Please try again.",
                    Toast.LENGTH_LONG).show();
        }
    }
}

