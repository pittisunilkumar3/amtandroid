package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.model.OtherCollectionReportModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying Other Collection Report records
 */
public class OtherCollectionReportAdapter extends RecyclerView.Adapter<OtherCollectionReportAdapter.ViewHolder> {

    private Context context;
    private List<OtherCollectionReportModel> collectionList;
    private String currency;

    public OtherCollectionReportAdapter(Context context, List<OtherCollectionReportModel> collectionList) {
        this.context = context;
        this.collectionList = collectionList;
        this.currency = Utility.getSharedPreferences(context, Constants.currency);
        if (this.currency == null || this.currency.isEmpty()) {
            this.currency = "₹";
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_other_collection_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OtherCollectionReportModel collection = collectionList.get(position);

        // Student name
        holder.studentNameTv.setText(collection.getFullName());

        // Admission number
        if (collection.getAdmissionNo() != null && !collection.getAdmissionNo().isEmpty()) {
            holder.admissionNoTv.setText("Adm No: " + collection.getAdmissionNo());
            holder.admissionNoTv.setVisibility(View.VISIBLE);
        } else {
            holder.admissionNoTv.setVisibility(View.GONE);
        }

        // Class and section
        String classSection = collection.getClassSection();
        if (!classSection.isEmpty()) {
            holder.classSecTv.setText(classSection);
            holder.classSecTv.setVisibility(View.VISIBLE);
        } else {
            holder.classSecTv.setVisibility(View.GONE);
        }

        // Fee type
        if (collection.getType() != null && !collection.getType().isEmpty()) {
            holder.feeTypeTv.setText(collection.getType());
            holder.feeTypeTv.setVisibility(View.VISIBLE);
        } else {
            holder.feeTypeTv.setVisibility(View.GONE);
        }

        // Fee group name
        if (collection.getName() != null && !collection.getName().isEmpty()) {
            holder.feeGroupTv.setText(collection.getName());
            holder.feeGroupTv.setVisibility(View.VISIBLE);
        } else {
            holder.feeGroupTv.setVisibility(View.GONE);
        }

        // Amount
        try {
            double totalAmount = collection.getTotalAmount();
            NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
            String formattedAmount = currency + " " + String.format(Locale.getDefault(), "%.2f", totalAmount);
            holder.amountTv.setText(formattedAmount);
        } catch (Exception e) {
            holder.amountTv.setText(currency + " 0.00");
        }

        // Payment date - use 'date' field instead of 'created_at'
        String paymentDate = collection.getDate();
        if (paymentDate == null || paymentDate.isEmpty()) {
            paymentDate = collection.getCreatedAt();
        }
        if (paymentDate != null && !paymentDate.isEmpty()) {
            String formattedDate = formatDate(paymentDate);
            holder.paymentDateTv.setText(formattedDate);
        } else {
            holder.paymentDateTv.setText("-");
        }

        // Payment mode
        if (collection.getPaymentMode() != null && !collection.getPaymentMode().isEmpty()) {
            holder.paymentModeTv.setText(collection.getPaymentMode().toUpperCase());
        } else {
            holder.paymentModeTv.setText("-");
        }

        // Received by - use the helper method that formats name and employee ID
        String receivedByDisplay = collection.getReceivedByDisplayName();
        if (receivedByDisplay != null && !receivedByDisplay.equals("-")) {
            holder.receivedByTv.setText("Received by: " + receivedByDisplay);
            holder.receivedByTv.setVisibility(View.VISIBLE);
        } else {
            holder.receivedByTv.setVisibility(View.GONE);
        }

        // Discount and fine details
        StringBuilder detailsBuilder = new StringBuilder();
        try {
            double amount = collection.getAmount() != null ? Double.parseDouble(collection.getAmount()) : 0.0;
            double discount = collection.getAmountDiscount() != null ? Double.parseDouble(collection.getAmountDiscount()) : 0.0;
            double fine = collection.getAmountFine() != null ? Double.parseDouble(collection.getAmountFine()) : 0.0;

            if (amount > 0) {
                detailsBuilder.append("Amount: ").append(currency).append(" ").append(String.format(Locale.getDefault(), "%.2f", amount));
            }
            if (discount > 0) {
                if (detailsBuilder.length() > 0) detailsBuilder.append("\n");
                detailsBuilder.append("Discount: ").append(currency).append(" ").append(String.format(Locale.getDefault(), "%.2f", discount));
            }
            if (fine > 0) {
                if (detailsBuilder.length() > 0) detailsBuilder.append("\n");
                detailsBuilder.append("Fine: ").append(currency).append(" ").append(String.format(Locale.getDefault(), "%.2f", fine));
            }
        } catch (NumberFormatException e) {
            // Ignore
        }

        if (detailsBuilder.length() > 0) {
            holder.detailsTv.setText(detailsBuilder.toString());
            holder.detailsTv.setVisibility(View.VISIBLE);
        } else {
            holder.detailsTv.setVisibility(View.GONE);
        }

        // Apply theme color
        String primaryColor = Utility.getSharedPreferences(context, Constants.primaryColour);
        if (primaryColor != null && !primaryColor.isEmpty()) {
            try {
                holder.amountTv.setTextColor(Color.parseColor(primaryColor));
                holder.paymentModeTv.setTextColor(Color.parseColor(primaryColor));
            } catch (Exception e) {
                // Ignore color parsing errors
            }
        }
    }

    @Override
    public int getItemCount() {
        return collectionList.size();
    }

    private String formatDate(String dateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            // Try without time
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                Date date = inputFormat.parse(dateString);
                return outputFormat.format(date);
            } catch (ParseException ex) {
                return dateString;
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView studentNameTv;
        TextView admissionNoTv;
        TextView classSecTv;
        TextView feeTypeTv;
        TextView feeGroupTv;
        TextView amountTv;
        TextView paymentDateTv;
        TextView paymentModeTv;
        TextView receivedByTv;
        TextView detailsTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            studentNameTv = itemView.findViewById(R.id.student_name_tv);
            admissionNoTv = itemView.findViewById(R.id.admission_no_tv);
            classSecTv = itemView.findViewById(R.id.class_sec_tv);
            feeTypeTv = itemView.findViewById(R.id.fee_type_tv);
            feeGroupTv = itemView.findViewById(R.id.fee_group_tv);
            amountTv = itemView.findViewById(R.id.amount_tv);
            paymentDateTv = itemView.findViewById(R.id.payment_date_tv);
            paymentModeTv = itemView.findViewById(R.id.payment_mode_tv);
            receivedByTv = itemView.findViewById(R.id.received_by_tv);
            detailsTv = itemView.findViewById(R.id.details_tv);
        }
    }
}

