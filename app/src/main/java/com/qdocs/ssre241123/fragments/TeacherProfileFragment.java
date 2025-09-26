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
import com.qdocs.ssre241123.R;
import com.qdocs.ssre241123.adapters.StudentProfileAdapter;
import com.qdocs.ssre241123.utils.Utility;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("ValidFragment")
public class TeacherProfileFragment extends Fragment {
    
    RecyclerView recyclerView;
    StudentProfileAdapter adapter;
    ArrayList<String> profileValues = new ArrayList<String>();
    HashMap<String, String> profileData = new HashMap<>();
    
    // Profile field arrays matching the comprehensive teacher profile
    int[] profileHeaderArray = {
        R.string.employee_id,
        R.string.designation,
        R.string.department,
        R.string.phone,
        R.string.email,
        R.string.emergency_contact,
        R.string.qualification,
        R.string.work_experience,
        R.string.date_of_joining,
        R.string.marital_status,
        R.string.father_name,
        R.string.mother_name,
        R.string.local_address,
        R.string.permanent_address
    };

    @SuppressLint("ValidFragment")
    public TeacherProfileFragment() {
    }

    public static TeacherProfileFragment newInstance(JSONObject teacherData) {
        TeacherProfileFragment fragment = new TeacherProfileFragment();
        Bundle args = new Bundle();
        if (teacherData != null) {
            args.putString("teacherData", teacherData.toString());
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadProfileData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_teacher_profile, container, false);
        
        recyclerView = mainView.findViewById(R.id.teacherProfileFragment_recyclerView);
        adapter = new StudentProfileAdapter(getActivity().getApplicationContext(), 
                                          profileHeaderArray, profileValues, profileData);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        
        return mainView;
    }

