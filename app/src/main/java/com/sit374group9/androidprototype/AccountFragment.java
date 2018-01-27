package com.sit374group9.androidprototype;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountFragment extends Fragment {

    private static final String TAG = "AccountFragment";

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private String userID;

    private EditText textFirstName;
    private EditText textEmail;
    private EditText textAddress;

    private LinearLayout loading;
    private LinearLayout mainContainer;
    private LinearLayout emptyContainer;

    private Button editName;
    private Button editEmail;
    private Button editAddress;

    private Button saveName;
    private Button saveEmail;
    private Button saveAddress;

    private Button changePassword;
    private Button logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstaceState) {
        super.onActivityCreated(savedInstaceState);

        mainContainer = (LinearLayout) getActivity().findViewById(R.id.container_account);
        emptyContainer = (LinearLayout) getActivity().findViewById(R.id.account_processing_warning);

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
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

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

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "" + dataSnapshot.getValue());
                showData(dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

    private void showData(Object object) {
        Gson gson = new Gson();
        String json = gson.toJson(object);

        String userFirstName = "";
        String userEmail = "";
        String userAddress = "";

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject userObject = jsonObject.getJSONObject("user");;
            JSONObject usersObject = userObject.getJSONObject("users");
            JSONObject userDetailsObject = usersObject.getJSONObject(userID);

            userFirstName = userDetailsObject.getString("firstName");
            userEmail = userDetailsObject.getString("email");
            userAddress = userDetailsObject.getString("address");

            textFirstName.setText(userFirstName);
            textEmail.setText(userEmail);
            textAddress.setText(userAddress);

            Log.d(TAG, userFirstName + " " + userEmail + " " + userAddress);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        loading.setVisibility(View.GONE);

        if (userFirstName.isEmpty() || userAddress.isEmpty() || userEmail.isEmpty()) {
            emptyContainer.setVisibility(View.VISIBLE);
        } else {
            mainContainer.setVisibility(View.VISIBLE);
        }
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

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("user").child("users").child(userID).child("firstName").setValue(name);
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

                        user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Email changed successfully", Toast.LENGTH_SHORT).show();
                                    databaseReference = FirebaseDatabase.getInstance().getReference();
                                    databaseReference.child("user").child("users").child(userID).child("email").setValue(email);
                                    databaseReference.child("user").child("users").child(userID).child("providerData").child("0").child("email").setValue(email);
                                    databaseReference.child("user").child("users").child(userID).child("providerData").child("1").child("uid").setValue(email);
                                    textEmail.setText(email);
                                } else {
                                    Toast.makeText(getActivity(), "There was an error updating your email, please try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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

    public void handleAddressSave(String address) {
        Toast.makeText(getActivity(), "Address changed successfully", Toast.LENGTH_SHORT).show();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("user").child("users").child(userID).child("address").setValue(address);
        textAddress.setText(address);

        editAddress.setVisibility(View.VISIBLE);
        saveAddress.setVisibility(View.GONE);
        textAddress.setEnabled(false);
    }
}
