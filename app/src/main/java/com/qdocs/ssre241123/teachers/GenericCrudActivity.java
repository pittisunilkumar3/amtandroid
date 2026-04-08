package com.qdocs.ssre241123.teachers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
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
import com.qdocs.ssre241123.adapters.GenericCrudAdapter;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generic CRUD Activity that can be configured via Intent extras to work with any API
 * that follows the standard list/get/create/update/delete pattern.
 *
 * Required Intent Extras:
 *   screen_title - Title shown in action bar
 *   list_url - API endpoint for listing (POST)
 *   create_url - API endpoint for creating (POST)
 *   update_url - API endpoint for updating (POST)
 *   delete_url - API endpoint for deleting (POST)
 *   name_field - JSON field name for the display text (e.g. "department_name", "class")
 *   id_field - JSON field name for the ID (default: "id")
 *
 * Optional Intent Extras:
 *   name_field_label - Label shown above the input field (default: name_field)
 *   show_active_switch - Whether to show active/inactive switch (default: false)
 *   active_field - JSON field name for active status (default: "is_active")
 *   extra_fields - JSON string of extra fields [{"field":"desc","label":"Description","type":"text"}]
 */
public class GenericCrudActivity extends BaseActivity {

    private static final String TAG = "GenericCrud";

    // Config from intent
    private String screenTitle;
    private String listUrl;
    private String createUrl;
    private String updateUrl;
    private String deleteUrl;
    private String nameField;
    private String idField = "id";
    private String nameFieldLabel;
    private boolean showActiveSwitch = false;
    private String activeField = "is_active";
    private String extraFieldsJson;

    // UI
    private ImageView backButton;
    private TextView titleTextView;
    private ProgressBar progressBar;
    private LinearLayout nodataLayout;
    private RecyclerView recyclerView;
    private LinearLayout formLayout;
    private EditText nameInput;
    private SwitchCompat activeSwitch;
    private Button saveButton;
    private Button cancelButton;
    private TextView countTextView;

