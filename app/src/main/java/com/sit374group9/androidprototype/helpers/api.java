package com.sit374group9.androidprototype.helpers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.sit374group9.androidprototype.AndroidPrototype;
import com.sit374group9.androidprototype.LoadingActivity;

public class api {

    /**
     * Forgot password method to send password reset email to be called in case the user forgets their login password
     * @param context : ForgotPasswordActivity activity context
     * @param email : Email of the user to send reset email
     */
    public static void forgotPassword(final Context context, String email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Password reset link has successfully been sent to your email", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(context, "Your email address is invalid, please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * Update password method to change current password of the user
     * @param context : PasswordActivity context
     * @param newPass : The updated password for the user
     */
    public static void updatePassword(final Context context, String newPass) {
        FirebaseAuth.getInstance().getCurrentUser().updatePassword(newPass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Your password has been changed successfully", Toast.LENGTH_LONG).show();
                        } else {
                            Log.e("UpdatePassword", "onComplete: Failed=" + task.getException().getMessage());
                            Toast.makeText(context, "There was a problem changing your password, please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * Initialize a value event listener on the complete Firebase DB and update values if changed in the Usage Fragment
     */
    public static void trackerEventListener() {
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("LoadingActivity", "" + dataSnapshot.getValue());
                LoadingActivity.handleUserData(dataSnapshot.getValue());
                broadcastmanager.sendBroadcast(AndroidPrototype.getAppContext(), "FETCHED_USER_DATA");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Update name of user in Firebase Real Time DB
     * @param userID : Firebase UID of the user
     * @param mobile : Name to be updated for the user
     */
    public static void updateMobile(String userID, String mobile) {
        FirebaseDatabase.getInstance().getReference().child("user").child("users").child(userID).child("mobile").setValue(mobile);
    }

    /**
     * Update email address in Firebase Real Time DB
     * @param context : Base context from AccountActivity
     * @param userID : Firebase UID of the user
     * @param email : Email to be updated for the user
     */
    public static void updateEmail(final Context context, final String userID, final String email) {
        FirebaseAuth.getInstance().getCurrentUser().updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Email changed successfully", Toast.LENGTH_SHORT).show();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("user").child("users").child(userID).child("email").setValue(email);
                    databaseReference.child("user").child("users").child(userID).child("providerData").child("0").child("email").setValue(email);
                    databaseReference.child("user").child("users").child(userID).child("providerData").child("1").child("uid").setValue(email);
                } else {
                    Toast.makeText(context, "There was an error updating your email, please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
