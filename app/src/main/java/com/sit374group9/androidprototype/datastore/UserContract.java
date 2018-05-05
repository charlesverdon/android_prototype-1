package com.sit374group9.androidprototype.datastore;

/**
 * Created by robcunning on 5/5/18.
 */

public final class UserContract {
    // Avoid accidental initialisation
    private UserContract() {

    }

    public static class UserEntry {
        public static String TABLE_NAME = "user_info";

        // User info columns
        public static String ID = "id";
        public static String FIRST_NAME = "first_name";
        public static String LAST_NAME = "last_name";
        public static String EMAIL = "email";
        public static String ADDRESS = "address";

        // Usage columns
        public static String LIVE_USAGE = "live_usage";
        public static String MONTHLY_USAGE = "monthly_usage";
        public static String LAST_MONTH_USAGE = "last_month_usage";

        // Cost columns
        public static String LIVE_COST = "live_cost";
        public static String MONTHLY_COST = "monthly_cost";
        public static String LAST_MONTH_COST = "last_month_cost";
    }
}
