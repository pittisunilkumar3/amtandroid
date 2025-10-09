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
import com.qdocs.ssre241123.adapters.ClassSectionReportAdapter;
import com.qdocs.ssre241123.model.ClassSectionReportModel;
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

public class ClassSectionReportActivity extends TeacherReportDetailActivity {

    private static final String TAG = "ClassSectionReport";
    private RecyclerView reportContentRecyclerView;
    private ClassSectionReportAdapter adapter;
    private List<ClassSectionReportModel> classSectionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize RecyclerView
        reportContentRecyclerView = findViewById(com.qdocs.ssre241123.R.id.report_content_recyclerView);
        reportContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        classSectionList = new ArrayList<>();
        adapter = new ClassSectionReportAdapter(this, classSectionList);
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

        // For Class Section Report, we can load data with or without filters
        // If no filters selected, load all class sections
        showLoading();
        fetchClassSectionReport(sessionId, classId, sectionId);
    }

    private void fetchClassSectionReport(String sessionId, String classId, String sectionId) {
        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + "class-section-report/filter";

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
                        Log.d(TAG, "=== API Response Received ===");
                        Log.d(TAG, "Response Length: " + response.length());
                        Log.d(TAG, "Response: " + response);
                        hideLoading();
                        parseClassSectionReportResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideLoading();
                        Log.e(TAG, "=== API Error ===");

                        String errorMessage = "Error loading class section report";

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

                        Toast.makeText(ClassSectionReportActivity.this, errorMessage, Toast.LENGTH_LONG).show();
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
                    
                    // Add filters only if they are selected
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

    private void parseClassSectionReportResponse(String response) {
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

                classSectionList.clear();

                if (dataArray != null && dataArray.length() > 0) {
                    Log.d(TAG, "Processing " + dataArray.length() + " class sections");

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject classSectionObj = dataArray.getJSONObject(i);

                        ClassSectionReportModel classSection = new ClassSectionReportModel();
                        classSection.setId(classSectionObj.optString("id", ""));
                        classSection.setClassId(classSectionObj.optString("class_id", ""));
                        classSection.setSectionId(classSectionObj.optString("section_id", ""));
                        classSection.setClassName(classSectionObj.optString("class", ""));
                        classSection.setSectionName(classSectionObj.optString("section", ""));
                        classSection.setStudentCount(classSectionObj.optString("student_count", "0"));
                        classSection.setIsActive("yes"); // Default to active

                        classSectionList.add(classSection);

                        if (i == 0) {
                            Log.d(TAG, "First Class Section: " + classSection.getClassSection());
                        }
                    }

                    Log.d(TAG, "Class section list size: " + classSectionList.size());
                    Log.d(TAG, "Notifying adapter...");
                    adapter.notifyDataSetChanged();

                    Log.d(TAG, "Showing content...");
                    showContent();

                    // Parse summary if available
                    JSONObject summary = jsonObject.optJSONObject("summary");
                    if (summary != null) {
                        int totalClasses = summary.optInt("total_classes", 0);
                        int totalSections = summary.optInt("total_sections", 0);
                        int totalStudents = summary.optInt("total_students", 0);
                        
                        String message = "Found " + totalClasses + " class(es), " + 
                                       totalSections + " section(s) with " + 
                                       totalStudents + " student(s)";
                        Log.d(TAG, "Success message: " + message);
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    } else {
                        String message = "Found " + totalRecords + " class section(s)";
                        Log.d(TAG, "Success message: " + message);
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.w(TAG, "Data array is null or empty");
                    showNoData();
                    Toast.makeText(this, "No class sections found for selected filters", Toast.LENGTH_SHORT).show();
                }
            } else {
                String message = jsonObject.optString("message", "Failed to load class section report");
                Log.e(TAG, "API returned status 0: " + message);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                showNoData();
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSON Parsing Error", e);
            e.printStackTrace();
            Toast.makeText(this, "Error parsing class section data: " + e.getMessage(), Toast.LENGTH_LONG).show();
            showNoData();
        }
    }
}

