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
import com.qdocs.ssre241123.adapters.ParentLoginAdapter;
import com.qdocs.ssre241123.model.ParentLoginModel;
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

public class ParentLoginActivity extends TeacherReportDetailActivity {

    private static final String TAG = "ParentLoginActivity";
    private RecyclerView reportContentRecyclerView;
    private ParentLoginAdapter adapter;
    private List<ParentLoginModel> parentLoginList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize RecyclerView
        reportContentRecyclerView = findViewById(R.id.report_content_recyclerView);
        reportContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        parentLoginList = new ArrayList<>();
        adapter = new ParentLoginAdapter(this, parentLoginList);
        reportContentRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void loadReportData() {
        String sessionId = getSelectedSessionId();
        String classId = getSelectedClassId();
        String sectionId = getSelectedSectionId();

        Log.d(TAG, "loadReportData called");
        Log.d(TAG, "Session ID: " + sessionId);
        Log.d(TAG, "Class ID: " + classId);
        Log.d(TAG, "Section ID: " + sectionId);

        // For parent login report, filters are optional
        // If any filter is null, we'll send it as null to get all records
        
        showLoading();
        fetchParentLoginReport(sessionId, classId, sectionId);
    }

    private void fetchParentLoginReport(String sessionId, String classId, String sectionId) {
        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + Constants.parentLoginDetailReportFilterUrl;

        Log.d(TAG, "=== API Request Details ===");
        Log.d(TAG, "Base URL: " + baseUrl);
        Log.d(TAG, "Full API URL: " + url);
        Log.d(TAG, "Session ID: " + sessionId);
        Log.d(TAG, "Class ID: " + classId);
        Log.d(TAG, "Section ID: " + sectionId);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoading();
                        Log.d(TAG, "=== API Response ===");
                        Log.d(TAG, "Response: " + response);
                        parseParentLoginResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideLoading();
                        
                        String errorMessage = "Error loading parent login report";
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

                        Toast.makeText(ParentLoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
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
                    
                    // Add filters only if they are not null
                    if (sessionId != null && !sessionId.isEmpty()) {
                        jsonBody.put("session_id", Integer.parseInt(sessionId));
                    }
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

    private void parseParentLoginResponse(String response) {
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
                parentLoginList.clear();

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject studentObj = dataArray.getJSONObject(i);

                    Log.d(TAG, "Processing student " + (i+1) + ": " + studentObj.toString());

                    ParentLoginModel parentLogin = new ParentLoginModel();
                    parentLogin.setId(studentObj.optString("id", ""));
                    parentLogin.setAdmissionNo(studentObj.optString("admission_no", ""));
                    parentLogin.setRollNo(studentObj.optString("roll_no", ""));
                    parentLogin.setFirstname(studentObj.optString("firstname", ""));
                    parentLogin.setMiddlename(studentObj.optString("middlename", ""));
                    parentLogin.setLastname(studentObj.optString("lastname", ""));
                    parentLogin.setClassName(studentObj.optString("class", ""));
                    parentLogin.setSectionName(studentObj.optString("section", ""));
                    parentLogin.setFatherName(studentObj.optString("father_name", ""));
                    parentLogin.setGuardianName(studentObj.optString("guardian_name", ""));
                    parentLogin.setGuardianPhone(studentObj.optString("guardian_phone", ""));
                    parentLogin.setGuardianRelation(studentObj.optString("guardian_relation", ""));
                    parentLogin.setMobileno(studentObj.optString("mobileno", ""));
                    parentLogin.setEmail(studentObj.optString("email", ""));
                    parentLogin.setParentUsername(studentObj.optString("parent_username", ""));
                    parentLogin.setParentPassword(studentObj.optString("parent_password", ""));
                    parentLogin.setIsActive(studentObj.optString("is_active", ""));

                    Log.d(TAG, "Parent Username: " + parentLogin.getParentUsername());
                    Log.d(TAG, "Parent Password: " + parentLogin.getParentPassword());

                    parentLoginList.add(parentLogin);
                }

                Log.d(TAG, "Total records parsed: " + parentLoginList.size());

                if (parentLoginList.isEmpty()) {
                    Log.d(TAG, "List is empty, showing no data");
                    showNoData();
                    Toast.makeText(this, "No parent login records found", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "Showing content with " + parentLoginList.size() + " records");
                    showContent();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Loaded " + parentLoginList.size() + " records", Toast.LENGTH_SHORT).show();
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

