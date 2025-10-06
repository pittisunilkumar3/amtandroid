package com.qdocs.ssre241123;

import static android.widget.Toast.makeText;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.ssre241123.students.NewDashboard;
import com.qdocs.ssre241123.students.StudentFees;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SplashActivity extends Activity {

    private static final int SPLASH_TIME_OUT = 1000;
    ImageView logoIV;
    Boolean isLoggegIn,isLock;
    Boolean isUrlTaken;

    public Map<String, String>  headers = new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_activity);
        logoIV = findViewById(R.id.splash_logo);

        Boolean isLocaleSet;

        try {
            isLocaleSet = Utility.getSharedPreferencesBoolean(getApplicationContext(), "isLocaleSet");
        } catch (NullPointerException e) {
            isLocaleSet = false;
        }

        if(isLocaleSet) {
            setLocale(Utility.getSharedPreferences(getApplicationContext(), Constants.langCode));
        }
       splash();

    }

    private void splash() {

        new Handler().postDelayed(new Runnable() {
            public void run() {

                try {
                    isLoggegIn = Utility.getSharedPreferencesBoolean(getApplicationContext(), Constants.isLoggegIn);
                    isLock = Utility.getSharedPreferencesBoolean(getApplicationContext(), Constants.isLock);
                    isUrlTaken = Utility.getSharedPreferencesBoolean(getApplicationContext(), "isUrlTaken");
                } catch (NullPointerException NPE) {
                    isLoggegIn = false;
                    isUrlTaken = false;
                    isLock = false;
                }

                Log.e("loggeg", isLoggegIn.toString());
                Log.e("isLock", isLock.toString());
                Log.e("isUrlTaken", isUrlTaken.toString());

                // Set API URL first
                String apiUrl = Utility.getApiUrl(getApplicationContext());
                Utility.setSharedPreferenceBoolean(getApplicationContext(), "isUrlTaken", false);
                Log.e("API URL Set", "Using configured domain: " + apiUrl);

                // Check if maintenance mode check is enabled
                if(Constants.checkMaintenanceMode) {
                    // Maintenance check is ENABLED - call the API
                    Log.e("SplashActivity", "Maintenance check ENABLED - calling API");

                    if(Constants.askUrlFromUser) {
                        if(isUrlTaken) {
                            if(Utility.isConnectingToInternet(SplashActivity.this)){
                                ismaintenancemode(apiUrl);
                            }else{
                                makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Intent asd = new Intent(getApplicationContext(), TakeUrl.class);
                            startActivity(asd);
                            finish();
                        }
                    } else {
                        if(Utility.isConnectingToInternet(SplashActivity.this)){
                            ismaintenancemode(apiUrl);
                        }else{
                            makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    // Maintenance check is DISABLED - skip API call and go directly to next screen
                    Log.e("SplashActivity", "Maintenance check DISABLED - skipping API call");
                    Log.e("SplashActivity", "Reason: Backend has PHP error (language_model not loaded)");
                    Log.e("SplashActivity", "Going directly to next screen...");

                    // Set maintenance mode to false (not in maintenance)
                    Utility.setSharedPreferenceBoolean(getApplicationContext(), "maintenance_mode", false);

                    // Navigate to next screen
                    navigateToNextScreen();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    public void setLocale(String localeName) {
        Locale myLocale = new Locale(localeName);
        Locale.setDefault(myLocale);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Log.e("Status", "Locale updated!");
    }

    private void ismaintenancemode(String siteurl) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        String url = siteurl+ Constants.getMaintenanceModeStatusUrl;
        Log.d("SplashActivity", "Maintenance Mode API URL: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                Log.d("SplashActivity", "API Response: " + result);

                // Check if response contains HTML error (500 error from server)
                if (result != null && result.trim().startsWith("<")) {
                    Log.e("SplashActivity", "Server returned HTML error page instead of JSON");
                    pd.dismiss();
                    handleServerError();
                    return;
                }

                try {
                    JSONObject object = new JSONObject(result);

                    // Check if response has maintenance_mode field
                    if (!object.has("maintenance_mode")) {
                        Log.e("SplashActivity", "Response missing maintenance_mode field");
                        pd.dismiss();
                        handleServerError();
                        return;
                    }

                    String maintenance_mode = object.getString("maintenance_mode");
                    Log.d("SplashActivity", "Maintenance mode: " + maintenance_mode);

                    if(maintenance_mode.equals("0")){
                        Utility.setSharedPreferenceBoolean(getApplicationContext(), "maintenance_mode", false);
                        pd.dismiss();
                        navigateToNextScreen();
                    } else{
                        Utility.setSharedPreferenceBoolean(getApplicationContext(), "maintenance_mode", true);
                        pd.dismiss();
                        showMaintenanceDialog();
                    }
                } catch (JSONException e) {
                    Log.e("SplashActivity", "JSON parsing error: " + e.getMessage());
                    e.printStackTrace();
                    pd.dismiss();
                    handleServerError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("SplashActivity", "Volley Error: " + volleyError.toString());

                // Get detailed error information
                String errorMessage = "Unable to connect to server";
                if (volleyError.networkResponse != null) {
                    int statusCode = volleyError.networkResponse.statusCode;
                    Log.e("SplashActivity", "HTTP Status Code: " + statusCode);

                    if (statusCode == 500) {
                        errorMessage = "Server error (500). Please contact administrator.";
                    } else if (statusCode == 404) {
                        errorMessage = "API endpoint not found (404)";
                    } else if (statusCode >= 400 && statusCode < 500) {
                        errorMessage = "Client error (" + statusCode + ")";
                    } else if (statusCode >= 500) {
                        errorMessage = "Server error (" + statusCode + ")";
                    }

                    // Log response body if available
                    if (volleyError.networkResponse.data != null) {
                        String responseBody = new String(volleyError.networkResponse.data);
                        Log.e("SplashActivity", "Error Response Body: " + responseBody);
                    }
                } else {
                    Log.e("SplashActivity", "Network error - no response from server");
                    errorMessage = "Network error. Please check your internet connection.";
                }

                volleyError.printStackTrace();
                showErrorDialog(errorMessage);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                Log.e("Headers", headers.toString());
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }


        };
        RequestQueue requestQueue = Volley.newRequestQueue(SplashActivity.this);//Creating a Request Queue
        requestQueue.add(stringRequest); //Adding request to the queue
    }

    /**
     * Navigate to the appropriate screen based on login status
     */
    private void navigateToNextScreen() {
        if(isLoggegIn){
            if(isLock){
                Intent i = new Intent(getApplicationContext(), StudentFees.class);
                startActivity(i);
                finish();
            }else{
                Intent i = new Intent(getApplicationContext(), NewDashboard.class);
                startActivity(i);
                finish();
            }
        }else {
            Intent i = new Intent(getApplicationContext(), Login.class);
            startActivity(i);
            finish();
        }
    }

    /**
     * Show maintenance mode dialog
     */
    private void showMaintenanceDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SplashActivity.this);
        builder.setCancelable(false);
        builder.setMessage(R.string.maintainMessage);
        builder.setTitle("Maintenance Mode");
        builder.setPositiveButton("OK", (dialog, which) -> {
            finish();
        });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Handle server errors (500, HTML responses, etc.)
     * Allows user to proceed to login or retry
     */
    private void handleServerError() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SplashActivity.this);
        builder.setCancelable(false);
        builder.setTitle("Server Error");
        builder.setMessage("The server is experiencing issues. You can:\n\n" +
                "1. Retry the connection\n" +
                "2. Continue to login (maintenance check will be skipped)\n" +
                "3. Exit the app\n\n" +
                "Note: If the problem persists, please contact your administrator.");

        builder.setPositiveButton("Retry", (dialog, which) -> {
            dialog.dismiss();
            // Retry the API call - getApiUrl() always returns the configured domain
            ismaintenancemode(Utility.getApiUrl(getApplicationContext()));
        });

        builder.setNeutralButton("Continue to Login", (dialog, which) -> {
            dialog.dismiss();
            // Skip maintenance check and go to login
            Utility.setSharedPreferenceBoolean(getApplicationContext(), "maintenance_mode", false);
            navigateToNextScreen();
        });

        builder.setNegativeButton("Exit", (dialog, which) -> {
            dialog.dismiss();
            finish();
        });

        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Show error dialog with custom message
     */
    private void showErrorDialog(String errorMessage) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SplashActivity.this);
        builder.setCancelable(false);
        builder.setTitle("Connection Error");
        builder.setMessage(errorMessage + "\n\nWhat would you like to do?");

        builder.setPositiveButton("Retry", (dialog, which) -> {
            dialog.dismiss();
            // Retry the API call - getApiUrl() always returns the configured domain
            ismaintenancemode(Utility.getApiUrl(getApplicationContext()));
        });

        builder.setNeutralButton("Continue Anyway", (dialog, which) -> {
            dialog.dismiss();
            // Skip maintenance check and go to login
            Utility.setSharedPreferenceBoolean(getApplicationContext(), "maintenance_mode", false);
            navigateToNextScreen();
        });

        builder.setNegativeButton("Exit", (dialog, which) -> {
            dialog.dismiss();
            finish();
        });

        android.app.AlertDialog alert = builder.create();
        alert.show();
    }
}
