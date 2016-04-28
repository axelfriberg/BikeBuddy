package com.axelfriberg.bikebuddy;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Scans and NFC tag.
 */
public class NFCFragment extends Fragment implements View.OnClickListener{

    private Button button;
    private TextView text;
    private ImageView image;
    private boolean isYourBike = true;

    public NFCFragment() {

        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.nfc_fragment_title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_nfc, container, false);
        button = (Button)v.findViewById(R.id.scan);
        text = (TextView) v.findViewById(R.id.scan_text);
        image = (ImageView)v.findViewById(R.id.scan_image);
        button.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        if (isYourBike) {
            button.setText("Scan again");
            text.setText("This is your bike!");
            image.setImageResource(R.drawable.check);
            isYourBike = false;
        } else {
            button.setText("Scan again");
            text.setText("This is not your bike!");
            image.setImageResource(R.drawable.cross);
            isYourBike = true;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        getActivity().setTitle(R.string.nfc_fragment_title);
    }

}
