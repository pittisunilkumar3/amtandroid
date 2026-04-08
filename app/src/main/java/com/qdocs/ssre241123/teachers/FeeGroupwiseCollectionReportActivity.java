package com.qdocs.ssre241123.teachers;

import android.os.Bundle;
import android.view.View;

import com.qdocs.ssre241123.R;

/**
 * Fee Group-wise Collection Report Activity
 * API: feegroupwise-collection-report/filter
 */
public class FeeGroupwiseCollectionReportActivity extends GenericReportActivity {
    @Override
    protected void initViews() {
        apiEndpoint = "feegroupwise-collection-report/filter";
        titleField = "fee_group";
        subtitleField = "class";
        detailField = "amount";

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

        titleTextView.setText("Fee Group-wise Collection Report");
        applyThemeColor(actionBar, generateReportButton);
        backButton.setOnClickListener(v -> { finish(); overridePendingTransition(R.anim.slide_rightleft, R.anim.no_animation); });
        generateReportButton.setOnClickListener(v -> generateReport());
    }
}
