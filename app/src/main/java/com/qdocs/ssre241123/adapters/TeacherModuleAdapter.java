package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.model.TeacherModule;
import com.qdocs.ssre241123.teachers.TeacherProfile;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import java.util.List;

public class TeacherModuleAdapter extends RecyclerView.Adapter<TeacherModuleAdapter.ModuleViewHolder> {

    private FragmentActivity context;
    private List<TeacherModule> moduleList;

    public TeacherModuleAdapter(FragmentActivity context, List<TeacherModule> moduleList) {
        this.context = context;
        this.moduleList = moduleList;
    }

    @NonNull
    @Override
    public ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_teacher_module, parent, false);
        return new ModuleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleViewHolder holder, int position) {
        TeacherModule module = moduleList.get(position);
        
        holder.moduleName.setText(module.getDisplayName());
        holder.moduleIcon.setImageResource(module.getIconResource());
        
        // Apply theme colors
        String hintColor = Utility.getSharedPreferences(context, Constants.secondaryColour);
        if (hintColor != null && !hintColor.isEmpty()) {
            try {
                holder.moduleIcon.setColorFilter(android.graphics.Color.parseColor(hintColor));
                holder.moduleName.setTextColor(android.graphics.Color.parseColor(hintColor));
            } catch (Exception e) {
                // Use default colors if parsing fails
            }
        }
        
        if (!module.isEnabled()) {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        } else {
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, 
                ViewGroup.LayoutParams.WRAP_CONTENT));
            holder.itemView.setVisibility(View.VISIBLE);
        }

        holder.moduleLayout.setOnClickListener(v -> handleModuleClick(module));
    }

    private void handleModuleClick(TeacherModule module) {
        switch (module.getName()) {
            case "results":
                showComingSoon("Results");
                break;
            case "tc_generation":
                showComingSoon("TC Generation");
                break;
            case "importing":
                showComingSoon("Importing");
                break;
            case "zoom_live_classes":
                showComingSoon("Zoom Live Classes");
                break;
            case "gmeet_live_classes":
                showComingSoon("Gmeet Live Classes");
                break;
            case "student_information":
                showComingSoon("Student Information");
                break;
            case "fees_collection":
                showComingSoon("Fees Collection");
                break;
            case "income":
                showComingSoon("Income");
                break;
            case "examinations":
                showComingSoon("Examinations");
                break;
            case "online_examinations":
                showComingSoon("Online Examinations");
                break;
            case "lesson_plan":
                showComingSoon("Lesson Plan");
                break;
            case "academics":
                showComingSoon("Academics");
                break;
            case "homework":
                showComingSoon("Homework");
                break;
            case "library":
                showComingSoon("Library");
                break;
            case "inventory":
                showComingSoon("Inventory");
                break;
            case "transport":
                showComingSoon("Transport");
                break;
            case "hostel":
                showComingSoon("Hostel");
                break;
            case "system_setting":
                showComingSoon("System Settings");
                break;
            case "behaviour_records":
                showComingSoon("Behaviour Records");
                break;
            case "multi_branch":
                showComingSoon("Multi Branch");
                break;
            case "fee_discount":
                showComingSoon("Fee Discount");
                break;
            case "referral_application":
                showComingSoon("Referral Application");
                break;
            case "admission_no":
                showComingSoon("Admission No");
                break;
            case "hall_ticket_no":
                showComingSoon("Hall Ticket No");
                break;
            case "other_fees":
                showComingSoon("Other Fees");
                break;
            case "expenses":
                showComingSoon("Expenses");
                break;
            case "accounting":
                showComingSoon("Accounting");
                break;
            case "attendance":
                showComingSoon("Attendance");
                break;
            case "cbse_examination":
                showComingSoon("CBSE Examination");
                break;
            case "hall_ticket_generation":
                showComingSoon("Hall Ticket Generation");
                break;
            case "front_office":
                showComingSoon("Front Office");
                break;
            case "human_resource":
                showComingSoon("Human Resource");
                break;
            case "communicate":
                showComingSoon("Communicate");
                break;
            case "certificate":
                showComingSoon("Certificate");
                break;
            case "front_cms":
                showComingSoon("Front CMS");
                break;
            case "download_center":
                showComingSoon("Download Center");
                break;
            case "alumni":
                showComingSoon("Alumni");
                break;
            case "reports":
                showComingSoon("Reports");
                break;
            case "teacher_profile":
                Intent profileIntent = new Intent(context, TeacherProfile.class);
                context.startActivity(profileIntent);
                context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
                break;
            default:
                showComingSoon(module.getDisplayName());
                break;
        }
    }

    private void showComingSoon(String moduleName) {
        Toast.makeText(context, moduleName + " - Coming Soon", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return moduleList.size();
    }

    public static class ModuleViewHolder extends RecyclerView.ViewHolder {
        TextView moduleName;
        ImageView moduleIcon;
        LinearLayout moduleLayout;

        public ModuleViewHolder(@NonNull View itemView) {
            super(itemView);
            moduleName = itemView.findViewById(R.id.modulename);
            moduleIcon = itemView.findViewById(R.id.moduleicon);
            moduleLayout = itemView.findViewById(R.id.module_layout);
        }
    }
}