    private void loadProfileData() {
        profileValues.clear();
        profileData.clear();

        if (getArguments() != null && getArguments().getString("teacherData") != null) {
            try {
                JSONObject teacherData = new JSONObject(getArguments().getString("teacherData"));

                // Check if this is an error state
                if (teacherData.optBoolean("error", false)) {
                    String errorMessage = teacherData.optString("error_message", "Unknown error occurred");
                    for (int i = 0; i < profileHeaderArray.length; i++) {
                        profileValues.add("Error: " + errorMessage);
                    }
                    return;
                }

                // Get school settings to control field visibility
                JSONObject schoolSettings = teacherData.optJSONObject("school_settings");

                // Extract basic info
                JSONObject basicInfo = teacherData.optJSONObject("basic_info");
                if (basicInfo != null) {
                    profileValues.add(basicInfo.optString("employee_id", "Not provided"));
                    profileValues.add(basicInfo.optString("designation_name", "Not provided"));
                    profileValues.add(basicInfo.optString("department_name", "Not provided"));
                } else {
                    profileValues.add("Not available");
                    profileValues.add("Not available");
                    profileValues.add("Not available");
                }

                // Extract contact info with school settings check
                JSONObject contactInfo = teacherData.optJSONObject("contact_info");
                if (contactInfo != null) {
                    // Phone - check school setting
                    if (schoolSettings != null && schoolSettings.optInt("staff_phone", 1) == 1) {
                        profileValues.add(contactInfo.optString("contact_no", "Not provided"));
                    } else {
                        profileValues.add("Hidden by school policy");
                    }

                    profileValues.add(contactInfo.optString("email", "Not provided"));

                    // Emergency contact - check school setting
                    if (schoolSettings != null && schoolSettings.optInt("staff_emergency_contact", 1) == 1) {
                        profileValues.add(contactInfo.optString("emergency_contact_no", "Not provided"));
                    } else {
                        profileValues.add("Hidden by school policy");
                    }
                } else {
                    profileValues.add("Not available");
                    profileValues.add("Not available");
                    profileValues.add("Not available");
                }

                // Extract personal info with school settings check
                JSONObject personalInfo = teacherData.optJSONObject("personal_info");
                if (personalInfo != null) {
                    // Qualification - check school setting
                    if (schoolSettings != null && schoolSettings.optInt("staff_qualification", 1) == 1) {
                        profileValues.add(personalInfo.optString("qualification", "Not provided"));
                    } else {
                        profileValues.add("Hidden by school policy");
                    }

                    // Work experience - check school setting
                    if (schoolSettings != null && schoolSettings.optInt("staff_work_experience", 1) == 1) {
                        profileValues.add(personalInfo.optString("work_exp", "Not provided"));
                    } else {
                        profileValues.add("Hidden by school policy");
                    }

                    // Date of joining from basic_info
                    String joiningDate = "Not provided";
                    if (basicInfo != null) {
                        String rawDate = basicInfo.optString("date_of_joining", "");
                        if (!rawDate.isEmpty()) {
                            try {
                                joiningDate = Utility.parseDate("yyyy-MM-dd",
                                    Utility.getSharedPreferences(getActivity(), "dateFormat"),
                                    rawDate);
                            } catch (Exception e) {
                                joiningDate = rawDate; // Use raw date if parsing fails
                            }
                        }
                    }
                    profileValues.add(joiningDate);

                    // Marital status - check school setting
                    if (schoolSettings != null && schoolSettings.optInt("staff_marital_status", 1) == 1) {
                        profileValues.add(personalInfo.optString("marital_status", "Not provided"));
                    } else {
                        profileValues.add("Hidden by school policy");
                    }

                    // Father name - check school setting
                    if (schoolSettings != null && schoolSettings.optInt("staff_father_name", 1) == 1) {
                        profileValues.add(personalInfo.optString("father_name", "Not provided"));
                    } else {
                        profileValues.add("Hidden by school policy");
                    }

                    // Mother name - check school setting
                    if (schoolSettings != null && schoolSettings.optInt("staff_mother_name", 1) == 1) {
                        profileValues.add(personalInfo.optString("mother_name", "Not provided"));
                    } else {
                        profileValues.add("Hidden by school policy");
                    }
                } else {
                    profileValues.add("Not available");
                    profileValues.add("Not available");
                    profileValues.add("Not available");
                    profileValues.add("Not available");
                    profileValues.add("Not available");
                    profileValues.add("Not available");
                }

                // Extract address info with school settings check
                JSONObject addressInfo = teacherData.optJSONObject("address_info");
                if (addressInfo != null) {
                    // Local address - check school setting
                    if (schoolSettings != null && schoolSettings.optInt("staff_current_address", 1) == 1) {
                        profileValues.add(addressInfo.optString("local_address", "Not provided"));
                    } else {
                        profileValues.add("Hidden by school policy");
                    }

                    // Permanent address - check school setting
                    if (schoolSettings != null && schoolSettings.optInt("staff_permanent_address", 1) == 1) {
                        profileValues.add(addressInfo.optString("permanent_address", "Not provided"));
                    } else {
                        profileValues.add("Hidden by school policy");
                    }
                } else {
                    profileValues.add("Not available");
                    profileValues.add("Not available");
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("TeacherProfileFragment", "JSON parsing error: " + e.getMessage());
                // Fill with error values if parsing fails
                for (int i = 0; i < profileHeaderArray.length; i++) {
                    profileValues.add("Error loading data");
                }
            }
        } else {
            // Fill with empty values if no data
            for (int i = 0; i < profileHeaderArray.length; i++) {
                profileValues.add("No data available");
            }
        }

        // Populate profile data map for adapter
        for (int i = 0; i < profileHeaderArray.length && i < profileValues.size(); i++) {
            profileData.put(String.valueOf(profileHeaderArray[i]), profileValues.get(i));
        }
    }

    /**
     * Update the fragment with new teacher profile data
     * This method is called when API data is received
     */
    public void updateProfileData(JSONObject teacherData) {
        if (teacherData != null) {
            Log.d("TeacherProfileFragment", "Updating profile data with new teacher data");

            // Update the arguments with new data
            Bundle args = getArguments();
            if (args == null) {
                args = new Bundle();
            }
            args.putString("teacherData", teacherData.toString());
            setArguments(args);

            // Reload profile data with new information
            loadProfileData();

            // Notify adapter of data changes if it exists
            if (adapter != null) {
                adapter.notifyDataSetChanged();
                Log.d("TeacherProfileFragment", "Profile adapter notified of data changes");
            }
        } else {
            Log.w("TeacherProfileFragment", "Attempted to update with null teacher data");
        }
    }
}
