package com.qdocs.ssre241123.teachers;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.TotalFeeCollectionReportAdapter;
import com.qdocs.ssre241123.model.TotalFeeCollectionReportModel;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Activity for Total Fee Collection Report
 * Filters: Search Duration, Class, Section, Fee Type, Collect By, Group By
 *
 * Features:
 * - Combined data from regular fees + other fees + transport fees
 * - Fee type breakdown in summary
 * - Supports grouping by class, collection, or payment mode
 * - Flexible filtering options
 */
public class TotalFeeCollectionReportActivity extends BaseFinanceReportActivity {

    private static final String TAG = "TotalFeeCollectionReport";

    // UI Components for summary
    private CardView summaryCard;
    private TextView totalRecordsTv;
    private TextView totalAmountTv;
    private LinearLayout feeTypeBreakdownLayout;

    // Data
    private List<TotalFeeCollectionReportModel> collectionList;
    private TotalFeeCollectionReportAdapter adapter;
    private String currency;
    private NumberFormat numberFormat;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_total_fee_collection_report;
    }

    @Override
    protected String getReportTitle() {
        return getString(R.string.total_fee_collection_report);
    }

    @Override
    protected String getReportApiUrl() {
        return Constants.totalFeeCollectionReportFilterUrl;
    }

    @Override
    protected void setupSpecificFilters() {
        // Setup Search Duration Spinner with date pickers
        setupSearchDurationSpinner();
        setupDatePickers();

        // Set default to today
        setTodayDates();

        // Initialize summary components
        summaryCard = findViewById(R.id.summaryCard);
        totalRecordsTv = findViewById(R.id.totalRecordsTv);
        totalAmountTv = findViewById(R.id.totalAmountTv);
        feeTypeBreakdownLayout = findViewById(R.id.feeTypeBreakdownLayout);

        // Initialize data list
        collectionList = new ArrayList<>();

        // Get currency
        currency = Utility.getSharedPreferences(getApplicationContext(), Constants.currency);
        if (currency == null || currency.isEmpty()) {
            currency = "₹";
        }

        // Initialize number formatter
        numberFormat = NumberFormat.getInstance(new Locale("en", "IN"));
    }

    @Override
    protected void setupAllSpinners() {
        // Since this layout doesn't have a sessionSpinner, we need to populate
        // the class spinner with all classes from all sessions
        if (classSpinner != null && !sessionsList.isEmpty()) {
            setupClassSpinnerWithAllClasses();
        }

        // Setup other spinners
        if (feeTypeSpinner != null) setupFeeTypeSpinner();
        if (collectBySpinner != null) setupCollectBySpinner();
        if (groupBySpinner != null) setupGroupBySpinner();
    }

    /**
     * Setup class spinner with all classes from all sessions
     * This is used when there's no session spinner in the layout
     */
    private void setupClassSpinnerWithAllClasses() {
        // Collect all classes from all sessions
        currentClassesList.clear();
        for (SessionData session : sessionsList) {
            if (session.classes != null) {
                currentClassesList.addAll(session.classes);
            }
        }

        // Setup class spinner
        List<String> classNames = new ArrayList<>();
        classNames.add("Select Class");
        for (ClassData classData : currentClassesList) {
            classNames.add(classData.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, classNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(adapter);

        Log.d(TAG, "Class spinner populated with " + currentClassesList.size() + " classes");
    }

    @Override
    protected void parseReportResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);

            Log.d(TAG, "Response: " + response);

            if (jsonResponse.getInt("status") == 1) {
                // Clear previous data
                collectionList.clear();

                // Parse data
                if (jsonResponse.has("data")) {
                    JSONArray dataArray = jsonResponse.getJSONArray("data");

                    // Check if data is grouped
                    if (dataArray.length() > 0) {
                        JSONObject firstItem = dataArray.getJSONObject(0);

                        if (firstItem.has("group_name")) {
                            // Grouped data
                            parseGroupedData(dataArray);
                        } else {
                            // Regular data
                            parseRegularData(dataArray);
                        }
                    }
                }

                // Display data
                if (collectionList.isEmpty()) {
                    showNoData();
                    Toast.makeText(this, "No records found", Toast.LENGTH_SHORT).show();
                } else {
                    // Calculate and display summary from parsed data
                    calculateAndDisplaySummary();
                    displayReport();
                    Toast.makeText(this, "Report loaded successfully", Toast.LENGTH_SHORT).show();
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
     * Calculate and display summary from parsed collection data
     * This is needed because the API returns zero amounts in the summary
     */
    private void calculateAndDisplaySummary() {
        try {
            // Calculate total records
            int totalRecords = collectionList.size();
            totalRecordsTv.setText(String.valueOf(totalRecords));

            // Calculate total amount from all records
            double totalAmount = 0;
            Map<String, FeeTypeBreakdownData> feeTypeMap = new HashMap<>();

            for (TotalFeeCollectionReportModel record : collectionList) {
                double netAmount = record.getNetAmount();
                totalAmount += netAmount;

                // Calculate fee type breakdown
                String feeType = record.getFeeType();
                if (feeType != null && !feeType.isEmpty()) {
                    if (!feeTypeMap.containsKey(feeType)) {
                        FeeTypeBreakdownData breakdownData = new FeeTypeBreakdownData();
                        breakdownData.feeType = feeType;
                        breakdownData.count = 0;
                        breakdownData.total = 0;
                        feeTypeMap.put(feeType, breakdownData);
                    }
                    FeeTypeBreakdownData breakdownData = feeTypeMap.get(feeType);
                    breakdownData.count++;
                    breakdownData.total += netAmount;
                }
            }

            // Display total amount
            String formattedAmount = currency + " " + numberFormat.format(totalAmount);
            totalAmountTv.setText(formattedAmount);

            // Display fee type breakdown
            displayCalculatedFeeTypeBreakdown(feeTypeMap);

            // Show summary card
            summaryCard.setVisibility(View.VISIBLE);

            Log.d(TAG, "Summary calculated - Total Records: " + totalRecords + ", Total Amount: " + totalAmount);

        } catch (Exception e) {
            Log.e(TAG, "Error calculating summary", e);
        }
    }

    /**
     * Display calculated fee type breakdown
     */
    private void displayCalculatedFeeTypeBreakdown(Map<String, FeeTypeBreakdownData> feeTypeMap) {
        try {
            // Clear previous breakdown
            feeTypeBreakdownLayout.removeAllViews();

            // Add each fee type
            for (FeeTypeBreakdownData breakdownData : feeTypeMap.values()) {
                // Inflate breakdown item
                View itemView = LayoutInflater.from(this).inflate(
                    R.layout.item_fee_type_breakdown,
                    feeTypeBreakdownLayout,
                    false
                );

                TextView feeTypeNameTv = itemView.findViewById(R.id.fee_type_name_tv);
                TextView feeTypeCountTv = itemView.findViewById(R.id.fee_type_count_tv);
                TextView feeTypeTotalTv = itemView.findViewById(R.id.fee_type_total_tv);

                feeTypeNameTv.setText(breakdownData.feeType);
                feeTypeCountTv.setText("(" + breakdownData.count + ")");

                String formattedTotal = currency + " " + numberFormat.format(breakdownData.total);
                feeTypeTotalTv.setText(formattedTotal);

                // Apply theme color
                String primaryColor = Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour);
                if (primaryColor != null && !primaryColor.isEmpty()) {
                    try {
                        feeTypeTotalTv.setTextColor(Color.parseColor(primaryColor));
                    } catch (Exception e) {
                        // Use default color
                    }
                }

                feeTypeBreakdownLayout.addView(itemView);
            }

        } catch (Exception e) {
            Log.e(TAG, "Error displaying fee type breakdown", e);
        }
    }

    /**
     * Helper class for fee type breakdown data
     */
    private static class FeeTypeBreakdownData {
        String feeType;
        int count;
        double total;
    }

    /**
     * Parse regular (non-grouped) data
     */
    private void parseRegularData(JSONArray dataArray) {
        try {
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject item = dataArray.getJSONObject(i);
                TotalFeeCollectionReportModel model = parseCollectionItem(item);
                if (model != null) {
                    collectionList.add(model);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing regular data", e);
        }
    }

    /**
     * Parse grouped data
     */
    private void parseGroupedData(JSONArray dataArray) {
        try {
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject group = dataArray.getJSONObject(i);

                if (group.has("records")) {
                    JSONArray records = group.getJSONArray("records");

                    for (int j = 0; j < records.length(); j++) {
                        JSONObject item = records.getJSONObject(j);
                        TotalFeeCollectionReportModel model = parseCollectionItem(item);
                        if (model != null) {
                            // Add group information
                            model.setGroupName(group.optString("group_name", ""));
                            collectionList.add(model);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing grouped data", e);
        }
    }

    /**
     * Parse individual collection item
     * Parses the amount_detail JSON string to extract correct amounts
     */
    private TotalFeeCollectionReportModel parseCollectionItem(JSONObject item) {
        try {
            TotalFeeCollectionReportModel model = new TotalFeeCollectionReportModel();

            // Basic information
            model.setId(item.optString("id", ""));
            model.setAdmissionNo(item.optString("admission_no", ""));

            // Student information - Build full name from firstname, middlename, lastname
            String firstname = item.optString("firstname", "");
            String middlename = item.optString("middlename", "");
            String lastname = item.optString("lastname", "");

            StringBuilder fullName = new StringBuilder();
            if (!firstname.isEmpty()) {
                fullName.append(firstname);
            }
            if (!middlename.isEmpty() && !"null".equals(middlename)) {
                if (fullName.length() > 0) fullName.append(" ");
                fullName.append(middlename);
            }
            if (!lastname.isEmpty() && !"null".equals(lastname)) {
                if (fullName.length() > 0) fullName.append(" ");
                fullName.append(lastname);
            }
            model.setStudentName(fullName.toString());

            // Class and section information
            model.setClassName(item.optString("class", ""));
            model.setSectionName(item.optString("section", ""));

            // Fee information - "type" field contains the fee type name
            model.setFeeType(item.optString("type", ""));
            model.setFeeCode(item.optString("code", ""));

            // Fee source (regular or other)
            String feeSource = item.optString("fee_source", "regular");
            model.setType(feeSource);

            // Parse amount_detail JSON string to get correct amounts
            double amount = 0;
            double fine = 0;
            double discount = 0;
            String paymentMode = "";
            String date = "";
            String collectedBy = "";
            String note = "";

            if (item.has("amount_detail") && !item.isNull("amount_detail")) {
                String amountDetailStr = item.optString("amount_detail", "");
                if (!amountDetailStr.isEmpty() && !"null".equals(amountDetailStr)) {
                    try {
                        JSONObject amountDetailObj = new JSONObject(amountDetailStr);

                        // The amount_detail contains nested objects with keys like "1", "2", etc.
                        // We need to iterate through all keys and sum up the amounts
                        java.util.Iterator<String> keys = amountDetailObj.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            JSONObject paymentDetail = amountDetailObj.getJSONObject(key);

                            // Extract amounts
                            amount += paymentDetail.optDouble("amount", 0);
                            fine += paymentDetail.optDouble("amount_fine", 0);
                            discount += paymentDetail.optDouble("amount_discount", 0);

                            // Get payment details from the first payment (or last one will be used)
                            if (paymentMode.isEmpty()) {
                                paymentMode = paymentDetail.optString("payment_mode", "");
                            }
                            if (date.isEmpty()) {
                                date = paymentDetail.optString("date", "");
                            }
                            if (collectedBy.isEmpty()) {
                                collectedBy = paymentDetail.optString("collected_by", "");
                            }
                            if (note.isEmpty()) {
                                note = paymentDetail.optString("description", "");
                            }
                        }

                        Log.d(TAG, "Parsed amount_detail - Amount: " + amount + ", Fine: " + fine + ", Discount: " + discount);

                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing amount_detail JSON: " + amountDetailStr, e);
                        // Fall back to direct fields if amount_detail parsing fails
                        amount = item.optDouble("amount", 0);
                        fine = item.optDouble("fine", 0);
                        discount = item.optDouble("discount", 0);
                    }
                }
            } else {
                // Fall back to direct fields if amount_detail doesn't exist
                amount = item.optDouble("amount", 0);
                fine = item.optDouble("fine", 0);
                discount = item.optDouble("discount", 0);
            }

            // Set amount information
            model.setAmount(amount);
            model.setFine(fine);
            model.setDiscount(discount);

            // Calculate net amount: amount - discount + fine
            double netAmount = amount - discount + fine;
            model.setNetAmount(netAmount);

            // Payment information
            if (!paymentMode.isEmpty()) {
                model.setPaymentMode(paymentMode);
            } else {
                model.setPaymentMode(item.optString("payment_mode", ""));
            }

            if (!date.isEmpty()) {
                model.setDate(date);
            } else {
                model.setDate(item.optString("date", item.optString("created_at", "")));
            }

            if (!collectedBy.isEmpty()) {
                model.setCollectedBy(collectedBy);
            } else {
                model.setCollectedBy(item.optString("collected_by", ""));
            }

            if (!note.isEmpty()) {
                model.setNote(note);
            } else {
                model.setNote(item.optString("note", ""));
            }

            // Invoice number from amount_detail
            if (item.has("amount_detail") && !item.isNull("amount_detail")) {
                String amountDetailStr = item.optString("amount_detail", "");
                if (!amountDetailStr.isEmpty() && !"null".equals(amountDetailStr)) {
                    try {
                        JSONObject amountDetailObj = new JSONObject(amountDetailStr);
                        java.util.Iterator<String> keys = amountDetailObj.keys();
                        if (keys.hasNext()) {
                            String key = keys.next();
                            JSONObject paymentDetail = amountDetailObj.getJSONObject(key);
                            String invNo = paymentDetail.optString("inv_no", "");
                            if (!invNo.isEmpty()) {
                                model.setInvoiceNo(invNo);
                            }
                        }
                    } catch (JSONException e) {
                        // Ignore
                    }
                }
            }

            Log.d(TAG, "Parsed item: " + model.getStudentName() + " - " + model.getFeeType() + " - " + netAmount);

            return model;

        } catch (Exception e) {
            Log.e(TAG, "Error parsing collection item", e);
            return null;
        }
    }

    /**
     * Display report in RecyclerView
     */
    private void displayReport() {
        // Setup adapter
        adapter = new TotalFeeCollectionReportAdapter(this, collectionList);
        reportContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reportContentRecyclerView.setAdapter(adapter);

        // Show content
        showContent();
    }
}

