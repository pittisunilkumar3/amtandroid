package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.model.ReportCategory;
import com.qdocs.ssre241123.teachers.TeacherReportCategoryActivity;
import com.qdocs.ssre241123.teachers.UserLogReportActivity;
import com.qdocs.ssre241123.teachers.AlumniReportActivity;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import java.util.List;

public class ReportCategoryAdapter extends RecyclerView.Adapter<ReportCategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<ReportCategory> categoryList;

    public ReportCategoryAdapter(Context context, List<ReportCategory> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_report_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        ReportCategory category = categoryList.get(position);
        
        holder.categoryName.setText(category.getDisplayName());
        holder.categoryIcon.setImageResource(category.getIconResource());
        
        // Apply theme colors
        String hintColor = Utility.getSharedPreferences(context, Constants.secondaryColour);
        if (hintColor != null && !hintColor.isEmpty()) {
            try {
                holder.categoryIcon.setColorFilter(android.graphics.Color.parseColor(hintColor));
                holder.categoryName.setTextColor(android.graphics.Color.parseColor(hintColor));
            } catch (Exception e) {
                // Use default colors if parsing fails
            }
        }

        holder.categoryLayout.setOnClickListener(v -> handleCategoryClick(category));
    }

    private void handleCategoryClick(ReportCategory category) {
        Intent intent;
        
        // Handle special categories that should go directly to their report screens
        if ("user_log".equals(category.getId())) {
            // For User Log, go directly to UserLogReportActivity
            intent = new Intent(context, UserLogReportActivity.class);
        } else if ("alumni".equals(category.getId())) {
            // For Alumni, go directly to AlumniReportActivity  
            intent = new Intent(context, AlumniReportActivity.class);
        } else {
            // For other categories, go to the category screen
            intent = new Intent(context, TeacherReportCategoryActivity.class);
            intent.putExtra("category_id", category.getId());
            intent.putExtra("category_name", category.getDisplayName());
        }
        
        context.startActivity(intent);
        if (context instanceof android.app.Activity) {
            ((android.app.Activity) context).overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        LinearLayout categoryLayout;
        ImageView categoryIcon;
        TextView categoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryLayout = itemView.findViewById(R.id.category_layout);
            categoryIcon = itemView.findViewById(R.id.category_icon);
            categoryName = itemView.findViewById(R.id.category_name);
        }
    }
}
