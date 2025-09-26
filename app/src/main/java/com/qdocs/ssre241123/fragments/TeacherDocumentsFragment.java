package com.qdocs.ssre241123.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.StudentProfileAdapter;
import com.qdocs.ssre241123.adapters.TeacherDocumentAdapter;
import com.qdocs.ssre241123.models.TeacherDocument;
import com.qdocs.ssre241123.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@SuppressLint("ValidFragment")
public class TeacherDocumentsFragment extends Fragment {
    
    RecyclerView recyclerView;
    TextView documentsInfoTV;
    StudentProfileAdapter adapter;
    TeacherDocumentAdapter documentAdapter;
    ArrayList<String> documentsValues = new ArrayList<String>();
    HashMap<String, String> documentsData = new HashMap<>();
    List<TeacherDocument> documents = new ArrayList<>();

    @SuppressLint("ValidFragment")
    public TeacherDocumentsFragment() {
    }

    public static TeacherDocumentsFragment newInstance(JSONObject teacherData) {
        TeacherDocumentsFragment fragment = new TeacherDocumentsFragment();
        Bundle args = new Bundle();
        if (teacherData != null) {
            args.putString("teacherData", teacherData.toString());
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_teacher_documents, container, false);
        
        recyclerView = mainView.findViewById(R.id.teacherDocumentsFragment_recyclerView);
        documentsInfoTV = mainView.findViewById(R.id.teacherDocumentsInfo);
        
        loadDocumentsData();
        
        if (documentsValues.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            documentsInfoTV.setVisibility(View.VISIBLE);
            documentsInfoTV.setText("Teacher Documents information will be displayed here.\n\n" +
                                  "This section will show:\n" +
                                  "• Uploaded certificates\n" +
                                  "• Identity documents\n" +
                                  "• Educational qualifications\n" +
                                  "• Professional documents\n\n" +
                                  "No documents available currently.");
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            documentsInfoTV.setVisibility(View.GONE);
            
            // Create dynamic header array based on available documents
            int[] documentsHeaderArray = new int[documentsValues.size()];
            for (int i = 0; i < documentsValues.size(); i++) {
                documentsHeaderArray[i] = R.string.documents; // Generic document string
            }
            
            // Initialize both adapters
            adapter = new StudentProfileAdapter(getActivity().getApplicationContext(),
                                              documentsHeaderArray, documentsValues, documentsData);
            documentAdapter = new TeacherDocumentAdapter(getActivity().getApplicationContext(), documents);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            // Set click listener for document items
            documentAdapter.setOnDocumentItemClickListener(new TeacherDocumentAdapter.OnDocumentItemClickListener() {
                @Override
                public void onDownloadClick(TeacherDocument document, int position) {
                    // Handle document download - can be implemented later
                    String message = "Download: " + document.getTitle();
                    android.widget.Toast.makeText(getActivity(), message, android.widget.Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onViewClick(TeacherDocument document, int position) {
                    // Handle document view - can be implemented later
                    String message = "View: " + document.getTitle();
                    android.widget.Toast.makeText(getActivity(), message, android.widget.Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onDocumentClick(TeacherDocument document, int position) {
                    // Handle document click - can be implemented later
                    String message = "Document: " + document.getTitle() + " (" + document.getFileType() + ")";
                    android.widget.Toast.makeText(getActivity(), message, android.widget.Toast.LENGTH_SHORT).show();
                }
            });

            // Initially set the generic adapter, will switch based on data availability
            recyclerView.setAdapter(adapter);
        }
        
        return mainView;
    }

    private void loadDocumentsData() {
        documentsValues.clear();
        documentsData.clear();
        documents.clear();

        if (getArguments() != null && getArguments().getString("teacherData") != null) {
            try {
                JSONObject teacherData = new JSONObject(getArguments().getString("teacherData"));

                // Check school settings for document visibility
                JSONObject schoolSettings = teacherData.optJSONObject("school_settings");
                boolean canShowDocuments = schoolSettings == null || schoolSettings.optInt("staff_upload_documents", 1) == 1;

                if (!canShowDocuments) {
                    documentsValues.add("Document access is restricted by school policy");
                    documentsData.put("Policy", "Document access restricted");
                    return;
                }

                // Extract documents array
                JSONArray documentsArray = teacherData.optJSONArray("documents");
                if (documentsArray != null && documentsArray.length() > 0) {
                    documentsValues.add("=== UPLOADED DOCUMENTS ===");
                    documentsData.put("Documents", "Teacher uploaded documents");

                    // Process each document in the array
                    for (int i = 0; i < documentsArray.length(); i++) {
                        JSONObject document = documentsArray.optJSONObject(i);
                        if (document != null) {
                            String title = document.optString("title", "Document " + (i + 1));
                            String fileName = document.optString("file_name", "");
                            String uploadDate = document.optString("created_at", "");
                            String fileType = document.optString("file_type", "");
                            String fileSize = document.optString("file_size", "");

                            // Format upload date if available
                            if (!uploadDate.isEmpty()) {
                                try {
                                    uploadDate = Utility.parseDate("yyyy-MM-dd HH:mm:ss",
                                        Utility.getSharedPreferences(getActivity(), "dateFormat"),
                                        uploadDate);
                                } catch (Exception e) {
                                    Log.e("TeacherDocumentsFragment", "Date parsing error: " + e.getMessage());
                                }
                            }

                            String documentInfo = !fileName.isEmpty() ? fileName : title;
                            if (!uploadDate.isEmpty()) {
                                documentInfo += "\nUploaded: " + uploadDate;
                            }
                            if (!fileType.isEmpty()) {
                                documentInfo += "\nType: " + fileType;
                            }
                            if (!fileSize.isEmpty()) {
                                documentInfo += "\nSize: " + fileSize;
                            }

                            documentsValues.add(documentInfo);
                            documentsData.put(title, documentInfo);
                        }
                    }
                } else {
                    // No documents uploaded - show helpful information
                    documentsValues.add("=== DOCUMENT INFORMATION ===");
                    documentsData.put("Document Info", "Information about document requirements");

                    documentsValues.add("No documents have been uploaded yet");
                    documentsData.put("Status", "No documents uploaded");

                    documentsValues.add("Required documents may include:");
                    documentsValues.add("• Educational certificates");
                    documentsValues.add("• Identity proof (Aadhar, PAN)");
                    documentsValues.add("• Experience certificates");
                    documentsValues.add("• Medical certificates");
                    documentsValues.add("• Address proof");

                    documentsData.put("Requirements", "Document requirements listed");

                    // Check if documents is an object instead of array (fallback)
                    JSONObject documents = teacherData.optJSONObject("documents");
                    if (documents != null) {
                        Iterator<String> keys = documents.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            String value = documents.optString(key, "");
                            if (!value.isEmpty()) {
                                documentsValues.add(key + ": " + value);
                                documentsData.put(key, value);
                            }
                        }
                    }
                }

                // Add custom fields if available
                JSONArray customFields = teacherData.optJSONArray("custom_fields");
                if (customFields != null && customFields.length() > 0) {
                    documentsValues.add("=== ADDITIONAL INFORMATION ===");
                    documentsData.put("Additional Info", "Custom fields and additional data");

                    for (int i = 0; i < customFields.length(); i++) {
                        JSONObject field = customFields.optJSONObject(i);
                        if (field != null) {
                            String fieldName = field.optString("field_name", "");
                            String fieldValue = field.optString("field_value", "");

                            if (!fieldName.isEmpty() && !fieldValue.isEmpty()) {
                                documentsValues.add(fieldName + ": " + fieldValue);
                                documentsData.put(fieldName, fieldValue);
                            }
                        }
                    }
                }

                // Parse individual documents for list display
                parseDocuments(teacherData);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Update the fragment with new teacher documents data
     */
    public void updateDocumentsData(JSONObject teacherData) {
        if (teacherData != null) {
            Log.d("TeacherDocumentsFragment", "Updating documents data with new teacher data");

            // Update the arguments with new data
            Bundle args = getArguments();
            if (args == null) {
                args = new Bundle();
            }
            args.putString("teacherData", teacherData.toString());
            setArguments(args);

            // Reload documents data with new information
            loadDocumentsData();

            // Decide which adapter to use based on available data
            if (documents.size() > 0) {
                Log.d("TeacherDocumentsFragment", "Using list adapter with " + documents.size() + " documents");
                documentAdapter.updateData(documents);
                recyclerView.setAdapter(documentAdapter);
                recyclerView.setVisibility(View.VISIBLE);
                documentsInfoTV.setVisibility(View.GONE);
            } else if (!documentsValues.isEmpty()) {
                Log.d("TeacherDocumentsFragment", "Using generic adapter with summary data");
                recyclerView.setAdapter(adapter);
                recyclerView.setVisibility(View.VISIBLE);
                documentsInfoTV.setVisibility(View.GONE);
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                    Log.d("TeacherDocumentsFragment", "Documents adapter notified of data changes");
                }
            } else {
                recyclerView.setVisibility(View.GONE);
                documentsInfoTV.setVisibility(View.VISIBLE);
            }
        } else {
            Log.w("TeacherDocumentsFragment", "Attempted to update with null teacher data");
        }
    }

    private void parseDocuments(JSONObject teacherData) {
        try {
            // Extract documents array
            JSONArray documentsArray = teacherData.optJSONArray("documents");
            if (documentsArray != null && documentsArray.length() > 0) {
                Log.d("TeacherDocumentsFragment", "Found " + documentsArray.length() + " documents");

                for (int i = 0; i < documentsArray.length(); i++) {
                    JSONObject documentObj = documentsArray.optJSONObject(i);
                    if (documentObj != null) {
                        TeacherDocument document = new TeacherDocument();
                        document.setId(documentObj.optString("id", String.valueOf(i + 1)));
                        document.setTitle(documentObj.optString("title", "Document " + (i + 1)));
                        document.setFileName(documentObj.optString("file_name", ""));
                        document.setFileType(documentObj.optString("file_type", ""));
                        document.setFileSize(documentObj.optString("file_size", ""));
                        document.setCreatedAt(documentObj.optString("created_at", ""));
                        document.setFileUrl(documentObj.optString("file_url", ""));
                        document.setDescription(documentObj.optString("description", ""));
                        document.setUploadedBy(documentObj.optString("uploaded_by", ""));
                        document.setCategory(documentObj.optString("category", ""));

                        documents.add(document);
                        Log.d("TeacherDocumentsFragment", "Added document: " + document.getTitle());
                    }
                }
            } else {
                Log.d("TeacherDocumentsFragment", "No documents array found or empty");
            }
        } catch (Exception e) {
            Log.e("TeacherDocumentsFragment", "Error parsing documents: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
