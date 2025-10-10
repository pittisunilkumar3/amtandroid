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
import com.qdocs.ssre241123.model.TotalFeeCollectionReportModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying Total Fee Collection Report records
 */
public class TotalFeeCollectionReportAdapter extends RecyclerView.Adapter<TotalFeeCollectionReportAdapter.ViewHolder> {
    
    private Context context;
    private List<TotalFeeCollectionReportModel> collectionList;
    private String currency;
    private NumberFormat numberFormat;
    
    public TotalFeeCollectionReportAdapter(Context context, List<TotalFeeCollectionReportModel> collectionList) {
        this.context = context;
        this.collectionList = collectionList;
        
        // Get currency symbol
        this.currency = Utility.getSharedPreferences(context, Constants.currency);
        if (this.currency == null || this.currency.isEmpty()) {
            this.currency = "₹";
        }
        
        // Initialize number formatter
        this.numberFormat = NumberFormat.getInstance(new Locale("en", "IN"));
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_total_fee_collection_report, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TotalFeeCollectionReportModel collection = collectionList.get(position);
        
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
        if (collection.getInvoiceNo() != null && !collection.getInvoiceNo().isEmpty()) {
            holder.invoiceNoTv.setText("Invoice: " + collection.getInvoiceNo());
        } else {
            holder.invoiceNoTv.setText("ID: " + collection.getId());
        }
        
        // Set date
        holder.dateTv.setText(collection.getFormattedDate());
        
        // Set type (show only for other_fees or transport_fees)
        if ("other_fees".equals(collection.getType()) || "transport_fees".equals(collection.getType())) {
            holder.typeTv.setVisibility(View.VISIBLE);
            holder.typeTv.setText(collection.getTypeLabel());
        } else {
            holder.typeTv.setVisibility(View.GONE);
        }
        
        // Set student information
        holder.studentNameTv.setText(collection.getStudentName());
        
        if (collection.getAdmissionNo() != null && !collection.getAdmissionNo().isEmpty()) {
            holder.admissionNoTv.setText("Adm No: " + collection.getAdmissionNo());
            holder.admissionNoTv.setVisibility(View.VISIBLE);
        } else {
            holder.admissionNoTv.setVisibility(View.GONE);
        }
        
        if (collection.getFullClassName() != null && !collection.getFullClassName().isEmpty()) {
            holder.classTv.setText(collection.getFullClassName());
            holder.classTv.setVisibility(View.VISIBLE);
        } else {
            holder.classTv.setVisibility(View.GONE);
        }
        
        // Set fee type
        holder.feeTypeTv.setText(collection.getFeeType());
        
        // Set fee code (optional)
        if (collection.getFeeCode() != null && !collection.getFeeCode().isEmpty()) {
            holder.feeCodeTv.setText(collection.getFeeCode());
            holder.feeCodeLayout.setVisibility(View.VISIBLE);
        } else {
            holder.feeCodeLayout.setVisibility(View.GONE);
        }
        
        // Set amount
        String formattedAmount = currency + " " + numberFormat.format(collection.getAmount());
        holder.amountTv.setText(formattedAmount);
        
        // Set fine (optional)
        if (collection.getFine() > 0) {
            String formattedFine = currency + " " + numberFormat.format(collection.getFine());
            holder.fineTv.setText(formattedFine);
            holder.fineLayout.setVisibility(View.VISIBLE);
        } else {
            holder.fineLayout.setVisibility(View.GONE);
        }
        
        // Set discount (optional)
        if (collection.getDiscount() > 0) {
            String formattedDiscount = currency + " " + numberFormat.format(collection.getDiscount());
            holder.discountTv.setText(formattedDiscount);
            holder.discountLayout.setVisibility(View.VISIBLE);
        } else {
            holder.discountLayout.setVisibility(View.GONE);
        }
        
        // Set net amount
        String formattedNetAmount = currency + " " + numberFormat.format(collection.getNetAmount());
        holder.netAmountTv.setText(formattedNetAmount);
        
        // Set payment mode
        if (collection.getPaymentMode() != null && !collection.getPaymentMode().isEmpty()) {
            holder.paymentModeTv.setText(collection.getPaymentMode());
        } else {
            holder.paymentModeTv.setText("N/A");
        }
        
        // Set collected by (optional)
        if (collection.getCollectedBy() != null && !collection.getCollectedBy().isEmpty()) {
            holder.collectedByTv.setText(collection.getCollectedBy());
            holder.collectedByLayout.setVisibility(View.VISIBLE);
        } else {
            holder.collectedByLayout.setVisibility(View.GONE);
        }
        
        // Set note (optional)
        if (collection.getNote() != null && !collection.getNote().isEmpty()) {
            holder.noteTv.setText("Note: " + collection.getNote());
            holder.noteTv.setVisibility(View.VISIBLE);
        } else {
            holder.noteTv.setVisibility(View.GONE);
        }
    }
    
    @Override
    public int getItemCount() {
        return collectionList.size();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        LinearLayout headerLayout;
        TextView invoiceNoTv;
        TextView dateTv;
        TextView typeTv;
        TextView studentNameTv;
        TextView admissionNoTv;
        TextView classTv;
        TextView feeTypeTv;
        TextView feeCodeTv;
        LinearLayout feeCodeLayout;
        TextView amountTv;
        TextView fineTv;
        LinearLayout fineLayout;
        TextView discountTv;
        LinearLayout discountLayout;
        TextView netAmountTv;
        TextView paymentModeTv;
        TextView collectedByTv;
        LinearLayout collectedByLayout;
        TextView noteTv;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            
            cardView = itemView.findViewById(R.id.card_view);
            headerLayout = itemView.findViewById(R.id.header_layout);
            invoiceNoTv = itemView.findViewById(R.id.invoice_no_tv);
            dateTv = itemView.findViewById(R.id.date_tv);
            typeTv = itemView.findViewById(R.id.type_tv);
            studentNameTv = itemView.findViewById(R.id.student_name_tv);
            admissionNoTv = itemView.findViewById(R.id.admission_no_tv);
            classTv = itemView.findViewById(R.id.class_tv);
            feeTypeTv = itemView.findViewById(R.id.fee_type_tv);
            feeCodeTv = itemView.findViewById(R.id.fee_code_tv);
            feeCodeLayout = itemView.findViewById(R.id.fee_code_layout);
            amountTv = itemView.findViewById(R.id.amount_tv);
            fineTv = itemView.findViewById(R.id.fine_tv);
            fineLayout = itemView.findViewById(R.id.fine_layout);
            discountTv = itemView.findViewById(R.id.discount_tv);
            discountLayout = itemView.findViewById(R.id.discount_layout);
            netAmountTv = itemView.findViewById(R.id.net_amount_tv);
            paymentModeTv = itemView.findViewById(R.id.payment_mode_tv);
            collectedByTv = itemView.findViewById(R.id.collected_by_tv);
            collectedByLayout = itemView.findViewById(R.id.collected_by_layout);
            noteTv = itemView.findViewById(R.id.note_tv);
        }
    }
}

