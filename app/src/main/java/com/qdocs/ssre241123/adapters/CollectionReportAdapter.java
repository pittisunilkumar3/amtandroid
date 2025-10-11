package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.model.CollectionReportModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for Collection Report RecyclerView
 */
public class CollectionReportAdapter extends RecyclerView.Adapter<CollectionReportAdapter.ViewHolder> {

    private Context context;
    private List<CollectionReportModel> collectionList;
    private String currency;

    public CollectionReportAdapter(Context context, List<CollectionReportModel> collectionList) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_collection_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CollectionReportModel collection = collectionList.get(position);

        // Apply theme color to header
        String primaryColor = Utility.getSharedPreferences(context, Constants.primaryColour);
        if (primaryColor != null && !primaryColor.isEmpty()) {
            try {
                holder.headerLayout.setBackgroundColor(Color.parseColor(primaryColor));
            } catch (Exception e) {
                // Use default color if parsing fails
            }
        }

        // Set invoice number
        if (collection.getInvNo() != null && !collection.getInvNo().isEmpty()) {
            holder.invoiceNoTv.setText("Invoice: " + collection.getInvNo());
        } else {
            holder.invoiceNoTv.setText("ID: " + collection.getId());
        }

        // Set date
        holder.dateTv.setText(formatDate(collection.getDate()));

        // Set student information
        holder.studentNameTv.setText(collection.getFullName());

        if (collection.getAdmissionNo() != null && !collection.getAdmissionNo().isEmpty()) {
            holder.admissionNoTv.setText("Adm No: " + collection.getAdmissionNo());
            holder.admissionNoTv.setVisibility(View.VISIBLE);
        } else {
            holder.admissionNoTv.setVisibility(View.GONE);
        }

        String classSection = collection.getClassSection();
        if (!classSection.isEmpty()) {
            holder.classTv.setText(classSection);
            holder.classTv.setVisibility(View.VISIBLE);
        } else {
            holder.classTv.setVisibility(View.GONE);
        }

        // Set fee information
        if (collection.getType() != null && !collection.getType().isEmpty()) {
            holder.feeTypeTv.setText(collection.getType());
        } else {
            holder.feeTypeTv.setText("-");
        }

        if (collection.getCode() != null && !collection.getCode().isEmpty()) {
            holder.feeCodeTv.setText(collection.getCode());
            holder.feeCodeLayout.setVisibility(View.VISIBLE);
        } else {
            holder.feeCodeLayout.setVisibility(View.GONE);
        }

        if (collection.getName() != null && !collection.getName().isEmpty()) {
            holder.feeGroupTv.setText(collection.getName());
            holder.feeGroupLayout.setVisibility(View.VISIBLE);
        } else {
            holder.feeGroupLayout.setVisibility(View.GONE);
        }

