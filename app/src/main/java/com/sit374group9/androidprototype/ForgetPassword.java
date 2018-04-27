package com.sit374group9.androidprototype;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sit374group9.androidprototype.helpers.api;

public class ForgetPassword extends AppCompatActivity {


    EditText Forgetinput;
    Button Submitbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        Forgetinput = (EditText) findViewById(R.id.forgetemail);
        Submitbtn = (Button)findViewById(R.id.button2);

        Submitbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String mesg = Forgetinput.getText().toString();
                api.forgotPassword(ForgetPassword.this, mesg);
            }
        });
    }
}
