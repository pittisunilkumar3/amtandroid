package com.qdocs.ssre241123.teachers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.qdocs.ssre241123.Login;
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.model.TeacherModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class TeacherLogin extends AppCompatActivity {

    TextView goback, tv_forgotPass;
    LinearLayout teacher_btn_login;
    EditText et_userName, et_password;
    ImageView btn_showPassword;
    boolean isPasswordVisible = false;
    String device_token;
    
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_login);

        initializeViews();
        setupClickListeners();

        // Ensure API URL is set like in student login
        String apiUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        if (apiUrl == null || apiUrl.isEmpty()) {
            apiUrl = Constants.domain + "/api/";
            Utility.setSharedPreference(getApplicationContext(), "apiUrl", apiUrl);
            Log.e("API URL Set", apiUrl);
        }
        Log.e("Current API URL", apiUrl);

        // Get Firebase device token
        try {
            device_token = FirebaseInstanceId.getInstance().getToken();
            if (device_token == null) {
                device_token = "";
            }
            Log.e("DEVICE TOKEN", device_token);
        } catch (Exception e) {
            device_token = "";
            Log.e("DEVICE TOKEN ERROR", e.getMessage());
        }

        // For testing - add demo credentials (remove this in production)
        if (Constants.isDemoModeOn) {
            et_userName.setText("teacher@gmail.com");
            et_password.setText("teacher");
        }

        // For testing - you can remove this later
        if (Constants.isDemoModeOn) {
            et_userName.setText("tulasiganasala123@gmail.com");
            et_password.setText("1984");
        }
    }

    private void initializeViews() {
        teacher_btn_login = findViewById(R.id.teacher_btn_login);
        goback = findViewById(R.id.go_back_to_login_text);
        et_userName = findViewById(R.id.et_username_login);
        et_password = findViewById(R.id.et_password_login);
        btn_showPassword = findViewById(R.id.login_password_visibleBtn);
        tv_forgotPass = findViewById(R.id.tv_passwordReset_login);
    }

    private void setupClickListeners() {
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        teacher_btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        btn_showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPasswordVisible) {
                    et_password.setTransformationMethod(null);
                    btn_showPassword.setImageResource(R.drawable.eye_black);
                    isPasswordVisible = true;
                } else {
                    et_password.setTransformationMethod(new PasswordTransformationMethod());
                    btn_showPassword.setImageResource(R.drawable.eyehide);
                    isPasswordVisible = false;
                }
            }
        });
    }

    private void performLogin() {
        String email = et_userName.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Utility.isConnectingToInternet(getApplicationContext())) {
            params.clear(); // Clear any previous params
            params.put("email", email);
            params.put("password", password);
            // Add deviceToken like student login - always add it

            JSONObject obj = new JSONObject(params);
            Log.e("Teacher Login params", obj.toString());
            System.out.println("Teacher Login Details==" + obj.toString());
            getDataFromApi(obj.toString());
        } else {
            Toast.makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_LONG).show();
        }
    }

    private void getDataFromApi(String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        // Get API URL - use the same logic as student login
        String apiUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        if (apiUrl == null || apiUrl.isEmpty()) {
            // If no API URL is set, use the default domain + api/
            apiUrl = Constants.domain + "/api/";
            Utility.setSharedPreference(getApplicationContext(), "apiUrl", apiUrl);
        }

        String url = apiUrl + Constants.teacherLoginUrl;
        Log.e("Teacher Login URL", url);
        Log.e("Teacher Login Body", requestBody);
        
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);
                        String status = object.getString("status");
                        String message = object.getString("message");

                        if (status.equals("1")) {
                            // Login successful
                            String id = object.getString("id");
                            String token = object.getString("token");
                            // Handle null jwt_token properly
                            String jwt_token = "";
                            if (!object.isNull("jwt_token") && object.get("jwt_token") != null) {
                                jwt_token = object.getString("jwt_token");
                            }
                            String role = object.getString("role");

                            JSONObject record = object.getJSONObject("record");

                            // Save teacher data to SharedPreferences
                            saveTeacherData(id, token, jwt_token, record);

                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                            // Navigate to Teacher Dashboard
                            Intent intent = new Intent(getApplicationContext(), TeacherDashboard.class);
                            startActivity(intent);
                            finish();

                        } else {
                            // Login failed - status is "0"
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            Log.e("Login Error", "Login failed: " + message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "No response from server", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("Teacher Login Volley Error", volleyError.toString());
                if (volleyError.networkResponse != null) {
                    Log.e("Teacher Login Error Code", String.valueOf(volleyError.networkResponse.statusCode));
                    if (volleyError.networkResponse.data != null) {
                        Log.e("Teacher Login Error Data", new String(volleyError.networkResponse.data));
                    }
                }
                Toast.makeText(TeacherLogin.this, R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.put("Client-Service", "smartschool");
                headers.put("Auth-Key", "schoolAdmin@");
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };

        // Add retry policy like student login
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(TeacherLogin.this);
        requestQueue.add(stringRequest);
    }

    private void saveTeacherData(String id, String token, String jwt_token, JSONObject record) {
        try {
            // Set teacher login status
            Utility.setSharedPreferenceBoolean(getApplicationContext(), Constants.isTeacherLoggedIn, true);

            // Save authentication data
            Utility.setSharedPreference(getApplicationContext(), Constants.teacherId, id);
            Utility.setSharedPreference(getApplicationContext(), Constants.teacherToken, token);
            Utility.setSharedPreference(getApplicationContext(), Constants.teacherJwtToken, jwt_token != null ? jwt_token : "");

            // Save teacher profile data with null checks
            Utility.setSharedPreference(getApplicationContext(), Constants.teacherStaffId, record.getString("staff_id"));
            Utility.setSharedPreference(getApplicationContext(), Constants.teacherEmployeeId, record.getString("employee_id"));
            Utility.setSharedPreference(getApplicationContext(), Constants.teacherName, record.getString("name"));
            Utility.setSharedPreference(getApplicationContext(), Constants.teacherSurname, getStringOrEmpty(record, "surname"));
            Utility.setSharedPreference(getApplicationContext(), Constants.teacherEmail, record.getString("email"));
            Utility.setSharedPreference(getApplicationContext(), Constants.teacherContact, getStringOrEmpty(record, "contact_no"));
            Utility.setSharedPreference(getApplicationContext(), Constants.teacherDesignation, getStringOrEmpty(record, "designation"));
            Utility.setSharedPreference(getApplicationContext(), Constants.teacherDepartment, getStringOrEmpty(record, "department"));
            Utility.setSharedPreference(getApplicationContext(), Constants.teacherImage, getStringOrEmpty(record, "image"));
            Utility.setSharedPreference(getApplicationContext(), Constants.userName, record.getString("username"));
            Utility.setSharedPreference(getApplicationContext(), Constants.loginType, "teacher");

            // Save additional fields from your API response with null checks
            Utility.setSharedPreference(getApplicationContext(), "date_format", getStringOrEmpty(record, "date_format"));
            Utility.setSharedPreference(getApplicationContext(), "currency_symbol", getStringOrEmpty(record, "currency_symbol"));
            Utility.setSharedPreference(getApplicationContext(), "currency_short_name", getStringOrEmpty(record, "currency_short_name"));
            Utility.setSharedPreference(getApplicationContext(), "currency_id", getStringOrEmpty(record, "currency_id"));
            Utility.setSharedPreference(getApplicationContext(), "timezone", getStringOrEmpty(record, "timezone"));
            Utility.setSharedPreference(getApplicationContext(), "sch_name", getStringOrEmpty(record, "sch_name"));
            Utility.setSharedPreference(getApplicationContext(), "is_rtl", getStringOrEmpty(record, "is_rtl"));
            Utility.setSharedPreference(getApplicationContext(), "theme", getStringOrEmpty(record, "theme"));
            Utility.setSharedPreference(getApplicationContext(), "start_week", getStringOrEmpty(record, "start_week"));
            Utility.setSharedPreference(getApplicationContext(), "superadmin_restriction", getStringOrEmpty(record, "superadmin_restriction"));

            // Save language data with null checks
            if (!record.isNull("language")) {
                JSONObject language = record.getJSONObject("language");
                Utility.setSharedPreference(getApplicationContext(), "lang_id", getStringOrEmpty(language, "lang_id"));
                Utility.setSharedPreference(getApplicationContext(), "language", getStringOrEmpty(language, "language"));
                Utility.setSharedPreference(getApplicationContext(), "short_code", getStringOrEmpty(language, "short_code"));
            }

            Log.d("SaveTeacherData", "Teacher data saved successfully");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("SaveTeacherData", "Error saving teacher data: " + e.getMessage());
        }
    }

    // Helper method to handle null values from JSON
    private String getStringOrEmpty(JSONObject jsonObject, String key) {
        try {
            if (jsonObject.isNull(key) || jsonObject.get(key) == null) {
                return "";
            }
            return jsonObject.getString(key);
        } catch (JSONException e) {
            return "";
        }
    }
}
