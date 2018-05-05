package com.sit374group9.androidprototype;

import android.app.Application;
import android.content.Context;

/**
 * Created by robcunning on 6/5/18.
 * Helper class to get context in static methods
 */

public class AndroidPrototype extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        AndroidPrototype.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return AndroidPrototype.context;
    }
}

