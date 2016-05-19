package com.axelfriberg.bikebuddy;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;

public class AlarmActivity extends AppCompatActivity {
    private Vibrator v;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        
        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        mp = MediaPlayer.create(this,R.raw.car_alarm);

        long[] vibrator = new long[]{0,500,500,500,500,500,500,500,500,500,500};
        v.vibrate(vibrator,-1);

        mp.start();
    }

    @Override
    public void onPause(){
        super.onPause();
        mp.stop();
        v.cancel();
    }
}


