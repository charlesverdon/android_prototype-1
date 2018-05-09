package com.sit374group9.androidprototype;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sit374group9.androidprototype.helpers.api;

public class ForgotPasswordActivity extends AppCompatActivity {

    TextView forgotTextView;
    EditText forgotInput;
    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setTitle("Forgot Password");

        setContentView(R.layout.activity_forget_password);

        forgotInput = (EditText) findViewById(R.id.forgetemail);
        submitBtn = (Button)findViewById(R.id.button2);
        forgotTextView = (TextView)findViewById(R.id.forgotPasswordTextView);

        submitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String msg = forgotInput.getText().toString();
                if (msg.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter your email address", Toast.LENGTH_LONG).show();
                } else {
                    api.forgotPassword(ForgotPasswordActivity.this, msg);
                }
            }
        });
    }
}
