package com.qdocs.ssre241123.teachers;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.ssre241123.adapters.StudentLoginAdapter;
import com.qdocs.ssre241123.model.StudentLoginModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Activity for displaying Student Login Credential Report
 * Shows student login usernames and passwords with filtering by class and section
 */
public class StudentLoginActivity extends TeacherReportDetailActivity {

    private static final String TAG = "StudentLoginActivity";

    private RecyclerView reportContentRecyclerView;
    private List<StudentLoginModel> studentLoginList;
    private StudentLoginAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize RecyclerView
        reportContentRecyclerView = getReportContentRecyclerView();
        reportContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize list and adapter
        studentLoginList = new ArrayList<>();
        adapter = new StudentLoginAdapter(this, studentLoginList);
        reportContentRecyclerView.setAdapter(adapter);

        Log.d(TAG, "StudentLoginActivity initialized");
    }
    
    @Override
    protected void loadReportData() {
        Log.d(TAG, "loadReportData called");
        
        // Get selected filter values
        String classId = getSelectedClassId();
        String sectionId = getSelectedSectionId();
        
        Log.d(TAG, "Filters - Class: " + classId + ", Section: " + sectionId);
        
        // Fetch student login report
        fetchStudentLoginReport(classId, sectionId);
    }
    
    /**
     * Fetch student login report from API
     */
    private void fetchStudentLoginReport(String classId, String sectionId) {
        showLoading();
        
        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + Constants.loginDetailReportFilterUrl;
        
        Log.d(TAG, "API URL: " + url);
        
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "Response received: " + response);
                    hideLoading();
                    parseStudentLoginResponse(response);
                },
                error -> {
                    Log.e(TAG, "Error fetching student login report", error);
                    hideLoading();
                    Toast.makeText(this, "Error loading student login report", Toast.LENGTH_SHORT).show();
                    showNoData();
                }) {
            
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", "smartschool");
                headers.put("Auth-Key", "schoolAdmin@");
                headers.put("Content-Type", "application/json");
                Log.d(TAG, "Headers: " + headers);
                return headers;
            }
            
            @Override
            public byte[] getBody() {
                try {
                    JSONObject jsonBody = new JSONObject();
                    
                    // Add filters only if they are not null or empty
                    if (classId != null && !classId.isEmpty()) {
                        jsonBody.put("class_id", classId);
                    }
                    if (sectionId != null && !sectionId.isEmpty()) {
                        jsonBody.put("section_id", sectionId);
                    }
                    
                    String bodyString = jsonBody.toString();
                    Log.d(TAG, "Request body: " + bodyString);
                    return bodyString.getBytes("utf-8");
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

        // Add request to queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    
    /**
     * Parse student login response
     */
    private void parseStudentLoginResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            int status = jsonResponse.optInt("status", 0);
            
            Log.d(TAG, "Response status: " + status);
            
            if (status == 1) {
                studentLoginList.clear();
                
                JSONArray dataArray = jsonResponse.optJSONArray("data");
                if (dataArray != null) {
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject studentObj = dataArray.getJSONObject(i);
                        
                        StudentLoginModel student = new StudentLoginModel();
                        student.setId(studentObj.optString("id", ""));
                        student.setAdmissionNo(studentObj.optString("admission_no", ""));
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
                        student.setEmail(studentObj.optString("email", ""));
                        student.setUsername(studentObj.optString("username", ""));
                        student.setPassword(studentObj.optString("password", ""));
                        student.setIsActive(studentObj.optString("is_active", ""));
                        
                        studentLoginList.add(student);
                    }
                }
                
                if (studentLoginList.isEmpty()) {
                    showNoData();
                } else {
                    showContent();
                    adapter.notifyDataSetChanged();
                }
                
                Log.d(TAG, "Parsed " + studentLoginList.size() + " student login records");
            } else {
                String message = jsonResponse.optString("message", "No data found");
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                showNoData();
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing response", e);
            Toast.makeText(this, "Error parsing data", Toast.LENGTH_SHORT).show();
            showNoData();
        }
    }
}

