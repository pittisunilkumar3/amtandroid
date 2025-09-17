package com.qdocs.ssre241123.teachers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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
    private RecyclerView recyclerView;
    private StudentProfileAdapter adapter;
    private CardView cardViewOuter;
    private Map<String, String> headers = new HashMap<String, String>();

    // Profile data arrays for RecyclerView
    private ArrayList<String> profileValues = new ArrayList<>();
    private int[] profileHeaderArray = {
        R.string.email,
        R.string.phone,
        R.string.designation,
        R.string.department
    };

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
        recyclerView = findViewById(R.id.teacher_profile_recyclerView);
        cardViewOuter = findViewById(R.id.card_view_outer);

        // Set theme color for outer card
        cardViewOuter.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
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

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl") + Constants.teacherProfileUrl;
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
                            // Profile retrieved successfully
                            JSONObject data = object.getJSONObject("data");
                            updateProfileUI(data);
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

    private void updateProfileUI(JSONObject data) {
        try {
            String name = data.optString("name", "") + " " + data.optString("surname", "");
            String employeeId = data.optString("employee_id", "");
            String email = data.optString("email", "");
            String phone = data.optString("phone", "");
            String designation = data.optString("designation", "");
            String department = data.optString("department", "");

            // Update header information
            nameTV.setText(name.trim());
            designationTV.setText(designation);
            employeeIdTV.setText(employeeId);

            // Update profile values for RecyclerView
            updateProfileValues(email, phone, designation, department);

            // Update cached data
            Utility.setSharedPreference(this, Constants.teacherName, data.optString("name", ""));
            Utility.setSharedPreference(this, Constants.teacherSurname, data.optString("surname", ""));
            Utility.setSharedPreference(this, Constants.teacherEmployeeId, employeeId);
            Utility.setSharedPreference(this, Constants.teacherEmail, email);
            Utility.setSharedPreference(this, Constants.teacherContact, phone);
            Utility.setSharedPreference(this, Constants.teacherDesignation, designation);
            Utility.setSharedPreference(this, Constants.teacherDepartment, department);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Teacher Profile Update", "Error updating profile UI: " + e.getMessage());
        }
    }
}
