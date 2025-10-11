package com.qdocs.ssre241123.teachers;

import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.CollectionReportAdapter;
import com.qdocs.ssre241123.model.CollectionReportModel;
import com.qdocs.ssre241123.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for Fees Collection Report
 * Filters: Search Duration, Session, Class, Fee Type, Collected By, Group By
 */
public class FeesCollectionReportActivity extends BaseFinanceReportActivity {

    private static final String TAG = "FeesCollectionReport";

    private List<CollectionReportModel> collectionList;
    private CollectionReportAdapter adapter;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_fees_collection_report;
    }

    @Override
    protected String getReportTitle() {
        return getString(R.string.fees_collection_report);
    }

    @Override
    protected String getReportApiUrl() {
        return Constants.collectionReportFilterUrl;
    }

    @Override
    protected void setupSpecificFilters() {
        // Setup Search Duration Spinner with date pickers
        setupSearchDurationSpinner();
        setupDatePickers();

        // Set default to today
        setTodayDates();
    }

    @Override
    protected void parseReportResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);

            Log.d(TAG, "API Response: " + response);

            if (jsonResponse.getInt("status") == 1) {
                // Parse data array
                JSONArray dataArray = jsonResponse.optJSONArray("data");

                if (dataArray != null && dataArray.length() > 0) {
                    collectionList = new ArrayList<>();

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject item = dataArray.getJSONObject(i);
                        CollectionReportModel collection = parseCollectionItem(item);
                        if (collection != null) {
                            collectionList.add(collection);
                        }
                    }

                    if (!collectionList.isEmpty()) {
                        displayReport();
                        Toast.makeText(this, "Report loaded: " + collectionList.size() + " records", Toast.LENGTH_SHORT).show();
                    } else {
                        showNoData();
                        Toast.makeText(this, "No records found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNoData();
                    Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
                }
            } else {
                showNoData();
                String message = jsonResponse.optString("message", "No data found");
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing report response", e);
            showNoData();
            Toast.makeText(this, "Error parsing report", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Parse individual collection item from JSON
     */
    private CollectionReportModel parseCollectionItem(JSONObject item) {
        try {
            CollectionReportModel collection = new CollectionReportModel();

            // Basic IDs
            collection.setId(item.optString("id", ""));
            collection.setStudentFeesMasterId(item.optString("student_fees_master_id", ""));
            collection.setFeeGroupsFeetypeId(item.optString("fee_groups_feetype_id", ""));
            collection.setStudentId(item.optString("student_id", ""));
            collection.setStudentSessionId(item.optString("student_session_id", ""));

            // Student Information
            collection.setAdmissionNo(item.optString("admission_no", ""));
            collection.setFirstname(item.optString("firstname", ""));
            collection.setMiddlename(item.optString("middlename", ""));
            collection.setLastname(item.optString("lastname", ""));

            // Class Information
            collection.setClassId(item.optString("class_id", ""));
            collection.setClassName(item.optString("class", ""));
            collection.setSectionId(item.optString("section_id", ""));
            collection.setSection(item.optString("section", ""));

            // Fee Information
            collection.setName(item.optString("name", ""));
            collection.setType(item.optString("type", ""));
            collection.setCode(item.optString("code", ""));
            collection.setIsSystem(item.optString("is_system", ""));

            // Payment Information
            collection.setAmount(item.optString("amount", "0"));
            collection.setAmountDiscount(item.optString("amount_discount", "0"));
            collection.setAmountFine(item.optString("amount_fine", "0"));
            collection.setDescription(item.optString("description", ""));
            collection.setPaymentMode(item.optString("payment_mode", ""));
            collection.setDate(item.optString("date", ""));
            collection.setInvNo(item.optString("inv_no", ""));
            collection.setReceivedBy(item.optString("received_by", ""));

            return collection;
        } catch (Exception e) {
            Log.e(TAG, "Error parsing collection item", e);
            return null;
        }
    }

    /**
     * Display report in RecyclerView
     */
    private void displayReport() {
        if (reportContentRecyclerView != null) {
            adapter = new CollectionReportAdapter(this, collectionList);
            reportContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            reportContentRecyclerView.setAdapter(adapter);
            showContent();
            Log.d(TAG, "RecyclerView adapter set successfully with " + collectionList.size() + " items");
        } else {
            Log.e(TAG, "reportContentRecyclerView is NULL!");
        }
    }
}

