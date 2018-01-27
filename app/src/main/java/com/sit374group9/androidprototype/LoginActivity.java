package com.sit374group9.androidprototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    //layout vars
    EditText editUsername;
    EditText editPassword;

    TextView textErrorLogin;

    Button loginButton;

    ProgressBar loading;

    //class vars
    private FirebaseAuth firebaseAuth;

    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setup();
    }

    private void setup() {
        firebaseAuth = FirebaseAuth.getInstance();

        editUsername = (EditText) findViewById(R.id.edit_username);
        editPassword = (EditText) findViewById(R.id.edit_password);

        textErrorLogin = (TextView) findViewById(R.id.error_login);

        loading = (ProgressBar) findViewById(R.id.progress_bar_login);

        loginButton = (Button) findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButtonOnClick();
            }
        });
    }

    public void loginButtonOnClick() {
        loading.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.GONE);

        textErrorLogin.setText(R.string.empty_string);

        username = editUsername.getText().toString().trim();
        password = editPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            textErrorLogin.setText(R.string.error_empty_field_login_signup);
            loading.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        } else {
            firebaseAuth.signInWithEmailAndPassword(username, password) .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    loading.setVisibility(View.GONE);
                    loginButton.setVisibility(View.VISIBLE);
                    if (task.isSuccessful()) {
                        Intent loginIntent = new Intent(LoginActivity.this, CustomerActivity.class);
                        startActivity(loginIntent);
                    } else {
                        textErrorLogin.setText(task.getException().getMessage());
                    }
                }
            });
        }
    }
}
