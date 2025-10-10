package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.model.StudentAcademicReportModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Adapter for displaying fee details in Student Academic Report
 */
public class FeeDetailAdapter extends RecyclerView.Adapter<FeeDetailAdapter.FeeDetailViewHolder> {

    private Context context;
    private List<StudentAcademicReportModel.FeeDetail> feeDetails;
    private DecimalFormat currencyFormat;
    private String currencySymbol;

    public FeeDetailAdapter(Context context, List<StudentAcademicReportModel.FeeDetail> feeDetails) {
        this.context = context;
        this.feeDetails = feeDetails;
        this.currencyFormat = new DecimalFormat("#,##0.00");
        this.currencySymbol = Utility.getSharedPreferences(context, Constants.currency);
        if (currencySymbol == null || currencySymbol.isEmpty()) {
            currencySymbol = "₹";
        }
    }

    @NonNull
    @Override
    public FeeDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fee_detail, parent, false);
        return new FeeDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeeDetailViewHolder holder, int position) {
        StudentAcademicReportModel.FeeDetail feeDetail = feeDetails.get(position);

        // Set fee name
        holder.feeNameTextView.setText(feeDetail.getName());

        // Set amount
        holder.amountTextView.setText(currencySymbol + " " + currencyFormat.format(feeDetail.getAmountDouble()));

        // Set paid amount
        holder.paidAmountTextView.setText(currencySymbol + " " + currencyFormat.format(feeDetail.getAmountPaidDouble()));

        // Set discount (show only if > 0)
        double discount = feeDetail.getAmountDiscountDouble();
        if (discount > 0) {
            holder.discountRow.setVisibility(View.VISIBLE);
            holder.discountTextView.setText(currencySymbol + " " + currencyFormat.format(discount));
        } else {
            holder.discountRow.setVisibility(View.GONE);
        }

        // Set fine (show only if > 0)
        double fine = feeDetail.getAmountFineDouble();
        if (fine > 0) {
            holder.fineRow.setVisibility(View.VISIBLE);
            holder.fineTextView.setText(currencySymbol + " " + currencyFormat.format(fine));
        } else {
            holder.fineRow.setVisibility(View.GONE);
        }

        // Set balance
        double balance = feeDetail.getBalance();
        holder.balanceTextView.setText(currencySymbol + " " + currencyFormat.format(balance));
    }

    @Override
    public int getItemCount() {
        return feeDetails != null ? feeDetails.size() : 0;
    }

    public static class FeeDetailViewHolder extends RecyclerView.ViewHolder {
        TextView feeNameTextView;
        TextView amountTextView;
        TextView paidAmountTextView;
        TextView discountTextView;
        TextView fineTextView;
        TextView balanceTextView;
        LinearLayout discountRow;
        LinearLayout fineRow;

        public FeeDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            feeNameTextView = itemView.findViewById(R.id.feeNameTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            paidAmountTextView = itemView.findViewById(R.id.paidAmountTextView);
            discountTextView = itemView.findViewById(R.id.discountTextView);
            fineTextView = itemView.findViewById(R.id.fineTextView);
            balanceTextView = itemView.findViewById(R.id.balanceTextView);
            discountRow = itemView.findViewById(R.id.discountRow);
            fineRow = itemView.findViewById(R.id.fineRow);
        }
    }
}

