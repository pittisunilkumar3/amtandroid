package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic adapter to display report items from JSON data.
 * Each item can show a title, subtitle, and detail line.
 * The mapping of JSON fields to views is configurable.
 */
public class GenericReportAdapter extends RecyclerView.Adapter<GenericReportAdapter.ViewHolder> {

    private Context context;
    private List<JSONObject> items = new ArrayList<>();
    private String titleField = "name";
    private String subtitleField = "";
    private String detailField = "";
    private String titlePrefix = "";
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(JSONObject item, int position);
    }

    public GenericReportAdapter(Context context) {
        this.context = context;
    }

    public GenericReportAdapter(Context context, String titleField) {
        this.context = context;
        this.titleField = titleField;
    }

    public GenericReportAdapter(Context context, String titleField, String subtitleField) {
        this.context = context;
        this.titleField = titleField;
        this.subtitleField = subtitleField != null ? subtitleField : "";
    }

    public GenericReportAdapter(Context context, String titleField, String subtitleField, String detailField) {
        this.context = context;
        this.titleField = titleField;
        this.subtitleField = subtitleField != null ? subtitleField : "";
        this.detailField = detailField != null ? detailField : "";
    }

    public void setData(JSONArray data) {
        items.clear();
        if (data != null) {
            for (int i = 0; i < data.length(); i++) {
                try {
                    items.add(data.getJSONObject(i));
                } catch (Exception e) {
                    // skip
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setTitlePrefix(String prefix) {
        this.titlePrefix = prefix;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setTitleField(String field) { this.titleField = field; }
    public void setSubtitleField(String field) { this.subtitleField = field; }
    public void setDetailField(String field) { this.detailField = field; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_generic_report_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JSONObject item = items.get(position);

        String title = getFieldValue(item, titleField);
        if (!titlePrefix.isEmpty()) {
            title = titlePrefix + " " + title;
        }
        holder.titleText.setText(title);

        if (!subtitleField.isEmpty()) {
            String subtitle = getFieldValue(item, subtitleField);
            if (!subtitle.isEmpty()) {
                holder.subtitleText.setText(subtitle);
                holder.subtitleText.setVisibility(View.VISIBLE);
            } else {
                holder.subtitleText.setVisibility(View.GONE);
            }
        } else {
            holder.subtitleText.setVisibility(View.GONE);
        }

        if (!detailField.isEmpty()) {
            String detail = getFieldValue(item, detailField);
            if (!detail.isEmpty()) {
                holder.detailText.setText(detail);
                holder.detailText.setVisibility(View.VISIBLE);
            } else {
                holder.detailText.setVisibility(View.GONE);
            }
        } else {
            holder.detailText.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item, position);
        });
    }

    private String getFieldValue(JSONObject obj, String field) {
        if (field == null || field.isEmpty()) return "";
        // Support dot notation like "class.class_name"
        String[] parts = field.split("\\.");
        JSONObject current = obj;
        for (int i = 0; i < parts.length - 1; i++) {
            current = current.optJSONObject(parts[i]);
            if (current == null) return "";
        }
        return current.optString(parts[parts.length - 1], "");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        TextView subtitleText;
        TextView detailText;

        ViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            subtitleText = itemView.findViewById(R.id.subtitleText);
            detailText = itemView.findViewById(R.id.detailText);
        }
    }
}
