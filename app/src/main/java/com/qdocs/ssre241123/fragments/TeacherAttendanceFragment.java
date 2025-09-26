package com.qdocs.ssre241123.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.StudentProfileAdapter;
import com.qdocs.ssre241123.utils.CustomCalendar;
import com.qdocs.ssre241123.utils.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@SuppressLint("ValidFragment")
public class TeacherAttendanceFragment extends Fragment implements CustomCalendar.RobotoCalendarListener {

    TextView attendanceInfoTV;
    RecyclerView recyclerView;
    StudentProfileAdapter adapter;
    CustomCalendar attendanceCalendar;
    CardView calendarCard, summaryCard;

    ArrayList<String> attendanceValues = new ArrayList<>();
    HashMap<String, String> attendanceData = new HashMap<>();
    List<String> attendanceDateList = new ArrayList<>();
    HashMap<String, String> attendanceRecordMap = new HashMap<>();

    Calendar calendar;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    @SuppressLint("ValidFragment")
    public TeacherAttendanceFragment() {
    }

    public static TeacherAttendanceFragment newInstance(JSONObject teacherData) {
        TeacherAttendanceFragment fragment = new TeacherAttendanceFragment();
        Bundle args = new Bundle();
        if (teacherData != null) {
            args.putString("teacherData", teacherData.toString());
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_teacher_attendance, container, false);

        // Initialize views
        attendanceInfoTV = mainView.findViewById(R.id.teacherAttendanceInfo);
        recyclerView = mainView.findViewById(R.id.teacherAttendanceFragment_recyclerView);
        attendanceCalendar = mainView.findViewById(R.id.teacher_attendance_calendar);
        calendarCard = mainView.findViewById(R.id.calendar_card);
        summaryCard = mainView.findViewById(R.id.summary_card);

        // Initialize calendar
        calendar = Calendar.getInstance();

        // Set up calendar listener
        attendanceCalendar.setRobotoCalendarListener(this);
        attendanceCalendar.setShortWeekDays(false);
        attendanceCalendar.showDateTitle(true);
        attendanceCalendar.updateView();

        loadAttendanceData();

        // Set up UI based on data availability
        if (attendanceValues.isEmpty() && attendanceDateList.isEmpty()) {
            // No data available - show info message
            calendarCard.setVisibility(View.GONE);
            summaryCard.setVisibility(View.GONE);
            attendanceInfoTV.setVisibility(View.VISIBLE);
            attendanceInfoTV.setText("Teacher Attendance information will be displayed here.\n\n" +
                                   "This section will show:\n" +
                                   "• Calendar view with attendance status\n" +
                                   "• Daily attendance records\n" +
                                   "• Monthly attendance summary\n" +
                                   "• Attendance statistics\n" +
                                   "• Check-in/Check-out times\n\n" +
                                   "No attendance data available currently.");
        } else {
            // Data available - show calendar and summary
            attendanceInfoTV.setVisibility(View.GONE);
            calendarCard.setVisibility(View.VISIBLE);

            // Mark attendance dates on calendar
            markAttendanceDates();

            if (attendanceValues.isEmpty()) {
                summaryCard.setVisibility(View.GONE);
            } else {
                summaryCard.setVisibility(View.VISIBLE);

                // Create dynamic header array based on available attendance data
                int[] attendanceHeaderArray = new int[attendanceValues.size()];
                for (int i = 0; i < attendanceValues.size(); i++) {
                    attendanceHeaderArray[i] = R.string.attendance; // Generic attendance string
                }

                adapter = new StudentProfileAdapter(getActivity().getApplicationContext(),
                                                  attendanceHeaderArray, attendanceValues, attendanceData);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            }
        }

        return mainView;
    }

