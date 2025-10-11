package com.qdocs.ssre241123.teachers;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.CollectionReportAdapter;
import com.qdocs.ssre241123.model.CollectionReportModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Activity for Combined Collection Report (Other Fee and Collection Fee Combined)
 * Filters: Search Duration, Session, Class, Section, Collect By, Group By
 * Note: Fee Type filter has been removed as per API specification
 */
public class OtherFeeAndCollectionFeeCombinedActivity extends BaseFinanceReportActivity {

    private static final String TAG = "CombinedCollectionReport";

    // UI Components
    private CardView summaryCard;
    private TextView totalRecordsTv, totalAmountTv, totalDiscountTv, totalFineTv, grandTotalTv;
    private TextView regularFeesCountTv, otherFeesCountTv;
    private LinearLayout feeTypeBreakdownLayout;

    // Data
    private List<CollectionReportModel> collectionList = new ArrayList<>();
    private CollectionReportAdapter adapter;
    private NumberFormat currencyFormat;
    private String currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize currency
        currency = Utility.getSharedPreferences(this, Constants.currency);
        if (currency == null || currency.isEmpty()) {
            currency = "₹";
        }
        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

        // Initialize summary card
        initializeSummaryCard();

        // Setup adapter
        adapter = new CollectionReportAdapter(this, collectionList);
        if (reportContentRecyclerView != null) {
            reportContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            reportContentRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_other_fee_and_collection_fee_combined;
    }

    @Override
    protected String getReportTitle() {
        return getString(R.string.other_fee_and_collection_fee_combined);
    }

    @Override
    protected String getReportApiUrl() {
        // Use the new Combined Collection Report API endpoint
        return Constants.combinedCollectionReportFilterUrl;
    }

    @Override
    protected void setupSpecificFilters() {
        // Setup Search Duration Spinner with date pickers
        setupSearchDurationSpinner();
        setupDatePickers();

        // Set default to today
        setTodayDates();

        // Note: Fee Type spinner has been removed from the layout
        // The API always returns ALL fee types
    }

    private void initializeSummaryCard() {
        // Find summary card in the layout (we'll need to add this to the layout)
        // For now, we'll create it programmatically if needed
        // This will be handled in the layout update
    }

