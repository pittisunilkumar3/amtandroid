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
import com.qdocs.ssre241123.adapters.SubmenuItemAdapter;
import com.qdocs.ssre241123.model.MenuResponse;
import com.qdocs.ssre241123.model.MenuItem;
import com.qdocs.ssre241123.model.MenuSubmenuItem;
import com.qdocs.ssre241123.model.SubMenuItem;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherSubmenuActivity extends BaseActivity {

    private static final String TAG = "TeacherSubmenuActivity";

    // Static cache for menu data (shared across instances)
    private static List<MenuItem> cachedMenuItems = null;

    private RecyclerView submenuRecyclerView;
    private SubmenuItemAdapter adapter;
    private ImageView backButton;
    private TextView titleTextView;
    private TextView submenuTitleTextView;
    private ProgressBar progressBar;
    private TextView errorTextView;

    private String menuId;
    private String menuName;
    private String activateMenu;

    // Method to cache menu data from dashboard
    public static void cacheMenuData(List<MenuItem> menuItems) {
        cachedMenuItems = menuItems;
        Log.d("TeacherSubmenuActivity", "Cached " + (menuItems != null ? menuItems.size() : 0) + " menu items");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_submenu);

        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), "langCode"));

        getIntentData();
        initializeViews();
        setupRecyclerView();
        loadSubmenuFromAPI();
    }

    private void getIntentData() {
        menuId = getIntent().getStringExtra("menu_id");
        menuName = getIntent().getStringExtra("menu_name");
        activateMenu = getIntent().getStringExtra("activate_menu");
        
        Log.d(TAG, "Menu ID: " + menuId + ", Name: " + menuName + ", Activate: " + activateMenu);
    }

    private void initializeViews() {
        submenuRecyclerView = findViewById(R.id.submenu_recyclerView);
        backButton = findViewById(R.id.back_button);
        titleTextView = findViewById(R.id.title);
        submenuTitleTextView = findViewById(R.id.submenu_title);
        progressBar = findViewById(R.id.progressBar);
        errorTextView = findViewById(R.id.error_text);

        // Set title
        if (menuName != null) {
            titleTextView.setText(menuName);
            submenuTitleTextView.setText(menuName);
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
        submenuRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
    }

    private void showLoading() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        if (submenuRecyclerView != null) {
            submenuRecyclerView.setVisibility(View.GONE);
        }
        if (errorTextView != null) {
            errorTextView.setVisibility(View.GONE);
        }
    }

    private void showContent() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        if (submenuRecyclerView != null) {
            submenuRecyclerView.setVisibility(View.VISIBLE);
        }
        if (errorTextView != null) {
            errorTextView.setVisibility(View.GONE);
        }
    }

    private void showError(String message) {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        if (submenuRecyclerView != null) {
            submenuRecyclerView.setVisibility(View.GONE);
        }
        if (errorTextView != null) {
            errorTextView.setVisibility(View.VISIBLE);
            errorTextView.setText(message);
        }
    }

    private void loadSubmenuFromAPI() {
        showLoading();

        Log.d(TAG, "Loading submenus for menu: " + menuName + " (ID: " + menuId + ")");

        // First, try to use cached data
        if (cachedMenuItems != null && !cachedMenuItems.isEmpty()) {
            Log.d(TAG, "Using cached menu data (" + cachedMenuItems.size() + " items)");
            processMenuData(cachedMenuItems);
            return;
        }

        // If no cache, try API call
        Log.d(TAG, "No cached data, attempting API call");

        String staffId = Utility.getSharedPreferences(getApplicationContext(), "staffId");
        if (staffId == null || staffId.isEmpty()) {
            staffId = "1"; // Default for testing
        }

        String url = Utility.getSharedPreferences(getApplicationContext(), Constants.apiUrl) + "/api/teacher/menu";

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("staff_id", Integer.parseInt(staffId));
        } catch (Exception e) {
            Log.e(TAG, "Error creating request body", e);
        }

        final String requestBodyString = requestBody.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "API Response received");
                        try {
                            Gson gson = new Gson();
                            MenuResponse menuResponse = gson.fromJson(response, MenuResponse.class);

                            if (menuResponse != null && menuResponse.getStatus() == 1 && menuResponse.getData() != null) {
                                List<MenuItem> menus = menuResponse.getData().getMenus();
                                Log.d(TAG, "Total menus in response: " + (menus != null ? menus.size() : 0));

                                if (menus != null && !menus.isEmpty()) {
                                    // Cache the data for future use
                                    cachedMenuItems = menus;
                                    processMenuData(menus);
                                } else {
                                    Log.e(TAG, "No menu items in response");
                                    showError("Unable to load menu items");
                                }
                            } else {
                                String errorMsg = menuResponse != null ? menuResponse.getMessage() : "Unknown error";
                                Log.e(TAG, "Error: " + errorMsg);
                                showError("Error loading menu items");
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing response", e);
                            showError("Error loading menu items");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "API Error", error);
                        showError("Network error. Please try again.");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBodyString == null ? null : requestBodyString.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBodyString, "utf-8");
                    return null;
                }
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void processMenuData(List<MenuItem> menus) {
        // Find the menu with matching ID or activate_menu
        MenuItem targetMenu = null;
        for (MenuItem menu : menus) {
            Log.d(TAG, "Checking menu: " + menu.getMenu() + " (ID: " + menu.getId() + ", Activate: " + menu.getActivateMenu() + ")");
            if ((menuId != null && menuId.equals(menu.getId())) ||
                (activateMenu != null && activateMenu.equals(menu.getActivateMenu()))) {
                targetMenu = menu;
                Log.d(TAG, "Found matching menu: " + menu.getMenu());
                break;
            }
        }

        if (targetMenu != null) {
            Log.d(TAG, "Target menu found: " + targetMenu.getMenu());
            Log.d(TAG, "Submenus count: " + (targetMenu.getSubmenus() != null ? targetMenu.getSubmenus().size() : 0));

            if (targetMenu.getSubmenus() != null && !targetMenu.getSubmenus().isEmpty()) {
                List<MenuSubmenuItem> submenuItems = parseSubmenuItems(targetMenu.getSubmenus());
                displaySubmenuItems(submenuItems);
                showContent();
            } else {
                Log.w(TAG, "No submenus found for menu: " + menuName);
                showError("No items available");
            }
        } else {
            Log.w(TAG, "Target menu not found. Looking for ID: " + menuId + ", Activate: " + activateMenu);
            showError("Menu not found");
        }
    }

    private List<MenuSubmenuItem> parseSubmenuItems(List<SubMenuItem> submenus) {
        List<MenuSubmenuItem> submenuItems = new ArrayList<>();

        Log.d(TAG, "Parsing " + submenus.size() + " submenu items");

        for (SubMenuItem submenu : submenus) {
            String itemId = submenu.getId();
            String itemName = submenu.getMenu();
            String itemLangKey = submenu.getLangKey();
            String itemUrl = submenu.getUrl();

            // Get icon resource based on item name
            int iconResource = getIconForSubmenuItem(itemName);

            MenuSubmenuItem item = new MenuSubmenuItem(
                itemId,
                itemName,
                itemLangKey != null ? itemLangKey : itemName,
                itemUrl,
                iconResource,
                menuId
            );

            submenuItems.add(item);
            Log.d(TAG, "Added submenu item: " + itemName);
        }

        return submenuItems;
    }

    private int getIconForSubmenuItem(String itemName) {
        // Sophisticated 3-level icon mapping for maximum visual distinction
        String lowerName = itemName != null ? itemName.toLowerCase() : "";
        int iconResource;

        // ========== LEVEL 1: EXACT NAME MATCHES (Highest Priority) ==========
        switch (lowerName) {
            // Student Information
            case "student_details": iconResource = R.drawable.ic_fa_user; break;
            case "student_admission": iconResource = R.drawable.ic_fa_user_plus; break;
            case "online_admission": iconResource = R.drawable.ic_fa_globe; break;
            case "disable_student": iconResource = R.drawable.ic_fa_exclamation_triangle; break;
            case "multi_class_student": iconResource = R.drawable.ic_fa_sitemap; break;
            case "bulk_delete": iconResource = R.drawable.ic_delete; break;
            case "student_categories": iconResource = R.drawable.ic_fa_table; break;
            case "student_house": iconResource = R.drawable.ic_fa_home; break;
            case "disable_reason": iconResource = R.drawable.ic_info; break;

            // Fees Collection
            case "collect_fees": iconResource = R.drawable.ic_fa_money; break;
            case "offline_bank_payments": iconResource = R.drawable.ic_fa_credit_card; break;
            case "search_fees_payment": iconResource = R.drawable.ic_fa_search; break;
            case "search_due_fees": iconResource = R.drawable.ic_fa_calculator; break;
            case "fees_master": iconResource = R.drawable.ic_fa_dollar; break;
            case "fees_group": iconResource = R.drawable.ic_fa_users; break;
            case "fees_type": iconResource = R.drawable.ic_fa_list_alt; break;
            case "fees_discount": iconResource = R.drawable.ic_fa_percent; break;
            case "fees_carry_forward": iconResource = R.drawable.ic_fa_history; break;
            case "fees_reminder": iconResource = R.drawable.ic_notification; break;

            // Other Fees
            case "other_fees_search": iconResource = R.drawable.ic_fa_search; break;
            case "other_fees_master": iconResource = R.drawable.ic_fa_dollar; break;
            case "other_fees_group": iconResource = R.drawable.ic_fa_users; break;
            case "other_fees_type": iconResource = R.drawable.ic_fa_list_alt; break;

            // Front Office
            case "admission_enquiry": iconResource = R.drawable.ic_fa_address_book; break;
            case "visitor_book": iconResource = R.drawable.ic_visitors; break;
            case "phone_call_log": iconResource = R.drawable.ic_phone; break;
            case "postal_dispatch": iconResource = R.drawable.ic_fa_upload; break;
            case "postal_receive": iconResource = R.drawable.ic_download; break;
            case "complain": iconResource = R.drawable.ic_fa_exclamation_triangle; break;
            case "setup_front_office": iconResource = R.drawable.ic_fa_cogs; break;

            // Attendance
            case "student_attendance": iconResource = R.drawable.ic_fa_calendar_check; break;
            case "approve_leave": iconResource = R.drawable.ic_fa_check_circle; break;
            case "attendance_by_date": iconResource = R.drawable.ic_calender; break;
            case "staff_attendance": iconResource = R.drawable.ic_dashboard_attendance; break;

            // Examinations
            case "exam_group": iconResource = R.drawable.ic_fa_users; break;
            case "exam_schedule": iconResource = R.drawable.ic_calender_time; break;
            case "exam_result": iconResource = R.drawable.ic_fa_bar_chart; break;
            case "design_admit_card": iconResource = R.drawable.ic_edit; break;
            case "print_admit_card": iconResource = R.drawable.ic_fa_file_text; break;
            case "design_marksheet": iconResource = R.drawable.ic_edit; break;
            case "print_marksheet": iconResource = R.drawable.ic_document_pdf; break;
            case "marks_grade": iconResource = R.drawable.ic_fa_graduation_cap; break;
            case "marks_division": iconResource = R.drawable.ic_fa_pie_chart; break;

            // CBSE Examination
            case "exam": iconResource = R.drawable.ic_nav_examination; break;
            case "assign_observation": iconResource = R.drawable.ic_assignment; break;
            case "observation": iconResource = R.drawable.ic_view; break;
            case "observation_parameter": iconResource = R.drawable.ic_fa_tasks; break;
            case "assessment": iconResource = R.drawable.ic_fa_check; break;
            case "term": iconResource = R.drawable.ic_calender; break;
            case "template": iconResource = R.drawable.ic_description; break;

            // Online Examinations
            case "online_exam": iconResource = R.drawable.ic_onlineexam; break;
            case "question_bank": iconResource = R.drawable.ic_quiz; break;

            // Academics
            case "class_timetable": iconResource = R.drawable.ic_calender_time; break;
            case "teachers_timetable": iconResource = R.drawable.ic_teacher; break;
            case "assign_class_teacher": iconResource = R.drawable.ic_fa_user_plus; break;
            case "promote_students": iconResource = R.drawable.ic_fa_upload; break;
            case "subject_group": iconResource = R.drawable.ic_subject_type; break;
            case "subjects": iconResource = R.drawable.ic_subject; break;
            case "class": iconResource = R.drawable.ic_fa_building; break;
            case "sections": iconResource = R.drawable.ic_fa_sitemap; break;

            // Lesson Plan
            case "manage_lesson_plan": iconResource = R.drawable.ic_lessonplan; break;
            case "manage_syllabus_status": iconResource = R.drawable.ic_syllabus; break;
            case "lesson": iconResource = R.drawable.ic_open_book; break;
            case "topic": iconResource = R.drawable.ic_fa_rss; break;

            // Human Resource
            case "staff_directory": iconResource = R.drawable.ic_fa_address_book; break;
            case "payroll": iconResource = R.drawable.ic_briefcase; break;
            case "approve_leave_request": iconResource = R.drawable.ic_fa_check_circle; break;
            case "apply_leave": iconResource = R.drawable.ic_leave; break;
            case "leave_type": iconResource = R.drawable.ic_calender_cross; break;
            case "teachers_rating": iconResource = R.drawable.ic_fa_trophy; break;
            case "department": iconResource = R.drawable.ic_fa_building; break;
            case "designation": iconResource = R.drawable.ic_fa_id_card; break;
            case "disabled_staff": iconResource = R.drawable.ic_fa_exclamation_triangle; break;

            // Communicate
            case "notice_board": iconResource = R.drawable.ic_notice; break;
            case "send_email": iconResource = R.drawable.ic_email; break;
            case "send_sms": iconResource = R.drawable.ic_phone_filled; break;
            case "email_sms_log": iconResource = R.drawable.ic_fa_history; break;
            case "schedule_email_sms_log": iconResource = R.drawable.ic_calender_time; break;
            case "login_credentials_send": iconResource = R.drawable.ic_fa_key; break;
            case "email_template": iconResource = R.drawable.ic_email_filled; break;
            case "sms_template": iconResource = R.drawable.ic_phone; break;

            // Download Center
            case "content_type": iconResource = R.drawable.ic_fa_list_alt; break;
            case "content_share_list": iconResource = R.drawable.ic_fa_share_alt; break;
            case "upload_content": iconResource = R.drawable.ic_file_upload; break;
            case "video_tutorial": iconResource = R.drawable.ic_videocam; break;

            // Homework
            case "add_homework": iconResource = R.drawable.ic_fa_plus; break;
            case "daily_assignment": iconResource = R.drawable.ic_assignment; break;
            case "homework": iconResource = R.drawable.ic_dashboard_homework; break;

            // Library
            case "book_list": iconResource = R.drawable.ic_open_book; break;
            case "issue_return": iconResource = R.drawable.ic_fa_history; break;
            case "add_student": iconResource = R.drawable.ic_profile_plus; break;
            case "add_staff_member": iconResource = R.drawable.ic_fa_user_plus; break;

            // Inventory
            case "issue_item": iconResource = R.drawable.ic_fa_upload; break;
            case "add_item_stock": iconResource = R.drawable.ic_fa_plus; break;
            case "add_item": iconResource = R.drawable.ic_add_black; break;
            case "item_category": iconResource = R.drawable.ic_fa_table; break;
            case "item_store": iconResource = R.drawable.ic_fa_archive; break;
            case "item_supplier": iconResource = R.drawable.ic_publisher; break;

            // Transport
            case "pickup_point": iconResource = R.drawable.ic_location; break;
            case "routes": iconResource = R.drawable.ic_nav_transport; break;
            case "vehicles": iconResource = R.drawable.ic_bus; break;
            case "assign_vehicle": iconResource = R.drawable.ic_fa_plus; break;
            case "route_pickup_point": iconResource = R.drawable.ic_place_black; break;
            case "student_transport_fees": iconResource = R.drawable.ic_fa_money; break;

            // Hostel
            case "hostel_rooms": iconResource = R.drawable.ic_room; break;
            case "room_type": iconResource = R.drawable.ic_fa_list_alt; break;
            case "hostel": iconResource = R.drawable.ic_hostel; break;
            case "hostel_fees_master": iconResource = R.drawable.ic_fa_dollar; break;
            case "assign_hostel_fees": iconResource = R.drawable.ic_fa_money; break;

            default:
                // Continue to Level 2 if no exact match
                iconResource = getIconByMultiKeyword(lowerName);
                break;
        }

        Log.d(TAG, "Icon mapping: '" + itemName + "' -> " + getResourceName(iconResource));
        return iconResource;
    }

    private int getIconByMultiKeyword(String lowerName) {
        // ========== LEVEL 2: MULTI-KEYWORD COMBINATIONS ==========

        // Certificate related
        if (lowerName.contains("certificate") && lowerName.contains("student")) {
            return R.drawable.ic_documents_certificate;
        } else if (lowerName.contains("certificate") && lowerName.contains("generate")) {
            return R.drawable.ic_fa_certificate;
        } else if (lowerName.contains("id_card") && lowerName.contains("student")) {
            return R.drawable.ic_fa_id_card;
        } else if (lowerName.contains("id_card") && lowerName.contains("generate")) {
            return R.drawable.ic_qr_code;
        } else if (lowerName.contains("id_card") && lowerName.contains("staff")) {
            return R.drawable.ic_fa_id_card;
        }

        // Income/Expense
        else if (lowerName.contains("add") && lowerName.contains("income")) {
            return R.drawable.ic_fa_plus;
        } else if (lowerName.contains("search") && lowerName.contains("income")) {
            return R.drawable.ic_fa_search;
        } else if (lowerName.contains("income") && lowerName.contains("head")) {
            return R.drawable.ic_fa_list_alt;
        } else if (lowerName.contains("add") && lowerName.contains("expense")) {
            return R.drawable.ic_fa_plus;
        } else if (lowerName.contains("search") && lowerName.contains("expense")) {
            return R.drawable.ic_fa_search;
        } else if (lowerName.contains("expense") && lowerName.contains("head")) {
            return R.drawable.ic_fa_list_alt;
        }

        // Results
        else if (lowerName.contains("internal") && lowerName.contains("result")) {
            return R.drawable.ic_fa_bar_chart;
        } else if (lowerName.contains("external") && lowerName.contains("result")) {
            return R.drawable.ic_fa_pie_chart;
        } else if (lowerName.contains("adding") && lowerName.contains("result")) {
            return R.drawable.ic_fa_plus;
        } else if (lowerName.contains("result") && lowerName.contains("bulk")) {
            return R.drawable.ic_file_upload;
        } else if (lowerName.contains("result") && lowerName.contains("subject")) {
            return R.drawable.ic_subject;
        } else if (lowerName.contains("examtype")) {
            return R.drawable.ic_fa_list_alt;
        }

        // Continue to Level 3
        else {
            return getIconBySingleKeyword(lowerName);
        }
    }

    private int getIconBySingleKeyword(String lowerName) {
        // ========== LEVEL 3: SINGLE KEYWORD MATCHES (Lowest Priority) ==========

        // Accounting
        if (lowerName.contains("account") && lowerName.contains("transaction")) {
            return R.drawable.ic_fa_credit_card_alt;
        } else if (lowerName.contains("account") && lowerName.contains("report")) {
            return R.drawable.ic_fa_bar_chart;
        } else if (lowerName.contains("account") && lowerName.contains("category")) {
            return R.drawable.ic_fa_table;
        } else if (lowerName.contains("account") && lowerName.contains("type")) {
            return R.drawable.ic_fa_list_alt;
        } else if (lowerName.contains("addaccount")) {
            return R.drawable.ic_fa_plus;
        }

        // HallTicket
        else if (lowerName.contains("hallticket") || lowerName.contains("halltickect")) {
            return R.drawable.ic_fa_ticket;
        } else if (lowerName.contains("hallsubject")) {
            return R.drawable.ic_subject;
        }

        // Admission No
        else if (lowerName.contains("admission") && lowerName.contains("no")) {
            return R.drawable.ic_fa_id_card;
        } else if (lowerName.contains("admission") && lowerName.contains("bulk")) {
            return R.drawable.ic_file_upload;
        } else if (lowerName.contains("admission") && lowerName.contains("search")) {
            return R.drawable.ic_fa_search;
        }

        // Live Classes
        else if (lowerName.contains("live") && lowerName.contains("meeting")) {
            return R.drawable.ic_videocam;
        } else if (lowerName.contains("live") && lowerName.contains("class")) {
            return R.drawable.ic_onlinecourse;
        } else if (lowerName.contains("live") && lowerName.contains("report")) {
            return R.drawable.ic_fa_bar_chart;
        }

        // Behaviour Records
        else if (lowerName.contains("assign") && lowerName.contains("incident")) {
            return R.drawable.ic_fa_plus;
        } else if (lowerName.contains("incidents")) {
            return R.drawable.ic_fa_exclamation_triangle;
        }

        // TC Generation
        else if (lowerName.contains("tc") && lowerName.contains("generation")) {
            return R.drawable.ic_fa_certificate;
        } else if (lowerName.contains("tc") && lowerName.contains("creation")) {
            return R.drawable.ic_edit;
        }

        // Referral
        else if (lowerName.contains("referral")) {
            return R.drawable.ic_fa_share_alt;
        }

        // Fee Discount
        else if (lowerName.contains("feediscount")) {
            return R.drawable.ic_fa_percent;
        }

        // Alumni
        else if (lowerName.contains("alumni") || lowerName.contains("alumini")) {
            return R.drawable.ic_fa_graduation_cap;
        } else if (lowerName.contains("manage_alumini")) {
            return R.drawable.ic_fa_users;
        } else if (lowerName.contains("events")) {
            return R.drawable.ic_events;
        }

        // Front CMS
        else if (lowerName.contains("event")) {
            return R.drawable.ic_events;
        } else if (lowerName.contains("gallery")) {
            return R.drawable.ic_photo_library;
        } else if (lowerName.contains("news")) {
            return R.drawable.ic_fa_rss;
        } else if (lowerName.contains("media_manager")) {
            return R.drawable.ic_photo_camera;
        } else if (lowerName.contains("pages")) {
            return R.drawable.ic_description;
        } else if (lowerName.contains("menus")) {
            return R.drawable.ic_fa_list_alt;
        } else if (lowerName.contains("banner")) {
            return R.drawable.ic_photo_library;
        }

        // System Settings
        else if (lowerName.contains("general_setting")) {
            return R.drawable.ic_fa_cogs;
        } else if (lowerName.contains("session_setting")) {
            return R.drawable.ic_calender;
        } else if (lowerName.contains("notification_setting")) {
            return R.drawable.ic_notification;
        } else if (lowerName.contains("sms_setting")) {
            return R.drawable.ic_phone;
        } else if (lowerName.contains("email_setting")) {
            return R.drawable.ic_email;
        } else if (lowerName.contains("payment_methods")) {
            return R.drawable.ic_fa_credit_card;
        } else if (lowerName.contains("print_headerfooter")) {
            return R.drawable.ic_document_pdf;
        } else if (lowerName.contains("front_cms_setting")) {
            return R.drawable.ic_fa_desktop;
        } else if (lowerName.contains("roles_permissions")) {
            return R.drawable.ic_fa_key;
        } else if (lowerName.contains("backup_restore")) {
            return R.drawable.ic_fa_history;
        } else if (lowerName.contains("languages")) {
            return R.drawable.ic_fa_globe;
        } else if (lowerName.contains("currency")) {
            return R.drawable.ic_fa_dollar;
        } else if (lowerName.contains("users")) {
            return R.drawable.ic_user;
        } else if (lowerName.contains("modules")) {
            return R.drawable.ic_fa_sitemap;
        } else if (lowerName.contains("custom_fields")) {
            return R.drawable.ic_edit;
        } else if (lowerName.contains("captcha")) {
            return R.drawable.ic_fa_fingerprint;
        } else if (lowerName.contains("system_fields")) {
            return R.drawable.ic_fa_table;
        } else if (lowerName.contains("student_profile_update")) {
            return R.drawable.ic_profile_plus;
        } else if (lowerName.contains("file_types")) {
            return R.drawable.ic_file;
        } else if (lowerName.contains("sidebar_menu")) {
            return R.drawable.ic_fa_list_alt;
        } else if (lowerName.contains("system_update")) {
            return R.drawable.ic_fa_upload;
        }

        // Importing
        else if (lowerName.contains("import") && lowerName.contains("student")) {
            return R.drawable.ic_file_upload;
        } else if (lowerName.contains("import") && lowerName.contains("fee")) {
            return R.drawable.ic_file_upload;
        }

        // Reports (generic)
        else if (lowerName.contains("report")) {
            return R.drawable.ic_fa_bar_chart;
        }

        // Multi Branch
        else if (lowerName.contains("overview")) {
            return R.drawable.ic_fa_pie_chart;
        }

        // User Log
        else if (lowerName.contains("user_log")) {
            return R.drawable.ic_fa_history;
        } else if (lowerName.contains("audit_trail")) {
            return R.drawable.ic_fa_history;
        }

        // Copy Old Lessons
        else if (lowerName.contains("copy") && lowerName.contains("lesson")) {
            return R.drawable.ic_fa_share_alt;
        }

        // Generic keywords (fallback)
        else if (lowerName.contains("setting")) {
            return R.drawable.ic_fa_cogs;
        } else if (lowerName.contains("search")) {
            return R.drawable.ic_fa_search;
        } else if (lowerName.contains("add")) {
            return R.drawable.ic_fa_plus;
        } else if (lowerName.contains("delete")) {
            return R.drawable.ic_delete;
        } else if (lowerName.contains("edit")) {
            return R.drawable.ic_edit;
        } else if (lowerName.contains("view")) {
            return R.drawable.ic_view;
        } else if (lowerName.contains("print")) {
            return R.drawable.ic_document_pdf;
        } else if (lowerName.contains("upload")) {
            return R.drawable.ic_file_upload;
        } else if (lowerName.contains("download")) {
            return R.drawable.ic_download;
        } else if (lowerName.contains("generate")) {
            return R.drawable.ic_fa_plus;
        } else if (lowerName.contains("assign")) {
            return R.drawable.ic_fa_plus;
        } else if (lowerName.contains("manage")) {
            return R.drawable.ic_fa_cogs;
        } else if (lowerName.contains("approve")) {
            return R.drawable.ic_fa_check_circle;
        } else if (lowerName.contains("finance")) {
            return R.drawable.ic_fa_money;
        } else if (lowerName.contains("library")) {
            return R.drawable.ic_library;
        } else if (lowerName.contains("inventory")) {
            return R.drawable.ic_fa_archive;
        } else if (lowerName.contains("transport")) {
            return R.drawable.ic_fa_bus;
        } else if (lowerName.contains("hostel")) {
            return R.drawable.ic_hostel;
        } else if (lowerName.contains("lesson")) {
            return R.drawable.ic_lessonplan;
        } else if (lowerName.contains("homework")) {
            return R.drawable.ic_dashboard_homework;
        } else if (lowerName.contains("attendance")) {
            return R.drawable.ic_dashboard_attendance;
        } else if (lowerName.contains("examination")) {
            return R.drawable.ic_nav_examination;
        } else if (lowerName.contains("human_resource")) {
            return R.drawable.ic_briefcase;
        } else if (lowerName.contains("student_information")) {
            return R.drawable.ic_fa_user;
        }

        // Default icon
        else {
            return R.drawable.ic_fa_list_alt;
        }
    }

    private String getResourceName(int resourceId) {
        try {
            return getResources().getResourceEntryName(resourceId);
        } catch (Exception e) {
            return "unknown";
        }
    }

    private void displaySubmenuItems(List<MenuSubmenuItem> submenuItems) {
        Log.d(TAG, "Displaying " + submenuItems.size() + " submenu items");
        adapter = new SubmenuItemAdapter(this, submenuItems);
        submenuRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Log.d(TAG, "Adapter set and notified");
    }
}

