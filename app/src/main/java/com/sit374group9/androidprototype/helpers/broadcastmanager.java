package com.sit374group9.androidprototype.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.util.Map;

/**
 * Created by robcunning on 6/5/18.
 */

public class broadcastmanager {
    public static void register(Context ctx, BroadcastReceiver receiver, IntentFilter filter) {
        LocalBroadcastManager.getInstance(ctx).registerReceiver(receiver, filter);
    }

    public static void unregister(Context ctx, BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(ctx).unregisterReceiver(receiver);
    }

    public static void sendBroadcast(Context ctx, String action) {
        sendBroadcast(ctx, action, null);
    }

    public static void sendBroadcast(Context ctx, String action, Map<String, String> extras) {
        Intent intent = new Intent();
        intent.setAction(action);
        if(extras != null){
            for (Map.Entry<String, String> entry : extras.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                intent.putExtra(key, value);
            }
        }
        LocalBroadcastManager.getInstance(ctx).sendBroadcast(intent);
    }
}