    @Override
    protected void parseReportResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);

            // Check status
            int status = jsonResponse.optInt("status", 0);
            if (status != 1) {
                String message = jsonResponse.optString("message", "Failed to fetch report");
                Log.e(TAG, "API Error: " + message);
                showNoData();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;
            }

            // Clear existing data
            collectionList.clear();

            // Parse summary
            if (jsonResponse.has("summary")) {
                parseSummary(jsonResponse.getJSONObject("summary"));
            }

            // Parse data array
            if (jsonResponse.has("data")) {
                JSONArray dataArray = jsonResponse.getJSONArray("data");
                Log.d(TAG, "Collection records count: " + dataArray.length());

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject recordObj = dataArray.getJSONObject(i);
                    CollectionReportModel record = parseCollectionRecord(recordObj);
                    collectionList.add(record);
                }

                Log.d(TAG, "Collection list size: " + collectionList.size());

                if (!collectionList.isEmpty()) {
                    // Update adapter
                    adapter.notifyDataSetChanged();

                    // Show content
                    showContent();

                    // Show success message
                    String successMessage = "Found " + collectionList.size() + " collection record(s)";
                    Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "No data in response");
                    showNoData();
                    Toast.makeText(this, "No collection records found for the selected filters",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d(TAG, "No data field in response");
                showNoData();
                Toast.makeText(this, "No collection records found", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing response", e);
            showNoData();
            Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Parse summary object from API response
     */
    private void parseSummary(JSONObject summary) {
        try {
            int totalRecords = summary.optInt("total_records", 0);
            String totalAmount = summary.optString("total_amount", "0.00");
            String totalDiscount = summary.optString("total_discount", "0.00");
            String totalFine = summary.optString("total_fine", "0.00");
            String grandTotal = summary.optString("grand_total", "0.00");
            int regularFeesCount = summary.optInt("regular_fees_count", 0);
            int otherFeesCount = summary.optInt("other_fees_count", 0);

            Log.d(TAG, "Summary - Total Records: " + totalRecords +
                    ", Total Amount: " + totalAmount +
                    ", Regular Fees: " + regularFeesCount +
                    ", Other Fees: " + otherFeesCount);

            // Update summary card if it exists
            // This will be implemented when we add the summary card to the layout

        } catch (Exception e) {
            Log.e(TAG, "Error parsing summary", e);
        }
    }

    /**
     * Parse individual collection record from JSON
     */
    private CollectionReportModel parseCollectionRecord(JSONObject recordObj) {
        CollectionReportModel record = new CollectionReportModel();

        try {
            // Basic IDs
            record.setId(recordObj.optString("id", ""));

            // Student Information
            record.setAdmissionNo(recordObj.optString("admission_no", ""));
            record.setFirstname(recordObj.optString("firstname", ""));
            record.setMiddlename(recordObj.optString("middlename", ""));
            record.setLastname(recordObj.optString("lastname", ""));

            // Class Information
            record.setClassName(recordObj.optString("class", ""));
            record.setSection(recordObj.optString("section", ""));

            // Fee Information
            record.setName(recordObj.optString("name", "")); // Fee group name
            record.setType(recordObj.optString("type", "")); // Fee type
            record.setCode(recordObj.optString("code", "")); // Fee code

            // Payment Information
            record.setAmount(recordObj.optString("amount", "0"));
            record.setAmountDiscount(recordObj.optString("discount", "0"));
            record.setAmountFine(recordObj.optString("fine", "0"));
            record.setPaymentMode(recordObj.optString("payment_mode", ""));
            record.setDate(recordObj.optString("date", ""));
            record.setReceivedBy(recordObj.optString("received_by", ""));

            // Fee source (regular or other)
            String feeSource = recordObj.optString("fee_source", "regular");
            // Store this in a note or description field if needed

            Log.d(TAG, "Parsed record: " + record.getFullName() +
                    " - " + record.getType() +
                    " - " + currency + record.getAmount());

        } catch (Exception e) {
            Log.e(TAG, "Error parsing collection record", e);
        }

        return record;
    }

    @Override
    protected void showContent() {
        super.showContent();
        // Show summary card if it exists
        if (summaryCard != null) {
            summaryCard.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void showNoData() {
        super.showNoData();
        // Hide summary card
        if (summaryCard != null) {
            summaryCard.setVisibility(View.GONE);
        }
    }

    /**
     * Override buildRequestBody to use correct parameter names for Combined Collection Report API
     */
    @Override
    protected String buildRequestBody() {
        try {
            JSONObject jsonBody = new JSONObject();

            // Add search_type only if it's a predefined type (not "all" or "period")
            if (selectedSearchType != null && !selectedSearchType.isEmpty()
                    && !"all".equals(selectedSearchType) && !"period".equals(selectedSearchType)) {
                jsonBody.put("search_type", selectedSearchType);
            }

            // Add date_from and date_to (not from_date and to_date)
            if (selectedFromDate != null && !selectedFromDate.isEmpty()) {
                jsonBody.put("date_from", selectedFromDate);
            }
            if (selectedToDate != null && !selectedToDate.isEmpty()) {
                jsonBody.put("date_to", selectedToDate);
            }

            // Add session_id
            if (selectedSessionId != null && !selectedSessionId.isEmpty()) {
                jsonBody.put("session_id", selectedSessionId);
            }

            // Add class_id
            if (selectedClassId != null && !selectedClassId.isEmpty()) {
                jsonBody.put("class_id", selectedClassId);
            }

            // Add section_id
            if (selectedSectionId != null && !selectedSectionId.isEmpty()) {
                jsonBody.put("section_id", selectedSectionId);
            }

            // Add received_by (not collect_by_id)
            if (selectedCollectById != null && !selectedCollectById.isEmpty()) {
                jsonBody.put("received_by", selectedCollectById);
            }

            // Add group (not group_by)
            if (selectedGroupBy != null && !selectedGroupBy.isEmpty()) {
                jsonBody.put("group", selectedGroupBy);
            }

            String requestBody = jsonBody.toString();
            Log.d(TAG, "Request Body: " + requestBody);

            return requestBody;
        } catch (JSONException e) {
            Log.e(TAG, "Error creating request body", e);
            return "{}";
        }
    }
}

