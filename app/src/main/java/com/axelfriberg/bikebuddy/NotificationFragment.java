package com.axelfriberg.bikebuddy;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class NotificationFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.navigation_notification);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_fragment_2,container,false);
    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().setTitle(R.string.navigation_notification);
    }
}
