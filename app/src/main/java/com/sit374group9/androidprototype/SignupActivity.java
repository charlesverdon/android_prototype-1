package com.sit374group9.androidprototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by robcunning on 16/12/17.
 */

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    private FirebaseAuth mAuth;

    //layout vars
    EditText editUsername;
    EditText editPassword;

    TextView textErrorSignup;

    //class vars
    String username;
    String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setup();
    }

    private void setup() {
        mAuth = FirebaseAuth.getInstance();

        editUsername = (EditText) findViewById(R.id.edit_username);
        editPassword = (EditText) findViewById(R.id.edit_password);

        textErrorSignup = (TextView) findViewById(R.id.error_signup);
    }

    public void signupButtonOnClick(View view) {
        username = editUsername.getText().toString().trim();
        password = editPassword.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Account successfully created, please login", Toast.LENGTH_LONG).show();
//                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent loginIntent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(loginIntent);
                        } else {
                            textErrorSignup.setText(task.getException().getMessage());
                        }
                    }
                });
    }
}
