package com.qdocs.ssre241123.teachers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.qdocs.ssre241123.BaseActivity;
import com.qdocs.ssre241123.Login;
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.StudentProfileAdapter;
import com.qdocs.ssre241123.fragments.TeacherAttendanceFragment;
import com.qdocs.ssre241123.fragments.TeacherDocumentsFragment;
import com.qdocs.ssre241123.fragments.TeacherLeavesFragment;
import com.qdocs.ssre241123.fragments.TeacherPayrollFragment;
import com.qdocs.ssre241123.fragments.TeacherProfileFragment;
import com.qdocs.ssre241123.fragments.TeacherTimelineFragment;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.TeacherAuthHelper;
import com.qdocs.ssre241123.utils.Utility;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherProfile extends BaseActivity {

    private TextView nameTV, designationTV, employeeIdTV;
    private CircleImageView profileImageIV;
    private ImageView qrcodeIV;
    private RecyclerView recyclerView;
    private StudentProfileAdapter adapter;
    private CardView cardViewOuter;
    private LinearLayout qrcode_layout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TeacherProfileViewPagerAdapter viewPagerAdapter;
    private Map<String, String> headers = new HashMap<String, String>();

    // Profile data arrays for comprehensive profile display
    private ArrayList<String> profileValues = new ArrayList<>();
    private int[] profileHeaderArray = {
        R.string.email,
        R.string.phone,
        R.string.emergency_contact,
        R.string.designation,
        R.string.department,
        R.string.date_of_joining,
        R.string.qualification,
        R.string.work_experience,
        R.string.marital_status,
        R.string.father_name,
        R.string.mother_name,
        R.string.local_address,
        R.string.permanent_address
    };

    // Profile sections data
    private JSONObject basicInfo, contactInfo, personalInfo, addressInfo, bankDetails, socialMedia, qrCodeData;

    // Complete teacher profile data for fragments
    private JSONObject teacherProfileData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if teacher is logged in
        if (!TeacherAuthHelper.isTeacherLoggedIn(this)) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
            return;
        }

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.teacher_profile_activity, null, false);
        mDrawerLayout.addView(contentView, 0);

        titleTV.setText(getApplicationContext().getString(R.string.profile));

        initializeViews();
        loadTeacherProfileData();

        if (Utility.isConnectingToInternet(getApplicationContext())) {
            getTeacherProfileFromApi();
        } else {
            Toast.makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeViews() {
        nameTV = findViewById(R.id.teacher_profile_nameTV);
        designationTV = findViewById(R.id.teacher_profile_designationTV);
        employeeIdTV = findViewById(R.id.teacher_profile_employeeIdTV);
        profileImageIV = findViewById(R.id.teacher_profile_imageIV);
        qrcodeIV = findViewById(R.id.teacherProfile_qrcodeIV);
        cardViewOuter = findViewById(R.id.card_view_outer);
        qrcode_layout = findViewById(R.id.teacher_qrcode_layout);

        // Initialize TabLayout and ViewPager
        viewPager = findViewById(R.id.teacherProfileViewPager);
        tabLayout = findViewById(R.id.teacherProfileTabLayout);
        viewPagerAdapter = new TeacherProfileViewPagerAdapter(getSupportFragmentManager());

        // Set theme color for outer card
        cardViewOuter.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));

        // Set up tabs
        tabLayout.setupWithViewPager(viewPager);
        setupViewPager(viewPager);
        decorateTabLayout();
    }

    private void setupViewPager(ViewPager viewPager) {
        // Create fragments with null data initially - they will be updated when API data arrives
        viewPagerAdapter.addFragment(TeacherProfileFragment.newInstance(null),
                                   getString(R.string.teacher_profile_tab));
        viewPagerAdapter.addFragment(TeacherPayrollFragment.newInstance(null),
                                   getString(R.string.teacher_payroll_tab));
        viewPagerAdapter.addFragment(TeacherLeavesFragment.newInstance(null),
                                   getString(R.string.teacher_leaves_tab));
        viewPagerAdapter.addFragment(TeacherAttendanceFragment.newInstance(null),
                                   getString(R.string.teacher_attendance_tab));
        viewPagerAdapter.addFragment(TeacherDocumentsFragment.newInstance(null),
                                   getString(R.string.teacher_documents_tab));
        viewPagerAdapter.addFragment(TeacherTimelineFragment.newInstance(null),
                                   getString(R.string.teacher_timeline_tab));
        viewPager.setAdapter(viewPagerAdapter);
    }

    private void decorateTabLayout() {
        tabLayout.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
    }

    private void refreshViewPagerFragments() {
        if (viewPagerAdapter != null && teacherProfileData != null) {
            Log.d("Teacher Profile Refresh", "Starting ViewPager fragments refresh");
            Log.d("Teacher Profile Refresh", "Fragment count: " + viewPagerAdapter.mFragmentList.size());
            Log.d("Teacher Profile Refresh", "Teacher data available: " + (teacherProfileData != null));

            // Update existing fragments with new data instead of recreating them
            for (int i = 0; i < viewPagerAdapter.mFragmentList.size(); i++) {
                Fragment fragment = viewPagerAdapter.mFragmentList.get(i);
                String fragmentName = fragment.getClass().getSimpleName();
                Log.d("Teacher Profile Refresh", "Updating fragment " + i + ": " + fragmentName);

                if (fragment instanceof TeacherProfileFragment) {
                    Log.d("Teacher Profile Refresh", "Updating TeacherProfileFragment with profile data");
                    ((TeacherProfileFragment) fragment).updateProfileData(teacherProfileData);
                } else if (fragment instanceof TeacherPayrollFragment) {
                    Log.d("Teacher Profile Refresh", "Updating TeacherPayrollFragment with payroll data");
                    ((TeacherPayrollFragment) fragment).updatePayrollData(teacherProfileData);
                } else if (fragment instanceof TeacherLeavesFragment) {
                    Log.d("Teacher Profile Refresh", "Updating TeacherLeavesFragment with leaves data");
                    ((TeacherLeavesFragment) fragment).updateLeavesData(teacherProfileData);
                } else if (fragment instanceof TeacherAttendanceFragment) {
                    Log.d("Teacher Profile Refresh", "Updating TeacherAttendanceFragment with attendance data");
                    ((TeacherAttendanceFragment) fragment).updateAttendanceData(teacherProfileData);
                } else if (fragment instanceof TeacherDocumentsFragment) {
                    Log.d("Teacher Profile Refresh", "Updating TeacherDocumentsFragment with documents data");
                    ((TeacherDocumentsFragment) fragment).updateDocumentsData(teacherProfileData);
                } else if (fragment instanceof TeacherTimelineFragment) {
                    Log.d("Teacher Profile Refresh", "Updating TeacherTimelineFragment with timeline data");
                    ((TeacherTimelineFragment) fragment).updateTimelineData(teacherProfileData);
                } else {
                    Log.w("Teacher Profile Refresh", "Unknown fragment type: " + fragmentName);
                }
            }

            Log.d("Teacher Profile Refresh", "ViewPager fragments refresh completed successfully");
        } else {
            Log.w("Teacher Profile Refresh", "Cannot refresh fragments - viewPagerAdapter: " +
                  (viewPagerAdapter != null) + ", teacherProfileData: " + (teacherProfileData != null));
        }
    }

    private void loadTeacherProfileData() {
        // Load cached teacher data from SharedPreferences
        String teacherName = TeacherAuthHelper.getTeacherName(this);
        String employeeId = Utility.getSharedPreferences(this, Constants.teacherEmployeeId);
        String email = Utility.getSharedPreferences(this, Constants.teacherEmail);
        String phone = Utility.getSharedPreferences(this, Constants.teacherContact);
        String designation = Utility.getSharedPreferences(this, Constants.teacherDesignation);
        String department = Utility.getSharedPreferences(this, Constants.teacherDepartment);
        String imageUrl = Utility.getSharedPreferences(this, Constants.teacherImage);

        // Set header information
        nameTV.setText(teacherName);
        designationTV.setText(designation);
        employeeIdTV.setText(employeeId);

        // Update profile values for RecyclerView
        updateProfileValues(email, phone, designation, department);

        // Load teacher profile image
        loadTeacherImage(imageUrl);
    }

    private void updateProfileValues(String email, String phone, String designation, String department) {
        profileValues.clear();
        profileValues.add(email != null && !email.isEmpty() ? email : "Not provided");
        profileValues.add(phone != null && !phone.isEmpty() ? phone : "Not provided");
        profileValues.add(designation != null && !designation.isEmpty() ? designation : "Not provided");
        profileValues.add(department != null && !department.isEmpty() ? department : "Not provided");

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void loadTeacherImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty() && !imageUrl.equals("null") && !imageUrl.equals("")) {
            String baseUrl = Utility.getSharedPreferences(this, "apiUrl");
            // Remove the trailing slash if present
            if (baseUrl.endsWith("/")) {
                baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
            }
            String fullImageUrl = baseUrl + "/uploads/staff_images/" + imageUrl;

            Log.e("Teacher Profile Image URL", fullImageUrl);

            Picasso.with(this)
                    .load(fullImageUrl)
                    .placeholder(R.drawable.demo)
                    .error(R.drawable.demo)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(profileImageIV);
        } else {
            Log.d("Teacher Profile Image", "No image URL available, using default");
            profileImageIV.setImageResource(R.drawable.demo);
        }
    }

    private void getTeacherProfileFromApi() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading Teacher Profile...");
        pd.setTitle("Please Wait");
        pd.setCancelable(false);
        pd.show();

        // Use GET method with staff_id in URL path - FIXED to use staff_id instead of teacherId
        String staffId = TeacherAuthHelper.getTeacherStaffId(this);
        String teacherId = TeacherAuthHelper.getTeacherId(this);
        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + Constants.teacherProfileUrl + "/" + staffId;

        // Validate required data before making API call
        if (staffId == null || staffId.isEmpty()) {
            pd.dismiss();
            Toast.makeText(this, "Staff ID not found. Please login again.", Toast.LENGTH_LONG).show();
            return;
        }

        if (baseUrl == null || baseUrl.isEmpty()) {
            pd.dismiss();
            Toast.makeText(this, "API URL not configured. Please contact administrator.", Toast.LENGTH_LONG).show();
            return;
        }

        Log.d("Teacher Profile API", "=== API CALL DEBUG ===");
        Log.d("Teacher Profile API", "Base URL: " + baseUrl);
        Log.d("Teacher Profile API", "Full API URL: " + url);
        Log.d("Teacher Profile API", "Staff ID (for API): " + staffId);
        Log.d("Teacher Profile API", "Teacher ID (internal): " + teacherId);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.d("Teacher Profile API", "API Response received, length: " + result.length());
                        Log.d("Teacher Profile API", "API Response: " + result);

                        JSONObject object = new JSONObject(result);
                        String status = object.getString("status");
                        String message = object.getString("message");

                        Log.d("Teacher Profile API", "Response status: " + status);
                        Log.d("Teacher Profile API", "Response message: " + message);

                        if (status.equals("1")) {
                            // Store the complete response for fragments
                            teacherProfileData = object;
                            Log.d("Teacher Profile API", "Profile data stored successfully");

                            // Profile retrieved successfully - parse comprehensive response
                            parseComprehensiveTeacherProfile(object);

                            // Show success message
                            Toast.makeText(getApplicationContext(), "Profile loaded successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w("Teacher Profile API", "API returned error status: " + status + ", message: " + message);
                            String errorMsg = message.isEmpty() ? "Failed to load profile data" : message;
                            Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();

                            // Show error state in fragments
                            showErrorState("Server Error: " + errorMsg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("Teacher Profile API", "JSON parsing error: " + e.getMessage());
                        Toast.makeText(getApplicationContext(), "Error parsing profile data. Please try again.", Toast.LENGTH_LONG).show();
                        showErrorState("Data Parsing Error: Invalid response format");
                    }
                } else {
                    pd.dismiss();
                    Log.e("Teacher Profile API", "API response is null");
                    Toast.makeText(getApplicationContext(), "No response from server. Please check your connection.", Toast.LENGTH_LONG).show();
                    showErrorState("Network Error: No response received");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Teacher Profile Volley Error", "GET request failed: " + volleyError.toString());

                // Try POST method as fallback
                Log.d("Teacher Profile API", "GET failed, trying POST method as fallback");
                tryPostMethod(pd, staffId, baseUrl);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", staffId);  // Use staff_id for User-ID header

                String jwtToken = TeacherAuthHelper.getTeacherJwtToken(TeacherProfile.this);
                if (!jwtToken.isEmpty()) {
                    headers.put("Authorization", "Bearer " + jwtToken);
                }

                Log.d("Teacher Profile Headers", "=== REQUEST HEADERS ===");
                Log.d("Teacher Profile Headers", "Client-Service: " + Constants.clientService);
                Log.d("Teacher Profile Headers", "Auth-Key: " + Constants.authKey);
                Log.d("Teacher Profile Headers", "User-ID: " + staffId);
                Log.d("Teacher Profile Headers", "Authorization: " + (jwtToken.isEmpty() ? "Not provided" : "Bearer token provided"));
                Log.d("Teacher Profile Headers", "Full headers: " + headers.toString());
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(TeacherProfile.this);
        requestQueue.add(stringRequest);
    }

    private void tryPostMethod(ProgressDialog pd, String staffId, String baseUrl) {
        Log.d("Teacher Profile API", "=== TRYING POST METHOD ===");

        String url = baseUrl + Constants.teacherProfileUrl;
        Log.d("Teacher Profile API", "POST URL: " + url);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.d("Teacher Profile API", "POST Response received: " + result);

                        JSONObject object = new JSONObject(result);
                        String status = object.getString("status");
                        String message = object.getString("message");

                        if (status.equals("1")) {
                            teacherProfileData = object;
                            parseComprehensiveTeacherProfile(object);
                        } else {
                            Log.w("Teacher Profile API", "POST API error: " + message);
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("Teacher Profile API", "POST JSON parsing error: " + e.getMessage());
                        Toast.makeText(getApplicationContext(), "Error parsing profile data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    pd.dismiss();
                    Log.e("Teacher Profile API", "POST response is null");
                    Toast.makeText(getApplicationContext(), "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("Teacher Profile Volley Error", "Both GET and POST failed: " + volleyError.toString());
                Toast.makeText(TeacherProfile.this, "Failed to load profile. Please check your connection.", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> postHeaders = new HashMap<>();
                postHeaders.put("Client-Service", Constants.clientService);
                postHeaders.put("Auth-Key", Constants.authKey);
                postHeaders.put("Content-Type", Constants.contentType);
                postHeaders.put("User-ID", staffId);

                String jwtToken = TeacherAuthHelper.getTeacherJwtToken(TeacherProfile.this);
                if (!jwtToken.isEmpty()) {
                    postHeaders.put("Authorization", "Bearer " + jwtToken);
                }

                Log.d("Teacher Profile Headers", "POST Headers: " + postHeaders.toString());
                return postHeaders;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("staff_id", staffId);
                Log.d("Teacher Profile API", "POST Parameters: " + params.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(TeacherProfile.this);
        requestQueue.add(postRequest);
    }

    private void parseComprehensiveTeacherProfile(JSONObject response) {
        try {
            Log.d("Teacher Profile Debug", "=== COMPREHENSIVE PROFILE PARSING DEBUG ===");
            Log.d("Teacher Profile Debug", "Response status: " + response.optString("status"));
            Log.d("Teacher Profile Debug", "Response message: " + response.optString("message"));

            // Parse all sections of the comprehensive profile response
            basicInfo = response.optJSONObject("basic_info");
            contactInfo = response.optJSONObject("contact_info");
            personalInfo = response.optJSONObject("personal_info");
            addressInfo = response.optJSONObject("address_info");
            bankDetails = response.optJSONObject("bank_details");
            socialMedia = response.optJSONObject("social_media");
            qrCodeData = response.optJSONObject("qr_code");

            // Log all main sections
            Log.d("Teacher Profile Debug", "Basic Info: " + (response.has("basic_info") ? "Present" : "Missing"));
            Log.d("Teacher Profile Debug", "Contact Info: " + (response.has("contact_info") ? "Present" : "Missing"));
            Log.d("Teacher Profile Debug", "Personal Info: " + (response.has("personal_info") ? "Present" : "Missing"));
            Log.d("Teacher Profile Debug", "Address Info: " + (response.has("address_info") ? "Present" : "Missing"));
            Log.d("Teacher Profile Debug", "Bank Details: " + (response.has("bank_details") ? "Present" : "Missing"));
            Log.d("Teacher Profile Debug", "Payroll Details: " + (response.has("payroll_details") ? "Present" : "Missing"));
            Log.d("Teacher Profile Debug", "Leave Records: " + (response.has("leave_records") ? "Present" : "Missing"));
            Log.d("Teacher Profile Debug", "Attendance Records: " + (response.has("attendance_records") ? "Present" : "Missing"));
            Log.d("Teacher Profile Debug", "Documents: " + (response.has("documents") ? "Present" : "Missing"));
            Log.d("Teacher Profile Debug", "QR Code: " + (response.has("qr_code") ? "Present" : "Missing"));

            // Log payroll details specifically
            JSONObject payrollDetails = response.optJSONObject("payroll_details");
            if (payrollDetails != null) {

                JSONArray payrollRecords = payrollDetails.optJSONArray("payroll_records");
                Log.d("Teacher Profile Debug", "Payroll records count: " + (payrollRecords != null ? payrollRecords.length() : 0));
                if (payrollRecords != null && payrollRecords.length() > 0) {
                    Log.d("Teacher Profile Debug", "First payroll record: " + payrollRecords.optJSONObject(0).toString());
                }
            } else {
                Log.d("Teacher Profile Debug", "Payroll details is null");
            }

            // Log QR code details
            JSONObject qrCode = response.optJSONObject("qr_code");
            if (qrCode != null) {
                Log.d("Teacher Profile Debug", "QR Code URL: " + qrCode.optString("qr_code_url"));
                Log.d("Teacher Profile Debug", "QR Code Data: " + qrCode.optJSONObject("data"));
            } else {
                Log.d("Teacher Profile Debug", "QR Code is null");
            }

            // Update header information
            Log.d("Teacher Profile Parse", "Updating header information");
            updateHeaderInformation();

            // Update comprehensive profile values
            Log.d("Teacher Profile Parse", "Updating comprehensive profile values");
            updateComprehensiveProfileValues();

            // Load profile image
            String profileImageUrl = response.optString("profile_image", "");
            Log.d("Teacher Profile Parse", "Profile image URL: " + profileImageUrl);
            loadProfileImageFromUrl(profileImageUrl);

            // Load QR code
            loadQRCode();

            // Refresh ViewPager fragments with new data
            refreshViewPagerFragments();

            // Cache important data
            cacheComprehensiveProfileData(response);

            Log.d("Teacher Profile Debug", "=== END COMPREHENSIVE PROFILE PARSING DEBUG ===");
            Log.d("Teacher Profile", "Comprehensive profile parsing completed successfully");

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Teacher Profile Parse", "Error parsing comprehensive profile: " + e.getMessage());
            Toast.makeText(this, "Error processing profile data", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateHeaderInformation() {
        if (basicInfo != null) {
            String fullName = basicInfo.optString("full_name", "");
            String employeeId = basicInfo.optString("employee_id", "");
            String designationName = basicInfo.optString("designation_name", "");

            nameTV.setText(fullName);
            designationTV.setText(designationName);
            employeeIdTV.setText(employeeId);
        }
    }

    private void updateComprehensiveProfileValues() {
        profileValues.clear();

        // Email
        profileValues.add(contactInfo != null ? contactInfo.optString("email", "Not provided") : "Not provided");

        // Phone
        profileValues.add(contactInfo != null ? contactInfo.optString("contact_no", "Not provided") : "Not provided");

        // Emergency Contact
        profileValues.add(contactInfo != null ? contactInfo.optString("emergency_contact_no", "Not provided") : "Not provided");

        // Designation
        profileValues.add(basicInfo != null ? basicInfo.optString("designation_name", "Not provided") : "Not provided");

        // Department
        profileValues.add(basicInfo != null ? basicInfo.optString("department_name", "Not provided") : "Not provided");

        // Date of Joining
        profileValues.add(basicInfo != null ? basicInfo.optString("date_of_joining", "Not provided") : "Not provided");

        // Qualification
        profileValues.add(personalInfo != null ? personalInfo.optString("qualification", "Not provided") : "Not provided");

        // Work Experience
        profileValues.add(personalInfo != null ? personalInfo.optString("work_exp", "Not provided") : "Not provided");

        // Marital Status
        profileValues.add(personalInfo != null ? personalInfo.optString("marital_status", "Not provided") : "Not provided");

        // Father's Name
        profileValues.add(personalInfo != null ? personalInfo.optString("father_name", "Not provided") : "Not provided");

        // Mother's Name
        profileValues.add(personalInfo != null ? personalInfo.optString("mother_name", "Not provided") : "Not provided");

        // Local Address
        profileValues.add(addressInfo != null ? addressInfo.optString("local_address", "Not provided") : "Not provided");

        // Permanent Address
        profileValues.add(addressInfo != null ? addressInfo.optString("permanent_address", "Not provided") : "Not provided");

        // Notify adapter of data changes
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void loadProfileImageFromUrl(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Log.e("Teacher Profile Image URL", imageUrl);

            Picasso.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.demo)
                    .error(R.drawable.demo)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(profileImageIV);
        } else {
            Log.d("Teacher Profile Image", "No image URL available, using default");
            profileImageIV.setImageResource(R.drawable.demo);
        }
    }

    private void loadQRCode() {
        Log.d("Teacher QR Code", "=== QR CODE LOADING DEBUG ===");

        if (qrCodeData != null) {
            Log.d("Teacher QR Code", "QR Code data available: " + qrCodeData.toString());

            String qrCodeUrl = qrCodeData.optString("qr_code_url", "");
            Log.d("Teacher QR Code", "QR Code URL: " + qrCodeUrl);

            if (!qrCodeUrl.isEmpty()) {
                // Try to load the QR code with error handling
                Picasso.with(getApplicationContext())
                    .load(qrCodeUrl)
                    .placeholder(R.drawable.demo)  // Show placeholder while loading
                    .error(R.drawable.demo)        // Show default image if loading fails
                    .into(qrcodeIV, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("Teacher QR Code", "QR Code loaded successfully");
                            qrcode_layout.setVisibility(View.VISIBLE);

                            // Set up click listener
                            qrcodeIV.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    showAddDialog(TeacherProfile.this, qrCodeUrl, "QR Code");
                                }
                            });
                        }

                        @Override
                        public void onError() {
                            Log.e("Teacher QR Code", "Failed to load QR Code from URL: " + qrCodeUrl);

                            // Try alternative: Show QR code data in a dialog
                            String qrString = qrCodeData.optString("qr_string", "");
                            if (!qrString.isEmpty()) {
                                Log.d("Teacher QR Code", "Showing QR code with fallback handling");
                                qrcode_layout.setVisibility(View.VISIBLE);
                                qrcodeIV.setImageResource(R.drawable.demo);

                                // Set up click listener to show the QR string in a dialog
                                qrcodeIV.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        showQRStringDialog(qrString);
                                    }
                                });
                            } else {
                                Log.w("Teacher QR Code", "No QR string available, hiding QR code layout");
                                qrcode_layout.setVisibility(View.GONE);
                            }
                        }
                    });
            } else {
                Log.w("Teacher QR Code", "QR Code URL is empty");
                qrcode_layout.setVisibility(View.GONE);
            }
        } else {
            Log.w("Teacher QR Code", "QR Code data is null");
            qrcode_layout.setVisibility(View.GONE);
        }

        Log.d("Teacher QR Code", "=== END QR CODE LOADING DEBUG ===");
    }

    private void cacheComprehensiveProfileData(JSONObject response) {
        try {
            // Cache the complete response for offline access
            Utility.setSharedPreference(this, "teacher_profile_data", response.toString());

            // Cache important profile data for quick access
            if (basicInfo != null) {
                Utility.setSharedPreference(this, Constants.teacherName, basicInfo.optString("name", ""));
                Utility.setSharedPreference(this, Constants.teacherSurname, basicInfo.optString("surname", ""));
                Utility.setSharedPreference(this, Constants.teacherEmployeeId, basicInfo.optString("employee_id", ""));
                Utility.setSharedPreference(this, Constants.teacherDesignation, basicInfo.optString("designation_name", ""));
                Utility.setSharedPreference(this, Constants.teacherDepartment, basicInfo.optString("department_name", ""));
            }

            if (contactInfo != null) {
                Utility.setSharedPreference(this, Constants.teacherEmail, contactInfo.optString("email", ""));
                Utility.setSharedPreference(this, Constants.teacherContact, contactInfo.optString("contact_no", ""));
            }

            // Cache profile image URL
            String profileImageUrl = response.optString("profile_image", "");
            if (!profileImageUrl.isEmpty()) {
                Utility.setSharedPreference(this, "teacher_profile_image", profileImageUrl);
            }

            Log.d("Teacher Profile", "Comprehensive profile data cached successfully");

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Teacher Profile Cache", "Error caching comprehensive profile data: " + e.getMessage());
        }
    }

    private void showAddDialog(Context context, String url, String name) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.qrcode_layout);

        TextView nameTV = (TextView) dialog.findViewById(R.id.nameTV);
        nameTV.setText(name);
        ImageView qrcode_image = (ImageView) dialog.findViewById(R.id.qrcode_image);
        ImageView crossIcon = (ImageView) dialog.findViewById(R.id.crossIcon);

        Picasso.with(getApplicationContext()).load(url).into(qrcode_image);

        crossIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showQRStringDialog(String qrString) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("QR Code Data");
        builder.setMessage(qrString);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    // TeacherProfileViewPagerAdapter class - identical to student profile pattern
    class TeacherProfileViewPagerAdapter extends FragmentPagerAdapter {
        public final List<Fragment> mFragmentList = new ArrayList<>();
        public final List<String> mFragmentTitleList = new ArrayList<>();

        public TeacherProfileViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    /**
     * Show error state in all fragments when API call fails
     */
    private void showErrorState(String errorMessage) {
        Log.e("Teacher Profile Error", "Showing error state: " + errorMessage);

        // Create error data object
        try {
            JSONObject errorData = new JSONObject();
            errorData.put("error", true);
            errorData.put("error_message", errorMessage);
            errorData.put("status", "0");

            // Update all fragments with error state
            if (viewPagerAdapter != null) {
                for (int i = 0; i < viewPagerAdapter.mFragmentList.size(); i++) {
                    Fragment fragment = viewPagerAdapter.mFragmentList.get(i);

                    if (fragment instanceof TeacherProfileFragment) {
                        ((TeacherProfileFragment) fragment).updateProfileData(errorData);
                    } else if (fragment instanceof TeacherPayrollFragment) {
                        ((TeacherPayrollFragment) fragment).updatePayrollData(errorData);
                    } else if (fragment instanceof TeacherLeavesFragment) {
                        ((TeacherLeavesFragment) fragment).updateLeavesData(errorData);
                    } else if (fragment instanceof TeacherAttendanceFragment) {
                        ((TeacherAttendanceFragment) fragment).updateAttendanceData(errorData);
                    } else if (fragment instanceof TeacherDocumentsFragment) {
                        ((TeacherDocumentsFragment) fragment).updateDocumentsData(errorData);
                    } else if (fragment instanceof TeacherTimelineFragment) {
                        ((TeacherTimelineFragment) fragment).updateTimelineData(errorData);
                    }
                }
            }

        } catch (Exception e) {
            Log.e("Teacher Profile Error", "Error creating error state: " + e.getMessage());
        }
    }
}
