package com.qdocs.ssre241123.teachers;

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

import androidx.appcompat.app.AlertDialog;
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
import com.qdocs.ssre241123.adapters.DisableReasonAdapter;
import com.qdocs.ssre241123.model.DisableReason;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisableReasonsActivity extends BaseActivity {

    private static final String TAG = "DisableReasons";

    // UI Components
    private ImageView backButton;
    private TextView titleTextView;
    private CardView formCard;
    private TextView formTitleTV;
    private TextInputLayout reasonInputLayout;
    private TextInputEditText reasonInput;
    private Button cancelButton;
    private Button saveButton;
    private TextView reasonCountTV;
    private RecyclerView reasonsRecyclerView;
    private LinearLayout nodataLayout;
    private ProgressBar progressBar;

    // Data
    private List<DisableReason> reasonList;
    private DisableReasonAdapter adapter;
    private RequestQueue requestQueue;

    // Edit mode
    private boolean isEditMode = false;
    private DisableReason editingReason = null;
    private int editingPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disable_reasons);

        initializeViews();
        setupRecyclerView();
        setupListeners();
        loadReasons();
    }

    private void initializeViews() {
        backButton = findViewById(R.id.back_button);
        titleTextView = findViewById(R.id.title_textview);
        formCard = findViewById(R.id.form_card);
        formTitleTV = findViewById(R.id.form_title_tv);
        reasonInputLayout = findViewById(R.id.reason_input_layout);
        reasonInput = findViewById(R.id.reason_input);
        cancelButton = findViewById(R.id.cancel_button);
        saveButton = findViewById(R.id.save_button);
        reasonCountTV = findViewById(R.id.reason_count_tv);
        reasonsRecyclerView = findViewById(R.id.reasons_recyclerView);
        nodataLayout = findViewById(R.id.nodata_layout);
        progressBar = findViewById(R.id.progressBar);

        requestQueue = Volley.newRequestQueue(this);
    }

    private void setupRecyclerView() {
        reasonList = new ArrayList<>();
        adapter = new DisableReasonAdapter(this, reasonList);
        reasonsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reasonsRecyclerView.setAdapter(adapter);

        adapter.setOnReasonActionListener(new DisableReasonAdapter.OnReasonActionListener() {
            @Override
            public void onEditClick(DisableReason reason, int position) {
                enterEditMode(reason, position);
            }

            @Override
            public void onDeleteClick(DisableReason reason, int position) {
                showDeleteConfirmation(reason, position);
            }

            @Override
            public void onItemClick(DisableReason reason, int position) {
                Toast.makeText(DisableReasonsActivity.this,
                        reason.getReason(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListeners() {
        backButton.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_rightleft, R.anim.no_animation);
        });

        saveButton.setOnClickListener(v -> {
            if (isEditMode) {
                updateReason();
            } else {
                createReason();
            }
        });

        cancelButton.setOnClickListener(v -> {
            exitEditMode();
        });
    }

    private void loadReasons() {
        showProgress();
        String url = Utility.buildApiUrl(getApplicationContext(), Constants.disableReasonListUrl);
        Log.d(TAG, "Loading reasons from: " + url);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "Response: " + response);
                    hideProgress();
                    parseReasonsResponse(response);
                },
                error -> {
                    Log.e(TAG, "Error loading reasons: " + error.toString());
                    hideProgress();
                    showError("Failed to load reasons");
                    if (error.networkResponse != null) {
                        try {
                            String errorBody = new String(error.networkResponse.data, "UTF-8");
                            Log.e(TAG, "Error body: " + errorBody);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
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

    private void parseReasonsResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.getInt("status");

            if (status == 1) {
                reasonList.clear();
                JSONArray dataArray = jsonObject.getJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject reasonObj = dataArray.getJSONObject(i);
                    DisableReason reason = new DisableReason();
                    reason.setId(reasonObj.getInt("id"));
                    reason.setReason(reasonObj.getString("reason"));
                    reason.setCreatedAt(reasonObj.optString("created_at", ""));
                    reason.setUpdatedAt(reasonObj.optString("updated_at", ""));
                    reasonList.add(reason);
                }

                adapter.updateData(reasonList);
                updateReasonCount();

                if (reasonList.isEmpty()) {
                    showNoData();
                } else {
                    showContent();
                }
            } else {
                String message = jsonObject.optString("message", "Failed to load reasons");
                showError(message);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing response: " + e.getMessage());
            e.printStackTrace();
            showError("Error parsing data");
        }
    }

    private void createReason() {
        String reasonText = reasonInput.getText().toString().trim();

        if (reasonText.isEmpty()) {
            reasonInputLayout.setError("Reason is required");
            return;
        }

        reasonInputLayout.setError(null);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating reason...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = Utility.buildApiUrl(getApplicationContext(), Constants.disableReasonCreateUrl);
        Log.d(TAG, "Creating reason at: " + url);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "Create response: " + response);
                    progressDialog.dismiss();
                    handleCreateResponse(response);
                },
                error -> {
                    Log.e(TAG, "Error creating reason: " + error.toString());
                    progressDialog.dismiss();
                    Toast.makeText(this, "Failed to create reason", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getApiHeaders();
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("reason", reasonText);
                    return jsonBody.toString().getBytes("utf-8");
                } catch (Exception e) {
                    e.printStackTrace();
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
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                clearForm();
                loadReasons();
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing create response: " + e.getMessage());
            Toast.makeText(this, "Error creating reason", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateReason() {
        if (editingReason == null) {
            return;
        }

        String reasonText = reasonInput.getText().toString().trim();

        if (reasonText.isEmpty()) {
            reasonInputLayout.setError("Reason is required");
            return;
        }

        reasonInputLayout.setError(null);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating reason...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = Utility.buildApiUrl(getApplicationContext(), Constants.disableReasonUpdateUrl) + "/" + editingReason.getId();
        Log.d(TAG, "Updating reason at: " + url);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "Update response: " + response);
                    progressDialog.dismiss();
                    handleUpdateResponse(response);
                },
                error -> {
                    Log.e(TAG, "Error updating reason: " + error.toString());
                    progressDialog.dismiss();
                    Toast.makeText(this, "Failed to update reason", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getApiHeaders();
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("reason", reasonText);
                    return jsonBody.toString().getBytes("utf-8");
                } catch (Exception e) {
                    e.printStackTrace();
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
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                exitEditMode();
                loadReasons();
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing update response: " + e.getMessage());
            Toast.makeText(this, "Error updating reason", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteReason(DisableReason reason, int position) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Deleting reason...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = Utility.buildApiUrl(getApplicationContext(), Constants.disableReasonDeleteUrl) + "/" + reason.getId();
        Log.d(TAG, "Deleting reason at: " + url);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "Delete response: " + response);
                    progressDialog.dismiss();
                    handleDeleteResponse(response, position);
                },
                error -> {
                    Log.e(TAG, "Error deleting reason: " + error.toString());
                    progressDialog.dismiss();
                    Toast.makeText(this, "Failed to delete reason", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                adapter.removeReason(position);
                updateReasonCount();

                if (reasonList.isEmpty()) {
                    showNoData();
                }
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing delete response: " + e.getMessage());
            Toast.makeText(this, "Error deleting reason", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteConfirmation(DisableReason reason, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Reason")
                .setMessage("Are you sure you want to delete \"" + reason.getReason() + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    deleteReason(reason, position);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void enterEditMode(DisableReason reason, int position) {
        isEditMode = true;
        editingReason = reason;
        editingPosition = position;

        formTitleTV.setText("Edit Disable Reason");
        saveButton.setText("Update");
        reasonInput.setText(reason.getReason());

        // Scroll to form
        formCard.requestFocus();
    }

    private void exitEditMode() {
        isEditMode = false;
        editingReason = null;
        editingPosition = -1;

        formTitleTV.setText("Add New Disable Reason");
        saveButton.setText("Save");
        clearForm();
    }

    private void clearForm() {
        reasonInput.setText("");
        reasonInputLayout.setError(null);
    }

    private void updateReasonCount() {
        int count = reasonList.size();
        reasonCountTV.setText(count + (count == 1 ? " Reason" : " Reasons"));
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        reasonsRecyclerView.setVisibility(View.GONE);
        nodataLayout.setVisibility(View.GONE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private void showContent() {
        reasonsRecyclerView.setVisibility(View.VISIBLE);
        nodataLayout.setVisibility(View.GONE);
        updateReasonCount();
    }

    private void showNoData() {
        reasonsRecyclerView.setVisibility(View.GONE);
        nodataLayout.setVisibility(View.VISIBLE);
        updateReasonCount();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        showNoData();
    }

    private Map<String, String> getApiHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Client-Service", Constants.clientService);
        headers.put("Auth-Key", Constants.authKey);
        headers.put("Content-Type", "application/json");
        return headers;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_rightleft, R.anim.no_animation);
    }
}

