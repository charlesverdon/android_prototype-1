package com.sit374group9.androidprototype;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
                FirebaseAuth.getInstance().sendPasswordResetEmail(Forgetinput.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgetPassword.this, "Password reset Link has Successfully Send to your Email!", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(ForgetPassword.this, "Your Email Address is invaild, please try again!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });









            }
        });
    }
}
