package com.sit374group9.androidprototype;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.google.firebase.auth.FirebaseAuth;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.sit374group9.androidprototype.datastore.UserContract;
import com.sit374group9.androidprototype.datastore.UserHelper;

public class PaymentsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle mToggle;
    String fee;
    String due;
    String Dayissued;
    String Period;




    TextView usagefee;
    TextView duedate;
    TextView dateissued;
    TextView period;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        usagefee = (TextView)findViewById(R.id.usagepay);
        duedate = (TextView)findViewById(R.id.duedate);
        dateissued = (TextView)findViewById(R.id.dateissued);
        period = (TextView)findViewById(R.id.period);


        setup();
        setupGraph();
        getUsageInfo();
    }

    public void setup() {
        setTitle("Payments");
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
        GraphView graphView = (GraphView) findViewById(R.id.graph_payment);

        BarGraphSeries<DataPoint> barGraphSeries = new BarGraphSeries<>(getDataPoint());

        graphView.addSeries(barGraphSeries);
        graphView.setTitle("Past Payments");
        graphView.setTitleTextSize(75);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"Feb", "Mar", "Apr", "May"});
        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        barGraphSeries.setSpacing(5);

    }

    private DataPoint[] getDataPoint() {
        DataPoint[] dataPoints = new DataPoint[] {
                new DataPoint(0, 30),
                new DataPoint(1, 60),
                new DataPoint(2, 90),
                new DataPoint(3, 60),
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

    public void goToPayment(View view) {
        Intent makePaymentIntent = new Intent(this, MakePaymentActivity.class);
        startActivity(makePaymentIntent);
    }

    public void getUsageInfo() {
        UserHelper userHelper = new UserHelper(this);
        SQLiteDatabase db = userHelper.getReadableDatabase();
        Cursor cursor = userHelper.readUserInfo(db);

        while (cursor.moveToNext()) {
            fee = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.LIVE_COST));
            due = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.DUE_DATE));
            Dayissued = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.INVOICE_DATE_ISSUED));


        }

        usagefee.setText("$"+fee);
        duedate.setText("Due on: "+due);
        dateissued.setText(""+Dayissued);
        period.setText("Jan 3rd - Feb 2nd");
    }
}
