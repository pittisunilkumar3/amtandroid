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
import com.qdocs.ssre241123.model.ClassSectionReportModel;

import java.util.List;

public class ClassSectionReportAdapter extends RecyclerView.Adapter<ClassSectionReportAdapter.ViewHolder> {

    private Context context;
    private List<ClassSectionReportModel> classSectionList;

    public ClassSectionReportAdapter(Context context, List<ClassSectionReportModel> classSectionList) {
        this.context = context;
        this.classSectionList = classSectionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_class_section_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClassSectionReportModel classSection = classSectionList.get(position);

        // Set class name
        if (classSection.getClassName() != null && !classSection.getClassName().isEmpty()) {
            holder.classNameTv.setText(classSection.getClassName());
            holder.classNameTv.setVisibility(View.VISIBLE);
        } else {
            holder.classNameTv.setVisibility(View.GONE);
        }

        // Set section name
        if (classSection.getSectionName() != null && !classSection.getSectionName().isEmpty()) {
            holder.sectionNameTv.setText(classSection.getSectionName());
            holder.sectionNameTv.setVisibility(View.VISIBLE);
        } else {
            holder.sectionNameTv.setVisibility(View.GONE);
        }

        // Set class-section combined
        holder.classSectionTv.setText(classSection.getClassSection());

        // Set student count
        String studentCountText = "Students: " + classSection.getStudentCount();
        holder.studentCountTv.setText(studentCountText);

        // Set class ID
        if (classSection.getClassId() != null && !classSection.getClassId().isEmpty()) {
            holder.classIdTv.setText("Class ID: " + classSection.getClassId());
            holder.classIdTv.setVisibility(View.VISIBLE);
        } else {
            holder.classIdTv.setVisibility(View.GONE);
        }

        // Set section ID
        if (classSection.getSectionId() != null && !classSection.getSectionId().isEmpty()) {
            holder.sectionIdTv.setText("Section ID: " + classSection.getSectionId());
            holder.sectionIdTv.setVisibility(View.VISIBLE);
        } else {
            holder.sectionIdTv.setVisibility(View.GONE);
        }

        // Set status indicator
        if (classSection.isActiveSection()) {
            holder.statusTv.setText("Active");
            holder.statusTv.setVisibility(View.VISIBLE);
        } else {
            holder.statusTv.setText("Inactive");
            holder.statusTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return classSectionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView classNameTv;
        TextView sectionNameTv;
        TextView classSectionTv;
        TextView studentCountTv;
        TextView classIdTv;
        TextView sectionIdTv;
        TextView statusTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            classNameTv = itemView.findViewById(R.id.class_name_tv);
            sectionNameTv = itemView.findViewById(R.id.section_name_tv);
            classSectionTv = itemView.findViewById(R.id.class_section_tv);
            studentCountTv = itemView.findViewById(R.id.student_count_tv);
            classIdTv = itemView.findViewById(R.id.class_id_tv);
            sectionIdTv = itemView.findViewById(R.id.section_id_tv);
            statusTv = itemView.findViewById(R.id.status_tv);
        }
    }
}

