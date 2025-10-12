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
import com.qdocs.ssre241123.model.UserLogModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import java.util.List;

/**
 * Adapter for User Log RecyclerView
 * Displays user login activity records
 */
public class UserLogAdapter extends RecyclerView.Adapter<UserLogAdapter.UserLogViewHolder> {

    private Context context;
    private List<UserLogModel> userLogList;

    public UserLogAdapter(Context context, List<UserLogModel> userLogList) {
        this.context = context;
        this.userLogList = userLogList;
    }

    @NonNull
    @Override
    public UserLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_user_log_item, parent, false);
        return new UserLogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserLogViewHolder holder, int position) {
        UserLogModel userLog = userLogList.get(position);

        // Set user name
        holder.userNameTv.setText(userLog.getFormattedUser());

        // Set role with badge
        holder.roleBadgeTv.setText(userLog.getFormattedRole());

        // Set role badge color based on role
        String role = userLog.getRole();
        if (role != null) {
            if (role.equalsIgnoreCase("student")) {
                holder.roleBadgeTv.setBackgroundColor(Color.parseColor("#4CAF50")); // Green
            } else if (role.equalsIgnoreCase("parent")) {
                holder.roleBadgeTv.setBackgroundColor(Color.parseColor("#2196F3")); // Blue
            } else if (role.equalsIgnoreCase("teacher")) {
                holder.roleBadgeTv.setBackgroundColor(Color.parseColor("#FF9800")); // Orange
            } else if (role.equalsIgnoreCase("super admin") || role.equalsIgnoreCase("admin")) {
                holder.roleBadgeTv.setBackgroundColor(Color.parseColor("#F44336")); // Red
            } else {
                holder.roleBadgeTv.setBackgroundColor(Color.parseColor("#9E9E9E")); // Gray
            }
        }

        // Set class/section (only visible for students)
        String classSection = userLog.getFormattedClassSection();
        if (classSection != null && !classSection.equals("-")) {
            holder.classSectionLayout.setVisibility(View.VISIBLE);
            holder.classSectionTv.setText(classSection);
        } else {
            holder.classSectionLayout.setVisibility(View.GONE);
        }

        // Set date and time
        holder.dateTimeTv.setText(userLog.getFormattedDateTime());

        // Set IP address
        holder.ipAddressTv.setText(userLog.getFormattedIpAddress());

        // Set device info
        String deviceInfo = userLog.getDeviceInfo() + " - " + userLog.getBrowserInfo();
        holder.deviceInfoTv.setText(deviceInfo);

        // Apply theme colors
        String primaryColor = Utility.getSharedPreferences(context, Constants.primaryColour);
        if (primaryColor != null && !primaryColor.isEmpty()) {
            try {
                // Apply primary color to user icon tint (if needed)
                // holder.userIconIv.setColorFilter(Color.parseColor(primaryColor));
            } catch (Exception e) {
                // Use default colors if parsing fails
            }
        }
    }

    @Override
    public int getItemCount() {
        return userLogList.size();
    }

    public static class UserLogViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTv;
        TextView roleBadgeTv;
        LinearLayout classSectionLayout;
        TextView classSectionTv;
        TextView dateTimeTv;
        TextView ipAddressTv;
        TextView deviceInfoTv;

        public UserLogViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTv = itemView.findViewById(R.id.user_name_tv);
            roleBadgeTv = itemView.findViewById(R.id.role_badge_tv);
            classSectionLayout = itemView.findViewById(R.id.class_section_layout);
            classSectionTv = itemView.findViewById(R.id.class_section_tv);
            dateTimeTv = itemView.findViewById(R.id.date_time_tv);
            ipAddressTv = itemView.findViewById(R.id.ip_address_tv);
            deviceInfoTv = itemView.findViewById(R.id.device_info_tv);
        }
    }
}