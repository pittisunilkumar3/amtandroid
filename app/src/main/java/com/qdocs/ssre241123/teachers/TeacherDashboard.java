package com.qdocs.ssre241123.teachers;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.qdocs.ssre241123.AboutSchool;
import com.qdocs.ssre241123.Login;
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.SettingActivity;
import com.qdocs.ssre241123.adapters.TeacherModuleAdapter;
import com.qdocs.ssre241123.model.TeacherModule;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.DrawerArrowDrawable;
import com.qdocs.ssre241123.utils.TeacherAuthHelper;
import com.qdocs.ssre241123.utils.Utility;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.List;

public class TeacherDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerArrowDrawable drawerArrowDrawable;
    ImageView drawerIndicator;
    public DrawerLayout drawer;
    protected FrameLayout mDrawerLayout, actionBar;
    private NavigationView navigationView;
    public boolean flipped;
    public float offset;
    ImageView actionBarLogo;
    FrameLayout notification_alert;
    private TextView classTV, nameTV, childDetailsTV;
    private ImageView profileImageIV;
    private LinearLayout switchChildBtn;
    private RelativeLayout drawerHead;
    TextView name, admissionno, classdata;
    ImageView profileImageview;
    LinearLayout profilelinear;
    
    // RecyclerViews for teacher modules
    private RecyclerView teacherManagementRecyclerView;
    private RecyclerView teacherAcademicRecyclerView;
    private RecyclerView teacherCommunicationRecyclerView;
    private RecyclerView teacherToolsRecyclerView;
    
    // Adapters
    private TeacherModuleAdapter managementAdapter;
    private TeacherModuleAdapter academicAdapter;
    private TeacherModuleAdapter communicationAdapter;
    private TeacherModuleAdapter toolsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        // Check if teacher is logged in
        if (!TeacherAuthHelper.isTeacherLoggedIn(this)) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
            return;
        }

        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), Constants.langCode));

        initializeViews();
        setupDrawer();
        decorate();
        loadTeacherProfile();
        setupModules();
    }

    private void initializeViews() {
        drawerIndicator = findViewById(R.id.drawer_indicator);
        drawer = findViewById(R.id.drawer_layout);
        actionBar = findViewById(R.id.actionBar);
        actionBarLogo = findViewById(R.id.actionBar_logo);
        notification_alert = findViewById(R.id.notification_alert);
        navigationView = findViewById(R.id.nav_view);
        profilelinear = findViewById(R.id.profilelinear);
        name = findViewById(R.id.name);
        admissionno = findViewById(R.id.admissionno);
        classdata = findViewById(R.id.classdata);
        profileImageview = findViewById(R.id.studentProfile_profileImageview);

        // Initialize RecyclerViews
        teacherManagementRecyclerView = findViewById(R.id.teacher_management_recyclerView);
        teacherAcademicRecyclerView = findViewById(R.id.teacher_academic_recyclerView);
        teacherCommunicationRecyclerView = findViewById(R.id.teacher_communication_recyclerView);
        teacherToolsRecyclerView = findViewById(R.id.teacher_tools_recyclerView);

        navigationView.setNavigationItemSelectedListener(this);

        // Set up navigation drawer header
        View headerLayout = navigationView.getHeaderView(0);
        classTV = headerLayout.findViewById(R.id.drawer_userClass);
        nameTV = headerLayout.findViewById(R.id.drawer_userName);
        profileImageIV = headerLayout.findViewById(R.id.drawer_logo);
        drawerHead = headerLayout.findViewById(R.id.drawer_head);
        switchChildBtn = headerLayout.findViewById(R.id.drawer_switchChildBtn);
        childDetailsTV = headerLayout.findViewById(R.id.drawer_studentDetailsTV);

        // Hide switch child button for teachers
        switchChildBtn.setVisibility(View.GONE);

        // Set up version info in navigation drawer
        Menu menu = navigationView.getMenu();
        RelativeLayout tracks = (RelativeLayout) menu.findItem(R.id.nav_log_version).getActionView();
        TextView version_name = (TextView) tracks.findViewById(R.id.version_name);
        version_name.setText(getApplicationContext().getString(R.string.version) + " on " + Utility.getSharedPreferences(getApplicationContext(), Constants.app_ver));
    }

    private void setupDrawer() {
        Resources resources = getResources();
        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.drawerIndicatorColour));

        drawerIndicator.setImageDrawable(drawerArrowDrawable);

        drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                offset = slideOffset;
                // Sometimes slideOffset ends up so close to but not quite 1 or 0.
                if (slideOffset >= .995) {
                    flipped = true;
                    drawerArrowDrawable.setFlip(flipped);
                } else if (slideOffset <= .005) {
                    flipped = false;
                    drawerArrowDrawable.setFlip(flipped);
                }
                drawerArrowDrawable.setParameter(offset);
            }
        });

        drawerIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void decorate() {
        Utility.setLocale(getApplicationContext(), Utility.getSharedPreferences(getApplicationContext(), Constants.langCode));
        String appLogo = Utility.getSharedPreferences(this, Constants.appLogo) + "?" + System.currentTimeMillis();

        // Set colors based on theme
        actionBar.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        profilelinear.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        drawerHead.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        }

        // Load app logo
        Picasso.with(getApplicationContext()).load(appLogo).fit().centerInside().placeholder(null).into(actionBarLogo);
    }

    private void loadTeacherProfile() {
        // Load teacher data from SharedPreferences
        String teacherName = TeacherAuthHelper.getTeacherName(this);
        String employeeId = Utility.getSharedPreferences(this, Constants.teacherEmployeeId);
        String designation = Utility.getSharedPreferences(this, Constants.teacherDesignation);
        String department = Utility.getSharedPreferences(this, Constants.teacherDepartment);
        String imageUrl = Utility.getSharedPreferences(this, Constants.teacherImage);
        String schoolName = Utility.getSharedPreferences(this, "sch_name");

        // Set teacher information in main profile area
        name.setText(teacherName);
        admissionno.setText("Emp. ID: " + employeeId);

        // Build display info for the third line
        String displayInfo = "";
        if (!designation.isEmpty() && !designation.equals("null")) {
            displayInfo = "Designation: " + designation;
        }
        if (!department.isEmpty() && !department.equals("null")) {
            if (!displayInfo.isEmpty()) {
                displayInfo += " | Dept: " + department;
            } else {
                displayInfo = "Department: " + department;
            }
        }
        if (displayInfo.isEmpty()) {
            displayInfo = !schoolName.isEmpty() ? schoolName : "Teacher";
        }
        classdata.setText(displayInfo);

        // Set teacher information in drawer header
        nameTV.setText(teacherName);
        classTV.setText(displayInfo);
        childDetailsTV.setVisibility(View.GONE);
        classTV.setVisibility(View.VISIBLE);

        // Load teacher profile image
        loadTeacherImage(imageUrl);
    }

    private void loadTeacherImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty() && !imageUrl.equals("null") && !imageUrl.equals("")) {
            String baseUrl = Utility.getSharedPreferences(this, "apiUrl");
            // Remove the trailing slash if present
            if (baseUrl.endsWith("/")) {
                baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
            }
            String fullImageUrl = baseUrl + "/uploads/staff_images/" + imageUrl;

            Log.e("Teacher Image URL", fullImageUrl);

            // Load image in main profile area
            Picasso.with(this)
                    .load(fullImageUrl)
                    .placeholder(R.drawable.demo)
                    .error(R.drawable.demo)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(profileImageview);

            // Load image in drawer header
            Picasso.with(this)
                    .load(fullImageUrl)
                    .placeholder(R.drawable.placeholder_user)
                    .error(R.drawable.placeholder_user)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(profileImageIV);
        } else {
            Log.d("Teacher Image", "No image URL available, using default");
            profileImageview.setImageResource(R.drawable.demo);
            profileImageIV.setImageResource(R.drawable.placeholder_user);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                // Already on dashboard, just close drawer
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_profile:
                Intent profile = new Intent(TeacherDashboard.this, TeacherProfile.class);
                startActivity(profile);
                overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_about:
                Intent about = new Intent(TeacherDashboard.this, AboutSchool.class);
                startActivity(about);
                overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_setting:
                Intent setting = new Intent(TeacherDashboard.this, SettingActivity.class);
                startActivity(setting);
                overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_logout:
                showLogoutConfirmationDialog();
                break;
        }

        return true;
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                TeacherAuthHelper.performTeacherLogout(TeacherDashboard.this);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerVisible(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setupModules() {
        // Setup Management & Administrative Modules (10 modules)
        List<TeacherModule> managementModules = new ArrayList<>();
        managementModules.add(new TeacherModule("student_information", "Student Information", "fa-user", R.drawable.ic_fa_user, true));
        managementModules.add(new TeacherModule("fees_collection", "Fees Collection", "fa-money", R.drawable.ic_fa_money, true));
        managementModules.add(new TeacherModule("income", "Income", "fa-dollar", R.drawable.ic_fa_dollar, true));
        managementModules.add(new TeacherModule("other_fees", "Other Fees", "fa-credit-card", R.drawable.ic_fa_credit_card, true));
        managementModules.add(new TeacherModule("expenses", "Expenses", "fa-credit-card-alt", R.drawable.ic_fa_credit_card_alt, true));
        managementModules.add(new TeacherModule("accounting", "Accounting", "fa-calculator", R.drawable.ic_fa_calculator, true));
        managementModules.add(new TeacherModule("front_office", "Front Office", "fa-address-book", R.drawable.ic_fa_address_book, true));
        managementModules.add(new TeacherModule("human_resource", "Human Resource", "fa-users", R.drawable.ic_fa_users, true));
        managementModules.add(new TeacherModule("multi_branch", "Multi Branch", "fa-sitemap", R.drawable.ic_fa_sitemap, true));
        managementModules.add(new TeacherModule("fee_discount", "Fee Discount", "fa-percent", R.drawable.ic_fa_percent, true));

        // Setup Academic & Examination Modules (10 modules)
        List<TeacherModule> academicModules = new ArrayList<>();
        academicModules.add(new TeacherModule("attendance", "Attendance", "fa-calendar-check-o", R.drawable.ic_fa_calendar_check, true));
        academicModules.add(new TeacherModule("examinations", "Examinations", "fa-file-text", R.drawable.ic_fa_file_text, true));
        academicModules.add(new TeacherModule("online_examinations", "Online Examinations", "fa-rss", R.drawable.ic_fa_rss, true));
        academicModules.add(new TeacherModule("cbse_examination", "CBSE Examination", "fa-book", R.drawable.ic_fa_book, true));
        academicModules.add(new TeacherModule("lesson_plan", "Lesson Plan", "fa-book", R.drawable.ic_fa_book, true));
        academicModules.add(new TeacherModule("academics", "Academics", "fa-graduation-cap", R.drawable.ic_fa_graduation_cap, true));
        academicModules.add(new TeacherModule("homework", "Homework", "fa-tasks", R.drawable.ic_fa_tasks, true));
        academicModules.add(new TeacherModule("library", "Library", "fa-book", R.drawable.ic_fa_book, true));
        academicModules.add(new TeacherModule("results", "Results", "fa-list-alt", R.drawable.ic_fa_list_alt, true));
        academicModules.add(new TeacherModule("hall_ticket_generation", "Hall Ticket Generation", "fa-ticket", R.drawable.ic_fa_ticket, true));

        // Setup Communication & Services Modules (9 modules)
        List<TeacherModule> communicationModules = new ArrayList<>();
        communicationModules.add(new TeacherModule("communicate", "Communicate", "fa-envelope", R.drawable.ic_fa_envelope, true));
        communicationModules.add(new TeacherModule("zoom_live_classes", "Zoom Live Classes", "fa-video-camera", R.drawable.ic_videocam, true));
        communicationModules.add(new TeacherModule("gmeet_live_classes", "Gmeet Live Classes", "fa-video-camera", R.drawable.ic_videocam, true));
        communicationModules.add(new TeacherModule("behaviour_records", "Behaviour Records", "fa-exclamation-triangle", R.drawable.ic_fa_exclamation_triangle, true));
        communicationModules.add(new TeacherModule("inventory", "Inventory", "fa-archive", R.drawable.ic_fa_archive, true));
        communicationModules.add(new TeacherModule("transport", "Transport", "fa-bus", R.drawable.ic_fa_bus, true));
        communicationModules.add(new TeacherModule("hostel", "Hostel", "fa-building", R.drawable.ic_fa_building, true));
        communicationModules.add(new TeacherModule("alumni", "Alumni", "fa-graduation-cap", R.drawable.ic_fa_graduation_cap, true));
        communicationModules.add(new TeacherModule("referral_application", "Referral Application", "fa-share-alt", R.drawable.ic_fa_share_alt, true));

        // Setup Tools, Reports & Admin Modules (9 modules)
        List<TeacherModule> toolsModules = new ArrayList<>();
        toolsModules.add(new TeacherModule("reports", "Reports", "fa-bar-chart", R.drawable.ic_fa_bar_chart, true));
        toolsModules.add(new TeacherModule("tc_generation", "TC Generation", "fa-certificate", R.drawable.ic_fa_certificate, true));
        toolsModules.add(new TeacherModule("certificate", "Certificate", "fa-certificate", R.drawable.ic_fa_certificate, true));
        toolsModules.add(new TeacherModule("admission_no", "Admission No", "fa-id-card", R.drawable.ic_fa_id_card, true));
        toolsModules.add(new TeacherModule("hallticket_no", "HallTicket No", "fa-ticket", R.drawable.ic_fa_ticket, true));
        toolsModules.add(new TeacherModule("importing", "Importing", "fa-upload", R.drawable.ic_fa_upload, true));
        toolsModules.add(new TeacherModule("download_center", "Download Center", "fa-download", R.drawable.ic_download, true));
        toolsModules.add(new TeacherModule("front_cms", "Front CMS", "fa-desktop", R.drawable.ic_fa_desktop, true));
        toolsModules.add(new TeacherModule("system_setting", "System Settings", "fa-cogs", R.drawable.ic_fa_cogs, true));

        // Setup RecyclerViews with GridLayoutManager (4 columns each)
        teacherManagementRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        teacherAcademicRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        teacherCommunicationRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        teacherToolsRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        // Initialize adapters
        managementAdapter = new TeacherModuleAdapter(this, managementModules);
        academicAdapter = new TeacherModuleAdapter(this, academicModules);
        communicationAdapter = new TeacherModuleAdapter(this, communicationModules);
        toolsAdapter = new TeacherModuleAdapter(this, toolsModules);

        // Set adapters
        teacherManagementRecyclerView.setAdapter(managementAdapter);
        teacherAcademicRecyclerView.setAdapter(academicAdapter);
        teacherCommunicationRecyclerView.setAdapter(communicationAdapter);
        teacherToolsRecyclerView.setAdapter(toolsAdapter);
    }
}