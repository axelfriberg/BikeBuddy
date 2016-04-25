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
 * A simple {@link Fragment} subclass.
 */
public class LockFragment extends Fragment implements View.OnClickListener {
    private Button b;
    private TextView locked;
    private ImageView lockImage;

    public LockFragment() {

        // Required empty public constructor
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.navigation_item_lock);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lock, container, false);
        b = (Button)v.findViewById(R.id.button_lock);
        locked = (TextView)v.findViewById(R.id.locked_text);
        lockImage = (ImageView)v.findViewById(R.id.lock_image);
        b.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        if (b.getText().equals("Unlock")) {
            b.setText("Lock");
            locked.setText("Your bike is unlocked");
            lockImage.setImageResource(R.drawable.unlock_lock);
        }else{
            b.setText("Unlock");
            locked.setText("Your bike is locked");
            lockImage.setImageResource(R.drawable.lock_lock);
        }

    }
    public void onResume(){
        super.onResume();
        getActivity().setTitle(R.string.navigation_item_lock);
    }
}


