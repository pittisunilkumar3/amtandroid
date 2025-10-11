package com.qdocs.ssre241123.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for parsing and managing Other Collection Report filter data
 * Handles the response from /api/other-collection-report/list endpoint
 */
public class OtherCollectionReportFilterHelper {

    private static final String TAG = "OtherCollectionFilterHelper";

    // Filter data containers
    private List<SearchTypeOption> searchTypes = new ArrayList<>();
    private List<GroupByOption> groupByOptions = new ArrayList<>();
    private List<ClassOption> classes = new ArrayList<>();
    private List<FeeTypeOption> feeTypes = new ArrayList<>();
    private List<ReceivedByOption> receivedBy = new ArrayList<>();

    /**
     * Parse the complete filter data response from the API
     */
    public boolean parseFilterData(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            
            if (jsonResponse.getInt("status") != 1) {
                Log.e(TAG, "API returned error status");
                return false;
            }

            if (!jsonResponse.has("data")) {
                Log.e(TAG, "No data object in response");
                return false;
            }

            JSONObject data = jsonResponse.getJSONObject("data");
            
            // Parse all filter arrays
            if (data.has("search_types")) {
                parseSearchTypes(data.getJSONArray("search_types"));
            }
            
            if (data.has("group_by")) {
                parseGroupBy(data.getJSONArray("group_by"));
            }
            
            if (data.has("classes")) {
                parseClasses(data.getJSONArray("classes"));
            }
            
            if (data.has("fee_types")) {
                parseFeeTypes(data.getJSONArray("fee_types"));
            }
            
            if (data.has("received_by")) {
                parseReceivedBy(data.getJSONArray("received_by"));
            }

            Log.d(TAG, "Successfully parsed filter data:");
            Log.d(TAG, "  - Search Types: " + searchTypes.size());
            Log.d(TAG, "  - Group By Options: " + groupByOptions.size());
            Log.d(TAG, "  - Classes: " + classes.size());
            Log.d(TAG, "  - Fee Types: " + feeTypes.size());
            Log.d(TAG, "  - Received By: " + receivedBy.size());

            return true;
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing filter data", e);
            return false;
        }
    }

    /**
     * Parse search_types array
     * Can handle both formats:
     * 1. Array of strings: ["Today", "This Week", ...]
     * 2. Array of objects: [{"key":"today","label":"Today"}, ...]
     */
    private void parseSearchTypes(JSONArray array) throws JSONException {
        searchTypes.clear();
        for (int i = 0; i < array.length(); i++) {
            Object item = array.get(i);

            if (item instanceof String) {
                // Format 1: Simple string array
                String type = (String) item;
                searchTypes.add(new SearchTypeOption(type, type.toLowerCase().replace(" ", "_")));
                Log.d(TAG, "Search Type (string): " + type);
            } else if (item instanceof JSONObject) {
                // Format 2: Object with key and label
                JSONObject obj = (JSONObject) item;
                String label = obj.getString("label");
                String key = obj.getString("key");
                searchTypes.add(new SearchTypeOption(label, key));
                Log.d(TAG, "Search Type (object): label=" + label + ", key=" + key);
            } else {
                Log.w(TAG, "Unknown search type format at index " + i);
            }
        }
    }

    /**
     * Parse group_by array
     * Example: ["Group By Class", "Group By Collection", "Group By Payment Mode"]
     */
    private void parseGroupBy(JSONArray array) throws JSONException {
        groupByOptions.clear();
        for (int i = 0; i < array.length(); i++) {
            String option = array.getString(i);
            groupByOptions.add(new GroupByOption(option));
            Log.d(TAG, "Group By: " + option);
        }
    }

    /**
     * Parse classes array
     * Example: [{"id": "1", "class": "Class 1"}, {"id": "2", "class": "Class 2"}]
     */
    private void parseClasses(JSONArray array) throws JSONException {
        classes.clear();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String id = obj.optString("id", "");
            String className = obj.optString("class", "");
            
            if (!id.isEmpty() && !className.isEmpty()) {
                classes.add(new ClassOption(id, className));
                Log.d(TAG, "Class: id=" + id + ", name=" + className);
            }
        }
    }

    /**
     * Parse fee_types array
     * Example: [{"id": "1", "type": "Hostel Fee"}, {"id": "2", "type": "Library Fee"}]
     */
    private void parseFeeTypes(JSONArray array) throws JSONException {
        feeTypes.clear();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String id = obj.optString("id", "");
            String type = obj.optString("type", "");
            
            if (!id.isEmpty() && !type.isEmpty()) {
                feeTypes.add(new FeeTypeOption(id, type));
                Log.d(TAG, "Fee Type: id=" + id + ", type=" + type);
            }
        }
    }

    /**
     * Parse received_by array
     * Example: [{"id": "1", "name": "John Doe"}, {"id": "2", "name": "Jane Smith"}]
     */
    private void parseReceivedBy(JSONArray array) throws JSONException {
        receivedBy.clear();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String id = obj.optString("id", "");
            String name = obj.optString("name", "");
            
            if (!id.isEmpty() && !name.isEmpty()) {
                receivedBy.add(new ReceivedByOption(id, name));
                Log.d(TAG, "Received By: id=" + id + ", name=" + name);
            }
        }
    }

    // Getters for filter data
    public List<SearchTypeOption> getSearchTypes() {
        return searchTypes;
    }

    public List<GroupByOption> getGroupByOptions() {
        return groupByOptions;
    }

    public List<ClassOption> getClasses() {
        return classes;
    }

    public List<FeeTypeOption> getFeeTypes() {
        return feeTypes;
    }

    public List<ReceivedByOption> getReceivedBy() {
        return receivedBy;
    }

    // Data classes for filter options
    public static class SearchTypeOption {
        private String displayName;
        private String value;

        public SearchTypeOption(String displayName) {
            this.displayName = displayName;
            // Convert display name to value (lowercase with underscores)
            this.value = displayName.toLowerCase().replace(" ", "_");
        }

        public SearchTypeOption(String displayName, String value) {
            this.displayName = displayName;
            this.value = value;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getValue() {
            return value;
        }
    }

    public static class GroupByOption {
        private String displayName;
        private String value;

        public GroupByOption(String displayName) {
            this.displayName = displayName;
            // Extract the grouping key from display name
            // "Group By Class" -> "class"
            // "Group By Collection" -> "collection"
            // "Group By Payment Mode" -> "payment_mode"
            if (displayName.toLowerCase().contains("class")) {
                this.value = "class";
            } else if (displayName.toLowerCase().contains("collection")) {
                this.value = "collection";
            } else if (displayName.toLowerCase().contains("payment mode")) {
                this.value = "payment_mode";
            } else {
                this.value = displayName.toLowerCase().replace("group by ", "").replace(" ", "_");
            }
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getValue() {
            return value;
        }
    }

    public static class ClassOption {
        private String id;
        private String className;

        public ClassOption(String id, String className) {
            this.id = id;
            this.className = className;
        }

        public String getId() {
            return id;
        }

        public String getClassName() {
            return className;
        }
    }

    public static class FeeTypeOption {
        private String id;
        private String type;

        public FeeTypeOption(String id, String type) {
            this.id = id;
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }
    }

    public static class ReceivedByOption {
        private String id;
        private String name;

        public ReceivedByOption(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}

