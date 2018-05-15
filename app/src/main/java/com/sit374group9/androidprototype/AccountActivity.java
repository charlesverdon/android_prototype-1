package com.sit374group9.androidprototype;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sit374group9.androidprototype.datastore.UserContract;
import com.sit374group9.androidprototype.datastore.UserHelper;
import com.sit374group9.androidprototype.helpers.api;

public class AccountActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle mToggle;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authListener;

    private static String userID;

    Button saveEmailButton;
    Button saveMobileButton;

    Button editEmailButton;
    Button editMobileButton;

    EditText textEmail;
    EditText textMobile;

    TextView nametxt;
    TextView acctnumb;
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

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        userID = user.getUid();

        nametxt = (TextView) findViewById(R.id.username);
        acctnumb = (TextView) findViewById(R.id.accountnumb);
        addresstxt = (TextView) findViewById(R.id.address);

        textEmail = (EditText) findViewById(R.id.email);
        textMobile = (EditText) findViewById(R.id.mobile);

        editEmailButton = (Button) findViewById(R.id.edit_email);
        editMobileButton = (Button) findViewById(R.id.edit_mobile);

        saveEmailButton = (Button) findViewById(R.id.save_email);
        saveMobileButton = (Button) findViewById(R.id.save_mobile);

        editEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editEmail();
            }
        });

        editMobileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editMobile();
            }
        });

        saveEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEmail(textEmail.getText().toString());
            }
        });

        saveMobileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMobile(textMobile.getText().toString());
            }
        });

    }

    private void editEmail() {
        editEmailButton.setVisibility(View.GONE);
        saveEmailButton.setVisibility(View.VISIBLE);
        textEmail.setEnabled(true);
        textEmail.setText("");
        textEmail.requestFocus();
    }

    private void editMobile() {
        editMobileButton.setVisibility(View.GONE);
        saveMobileButton.setVisibility(View.VISIBLE);
        textMobile.setEnabled(true);
        textMobile.setText("");
        textMobile.requestFocus();
    }

    private void saveEmail(final String email) {

        if (email.isEmpty()) {
            final AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(this);

            builder.setTitle("Reminder")
                    .setMessage("You must use your new email to sign in.")
                    .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;

                            api.updateEmail(getApplicationContext(), userID, email);
                        }
                    })
                    .setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();

            editEmailButton.setVisibility(View.VISIBLE);
            saveEmailButton.setVisibility(View.GONE);
            textEmail.setEnabled(false);
        } else {
            Toast.makeText(this, "Please enter your email address", Toast.LENGTH_LONG).show();
        }

    }

    private void saveMobile(String mobile) {
        if (mobile.isEmpty()) {
            Toast.makeText(this, "Mobile changed successfully", Toast.LENGTH_LONG).show();

            api.updateMobile(userID, mobile);

            textMobile.setText(mobile);

            editMobileButton.setVisibility(View.VISIBLE);
            saveMobileButton.setVisibility(View.GONE);
            textMobile.setEnabled(false);
        } else {
            Toast.makeText(this, "Please enter your mobile number", Toast.LENGTH_LONG).show();
        }
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
        textEmail.setText(String.format("Email: %s", email));
        textMobile.setText(String.format("Mobile: %s", mobilephone));
    }

    public void goToChangePassword(View view) {
        Intent passwordIntent = new Intent(this, PasswordActivity.class);
        startActivity(passwordIntent);
    }
}
