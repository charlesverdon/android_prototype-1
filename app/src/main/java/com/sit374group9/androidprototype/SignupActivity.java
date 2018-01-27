package com.sit374group9.androidprototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    //layout vars
    EditText editUsername;
    EditText editPassword;

    TextView textErrorSignup;

    Button signupButton;

    ProgressBar loading;

    //class vars
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setup();
    }

    private void setup() {
        firebaseAuth = FirebaseAuth.getInstance();

        editUsername = (EditText) findViewById(R.id.edit_username);
        editPassword = (EditText) findViewById(R.id.edit_password);

        textErrorSignup = (TextView) findViewById(R.id.error_signup);

        loading = (ProgressBar) findViewById(R.id.progress_bar_signup);

        signupButton = (Button) findViewById(R.id.button_signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupButtonOnClick();
            }
        });
    }

    public void signupButtonOnClick() {
        loading.setVisibility(View.VISIBLE);
        signupButton.setVisibility(View.GONE);

        textErrorSignup.setText(R.string.empty_string);

        username = editUsername.getText().toString().trim();
        password = editPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            textErrorSignup.setText(R.string.error_empty_field_login_signup);
            loading.setVisibility(View.GONE);
            signupButton.setVisibility(View.VISIBLE);
        } else {
            firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    loading.setVisibility(View.GONE);
                    signupButton.setVisibility(View.VISIBLE);

                    if (task.isSuccessful()) {
                        Toast.makeText(SignupActivity.this, "Account successfully created, please login", Toast.LENGTH_LONG).show();

                        firebaseUser = firebaseAuth.getCurrentUser();
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference(USER_SERVICE);

                        databaseReference.child("users").child(firebaseUser.getUid()).setValue(firebaseUser);
                        Intent loginIntent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                    } else {
                        textErrorSignup.setText(task.getException().getMessage());
                    }
                }
            });
        }
    }
}
