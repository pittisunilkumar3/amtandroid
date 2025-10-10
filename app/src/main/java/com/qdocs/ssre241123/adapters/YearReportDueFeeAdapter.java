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
import com.qdocs.ssre241123.model.DueFeeReportModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import java.util.List;

/**
 * Adapter for displaying Year Report Due Fee records
 * Shows students with due fees for the entire year (up to December 31st)
 */
public class YearReportDueFeeAdapter extends RecyclerView.Adapter<YearReportDueFeeAdapter.ViewHolder> {
    
    private Context context;
    private List<DueFeeReportModel> dueFeeList;
    
    public YearReportDueFeeAdapter(Context context, List<DueFeeReportModel> dueFeeList) {
        this.context = context;
        this.dueFeeList = dueFeeList;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_due_fee_report, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DueFeeReportModel dueFee = dueFeeList.get(position);
        
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
        holder.studentNameTv.setText(dueFee.getFullName());
        holder.admissionNoTv.setText("Adm. No: " + dueFee.getAdmissionNo());
        holder.classSectionTv.setText(dueFee.getClassSection());
        
        // Set father name
        if (dueFee.getFatherName() != null && !dueFee.getFatherName().isEmpty()) {
            holder.fatherNameTv.setVisibility(View.VISIBLE);
            holder.fatherNameTv.setText("Father: " + dueFee.getFatherName());
        } else {
            holder.fatherNameTv.setVisibility(View.GONE);
        }
        
        // Set mobile number
        if (dueFee.getMobileno() != null && !dueFee.getMobileno().isEmpty()) {
            holder.mobileNoTv.setVisibility(View.VISIBLE);
            holder.mobileNoTv.setText("📱 " + dueFee.getMobileno());
        } else {
            holder.mobileNoTv.setVisibility(View.GONE);
        }
        
        // Set guardian information
        if (dueFee.getGuardianName() != null && !dueFee.getGuardianName().isEmpty()) {
            holder.guardianInfoTv.setVisibility(View.VISIBLE);
            holder.guardianInfoTv.setText("Guardian: " + dueFee.getGuardianName());
        } else {
            holder.guardianInfoTv.setVisibility(View.GONE);
        }
        
        // Set guardian phone
        if (dueFee.getGuardianPhone() != null && !dueFee.getGuardianPhone().isEmpty()) {
            holder.guardianPhoneTv.setVisibility(View.VISIBLE);
            holder.guardianPhoneTv.setText("📞 " + dueFee.getGuardianPhone());
        } else {
            holder.guardianPhoneTv.setVisibility(View.GONE);
        }
        
        // Get currency symbol
        String currency = Utility.getSharedPreferences(context, Constants.currency);
        if (currency == null || currency.isEmpty()) {
            currency = "₹";
        }
        
        // Set fee summary
        holder.totalAmountTv.setText(currency + " " + dueFee.getTotalAmount());
        holder.totalPaidTv.setText(currency + " " + dueFee.getTotalPaid());
        holder.totalBalanceTv.setText(currency + " " + dueFee.getTotalBalance());
        
        // Highlight balance in red if there's due amount
        if (dueFee.hasDueBalance()) {
            holder.totalBalanceTv.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        } else {
            holder.totalBalanceTv.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        }
        
        // Set fine (show only if > 0)
        try {
            double fine = Double.parseDouble(dueFee.getTotalFine());
            if (fine > 0) {
                holder.fineRow.setVisibility(View.VISIBLE);
                holder.totalFineTv.setText(currency + " " + dueFee.getTotalFine());
            } else {
                holder.fineRow.setVisibility(View.GONE);
            }
        } catch (NumberFormatException e) {
            holder.fineRow.setVisibility(View.GONE);
        }
        
        // Set discount (show only if > 0)
        try {
            double discount = Double.parseDouble(dueFee.getTotalDiscount());
            if (discount > 0) {
                holder.discountRow.setVisibility(View.VISIBLE);
                holder.totalDiscountTv.setText(currency + " " + dueFee.getTotalDiscount());
            } else {
                holder.discountRow.setVisibility(View.GONE);
            }
        } catch (NumberFormatException e) {
            holder.discountRow.setVisibility(View.GONE);
        }
        
        // Set fee items count
        int totalFeeItems = dueFee.getTotalFeeItems();
        holder.feeItemsCountTv.setText(totalFeeItems + " fee item(s)");
        
        // Build fee details text
        StringBuilder feeDetails = new StringBuilder();
        
        // Add regular fees
        if (dueFee.getFeesList() != null && !dueFee.getFeesList().isEmpty()) {
            for (DueFeeReportModel.FeeDetail fee : dueFee.getFeesList()) {
                feeDetails.append("• ")
                    .append(fee.getFeeType())
                    .append(" (")
                    .append(fee.getFeeCode())
                    .append("): ")
                    .append(currency)
                    .append(" ")
                    .append(fee.getBalanceAmount())
                    .append("\n");
            }
        }
        
        // Add transport fees
        if (dueFee.getTransportFeesList() != null && !dueFee.getTransportFeesList().isEmpty()) {
            for (DueFeeReportModel.FeeDetail fee : dueFee.getTransportFeesList()) {
                feeDetails.append("• ")
                    .append(fee.getFeeType())
                    .append(": ")
                    .append(currency)
                    .append(" ")
                    .append(fee.getBalanceAmount())
                    .append("\n");
            }
        }
        
        // Set fee details
        if (feeDetails.length() > 0) {
            // Remove last newline
            feeDetails.setLength(feeDetails.length() - 1);
            holder.feeDetailsTv.setText(feeDetails.toString());
            holder.feeDetailsTv.setVisibility(View.VISIBLE);
        } else {
            holder.feeDetailsTv.setVisibility(View.GONE);
        }
    }
    
