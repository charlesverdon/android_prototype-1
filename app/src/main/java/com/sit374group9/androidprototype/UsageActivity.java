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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.sit374group9.androidprototype.datastore.UserContract;
import com.sit374group9.androidprototype.datastore.UserHelper;

import static com.sit374group9.androidprototype.LoadingActivity.liveCost;
import static com.sit374group9.androidprototype.LoadingActivity.projectedCost;

public class UsageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle mToggle;
    String liveUsage;
    String targetUsage;
    String ProjectedUsage;

    TextView usage;
    TextView Tusage;
    TextView Pusage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage);

        usage = (TextView) this.findViewById(R.id.usage);
        Tusage = (TextView) this.findViewById(R.id.targetusage);
        Pusage = (TextView) this.findViewById(R.id.projectedusage);

        setup();
        setupGraph();
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
    }

    public void setupGraph() {
        GraphView graphView = (GraphView) findViewById(R.id.graph_usage);

        LineGraphSeries<DataPoint> usageLineGraphSeries = new LineGraphSeries<>(getUsageData());
        usageLineGraphSeries.setTitle("Estimate recent usage");
        usageLineGraphSeries.setColor(Color.GREEN);
        graphView.addSeries(usageLineGraphSeries);

        LineGraphSeries<DataPoint> usageProjectedGraphSeries = new LineGraphSeries<>(getProjectedData());
        usageProjectedGraphSeries.setTitle("Projected usage");
        usageProjectedGraphSeries.setColor(Color.RED);
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
            new DataPoint(0, 22.7),
            new DataPoint(1, 30.5),
            new DataPoint(2, 18.9),
            new DataPoint(3, 25.4),
            new DataPoint(4, 23.3),
            new DataPoint(5, 19.1),
            new DataPoint(6, 20.8),
        };

        return dataPoints;
    }

    private DataPoint[] getProjectedData() {
        DataPoint[] dataPoints = new DataPoint[] {
            new DataPoint(0, 25.0),
            new DataPoint(1, 25.0),
            new DataPoint(2, 25.0),
            new DataPoint(3, 18.0),
            new DataPoint(4, 18.0),
            new DataPoint(5, 18.0),
            new DataPoint(6, 18.0),
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
            targetUsage = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.ESTIMATE_RECENT_USAGE));
            ProjectedUsage = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.PROJECTED_COST));


        }

        usage.setText("$"+liveUsage);
        Tusage.setText("$"+targetUsage);
        Pusage.setText("$"+ProjectedUsage);
    }



}
