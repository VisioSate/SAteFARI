package com.visiosate.test0909;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * class associated with activity_next : allows the user to choose between using the ps4 controller or the MYO armband (not yet implemented)
 * After 7 seconds, the ps4 controller is automatically chosen
 */

public class ControllerActivity extends MainActivity {


    private final static int REQUEST_ENABLE_BT = 1;
    final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_next);

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        /* countdown : after 7 seconds, the ps4 controller is selected */
        final CountDownTimer controllerTimer = new CountDownTimer(7000, 100) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {

                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }

                ImageButton chosen = (ImageButton) findViewById(R.id.imageView3);
                Animation anim = new AlphaAnimation(1.0f, 0.0f);
                anim.setDuration(1500);
                chosen.startAnimation(anim);

                if (mBluetoothAdapter.isEnabled()) {
                    Intent myIntent = new Intent(ControllerActivity.this, StreamActivity.class);
                    startActivity(myIntent);
                    finish();
                }
            }
        }.start();

        /* When MYO is selected  (currently does nothing except showing text on screen) */
        ImageView myButton = (ImageView) findViewById(R.id.imageView5);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"MYO isn't functional, please use the controller",LENGTH_SHORT).show();
                /*controllerTimer.cancel();
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }

                ImageView chosen = (ImageView) findViewById(R.id.imageView5);
                Animation anim = new AlphaAnimation(1.0f, 0.0f);
                anim.setDuration(1500);
                chosen.startAnimation(anim);

                if (mBluetoothAdapter.isEnabled()) {
                    Intent myIntent = new Intent(ControllerActivity.this, CamMYOActivity.class);
                    startActivity(myIntent);
                }*/
            }
        });

        /* When the ps4 controller is selected */
        ImageButton myButton2 = (ImageButton) findViewById(R.id.imageView3);
        myButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controllerTimer.cancel();
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }

                ImageButton chosen = (ImageButton) findViewById(R.id.imageView3);
                Animation anim = new AlphaAnimation(1.0f, 0.0f);
                anim.setDuration(1500);
                chosen.startAnimation(anim);

                if (mBluetoothAdapter.isEnabled()) {
                    Intent myIntent = new Intent(ControllerActivity.this, StreamActivity.class);
                    startActivity(myIntent);
                    finish();
                }
            }
        });

    }
}
