package com.qdocs.ssre241123.teachers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

/**
 * Generic WebView activity that loads web submenu pages.
 * All submenu items that don't have a native Android implementation
 * are routed here to load the web version.
 */
public class TeacherSubmenuDetailActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private ProgressBar loadingProgress;
    private TextView toolbarTitle;
    private View emptyState;
    private TextView emptyTitle;
    private TextView emptyMessage;
    private Button btnOpenWeb;
    private ImageView btnBack;

    private String submenuUrl;
    private String submenuName;
    private String parentMenuName;
    private String fullWebUrl;

    // These submenu names open in WebView (have web pages that work well on mobile)
    private static final String[] WEBVIEW_SUBMENUS = {
        // Student Information
        "student_admission", "online_admission", "disable_student",
        "student_details", "multi_class_student", "bulk_delete",
        "student_categories", "student_house", "disable_reason",
        // Fees Collection
        "collect_fees", "search_fees_payment", "search_due_fees",
        "fees_master", "fees_group", "fees_type", "fees_discount",
        "fees_carry_forward", "fees_reminder", "offline_bank_payments",
        "feediscountapproval",
        // Income
        "add_income", "search_income", "income_head",
        // Expense
        "add_expense", "search_expense", "expense_head",
        // Attendance
        "attendance_by_date", "student_attendance", "approve_leave",
        // Examinations
        "exam_group", "exam_schedule", "exam_result",
        "design_admit_card", "print_admit_card",
        "design_marksheet", "print_marksheet", "marks_grade", "marks_division",
        // Online Examinations
        "online_exam", "question_bank",
        // Lesson Plan
        "manage_lesson_plan", "manage_syllabus_status",
        "lesson", "topic",
        // Academics
        "class_timetable", "teachers_timetable",
        "assign_class_teacher", "promote_students",
        "subject_group", "subjects", "class", "sections",
        // Human Resource
        "staff_directory", "staff_attendance", "payroll",
        "approve_leave_request", "apply_leave",
        "leave_type", "teachers_rating",
        "department", "designation", "disabled_staff",
        // Communicate
        "notice_board", "send_email", "send_sms",
        "email_sms_log", "schedule_email_sms_log",
        "login_credentials_send", "email_template", "sms_template",
        // Download Center
        "content_type", "content_share_list", "upload_content", "video_tutorial",
        // Homework
        "add_homework", "daily_assignment",
        // Library
        "book_list", "issue_return", "add_student", "add_staff_member",
        // Inventory
        "issue_item", "add_item_stock", "add_item",
        "item_category", "item_store", "item_supplier",
        // Transport
        "fees_master_transport", "pickup_point", "routes",
        "vehicles", "assign_vehicle", "route_pickup_point",
        "student_transport_fees",
        // Hostel
        "hostel_rooms", "room_type", "hostel",
        "hostel_fees_master", "assign_hostel_fees",
        // Certificate
        "student_certificate", "generate_certificate",
        "student_id_card", "generate_id_card",
        "staff_id_card", "generate_staff_id_card",
        // Front CMS
        "event", "gallery", "news", "media_manager",
        "pages", "menus", "banner_images",
        // Alumni
        "manage_alumini", "events",
        // CBSE Examination
        "exam_cbse", "exam_schedule_cbse", "print_marksheet_cbse",
        "exam_grade_cbse", "assign_observation", "observation",
        "observation_parameter", "assessment", "term", "template",
        "reports_cbse", "setting_cbse",
        // Results
        "internal_results", "external_results",
        "adding_internal_results", "adding_external_results",
        "internal_result_subject_group", "external_result_subject_group",
        "internal_examtype", "external_examtype",
        "subjects_results", "internal_result_bulk_import",
        "external_result_bulk_import",
        // TC Generation
        "tc_generation", "tc_creation",
        // Importing
        "student_import", "student_fee_import",
        // Live Classes
        "live_classes", "live_class_report", "live_meeting", "live_meeting_report",
        // Other Fees
        "other_fees_search", "other_fees_master", "other_fees_group", "other_fees_type",
        // Accounting
        "accounttransactionreport", "accountreport", "accounttransaction",
        "addaccount", "accountcategorygroup", "accountcategory", "accounttype",
        // HallTicket Generation
        "halltickectgeneration", "hallticketcreation",
        "hallsubjectgroupcombo", "hallsubjectgroup", "hallsubject",
        // Face Attendance
        "face_attendance_student_registration", "face_attendance_mark_attendance",
        // Behaviour Records
        "assign_incident", "incidents", "reports_behaviour", "setting_behaviour",
        // Multi Branch
        "multibranch_overview", "multibranch_report", "multibranch_setting",
        // Fee Discount
        "feediscountapproval",
        // Referral
        "referral",
        // Admission No / HallTicket No
        "add_admission_no", "admission_bulk_import", "search_admission",
        "add_hallticket_no", "hallticket_bulk_import",
        // Generate Paper
        "generate_paper",
        // Student Resume
        "build_cv", "setting_resume", "download_cv",
        // System Settings
        "general_setting", "session_setting", "notification_setting",
        "sms_setting", "email_setting", "payment_methods",
        "print_headerfooter", "front_cms_setting",
        "roles_permissions", "backup_restore", "users",
        "languages", "modules", "custom_fields",
        "captcha_setting", "system_fields",
        "student_profile_update", "online_admission_setting",
        "file_types", "system_update", "sidebar_menu",
        "currency", "time_range_assignments", "whatsapp_messaging",
        // Copy Old Lessons
        "copy_old_lessons",
        // Front Office
        "admission_enquiry", "visitor_book", "phone_call_log",
        "postal_dispatch", "postal_receive", "complain", "setup_front_office",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_submenu_detail);

        toolbarTitle = findViewById(R.id.toolbar_title);
        btnBack = findViewById(R.id.btn_back);
        progressBar = findViewById(R.id.progress_bar);
        loadingProgress = findViewById(R.id.loading_progress);
        webView = findViewById(R.id.webview);
        emptyState = findViewById(R.id.empty_state);
        emptyTitle = findViewById(R.id.empty_title);
        emptyMessage = findViewById(R.id.empty_message);
        btnOpenWeb = findViewById(R.id.btn_open_web);

        // Get intent data
        submenuName = getIntent().getStringExtra("submenu_name");
        submenuUrl = getIntent().getStringExtra("submenu_url");
        parentMenuName = getIntent().getStringExtra("parent_menu_name");
        String submenuId = getIntent().getStringExtra("submenu_id");

        if (submenuName != null) {
            toolbarTitle.setText(submenuName);
        }

        btnBack.setOnClickListener(v -> onBackPressed());

        // Apply theme color to toolbar
        String primaryColor = Utility.getSharedPreferences(this, "primaryColour");
        if (primaryColor != null && !primaryColor.isEmpty()) {
            try {
                findViewById(R.id.toolbar).setBackgroundColor(android.graphics.Color.parseColor(primaryColor));
            } catch (Exception ignored) {}
        }

        // Build the web URL
        String domain = Constants.domain;
        // Remove trailing slash
        if (domain.endsWith("/")) {
            domain = domain.substring(0, domain.length() - 1);
        }

        if (submenuUrl != null && !submenuUrl.isEmpty()) {
            fullWebUrl = domain + "/" + submenuUrl;
        } else {
            fullWebUrl = null;
        }

        // Decide how to handle this submenu
        String normalizedName = normalizeName(submenuName);
        boolean shouldOpenInWebView = shouldOpenInWebView(normalizedName);

        if (shouldOpenInWebView && fullWebUrl != null) {
            setupAndLoadWebView(fullWebUrl);
        } else {
            showEmptyState(submenuName, fullWebUrl);
        }
    }

    private boolean shouldOpenInWebView(String normalizedName) {
        if (normalizedName == null) return false;
        for (String item : WEBVIEW_SUBMENUS) {
            if (item.equalsIgnoreCase(normalizedName)) return true;
        }
        // Default: open in WebView if URL exists
        return fullWebUrl != null;
    }

    private String normalizeName(String name) {
        if (name == null) return null;
        return name.toLowerCase()
                   .replace(" ", "_")
                   .replace("-", "_")
                   .replace(".", "")
                   .trim();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupAndLoadWebView(String url) {
        loadingProgress.setVisibility(View.VISIBLE);
        webView.setVisibility(View.VISIBLE);
        emptyState.setVisibility(View.GONE);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setUserAgentString(
            webView.getSettings().getUserAgentString() + " SchoolApp/Android"
        );

        // Enable cookie management for session persistence
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(webView, true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                loadingProgress.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                // Keep navigation within the same domain
                String domain = Constants.domain;
                if (url.startsWith(domain) || url.startsWith("https://") || url.startsWith("http://")) {
                    // If it's a login page, let it load
                    if (url.contains("/login") || url.contains("/auth")) {
                        view.loadUrl(url);
                        return true;
                    }
                    return false;
                }
                // Open external links in browser
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        webView.loadUrl(url);
    }

    private void showEmptyState(String name, String url) {
        webView.setVisibility(View.GONE);
        loadingProgress.setVisibility(View.GONE);
        emptyState.setVisibility(View.VISIBLE);

        if (name != null) {
            emptyTitle.setText(name);
        }
        emptyMessage.setText("This feature is available on the web portal");

        if (url != null) {
            btnOpenWeb.setVisibility(View.VISIBLE);
            btnOpenWeb.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
