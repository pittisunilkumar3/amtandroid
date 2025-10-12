package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.model.ClassAttendanceReportModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import java.util.List;

public class ClassAttendanceReportAdapter extends RecyclerView.Adapter<ClassAttendanceReportAdapter.AttendanceViewHolder> {

    private Context context;
    private List<ClassAttendanceReportModel> attendanceList;

    public ClassAttendanceReportAdapter(Context context, List<ClassAttendanceReportModel> attendanceList) {
        this.context = context;
        this.attendanceList = attendanceList;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_class_attendance_item, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        ClassAttendanceReportModel attendance = attendanceList.get(position);

        // Set student information
        String fullName = attendance.getStudentName();
        if (fullName != null && !fullName.trim().isEmpty()) {
            holder.studentNameTv.setText(fullName.trim());
        } else {
            holder.studentNameTv.setText("Unknown Student");
        }

        // Set admission number
        if (attendance.getAdmissionNo() != null && !attendance.getAdmissionNo().isEmpty()) {
            holder.admissionNoTv.setText("Admission No: " + attendance.getAdmissionNo());
            holder.admissionNoTv.setVisibility(View.VISIBLE);
        } else {
            holder.admissionNoTv.setVisibility(View.GONE);
        }

        // Set class and section
        String classSection = "";
        if (attendance.getClassName() != null && !attendance.getClassName().isEmpty()) {
            classSection = attendance.getClassName();
        }
        if (attendance.getSectionName() != null && !attendance.getSectionName().isEmpty()) {
            if (!classSection.isEmpty()) {
                classSection += " - " + attendance.getSectionName();
            } else {
                classSection = attendance.getSectionName();
            }
        }
        if (classSection.isEmpty()) {
            classSection = "Unknown Class";
        }
        holder.classSectionTv.setText(classSection);

        // Set attendance counts
        holder.presentCountTv.setText(attendance.getPresentCount() != null ? attendance.getPresentCount() : "0");
        holder.excuseCountTv.setText(attendance.getExcuseCount() != null ? attendance.getExcuseCount() : "0");
        holder.lateCountTv.setText(attendance.getLateCount() != null ? attendance.getLateCount() : "0");
        holder.absentCountTv.setText(attendance.getAbsentCount() != null ? attendance.getAbsentCount() : "0");

        // Set attendance percentage
        String percentage = attendance.getPresentPercentage();
        if (percentage != null && !percentage.isEmpty()) {
            holder.attendancePercentageTv.setText(percentage);
            
            // Color code based on percentage
            try {
                double percentValue = Double.parseDouble(percentage.replace("%", ""));
                if (percentValue >= 90) {
                    holder.attendancePercentageTv.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#4CAF50"))); // Green
                } else if (percentValue >= 75) {
                    holder.attendancePercentageTv.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#FF9800"))); // Orange
                } else {
                    holder.attendancePercentageTv.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#F44336"))); // Red
                }
            } catch (Exception e) {
                holder.attendancePercentageTv.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#9E9E9E"))); // Gray
            }
        } else {
            holder.attendancePercentageTv.setText("0%");
            holder.attendancePercentageTv.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#9E9E9E"))); // Gray
        }

        // Set total days
        holder.totalDaysTv.setText(String.valueOf(attendance.getTotalDays()));

        // Set gender
        if (holder.genderTv != null) {
            String gender = attendance.getGender();
            if (gender != null && !gender.isEmpty() && !gender.equals("null")) {
                holder.genderTv.setText(gender);
            } else {
                holder.genderTv.setText("N/A");
            }
        }

        // Apply theme colors
        String primaryColor = Utility.getSharedPreferences(context, Constants.primaryColour);
        if (primaryColor != null && !primaryColor.isEmpty()) {
            try {
                // Apply primary color to icons and certain text elements
                // This can be implemented if specific theming is needed
            } catch (Exception e) {
                // Use default colors if parsing fails
            }
        }
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        TextView studentNameTv;
        TextView admissionNoTv;
        TextView classSectionTv;
        TextView attendancePercentageTv;
        TextView presentCountTv;
        TextView excuseCountTv;
        TextView lateCountTv;
        TextView absentCountTv;
        TextView totalDaysTv;
        TextView genderTv;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            studentNameTv = itemView.findViewById(R.id.studentNameTv);
            admissionNoTv = itemView.findViewById(R.id.admissionNoTv);
            classSectionTv = itemView.findViewById(R.id.classSectionTv);
            attendancePercentageTv = itemView.findViewById(R.id.attendancePercentageTv);
            presentCountTv = itemView.findViewById(R.id.presentCountTv);
            excuseCountTv = itemView.findViewById(R.id.excuseCountTv);
            lateCountTv = itemView.findViewById(R.id.lateCountTv);
            absentCountTv = itemView.findViewById(R.id.absentCountTv);
            totalDaysTv = itemView.findViewById(R.id.totalDaysTv);
            genderTv = itemView.findViewById(R.id.genderTv);
        }
    }
}
