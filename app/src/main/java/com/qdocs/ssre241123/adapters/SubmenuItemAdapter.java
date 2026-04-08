package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.content.Intent;

import java.util.List;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.model.MenuSubmenuItem;
import com.qdocs.ssre241123.teachers.*;
import com.qdocs.ssre241123.utils.Constants;

import java.util.HashMap;
import java.util.Map;
import com.qdocs.ssre241123.model.MenuSubmenuItem;
import com.qdocs.ssre241123.teachers.*;
import com.qdocs.ssre241123.utils.Constants;

/**
 * Routes ALL submenu items to native API-based activities.
 * NO WebView usage - every screen calls an API endpoint.
 */
public class SubmenuItemAdapter extends RecyclerView.Adapter<SubmenuItemAdapter.SubmenuItemViewHolder> {

    private Context context;
    private List<MenuSubmenuItem> submenuItems;

    // Maps submenu name -> API list endpoint (for GenericListActivity)
    private static final Map<String, String> LIST_ENDPOINTS = new HashMap<>();
    // Maps submenu name -> {listUrl, nameField} for list-only screens
    private static final Map<String, String[]> LIST_CONFIGS = new HashMap<>();

    static {
        // ============ STUDENT INFORMATION ============
        LIST_CONFIGS.put("student_admission", new String[]{"", ""}); // complex form
        LIST_ENDPOINTS.put("online_admission", Constants.onlineAdmissionListUrl);
        LIST_ENDPOINTS.put("disable_student", Constants.staffDirectoryListUrl);
        LIST_ENDPOINTS.put("multi_class_student", Constants.staffDirectoryListUrl);
        LIST_ENDPOINTS.put("bulk_delete", Constants.staffDirectoryListUrl);

        // ============ FEES COLLECTION ============
        LIST_ENDPOINTS.put("collect_fees", Constants.feeCollectionFiltersGetUrl);
        LIST_ENDPOINTS.put("search_fees_payment", Constants.feeCollectionFiltersGetUrl);
        LIST_ENDPOINTS.put("search_due_fees", Constants.dueFeeReportFilterUrl);
        LIST_ENDPOINTS.put("fees_carry_forward", Constants.feeGroupListUrl);
        LIST_ENDPOINTS.put("fees_reminder", Constants.feeGroupListUrl);
        LIST_ENDPOINTS.put("offline_bank_payments", Constants.feeGroupListUrl);

        // ============ INCOME ============
        LIST_ENDPOINTS.put("add_income", Constants.incomeListUrl);
        LIST_ENDPOINTS.put("search_income", Constants.incomeSearchUrl);

        // ============ EXPENSE ============
        LIST_ENDPOINTS.put("add_expense", Constants.expenseHeadListUrl);
        LIST_ENDPOINTS.put("search_expense", Constants.expenseReportFilterUrl);

        // ============ ATTENDANCE ============
        LIST_ENDPOINTS.put("attendance_by_date", Constants.dailyAttendanceReportFilterUrl);
        LIST_ENDPOINTS.put("student_attendance", Constants.dailyAttendanceReportFilterUrl);
        LIST_ENDPOINTS.put("approve_leave", Constants.leaveAdminRequestsUrl);

        // ============ ACADEMICS ============
        LIST_ENDPOINTS.put("class_timetable", Constants.lessonAdminSyllabusUrl);
        LIST_ENDPOINTS.put("teachers_timetable", Constants.lessonAdminSyllabusUrl);
        LIST_ENDPOINTS.put("assign_class_teacher", Constants.staffDirectoryListUrl);
        LIST_ENDPOINTS.put("promote_students", Constants.staffDirectoryListUrl);

        // ============ HUMAN RESOURCE ============
        LIST_ENDPOINTS.put("staff_directory", Constants.staffDirectoryListUrl);
        LIST_ENDPOINTS.put("staff_attendance", Constants.staffAttendanceReportFilterUrl);
        LIST_ENDPOINTS.put("payroll", Constants.payrollAdminListUrl);
        LIST_ENDPOINTS.put("approve_leave_request", Constants.leaveAdminRequestsUrl);
        LIST_ENDPOINTS.put("apply_leave", Constants.leaveAdminRequestsUrl);
        LIST_ENDPOINTS.put("teachers_rating", Constants.staffDirectoryListUrl);
        LIST_ENDPOINTS.put("disabled_staff", Constants.staffDirectoryListUrl);

        // ============ COMMUNICATE ============
        LIST_ENDPOINTS.put("notice_board", Constants.notificationAdminListUrl);
        LIST_ENDPOINTS.put("send_email", Constants.mailsmsAdminListUrl);
        LIST_ENDPOINTS.put("send_sms", Constants.mailsmsAdminListUrl);
        LIST_ENDPOINTS.put("email_sms_log", Constants.mailsmsAdminListUrl);
        LIST_ENDPOINTS.put("schedule_email_sms_log", Constants.mailsmsAdminListUrl);
        LIST_ENDPOINTS.put("login_credentials_send", Constants.staffDirectoryListUrl);

        // ============ DOWNLOAD CENTER ============
        LIST_ENDPOINTS.put("content_type", Constants.incomeHeadListUrl);
        LIST_ENDPOINTS.put("content_share_list", Constants.libraryAdminBooksUrl);
        LIST_ENDPOINTS.put("upload_content", Constants.libraryAdminBooksUrl);
        LIST_ENDPOINTS.put("video_tutorial", Constants.getVideoTutorialUrl);

        // ============ HOMEWORK ============
        LIST_ENDPOINTS.put("add_homework", Constants.homeworkAdminListUrl);
        LIST_ENDPOINTS.put("daily_assignment", Constants.homeworkAdminDailyUrl);

        // ============ LIBRARY ============
        LIST_ENDPOINTS.put("book_list", Constants.libraryAdminBooksUrl);
        LIST_ENDPOINTS.put("issue_return", Constants.libraryAdminMembersUrl);
        LIST_ENDPOINTS.put("add_student", Constants.libraryAdminMembersUrl);
        LIST_ENDPOINTS.put("add_staff_member", Constants.libraryAdminMembersUrl);

        // ============ INVENTORY ============
        LIST_ENDPOINTS.put("issue_item", Constants.addItemReportFilterUrl);
        LIST_ENDPOINTS.put("add_item_stock", Constants.inventoryStockReportFilterUrl);
        LIST_ENDPOINTS.put("assign_vehicle", Constants.transportAdminVehicleRoutesUrl);
        LIST_ENDPOINTS.put("route_pickup_point", Constants.transportAdminPickupPointsUrl);
        LIST_ENDPOINTS.put("student_transport_fees", Constants.transportAdminRoutesUrl);

        // ============ HOSTEL ============
        LIST_ENDPOINTS.put("hostel_fees_master", Constants.hostelAdminHostelsUrl);
        LIST_ENDPOINTS.put("assign_hostel_fees", Constants.hostelAdminHostelsUrl);

        // ============ CERTIFICATE ============
        LIST_ENDPOINTS.put("generate_certificate", Constants.departmentListUrl);
        LIST_ENDPOINTS.put("generate_id_card", Constants.departmentListUrl);
        LIST_ENDPOINTS.put("generate_staff_id_card", Constants.departmentListUrl);

        // ============ FRONT CMS ============
        LIST_ENDPOINTS.put("gallery", Constants.cmsAdminGalleryUrl);
        LIST_ENDPOINTS.put("media_manager", Constants.cmsAdminMediaUrl);
        LIST_ENDPOINTS.put("banner_images", Constants.cmsAdminBannersUrl);

        // ============ BEHAVIOUR ============
        LIST_ENDPOINTS.put("incidents", Constants.studentbehaviourListUrl);
        LIST_ENDPOINTS.put("reports", Constants.studentbehaviourListUrl);
        LIST_ENDPOINTS.put("setting", Constants.behaviourSettingGetUrl);

        // ============ RESULTS ============
        LIST_ENDPOINTS.put("internal_results", Constants.internalResultReportFilterUrl);
        LIST_ENDPOINTS.put("external_results", Constants.externalResultReportFilterUrl);
        LIST_ENDPOINTS.put("adding_internal_results", Constants.internalResultReportFilterUrl);
        LIST_ENDPOINTS.put("adding_external_results", Constants.externalResultReportFilterUrl);
        LIST_ENDPOINTS.put("internal_result_bulk_import", Constants.internalResultReportFilterUrl);
        LIST_ENDPOINTS.put("external_result_bulk_import", Constants.externalResultReportFilterUrl);

        // ============ CBSE EXAMINATION ============
        LIST_ENDPOINTS.put("exam", Constants.examAdminExamGroupsUrl);
        LIST_ENDPOINTS.put("exam_schedule", Constants.examAdminScheduleUrl);
        LIST_ENDPOINTS.put("print_marksheet", Constants.internalResultReportFilterUrl);
        LIST_ENDPOINTS.put("assign_observation", Constants.internalResultReportFilterUrl);
        LIST_ENDPOINTS.put("assessment", Constants.internalResultReportFilterUrl);
        LIST_ENDPOINTS.put("reports", Constants.internalResultReportFilterUrl);
        LIST_ENDPOINTS.put("setting", Constants.settingsAdminGeneralUrl);

        // ============ EXAMINATIONS ============
        LIST_ENDPOINTS.put("exam_result", Constants.rankReportFilterUrl);
        LIST_ENDPOINTS.put("design_admit_card", Constants.examAdminScheduleUrl);
        LIST_ENDPOINTS.put("print_admit_card", Constants.examAdminScheduleUrl);
        LIST_ENDPOINTS.put("design_marksheet", Constants.examAdminScheduleUrl);
        LIST_ENDPOINTS.put("print_marksheet", Constants.internalResultReportFilterUrl);

        // ============ ONLINE EXAMINATIONS ============
        LIST_ENDPOINTS.put("online_exam", Constants.onlineExamsReportFilterUrl);
        LIST_ENDPOINTS.put("question_bank", Constants.onlineExamsReportFilterUrl);

        // ============ LESSON PLAN ============
        LIST_ENDPOINTS.put("manage_lesson_plan", Constants.lessonAdminSyllabusUrl);
        LIST_ENDPOINTS.put("manage_syllabus_status", Constants.lessonAdminSyllabusUrl);
        LIST_ENDPOINTS.put("Copy Old Lessons", Constants.lessonAdminLessonsUrl);

        // ============ MULTI BRANCH ============
        LIST_ENDPOINTS.put("overview", Constants.settingsAdminGeneralUrl);
        LIST_ENDPOINTS.put("report", Constants.collectionReportFilterUrl);
        LIST_ENDPOINTS.put("setting", Constants.settingsAdminGeneralUrl);

        // ============ OTHERS ============
        LIST_ENDPOINTS.put("feediscountapproval", Constants.feeDiscountApprovalListUrl);
        LIST_ENDPOINTS.put("Referral", Constants.studentReferralListUrl);

        // ============ TC GENERATION ============
        LIST_ENDPOINTS.put("TC Generation", Constants.tcGenerationListUrl);
        LIST_ENDPOINTS.put("TC Creation", Constants.tcGenerationListUrl);

        // ============ IMPORTING ============
        LIST_ENDPOINTS.put("Student Import", Constants.onlineAdmissionListUrl);
        LIST_ENDPOINTS.put("Student Fee Import", Constants.feeGroupListUrl);

        // ============ LIVE CLASSES ============
        LIST_ENDPOINTS.put("Live Meeting", Constants.liveclassesUrl);
        LIST_ENDPOINTS.put("Live Classes", Constants.liveclassesUrl);
        LIST_ENDPOINTS.put("Live Class Report", Constants.liveclassesUrl);
        LIST_ENDPOINTS.put("Live Meeting Report", Constants.liveclassesUrl);
        LIST_ENDPOINTS.put("Setting", Constants.settingsAdminGeneralUrl);

        // ============ OTHER FEES ============
        LIST_ENDPOINTS.put("other_fees_search", Constants.feeMasterListUrl);

        // ============ ACCOUNTING ============
        LIST_ENDPOINTS.put("accounttransactionreport", Constants.accountTransactionReportListUrl);
        LIST_ENDPOINTS.put("accountreport", Constants.accountReportGenerateUrl);
        LIST_ENDPOINTS.put("accounttransaction", Constants.accountTransactionListUrl);
        LIST_ENDPOINTS.put("accounttype", Constants.addAccountListUrl);

        // ============ HALLTICKET ============
        LIST_ENDPOINTS.put("halltickectgeneration", Constants.tcGenerationListUrl);
        LIST_ENDPOINTS.put("hallticketcreation", Constants.tcGenerationListUrl);
        LIST_ENDPOINTS.put("hallsubjectgroupcombo", Constants.subjectgroupListUrl);
        LIST_ENDPOINTS.put("hallsubjectgroup", Constants.subjectgroupListUrl);

        // ============ FACE ATTENDANCE ============
        LIST_ENDPOINTS.put("Student Registration", Constants.staffDirectoryListUrl);
        LIST_ENDPOINTS.put("Mark Attendance", Constants.dailyAttendanceReportFilterUrl);

        // ============ GENERATE PAPER ============
        LIST_ENDPOINTS.put("AI Question Paper Generator", Constants.onlineExamsReportFilterUrl);

        // ============ STUDENT RESUME ============
        LIST_ENDPOINTS.put("build_cv", Constants.staffDirectoryListUrl);
        LIST_ENDPOINTS.put("download_cv", Constants.staffDirectoryListUrl);

        // ============ ALUMNI ============
        LIST_ENDPOINTS.put("manage_alumini", Constants.alumniAdminListUrl);
        LIST_ENDPOINTS.put("events", Constants.alumniAdminEventsUrl);

        // ============ SYSTEM SETTINGS ============
        LIST_ENDPOINTS.put("general_setting", Constants.settingsAdminGeneralUrl);
        LIST_ENDPOINTS.put("session_setting", Constants.sessionAdminListUrl);
        LIST_ENDPOINTS.put("notification_setting", Constants.settingsAdminNotificationUrl);
        LIST_ENDPOINTS.put("sms_setting", Constants.settingsAdminSmsUrl);
        LIST_ENDPOINTS.put("email_setting", Constants.settingsAdminEmailUrl);
        LIST_ENDPOINTS.put("payment_methods", Constants.settingsAdminPaymentUrl);
        LIST_ENDPOINTS.put("print_headerfooter", Constants.settingsAdminGeneralUrl);
        LIST_ENDPOINTS.put("front_cms_setting", Constants.settingsAdminGeneralUrl);
        LIST_ENDPOINTS.put("roles_permissions", Constants.settingsAdminRolesUrl);
        LIST_ENDPOINTS.put("backup_restore", Constants.settingsAdminGeneralUrl);
        LIST_ENDPOINTS.put("users", Constants.settingsAdminUsersUrl);
        LIST_ENDPOINTS.put("languages", Constants.settingsAdminLanguagesUrl);
        LIST_ENDPOINTS.put("modules", Constants.settingsAdminModulesUrl);
        LIST_ENDPOINTS.put("captcha_setting", Constants.settingsAdminGeneralUrl);
        LIST_ENDPOINTS.put("system_fields", Constants.settingsAdminGeneralUrl);
        LIST_ENDPOINTS.put("student_profile_update", Constants.settingsAdminGeneralUrl);
        LIST_ENDPOINTS.put("online_admission", Constants.onlineAdmissionListUrl);
        LIST_ENDPOINTS.put("file_types", Constants.settingsAdminGeneralUrl);
        LIST_ENDPOINTS.put("system_update", Constants.settingsAdminGeneralUrl);
        LIST_ENDPOINTS.put("sidebar_menu", Constants.settingsAdminGeneralUrl);
        LIST_ENDPOINTS.put("Time Range Assignments", Constants.settingsAdminGeneralUrl);
        LIST_ENDPOINTS.put("whatsapp_messaging", Constants.settingsAdminWhatsappUrl);

        // ============ ADMISSION NO ============
        LIST_ENDPOINTS.put("Add Admission No", Constants.departmentListUrl);
        LIST_ENDPOINTS.put("Admission Bulk Import", Constants.onlineAdmissionListUrl);
        LIST_ENDPOINTS.put("Search Admission", Constants.departmentListUrl);

        // ============ HALLTICKET NO ============
        LIST_ENDPOINTS.put("Add HallTicket No", Constants.departmentListUrl);
        LIST_ENDPOINTS.put("Hallticket Bulk Import", Constants.departmentListUrl);

        // ============ GMEET ============
        LIST_ENDPOINTS.put("Live Classes", Constants.gmeetclassesUrl);
        LIST_ENDPOINTS.put("Live Meeting", Constants.gmeetclassesUrl);
        LIST_ENDPOINTS.put("Live Classes Report", Constants.gmeetclassesUrl);
        LIST_ENDPOINTS.put("Live Meeting Report", Constants.gmeetclassesUrl);
    }

