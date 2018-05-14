package com.sit374group9.androidprototype;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.sit374group9.androidprototype.datastore.UserContract;
import com.sit374group9.androidprototype.datastore.UserHelper;
import com.sit374group9.androidprototype.helpers.broadcastmanager;

public class UsageFragment extends Fragment {

    private static final String TAG = "UsageFragment";

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private static String userID;

    //Usage text views
    private TextView textLiveUsage;
    private TextView textMonthlyUsage;
    private TextView textLastMonthUsage;

    //Cost text views
    private TextView textLiveCost;
    private TextView textMonthlyCost;
    private TextView textLastMonthCost;

    private LinearLayout loading;
    private LinearLayout mainContainer;

    //Usage strings
    String liveUsage;
    String monthlyUsage;
    String lastMonthUsage;

    //Cost strings
    String liveCost;
    String monthlyCost;
    String lastMonthCost;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        return layoutInflater.inflate((R.layout.fragment_usage), container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstaceState) {
        super.onActivityCreated(savedInstaceState);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("WROTE_TO_DATABASE");

        broadcastmanager.register(getContext(), broadcastReceiver, intentFilter);

        mainContainer = (LinearLayout) getActivity().findViewById(R.id.container_usage);

        textLiveUsage = (TextView) getActivity().findViewById(R.id.account_today_usage_value);
        textMonthlyUsage = (TextView) getActivity().findViewById(R.id.account_this_month_usage_value);
        textLastMonthUsage = (TextView) getActivity().findViewById(R.id.account_last_month_usage_value);

        textLiveCost = (TextView) getActivity().findViewById(R.id.account_today_cost);
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

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            assert action != null;
            if (action.equals("WROTE_TO_DATABASE")) {
                getUsageInfo();
            }
        }
    };

    public void getUsageInfo() {
//        UserHelper userHelper = new UserHelper(getActivity());
//        SQLiteDatabase db = userHelper.getReadableDatabase();
//
//        Cursor cursor = userHelper.readUserInfo(db);
//
//        while (cursor.moveToNext()) {
//            liveUsage = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.LIVE_USAGE));
//            monthlyUsage = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.MONTHLY_USAGE));
//            lastMonthUsage = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.LAST_MONTH_USAGE));
//            liveCost = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.LIVE_COST));
//            monthlyCost = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.MONTHLY_COST));
//            lastMonthCost = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.LAST_MONTH_COST));
//        }
//
//        textLiveUsage.setText(liveUsage);
//        textMonthlyUsage.setText(monthlyUsage);
//        textLastMonthUsage.setText(lastMonthUsage);
//
//        textLiveCost.setText(liveCost);
//        textMonthlyCost.setText(monthlyCost);
//        textLastMonthCost.setText(lastMonthCost);
//
//        // Hide loading container and show main container
//        loading.setVisibility(View.GONE);
//        mainContainer.setVisibility(View.VISIBLE);
    }
}
