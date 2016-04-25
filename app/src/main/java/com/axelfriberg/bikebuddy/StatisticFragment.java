package com.axelfriberg.bikebuddy;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class StatisticFragment extends Fragment{
    private TextView text;
    private ListView list;

    public StatisticFragment() {

        // Required empty public constructor

    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.navigation_Statistics);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_statistic, container, false);
        text = (TextView)v.findViewById(R.id.sf_text);
        list = (ListView)getActivity().findViewById(R.id.statistics_list_View);
        return v;
    }
    public void onResume(){
        super.onResume();
        getActivity().setTitle(R.string.navigation_Statistics);
    }


    public void onClick(View v) {
    }
}