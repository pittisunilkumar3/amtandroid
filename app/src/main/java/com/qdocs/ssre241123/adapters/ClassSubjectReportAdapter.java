package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.model.ClassSubjectReportModel;

import java.util.List;

public class ClassSubjectReportAdapter extends RecyclerView.Adapter<ClassSubjectReportAdapter.ViewHolder> {

    private Context context;
    private List<ClassSubjectReportModel> subjectList;

    public ClassSubjectReportAdapter(Context context, List<ClassSubjectReportModel> subjectList) {
        this.context = context;
        this.subjectList = subjectList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_class_subject_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClassSubjectReportModel subject = subjectList.get(position);

        // Set subject name with code
        holder.subjectNameTv.setText(subject.getSubjectWithCode());

        // Set class-section
        holder.classSectionTv.setText(subject.getClassSection());

        // Set teacher name
        holder.teacherNameTv.setText(subject.getTeacherFullName());

        // Set subject type
        if (subject.getSubjectType() != null && !subject.getSubjectType().isEmpty()) {
            holder.subjectTypeTv.setText(subject.getSubjectType());
            holder.subjectTypeTv.setVisibility(View.VISIBLE);
        } else {
            holder.subjectTypeTv.setVisibility(View.GONE);
        }

        // Set day and time
        if (subject.getDayTimeInfo() != null && !subject.getDayTimeInfo().isEmpty()) {
            holder.dayTimeTv.setText(subject.getDayTimeInfo());
            holder.dayTimeTv.setVisibility(View.VISIBLE);
        } else {
            holder.dayTimeTv.setVisibility(View.GONE);
        }

        // Set room number
        if (subject.getRoomNo() != null && !subject.getRoomNo().isEmpty()) {
            holder.roomNoTv.setText("Room: " + subject.getRoomNo());
            holder.roomNoTv.setVisibility(View.VISIBLE);
        } else {
            holder.roomNoTv.setVisibility(View.GONE);
        }

        // Set employee ID
        if (subject.getEmployeeId() != null && !subject.getEmployeeId().isEmpty()) {
            holder.employeeIdTv.setText("Emp ID: " + subject.getEmployeeId());
            holder.employeeIdTv.setVisibility(View.VISIBLE);
        } else {
            holder.employeeIdTv.setVisibility(View.GONE);
        }

        // Set subject ID
        if (subject.getSubjectId() != null && !subject.getSubjectId().isEmpty()) {
            holder.subjectIdTv.setText("Subject ID: " + subject.getSubjectId());
            holder.subjectIdTv.setVisibility(View.VISIBLE);
        } else {
            holder.subjectIdTv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView subjectNameTv;
        TextView classSectionTv;
        TextView teacherNameTv;
        TextView subjectTypeTv;
        TextView dayTimeTv;
        TextView roomNoTv;
        TextView employeeIdTv;
        TextView subjectIdTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            subjectNameTv = itemView.findViewById(R.id.subject_name_tv);
            classSectionTv = itemView.findViewById(R.id.class_section_tv);
            teacherNameTv = itemView.findViewById(R.id.teacher_name_tv);
            subjectTypeTv = itemView.findViewById(R.id.subject_type_tv);
            dayTimeTv = itemView.findViewById(R.id.day_time_tv);
            roomNoTv = itemView.findViewById(R.id.room_no_tv);
            employeeIdTv = itemView.findViewById(R.id.employee_id_tv);
            subjectIdTv = itemView.findViewById(R.id.subject_id_tv);
        }
    }
}

