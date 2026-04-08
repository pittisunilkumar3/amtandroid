package com.qdocs.ssre241123.teachers;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Boys/Girls Ratio Report Activity
 * Shows the ratio of boys to girls per class/section
 * API: boys-girls-ratio-report/filter
 */
public class BoysGirlsRatioReportActivity extends GenericReportActivity {

    private static final String TAG = "BoysGirlsRatio";

    private LinearLayout ratioSummaryLayout;
    private TextView totalBoysTv, totalGirlsTv, totalRatioTv;

    @Override
    protected void initViews() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            apiEndpoint = "boys-girls-ratio-report/filter";
            titleField = "class";
            subtitleField = "section";
            detailField = "";
        }

        View actionBar = findViewById(R.id.actionBar);
        android.widget.ImageView backButton = findViewById(R.id.back_button);
        TextView titleTextView = findViewById(R.id.title);
        sessionSpinner = findViewById(R.id.session_spinner);
        classSpinner = findViewById(R.id.class_spinner);
        sectionSpinner = findViewById(R.id.section_spinner);
        generateReportButton = findViewById(R.id.generate_report_button);
        recyclerView = findViewById(R.id.reportRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        nodataLayout = findViewById(R.id.nodata_layout);

        titleTextView.setText("Boys/Girls Ratio Report");
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
                    // Use generic adapter with custom fields
                    com.qdocs.ssre241123.adapters.GenericReportAdapter adapter =
                        new com.qdocs.ssre241123.adapters.GenericReportAdapter(this, "class", "ratio_detail", "");
                    adapter.setData(data);
                    recyclerView.setAdapter(adapter);
                } else {
                    showNoData();
                    Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
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
