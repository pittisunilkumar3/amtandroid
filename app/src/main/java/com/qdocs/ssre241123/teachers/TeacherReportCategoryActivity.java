package com.qdocs.ssre241123.teachers;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.BaseActivity;
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.ReportItemAdapter;
import com.qdocs.ssre241123.model.ReportItem;
import com.qdocs.ssre241123.utils.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeacherReportCategoryActivity extends BaseActivity {

    private RecyclerView reportItemsRecyclerView;
    private ReportItemAdapter adapter;
    private ImageView backButton;
    private TextView titleTextView;
    private TextView categoryTitleTextView;
    
    private String categoryId;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_report_category);

        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), "langCode"));

        getIntentData();
        initializeViews();
        setupRecyclerView();
        loadReportItems();
    }

    private void getIntentData() {
        categoryId = getIntent().getStringExtra("category_id");
        categoryName = getIntent().getStringExtra("category_name");
    }

    private void initializeViews() {
        reportItemsRecyclerView = findViewById(R.id.report_items_recyclerView);
        backButton = findViewById(R.id.back_button);
        titleTextView = findViewById(R.id.title);
        categoryTitleTextView = findViewById(R.id.category_title);

        // Set title
        if (categoryName != null) {
            titleTextView.setText(categoryName);
            categoryTitleTextView.setText(categoryName + " Reports");
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_rightleft, R.anim.no_animation);
            }
        });
    }

    private void setupRecyclerView() {
        reportItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadReportItems() {
        List<ReportItem> reportItems = getReportItemsForCategory(categoryId);
        adapter = new ReportItemAdapter(this, reportItems);
        reportItemsRecyclerView.setAdapter(adapter);
    }

    private List<ReportItem> getReportItemsForCategory(String categoryId) {
        List<ReportItem> reportItems = new ArrayList<>();

        if (categoryId == null) {
            return reportItems;
        }

        switch (categoryId) {
            case "student_information":
                reportItems = Arrays.asList(
                    new ReportItem("student_report", "student_report", getString(R.string.student_report), "student_information", R.drawable.ic_fa_user),
                    new ReportItem("student_history", "student_history", getString(R.string.student_history), "student_information", R.drawable.ic_fa_history),
                    new ReportItem("class_subject_report", "class_subject_report", getString(R.string.class_subject_report), "student_information", R.drawable.ic_fa_book),
                    new ReportItem("student_profile_report", "student_profile_report", getString(R.string.student_profile_report), "student_information", R.drawable.ic_fa_user),
                    new ReportItem("online_admission_report", "online_admission_report", getString(R.string.online_admission_report), "student_information", R.drawable.ic_fa_globe),
                    new ReportItem("class_section_report", "class_section_report", getString(R.string.class_section_report), "student_information", R.drawable.ic_fa_users),
                    new ReportItem("student_login_credential", "student_login_credential", getString(R.string.student_login_credential), "student_information", R.drawable.ic_fa_key),
                    new ReportItem("admission_report", "admission_report", getString(R.string.admission_report), "student_information", R.drawable.ic_fa_file_text),
                    new ReportItem("student_gender_ratio_report", "student_gender_ratio_report", getString(R.string.student_gender_ratio_report), "student_information", R.drawable.ic_fa_pie_chart),
                    new ReportItem("guardian_report", "guardian_report", getString(R.string.guardian_report), "student_information", R.drawable.ic_fa_user_plus),
                    new ReportItem("parent_login_credential", "parent_login_credential", getString(R.string.parent_login_credential), "student_information", R.drawable.ic_fa_key),
                    new ReportItem("sibling_report", "sibling_report", getString(R.string.sibling_report), "student_information", R.drawable.ic_fa_users),
                    new ReportItem("student_teacher_ratio_report", "student_teacher_ratio_report", getString(R.string.student_teacher_ratio_report), "student_information", R.drawable.ic_fa_bar_chart)
                );
                break;

            case "finance":
                reportItems = Arrays.asList(
                    new ReportItem("total_balance_fees_statement", "total_balance_fees_statement", getString(R.string.total_balance_fees_statement), "finance", R.drawable.ic_fa_money),
                    new ReportItem("type_wise_balance_report", "type_wise_balance_report", getString(R.string.type_wise_balance_report), "finance", R.drawable.ic_fa_bar_chart),
                    new ReportItem("total_balance_fees_report", "total_balance_fees_report", getString(R.string.total_balance_fees_report), "finance", R.drawable.ic_fa_money),
                    new ReportItem("other_fees_collection_report", "other_fees_collection_report", getString(R.string.other_fees_collection_report), "finance", R.drawable.ic_fa_credit_card),
                    new ReportItem("online_fees_collection_report", "online_fees_collection_report", getString(R.string.online_fees_collection_report), "finance", R.drawable.ic_fa_globe),
                    new ReportItem("expense_report", "expense_report", getString(R.string.expense_report), "finance", R.drawable.ic_fa_credit_card),
                    new ReportItem("expense_group_report", "expense_group_report", getString(R.string.expense_group_report), "finance", R.drawable.ic_fa_credit_card),
                    new ReportItem("balance_fees_statement", "balance_fees_statement", getString(R.string.balance_fees_statement), "finance", R.drawable.ic_fa_file_text),
                    new ReportItem("fees_statement", "fees_statement", getString(R.string.fees_statement), "finance", R.drawable.ic_fa_file_text),
                    new ReportItem("total_fee_collection_report", "total_fee_collection_report", getString(R.string.total_fee_collection_report), "finance", R.drawable.ic_fa_money),
                    new ReportItem("other_fee_and_collection_fee_combined", "other_fee_and_collection_fee_combined", getString(R.string.other_fee_and_collection_fee_combined), "finance", R.drawable.ic_fa_money),
                    new ReportItem("balance_fees_report_with_remark", "balance_fees_report_with_remark", getString(R.string.balance_fees_report_with_remark), "finance", R.drawable.ic_fa_file_text),
                    new ReportItem("payroll_report", "payroll_report", getString(R.string.payroll_report), "finance", R.drawable.ic_fa_money),
                    new ReportItem("online_admission_fees_collection_report", "online_admission_fees_collection_report", getString(R.string.online_admission_fees_collection_report), "finance", R.drawable.ic_fa_globe),
                    new ReportItem("daily_collection_report", "daily_collection_report", getString(R.string.daily_collection_report), "finance", R.drawable.ic_fa_calendar),
                    new ReportItem("balance_fees_report", "balance_fees_report", getString(R.string.balance_fees_report), "finance", R.drawable.ic_fa_file_text),
                    new ReportItem("fees_collection_report", "fees_collection_report", getString(R.string.fees_collection_report), "finance", R.drawable.ic_fa_money),
                    new ReportItem("fee_collection_report_column_wise", "fee_collection_report_column_wise", getString(R.string.fee_collection_report_column_wise), "finance", R.drawable.ic_fa_table),
                    new ReportItem("income_report", "income_report", getString(R.string.income_report), "finance", R.drawable.ic_fa_dollar),
                    new ReportItem("income_group_report", "income_group_report", getString(R.string.income_group_report), "finance", R.drawable.ic_fa_dollar)
                );
                break;

            case "attendance":
                reportItems = Arrays.asList(
                    new ReportItem("attendance_report", "attendance_report", getString(R.string.attendance_report), "attendance", R.drawable.ic_fa_calendar_check),
                    new ReportItem("student_attendance_type_report", "student_attendance_type_report", getString(R.string.student_attendance_type_report), "attendance", R.drawable.ic_fa_calendar_check),
                    new ReportItem("daily_attendance_report", "daily_attendance_report", getString(R.string.daily_attendance_report), "attendance", R.drawable.ic_fa_calendar),
                    new ReportItem("staff_attendance_report", "staff_attendance_report", getString(R.string.staff_attendance_report), "attendance", R.drawable.ic_fa_users),
                    new ReportItem("biometric_attendance_log", "biometric_attendance_log", getString(R.string.biometric_attendance_log), "attendance", R.drawable.ic_fa_fingerprint)
                );
                break;

            case "examinations":
                reportItems = Arrays.asList(
                    new ReportItem("rank_report", "rank_report", getString(R.string.rank_report), "examinations", R.drawable.ic_fa_trophy)
                );
                break;

            case "online_examinations":
                reportItems = Arrays.asList(
                    new ReportItem("result_report", "result_report", getString(R.string.result_report), "online_examinations", R.drawable.ic_fa_file_text),
                    new ReportItem("exams_report", "exams_report", getString(R.string.exams_report), "online_examinations", R.drawable.ic_fa_list_alt),
                    new ReportItem("student_exams_attempt_report", "student_exams_attempt_report", getString(R.string.student_exams_attempt_report), "online_examinations", R.drawable.ic_fa_user),
                    new ReportItem("exams_rank_report", "exams_rank_report", getString(R.string.exams_rank_report), "online_examinations", R.drawable.ic_fa_trophy)
                );
                break;

            case "lesson_plan":
                reportItems = Arrays.asList(
                    new ReportItem("syllabus_status_report", "syllabus_status_report", getString(R.string.syllabus_status_report), "lesson_plan", R.drawable.ic_fa_check_circle),
                    new ReportItem("subject_lesson_plan_report", "subject_lesson_plan_report", getString(R.string.subject_lesson_plan_report), "lesson_plan", R.drawable.ic_fa_book)
                );
                break;

            case "human_resource":
                reportItems = Arrays.asList(
                    new ReportItem("staff_report", "staff_report", getString(R.string.staff_report), "human_resource", R.drawable.ic_fa_users),
                    new ReportItem("payroll_report_hr", "payroll_report", getString(R.string.payroll_report), "human_resource", R.drawable.ic_fa_money)
                );
                break;

            case "homework":
                reportItems = Arrays.asList(
                    new ReportItem("homework_report", "homework_report", getString(R.string.homework_report), "homework", R.drawable.ic_fa_file_text),
                    new ReportItem("homework_evaluation_report", "homework_evaluation_report", getString(R.string.homework_evaluation_report), "homework", R.drawable.ic_fa_check_circle),
                    new ReportItem("daily_assignment_report", "daily_assignment_report", getString(R.string.daily_assignment_report), "homework", R.drawable.ic_fa_calendar)
                );
                break;

            case "library":
                reportItems = Arrays.asList(
                    new ReportItem("book_issue_report", "book_issue_report", getString(R.string.book_issue_report), "library", R.drawable.ic_fa_book),
                    new ReportItem("book_due_report", "book_due_report", getString(R.string.book_due_report), "library", R.drawable.ic_fa_calendar),
                    new ReportItem("book_inventory_report", "book_inventory_report", getString(R.string.book_inventory_report), "library", R.drawable.ic_fa_list_alt),
                    new ReportItem("book_issue_return_report", "book_issue_return_report", getString(R.string.book_issue_return_report), "library", R.drawable.ic_fa_check_circle)
                );
                break;

            case "inventory":
                reportItems = Arrays.asList(
                    new ReportItem("stock_report", "stock_report", getString(R.string.stock_report), "inventory", R.drawable.ic_fa_archive),
                    new ReportItem("add_item_report", "add_item_report", getString(R.string.add_item_report), "inventory", R.drawable.ic_fa_plus),
                    new ReportItem("issue_item_report", "issue_item_report", getString(R.string.issue_item_report), "inventory", R.drawable.ic_fa_file_text)
                );
                break;

            default:
                // For categories without specific reports yet (transport, hostel, alumni, user_log, audit_trail)
                reportItems = new ArrayList<>();
                break;
        }

        return reportItems;
    }
}
