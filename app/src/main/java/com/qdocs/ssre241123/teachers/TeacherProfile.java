package com.qdocs.ssre241123.teachers;

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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.ssre241123.BaseActivity;
import com.qdocs.ssre241123.Login;
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.StudentProfileAdapter;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.TeacherAuthHelper;
import com.qdocs.ssre241123.utils.Utility;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
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
        setupRecyclerView();
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
        recyclerView = findViewById(R.id.teacher_profile_recyclerView);
        cardViewOuter = findViewById(R.id.card_view_outer);
        qrcode_layout = findViewById(R.id.teacher_qrcode_layout);

        // Set theme color for outer card
        cardViewOuter.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));

        // QR code click listener will be set up after API response
    }

    private void setupRecyclerView() {
        // Initialize profile values with empty strings
        profileValues.clear();
        for (int i = 0; i < profileHeaderArray.length; i++) {
            profileValues.add("");
        }

        adapter = new StudentProfileAdapter(getApplicationContext(), profileHeaderArray, profileValues, new HashMap<>());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
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
        pd.setMessage("Loading Profile...");
        pd.setCancelable(false);
        pd.show();

        // Use GET method with staff_id in URL path
        String teacherId = TeacherAuthHelper.getTeacherId(this);
        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl") + Constants.teacherProfileUrl + "/" + teacherId;
        Log.e("Teacher Profile URL", url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Teacher Profile Result", result);
                        JSONObject object = new JSONObject(result);
                        String status = object.getString("status");
                        String message = object.getString("message");

                        if (status.equals("1")) {
                            // Profile retrieved successfully - parse comprehensive response
                            parseComprehensiveProfile(object);
                        } else {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error parsing profile data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("Teacher Profile Volley Error", volleyError.toString());
                Toast.makeText(TeacherProfile.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", TeacherAuthHelper.getTeacherId(TeacherProfile.this));
                headers.put("Authorization", TeacherAuthHelper.getTeacherJwtToken(TeacherProfile.this));
                Log.e("Teacher Profile Headers", headers.toString());
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(TeacherProfile.this);
        requestQueue.add(stringRequest);
    }

    private void parseComprehensiveProfile(JSONObject response) {
        try {
            // Parse all sections of the comprehensive profile response
            basicInfo = response.optJSONObject("basic_info");
            contactInfo = response.optJSONObject("contact_info");
            personalInfo = response.optJSONObject("personal_info");
            addressInfo = response.optJSONObject("address_info");
            bankDetails = response.optJSONObject("bank_details");
            socialMedia = response.optJSONObject("social_media");
            qrCodeData = response.optJSONObject("qr_code");

            // Update header information
            updateHeaderInformation();

            // Update comprehensive profile values
            updateComprehensiveProfileValues();

            // Load profile image
            String profileImageUrl = response.optString("profile_image", "");
            loadProfileImageFromUrl(profileImageUrl);

            // Load QR code
            loadQRCode();

            // Cache important data
            cacheProfileData();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Teacher Profile Parse", "Error parsing comprehensive profile: " + e.getMessage());
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
        if (qrCodeData != null) {
            String qrCodeUrl = qrCodeData.optString("qr_code_url", "");
            if (!qrCodeUrl.isEmpty()) {
                Log.e("Teacher QR Code URL", qrCodeUrl);

                // Load QR code image exactly like student profile
                Picasso.with(getApplicationContext()).load(qrCodeUrl).into(qrcodeIV);

                // Set up click listener exactly like student profile
                qrcodeIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showAddDialog(TeacherProfile.this, qrCodeUrl, "QR Code");
                    }
                });

                // Show QR code layout
                qrcode_layout.setVisibility(View.VISIBLE);
            } else {
                // Hide QR code layout if no QR code available
                qrcode_layout.setVisibility(View.GONE);
            }
        } else {
            qrcode_layout.setVisibility(View.GONE);
        }
    }

    private void cacheProfileData() {
        try {
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

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Teacher Profile Cache", "Error caching profile data: " + e.getMessage());
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
}
