package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.widget.SwitchCompat;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.utils.Utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generic RecyclerView adapter for CRUD list items.
 * Displays each item with name, active switch, edit/delete buttons.
 */
public class GenericCrudAdapter extends RecyclerView.Adapter<GenericCrudAdapter.ViewHolder> {

    private Context context;
    private List<HashMap<String, String>> items;
    private String nameField;
    private String idField;
    private OnActionListener actionListener;
    private String hintColor;

    public interface OnActionListener {
        void onEdit(int position);
        void onDelete(int position);
        void onToggleActive(int position, boolean isActive);
    }

    public GenericCrudAdapter(Context context, List<HashMap<String, String>> items, String nameField, String idField) {
        this.context = context;
        this.items = items;
        this.nameField = nameField;
        this.idField = idField;
        this.hintColor = Utility.getSharedPreferences(context, "primaryColour");
    }

    public void setOnActionListener(OnActionListener listener) {
        this.actionListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_generic_crud_item, parent, false);
        return new ViewHolder(view);
    }

    // Color palette for item icons
    private static final int[][] ITEM_COLORS = {
        {R.drawable.bg_circle_blue, R.color.cardBlueIcon},
        {R.drawable.bg_circle_green, R.color.cardGreenIcon},
        {R.drawable.bg_circle_purple, R.color.cardPurpleIcon},
        {R.drawable.bg_circle_orange, R.color.cardOrangeIcon},
        {R.drawable.bg_circle_teal, R.color.cardTealIcon},
        {R.drawable.bg_circle_pink, R.color.cardPinkIcon},
    };

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String, String> item = items.get(position);
        String name = item.getOrDefault(nameField, "Unknown");

        holder.nameTextView.setText(name);

        // Set initial letter in circle
        String initial = name.isEmpty() ? "?" : name.substring(0, 1).toUpperCase();
        holder.itemInitial.setText(initial);
        int[] colorPair = ITEM_COLORS[position % ITEM_COLORS.length];
        holder.iconBg.setBackgroundResource(colorPair[0]);
        holder.itemInitial.setTextColor(context.getResources().getColor(colorPair[1]));

        // Show active status
        String activeStatus = item.getOrDefault("is_active", "yes");
        boolean isActive = "yes".equalsIgnoreCase(activeStatus) || "1".equals(activeStatus);
        holder.activeSwitch.setChecked(isActive);
        holder.activeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (actionListener != null) actionListener.onToggleActive(position, isChecked);
        });
        holder.editButton.setOnClickListener(v -> { if (actionListener != null) actionListener.onEdit(position); });
        holder.deleteButton.setOnClickListener(v -> { if (actionListener != null) actionListener.onDelete(position); });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public HashMap<String, String> getItem(int position) {
        return items.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView itemInitial;
        View iconBg;
        ImageView editButton;
        ImageView deleteButton;
        SwitchCompat activeSwitch;
        LinearLayout itemLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.item_name);
            itemInitial = itemView.findViewById(R.id.item_initial);
            iconBg = itemView.findViewById(R.id.icon_bg);
            editButton = itemView.findViewById(R.id.btn_edit);
            deleteButton = itemView.findViewById(R.id.btn_delete);
            activeSwitch = itemView.findViewById(R.id.active_switch);
            itemLayout = itemView.findViewById(R.id.item_layout);
        }
    }
}
