package com.qdocs.ssre241123.teachers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.qdocs.ssre241123.BaseActivity;
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.StudentHouseAdapter;
import com.qdocs.ssre241123.model.StudentHouse;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentHousesActivity extends BaseActivity {

    private static final String TAG = "StudentHouses";

    // UI Components
    private ImageView backButton;
    private TextView titleTextView;
    private CardView formCard;
    private TextView formTitleTV;
    private TextInputLayout houseNameInputLayout;
    private TextInputEditText houseNameInput;
    private TextInputLayout houseDescriptionInputLayout;
    private TextInputEditText houseDescriptionInput;
    private SwitchCompat houseActiveSwitch;
    private Button cancelButton;
    private Button saveButton;
    private TextView houseCountTV;
    private RecyclerView housesRecyclerView;
    private LinearLayout nodataLayout;
    private ProgressBar progressBar;

    // Data
    private List<StudentHouse> houseList;
    private StudentHouseAdapter adapter;
    private RequestQueue requestQueue;

    // Edit mode
    private boolean isEditMode = false;
    private StudentHouse editingHouse = null;
    private int editingPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_houses);

        initializeViews();
        setupRecyclerView();
        setupListeners();
        loadHouses();
    }

    private void initializeViews() {
        backButton = findViewById(R.id.back_button);
        titleTextView = findViewById(R.id.title_textview);
        formCard = findViewById(R.id.form_card);
        formTitleTV = findViewById(R.id.form_title_tv);
        houseNameInputLayout = findViewById(R.id.house_name_input_layout);
        houseNameInput = findViewById(R.id.house_name_input);
        houseDescriptionInputLayout = findViewById(R.id.house_description_input_layout);
        houseDescriptionInput = findViewById(R.id.house_description_input);
        houseActiveSwitch = findViewById(R.id.house_active_switch);
        cancelButton = findViewById(R.id.cancel_button);
        saveButton = findViewById(R.id.save_button);
        houseCountTV = findViewById(R.id.house_count_tv);
        housesRecyclerView = findViewById(R.id.houses_recyclerView);
        nodataLayout = findViewById(R.id.nodata_layout);
        progressBar = findViewById(R.id.progressBar);

        requestQueue = Volley.newRequestQueue(this);
    }

    private void setupRecyclerView() {
        houseList = new ArrayList<>();
        adapter = new StudentHouseAdapter(this, houseList);
        housesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        housesRecyclerView.setAdapter(adapter);

        adapter.setOnHouseActionListener(new StudentHouseAdapter.OnHouseActionListener() {
            @Override
            public void onEditClick(StudentHouse house, int position) {
                enterEditMode(house, position);
            }

            @Override
            public void onDeleteClick(StudentHouse house, int position) {
                showDeleteConfirmation(house, position);
            }

            @Override
            public void onItemClick(StudentHouse house, int position) {
                Toast.makeText(StudentHousesActivity.this,
                        house.getHouseName(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListeners() {
        backButton.setOnClickListener(v -> finish());

        saveButton.setOnClickListener(v -> {
            if (isEditMode) {
                updateHouse();
            } else {
                createHouse();
            }
        });

        cancelButton.setOnClickListener(v -> {
            exitEditMode();
            clearForm();
        });
    }

    private void loadHouses() {
        showProgress();

        String url = Utility.buildApiUrl(getApplicationContext(), Constants.studentHouseListUrl);
        Log.d(TAG, "Loading houses from: " + url);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    hideProgress();
                    Log.d(TAG, "Houses Response: " + response);
                    parseHouses(response);
                },
                error -> {
                    hideProgress();
                    Log.e(TAG, "Error loading houses: " + error.toString());
                    showNoData();
                    Toast.makeText(this, "Error loading houses", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getApiHeaders();
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return "{}".getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        requestQueue.add(request);
    }

    private void parseHouses(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.getInt("status");

            if (status == 1) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                houseList.clear();

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject houseObj = dataArray.getJSONObject(i);
                    StudentHouse house = new StudentHouse();
                    house.setId(houseObj.getInt("id"));
                    house.setHouseName(houseObj.getString("house_name"));
                    house.setDescription(houseObj.optString("description", ""));
                    house.setIsActive(houseObj.getString("is_active"));
                    house.setCreatedAt(houseObj.optString("created_at", ""));
                    house.setUpdatedAt(houseObj.optString("updated_at", ""));
                    houseList.add(house);
                }

                adapter.notifyDataSetChanged();
                updateHouseCount();

                if (houseList.isEmpty()) {
                    showNoData();
                } else {
                    hideNoData();
                }
            } else {
                String message = jsonObject.optString("message", "Failed to load houses");
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                showNoData();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing houses", e);
            Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
            showNoData();
        }
    }

    private void createHouse() {
        String houseName = houseNameInput.getText().toString().trim();
        String description = houseDescriptionInput.getText().toString().trim();

        if (houseName.isEmpty()) {
            houseNameInputLayout.setError("House name is required");
            return;
        }

        houseNameInputLayout.setError(null);

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Creating house...");
        pd.setCancelable(false);
        pd.show();

        String url = Utility.buildApiUrl(getApplicationContext(), Constants.studentHouseCreateUrl);
        Log.d(TAG, "Creating house at: " + url);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    pd.dismiss();
                    Log.d(TAG, "Create Response: " + response);
                    handleCreateResponse(response);
                },
                error -> {
                    pd.dismiss();
                    Log.e(TAG, "Create Error: " + error.toString());
                    Toast.makeText(this, "Error creating house", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getApiHeaders();
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("house_name", houseName);
                    jsonBody.put("description", description);
                    return jsonBody.toString().getBytes("utf-8");
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

        requestQueue.add(request);
    }

    private void handleCreateResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.getInt("status");
            String message = jsonObject.optString("message", "");

            if (status == 1) {
                Toast.makeText(this, "House created successfully", Toast.LENGTH_SHORT).show();
                clearForm();
                loadHouses(); // Reload the list
            } else {
                Toast.makeText(this, message.isEmpty() ? "Failed to create house" : message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing create response", e);
            Toast.makeText(this, "Error processing response", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateHouse() {
        if (editingHouse == null) {
            return;
        }

        String houseName = houseNameInput.getText().toString().trim();
        String description = houseDescriptionInput.getText().toString().trim();

        if (houseName.isEmpty()) {
            houseNameInputLayout.setError("House name is required");
            return;
        }

        houseNameInputLayout.setError(null);

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Updating house...");
        pd.setCancelable(false);
        pd.show();

        String url = Utility.buildApiUrl(getApplicationContext(), Constants.studentHouseUpdateUrl) + "/" + editingHouse.getId();
        Log.d(TAG, "Updating house at: " + url);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    pd.dismiss();
                    Log.d(TAG, "Update Response: " + response);
                    handleUpdateResponse(response);
                },
                error -> {
                    pd.dismiss();
                    Log.e(TAG, "Update Error: " + error.toString());
                    Toast.makeText(this, "Error updating house", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getApiHeaders();
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("house_name", houseName);
                    jsonBody.put("description", description);
                    jsonBody.put("is_active", houseActiveSwitch.isChecked() ? "yes" : "no");
                    return jsonBody.toString().getBytes("utf-8");
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

        requestQueue.add(request);
    }

    private void handleUpdateResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.getInt("status");
            String message = jsonObject.optString("message", "");

            if (status == 1) {
                Toast.makeText(this, "House updated successfully", Toast.LENGTH_SHORT).show();
                exitEditMode();
                clearForm();
                loadHouses(); // Reload the list
            } else {
                Toast.makeText(this, message.isEmpty() ? "Failed to update house" : message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing update response", e);
            Toast.makeText(this, "Error processing response", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteHouse(StudentHouse house, int position) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Deleting house...");
        pd.setCancelable(false);
        pd.show();

        String url = Utility.buildApiUrl(getApplicationContext(), Constants.studentHouseDeleteUrl) + "/" + house.getId();
        Log.d(TAG, "Deleting house at: " + url);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    pd.dismiss();
                    Log.d(TAG, "Delete Response: " + response);
                    handleDeleteResponse(response, position);
                },
                error -> {
                    pd.dismiss();
                    Log.e(TAG, "Delete Error: " + error.toString());
                    Toast.makeText(this, "Error deleting house", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getApiHeaders();
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return "{}".getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        requestQueue.add(request);
    }

    private void handleDeleteResponse(String response, int position) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.getInt("status");
            String message = jsonObject.optString("message", "");

            if (status == 1) {
                Toast.makeText(this, "House deleted successfully", Toast.LENGTH_SHORT).show();
                adapter.removeHouse(position);
                updateHouseCount();

                if (houseList.isEmpty()) {
                    showNoData();
                }
            } else {
                Toast.makeText(this, message.isEmpty() ? "Failed to delete house" : message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing delete response", e);
            Toast.makeText(this, "Error processing response", Toast.LENGTH_SHORT).show();
        }
    }

    private void enterEditMode(StudentHouse house, int position) {
        isEditMode = true;
        editingHouse = house;
        editingPosition = position;

        formTitleTV.setText("Edit House");
        saveButton.setText("Update");
        houseNameInput.setText(house.getHouseName());
        houseDescriptionInput.setText(house.getDescription());
        houseActiveSwitch.setChecked(house.isActiveHouse());

        // Scroll to top to show the form
        housesRecyclerView.smoothScrollToPosition(0);
    }

    private void exitEditMode() {
        isEditMode = false;
        editingHouse = null;
        editingPosition = -1;

        formTitleTV.setText("Add New House");
        saveButton.setText("Save");
    }

    private void clearForm() {
        houseNameInput.setText("");
        houseDescriptionInput.setText("");
        houseActiveSwitch.setChecked(true);
        houseNameInputLayout.setError(null);
        houseDescriptionInputLayout.setError(null);
    }

    private void showDeleteConfirmation(StudentHouse house, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete House")
                .setMessage("Are you sure you want to delete \"" + house.getHouseName() + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> deleteHouse(house, position))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateHouseCount() {
        int count = houseList.size();
        houseCountTV.setText(count + (count == 1 ? " House" : " Houses"));
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        housesRecyclerView.setVisibility(View.GONE);
        nodataLayout.setVisibility(View.GONE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private void showNoData() {
        housesRecyclerView.setVisibility(View.GONE);
        nodataLayout.setVisibility(View.VISIBLE);
        updateHouseCount();
    }

    private void hideNoData() {
        housesRecyclerView.setVisibility(View.VISIBLE);
        nodataLayout.setVisibility(View.GONE);
    }

    private Map<String, String> getApiHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Client-Service", Constants.clientService);
        headers.put("Auth-Key", Constants.authKey);
        headers.put("Content-Type", Constants.contentType);
        return headers;
    }
}