    // Data
    private List<HashMap<String, String>> itemsList;
    private GenericCrudAdapter adapter;
    private RequestQueue requestQueue;
    private boolean isEditMode = false;
    private int editPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_crud);

        readConfig();
        initializeViews();
        setupRecyclerView();
        setupListeners();
        loadData();
    }

    private void readConfig() {
        Bundle extras = getIntent().getExtras();
        screenTitle = extras.getString("screen_title", "Manage");
        listUrl = extras.getString("list_url", "");
        createUrl = extras.getString("create_url", "");
        updateUrl = extras.getString("update_url", "");
        deleteUrl = extras.getString("delete_url", "");
        nameField = extras.getString("name_field", "name");
        idField = extras.getString("id_field", "id");
        nameFieldLabel = extras.getString("name_field_label", nameField.replace("_", " "));
        showActiveSwitch = extras.getBoolean("show_active_switch", false);
        activeField = extras.getString("active_field", "is_active");
        extraFieldsJson = extras.getString("extra_fields", null);
    }

    private void initializeViews() {
        backButton = findViewById(R.id.crud_back_button);
        titleTextView = findViewById(R.id.crud_title_textview);
        progressBar = findViewById(R.id.crud_progress_bar);
        nodataLayout = findViewById(R.id.crud_nodata_layout);
        recyclerView = findViewById(R.id.crud_recyclerView);
        formLayout = findViewById(R.id.crud_form_layout);
        nameInput = findViewById(R.id.crud_name_input);
        activeSwitch = findViewById(R.id.crud_active_switch);
        saveButton = findViewById(R.id.crud_save_button);
        cancelButton = findViewById(R.id.crud_cancel_button);
        countTextView = findViewById(R.id.crud_count_textview);

        titleTextView.setText(screenTitle);
        if (!showActiveSwitch) {
            activeSwitch.setVisibility(View.GONE);
        }

        // Set hint
        String[] words = nameFieldLabel.split(" ");
        StringBuilder hint = new StringBuilder("Enter ");
        for (String w : words) hint.append(Character.toUpperCase(w.charAt(0))).append(w.length() > 1 ? w.substring(1) : "").append(" ");
        nameInput.setHint(hint.toString().trim());

        requestQueue = Volley.newRequestQueue(this);
    }

    private void setupRecyclerView() {
        itemsList = new ArrayList<>();
        adapter = new GenericCrudAdapter(this, itemsList, nameField, idField);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnActionListener(new GenericCrudAdapter.OnActionListener() {
            @Override
            public void onEdit(int position) {
                enterEditMode(position);
            }

            @Override
            public void onDelete(int position) {
                showDeleteDialog(position);
            }

            @Override
            public void onToggleActive(int position, boolean isActive) {
                toggleActiveStatus(position, isActive);
            }
        });
    }

    private void setupListeners() {
        backButton.setOnClickListener(v -> onBackPressed());
        saveButton.setOnClickListener(v -> saveItem());
        cancelButton.setOnClickListener(v -> resetForm());

        // Add header layout with FAB-like button
        LinearLayout headerLayout = findViewById(R.id.crud_header_layout);
        if (headerLayout != null) {
            View addButton = findViewById(R.id.crud_add_button);
            if (addButton != null) {
                addButton.setOnClickListener(v -> {
                    findViewById(R.id.crud_form_card).setVisibility(View.VISIBLE);
                    formLayout.setVisibility(View.VISIBLE);
                    resetForm();
                    nameInput.requestFocus();
                });
            }
        }
    }

    private void loadData() {
        if (listUrl.isEmpty()) return;
        progressBar.setVisibility(View.VISIBLE);
        nodataLayout.setVisibility(View.GONE);

        String url = Utility.buildApiUrl(getApplicationContext(), listUrl);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.optInt("status") == 1) {
                            JSONArray data = json.optJSONArray("data");
                            itemsList.clear();
                            if (data != null) {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject item = data.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<>();
                                    for (int j = 0; j < item.names().length(); j++) {
                                        String key = item.names().getString(j);
                                        map.put(key, item.optString(key, ""));
                                    }
                                    itemsList.add(map);
                                }
                            }
                            adapter.notifyDataSetChanged();
                            countTextView.setText(itemsList.size() + " item(s) found");
                            if (itemsList.isEmpty()) {
                                nodataLayout.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(this, json.optString("message", "Failed to load data"), Toast.LENGTH_SHORT).show();
                            nodataLayout.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Parse error", e);
                        Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        nodataLayout.setVisibility(View.VISIBLE);
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    nodataLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", "smartschool");
                headers.put("Auth-Key", "schoolAdmin@");
                return headers;
            }
        };
        requestQueue.add(request);
    }

    private void enterEditMode(int position) {
        isEditMode = true;
        editPosition = position;
        HashMap<String, String> item = itemsList.get(position);
        String name = item.getOrDefault(nameField, "");
        nameInput.setText(name);
        if (showActiveSwitch) {
            String active = item.getOrDefault(activeField, "yes");
            activeSwitch.setChecked("yes".equalsIgnoreCase(active) || "1".equals(active));
        }
        saveButton.setText("Update");
        findViewById(R.id.crud_form_card).setVisibility(View.VISIBLE);
        formLayout.setVisibility(View.VISIBLE);
        formLayout.setTag("edit");
    }

    private void resetForm() {
        isEditMode = false;
        editPosition = -1;
        nameInput.setText("");
        if (showActiveSwitch) activeSwitch.setChecked(true);
        saveButton.setText("Save");
        formLayout.setTag("add");
    }

    private void saveItem() {
        String name = nameInput.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            nameInput.setError("This field is required");
            nameInput.requestFocus();
            return;
        }

        if (isEditMode) {
            updateItem(name);
        } else {
            createItem(name);
        }
    }

    private void createItem(String name) {
        if (createUrl.isEmpty()) return;
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Creating...");
        pd.setCancelable(false);
        pd.show();

        String url = Utility.buildApiUrl(getApplicationContext(), createUrl);
        JSONObject body = new JSONObject();
        try {
            body.put(nameField, name);
            if (showActiveSwitch) {
                body.put(activeField, activeSwitch.isChecked() ? "yes" : "no");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    pd.dismiss();
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.optInt("status") == 1) {
                            Toast.makeText(this, json.optString("message", "Created successfully"), Toast.LENGTH_SHORT).show();
                            resetForm();
                            loadData();
                        } else {
                            Toast.makeText(this, json.optString("message", "Failed to create"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    pd.dismiss();
                    Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return body.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", "smartschool");
                headers.put("Auth-Key", "schoolAdmin@");
                return headers;
            }
        };
        requestQueue.add(request);
    }

    private void updateItem(String name) {
        if (updateUrl.isEmpty() || editPosition < 0) return;
        HashMap<String, String> item = itemsList.get(editPosition);
        String itemId = item.getOrDefault(idField, "");

        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Updating...");
        pd.setCancelable(false);
        pd.show();

        // Append ID to URL if it has a placeholder
        String url = updateUrl;
        if (url.endsWith("/(:num)")) {
            url = url.replace("(:num)", itemId);
        }

        url = Utility.buildApiUrl(getApplicationContext(), url);
        JSONObject body = new JSONObject();
        try {
            body.put("id", itemId);
            body.put(nameField, name);
            if (showActiveSwitch) {
                body.put(activeField, activeSwitch.isChecked() ? "yes" : "no");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    pd.dismiss();
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.optInt("status") == 1) {
                            Toast.makeText(this, json.optString("message", "Updated successfully"), Toast.LENGTH_SHORT).show();
                            resetForm();
                            loadData();
                        } else {
                            Toast.makeText(this, json.optString("message", "Failed to update"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    pd.dismiss();
                    Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return body.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", "smartschool");
                headers.put("Auth-Key", "schoolAdmin@");
                return headers;
            }
        };
        requestQueue.add(request);
    }

    private void showDeleteDialog(int position) {
        HashMap<String, String> item = itemsList.get(position);
        String name = item.getOrDefault(nameField, "this item");

        new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete \"" + name + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> deleteItem(position))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteItem(int position) {
        if (deleteUrl.isEmpty()) return;
        HashMap<String, String> item = itemsList.get(position);
        String itemId = item.getOrDefault(idField, "");

        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Deleting...");
        pd.setCancelable(false);
        pd.show();

        String url = deleteUrl;
        if (url.endsWith("/(:num)")) {
            url = url.replace("(:num)", itemId);
        }
        url = Utility.buildApiUrl(getApplicationContext(), url);

        JSONObject body = new JSONObject();
        try {
            body.put("id", itemId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    pd.dismiss();
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.optInt("status") == 1) {
                            Toast.makeText(this, json.optString("message", "Deleted successfully"), Toast.LENGTH_SHORT).show();
                            resetForm();
                            loadData();
                        } else {
                            Toast.makeText(this, json.optString("message", "Failed to delete"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    pd.dismiss();
                    Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return body.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", "smartschool");
                headers.put("Auth-Key", "schoolAdmin@");
                return headers;
            }
        };
        requestQueue.add(request);
    }

    private void toggleActiveStatus(int position, boolean isActive) {
        HashMap<String, String> item = itemsList.get(position);
        String itemId = item.getOrDefault(idField, "");
        String newStatus = isActive ? "yes" : "no";

        // Update locally first for instant feedback
        item.put(activeField, newStatus);
        adapter.notifyItemChanged(position);

        // Send update to server
        if (!updateUrl.isEmpty()) {
            String url = updateUrl;
            if (url.endsWith("/(:num)")) {
                url = url.replace("(:num)", itemId);
            }
            url = Utility.buildApiUrl(getApplicationContext(), url);

            JSONObject body = new JSONObject();
            try {
                body.put("id", itemId);
                body.put(activeField, newStatus);
                // Also send name to avoid validation errors
                body.put(nameField, item.getOrDefault(nameField, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    response -> {
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.optInt("status") != 1) {
                                // Revert on failure
                                item.put(activeField, isActive ? "no" : "yes");
                                adapter.notifyItemChanged(position);
                                Toast.makeText(this, json.optString("message", "Failed to update"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            item.put(activeField, isActive ? "no" : "yes");
                            adapter.notifyItemChanged(position);
                        }
                    },
                    error -> {
                        item.put(activeField, isActive ? "no" : "yes");
                        adapter.notifyItemChanged(position);
                    }) {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    return body.toString().getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Client-Service", "smartschool");
                    headers.put("Auth-Key", "schoolAdmin@");
                    return headers;
                }
            };
            requestQueue.add(request);
        }
    }
}
