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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.sit374group9.androidprototype.datastore.UserContract;
import com.sit374group9.androidprototype.datastore.UserHelper;

public class AccountActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle mToggle;

    TextView nametxt;
    TextView acctnumb;
    TextView emailtxt;
    TextView mobiletxt;
    TextView addresstxt;

    String firstname;
    String lastname;
    String accountnumb;
    String email;
    String mobilephone;
    String address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        nametxt = (TextView)findViewById(R.id.username);
        acctnumb = (TextView)findViewById(R.id.accountnumb);
        addresstxt = (TextView)findViewById(R.id.address);
        emailtxt = (TextView)findViewById(R.id.email);
        mobiletxt = (TextView)findViewById(R.id.mobile);

        setup();
        getUsageInfo();
    }

    public void setup() {
        setTitle("Account");
        // Setup drawer menu
        DrawerLayout mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Fixes oreo no animation flash bug
        overridePendingTransition(R.anim.empty_animation, R.anim.empty_animation);
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

            firstname = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.FIRST_NAME));
            lastname = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.LAST_NAME));
            accountnumb = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.ID));
            address = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.ADDRESS));
            email = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.EMAIL));
            mobilephone = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.MOBILE));

        }

        nametxt.setText(String.format("Name: %s %s", firstname, lastname));
        acctnumb.setText(String.format("Account No: %s", accountnumb));
        addresstxt.setText(String.format("Address: %s", address));
        emailtxt.setText(String.format("Email: %s", email));
        mobiletxt.setText(String.format("Mobile: %s", mobilephone));
    }
}
