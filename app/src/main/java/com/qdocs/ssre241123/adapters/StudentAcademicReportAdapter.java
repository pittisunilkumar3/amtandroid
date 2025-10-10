package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.model.StudentAcademicReportModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Adapter for displaying Student Academic Report
 */
public class StudentAcademicReportAdapter extends RecyclerView.Adapter<StudentAcademicReportAdapter.StudentViewHolder> {

    private Context context;
    private List<StudentAcademicReportModel> students;
    private DecimalFormat currencyFormat;
    private String currencySymbol;

    public StudentAcademicReportAdapter(Context context, List<StudentAcademicReportModel> students) {
        this.context = context;
        this.students = students;
        this.currencyFormat = new DecimalFormat("#,##0.00");
        this.currencySymbol = Utility.getSharedPreferences(context, Constants.currency);
        if (currencySymbol == null || currencySymbol.isEmpty()) {
            currencySymbol = "₹";
        }
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_academic_report, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        StudentAcademicReportModel student = students.get(position);

        // Apply theme color to header
        String secondaryColor = Utility.getSharedPreferences(context, Constants.secondaryColour);
        if (secondaryColor != null && !secondaryColor.isEmpty()) {
            try {
                holder.studentHeaderLayout.setBackgroundColor(Color.parseColor(secondaryColor));
            } catch (Exception e) {
                // Use default color if parsing fails
            }
        }

        // Set student information
        holder.studentNameTextView.setText(student.getFullName());
        holder.admissionNoTextView.setText(student.getAdmissionNo());
        holder.classTextView.setText(student.getClassSection());
        holder.rollNoTextView.setText(student.getRollNo());
        holder.fatherNameTextView.setText(student.getFatherName());

        // Set up fee details RecyclerView
        FeeDetailAdapter feeDetailAdapter = new FeeDetailAdapter(context, student.getFees());
        holder.feeDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.feeDetailsRecyclerView.setAdapter(feeDetailAdapter);

        // Calculate and display total balance
        double totalBalance = 0.0;
        if (student.getFees() != null) {
            for (StudentAcademicReportModel.FeeDetail fee : student.getFees()) {
                totalBalance += fee.getBalance();
            }
        }
        holder.totalBalanceTextView.setText(currencySymbol + " " + currencyFormat.format(totalBalance));
    }

    @Override
    public int getItemCount() {
        return students != null ? students.size() : 0;
    }

    public void updateData(List<StudentAcademicReportModel> newStudents) {
        this.students = newStudents;
        notifyDataSetChanged();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        LinearLayout studentHeaderLayout;
        TextView studentNameTextView;
        TextView admissionNoTextView;
        TextView classTextView;
        TextView rollNoTextView;
        TextView fatherNameTextView;
        RecyclerView feeDetailsRecyclerView;
        TextView totalBalanceTextView;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            studentHeaderLayout = itemView.findViewById(R.id.studentHeaderLayout);
            studentNameTextView = itemView.findViewById(R.id.studentNameTextView);
            admissionNoTextView = itemView.findViewById(R.id.admissionNoTextView);
            classTextView = itemView.findViewById(R.id.classTextView);
            rollNoTextView = itemView.findViewById(R.id.rollNoTextView);
            fatherNameTextView = itemView.findViewById(R.id.fatherNameTextView);
            feeDetailsRecyclerView = itemView.findViewById(R.id.feeDetailsRecyclerView);
            totalBalanceTextView = itemView.findViewById(R.id.totalBalanceTextView);
        }
    }
}

