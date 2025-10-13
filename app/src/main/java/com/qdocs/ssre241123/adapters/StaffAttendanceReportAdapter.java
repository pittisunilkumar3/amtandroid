package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.model.StaffAttendanceReportModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying Staff Attendance Report in RecyclerView
 */
public class StaffAttendanceReportAdapter extends RecyclerView.Adapter<StaffAttendanceReportAdapter.ViewHolder> {

    private Context context;
    private List<StaffAttendanceReportModel> attendanceList;

    public StaffAttendanceReportAdapter(Context context, List<StaffAttendanceReportModel> attendanceList) {
        this.context = context;
        this.attendanceList = attendanceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_staff_attendance_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StaffAttendanceReportModel attendance = attendanceList.get(position);

        // Staff Name
        holder.staffNameTv.setText(attendance.getFullName());

        // Employee ID
        if (attendance.getEmployeeId() != null && !attendance.getEmployeeId().isEmpty()) {
            holder.employeeIdTv.setText("ID: " + attendance.getEmployeeId());
            holder.employeeIdTv.setVisibility(View.VISIBLE);
        } else {
            holder.employeeIdTv.setVisibility(View.GONE);
        }

        // Department
        if (attendance.getDepartment() != null && !attendance.getDepartment().isEmpty()) {
            holder.departmentTv.setText(attendance.getDepartment());
            holder.departmentTv.setVisibility(View.VISIBLE);
        } else {
            holder.departmentTv.setVisibility(View.GONE);
        }

        // Designation
        if (attendance.getDesignation() != null && !attendance.getDesignation().isEmpty()) {
            holder.designationTv.setText(attendance.getDesignation());
            holder.designationTv.setVisibility(View.VISIBLE);
        } else {
            holder.designationTv.setVisibility(View.GONE);
        }

        // Role
        if (attendance.getRole() != null && !attendance.getRole().isEmpty()) {
            holder.roleTv.setText(attendance.getRole());
            holder.roleTv.setVisibility(View.VISIBLE);
        } else {
            holder.roleTv.setVisibility(View.GONE);
        }

        // Date
        holder.dateTv.setText(formatDate(attendance.getDate()));

        // Attendance Type with color coding
        String attendanceType = attendance.getAttendanceType();
        holder.attendanceTypeTv.setText(attendanceType);
        
        // Set color based on attendance type
        if (attendanceType != null) {
            switch (attendanceType.toLowerCase()) {
                case "present":
                    holder.attendanceTypeTv.setTextColor(Color.parseColor("#4CAF50")); // Green
                    holder.attendanceTypeCard.setCardBackgroundColor(Color.parseColor("#E8F5E9"));
                    break;
                case "absent":
                    holder.attendanceTypeTv.setTextColor(Color.parseColor("#F44336")); // Red
                    holder.attendanceTypeCard.setCardBackgroundColor(Color.parseColor("#FFEBEE"));
                    break;
                case "late":
                    holder.attendanceTypeTv.setTextColor(Color.parseColor("#FF9800")); // Orange
                    holder.attendanceTypeCard.setCardBackgroundColor(Color.parseColor("#FFF3E0"));
                    break;
                case "half_day":
                case "half day":
                    holder.attendanceTypeTv.setTextColor(Color.parseColor("#2196F3")); // Blue
                    holder.attendanceTypeCard.setCardBackgroundColor(Color.parseColor("#E3F2FD"));
                    break;
                default:
                    holder.attendanceTypeTv.setTextColor(Color.parseColor("#757575")); // Gray
                    holder.attendanceTypeCard.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
                    break;
            }
        }

        // Remark
        if (attendance.getRemark() != null && !attendance.getRemark().isEmpty()) {
            holder.remarkTv.setText("Remark: " + attendance.getRemark());
            holder.remarkTv.setVisibility(View.VISIBLE);
        } else {
            holder.remarkTv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    /**
     * Format date from YYYY-MM-DD to DD MMM YYYY
     */
    private String formatDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return "";
        }

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            Date date = inputFormat.parse(dateStr);
            return outputFormat.format(date);
        } catch (ParseException e) {
            return dateStr;
        }
    }

    /**
     * Update the adapter data
     */
    public void updateData(List<StaffAttendanceReportModel> newList) {
        this.attendanceList = newList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView staffNameTv, employeeIdTv, departmentTv, designationTv, roleTv;
        TextView dateTv, attendanceTypeTv, remarkTv;
        CardView attendanceTypeCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            staffNameTv = itemView.findViewById(R.id.staffNameTv);
            employeeIdTv = itemView.findViewById(R.id.employeeIdTv);
            departmentTv = itemView.findViewById(R.id.departmentTv);
            designationTv = itemView.findViewById(R.id.designationTv);
            roleTv = itemView.findViewById(R.id.roleTv);
            dateTv = itemView.findViewById(R.id.dateTv);
            attendanceTypeTv = itemView.findViewById(R.id.attendanceTypeTv);
            remarkTv = itemView.findViewById(R.id.remarkTv);
            attendanceTypeCard = itemView.findViewById(R.id.attendanceTypeCard);
        }
    }
}
