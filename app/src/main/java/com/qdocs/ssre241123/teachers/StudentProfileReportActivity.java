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
import com.qdocs.ssre241123.adapters.StudentProfileReportAdapter;
import com.qdocs.ssre241123.model.StudentProfileReportModel;
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
 * Activity for displaying Student Profile Report
 * Shows comprehensive student information with optional filtering by class and section
 */
public class StudentProfileReportActivity extends TeacherReportDetailActivity {

    private static final String TAG = "StudentProfileReport";
    private RecyclerView reportContentRecyclerView;
    private StudentProfileReportAdapter adapter;
    private List<StudentProfileReportModel> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate called");

        // Initialize RecyclerView
        reportContentRecyclerView = findViewById(R.id.report_content_recyclerView);
        if (reportContentRecyclerView != null) {
            reportContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            
            studentList = new ArrayList<>();
            adapter = new StudentProfileReportAdapter(this, studentList);
            reportContentRecyclerView.setAdapter(adapter);
            
            Log.d(TAG, "RecyclerView initialized successfully");
        } else {
            Log.e(TAG, "RecyclerView is null!");
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
        fetchStudentProfileReport(sessionId, classId, sectionId);
    }

    private void fetchStudentProfileReport(String sessionId, String classId, String sectionId) {
        Log.d(TAG, "=== Fetching Student Profile Report ===");
        Log.d(TAG, "Note: Filters are optional. Will send only selected filters to API.");

        // Use buildApiUrl() to ensure consistent URL construction with configured domain
        String url = Utility.buildApiUrl(getApplicationContext(), Constants.studentProfileReportFilterUrl);

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
                        parseStudentProfileResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "=== API Error ===");
                        Log.e(TAG, "Error: " + error.toString());
                        
                        hideLoading();
                        showNoData();
                        
                        // Extract error message
                        String errorMessage = "Failed to load student profile report";
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String errorResponse = new String(error.networkResponse.data, "UTF-8");
                                Log.e(TAG, "Error Response: " + errorResponse);
                                
