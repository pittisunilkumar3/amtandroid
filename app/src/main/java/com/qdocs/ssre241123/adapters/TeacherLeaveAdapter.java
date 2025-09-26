package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.models.TeacherLeaveRecord;
import com.qdocs.ssre241123.utils.Utility;
import java.util.List;

public class TeacherLeaveAdapter extends RecyclerView.Adapter<TeacherLeaveAdapter.LeaveViewHolder> {

    private Context context;
    private List<TeacherLeaveRecord> leaveRecords;
    private OnLeaveItemClickListener listener;

    public interface OnLeaveItemClickListener {
        void onViewLeaveClick(TeacherLeaveRecord record, int position);
    }

    public TeacherLeaveAdapter(Context context, List<TeacherLeaveRecord> leaveRecords) {
        this.context = context;
        this.leaveRecords = leaveRecords;
    }

    public void setOnLeaveItemClickListener(OnLeaveItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public LeaveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_teacher_leave, parent, false);
        return new LeaveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveViewHolder holder, int position) {
        TeacherLeaveRecord record = leaveRecords.get(position);
        
        // Set leave type
        holder.leaveTypeTV.setText(record.getType());
        
        // Set leave date range
        String dateRange = formatDateRange(record.getLeaveFrom(), record.getLeaveTo());
        holder.leaveDateRangeTV.setText(dateRange);
        
        // Set number of days
        holder.leaveDaysTV.setText(record.getLeaveDays());
        
        // Set apply date
        String applyDate = formatDate(record.getDate());
        holder.leaveApplyDateTV.setText(applyDate);
        
        // Set employee reason
        String reason = record.getEmployeeRemark();
        if (reason == null || reason.isEmpty()) {
            reason = "No reason provided";
        }
        holder.leaveReasonTV.setText(reason);
        
        // Set status with appropriate background
        String status = record.getStatus();
        holder.leaveStatusTV.setText(capitalizeFirst(status));
        setStatusBackground(holder.leaveStatusTV, status);
        
        // Handle admin remark
        String adminRemark = record.getAdminRemark();
        if (adminRemark != null && !adminRemark.isEmpty() && !adminRemark.equals("null")) {
            holder.adminRemarkLayout.setVisibility(View.VISIBLE);
            holder.leaveAdminRemarkTV.setText(adminRemark);
        } else {
            holder.adminRemarkLayout.setVisibility(View.GONE);
        }
        
        // Set click listener for view button
        holder.viewButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onViewLeaveClick(record, position);
            }
        });
        
        // Set click listener for entire item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onViewLeaveClick(record, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return leaveRecords != null ? leaveRecords.size() : 0;
    }

    private String formatDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return "N/A";
        }
        
        try {
            // Try to format the date using utility method
            return Utility.parseDate("yyyy-MM-dd", 
                Utility.getSharedPreferences(context, "dateFormat"), 
                dateString);
        } catch (Exception e) {
            // If parsing fails, return the original string
            return dateString;
        }
    }

    private String formatDateRange(String fromDate, String toDate) {
        if (fromDate == null || fromDate.isEmpty()) {
            return "N/A";
        }
        
        String formattedFromDate = formatDate(fromDate);
        
        if (toDate == null || toDate.isEmpty() || fromDate.equals(toDate)) {
            return formattedFromDate;
        }
        
        String formattedToDate = formatDate(toDate);
        return formattedFromDate + " - " + formattedToDate;
    }

    private String capitalizeFirst(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    private void setStatusBackground(TextView statusTV, String status) {
        int backgroundColor;
        
        switch (status.toLowerCase()) {
            case "approved":
                backgroundColor = ContextCompat.getColor(context, R.color.status_approved);
                break;
            case "pending":
                backgroundColor = ContextCompat.getColor(context, R.color.status_pending);
                break;
            case "rejected":
            case "disapproved":
                backgroundColor = ContextCompat.getColor(context, R.color.status_rejected);
                break;
            default:
                backgroundColor = ContextCompat.getColor(context, R.color.status_pending);
                break;
        }
        
        // Create rounded background
        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.RECTANGLE);
        background.setColor(backgroundColor);
        background.setCornerRadius(12f);
        
        statusTV.setBackground(background);
    }

    public static class LeaveViewHolder extends RecyclerView.ViewHolder {
        TextView leaveTypeTV, leaveStatusTV, leaveDateRangeTV, leaveDaysTV;
        TextView leaveApplyDateTV, leaveReasonTV, leaveAdminRemarkTV;
        LinearLayout adminRemarkLayout;
        Button viewButton;

        public LeaveViewHolder(@NonNull View itemView) {
            super(itemView);
            leaveTypeTV = itemView.findViewById(R.id.leave_type);
            leaveStatusTV = itemView.findViewById(R.id.leave_status);
            leaveDateRangeTV = itemView.findViewById(R.id.leave_date_range);
            leaveDaysTV = itemView.findViewById(R.id.leave_days);
            leaveApplyDateTV = itemView.findViewById(R.id.leave_apply_date);
            leaveReasonTV = itemView.findViewById(R.id.leave_reason);
            leaveAdminRemarkTV = itemView.findViewById(R.id.leave_admin_remark);
            adminRemarkLayout = itemView.findViewById(R.id.admin_remark_layout);
            viewButton = itemView.findViewById(R.id.leave_view_button);
        }
    }

    public void updateData(List<TeacherLeaveRecord> newRecords) {
        this.leaveRecords = newRecords;
        notifyDataSetChanged();
    }
}
