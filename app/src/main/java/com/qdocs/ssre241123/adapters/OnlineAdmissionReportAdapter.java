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
import com.qdocs.ssre241123.model.OnlineAdmissionReportModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for Online Admission Fee Collection Report RecyclerView
 */
public class OnlineAdmissionReportAdapter extends RecyclerView.Adapter<OnlineAdmissionReportAdapter.ViewHolder> {

    private Context context;
    private List<OnlineAdmissionReportModel> admissionList;
    private String currency;
    private NumberFormat numberFormat;
    private SimpleDateFormat inputDateFormat;
    private SimpleDateFormat outputDateFormat;

    public OnlineAdmissionReportAdapter(Context context, List<OnlineAdmissionReportModel> admissionList) {
        this.context = context;
        this.admissionList = admissionList;
        
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_online_admission_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OnlineAdmissionReportModel admission = admissionList.get(position);

        // Applicant information
        holder.applicantNameTv.setText(admission.getFullName());
        holder.referenceNoTv.setText("Ref: " + admission.getReferenceNo());
        holder.classSecTv.setText(admission.getClassSection());

        // Contact information
        if (admission.getMobileno() != null && !admission.getMobileno().isEmpty()) {
            holder.mobileTv.setText("Mobile: " + admission.getMobileno());
            holder.mobileTv.setVisibility(View.VISIBLE);
        } else {
            holder.mobileTv.setVisibility(View.GONE);
        }

        if (admission.getEmail() != null && !admission.getEmail().isEmpty()) {
            holder.emailTv.setText("Email: " + admission.getEmail());
            holder.emailTv.setVisibility(View.VISIBLE);
        } else {
            holder.emailTv.setVisibility(View.GONE);
        }

        // Category
        if (admission.getCategory() != null && !admission.getCategory().isEmpty()) {
            holder.categoryTv.setText(admission.getCategory());
            holder.categoryTv.setVisibility(View.VISIBLE);
        } else {
            holder.categoryTv.setVisibility(View.GONE);
        }
        
        // Amount
        try {
            double amount = Double.parseDouble(admission.getPaidAmount());
            String formattedAmount = currency + " " + numberFormat.format(amount);
            holder.amountTv.setText(formattedAmount);
        } catch (NumberFormatException e) {
            holder.amountTv.setText(currency + " " + admission.getPaidAmount());
        }

        // Payment date
        String formattedDate = formatDate(admission.getDate());
        holder.paymentDateTv.setText(formattedDate);

        // Payment mode
        holder.paymentModeTv.setText(admission.getPaymentMode());

        // Payment ID
        if (admission.getPaymentId() != null && !admission.getPaymentId().isEmpty()) {
            holder.paymentIdTv.setText("Payment ID: " + admission.getPaymentId());
            holder.paymentIdTv.setVisibility(View.VISIBLE);
        } else {
            holder.paymentIdTv.setVisibility(View.GONE);
        }

        // Additional information (Hostel, Transport, House)
        StringBuilder additionalInfo = new StringBuilder();
        
        if (admission.getHostelName() != null && !admission.getHostelName().isEmpty()) {
            additionalInfo.append("Hostel: ").append(admission.getHostelName());
            if (admission.getRoomType() != null && !admission.getRoomType().isEmpty()) {
                additionalInfo.append(" (").append(admission.getRoomType()).append(")");
            }
        }
        
        if (admission.getRouteTitle() != null && !admission.getRouteTitle().isEmpty()) {
            if (additionalInfo.length() > 0) additionalInfo.append("\n");
            additionalInfo.append("Transport: ").append(admission.getRouteTitle());
            if (admission.getVehicleNo() != null && !admission.getVehicleNo().isEmpty()) {
                additionalInfo.append(" (").append(admission.getVehicleNo()).append(")");
            }
        }
        
        if (admission.getHouseName() != null && !admission.getHouseName().isEmpty()) {
            if (additionalInfo.length() > 0) additionalInfo.append("\n");
            additionalInfo.append("House: ").append(admission.getHouseName());
        }

        if (additionalInfo.length() > 0) {
            holder.additionalInfoTv.setText(additionalInfo.toString());
            holder.additionalInfoTv.setVisibility(View.VISIBLE);
        } else {
            holder.additionalInfoTv.setVisibility(View.GONE);
        }

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
        return admissionList.size();
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
        TextView applicantNameTv;
        TextView referenceNoTv;
        TextView classSecTv;
        TextView mobileTv;
        TextView emailTv;
        TextView categoryTv;
        TextView amountTv;
        TextView paymentDateTv;
        TextView paymentModeTv;
        TextView paymentIdTv;
        TextView additionalInfoTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            applicantNameTv = itemView.findViewById(R.id.applicant_name_tv);
            referenceNoTv = itemView.findViewById(R.id.reference_no_tv);
            classSecTv = itemView.findViewById(R.id.class_sec_tv);
            mobileTv = itemView.findViewById(R.id.mobile_tv);
            emailTv = itemView.findViewById(R.id.email_tv);
            categoryTv = itemView.findViewById(R.id.category_tv);
            amountTv = itemView.findViewById(R.id.amount_tv);
            paymentDateTv = itemView.findViewById(R.id.payment_date_tv);
            paymentModeTv = itemView.findViewById(R.id.payment_mode_tv);
            paymentIdTv = itemView.findViewById(R.id.payment_id_tv);
            additionalInfoTv = itemView.findViewById(R.id.additional_info_tv);
        }
    }
}

