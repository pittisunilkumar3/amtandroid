package com.qdocs.ssre241123.teachers;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.ssre241123.BaseActivity;
import com.qdocs.ssre241123.R;
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
 * Base activity for all filter-based reports that need Session/Class/Section dropdowns.
 * Subclasses just override getApiEndpoint(), parseResponse(), and setupAdapter().
 */
public abstract class BaseFilterReportActivity extends BaseActivity {

    protected static final String TAG = "BaseFilterReport";

    protected Spinner sessionSpinner, classSpinner, sectionSpinner;
    protected Button generateReportButton;
    protected RecyclerView recyclerView;
    protected ProgressBar progressBar;
    protected LinearLayout nodataLayout;
    protected View actionBarView;
    protected TextView titleTextView;

    protected List<String> sessionNames = new ArrayList<>();
    protected List<String> sessionIds = new ArrayList<>();
    protected List<String> classNames = new ArrayList<>();
    protected List<String> classIds = new ArrayList<>();
    protected List<String> sectionNames = new ArrayList<>();
    protected List<String> sectionIds = new ArrayList<>();

    protected String selectedSessionId = "";
    protected String selectedClassId = "";
    protected String selectedSectionId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());

        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), "langCode"));

        initViews();
        setupRecyclerView();
        loadSessions();
    }

    protected abstract int getLayoutResourceId();

    /**
     * Return the API endpoint for filter (e.g., "staff-report/filter")
     */
    protected abstract String getApiEndpoint();

    protected void initViews() {
        // Subclasses should override to find their specific views
        // but call super for standard ones
    }

    protected void setupRecyclerView() {
        // Subclasses override
    }

    protected void loadSessions() {
        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + "teacher/sessions-with-classes-sections";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.optInt("status") == 1) {
                            JSONArray data = json.optJSONArray("data");
                            if (data != null) {
                                sessionNames.clear();
                                sessionIds.clear();
                                sessionNames.add("Select Session");
                                sessionIds.add("");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject obj = data.getJSONObject(i);
                                    sessionNames.add(obj.optString("session_name"));
                                    sessionIds.add(obj.optString("session_id"));
                                }
                                setupSessionSpinner();
                            }
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Parse error", e);
                    }
                },
                error -> Log.e(TAG, "Error loading sessions", error)) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return new JSONObject().toString().getBytes();
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    protected void setupSessionSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sessionNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sessionSpinner.setAdapter(adapter);

        sessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedSessionId = sessionIds.get(position);
                    loadClasses();
                } else {
                    selectedSessionId = "";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    protected void loadClasses() {
        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + "teacher/classes-with-sections";

        try {
            JSONObject body = new JSONObject();
            if (!selectedSessionId.isEmpty()) {
                body.put("session_id", selectedSessionId);
            }

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    response -> {
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.optInt("status") == 1) {
                                JSONArray data = json.optJSONArray("data");
                                if (data != null) {
                                    classNames.clear();
                                    classIds.clear();
                                    classNames.add("Select Class");
                                    classIds.add("");
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject obj = data.getJSONObject(i);
                                        classNames.add(obj.optString("class_name", obj.optString("class")));
                                        classIds.add(obj.optString("class_id", obj.optString("id")));
                                    }
                                    setupClassSpinner();
                                }
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Parse error", e);
                        }
                    },
                    error -> Log.e(TAG, "Error loading classes", error)) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Client-Service", Constants.clientService);
                    headers.put("Auth-Key", Constants.authKey);
                    headers.put("Content-Type", "application/json");
                    return headers;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    return body.toString().getBytes();
                }
            };
            Volley.newRequestQueue(this).add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void setupClassSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, classNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter);

        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedClassId = classIds.get(position);
                    loadSections();
                } else {
                    selectedClassId = "";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    protected void loadSections() {
        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + "class-sections/list";

        try {
            JSONObject body = new JSONObject();
            body.put("class_id", selectedClassId);

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    response -> {
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray data = json.optJSONArray("data");
                            if (data != null) {
                                sectionNames.clear();
                                sectionIds.clear();
                                sectionNames.add("Select Section");
                                sectionIds.add("");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject obj = data.getJSONObject(i);
                                    sectionNames.add(obj.optString("section_name", obj.optString("section")));
                                    sectionIds.add(obj.optString("section_id", obj.optString("id")));
                                }
                                setupSectionSpinner();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Parse error", e);
                        }
                    },
                    error -> Log.e(TAG, "Error loading sections", error)) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Client-Service", Constants.clientService);
                    headers.put("Auth-Key", Constants.authKey);
                    headers.put("Content-Type", "application/json");
                    return headers;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    return body.toString().getBytes();
                }
            };
            Volley.newRequestQueue(this).add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void setupSectionSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sectionNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectionSpinner.setAdapter(adapter);

        sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSectionId = position > 0 ? sectionIds.get(position) : "";
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    protected void generateReport() {
        if (!Utility.isConnectingToInternet(getApplicationContext())) {
            Toast.makeText(this, R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        String baseUrl = Utility.getSharedPreferences(getApplicationContext(), "apiUrl");
        String url = baseUrl + getApiEndpoint();

        showLoading();

        try {
            JSONObject body = buildRequestBody();

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    response -> {
                        hideLoading();
                        Log.d(TAG, "Response: " + response);
                        parseResponse(response);
                    },
                    error -> {
                        hideLoading();
                        Log.e(TAG, "Error: " + error.toString());
                        Toast.makeText(this, "Error loading report", Toast.LENGTH_SHORT).show();
                        showNoData();
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Client-Service", Constants.clientService);
                    headers.put("Auth-Key", Constants.authKey);
                    headers.put("Content-Type", "application/json");
                    return headers;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    return body.toString().getBytes();
                }
            };

            Volley.newRequestQueue(this).add(request);
        } catch (JSONException e) {
            hideLoading();
            e.printStackTrace();
        }
    }

    protected JSONObject buildRequestBody() throws JSONException {
        JSONObject body = new JSONObject();
        if (!selectedSessionId.isEmpty()) body.put("session_id", selectedSessionId);
        if (!selectedClassId.isEmpty()) body.put("class_id", selectedClassId);
        if (!selectedSectionId.isEmpty()) body.put("section_id", selectedSectionId);
        return body;
    }

    protected void parseResponse(String response) {
        // Override in subclasses
        try {
            JSONObject json = new JSONObject(response);
            if (json.optInt("status") == 1) {
                JSONArray data = json.optJSONArray("data");
                if (data != null && data.length() > 0) {
                    showContent();
                    populateData(data);
                } else {
                    showNoData();
                    Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
                }
            } else {
                showNoData();
                Toast.makeText(this, json.optString("message", "No data found"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            showNoData();
            Log.e(TAG, "JSON Error", e);
        }
    }

    protected void populateData(JSONArray data) {
        // Override in subclasses
    }

    protected void showLoading() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        if (nodataLayout != null) nodataLayout.setVisibility(View.GONE);
        if (recyclerView != null) recyclerView.setVisibility(View.GONE);
    }

    protected void hideLoading() {
        if (progressBar != null) progressBar.setVisibility(View.GONE);
    }

    protected void showNoData() {
        if (nodataLayout != null) nodataLayout.setVisibility(View.VISIBLE);
        if (recyclerView != null) recyclerView.setVisibility(View.GONE);
    }

    protected void showContent() {
        if (nodataLayout != null) nodataLayout.setVisibility(View.GONE);
        if (recyclerView != null) recyclerView.setVisibility(View.VISIBLE);
    }

    protected void setupBackButton() {
        View backBtn = findViewById(R.id.back_button);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> {
                finish();
                overridePendingTransition(R.anim.slide_rightleft, R.anim.no_animation);
            });
        }
    }

    protected void applyThemeColor(View... views) {
        String primaryColor = Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour);
        if (primaryColor != null && !primaryColor.isEmpty()) {
            try {
                for (View view : views) {
                    if (view != null) view.setBackgroundColor(android.graphics.Color.parseColor(primaryColor));
                }
            } catch (Exception e) {}
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_rightleft, R.anim.no_animation);
    }
}
