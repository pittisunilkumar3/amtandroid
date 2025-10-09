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
import com.qdocs.ssre241123.model.StudentReportModel;

import java.util.List;

public class StudentReportAdapter extends RecyclerView.Adapter<StudentReportAdapter.ViewHolder> {

    private Context context;
    private List<StudentReportModel> studentList;

    public StudentReportAdapter(Context context, List<StudentReportModel> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentReportModel student = studentList.get(position);

        // Set student name
        holder.studentNameTv.setText(student.getFullName());

        // Set admission number
        if (student.getAdmissionNo() != null && !student.getAdmissionNo().isEmpty()) {
            holder.admissionNoTv.setText("Adm. No: " + student.getAdmissionNo());
            holder.admissionNoTv.setVisibility(View.VISIBLE);
        } else {
            holder.admissionNoTv.setVisibility(View.GONE);
        }

        // Set roll number
        if (student.getRollNo() != null && !student.getRollNo().isEmpty()) {
            holder.rollNoTv.setText("Roll No: " + student.getRollNo());
            holder.rollNoTv.setVisibility(View.VISIBLE);
        } else {
            holder.rollNoTv.setVisibility(View.GONE);
        }

        // Set class and section
        String classSection = student.getClassSection();
        if (!classSection.isEmpty()) {
            holder.classSectionTv.setText(classSection);
            holder.classSectionTv.setVisibility(View.VISIBLE);
        } else {
            holder.classSectionTv.setVisibility(View.GONE);
        }

        // Set father name
        if (student.getFatherName() != null && !student.getFatherName().isEmpty()) {
            holder.fatherNameTv.setText("Father: " + student.getFatherName());
            holder.fatherNameTv.setVisibility(View.VISIBLE);
        } else {
            holder.fatherNameTv.setVisibility(View.GONE);
        }

        // Set gender
        if (student.getGender() != null && !student.getGender().isEmpty()) {
            holder.genderTv.setText(student.getGender());
            holder.genderTv.setVisibility(View.VISIBLE);
        } else {
            holder.genderTv.setVisibility(View.GONE);
        }

        // Set date of birth
        if (student.getDob() != null && !student.getDob().isEmpty()) {
            holder.dobTv.setText("DOB: " + student.getDob());
            holder.dobTv.setVisibility(View.VISIBLE);
        } else {
            holder.dobTv.setVisibility(View.GONE);
        }

        // Set mobile number
        if (student.getMobileno() != null && !student.getMobileno().isEmpty()) {
            holder.mobileNoTv.setText("Mobile: " + student.getMobileno());
            holder.mobileNoTv.setVisibility(View.VISIBLE);
        } else {
            holder.mobileNoTv.setVisibility(View.GONE);
        }

        // Set category
        if (student.getCategoryName() != null && !student.getCategoryName().isEmpty()) {
            holder.categoryTv.setText(student.getCategoryName());
            holder.categoryTv.setVisibility(View.VISIBLE);
        } else {
            holder.categoryTv.setVisibility(View.GONE);
        }

        // Set email
        if (student.getEmail() != null && !student.getEmail().isEmpty()) {
            holder.emailTv.setText(student.getEmail());
            holder.emailTv.setVisibility(View.VISIBLE);
        } else {
            holder.emailTv.setVisibility(View.GONE);
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
        TextView rollNoTv;
        TextView classSectionTv;
        TextView fatherNameTv;
        TextView genderTv;
        TextView dobTv;
        TextView mobileNoTv;
        TextView categoryTv;
        TextView emailTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            studentNameTv = itemView.findViewById(R.id.student_name_tv);
            admissionNoTv = itemView.findViewById(R.id.admission_no_tv);
            rollNoTv = itemView.findViewById(R.id.roll_no_tv);
            classSectionTv = itemView.findViewById(R.id.class_section_tv);
            fatherNameTv = itemView.findViewById(R.id.father_name_tv);
            genderTv = itemView.findViewById(R.id.gender_tv);
            dobTv = itemView.findViewById(R.id.dob_tv);
            mobileNoTv = itemView.findViewById(R.id.mobile_no_tv);
            categoryTv = itemView.findViewById(R.id.category_tv);
            emailTv = itemView.findViewById(R.id.email_tv);
        }
    }
}

