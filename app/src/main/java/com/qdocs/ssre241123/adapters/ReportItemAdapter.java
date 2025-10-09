package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.model.ReportItem;
import com.qdocs.ssre241123.teachers.ClassSectionReportActivity;
import com.qdocs.ssre241123.teachers.ClassSubjectReportActivity;
import com.qdocs.ssre241123.teachers.GuardianReportActivity;
import com.qdocs.ssre241123.teachers.OnlineAdmissionReportActivity;
import com.qdocs.ssre241123.teachers.ParentLoginActivity;
import com.qdocs.ssre241123.teachers.StudentHistoryActivity;
import com.qdocs.ssre241123.teachers.StudentLoginActivity;
import com.qdocs.ssre241123.teachers.StudentReportActivity;
import com.qdocs.ssre241123.teachers.StudentTeacherRatioActivity;
import com.qdocs.ssre241123.teachers.TeacherReportDetailActivity;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import java.util.List;

public class ReportItemAdapter extends RecyclerView.Adapter<ReportItemAdapter.ReportItemViewHolder> {

    private static final String TAG = "ReportItemAdapter";
    private Context context;
    private List<ReportItem> reportItems;

    public ReportItemAdapter(Context context, List<ReportItem> reportItems) {
        this.context = context;
        this.reportItems = reportItems;
    }

    @NonNull
    @Override
    public ReportItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_report_item, parent, false);
        return new ReportItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportItemViewHolder holder, int position) {
        ReportItem reportItem = reportItems.get(position);
        
        holder.reportItemName.setText(reportItem.getDisplayName());
        holder.reportItemIcon.setImageResource(reportItem.getIconResource());
        
        // Hide description for now
        holder.reportItemDescription.setVisibility(View.GONE);
        
        // Apply theme colors
        String hintColor = Utility.getSharedPreferences(context, Constants.secondaryColour);
        if (hintColor != null && !hintColor.isEmpty()) {
            try {
                holder.reportItemIcon.setColorFilter(android.graphics.Color.parseColor(hintColor));
                holder.reportItemArrow.setColorFilter(android.graphics.Color.parseColor(hintColor));
            } catch (Exception e) {
                // Use default colors if parsing fails
            }
        }

        holder.reportItemLayout.setOnClickListener(v -> handleReportItemClick(reportItem));
    }

    private void handleReportItemClick(ReportItem reportItem) {
        Log.d(TAG, "=== Report Item Clicked ===");
        Log.d(TAG, "Report ID: " + reportItem.getId());
        Log.d(TAG, "Report Name: " + reportItem.getDisplayName());
        Log.d(TAG, "Category ID: " + reportItem.getCategoryId());

        Intent intent;

        // Route to specific activities based on report ID
        if ("1".equals(reportItem.getId()) || "student_report".equals(reportItem.getId())) {
            // Launch StudentReportActivity for Student Report
            Log.d(TAG, "Launching StudentReportActivity");
            intent = new Intent(context, StudentReportActivity.class);
        } else if ("2".equals(reportItem.getId()) || "student_history".equals(reportItem.getId())) {
            // Launch StudentHistoryActivity for Student History (Admission Report)
            Log.d(TAG, "Launching StudentHistoryActivity");
            intent = new Intent(context, StudentHistoryActivity.class);
        } else if ("parent_login_credential".equals(reportItem.getId())) {
            // Launch ParentLoginActivity for Parent Login Credential Report
            Log.d(TAG, "Launching ParentLoginActivity");
            intent = new Intent(context, ParentLoginActivity.class);
        } else if ("student_login_credential".equals(reportItem.getId())) {
            // Launch StudentLoginActivity for Student Login Credential Report
            Log.d(TAG, "Launching StudentLoginActivity");
            intent = new Intent(context, StudentLoginActivity.class);
        } else if ("guardian_report".equals(reportItem.getId())) {
            // Launch GuardianReportActivity for Guardian Report
            Log.d(TAG, "Launching GuardianReportActivity");
            intent = new Intent(context, GuardianReportActivity.class);
        } else if ("class_section_report".equals(reportItem.getId())) {
            // Launch ClassSectionReportActivity for Class Section Report
            Log.d(TAG, "Launching ClassSectionReportActivity");
            intent = new Intent(context, ClassSectionReportActivity.class);
        } else if ("student_teacher_ratio_report".equals(reportItem.getId())) {
            // Launch StudentTeacherRatioActivity for Student Teacher Ratio Report
            Log.d(TAG, "Launching StudentTeacherRatioActivity");
            intent = new Intent(context, StudentTeacherRatioActivity.class);
        } else if ("class_subject_report".equals(reportItem.getId())) {
            // Launch ClassSubjectReportActivity for Class Subject Report
            Log.d(TAG, "Launching ClassSubjectReportActivity");
            intent = new Intent(context, ClassSubjectReportActivity.class);
        } else if ("online_admission_report".equals(reportItem.getId())) {
            // Launch OnlineAdmissionReportActivity for Online Admission Report
            Log.d(TAG, "Launching OnlineAdmissionReportActivity");
            intent = new Intent(context, OnlineAdmissionReportActivity.class);
        } else {
            // Launch generic TeacherReportDetailActivity for other reports
            Log.d(TAG, "Launching TeacherReportDetailActivity");
            intent = new Intent(context, TeacherReportDetailActivity.class);
        }

        intent.putExtra("report_id", reportItem.getId());
        intent.putExtra("report_name", reportItem.getDisplayName());
        intent.putExtra("category_id", reportItem.getCategoryId());

        Log.d(TAG, "Starting activity: " + intent.getComponent().getClassName());

        context.startActivity(intent);
        if (context instanceof android.app.Activity) {
            ((android.app.Activity) context).overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
        }
    }

    @Override
    public int getItemCount() {
        return reportItems.size();
    }

    public static class ReportItemViewHolder extends RecyclerView.ViewHolder {
        CardView reportItemCard;
        LinearLayout reportItemLayout;
        ImageView reportItemIcon;
        TextView reportItemName;
        TextView reportItemDescription;
        ImageView reportItemArrow;

        public ReportItemViewHolder(@NonNull View itemView) {
            super(itemView);
            reportItemCard = itemView.findViewById(R.id.report_item_card);
            reportItemLayout = itemView.findViewById(R.id.report_item_layout);
            reportItemIcon = itemView.findViewById(R.id.report_item_icon);
            reportItemName = itemView.findViewById(R.id.report_item_name);
            reportItemDescription = itemView.findViewById(R.id.report_item_description);
            reportItemArrow = itemView.findViewById(R.id.report_item_arrow);
        }
    }
}
