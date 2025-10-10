package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying Fees Statement report
 */
public class FeesStatementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_FEE_GROUP = 1;
    private static final int VIEW_TYPE_SUMMARY = 2;

    private Context context;
    private List<Object> items;
    private DecimalFormat currencyFormat;

    public FeesStatementAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<>();
        this.currencyFormat = new DecimalFormat("₹#,##0.00");
    }

    public void setData(StudentHeader header, List<FeeGroup> feeGroups, FeeSummary summary) {
        items.clear();
        if (header != null) {
            items.add(header);
        }
        if (feeGroups != null) {
            items.addAll(feeGroups);
        }
        if (summary != null) {
            items.add(summary);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        if (item instanceof StudentHeader) {
            return VIEW_TYPE_HEADER;
        } else if (item instanceof FeeGroup) {
            return VIEW_TYPE_FEE_GROUP;
        } else if (item instanceof FeeSummary) {
            return VIEW_TYPE_SUMMARY;
        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                View headerView = inflater.inflate(R.layout.item_fee_statement_header, parent, false);
                return new HeaderViewHolder(headerView);
            case VIEW_TYPE_FEE_GROUP:
                View groupView = inflater.inflate(R.layout.item_fee_statement_group, parent, false);
                return new FeeGroupViewHolder(groupView);
            case VIEW_TYPE_SUMMARY:
                View summaryView = inflater.inflate(R.layout.item_fee_statement_summary, parent, false);
                return new SummaryViewHolder(summaryView);
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = items.get(position);

        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bind((StudentHeader) item);
        } else if (holder instanceof FeeGroupViewHolder) {
            ((FeeGroupViewHolder) holder).bind((FeeGroup) item);
        } else if (holder instanceof SummaryViewHolder) {
            ((SummaryViewHolder) holder).bind((FeeSummary) item);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // ViewHolder for Student Header
    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView studentNameTextView;
        TextView admissionNoTextView;
        TextView classTextView;
        TextView rollNoTextView;
        TextView fatherNameTextView;

        HeaderViewHolder(View itemView) {
            super(itemView);
            studentNameTextView = itemView.findViewById(R.id.studentNameTextView);
            admissionNoTextView = itemView.findViewById(R.id.admissionNoTextView);
            classTextView = itemView.findViewById(R.id.classTextView);
            rollNoTextView = itemView.findViewById(R.id.rollNoTextView);
            fatherNameTextView = itemView.findViewById(R.id.fatherNameTextView);
        }

        void bind(StudentHeader header) {
            studentNameTextView.setText(header.studentName);
            admissionNoTextView.setText(header.admissionNo);
            classTextView.setText(header.className + " - " + header.section);
            rollNoTextView.setText(header.rollNo);
            fatherNameTextView.setText(header.fatherName);
        }
    }

    // ViewHolder for Fee Group
    class FeeGroupViewHolder extends RecyclerView.ViewHolder {
        TextView feeGroupNameTextView;
        LinearLayout feeTypesContainer;

        FeeGroupViewHolder(View itemView) {
            super(itemView);
            feeGroupNameTextView = itemView.findViewById(R.id.feeGroupNameTextView);
            feeTypesContainer = itemView.findViewById(R.id.feeTypesContainer);
        }

        void bind(FeeGroup feeGroup) {
            feeGroupNameTextView.setText(feeGroup.groupName);
            feeTypesContainer.removeAllViews();

            for (FeeType feeType : feeGroup.feeTypes) {
                View feeTypeView = LayoutInflater.from(context).inflate(
                    R.layout.item_fee_statement_type, feeTypesContainer, false);

                TextView feeTypeNameTextView = feeTypeView.findViewById(R.id.feeTypeNameTextView);
                TextView amountTextView = feeTypeView.findViewById(R.id.amountTextView);
                TextView paidAmountTextView = feeTypeView.findViewById(R.id.paidAmountTextView);
                TextView discountTextView = feeTypeView.findViewById(R.id.discountTextView);
                TextView fineTextView = feeTypeView.findViewById(R.id.fineTextView);
                TextView balanceTextView = feeTypeView.findViewById(R.id.balanceTextView);
                TextView dueDateTextView = feeTypeView.findViewById(R.id.dueDateTextView);
                LinearLayout discountRow = feeTypeView.findViewById(R.id.discountRow);
                LinearLayout fineRow = feeTypeView.findViewById(R.id.fineRow);
                LinearLayout dueDateRow = feeTypeView.findViewById(R.id.dueDateRow);
                Button viewPaymentsButton = feeTypeView.findViewById(R.id.viewPaymentsButton);

                feeTypeNameTextView.setText(feeType.typeName);
                amountTextView.setText(currencyFormat.format(feeType.amount));
                paidAmountTextView.setText(currencyFormat.format(feeType.paidAmount));
                balanceTextView.setText(currencyFormat.format(feeType.balance));

                if (feeType.discount > 0) {
                    discountRow.setVisibility(View.VISIBLE);
                    discountTextView.setText(currencyFormat.format(feeType.discount));
                } else {
                    discountRow.setVisibility(View.GONE);
                }

                if (feeType.fine > 0) {
                    fineRow.setVisibility(View.VISIBLE);
                    fineTextView.setText(currencyFormat.format(feeType.fine));
                } else {
                    fineRow.setVisibility(View.GONE);
                }

                if (feeType.dueDate != null && !feeType.dueDate.isEmpty()) {
                    dueDateRow.setVisibility(View.VISIBLE);
                    dueDateTextView.setText(feeType.dueDate);
                } else {
                    dueDateRow.setVisibility(View.GONE);
                }

                if (feeType.hasPayments) {
                    viewPaymentsButton.setVisibility(View.VISIBLE);
                } else {
                    viewPaymentsButton.setVisibility(View.GONE);
                }

                feeTypesContainer.addView(feeTypeView);
            }
        }
    }

    // ViewHolder for Summary
    class SummaryViewHolder extends RecyclerView.ViewHolder {
        TextView totalFeeTextView;
        TextView totalPaidTextView;
        TextView totalDiscountTextView;
        TextView totalFineTextView;
        TextView totalBalanceTextView;
        LinearLayout totalDiscountRow;
        LinearLayout totalFineRow;

        SummaryViewHolder(View itemView) {
            super(itemView);
            totalFeeTextView = itemView.findViewById(R.id.totalFeeTextView);
            totalPaidTextView = itemView.findViewById(R.id.totalPaidTextView);
            totalDiscountTextView = itemView.findViewById(R.id.totalDiscountTextView);
            totalFineTextView = itemView.findViewById(R.id.totalFineTextView);
            totalBalanceTextView = itemView.findViewById(R.id.totalBalanceTextView);
            totalDiscountRow = itemView.findViewById(R.id.totalDiscountRow);
            totalFineRow = itemView.findViewById(R.id.totalFineRow);
        }

        void bind(FeeSummary summary) {
            totalFeeTextView.setText(currencyFormat.format(summary.totalFee));
            totalPaidTextView.setText(currencyFormat.format(summary.totalPaid));
            totalBalanceTextView.setText(currencyFormat.format(summary.totalBalance));

            if (summary.totalDiscount > 0) {
                totalDiscountRow.setVisibility(View.VISIBLE);
                totalDiscountTextView.setText(currencyFormat.format(summary.totalDiscount));
            } else {
                totalDiscountRow.setVisibility(View.GONE);
            }

            if (summary.totalFine > 0) {
                totalFineRow.setVisibility(View.VISIBLE);
                totalFineTextView.setText(currencyFormat.format(summary.totalFine));
            } else {
                totalFineRow.setVisibility(View.GONE);
            }
        }
    }

    // Data classes
    public static class StudentHeader {
        public String studentName;
        public String admissionNo;
        public String className;
        public String section;
        public String rollNo;
        public String fatherName;
    }

    public static class FeeGroup {
        public String groupName;
        public List<FeeType> feeTypes;
    }

    public static class FeeType {
        public String typeName;
        public double amount;
        public double paidAmount;
        public double discount;
        public double fine;
        public double balance;
        public String dueDate;
        public boolean hasPayments;
    }

    public static class FeeSummary {
        public double totalFee;
        public double totalPaid;
        public double totalDiscount;
        public double totalFine;
        public double totalBalance;
    }
}