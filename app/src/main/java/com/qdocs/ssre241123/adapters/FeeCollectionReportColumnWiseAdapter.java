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
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for Fee Collection Report Column Wise
 * Displays collection data in a column-wise format (table-like structure)
 */
public class FeeCollectionReportColumnWiseAdapter extends RecyclerView.Adapter<FeeCollectionReportColumnWiseAdapter.ViewHolder> {

    private Context context;
    private List<CollectionReportColumnWiseModel> reportList;
    private String currency;

    public FeeCollectionReportColumnWiseAdapter(Context context, List<CollectionReportColumnWiseModel> reportList) {
        this.context = context;
        this.reportList = reportList;
        this.currency = Utility.getSharedPreferences(context, Constants.currency);
        if (this.currency == null || this.currency.isEmpty()) {
            this.currency = "₹";
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fee_collection_report_column_wise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CollectionReportColumnWiseModel model = reportList.get(position);

        // Apply theme color to header
        String primaryColor = Utility.getSharedPreferences(context, Constants.primaryColour);
        if (primaryColor != null && !primaryColor.isEmpty()) {
            try {
                holder.headerLayout.setBackgroundColor(Color.parseColor(primaryColor));
            } catch (Exception e) {
                // Use default color if parsing fails
            }
        }

        // Set invoice number and date in header
        if (model.invNo != null && !model.invNo.isEmpty()) {
            holder.invoiceNoTv.setText("Invoice: " + model.invNo);
        } else {
            holder.invoiceNoTv.setText("ID: " + model.id);
        }
        holder.dateTv.setText(formatDate(model.date));

        // Set student information
        holder.studentNameTv.setText(model.getFullName());
        
        if (model.admissionNo != null && !model.admissionNo.isEmpty()) {
            holder.admissionNoTv.setText(model.admissionNo);
        } else {
            holder.admissionNoTv.setText("-");
        }

        String classSection = model.getClassSection();
        if (!classSection.isEmpty()) {
            holder.classSectionTv.setText(classSection);
        } else {
            holder.classSectionTv.setText("-");
        }

        // Set fee information
        if (model.feeType != null && !model.feeType.isEmpty()) {
            holder.feeTypeTv.setText(model.feeType);
        } else {
            holder.feeTypeTv.setText("-");
        }

        if (model.feeCode != null && !model.feeCode.isEmpty()) {
            holder.feeCodeTv.setText(model.feeCode);
        } else {
            holder.feeCodeTv.setText("-");
        }

        if (model.feeGroup != null && !model.feeGroup.isEmpty()) {
            holder.feeGroupTv.setText(model.feeGroup);
        } else {
            holder.feeGroupTv.setText("-");
        }

        // Set amount details
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("en", "IN"));
        
        try {
            double amount = Double.parseDouble(model.amount);
            holder.amountTv.setText(currency + " " + numberFormat.format(amount));
        } catch (Exception e) {
            holder.amountTv.setText(currency + " 0.00");
        }

        try {
            double discount = Double.parseDouble(model.amountDiscount);
            holder.discountTv.setText(currency + " " + numberFormat.format(discount));
        } catch (Exception e) {
            holder.discountTv.setText(currency + " 0.00");
        }

        try {
            double fine = Double.parseDouble(model.amountFine);
            holder.fineTv.setText(currency + " " + numberFormat.format(fine));
        } catch (Exception e) {
            holder.fineTv.setText(currency + " 0.00");
        }

        // Set total amount
        double totalAmount = model.getTotalAmount();
        holder.totalAmountTv.setText(currency + " " + numberFormat.format(totalAmount));

        // Set payment information
        if (model.paymentMode != null && !model.paymentMode.isEmpty()) {
            holder.paymentModeTv.setText(model.paymentMode);
        } else {
            holder.paymentModeTv.setText("-");
        }

        if (model.receivedBy != null && !model.receivedBy.isEmpty()) {
            holder.receivedByTv.setText(model.receivedBy);
        } else {
            holder.receivedByTv.setText("-");
        }

        // Set description (optional)
        if (model.description != null && !model.description.isEmpty()) {
            holder.descriptionTv.setText(model.description);
            holder.descriptionTv.setVisibility(View.VISIBLE);
        } else {
            holder.descriptionTv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    /**
     * Format date from YYYY-MM-DD to MMM DD, YYYY
     */
    private String formatDate(String date) {
        if (date == null || date.isEmpty()) {
            return "";
        }

        try {
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
        TextView classSectionTv;
        TextView feeTypeTv;
        TextView feeCodeTv;
        TextView feeGroupTv;
        TextView amountTv;
        TextView discountTv;
        TextView fineTv;
        TextView totalAmountTv;
        TextView paymentModeTv;
        TextView receivedByTv;
        TextView descriptionTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            headerLayout = itemView.findViewById(R.id.headerLayout);
            invoiceNoTv = itemView.findViewById(R.id.invoiceNoTv);
            dateTv = itemView.findViewById(R.id.dateTv);
            studentNameTv = itemView.findViewById(R.id.studentNameTv);
            admissionNoTv = itemView.findViewById(R.id.admissionNoTv);
            classSectionTv = itemView.findViewById(R.id.classSectionTv);
            feeTypeTv = itemView.findViewById(R.id.feeTypeTv);
            feeCodeTv = itemView.findViewById(R.id.feeCodeTv);
            feeGroupTv = itemView.findViewById(R.id.feeGroupTv);
            amountTv = itemView.findViewById(R.id.amountTv);
            discountTv = itemView.findViewById(R.id.discountTv);
            fineTv = itemView.findViewById(R.id.fineTv);
            totalAmountTv = itemView.findViewById(R.id.totalAmountTv);
            paymentModeTv = itemView.findViewById(R.id.paymentModeTv);
            receivedByTv = itemView.findViewById(R.id.receivedByTv);
            descriptionTv = itemView.findViewById(R.id.descriptionTv);
        }
    }

    /**
     * Model class for Column-wise Collection Report
     */
    public static class CollectionReportColumnWiseModel {
        public String id;
        public String invNo;
        public String date;
        public String admissionNo;
        public String firstname;
        public String middlename;
        public String lastname;
        public String className;
        public String section;
        public String feeType;
        public String feeCode;
        public String feeGroup;
        public String amount;
        public String amountDiscount;
        public String amountFine;
        public String paymentMode;
        public String receivedBy;
        public String description;

        public String getFullName() {
            StringBuilder name = new StringBuilder();
            if (firstname != null && !firstname.isEmpty()) {
                name.append(firstname);
            }
            if (middlename != null && !middlename.isEmpty()) {
                if (name.length() > 0) name.append(" ");
                name.append(middlename);
            }
            if (lastname != null && !lastname.isEmpty()) {
                if (name.length() > 0) name.append(" ");
                name.append(lastname);
            }
            return name.toString();
        }

        public String getClassSection() {
            if (className != null && !className.isEmpty() && section != null && !section.isEmpty()) {
                return className + " - " + section;
            } else if (className != null && !className.isEmpty()) {
                return className;
            } else if (section != null && !section.isEmpty()) {
                return section;
            }
            return "";
        }

        public double getTotalAmount() {
            try {
                double amt = Double.parseDouble(amount);
                double discount = Double.parseDouble(amountDiscount);
                double fine = Double.parseDouble(amountFine);
                return amt - discount + fine;
            } catch (Exception e) {
                return 0.0;
            }
        }
    }
}

