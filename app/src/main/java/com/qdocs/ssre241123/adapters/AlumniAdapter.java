package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.model.AlumniModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import java.util.List;

public class AlumniAdapter extends RecyclerView.Adapter<AlumniAdapter.AlumniViewHolder> {

    private Context context;
    private List<AlumniModel> alumniList;

    public AlumniAdapter(Context context, List<AlumniModel> alumniList) {
        this.context = context;
        this.alumniList = alumniList;
    }

    @NonNull
    @Override
    public AlumniViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_alumni_item, parent, false);
        return new AlumniViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlumniViewHolder holder, int position) {
        AlumniModel alumni = alumniList.get(position);

        // Set student name
        holder.studentNameTv.setText(alumni.getFormattedName());

        // Set admission number
        holder.admissionNoTv.setText(alumni.getFormattedAdmissionNo());

        // Set pass out year badge
        holder.passOutYearBadge.setText(alumni.getFormattedPassOutYear());

        // Set class/section
        holder.classSectionTv.setText(alumni.getFormattedClassSection());

        // Set occupation
        holder.occupationTv.setText(alumni.getFormattedOccupation());

        // Set email
        holder.emailTv.setText(alumni.getFormattedEmail());

        // Set phone
        holder.phoneTv.setText(alumni.getFormattedPhone());

        // Apply theme color to badge
        String primaryColor = Utility.getSharedPreferences(context, Constants.primaryColour);
        if (primaryColor != null && !primaryColor.isEmpty()) {
            try {
                holder.passOutYearBadge.setBackgroundColor(Color.parseColor(primaryColor));
            } catch (Exception e) {
                // Use default color if parsing fails
            }
        }
    }

    @Override
    public int getItemCount() {
        return alumniList.size();
    }

    static class AlumniViewHolder extends RecyclerView.ViewHolder {
        TextView studentNameTv;
        TextView admissionNoTv;
        TextView passOutYearBadge;
        TextView classSectionTv;
        TextView occupationTv;
        TextView emailTv;
        TextView phoneTv;

        public AlumniViewHolder(@NonNull View itemView) {
            super(itemView);
            studentNameTv = itemView.findViewById(R.id.studentNameTv);
            admissionNoTv = itemView.findViewById(R.id.admissionNoTv);
            passOutYearBadge = itemView.findViewById(R.id.passOutYearBadge);
            classSectionTv = itemView.findViewById(R.id.classSectionTv);
            occupationTv = itemView.findViewById(R.id.occupationTv);
            emailTv = itemView.findViewById(R.id.emailTv);
            phoneTv = itemView.findViewById(R.id.phoneTv);
        }
    }
}

