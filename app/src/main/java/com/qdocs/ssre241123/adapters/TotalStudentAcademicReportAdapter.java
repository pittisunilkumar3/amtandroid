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
import com.qdocs.ssre241123.model.TotalStudentAcademicReportModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying Total Student Academic Report
 * Shows student fee summary with total fee, deposit, discount, fine, and balance
 */
public class TotalStudentAcademicReportAdapter extends RecyclerView.Adapter<TotalStudentAcademicReportAdapter.ViewHolder> {

    private Context context;
    private List<TotalStudentAcademicReportModel> studentList;
    private String currency;
    private NumberFormat numberFormat;

    public TotalStudentAcademicReportAdapter(Context context, List<TotalStudentAcademicReportModel> studentList) {
        this.context = context;
        this.studentList = studentList;
        this.currency = Utility.getSharedPreferences(context, Constants.currency);
        if (currency == null || currency.isEmpty()) {
            currency = "₹";
        }
        this.numberFormat = NumberFormat.getInstance(new Locale("en", "IN"));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_total_student_academic_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TotalStudentAcademicReportModel student = studentList.get(position);

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
        holder.studentNameTv.setText(student.getName());
        holder.admissionNoTv.setText("Adm No: " + student.getAdmissionNo());
        holder.classSecTv.setText(student.getClassSection());
        holder.rollNoTv.setText("Roll No: " + student.getRollNo());
        holder.fatherNameTv.setText("Father: " + student.getFatherName());

        // Format and set fee amounts
        try {
            double totalFee = student.getTotalFeeDouble();
            double deposit = student.getDepositDouble();
            double discount = student.getDiscountDouble();
            double fine = student.getFineDouble();
            double balance = student.getBalanceDouble();

            holder.totalFeeTv.setText(currency + " " + numberFormat.format(totalFee));
            holder.depositTv.setText(currency + " " + numberFormat.format(deposit));
            holder.discountTv.setText(currency + " " + numberFormat.format(discount));
            holder.fineTv.setText(currency + " " + numberFormat.format(fine));
            holder.balanceTv.setText(currency + " " + numberFormat.format(balance));

            // Color code balance - red for due, green for paid/zero
            if (balance > 0) {
                holder.balanceTv.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            } else {
                holder.balanceTv.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
            }
        } catch (Exception e) {
            // If formatting fails, display raw values
            holder.totalFeeTv.setText(currency + " " + student.getTotalFee());
            holder.depositTv.setText(currency + " " + student.getDeposit());
            holder.discountTv.setText(currency + " " + student.getDiscount());
            holder.fineTv.setText(currency + " " + student.getFine());
            holder.balanceTv.setText(currency + " " + student.getBalance());
        }
    }

    @Override
    public int getItemCount() {
        return studentList != null ? studentList.size() : 0;
    }

    public void updateData(List<TotalStudentAcademicReportModel> newStudentList) {
        this.studentList = newStudentList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout headerLayout;
        TextView studentNameTv;
        TextView admissionNoTv;
        TextView classSecTv;
        TextView rollNoTv;
        TextView fatherNameTv;
        TextView totalFeeTv;
        TextView depositTv;
        TextView discountTv;
        TextView fineTv;
        TextView balanceTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            headerLayout = itemView.findViewById(R.id.headerLayout);
            studentNameTv = itemView.findViewById(R.id.studentNameTv);
            admissionNoTv = itemView.findViewById(R.id.admissionNoTv);
            classSecTv = itemView.findViewById(R.id.classSecTv);
            rollNoTv = itemView.findViewById(R.id.rollNoTv);
            fatherNameTv = itemView.findViewById(R.id.fatherNameTv);
            totalFeeTv = itemView.findViewById(R.id.totalFeeTv);
            depositTv = itemView.findViewById(R.id.depositTv);
            discountTv = itemView.findViewById(R.id.discountTv);
            fineTv = itemView.findViewById(R.id.fineTv);
            balanceTv = itemView.findViewById(R.id.balanceTv);
        }
    }
}

