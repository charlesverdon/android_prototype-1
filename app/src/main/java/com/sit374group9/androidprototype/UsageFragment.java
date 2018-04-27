package com.sit374group9.androidprototype;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.sit374group9.androidprototype.helpers.api;

import org.json.JSONException;
import org.json.JSONObject;

public class UsageFragment extends Fragment {

    private static final String TAG = "UsageFragment";

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private static String userID;

    //Usage text views
    private static TextView textDailyUsage;
    private static TextView textMonthlyUsage;
    private static TextView textLastMonthUsage;

    //Cost text views
    private static TextView textDailyCost;
    private static TextView textMonthlyCost;
    private static TextView textLastMonthCost;

    private static LinearLayout loading;
    private static LinearLayout mainContainer;
    private static LinearLayout emptyContainer;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        return layoutInflater.inflate((R.layout.fragment_usage), container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstaceState) {
        super.onActivityCreated(savedInstaceState);

        mainContainer = (LinearLayout) getActivity().findViewById(R.id.container_usage);
        emptyContainer = (LinearLayout) getActivity().findViewById(R.id.usage_processing_warning);

        textDailyUsage = (TextView) getActivity().findViewById(R.id.account_today_usage_value);
        textMonthlyUsage = (TextView) getActivity().findViewById(R.id.account_this_month_usage_value);
        textLastMonthUsage = (TextView) getActivity().findViewById(R.id.account_last_month_usage_value);

        textDailyCost = (TextView) getActivity().findViewById(R.id.account_today_cost);
        textMonthlyCost = (TextView) getActivity().findViewById(R.id.account_this_month_cost);
        textLastMonthCost = (TextView) getActivity().findViewById(R.id.account_last_month_cost);

        loading = (LinearLayout) getActivity().findViewById(R.id.progress_bar_usage);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        userID = user.getUid();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "user is signed in " + user.getUid() + " " + user.getEmail());
                } else {
                    Log.d(TAG, "user is logged out");
                }
            }
        };

        api.usageTrackerEventListener();
    }

    public static void showData(Object object) {
        Log.d(TAG, "Attempting to fetch usage/cost data");
        Gson gson = new Gson();
        String json = gson.toJson(object);

        String dailyUsage = "";
        String dailyCost = "";

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject userObject = jsonObject.getJSONObject("user");;
            JSONObject usersObject = userObject.getJSONObject("users");
            JSONObject userDetailsObject = usersObject.getJSONObject(userID);

            //Usage strings
            dailyUsage = userDetailsObject.getString("dailyUsage");
            String monthlyUsage = userDetailsObject.getString("monthlyUsage");
            String lastMonthUsage = userDetailsObject.getString("lastMonthUsage");

            //Set usage text views
            textDailyUsage.setText(String.format("%s kWh", dailyUsage));
            textMonthlyUsage.setText(String.format("%s kWh", monthlyUsage));
            textLastMonthUsage.setText(String.format("%s kWh", lastMonthUsage));

            //Cost strings
            dailyCost = userDetailsObject.getString("dailyCost");
            String monthlyCost = userDetailsObject.getString("monthlyCost");
            String lastMonthCost = userDetailsObject.getString("lastMonthCost");

            //Set cost text views
            textDailyCost.setText(String.format("$%s", dailyCost));
            textMonthlyCost.setText(String.format("$%s", monthlyCost));
            textLastMonthCost.setText(String.format("$%s", lastMonthCost));

            Log.d(TAG, "Successfully retrieved usage/cost data");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Failed to retrieved usage/cost data");
        }

        loading.setVisibility(View.GONE);

        if (dailyUsage.isEmpty() || dailyCost.isEmpty()) {
            emptyContainer.setVisibility(View.VISIBLE);
        } else {
            mainContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            firebaseAuth.removeAuthStateListener(authListener);
        }
    }
}
