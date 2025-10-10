package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.model.DailyCollectionReportModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying Daily Collection Report records
 */
public class DailyCollectionReportAdapter extends RecyclerView.Adapter<DailyCollectionReportAdapter.ViewHolder> {
    
    private Context context;
    private List<DailyCollectionReportModel> collectionList;
    
    public DailyCollectionReportAdapter(Context context, List<DailyCollectionReportModel> collectionList) {
        this.context = context;
        this.collectionList = collectionList;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_daily_collection_report, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DailyCollectionReportModel collection = collectionList.get(position);
        
        // Apply theme color to header
        String primaryColor = Utility.getSharedPreferences(context, Constants.primaryColour);
        if (primaryColor != null && !primaryColor.isEmpty()) {
            try {
                holder.headerLayout.setBackgroundColor(Color.parseColor(primaryColor));
            } catch (Exception e) {
                // Use default color if parsing fails
            }
        }
        
        // Set date
        holder.dateTv.setText(collection.getFormattedDate());
        
        // Set type (show only for other_fees)
        if ("other_fees".equals(collection.getType())) {
            holder.typeTv.setVisibility(View.VISIBLE);
            holder.typeTv.setText(collection.getTypeLabel());
        } else {
            holder.typeTv.setVisibility(View.GONE);
        }
        
        // Get currency symbol
        String currency = Utility.getSharedPreferences(context, Constants.currency);
        if (currency == null || currency.isEmpty()) {
            currency = "₹";
        }
        
        // Format amount with currency
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("en", "IN"));
        String formattedAmount = currency + " " + numberFormat.format(collection.getAmount());
        holder.amountTv.setText(formattedAmount);
        
        // Set count
        holder.countTv.setText(String.valueOf(collection.getCount()));
        
        // Handle zero collection days
        if (collection.isZeroCollection()) {
            holder.amountTv.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            holder.amountTv.setText(currency + " 0");
            holder.viewDetailsBtn.setVisibility(View.GONE);
        } else {
            holder.amountTv.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
            holder.viewDetailsBtn.setVisibility(View.VISIBLE);
        }
        
        // Set transaction IDs
        holder.transactionIdsTv.setText(collection.getTransactionIdsString());
        
        // Toggle transaction IDs visibility
        holder.viewDetailsBtn.setOnClickListener(v -> {
            if (holder.transactionIdsTv.getVisibility() == View.VISIBLE) {
                holder.transactionIdsTv.setVisibility(View.GONE);
                holder.viewDetailsBtn.setText("View Transaction IDs");
            } else {
                holder.transactionIdsTv.setVisibility(View.VISIBLE);
                holder.viewDetailsBtn.setText("Hide Transaction IDs");
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return collectionList.size();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        LinearLayout headerLayout;
        TextView dateTv;
        TextView typeTv;
        TextView amountTv;
        TextView countTv;
        Button viewDetailsBtn;
        TextView transactionIdsTv;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            
            cardView = itemView.findViewById(R.id.card_view);
            headerLayout = itemView.findViewById(R.id.header_layout);
            dateTv = itemView.findViewById(R.id.date_tv);
            typeTv = itemView.findViewById(R.id.type_tv);
            amountTv = itemView.findViewById(R.id.amount_tv);
            countTv = itemView.findViewById(R.id.count_tv);
            viewDetailsBtn = itemView.findViewById(R.id.view_details_btn);
            transactionIdsTv = itemView.findViewById(R.id.transaction_ids_tv);
        }
    }
}

