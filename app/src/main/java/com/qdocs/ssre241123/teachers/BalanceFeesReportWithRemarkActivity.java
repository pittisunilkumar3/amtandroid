package com.qdocs.ssre241123.teachers;

import android.util.Log;
import android.widget.Toast;

import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Activity for Balance Fees Report With Remark
 * Filters: Session, Class, Section
 */
public class BalanceFeesReportWithRemarkActivity extends BaseFinanceReportActivity {

    private static final String TAG = "BalanceFeesReportWithRemark";

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_balance_fees_report_with_remark;
    }

    @Override
    protected String getReportTitle() {
        return getString(R.string.balance_fees_report_with_remark);
    }

    @Override
    protected String getReportApiUrl() {
        return Constants.balanceFeesReportWithRemarkFilterUrl;
    }

    @Override
    protected void setupSpecificFilters() {
        // No additional filters needed - Session, Class, Section are handled by base class
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

