package com.sit374group9.androidprototype;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import org.json.JSONException;
import org.json.JSONObject;

public class CustomerActivity extends AppCompatActivity {

    private static final String TAG = "CustomerActivity";

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;

    //Usage strings
    static String liveUsage;
    static String monthlyUsage;
    static String lastMonthUsage;

    //Cost strings
    static String liveCost;
    static String monthlyCost;
    static String lastMonthCost;

    //User strings
    static String userFirstName;
    static String userLastName;
    static String userEmail;
    static String userAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        setupToolbar();
        api.trackerEventListener();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("FETCHED_USER_DATA");

        broadcastmanager.register(this, broadcastReceiver, intentFilter);
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

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Usage"));
        tabLayout.addTab(tabLayout.newTab().setText("Account"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            JSONObject userObject = jsonObject.getJSONObject("user");;
            JSONObject usersObject = userObject.getJSONObject("users");
            JSONObject userDetailsObject = usersObject.getJSONObject(userID);

            //Usage strings
            liveUsage = userDetailsObject.getString("dailyUsage");
            monthlyUsage = userDetailsObject.getString("monthlyUsage");
            lastMonthUsage = userDetailsObject.getString("lastMonthUsage");

            //Cost strings
            liveCost = userDetailsObject.getString("dailyCost");
            monthlyCost = userDetailsObject.getString("monthlyCost");
            lastMonthCost = userDetailsObject.getString("lastMonthCost");

            userFirstName = userDetailsObject.getString("firstName");
            userLastName = userDetailsObject.getString("lastName");
            userEmail = userDetailsObject.getString("email");
            userAddress = userDetailsObject.getString("address");


        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public void writeToDatabase() {
        UserHelper userHelper = new UserHelper(getApplicationContext());
        SQLiteDatabase db = userHelper.getWritableDatabase();
        UserHelper.addUserInfo(1, userFirstName, userLastName, userEmail, userAddress, liveUsage, monthlyUsage, lastMonthUsage, liveCost, monthlyCost, lastMonthCost, db);

        broadcastmanager.sendBroadcast(this, "WROTE_TO_DATABASE");
    }
}
