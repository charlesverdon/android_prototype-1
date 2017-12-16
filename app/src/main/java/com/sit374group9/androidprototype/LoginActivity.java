package com.sit374group9.androidprototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by robcunning on 16/12/17.
 */

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;

    //layout vars
    EditText editUsername;
    EditText editPassword;

    TextView textErrorLogin;

    //class vars
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setup();
    }

    private void setup() {
        mAuth = FirebaseAuth.getInstance();

        editUsername = (EditText) findViewById(R.id.edit_username);
        editPassword = (EditText) findViewById(R.id.edit_password);

        textErrorLogin = (TextView) findViewById(R.id.error_login);
    }

    public void loginButtonOnClick(View view) {
        username = editUsername.getText().toString().trim();
        password = editPassword.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent loginIntent = new Intent(LoginActivity.this, AccountActivity.class);
                            startActivity(loginIntent);
                        } else {
                            textErrorLogin.setText(task.getException().getMessage());
                        }
                    }
                });
    }
}
