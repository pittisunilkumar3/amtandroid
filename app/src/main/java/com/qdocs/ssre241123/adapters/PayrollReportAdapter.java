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
import com.qdocs.ssre241123.model.PayrollReportModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for Payroll Report RecyclerView
 */
public class PayrollReportAdapter extends RecyclerView.Adapter<PayrollReportAdapter.PayrollViewHolder> {

    private Context context;
    private List<PayrollReportModel> payrollList;
    private String currency;

    public PayrollReportAdapter(Context context, List<PayrollReportModel> payrollList) {
        this.context = context;
        this.payrollList = payrollList;
        
        // Get currency from shared preferences
        this.currency = Utility.getSharedPreferences(context.getApplicationContext(), Constants.currency);
        if (currency == null || currency.isEmpty()) {
            currency = "₹";
        }
    }

    @NonNull
    @Override
    public PayrollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_payroll_report, parent, false);
        return new PayrollViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PayrollViewHolder holder, int position) {
        PayrollReportModel payroll = payrollList.get(position);

        // Set staff name
        holder.staffNameTv.setText(payroll.getName());

        // Set employee ID
        String employeeIdText = "Employee ID: " + payroll.getEmployeeId();
        holder.employeeIdTv.setText(employeeIdText);

        // Set role and designation
        String roleDesignation = payroll.getRole() + " - " + payroll.getDesignation();
        holder.roleDesignationTv.setText(roleDesignation);

        // Set period (Month Year)
        String period = payroll.getMonth() + " " + payroll.getYear();
        holder.periodTv.setText(period);

        // Format and set basic salary
        holder.basicSalaryTv.setText(formatCurrency(payroll.getBasicSalary()));

        // Format and set earnings
        holder.earningsTv.setText(formatCurrency(payroll.getEarnings()));

        // Format and set deductions
        holder.deductionsTv.setText(formatCurrency(payroll.getDeductions()));

        // Format and set net salary
        holder.netSalaryTv.setText(formatCurrency(payroll.getNetSalary()));

        // Format and set payment date
        if (payroll.getPaymentDate() != null && !payroll.getPaymentDate().isEmpty()) {
            String formattedDate = formatDate(payroll.getPaymentDate());
            holder.paymentDateTv.setText("Paid on: " + formattedDate);
        } else {
            holder.paymentDateTv.setText("Payment Date: N/A");
        }

        // Set status
        setStatus(holder, payroll.getStatus());

        // Apply theme colors
        applyThemeColors(holder);
    }

    @Override
    public int getItemCount() {
        return payrollList.size();
    }

    /**
     * Format currency with locale-specific formatting
     */
    private String formatCurrency(String amount) {
        try {
            double value = Double.parseDouble(amount);
            NumberFormat numberFormat = NumberFormat.getInstance(new Locale("en", "IN"));
            return currency + " " + numberFormat.format(value);
        } catch (NumberFormatException e) {
            return currency + " " + amount;
        }
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
     * Set status with appropriate background color
     */
    private void setStatus(PayrollViewHolder holder, String status) {
        holder.statusTv.setText(status);
        
        // Set background based on status
        if ("Paid".equalsIgnoreCase(status) || "paid".equalsIgnoreCase(status)) {
            holder.statusTv.setBackgroundResource(R.drawable.status_approved_bg);
        } else if ("Pending".equalsIgnoreCase(status) || "pending".equalsIgnoreCase(status)) {
            holder.statusTv.setBackgroundResource(R.drawable.status_pending_bg);
        } else if ("Generated".equalsIgnoreCase(status) || "generated".equalsIgnoreCase(status)) {
            holder.statusTv.setBackgroundResource(R.drawable.status_generated_bg);
        } else {
            holder.statusTv.setBackgroundResource(R.drawable.status_pending_bg);
        }
    }

    /**
     * Apply theme colors from shared preferences
     */
    private void applyThemeColors(PayrollViewHolder holder) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        
        // Get primary color
        String primaryColorStr = sharedPreferences.getString("primaryColor", "#FF6200EE");
        try {
            int primaryColor = Color.parseColor(primaryColorStr);
            // Apply green color for net salary (positive amount)
            holder.netSalaryTv.setTextColor(Color.parseColor("#4CAF50"));
            // Apply green color for earnings
            holder.earningsTv.setTextColor(Color.parseColor("#4CAF50"));
            // Apply red color for deductions
            holder.deductionsTv.setTextColor(Color.parseColor("#D32F2F"));
        } catch (IllegalArgumentException e) {
            // Use default colors if parsing fails
            holder.netSalaryTv.setTextColor(Color.parseColor("#4CAF50"));
            holder.earningsTv.setTextColor(Color.parseColor("#4CAF50"));
            holder.deductionsTv.setTextColor(Color.parseColor("#D32F2F"));
        }
    }

    /**
     * ViewHolder class
     */
    public static class PayrollViewHolder extends RecyclerView.ViewHolder {
        
        CardView cardView;
        TextView staffNameTv;
        TextView employeeIdTv;
        TextView roleDesignationTv;
        TextView netSalaryTv;
        TextView periodTv;
        TextView basicSalaryTv;
        TextView earningsTv;
        TextView deductionsTv;
        TextView paymentDateTv;
        TextView statusTv;

        public PayrollViewHolder(@NonNull View itemView) {
            super(itemView);
            
            cardView = itemView.findViewById(R.id.card_view);
            staffNameTv = itemView.findViewById(R.id.staff_name_tv);
            employeeIdTv = itemView.findViewById(R.id.employee_id_tv);
            roleDesignationTv = itemView.findViewById(R.id.role_designation_tv);
            netSalaryTv = itemView.findViewById(R.id.net_salary_tv);
            periodTv = itemView.findViewById(R.id.period_tv);
            basicSalaryTv = itemView.findViewById(R.id.basic_salary_tv);
            earningsTv = itemView.findViewById(R.id.earnings_tv);
            deductionsTv = itemView.findViewById(R.id.deductions_tv);
            paymentDateTv = itemView.findViewById(R.id.payment_date_tv);
            statusTv = itemView.findViewById(R.id.status_tv);
        }
    }
}

