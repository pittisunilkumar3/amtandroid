package com.qdocs.ssre241123.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.qdocs.ssre241123.Login;
import com.qdocs.ssre241123.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class TeacherAuthHelper {

    public static void performTeacherLogout(Context context) {
        if (!Utility.isConnectingToInternet(context)) {
            Toast.makeText(context, R.string.noInternetMsg, Toast.LENGTH_LONG).show();
            return;
        }

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Logging out...");
        pd.setCancelable(false);
        pd.show();

        Map<String, String> params = new Hashtable<String, String>();
        Map<String, String> headers = new HashMap<String, String>();

        // Add device token if available
        String device_token = FirebaseInstanceId.getInstance().getToken() + "";
        if (device_token != null && !device_token.equals("null")) {
            params.put("deviceToken", device_token);
        }

        JSONObject obj = new JSONObject(params);
        final String requestBody = obj.toString();
        
        String url = Utility.getSharedPreferences(context, "apiUrl") + Constants.teacherLogoutUrl;
        Log.e("Logout URL", url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Logout Result", result);
                        JSONObject object = new JSONObject(result);
                        String status = object.getString("status");
                        String message = object.getString("message");

                        if (status.equals("200") || status.equals("1")) {
                            // Logout successful
                            clearTeacherData(context);
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            
                            // Navigate to Login screen
                            Intent intent = new Intent(context, Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            
                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Even if parsing fails, clear local data
                        clearTeacherData(context);
                        Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show();
                        
                        Intent intent = new Intent(context, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                } else {
                    pd.dismiss();
                    // Clear local data even if server response fails
                    clearTeacherData(context);
                    Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show();
                    
                    Intent intent = new Intent(context, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Log.e("Logout Volley Error", volleyError.toString());
                
                // Clear local data even if network request fails
                clearTeacherData(context);
                Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show();
                
                Intent intent = new Intent(context, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(context, Constants.teacherId));
                headers.put("Authorization", Utility.getSharedPreferences(context, Constants.teacherToken));
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public static void clearTeacherData(Context context) {
        // Clear teacher login status
        Utility.setSharedPreferenceBoolean(context, Constants.isTeacherLoggedIn, false);
        
        // Clear authentication data
        Utility.setSharedPreference(context, Constants.teacherId, "");
        Utility.setSharedPreference(context, Constants.teacherToken, "");
        Utility.setSharedPreference(context, Constants.teacherJwtToken, "");
        
        // Clear teacher profile data
        Utility.setSharedPreference(context, Constants.teacherStaffId, "");
        Utility.setSharedPreference(context, Constants.teacherEmployeeId, "");
        Utility.setSharedPreference(context, Constants.teacherName, "");
        Utility.setSharedPreference(context, Constants.teacherSurname, "");
        Utility.setSharedPreference(context, Constants.teacherEmail, "");
        Utility.setSharedPreference(context, Constants.teacherContact, "");
        Utility.setSharedPreference(context, Constants.teacherDesignation, "");
        Utility.setSharedPreference(context, Constants.teacherDepartment, "");
        Utility.setSharedPreference(context, Constants.teacherImage, "");
        Utility.setSharedPreference(context, Constants.userName, "");
        Utility.setSharedPreference(context, Constants.loginType, "");
    }

    public static boolean isTeacherLoggedIn(Context context) {
        return Utility.getSharedPreferencesBoolean(context, Constants.isTeacherLoggedIn);
    }

    public static String getTeacherToken(Context context) {
        return Utility.getSharedPreferences(context, Constants.teacherToken);
    }

    public static String getTeacherJwtToken(Context context) {
        String jwtToken = Utility.getSharedPreferences(context, Constants.teacherJwtToken);
        return (jwtToken != null && !jwtToken.equals("null")) ? jwtToken : "";
    }

    public static String getTeacherId(Context context) {
        return Utility.getSharedPreferences(context, Constants.teacherId);
    }

    public static String getTeacherName(Context context) {
        String firstName = Utility.getSharedPreferences(context, Constants.teacherName);
        String lastName = Utility.getSharedPreferences(context, Constants.teacherSurname);
        return firstName + " " + lastName;
    }
}
