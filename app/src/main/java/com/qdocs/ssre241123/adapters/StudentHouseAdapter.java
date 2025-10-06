package com.qdocs.ssre241123.adapters;

import android.content.Context;
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
import com.qdocs.ssre241123.model.StudentHouse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StudentHouseAdapter extends RecyclerView.Adapter<StudentHouseAdapter.HouseViewHolder> {

    private static final String TAG = "StudentHouseAdapter";

    private Context context;
    private List<StudentHouse> houseList;
    private OnHouseActionListener listener;

    public interface OnHouseActionListener {
        void onEditClick(StudentHouse house, int position);
        void onDeleteClick(StudentHouse house, int position);
        void onItemClick(StudentHouse house, int position);
    }

    public StudentHouseAdapter(Context context, List<StudentHouse> houseList) {
        this.context = context;
        this.houseList = houseList;
    }

    public void setOnHouseActionListener(OnHouseActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public HouseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_house, parent, false);
        return new HouseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HouseViewHolder holder, int position) {
        StudentHouse house = houseList.get(position);

        holder.houseNameTV.setText(house.getHouseName());
        holder.houseIdTV.setText("ID: " + house.getId());

        // Set description
        if (house.getDescription() != null && !house.getDescription().isEmpty() && !"null".equals(house.getDescription())) {
            holder.houseDescriptionTV.setText(house.getDescription());
            holder.houseDescriptionTV.setVisibility(View.VISIBLE);
        } else {
            holder.houseDescriptionTV.setVisibility(View.GONE);
        }

        // Set created date
        if (house.getCreatedAt() != null && !house.getCreatedAt().isEmpty()) {
            holder.houseCreatedTV.setText("Created: " + formatDate(house.getCreatedAt()));
            holder.houseCreatedTV.setVisibility(View.VISIBLE);
        } else {
            holder.houseCreatedTV.setVisibility(View.GONE);
        }

        // Set updated date
        if (house.getUpdatedAt() != null && !house.getUpdatedAt().isEmpty() && !"null".equals(house.getUpdatedAt())) {
            holder.houseUpdatedTV.setText("Updated: " + formatDate(house.getUpdatedAt()));
            holder.houseUpdatedTV.setVisibility(View.VISIBLE);
        } else {
            holder.houseUpdatedTV.setVisibility(View.GONE);
        }

        // Set click listeners
        holder.editButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(house, position);
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(house, position);
            }
        });

        holder.cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(house, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return houseList.size();
    }

    private String formatDate(String dateString) {
        try {
            // Try parsing with different formats
            SimpleDateFormat inputFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat inputFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

            Date date = null;
            try {
                date = inputFormat1.parse(dateString);
            } catch (ParseException e) {
                try {
                    date = inputFormat2.parse(dateString);
                } catch (ParseException ex) {
                    return dateString;
                }
            }

            return date != null ? outputFormat.format(date) : dateString;
        } catch (Exception e) {
            Log.e(TAG, "Error formatting date: " + dateString, e);
            return dateString;
        }
    }

    public void updateData(List<StudentHouse> newHouseList) {
        this.houseList = newHouseList;
        notifyDataSetChanged();
    }

    public void updateHouse(int position, StudentHouse house) {
        if (position >= 0 && position < houseList.size()) {
            houseList.set(position, house);
            notifyItemChanged(position);
        }
    }

    public void removeHouse(int position) {
        if (position >= 0 && position < houseList.size()) {
            houseList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, houseList.size());
        }
    }

    public void addHouse(StudentHouse house) {
        houseList.add(0, house);
        notifyItemInserted(0);
    }

    static class HouseViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView houseNameTV;
        TextView houseIdTV;
        TextView houseDescriptionTV;
        TextView houseCreatedTV;
        TextView houseUpdatedTV;
        LinearLayout editButton;
        LinearLayout deleteButton;

        public HouseViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.house_cardView);
            houseNameTV = itemView.findViewById(R.id.house_name_tv);
            houseIdTV = itemView.findViewById(R.id.house_id_tv);
            houseDescriptionTV = itemView.findViewById(R.id.house_description_tv);
            houseCreatedTV = itemView.findViewById(R.id.house_created_tv);
            houseUpdatedTV = itemView.findViewById(R.id.house_updated_tv);
            editButton = itemView.findViewById(R.id.house_edit_btn);
            deleteButton = itemView.findViewById(R.id.house_delete_btn);
        }
    }
}

