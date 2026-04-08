package com.qdocs.ssre241123.teachers;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.qdocs.ssre241123.R;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Sibling Report Activity
 * Shows students who have siblings in the same school
 * API: sibling-report/filter
 */
public class SiblingReportActivity extends GenericReportActivity {

    private static final String TAG = "SiblingReport";

    @Override
    protected void initViews() {
        apiEndpoint = "sibling-report/filter";

        View actionBar = findViewById(R.id.actionBar);
        android.widget.ImageView backButton = findViewById(R.id.back_button);
        android.widget.TextView titleTextView = findViewById(R.id.title);
        sessionSpinner = findViewById(R.id.session_spinner);
        classSpinner = findViewById(R.id.class_spinner);
        sectionSpinner = findViewById(R.id.section_spinner);
        generateReportButton = findViewById(R.id.generate_report_button);
        recyclerView = findViewById(R.id.reportRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        nodataLayout = findViewById(R.id.nodata_layout);

        titleTextView.setText("Sibling Report");
        applyThemeColor(actionBar, generateReportButton);

        backButton.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_rightleft, R.anim.no_animation);
        });

        generateReportButton.setOnClickListener(v -> generateReport());
    }

    @Override
    protected void parseResponse(String response) {
        try {
            JSONObject json = new JSONObject(response);
            if (json.optInt("status") == 1) {
                JSONArray data = json.optJSONArray("data");
                if (data != null && data.length() > 0) {
                    showContent();
                    com.qdocs.ssre241123.adapters.GenericReportAdapter adapter =
                        new com.qdocs.ssre241123.adapters.GenericReportAdapter(
                            this, "parent_name", "total_siblings", "children_names");
                    adapter.setData(data);
                    recyclerView.setAdapter(adapter);
                } else {
                    showNoData();
                    Toast.makeText(this, "No sibling records found", Toast.LENGTH_SHORT).show();
                }
            } else {
                showNoData();
            }
        } catch (Exception e) {
            showNoData();
            Log.e(TAG, "Parse error", e);
        }
    }
}
