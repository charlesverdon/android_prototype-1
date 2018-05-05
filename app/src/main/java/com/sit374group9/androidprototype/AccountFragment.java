package com.sit374group9.androidprototype;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sit374group9.androidprototype.datastore.UserContract;
import com.sit374group9.androidprototype.datastore.UserHelper;
import com.sit374group9.androidprototype.helpers.api;
import com.sit374group9.androidprototype.helpers.broadcastmanager;

public class AccountFragment extends Fragment {

    private static final String TAG = "AccountFragment";

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authListener;

    private static String userID;

    private EditText textFirstName;
    //TODO: fix this
    private static EditText textEmail;
    private EditText textAddress;

    private LinearLayout loading;
    private LinearLayout mainContainer;

    private Button editName;
    private Button editEmail;
    private Button editAddress;

    private Button saveName;
    private Button saveEmail;
    private Button saveAddress;

    private Button changePassword;
    private Button logout;

    String firstName;
    String lastName;
    String email;
    String address;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstaceState) {
        super.onActivityCreated(savedInstaceState);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("WROTE_TO_DATABASE");

        broadcastmanager.register(getContext(), broadcastReceiver, intentFilter);

        mainContainer = (LinearLayout) getActivity().findViewById(R.id.container_account);

        textFirstName = (EditText) getActivity().findViewById(R.id.account_name_value);
        textEmail = (EditText) getActivity().findViewById(R.id.account_email_value);
        textAddress = (EditText) getActivity().findViewById(R.id.account_address_value);

        loading = (LinearLayout) getActivity().findViewById(R.id.progress_bar_account);

        editName = (Button) getActivity().findViewById(R.id.edit_name_btn);
        editAddress = (Button) getActivity().findViewById(R.id.edit_address_btn);
        editEmail = (Button) getActivity().findViewById(R.id.edit_email_btn);

        saveName = (Button) getActivity().findViewById(R.id.save_name_btn);
        saveEmail = (Button) getActivity().findViewById(R.id.save_email_btn);
        saveAddress = (Button) getActivity().findViewById(R.id.save_address_btn);

        changePassword = (Button) getActivity().findViewById(R.id.change_password_btn);
        logout = (Button) getActivity().findViewById(R.id.logout_btn);

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        userID = user.getUid();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "user is signed in " + user.getUid() + " " + user.getEmail());
                } else {
                    Log.d(TAG, "user is logged out");
                }
            }
        };

        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNameChange();
            }
        });

        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleEmailChange();
            }
        });

        editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAddressChange();
            }
        });

        saveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNameSave(textFirstName.getText().toString());
            }
        });

        saveEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleEmailSave(textEmail.getText().toString());
            }
        });

        saveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAddressSave(textAddress.getText().toString());
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PasswordActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            firebaseAuth.removeAuthStateListener(authListener);
        }
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            assert action != null;
            if (action.equals("WROTE_TO_DATABASE")) {
                getCustomerInfo();
            }
        }
    };

    public void handleNameChange() {
        editName.setVisibility(View.GONE);
        saveName.setVisibility(View.VISIBLE);
        textFirstName.setEnabled(true);
    }

    public void handleEmailChange() {
        editEmail.setVisibility(View.GONE);
        saveEmail.setVisibility(View.VISIBLE);
        textEmail.setEnabled(true);
    }

    public void handleAddressChange() {
        editAddress.setVisibility(View.GONE);
        saveAddress.setVisibility(View.VISIBLE);
        textAddress.setEnabled(true);
    }

    public void handleNameSave(String name) {
        Toast.makeText(getActivity(), "Name changed successfully", Toast.LENGTH_LONG).show();

        api.updateName(userID, name);

        textFirstName.setText(name);

        editName.setVisibility(View.VISIBLE);
        saveName.setVisibility(View.GONE);
        textFirstName.setEnabled(false);
    }

    public void handleEmailSave(final String email) {

        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Reminder")
                .setMessage("You must use your new email to sign in.")
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        assert user != null;

                        api.updateEmail(getContext(), userID, email);
                    }
                })
                .setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();

        editEmail.setVisibility(View.VISIBLE);
        saveEmail.setVisibility(View.GONE);
        textEmail.setEnabled(false);
    }

    public static void updateTextEmail(String email) {
        textEmail.setText(email);
    }

    public void handleAddressSave(String address) {
        Toast.makeText(getActivity(), "Address changed successfully", Toast.LENGTH_SHORT).show();

        api.updateAddress(userID, address);

        textAddress.setText(address);

        editAddress.setVisibility(View.VISIBLE);
        saveAddress.setVisibility(View.GONE);
        textAddress.setEnabled(false);
    }

    public void getCustomerInfo() {
        UserHelper userHelper = new UserHelper(getActivity());
        SQLiteDatabase db = userHelper.getReadableDatabase();

        Cursor cursor = userHelper.readUserInfo(db);

        while (cursor.moveToNext()) {
            firstName = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.FIRST_NAME));
            lastName = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.LAST_NAME));
            email = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.EMAIL));
            address = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.ADDRESS));
        }

        textFirstName.setText(firstName);
        textEmail.setText(email);
        textAddress.setText(address);

        // Hide loading container and show main container
        loading.setVisibility(View.GONE);
        mainContainer.setVisibility(View.VISIBLE);
    }
}
