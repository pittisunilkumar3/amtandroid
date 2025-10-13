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
import com.qdocs.ssre241123.teachers.AdmissionReportActivity;
import com.qdocs.ssre241123.teachers.BalanceFeesReportActivity;
import com.qdocs.ssre241123.teachers.BalanceFeesReportWithRemarkActivity;
import com.qdocs.ssre241123.teachers.ClassAttendanceReportActivity;
import com.qdocs.ssre241123.teachers.ClassSectionReportActivity;
import com.qdocs.ssre241123.teachers.ClassSubjectReportActivity;
import com.qdocs.ssre241123.teachers.DailyAttendanceReportActivity;
import com.qdocs.ssre241123.teachers.DailyCollectionReportActivity;
import com.qdocs.ssre241123.teachers.DueFeeReportActivity;
import com.qdocs.ssre241123.teachers.ExpenseGroupReportActivity;
import com.qdocs.ssre241123.teachers.FeeCollectionReportColumnWiseActivity;
import com.qdocs.ssre241123.teachers.FeesCollectionReportActivity;
import com.qdocs.ssre241123.teachers.FeesStatementActivity;
import com.qdocs.ssre241123.teachers.IncomeGroupReportActivity;
import com.qdocs.ssre241123.teachers.GuardianReportActivity;
import com.qdocs.ssre241123.teachers.IncomeReportActivity;
import com.qdocs.ssre241123.teachers.ExpenseReportActivity;
import com.qdocs.ssre241123.teachers.PayrollReportActivity;
import com.qdocs.ssre241123.teachers.OnlineAdmissionReportActivity;
import com.qdocs.ssre241123.teachers.OnlineAdmissionFeeReportActivity;
import com.qdocs.ssre241123.teachers.OnlineFeesReportActivity;
import com.qdocs.ssre241123.teachers.StaffAttendanceReportActivity;
import com.qdocs.ssre241123.teachers.OtherFeeAndCollectionFeeCombinedActivity;
import com.qdocs.ssre241123.teachers.OtherFeesCollectionReportActivity;
import com.qdocs.ssre241123.teachers.OtherCollectionReportActivity;
import com.qdocs.ssre241123.teachers.ParentLoginActivity;
import com.qdocs.ssre241123.teachers.StudentHistoryActivity;
import com.qdocs.ssre241123.teachers.StudentLoginActivity;
import com.qdocs.ssre241123.teachers.StudentProfileReportActivity;
import com.qdocs.ssre241123.teachers.StudentReportActivity;
import com.qdocs.ssre241123.teachers.StudentTeacherRatioActivity;
import com.qdocs.ssre241123.teachers.TeacherReportDetailActivity;
import com.qdocs.ssre241123.teachers.TotalBalanceFeesReportActivity;
import com.qdocs.ssre241123.teachers.TotalFeeCollectionReportActivity;
import com.qdocs.ssre241123.teachers.TypeWiseBalanceReportActivity;
import com.qdocs.ssre241123.teachers.UserLogReportActivity;
import com.qdocs.ssre241123.teachers.YearReportDueFeeActivity;
import com.qdocs.ssre241123.teachers.AlumniReportActivity;
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
        } else if ("student_profile_report".equals(reportItem.getId()) || "student_profile".equals(reportItem.getId())) {
            // Launch StudentProfileReportActivity for Student Profile Report
            Log.d(TAG, "Launching StudentProfileReportActivity");
            intent = new Intent(context, StudentProfileReportActivity.class);
        } else if ("admission_report".equals(reportItem.getId())) {
            // Launch AdmissionReportActivity for Admission Report
            Log.d(TAG, "Launching AdmissionReportActivity");
            intent = new Intent(context, AdmissionReportActivity.class);
        } else if ("total_balance_fees_statement".equals(reportItem.getId())) {
            // Launch DueFeeReportActivity for Total Balance Fees Statement
            Log.d(TAG, "Launching DueFeeReportActivity");
            intent = new Intent(context, DueFeeReportActivity.class);
        } else if ("balance_fees_statement".equals(reportItem.getId())) {
            // Launch YearReportDueFeeActivity for Balance Fees Statement (Year Report)
            Log.d(TAG, "Launching YearReportDueFeeActivity");
            intent = new Intent(context, YearReportDueFeeActivity.class);
        } else if ("daily_collection_report".equals(reportItem.getId())) {
            // Launch DailyCollectionReportActivity for Daily Collection Report
            Log.d(TAG, "Launching DailyCollectionReportActivity");
            intent = new Intent(context, DailyCollectionReportActivity.class);
        } else if ("online_fees_collection_report".equals(reportItem.getId())) {
            // Launch OnlineFeesReportActivity for Online Fees Collection Report
            Log.d(TAG, "Launching OnlineFeesReportActivity");
            intent = new Intent(context, OnlineFeesReportActivity.class);
        } else if ("online_admission_fees_collection_report".equals(reportItem.getId()) ||
                   "online_admission_fee_collection_report".equals(reportItem.getId())) {
            // Launch OnlineAdmissionFeeReportActivity for Online Admission Fee Collection Report
            Log.d(TAG, "Launching OnlineAdmissionFeeReportActivity");
            intent = new Intent(context, OnlineAdmissionFeeReportActivity.class);
        } else if ("type_wise_balance_report".equals(reportItem.getId())) {
            // Launch TypeWiseBalanceReportActivity for Type Wise Balance Report
            Log.d(TAG, "Launching TypeWiseBalanceReportActivity");
            intent = new Intent(context, TypeWiseBalanceReportActivity.class);
        } else if ("fee_collection_report_column_wise".equals(reportItem.getId())) {
            // Launch FeeCollectionReportColumnWiseActivity for Fee Collection Report Column Wise
            Log.d(TAG, "Launching FeeCollectionReportColumnWiseActivity");
            intent = new Intent(context, FeeCollectionReportColumnWiseActivity.class);
        } else if ("total_balance_fees_report".equals(reportItem.getId())) {
            // Launch TotalBalanceFeesReportActivity for Total Balance Fees Report
            Log.d(TAG, "Launching TotalBalanceFeesReportActivity");
            intent = new Intent(context, TotalBalanceFeesReportActivity.class);
        } else if ("total_fee_collection_report".equals(reportItem.getId())) {
            // Launch TotalFeeCollectionReportActivity for Total Fee Collection Report
            Log.d(TAG, "Launching TotalFeeCollectionReportActivity");
            intent = new Intent(context, TotalFeeCollectionReportActivity.class);
        } else if ("fees_collection_report".equals(reportItem.getId())) {
            // Launch FeesCollectionReportActivity for Fees Collection Report
            Log.d(TAG, "Launching FeesCollectionReportActivity");
            intent = new Intent(context, FeesCollectionReportActivity.class);
        } else if ("other_fees_collection_report".equals(reportItem.getId())) {
            // Launch OtherFeesCollectionReportActivity for Other Fees Collection Report
            Log.d(TAG, "Launching OtherFeesCollectionReportActivity");
            intent = new Intent(context, OtherFeesCollectionReportActivity.class);
        } else if ("other_collection_report".equals(reportItem.getId())) {
            // Launch OtherCollectionReportActivity for Other Collection Report
            Log.d(TAG, "Launching OtherCollectionReportActivity");
            intent = new Intent(context, OtherCollectionReportActivity.class);
        } else if ("other_fee_and_collection_fee_combined".equals(reportItem.getId())) {
            // Launch OtherFeeAndCollectionFeeCombinedActivity for Other Fee and Collection Fee Combined
            Log.d(TAG, "Launching OtherFeeAndCollectionFeeCombinedActivity");
            intent = new Intent(context, OtherFeeAndCollectionFeeCombinedActivity.class);
        } else if ("balance_fees_report".equals(reportItem.getId())) {
            // Launch BalanceFeesReportActivity for Balance Fees Report
            Log.d(TAG, "Launching BalanceFeesReportActivity");
            intent = new Intent(context, BalanceFeesReportActivity.class);
        } else if ("balance_fees_report_with_remark".equals(reportItem.getId())) {
            // Launch BalanceFeesReportWithRemarkActivity for Balance Fees Report With Remark
            Log.d(TAG, "Launching BalanceFeesReportWithRemarkActivity");
            intent = new Intent(context, BalanceFeesReportWithRemarkActivity.class);
        } else if ("fees_statement".equals(reportItem.getId())) {
            // Launch FeesStatementActivity for Fees Statement
            Log.d(TAG, "Launching FeesStatementActivity");
            intent = new Intent(context, FeesStatementActivity.class);
        } else if ("income_report".equals(reportItem.getId())) {
            // Launch IncomeReportActivity for Income Report
            Log.d(TAG, "Launching IncomeReportActivity");
            intent = new Intent(context, IncomeReportActivity.class);
        } else if ("income_group_report".equals(reportItem.getId())) {
            // Launch IncomeGroupReportActivity for Income Group Report
            Log.d(TAG, "Launching IncomeGroupReportActivity");
            intent = new Intent(context, IncomeGroupReportActivity.class);
        } else if ("expense_report".equals(reportItem.getId())) {
            // Launch ExpenseReportActivity for Expense Report
            Log.d(TAG, "Launching ExpenseReportActivity");
            intent = new Intent(context, ExpenseReportActivity.class);
        } else if ("expense_group_report".equals(reportItem.getId())) {
            // Launch ExpenseGroupReportActivity for Expense Group Report
            Log.d(TAG, "Launching ExpenseGroupReportActivity");
            intent = new Intent(context, ExpenseGroupReportActivity.class);
        } else if ("payroll_report".equals(reportItem.getId())) {
            // Launch PayrollReportActivity for Payroll Report
            Log.d(TAG, "Launching PayrollReportActivity");
            intent = new Intent(context, PayrollReportActivity.class);
        } else if ("user_log".equals(reportItem.getId()) || "user_log_report".equals(reportItem.getId())) {
            // Launch UserLogReportActivity for User Log Report
            Log.d(TAG, "Launching UserLogReportActivity");
            intent = new Intent(context, UserLogReportActivity.class);
        } else if ("alumni".equals(reportItem.getId()) || "alumni_report".equals(reportItem.getId())) {
            // Launch AlumniReportActivity for Alumni Report
            Log.d(TAG, "Launching AlumniReportActivity");
            intent = new Intent(context, AlumniReportActivity.class);
        } else if ("attendance_report".equals(reportItem.getId())) {
            // Launch ClassAttendanceReportActivity for Class Attendance Report
            Log.d(TAG, "Launching ClassAttendanceReportActivity");
            intent = new Intent(context, ClassAttendanceReportActivity.class);
        } else if ("daily_attendance_report".equals(reportItem.getId())) {
            // Launch DailyAttendanceReportActivity for Daily Attendance Report
            Log.d(TAG, "Launching DailyAttendanceReportActivity");
            intent = new Intent(context, DailyAttendanceReportActivity.class);
        } else if ("staff_attendance_report".equals(reportItem.getId())) {
            // Launch StaffAttendanceReportActivity for Staff Attendance Report
            Log.d(TAG, "Launching StaffAttendanceReportActivity");
            intent = new Intent(context, StaffAttendanceReportActivity.class);
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
