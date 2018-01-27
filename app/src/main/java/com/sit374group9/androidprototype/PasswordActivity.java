package com.sit374group9.androidprototype;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PasswordActivity extends AppCompatActivity {

    private static final String TAG = "PasswordActivity";

    //layout vars
    EditText newPassword;
    EditText newPasswordConfirmed;

    Button updateButton;

    ProgressBar loading;

    //class vars
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        setup();
    }

    private void setup() {
        loading = (ProgressBar) findViewById(R.id.progress_bar_password);

        firebaseAuth = FirebaseAuth.getInstance();

        newPassword = (EditText) findViewById(R.id.new_password);
        newPasswordConfirmed = (EditText) findViewById(R.id.new_password_confirm);

        updateButton = (Button) findViewById(R.id.button_update);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePasswordChange(newPassword.getText().toString(), newPasswordConfirmed.getText().toString());
            }
        });
    }

    public void handlePasswordChange(String newPass, String newPassConf) {
        loading.setVisibility(View.VISIBLE);
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (newPass.equals(newPassConf)) {

            user.updatePassword(newPass)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(PasswordActivity.this, "Your password has been changed successfully", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Log.e(TAG, "onComplete: Failed=" + task.getException().getMessage());
                                Toast.makeText(PasswordActivity.this, "There was a problem changing your password, please try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    });


        } else {
            Toast.makeText(PasswordActivity.this, "Your new passwords did not match, please try again.", Toast.LENGTH_LONG).show();
            loading.setVisibility(View.GONE);
        }

    }
}
