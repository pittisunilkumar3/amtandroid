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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.model.MenuSubmenuItem;
import com.qdocs.ssre241123.teachers.TeacherStudentDetailsActivity;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import java.util.List;

public class SubmenuItemAdapter extends RecyclerView.Adapter<SubmenuItemAdapter.SubmenuItemViewHolder> {

    private Context context;
    private List<MenuSubmenuItem> submenuItems;

    public SubmenuItemAdapter(Context context, List<MenuSubmenuItem> submenuItems) {
        this.context = context;
        this.submenuItems = submenuItems;
    }

    @NonNull
    @Override
    public SubmenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_submenu_item, parent, false);
        return new SubmenuItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubmenuItemViewHolder holder, int position) {
        MenuSubmenuItem submenuItem = submenuItems.get(position);

        holder.submenuItemName.setText(formatDisplayName(submenuItem.getDisplayName()));
        holder.submenuItemIcon.setImageResource(submenuItem.getIconResource());

        // Apply theme colors
        String hintColor = Utility.getSharedPreferences(context, Constants.secondaryColour);
        if (hintColor != null && !hintColor.isEmpty()) {
            try {
                holder.submenuItemIcon.setColorFilter(android.graphics.Color.parseColor(hintColor));
                holder.submenuItemName.setTextColor(android.graphics.Color.parseColor(hintColor));
            } catch (Exception e) {
                // Use default colors if parsing fails
            }
        }

        holder.submenuItemLayout.setOnClickListener(v -> handleSubmenuItemClick(submenuItem));
    }

    private String formatDisplayName(String displayName) {
        if (displayName == null || displayName.isEmpty()) {
            return displayName;
        }
        
        // Replace underscores with spaces and capitalize words
        String formatted = displayName.replace("_", " ");
        
        // Capitalize first letter of each word
        String[] words = formatted.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (word.length() > 0) {
                result.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) {
                    result.append(word.substring(1).toLowerCase());
                }
                result.append(" ");
            }
        }
        
        return result.toString().trim();
    }

    private void handleSubmenuItemClick(MenuSubmenuItem submenuItem) {
        // Check for specific submenu items that have custom implementations
        String itemName = submenuItem.getName();

        if ("student_details".equals(itemName)) {
            // Navigate to Student Details Activity
            Intent intent = new Intent(context, TeacherStudentDetailsActivity.class);
            context.startActivity(intent);
            if (context instanceof android.app.Activity) {
                ((android.app.Activity) context).overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
            }
            return;
        }

        // For other items, show a coming soon message
        Toast.makeText(context, submenuItem.getDisplayName() + " - Coming Soon", Toast.LENGTH_SHORT).show();

        // TODO: Implement navigation to other specific functionality screens
        // This will be implemented based on the submenu item type
        /*
        Intent intent = new Intent(context, TeacherSubmenuDetailActivity.class);
        intent.putExtra("submenu_id", submenuItem.getId());
        intent.putExtra("submenu_name", submenuItem.getDisplayName());
        intent.putExtra("submenu_url", submenuItem.getUrl());
        intent.putExtra("parent_menu_id", submenuItem.getParentMenuId());
        context.startActivity(intent);
        if (context instanceof android.app.Activity) {
            ((android.app.Activity) context).overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
        }
        */
    }

    @Override
    public int getItemCount() {
        return submenuItems.size();
    }

    public static class SubmenuItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout submenuItemLayout;
        ImageView submenuItemIcon;
        TextView submenuItemName;

        public SubmenuItemViewHolder(@NonNull View itemView) {
            super(itemView);
            submenuItemLayout = itemView.findViewById(R.id.submenu_item_layout);
            submenuItemIcon = itemView.findViewById(R.id.submenu_item_icon);
            submenuItemName = itemView.findViewById(R.id.submenu_item_name);
        }
    }
}

