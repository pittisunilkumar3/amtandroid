package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.model.Student;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.StudentViewHolder> {

    private Context context;
    private List<Student> studentList;
    private OnStudentClickListener listener;

    public interface OnStudentClickListener {
        void onStudentClick(Student student, int position);
    }

    public StudentListAdapter(Context context, List<Student> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    public void setOnStudentClickListener(OnStudentClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_list, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);

        // Set student name
        holder.studentName.setText(student.getFullName());

        // Set admission number
        holder.studentAdmissionNo.setText(student.getAdmissionNo());

        // Set roll number
        holder.studentRollNo.setText(student.getRollNo());

        // Set class and section
        holder.studentClassSection.setText(student.getClassSection());

        // Set guardian name (prefer father, then mother, then guardian)
        String guardianInfo = "";
        if (student.getFatherName() != null && !student.getFatherName().isEmpty()) {
            guardianInfo = "Father: " + student.getFatherName();
        } else if (student.getMotherName() != null && !student.getMotherName().isEmpty()) {
            guardianInfo = "Mother: " + student.getMotherName();
        } else if (student.getGuardianName() != null && !student.getGuardianName().isEmpty()) {
            guardianInfo = "Guardian: " + student.getGuardianName();
        } else {
            guardianInfo = "Guardian: N/A";
        }
        holder.studentGuardianName.setText(guardianInfo);

        // Load student image
        if (student.getImage() != null && !student.getImage().isEmpty()) {
            Picasso.with(context)
                    .load(student.getImage())
                    .placeholder(R.drawable.placeholder_user)
                    .error(R.drawable.placeholder_user)
                    .into(holder.studentImage);
        } else {
            holder.studentImage.setImageResource(R.drawable.placeholder_user);
        }

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onStudentClick(student, position);
            } else {
                // Default action - show toast
                Toast.makeText(context, "Student: " + student.getFullName(), Toast.LENGTH_SHORT).show();
            }
        });

        // Set view details button click listener
        holder.viewDetailsButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onStudentClick(student, position);
            } else {
                // Default action - show toast
                Toast.makeText(context, "View details for: " + student.getFullName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public void updateData(List<Student> newStudentList) {
        this.studentList = newStudentList;
        notifyDataSetChanged();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        CircleImageView studentImage;
        TextView studentName;
        TextView studentAdmissionNo;
        TextView studentRollNo;
        TextView studentClassSection;
        TextView studentGuardianName;
        ImageView viewDetailsButton;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            studentImage = itemView.findViewById(R.id.student_image);
            studentName = itemView.findViewById(R.id.student_name);
            studentAdmissionNo = itemView.findViewById(R.id.student_admission_no);
            studentRollNo = itemView.findViewById(R.id.student_roll_no);
            studentClassSection = itemView.findViewById(R.id.student_class_section);
            studentGuardianName = itemView.findViewById(R.id.student_guardian_name);
            viewDetailsButton = itemView.findViewById(R.id.student_view_details);
        }
    }
}

