package com.qdocs.ssre241123.teachers;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.ssre241123.BaseActivity;
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.StudentListAdapter;
import com.qdocs.ssre241123.model.Student;
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

public class TeacherStudentDetailsActivity extends BaseActivity {

    private static final String TAG = "TeacherStudentDetails";

    private FrameLayout actionBar;
    private ImageView backButton;
    private TextView titleTextView;
    private Spinner sessionSpinner;
    private Spinner classSpinner;
    private Spinner sectionSpinner;
    private Button applyFilterButton;
    private RecyclerView studentsRecyclerView;
    private ProgressBar progressBar;
    private LinearLayout nodataLayout;
    private TextView studentCountTextView;

    private StudentListAdapter adapter;
    private List<Student> studentList;

    // Data structures for API data
    private List<SessionData> sessionsList;
    private List<ClassData> classesList;
    private List<SectionData> sectionsList;

    private String selectedSessionId;
    private String selectedClassId;
    private String selectedSectionId;

    // Inner classes for data structures
    private static class SessionData {
        String id;
        String name;
        List<ClassData> classes;

        SessionData(String id, String name) {
            this.id = id;
            this.name = name;
            this.classes = new ArrayList<>();
        }
    }

    private static class ClassData {
        String id;
        String name;
        List<SectionData> sections;

        ClassData(String id, String name) {
            this.id = id;
            this.name = name;
            this.sections = new ArrayList<>();
        }
    }

    private static class SectionData {
        String id;
        String name;

