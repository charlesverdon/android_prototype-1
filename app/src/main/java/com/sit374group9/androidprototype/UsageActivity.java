package com.sit374group9.androidprototype;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.sit374group9.androidprototype.datastore.UserContract;
import com.sit374group9.androidprototype.datastore.UserHelper;

import org.json.JSONArray;
import org.json.JSONException;

public class UsageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle mToggle;
    String liveUsage;
    String targetUsage;
    String projectedUsage;
    String projectedGraphString;
    String estimateString;

    JSONArray projectedGraphData;
    float[] projectedArray;

    JSONArray estimateUsage;
    float[] estimateArray;

    TextView usage;
    TextView Tusage;
    TextView Pusage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage);

        setup();
        getUsageInfo();
    }

    public void setup() {
        setTitle("Usage");
        // Setup drawer menu
        DrawerLayout mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Fixes oreo no animation flash bug
        overridePendingTransition(R.anim.empty_animation, R.anim.empty_animation);

        usage = (TextView) this.findViewById(R.id.usage);
        Tusage = (TextView) this.findViewById(R.id.targetusage);
        Pusage = (TextView) this.findViewById(R.id.projectedusage);
    }

    public void setupGraph() {
        GraphView graphView = (GraphView) findViewById(R.id.graph_usage);

        LineGraphSeries<DataPoint> usageLineGraphSeries = new LineGraphSeries<>(getUsageData());
        usageLineGraphSeries.setTitle("Estimate recent usage");
        usageLineGraphSeries.setColor(Color.rgb(75, 96, 54));
        usageLineGraphSeries.setThickness(10);
        graphView.addSeries(usageLineGraphSeries);

        LineGraphSeries<DataPoint> usageProjectedGraphSeries = new LineGraphSeries<>(getProjectedData());
        usageProjectedGraphSeries.setTitle("Projected usage");
        usageProjectedGraphSeries.setColor(Color.rgb(179, 152, 97));
        usageProjectedGraphSeries.setDrawBackground(true);
        usageProjectedGraphSeries.setBackgroundColor(Color.argb(100, 179, 152, 97));

        graphView.addSeries(usageProjectedGraphSeries);

        graphView.setTitle("Average recent usage in kWh");
        graphView.setTitleTextSize(60);
        graphView.getLegendRenderer().setVisible(true);
        graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"Fri", "Sat", "Sun", "Mon", "Tue", "Wed", "Thur"});
        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
    }

    private DataPoint[] getUsageData() {
        DataPoint[] dataPoints = new DataPoint[]{
            new DataPoint(0, estimateArray[0]),
            new DataPoint(1, estimateArray[1]),
            new DataPoint(2, estimateArray[2]),
            new DataPoint(3, estimateArray[3]),
            new DataPoint(4, estimateArray[4]),
            new DataPoint(5, estimateArray[5]),
            new DataPoint(6, estimateArray[6]),
        };

        return dataPoints;
    }

    private DataPoint[] getProjectedData() {
        DataPoint[] dataPoints = new DataPoint[] {
            new DataPoint(0, projectedArray[0]),
            new DataPoint(1, projectedArray[1]),
            new DataPoint(2, projectedArray[2]),
            new DataPoint(3, projectedArray[3]),
            new DataPoint(4, projectedArray[4]),
            new DataPoint(5, projectedArray[5]),
            new DataPoint(6, projectedArray[6]),
        };

        return dataPoints;
    }

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        int id = item.getItemId();

        if (id == R.id.drawer_usage) {
            Intent usageIntent = new Intent(this, UsageActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(usageIntent);
        }

        if (id == R.id.drawer_payments) {
            Intent paymentsIntent = new Intent(this, PaymentsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(paymentsIntent);
        }

        if (id == R.id.drawer_account) {
            Intent accountIntent = new Intent(this, AccountActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(accountIntent);
        }

        if (id == R.id.drawer_more) {
            Intent moreIntent = new Intent(this, MoreActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(moreIntent);
        }

        if (id == R.id.drawer_logout) {
            firebaseAuth.signOut();
            Intent signupIntent = new Intent(this, MainActivity.class);
            startActivity(signupIntent);
        }

        return false;
    }

    public void getUsageInfo() {
        UserHelper userHelper = new UserHelper(this);
        SQLiteDatabase db = userHelper.getReadableDatabase();
        Cursor cursor = userHelper.readUserInfo(db);

        while (cursor.moveToNext()) {
            liveUsage = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.LIVE_COST));
            targetUsage = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.TARGET_COST));
            projectedUsage = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.PROJECTED_COST));
            projectedGraphString = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.PROJECTED_GRAPH_DATA));
            estimateString = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.ESTIMATE_RECENT_USAGE));
        }

        try {
            projectedGraphData = new JSONArray(projectedGraphString);
            estimateUsage = new JSONArray(estimateString);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        float[] projected = new float[projectedGraphData.length()];

        for (int i = 0; i < projectedGraphData.length(); ++i) {
            projected[i] = projectedGraphData.optInt(i);
        }

        projectedArray = projected;

        float[] estimate = new float[estimateUsage.length()];

        for (int i = 0; i < estimateUsage.length(); ++i) {
            estimate[i] = estimateUsage.optInt(i);
        }

        estimateArray = estimate;

        usage.setText(String.format("$%s", liveUsage));
        Tusage.setText(String.format("$%s", targetUsage));
        Pusage.setText(String.format("$%s", projectedUsage));

        setupGraph();
    }
}
