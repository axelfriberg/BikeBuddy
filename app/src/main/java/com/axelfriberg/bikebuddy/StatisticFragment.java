package com.axelfriberg.bikebuddy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class StatisticFragment extends Fragment {


    public StatisticFragment() {

    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.navigation_Statistics);
    }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_statistic, container, false);


return v;
    }
    public void onResume(){
        super.onResume();
        getActivity().setTitle(R.string.navigation_Statistics);
    }
    public void onClick(View v) {
    }
}