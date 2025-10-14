package com.qdocs.ssre241123.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.model.MonthlyStaffAttendanceModel;

import java.util.List;
import java.util.Map;

/**
 * Adapter for Monthly Staff Attendance Report
 * Displays staff attendance in a monthly calendar view with daily attendance markers
 */
public class MonthlyStaffAttendanceAdapter extends RecyclerView.Adapter<MonthlyStaffAttendanceAdapter.ViewHolder> {

    private Context context;
    private List<MonthlyStaffAttendanceModel> attendanceList;
    private List<String> dates; // List of all dates in the month

    public MonthlyStaffAttendanceAdapter(Context context, List<MonthlyStaffAttendanceModel> attendanceList, List<String> dates) {
        this.context = context;
        this.attendanceList = attendanceList;
        this.dates = dates;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_monthly_staff_attendance_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MonthlyStaffAttendanceModel staff = attendanceList.get(position);

        String staffName = (staff.getStaffInfo() != null) ? staff.getStaffInfo().getFullName() : "Unknown";
        android.util.Log.d("MonthlyStaffAdapter", "=== Binding staff at position " + position + ": " + staffName + " ===");

        // Staff Information
        if (staff.getStaffInfo() != null) {
            holder.staffNameTv.setText(staff.getStaffInfo().getFullName());
            holder.employeeIdTv.setText("ID: " + staff.getStaffInfo().getEmployeeId());
            holder.roleTv.setText(staff.getStaffInfo().getRole());
            android.util.Log.d("MonthlyStaffAdapter", "Staff Info - Name: " + staff.getStaffInfo().getFullName() +
                              ", ID: " + staff.getStaffInfo().getEmployeeId() +
                              ", Role: " + staff.getStaffInfo().getRole());
        } else {
            android.util.Log.e("MonthlyStaffAdapter", "Staff info is NULL!");
        }

        // Attendance Percentage
        holder.percentageTv.setText(staff.getAttendancePercentageDisplay() + "%");
        android.util.Log.d("MonthlyStaffAdapter", "Attendance Percentage: " + staff.getAttendancePercentageDisplay() + "%");

        // Set percentage color based on status
        int percentageColor;
        switch (staff.getAttendanceStatusClass()) {
            case "success":
                percentageColor = Color.parseColor("#28a745"); // Green
                break;
            case "danger":
                percentageColor = Color.parseColor("#dc3545"); // Red
                break;
            default:
                percentageColor = Color.parseColor("#6c757d"); // Gray
                break;
        }
        holder.percentageTv.setTextColor(percentageColor);
        holder.statusTv.setText(staff.getAttendanceStatus());
        holder.statusTv.setTextColor(percentageColor);
        android.util.Log.d("MonthlyStaffAdapter", "Status: " + staff.getAttendanceStatus() +
                          " (Class: " + staff.getAttendanceStatusClass() + ")");

        // Attendance Summary
        if (staff.getAttendanceSummary() != null) {
            holder.presentTv.setText("P: " + staff.getAttendanceSummary().getPresent());
            holder.absentTv.setText("A: " + staff.getAttendanceSummary().getAbsent());
            holder.lateTv.setText("L: " + staff.getAttendanceSummary().getLate());
            holder.halfDayTv.setText("H: " + staff.getAttendanceSummary().getHalfDay());
            holder.holidayTv.setText("HD: " + staff.getAttendanceSummary().getHoliday());
            android.util.Log.d("MonthlyStaffAdapter", "Summary - P: " + staff.getAttendanceSummary().getPresent() +
                              ", A: " + staff.getAttendanceSummary().getAbsent() +
                              ", L: " + staff.getAttendanceSummary().getLate() +
                              ", H: " + staff.getAttendanceSummary().getHalfDay() +
                              ", HD: " + staff.getAttendanceSummary().getHoliday());
        } else {
            holder.presentTv.setText("P: 0");
            holder.absentTv.setText("A: 0");
            holder.lateTv.setText("L: 0");
            holder.halfDayTv.setText("H: 0");
            holder.holidayTv.setText("HD: 0");
            android.util.Log.w("MonthlyStaffAdapter", "Attendance summary is NULL!");
        }

        // Working days info
        holder.workingDaysTv.setText("Working Days: " + staff.getTotalWorkingDays() +
                                    " | Present: " + staff.getTotalPresentDays());
        android.util.Log.d("MonthlyStaffAdapter", "Working Days: " + staff.getTotalWorkingDays() +
                          ", Present Days: " + staff.getTotalPresentDays());

        // Clear previous daily attendance views
        holder.dailyAttendanceContainer.removeAllViews();

        // Add ALL daily attendance markers in horizontal scroll
        Map<String, MonthlyStaffAttendanceModel.DailyAttendance> dailyMap = staff.getDailyAttendance();

        // Safe logging with null check
        android.util.Log.d("MonthlyStaffAdapter", "Creating day views for staff: " + staffName);
        android.util.Log.d("MonthlyStaffAdapter", "Dates list size: " + dates.size());
        android.util.Log.d("MonthlyStaffAdapter", "Daily map size: " + (dailyMap != null ? dailyMap.size() : 0));
        
        if (dailyMap == null) {
            android.util.Log.e("MonthlyStaffAdapter", "Daily attendance map is NULL!");
            return;
        }
        
        if (dates.isEmpty()) {
            android.util.Log.e("MonthlyStaffAdapter", "Dates list is EMPTY!");
            return;
        }
        
        for (int i = 0; i < dates.size(); i++) {
            String date = dates.get(i);
            MonthlyStaffAttendanceModel.DailyAttendance dayAttendance = dailyMap.get(date);
            
            android.util.Log.d("MonthlyStaffAdapter", "Date: " + date + " | Has attendance: " + (dayAttendance != null));
            if (dayAttendance != null) {
                android.util.Log.d("MonthlyStaffAdapter", "  Type: " + dayAttendance.getAttendanceType() + " | Key: " + dayAttendance.getAttendanceKey());
            }
            
            View dayView = createDayView(date, dayAttendance);
            holder.dailyAttendanceContainer.addView(dayView);
        }
        
        android.util.Log.d("MonthlyStaffAdapter", "Total day views added: " + holder.dailyAttendanceContainer.getChildCount());
        
        // Add click listener to show full calendar popup
        holder.cardView.setOnClickListener(v -> showFullCalendarDialog(staff));
    }

