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
import com.qdocs.ssre241123.utils.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("ValidFragment")
public class TeacherTimelineFragment extends Fragment {

    TextView timelineInfoTV;
    RecyclerView recyclerView;
    StudentProfileAdapter adapter;
    ArrayList<String> timelineValues = new ArrayList<>();
    HashMap<String, String> timelineData = new HashMap<>();

    @SuppressLint("ValidFragment")
    public TeacherTimelineFragment() {
    }

    public static TeacherTimelineFragment newInstance(JSONObject teacherData) {
        TeacherTimelineFragment fragment = new TeacherTimelineFragment();
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
        View mainView = inflater.inflate(R.layout.fragment_teacher_timeline, container, false);

        timelineInfoTV = mainView.findViewById(R.id.teacherTimelineInfo);
        recyclerView = mainView.findViewById(R.id.teacherTimelineFragment_recyclerView);

        loadTimelineData();

        if (timelineValues.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            timelineInfoTV.setVisibility(View.VISIBLE);
            timelineInfoTV.setText("Teacher Timeline information will be displayed here.\n\n" +
                                 "This section will show:\n" +
                                 "• Professional milestones\n" +
                                 "• Career progression\n" +
                                 "• Training and certifications\n" +
                                 "• Performance reviews\n" +
                                 "• Important events\n\n" +
                                 "No timeline events available currently.");
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            timelineInfoTV.setVisibility(View.GONE);

            // Create dynamic header array based on available timeline data
            int[] timelineHeaderArray = new int[timelineValues.size()];
            for (int i = 0; i < timelineValues.size(); i++) {
                timelineHeaderArray[i] = R.string.timeline; // Generic timeline string
            }

            adapter = new StudentProfileAdapter(getActivity().getApplicationContext(),
                                              timelineHeaderArray, timelineValues, timelineData);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }

        return mainView;
    }

    private void loadTimelineData() {
        timelineValues.clear();
        timelineData.clear();

        if (getArguments() != null && getArguments().getString("teacherData") != null) {
            try {
                JSONObject teacherData = new JSONObject(getArguments().getString("teacherData"));

                // Extract timeline data
                JSONObject timeline = teacherData.optJSONObject("timeline");
                if (timeline != null) {
                    JSONArray timelineEvents = timeline.optJSONArray("timeline_events");
                    int totalEvents = timeline.optInt("total_events", 0);

                    if (timelineEvents != null && timelineEvents.length() > 0) {
                        timelineValues.add("=== TIMELINE EVENTS ===");
                        timelineData.put("Timeline Events", "Professional timeline and milestones");

                        for (int i = 0; i < timelineEvents.length(); i++) {
                            JSONObject event = timelineEvents.optJSONObject(i);
                            if (event != null) {
                                String title = event.optString("title", "");
                                String description = event.optString("description", "");
                                String date = event.optString("date", "");
                                String status = event.optString("status", "");

                                // Format date if available
                                if (!date.isEmpty()) {
                                    try {
                                        date = Utility.parseDate("yyyy-MM-dd",
                                            Utility.getSharedPreferences(getActivity(), "dateFormat"),
                                            date);
                                    } catch (Exception e) {
                                        Log.e("TeacherTimelineFragment", "Date parsing error: " + e.getMessage());
                                    }
                                }

                                String eventInfo = title;
                                if (!date.isEmpty()) {
                                    eventInfo += " (" + date + ")";
                                }
                                if (!status.isEmpty()) {
                                    eventInfo += " - " + status;
                                }
                                if (!description.isEmpty()) {
                                    eventInfo += "\n" + description;
                                }

                                timelineValues.add(eventInfo);
                                timelineData.put("Event " + (i + 1), eventInfo);
                            }
                        }
                    }

                    // Add summary information
                    if (totalEvents > 0) {
                        timelineValues.add("=== SUMMARY ===");
                        timelineValues.add("Total Events: " + totalEvents);
                        timelineData.put("Total Events", String.valueOf(totalEvents));
                    }
                }

                // If no timeline data, add basic profile milestones from other sections
                if (timelineValues.isEmpty()) {
                    addBasicMilestones(teacherData);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("TeacherTimelineFragment", "JSON parsing error: " + e.getMessage());
            }
        }
    }

    private void addBasicMilestones(JSONObject teacherData) {
        try {
            timelineValues.add("=== CAREER MILESTONES ===");
            timelineData.put("Career Milestones", "Key professional milestones");

            // Add joining date as a milestone
            JSONObject basicInfo = teacherData.optJSONObject("basic_info");
            if (basicInfo != null) {
                String joiningDate = basicInfo.optString("date_of_joining", "");
                String designation = basicInfo.optString("designation_name", "");
                String department = basicInfo.optString("department_name", "");

                if (!joiningDate.isEmpty()) {
                    String formattedDate = Utility.parseDate("yyyy-MM-dd",
                        Utility.getSharedPreferences(getActivity(), "dateFormat"),
                        joiningDate);

                    String milestone = "Joined as " + designation + " in " + department + " department";
                    timelineValues.add(milestone + " (" + formattedDate + ")");
                    timelineData.put("Joining", milestone);
                }
            }

            // Add qualification as milestone
            JSONObject personalInfo = teacherData.optJSONObject("personal_info");
            if (personalInfo != null) {
                String qualification = personalInfo.optString("qualification", "");
                String workExp = personalInfo.optString("work_exp", "");

                if (!qualification.isEmpty()) {
                    timelineValues.add("Education: " + qualification);
                    timelineData.put("Education", qualification);
                }

                if (!workExp.isEmpty()) {
                    timelineValues.add("Work Experience: " + workExp);
                    timelineData.put("Experience", workExp);
                }
            }

        } catch (Exception e) {
            Log.e("TeacherTimelineFragment", "Error adding basic milestones: " + e.getMessage());
        }
    }

    /**
     * Update the fragment with new teacher timeline data
     */
    public void updateTimelineData(JSONObject teacherData) {
        if (teacherData != null) {
            Log.d("TeacherTimelineFragment", "Updating timeline data with new teacher data");

            // Update the arguments with new data
            Bundle args = getArguments();
            if (args == null) {
                args = new Bundle();
            }
            args.putString("teacherData", teacherData.toString());
            setArguments(args);

            // Reload timeline data with new information
            loadTimelineData();

            // Update UI visibility and adapter
            if (timelineValues.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                timelineInfoTV.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                timelineInfoTV.setVisibility(View.GONE);

                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                    Log.d("TeacherTimelineFragment", "Timeline adapter notified of data changes");
                }
            }
        } else {
            Log.w("TeacherTimelineFragment", "Attempted to update with null teacher data");
        }
    }
}
