package com.axelfriberg.bikebuddy;



import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class NotificationFragment extends Fragment implements View.OnClickListener {
    private Button b;
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle(R.string.navigation_notification);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.fragment_notification,container,false);
        b = (Button)v.findViewById(R.id.alarm_id);
        b.setOnClickListener(this);
        return v;
    }
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), AlarmActivity.class);
        startActivity(intent);
    }
    @Override
    public void onResume(){
        super.onResume();
        getActivity().setTitle(R.string.navigation_notification);
    }
}
