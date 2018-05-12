package com.sit374group9.androidprototype;

import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;

public class MoreActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle mToggle;
    Button contactUsButton;
    Button feedbackButton;
    Button aboutUsButton;
    Button termsAndConditionsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        setup();
    }

    public void setup() {
        setTitle("More");
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

        contactUsButton = (Button) findViewById(R.id.button_contact_us);
        contactUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick();
            }
        });

        feedbackButton = (Button) findViewById(R.id.button_feedback);
        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick();
            }
        });

        aboutUsButton = (Button) findViewById(R.id.button_about_us);
        aboutUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick();
            }
        });

        termsAndConditionsButton = (Button) findViewById(R.id.button_termsconditions);
        termsAndConditionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick();
            }
        });
    }

    public void buttonClick() {
        Intent aboutUsIntent = new Intent(this, PlaceholderActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(aboutUsIntent);
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
}