    @Override
    public int getItemCount() {
        return dueFeeList.size();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        LinearLayout headerLayout;
        TextView studentNameTv;
        TextView admissionNoTv;
        TextView classSectionTv;
        TextView fatherNameTv;
        TextView mobileNoTv;
        TextView guardianInfoTv;
        TextView guardianPhoneTv;
        TextView totalAmountTv;
        TextView totalPaidTv;
        TextView totalBalanceTv;
        TextView totalFineTv;
        TextView totalDiscountTv;
        TextView feeItemsCountTv;
        TextView feeDetailsTv;
        LinearLayout fineRow;
        LinearLayout discountRow;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            
            cardView = itemView.findViewById(R.id.card_view);
            headerLayout = itemView.findViewById(R.id.header_layout);
            studentNameTv = itemView.findViewById(R.id.student_name_tv);
            admissionNoTv = itemView.findViewById(R.id.admission_no_tv);
            classSectionTv = itemView.findViewById(R.id.class_section_tv);
            fatherNameTv = itemView.findViewById(R.id.father_name_tv);
            mobileNoTv = itemView.findViewById(R.id.mobile_no_tv);
            guardianInfoTv = itemView.findViewById(R.id.guardian_info_tv);
            guardianPhoneTv = itemView.findViewById(R.id.guardian_phone_tv);
            totalAmountTv = itemView.findViewById(R.id.total_amount_tv);
            totalPaidTv = itemView.findViewById(R.id.total_paid_tv);
            totalBalanceTv = itemView.findViewById(R.id.total_balance_tv);
            totalFineTv = itemView.findViewById(R.id.total_fine_tv);
            totalDiscountTv = itemView.findViewById(R.id.total_discount_tv);
            feeItemsCountTv = itemView.findViewById(R.id.fee_items_count_tv);
            feeDetailsTv = itemView.findViewById(R.id.fee_details_tv);
            fineRow = itemView.findViewById(R.id.fine_row);
            discountRow = itemView.findViewById(R.id.discount_row);
        }
    }
}