    /**
     * Create a view for a single day's attendance
     */
    private View createDayView(String date, MonthlyStaffAttendanceModel.DailyAttendance dayAttendance) {
        LinearLayout dayLayout = new LinearLayout(context);
        dayLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(4, 2, 4, 2);
        dayLayout.setLayoutParams(params);
        dayLayout.setPadding(8, 4, 8, 4);

        // Day number (extract from date YYYY-MM-DD)
        String dayNumber = date.substring(8, 10);
        TextView dayTv = new TextView(context);
        dayTv.setText(dayNumber);
        dayTv.setTextSize(10);
        dayTv.setTextColor(Color.parseColor("#333333"));
        dayTv.setGravity(android.view.Gravity.CENTER);
        dayLayout.addView(dayTv);

        // Attendance marker
        TextView markerTv = new TextView(context);
        markerTv.setTextSize(14);
        markerTv.setTextColor(Color.parseColor("#000000"));
        markerTv.setGravity(android.view.Gravity.CENTER);
        markerTv.setPadding(4, 2, 4, 2);
        markerTv.setMinWidth(30);
        markerTv.setMinHeight(30);
        
        if (dayAttendance != null) {
            // Parse HTML key (e.g., <b class='text text-success'>P</b>)
            String htmlKey = dayAttendance.getAttendanceKey();
            android.util.Log.d("MonthlyStaffAdapter", "Day " + dayNumber + " htmlKey: " + htmlKey);
            
            if (htmlKey != null && !htmlKey.isEmpty() && !htmlKey.equals("-")) {
                // Extract plain text from HTML
                String plainKey = htmlKey.replaceAll("<[^>]*>", "").trim();
                markerTv.setText(plainKey);
                android.util.Log.d("MonthlyStaffAdapter", "Day " + dayNumber + " marker: " + plainKey);
            } else {
                markerTv.setText("-");
            }
            
            // Set background color based on attendance type
            String attendanceType = dayAttendance.getAttendanceType();
            int bgColor = Color.parseColor("#EEEEEE"); // Default gray
            
            if (attendanceType != null && !attendanceType.equals("Not Marked")) {
                switch (attendanceType) {
                    case "Present":
                        bgColor = Color.parseColor("#D4EDDA"); // Light green
                        break;
                    case "Absent":
                        bgColor = Color.parseColor("#F8D7DA"); // Light red
                        break;
                    case "Late":
                        bgColor = Color.parseColor("#FFF3CD"); // Light yellow
                        break;
                    case "Half Day":
                        bgColor = Color.parseColor("#D1ECF1"); // Light blue
                        break;
                    case "Holiday":
                        bgColor = Color.parseColor("#E2E3E5"); // Light gray
                        break;
                }
            }
            markerTv.setBackgroundColor(bgColor);
        } else {
            markerTv.setText("-");
            markerTv.setBackgroundColor(Color.parseColor("#EEEEEE")); // Gray for no data
        }
        
        dayLayout.addView(markerTv);
        return dayLayout;
    }

