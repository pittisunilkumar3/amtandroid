package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.model.GuardianReportModel;

import java.util.List;

/**
 * Adapter for displaying Guardian Report list
 */
public class GuardianReportAdapter extends RecyclerView.Adapter<GuardianReportAdapter.ViewHolder> {

    private Context context;
    private List<GuardianReportModel> guardianList;

    public GuardianReportAdapter(Context context, List<GuardianReportModel> guardianList) {
        this.context = context;
        this.guardianList = guardianList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_guardian_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GuardianReportModel guardian = guardianList.get(position);

        // Student Information
        holder.tvStudentName.setText(guardian.getFullName());
        holder.tvClassSection.setText(guardian.getClassSection());
        holder.tvAdmissionNo.setText("Adm. No: " + (guardian.getAdmissionNo() != null ? guardian.getAdmissionNo() : "N/A"));
        
        // Student Mobile
        if (guardian.getMobileno() != null && !guardian.getMobileno().isEmpty()) {
            holder.tvStudentMobile.setText("Mobile: " + guardian.getMobileno());
            holder.tvStudentMobile.setVisibility(View.VISIBLE);
        } else {
            holder.tvStudentMobile.setVisibility(View.GONE);
        }

        // Guardian Information
        if (guardian.getGuardianName() != null && !guardian.getGuardianName().isEmpty()) {
            holder.tvGuardianName.setText(guardian.getGuardianName());
            holder.guardianSection.setVisibility(View.VISIBLE);
        } else {
            holder.guardianSection.setVisibility(View.GONE);
        }

        if (guardian.getGuardianRelation() != null && !guardian.getGuardianRelation().isEmpty()) {
            holder.tvGuardianRelation.setText("Relation: " + guardian.getGuardianRelation());
            holder.tvGuardianRelation.setVisibility(View.VISIBLE);
        } else {
            holder.tvGuardianRelation.setVisibility(View.GONE);
        }

        if (guardian.getGuardianPhone() != null && !guardian.getGuardianPhone().isEmpty()) {
            holder.tvGuardianPhone.setText("Phone: " + guardian.getGuardianPhone());
            holder.tvGuardianPhone.setVisibility(View.VISIBLE);
        } else {
            holder.tvGuardianPhone.setVisibility(View.GONE);
        }

        // Father Information
        if (guardian.getFatherName() != null && !guardian.getFatherName().isEmpty()) {
            holder.tvFatherName.setText(guardian.getFatherName());
            holder.fatherSection.setVisibility(View.VISIBLE);
        } else {
            holder.fatherSection.setVisibility(View.GONE);
        }

        if (guardian.getFatherPhone() != null && !guardian.getFatherPhone().isEmpty()) {
            holder.tvFatherPhone.setText("Phone: " + guardian.getFatherPhone());
            holder.tvFatherPhone.setVisibility(View.VISIBLE);
        } else {
            holder.tvFatherPhone.setVisibility(View.GONE);
        }

        // Mother Information
        if (guardian.getMotherName() != null && !guardian.getMotherName().isEmpty()) {
            holder.tvMotherName.setText(guardian.getMotherName());
            holder.motherSection.setVisibility(View.VISIBLE);
        } else {
            holder.motherSection.setVisibility(View.GONE);
        }

        if (guardian.getMotherPhone() != null && !guardian.getMotherPhone().isEmpty()) {
            holder.tvMotherPhone.setText("Phone: " + guardian.getMotherPhone());
            holder.tvMotherPhone.setVisibility(View.VISIBLE);
        } else {
            holder.tvMotherPhone.setVisibility(View.GONE);
        }

        // Active Status Badge
        if (guardian.isActive()) {
            holder.tvActiveStatus.setText("Active");
            holder.tvActiveStatus.setBackgroundResource(R.drawable.bg_status_active);
            holder.tvActiveStatus.setVisibility(View.VISIBLE);
        } else {
            holder.tvActiveStatus.setText("Inactive");
            holder.tvActiveStatus.setBackgroundResource(R.drawable.bg_status_inactive);
            holder.tvActiveStatus.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return guardianList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView ivStudentIcon;
        TextView tvStudentName;
        TextView tvClassSection;
        TextView tvAdmissionNo;
        TextView tvStudentMobile;
        TextView tvActiveStatus;

        // Guardian Section
        View guardianSection;
        TextView tvGuardianLabel;
        TextView tvGuardianName;
        TextView tvGuardianRelation;
        TextView tvGuardianPhone;

        // Father Section
        View fatherSection;
        TextView tvFatherLabel;
        TextView tvFatherName;
        TextView tvFatherPhone;

        // Mother Section
        View motherSection;
        TextView tvMotherLabel;
        TextView tvMotherName;
        TextView tvMotherPhone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            ivStudentIcon = itemView.findViewById(R.id.iv_student_icon);
            tvStudentName = itemView.findViewById(R.id.tv_student_name);
            tvClassSection = itemView.findViewById(R.id.tv_class_section);
            tvAdmissionNo = itemView.findViewById(R.id.tv_admission_no);
            tvStudentMobile = itemView.findViewById(R.id.tv_student_mobile);
            tvActiveStatus = itemView.findViewById(R.id.tv_active_status);

            // Guardian Section
            guardianSection = itemView.findViewById(R.id.guardian_section);
            tvGuardianLabel = itemView.findViewById(R.id.tv_guardian_label);
            tvGuardianName = itemView.findViewById(R.id.tv_guardian_name);
            tvGuardianRelation = itemView.findViewById(R.id.tv_guardian_relation);
            tvGuardianPhone = itemView.findViewById(R.id.tv_guardian_phone);

            // Father Section
            fatherSection = itemView.findViewById(R.id.father_section);
            tvFatherLabel = itemView.findViewById(R.id.tv_father_label);
            tvFatherName = itemView.findViewById(R.id.tv_father_name);
            tvFatherPhone = itemView.findViewById(R.id.tv_father_phone);

            // Mother Section
            motherSection = itemView.findViewById(R.id.mother_section);
            tvMotherLabel = itemView.findViewById(R.id.tv_mother_label);
            tvMotherName = itemView.findViewById(R.id.tv_mother_name);
            tvMotherPhone = itemView.findViewById(R.id.tv_mother_phone);
        }
    }
}