                                JSONObject errorJson = new JSONObject(errorResponse);
                                if (errorJson.has("message")) {
                                    errorMessage = errorJson.getString("message");
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Error parsing error response", e);
                            }
                        } else if (error.getMessage() != null) {
                            errorMessage = error.getMessage();
                        }
                        
                        Toast.makeText(StudentProfileReportActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
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
                    
                    if (classId != null && !classId.isEmpty()) {
                        try {
                            int classIdInt = Integer.parseInt(classId);
                            jsonBody.put("class_id", classIdInt);
                            Log.d(TAG, "Added class_id filter: " + classIdInt);
                        } catch (NumberFormatException e) {
                            Log.w(TAG, "Invalid class_id format: " + classId + ", skipping this filter", e);
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
                        }
                    } else {
                        Log.d(TAG, "section_id not selected, will fetch all sections");
                    }

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

        // Set timeout to 30 seconds
        stringRequest.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                30000,
                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);
        Log.d(TAG, "Request added to queue");
    }

    private void parseStudentProfileResponse(String response) {
        Log.d(TAG, "=== Parsing Student Profile Response ===");

        try {
            // Validate response
            if (response == null || response.trim().isEmpty()) {
                Log.e(TAG, "Response is null or empty");
                hideLoading();
                showNoData();
                Toast.makeText(this, "Empty response from server", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d(TAG, "Response length: " + response.length());
            Log.d(TAG, "Response preview: " + (response.length() > 200 ? response.substring(0, 200) + "..." : response));

            JSONObject jsonResponse = new JSONObject(response);
            Log.d(TAG, "JSON Response parsed successfully");

            // Check for success status - support both formats
            // Format 1: "success": true (boolean)
            // Format 2: "status": 1 (integer)
            boolean success = false;
            if (jsonResponse.has("success")) {
                success = jsonResponse.optBoolean("success", false);
                Log.d(TAG, "Success (boolean): " + success);
            } else if (jsonResponse.has("status")) {
                int status = jsonResponse.optInt("status", 0);
                success = (status == 1);
                Log.d(TAG, "Status (integer): " + status + ", Success: " + success);
            }

            if (!success) {
                String message = jsonResponse.optString("message", "Failed to load data");
                Log.e(TAG, "API returned error status. Message: " + message);
                hideLoading();
                showNoData();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;
            }

            // Get data array
            JSONArray dataArray = jsonResponse.optJSONArray("data");
            if (dataArray == null) {
                Log.e(TAG, "Data array is null");
                hideLoading();
                showNoData();
                Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dataArray.length() == 0) {
                Log.d(TAG, "Data array is empty");
                hideLoading();
                showNoData();
                Toast.makeText(this, "No student profiles found", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d(TAG, "Found " + dataArray.length() + " student profiles");

            // Clear existing list
            if (studentList == null) {
                studentList = new ArrayList<>();
                Log.w(TAG, "studentList was null, created new ArrayList");
            }
            studentList.clear();

            // Parse each student profile
            for (int i = 0; i < dataArray.length(); i++) {
                try {
                    JSONObject studentJson = dataArray.getJSONObject(i);

                    // Skip null objects
                    if (studentJson == null) {
                        Log.w(TAG, "Skipping null student object at index " + i);
                        continue;
                    }

                    Log.d(TAG, "Processing student " + (i + 1) + "/" + dataArray.length());

                    StudentProfileReportModel student = parseStudentProfile(studentJson);
                    if (student != null) {
                        studentList.add(student);
                        Log.d(TAG, "Added student: " + student.getFullName());
                    } else {
                        Log.w(TAG, "parseStudentProfile returned null for index " + i);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing student at index " + i, e);
                    // Continue with next student
                }
            }

            Log.d(TAG, "Successfully parsed " + studentList.size() + " student profiles");

            // Update UI
            hideLoading();
            if (studentList.isEmpty()) {
                Log.d(TAG, "Student list is empty, showing no data");
                showNoData();
                Toast.makeText(this, "No valid student profiles found", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "Showing content with " + studentList.size() + " student profiles");
                showContent();
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "Adapter notified of data change");
                } else {
                    Log.e(TAG, "Adapter is null!");
                }
                Toast.makeText(this, "Loaded " + studentList.size() + " student profiles", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Log.e(TAG, "JSON parsing error", e);
            Log.e(TAG, "Response that failed to parse: " + response);
            hideLoading();
            showNoData();
            Toast.makeText(this, "Error parsing response: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private StudentProfileReportModel parseStudentProfile(JSONObject json) {
        try {
            StudentProfileReportModel student = new StudentProfileReportModel();

            // Basic Information
            student.setId(json.optString("id", ""));
            student.setAdmissionNo(json.optString("admission_no", ""));
            student.setRollNo(json.optString("roll_no", ""));
            student.setAdmissionDate(json.optString("admission_date", ""));
            student.setFirstname(json.optString("firstname", ""));
            student.setMiddlename(json.optString("middlename", ""));
            student.setLastname(json.optString("lastname", ""));

            // Build full name if not provided
            String fullName = json.optString("full_name", "");
            if (fullName.isEmpty()) {
                StringBuilder nameBuilder = new StringBuilder();
                if (!student.getFirstname().isEmpty()) nameBuilder.append(student.getFirstname());
                if (!student.getMiddlename().isEmpty()) {
                    if (nameBuilder.length() > 0) nameBuilder.append(" ");
                    nameBuilder.append(student.getMiddlename());
                }
                if (!student.getLastname().isEmpty()) {
                    if (nameBuilder.length() > 0) nameBuilder.append(" ");
                    nameBuilder.append(student.getLastname());
                }
                fullName = nameBuilder.toString();
            }
            student.setFullName(fullName);

            Log.d(TAG, "Parsing student: " + fullName + " (ID: " + student.getId() + ")");

            student.setRte(json.optString("rte", ""));
            student.setImage(json.optString("image", ""));
            student.setMobileno(json.optString("mobileno", ""));
            student.setEmail(json.optString("email", ""));
            student.setState(json.optString("state", ""));
            student.setCity(json.optString("city", ""));
            student.setPincode(json.optString("pincode", ""));
            student.setReligion(json.optString("religion", ""));
            student.setCast(json.optString("cast", ""));
            student.setDob(json.optString("dob", ""));
            student.setCurrentAddress(json.optString("current_address", ""));
            student.setPermanentAddress(json.optString("permanent_address", ""));
            student.setCategoryId(json.optString("category_id", ""));
            student.setCategoryName(json.optString("category_name", ""));
            student.setAdharNo(json.optString("adhar_no", ""));
            student.setSamagraId(json.optString("samagra_id", ""));
            student.setBankAccountNo(json.optString("bank_account_no", ""));
            student.setBankName(json.optString("bank_name", ""));
            student.setIfscCode(json.optString("ifsc_code", ""));
            student.setGuardianIs(json.optString("guardian_is", ""));
            student.setIsActive(json.optString("is_active", ""));
            student.setCreatedAt(json.optString("created_at", ""));
            student.setUpdatedAt(json.optString("updated_at", ""));
            student.setFatherPic(json.optString("father_pic", ""));
            student.setMotherPic(json.optString("mother_pic", ""));
            student.setGuardianPic(json.optString("guardian_pic", ""));
            student.setGender(json.optString("gender", ""));
            student.setBloodGroup(json.optString("blood_group", ""));
            student.setSchoolHouseId(json.optString("school_house_id", ""));
            student.setSchoolHouseName(json.optString("school_house_name", ""));
            student.setNote(json.optString("note", ""));
            student.setPreviousSchool(json.optString("previous_school", ""));
            student.setHeight(json.optString("height", ""));
            student.setWeight(json.optString("weight", ""));
            student.setMeasurementDate(json.optString("measurement_date", ""));
            student.setDisableReason(json.optString("disable_reason", ""));
            student.setDisableNote(json.optString("disable_note", ""));

            // Class Information - try multiple field name variations
            student.setClassId(json.optString("class_id", ""));
            student.setClassName(json.optString("class_name", json.optString("class", "")));
            student.setSectionId(json.optString("section_id", ""));
            student.setSectionName(json.optString("section_name", json.optString("section", "")));

            // Session Information
            student.setSessionId(json.optString("session_id", ""));
            student.setSessionName(json.optString("session_name", json.optString("session", "")));

            // Father Information
            student.setFatherName(json.optString("father_name", ""));
            student.setFatherPhone(json.optString("father_phone", ""));
            student.setFatherOccupation(json.optString("father_occupation", ""));

            // Mother Information
            student.setMotherName(json.optString("mother_name", ""));
            student.setMotherPhone(json.optString("mother_phone", ""));
            student.setMotherOccupation(json.optString("mother_occupation", ""));

            // Guardian Information
            student.setGuardianName(json.optString("guardian_name", ""));
            student.setGuardianRelation(json.optString("guardian_relation", ""));
            student.setGuardianPhone(json.optString("guardian_phone", ""));
            student.setGuardianOccupation(json.optString("guardian_occupation", ""));
            student.setGuardianAddress(json.optString("guardian_address", ""));
            student.setGuardianEmail(json.optString("guardian_email", ""));

            // Hostel Information
            student.setHostelId(json.optString("hostel_id", ""));
            student.setHostelName(json.optString("hostel_name", ""));
            student.setHostelRoomNo(json.optString("hostel_room_no", ""));
            student.setHostelRoomType(json.optString("hostel_room_type", ""));
            student.setHostelCostPerBed(json.optString("hostel_cost_per_bed", ""));

            // Transport Information
            student.setVehicleNo(json.optString("vehicle_no", ""));
            student.setVehicleModel(json.optString("vehicle_model", ""));
            student.setVehicleRouteId(json.optString("vehicle_route_id", ""));
            student.setVehicleRouteName(json.optString("vehicle_route_name", ""));
            student.setDriverName(json.optString("driver_name", ""));
            student.setDriverContact(json.optString("driver_contact", ""));
            student.setPickupPointName(json.optString("pickup_point_name", ""));
            student.setTransportFees(json.optString("transport_fees", ""));

            // Login Credentials
            student.setUsername(json.optString("username", ""));
            student.setPassword(json.optString("password", ""));

            // Fees Information
            student.setFeesDiscount(json.optString("fees_discount", ""));

            Log.d(TAG, "Successfully parsed student: " + student.getFullName() +
                  " - Class: " + student.getClassName() + " - Section: " + student.getSectionName());

            return student;
        } catch (Exception e) {
            Log.e(TAG, "Error parsing individual student profile", e);
            Log.e(TAG, "JSON that failed: " + json.toString());
            return null;
        }
    }
}

