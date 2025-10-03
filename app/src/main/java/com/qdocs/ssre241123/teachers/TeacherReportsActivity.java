package com.qdocs.ssre241123.teachers;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.qdocs.ssre241123.BaseActivity;
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.ReportCategoryAdapter;
import com.qdocs.ssre241123.model.MenuResponse;
import com.qdocs.ssre241123.model.ReportCategory;
import com.qdocs.ssre241123.model.ReportItem;
import com.qdocs.ssre241123.model.SubMenuItem;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherReportsActivity extends BaseActivity {

    private static final String TAG = "TeacherReportsActivity";

    private RecyclerView reportsRecyclerView;
    private ReportCategoryAdapter adapter;
    private ImageView backButton;
    private ProgressBar progressBar;
    private TextView errorTextView;

    private boolean useApiData = true; // Set to true to use API, false for static data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_reports);

        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), "langCode"));

        initializeViews();
        setupRecyclerView();

        if (useApiData) {
            loadReportsFromAPI();
        } else {
            loadStaticReportCategories();
        }
    }

    private void initializeViews() {
        reportsRecyclerView = findViewById(R.id.reports_recyclerView);
        backButton = findViewById(R.id.back_button);
        progressBar = findViewById(R.id.progressBar);
        errorTextView = findViewById(R.id.error_text);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_rightleft, R.anim.no_animation);
            }
        });
    }

    private void setupRecyclerView() {
        reportsRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }

    private void showLoading() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        if (reportsRecyclerView != null) {
            reportsRecyclerView.setVisibility(View.GONE);
        }
        if (errorTextView != null) {
            errorTextView.setVisibility(View.GONE);
        }
    }

    private void showContent() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        if (reportsRecyclerView != null) {
            reportsRecyclerView.setVisibility(View.VISIBLE);
        }
        if (errorTextView != null) {
            errorTextView.setVisibility(View.GONE);
        }
    }

    private void showError(String message) {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        if (reportsRecyclerView != null) {
            reportsRecyclerView.setVisibility(View.GONE);
        }
        if (errorTextView != null) {
            errorTextView.setVisibility(View.VISIBLE);
            errorTextView.setText(message);
        }
    }

    private void loadReportsFromAPI() {
        showLoading();

        String url = Utility.buildApiUrl(getApplicationContext(), Constants.teacherMenuUrl);

        // Get staff ID from shared preferences
        String staffId = Utility.getSharedPreferences(getApplicationContext(), Constants.teacherStaffId);
        if (staffId == null || staffId.isEmpty()) {
            staffId = Utility.getSharedPreferences(getApplicationContext(), Constants.userId);
        }
        if (staffId == null || staffId.isEmpty()) {
            staffId = "1"; // Default fallback
        }

        // Create JSON request body
        Map<String, String> params = new HashMap<>();
        params.put("staff_id", staffId);

        JSONObject jsonBody = new JSONObject(params);
        final String requestBody = jsonBody.toString();

        Log.d(TAG, "=== API REQUEST ===");
        Log.d(TAG, "URL: " + url);
        Log.d(TAG, "Staff ID: " + staffId);
        Log.d(TAG, "Request Body: " + requestBody);
        Log.d(TAG, "==================");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "=== API RESPONSE ===");
                    Log.d(TAG, "Response: " + response.substring(0, Math.min(500, response.length())));
                    Log.d(TAG, "==================");

                    try {
                        Gson gson = new Gson();
                        MenuResponse menuResponse = gson.fromJson(response, MenuResponse.class);

                        if (menuResponse != null && menuResponse.getStatus() == 1 && menuResponse.getData() != null) {
                            List<com.qdocs.ssre241123.model.MenuItem> menus = menuResponse.getData().getMenus();
                            if (menus != null && !menus.isEmpty()) {
                                Log.d(TAG, "✓ Success: Received " + menus.size() + " menu items");

                                // Find the Reports menu item
                                com.qdocs.ssre241123.model.MenuItem reportsMenu = null;
                                for (com.qdocs.ssre241123.model.MenuItem menuItem : menus) {
                                    String menuName = menuItem.getMenu();
                                    String activateMenu = menuItem.getActivateMenu();

                                    // Check if this is the Reports menu
                                    if (menuName != null && (menuName.equalsIgnoreCase("Reports") ||
                                        menuName.equalsIgnoreCase("Report") ||
                                        (activateMenu != null && activateMenu.contains("report")))) {
                                        reportsMenu = menuItem;
                                        Log.d(TAG, "✓ Found Reports menu: " + menuName);
                                        break;
                                    }
                                }

                                if (reportsMenu != null && reportsMenu.getSubmenus() != null && !reportsMenu.getSubmenus().isEmpty()) {
                                    List<ReportCategory> categories = parseReportCategories(reportsMenu.getSubmenus());
                                    if (!categories.isEmpty()) {
                                        displayReportCategories(categories);
                                        showContent();
                                    } else {
                                        Log.w(TAG, "No report categories parsed, using static data");
                                        loadStaticReportCategories();
                                    }
                                } else {
                                    Log.w(TAG, "Reports menu has no submenus, using static data");
                                    loadStaticReportCategories();
                                }
                            } else {
                                Log.e(TAG, "✗ Error: No menu items in response");
                                loadStaticReportCategories();
                            }
                        } else {
                            String errorMsg = menuResponse != null ? menuResponse.getMessage() : "Unknown error";
                            Log.e(TAG, "✗ Error: " + errorMsg);
                            loadStaticReportCategories();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "✗ JSON parsing error: " + e.getMessage());
                        e.printStackTrace();
                        loadStaticReportCategories();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "=== API ERROR ===");
                    String errorMsg = "Network error";
                    if (error.networkResponse != null) {
                        errorMsg += " - Status Code: " + error.networkResponse.statusCode;
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            Log.e(TAG, "Error Response: " + responseBody);
                        } catch (Exception e) {
                            Log.e(TAG, "Could not parse error response");
                        }
                    }
                    if (error.getMessage() != null) {
                        Log.e(TAG, "Error Message: " + error.getMessage());
                    }
                    Log.e(TAG, "=================");

                    // Fallback to static data on error
                    Toast.makeText(TeacherReportsActivity.this, "Loading reports from cache", Toast.LENGTH_SHORT).show();
                    loadStaticReportCategories();
                }
            }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(getApplicationContext(), Constants.userId));

                // Add JWT token if available
                String token = Utility.getSharedPreferences(getApplicationContext(), Constants.teacherJwtToken);
                if (token != null && !token.isEmpty()) {
                    headers.put("Authorization", "Bearer " + token);
                }

                Log.d(TAG, "Request Headers: " + headers.toString());
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private List<ReportCategory> parseReportCategories(List<SubMenuItem> submenus) {
        List<ReportCategory> categories = new ArrayList<>();

        Log.d(TAG, "Parsing " + submenus.size() + " report categories from API");

        for (SubMenuItem submenu : submenus) {
            String categoryName = submenu.getMenu();
            String categoryKey = submenu.getKey();

            if (categoryName == null || categoryName.isEmpty()) {
                continue;
            }

            // Generate a category ID from the key or name
            String categoryId = categoryKey != null && !categoryKey.isEmpty() ?
                categoryKey.toLowerCase().replace(" ", "_") :
                categoryName.toLowerCase().replace(" ", "_");

            // Get icon resource based on category name
            int iconResource = getIconForCategory(categoryId, categoryName);

            // For now, create empty report items list - these will be populated when user clicks the category
            List<ReportItem> reportItems = new ArrayList<>();

            ReportCategory category = new ReportCategory(
                categoryId,
                categoryKey != null ? categoryKey : categoryId,
                categoryName,
                iconResource,
                reportItems
            );

            categories.add(category);
            Log.d(TAG, "Added category: " + categoryName + " (ID: " + categoryId + ")");
        }

        return categories;
    }

    private int getIconForCategory(String categoryId, String categoryName) {
        // Map category names/IDs to appropriate icons
        String lowerName = categoryName.toLowerCase();
        String lowerId = categoryId.toLowerCase();

        if (lowerName.contains("student") || lowerId.contains("student")) {
            return R.drawable.ic_fa_user;
        } else if (lowerName.contains("finance") || lowerName.contains("fee") || lowerId.contains("finance")) {
            return R.drawable.ic_fa_money;
        } else if (lowerName.contains("attendance") || lowerId.contains("attendance")) {
            return R.drawable.ic_fa_calendar_check;
        } else if (lowerName.contains("examination") || lowerName.contains("exam") || lowerId.contains("exam")) {
            return R.drawable.ic_fa_graduation_cap;
        } else if (lowerName.contains("lesson") || lowerName.contains("syllabus") || lowerId.contains("lesson")) {
            return R.drawable.ic_fa_book;
        } else if (lowerName.contains("human") || lowerName.contains("staff") || lowerId.contains("human")) {
            return R.drawable.ic_fa_users;
        } else if (lowerName.contains("homework") || lowerName.contains("assignment") || lowerId.contains("homework")) {
            return R.drawable.ic_fa_file_text;
        } else if (lowerName.contains("library") || lowerId.contains("library")) {
            return R.drawable.ic_fa_book;
        } else if (lowerName.contains("inventory") || lowerId.contains("inventory")) {
            return R.drawable.ic_fa_archive;
        } else if (lowerName.contains("transport") || lowerId.contains("transport")) {
            return R.drawable.ic_fa_bus;
        } else if (lowerName.contains("hostel") || lowerId.contains("hostel")) {
            return R.drawable.ic_fa_home;
        } else if (lowerName.contains("alumni") || lowerId.contains("alumni")) {
            return R.drawable.ic_fa_graduation_cap;
        } else if (lowerName.contains("log") || lowerName.contains("audit") || lowerId.contains("log")) {
            return R.drawable.ic_fa_search;
        } else {
            return R.drawable.ic_fa_bar_chart; // Default icon for reports
        }
    }

    private void displayReportCategories(List<ReportCategory> categories) {
        adapter = new ReportCategoryAdapter(this, categories);
        reportsRecyclerView.setAdapter(adapter);
    }

    private void loadStaticReportCategories() {
        showContent();
        List<ReportCategory> categories = new ArrayList<>();

        // Student Information Reports
        List<ReportItem> studentInfoReports = Arrays.asList(
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

        categories.add(new ReportCategory("student_information", "student_information", getString(R.string.student_information_reports), R.drawable.ic_fa_user, studentInfoReports));

        // Finance Reports
        List<ReportItem> financeReports = Arrays.asList(
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

        categories.add(new ReportCategory("finance", "finance", getString(R.string.finance_reports), R.drawable.ic_fa_money, financeReports));

        // Attendance Reports
        List<ReportItem> attendanceReports = Arrays.asList(
            new ReportItem("attendance_report", "attendance_report", getString(R.string.attendance_report), "attendance", R.drawable.ic_fa_calendar_check),
            new ReportItem("student_attendance_type_report", "student_attendance_type_report", getString(R.string.student_attendance_type_report), "attendance", R.drawable.ic_fa_calendar_check),
            new ReportItem("daily_attendance_report", "daily_attendance_report", getString(R.string.daily_attendance_report), "attendance", R.drawable.ic_fa_calendar),
            new ReportItem("staff_attendance_report", "staff_attendance_report", getString(R.string.staff_attendance_report), "attendance", R.drawable.ic_fa_users),
            new ReportItem("biometric_attendance_log", "biometric_attendance_log", getString(R.string.biometric_attendance_log), "attendance", R.drawable.ic_fa_fingerprint)
        );

        categories.add(new ReportCategory("attendance", "attendance", getString(R.string.attendance_reports), R.drawable.ic_fa_calendar_check, attendanceReports));

        // Examinations Reports
        List<ReportItem> examinationsReports = Arrays.asList(
            new ReportItem("rank_report", "rank_report", getString(R.string.rank_report), "examinations", R.drawable.ic_fa_trophy)
        );

        categories.add(new ReportCategory("examinations", "examinations", getString(R.string.examinations_reports), R.drawable.ic_fa_graduation_cap, examinationsReports));

        // Online Examinations Reports
        List<ReportItem> onlineExaminationsReports = Arrays.asList(
            new ReportItem("result_report", "result_report", getString(R.string.result_report), "online_examinations", R.drawable.ic_fa_file_text),
            new ReportItem("exams_report", "exams_report", getString(R.string.exams_report), "online_examinations", R.drawable.ic_fa_list_alt),
            new ReportItem("student_exams_attempt_report", "student_exams_attempt_report", getString(R.string.student_exams_attempt_report), "online_examinations", R.drawable.ic_fa_user),
            new ReportItem("exams_rank_report", "exams_rank_report", getString(R.string.exams_rank_report), "online_examinations", R.drawable.ic_fa_trophy)
        );

        categories.add(new ReportCategory("online_examinations", "online_examinations", getString(R.string.online_examinations_reports), R.drawable.ic_fa_globe, onlineExaminationsReports));

        // Lesson Plan Reports
        List<ReportItem> lessonPlanReports = Arrays.asList(
            new ReportItem("syllabus_status_report", "syllabus_status_report", getString(R.string.syllabus_status_report), "lesson_plan", R.drawable.ic_fa_check_circle),
            new ReportItem("subject_lesson_plan_report", "subject_lesson_plan_report", getString(R.string.subject_lesson_plan_report), "lesson_plan", R.drawable.ic_fa_book)
        );

        categories.add(new ReportCategory("lesson_plan", "lesson_plan", getString(R.string.lesson_plan_reports), R.drawable.ic_fa_book, lessonPlanReports));

        // Human Resource Reports
        List<ReportItem> humanResourceReports = Arrays.asList(
            new ReportItem("staff_report", "staff_report", getString(R.string.staff_report), "human_resource", R.drawable.ic_fa_users),
            new ReportItem("payroll_report_hr", "payroll_report", getString(R.string.payroll_report), "human_resource", R.drawable.ic_fa_money)
        );

        categories.add(new ReportCategory("human_resource", "human_resource", getString(R.string.human_resource_reports), R.drawable.ic_fa_users, humanResourceReports));

        // Homework Reports
        List<ReportItem> homeworkReports = Arrays.asList(
            new ReportItem("homework_report", "homework_report", getString(R.string.homework_report), "homework", R.drawable.ic_fa_file_text),
            new ReportItem("homework_evaluation_report", "homework_evaluation_report", getString(R.string.homework_evaluation_report), "homework", R.drawable.ic_fa_check_circle),
            new ReportItem("daily_assignment_report", "daily_assignment_report", getString(R.string.daily_assignment_report), "homework", R.drawable.ic_fa_calendar)
        );

        categories.add(new ReportCategory("homework", "homework", getString(R.string.homework_reports), R.drawable.ic_fa_file_text, homeworkReports));

        // Library Reports
        List<ReportItem> libraryReports = Arrays.asList(
            new ReportItem("book_issue_report", "book_issue_report", getString(R.string.book_issue_report), "library", R.drawable.ic_fa_book),
            new ReportItem("book_due_report", "book_due_report", getString(R.string.book_due_report), "library", R.drawable.ic_fa_calendar),
            new ReportItem("book_inventory_report", "book_inventory_report", getString(R.string.book_inventory_report), "library", R.drawable.ic_fa_list_alt),
            new ReportItem("book_issue_return_report", "book_issue_return_report", getString(R.string.book_issue_return_report), "library", R.drawable.ic_fa_check_circle)
        );
        categories.add(new ReportCategory("library", "library", getString(R.string.library_reports), R.drawable.ic_fa_book, libraryReports));

        // Inventory Reports
        List<ReportItem> inventoryReports = Arrays.asList(
            new ReportItem("stock_report", "stock_report", getString(R.string.stock_report), "inventory", R.drawable.ic_fa_archive),
            new ReportItem("add_item_report", "add_item_report", getString(R.string.add_item_report), "inventory", R.drawable.ic_fa_plus),
            new ReportItem("issue_item_report", "issue_item_report", getString(R.string.issue_item_report), "inventory", R.drawable.ic_fa_file_text)
        );
        categories.add(new ReportCategory("inventory", "inventory", getString(R.string.inventory_reports), R.drawable.ic_fa_archive, inventoryReports));

        // Transport Reports
        List<ReportItem> transportReports = new ArrayList<>();
        categories.add(new ReportCategory("transport", "transport", getString(R.string.transport_reports), R.drawable.ic_fa_bus, transportReports));

        // Hostel Reports
        List<ReportItem> hostelReports = new ArrayList<>();
        categories.add(new ReportCategory("hostel", "hostel", getString(R.string.hostel_reports), R.drawable.ic_fa_home, hostelReports));

        // Alumni Reports
        List<ReportItem> alumniReports = new ArrayList<>();
        categories.add(new ReportCategory("alumni", "alumni", getString(R.string.alumni_reports), R.drawable.ic_fa_graduation_cap, alumniReports));

        // User Log Reports
        List<ReportItem> userLogReports = new ArrayList<>();
        categories.add(new ReportCategory("user_log", "user_log", getString(R.string.user_log_reports), R.drawable.ic_fa_list_alt, userLogReports));

        // Audit Trail Reports
        List<ReportItem> auditTrailReports = new ArrayList<>();
        categories.add(new ReportCategory("audit_trail", "audit_trail", getString(R.string.audit_trail_reports), R.drawable.ic_fa_search, auditTrailReports));

        adapter = new ReportCategoryAdapter(this, categories);
        reportsRecyclerView.setAdapter(adapter);
    }
}
