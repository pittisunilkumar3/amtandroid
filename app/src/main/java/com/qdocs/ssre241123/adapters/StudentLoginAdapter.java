package com.qdocs.ssre241123.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
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
import com.qdocs.ssre241123.model.StudentLoginModel;

import java.util.List;

/**
 * Adapter for displaying student login credentials in a RecyclerView
 */
public class StudentLoginAdapter extends RecyclerView.Adapter<StudentLoginAdapter.ViewHolder> {
    
    private Context context;
    private List<StudentLoginModel> studentLoginList;
    
    public StudentLoginAdapter(Context context, List<StudentLoginModel> studentLoginList) {
        this.context = context;
        this.studentLoginList = studentLoginList;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_login, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentLoginModel student = studentLoginList.get(position);
        
        // Set student name
        holder.tvStudentName.setText(student.getFullName());
        
        // Set class and section
        holder.tvClassSection.setText(student.getClassSection());
        
        // Set admission number
        if (student.getAdmissionNo() != null && !student.getAdmissionNo().isEmpty()) {
            holder.tvAdmissionNo.setText("Adm. No: " + student.getAdmissionNo());
            holder.tvAdmissionNo.setVisibility(View.VISIBLE);
        } else {
            holder.tvAdmissionNo.setVisibility(View.GONE);
        }
        
        // Set mobile number
        if (student.getMobileno() != null && !student.getMobileno().isEmpty()) {
            holder.tvMobile.setText("Mobile: " + student.getMobileno());
            holder.tvMobile.setVisibility(View.VISIBLE);
        } else {
            holder.tvMobile.setVisibility(View.GONE);
        }
        
        // Set email
        if (student.getEmail() != null && !student.getEmail().isEmpty()) {
            holder.tvEmail.setText("Email: " + student.getEmail());
            holder.tvEmail.setVisibility(View.VISIBLE);
        } else {
            holder.tvEmail.setVisibility(View.GONE);
        }
        
        // Set session
        if (student.getSessionName() != null && !student.getSessionName().isEmpty()) {
            holder.tvSession.setText("Session: " + student.getSessionName());
            holder.tvSession.setVisibility(View.VISIBLE);
        } else {
            holder.tvSession.setVisibility(View.GONE);
        }
        
        // Set username
        if (student.getUsername() != null && !student.getUsername().isEmpty()) {
            holder.tvUsername.setText(student.getUsername());
            holder.tvUsername.setVisibility(View.VISIBLE);
            holder.btnCopyUsername.setVisibility(View.VISIBLE);
        } else {
            holder.tvUsername.setText("N/A");
            holder.btnCopyUsername.setVisibility(View.GONE);
        }
        
        // Set password
        if (student.getPassword() != null && !student.getPassword().isEmpty()) {
            holder.tvPassword.setText(student.getPassword());
            holder.tvPassword.setVisibility(View.VISIBLE);
            holder.btnCopyPassword.setVisibility(View.VISIBLE);
        } else {
            holder.tvPassword.setText("N/A");
            holder.btnCopyPassword.setVisibility(View.GONE);
        }
        
        // Set active status indicator
        if ("yes".equalsIgnoreCase(student.getIsActive())) {
            holder.tvActiveStatus.setText("Active");
            holder.tvActiveStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else {
            holder.tvActiveStatus.setText("Inactive");
            holder.tvActiveStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        }
        
        // Copy username button click
        holder.btnCopyUsername.setOnClickListener(v -> {
            copyToClipboard("Username", student.getUsername());
        });
        
        // Copy password button click
        holder.btnCopyPassword.setOnClickListener(v -> {
            copyToClipboard("Password", student.getPassword());
        });
    }
    
    @Override
    public int getItemCount() {
        return studentLoginList.size();
    }
    
    /**
     * Copy text to clipboard
     */
    private void copyToClipboard(String label, String text) {
        if (text != null && !text.isEmpty()) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(label, text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, label + " copied to clipboard", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * ViewHolder class
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentName;
        TextView tvClassSection;
        TextView tvAdmissionNo;
        TextView tvMobile;
        TextView tvEmail;
        TextView tvSession;
        TextView tvUsername;
        TextView tvPassword;
        TextView tvActiveStatus;
        ImageView btnCopyUsername;
        ImageView btnCopyPassword;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            
            tvStudentName = itemView.findViewById(R.id.tv_student_name);
            tvClassSection = itemView.findViewById(R.id.tv_class_section);
            tvAdmissionNo = itemView.findViewById(R.id.tv_admission_no);
            tvMobile = itemView.findViewById(R.id.tv_mobile);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvSession = itemView.findViewById(R.id.tv_session);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvPassword = itemView.findViewById(R.id.tv_password);
            tvActiveStatus = itemView.findViewById(R.id.tv_active_status);
            btnCopyUsername = itemView.findViewById(R.id.btn_copy_username);
            btnCopyPassword = itemView.findViewById(R.id.btn_copy_password);
        }
    }
}

