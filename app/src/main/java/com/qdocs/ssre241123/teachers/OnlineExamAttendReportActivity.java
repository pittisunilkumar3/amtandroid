package com.qdocs.ssre241123.teachers;

import android.os.Bundle;
import android.view.View;

import com.qdocs.ssre241123.R;

/**
 * Online Exam Attend Report Activity
 * API: online-exam-attend-report/filter
 */
public class OnlineExamAttendReportActivity extends GenericReportActivity {
    @Override
    protected void initViews() {
        apiEndpoint = "online-exam-attend-report/filter";
        titleField = "student_name";
        subtitleField = "exam";
        detailField = "status";

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

        titleTextView.setText("Online Exam Attend Report");
        applyThemeColor(actionBar, generateReportButton);
        backButton.setOnClickListener(v -> { finish(); overridePendingTransition(R.anim.slide_rightleft, R.anim.no_animation); });
        generateReportButton.setOnClickListener(v -> generateReport());
    }
}
