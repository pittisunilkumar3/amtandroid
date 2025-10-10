package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.model.OnlineFeesReportModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for Online Fees Report RecyclerView
 */
public class OnlineFeesReportAdapter extends RecyclerView.Adapter<OnlineFeesReportAdapter.ViewHolder> {

    private Context context;
    private List<OnlineFeesReportModel> feesList;
    private String currency;
    private NumberFormat numberFormat;
    private SimpleDateFormat inputDateFormat;
    private SimpleDateFormat outputDateFormat;

    public OnlineFeesReportAdapter(Context context, List<OnlineFeesReportModel> feesList) {
        this.context = context;
        this.feesList = feesList;
        
        // Get currency
        this.currency = Utility.getSharedPreferences(context, Constants.currency);
        if (this.currency == null || this.currency.isEmpty()) {
            this.currency = "₹";
        }
        
        // Initialize number formatter
        this.numberFormat = NumberFormat.getInstance(new Locale("en", "IN"));
        
        // Initialize date formatters
        this.inputDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        this.outputDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_online_fees_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OnlineFeesReportModel fee = feesList.get(position);

        // Student information
        holder.studentNameTv.setText(fee.getStudentName());
        holder.admissionNoTv.setText("Adm No: " + fee.getAdmissionNo());
        holder.classSecTv.setText(fee.getClassName() + " - " + fee.getSectionName());

        // Fee information
        holder.feeGroupTv.setText(fee.getFeeGroup());
        holder.feeTypeTv.setText(fee.getFeeType());
        
        // Amount
        try {
            double amount = Double.parseDouble(fee.getAmount());
            String formattedAmount = currency + " " + numberFormat.format(amount);
            holder.amountTv.setText(formattedAmount);
        } catch (NumberFormatException e) {
            holder.amountTv.setText(currency + " " + fee.getAmount());
        }

        // Payment date
        String formattedDate = formatDate(fee.getPaymentDate());
        holder.paymentDateTv.setText(formattedDate);

        // Payment mode
        holder.paymentModeTv.setText(fee.getPaymentMode());

        // Apply theme color
        String primaryColor = Utility.getSharedPreferences(context, Constants.primaryColour);
        if (primaryColor != null && !primaryColor.isEmpty()) {
            try {
                holder.amountTv.setTextColor(Color.parseColor(primaryColor));
                holder.paymentModeTv.setTextColor(Color.parseColor(primaryColor));
            } catch (Exception e) {
                // Use default color
            }
        }
    }

    @Override
    public int getItemCount() {
        return feesList.size();
    }

    /**
     * Format date from yyyy-MM-dd to dd MMM yyyy
     */
    private String formatDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return "";
        }
        
        try {
            Date date = inputDateFormat.parse(dateStr);
            if (date != null) {
                return outputDateFormat.format(date);
            }
        } catch (ParseException e) {
            // Return original if parsing fails
        }
        
        return dateStr;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView studentNameTv;
        TextView admissionNoTv;
        TextView classSecTv;
        TextView feeGroupTv;
        TextView feeTypeTv;
        TextView amountTv;
        TextView paymentDateTv;
        TextView paymentModeTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            studentNameTv = itemView.findViewById(R.id.student_name_tv);
            admissionNoTv = itemView.findViewById(R.id.admission_no_tv);
            classSecTv = itemView.findViewById(R.id.class_sec_tv);
            feeGroupTv = itemView.findViewById(R.id.fee_group_tv);
            feeTypeTv = itemView.findViewById(R.id.fee_type_tv);
            amountTv = itemView.findViewById(R.id.amount_tv);
            paymentDateTv = itemView.findViewById(R.id.payment_date_tv);
            paymentModeTv = itemView.findViewById(R.id.payment_mode_tv);
        }
    }
}