        // Set amount details
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("en", "IN"));
        
        try {
            double amount = Double.parseDouble(collection.getAmount());
            String formattedAmount = currency + " " + numberFormat.format(amount);
            holder.amountTv.setText(formattedAmount);
        } catch (Exception e) {
            holder.amountTv.setText(currency + " 0.00");
        }

        // Set discount (optional)
        try {
            double discount = Double.parseDouble(collection.getAmountDiscount());
            if (discount > 0) {
                String formattedDiscount = currency + " " + numberFormat.format(discount);
                holder.discountTv.setText(formattedDiscount);
                holder.discountLayout.setVisibility(View.VISIBLE);
            } else {
                holder.discountLayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            holder.discountLayout.setVisibility(View.GONE);
        }

        // Set fine (optional)
        try {
            double fine = Double.parseDouble(collection.getAmountFine());
            if (fine > 0) {
                String formattedFine = currency + " " + numberFormat.format(fine);
                holder.fineTv.setText(formattedFine);
                holder.fineLayout.setVisibility(View.VISIBLE);
            } else {
                holder.fineLayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            holder.fineLayout.setVisibility(View.GONE);
        }

        // Set total amount
        double totalAmount = collection.getTotalAmount();
        String formattedTotal = currency + " " + numberFormat.format(totalAmount);
        holder.totalAmountTv.setText(formattedTotal);

        // Set payment mode
        if (collection.getPaymentMode() != null && !collection.getPaymentMode().isEmpty()) {
            holder.paymentModeTv.setText(collection.getPaymentMode());
        } else {
            holder.paymentModeTv.setText("N/A");
        }

        // Set received by (optional)
        if (collection.getReceivedBy() != null && !collection.getReceivedBy().isEmpty()) {
            holder.receivedByTv.setText(collection.getReceivedBy());
            holder.receivedByLayout.setVisibility(View.VISIBLE);
        } else {
            holder.receivedByLayout.setVisibility(View.GONE);
        }

        // Set description (optional)
        if (collection.getDescription() != null && !collection.getDescription().isEmpty()) {
            holder.descriptionTv.setText(collection.getDescription());
            holder.descriptionTv.setVisibility(View.VISIBLE);
        } else {
            holder.descriptionTv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return collectionList.size();
    }

    /**
     * Format date from YYYY-MM-DD to MMM DD, YYYY
     */
    private String formatDate(String date) {
        if (date == null || date.isEmpty()) {
            return "";
        }

        try {
            // Parse date in format YYYY-MM-DD
            String[] parts = date.split("-");
            if (parts.length == 3) {
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int day = Integer.parseInt(parts[2]);

                String[] monthNames = {
                    "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
                };

                String monthName = monthNames[month - 1];
                return String.format("%s %02d, %d", monthName, day, year);
            }
        } catch (Exception e) {
            // Return original date if parsing fails
        }

        return date;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout headerLayout;
        TextView invoiceNoTv;
        TextView dateTv;
        TextView studentNameTv;
        TextView admissionNoTv;
        TextView classTv;
        TextView feeTypeTv;
        TextView feeCodeTv;
        LinearLayout feeCodeLayout;
        TextView feeGroupTv;
        LinearLayout feeGroupLayout;
        TextView amountTv;
        TextView discountTv;
        LinearLayout discountLayout;
        TextView fineTv;
        LinearLayout fineLayout;
        TextView totalAmountTv;
        TextView paymentModeTv;
        TextView receivedByTv;
        LinearLayout receivedByLayout;
        TextView descriptionTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            headerLayout = itemView.findViewById(R.id.headerLayout);
            invoiceNoTv = itemView.findViewById(R.id.invoiceNoTv);
            dateTv = itemView.findViewById(R.id.dateTv);
            studentNameTv = itemView.findViewById(R.id.studentNameTv);
            admissionNoTv = itemView.findViewById(R.id.admissionNoTv);
            classTv = itemView.findViewById(R.id.classTv);
            feeTypeTv = itemView.findViewById(R.id.feeTypeTv);
            feeCodeTv = itemView.findViewById(R.id.feeCodeTv);
            feeCodeLayout = itemView.findViewById(R.id.feeCodeLayout);
            feeGroupTv = itemView.findViewById(R.id.feeGroupTv);
            feeGroupLayout = itemView.findViewById(R.id.feeGroupLayout);
            amountTv = itemView.findViewById(R.id.amountTv);
            discountTv = itemView.findViewById(R.id.discountTv);
            discountLayout = itemView.findViewById(R.id.discountLayout);
            fineTv = itemView.findViewById(R.id.fineTv);
            fineLayout = itemView.findViewById(R.id.fineLayout);
            totalAmountTv = itemView.findViewById(R.id.totalAmountTv);
            paymentModeTv = itemView.findViewById(R.id.paymentModeTv);
            receivedByTv = itemView.findViewById(R.id.receivedByTv);
            receivedByLayout = itemView.findViewById(R.id.receivedByLayout);
            descriptionTv = itemView.findViewById(R.id.descriptionTv);
        }
    }
}

