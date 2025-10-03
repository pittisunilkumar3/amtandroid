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
import com.qdocs.ssre241123.teachers.TeacherSubmenuActivity;
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
        // Special cases that have custom implementations
        if ("reports".equals(module.getName())) {
            Intent reportsIntent = new Intent(context, com.qdocs.ssre241123.teachers.TeacherReportsActivity.class);
            context.startActivity(reportsIntent);
            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
            return;
        }

        if ("teacher_profile".equals(module.getName())) {
            Intent profileIntent = new Intent(context, TeacherProfile.class);
            context.startActivity(profileIntent);
            context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
            return;
        }

        // For all other modules, navigate to the generic submenu activity
        Intent submenuIntent = new Intent(context, TeacherSubmenuActivity.class);
        submenuIntent.putExtra("menu_id", module.getId());
        submenuIntent.putExtra("menu_name", module.getDisplayName());
        submenuIntent.putExtra("activate_menu", module.getActivateMenu());
        context.startActivity(submenuIntent);
        context.overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
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