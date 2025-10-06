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
import com.qdocs.ssre241123.adapters.StudentCategoryAdapter;
import com.qdocs.ssre241123.model.StudentCategory;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentCategoriesActivity extends BaseActivity {

    private static final String TAG = "StudentCategories";

    // UI Components
    private ImageView backButton;
    private TextView titleTextView;
    private CardView formCard;
    private TextView formTitleTV;
    private TextInputLayout categoryNameInputLayout;
    private TextInputEditText categoryNameInput;
    private SwitchCompat categoryActiveSwitch;
    private Button cancelButton;
    private Button saveButton;
    private TextView categoryCountTV;
    private RecyclerView categoriesRecyclerView;
    private LinearLayout nodataLayout;
    private ProgressBar progressBar;

    // Data
    private List<StudentCategory> categoryList;
    private StudentCategoryAdapter adapter;
    private RequestQueue requestQueue;

    // Edit mode
    private boolean isEditMode = false;
    private StudentCategory editingCategory = null;
    private int editingPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_categories);

        initializeViews();
        setupRecyclerView();
        setupListeners();
        loadCategories();
    }

    private void initializeViews() {
        backButton = findViewById(R.id.back_button);
        titleTextView = findViewById(R.id.title_textview);
        formCard = findViewById(R.id.form_card);
        formTitleTV = findViewById(R.id.form_title_tv);
        categoryNameInputLayout = findViewById(R.id.category_name_input_layout);
        categoryNameInput = findViewById(R.id.category_name_input);
        categoryActiveSwitch = findViewById(R.id.category_active_switch);
        cancelButton = findViewById(R.id.cancel_button);
        saveButton = findViewById(R.id.save_button);
        categoryCountTV = findViewById(R.id.category_count_tv);
        categoriesRecyclerView = findViewById(R.id.categories_recyclerView);
        nodataLayout = findViewById(R.id.nodata_layout);
        progressBar = findViewById(R.id.progressBar);

        requestQueue = Volley.newRequestQueue(this);
    }

    private void setupRecyclerView() {
        categoryList = new ArrayList<>();
        adapter = new StudentCategoryAdapter(this, categoryList);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoriesRecyclerView.setAdapter(adapter);

        adapter.setOnCategoryActionListener(new StudentCategoryAdapter.OnCategoryActionListener() {
            @Override
            public void onEditClick(StudentCategory category, int position) {
                enterEditMode(category, position);
            }

            @Override
            public void onDeleteClick(StudentCategory category, int position) {
                showDeleteConfirmation(category, position);
            }

            @Override
            public void onItemClick(StudentCategory category, int position) {
                Toast.makeText(StudentCategoriesActivity.this,
                        category.getCategoryName(),
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
                updateCategory();
            } else {
                createCategory();
            }
        });

        cancelButton.setOnClickListener(v -> {
            exitEditMode();
        });
    }

    private void loadCategories() {
        showProgress();
        String url = Utility.buildApiUrl(getApplicationContext(), Constants.teacherStudentCategoriesUrl);
        Log.d(TAG, "Loading categories from: " + url);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    hideProgress();
                    Log.d(TAG, "Categories Response: " + response);
                    parseCategories(response);
                },
                error -> {
                    hideProgress();
                    Log.e(TAG, "Error loading categories: " + error.toString());
                    showNoData();
                    Toast.makeText(this, "Error loading categories", Toast.LENGTH_SHORT).show();
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

    private void parseCategories(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.optInt("status", 0);

            if (status == 1) {
                JSONArray dataArray = jsonObject.optJSONArray("data");
                categoryList.clear();

                if (dataArray != null && dataArray.length() > 0) {
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject categoryObj = dataArray.getJSONObject(i);
                        StudentCategory category = new StudentCategory();
                        category.setCategoryId(categoryObj.optInt("category_id", 0));
                        category.setCategoryName(categoryObj.optString("category_name", ""));

                        // Parse is_active field - handle both string and integer values
                        String isActiveValue = categoryObj.optString("is_active", "no");
                        Log.d(TAG, "Category: " + category.getCategoryName() +
                              ", is_active raw value: '" + isActiveValue + "'");

                        // Normalize the value - handle "yes", "no", "1", "0", etc.
                        if ("yes".equalsIgnoreCase(isActiveValue) || "1".equals(isActiveValue)) {
                            category.setIsActive("yes");
                        } else {
                            category.setIsActive("no");
                        }

                        Log.d(TAG, "Category: " + category.getCategoryName() +
                              ", normalized is_active: '" + category.getIsActive() +
                              "', isActiveCategory(): " + category.isActiveCategory());

                        category.setCreatedAt(categoryObj.optString("created_at", ""));
                        category.setUpdatedAt(categoryObj.optString("updated_at", null));
                        categoryList.add(category);
                    }
                    showData();
                    updateCategoryCount();
                } else {
                    showNoData();
                }
            } else {
                showNoData();
                String message = jsonObject.optString("message", "Failed to load categories");
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing categories: " + e.getMessage());
            e.printStackTrace();
            showNoData();
            Toast.makeText(this, "Error parsing data", Toast.LENGTH_SHORT).show();
        }
    }

    private void createCategory() {
        String categoryName = categoryNameInput.getText().toString().trim();
        boolean isActive = categoryActiveSwitch.isChecked();

        if (categoryName.isEmpty()) {
            categoryNameInputLayout.setError("Category name is required");
            return;
        }

        categoryNameInputLayout.setError(null);

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Creating category...");
        pd.setCancelable(false);
        pd.show();

        String url = Utility.buildApiUrl(getApplicationContext(), Constants.teacherStudentCategoryCreateUrl);
        Log.d(TAG, "Creating category at: " + url);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    pd.dismiss();
                    Log.d(TAG, "Create Response: " + response);
                    handleCreateResponse(response);
                },
                error -> {
                    pd.dismiss();
                    Log.e(TAG, "Create Error: " + error.toString());
                    Toast.makeText(this, "Error creating category", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getApiHeaders();
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("category_name", categoryName);
                    jsonBody.put("is_active", isActive ? "yes" : "no");
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
            int status = jsonObject.optInt("status", 0);
            String message = jsonObject.optString("message", "");

            if (status == 1) {
                // Success
                JSONObject dataObj = jsonObject.optJSONObject("data");
                if (dataObj != null) {
                    StudentCategory newCategory = new StudentCategory();
                    newCategory.setCategoryId(dataObj.optInt("category_id", 0));
                    newCategory.setCategoryName(dataObj.optString("category_name", ""));
                    newCategory.setIsActive(dataObj.optString("is_active", "no"));
                    newCategory.setCreatedAt(dataObj.optString("created_at", ""));
                    newCategory.setUpdatedAt(null);

                    adapter.addCategory(newCategory);
                    updateCategoryCount();
                    showData();
                    clearForm();
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                }
            } else {
                // Error
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error handling create response: " + e.getMessage());
            Toast.makeText(this, "Error processing response", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCategory() {
        if (editingCategory == null) {
            return;
        }

        String categoryName = categoryNameInput.getText().toString().trim();
        boolean isActive = categoryActiveSwitch.isChecked();

        if (categoryName.isEmpty()) {
            categoryNameInputLayout.setError("Category name is required");
            return;
        }

        categoryNameInputLayout.setError(null);

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Updating category...");
        pd.setCancelable(false);
        pd.show();

        String url = Utility.buildApiUrl(getApplicationContext(), Constants.teacherStudentCategoryUpdateUrl);
        Log.d(TAG, "Updating category at: " + url);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    pd.dismiss();
                    Log.d(TAG, "Update Response: " + response);
                    handleUpdateResponse(response);
                },
                error -> {
                    pd.dismiss();
                    Log.e(TAG, "Update Error: " + error.toString());
                    Toast.makeText(this, "Error updating category", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getApiHeaders();
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("category_id", editingCategory.getCategoryId());
                    jsonBody.put("category_name", categoryName);
                    jsonBody.put("is_active", isActive ? "yes" : "no");
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
            int status = jsonObject.optInt("status", 0);
            String message = jsonObject.optString("message", "");

            if (status == 1) {
                // Success
                JSONObject dataObj = jsonObject.optJSONObject("data");
                if (dataObj != null) {
                    StudentCategory updatedCategory = new StudentCategory();
                    updatedCategory.setCategoryId(dataObj.optInt("category_id", 0));
                    updatedCategory.setCategoryName(dataObj.optString("category_name", ""));
                    updatedCategory.setIsActive(dataObj.optString("is_active", "no"));
                    updatedCategory.setCreatedAt(dataObj.optString("created_at", ""));
                    updatedCategory.setUpdatedAt(dataObj.optString("updated_at", null));

                    adapter.updateCategory(editingPosition, updatedCategory);
                    exitEditMode();
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                }
            } else {
                // Error
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error handling update response: " + e.getMessage());
            Toast.makeText(this, "Error processing response", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteConfirmation(StudentCategory category, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Category")
                .setMessage("Are you sure you want to delete \"" + category.getCategoryName() + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    deleteCategory(category, position);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteCategory(StudentCategory category, int position) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Deleting category...");
        pd.setCancelable(false);
        pd.show();

        String url = Utility.buildApiUrl(getApplicationContext(), Constants.teacherStudentCategoryDeleteUrl);
        Log.d(TAG, "Deleting category at: " + url);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    pd.dismiss();
                    Log.d(TAG, "Delete Response: " + response);
                    handleDeleteResponse(response, position);
                },
                error -> {
                    pd.dismiss();
                    Log.e(TAG, "Delete Error: " + error.toString());
                    Toast.makeText(this, "Error deleting category", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getApiHeaders();
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("category_id", category.getCategoryId());
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

    private void handleDeleteResponse(String response, int position) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.optInt("status", 0);
            String message = jsonObject.optString("message", "");

            if (status == 1) {
                // Success
                adapter.removeCategory(position);
                updateCategoryCount();
                if (categoryList.isEmpty()) {
                    showNoData();
                }
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            } else {
                // Error
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error handling delete response: " + e.getMessage());
            Toast.makeText(this, "Error processing response", Toast.LENGTH_SHORT).show();
        }
    }

    private void enterEditMode(StudentCategory category, int position) {
        isEditMode = true;
        editingCategory = category;
        editingPosition = position;

        formTitleTV.setText("Edit Category");
        categoryNameInput.setText(category.getCategoryName());
        categoryActiveSwitch.setChecked(category.isActiveCategory());
        saveButton.setText("Update Category");
        cancelButton.setVisibility(View.VISIBLE);

        // Scroll to top to show form
        categoriesRecyclerView.smoothScrollToPosition(0);
    }

    private void exitEditMode() {
        isEditMode = false;
        editingCategory = null;
        editingPosition = -1;

        formTitleTV.setText("Add New Category");
        saveButton.setText("Save Category");
        cancelButton.setVisibility(View.GONE);
        clearForm();
    }

    private void clearForm() {
        categoryNameInput.setText("");
        categoryActiveSwitch.setChecked(false);
        categoryNameInputLayout.setError(null);
    }

    private void updateCategoryCount() {
        int count = categoryList.size();
        categoryCountTV.setText(count + (count == 1 ? " category" : " categories"));
        adapter.notifyDataSetChanged();
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        categoriesRecyclerView.setVisibility(View.GONE);
        nodataLayout.setVisibility(View.GONE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private void showData() {
        categoriesRecyclerView.setVisibility(View.VISIBLE);
        nodataLayout.setVisibility(View.GONE);
    }

    private void showNoData() {
        categoriesRecyclerView.setVisibility(View.GONE);
        nodataLayout.setVisibility(View.VISIBLE);
        updateCategoryCount();
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

