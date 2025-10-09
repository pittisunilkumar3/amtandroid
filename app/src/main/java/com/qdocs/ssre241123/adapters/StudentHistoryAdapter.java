package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.model.StudentHistoryModel;

import java.util.List;

public class StudentHistoryAdapter extends RecyclerView.Adapter<StudentHistoryAdapter.ViewHolder> {

    private Context context;
    private List<StudentHistoryModel> studentList;

    public StudentHistoryAdapter(Context context, List<StudentHistoryModel> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentHistoryModel student = studentList.get(position);

        // Set student name
        holder.studentNameTv.setText(student.getFullName());

        // Set admission number
        if (student.getAdmissionNo() != null && !student.getAdmissionNo().isEmpty()) {
            holder.admissionNoTv.setText("Adm. No: " + student.getAdmissionNo());
            holder.admissionNoTv.setVisibility(View.VISIBLE);
        } else {
            holder.admissionNoTv.setVisibility(View.GONE);
        }

        // Set admission date
        if (student.getAdmissionDate() != null && !student.getAdmissionDate().isEmpty()) {
            holder.admissionDateTv.setText(student.getAdmissionDate());
            holder.admissionDateTv.setVisibility(View.VISIBLE);
        } else {
            holder.admissionDateTv.setVisibility(View.GONE);
        }

        // Set class and section
        String classSection = student.getClassSection();
        if (!classSection.isEmpty()) {
            holder.classSectionTv.setText(classSection);
            holder.classSectionTv.setVisibility(View.VISIBLE);
        } else {
            holder.classSectionTv.setVisibility(View.GONE);
        }

        // Set session
        if (student.getSessionName() != null && !student.getSessionName().isEmpty()) {
            holder.sessionTv.setText(student.getSessionName());
            holder.sessionTv.setVisibility(View.VISIBLE);
        } else {
            holder.sessionTv.setVisibility(View.GONE);
        }

        // Set guardian info
        String guardianInfo = student.getGuardianInfo();
        if (!guardianInfo.isEmpty()) {
            holder.guardianInfoTv.setText("Guardian: " + guardianInfo);
            holder.guardianInfoTv.setVisibility(View.VISIBLE);
        } else {
            holder.guardianInfoTv.setVisibility(View.GONE);
        }

        // Set mobile number
        if (student.getMobileno() != null && !student.getMobileno().isEmpty()) {
            holder.mobileNoTv.setText("📱 " + student.getMobileno());
            holder.mobileNoTv.setVisibility(View.VISIBLE);
        } else {
            holder.mobileNoTv.setVisibility(View.GONE);
        }

        // Set guardian phone
        if (student.getGuardianPhone() != null && !student.getGuardianPhone().isEmpty()) {
            holder.guardianPhoneTv.setText("📞 " + student.getGuardianPhone());
            holder.guardianPhoneTv.setVisibility(View.VISIBLE);
        } else {
            holder.guardianPhoneTv.setVisibility(View.GONE);
        }

        // Set status
        String status = student.getIsActive();
        if (status != null && !status.isEmpty()) {
            if (status.equalsIgnoreCase("yes")) {
                holder.statusTv.setText("Active");
                holder.statusTv.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
            } else {
                holder.statusTv.setText("Inactive");
                holder.statusTv.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            }
            holder.statusTv.setVisibility(View.VISIBLE);
        } else {
            holder.statusTv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
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

