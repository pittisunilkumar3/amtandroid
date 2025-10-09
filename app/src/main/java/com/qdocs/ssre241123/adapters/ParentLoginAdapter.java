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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.model.ParentLoginModel;

import java.util.List;

public class ParentLoginAdapter extends RecyclerView.Adapter<ParentLoginAdapter.ViewHolder> {

    private Context context;
    private List<ParentLoginModel> parentLoginList;

    public ParentLoginAdapter(Context context, List<ParentLoginModel> parentLoginList) {
        this.context = context;
        this.parentLoginList = parentLoginList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_parent_login, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParentLoginModel parentLogin = parentLoginList.get(position);

        // Set student name
        holder.studentNameTv.setText(parentLogin.getFullName());

        // Set admission number
        if (parentLogin.getAdmissionNo() != null && !parentLogin.getAdmissionNo().isEmpty()) {
            holder.admissionNoTv.setText("Adm. No: " + parentLogin.getAdmissionNo());
            holder.admissionNoTv.setVisibility(View.VISIBLE);
        } else {
            holder.admissionNoTv.setVisibility(View.GONE);
        }

        // Set roll number
        if (parentLogin.getRollNo() != null && !parentLogin.getRollNo().isEmpty()) {
            holder.rollNoTv.setText("Roll No: " + parentLogin.getRollNo());
            holder.rollNoTv.setVisibility(View.VISIBLE);
        } else {
            holder.rollNoTv.setVisibility(View.GONE);
        }

        // Set class and section
        String classSection = parentLogin.getClassSection();
        if (!classSection.isEmpty()) {
            holder.classSectionTv.setText(classSection);
            holder.classSectionTv.setVisibility(View.VISIBLE);
        } else {
            holder.classSectionTv.setVisibility(View.GONE);
        }

        // Set father name
        if (parentLogin.getFatherName() != null && !parentLogin.getFatherName().isEmpty()) {
            holder.fatherNameTv.setText("Father: " + parentLogin.getFatherName());
            holder.fatherNameTv.setVisibility(View.VISIBLE);
        } else {
            holder.fatherNameTv.setVisibility(View.GONE);
        }

        // Set guardian info
        if (parentLogin.getGuardianName() != null && !parentLogin.getGuardianName().isEmpty()) {
            holder.guardianNameTv.setText("Guardian: " + parentLogin.getGuardianName());
            holder.guardianNameTv.setVisibility(View.VISIBLE);
        } else {
            holder.guardianNameTv.setVisibility(View.GONE);
        }

        // Set guardian phone
        if (parentLogin.getGuardianPhone() != null && !parentLogin.getGuardianPhone().isEmpty()) {
            holder.guardianPhoneTv.setText("Phone: " + parentLogin.getGuardianPhone());
            holder.guardianPhoneTv.setVisibility(View.VISIBLE);
        } else {
            holder.guardianPhoneTv.setVisibility(View.GONE);
        }

        // Set parent username
        if (parentLogin.getParentUsername() != null && !parentLogin.getParentUsername().isEmpty()) {
            holder.usernameTv.setText(parentLogin.getParentUsername());
            holder.usernameContainer.setVisibility(View.VISIBLE);
        } else {
            holder.usernameContainer.setVisibility(View.GONE);
        }

        // Set parent password
        if (parentLogin.getParentPassword() != null && !parentLogin.getParentPassword().isEmpty()) {
            holder.passwordTv.setText(parentLogin.getParentPassword());
            holder.passwordContainer.setVisibility(View.VISIBLE);
        } else {
            holder.passwordContainer.setVisibility(View.GONE);
        }

        // Copy username to clipboard
        holder.copyUsernameBtn.setOnClickListener(v -> {
            copyToClipboard("Username", parentLogin.getParentUsername());
        });

        // Copy password to clipboard
        holder.copyPasswordBtn.setOnClickListener(v -> {
            copyToClipboard("Password", parentLogin.getParentPassword());
        });
    }

    private void copyToClipboard(String label, String text) {
        if (text != null && !text.isEmpty()) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(label, text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, label + " copied to clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return parentLoginList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView studentNameTv;
        TextView admissionNoTv;
        TextView rollNoTv;
        TextView classSectionTv;
        TextView fatherNameTv;
        TextView guardianNameTv;
        TextView guardianPhoneTv;
        View usernameContainer;
        TextView usernameTv;
        ImageView copyUsernameBtn;
        View passwordContainer;
        TextView passwordTv;
        ImageView copyPasswordBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            studentNameTv = itemView.findViewById(R.id.student_name_tv);
            admissionNoTv = itemView.findViewById(R.id.admission_no_tv);
            rollNoTv = itemView.findViewById(R.id.roll_no_tv);
            classSectionTv = itemView.findViewById(R.id.class_section_tv);
            fatherNameTv = itemView.findViewById(R.id.father_name_tv);
            guardianNameTv = itemView.findViewById(R.id.guardian_name_tv);
            guardianPhoneTv = itemView.findViewById(R.id.guardian_phone_tv);
            usernameContainer = itemView.findViewById(R.id.username_container);
            usernameTv = itemView.findViewById(R.id.username_tv);
            copyUsernameBtn = itemView.findViewById(R.id.copy_username_btn);
            passwordContainer = itemView.findViewById(R.id.password_container);
            passwordTv = itemView.findViewById(R.id.password_tv);
            copyPasswordBtn = itemView.findViewById(R.id.copy_password_btn);
        }
    }
}