    /**
     * Show full calendar popup dialog with all attendance details
     */
    private void showFullCalendarDialog(MonthlyStaffAttendanceModel staff) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_monthly_calendar);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        
        // Staff information
        TextView dialogStaffName = dialog.findViewById(R.id.dialogStaffName);
        TextView dialogEmployeeId = dialog.findViewById(R.id.dialogEmployeeId);
        TextView dialogRole = dialog.findViewById(R.id.dialogRole);
        TextView dialogPercentage = dialog.findViewById(R.id.dialogPercentage);
        TextView dialogStatus = dialog.findViewById(R.id.dialogStatus);
        
        // Attendance summary
        TextView dialogPresent = dialog.findViewById(R.id.dialogPresent);
        TextView dialogAbsent = dialog.findViewById(R.id.dialogAbsent);
        TextView dialogLate = dialog.findViewById(R.id.dialogLate);
        TextView dialogHalfDay = dialog.findViewById(R.id.dialogHalfDay);
        TextView dialogHoliday = dialog.findViewById(R.id.dialogHoliday);
        
        // Calendar grid
        GridLayout calendarGrid = dialog.findViewById(R.id.calendarGrid);
        
        // Set staff info
        if (staff.getStaffInfo() != null) {
            dialogStaffName.setText(staff.getStaffInfo().getFullName());
            dialogEmployeeId.setText("Employee ID: " + staff.getStaffInfo().getEmployeeId());
            dialogRole.setText("Role: " + staff.getStaffInfo().getRole());
        }
        
        // Set percentage and status
        dialogPercentage.setText(staff.getAttendancePercentageDisplay() + "%");
        dialogStatus.setText(staff.getAttendanceStatus());
        
        int percentageColor;
        switch (staff.getAttendanceStatusClass()) {
            case "success":
                percentageColor = Color.parseColor("#28a745"); // Green
                break;
            case "danger":
                percentageColor = Color.parseColor("#dc3545"); // Red
                break;
            default:
                percentageColor = Color.parseColor("#6c757d"); // Gray
                break;
        }
        dialogPercentage.setTextColor(percentageColor);
        dialogStatus.setTextColor(percentageColor);
        
        // Set summary
        if (staff.getAttendanceSummary() != null) {
            dialogPresent.setText("Present: " + staff.getAttendanceSummary().getPresent());
            dialogAbsent.setText("Absent: " + staff.getAttendanceSummary().getAbsent());
            dialogLate.setText("Late: " + staff.getAttendanceSummary().getLate());
            dialogHalfDay.setText("Half Day: " + staff.getAttendanceSummary().getHalfDay());
            dialogHoliday.setText("Holiday: " + staff.getAttendanceSummary().getHoliday());
        } else {
            dialogPresent.setText("Present: 0");
            dialogAbsent.setText("Absent: 0");
            dialogLate.setText("Late: 0");
            dialogHalfDay.setText("Half Day: 0");
            dialogHoliday.setText("Holiday: 0");
        }
        
        // Populate calendar grid (7 columns for days of week)
        calendarGrid.setColumnCount(7);
        Map<String, MonthlyStaffAttendanceModel.DailyAttendance> dailyMap = staff.getDailyAttendance();
        
        // Add day headers
        String[] dayHeaders = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (String header : dayHeaders) {
            TextView headerTv = new TextView(context);
            headerTv.setText(header);
            headerTv.setTextSize(12);
            headerTv.setGravity(android.view.Gravity.CENTER);
            headerTv.setPadding(8, 8, 8, 8);
            headerTv.setTextColor(Color.parseColor("#333333"));
            calendarGrid.addView(headerTv);
        }
        
        // Add empty cells for offset (to align first day correctly)
        if (dates.size() > 0) {
            String firstDate = dates.get(0);
            MonthlyStaffAttendanceModel.DailyAttendance firstDay = dailyMap.get(firstDate);
            if (firstDay != null) {
                int dayOffset = getDayOffset(firstDay.getDayShort());
                for (int i = 0; i < dayOffset; i++) {
                    View emptyView = new View(context);
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = 0;
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                    emptyView.setLayoutParams(params);
                    calendarGrid.addView(emptyView);
                }
            }
        }
        
        // Add all days
        for (String date : dates) {
            MonthlyStaffAttendanceModel.DailyAttendance dayAttendance = dailyMap.get(date);
            View dayView = createCalendarDayView(date, dayAttendance);
            calendarGrid.addView(dayView);
        }
        
        // Close button
        dialog.findViewById(R.id.closeButton).setOnClickListener(v -> dialog.dismiss());
        
        dialog.show();
    }
    
    /**
     * Get day offset for calendar grid (0 = Monday, 6 = Sunday)
     */
    private int getDayOffset(String dayShort) {
        switch (dayShort.toLowerCase()) {
            case "mon": return 0;
            case "tue": return 1;
            case "wed": return 2;
            case "thu": return 3;
            case "fri": return 4;
            case "sat": return 5;
            case "sun": return 6;
            default: return 0;
        }
    }
    
    /**
     * Create a calendar day view for the popup dialog
     */
    private View createCalendarDayView(String date, MonthlyStaffAttendanceModel.DailyAttendance dayAttendance) {
        LinearLayout dayLayout = new LinearLayout(context);
        dayLayout.setOrientation(LinearLayout.VERTICAL);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.setMargins(4, 4, 4, 4);
        dayLayout.setLayoutParams(params);
        dayLayout.setPadding(8, 8, 8, 8);
        
        // Day number
        String dayNumber = date.substring(8, 10);
        TextView dayTv = new TextView(context);
        dayTv.setText(dayNumber);
        dayTv.setTextSize(12);
        dayTv.setGravity(android.view.Gravity.CENTER);
        dayTv.setTextColor(Color.parseColor("#333333"));
        dayLayout.addView(dayTv);
        
        // Attendance marker
        TextView markerTv = new TextView(context);
        markerTv.setTextSize(16);
        markerTv.setGravity(android.view.Gravity.CENTER);
        markerTv.setPadding(4, 4, 4, 4);
        
        if (dayAttendance != null) {
            String attendanceKey = dayAttendance.getAttendanceKey();
            if (attendanceKey != null && !attendanceKey.isEmpty() && !attendanceKey.equals("-")) {
                // Extract just the letter from HTML (P, L, A, F, H)
                String cleanKey = attendanceKey.replaceAll("<[^>]*>", "").trim();
                markerTv.setText(cleanKey);
            } else {
                markerTv.setText("-");
            }
            
            // Set background color
            String attendanceType = dayAttendance.getAttendanceType();
            int bgColor = Color.parseColor("#EEEEEE");
            
            if (attendanceType != null && !attendanceType.equals("Not Marked")) {
                switch (attendanceType) {
                    case "Present":
                        bgColor = Color.parseColor("#D4EDDA"); // Light green
                        break;
                    case "Absent":
                        bgColor = Color.parseColor("#F8D7DA"); // Light red
                        break;
                    case "Late":
                        bgColor = Color.parseColor("#FFF3CD"); // Light yellow
                        break;
                    case "Half Day":
                        bgColor = Color.parseColor("#D1ECF1"); // Light blue
                        break;
                    case "Holiday":
                        bgColor = Color.parseColor("#E2E3E5"); // Light gray
                        break;
                }
            }
            dayLayout.setBackgroundColor(bgColor);
        } else {
            markerTv.setText("-");
            dayLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        
        dayLayout.addView(markerTv);
        
        // Add day name (short)
        if (dayAttendance != null) {
            TextView dayNameTv = new TextView(context);
            dayNameTv.setText(dayAttendance.getDayShort());
            dayNameTv.setTextSize(9);
            dayNameTv.setGravity(android.view.Gravity.CENTER);
            dayNameTv.setTextColor(Color.parseColor("#666666"));
            dayLayout.addView(dayNameTv);
        }
        
        return dayLayout;
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public void updateData(List<MonthlyStaffAttendanceModel> newList, List<String> newDates) {
        this.attendanceList = newList;
        this.dates = newDates;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView staffNameTv, employeeIdTv, roleTv;
        TextView percentageTv, statusTv;
        TextView presentTv, absentTv, lateTv, halfDayTv, holidayTv;
        TextView workingDaysTv;
        LinearLayout dailyAttendanceContainer;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            staffNameTv = itemView.findViewById(R.id.staffNameTv);
            employeeIdTv = itemView.findViewById(R.id.employeeIdTv);
            roleTv = itemView.findViewById(R.id.roleTv);
            percentageTv = itemView.findViewById(R.id.percentageTv);
            statusTv = itemView.findViewById(R.id.statusTv);
            presentTv = itemView.findViewById(R.id.presentTv);
            absentTv = itemView.findViewById(R.id.absentTv);
            lateTv = itemView.findViewById(R.id.lateTv);
            halfDayTv = itemView.findViewById(R.id.halfDayTv);
            holidayTv = itemView.findViewById(R.id.holidayTv);
            workingDaysTv = itemView.findViewById(R.id.workingDaysTv);
            dailyAttendanceContainer = itemView.findViewById(R.id.dailyAttendanceContainer);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