        SectionData(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_student_details);

        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), Constants.langCode));

        initializeViews();
        setupTheme();
        setupRecyclerView();
        setupListeners();

        // Load sessions from API
        loadSessionsFromAPI();
    }

    private void initializeViews() {
        actionBar = findViewById(R.id.actionBar);
        backButton = findViewById(R.id.back_button);
        titleTextView = findViewById(R.id.title);
        sessionSpinner = findViewById(R.id.session_spinner);
        classSpinner = findViewById(R.id.class_spinner);
        sectionSpinner = findViewById(R.id.section_spinner);
        applyFilterButton = findViewById(R.id.apply_filter_button);
        studentsRecyclerView = findViewById(R.id.students_recyclerView);
        progressBar = findViewById(R.id.progressBar);
        nodataLayout = findViewById(R.id.nodata_layout);
        studentCountTextView = findViewById(R.id.student_count);

        titleTextView.setText("Student Details");

        // Initialize data structures
        sessionsList = new ArrayList<>();
        classesList = new ArrayList<>();
        sectionsList = new ArrayList<>();
    }

    private void setupTheme() {
        String primaryColor = Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour);
        String secondaryColor = Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour);

        if (primaryColor != null && !primaryColor.isEmpty()) {
            try {
                actionBar.setBackgroundColor(Color.parseColor(secondaryColor));
                applyFilterButton.setBackgroundColor(Color.parseColor(primaryColor));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.parseColor(primaryColor));
                }
            } catch (Exception e) {
                Log.e(TAG, "Error applying theme colors", e);
            }
        }
    }

    private void setupRecyclerView() {
        studentList = new ArrayList<>();
        adapter = new StudentListAdapter(this, studentList);
        studentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentsRecyclerView.setAdapter(adapter);

        adapter.setOnStudentClickListener((student, position) -> {
            Toast.makeText(TeacherStudentDetailsActivity.this,
                    "Clicked: " + student.getFullName() + "\nAdmission No: " + student.getAdmissionNo(),
                    Toast.LENGTH_SHORT).show();
        });
    }

    private void setupListeners() {
        backButton.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_rightleft, R.anim.no_animation);
        });

        applyFilterButton.setOnClickListener(v -> applyFilters());

        sessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && sessionsList.size() > position - 1) {
                    SessionData session = sessionsList.get(position - 1);
                    selectedSessionId = session.id;
                    loadClassesForSession(session);
                } else {
                    selectedSessionId = null;
                    classesList.clear();
                    sectionsList.clear();
                    setupClassSpinner();
                    setupSectionSpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSessionId = null;
            }
        });

        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && classesList.size() > position - 1) {
                    ClassData classData = classesList.get(position - 1);
                    selectedClassId = classData.id;
                    loadSectionsForClass(classData);
                } else {
                    selectedClassId = null;
                    sectionsList.clear();
                    setupSectionSpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedClassId = null;
            }
        });

        sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && sectionsList.size() > position - 1) {
                    SectionData section = sectionsList.get(position - 1);
                    selectedSectionId = section.id;
                } else {
                    selectedSectionId = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSectionId = null;
            }
        });
    }

    private void applyFilters() {
        if (selectedSessionId == null || selectedClassId == null || selectedSectionId == null) {
            Toast.makeText(this, "Please select all filters", Toast.LENGTH_SHORT).show();
            return;
        }

        loadStudentsFromAPI();
    }

    private void loadSessionsFromAPI() {
        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            Toast.makeText(this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading sessions...");
        pd.setCancelable(false);
        pd.show();

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl") + Constants.teacherSessionsWithClassesSectionsUrl;
        Log.d(TAG, "Sessions API URL: " + url);

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("include_inactive", false);
        } catch (JSONException e) {
            Log.e(TAG, "Error creating request body", e);
        }

        final String requestBodyString = requestBody.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    pd.dismiss();
                    Log.d(TAG, "Sessions API Response: " + response);
                    parseSessionsResponse(response);
                },
                error -> {
                    pd.dismiss();
                    Log.e(TAG, "Sessions API Error: " + error.toString());
                    Toast.makeText(TeacherStudentDetailsActivity.this,
                            "Error loading sessions: " + error.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBodyString.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    VolleyLog.wtf("Unsupported Encoding");
                    return null;
                }
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseSessionsResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.getInt("status");

            if (status == 1) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                sessionsList.clear();

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject sessionObj = dataArray.getJSONObject(i);
                    String sessionId = sessionObj.getString("session_id");
                    String sessionName = sessionObj.getString("session_name");

                    SessionData session = new SessionData(sessionId, sessionName);

                    // Parse classes for this session
                    if (sessionObj.has("classes")) {
                        JSONArray classesArray = sessionObj.getJSONArray("classes");
                        for (int j = 0; j < classesArray.length(); j++) {
                            JSONObject classObj = classesArray.getJSONObject(j);
                            String classId = classObj.getString("class_id");
                            String className = classObj.getString("class_name");

                            ClassData classData = new ClassData(classId, className);

                            // Parse sections for this class
                            if (classObj.has("sections")) {
                                JSONArray sectionsArray = classObj.getJSONArray("sections");
                                for (int k = 0; k < sectionsArray.length(); k++) {
                                    JSONObject sectionObj = sectionsArray.getJSONObject(k);
                                    String sectionId = sectionObj.getString("section_id");
                                    String sectionName = sectionObj.getString("section_name");

                                    SectionData section = new SectionData(sectionId, sectionName);
                                    classData.sections.add(section);
                                }
                            }

                            session.classes.add(classData);
                        }
                    }

                    sessionsList.add(session);
                }

                setupSessionSpinner();
            } else {
                String message = jsonObject.optString("message", "Failed to load sessions");
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing sessions response", e);
            Toast.makeText(this, "Error parsing sessions data", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupSessionSpinner() {
        List<String> sessionNames = new ArrayList<>();
        sessionNames.add("Select Session");
        for (SessionData session : sessionsList) {
            sessionNames.add(session.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sessionNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sessionSpinner.setAdapter(adapter);
    }

    private void loadClassesForSession(SessionData session) {
        classesList.clear();
        classesList.addAll(session.classes);
        setupClassSpinner();

        // Clear sections
        sectionsList.clear();
        setupSectionSpinner();
    }

    private void setupClassSpinner() {
        List<String> classNames = new ArrayList<>();
        classNames.add("Select Class");
        for (ClassData classData : classesList) {
            classNames.add(classData.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter);
    }

    private void loadSectionsForClass(ClassData classData) {
        sectionsList.clear();
        sectionsList.addAll(classData.sections);
        setupSectionSpinner();
    }

    private void setupSectionSpinner() {
        List<String> sectionNames = new ArrayList<>();
        sectionNames.add("Select Section");
        for (SectionData section : sectionsList) {
            sectionNames.add(section.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sectionNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectionSpinner.setAdapter(adapter);
    }

    private void loadStudentsFromAPI() {
        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            Toast.makeText(this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading();

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl") + Constants.teacherStudentsUrl;
        Log.d(TAG, "Students API URL: " + url);

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("class_id", selectedClassId);
            requestBody.put("section_id", selectedSectionId);
            requestBody.put("session_id", selectedSessionId);
        } catch (JSONException e) {
            Log.e(TAG, "Error creating request body", e);
        }

        final String requestBodyString = requestBody.toString();
        Log.d(TAG, "Request Body: " + requestBodyString);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "Students API Response: " + response);
                    parseStudentsResponse(response);
                },
                error -> {
                    showNoData();
                    Log.e(TAG, "Students API Error: " + error.toString());
                    Toast.makeText(TeacherStudentDetailsActivity.this,
                            "Error loading students: " + error.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBodyString.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    VolleyLog.wtf("Unsupported Encoding");
                    return null;
                }
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void parseStudentsResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.getInt("status");

            if (status == 1) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                studentList.clear();

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject studentObj = dataArray.getJSONObject(i);

                    Student student = new Student();
                    student.setId(studentObj.optString("student_id", ""));
                    student.setStudentSessionId(studentObj.optString("student_session_id", ""));
                    student.setAdmissionNo(studentObj.optString("admission_no", ""));
                    student.setRollNo(studentObj.optString("roll_no", ""));
                    student.setFirstName(studentObj.optString("firstname", ""));
                    student.setMiddleName(studentObj.optString("middlename", ""));
                    student.setLastName(studentObj.optString("lastname", ""));
                    student.setFullName(studentObj.optString("full_name", ""));
                    student.setGender(studentObj.optString("gender", ""));
                    student.setDateOfBirth(studentObj.optString("dob", ""));
                    student.setEmail(studentObj.optString("email", ""));
                    student.setMobileNo(studentObj.optString("mobileno", ""));
                    student.setBloodGroup(studentObj.optString("blood_group", ""));
                    student.setImage(studentObj.optString("profile_image", ""));
                    student.setCategoryId(studentObj.optString("category_id", ""));
                    student.setIsActive(studentObj.optString("is_active", ""));

                    // Parse class_info
                    if (studentObj.has("class_info")) {
                        JSONObject classInfo = studentObj.getJSONObject("class_info");
                        student.setClassId(classInfo.optString("class_id", ""));
                        student.setClassName(classInfo.optString("class_name", ""));
                        student.setSectionId(classInfo.optString("section_id", ""));
                        student.setSectionName(classInfo.optString("section_name", ""));
                        student.setSessionId(classInfo.optString("session_id", ""));
                        student.setSessionName(classInfo.optString("session_name", ""));
                    }

                    // Parse guardian_info
                    if (studentObj.has("guardian_info")) {
                        JSONObject guardianInfo = studentObj.getJSONObject("guardian_info");
                        student.setFatherName(guardianInfo.optString("father_name", ""));
                        student.setFatherPhone(guardianInfo.optString("father_phone", ""));
                        student.setMotherName(guardianInfo.optString("mother_name", ""));
                        student.setMotherPhone(guardianInfo.optString("mother_phone", ""));
                        student.setGuardianName(guardianInfo.optString("guardian_name", ""));
                        student.setGuardianPhone(guardianInfo.optString("guardian_phone", ""));
                        student.setGuardianRelation(guardianInfo.optString("guardian_relation", ""));
                    }

                    // Parse address_info
                    if (studentObj.has("address_info")) {
                        JSONObject addressInfo = studentObj.getJSONObject("address_info");
                        student.setCurrentAddress(addressInfo.optString("current_address", ""));
                        student.setPermanentAddress(addressInfo.optString("permanent_address", ""));
                    }

                    studentList.add(student);
                }

                if (studentList.isEmpty()) {
                    showNoData();
                } else {
                    showContent();
                    adapter.notifyDataSetChanged();
                    studentCountTextView.setText("Total Students: " + studentList.size());
                }
            } else {
                showNoData();
                String message = jsonObject.optString("message", "No students found");
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            showNoData();
            Log.e(TAG, "Error parsing students response", e);
            Toast.makeText(this, "Error parsing students data", Toast.LENGTH_SHORT).show();
        }
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        studentsRecyclerView.setVisibility(View.GONE);
        nodataLayout.setVisibility(View.GONE);
    }

    private void showContent() {
        progressBar.setVisibility(View.GONE);
        studentsRecyclerView.setVisibility(View.VISIBLE);
        nodataLayout.setVisibility(View.GONE);
    }

    private void showNoData() {
        progressBar.setVisibility(View.GONE);
        studentsRecyclerView.setVisibility(View.GONE);
        nodataLayout.setVisibility(View.VISIBLE);
        studentCountTextView.setText("Total Students: 0");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_rightleft, R.anim.no_animation);
    }
}

