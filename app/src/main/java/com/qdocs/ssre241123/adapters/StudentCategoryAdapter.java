package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.model.StudentCategory;

import java.util.List;

public class StudentCategoryAdapter extends RecyclerView.Adapter<StudentCategoryAdapter.CategoryViewHolder> {

    private static final String TAG = "StudentCategoryAdapter";

    private Context context;
    private List<StudentCategory> categoryList;
    private OnCategoryActionListener listener;

    public interface OnCategoryActionListener {
        void onEditClick(StudentCategory category, int position);
        void onDeleteClick(StudentCategory category, int position);
        void onItemClick(StudentCategory category, int position);
    }

    public StudentCategoryAdapter(Context context, List<StudentCategory> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    public void setOnCategoryActionListener(OnCategoryActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        StudentCategory category = categoryList.get(position);

        holder.categoryNameTV.setText(category.getCategoryName());
        holder.categoryIdTV.setText("ID: " + category.getCategoryId());

        // Debug logging
        Log.d(TAG, "Binding category: " + category.getCategoryName() +
              ", is_active: '" + category.getIsActive() +
              "', isActiveCategory(): " + category.isActiveCategory());

        // Set status
        if (category.isActiveCategory()) {
            holder.statusTV.setText("Active");
            holder.statusTV.setTextColor(Color.parseColor("#4CAF50"));
            holder.statusTV.setBackgroundResource(R.drawable.bg_status_active);
            Log.d(TAG, "Setting ACTIVE badge for: " + category.getCategoryName());
        } else {
            holder.statusTV.setText("Inactive");
            holder.statusTV.setTextColor(Color.parseColor("#F44336"));
            holder.statusTV.setBackgroundResource(R.drawable.bg_status_inactive);
            Log.d(TAG, "Setting INACTIVE badge for: " + category.getCategoryName());
        }

        // Set created date
        if (category.getCreatedAt() != null && !category.getCreatedAt().isEmpty()) {
            holder.createdAtTV.setText("Created: " + formatDate(category.getCreatedAt()));
            holder.createdAtTV.setVisibility(View.VISIBLE);
        } else {
            holder.createdAtTV.setVisibility(View.GONE);
        }

        // Set updated date
        if (category.getUpdatedAt() != null && !category.getUpdatedAt().isEmpty() && !"null".equals(category.getUpdatedAt())) {
            holder.updatedAtTV.setText("Updated: " + formatDate(category.getUpdatedAt()));
            holder.updatedAtTV.setVisibility(View.VISIBLE);
        } else {
            holder.updatedAtTV.setVisibility(View.GONE);
        }

        // Set click listeners
        holder.editButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(category, position);
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(category, position);
            }
        });

        holder.cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(category, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    private String formatDate(String dateTime) {
        // Format: "2023-08-01 17:30:49" -> "Aug 01, 2023"
        if (dateTime == null || dateTime.isEmpty()) {
            return "";
        }
        try {
            String[] parts = dateTime.split(" ");
            if (parts.length > 0) {
                String datePart = parts[0];
                String[] dateParts = datePart.split("-");
                if (dateParts.length == 3) {
                    String year = dateParts[0];
                    String month = getMonthName(dateParts[1]);
                    String day = dateParts[2];
                    return month + " " + day + ", " + year;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    private String getMonthName(String month) {
        switch (month) {
            case "01": return "Jan";
            case "02": return "Feb";
            case "03": return "Mar";
            case "04": return "Apr";
            case "05": return "May";
            case "06": return "Jun";
            case "07": return "Jul";
            case "08": return "Aug";
            case "09": return "Sep";
            case "10": return "Oct";
            case "11": return "Nov";
            case "12": return "Dec";
            default: return month;
        }
    }

    public void updateCategory(int position, StudentCategory category) {
        categoryList.set(position, category);
        notifyItemChanged(position);
    }

    public void removeCategory(int position) {
        categoryList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, categoryList.size());
    }

    public void addCategory(StudentCategory category) {
        categoryList.add(0, category);
        notifyItemInserted(0);
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView categoryNameTV;
        TextView categoryIdTV;
        TextView statusTV;
        TextView createdAtTV;
        TextView updatedAtTV;
        LinearLayout editButton;
        LinearLayout deleteButton;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.category_cardView);
            categoryNameTV = itemView.findViewById(R.id.category_name_tv);
            categoryIdTV = itemView.findViewById(R.id.category_id_tv);
            statusTV = itemView.findViewById(R.id.category_status_tv);
            createdAtTV = itemView.findViewById(R.id.category_created_tv);
            updatedAtTV = itemView.findViewById(R.id.category_updated_tv);
            editButton = itemView.findViewById(R.id.category_edit_btn);
            deleteButton = itemView.findViewById(R.id.category_delete_btn);
        }
    }
}

