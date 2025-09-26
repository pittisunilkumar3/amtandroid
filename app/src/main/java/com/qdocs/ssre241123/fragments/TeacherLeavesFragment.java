package com.qdocs.ssre241123.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.StudentProfileAdapter;
import com.qdocs.ssre241123.adapters.TeacherLeaveAdapter;
import com.qdocs.ssre241123.models.TeacherLeaveRecord;
import com.qdocs.ssre241123.utils.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("ValidFragment")
public class TeacherLeavesFragment extends Fragment {

    TextView leavesInfoTV;
    RecyclerView recyclerView;
    StudentProfileAdapter adapter;
    TeacherLeaveAdapter leaveAdapter;
    ArrayList<String> leavesValues = new ArrayList<>();
    HashMap<String, String> leavesData = new HashMap<>();
    List<TeacherLeaveRecord> leaveRecords = new ArrayList<>();

    @SuppressLint("ValidFragment")
    public TeacherLeavesFragment() {
    }

    public static TeacherLeavesFragment newInstance(JSONObject teacherData) {
        TeacherLeavesFragment fragment = new TeacherLeavesFragment();
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
        View mainView = inflater.inflate(R.layout.fragment_teacher_leaves, container, false);

        leavesInfoTV = mainView.findViewById(R.id.teacherLeavesInfo);
        recyclerView = mainView.findViewById(R.id.teacherLeavesFragment_recyclerView);

        loadLeavesData();

        if (leavesValues.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            leavesInfoTV.setVisibility(View.VISIBLE);
            leavesInfoTV.setText("Teacher Leaves information will be displayed here.\n\n" +
                               "This section will show:\n" +
                               "• Leave applications\n" +
                               "• Leave balance\n" +
                               "• Leave history\n" +
                               "• Leave policies\n\n" +
                               "No leave data available currently.");
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            leavesInfoTV.setVisibility(View.GONE);

            // Create dynamic header array based on available leave data
            int[] leavesHeaderArray = new int[leavesValues.size()];
            for (int i = 0; i < leavesValues.size(); i++) {
                leavesHeaderArray[i] = R.string.leaves; // Generic leaves string
            }

            // Initialize both adapters
            adapter = new StudentProfileAdapter(getActivity().getApplicationContext(),
                                              leavesHeaderArray, leavesValues, leavesData);
            leaveAdapter = new TeacherLeaveAdapter(getActivity().getApplicationContext(), leaveRecords);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            // Set click listener for leave items
            leaveAdapter.setOnLeaveItemClickListener(new TeacherLeaveAdapter.OnLeaveItemClickListener() {
                @Override
                public void onViewLeaveClick(TeacherLeaveRecord record, int position) {
                    // Handle leave view click - can be implemented later
                    String message = "Leave: " + record.getType() + " (" + record.getLeaveDays() + " days) - " +
                                   record.getStatus();
                    android.widget.Toast.makeText(getActivity(), message, android.widget.Toast.LENGTH_SHORT).show();
                }
            });

            // Initially set the generic adapter, will switch based on data availability
            recyclerView.setAdapter(adapter);
        }

        return mainView;
    }

