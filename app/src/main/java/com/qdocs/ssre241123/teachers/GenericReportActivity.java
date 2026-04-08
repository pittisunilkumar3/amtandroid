package com.qdocs.ssre241123.teachers;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.GenericReportAdapter;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import org.json.JSONArray;

/**
 * Generic Report Activity that can display any report from the API.
 * Report behavior is configured via Intent extras:
 *   - report_id: Unique identifier for the report
 *   - report_name: Display title
 *   - api_endpoint: The API endpoint to call (e.g., "staff-report/filter")
 *   - title_field: JSON field to use as the item title (e.g., "name")
 *   - subtitle_field: JSON field to use as subtitle (e.g., "class")
 *   - detail_field: JSON field to use as detail (e.g., "email")
 *   - title_prefix: Optional prefix for title (e.g., "#")
 */
public class GenericReportActivity extends BaseFilterReportActivity {

    private static final String TAG = "GenericReport";

    protected String apiEndpoint = "";
    protected String titleField = "name";
    protected String subtitleField = "";
    protected String detailField = "";
    protected String titlePrefix = "";
    private GenericReportAdapter adapter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_generic_filter_report;
    }

    @Override
    protected String getApiEndpoint() {
        return apiEndpoint;
    }

    @Override
    protected void initViews() {
        // Get config from intent
        Bundle extras = getIntent().getExtras();
        String reportName = "Report";

        if (extras != null) {
            apiEndpoint = extras.getString("api_endpoint", "");
            titleField = extras.getString("title_field", "name");
            subtitleField = extras.getString("subtitle_field", "");
            detailField = extras.getString("detail_field", "");
            titlePrefix = extras.getString("title_prefix", "");
            reportName = extras.getString("report_name", extras.getString("title", "Report"));
        }

        // Find views
        View actionBar = findViewById(R.id.actionBar);
        ImageView backButton = findViewById(R.id.back_button);
        TextView titleTextView = findViewById(R.id.title);
        sessionSpinner = findViewById(R.id.session_spinner);
        classSpinner = findViewById(R.id.class_spinner);
        sectionSpinner = findViewById(R.id.section_spinner);
        generateReportButton = findViewById(R.id.generate_report_button);
        recyclerView = findViewById(R.id.reportRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        nodataLayout = findViewById(R.id.nodata_layout);

        // Set title
        if (titleTextView != null && reportName != null) {
            titleTextView.setText(reportName);
        }

        // Apply theme
        applyThemeColor(actionBar, generateReportButton);

        // Back button
        if (backButton != null) {
            backButton.setOnClickListener(v -> {
                finish();
                overridePendingTransition(R.anim.slide_rightleft, R.anim.no_animation);
            });
        }

        // Generate button
        if (generateReportButton != null) {
            generateReportButton.setOnClickListener(v -> generateReport());
        }
    }

    @Override
    protected void setupRecyclerView() {
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new GenericReportAdapter(this, titleField, subtitleField, detailField);
            if (!titlePrefix.isEmpty()) {
                adapter.setTitlePrefix(titlePrefix);
            }
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void parseResponse(String response) {
        try {
            org.json.JSONObject json = new org.json.JSONObject(response);
            int status = json.optInt("status", 0);

            if (status == 1) {
                JSONArray data = json.optJSONArray("data");
                if (data != null && data.length() > 0) {
                    showContent();
                    adapter.setData(data);

                    String message = json.optString("message", "");
                    if (!message.isEmpty()) {
                        Log.d(TAG, message);
                    }
                } else {
                    showNoData();
                    Toast.makeText(this, json.optString("message", "No data found"), Toast.LENGTH_SHORT).show();
                }
            } else {
                showNoData();
                Toast.makeText(this, json.optString("message", "Failed to load report"), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            showNoData();
            Log.e(TAG, "Parse error", e);
        }
    }
}
