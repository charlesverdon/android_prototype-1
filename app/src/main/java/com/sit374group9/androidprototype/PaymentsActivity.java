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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;

public class PaymentsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle mToggle;
    String dueDate;
    String dateIssued;
    String pastPayments;

    JSONArray pastPaymentsArray;
    int[] paymentsArray;

    TextView currentCostText;
    TextView dueDateText;
    TextView dateIssuedText;
    TextView periodText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        setup();
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

        currentCostText = (TextView)findViewById(R.id.usagepay);
        dueDateText = (TextView)findViewById(R.id.duedate);
        dateIssuedText = (TextView)findViewById(R.id.dateissued);
        periodText = (TextView)findViewById(R.id.period);
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
        return new DataPoint[]{
                new DataPoint(0, paymentsArray[0]),
                new DataPoint(1, paymentsArray[1]),
                new DataPoint(2, paymentsArray[2]),
                new DataPoint(3, paymentsArray[3]),
        };
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
            dueDate = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.DUE_DATE));
            dateIssued = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.INVOICE_DATE_ISSUED));
            pastPayments = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.PAST_PAYMENTS));

        }

        try {
            pastPaymentsArray = new JSONArray(pastPayments);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int[] payments = new int[pastPaymentsArray.length()];

        for (int i = 0; i < pastPaymentsArray.length(); ++i) {
            payments[i] = pastPaymentsArray.optInt(i);
        }

        paymentsArray = payments;

        currentCostText.setText(String.format("$%s.00", paymentsArray[3]));
        dueDateText.setText(String.format("Due on: %s", dueDate));
        dateIssuedText.setText(dateIssued);
        periodText.setText("Jan 3rd - Feb 2nd");

        setupGraph();
    }
}
