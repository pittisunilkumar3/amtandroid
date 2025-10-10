package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.utils.Utility;
import com.qdocs.ssre241123.utils.Constants;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Adapter for displaying search results in the search dialog
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private Context context;
    private List<SearchResultItem> searchResults;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(SearchResultItem item);
    }

    public SearchResultAdapter(Context context, List<SearchResultItem> searchResults, OnItemClickListener listener) {
        this.context = context;
        this.searchResults = searchResults;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchResultItem item = searchResults.get(position);

        holder.studentNameTextView.setText(item.getFullName());
        holder.admissionNoTextView.setText(item.getAdmissionNo());
        holder.classTextView.setText(item.getClassSection());
        holder.rollNoTextView.setText(item.getRollNo());

        // Format currency
        String currency = Utility.getSharedPreferences(context, Constants.currency);
        if (currency == null || currency.isEmpty()) {
            currency = "₹";
        }

        DecimalFormat df = new DecimalFormat("#,##0.00");
        holder.totalFeeTextView.setText(currency + " " + df.format(item.getTotalFee()));
        holder.paidTextView.setText(currency + " " + df.format(item.getDeposit()));
        holder.balanceTextView.setText(currency + " " + df.format(item.getBalance()));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentNameTextView;
        TextView admissionNoTextView;
        TextView classTextView;
        TextView rollNoTextView;
        TextView totalFeeTextView;
        TextView paidTextView;
        TextView balanceTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentNameTextView = itemView.findViewById(R.id.studentNameTextView);
            admissionNoTextView = itemView.findViewById(R.id.admissionNoTextView);
            classTextView = itemView.findViewById(R.id.classTextView);
            rollNoTextView = itemView.findViewById(R.id.rollNoTextView);
            totalFeeTextView = itemView.findViewById(R.id.totalFeeTextView);
            paidTextView = itemView.findViewById(R.id.paidTextView);
            balanceTextView = itemView.findViewById(R.id.balanceTextView);
        }
    }

    /**
     * Data class for search result items
     */
    public static class SearchResultItem {
        private String studentId;
        private String admissionNo;
        private String firstname;
        private String middlename;
        private String lastname;
        private String fullName;
        private String classId;
        private String className;
        private String sectionId;
        private String section;
        private String rollNo;
        private String fatherName;
        private double totalFee;
        private double deposit;
        private double discount;
        private double fine;
        private double balance;

        public String getStudentId() {
            return studentId;
        }

        public void setStudentId(String studentId) {
            this.studentId = studentId;
        }

        public String getAdmissionNo() {
            return admissionNo;
        }

        public void setAdmissionNo(String admissionNo) {
            this.admissionNo = admissionNo;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getMiddlename() {
            return middlename;
        }

        public void setMiddlename(String middlename) {
            this.middlename = middlename;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getSectionId() {
            return sectionId;
        }

        public void setSectionId(String sectionId) {
            this.sectionId = sectionId;
        }

        public String getSection() {
            return section;
        }

        public void setSection(String section) {
            this.section = section;
        }

        public String getRollNo() {
            return rollNo;
        }

        public void setRollNo(String rollNo) {
            this.rollNo = rollNo;
        }

        public String getFatherName() {
            return fatherName;
        }

        public void setFatherName(String fatherName) {
            this.fatherName = fatherName;
        }

        public double getTotalFee() {
            return totalFee;
        }

        public void setTotalFee(double totalFee) {
            this.totalFee = totalFee;
        }

        public double getDeposit() {
            return deposit;
        }

        public void setDeposit(double deposit) {
            this.deposit = deposit;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public double getFine() {
            return fine;
        }

        public void setFine(double fine) {
            this.fine = fine;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public String getClassSection() {
            if (section != null && !section.isEmpty()) {
                return className + " - " + section;
            }
            return className;
        }
    }
}

