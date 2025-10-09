package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.model.OnlineAdmissionModel;

import java.util.List;

public class OnlineAdmissionAdapter extends RecyclerView.Adapter<OnlineAdmissionAdapter.ViewHolder> {

    private Context context;
    private List<OnlineAdmissionModel> admissionList;

    public OnlineAdmissionAdapter(Context context, List<OnlineAdmissionModel> admissionList) {
        this.context = context;
        this.admissionList = admissionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_online_admission, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OnlineAdmissionModel admission = admissionList.get(position);

        // Set student name
        if (admission.getFullName() != null && !admission.getFullName().isEmpty()) {
            holder.studentNameTv.setText(admission.getFullName());
        } else {
            holder.studentNameTv.setText("N/A");
        }

        // Set reference number
        if (admission.getReferenceNo() != null && !admission.getReferenceNo().isEmpty()) {
            holder.referenceNoTv.setText(admission.getReferenceNo());
        } else {
            holder.referenceNoTv.setText("N/A");
        }

        // Set admission number
        if (admission.getAdmissionNo() != null && !admission.getAdmissionNo().isEmpty()) {
            holder.admissionNoTv.setText(admission.getAdmissionNo());
            holder.admissionNoLayout.setVisibility(View.VISIBLE);
        } else {
            holder.admissionNoLayout.setVisibility(View.GONE);
        }

        // Set enrollment status
        holder.enrollmentStatusTv.setText(admission.getEnrollmentStatus());
        if (admission.isEnrolled()) {
            holder.enrollmentStatusTv.setBackgroundColor(Color.parseColor("#4CAF50")); // Green
        } else {
            holder.enrollmentStatusTv.setBackgroundColor(Color.parseColor("#FF9800")); // Orange
        }

        // Set class and section
        holder.classSectionTv.setText(admission.getClassSection());

        // Set gender
        if (admission.getGender() != null && !admission.getGender().isEmpty()) {
            holder.genderTv.setText(admission.getGender());
        } else {
            holder.genderTv.setText("N/A");
        }

        // Set date of birth
        holder.dobTv.setText(admission.getFormattedDob());

        // Set contact
        if (admission.getMobileno() != null && !admission.getMobileno().isEmpty()) {
            holder.contactTv.setText(admission.getMobileno());
        } else {
            holder.contactTv.setText(admission.getParentContact());
        }

        // Set email
        if (admission.getEmail() != null && !admission.getEmail().isEmpty()) {
            holder.emailTv.setText(admission.getEmail());
            holder.emailLayout.setVisibility(View.VISIBLE);
        } else {
            holder.emailLayout.setVisibility(View.GONE);
        }

        // Set father name
        if (admission.getFatherName() != null && !admission.getFatherName().isEmpty()) {
            holder.fatherNameTv.setText(admission.getFatherName());
            holder.fatherLayout.setVisibility(View.VISIBLE);
        } else {
            holder.fatherLayout.setVisibility(View.GONE);
        }

        // Set admission date
        holder.admissionDateTv.setText(admission.getFormattedAdmissionDate());

        // Set payment status
        holder.paymentStatusTv.setText(admission.getPaymentStatus());
        if (admission.isPaid()) {
            holder.paymentStatusTv.setTextColor(Color.parseColor("#4CAF50")); // Green
        } else {
            holder.paymentStatusTv.setTextColor(Color.parseColor("#F44336")); // Red
        }
    }

    @Override
    public int getItemCount() {
        return admissionList != null ? admissionList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentNameTv;
        TextView enrollmentStatusTv;
        TextView referenceNoTv;
        TextView admissionNoTv;
        TextView classSectionTv;
        TextView genderTv;
        TextView dobTv;
        TextView contactTv;
        TextView emailTv;
        TextView fatherNameTv;
        TextView admissionDateTv;
        TextView paymentStatusTv;
        LinearLayout admissionNoLayout;
        LinearLayout emailLayout;
        LinearLayout fatherLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentNameTv = itemView.findViewById(R.id.student_name_tv);
            enrollmentStatusTv = itemView.findViewById(R.id.enrollment_status_tv);
            referenceNoTv = itemView.findViewById(R.id.reference_no_tv);
            admissionNoTv = itemView.findViewById(R.id.admission_no_tv);
            classSectionTv = itemView.findViewById(R.id.class_section_tv);
            genderTv = itemView.findViewById(R.id.gender_tv);
            dobTv = itemView.findViewById(R.id.dob_tv);
            contactTv = itemView.findViewById(R.id.contact_tv);
            emailTv = itemView.findViewById(R.id.email_tv);
            fatherNameTv = itemView.findViewById(R.id.father_name_tv);
            admissionDateTv = itemView.findViewById(R.id.admission_date_tv);
            paymentStatusTv = itemView.findViewById(R.id.payment_status_tv);
            admissionNoLayout = itemView.findViewById(R.id.admission_no_layout);
            emailLayout = itemView.findViewById(R.id.email_layout);
            fatherLayout = itemView.findViewById(R.id.father_layout);
        }
    }
}

