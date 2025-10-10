package com.qdocs.ssre241123.teachers;

import android.util.Log;
import android.widget.Toast;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Activity for Balance Fees Report
 * Filters: Session, Class, Section, Search Type (Paid/Unpaid/All)
 */
public class BalanceFeesReportActivity extends BaseFinanceReportActivity {

    private static final String TAG = "BalanceFeesReport";

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_balance_fees_report;
    }

    @Override
    protected String getReportTitle() {
        return getString(R.string.balance_fees_report);
    }

    @Override
    protected String getReportApiUrl() {
        return Constants.balanceFeesReportFilterUrl;
    }

    @Override
    protected void setupSpecificFilters() {
        // Setup Search Type Spinner
        setupSearchTypeSpinner();
    }

    @Override
    protected void parseReportResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            
            if (jsonResponse.getInt("status") == 1) {
                // TODO: Parse report data and display in RecyclerView
                showContent();
                Toast.makeText(this, "Report loaded successfully", Toast.LENGTH_SHORT).show();
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
}

