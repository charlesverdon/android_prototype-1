package com.sit374group9.androidprototype;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setup();
    }

    public void setup() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        TextView tv2 = (TextView) this.findViewById(R.id.textView2);
        String text = (String) "Terms and Conditions";
        SpannableString ss = new SpannableString(text);
        ss.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                Intent i = new Intent(MainActivity.this, PlaceholderActivity.class);
                startActivity(i);

            }
        }, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv2.setText(ss);
        tv2.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void goToLogin(View view) {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }
}

