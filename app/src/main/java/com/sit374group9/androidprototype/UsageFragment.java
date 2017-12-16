package com.sit374group9.androidprototype;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by robcunning on 16/12/17.
 */

public class UsageFragment extends Fragment {

    private static final String TAG = "UserFragment";

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        return layoutInflater.inflate((R.layout.fragment_usage), container, false);
    }
}