    public SubmenuItemAdapter(Context context, List<MenuSubmenuItem> submenuItems) {
        this.context = context;
        this.submenuItems = submenuItems;
    }

    @NonNull
    @Override
    public SubmenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_submenu_item, parent, false);
        return new SubmenuItemViewHolder(view);
    }

    // Color palettes for icon backgrounds
    private static final int[][] ICON_COLORS = {
        {R.drawable.bg_circle_blue, R.color.cardBlueIcon},
        {R.drawable.bg_circle_green, R.color.cardGreenIcon},
        {R.drawable.bg_circle_purple, R.color.cardPurpleIcon},
        {R.drawable.bg_circle_orange, R.color.cardOrangeIcon},
        {R.drawable.bg_circle_pink, R.color.cardPinkIcon},
        {R.drawable.bg_circle_teal, R.color.cardTealIcon},
        {R.drawable.bg_circle_red, R.color.cardRedIcon},
        {R.drawable.bg_circle_yellow, R.color.cardYellowIcon},
    };

    @Override
    public void onBindViewHolder(@NonNull SubmenuItemViewHolder holder, int position) {
        MenuSubmenuItem submenuItem = submenuItems.get(position);
        holder.submenuItemName.setText(formatDisplayName(submenuItem.getDisplayName()));
        holder.submenuItemIcon.setImageResource(submenuItem.getIconResource());

        // Set colored circle background for icon
        int[] colorPair = ICON_COLORS[position % ICON_COLORS.length];
        holder.iconBg.setBackgroundResource(colorPair[0]);
        holder.submenuItemIcon.setColorFilter(context.getResources().getColor(colorPair[1]));

        holder.submenuItemLayout.setOnClickListener(v -> handleSubmenuItemClick(submenuItem));
    }

    private String getSharedPref(String key) {
        try {
            return com.qdocs.ssre241123.utils.Utility.getSharedPreferences(context, key);
        } catch (Exception e) { return null; }
    }

    private String formatDisplayName(String displayName) {
        if (displayName == null || displayName.isEmpty()) return displayName;
        String[] words = displayName.replace("_", " ").split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (word.length() > 0) {
                result.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) result.append(word.substring(1).toLowerCase());
                result.append(" ");
            }
        }
        return result.toString().trim();
    }

    private void handleSubmenuItemClick(MenuSubmenuItem submenuItem) {
        String itemName = submenuItem.getName();
        String displayName = submenuItem.getDisplayName();

        // 1. Check native activities
        Class<?> nativeActivity = findNativeActivity(itemName);
        if (nativeActivity != null) {
            context.startActivity(new Intent(context, nativeActivity));
            performTransition();
            return;
        }

        // 2. Check CRUD screens
        CrudConfig crudConfig = findCrudConfig(itemName);
        if (crudConfig != null) {
            launchCrudActivity(crudConfig, displayName);
            return;
        }

        // 3. Check list endpoints
        String listUrl = LIST_ENDPOINTS.get(itemName);
        if (listUrl != null && !listUrl.isEmpty()) {
            String nameField = findNameField(itemName);
            launchListActivity(listUrl, nameField, displayName);
            return;
        }

        // 4. Fallback: show empty list (still native, no WebView)
        launchListActivity("", "name", displayName);
    }

    private Class<?> findNativeActivity(String name) {
        if (name == null) return null;
        switch (name) {
            case "student_details": return TeacherStudentDetailsActivity.class;
            case "student_categories":
            case "categories": return StudentCategoriesActivity.class;
            case "student_house":
            case "house": return StudentHousesActivity.class;
            case "disable_reason": return DisableReasonsActivity.class;
            default: return null;
        }
    }

    private CrudConfig findCrudConfig(String name) {
        if (name == null) return null;
        switch (name) {
            // FRONT OFFICE
            case "admission_enquiry": return new CrudConfig("Admission Enquiry", Constants.admissionEnquiryListUrl, Constants.admissionEnquiryCreateUrl, Constants.admissionEnquiryUpdateUrl, Constants.admissionEnquiryDeleteUrl, "name");
            case "visitor_book": return new CrudConfig("Visitors", Constants.visitorsListUrl, Constants.visitorsCreateUrl, Constants.visitorsUpdateUrl, Constants.visitorsDeleteUrl, "name");
            case "phone_call_log": return new CrudConfig("Phone Call Log", Constants.phoneCallListUrl, Constants.phoneCallCreateUrl, Constants.phoneCallUpdateUrl, Constants.phoneCallDeleteUrl, "name");
            case "postal_dispatch": return new CrudConfig("Postal Dispatch", Constants.postalDispatchListUrl, Constants.postalDispatchCreateUrl, Constants.postalDispatchUpdateUrl, Constants.postalDispatchDeleteUrl, "name");
            case "postal_receive": return new CrudConfig("Postal Receive", Constants.postalReceiveListUrl, Constants.postalReceiveCreateUrl, Constants.postalReceiveUpdateUrl, Constants.postalReceiveDeleteUrl, "name");
            case "complain": return new CrudConfig("Complaints", Constants.complaintListUrl, Constants.complaintCreateUrl, Constants.complaintUpdateUrl, Constants.complaintDeleteUrl, "name");
            case "setup_front_office": return new CrudConfig("Visitor Purpose", Constants.visitorsPurposeListUrl, Constants.visitorsPurposeCreateUrl, Constants.visitorsPurposeUpdateUrl, Constants.visitorsPurposeDeleteUrl, "purpose");

            // ACADEMICS
            case "class": return new CrudConfig("Classes", Constants.classesListUrl, Constants.classesCreateUrl, Constants.classesUpdateUrl, Constants.classesDeleteUrl, "class", true);
            case "sections": return new CrudConfig("Sections", Constants.sectionsListUrl, Constants.sectionsCreateUrl, Constants.sectionsUpdateUrl, Constants.sectionsDeleteUrl, "section", true);
            case "subjects": return new CrudConfig("Subjects", Constants.subjectsListUrl, Constants.subjectsCreateUrl, Constants.subjectsUpdateUrl, Constants.subjectsDeleteUrl, "name", true);
            case "subject_group": return new CrudConfig("Subject Groups", Constants.subjectgroupListUrl, Constants.subjectgroupCreateUrl, Constants.subjectgroupUpdateUrl, Constants.subjectgroupDeleteUrl, "name", true);

            // FEES
            case "fees_master": return new CrudConfig("Fee Master", Constants.feeMasterListUrl, Constants.feeMasterCreateUrl, Constants.feeMasterUpdateUrl, Constants.feeMasterDeleteUrl, "name", true);
            case "fees_group": return new CrudConfig("Fee Groups", Constants.feeGroupListUrl, Constants.feeGroupCreateUrl, Constants.feeGroupUpdateUrl, Constants.feeGroupDeleteUrl, "name", true);
            case "fees_type": return new CrudConfig("Fee Types", Constants.feeTypeListUrl, Constants.feeTypeCreateUrl, Constants.feeTypeUpdateUrl, Constants.feeTypeDeleteUrl, "name", true);
            case "fees_discount": return new CrudConfig("Fee Discounts", Constants.feeGroupListUrl, Constants.feeGroupCreateUrl, Constants.feeGroupUpdateUrl, Constants.feeGroupDeleteUrl, "name", true);

            // INCOME/EXPENSE
            case "income_head": return new CrudConfig("Income Heads", Constants.incomeHeadCrudListUrl, Constants.incomeHeadCreateUrl, Constants.incomeHeadUpdateUrl, Constants.incomeHeadDeleteUrl, "name", true);
            case "expense_head": return new CrudConfig("Expense Heads", Constants.expenseHeadListUrl2, null, null, null, "name", true);

            // HUMAN RESOURCE
            case "department": return new CrudConfig("Departments", Constants.departmentListUrl, Constants.departmentCreateUrl, Constants.departmentUpdateUrl, Constants.departmentDeleteUrl, "department_name", true);
            case "designation": return new CrudConfig("Designations", Constants.designationListUrl, Constants.designationCreateUrl, Constants.designationUpdateUrl, Constants.designationDeleteUrl, "designation", true);
            case "leave_type": return new CrudConfig("Leave Types", Constants.leaveAdminTypesUrl, null, null, null, "type", true);

            // INVENTORY
            case "add_item": return new CrudConfig("Items", Constants.incomeHeadListUrl, null, null, null, "name", true);
            case "item_category": return new CrudConfig("Item Categories", Constants.incomeHeadListUrl, null, null, null, "name", true);
            case "item_store": return new CrudConfig("Item Stores", Constants.incomeHeadListUrl, null, null, null, "name", true);
            case "item_supplier": return new CrudConfig("Item Suppliers", Constants.incomeHeadListUrl, null, null, null, "name", true);

            // TRANSPORT
            case "routes": return new CrudConfig("Routes", Constants.transportAdminRoutesUrl, null, null, null, "route_title", true);
            case "vehicles": return new CrudConfig("Vehicles", Constants.transportAdminVehiclesUrl, null, null, null, "vehicle_no", true);
            case "pickup_point": return new CrudConfig("Pickup Points", Constants.transportAdminPickupPointsUrl, null, null, null, "pickup_point", true);

            // HOSTEL
            case "hostel": return new CrudConfig("Hostels", Constants.hostelAdminHostelsUrl, null, null, null, "hostel_name", true);
            case "hostel_rooms": return new CrudConfig("Hostel Rooms", Constants.hostelAdminRoomsUrl, null, null, null, "room_no", true);
            case "room_type": return new CrudConfig("Room Types", Constants.hostelAdminRoomTypesUrl, null, null, null, "room_type", true);

            // CERTIFICATES
            case "student_certificate": return new CrudConfig("Student Certificates", Constants.departmentListUrl, null, null, null, "name", true);
            case "student_id_card": return new CrudConfig("Student ID Cards", Constants.departmentListUrl, null, null, null, "name", true);
            case "staff_id_card": return new CrudConfig("Staff ID Cards", Constants.departmentListUrl, null, null, null, "name", true);

            // COMMUNICATE
            case "notice_board": return new CrudConfig("Notice Board", Constants.notificationAdminListUrl, Constants.notificationAdminCreateUrl, null, Constants.notificationAdminDeleteUrl, "title", true);
            case "email_template": return new CrudConfig("Email Templates", Constants.departmentListUrl, null, null, null, "name", true);
            case "sms_template": return new CrudConfig("SMS Templates", Constants.departmentListUrl, null, null, null, "name", true);

            // ACCOUNTING
            case "addaccount": return new CrudConfig("Accounts", Constants.addAccountListUrl, Constants.addAccountCreateUrl, Constants.addAccountUpdateUrl, Constants.addAccountDeleteUrl, "name", true);
            case "accountcategorygroup": return new CrudConfig("Account Category Groups", Constants.accountCategoryGroupListUrl, Constants.accountCategoryGroupCreateUrl, Constants.accountCategoryGroupUpdateUrl, Constants.accountCategoryGroupDeleteUrl, "name", true);
            case "accountcategory": return new CrudConfig("Account Categories", Constants.accountCategoryListUrl, Constants.accountCategoryCreateUrl, Constants.accountCategoryUpdateUrl, Constants.accountCategoryDeleteUrl, "name", true);

            // FRONT CMS
            case "event": return new CrudConfig("Events", Constants.cmsAdminEventsUrl, null, null, null, "title", true);
            case "news": return new CrudConfig("News", Constants.cmsAdminPagesUrl, null, null, null, "title", true);
            case "pages": return new CrudConfig("Pages", Constants.cmsAdminPagesUrl, null, null, null, "title", true);
            case "menus": return new CrudConfig("Menus", Constants.cmsAdminMenusUrl, null, null, null, "menu", true);

            // BEHAVIOUR
            case "assign_incident": return new CrudConfig("Incidents", Constants.studentbehaviourListUrl, Constants.studentbehaviourCreateUrl, Constants.studentbehaviourUpdateUrl, Constants.studentbehaviourDeleteUrl, "name", true);

            // EXAMINATIONS
            case "exam_group": return new CrudConfig("Exam Groups", Constants.examAdminExamGroupsUrl, null, null, null, "name", true);
            case "marks_grade": return new CrudConfig("Grades", Constants.examAdminGradesUrl, null, null, null, "name", true);
            case "marks_division": return new CrudConfig("Marks Division", Constants.examAdminMarksDivisionsUrl, null, null, null, "name", true);

            // RESULTS
            case "internal_result_subject_group":
            case "external_result_subject_group": return new CrudConfig("Subject Groups", Constants.subjectgroupListUrl, Constants.subjectgroupCreateUrl, Constants.subjectgroupUpdateUrl, Constants.subjectgroupDeleteUrl, "name", true);
            case "internal_examtype":
            case "external_examtype": return new CrudConfig("Exam Types", Constants.departmentListUrl, null, null, null, "name", true);

            // ONLINE EXAMS
            case "question_bank": return new CrudConfig("Question Bank", Constants.onlineExamsReportFilterUrl, null, null, null, "name", true);

            // LESSON PLAN
            case "lesson": return new CrudConfig("Lessons", Constants.lessonAdminLessonsUrl, null, null, null, "name", true);
            case "topic": return new CrudConfig("Topics", Constants.lessonAdminTopicsUrl, null, null, null, "name", true);

            // CBSE
            case "exam_grade": return new CrudConfig("Exam Grades", Constants.examAdminGradesUrl, null, null, null, "name", true);
            case "observation": return new CrudConfig("Observations", Constants.departmentListUrl, null, null, null, "name", true);
            case "observation_parameter": return new CrudConfig("Observation Parameters", Constants.departmentListUrl, null, null, null, "name", true);
            case "term": return new CrudConfig("Terms", Constants.sessionAdminListUrl, null, null, null, "session", true);
            case "template": return new CrudConfig("Templates", Constants.departmentListUrl, null, null, null, "name", true);

            // OTHER FEES
            case "other_fees_master": return new CrudConfig("Fee Master", Constants.feeMasterListUrl, Constants.feeMasterCreateUrl, Constants.feeMasterUpdateUrl, Constants.feeMasterDeleteUrl, "name", true);
            case "other_fees_group": return new CrudConfig("Fee Groups", Constants.feeGroupListUrl, Constants.feeGroupCreateUrl, Constants.feeGroupUpdateUrl, Constants.feeGroupDeleteUrl, "name", true);
            case "other_fees_type": return new CrudConfig("Fee Types", Constants.feeTypeListUrl, Constants.feeTypeCreateUrl, Constants.feeTypeUpdateUrl, Constants.feeTypeDeleteUrl, "name", true);

            // HALLTICKET
            case "hallsubject": return new CrudConfig("Hall Subjects", Constants.subjectsListUrl, Constants.subjectsCreateUrl, Constants.subjectsUpdateUrl, Constants.subjectsDeleteUrl, "name", true);

            // ALUMNI
            case "events": return new CrudConfig("Events", Constants.alumniAdminEventsUrl, null, null, null, "title", true);

            // SYSTEM SETTINGS
            case "session_setting": return new CrudConfig("Sessions", Constants.sessionAdminListUrl, null, null, null, "session", true);
            case "custom_fields": return new CrudConfig("Custom Fields", Constants.departmentListUrl, null, null, null, "name", true);
            case "currency": return new CrudConfig("Currency", Constants.settingsAdminCurrencyUrl, null, null, null, "currency", true);

            default: return null;
        }
    }

    private String findNameField(String name) {
        if (name == null) return "name";
        if (name.contains("class") || name.equals("class")) return "class";
        if (name.contains("department")) return "department_name";
        if (name.contains("designation")) return "designation";
        if (name.contains("route")) return "route_title";
        if (name.contains("vehicle")) return "vehicle_no";
        if (name.contains("hostel")) return "hostel_name";
        if (name.contains("room")) return "room_no";
        if (name.contains("room_type")) return "room_type";
        if (name.contains("session")) return "session";
        if (name.contains("currency")) return "currency";
        if (name.contains("title")) return "title";
        return "name";
    }

    private void launchCrudActivity(CrudConfig config, String displayName) {
        Intent intent = new Intent(context, GenericCrudActivity.class);
        intent.putExtra("screen_title", config.title);
        intent.putExtra("list_url", config.listUrl);
        intent.putExtra("create_url", config.createUrl != null ? config.createUrl : "");
        intent.putExtra("update_url", config.updateUrl != null ? config.updateUrl : "");
        intent.putExtra("delete_url", config.deleteUrl != null ? config.deleteUrl : "");
        intent.putExtra("name_field", config.nameField);
        intent.putExtra("show_active_switch", config.showActiveSwitch);
        context.startActivity(intent);
        performTransition();
    }

    private void launchListActivity(String listUrl, String nameField, String displayName) {
        Intent intent = new Intent(context, GenericListActivity.class);
        intent.putExtra("screen_title", displayName);
        intent.putExtra("list_url", listUrl);
        intent.putExtra("name_field", nameField);
        context.startActivity(intent);
        performTransition();
    }

    private void performTransition() {
        if (context instanceof android.app.Activity) {
            ((android.app.Activity) context).overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
        }
    }

    private static class CrudConfig {
        String title, listUrl, createUrl, updateUrl, deleteUrl, nameField;
        boolean showActiveSwitch;
        CrudConfig(String title, String listUrl, String createUrl, String updateUrl, String deleteUrl, String nameField) {
            this(title, listUrl, createUrl, updateUrl, deleteUrl, nameField, false);
        }
        CrudConfig(String title, String listUrl, String createUrl, String updateUrl, String deleteUrl, String nameField, boolean showActiveSwitch) {
            this.title = title; this.listUrl = listUrl; this.createUrl = createUrl;
            this.updateUrl = updateUrl; this.deleteUrl = deleteUrl; this.nameField = nameField;
            this.showActiveSwitch = showActiveSwitch;
        }
    }

    @Override
    public int getItemCount() { return submenuItems.size(); }

    static class SubmenuItemViewHolder extends RecyclerView.ViewHolder {
        View submenuItemLayout;
        View iconBg;
        ImageView submenuItemIcon;
        TextView submenuItemName;
        SubmenuItemViewHolder(@NonNull View itemView) {
            super(itemView);
            submenuItemLayout = itemView.findViewById(R.id.submenu_item_layout);
            iconBg = itemView.findViewById(R.id.icon_bg);
            submenuItemIcon = itemView.findViewById(R.id.submenu_item_icon);
            submenuItemName = itemView.findViewById(R.id.submenu_item_name);
        }
    }
}
