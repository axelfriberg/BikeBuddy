package com.axelfriberg.bikebuddy;



import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class LockFragment extends Fragment implements View.OnClickListener, SensorEventListener {
    private Button b;
    private TextView locked;
    private ImageView lockImage;
    Sensor accelerometer;
    SensorManager sm;
    boolean yes;
    EditText password;

    public LockFragment() {

        // Required empty public constructor
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.navigation_item_lock);
        sm = (SensorManager)getActivity().getSystemService(getActivity().SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_lock, container, false);
        b = (Button)v.findViewById(R.id.button_lock);
        locked = (TextView)v.findViewById(R.id.locked_text);
        lockImage = (ImageView)v.findViewById(R.id.lock_image);
        b.setOnClickListener(this);
        password = (EditText)getActivity().findViewById(R.id.friendPassword);


        return v;
    }

    @Override
    public void onClick(View view) {
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        float z = event.values[2];

            if(z < -8 && yes == false){
                if (b.getText().equals("Unlock")) {
                    b.setText("Lock");
                    locked.setText("Your bike is unlocked");
                    lockImage.setImageResource(R.drawable.unlock_lock);

                }else{
                    b.setText("Unlock");
                    locked.setText("Your bike is locked");
                    lockImage.setImageResource(R.drawable.lock_lock);
                }

                yes = true;
            }
            if(z > 8 && yes == true){
                yes = false;
            }
        }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}


