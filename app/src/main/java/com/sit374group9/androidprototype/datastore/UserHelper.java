package com.sit374group9.androidprototype.datastore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by robcunning on 5/5/18.
 */

public class UserHelper extends SQLiteOpenHelper {

    private static final String TAG = "UserHelper";

    private static final String DATABASE_NAME = "UserDatabase";
    private static final int DATABASE_VERSION = 1;

    private static String numberType = " number,";
    private static String textType = " text,";

    // SQL query to create table
    // TODO: see if we can write this cleaner
    private static final String CREATE_TABLE = "create table "  + UserContract.UserEntry.TABLE_NAME
                                                                + "("
                                                                + UserContract.UserEntry.ID + numberType
                                                                + UserContract.UserEntry.ADDRESS + textType
                                                                + UserContract.UserEntry.EMAIL + textType
                                                                + UserContract.UserEntry.FIRST_NAME + textType
                                                                + UserContract.UserEntry.LAST_NAME + textType
                                                                + UserContract.UserEntry.MOBILE + textType
                                                                + UserContract.UserEntry.ESTIMATE_RECENT_USAGE + textType
                                                                + UserContract.UserEntry.PROJECTED_GRAPH_DATA + textType
                                                                + UserContract.UserEntry.DUE_DATE + textType
                                                                + UserContract.UserEntry.INVOICE_DATE_ISSUED + textType
                                                                + UserContract.UserEntry.LIVE_COST + textType
                                                                + UserContract.UserEntry.PAST_PAYMENTS + textType
                                                                + UserContract.UserEntry.PROJECTED_COST + textType
                                                                + " text);";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + UserContract.UserEntry.TABLE_NAME;


    public UserHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "Database created");
    }

    // Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        Log.d(TAG, "Table created");
    }

    // Update existing table
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DROP_TABLE);
        Log.d(TAG, "Table dropped");
        onCreate(db);
    }


    public static void addUserInfo(int id, String address, String email, String firstName, String lastName, String mobile, String estimateRecentUsage, String projectedGraphData, String dueDate, String invoiceDateIssued, String liveCost, String pastPayments, String projectedCost, String targetCost, SQLiteDatabase db) {

        ContentValues values = new ContentValues();
        values.put(UserContract.UserEntry.ID, id);
        values.put(UserContract.UserEntry.ADDRESS, address);
        values.put(UserContract.UserEntry.EMAIL, email);
        values.put(UserContract.UserEntry.FIRST_NAME, firstName);
        values.put(UserContract.UserEntry.LAST_NAME, lastName);
        values.put(UserContract.UserEntry.MOBILE, mobile);
        values.put(UserContract.UserEntry.ESTIMATE_RECENT_USAGE, estimateRecentUsage);
        values.put(UserContract.UserEntry.PROJECTED_GRAPH_DATA, projectedGraphData);
        values.put(UserContract.UserEntry.DUE_DATE, dueDate);
        values.put(UserContract.UserEntry.INVOICE_DATE_ISSUED, invoiceDateIssued);
        values.put(UserContract.UserEntry.LIVE_COST, liveCost);
        values.put(UserContract.UserEntry.PAST_PAYMENTS, pastPayments);
        values.put(UserContract.UserEntry.PROJECTED_COST, projectedCost);
        values.put(UserContract.UserEntry.TARGET_COST, targetCost);

        db.insert(UserContract.UserEntry.TABLE_NAME, null, values);
        Log.d(TAG, "Row inserted");
    }

    public Cursor readUserInfo(SQLiteDatabase db) {
        String[] columnNames = {
            UserContract.UserEntry.ID,
            UserContract.UserEntry.ADDRESS,
            UserContract.UserEntry.EMAIL,
            UserContract.UserEntry.FIRST_NAME,
            UserContract.UserEntry.LAST_NAME,
            UserContract.UserEntry.MOBILE,
            UserContract.UserEntry.ESTIMATE_RECENT_USAGE,
            UserContract.UserEntry.PROJECTED_GRAPH_DATA,
            UserContract.UserEntry.LIVE_COST,
            UserContract.UserEntry.DUE_DATE,
            UserContract.UserEntry.INVOICE_DATE_ISSUED,
            UserContract.UserEntry.LIVE_COST,
            UserContract.UserEntry.PAST_PAYMENTS,
            UserContract.UserEntry.PROJECTED_COST,
            UserContract.UserEntry.TARGET_COST
        };

        Cursor cursor = db.query(UserContract.UserEntry.TABLE_NAME, columnNames, null, null, null, null, null);

        return cursor;
    }
}
