package com.sit374group9.androidprototype;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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

public class CustomerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "CustomerActivity";

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;

    //Usage strings
    static String recentUsage;
    static String monthlyUsage;
    static String lastMonthUsage;

    //Cost strings
    static String recentCost;
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
        mDrawerlayout = (DrawerLayout)findViewById(R.id.drawerlayout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerlayout,R.string.open,R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView=(NavigationView)findViewById(R.id.navigationview);
        navigationView.setNavigationItemSelectedListener(this);


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

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)){
            return true;
        }
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
            recentUsage = userDetailsObject.getString("recentUsage");
            monthlyUsage = userDetailsObject.getString("monthlyUsage");
            lastMonthUsage = userDetailsObject.getString("lastMonthUsage");

            //Cost strings
            recentCost = userDetailsObject.getString("recentCost");
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
        UserHelper.addUserInfo(1, userFirstName, userLastName, userEmail, userAddress, recentUsage, monthlyUsage, lastMonthUsage, recentCost, monthlyCost, lastMonthCost, db);

        broadcastmanager.sendBroadcast(this, "WROTE_TO_DATABASE");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        int id =item.getItemId();

        if(id ==R.id.db0){
            Intent signupIntent = new Intent(CustomerActivity.this, MainActivity.class);
            startActivity(signupIntent);
        }

        return false;
    }

}
