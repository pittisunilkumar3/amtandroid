package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.model.StudentProfileReportModel;

import java.util.List;

/**
 * Adapter for displaying Student Profile Report in RecyclerView
 * Shows comprehensive student information in card format
 */
public class StudentProfileReportAdapter extends RecyclerView.Adapter<StudentProfileReportAdapter.StudentProfileViewHolder> {

    private Context context;
    private List<StudentProfileReportModel> studentList;

    public StudentProfileReportAdapter(Context context, List<StudentProfileReportModel> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public StudentProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_profile_report, parent, false);
        return new StudentProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentProfileViewHolder holder, int position) {
        StudentProfileReportModel student = studentList.get(position);

        // Student Name
        String fullName = buildFullName(student);
        holder.studentNameTv.setText(fullName);

        // Active Status Badge
        String isActive = student.getIsActive();
        if ("yes".equalsIgnoreCase(isActive)) {
            holder.activeStatusTv.setText("Active");
            holder.activeStatusTv.setBackgroundColor(Color.parseColor("#4CAF50")); // Green
        } else {
            holder.activeStatusTv.setText("Inactive");
            holder.activeStatusTv.setBackgroundColor(Color.parseColor("#F44336")); // Red
        }

        // Admission Number
        String admissionNo = student.getAdmissionNo();
        if (admissionNo != null && !admissionNo.isEmpty()) {
            holder.admissionNoTv.setText(admissionNo);
        } else {
            holder.admissionNoTv.setText("N/A");
        }

        // Roll Number
        String rollNo = student.getRollNo();
        if (rollNo != null && !rollNo.isEmpty()) {
            holder.rollNoTv.setText(rollNo);
        } else {
            holder.rollNoTv.setText("N/A");
        }

        // Class & Section
        String className = student.getClassName();
        String sectionName = student.getSectionName();
        if (className != null && !className.isEmpty()) {
            String classSection = className;
            if (sectionName != null && !sectionName.isEmpty()) {
                classSection += " - " + sectionName;
            }
            holder.classSectionTv.setText(classSection);
        } else {
            holder.classSectionTv.setText("N/A");
        }

        // Gender
        String gender = student.getGender();
        if (gender != null && !gender.isEmpty()) {
            holder.genderTv.setText(gender);
        } else {
            holder.genderTv.setText("N/A");
        }

        // Date of Birth
        String dob = student.getDob();
        if (dob != null && !dob.isEmpty()) {
            holder.dobTv.setText(dob);
        } else {
            holder.dobTv.setText("N/A");
        }

        // Contact
        String mobile = student.getMobileno();
        if (mobile != null && !mobile.isEmpty()) {
            holder.contactTv.setText(mobile);
        } else {
            holder.contactTv.setText("N/A");
        }

        // Email
        String email = student.getEmail();
        if (email != null && !email.isEmpty()) {
            holder.emailTv.setText(email);
            holder.emailLayout.setVisibility(View.VISIBLE);
        } else {
            holder.emailLayout.setVisibility(View.GONE);
        }

        // Father Information
        String fatherName = student.getFatherName();
        String fatherPhone = student.getFatherPhone();
        if (fatherName != null && !fatherName.isEmpty()) {
            holder.fatherNameTv.setText(fatherName);
            if (fatherPhone != null && !fatherPhone.isEmpty()) {
                holder.fatherPhoneTv.setText(fatherPhone);
                holder.fatherPhoneTv.setVisibility(View.VISIBLE);
            } else {
                holder.fatherPhoneTv.setVisibility(View.GONE);
            }
            holder.fatherLayout.setVisibility(View.VISIBLE);
        } else {
            holder.fatherLayout.setVisibility(View.GONE);
        }

        // Mother Information
        String motherName = student.getMotherName();
        String motherPhone = student.getMotherPhone();
        if (motherName != null && !motherName.isEmpty()) {
            holder.motherNameTv.setText(motherName);
            if (motherPhone != null && !motherPhone.isEmpty()) {
                holder.motherPhoneTv.setText(motherPhone);
                holder.motherPhoneTv.setVisibility(View.VISIBLE);
            } else {
                holder.motherPhoneTv.setVisibility(View.GONE);
            }
            holder.motherLayout.setVisibility(View.VISIBLE);
        } else {
            holder.motherLayout.setVisibility(View.GONE);
        }

        // Admission Date
        String admissionDate = student.getAdmissionDate();
        if (admissionDate != null && !admissionDate.isEmpty()) {
            holder.admissionDateTv.setText(admissionDate);
        } else {
            holder.admissionDateTv.setText("N/A");
        }

        // Category
        String categoryName = student.getCategoryName();
        if (categoryName != null && !categoryName.isEmpty()) {
            holder.categoryTv.setText(categoryName);
            holder.categoryLayout.setVisibility(View.VISIBLE);
        } else {
            holder.categoryLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    /**
     * Build full name from firstname, middlename, and lastname
     */
    private String buildFullName(StudentProfileReportModel student) {
        StringBuilder fullName = new StringBuilder();
        
        if (student.getFirstname() != null && !student.getFirstname().isEmpty()) {
            fullName.append(student.getFirstname());
        }
        
        if (student.getMiddlename() != null && !student.getMiddlename().isEmpty()) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(student.getMiddlename());
        }
        
        if (student.getLastname() != null && !student.getLastname().isEmpty()) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(student.getLastname());
        }
        
        return fullName.length() > 0 ? fullName.toString() : "N/A";
    }

    /**
     * ViewHolder class for Student Profile Report items
     */
    public static class StudentProfileViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView studentNameTv;
        TextView activeStatusTv;
        TextView admissionNoTv;
        TextView rollNoTv;
        TextView classSectionTv;
        TextView genderTv;
        TextView dobTv;
        TextView contactTv;
        LinearLayout emailLayout;
        TextView emailTv;
        LinearLayout fatherLayout;
        TextView fatherNameTv;
        TextView fatherPhoneTv;
        LinearLayout motherLayout;
        TextView motherNameTv;
        TextView motherPhoneTv;
        TextView admissionDateTv;
        LinearLayout categoryLayout;
        TextView categoryTv;

        public StudentProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            studentNameTv = itemView.findViewById(R.id.student_name_tv);
            activeStatusTv = itemView.findViewById(R.id.active_status_tv);
            admissionNoTv = itemView.findViewById(R.id.admission_no_tv);
            rollNoTv = itemView.findViewById(R.id.roll_no_tv);
            classSectionTv = itemView.findViewById(R.id.class_section_tv);
            genderTv = itemView.findViewById(R.id.gender_tv);
            dobTv = itemView.findViewById(R.id.dob_tv);
            contactTv = itemView.findViewById(R.id.contact_tv);
            emailLayout = itemView.findViewById(R.id.email_layout);
            emailTv = itemView.findViewById(R.id.email_tv);
            fatherLayout = itemView.findViewById(R.id.father_layout);
            fatherNameTv = itemView.findViewById(R.id.father_name_tv);
            fatherPhoneTv = itemView.findViewById(R.id.father_phone_tv);
            motherLayout = itemView.findViewById(R.id.mother_layout);
            motherNameTv = itemView.findViewById(R.id.mother_name_tv);
            motherPhoneTv = itemView.findViewById(R.id.mother_phone_tv);
            admissionDateTv = itemView.findViewById(R.id.admission_date_tv);
            categoryLayout = itemView.findViewById(R.id.category_layout);
            categoryTv = itemView.findViewById(R.id.category_tv);
        }
    }
}

