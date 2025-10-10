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
import com.qdocs.ssre241123.model.IncomeReportModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for Income Report RecyclerView
 */
public class IncomeReportAdapter extends RecyclerView.Adapter<IncomeReportAdapter.ViewHolder> {

    private Context context;
    private List<IncomeReportModel> incomeList;
    private String currency;
    private NumberFormat numberFormat;
    private SimpleDateFormat inputDateFormat;
    private SimpleDateFormat outputDateFormat;

    public IncomeReportAdapter(Context context, List<IncomeReportModel> incomeList) {
        this.context = context;
        this.incomeList = incomeList;
        
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_income_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IncomeReportModel income = incomeList.get(position);

        // Income name
        holder.incomeNameTv.setText(income.getName());
        
        // Invoice number
        holder.invoiceNoTv.setText("Invoice: " + income.getInvoiceNo());
        
        // Income head
        holder.incomeHeadTv.setText(income.getIncomeHead());

        // Amount
        try {
            double amount = Double.parseDouble(income.getAmount());
            String formattedAmount = currency + " " + numberFormat.format(amount);
            holder.amountTv.setText(formattedAmount);
        } catch (NumberFormatException e) {
            holder.amountTv.setText(currency + " " + income.getAmount());
        }

        // Date
        String formattedDate = formatDate(income.getDate());
        holder.dateTv.setText(formattedDate);

        // Note (show only if not empty)
        if (income.getNote() != null && !income.getNote().isEmpty()) {
            holder.noteLayout.setVisibility(View.VISIBLE);
            holder.noteTv.setText(income.getNote());
        } else {
            holder.noteLayout.setVisibility(View.GONE);
        }

        // Apply theme color
        String primaryColor = Utility.getSharedPreferences(context, Constants.primaryColour);
        if (primaryColor != null && !primaryColor.isEmpty()) {
            try {
                holder.amountTv.setTextColor(Color.parseColor(primaryColor));
            } catch (Exception e) {
                // Use default color
            }
        }
    }

    @Override
    public int getItemCount() {
        return incomeList.size();
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
        TextView incomeNameTv;
        TextView invoiceNoTv;
        TextView incomeHeadTv;
        TextView amountTv;
        TextView dateTv;
        LinearLayout noteLayout;
        TextView noteTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            incomeNameTv = itemView.findViewById(R.id.income_name_tv);
            invoiceNoTv = itemView.findViewById(R.id.invoice_no_tv);
            incomeHeadTv = itemView.findViewById(R.id.income_head_tv);
            amountTv = itemView.findViewById(R.id.amount_tv);
            dateTv = itemView.findViewById(R.id.date_tv);
            noteLayout = itemView.findViewById(R.id.note_layout);
            noteTv = itemView.findViewById(R.id.note_tv);
        }
    }
}

