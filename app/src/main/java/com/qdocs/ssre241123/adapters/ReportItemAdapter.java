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
import com.qdocs.ssre241123.model.ReportItem;
// import com.qdocs.ssre241123.teachers.TeacherReportDetailActivity; // TODO: Uncomment when detail activity is created
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import java.util.List;

public class ReportItemAdapter extends RecyclerView.Adapter<ReportItemAdapter.ReportItemViewHolder> {

    private Context context;
    private List<ReportItem> reportItems;

    public ReportItemAdapter(Context context, List<ReportItem> reportItems) {
        this.context = context;
        this.reportItems = reportItems;
    }

    @NonNull
    @Override
    public ReportItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_report_item, parent, false);
        return new ReportItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportItemViewHolder holder, int position) {
        ReportItem reportItem = reportItems.get(position);
        
        holder.reportItemName.setText(reportItem.getDisplayName());
        holder.reportItemIcon.setImageResource(reportItem.getIconResource());
        
        // Hide description for now
        holder.reportItemDescription.setVisibility(View.GONE);
        
        // Apply theme colors
        String hintColor = Utility.getSharedPreferences(context, Constants.secondaryColour);
        if (hintColor != null && !hintColor.isEmpty()) {
            try {
                holder.reportItemIcon.setColorFilter(android.graphics.Color.parseColor(hintColor));
                holder.reportItemArrow.setColorFilter(android.graphics.Color.parseColor(hintColor));
            } catch (Exception e) {
                // Use default colors if parsing fails
            }
        }

        holder.reportItemLayout.setOnClickListener(v -> handleReportItemClick(reportItem));
    }

    private void handleReportItemClick(ReportItem reportItem) {
        // For now, show a coming soon message
        // Later this will launch the specific report activity
        Toast.makeText(context, reportItem.getDisplayName() + " - Coming Soon", Toast.LENGTH_SHORT).show();
        
        // Uncomment when TeacherReportDetailActivity is implemented
        /*
        Intent intent = new Intent(context, TeacherReportDetailActivity.class);
        intent.putExtra("report_id", reportItem.getId());
        intent.putExtra("report_name", reportItem.getDisplayName());
        intent.putExtra("category_id", reportItem.getCategoryId());
        context.startActivity(intent);
        if (context instanceof android.app.Activity) {
            ((android.app.Activity) context).overridePendingTransition(R.anim.slide_leftright, R.anim.no_animation);
        }
        */
    }

    @Override
    public int getItemCount() {
        return reportItems.size();
    }

    public static class ReportItemViewHolder extends RecyclerView.ViewHolder {
        CardView reportItemCard;
        LinearLayout reportItemLayout;
        ImageView reportItemIcon;
        TextView reportItemName;
        TextView reportItemDescription;
        ImageView reportItemArrow;

        public ReportItemViewHolder(@NonNull View itemView) {
            super(itemView);
            reportItemCard = itemView.findViewById(R.id.report_item_card);
            reportItemLayout = itemView.findViewById(R.id.report_item_layout);
            reportItemIcon = itemView.findViewById(R.id.report_item_icon);
            reportItemName = itemView.findViewById(R.id.report_item_name);
            reportItemDescription = itemView.findViewById(R.id.report_item_description);
            reportItemArrow = itemView.findViewById(R.id.report_item_arrow);
        }
    }
}
