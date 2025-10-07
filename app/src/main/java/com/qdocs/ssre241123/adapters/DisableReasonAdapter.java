package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.model.DisableReason;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DisableReasonAdapter extends RecyclerView.Adapter<DisableReasonAdapter.ReasonViewHolder> {

    private Context context;
    private List<DisableReason> reasonList;
    private OnReasonActionListener listener;

    // Date formatters
    private SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private SimpleDateFormat displayDateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

    public interface OnReasonActionListener {
        void onEditClick(DisableReason reason, int position);
        void onDeleteClick(DisableReason reason, int position);
        void onItemClick(DisableReason reason, int position);
    }

    public DisableReasonAdapter(Context context, List<DisableReason> reasonList) {
        this.context = context;
        this.reasonList = reasonList;
    }

    public void setOnReasonActionListener(OnReasonActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReasonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_disable_reason, parent, false);
        return new ReasonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReasonViewHolder holder, int position) {
        DisableReason reason = reasonList.get(position);

        // Set reason text
        holder.reasonNameTV.setText(reason.getReason());
        holder.reasonIdTV.setText("ID: " + reason.getId());

        // Format and set created date
        String createdDate = formatDate(reason.getCreatedAt());
        holder.reasonCreatedTV.setText("Created: " + createdDate);

        // Format and set updated date
        if (reason.getUpdatedAt() != null && !reason.getUpdatedAt().isEmpty()) {
            String updatedDate = formatDate(reason.getUpdatedAt());
            holder.reasonUpdatedTV.setText("Updated: " + updatedDate);
            holder.updatedDateContainer.setVisibility(View.VISIBLE);
        } else {
            holder.updatedDateContainer.setVisibility(View.GONE);
        }

        // Set click listeners
        holder.editButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(reason, holder.getAdapterPosition());
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(reason, holder.getAdapterPosition());
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(reason, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return reasonList.size();
    }

    private String formatDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return "N/A";
        }
        try {
            Date date = apiDateFormat.parse(dateString);
            return displayDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }
    }

    // Method to update the entire list
    public void updateData(List<DisableReason> newReasonList) {
        this.reasonList = newReasonList;
        notifyDataSetChanged();
    }

    // Method to update a single reason
    public void updateReason(int position, DisableReason reason) {
        if (position >= 0 && position < reasonList.size()) {
            reasonList.set(position, reason);
            notifyItemChanged(position);
        }
    }

    // Method to remove a reason
    public void removeReason(int position) {
        if (position >= 0 && position < reasonList.size()) {
            reasonList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, reasonList.size());
        }
    }

    // Method to add a reason
    public void addReason(DisableReason reason) {
        reasonList.add(0, reason);
        notifyItemInserted(0);
    }

    static class ReasonViewHolder extends RecyclerView.ViewHolder {
        TextView reasonNameTV;
        TextView reasonIdTV;
        TextView reasonCreatedTV;
        TextView reasonUpdatedTV;
        LinearLayout updatedDateContainer;
        ImageView editButton;
        ImageView deleteButton;

        public ReasonViewHolder(@NonNull View itemView) {
            super(itemView);
            reasonNameTV = itemView.findViewById(R.id.reason_name_tv);
            reasonIdTV = itemView.findViewById(R.id.reason_id_tv);
            reasonCreatedTV = itemView.findViewById(R.id.reason_created_tv);
            reasonUpdatedTV = itemView.findViewById(R.id.reason_updated_tv);
            updatedDateContainer = itemView.findViewById(R.id.updated_date_container);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}

