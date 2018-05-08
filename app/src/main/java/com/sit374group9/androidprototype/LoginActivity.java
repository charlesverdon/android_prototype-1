package com.sit374group9.androidprototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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

        TextView tv2 = (TextView) this.findViewById(R.id.textView2);
        String text = (String) "Forget your Password? Click Here";
        SpannableString ss=new SpannableString(text);
        ss.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                Intent i=new Intent(LoginActivity.this,ForgetPassword.class);
                startActivity(i);

            }
        }, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv2.setText(ss);
        tv2.setMovementMethod(LinkMovementMethod.getInstance());

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
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
                        Intent loginIntent = new Intent(LoginActivity.this, LoadingActivity.class);
                        startActivity(loginIntent);
                    } else {
                        textErrorLogin.setText(task.getException().getMessage());
                    }
                }
            });
        }
    }
}
