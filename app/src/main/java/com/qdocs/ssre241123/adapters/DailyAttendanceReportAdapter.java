package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.model.DailyAttendanceReportModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import java.util.List;

/**
 * Adapter for displaying Daily Attendance Report records
 */
public class DailyAttendanceReportAdapter extends RecyclerView.Adapter<DailyAttendanceReportAdapter.ViewHolder> {
    
    private Context context;
    private List<DailyAttendanceReportModel> attendanceList;
    
    public DailyAttendanceReportAdapter(Context context, List<DailyAttendanceReportModel> attendanceList) {
        this.context = context;
        this.attendanceList = attendanceList;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_daily_attendance_report, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DailyAttendanceReportModel attendance = attendanceList.get(position);
        
        // Set class and section name
        holder.classSectionTv.setText(attendance.getClassSectionDisplay());
        
        // Set total students
        holder.totalStudentsTv.setText(attendance.getTotalStudent());
        
        // Set attendance counts
        holder.presentCountTv.setText(attendance.getPresent());
        holder.excuseCountTv.setText(attendance.getExcuse());
        holder.lateCountTv.setText(attendance.getLate());
        holder.halfDayCountTv.setText(attendance.getHalfDay());
        holder.absentCountTv.setText(attendance.getAbsent());
        
        // Set total present
        holder.totalPresentTv.setText(attendance.getTotalPresent());
        
        // Set percentages
        holder.presentPercentTv.setText(attendance.getPresentPercent());
        holder.absentPercentTv.setText(attendance.getAbsentPercent());
        
        // Set progress bar
        int presentPercent = attendance.getPresentPercentageInt();
        holder.attendanceProgressBar.setProgress(presentPercent);
        
        // Apply color based on attendance percentage
        String primaryColor = Utility.getSharedPreferences(context, Constants.primaryColour);
        if (presentPercent >= 90) {
            // Excellent attendance - green
            holder.attendanceProgressBar.getProgressDrawable().setColorFilter(
                Color.parseColor("#4CAF50"), android.graphics.PorterDuff.Mode.SRC_IN);
            holder.presentPercentTv.setTextColor(Color.parseColor("#4CAF50"));
        } else if (presentPercent >= 75) {
            // Good attendance - orange
            holder.attendanceProgressBar.getProgressDrawable().setColorFilter(
                Color.parseColor("#FF9800"), android.graphics.PorterDuff.Mode.SRC_IN);
            holder.presentPercentTv.setTextColor(Color.parseColor("#FF9800"));
        } else {
            // Poor attendance - red
            holder.attendanceProgressBar.getProgressDrawable().setColorFilter(
                Color.parseColor("#F44336"), android.graphics.PorterDuff.Mode.SRC_IN);
            holder.presentPercentTv.setTextColor(Color.parseColor("#F44336"));
        }
        
        // Apply theme color to card
        if (primaryColor != null && !primaryColor.isEmpty()) {
            try {
                int color = Color.parseColor(primaryColor);
                // Apply subtle tint to the card
            } catch (Exception e) {
                // Use default colors if parsing fails
            }
        }
    }
    
    @Override
    public int getItemCount() {
        return attendanceList.size();
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView classSectionTv;
        TextView totalStudentsTv;
        TextView presentCountTv;
        TextView excuseCountTv;
        TextView lateCountTv;
        TextView halfDayCountTv;
        TextView absentCountTv;
        TextView totalPresentTv;
        TextView presentPercentTv;
        TextView absentPercentTv;
        ProgressBar attendanceProgressBar;
        
        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            classSectionTv = itemView.findViewById(R.id.classSectionTv);
            totalStudentsTv = itemView.findViewById(R.id.totalStudentsTv);
            presentCountTv = itemView.findViewById(R.id.presentCountTv);
            excuseCountTv = itemView.findViewById(R.id.excuseCountTv);
            lateCountTv = itemView.findViewById(R.id.lateCountTv);
            halfDayCountTv = itemView.findViewById(R.id.halfDayCountTv);
            absentCountTv = itemView.findViewById(R.id.absentCountTv);
            totalPresentTv = itemView.findViewById(R.id.totalPresentTv);
            presentPercentTv = itemView.findViewById(R.id.presentPercentTv);
            absentPercentTv = itemView.findViewById(R.id.absentPercentTv);
            attendanceProgressBar = itemView.findViewById(R.id.attendanceProgressBar);
        }
    }
}