    private void loadLeavesData() {
        leavesValues.clear();
        leavesData.clear();
        leaveRecords.clear();

        if (getArguments() != null && getArguments().getString("teacherData") != null) {
            try {
                JSONObject teacherData = new JSONObject(getArguments().getString("teacherData"));

                // Extract leave records
                JSONObject leaveRecords = teacherData.optJSONObject("leave_records");
                if (leaveRecords != null) {

                    // Process leave balance
                    JSONArray leaveBalance = leaveRecords.optJSONArray("leave_balance");
                    if (leaveBalance != null && leaveBalance.length() > 0) {
                        leavesValues.add("=== LEAVE BALANCE ===");
                        leavesData.put("Leave Balance", "Available leave types and remaining days");

                        for (int i = 0; i < leaveBalance.length(); i++) {
                            JSONObject balance = leaveBalance.optJSONObject(i);
                            if (balance != null) {
                                String leaveType = balance.optString("type", "").trim();
                                String remaining = balance.optString("remaining_leave", "0");
                                String alloted = balance.optString("alloted_leave", "0");

                                String balanceInfo = remaining + " days remaining (of " + alloted + " alloted)";
                                leavesValues.add(balanceInfo);
                                leavesData.put(leaveType, balanceInfo);
                            }
                        }
                    }

                    // Process leave requests
                    JSONArray leaveRequests = leaveRecords.optJSONArray("leave_requests");
                    if (leaveRequests != null && leaveRequests.length() > 0) {
                        leavesValues.add("=== RECENT LEAVE REQUESTS ===");
                        leavesData.put("Leave Requests", "Recent leave applications and status");

                        for (int i = 0; i < Math.min(leaveRequests.length(), 5); i++) { // Show max 5 recent requests
                            JSONObject request = leaveRequests.optJSONObject(i);
                            if (request != null) {
                                String leaveType = request.optString("type", "");
                                String fromDate = request.optString("leave_from", "");
                                String toDate = request.optString("leave_to", "");
                                String status = request.optString("status", "");
                                String days = request.optString("leave_days", "");

                                // Format dates
                                if (!fromDate.isEmpty()) {
                                    fromDate = Utility.parseDate("yyyy-MM-dd",
                                        Utility.getSharedPreferences(getActivity(), "dateFormat"),
                                        fromDate);
                                }
                                if (!toDate.isEmpty()) {
                                    toDate = Utility.parseDate("yyyy-MM-dd",
                                        Utility.getSharedPreferences(getActivity(), "dateFormat"),
                                        toDate);
                                }

                                String requestInfo = fromDate + " to " + toDate + " (" + days + " days) - " + status.toUpperCase();
                                leavesValues.add(requestInfo);
                                leavesData.put(leaveType.trim(), requestInfo);
                            }
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Update the fragment with new teacher leaves data
     */
    public void updateLeavesData(JSONObject teacherData) {
        if (teacherData != null) {
            Log.d("TeacherLeavesFragment", "Updating leaves data with new teacher data");

            // Update the arguments with new data
            Bundle args = getArguments();
            if (args == null) {
                args = new Bundle();
            }
            args.putString("teacherData", teacherData.toString());
            setArguments(args);

            // Reload leaves data with new information
            loadLeavesData();

            // Parse individual leave records for list display
            parseLeaveRecords(teacherData);

            // Decide which adapter to use based on available data
            if (leaveRecords.size() > 0) {
                Log.d("TeacherLeavesFragment", "Using list adapter with " + leaveRecords.size() + " records");
                leaveAdapter.updateData(leaveRecords);
                recyclerView.setAdapter(leaveAdapter);
                recyclerView.setVisibility(View.VISIBLE);
                leavesInfoTV.setVisibility(View.GONE);
            } else if (!leavesValues.isEmpty()) {
                Log.d("TeacherLeavesFragment", "Using generic adapter with summary data");
                recyclerView.setAdapter(adapter);
                recyclerView.setVisibility(View.VISIBLE);
                leavesInfoTV.setVisibility(View.GONE);
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                    Log.d("TeacherLeavesFragment", "Leaves adapter notified of data changes");
                }
            } else {
                recyclerView.setVisibility(View.GONE);
                leavesInfoTV.setVisibility(View.VISIBLE);
            }
        } else {
            Log.w("TeacherLeavesFragment", "Attempted to update with null teacher data");
        }
    }

    private void parseLeaveRecords(JSONObject teacherData) {
        try {
            JSONObject leaveRecordsObj = teacherData.optJSONObject("leave_records");
            if (leaveRecordsObj != null) {
                JSONArray leaveRequestsArray = leaveRecordsObj.optJSONArray("leave_requests");

                if (leaveRequestsArray != null && leaveRequestsArray.length() > 0) {
                    Log.d("TeacherLeavesFragment", "Found " + leaveRequestsArray.length() + " leave records");

                    for (int i = 0; i < leaveRequestsArray.length(); i++) {
                        JSONObject recordObj = leaveRequestsArray.optJSONObject(i);
                        if (recordObj != null) {
                            TeacherLeaveRecord record = new TeacherLeaveRecord();
                            record.setId(recordObj.optString("id", String.valueOf(i + 1)));
                            record.setStaffId(recordObj.optString("staff_id", ""));
                            record.setLeaveTypeId(recordObj.optString("leave_type_id", ""));
                            record.setLeaveFrom(recordObj.optString("leave_from", ""));
                            record.setLeaveTo(recordObj.optString("leave_to", ""));
                            record.setLeaveDays(recordObj.optString("leave_days", ""));
                            record.setEmployeeRemark(recordObj.optString("employee_remark", ""));
                            record.setAdminRemark(recordObj.optString("admin_remark", ""));
                            record.setStatus(recordObj.optString("status", ""));
                            record.setAppliedBy(recordObj.optString("applied_by", ""));
                            record.setDocumentFile(recordObj.optString("document_file", ""));
                            record.setDate(recordObj.optString("date", ""));
                            record.setCreatedAt(recordObj.optString("created_at", ""));
                            record.setType(recordObj.optString("type", ""));
                            record.setName(recordObj.optString("name", ""));
                            record.setSurname(recordObj.optString("surname", ""));
                            record.setEmployeeId(recordObj.optString("employee_id", ""));

                            leaveRecords.add(record);
                            Log.d("TeacherLeavesFragment", "Added leave record: " + record.getType() + " - " + record.getStatus());
                        }
                    }
                } else {
                    Log.d("TeacherLeavesFragment", "No leave requests array found or empty");
                }
            } else {
                Log.d("TeacherLeavesFragment", "No leave records section found");
            }
        } catch (Exception e) {
            Log.e("TeacherLeavesFragment", "Error parsing leave records: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
