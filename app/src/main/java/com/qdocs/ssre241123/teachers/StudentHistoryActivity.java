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
import com.qdocs.ssre241123.adapters.StudentHistoryAdapter;
import com.qdocs.ssre241123.model.StudentHistoryModel;
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

public class StudentHistoryActivity extends TeacherReportDetailActivity {

    private static final String TAG = "StudentHistoryActivity";
    private RecyclerView reportContentRecyclerView;
    private StudentHistoryAdapter adapter;
    private List<StudentHistoryModel> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize RecyclerView
        reportContentRecyclerView = findViewById(com.qdocs.ssre241123.R.id.report_content_recyclerView);
        reportContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        studentList = new ArrayList<>();
        adapter = new StudentHistoryAdapter(this, studentList);
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

        if (sessionId == null || classId == null || sectionId == null) {
            Log.e(TAG, "One or more filters are null");
            Toast.makeText(this, "Please select all filters", Toast.LENGTH_SHORT).show();
            hideLoading();
            return;
        }

        showLoading();
        fetchStudentHistory(sessionId, classId, sectionId);
    }

    private void fetchStudentHistory(String sessionId, String classId, String sectionId) {
        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + "admission-report/filter";

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
                        parseStudentHistoryResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideLoading();
                        Log.e(TAG, "=== API Error ===");

                        String errorMessage = "Error loading student history";

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

                        Toast.makeText(StudentHistoryActivity.this, errorMessage, Toast.LENGTH_LONG).show();
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
                    jsonBody.put("class_id", Integer.parseInt(classId));
                    jsonBody.put("session_id", Integer.parseInt(sessionId));
                    
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

    private void parseStudentHistoryResponse(String response) {
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

                studentList.clear();

                if (dataArray != null && dataArray.length() > 0) {
                    Log.d(TAG, "Processing " + dataArray.length() + " students");

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject studentObj = dataArray.getJSONObject(i);

                        StudentHistoryModel student = new StudentHistoryModel();
                        student.setId(studentObj.optString("id", ""));
                        student.setAdmissionNo(studentObj.optString("admission_no", ""));
                        student.setAdmissionDate(studentObj.optString("admission_date", ""));
                        student.setFirstname(studentObj.optString("firstname", ""));
                        student.setMiddlename(studentObj.optString("middlename", ""));
                        student.setLastname(studentObj.optString("lastname", ""));
                        student.setClassId(studentObj.optString("class_id", ""));
                        student.setClassName(studentObj.optString("class", ""));
                        student.setSectionId(studentObj.optString("section_id", ""));
                        student.setSectionName(studentObj.optString("section", ""));
                        student.setSessionId(studentObj.optString("session_id", ""));
                        student.setSessionName(studentObj.optString("session", ""));
                        student.setMobileno(studentObj.optString("mobileno", ""));
                        student.setGuardianName(studentObj.optString("guardian_name", ""));
                        student.setGuardianRelation(studentObj.optString("guardian_relation", ""));
                        student.setGuardianPhone(studentObj.optString("guardian_phone", ""));
                        student.setIsActive(studentObj.optString("is_active", "yes"));

                        studentList.add(student);

                        if (i == 0) {
                            Log.d(TAG, "First Student: " + student.getFullName());
                        }
                    }

                    Log.d(TAG, "Student list size: " + studentList.size());
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
                String message = jsonObject.optString("message", "Failed to load student history");
                Log.e(TAG, "API returned status 0: " + message);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                showNoData();
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSON Parsing Error", e);
            e.printStackTrace();
            Toast.makeText(this, "Error parsing student history data: " + e.getMessage(), Toast.LENGTH_LONG).show();
            showNoData();
        }
    }
}

