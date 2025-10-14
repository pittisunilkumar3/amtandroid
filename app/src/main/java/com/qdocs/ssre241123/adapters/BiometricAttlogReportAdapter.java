package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.model.BiometricAttlogReportModel;

import java.util.List;

/**
 * Adapter for displaying Biometric Attendance Log Report in RecyclerView
 */
public class BiometricAttlogReportAdapter extends RecyclerView.Adapter<BiometricAttlogReportAdapter.ViewHolder> {

    private Context context;
    private List<BiometricAttlogReportModel> attlogList;

    public BiometricAttlogReportAdapter(Context context, List<BiometricAttlogReportModel> attlogList) {
        this.context = context;
        this.attlogList = attlogList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_biometric_attlog_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BiometricAttlogReportModel attlog = attlogList.get(position);

        // Set student name
        holder.studentNameTv.setText(attlog.getFullName());

        // Set attendance status with color
        holder.attendanceStatusTv.setText(attlog.getAttendanceTypeName());
        holder.attendanceStatusTv.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(attlog.getAttendanceTypeColor())
        );

        // Set admission number
        holder.admissionNoTv.setText(attlog.getAdmissionNo() != null ? attlog.getAdmissionNo() : "-");

        // Set roll number
        holder.rollNoTv.setText(attlog.getRollNo() != null ? attlog.getRollNo() : "-");

        // Set class
        holder.classTv.setText(attlog.getClassName() != null ? attlog.getClassName() : "-");

        // Set section
        holder.sectionTv.setText(attlog.getSection() != null ? attlog.getSection() : "-");

        // Set date
        holder.dateTv.setText(attlog.getDate() != null ? attlog.getDate() : "-");

        // Set biometric device data
        if (attlog.getBiometricDeviceData() != null && !attlog.getBiometricDeviceData().isEmpty()) {
            holder.biometricDeviceLayout.setVisibility(View.VISIBLE);
            holder.biometricDeviceTv.setText(attlog.getBiometricDeviceData());
        } else {
            holder.biometricDeviceLayout.setVisibility(View.GONE);
        }

        // Set remark (if available)
        if (attlog.getRemark() != null && !attlog.getRemark().isEmpty()) {
            holder.remarkLayout.setVisibility(View.VISIBLE);
            holder.remarkTv.setText(attlog.getRemark());
        } else {
            holder.remarkLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return attlogList.size();
    }

    /**
     * Update adapter data
     */
    public void updateData(List<BiometricAttlogReportModel> newList) {
        this.attlogList = newList;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentNameTv;
        TextView attendanceStatusTv;
        TextView admissionNoTv;
        TextView rollNoTv;
        TextView classTv;
        TextView sectionTv;
        TextView dateTv;
        LinearLayout biometricDeviceLayout;
        TextView biometricDeviceTv;
        LinearLayout remarkLayout;
        TextView remarkTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentNameTv = itemView.findViewById(R.id.studentNameTv);
            attendanceStatusTv = itemView.findViewById(R.id.attendanceStatusTv);
            admissionNoTv = itemView.findViewById(R.id.admissionNoTv);
            rollNoTv = itemView.findViewById(R.id.rollNoTv);
            classTv = itemView.findViewById(R.id.classTv);
            sectionTv = itemView.findViewById(R.id.sectionTv);
            dateTv = itemView.findViewById(R.id.dateTv);
            biometricDeviceLayout = itemView.findViewById(R.id.biometricDeviceLayout);
            biometricDeviceTv = itemView.findViewById(R.id.biometricDeviceTv);
            remarkLayout = itemView.findViewById(R.id.remarkLayout);
            remarkTv = itemView.findViewById(R.id.remarkTv);
        }
    }
}

