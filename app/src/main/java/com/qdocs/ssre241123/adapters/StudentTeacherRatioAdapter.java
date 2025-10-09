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
import com.qdocs.ssre241123.model.StudentTeacherRatioModel;

import java.util.List;

public class StudentTeacherRatioAdapter extends RecyclerView.Adapter<StudentTeacherRatioAdapter.ViewHolder> {

    private Context context;
    private List<StudentTeacherRatioModel> ratioList;

    public StudentTeacherRatioAdapter(Context context, List<StudentTeacherRatioModel> ratioList) {
        this.context = context;
        this.ratioList = ratioList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_teacher_ratio, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentTeacherRatioModel ratio = ratioList.get(position);

        // Set class-section
        holder.classSectionTv.setText(ratio.getClassSection());

        // Set total students
        holder.totalStudentsTv.setText(ratio.getTotalStudent());

        // Set male students
        holder.maleTv.setText(ratio.getMale());

        // Set female students
        holder.femaleTv.setText(ratio.getFemale());

        // Set total teachers
        holder.totalTeachersTv.setText(ratio.getTotalTeacher());

        // Set boys-girls ratio
        if (ratio.getBoysGirlsRatio() != null && !ratio.getBoysGirlsRatio().isEmpty()) {
            holder.boysGirlsRatioTv.setText(ratio.getBoysGirlsRatio());
            holder.boysGirlsRatioTv.setVisibility(View.VISIBLE);
        } else {
            holder.boysGirlsRatioTv.setVisibility(View.GONE);
        }

        // Set student-teacher ratio
        if (ratio.getTeacherRatio() != null && !ratio.getTeacherRatio().isEmpty()) {
            holder.studentTeacherRatioTv.setText(ratio.getTeacherRatio());
            holder.studentTeacherRatioTv.setVisibility(View.VISIBLE);
        } else {
            holder.studentTeacherRatioTv.setVisibility(View.GONE);
        }

        // Set class ID
        if (ratio.getClassId() != null && !ratio.getClassId().isEmpty()) {
            holder.classIdTv.setText("Class ID: " + ratio.getClassId());
            holder.classIdTv.setVisibility(View.VISIBLE);
        } else {
            holder.classIdTv.setVisibility(View.GONE);
        }

        // Set section ID
        if (ratio.getSectionId() != null && !ratio.getSectionId().isEmpty()) {
            holder.sectionIdTv.setText("Section ID: " + ratio.getSectionId());
            holder.sectionIdTv.setVisibility(View.VISIBLE);
        } else {
            holder.sectionIdTv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return ratioList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView classSectionTv;
        TextView totalStudentsTv;
        TextView maleTv;
        TextView femaleTv;
        TextView totalTeachersTv;
        TextView boysGirlsRatioTv;
        TextView studentTeacherRatioTv;
        TextView classIdTv;
        TextView sectionIdTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            classSectionTv = itemView.findViewById(R.id.class_section_tv);
            totalStudentsTv = itemView.findViewById(R.id.total_students_tv);
            maleTv = itemView.findViewById(R.id.male_tv);
            femaleTv = itemView.findViewById(R.id.female_tv);
            totalTeachersTv = itemView.findViewById(R.id.total_teachers_tv);
            boysGirlsRatioTv = itemView.findViewById(R.id.boys_girls_ratio_tv);
            studentTeacherRatioTv = itemView.findViewById(R.id.student_teacher_ratio_tv);
            classIdTv = itemView.findViewById(R.id.class_id_tv);
            sectionIdTv = itemView.findViewById(R.id.section_id_tv);
        }
    }
}

