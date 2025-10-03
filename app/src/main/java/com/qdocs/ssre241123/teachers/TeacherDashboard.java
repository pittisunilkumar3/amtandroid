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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.qdocs.ssre241123.AboutSchool;
import com.qdocs.ssre241123.Login;
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.SettingActivity;
import com.qdocs.ssre241123.adapters.TeacherModuleAdapter;
// Removed conflicting import - will use fully qualified names
import com.qdocs.ssre241123.model.MenuResponse;
import com.qdocs.ssre241123.model.TeacherModule;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.DrawerArrowDrawable;
import com.qdocs.ssre241123.utils.TeacherAuthHelper;
import com.qdocs.ssre241123.utils.Utility;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        loadTeacherMenus();
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

    private void loadTeacherMenus() {
        // Use consistent URL building like other API calls
        String url = Utility.buildApiUrl(getApplicationContext(), Constants.teacherMenuUrl);
        
        // Get staff ID from shared preferences with proper fallback
        String staffId = Utility.getSharedPreferences(getApplicationContext(), Constants.teacherStaffId);
        if (staffId == null || staffId.isEmpty()) {
            staffId = Utility.getSharedPreferences(getApplicationContext(), Constants.userId);
        }
        if (staffId == null || staffId.isEmpty()) {
            staffId = "1"; // Default fallback for testing
        }
        
        // Create JSON request body
        Map<String, String> params = new HashMap<>();
        params.put("staff_id", staffId);
        
        JSONObject jsonBody = new JSONObject(params);
        final String requestBody = jsonBody.toString();
        
        Log.d("TeacherMenuAPI", "=== API REQUEST ===");
        Log.d("TeacherMenuAPI", "URL: " + url);
        Log.d("TeacherMenuAPI", "Method: POST");
        Log.d("TeacherMenuAPI", "Staff ID: " + staffId);
        Log.d("TeacherMenuAPI", "Request Body: " + requestBody);
        Log.d("TeacherMenuAPI", "==================");
        
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("TeacherMenuAPI", "=== API RESPONSE ===");
                    Log.d("TeacherMenuAPI", "Response Length: " + response.length());
                    Log.d("TeacherMenuAPI", "Response: " + response.substring(0, Math.min(500, response.length())));
                    Log.d("TeacherMenuAPI", "==================");
                    
                    try {
                        Gson gson = new Gson();
                        MenuResponse menuResponse = gson.fromJson(response, MenuResponse.class);
                        
                        if (menuResponse != null && menuResponse.getStatus() == 1 && menuResponse.getData() != null) {
                            List<com.qdocs.ssre241123.model.MenuItem> menus = menuResponse.getData().getMenus();
                            if (menus != null && !menus.isEmpty()) {
                                Log.d("TeacherMenuAPI", "✓ Success: Received " + menus.size() + " menu items");

                                // Log first few menu items for debugging
                                for (int i = 0; i < Math.min(3, menus.size()); i++) {
                                    com.qdocs.ssre241123.model.MenuItem item = menus.get(i);
                                    Log.d("TeacherMenuAPI", "Menu " + (i+1) + ": " + item.getMenu() + " | Icon: " + item.getIcon());
                                }

                                // Cache menu data for submenu activities
                                TeacherSubmenuActivity.cacheMenuData(menus);

                                setupModulesFromAPI(menus);
                            } else {
                                Log.e("TeacherMenuAPI", "✗ Error: No menu items in data.menus array");
                                setupDefaultModules();
                            }
                        } else {
                            String errorMsg = menuResponse != null ? menuResponse.getMessage() : "Unknown error";
                            Log.e("TeacherMenuAPI", "✗ Error: " + errorMsg);
                            setupDefaultModules();
                        }
                    } catch (Exception e) {
                        Log.e("TeacherMenuAPI", "✗ JSON parsing error: " + e.getMessage());
                        e.printStackTrace();
                        setupDefaultModules();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TeacherMenuAPI", "=== API ERROR ===");
                    String errorMsg = "Network error";
                    if (error.networkResponse != null) {
                        errorMsg += " - Status Code: " + error.networkResponse.statusCode;
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            Log.e("TeacherMenuAPI", "Error Response: " + responseBody);
                        } catch (Exception e) {
                            Log.e("TeacherMenuAPI", "Could not parse error response");
                        }
                    }
                    if (error.getMessage() != null) {
                        Log.e("TeacherMenuAPI", "Error Message: " + error.getMessage());
                    }
                    Log.e("TeacherMenuAPI", "=================");
                    setupDefaultModules();
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
                
                Log.d("TeacherMenuAPI", "Request Headers: " + headers.toString());
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

    private void setupModulesFromAPI(List<com.qdocs.ssre241123.model.MenuItem> menuItems) {
        if (menuItems == null || menuItems.isEmpty()) {
            setupDefaultModules();
            return;
        }

        // Convert MenuItems to TeacherModules
        List<TeacherModule> allModules = new ArrayList<>();
        for (com.qdocs.ssre241123.model.MenuItem menuItem : menuItems) {
            TeacherModule module = TeacherModule.fromMenuItem(menuItem);
            allModules.add(module);
        }

        // Organize modules by categories based on level and system_level
        List<TeacherModule> managementModules = new ArrayList<>();
        List<TeacherModule> academicModules = new ArrayList<>();
        List<TeacherModule> communicationModules = new ArrayList<>();
        List<TeacherModule> toolsModules = new ArrayList<>();

        for (TeacherModule module : allModules) {
            String activateMenu = module.getActivateMenu();
            
            // Categorize based on activate_menu or level
            if (isManagementModule(activateMenu)) {
                managementModules.add(module);
            } else if (isAcademicModule(activateMenu)) {
                academicModules.add(module);
            } else if (isCommunicationModule(activateMenu)) {
                communicationModules.add(module);
            } else {
                toolsModules.add(module);
            }
        }

        // Setup RecyclerViews and adapters
        setupRecyclerViews(managementModules, academicModules, communicationModules, toolsModules);
    }

    private boolean isManagementModule(String activateMenu) {
        return activateMenu != null && (
            activateMenu.contains("student_information") ||
            activateMenu.contains("fees_collection") ||
            activateMenu.contains("income") ||
            activateMenu.contains("expense") ||
            activateMenu.contains("account_module") ||
            activateMenu.contains("other_fees") ||
            activateMenu.contains("human_resource")
        );
    }

    private boolean isAcademicModule(String activateMenu) {
        return activateMenu != null && (
            activateMenu.contains("attendance") ||
            activateMenu.contains("academics") ||
            activateMenu.contains("examination") ||
            activateMenu.contains("library") ||
            activateMenu.contains("results") ||
            activateMenu.contains("hallticketgeneration") ||
            activateMenu.contains("tc_generation")
        );
    }

    private boolean isCommunicationModule(String activateMenu) {
        return activateMenu != null && (
            activateMenu.contains("communicate") ||
            activateMenu.contains("online_classes") ||
            activateMenu.contains("gmeet") ||
            activateMenu.contains("behaviour_records") ||
            activateMenu.contains("inventory") ||
            activateMenu.contains("transport") ||
            activateMenu.contains("hostel") ||
            activateMenu.contains("referral_branch")
        );
    }

    private void setupDefaultModules() {
        // Fallback to hardcoded modules if API fails
        List<TeacherModule> managementModules = new ArrayList<>();
        managementModules.add(new TeacherModule("student_information", "Student Information", "fa-user", R.drawable.ic_fa_user, true));
        managementModules.add(new TeacherModule("fees_collection", "Fees Collection", "fa-money", R.drawable.ic_fa_money, true));
        managementModules.add(new TeacherModule("income", "Income", "fa-dollar", R.drawable.ic_fa_dollar, true));
        managementModules.add(new TeacherModule("expense", "Expenses", "fa-credit-card", R.drawable.ic_fa_credit_card, true));

        List<TeacherModule> academicModules = new ArrayList<>();
        academicModules.add(new TeacherModule("attendance", "Attendance", "fa-calendar-check-o", R.drawable.ic_fa_calendar_check, true));
        academicModules.add(new TeacherModule("academics", "Academics", "fa-graduation-cap", R.drawable.ic_fa_graduation_cap, true));
        academicModules.add(new TeacherModule("library", "Library", "fa-book", R.drawable.ic_fa_book, true));
        academicModules.add(new TeacherModule("reports", "Reports", "fa-bar-chart", R.drawable.ic_fa_bar_chart, true));

        List<TeacherModule> communicationModules = new ArrayList<>();
        communicationModules.add(new TeacherModule("communicate", "Communicate", "fa-envelope", R.drawable.ic_fa_envelope, true));
        communicationModules.add(new TeacherModule("transport", "Transport", "fa-bus", R.drawable.ic_fa_bus, true));
        communicationModules.add(new TeacherModule("hostel", "Hostel", "fa-building", R.drawable.ic_fa_building, true));
        communicationModules.add(new TeacherModule("inventory", "Inventory", "fa-archive", R.drawable.ic_fa_archive, true));

        List<TeacherModule> toolsModules = new ArrayList<>();
        toolsModules.add(new TeacherModule("certificate", "Certificate", "fa-certificate", R.drawable.ic_fa_certificate, true));
        toolsModules.add(new TeacherModule("system_settings", "System Settings", "fa-cogs", R.drawable.ic_fa_cogs, true));

        setupRecyclerViews(managementModules, academicModules, communicationModules, toolsModules);
    }

    private void setupRecyclerViews(List<TeacherModule> managementModules, 
                                   List<TeacherModule> academicModules,
                                   List<TeacherModule> communicationModules, 
                                   List<TeacherModule> toolsModules) {
        
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