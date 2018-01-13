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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    //layout vars
    EditText editUsername;
    EditText editPassword;

    TextView textErrorSignup;

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
    }

    public void signupButtonOnClick(View view) {
        username = editUsername.getText().toString().trim();
        password = editPassword.getText().toString().trim();

        firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
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
