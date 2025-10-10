package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.teachers.TypeWiseBalanceReportActivity;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import java.util.List;

/**
 * Adapter for displaying Type Wise Balance Report
 */
public class TypeWiseBalanceReportAdapter extends RecyclerView.Adapter<TypeWiseBalanceReportAdapter.ViewHolder> {
    
    private Context context;
    private List<TypeWiseBalanceReportActivity.TypeWiseBalanceReportData> reportDataList;
    
    public TypeWiseBalanceReportAdapter(Context context, List<TypeWiseBalanceReportActivity.TypeWiseBalanceReportData> reportDataList) {
        this.context = context;
        this.reportDataList = reportDataList;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_type_wise_balance_report, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TypeWiseBalanceReportActivity.TypeWiseBalanceReportData reportData = reportDataList.get(position);
        
        // Apply theme color to header
        String primaryColor = Utility.getSharedPreferences(context, Constants.primaryColour);
        if (primaryColor != null && !primaryColor.isEmpty()) {
            try {
                holder.headerLayout.setBackgroundColor(Color.parseColor(primaryColor));
            } catch (Exception e) {
                // Use default color if parsing fails
            }
        }
        
        // Set student information
        holder.studentNameTv.setText(reportData.studentName);
        holder.admissionNoTv.setText("Adm. No: " + (reportData.admissionNo != null ? reportData.admissionNo : "N/A"));
        
        // Set class and section
        String classSection = reportData.className;
        if (reportData.sectionName != null && !reportData.sectionName.isEmpty()) {
            classSection += " - " + reportData.sectionName;
        }
        holder.classSectionTv.setText(classSection);
        
        // Set fee type
        holder.feeTypeTv.setText(reportData.feeType != null ? reportData.feeType : "N/A");
        
        // Set fee group
        if (reportData.feeGroupName != null && !reportData.feeGroupName.isEmpty()) {
            holder.feeGroupTv.setText("Fee Group: " + reportData.feeGroupName);
            holder.feeGroupTv.setVisibility(View.VISIBLE);
        } else {
            holder.feeGroupTv.setVisibility(View.GONE);
        }
        
        // Set mobile number
        if (reportData.mobileNo != null && !reportData.mobileNo.isEmpty()) {
            holder.mobileNoTv.setText("📱 " + reportData.mobileNo);
            holder.mobileNoTv.setVisibility(View.VISIBLE);
        } else {
            holder.mobileNoTv.setVisibility(View.GONE);
        }
        
        // Get currency
        String currency = Utility.getSharedPreferences(context, Constants.currency);
        if (currency == null || currency.isEmpty()) {
            currency = "₹";
        }
        
        // Set fee summary
        // Total Amount
        if (reportData.total != null && !reportData.total.isEmpty()) {
            holder.totalAmountTv.setText(currency + " " + reportData.total);
        } else {
            holder.totalAmountTv.setText(currency + " 0.00");
        }
        
        // Total Paid
        holder.totalPaidTv.setText(currency + " " + reportData.totalAmount);
        
        // Fine
        if (reportData.fine != null && !reportData.fine.isEmpty()) {
            holder.fineTv.setText(currency + " " + reportData.fine);
            // Show fine row if fine > 0
            try {
                double fineValue = Double.parseDouble(reportData.fine);
                if (fineValue > 0) {
                    holder.fineRow.setVisibility(View.VISIBLE);
                } else {
                    holder.fineRow.setVisibility(View.GONE);
                }
            } catch (NumberFormatException e) {
                holder.fineRow.setVisibility(View.GONE);
            }
        } else {
            holder.fineTv.setText(currency + " 0.00");
            holder.fineRow.setVisibility(View.GONE);
        }
        
        // Discount
        holder.discountTv.setText(currency + " " + reportData.totalDiscount);
        // Show discount row if discount > 0
        if (reportData.totalDiscount > 0) {
            holder.discountRow.setVisibility(View.VISIBLE);
        } else {
            holder.discountRow.setVisibility(View.GONE);
        }
        
        // Balance
        if (reportData.balance != null && !reportData.balance.isEmpty()) {
            holder.balanceTv.setText(currency + " " + reportData.balance);
            // Highlight balance in red if there's due amount
            try {
                double balanceValue = Double.parseDouble(reportData.balance);
                if (balanceValue > 0) {
                    holder.balanceTv.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                } else {
                    holder.balanceTv.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
                }
            } catch (NumberFormatException e) {
                holder.balanceTv.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            }
        } else {
            holder.balanceTv.setText(currency + " 0.00");
            holder.balanceTv.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        }
    }
    
    @Override
    public int getItemCount() {
        return reportDataList.size();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        LinearLayout headerLayout;
        TextView studentNameTv;
        TextView admissionNoTv;
        TextView classSectionTv;
        TextView feeTypeTv;
        TextView feeGroupTv;
        TextView mobileNoTv;
        TextView totalAmountTv;
        TextView totalPaidTv;
        TextView fineTv;
        TextView discountTv;
        TextView balanceTv;
        LinearLayout fineRow;
        LinearLayout discountRow;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            headerLayout = itemView.findViewById(R.id.header_layout);
            studentNameTv = itemView.findViewById(R.id.student_name_tv);
            admissionNoTv = itemView.findViewById(R.id.admission_no_tv);
            classSectionTv = itemView.findViewById(R.id.class_section_tv);
            feeTypeTv = itemView.findViewById(R.id.fee_type_tv);
            feeGroupTv = itemView.findViewById(R.id.fee_group_tv);
            mobileNoTv = itemView.findViewById(R.id.mobile_no_tv);
            totalAmountTv = itemView.findViewById(R.id.total_amount_tv);
            totalPaidTv = itemView.findViewById(R.id.total_paid_tv);
            fineTv = itemView.findViewById(R.id.fine_tv);
            discountTv = itemView.findViewById(R.id.discount_tv);
            balanceTv = itemView.findViewById(R.id.balance_tv);
            fineRow = itemView.findViewById(R.id.fine_row);
            discountRow = itemView.findViewById(R.id.discount_row);
        }
    }
}

