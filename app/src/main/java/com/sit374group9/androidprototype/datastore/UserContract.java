package com.sit374group9.androidprototype.datastore;

import org.json.JSONArray;

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
        public static String ADDRESS = "address";
        public static String EMAIL = "email";
        public static String FIRST_NAME = "first_name";
        public static String LAST_NAME = "last_name";
        public static String MOBILE = "mobile";

        // Usage columns
        public static String ESTIMATE_RECENT_USAGE = "estimate_recent_usage";
        public static String PROJECTED_GRAPH_DATA = "projected_graph_data";

        // Cost columns
        public static String DUE_DATE = "due_date";
        public static String INVOICE_DATE_ISSUED = "invoice_date_issued";
        public static String LIVE_COST = "live_cost";
        public static String PAST_PAYMENTS = "past_payments";
        public static String PROJECTED_COST = "projected_cost";
        public static String TARGET_COST = "target_cost";
    }
}
