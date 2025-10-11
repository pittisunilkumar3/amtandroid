package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.qdocs.ssre241123.model.ExpenseReportModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for Expense Report RecyclerView
 */
public class ExpenseReportAdapter extends RecyclerView.Adapter<ExpenseReportAdapter.ExpenseViewHolder> {

    private Context context;
    private List<ExpenseReportModel> expenseList;
    private String currency;

    public ExpenseReportAdapter(Context context, List<ExpenseReportModel> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
        
        // Get currency from shared preferences
        this.currency = Utility.getSharedPreferences(context.getApplicationContext(), Constants.currency);
        if (currency == null || currency.isEmpty()) {
            currency = "₹";
        }
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_expense_report, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        ExpenseReportModel expense = expenseList.get(position);

        // Set expense name
        holder.expenseNameTv.setText(expense.getName());

        // Set invoice number
        String invoiceText = "Invoice: " + expense.getInvoiceNo();
        holder.invoiceNoTv.setText(invoiceText);

        // Set expense category
        holder.expenseCategoryTv.setText(expense.getExpCategory());

        // Format and set amount
        try {
            double amount = Double.parseDouble(expense.getAmount());
            NumberFormat numberFormat = NumberFormat.getInstance(new Locale("en", "IN"));
            String formattedAmount = currency + " " + numberFormat.format(amount);
            holder.amountTv.setText(formattedAmount);
        } catch (NumberFormatException e) {
            holder.amountTv.setText(currency + " " + expense.getAmount());
        }

        // Format and set date
        String formattedDate = formatDate(expense.getDate());
        holder.dateTv.setText(formattedDate);

        // Show/hide note
        if (expense.getNote() != null && !expense.getNote().isEmpty()) {
            holder.noteLayout.setVisibility(View.VISIBLE);
            holder.noteTv.setText(expense.getNote());
        } else {
            holder.noteLayout.setVisibility(View.GONE);
        }

        // Apply theme colors
        applyThemeColors(holder);
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    /**
     * Format date from yyyy-MM-dd to dd MMM yyyy
     */
    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            Date date = inputFormat.parse(dateStr);
            return outputFormat.format(date);
        } catch (ParseException e) {
            return dateStr;
        }
    }

    /**
     * Apply theme colors from shared preferences
     */
    private void applyThemeColors(ExpenseViewHolder holder) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        
        // Get primary color
        String primaryColorStr = sharedPreferences.getString("primaryColor", "#FF6200EE");
        try {
            int primaryColor = Color.parseColor(primaryColorStr);
            // Apply red color for expenses (negative amount)
            holder.amountTv.setTextColor(Color.parseColor("#D32F2F"));
        } catch (IllegalArgumentException e) {
            // Use default red color if parsing fails
            holder.amountTv.setTextColor(Color.parseColor("#D32F2F"));
        }
    }

    /**
     * ViewHolder class
     */
    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        
        CardView cardView;
        TextView expenseNameTv;
        TextView invoiceNoTv;
        TextView expenseCategoryTv;
        TextView amountTv;
        TextView dateTv;
        LinearLayout noteLayout;
        TextView noteTv;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            
            cardView = itemView.findViewById(R.id.card_view);
            expenseNameTv = itemView.findViewById(R.id.expense_name_tv);
            invoiceNoTv = itemView.findViewById(R.id.invoice_no_tv);
            expenseCategoryTv = itemView.findViewById(R.id.expense_category_tv);
            amountTv = itemView.findViewById(R.id.amount_tv);
            dateTv = itemView.findViewById(R.id.date_tv);
            noteLayout = itemView.findViewById(R.id.note_layout);
            noteTv = itemView.findViewById(R.id.note_tv);
        }
    }
}

