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
import com.qdocs.ssre241123.teachers.BiometricAttlogReportActivity;
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
import com.qdocs.ssre241123.teachers.AuditLogReportActivity;
import com.qdocs.ssre241123.teachers.BoysGirlsRatioReportActivity;
import com.qdocs.ssre241123.teachers.SiblingReportActivity;
import com.qdocs.ssre241123.teachers.StaffReportActivity;
import com.qdocs.ssre241123.teachers.LessonPlanReportActivity;
import com.qdocs.ssre241123.teachers.TeacherSyllabusStatusReportActivity;
import com.qdocs.ssre241123.teachers.OnlineExamsReportActivity;
import com.qdocs.ssre241123.teachers.OnlineExamRankReportActivity;
import com.qdocs.ssre241123.teachers.OnlineExamAttendReportActivity;
import com.qdocs.ssre241123.teachers.InternalResultReportActivity;
import com.qdocs.ssre241123.teachers.ExternalResultReportActivity;
import com.qdocs.ssre241123.teachers.HomeworkReportActivity;
import com.qdocs.ssre241123.teachers.DailyAssignmentReportActivity;
import com.qdocs.ssre241123.teachers.EvaluationReportActivity;
import com.qdocs.ssre241123.teachers.StudentBookIssueReportActivity;
import com.qdocs.ssre241123.teachers.BookDueReportActivity;
import com.qdocs.ssre241123.teachers.BookInventoryReportActivity;
import com.qdocs.ssre241123.teachers.IssueReturnReportActivity;
import com.qdocs.ssre241123.teachers.InventoryStockReportActivity;
import com.qdocs.ssre241123.teachers.AddItemReportActivity;
import com.qdocs.ssre241123.teachers.IssueInventoryReportActivity;
import com.qdocs.ssre241123.teachers.StudentTransportDetailsReportActivity;
import com.qdocs.ssre241123.teachers.StudentHostelDetailsReportActivity;
import com.qdocs.ssre241123.teachers.StudentAcademicReportActivity;
import com.qdocs.ssre241123.teachers.TotalStudentAcademicReportActivity;
import com.qdocs.ssre241123.teachers.FeeGroupwiseCollectionReportActivity;
import com.qdocs.ssre241123.teachers.SessionFeeStructureReportActivity;
import com.qdocs.ssre241123.teachers.GenericReportActivity;
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
        } else if ("biometric_attendance_log".equals(reportItem.getId()) ||
                   "biometric_attlog_report".equals(reportItem.getId())) {
            // Launch BiometricAttlogReportActivity for Biometric Attendance Log Report
            Log.d(TAG, "Launching BiometricAttlogReportActivity");
            intent = new Intent(context, BiometricAttlogReportActivity.class);
        } else if ("sibling_report".equals(reportItem.getId())) {
            Log.d(TAG, "Launching SiblingReportActivity");
            intent = new Intent(context, SiblingReportActivity.class);
        } else if ("student_gender_ratio_report".equals(reportItem.getId())) {
            Log.d(TAG, "Launching BoysGirlsRatioReportActivity");
            intent = new Intent(context, BoysGirlsRatioReportActivity.class);
        } else if ("boys_girls_ratio".equals(reportItem.getId()) || "boys_girls_ratio_report".equals(reportItem.getId())) {
            Log.d(TAG, "Launching BoysGirlsRatioReportActivity");
            intent = new Intent(context, BoysGirlsRatioReportActivity.class);
        } else if ("staff_report".equals(reportItem.getId()) || "staff_report_hr".equals(reportItem.getId()) || "payroll_report_hr".equals(reportItem.getId())) {
            Log.d(TAG, "Launching StaffReportActivity");
            intent = new Intent(context, StaffReportActivity.class);
        } else if ("subject_lesson_plan_report".equals(reportItem.getId()) || "lesson_plan_report".equals(reportItem.getId())) {
            Log.d(TAG, "Launching LessonPlanReportActivity");
            intent = new Intent(context, LessonPlanReportActivity.class);
        } else if ("syllabus_status_report".equals(reportItem.getId()) || "teacher_syllabus_status_report".equals(reportItem.getId())) {
            Log.d(TAG, "Launching TeacherSyllabusStatusReportActivity");
            intent = new Intent(context, TeacherSyllabusStatusReportActivity.class);
        } else if ("exams_report".equals(reportItem.getId()) || "online_exams".equals(reportItem.getId())) {
            Log.d(TAG, "Launching OnlineExamsReportActivity");
            intent = new Intent(context, OnlineExamsReportActivity.class);
        } else if ("exams_rank_report".equals(reportItem.getId()) || "online_exam_rank".equals(reportItem.getId())) {
            Log.d(TAG, "Launching OnlineExamRankReportActivity");
            intent = new Intent(context, OnlineExamRankReportActivity.class);
        } else if ("student_exams_attempt_report".equals(reportItem.getId()) || "online_exam_attend".equals(reportItem.getId())) {
            Log.d(TAG, "Launching OnlineExamAttendReportActivity");
            intent = new Intent(context, OnlineExamAttendReportActivity.class);
        } else if ("result_report".equals(reportItem.getId()) || "internal_result".equals(reportItem.getId())) {
            Log.d(TAG, "Launching InternalResultReportActivity");
            intent = new Intent(context, InternalResultReportActivity.class);
        } else if ("external_result".equals(reportItem.getId())) {
            Log.d(TAG, "Launching ExternalResultReportActivity");
            intent = new Intent(context, ExternalResultReportActivity.class);
        } else if ("homework_report".equals(reportItem.getId())) {
            Log.d(TAG, "Launching HomeworkReportActivity");
            intent = new Intent(context, HomeworkReportActivity.class);
        } else if ("homework_evaluation_report".equals(reportItem.getId()) || "evaluation_report".equals(reportItem.getId())) {
            Log.d(TAG, "Launching EvaluationReportActivity");
            intent = new Intent(context, EvaluationReportActivity.class);
        } else if ("daily_assignment_report".equals(reportItem.getId())) {
            Log.d(TAG, "Launching DailyAssignmentReportActivity");
            intent = new Intent(context, DailyAssignmentReportActivity.class);
        } else if ("book_issue_report".equals(reportItem.getId())) {
            Log.d(TAG, "Launching StudentBookIssueReportActivity");
            intent = new Intent(context, StudentBookIssueReportActivity.class);
        } else if ("book_due_report".equals(reportItem.getId())) {
            Log.d(TAG, "Launching BookDueReportActivity");
            intent = new Intent(context, BookDueReportActivity.class);
        } else if ("book_inventory_report".equals(reportItem.getId())) {
            Log.d(TAG, "Launching BookInventoryReportActivity");
            intent = new Intent(context, BookInventoryReportActivity.class);
        } else if ("book_issue_return_report".equals(reportItem.getId()) || "issue_return".equals(reportItem.getId())) {
            Log.d(TAG, "Launching IssueReturnReportActivity");
            intent = new Intent(context, IssueReturnReportActivity.class);
        } else if ("stock_report".equals(reportItem.getId()) || "inventory_stock_report".equals(reportItem.getId())) {
            Log.d(TAG, "Launching InventoryStockReportActivity");
            intent = new Intent(context, InventoryStockReportActivity.class);
        } else if ("add_item_report".equals(reportItem.getId())) {
            Log.d(TAG, "Launching AddItemReportActivity");
            intent = new Intent(context, AddItemReportActivity.class);
        } else if ("issue_item_report".equals(reportItem.getId()) || "issue_inventory_report".equals(reportItem.getId())) {
            Log.d(TAG, "Launching IssueInventoryReportActivity");
            intent = new Intent(context, IssueInventoryReportActivity.class);
        } else if ("student_transport_details".equals(reportItem.getId())) {
            Log.d(TAG, "Launching StudentTransportDetailsReportActivity");
            intent = new Intent(context, StudentTransportDetailsReportActivity.class);
        } else if ("student_hostel_details".equals(reportItem.getId())) {
            Log.d(TAG, "Launching StudentHostelDetailsReportActivity");
            intent = new Intent(context, StudentHostelDetailsReportActivity.class);
        } else if ("student_academic_report".equals(reportItem.getId())) {
            Log.d(TAG, "Launching StudentAcademicReportActivity");
            intent = new Intent(context, StudentAcademicReportActivity.class);
        } else if ("total_student_academic_report".equals(reportItem.getId())) {
            Log.d(TAG, "Launching TotalStudentAcademicReportActivity");
            intent = new Intent(context, TotalStudentAcademicReportActivity.class);
        } else if ("feegroupwise_collection".equals(reportItem.getId()) || "feegroupwise_collection_report".equals(reportItem.getId())) {
            Log.d(TAG, "Launching FeeGroupwiseCollectionReportActivity");
            intent = new Intent(context, FeeGroupwiseCollectionReportActivity.class);
        } else if ("session_fee_structure".equals(reportItem.getId())) {
            Log.d(TAG, "Launching SessionFeeStructureReportActivity");
            intent = new Intent(context, SessionFeeStructureReportActivity.class);
        } else if ("audit_log".equals(reportItem.getId()) || "audit_log_report".equals(reportItem.getId())) {
            Log.d(TAG, "Launching AuditLogReportActivity");
            intent = new Intent(context, AuditLogReportActivity.class);
        } else if ("report_by_name".equals(reportItem.getId())) {
            Log.d(TAG, "Launching GenericReportActivity for report_by_name");
            intent = new Intent(context, GenericReportActivity.class);
            intent.putExtra("api_endpoint", "report-by-name/filter");
            intent.putExtra("title_field", "name");
            intent.putExtra("subtitle_field", "class");
            intent.putExtra("detail_field", "amount");
        } else if ("login_detail_report".equals(reportItem.getId()) || "logindetailreport".equals(reportItem.getId())) {
            Log.d(TAG, "Launching StudentLoginActivity");
            intent = new Intent(context, StudentLoginActivity.class);
        } else if ("parent_login_detail_report".equals(reportItem.getId()) || "parentlogindetailreport".equals(reportItem.getId())) {
            Log.d(TAG, "Launching ParentLoginActivity");
            intent = new Intent(context, ParentLoginActivity.class);
        } else {
            // Launch generic TeacherReportDetailActivity for any remaining unhandled reports
            Log.d(TAG, "Launching TeacherReportDetailActivity for: " + reportItem.getId());
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
