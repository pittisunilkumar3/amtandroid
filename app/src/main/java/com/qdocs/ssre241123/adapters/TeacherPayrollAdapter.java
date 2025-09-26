package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.models.TeacherPayrollRecord;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;
import java.util.List;

public class TeacherPayrollAdapter extends RecyclerView.Adapter<TeacherPayrollAdapter.PayrollViewHolder> {

    private Context context;
    private List<TeacherPayrollRecord> payrollRecords;
    private OnPayrollItemClickListener listener;

    public interface OnPayrollItemClickListener {
        void onViewPayslipClick(TeacherPayrollRecord record, int position);
    }

    public TeacherPayrollAdapter(Context context, List<TeacherPayrollRecord> payrollRecords) {
        this.context = context;
        this.payrollRecords = payrollRecords;
    }

    public void setOnPayrollItemClickListener(OnPayrollItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PayrollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_teacher_payroll, parent, false);
        return new PayrollViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PayrollViewHolder holder, int position) {
        TeacherPayrollRecord record = payrollRecords.get(position);
        
        // Set month and year
        String monthYear = record.getMonth() + " " + record.getYear();
        holder.monthYearTV.setText(monthYear);
        
        // Set payslip number
        holder.payslipNumberTV.setText(record.getId());
        
        // Set payment date
        String paymentDate = formatDate(record.getPaymentDate());
        holder.paymentDateTV.setText(paymentDate);
        
        // Set mode (default to bank transfer if not specified)
        String mode = record.getMode();
        if (mode == null || mode.isEmpty()) {
            mode = "Transfer to Bank Account";
        }
        holder.modeTV.setText(mode);
        
        // Set net salary with currency
        String currency = Utility.getSharedPreferences(context, Constants.currency);
        if (currency == null || currency.isEmpty()) {
            currency = "₹";
        }
        String netSalary = currency + formatAmount(record.getNetSalary());
        holder.netSalaryTV.setText(netSalary);
        
        // Set status with appropriate background
        String status = record.getStatus();
        holder.statusTV.setText(capitalizeFirst(status));
        setStatusBackground(holder.statusTV, status);
        
        // Set click listener for view button
        holder.viewButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onViewPayslipClick(record, position);
            }
        });
        
        // Set click listener for entire item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onViewPayslipClick(record, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return payrollRecords != null ? payrollRecords.size() : 0;
    }

    private String formatDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return "N/A";
        }
        
        try {
            // Try to format the date using utility method
            return Utility.parseDate("yyyy-MM-dd", 
                Utility.getSharedPreferences(context, "dateFormat"), 
                dateString);
        } catch (Exception e) {
            // If parsing fails, return the original string
            return dateString;
        }
    }

    private String formatAmount(String amount) {
        if (amount == null || amount.isEmpty()) {
            return "0.00";
        }
        
        try {
            double value = Double.parseDouble(amount);
            return String.format("%.2f", value);
        } catch (NumberFormatException e) {
            return amount;
        }
    }

    private String capitalizeFirst(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    private void setStatusBackground(TextView statusTV, String status) {
        int backgroundColor;
        
        switch (status.toLowerCase()) {
            case "generated":
                backgroundColor = ContextCompat.getColor(context, R.color.status_generated);
                break;
            case "paid":
                backgroundColor = ContextCompat.getColor(context, R.color.status_approved);
                break;
            case "pending":
                backgroundColor = ContextCompat.getColor(context, R.color.status_pending);
                break;
            case "cancelled":
            case "rejected":
                backgroundColor = ContextCompat.getColor(context, R.color.status_rejected);
                break;
            default:
                backgroundColor = ContextCompat.getColor(context, R.color.status_pending);
                break;
        }
        
        // Create rounded background
        GradientDrawable background = new GradientDrawable();
        background.setShape(GradientDrawable.RECTANGLE);
        background.setColor(backgroundColor);
        background.setCornerRadius(12f);
        
        statusTV.setBackground(background);
    }

    public static class PayrollViewHolder extends RecyclerView.ViewHolder {
        TextView monthYearTV, statusTV, payslipNumberTV, paymentDateTV, modeTV, netSalaryTV;
        Button viewButton;

        public PayrollViewHolder(@NonNull View itemView) {
            super(itemView);
            monthYearTV = itemView.findViewById(R.id.payroll_month_year);
            statusTV = itemView.findViewById(R.id.payroll_status);
            payslipNumberTV = itemView.findViewById(R.id.payroll_payslip_number);
            paymentDateTV = itemView.findViewById(R.id.payroll_payment_date);
            modeTV = itemView.findViewById(R.id.payroll_mode);
            netSalaryTV = itemView.findViewById(R.id.payroll_net_salary);
            viewButton = itemView.findViewById(R.id.payroll_view_button);
        }
    }

    public void updateData(List<TeacherPayrollRecord> newRecords) {
        this.payrollRecords = newRecords;
        notifyDataSetChanged();
    }
}
