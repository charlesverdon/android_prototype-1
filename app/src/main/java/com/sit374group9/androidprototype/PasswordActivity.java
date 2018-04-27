package com.sit374group9.androidprototype;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sit374group9.androidprototype.helpers.api;

public class PasswordActivity extends AppCompatActivity {

    private static final String TAG = "PasswordActivity";

    //layout vars
    EditText newPassword;
    EditText newPasswordConfirmed;

    Button updateButton;

    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        setup();
    }

    private void setup() {
        loading = (ProgressBar) findViewById(R.id.progress_bar_password);

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

        if (newPass.equals(newPassConf)) {
            api.updatePassword(PasswordActivity.this, newPass);
        } else {
            Toast.makeText(PasswordActivity.this, "Your new passwords did not match, please try again.", Toast.LENGTH_LONG).show();
            loading.setVisibility(View.GONE);
        }

    }
}
