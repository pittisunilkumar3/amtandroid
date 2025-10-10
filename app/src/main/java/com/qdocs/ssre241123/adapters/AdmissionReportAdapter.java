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
import com.qdocs.ssre241123.model.AdmissionReportModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import java.util.List;

/**
 * Adapter for displaying Admission Report data in RecyclerView
 */
public class AdmissionReportAdapter extends RecyclerView.Adapter<AdmissionReportAdapter.ViewHolder> {

    private Context context;
    private List<AdmissionReportModel> admissionList;

    public AdmissionReportAdapter(Context context, List<AdmissionReportModel> admissionList) {
        this.context = context;
        this.admissionList = admissionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admission_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdmissionReportModel admission = admissionList.get(position);

        // Apply theme color to card
        String primaryColor = Utility.getSharedPreferences(context, Constants.primaryColour);
        if (primaryColor != null && !primaryColor.isEmpty()) {
            try {
                holder.headerLayout.setBackgroundColor(Color.parseColor(primaryColor));
            } catch (Exception e) {
                // Use default color if parsing fails
            }
        }

        // Set student name
        holder.studentNameTv.setText(admission.getFullName());

        // Set admission number
        if (admission.getAdmissionNo() != null && !admission.getAdmissionNo().isEmpty()) {
            holder.admissionNoTv.setText("Adm. No: " + admission.getAdmissionNo());
            holder.admissionNoTv.setVisibility(View.VISIBLE);
        } else {
            holder.admissionNoTv.setVisibility(View.GONE);
        }

        // Set admission date
        if (admission.getAdmissionDate() != null && !admission.getAdmissionDate().isEmpty()) {
            holder.admissionDateTv.setText("📅 " + admission.getAdmissionDate());
            holder.admissionDateTv.setVisibility(View.VISIBLE);
        } else {
            holder.admissionDateTv.setVisibility(View.GONE);
        }

        // Set class and section
        String classSection = admission.getClassSection();
        if (!classSection.isEmpty()) {
            holder.classSectionTv.setText("🎓 " + classSection);
            holder.classSectionTv.setVisibility(View.VISIBLE);
        } else {
            holder.classSectionTv.setVisibility(View.GONE);
        }

        // Set session
        if (admission.getSessionName() != null && !admission.getSessionName().isEmpty()) {
            holder.sessionTv.setText("📚 " + admission.getSessionName());
            holder.sessionTv.setVisibility(View.VISIBLE);
        } else {
            holder.sessionTv.setVisibility(View.GONE);
        }

        // Set guardian info
        String guardianInfo = admission.getGuardianInfo();
        if (!guardianInfo.isEmpty()) {
            holder.guardianInfoTv.setText("👤 Guardian: " + guardianInfo);
            holder.guardianInfoTv.setVisibility(View.VISIBLE);
        } else {
            holder.guardianInfoTv.setVisibility(View.GONE);
        }

        // Set mobile number
        if (admission.getMobileno() != null && !admission.getMobileno().isEmpty()) {
            holder.mobileNoTv.setText("📱 " + admission.getMobileno());
            holder.mobileNoTv.setVisibility(View.VISIBLE);
        } else {
            holder.mobileNoTv.setVisibility(View.GONE);
        }

        // Set guardian phone
        if (admission.getGuardianPhone() != null && !admission.getGuardianPhone().isEmpty()) {
            holder.guardianPhoneTv.setText("📞 " + admission.getGuardianPhone());
            holder.guardianPhoneTv.setVisibility(View.VISIBLE);
        } else {
            holder.guardianPhoneTv.setVisibility(View.GONE);
        }

        // Set status with color
        String status = admission.getIsActive();
        if (status != null && !status.isEmpty()) {
            if (status.equalsIgnoreCase("yes")) {
                holder.statusTv.setText("✓ Active");
                holder.statusTv.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
                holder.statusTv.setBackgroundResource(R.drawable.bg_status_active);
            } else {
                holder.statusTv.setText("✗ Inactive");
                holder.statusTv.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                holder.statusTv.setBackgroundResource(R.drawable.bg_status_inactive);
            }
            holder.statusTv.setVisibility(View.VISIBLE);
        } else {
            holder.statusTv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return admissionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        View headerLayout;
        TextView studentNameTv;
        TextView admissionNoTv;
        TextView admissionDateTv;
        TextView classSectionTv;
        TextView sessionTv;
        TextView guardianInfoTv;
        TextView mobileNoTv;
        TextView guardianPhoneTv;
        TextView statusTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            headerLayout = itemView.findViewById(R.id.header_layout);
            studentNameTv = itemView.findViewById(R.id.student_name_tv);
            admissionNoTv = itemView.findViewById(R.id.admission_no_tv);
            admissionDateTv = itemView.findViewById(R.id.admission_date_tv);
            classSectionTv = itemView.findViewById(R.id.class_section_tv);
            sessionTv = itemView.findViewById(R.id.session_tv);
            guardianInfoTv = itemView.findViewById(R.id.guardian_info_tv);
            mobileNoTv = itemView.findViewById(R.id.mobile_no_tv);
            guardianPhoneTv = itemView.findViewById(R.id.guardian_phone_tv);
            statusTv = itemView.findViewById(R.id.status_tv);
        }
    }
}