    private void loadAttendanceData() {
        attendanceValues.clear();
        attendanceData.clear();
        attendanceDateList.clear();
        attendanceRecordMap.clear();

        Log.d("TeacherAttendanceFragment", "🔍 Loading attendance data...");

        if (getArguments() != null && getArguments().getString("teacherData") != null) {
            try {
                JSONObject teacherData = new JSONObject(getArguments().getString("teacherData"));

                // Extract attendance records
                JSONObject attendanceRecords = teacherData.optJSONObject("attendance_records");
                if (attendanceRecords != null) {

                    // Process attendance summary
                    JSONObject attendanceSummary = attendanceRecords.optJSONObject("attendance_summary");
                    if (attendanceSummary != null) {
                        attendanceValues.add("=== ATTENDANCE SUMMARY ===");
                        attendanceData.put("Attendance Summary", "Monthly attendance statistics");

                        // Add each summary statistic
                        String[] summaryKeys = {"Present", "Late", "Absent", "Half Day", "Holiday"};
                        for (String key : summaryKeys) {
                            String value = attendanceSummary.optString(key, "0");
                            String summaryInfo = value + " days";
                            attendanceValues.add(summaryInfo);
                            attendanceData.put(key, summaryInfo);
                        }
                    }

                    // Process recent attendance records
                    JSONArray recentAttendance = attendanceRecords.optJSONArray("recent_attendance");
                    if (recentAttendance != null && recentAttendance.length() > 0) {
                        attendanceValues.add("=== RECENT ATTENDANCE ===");
                        attendanceData.put("Recent Attendance", "Latest attendance records");

                        Log.d("TeacherAttendanceFragment", "✅ Found " + recentAttendance.length() + " recent attendance records");

                        for (int i = 0; i < Math.min(recentAttendance.length(), 10); i++) { // Show max 10 recent records
                            JSONObject record = recentAttendance.optJSONObject(i);
                            if (record != null) {
                                String date = record.optString("date", "");
                                String status = record.optString("attendance_type", "");
                                String checkIn = record.optString("check_in", "");
                                String checkOut = record.optString("check_out", "");

                                // Add date to calendar marking list
                                if (!date.isEmpty()) {
                                    attendanceDateList.add(date);
                                    attendanceRecordMap.put(date, status + " - " + checkIn +
                                        (checkOut.isEmpty() ? "" : " to " + checkOut));

                                    Log.d("TeacherAttendanceFragment", "📅 Added attendance date: " + date + " - " + status);
                                }

                                // Format date for display
                                String formattedDate = date;
                                if (!date.isEmpty()) {
                                    formattedDate = Utility.parseDate("yyyy-MM-dd",
                                        Utility.getSharedPreferences(getActivity(), "dateFormat"),
                                        date);
                                }

                                String recordInfo = formattedDate + " - " + status;
                                if (!checkIn.isEmpty()) {
                                    recordInfo += " (In: " + checkIn;
                                    if (!checkOut.isEmpty()) {
                                        recordInfo += ", Out: " + checkOut;
                                    }
                                    recordInfo += ")";
                                }

                                attendanceValues.add(recordInfo);
                                attendanceData.put("Record " + (i + 1), recordInfo);
                            }
                        }
                    } else {
                        Log.d("TeacherAttendanceFragment", "⚠️ No recent attendance records found");
                    }

                    // Process attendance types (for reference)
                    JSONArray attendanceTypes = attendanceRecords.optJSONArray("attendance_types");
                    if (attendanceTypes != null && attendanceTypes.length() > 0) {
                        attendanceValues.add("=== ATTENDANCE TYPES ===");
                        attendanceData.put("Attendance Types", "Available attendance status types");

                        for (int i = 0; i < attendanceTypes.length(); i++) {
                            JSONObject type = attendanceTypes.optJSONObject(i);
                            if (type != null) {
                                String typeName = type.optString("type", "");
                                String keyValue = type.optString("key_value", "");
                                String isActive = type.optString("is_active", "");

                                // Clean HTML tags from key_value
                                String cleanKeyValue = keyValue.replaceAll("<[^>]*>", "");

                                String typeInfo = typeName + " (" + cleanKeyValue + ")";
                                if ("yes".equals(isActive)) {
                                    typeInfo += " - Active";
                                }

                                attendanceValues.add(typeInfo);
                                attendanceData.put(typeName, typeInfo);
                            }
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Update the fragment with new teacher attendance data
     */
    public void updateAttendanceData(JSONObject teacherData) {
        if (teacherData != null) {
            Log.d("TeacherAttendanceFragment", "Updating attendance data with new teacher data");

            // Update the arguments with new data
            Bundle args = getArguments();
            if (args == null) {
                args = new Bundle();
            }
            args.putString("teacherData", teacherData.toString());
            setArguments(args);

            // Reload attendance data with new information
            loadAttendanceData();

            // Update UI visibility and adapter
            if (attendanceValues.isEmpty() && attendanceDateList.isEmpty()) {
                // No data available
                calendarCard.setVisibility(View.GONE);
                summaryCard.setVisibility(View.GONE);
                attendanceInfoTV.setVisibility(View.VISIBLE);
            } else {
                // Data available - show calendar and summary
                attendanceInfoTV.setVisibility(View.GONE);
                calendarCard.setVisibility(View.VISIBLE);

                // Clear and re-mark calendar dates
                attendanceCalendar.clearCalendar();
                markAttendanceDates();

                if (attendanceValues.isEmpty()) {
                    summaryCard.setVisibility(View.GONE);
                } else {
                    summaryCard.setVisibility(View.VISIBLE);

                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                        Log.d("TeacherAttendanceFragment", "Attendance adapter notified of data changes");
                    }
                }
            }
        } else {
            Log.w("TeacherAttendanceFragment", "Attempted to update with null teacher data");
        }
    }

    /**
     * Mark attendance dates on the calendar
     */
    private void markAttendanceDates() {
        try {
            if (attendanceDateList.size() > 0) {
                Log.d("TeacherAttendanceFragment", "🎯 Marking " + attendanceDateList.size() + " attendance dates on calendar");

                for (String dateStr : attendanceDateList) {
                    try {
                        // Parse the date string and set it on calendar
                        String[] dateParts = dateStr.split("-");
                        if (dateParts.length == 3) {
                            int year = Integer.parseInt(dateParts[0]);
                            int month = Integer.parseInt(dateParts[1]) - 1; // Calendar months are 0-based
                            int day = Integer.parseInt(dateParts[2]);

                            Calendar markCalendar = Calendar.getInstance();
                            markCalendar.set(year, month, day);

                            // Mark the date on calendar with a circle
                            attendanceCalendar.markCircleImage1(markCalendar);

                            Log.d("TeacherAttendanceFragment", "✅ Marked date: " + dateStr);
                        }
                    } catch (Exception e) {
                        Log.e("TeacherAttendanceFragment", "Error marking date " + dateStr + ": " + e.getMessage());
                    }
                }
            } else {
                Log.d("TeacherAttendanceFragment", "⚠️ No attendance dates to mark on calendar");
            }
        } catch (Exception e) {
            Log.e("TeacherAttendanceFragment", "Error in markAttendanceDates: " + e.getMessage());
        }
    }

    // Calendar listener methods
    @Override
    public void onDayClick(Calendar daySelectedCalendar) {
        String selectedDate = dateFormatter.format(daySelectedCalendar.getTime());
        Log.d("TeacherAttendanceFragment", "📅 Calendar day clicked: " + selectedDate);

        // Check if this date has attendance record
        if (attendanceRecordMap.containsKey(selectedDate)) {
            String attendanceInfo = attendanceRecordMap.get(selectedDate);
            // You could show a dialog or toast with attendance details
            Log.d("TeacherAttendanceFragment", "📋 Attendance info for " + selectedDate + ": " + attendanceInfo);
        } else {
            Log.d("TeacherAttendanceFragment", "📋 No attendance record for " + selectedDate);
        }
    }

    @Override
    public void onDayLongClick(Calendar daySelectedCalendar) {
        // Handle long click if needed
        String selectedDate = dateFormatter.format(daySelectedCalendar.getTime());
        Log.d("TeacherAttendanceFragment", "📅 Calendar day long clicked: " + selectedDate);
    }

    @Override
    public void onRightButtonClick() {
        // Handle month navigation - could load attendance data for new month
        Log.d("TeacherAttendanceFragment", "📅 Calendar right button clicked - next month");
    }

    @Override
    public void onLeftButtonClick() {
        // Handle month navigation - could load attendance data for new month
        Log.d("TeacherAttendanceFragment", "📅 Calendar left button clicked - previous month");
    }
}
