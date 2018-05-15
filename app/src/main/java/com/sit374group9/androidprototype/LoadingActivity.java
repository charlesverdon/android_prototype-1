package com.sit374group9.androidprototype;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.sit374group9.androidprototype.datastore.UserHelper;
import com.sit374group9.androidprototype.helpers.api;
import com.sit374group9.androidprototype.helpers.broadcastmanager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadingActivity extends AppCompatActivity {

    private static final String TAG = "LoadingActivity";

    private ActionBarDrawerToggle mToggle;

    //Usage objects
    static JSONArray estimateRecentUsage;
    static JSONArray projectedGraphData;

    //Cost objects
    static String dueDate;
    static String invoiceDateIssued;
    static String liveCost;
    static JSONArray pastPayments;
    static String projectedCost;
    static String targetCost;

    //User strings
    static String userFirstName;
    static String userLastName;
    static String userEmail;
    static String userAddress;
    static String userMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        api.trackerEventListener();

        setup();
    }

    public void setup() {

        // Setup broadcast handling
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("FETCHED_USER_DATA");
        broadcastmanager.register(this, broadcastReceiver, intentFilter);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        // Fixes oreo no animation flash bug
        overridePendingTransition(R.anim.empty_animation, R.anim.empty_animation);
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            assert action != null;
            if (action.equals("FETCHED_USER_DATA")) {
                writeToDatabase();
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }

    public static void handleUserData(Object object) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        String userID = user.getUid();

        Gson gson = new Gson();
        String json = gson.toJson(object);

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject userObject = jsonObject.getJSONObject("user");
            JSONObject usersObject = userObject.getJSONObject("users");
            JSONObject userDetailsObject = usersObject.getJSONObject(userID);


            //Usage data
            estimateRecentUsage = userDetailsObject.getJSONArray("estimateRecentUsage");
            projectedGraphData = userDetailsObject.getJSONArray("projectedGraphData");

            //Cost data
            dueDate = userDetailsObject.getString("dueDate");
            invoiceDateIssued = userDetailsObject.getString("invoiceDateIssued");
            liveCost = userDetailsObject.getString("liveCost");
            pastPayments = userDetailsObject.getJSONArray("pastPayments");
            projectedCost = userDetailsObject.getString("projectedCost");
            targetCost = userDetailsObject.getString("targetCost");

            //User strings
            userFirstName = userDetailsObject.getString("firstName");
            userLastName = userDetailsObject.getString("lastName");
            userEmail = userDetailsObject.getString("email");
            userAddress = userDetailsObject.getString("address");
            userMobile = userDetailsObject.getString("mobile");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void writeToDatabase() {
        UserHelper userHelper = new UserHelper(getApplicationContext());
        SQLiteDatabase db = userHelper.getWritableDatabase();

        UserHelper.addUserInfo(1, userAddress, userEmail, userFirstName, userLastName, userMobile, estimateRecentUsage.toString(), projectedGraphData.toString(), dueDate, invoiceDateIssued, liveCost, pastPayments.toString(), projectedCost, db);

        broadcastmanager.sendBroadcast(this, "WROTE_TO_DATABASE");

        Intent intent = new Intent(this, UsageActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
