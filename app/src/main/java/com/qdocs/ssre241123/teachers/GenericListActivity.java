package com.qdocs.ssre241123.teachers;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.ssre241123.BaseActivity;
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.GenericCrudAdapter;
import com.qdocs.ssre241123.utils.Constants;
import com.qdocs.ssre241123.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generic List Activity that displays data from an API endpoint in a RecyclerView.
 * Used for screens that only need to view/search data (no CRUD operations).
 *
 * Required Intent Extras:
 *   screen_title - Title shown in action bar
 *   list_url - API endpoint for listing (POST)
 *   name_field - JSON field name for the display text
 *   id_field - JSON field name for the ID (default: "id")
 *
 * Optional Intent Extras:
 *   subtitle_field - Second field to display below name
 *   detail_field - Third field for additional details
 */
public class GenericListActivity extends BaseActivity {

    private static final String TAG = "GenericList";

    private String screenTitle;
    private String listUrl;
    private String nameField;
    private String idField = "id";
    private String subtitleField;
    private String detailField;

    private ImageView backButton;
    private TextView titleTextView;
    private ProgressBar progressBar;
    private LinearLayout nodataLayout;
    private RecyclerView recyclerView;
    private TextView countTextView;

    private List<HashMap<String, String>> itemsList;
    private GenericCrudAdapter adapter;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_list);

        readConfig();
        initializeViews();
        setupRecyclerView();
        loadData();
    }

    private void readConfig() {
        Bundle extras = getIntent().getExtras();
        screenTitle = extras.getString("screen_title", "List");
        listUrl = extras.getString("list_url", "");
        nameField = extras.getString("name_field", "name");
        idField = extras.getString("id_field", "id");
        subtitleField = extras.getString("subtitle_field", null);
        detailField = extras.getString("detail_field", null);
    }

    private void initializeViews() {
        backButton = findViewById(R.id.list_back_button);
        titleTextView = findViewById(R.id.list_title_textview);
        progressBar = findViewById(R.id.list_progress_bar);
        nodataLayout = findViewById(R.id.list_nodata_layout);
        recyclerView = findViewById(R.id.list_recyclerView);
        countTextView = findViewById(R.id.list_count_textview);

        titleTextView.setText(screenTitle);
        backButton.setOnClickListener(v -> onBackPressed());
        requestQueue = Volley.newRequestQueue(this);
    }

    private void setupRecyclerView() {
        itemsList = new ArrayList<>();
        adapter = new GenericCrudAdapter(this, itemsList, nameField, idField);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadData() {
        if (listUrl.isEmpty()) return;
        progressBar.setVisibility(View.VISIBLE);
        nodataLayout.setVisibility(View.GONE);

        String url = Utility.buildApiUrl(getApplicationContext(), listUrl);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.optInt("status") == 1) {
                            JSONArray data = json.optJSONArray("data");
                            itemsList.clear();
                            if (data != null) {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject item = data.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<>();
                                    for (int j = 0; j < item.names().length(); j++) {
                                        String key = item.names().getString(j);
                                        map.put(key, item.optString(key, ""));
                                    }
                                    itemsList.add(map);
                                }
                            }
                            adapter.notifyDataSetChanged();
                            countTextView.setText(itemsList.size() + " item(s)");
                            if (itemsList.isEmpty()) {
                                nodataLayout.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(this, json.optString("message", "Failed"), Toast.LENGTH_SHORT).show();
                            nodataLayout.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Parse error", e);
                        nodataLayout.setVisibility(View.VISIBLE);
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    nodataLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Client-Service", "smartschool");
                headers.put("Auth-Key", "schoolAdmin@");
                return headers;
            }
        };
        requestQueue.add(request);
    }
}
