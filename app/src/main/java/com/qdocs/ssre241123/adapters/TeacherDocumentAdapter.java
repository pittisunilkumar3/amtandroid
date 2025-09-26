package com.qdocs.ssre241123.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.models.TeacherDocument;
import com.qdocs.ssre241123.utils.Utility;
import java.util.List;

public class TeacherDocumentAdapter extends RecyclerView.Adapter<TeacherDocumentAdapter.DocumentViewHolder> {

    private Context context;
    private List<TeacherDocument> documents;
    private OnDocumentItemClickListener listener;

    public interface OnDocumentItemClickListener {
        void onDownloadClick(TeacherDocument document, int position);
        void onViewClick(TeacherDocument document, int position);
        void onDocumentClick(TeacherDocument document, int position);
    }

    public TeacherDocumentAdapter(Context context, List<TeacherDocument> documents) {
        this.context = context;
        this.documents = documents;
    }

    public void setOnDocumentItemClickListener(OnDocumentItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_teacher_document, parent, false);
        return new DocumentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder holder, int position) {
        TeacherDocument document = documents.get(position);
        
        // Set document title
        String title = document.getTitle();
        if (title == null || title.isEmpty()) {
            title = "Document " + (position + 1);
        }
        holder.documentTitleTV.setText(title);
        
        // Set file name
        String fileName = document.getFileName();
        if (fileName == null || fileName.isEmpty()) {
            fileName = "Unknown file";
        }
        holder.documentFilenameTV.setText(fileName);
        
        // Set file type
        String fileType = document.getFileType();
        String displayType = getFileTypeDisplay(fileType);
        holder.documentTypeTV.setText(displayType);
        
        // Set file size
        String fileSize = document.getFileSize();
        if (fileSize == null || fileSize.isEmpty()) {
            fileSize = "Unknown size";
        }
        holder.documentSizeTV.setText(fileSize);
        
        // Set upload date
        String uploadDate = formatDate(document.getCreatedAt());
        holder.documentUploadDateTV.setText(uploadDate);
        
        // Set document icon based on file type
        setDocumentIcon(holder.documentIconIV, fileType);
        
        // Set click listeners
        holder.downloadButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDownloadClick(document, position);
            }
        });
        
        holder.viewButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onViewClick(document, position);
            }
        });
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDocumentClick(document, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return documents != null ? documents.size() : 0;
    }

    private String formatDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return "N/A";
        }
        
        try {
            // Try to format the date using utility method
            return Utility.parseDate("yyyy-MM-dd HH:mm:ss", 
                Utility.getSharedPreferences(context, "dateFormat"), 
                dateString);
        } catch (Exception e) {
            try {
                // Try with date only format
                return Utility.parseDate("yyyy-MM-dd", 
                    Utility.getSharedPreferences(context, "dateFormat"), 
                    dateString);
            } catch (Exception ex) {
                // If parsing fails, return the original string
                return dateString;
            }
        }
    }

    private String getFileTypeDisplay(String fileType) {
        if (fileType == null || fileType.isEmpty()) {
            return "FILE";
        }
        
        // Extract file extension from MIME type or file name
        if (fileType.contains("pdf")) {
            return "PDF";
        } else if (fileType.contains("image")) {
            return "IMG";
        } else if (fileType.contains("word") || fileType.contains("doc")) {
            return "DOC";
        } else if (fileType.contains("excel") || fileType.contains("sheet")) {
            return "XLS";
        } else if (fileType.contains("powerpoint") || fileType.contains("presentation")) {
            return "PPT";
        } else if (fileType.contains("text")) {
            return "TXT";
        } else {
            // Try to extract extension from file type
            String[] parts = fileType.split("/");
            if (parts.length > 1) {
                return parts[1].toUpperCase();
            }
            return "FILE";
        }
    }

    private void setDocumentIcon(ImageView iconView, String fileType) {
        int iconResource = R.drawable.ic_file; // Default file icon
        
        if (fileType != null) {
            if (fileType.contains("pdf")) {
                iconResource = R.drawable.ic_document_pdf;
            } else if (fileType.contains("image")) {
                iconResource = R.drawable.ic_photo_library;
            } else if (fileType.contains("word") || fileType.contains("doc")) {
                iconResource = R.drawable.ic_description;
            } else if (fileType.contains("excel") || fileType.contains("sheet")) {
                iconResource = R.drawable.ic_description;
            } else if (fileType.contains("powerpoint") || fileType.contains("presentation")) {
                iconResource = R.drawable.ic_description;
            }
        }
        
        iconView.setImageResource(iconResource);
    }

    public static class DocumentViewHolder extends RecyclerView.ViewHolder {
        ImageView documentIconIV;
        TextView documentTitleTV, documentFilenameTV, documentTypeTV, documentSizeTV, documentUploadDateTV;
        ImageButton downloadButton, viewButton;

        public DocumentViewHolder(@NonNull View itemView) {
            super(itemView);
            documentIconIV = itemView.findViewById(R.id.document_icon);
            documentTitleTV = itemView.findViewById(R.id.document_title);
            documentFilenameTV = itemView.findViewById(R.id.document_filename);
            documentTypeTV = itemView.findViewById(R.id.document_type);
            documentSizeTV = itemView.findViewById(R.id.document_size);
            documentUploadDateTV = itemView.findViewById(R.id.document_upload_date);
            downloadButton = itemView.findViewById(R.id.document_download_button);
            viewButton = itemView.findViewById(R.id.document_view_button);
        }
    }

    public void updateData(List<TeacherDocument> newDocuments) {
        this.documents = newDocuments;
        notifyDataSetChanged();
    }
}
