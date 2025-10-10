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
import com.qdocs.ssre241123.adapters.YearReportDueFeeAdapter;
import com.qdocs.ssre241123.model.DueFeeReportModel;
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
 * Activity for displaying Year Report Due Fees (Balance Fees Statement)
 * Shows students with due fees for the entire year (up to December 31st)
 * 
 * Key Difference from Regular Due Fees Report:
 * - Regular: Uses current date for due date comparison
 * - Year Report: Uses December 31st of current year for due date comparison
 */
public class YearReportDueFeeActivity extends TeacherReportDetailActivity {
    
    private static final String TAG = "YearReportDueFee";
    
    private RecyclerView recyclerView;
    private YearReportDueFeeAdapter adapter;
    private List<DueFeeReportModel> dueFeeList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "onCreate called");
        
        // Initialize list
        dueFeeList = new ArrayList<>();

        // Setup RecyclerView
        recyclerView = getReportContentRecyclerView();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new YearReportDueFeeAdapter(this, dueFeeList);
        recyclerView.setAdapter(adapter);
        
        Log.d(TAG, "RecyclerView and adapter initialized");
    }
    
    @Override
    protected void loadReportData() {
        Log.d(TAG, "=== loadReportData called ===");
        
        // Get selected filter values
        String sessionId = getSelectedSessionId();
        String classId = getSelectedClassId();
        String sectionId = getSelectedSectionId();
        
        Log.d(TAG, "Selected Filters - Session: " + sessionId + ", Class: " + classId + ", Section: " + sectionId);
        
        // Note: All filters are optional for Year Report Due Fee
        // Show loading state
        showLoading();
        
        // Fetch data from API
        fetchYearReportDueFee(sessionId, classId, sectionId);
    }
    
    private void fetchYearReportDueFee(String sessionId, String classId, String sectionId) {
        Log.d(TAG, "=== Fetching Year Report Due Fee ===");
        
        // Get base URL from shared preferences
        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + Constants.yearReportDueFeeFilterUrl;
        
        Log.d(TAG, "Base URL: " + baseUrl);
        Log.d(TAG, "Full API URL: " + url);
        Log.d(TAG, "Session ID: " + sessionId);
        Log.d(TAG, "Class ID: " + classId);
        Log.d(TAG, "Section ID: " + sectionId);
        Log.d(TAG, "Note: This API uses December 31st of current year for due date comparison");
        
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
            response -> {
                Log.d(TAG, "=== API Response Received ===");
                Log.d(TAG, "Response Length: " + response.length());
                Log.d(TAG, "Response: " + response);
                hideLoading();
                parseYearReportDueFeeResponse(response);
            },
            error -> {
                Log.e(TAG, "=== API Error ===");
                Log.e(TAG, "Error: " + error.toString());
                
                String errorMessage = "Unknown error occurred";
                
                if (error.networkResponse != null) {
                    Log.e(TAG, "Status Code: " + error.networkResponse.statusCode);
                    try {
                        String errorBody = new String(error.networkResponse.data, "UTF-8");
                        Log.e(TAG, "Error Body: " + errorBody);
                        errorMessage = "Server error: " + error.networkResponse.statusCode;
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Error parsing error body", e);
                    }
                } else if (error instanceof com.android.volley.NoConnectionError) {
                    errorMessage = "No internet connection";
                    Log.e(TAG, "No connection error");
                } else if (error instanceof com.android.volley.TimeoutError) {
                    errorMessage = "Request timeout";
                    Log.e(TAG, "Timeout error");
                } else if (error instanceof com.android.volley.ServerError) {
                    errorMessage = "Server error";
                    Log.e(TAG, "Server error");
                } else if (error instanceof com.android.volley.ParseError) {
                    errorMessage = "Parse error";
                    Log.e(TAG, "Parse error");
                } else if (error.getMessage() != null) {
                    errorMessage = error.getMessage();
                }
                
                hideLoading();
                showNoData();
                Toast.makeText(this, "Error loading year report: " + errorMessage, 
                    Toast.LENGTH_LONG).show();
            }) {
            
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", "application/json");
                
                Log.d(TAG, "=== Request Headers ===");
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    Log.d(TAG, entry.getKey() + ": " + entry.getValue());
                }
                
                return headers;
            }
            
            @Override
            public byte[] getBody() {
                try {
                    JSONObject jsonBody = new JSONObject();
                    
                    // Add filters only if they are not null
                    if (classId != null && !classId.isEmpty()) {
                        jsonBody.put("class_id", classId);
                    }
                    if (sectionId != null && !sectionId.isEmpty()) {
                        jsonBody.put("section_id", sectionId);
                    }
                    if (sessionId != null && !sessionId.isEmpty()) {
                        jsonBody.put("session_id", sessionId);
                    }
                    
                    String requestBody = jsonBody.toString();
                    Log.d(TAG, "=== Request Body ===");
                    Log.d(TAG, requestBody);
                    
                    return requestBody.getBytes("UTF-8");
                } catch (JSONException | UnsupportedEncodingException e) {
                    Log.e(TAG, "Error creating request body", e);
                    return null;
                }
            }
        };
        
        // Add request to queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        
        Log.d(TAG, "Request added to queue");
    }
    
    private void parseYearReportDueFeeResponse(String response) {
        Log.d(TAG, "=== Parsing Response ===");
        
        try {
            JSONObject jsonObject = new JSONObject(response);
            
            int status = jsonObject.optInt("status", 0);
            String message = jsonObject.optString("message", "");
            
            Log.d(TAG, "Status: " + status);
            Log.d(TAG, "Message: " + message);
            
            if (status == 1) {
                // Success - parse data
                JSONArray dataArray = jsonObject.optJSONArray("data");
                
                if (dataArray != null && dataArray.length() > 0) {
                    Log.d(TAG, "Data Array Length: " + dataArray.length());
                    
                    dueFeeList.clear();
                    
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject studentObj = dataArray.getJSONObject(i);
                        
                        DueFeeReportModel dueFee = new DueFeeReportModel();
                        
                        // Parse student information
                        dueFee.setStudentId(studentObj.optString("student_id", ""));
                        dueFee.setAdmissionNo(studentObj.optString("admission_no", ""));
                        dueFee.setFirstname(studentObj.optString("firstname", ""));
                        dueFee.setMiddlename(studentObj.optString("middlename", ""));
                        dueFee.setLastname(studentObj.optString("lastname", ""));
                        dueFee.setClassName(studentObj.optString("class", ""));
                        dueFee.setSectionName(studentObj.optString("section", ""));
                        dueFee.setFatherName(studentObj.optString("father_name", ""));
                        dueFee.setMobileno(studentObj.optString("mobileno", ""));
                        dueFee.setGuardianName(studentObj.optString("guardian_name", ""));
                        dueFee.setGuardianPhone(studentObj.optString("guardian_phone", ""));
                        
                        // Calculate fee summary from fees_list
                        JSONArray feesListArray = studentObj.optJSONArray("fees_list");
                        double totalAmount = 0, totalPaid = 0, totalBalance = 0, totalFine = 0, totalDiscount = 0;
                        List<DueFeeReportModel.FeeDetail> feesList = new ArrayList<>();
                        
                        if (feesListArray != null) {
                            for (int j = 0; j < feesListArray.length(); j++) {
                                JSONObject feeObj = feesListArray.getJSONObject(j);
                                
                                // Get fee amount
                                double feeAmount = parseDouble(feeObj.optString("amount", "0"));
                                
                                // Parse amount_detail to calculate paid amount, fine, and discount
                                String amountDetailStr = feeObj.optString("amount_detail", null);
                                double paidAmount = 0;
                                double fineAmount = 0;
                                double discountAmount = 0;
                                
                                if (amountDetailStr != null && !amountDetailStr.equals("null") && !amountDetailStr.equals("0") && !amountDetailStr.isEmpty()) {
                                    try {
                                        JSONObject amountDetail = new JSONObject(amountDetailStr);
                                        
                                        // Iterate through all payment entries (e.g., "1", "2", etc.)
                                        java.util.Iterator<String> keys = amountDetail.keys();
                                        while (keys.hasNext()) {
                                            String key = keys.next();
                                            JSONObject payment = amountDetail.getJSONObject(key);
                                            
                                            // Sum up amounts from each payment
                                            paidAmount += parseDouble(payment.optString("amount", "0"));
                                            fineAmount += parseDouble(payment.optString("amount_fine", "0"));
                                            discountAmount += parseDouble(payment.optString("amount_discount", "0"));
                                        }
                                    } catch (JSONException e) {
                                        Log.e(TAG, "Error parsing amount_detail for fee " + feeObj.optString("type", ""), e);
                                    }
                                }
                                
                                // Calculate balance
                                double balanceAmount = feeAmount - paidAmount;
                                
                                // Determine status
                                String feeStatus;
                                if (paidAmount == 0) {
                                    feeStatus = "unpaid";
                                } else if (balanceAmount > 0) {
                                    feeStatus = "partial";
                                } else {
                                    feeStatus = "paid";
                                }
                                
                                // Create fee detail object
                                DueFeeReportModel.FeeDetail feeDetail = new DueFeeReportModel.FeeDetail();
                                feeDetail.setFeeType(feeObj.optString("type", ""));
                                feeDetail.setFeeCode(feeObj.optString("code", ""));
                                feeDetail.setDueDate(feeObj.optString("due_date", ""));
                                feeDetail.setAmount(String.format("%.2f", feeAmount));
                                feeDetail.setPaidAmount(String.format("%.2f", paidAmount));
                                feeDetail.setBalanceAmount(String.format("%.2f", balanceAmount));
                                feeDetail.setFineAmount(String.format("%.2f", fineAmount));
                                feeDetail.setDiscountAmount(String.format("%.2f", discountAmount));
                                feeDetail.setStatus(feeStatus);
                                
                                feesList.add(feeDetail);
                                
                                // Sum up totals
                                totalAmount += feeAmount;
                                totalPaid += paidAmount;
                                totalBalance += balanceAmount;
                                totalFine += fineAmount;
                                totalDiscount += discountAmount;
                                
                                // Log first fee for debugging
                                if (i == 0 && j == 0) {
                                    Log.d(TAG, "First Fee - Type: " + feeObj.optString("type", ""));
                                    Log.d(TAG, "First Fee - Amount: " + feeAmount);
                                    Log.d(TAG, "First Fee - Paid: " + paidAmount);
                                    Log.d(TAG, "First Fee - Balance: " + balanceAmount);
                                }
                            }
                        }
                        
                        dueFee.setFeesList(feesList);
                        
                        // Parse transport fees if available (same logic as regular fees)
                        // Transport fees structure may vary, keeping it simple for now
                        dueFee.setTransportFeesList(new ArrayList<>());
                        
                        // Set totals
                        dueFee.setTotalAmount(String.format("%.2f", totalAmount));
                        dueFee.setTotalPaid(String.format("%.2f", totalPaid));
                        dueFee.setTotalBalance(String.format("%.2f", totalBalance));
                        dueFee.setTotalFine(String.format("%.2f", totalFine));
                        dueFee.setTotalDiscount(String.format("%.2f", totalDiscount));
                        
                        dueFeeList.add(dueFee);
                        
                        if (i == 0) {
                            Log.d(TAG, "First Student: " + dueFee.getFullName());
                            Log.d(TAG, "Total Balance: " + dueFee.getTotalBalance());
                        }
                    }
                    
                    Log.d(TAG, "Year report due fee list size: " + dueFeeList.size());
                    
                    // Update adapter
                    adapter.notifyDataSetChanged();
                    
                    // Show content
                    showContent();
                    
                    // Show success message
                    String successMessage = "Found " + dueFeeList.size() + " student(s) with due fees for the year";
                    Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
                    
                } else {
                    Log.d(TAG, "No data in response");
                    showNoData();
                    Toast.makeText(this, "No students with due fees found for the year", Toast.LENGTH_SHORT).show();
                }
                
            } else {
                // Error status
                Log.e(TAG, "Error status from API: " + message);
                showNoData();
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
            
        } catch (JSONException e) {
            Log.e(TAG, "JSON parsing error", e);
            showNoData();
            Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
        }
    }
    
    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}

